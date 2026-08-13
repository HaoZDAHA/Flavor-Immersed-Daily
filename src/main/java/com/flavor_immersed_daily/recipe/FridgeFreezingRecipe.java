package com.flavor_immersed_daily.recipe;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

/**
 * 冰箱·冷冻室配方 — 单物品输入，处理后输出
 */
public class FridgeFreezingRecipe implements Recipe<RecipeInput> {

    private final Ingredient ingredient;
    private final ItemStack result;
    private final int cookingTime;

    public FridgeFreezingRecipe(Ingredient ingredient, ItemStack result, int cookingTime) {
        this.ingredient = ingredient;
        this.result = result;
        this.cookingTime = cookingTime;
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        if (input.size() < 1) return false;
        return this.ingredient.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FRIDGE_FREEZING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FRIDGE_FREEZING_TYPE.get();
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public ItemStack getResult() {
        return result;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    // ==================== 序列化 ====================

    public static final MapCodec<FridgeFreezingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(FridgeFreezingRecipe::getIngredient),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(FridgeFreezingRecipe::getResult),
                    Codec.INT.optionalFieldOf("cookingTime", 200).forGetter(FridgeFreezingRecipe::getCookingTime)
            ).apply(instance, FridgeFreezingRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FridgeFreezingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, FridgeFreezingRecipe::getIngredient,
                    ItemStack.STREAM_CODEC, FridgeFreezingRecipe::getResult,
                    ByteBufCodecs.INT, FridgeFreezingRecipe::getCookingTime,
                    FridgeFreezingRecipe::new
            );
}
