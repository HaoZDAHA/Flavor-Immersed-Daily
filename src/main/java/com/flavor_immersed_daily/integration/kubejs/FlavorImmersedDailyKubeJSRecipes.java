package com.flavor_immersed_daily.integration.kubejs;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.ListRecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

final class FlavorImmersedDailyKubeJSRecipes {
    private static final RecipeKey<Ingredient> INGREDIENT = IngredientComponent.INGREDIENT.inputKey("ingredient");
    private static final RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.outputKey("result");
    private static final RecipeKey<Integer> COOKING_TIME = NumberComponent.INT.otherKey("cookingTime").optional(200);

    static final RecipeSchema FRIDGE_TEMPERING = new RecipeSchema(RESULT, INGREDIENT, COOKING_TIME)
            .constructor(RESULT, INGREDIENT, COOKING_TIME);
    static final RecipeSchema FRIDGE_FREEZING = new RecipeSchema(RESULT, INGREDIENT, COOKING_TIME)
            .constructor(RESULT, INGREDIENT, COOKING_TIME);
    static final RecipeSchema WOOD_BASIN = new RecipeSchema(RESULT, INGREDIENT)
            .constructor(RESULT, INGREDIENT);

    private static final RecipeKey<java.util.List<Ingredient>> INGREDIENTS =
            ListRecipeComponent.create(IngredientComponent.INGREDIENT.instance(), false, false).inputKey("ingredients");
    private static final RecipeKey<java.util.List<ItemStack>> RESULTS =
            ListRecipeComponent.create(ItemStackComponent.ITEM_STACK.instance(), false, false).outputKey("results");
    private static final RecipeKey<Integer> EGG_BREAKING_TIME = NumberComponent.INT.otherKey("cookingTime").optional(100);
    private static final RecipeKey<Boolean> SHAPED = dev.latvian.mods.kubejs.recipe.component.BooleanComponent.BOOLEAN
            .otherKey("shaped").optional(false);

    static final RecipeSchema EGG_BREAKING = new RecipeSchema(RESULTS, INGREDIENTS, EGG_BREAKING_TIME, SHAPED)
            .constructor(RESULTS, INGREDIENTS, EGG_BREAKING_TIME);

    private FlavorImmersedDailyKubeJSRecipes() {
    }
}
