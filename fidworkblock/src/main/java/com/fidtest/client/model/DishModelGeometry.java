package com.fidtest.client.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.CompositeModel;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import net.minecraftforge.client.model.geometry.UnbakedGeometryHelper;

import java.util.List;
import java.util.function.Function;

public class DishModelGeometry implements IUnbakedGeometry<DishModelGeometry> {

    public DishModelGeometry() {}

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker,
                           Function<Material, TextureAtlasSprite> spriteGetter,
                           ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        TextureAtlasSprite baseSprite = spriteGetter.apply(context.getMaterial("layer0"));

        CompositeModel.Baked.Builder modelBuilder =
                CompositeModel.Baked.builder(context, baseSprite, ItemOverrides.EMPTY, context.getTransforms());

        RenderTypeGroup renderTypeGroup = new RenderTypeGroup(RenderType.cutout(), RenderType.cutout());

        List<BlockElement> unbaked = UnbakedGeometryHelper.createUnbakedItemElements(0, baseSprite.contents());
        List<BakedQuad> baseQuads = UnbakedGeometryHelper.bakeElements(unbaked, mat -> baseSprite, modelState, modelLocation);
        modelBuilder.addQuads(renderTypeGroup, baseQuads);

        modelBuilder.setParticle(baseSprite);
        BakedModel composite = modelBuilder.build();

// 关键：必须让烘焙模型的 isCustomRenderer() 返回 true，BlockEntityWithoutLevelRenderer
// 才会被调用去渲染动态食材色块（CompositeModel.Baked 固定返回 false，BEWLR 永远不会触发）
        return new BakedModelWrapper<BakedModel>(composite) {
            @Override
            public boolean isCustomRenderer() {
                return true;
            }
        };
    }
}
