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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SteamboxRenderer implements BlockEntityRenderer<SteamboxBlockEntity> {

    private static final float BASE_Y = 0.28F, ITEM_SCALE = 0.25F, CELL = 0.28F;
    private static final int COLS = 3;
    private final ItemRenderer itemRenderer;

    public SteamboxRenderer(BlockEntityRendererProvider.Context c) { itemRenderer = c.getItemRenderer(); }

    @Override
    public void render(SteamboxBlockEntity be, float pt, PoseStack ps, MultiBufferSource buf, int light, int overlay) {
        if (be.getBlockState().getValue(SteamboxBlock.PACKED) || be.getBlockState().getValue(SteamboxBlock.WORKING) || be.hasOutput()) return;
        List<ItemStack> items = be.getInputItems();
        if (items.isEmpty()) return;

        float yRot = 0;
        if (be.getLevel() != null) yRot = -be.getBlockState().getValue(SteamboxBlock.FACING).toYRot();

        int rows = (int) Math.ceil((float) items.size() / COLS);
        ps.pushPose(); ps.translate(0.5, BASE_Y, 0.5); ps.mulPose(Axis.YP.rotationDegrees(yRot)); ps.mulPose(Axis.XP.rotationDegrees(90)); ps.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        for (int i = 0; i < items.size(); i++) {
            float x = (i % COLS - (COLS - 1) * 0.5F) * CELL;
            float y = (i / COLS - (rows - 1) * 0.5F) * CELL;
            ps.pushPose(); ps.translate(x, y, 0);
            itemRenderer.renderStatic(items.get(i), ItemDisplayContext.FIXED, light, overlay, ps, buf, be.getLevel(), 0);
            ps.popPose();
        }
        ps.popPose();
    }
}
