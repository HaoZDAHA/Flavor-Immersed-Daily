package com.flavor_immersed_daily.client;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.block.WoodBasinBlock;
import com.flavor_immersed_daily.block.WoodBasinBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * 木盆渲染器 — 渲染chickenwithoutblood浮动 / 水果小堆
 */
public class WoodBasinBlockEntityRenderer implements BlockEntityRenderer<WoodBasinBlockEntity> {

    private static final ItemStack BLEDCHICKEN_STACK = new ItemStack(FlavorImmersedDaily.CHICKENWITHOUTBLOOD.get());

    public WoodBasinBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(WoodBasinBlockEntity entity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (entity.getBlockState().getValue(WoodBasinBlock.HAS_CHICKEN)) {
            renderChicken(entity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        }
        if (entity.getBlockState().getValue(WoodBasinBlock.HAS_FRUIT)) {
            renderFruitPile(entity, poseStack, bufferSource, packedLight, packedOverlay);
        }
    }

    private void renderChicken(WoodBasinBlockEntity entity, float partialTick, PoseStack poseStack,
                                MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        float ticks = entity.getTickCount() + partialTick;
        float floatOffset = (float) Math.sin(ticks * 0.08) * 0.06f;

        poseStack.pushPose();
        poseStack.translate(0.5, 0.25 + floatOffset, 0.5);
        poseStack.mulPose(Axis.XP.rotationDegrees(65));
        poseStack.scale(1.0f, 1.0f, 1.0f);

        Minecraft.getInstance().getItemRenderer().renderStatic(
                BLEDCHICKEN_STACK, ItemDisplayContext.GROUND, packedLight, packedOverlay,
                poseStack, bufferSource, entity.getLevel(), 0);
        poseStack.popPose();
    }

    private void renderFruitPile(WoodBasinBlockEntity entity, PoseStack poseStack,
                                  MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack fruitStack = entity.getFruitStack();
        if (fruitStack.isEmpty()) return;

        // 用blockPos做种子，每个位置的小堆外观固定
        long seed = entity.getBlockPos().asLong();
        RandomSource random = RandomSource.create(seed);

        // 渲染 9 个水果实例，躺平散落在盆内
        // 盆地 y≈4px=0.25，水果摊平在 y≈0.28~0.32
        for (int i = 0; i < 9; i++) {
            poseStack.pushPose();

            // 位置：散落在盆地内
            float x = (float) (0.25 + random.nextDouble() * 0.5);
            float z = (float) (0.25 + random.nextDouble() * 0.5);
            float y = (float) (0.26 + random.nextDouble() * 0.06);

            poseStack.translate(x, y, z);

            // 放平：绕X轴旋转约90度让物品横躺
            poseStack.mulPose(Axis.XP.rotationDegrees(80 + random.nextFloat() * 20));
            // 不同方向散开
            poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360));
            // 略微倾斜
            poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 30 - 15));

            // 大小不一
            float scale = 0.3f + random.nextFloat() * 0.3f;
            poseStack.scale(scale, scale, scale);

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    fruitStack, ItemDisplayContext.FIXED, packedLight, packedOverlay,
                    poseStack, bufferSource, entity.getLevel(), 0);
            poseStack.popPose();
        }
    }
}
