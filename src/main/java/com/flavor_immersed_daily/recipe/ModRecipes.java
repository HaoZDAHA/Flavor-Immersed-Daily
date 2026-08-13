package com.flavor_immersed_daily.recipe;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, FlavorImmersedDaily.MODID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, FlavorImmersedDaily.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<EggBreakingRecipe>> EGG_BREAKING_TYPE =
            RECIPE_TYPES.register("egg_breaking", () -> new RecipeType<>() {
            });
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<EggBreakingRecipe>> EGG_BREAKING_SERIALIZER =
            RECIPE_SERIALIZERS.register("egg_breaking", () -> EggBreakingRecipeSerializer.INSTANCE);

    public static final DeferredHolder<RecipeType<?>, RecipeType<FridgeTemperingRecipe>> FRIDGE_TEMPERING_TYPE =
            RECIPE_TYPES.register("fridge_tempering", () -> new RecipeType<>() {
            });
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FridgeTemperingRecipe>> FRIDGE_TEMPERING_SERIALIZER =
            RECIPE_SERIALIZERS.register("fridge_tempering",
                    () -> serializer(FridgeTemperingRecipe.CODEC, FridgeTemperingRecipe.STREAM_CODEC));

    public static final DeferredHolder<RecipeType<?>, RecipeType<FridgeFreezingRecipe>> FRIDGE_FREEZING_TYPE =
            RECIPE_TYPES.register("fridge_freezing", () -> new RecipeType<>() {
            });
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FridgeFreezingRecipe>> FRIDGE_FREEZING_SERIALIZER =
            RECIPE_SERIALIZERS.register("fridge_freezing",
                    () -> serializer(FridgeFreezingRecipe.CODEC, FridgeFreezingRecipe.STREAM_CODEC));

    public static final DeferredHolder<RecipeType<?>, RecipeType<WoodBasinRecipe>> WOOD_BASIN_TYPE =
            RECIPE_TYPES.register("wood_basin", () -> new RecipeType<>() {
            });
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<WoodBasinRecipe>> WOOD_BASIN_SERIALIZER =
            RECIPE_SERIALIZERS.register("wood_basin",
                    () -> serializer(WoodBasinRecipe.CODEC, WoodBasinRecipe.STREAM_CODEC));

    private ModRecipes() {
    }

    public static void register(IEventBus modEventBus) {
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
    }

    private static <T extends Recipe<?>> RecipeSerializer<T> serializer(
            MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        return new RecipeSerializer<>() {
            @Override
            public MapCodec<T> codec() {
                return codec;
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodec;
            }
        };
    }
}
