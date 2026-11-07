package com.fidtest.client;

import com.fidtest.block.entity.PlatedDishBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class PlatedDishRenderer implements BlockEntityRenderer<PlatedDishBlockEntity> {
    public PlatedDishRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(PlatedDishBlockEntity entity, float partialTick, PoseStack pose,
                       MultiBufferSource buffer, int light, int overlay) {
        DishItemRenderer.renderDish(entity.getDish(), pose, buffer, light, overlay);
    }
}
