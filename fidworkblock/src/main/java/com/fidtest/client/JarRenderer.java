package com.fidtest.client;

import com.fidtest.block.JarBlock;
import com.fidtest.block.entity.JarBlockEntity;
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

public class JarRenderer implements BlockEntityRenderer<JarBlockEntity> {

    private static final float BASE_Y = 2F / 16F, ITEM_SCALE = 0.22F, ITEM_Y_GAP = 0.01F;
    private static final float[][] ITEM_OFFSETS = {{-0.06F,0,-0.06F},{0.06F,0,-0.06F},{-0.06F,0,0.06F},{0.06F,0,0.06F}};
    private static final float[] ITEM_TILT_X = {0,10F,-8F,6F}, ITEM_TILT_Z = {0,-6F,8F,-4F}, ITEM_INIT_ROT = {0,90F,180F,270F};
    private final ItemRenderer itemRenderer;

    public JarRenderer(BlockEntityRendererProvider.Context c) { itemRenderer = c.getItemRenderer(); }

    @Override
    public void render(JarBlockEntity be, float pt, PoseStack ps, MultiBufferSource buf, int light, int overlay) {
        float yRot = 0;
        if (be.getLevel() != null) yRot = -be.getBlockState().getValue(JarBlock.FACING).toYRot();
        if (be.isWorking()) renderWorking(be, pt, ps, buf, light, overlay, yRot);
        else if (be.hasOutput()) { ItemStack o = be.getOutputItem(); if (!o.isEmpty()) renderSingle(o, ps, buf, light, overlay, yRot, be); }
        else if (be.hasAnyInput()) {
            for (int i = 0; i < 4; i++) { ItemStack in = be.getInputStack(i); if (!in.isEmpty()) { ItemStack s = in.copy(); s.setCount(1); renderInput(s, i, ps, buf, light, overlay, yRot, be); } }
        }
    }

    private void renderWorking(JarBlockEntity be, float pt, PoseStack ps, MultiBufferSource buf, int light, int overlay, float yRot) {
        List<ItemStack> items = be.getRenderInputItems(); if (items.isEmpty()) return;
        float pulse = (float) Math.sin((be.getProcessTick() + pt) * 0.15F) * 2F;
        ps.pushPose(); ps.translate(0.5, BASE_Y, 0.5); ps.mulPose(Axis.YP.rotationDegrees(yRot));
        for (int i = 0; i < items.size(); i++) {
            float[] off = ITEM_OFFSETS[i % 4];
            ps.pushPose(); ps.translate(off[0], i * (ITEM_SCALE*0.3F+ITEM_Y_GAP), off[2]);
            ps.mulPose(Axis.YP.rotationDegrees(ITEM_INIT_ROT[i%4]+pulse));
            ps.mulPose(Axis.XP.rotationDegrees(90+ITEM_TILT_X[i%4])); ps.mulPose(Axis.ZP.rotationDegrees(ITEM_TILT_Z[i%4]));
            ps.scale(ITEM_SCALE,ITEM_SCALE,ITEM_SCALE);
            itemRenderer.renderStatic(items.get(i), ItemDisplayContext.FIXED, light, overlay, ps, buf, be.getLevel(), 0);
            ps.popPose();
        }
        ps.popPose();
    }

    private void renderInput(ItemStack s, int idx, PoseStack ps, MultiBufferSource buf, int light, int overlay, float yRot, JarBlockEntity be) {
        float[] off = ITEM_OFFSETS[idx % 4];
        ps.pushPose(); ps.translate(0.5+off[0], BASE_Y+0.02F, 0.5+off[2]); ps.mulPose(Axis.YP.rotationDegrees(yRot));
        ps.mulPose(Axis.XP.rotationDegrees(90+ITEM_TILT_X[idx%4])); ps.mulPose(Axis.ZP.rotationDegrees(ITEM_TILT_Z[idx%4]));
        ps.scale(ITEM_SCALE,ITEM_SCALE,ITEM_SCALE);
        itemRenderer.renderStatic(s, ItemDisplayContext.FIXED, light, overlay, ps, buf, be.getLevel(), 0);
        ps.popPose();
    }

    private void renderSingle(ItemStack s, PoseStack ps, MultiBufferSource buf, int light, int overlay, float yRot, JarBlockEntity be) {
        ps.pushPose(); ps.translate(0.5, BASE_Y+0.02F, 0.5); ps.mulPose(Axis.YP.rotationDegrees(yRot)); ps.mulPose(Axis.XP.rotationDegrees(90)); ps.scale(ITEM_SCALE,ITEM_SCALE,ITEM_SCALE);
        itemRenderer.renderStatic(s, ItemDisplayContext.FIXED, light, overlay, ps, buf, be.getLevel(), 0);
        ps.popPose();
    }
}
