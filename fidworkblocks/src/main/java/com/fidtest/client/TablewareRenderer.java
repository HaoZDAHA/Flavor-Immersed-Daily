package com.fidtest.client;

import com.fidtest.block.entity.TablewareBlockEntity;
import com.fidtest.block.entity.TablewareBlockEntity.Kind;
import com.fidtest.block.entity.TablewareBlockEntity.Part;
import com.fidtest.item.DishItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import org.joml.Vector3f;

public class TablewareRenderer implements BlockEntityRenderer<TablewareBlockEntity> {
    public static final ResourceLocation PLATE = ResourceLocation.fromNamespaceAndPath("fidworkblock", "block/tableware_plate");
    public static final ResourceLocation LONG_PLATE = ResourceLocation.fromNamespaceAndPath("fidworkblock", "block/tableware_long_plate");
    public static final ResourceLocation SAUCER = ResourceLocation.fromNamespaceAndPath("fidworkblock", "block/tableware_small_plate");

    public static final ResourceLocation SAUCER_DRY_SEASONING = ResourceLocation.fromNamespaceAndPath("fidworkblock", "block/tableware_small_plate_dry_seasoning");
    public static final ResourceLocation SAUCER_WET_SEASONING = ResourceLocation.fromNamespaceAndPath("fidworkblock", "block/tableware_small_plate_wet_seasoning");
    private static final TagKey<Item> WET_SEASONING = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("fid", "wet_seasoning"));

    public TablewareRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(TablewareBlockEntity entity, float partialTick, PoseStack pose,
                       MultiBufferSource buffer, int light, int overlay) {
        var minecraft = Minecraft.getInstance();
        for (Part part : entity.getParts()) {
            ResourceLocation model = switch (part.kind) {
                case PLATE -> PLATE;
                case LONG_PLATE -> LONG_PLATE;
                case SAUCER -> SAUCER;
            };
            double centerX = part.kind == Kind.PLATE ? 8 : part.kind == Kind.LONG_PLATE ? 12.5 : 12;
            double centerZ = part.kind == Kind.SAUCER ? 4 : 8;
            pose.pushPose();
            pose.translate(part.x, part.layer * .125, part.z);
            pose.mulPose(Axis.YP.rotationDegrees(-part.facing.toYRot()));
            // Normalize the source model about its own center, without item display transforms.
            pose.translate(-centerX / 16, 0, -centerZ / 16);
            minecraft.getItemRenderer().renderModelLists(
                    minecraft.getModelManager().getModel(ModelResourceLocation.standalone(model)),
                    ItemStack.EMPTY, light, overlay, pose,
                    buffer.getBuffer(RenderType.entityCutoutNoCull(TextureAtlas.LOCATION_BLOCKS)));
            if (part.kind == Kind.SAUCER && !part.foods.isEmpty()) {
                ItemStack seasoning = part.foods.getFirst().stack;
                if (!seasoning.isEmpty()) renderSeasoning(seasoning, pose, buffer, light, overlay);
            }
            pose.popPose();
            if (part.kind == Kind.SAUCER) continue;

            double surface = part.layer * .125 + (part.kind == Kind.SAUCER ? .0625 : .125);
            for (int i = 0; i < part.foods.size(); i++) {
                var food = part.foods.get(i);
                if (food.stack.isEmpty()) continue;
                pose.pushPose();
                // Compress the existing slot coordinates too, so saved plates use the tighter layout.
                double x = part.kind == Kind.LONG_PLATE ? part.x + (food.x - part.x) * .65 : food.x;
                double z = part.kind == Kind.LONG_PLATE ? part.z + (food.z - part.z) * .65 : food.z;
                if (part.kind == Kind.LONG_PLATE) {
                    renderLongPlateFood(entity, part, food.stack, i, x, z, pose, buffer, light, overlay);
                    pose.popPose();
                    continue;
                }
                pose.translate(x, surface + .012 + (i % 8) * .003, z);
                if (food.stack.getItem() instanceof DishItem) {
                    float scale = part.kind == Kind.PLATE ? .55f : .24f;
                    pose.scale(scale, scale, scale);
                    pose.translate(-.5, 0, -.5);
                    // Render the complete procedural dish once, including its own serving plate.
                    DishItemRenderer.renderDish(food.stack, pose, buffer, light, overlay);
                } else {
                    float scale = part.kind == Kind.PLATE ? .30f : .18f;
                    pose.mulPose(Axis.YP.rotationDegrees(-part.facing.toYRot()));
                    pose.mulPose(Axis.XP.rotationDegrees(90));
                    pose.scale(scale, scale, scale);
                    minecraft.getItemRenderer().renderStatic(food.stack, ItemDisplayContext.FIXED,
                            light, overlay, pose, buffer, entity.getLevel(), i);
                }
                pose.popPose();
            }
        }
    }

    private static void renderSeasoning(ItemStack stack, PoseStack pose, MultiBufferSource buffer,
                                        int light, int overlay) {
        var minecraft = Minecraft.getInstance();
        boolean wet = stack.is(WET_SEASONING);
        ResourceLocation location = wet ? SAUCER_WET_SEASONING : SAUCER_DRY_SEASONING;
        BakedModel model = minecraft.getModelManager().getModel(ModelResourceLocation.standalone(location));
        int color = minecraft.getItemColors().getColor(stack, 100);
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;
        var water = wet ? minecraft.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                .apply(ResourceLocation.withDefaultNamespace("block/water_still")) : null;
        RandomSource random = RandomSource.create();
        Direction[] directions = Direction.values();
        for (int pass = 0; pass < (wet ? 2 : 1); pass++) {
            boolean waterPass = wet && pass == 0;
            boolean colorOverlay = wet && pass == 1;
            var consumer = buffer.getBuffer(colorOverlay
                    ? RenderType.entityTranslucent(TextureAtlas.LOCATION_BLOCKS)
                    : RenderType.entityCutoutNoCull(TextureAtlas.LOCATION_BLOCKS));
            pose.pushPose();
            if (colorOverlay) pose.translate(0, .0001, 0);
            for (int side = 0; side <= directions.length; side++) {
                random.setSeed(42L);
                for (var quad : model.getQuads(null, side == directions.length ? null : directions[side], random)) {
                    // The wet model has coincident top/bottom faces; no-cull renders the top from both sides.
                    if (wet && quad.getDirection() != Direction.UP) continue;
                    BakedQuad rendered = quad;
                    if (waterPass) {
                        var source = quad.getSprite();
                        // Preserve the model UVs while remapping a private copy into the animated water sprite.
                        int[] vertices = quad.getVertices().clone();
                        int stride = vertices.length / 4;
                        for (int i = 0; i < 4; i++) {
                            int offset = i * stride;
                            float u = (Float.intBitsToFloat(vertices[offset + 4]) - source.getU0()) / (source.getU1() - source.getU0());
                            float v = (Float.intBitsToFloat(vertices[offset + 5]) - source.getV0()) / (source.getV1() - source.getV0());
                            vertices[offset + 4] = Float.floatToRawIntBits(water.getU(u));
                            vertices[offset + 5] = Float.floatToRawIntBits(water.getV(v));
                        }
                        rendered = new BakedQuad(vertices, quad.getTintIndex(), quad.getDirection(), water,
                                quad.isShade(), quad.hasAmbientOcclusion());
                    }
                    consumer.putBulkData(pose.last(), rendered, waterPass ? 1 : red,
                            waterPass ? 1 : green, waterPass ? 1 : blue, colorOverlay ? .65f : 1, light, overlay);
                }
            }
            pose.popPose();
        }
    }

    private static void renderLongPlateFood(TablewareBlockEntity entity, Part part, ItemStack stack, int seed,
                                            double x, double z, PoseStack pose, MultiBufferSource buffer,
                                            int light, int overlay) {
        double support = part.layer * .125 + .0625 + .001;
        if (stack.getItem() instanceof DishItem) {
            // renderDish draws its own plate at y=0, without FIXED transforms or Y centering.
            pose.translate(x, support, z);
            pose.scale(.24f, .24f, .24f);
            pose.translate(-.5, 0, -.5);
            DishItemRenderer.renderDish(stack, pose, buffer, light, overlay);
            return;
        }

        var minecraft = Minecraft.getInstance();
        var renderer = minecraft.getItemRenderer();
        BakedModel model = renderer.getModel(stack, entity.getLevel(), null, seed);
        // ItemRenderer replaces these models before applying the FIXED display transform.
        if (stack.is(Items.TRIDENT) || stack.is(Items.SPYGLASS)) {
            String name = stack.is(Items.TRIDENT) ? "trident" : "spyglass";
            model = minecraft.getModelManager().getModel(
                    ModelResourceLocation.inventory(ResourceLocation.withDefaultNamespace(name)));
        }
        PoseStack measurement = new PoseStack();
        transformLongPlateFood(measurement, part);
        BakedModel displayedModel = model.applyTransform(ItemDisplayContext.FIXED, measurement, false);
        measurement.translate(-.5, -.5, -.5);
        float minY = Float.POSITIVE_INFINITY;
        if (!displayedModel.isCustomRenderer()) {
            boolean useDirectBuffer = !(stack.getItem() instanceof BlockItem blockItem)
                    || !(blockItem.getBlock() instanceof HalfTransparentBlock
                    || blockItem.getBlock() instanceof StainedGlassPaneBlock);
            for (BakedModel pass : displayedModel.getRenderPasses(stack, useDirectBuffer)) {
                if (!pass.getRenderTypes(stack, useDirectBuffer).isEmpty()) {
                    minY = Math.min(minY, minimumY(pass, measurement));
                }
            }
        }
        // Custom/empty models expose no measurable baked geometry: use the transformed
        // unit-cube bottom as a conservative fallback, rather than an index-based lift.
        if (!Float.isFinite(minY)) {
            Vector3f vertex = new Vector3f();
            for (int vx = 0; vx <= 1; vx++) for (int vy = 0; vy <= 1; vy++) for (int vz = 0; vz <= 1; vz++) {
                measurement.last().pose().transformPosition(vertex.set(vx, vy, vz));
                minY = Math.min(minY, vertex.y);
            }
        }
        pose.translate(x, support - minY, z);
        transformLongPlateFood(pose, part);
        // Only the measurement stack has FIXED/centering applied above. Render the
        // original override model so ItemRenderer applies them exactly once here.
        renderer.render(stack, ItemDisplayContext.FIXED, false, pose, buffer, light, overlay, model);
    }

    private static void transformLongPlateFood(PoseStack pose, Part part) {
        pose.mulPose(Axis.YP.rotationDegrees(-part.facing.toYRot()));
        pose.mulPose(Axis.XP.rotationDegrees(65));
        pose.scale(.29f, .29f, .29f * 1.8f);
    }

    private static float minimumY(BakedModel model, PoseStack pose) {
        float minY = Float.POSITIVE_INFINITY;
        RandomSource random = RandomSource.create();
        Vector3f vertex = new Vector3f();
        Direction[] directions = Direction.values();
        for (int side = 0; side <= directions.length; side++) {
            random.setSeed(42L);
            for (var quad : model.getQuads(null, side == directions.length ? null : directions[side], random)) {
                int[] vertices = quad.getVertices();
                int stride = vertices.length / 4;
                for (int i = 0; i < 4; i++) {
                    int offset = i * stride;
                    vertex.set(Float.intBitsToFloat(vertices[offset]), Float.intBitsToFloat(vertices[offset + 1]),
                            Float.intBitsToFloat(vertices[offset + 2]));
                    pose.last().pose().transformPosition(vertex);
                    minY = Math.min(minY, vertex.y);
                }
            }
        }
        return minY;
    }
}
