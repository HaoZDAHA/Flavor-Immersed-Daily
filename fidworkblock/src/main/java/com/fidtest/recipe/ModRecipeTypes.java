package com.fidtest.recipe;

import com.fidtest.FidworkMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, FidworkMod.MODID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, FidworkMod.MODID);

    public static final RegistryObject<RecipeType<CookingRecipe>> COOKING =
            RECIPE_TYPES.register("cooking",
                    () -> RecipeType.simple(new ResourceLocation(FidworkMod.MODID, "cooking")));

    public static final RegistryObject<RecipeSerializer<CookingRecipe>> COOKING_SERIALIZER =
            RECIPE_SERIALIZERS.register("cooking", () -> CookingRecipe.Serializer.INSTANCE);
}
