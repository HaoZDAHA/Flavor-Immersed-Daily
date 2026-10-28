package com.flavor_immersed_daily.recipe;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModRecipes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, FlavorImmersedDaily.MODID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, FlavorImmersedDaily.MODID);

    public static final RegistryObject<RecipeType<EggBreakingRecipe>> EGG_BREAKING_TYPE =
            RECIPE_TYPES.register("egg_breaking", () -> new RecipeType<>() {
            });
    public static final RegistryObject<RecipeSerializer<EggBreakingRecipe>> EGG_BREAKING_SERIALIZER =
            RECIPE_SERIALIZERS.register("egg_breaking", EggBreakingRecipe.Serializer::new);

    public static final RegistryObject<RecipeType<FridgeTemperingRecipe>> FRIDGE_TEMPERING_TYPE =
            RECIPE_TYPES.register("fridge_tempering", () -> new RecipeType<>() {
            });
    public static final RegistryObject<RecipeSerializer<FridgeTemperingRecipe>> FRIDGE_TEMPERING_SERIALIZER =
            RECIPE_SERIALIZERS.register("fridge_tempering", FridgeTemperingRecipe.Serializer::new);

    public static final RegistryObject<RecipeType<FridgeFreezingRecipe>> FRIDGE_FREEZING_TYPE =
            RECIPE_TYPES.register("fridge_freezing", () -> new RecipeType<>() {
            });
    public static final RegistryObject<RecipeSerializer<FridgeFreezingRecipe>> FRIDGE_FREEZING_SERIALIZER =
            RECIPE_SERIALIZERS.register("fridge_freezing", FridgeFreezingRecipe.Serializer::new);

    public static final RegistryObject<RecipeType<WoodBasinRecipe>> WOOD_BASIN_TYPE =
            RECIPE_TYPES.register("wood_basin", () -> new RecipeType<>() {
            });
    public static final RegistryObject<RecipeSerializer<WoodBasinRecipe>> WOOD_BASIN_SERIALIZER =
            RECIPE_SERIALIZERS.register("wood_basin", WoodBasinRecipe.Serializer::new);

// 作物转换组配方（作物鉴定机跨模组转换）
    public static final RegistryObject<RecipeType<CropConversionRecipe>> CROP_CONVERSION_TYPE =
            RECIPE_TYPES.register("crop_conversion", () -> new RecipeType<>() {
            });
    public static final RegistryObject<RecipeSerializer<CropConversionRecipe>> CROP_CONVERSION_SERIALIZER =
            RECIPE_SERIALIZERS.register("crop_conversion", CropConversionRecipe.Serializer::new);

    private ModRecipes() {
    }

    public static void register(IEventBus modEventBus) {
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
    }
}
