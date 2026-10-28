package com.fidtest.client;

import com.fidtest.block.MeatmincerBlock;
import com.fidtest.block.entity.MeatmincerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class MeatmincerRenderer implements BlockEntityRenderer<MeatmincerBlockEntity> {

    private static final float BASE_Y = 3F / 16F, ITEM_SCALE = 0.3F;
    private final ItemRenderer itemRenderer;

    public MeatmincerRenderer(BlockEntityRendererProvider.Context c) { itemRenderer = c.getItemRenderer(); }

    @Override
    public void render(MeatmincerBlockEntity be, float pt, PoseStack ps, MultiBufferSource buf, int light, int overlay) {
        float yRot = 0;
        if (be.getLevel() != null) yRot = -be.getBlockState().getValue(MeatmincerBlock.FACING).toYRot();
        if (be.isWorking()) renderWorking(be, pt, ps, buf, light, overlay, yRot);
        else if (be.hasOutput()) { ItemStack o = be.getOutputItem(); if (!o.isEmpty()) renderSingle(o, ps, buf, light, overlay, yRot, be); }
        else if (be.hasAnyInput()) {
            ItemStack in = be.getInputStack(); if (!in.isEmpty()) { ItemStack s = in.copy(); s.setCount(1); renderSingle(s, ps, buf, light, overlay, yRot, be); }
        }
    }

    private void renderWorking(MeatmincerBlockEntity be, float pt, PoseStack ps, MultiBufferSource buf, int light, int overlay, float yRot) {
        List<ItemStack> items = be.getRenderInputItems(); if (items.isEmpty()) return;
        float pulse = (float) Math.sin((be.getProcessTick() + pt) * 0.2F) * 3F;
        for (int i = 0; i < items.size(); i++) {
            ps.pushPose(); ps.translate(0.5, BASE_Y + i * 0.08F, 0.5); ps.mulPose(Axis.YP.rotationDegrees(yRot + pulse));
            ps.mulPose(Axis.XP.rotationDegrees(90)); ps.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            itemRenderer.renderStatic(items.get(i), ItemDisplayContext.FIXED, light, overlay, ps, buf, be.getLevel(), 0);
            ps.popPose();
        }
    }

    private void renderSingle(ItemStack s, PoseStack ps, MultiBufferSource buf, int light, int overlay, float yRot, MeatmincerBlockEntity be) {
        ps.pushPose(); ps.translate(0.5, BASE_Y + 0.02F, 0.5); ps.mulPose(Axis.YP.rotationDegrees(yRot)); ps.mulPose(Axis.XP.rotationDegrees(90)); ps.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        itemRenderer.renderStatic(s, ItemDisplayContext.FIXED, light, overlay, ps, buf, be.getLevel(), 0);
        ps.popPose();
    }
}
