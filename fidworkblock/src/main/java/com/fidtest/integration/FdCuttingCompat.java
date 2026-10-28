package com.fidtest.integration;

import com.fidtest.block.entity.ChoppingboardBlockEntity;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

// 农夫乐事砧板（切割）配方兼容工具。
// 农夫乐事及其附属模组通过 farmersdelight:cutting 配方类型注册砧板配方，
// 这里用反射读取，供本模组砧板的运行时逻辑与 JEI 展示共用。
// 未安装农夫乐事（或配方结构不匹配）时全部安全降级为空，不影响本模组运行。
public final class FdCuttingCompat {

// 一条切割配方：输入 + 刀具 + 原始配方（产物需按需读取）。
    public record Cutting(ResourceLocation id, Ingredient input, Ingredient tool, Recipe<?> recipe) {}

    private static boolean checked = false;
    @Nullable
    private static RecipeType<?> cuttingType = null;

    private FdCuttingCompat() {}

// 农夫乐事的砧板配方类型 farmersdelight:cutting；未安装返回 null。
    @Nullable
    public static RecipeType<?> getCuttingType() {
        if (!checked) {
            checked = true;
            try {
                var type = BuiltInRegistries.RECIPE_TYPE.get(
                        new ResourceLocation("farmersdelight", "cutting"));
                if (type != null) {
                    cuttingType = type;
                }
            } catch (Exception ignored) {}
        }
        return cuttingType;
    }

// 读取全部农夫乐事切割配方（含其附属模组）。
    public static List<Cutting> all(RecipeManager recipeManager) {
        List<Cutting> result = new ArrayList<>();
        RecipeType<?> type = getCuttingType();
        if (type == null || recipeManager == null) return result;
        try {
            @SuppressWarnings("unchecked")
            List<Recipe<?>> recipes = (List<Recipe<?>>) (List<?>) recipeManager.getAllRecipesFor((RecipeType) type);
            for (Recipe<?> recipe : recipes) {
                try {
                    List<Ingredient> ingredients = recipe.getIngredients();
                    if (ingredients.isEmpty()) continue;
// 刀具：CuttingBoardRecipe#getTool() 返回单一 Ingredient（JSON 里的列表会被合并）
                    Method toolMethod = recipe.getClass().getMethod("getTool");
                    Object tool = toolMethod.invoke(recipe);
                    if (!(tool instanceof Ingredient ingredient)) continue;
                    result.add(new Cutting(recipe.getId(), ingredients.get(0), ingredient, recipe));
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        return result;
    }

// 找出输入与刀具都匹配的切割配方，供砧板运行时使用。
    @Nullable
    public static Cutting findMatch(RecipeManager recipeManager, ItemStack input, ItemStack tool) {
        for (Cutting cutting : all(recipeManager)) {
            if (cutting.input().test(input) && cutting.tool().test(tool)) {
                return cutting;
            }
        }
        return null;
    }

// 该配方是否使用刀具。
// 农夫乐事切割配方也包含斧头去皮、锄头翻土等非刀具配方，
// 本模组砧板只接管刀具配方，因此 JEI 展示时需要过滤。
    public static boolean usesKnife(Cutting cutting) {
        for (ItemStack stack : cutting.tool().getItems()) {
            if (ChoppingboardBlockEntity.isKnife(stack)) return true;
// 兜底：附属模组增加的刀具可能未加入标签，按注册名判断
            if (stack.getItem().builtInRegistryHolder().key().location().getPath().contains("knife")) {
                return true;
            }
        }
        return false;
    }

// 全部产物（不做概率判定），用于 JEI 展示。
    public static List<ItemStack> readResults(Recipe<?> recipe, @Nullable RegistryAccess registries) {
        try {
            Method method = recipe.getClass().getMethod("getResults");
            Object value = method.invoke(recipe);
            if (value instanceof List<?> list) {
                List<ItemStack> out = collect(list);
                if (!out.isEmpty()) return out;
            }
        } catch (Exception ignored) {}
        if (registries == null) return List.of();
        ItemStack single = recipe.getResultItem(registries);
        return single.isEmpty() ? List.of() : List.of(single);
    }

// 按概率取产物（调用农夫乐事自己的 rollResults，行为与其砧板完全一致），
// 供本模组砧板实际掉落使用；失败时返回空表。
    public static List<ItemStack> rollResults(Recipe<?> recipe, RandomSource random) {
        for (Method method : recipe.getClass().getMethods()) {
            if (!method.getName().equals("rollResults") || method.getParameterCount() != 3) continue;
            try {
// 第三参数为 RecipeWrapper，农夫乐事实现中并未使用，传 null 即可
                Object value = method.invoke(recipe, new Object[]{random, 0, null});
                if (value instanceof List<?> list) {
                    return collect(list);
                }
            } catch (Exception ignored) {}
        }
        return List.of();
    }

    private static List<ItemStack> collect(List<?> list) {
        List<ItemStack> out = new ArrayList<>();
        for (Object element : list) {
            if (element instanceof ItemStack stack && !stack.isEmpty()) {
                out.add(stack);
            }
        }
        return out;
    }
}
