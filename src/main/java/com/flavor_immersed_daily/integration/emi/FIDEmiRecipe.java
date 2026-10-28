package com.flavor_immersed_daily.integration.emi;

import com.flavor_immersed_daily.basin.BasinWashDefinition;
import com.flavor_immersed_daily.recipe.EggBreakingRecipe;
import com.flavor_immersed_daily.recipe.FridgeFreezingRecipe;
import com.flavor_immersed_daily.recipe.FridgeTemperingRecipe;
import com.flavor_immersed_daily.recipe.WoodBasinRecipe;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

final class FIDEmiRecipe extends BasicEmiRecipe {
    private final int cookingTime;
    private final boolean gridLayout;

    private FIDEmiRecipe(EmiRecipeCategory category, ResourceLocation id, List<Ingredient> ingredients,
                         List<ItemStack> results, int cookingTime, boolean gridLayout) {
        super(category, id, gridLayout ? 142 : 106, gridLayout ? 62 : 30);
        this.inputs.addAll(ingredients.stream().map(EmiIngredient::of).toList());
        this.outputs.addAll(results.stream().filter(stack -> !stack.isEmpty()).map(EmiStack::of).toList());
        this.cookingTime = cookingTime;
        this.gridLayout = gridLayout;
    }

    static FIDEmiRecipe woodBasin(RecipeHolder<WoodBasinRecipe> holder) {
        WoodBasinRecipe recipe = holder.value();
        return new FIDEmiRecipe(FIDEmiPlugin.WOOD_BASIN, holder.id(), List.of(recipe.getIngredient()),
                List.of(recipe.getResult()), 0, false);
    }

// 数据包 basin_wash 定义：放进有水木盆的物品 → 取回产物 + 额外掉落。
    static FIDEmiRecipe basinWash(BasinWashDefinition def) {
        List<ItemStack> results = new ArrayList<>();
        if (def.output() != null) {
            results.add(new ItemStack(def.output()));
        }
        for (Item drop : def.drops()) {
            results.add(new ItemStack(drop));
        }
        String safe = def.id().getNamespace() + "_" + def.id().getPath().replace('/', '_');
        return new FIDEmiRecipe(FIDEmiPlugin.WOOD_BASIN, FIDEmiPlugin.id("basin_wash/" + safe),
                List.of(Ingredient.of(def.input())), results, 0, false);
    }

// 木盆内置洗鸡（未命中数据包 basin_wash 定义时的默认产出）：与 JEI 侧一致的信息条目。
    static FIDEmiRecipe woodBasinWashInfo(List<ItemStack> results) {
        return new FIDEmiRecipe(FIDEmiPlugin.WOOD_BASIN, FIDEmiPlugin.id("basin_wash_builtin"),
                List.of(), results, 0, false);
    }

    static FIDEmiRecipe eggBreaking(RecipeHolder<EggBreakingRecipe> holder) {
        EggBreakingRecipe recipe = holder.value();
        return new FIDEmiRecipe(FIDEmiPlugin.EGG_BREAKING, holder.id(), recipe.getIngredientList(),
                recipe.getResults(), recipe.getCookingTime(), true);
    }

    static FIDEmiRecipe tempering(RecipeHolder<FridgeTemperingRecipe> holder) {
        FridgeTemperingRecipe recipe = holder.value();
        return new FIDEmiRecipe(FIDEmiPlugin.FRIDGE_TEMPERING, holder.id(), List.of(recipe.getIngredient()),
                List.of(recipe.getResult()), recipe.getCookingTime(), false);
    }

    static FIDEmiRecipe freezing(RecipeHolder<FridgeFreezingRecipe> holder) {
        FridgeFreezingRecipe recipe = holder.value();
        return new FIDEmiRecipe(FIDEmiPlugin.FRIDGE_FREEZING, holder.id(), List.of(recipe.getIngredient()),
                List.of(recipe.getResult()), recipe.getCookingTime(), false);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        if (gridLayout) {
            addGridWidgets(widgets);
        } else {
            if (!inputs.isEmpty()) {
                widgets.addSlot(inputs.getFirst(), 2, 6);
            }
            widgets.addFillingArrow(28, 7, cookingTime > 0 ? cookingTime * 50 : 1000);
            if (!outputs.isEmpty()) {
                widgets.addSlot(outputs.getFirst(), 78, 6).recipeContext(this);
            }
        }
        if (cookingTime > 0) {
            widgets.addText(Component.translatable("emi.flavor_immersed_daily.time",
                            String.format(Locale.ROOT, "%.1f", cookingTime / 20.0F)),
                    width - 2, height - 9, 0x777777, false)
                    .horizontalAlign(dev.emi.emi.api.widget.TextWidget.Alignment.END);
        }
    }

    private void addGridWidgets(WidgetHolder widgets) {
        for (int i = 0; i < inputs.size(); i++) {
            widgets.addSlot(inputs.get(i), 2 + i % 3 * 18, 2 + i / 3 * 18);
        }
        widgets.addFillingArrow(60, 20, Math.max(1000, cookingTime * 50));
        for (int i = 0; i < outputs.size(); i++) {
            widgets.addSlot(outputs.get(i), 96 + i % 2 * 18, 11 + i / 2 * 18).recipeContext(this);
        }
    }
}
