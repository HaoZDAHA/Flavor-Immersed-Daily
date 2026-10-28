package com.flavor_immersed_daily.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

// 冰箱·冷冻室配方 — 单物品输入，处理后输出
public class FridgeFreezingRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final int cookingTime;

    public FridgeFreezingRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, int cookingTime) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
        this.cookingTime = cookingTime;
    }

    @Override
    public boolean matches(Container input, Level level) {
        if (input.getContainerSize() < 1) return false;
        return this.ingredient.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(Container input, RegistryAccess registries) {
        return this.result.copy();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
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

    public static class Serializer implements RecipeSerializer<FridgeFreezingRecipe> {
        @Override
        public FridgeFreezingRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            int cookingTime = GsonHelper.getAsInt(json, "cookingTime", 200);
            return new FridgeFreezingRecipe(id, ingredient, result, cookingTime);
        }

        @Override
        public FridgeFreezingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient ingredient = Ingredient.fromNetwork(buf);
            ItemStack result = buf.readItem();
            int cookingTime = buf.readVarInt();
            return new FridgeFreezingRecipe(id, ingredient, result, cookingTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, FridgeFreezingRecipe recipe) {
            recipe.getIngredient().toNetwork(buf);
            buf.writeItem(recipe.getResult());
            buf.writeVarInt(recipe.getCookingTime());
        }
    }
}
