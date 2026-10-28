package com.fidtest.client;

import com.fidtest.block.SteamboxBlock;
import com.fidtest.block.entity.SteamboxBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SteamboxRenderer implements BlockEntityRenderer<SteamboxBlockEntity> {

    private static final float BASE_Y = 0.28F;
    private static final float ITEM_SCALE = 0.25F;
    private static final int COLS = 3;
    private static final float CELL_SIZE = 0.28F;

    private final ItemRenderer itemRenderer;

    public SteamboxRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(SteamboxBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (be.getBlockState().getValue(SteamboxBlock.PACKED)) return;
        if (be.getBlockState().getValue(SteamboxBlock.WORKING)) return;
        if (be.hasOutput()) return;

        List<ItemStack> items = be.getInputItems();
        if (items.isEmpty()) return;

        float yRot = 0;
        if (be.getLevel() != null) {
            Direction facing = be.getBlockState().getValue(SteamboxBlock.FACING);
            yRot = -facing.toYRot();
        }

        int rows = (int) Math.ceil((float) items.size() / COLS);

        poseStack.pushPose();
        poseStack.translate(0.5, BASE_Y, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);

        for (int i = 0; i < items.size(); i++) {
            int col = i % COLS;
            int row = i / COLS;
            float xOff = (col - (COLS - 1) * 0.5F) * CELL_SIZE;
            float yOff = (row - (rows - 1) * 0.5F) * CELL_SIZE;

            poseStack.pushPose();
            poseStack.translate(xOff, yOff, 0);
            itemRenderer.renderStatic(items.get(i),
                    net.minecraft.world.item.ItemDisplayContext.FIXED,
                    packedLight, packedOverlay, poseStack, bufferSource, be.getLevel(), 0);
            poseStack.popPose();
        }

        poseStack.popPose();
    }
}
