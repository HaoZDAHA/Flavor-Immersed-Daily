package com.flavor_immersed_daily.client;

import com.flavor_immersed_daily.entity.SeatEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

// 座椅实体渲染器 - 什么都不渲染，因为座椅实体不可见。
public class SeatEntityRenderer extends EntityRenderer<SeatEntity> {

    public SeatEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(SeatEntity entity) {
        return null;
    }
}