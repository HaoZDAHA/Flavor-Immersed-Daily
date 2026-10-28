package com.fidtest.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

// 从食材物品图标的纹理中采样真实颜色，供工序菜渲染细碎色块使用。
// 采样结果按物品 ID 缓存，保证确定性。
public class DishColorHelper {
    private static final int MAX_COLORS_PER_ITEM = 6;
    private static final Map<String, int[]> CACHE = new HashMap<>();

    public static int[] getIngredientColors(String itemId) {
        return CACHE.computeIfAbsent(itemId, DishColorHelper::sampleFromTexture);
    }

    private static int[] sampleFromTexture(String itemId) {
        try {
            ResourceLocation itemRl = ResourceLocation.parse(itemId);
            ResourceLocation modelRl = itemRl.withPrefix("item/");
            Optional<Resource> modelRes = Minecraft.getInstance().getResourceManager().getResource(modelRl);
            if (modelRes.isEmpty()) return fallback(itemId);

            JsonObject model;
            try (InputStream in = modelRes.get().open()) {
                model = JsonParser.parseReader(new java.io.InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
            }
            if (!model.has("textures") || !model.getAsJsonObject("textures").has("layer0")) return fallback(itemId);
            String layer0 = model.getAsJsonObject("textures").get("layer0").getAsString();

            ResourceLocation texRl = ResourceLocation.parse(layer0);
            Optional<Resource> texRes = Minecraft.getInstance().getResourceManager().getResource(texRl);
            if (texRes.isEmpty()) return fallback(itemId);

            NativeImage img;
            try (InputStream in = texRes.get().open()) {
                img = NativeImage.read(in);
            }

// 颜色量化桶统计主色：确定性采样，且覆盖纹理的主要色调
// key -> {count, sumR, sumG, sumB}
            Map<Integer, long[]> buckets = new TreeMap<>();
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    int abgr = img.getPixelRGBA(x, y);
                    int a = (abgr >> 24) & 0xFF;
                    if (a < 200) continue;
                    int r = abgr & 0xFF;
                    int g = (abgr >> 8) & 0xFF;
                    int b = (abgr >> 16) & 0xFF;
// 跳过纯黑描边
                    if (Math.max(r, Math.max(g, b)) < 40) continue;
                    int key = (r >> 6) << 4 | (g >> 6) << 2 | (b >> 6);
                    long[] agg = buckets.computeIfAbsent(key, k -> new long[4]);
                    agg[0]++;
                    agg[1] += r;
                    agg[2] += g;
                    agg[3] += b;
                }
            }
            img.close();

            if (buckets.isEmpty()) return fallback(itemId);

            return buckets.entrySet().stream()
                    .sorted((x, y) -> {
                        int c = Long.compare(y.getValue()[0], x.getValue()[0]);
                        return c != 0 ? c : Integer.compare(x.getKey(), y.getKey());
                    })
                    .limit(MAX_COLORS_PER_ITEM)
                    .mapToInt(e -> {
                        long[] v = e.getValue();
                        int r = (int) (v[1] / v[0]);
                        int g = (int) (v[2] / v[0]);
                        int b = (int) (v[3] / v[0]);
                        return 0xFF000000 | (b << 16) | (g << 8) | r;
                    })
                    .toArray();
        } catch (Exception e) {
            return fallback(itemId);
        }
    }

    private static int[] fallback(String itemId) {
        int hash = itemId.hashCode();
        int r = ((hash >> 16) & 0xFF) | 0x40;
        int g = ((hash >> 8) & 0xFF) | 0x40;
        int b = (hash & 0xFF) | 0x40;
        r = Math.min(r, 220);
        g = Math.min(g, 220);
        b = Math.min(b, 220);
        return new int[]{0xFF000000 | (b << 16) | (g << 8) | r};
    }
}
