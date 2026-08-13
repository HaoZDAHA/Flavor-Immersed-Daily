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
 * 冰箱·变温室配方 — 单物品输入，处理后输出
 */
public class FridgeTemperingRecipe implements Recipe<RecipeInput> {

    private final Ingredient ingredient;
    private final ItemStack result;
    private final int cookingTime;

    public FridgeTemperingRecipe(Ingredient ingredient, ItemStack result, int cookingTime) {
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
        return ModRecipes.FRIDGE_TEMPERING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FRIDGE_TEMPERING_TYPE.get();
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

    public static final MapCodec<FridgeTemperingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(FridgeTemperingRecipe::getIngredient),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(FridgeTemperingRecipe::getResult),
                    Codec.INT.optionalFieldOf("cookingTime", 200).forGetter(FridgeTemperingRecipe::getCookingTime)
            ).apply(instance, FridgeTemperingRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FridgeTemperingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, FridgeTemperingRecipe::getIngredient,
                    ItemStack.STREAM_CODEC, FridgeTemperingRecipe::getResult,
                    ByteBufCodecs.INT, FridgeTemperingRecipe::getCookingTime,
                    FridgeTemperingRecipe::new
            );
}
