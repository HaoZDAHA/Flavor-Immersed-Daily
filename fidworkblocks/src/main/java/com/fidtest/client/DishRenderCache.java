package com.fidtest.client;

import com.fidtest.item.DishFragments;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.util.*;

/** Shared by inventory, held items and placed dishes; all access is on the render thread. */
public final class DishRenderCache {
    private static final int LIMIT = 128;
    private static final int ICON_SIZE = 16;
    private static final LinkedHashMap<CompoundTag, Entry> CACHE = new LinkedHashMap<>(16, .75f, true);
    private static NativeImage plateImage;

    private DishRenderCache() {}

    public static Entry get(ItemStack stack) {
        CompoundTag data = DishFragments.data(stack);
        CompoundTag key = new CompoundTag();
        key.put("Fragments", data.getList("Fragments", 10).copy());
        key.putLong("Seed", data.getLong("Seed"));
        key.putInt("Bites", data.getInt("Bites"));
        return get(key);
    }

    public static Entry getFragments(ListTag fragments) {
        CompoundTag key = new CompoundTag();
        key.put("Fragments", fragments.copy());
        return get(key);
    }

    private static Entry get(CompoundTag key) {
        Entry entry = CACHE.get(key);
        if (entry != null) return entry;
        entry = build(key.getList("Fragments", 10));
        CACHE.put(key, entry);
        if (CACHE.size() > LIMIT) {
            var iterator = CACHE.entrySet().iterator();
            iterator.next().getValue().close();
            iterator.remove();
        }
        return entry;
    }

    public static void clear() {
        CACHE.values().forEach(Entry::close);
        CACHE.clear();
        if (plateImage != null) { plateImage.close(); plateImage = null; }
    }

    static TextureAtlasSprite sprite(ItemStack ingredient) {
        var mc = Minecraft.getInstance();
        var model = mc.getItemRenderer().getModel(ingredient, mc.level, null, 0);
        var random = RandomSource.create(42);
        var quads = model.getQuads(null, null, random);
        if (!quads.isEmpty()) return quads.getFirst().getSprite();
        for (Direction direction : Direction.values()) {
            random.setSeed(42);
            quads = model.getQuads(null, direction, random);
            if (!quads.isEmpty()) return quads.getFirst().getSprite();
        }
        return model.getParticleIcon();
    }

