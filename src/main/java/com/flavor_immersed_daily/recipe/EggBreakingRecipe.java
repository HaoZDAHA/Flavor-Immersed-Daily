package com.flavor_immersed_daily.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class EggBreakingRecipe implements Recipe<CraftingInput> {

    private final Optional<ShapedRecipePattern> pattern;
    private final Optional<List<Ingredient>> ingredients;
    private final List<ItemStack> results;
    private final int cookingTime;
    private final boolean shaped;

    public EggBreakingRecipe(Optional<ShapedRecipePattern> pattern, Optional<List<Ingredient>> ingredients,
                             List<ItemStack> results, int cookingTime, boolean shaped) {
        this.pattern = pattern;
        this.ingredients = ingredients;
        this.results = results;
        this.cookingTime = cookingTime;
        this.shaped = shaped;
    }

    public List<ItemStack> getResults() {
        return results;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public boolean isShaped() {
        return shaped;
    }

    public List<Ingredient> getIngredientList() {
        if (shaped && pattern.isPresent()) {
            return pattern.get().ingredients();
        }
        return ingredients.orElse(List.of());
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (shaped && pattern.isPresent()) {
            return pattern.get().matches(input);
        }
        if (!shaped && ingredients.isPresent()) {
            return matchesShapeless(input, ingredients.get());
        }
        return false;
    }

    private boolean matchesShapeless(CraftingInput input, List<Ingredient> recipeIngredients) {
        // 收集输入中的非空物品
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                inputs.add(stack);
            }
        }
        if (inputs.size() != recipeIngredients.size()) return false;

        // 简单匹配：每个 recipe ingredient 需要被一个 input item 满足
        boolean[] used = new boolean[inputs.size()];
        for (Ingredient recipeIng : recipeIngredients) {
            boolean found = false;
            for (int i = 0; i < inputs.size(); i++) {
                if (!used[i] && recipeIng.test(inputs.get(i))) {
                    used[i] = true;
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return results.isEmpty() ? ItemStack.EMPTY : results.get(0).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width == 3 && height == 3;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return results.isEmpty() ? ItemStack.EMPTY : results.get(0).copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.EGG_BREAKING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.EGG_BREAKING_TYPE.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.addAll(getIngredientList());
        return list;
    }
}
