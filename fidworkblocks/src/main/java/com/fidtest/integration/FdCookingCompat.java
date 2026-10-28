package com.fidtest.integration;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

// 农夫乐事烹饪锅（farmersdelight:cooking）配方兼容工具。
// 农夫乐事及其附属模组通过该配方类型注册烹饪锅配方，这里用反射读取，
// 供本模组锅内配方的 JEI/EMI 展示共用（两侧同源，保证同步）。
// 未安装农夫乐事（或配方结构不匹配）时全部安全降级为空。
public final class FdCookingCompat {

// 一条烹饪锅配方。
    public record Cooking(ResourceLocation id, List<Ingredient> ingredients, ItemStack result,
                          ItemStack container, int cookTime, float experience) {}

    private static boolean checked = false;
    @Nullable
    private static RecipeType<?> cookingType = null;

    private FdCookingCompat() {}

// 农夫乐事的烹饪锅配方类型 farmersdelight:cooking；未安装返回 null。
    @Nullable
    public static RecipeType<?> getCookingType() {
        if (!checked) {
            checked = true;
            try {
                var type = BuiltInRegistries.RECIPE_TYPE.get(
                        ResourceLocation.fromNamespaceAndPath("farmersdelight", "cooking"));
                if (type != null) {
                    cookingType = type;
                }
            } catch (Exception ignored) {}
        }
        return cookingType;
    }

// 读取全部农夫乐事烹饪锅配方（含其附属模组）。
    public static List<Cooking> all(RecipeManager recipeManager, @Nullable HolderLookup.Provider registries) {
        List<Cooking> result = new ArrayList<>();
        RecipeType<?> type = getCookingType();
        if (type == null || recipeManager == null || registries == null) return result;
        try {
            @SuppressWarnings("unchecked")
            List<RecipeHolder<?>> holders = recipeManager.getAllRecipesFor((RecipeType) type);
            for (RecipeHolder<?> holder : holders) {
                try {
                    Recipe<?> recipe = holder.value();
                    ItemStack out = recipe.getResultItem(registries);
                    if (out.isEmpty()) continue;

                    int cookTime = 200;
                    float experience = 0.0F;
                    try {
                        Method m = recipe.getClass().getMethod("getCookTime");
                        cookTime = (int) m.invoke(recipe);
                    } catch (Exception ignored) {}
                    try {
                        Method m = recipe.getClass().getMethod("getExperience");
                        experience = (float) m.invoke(recipe);
                    } catch (Exception ignored) {}

                    result.add(new Cooking(holder.id(), recipe.getIngredients(), out,
                            readOutputContainer(recipe), cookTime, experience));
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        return result;
    }

// 读取配方的容器产物（getOutputContainer）；无则返回空栈。
    private static ItemStack readOutputContainer(Recipe<?> recipe) {
        try {
            Method m = recipe.getClass().getMethod("getOutputContainer");
            Object value = m.invoke(recipe);
            if (value instanceof ItemStack stack) {
                return stack;
            }
        } catch (Exception ignored) {}
        return ItemStack.EMPTY;
    }
}
