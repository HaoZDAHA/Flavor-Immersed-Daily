package com.fidtest.client;

import com.fidtest.block.SqueezingmachineBlock;
import com.fidtest.block.entity.SqueezingmachineBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

import java.util.List;

public class SqueezingmachineRenderer implements BlockEntityRenderer<SqueezingmachineBlockEntity> {

    private static final float BASE_Y = 6.0F / 16.0F;
    private static final float ITEM_SCALE = 0.25F;
    private static final float ITEM_Y_GAP = 0.01F;

    private static final float[][] ITEM_OFFSETS = {
            {0.0F, 0.0F, 0.0F},
            {0.04F, 0.0F, -0.03F},
            {-0.03F, 0.0F, 0.04F},
            {0.03F, 0.0F, 0.03F},
            {-0.04F, 0.0F, -0.04F},
    };

    private static final float[] ITEM_TILT_X = {0F, 15F, -10F, 8F, -12F};
    private static final float[] ITEM_TILT_Z = {0F, -8F, 12F, -5F, 10F};
    private static final float[] ITEM_INIT_ROT = {0F, 72F, 144F, 216F, 288F};

    private final ItemRenderer itemRenderer;

    public SqueezingmachineRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(SqueezingmachineBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        float yRot = 0;
        if (be.getLevel() != null) {
            Direction facing = be.getBlockState().getValue(SqueezingmachineBlock.FACING);
            yRot = -facing.toYRot();
        }

        if (be.isWorking()) {
            renderWorkingItems(be, partialTick, poseStack, bufferSource, packedLight, packedOverlay, yRot);
        } else if (be.hasOutput()) {
            ItemStack output = be.getOutputItem();
            if (!output.isEmpty()) {
                renderItem(output, poseStack, bufferSource, packedLight, packedOverlay, yRot, be);
            }
        } else if (be.hasInput()) {
            ItemStack input = be.getInputItem();
            if (!input.isEmpty()) {
                ItemStack single = input.copy();
                single.setCount(1);
                renderItem(single, poseStack, bufferSource, packedLight, packedOverlay, yRot, be);
            }
        }
    }

    private void renderWorkingItems(SqueezingmachineBlockEntity be, float partialTick,
                                    PoseStack poseStack, MultiBufferSource bufferSource,
                                    int packedLight, int packedOverlay, float yRot) {
        List<ItemStack> items = be.getRenderInputItems();
        if (items.isEmpty()) return;

        float tick = be.getProcessTick() + partialTick;
        float spinAngle = tick * 36.0F;

        poseStack.pushPose();
        poseStack.translate(0.5, BASE_Y, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));

        for (int i = 0; i < items.size(); i++) {
            float[] offset = ITEM_OFFSETS[i % ITEM_OFFSETS.length];

            poseStack.pushPose();
            float layerY = i * (ITEM_SCALE * 0.6F + ITEM_Y_GAP);
            poseStack.translate(offset[0], layerY, offset[2]);
            poseStack.mulPose(Axis.YP.rotationDegrees(spinAngle + ITEM_INIT_ROT[i % ITEM_INIT_ROT.length]));
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
                            SqueezingmachineBlockEntity be) {
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