    private static Entry build(ListTag tags) {
        List<Vertex> vertices = new ArrayList<>();
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < tags.size(); i++) {
            CompoundTag tag = tags.getCompound(i);
            ResourceLocation id = ResourceLocation.tryParse(tag.getString("Ingredient"));
            if (id == null) continue;
            ItemStack ingredient = new ItemStack(BuiltInRegistries.ITEM.get(id));
            if (ingredient.isEmpty()) continue;
            Piece piece = new Piece(tag, sprite(ingredient));
            pieces.add(piece);
        }
        pieces.sort(Comparator.comparingDouble(p -> p.y));
        for (Piece p : pieces) {
            for (int v = 0; v < 16; v++) {
                for (int u = 0; u < 16;) {
                    if (!p.has(u, v)) { u++; continue; }
                    int start = u++;
                    while (u < 16 && p.has(u, v)) u++;
                    p.quad(vertices, start, v, start, v + 1, u, v + 1, u, v);
                }
                for (int u = 0; u < 16; u++) {
                    if (!p.has(u, v)) continue;
                    if (!p.has(u, v - 1)) p.side(vertices, u, v, u + 1, v);
                    if (!p.has(u + 1, v)) p.side(vertices, u + 1, v, u + 1, v + 1);
                    if (!p.has(u, v + 1)) p.side(vertices, u + 1, v + 1, u, v + 1);
                    if (!p.has(u - 1, v)) p.side(vertices, u, v + 1, u, v);
                }
            }
        }
        return new Entry(vertices, pieces);
    }

    private static NativeImage plate() {
        if (plateImage == null) {
            ResourceLocation path = ResourceLocation.fromNamespaceAndPath("fidworkblock", "textures/item/dish_plate_item.png");
            try (var input = Minecraft.getInstance().getResourceManager().open(path)) {
                plateImage = NativeImage.read(input);
            } catch (IOException e) {
                throw new IllegalStateException("Cannot load dish plate icon", e);
            }
        }
        return plateImage;
    }

    public static final class Entry {
        private final List<Vertex> vertices;
        private final List<Piece> pieces;
        private ResourceLocation icon;

        private Entry(List<Vertex> vertices, List<Piece> pieces) {
            this.vertices = vertices;
            this.pieces = pieces;
        }

        public void render(PoseStack pose, VertexConsumer consumer, int light, int overlay) {
            for (Vertex v : vertices) {
                consumer.addVertex(pose.last(), v.x, v.y, v.z).setColor(239, 228, 216, 255)
                        .setUv(v.u, v.v).setOverlay(overlay).setLight(light)
                        .setNormal(pose.last(), v.nx, v.ny, v.nz);
            }
        }

        public ResourceLocation icon() {
            if (icon != null) return icon;
            NativeImage image = new NativeImage(ICON_SIZE, ICON_SIZE, false);
            NativeImage base = plate();
            for (int y = 0; y < ICON_SIZE; y++) for (int x = 0; x < ICON_SIZE; x++)
                image.setPixelRGBA(x, y, base.getPixelRGBA(x * base.getWidth() / ICON_SIZE, y * base.getHeight() / ICON_SIZE));
            List<List<Integer>> palettes = new ArrayList<>();
            Set<TextureAtlasSprite> sampled = new HashSet<>();
            for (Piece p : pieces) {
                if (!sampled.add(p.sprite)) continue;
                List<Integer> colors = new ArrayList<>();
                for (int y = 0; y < 16; y++) for (int x = 0; x < 16; x++) {
                    int color = p.sprite.getPixelRGBA(0, x * p.sprite.contents().width() / 16,
                            y * p.sprite.contents().height() / 16);
                    if ((color >>> 24) >= 128) colors.add(color | 0xFF000000);
                }
                if (!colors.isEmpty()) palettes.add(colors);
            }
            Random random = new Random(42);
            float remaining = Math.min(1F, pieces.size() / 40F);
            if (!palettes.isEmpty()) {
                for (int y = 4; y <= 11; y++) for (int x = 3; x <= 12; x++) {
                    float dx = (x - 7.5F) / 5F, dy = (y - 7.5F) / 4F;
                    float radius = dx * dx + dy * dy;
                    float edge = random.nextFloat() * 0.15F;
                    List<Integer> palette = palettes.get(random.nextInt(palettes.size()));
                    int color = palette.get(random.nextInt(palette.size()));
                    if (radius > remaining - edge) continue;
                    float shade = y <= 6 ? 1.05F : y >= 10 ? 0.82F : 0.95F;
                    int r = Math.min(255, (int)(((color & 255) * .9F + 70 * .1F) * shade));
                    int g = Math.min(255, (int)(((color >>> 8 & 255) * .9F + 40 * .1F) * shade));
                    int b = Math.min(255, (int)(((color >>> 16 & 255) * .9F + 20 * .1F) * shade));
                    image.setPixelRGBA(x, y, 0xFF000000 | b << 16 | g << 8 | r);
                }
            }
            DynamicTexture texture = new DynamicTexture(image);
            texture.setFilter(false, false);
            icon = Minecraft.getInstance().getTextureManager().register("fidworkblock_dish", texture);
            return icon;
        }

        private void close() {
            if (icon != null) Minecraft.getInstance().getTextureManager().release(icon);
        }
    }

    private record Vertex(float x, float y, float z, float u, float v, float nx, float ny, float nz) {}

    private static final class Piece {
        final TextureAtlasSprite sprite;
        final boolean[] mask = new boolean[256];
        final float x, y, z, scale, cos, sin, cu, cv;

        Piece(CompoundTag tag, TextureAtlasSprite sprite) {
            this.sprite = sprite;
            x = tag.getFloat("X"); z = tag.getFloat("Z");
            y = 0.08F + Math.max(0F, tag.getFloat("Y") - 0.08F) * 2.4F;
            scale = tag.getFloat("Scale");
            cos = (float)Math.cos(tag.getFloat("Angle")); sin = (float)Math.sin(tag.getFloat("Angle"));
            cu = (tag.getInt("MinU") + tag.getInt("MaxU")) * .5f;
            cv = (tag.getInt("MinV") + tag.getInt("MaxV")) * .5f;
            for (int pixel : tag.getIntArray("Pixels")) {
                if (pixel < 0 || pixel >= 256) continue;
                int color = sprite.getPixelRGBA(0, (pixel % 16) * sprite.contents().width() / 16,
                        (pixel / 16) * sprite.contents().height() / 16);
                mask[pixel] = (color >>> 24) != 0;
            }
        }

        boolean has(int u, int v) { return u >= 0 && u < 16 && v >= 0 && v < 16 && mask[v * 16 + u]; }

        void vertex(List<Vertex> out, float u, float v, float height, float nx, float ny, float nz) {
            float dx = (u - cu) * scale, dz = (v - cv) * scale;
            out.add(new Vertex(x + dx * cos - dz * sin, height + dx * sin * .22F + dz * cos * .18F, z + dx * sin + dz * cos,
                    sprite.getU(u / 16), sprite.getV(v / 16), nx * cos - nz * sin, ny, nx * sin + nz * cos));
        }

        void quad(List<Vertex> out, float u0, float v0, float u1, float v1,
                  float u2, float v2, float u3, float v3) {
            vertex(out, u0, v0, y, 0, 1, 0); vertex(out, u1, v1, y, 0, 1, 0);
            vertex(out, u2, v2, y, 0, 1, 0); vertex(out, u3, v3, y, 0, 1, 0);
        }

        void side(List<Vertex> out, float u0, float v0, float u1, float v1) {
            float nx = v1 - v0, nz = u0 - u1;
            vertex(out, u0, v0, y, nx, 0, nz); vertex(out, u1, v1, y, nx, 0, nz);
            vertex(out, u1, v1, y - .025f, nx, 0, nz); vertex(out, u0, v0, y - .025f, nx, 0, nz);
        }
    }
}
