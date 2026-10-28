package com.fidtest.client.model;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.CompositeModel;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.neoforged.neoforge.client.model.geometry.UnbakedGeometryHelper;

import java.util.function.Function;

public class DishModelGeometry implements IUnbakedGeometry<DishModelGeometry> {

    public DishModelGeometry() {}

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker,
                           Function<Material, TextureAtlasSprite> spriteGetter,
                           ModelState modelState, ItemOverrides overrides) {
        TextureAtlasSprite baseSprite = spriteGetter.apply(context.getMaterial("layer0"));

        CompositeModel.Baked.Builder modelBuilder =
                CompositeModel.Baked.builder(context, baseSprite,
                        ItemOverrides.EMPTY, context.getTransforms());

        var renderTypeGroup = new net.neoforged.neoforge.client.RenderTypeGroup(
                net.minecraft.client.renderer.RenderType.cutout(),
                net.minecraft.client.renderer.RenderType.cutout());

        var unbaked = UnbakedGeometryHelper.createUnbakedItemElements(0, baseSprite);
        var baseQuads = UnbakedGeometryHelper.bakeElements(unbaked, mat -> baseSprite, modelState);
        modelBuilder.addQuads(renderTypeGroup, baseQuads);

        modelBuilder.setParticle(baseSprite);
        BakedModel composite = modelBuilder.build();

// 关键：必须让烘焙模型的 isCustomRenderer() 返回 true，BlockEntityWithoutLevelRenderer
// 才会被调用去渲染动态食材色块（CompositeModel.Baked 固定返回 false，BEWLR 永远不会触发）
        return new BakedModelWrapper<>(composite) {
            @Override
            public boolean isCustomRenderer() {
                return true;
            }
        };
    }
}
