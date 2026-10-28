package com.flavor_immersed_daily.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EggBreakingRecipe implements Recipe<CraftingContainer> {

    private final ResourceLocation id;
// 展平列表（shaped 时按行平铺、含空格占位）
    private final List<Ingredient> ingredients;
    private final List<ItemStack> results;
    private final int cookingTime;
    private final boolean shaped;
// shaped 时有效
    private final int patternWidth;
// shaped 时有效
    private final int patternHeight;

    public EggBreakingRecipe(ResourceLocation id, List<Ingredient> ingredients,
                             List<ItemStack> results, int cookingTime, boolean shaped,
                             int patternWidth, int patternHeight) {
        this.id = id;
        this.ingredients = ingredients;
        this.results = results;
        this.cookingTime = cookingTime;
        this.shaped = shaped;
        this.patternWidth = patternWidth;
        this.patternHeight = patternHeight;
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

    public int getPatternWidth() {
        return patternWidth;
    }

    public int getPatternHeight() {
        return patternHeight;
    }

    public List<Ingredient> getIngredientList() {
        return ingredients;
    }

    @Override
    public boolean matches(CraftingContainer input, Level level) {
        if (shaped) {
            return matchesShaped(input);
        }
        return matchesShapeless(input);
    }

    private boolean matchesShaped(CraftingContainer input) {
        for (int i = 0; i <= input.getWidth() - patternWidth; i++) {
            for (int j = 0; j <= input.getHeight() - patternHeight; j++) {
                if (matchesShaped(input, i, j, true)) return true;
                if (matchesShaped(input, i, j, false)) return true;
            }
        }
        return false;
    }

    private boolean matchesShaped(CraftingContainer input, int offsetX, int offsetY, boolean mirror) {
        for (int k = 0; k < input.getWidth(); k++) {
            for (int l = 0; l < input.getHeight(); l++) {
                int i1 = k - offsetX;
                int j1 = l - offsetY;
                Ingredient ing = Ingredient.EMPTY;
                if (i1 >= 0 && j1 >= 0 && i1 < patternWidth && j1 < patternHeight) {
                    if (mirror) {
                        ing = ingredients.get(patternWidth - i1 - 1 + j1 * patternWidth);
                    } else {
                        ing = ingredients.get(i1 + j1 * patternWidth);
                    }
                }
                if (!ing.test(input.getItem(k + l * input.getWidth()))) return false;
            }
        }
        return true;
    }

    private boolean matchesShapeless(CraftingContainer input) {
        List<ItemStack> inputs = new ArrayList<>();
        for (int i = 0; i < input.getContainerSize(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                inputs.add(stack);
            }
        }
        if (inputs.size() != ingredients.size()) return false;

        boolean[] used = new boolean[inputs.size()];
        for (Ingredient recipeIng : ingredients) {
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
    public ItemStack assemble(CraftingContainer input, RegistryAccess registries) {
        return results.isEmpty() ? ItemStack.EMPTY : results.get(0).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width == 3 && height == 3;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registries) {
        return results.isEmpty() ? ItemStack.EMPTY : results.get(0).copy();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
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

    public static class Serializer implements RecipeSerializer<EggBreakingRecipe> {
        @Override
        public EggBreakingRecipe fromJson(ResourceLocation id, JsonObject json) {
            boolean shaped = GsonHelper.getAsBoolean(json, "shaped", true);
            List<Ingredient> ingredients;
            int w = 0;
            int h = 0;
            if (shaped) {
                List<String> rows = new ArrayList<>();
                for (JsonElement e : GsonHelper.getAsJsonArray(json, "pattern")) {
                    rows.add(e.getAsString());
                }
                if (rows.isEmpty()) {
                    throw new JsonSyntaxException("No pattern for shaped egg_breaking recipe " + id);
                }
                Map<String, Ingredient> key = new LinkedHashMap<>();
                JsonObject keyJson = GsonHelper.getAsJsonObject(json, "key");
                for (Map.Entry<String, JsonElement> e : keyJson.entrySet()) {
                    key.put(e.getKey(), Ingredient.fromJson(e.getValue()));
                }
                w = rows.get(0).length();
                h = rows.size();
                NonNullList<Ingredient> flat = NonNullList.withSize(w * h, Ingredient.EMPTY);
                for (int row = 0; row < h; row++) {
                    String s = rows.get(row);
                    if (s.length() != w) {
                        throw new JsonSyntaxException("Pattern rows must be the same width for recipe " + id);
                    }
                    for (int col = 0; col < w; col++) {
                        char c = s.charAt(col);
                        Ingredient ing = c == ' ' ? Ingredient.EMPTY : key.get(String.valueOf(c));
                        if (ing == null) {
                            throw new JsonSyntaxException("Pattern references symbol '" + c + "' but no key defined in " + id);
                        }
                        flat.set(col + row * w, ing);
                    }
                }
                ingredients = flat;
            } else {
                ingredients = new ArrayList<>();
                for (JsonElement e : GsonHelper.getAsJsonArray(json, "ingredients")) {
                    ingredients.add(Ingredient.fromJson(e));
                }
            }

            List<ItemStack> results = new ArrayList<>();
            JsonArray resultsJson = GsonHelper.getAsJsonArray(json, "results");
            for (JsonElement e : resultsJson) {
                results.add(ShapedRecipe.itemStackFromJson(e.getAsJsonObject()));
            }
            if (results.isEmpty()) {
                throw new JsonSyntaxException("No results for egg_breaking recipe " + id);
            }

            int cookingTime = GsonHelper.getAsInt(json, "cookingTime", 100);
            return new EggBreakingRecipe(id, ingredients, results, cookingTime, shaped, w, h);
        }

        @Override
        public EggBreakingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            boolean shaped = buf.readBoolean();
            int ingredientCount = buf.readVarInt();
            List<Ingredient> ingredients = new ArrayList<>();
            for (int i = 0; i < ingredientCount; i++) {
                ingredients.add(Ingredient.fromNetwork(buf));
            }
            int w = shaped ? buf.readVarInt() : 0;
            int h = shaped ? buf.readVarInt() : 0;
            int resultCount = buf.readVarInt();
            List<ItemStack> results = new ArrayList<>();
            for (int i = 0; i < resultCount; i++) {
                results.add(buf.readItem());
            }
            int cookingTime = buf.readVarInt();
            return new EggBreakingRecipe(id, ingredients, results, cookingTime, shaped, w, h);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, EggBreakingRecipe recipe) {
            buf.writeBoolean(recipe.isShaped());
            buf.writeVarInt(recipe.getIngredientList().size());
            for (Ingredient ing : recipe.getIngredientList()) {
                ing.toNetwork(buf);
            }
            if (recipe.isShaped()) {
                buf.writeVarInt(recipe.getPatternWidth());
                buf.writeVarInt(recipe.getPatternHeight());
            }
            buf.writeVarInt(recipe.getResults().size());
            for (ItemStack s : recipe.getResults()) {
                buf.writeItem(s);
            }
            buf.writeVarInt(recipe.getCookingTime());
        }
    }
}
