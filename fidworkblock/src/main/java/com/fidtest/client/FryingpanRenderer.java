package com.fidtest.client;

import com.fidtest.block.FryingpanBlock;
import com.fidtest.block.entity.FryingpanBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FryingpanRenderer implements BlockEntityRenderer<FryingpanBlockEntity> {

    private static final float BASE_Y = 2F / 16F, ITEM_SCALE = 0.3F, ITEM_Y_GAP = 0.01F;
    private static final float[][] ITEM_OFFSETS = {{0,0,0},{0.06F,0,-0.04F},{-0.04F,0,0.05F},{0.04F,0,0.04F},{-0.05F,0,-0.05F}};
    private static final float[] ITEM_TILT_X = {0,10F,-8F,6F,-10F}, ITEM_TILT_Z = {0,-6F,8F,-4F,7F}, ITEM_INIT_ROT = {0,72F,144F,216F,288F};
    private final ItemRenderer itemRenderer;

    public FryingpanRenderer(BlockEntityRendererProvider.Context c) { itemRenderer = c.getItemRenderer(); }

    @Override
    public void render(FryingpanBlockEntity be, float pt, PoseStack ps, MultiBufferSource buf, int light, int overlay) {
        float yRot = 0;
        if (be.getLevel() != null) yRot = -be.getBlockState().getValue(FryingpanBlock.FACING).toYRot();
        if (be.isWorking()) renderWorking(be, pt, ps, buf, light, overlay, yRot);
        else if (be.hasOutput()) {
            ItemStack o = be.getOutputItem(); if (!o.isEmpty()) renderSingle(o, ps, buf, light, overlay, yRot, be, BASE_Y + 0.02F);
        } else if (be.hasIngredient()) {
            ItemStack in = be.getIngredientItem(); if (!in.isEmpty()) { ItemStack s = in.copy(); s.setCount(1); renderSingle(s, ps, buf, light, overlay, yRot, be, BASE_Y); }
        }
    }

    private void renderWorking(FryingpanBlockEntity be, float pt, PoseStack ps, MultiBufferSource buf, int light, int overlay, float yRot) {
        List<ItemStack> items = be.getRenderInputItems();
        if (items.isEmpty()) return;
        float wobble = (float) Math.sin((be.getProcessTick() + pt) * 0.5F) * 3F;
        ps.pushPose(); ps.translate(0.5, BASE_Y, 0.5); ps.mulPose(Axis.YP.rotationDegrees(yRot));
        for (int i = 0; i < items.size(); i++) {
            float[] off = ITEM_OFFSETS[i % ITEM_OFFSETS.length];
            ps.pushPose(); ps.translate(off[0], i * (ITEM_SCALE * 0.3F + ITEM_Y_GAP), off[2]);
            ps.mulPose(Axis.YP.rotationDegrees(ITEM_INIT_ROT[i % ITEM_INIT_ROT.length] + wobble));
            ps.mulPose(Axis.XP.rotationDegrees(90 + ITEM_TILT_X[i % ITEM_TILT_X.length]));
            ps.mulPose(Axis.ZP.rotationDegrees(ITEM_TILT_Z[i % ITEM_TILT_Z.length]));
            ps.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            itemRenderer.renderStatic(items.get(i), ItemDisplayContext.FIXED, light, overlay, ps, buf, be.getLevel(), 0);
            ps.popPose();
        }
        ps.popPose();
    }

    private void renderSingle(ItemStack s, PoseStack ps, MultiBufferSource buf, int light, int overlay, float yRot, FryingpanBlockEntity be, float yOff) {
        ps.pushPose(); ps.translate(0.5, yOff, 0.5); ps.mulPose(Axis.YP.rotationDegrees(yRot)); ps.mulPose(Axis.XP.rotationDegrees(90)); ps.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        itemRenderer.renderStatic(s, ItemDisplayContext.FIXED, light, overlay, ps, buf, be.getLevel(), 0);
        ps.popPose();
    }

}
