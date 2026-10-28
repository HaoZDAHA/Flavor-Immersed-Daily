package com.fidtest.client;

import com.fidtest.block.MeatmincerBlock;
import com.fidtest.block.entity.MeatmincerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class MeatmincerRenderer implements BlockEntityRenderer<MeatmincerBlockEntity> {

    private static final float BASE_Y = 2.0F / 16.0F;
    private static final float ITEM_SCALE = 0.3F;

    private final ItemRenderer itemRenderer;

    public MeatmincerRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(MeatmincerBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        float yRot = 0;
        if (be.getLevel() != null) {
            Direction facing = be.getBlockState().getValue(MeatmincerBlock.FACING);
            yRot = -facing.toYRot();
        }

        if (be.isWorking()) {
            renderWorkingItems(be, partialTick, poseStack, bufferSource, packedLight, packedOverlay, yRot);
        } else if (be.hasOutput()) {
            ItemStack output = be.getOutputItem();
            if (!output.isEmpty()) {
                renderItem(output, poseStack, bufferSource, packedLight, packedOverlay, yRot, be);
            }
        } else if (be.hasAnyInput()) {
            ItemStack input = be.getInputStack();
            if (!input.isEmpty()) {
                ItemStack single = input.copy();
                single.setCount(1);
                renderItem(single, poseStack, bufferSource, packedLight, packedOverlay, yRot, be);
            }
        }
    }

    private void renderWorkingItems(MeatmincerBlockEntity be, float partialTick,
                                    PoseStack poseStack, MultiBufferSource bufferSource,
                                    int packedLight, int packedOverlay, float yRot) {
        List<ItemStack> items = be.getRenderInputItems();
        if (items.isEmpty()) return;

        float tick = be.getProcessTick() + partialTick;
        float pulse = (float) Math.sin(tick * 0.15F) * 2.0F;

        poseStack.pushPose();
        poseStack.translate(0.5, BASE_Y, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));

        for (int i = 0; i < items.size(); i++) {
            poseStack.pushPose();
            float layerY = i * (ITEM_SCALE * 0.3F + 0.01F);
            poseStack.translate(0, layerY, 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(pulse));
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            itemRenderer.renderStatic(items.get(i),
                    net.minecraft.world.item.ItemDisplayContext.FIXED,
                    packedLight, packedOverlay, poseStack, bufferSource, be.getLevel(), 0);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    private void renderItem(ItemStack stack, PoseStack poseStack,
                            MultiBufferSource bufferSource,
                            int packedLight, int packedOverlay, float yRot,
                            MeatmincerBlockEntity be) {
        poseStack.pushPose();
        poseStack.translate(0.5, BASE_Y + 0.02F, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        itemRenderer.renderStatic(stack,
                net.minecraft.world.item.ItemDisplayContext.FIXED,
                packedLight, packedOverlay, poseStack, bufferSource, be.getLevel(), 0);
        poseStack.popPose();
    }
}
