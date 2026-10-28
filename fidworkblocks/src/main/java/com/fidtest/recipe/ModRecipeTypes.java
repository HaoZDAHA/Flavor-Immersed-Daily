package com.fidtest.recipe;


import com.fidtest.ExampleMod;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, ExampleMod.MODID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ExampleMod.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<CookingRecipe>> COOKING =
            RECIPE_TYPES.register("cooking",
                    () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(ExampleMod.MODID, "cooking")));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CookingRecipe>> COOKING_SERIALIZER =
            RECIPE_SERIALIZERS.register("cooking",
                    () -> new RecipeSerializer<>() {
                        @Override
                        public MapCodec<CookingRecipe> codec() {
                            return CookingRecipe.CODEC;
                        }

                        @Override
                        public StreamCodec<RegistryFriendlyByteBuf, CookingRecipe> streamCodec() {
                            return CookingRecipe.STREAM_CODEC;
                        }
                    });

}
