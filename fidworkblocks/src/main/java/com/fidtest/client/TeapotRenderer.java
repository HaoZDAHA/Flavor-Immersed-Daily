package com.fidtest.client;

import com.fidtest.block.TeapotBlock;
import com.fidtest.block.entity.TeapotBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TeapotRenderer implements BlockEntityRenderer<TeapotBlockEntity> {

    private static final float BASE_Y = 2.0F / 16.0F;
    private static final float ITEM_SCALE = 0.22F;
    private static final float ITEM_Y_GAP = 0.01F;

    private static final float[][] ITEM_OFFSETS = {
            {-0.06F, 0.0F, -0.06F},
            {0.06F, 0.0F, -0.06F},
            {-0.06F, 0.0F, 0.06F},
            {0.06F, 0.0F, 0.06F},
    };

    private static final float[] ITEM_TILT_X = {0F, 10F, -8F, 6F};
    private static final float[] ITEM_TILT_Z = {0F, -6F, 8F, -4F};
    private static final float[] ITEM_INIT_ROT = {0F, 90F, 180F, 270F};

    private final ItemRenderer itemRenderer;

    public TeapotRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(TeapotBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        float yRot = 0;
        if (be.getLevel() != null) {
            Direction facing = be.getBlockState().getValue(TeapotBlock.FACING);
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
            for (int i = 0; i < 4; i++) {
                ItemStack input = be.getInputStack(i);
                if (!input.isEmpty()) {
                    ItemStack single = input.copy();
                    single.setCount(1);
                    renderInputItem(single, i, poseStack, bufferSource, packedLight, packedOverlay, yRot, be);
                }
            }
        }
    }

    private void renderWorkingItems(TeapotBlockEntity be, float partialTick,
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
            float[] offset = ITEM_OFFSETS[i % ITEM_OFFSETS.length];
            poseStack.pushPose();
            float layerY = i * (ITEM_SCALE * 0.3F + ITEM_Y_GAP);
            poseStack.translate(offset[0], layerY, offset[2]);
            poseStack.mulPose(Axis.YP.rotationDegrees(ITEM_INIT_ROT[i % ITEM_INIT_ROT.length] + pulse));
            poseStack.mulPose(Axis.XP.rotationDegrees(90 + ITEM_TILT_X[i % ITEM_TILT_X.length]));
            poseStack.mulPose(Axis.ZP.rotationDegrees(ITEM_TILT_Z[i % ITEM_TILT_Z.length]));
            poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            itemRenderer.renderStatic(items.get(i),
                    net.minecraft.world.item.ItemDisplayContext.FIXED,
                    packedLight, packedOverlay, poseStack, bufferSource, be.getLevel(), 0);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    private void renderInputItem(ItemStack stack, int slotIndex,
                                 PoseStack poseStack, MultiBufferSource bufferSource,
                                 int packedLight, int packedOverlay, float yRot,
                                 TeapotBlockEntity be) {
        float[] offset = ITEM_OFFSETS[slotIndex % ITEM_OFFSETS.length];
        poseStack.pushPose();
        poseStack.translate(0.5 + offset[0], BASE_Y + 0.02F, 0.5 + offset[2]);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(90 + ITEM_TILT_X[slotIndex % ITEM_TILT_X.length]));
        poseStack.mulPose(Axis.ZP.rotationDegrees(ITEM_TILT_Z[slotIndex % ITEM_TILT_Z.length]));
        poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        itemRenderer.renderStatic(stack,
                net.minecraft.world.item.ItemDisplayContext.FIXED,
                packedLight, packedOverlay, poseStack, bufferSource, be.getLevel(), 0);
        poseStack.popPose();
    }

    private void renderItem(ItemStack stack, PoseStack poseStack,
                            MultiBufferSource bufferSource,
                            int packedLight, int packedOverlay, float yRot,
                            TeapotBlockEntity be) {
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
