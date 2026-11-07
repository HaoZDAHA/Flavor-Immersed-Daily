package com.fidtest.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import java.util.function.Function;

public class DishModelGeometry implements IUnbakedGeometry<DishModelGeometry> {
    public static final ResourceLocation PLATE = ResourceLocation.fromNamespaceAndPath("fidworkblock", "item/dish_plate");

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
        modelGetter.apply(PLATE).resolveParents(modelGetter);
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker,
                           Function<Material, TextureAtlasSprite> spriteGetter,
                           ModelState modelState, ItemOverrides overrides) {
        BakedModel plate = baker.bake(PLATE, modelState);
        return new BakedModelWrapper<>(plate) {
            @Override
            public boolean isCustomRenderer() { return true; }

            @Override
            public boolean usesBlockLight() { return false; }

            @Override
            public BakedModel applyTransform(ItemDisplayContext display, PoseStack pose, boolean leftHand) {
                if (display.firstPerson()) {
                    new net.minecraft.client.renderer.block.model.ItemTransform(
                            new org.joml.Vector3f(0, -90, 25), new org.joml.Vector3f(0.070625F, 0.2F, 0.070625F),
                            new org.joml.Vector3f(0.68F)).apply(leftHand, pose);
                } else if (display == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                        || display == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
                    new net.minecraft.client.renderer.block.model.ItemTransform(
                            new org.joml.Vector3f(), new org.joml.Vector3f(0, 0.1875F, 0.0625F),
                            new org.joml.Vector3f(0.55F)).apply(leftHand, pose);
                } else if (display != ItemDisplayContext.GUI) {
                    originalModel.applyTransform(display, pose, leftHand);
                }
                return this;
            }
        };
    }
}
