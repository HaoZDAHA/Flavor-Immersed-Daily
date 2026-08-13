package com.flavor_immersed_daily.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

/** Fruit-to-jam conversion performed by stomping fruit in a wood basin. */
public class WoodBasinRecipe implements Recipe<RecipeInput> {
    private final Ingredient ingredient;
    private final ItemStack result;

    public WoodBasinRecipe(Ingredient ingredient, ItemStack result) {
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        return input.size() >= 1 && ingredient.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.WOOD_BASIN_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.WOOD_BASIN_TYPE.get();
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public ItemStack getResult() {
        return result;
    }

    public static final MapCodec<WoodBasinRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(WoodBasinRecipe::getIngredient),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(WoodBasinRecipe::getResult)
            ).apply(instance, WoodBasinRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, WoodBasinRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, WoodBasinRecipe::getIngredient,
                    ItemStack.STREAM_CODEC, WoodBasinRecipe::getResult,
                    WoodBasinRecipe::new
            );

    public record Input(ItemStack item) implements RecipeInput {
        @Override
        public ItemStack getItem(int index) {
            return index == 0 ? item : ItemStack.EMPTY;
        }

        @Override
        public int size() {
            return 1;
        }
    }
}
