package com.fidtest.client;

import com.fidtest.block.WokBlock;
import com.fidtest.block.entity.WokBlockEntity;
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

public class WokRenderer implements BlockEntityRenderer<WokBlockEntity> {

    private static final float BASE_Y = 2F / 16F, ITEM_SCALE = 0.28F;
    private static final float[][] POS = {{-0.08F,-0.04F},{0F,-0.04F},{0.08F,-0.04F},{-0.08F,0.04F},{0F,0.04F},{0.08F,0.04F}};
    private final ItemRenderer itemRenderer;

    public WokRenderer(BlockEntityRendererProvider.Context c) { itemRenderer = c.getItemRenderer(); }

    @Override
    public void render(WokBlockEntity be, float pt, PoseStack ps, MultiBufferSource buf, int light, int overlay) {
        List<ItemStack> items = be.getStoredItems(); if (items.isEmpty()) return;
        float yRot = 0;
        if (be.getLevel() != null) yRot = -be.getBlockState().getValue(WokBlock.FACING).toYRot();
        float toss = be.getTossTicks() > 0 ? (float) Math.sin((be.getTossTicks() - pt) / 12F * Math.PI) * 0.3F : 0;
        ps.pushPose(); ps.translate(0.5, BASE_Y + toss, 0.5); ps.mulPose(Axis.YP.rotationDegrees(yRot));
        for (int i = 0; i < items.size(); i++) {
            float[] p = POS[i % 6];
            ps.pushPose(); ps.translate(p[0], i * 0.015F, p[1]);
            if (be.getTossTicks() > 0) ps.mulPose(Axis.YP.rotationDegrees((be.getTossTicks() - pt) * 30F));
            else ps.mulPose(Axis.XP.rotationDegrees(90));
            ps.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            itemRenderer.renderStatic(items.get(i), ItemDisplayContext.FIXED, light, overlay, ps, buf, be.getLevel(), 0);
            ps.popPose();
        }
        ps.popPose();
    }
}
