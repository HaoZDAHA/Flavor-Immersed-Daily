package com.fidtest.integration.emi;

import com.fidtest.recipe.ApplianceType;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

// EMI 通用机器配方 —— 与 JEI 各机器分类一一对应，槽位坐标沿用 JEI 的布局，保证两侧同步。
// 覆盖：锅/蒸笼/炒锅/高压锅/绞肉机（多输入烹饪布局）、烘干机（烘干/烤箱）、
// 研钵、榨汁机、平底锅（含油）、罐子、茶壶。
final class FIDApplianceEmiRecipe extends BasicEmiRecipe {

// 对齐 JEI CookingRecipeCategory 的输入起始坐标/列数（按材料数取用）。
    private static final int[][] INPUT_START = {
            {40, 10}, {26, 0}, {22, 10}, {13, 10}, {13, 1}, {4, 10}, {1, 1}
    };
    private static final int[] INPUT_COLS = {1, 1, 2, 3, 2, 3, 3};
    private static final int PAD = 1;

    private enum Layout { SINGLE, FRYINGPAN, FOUR_LEFT, COOKING, DRYER }

    private final Layout layout;
    private final int inputCount;
    private final ItemStack container;
    private final int cookingTime;
    private final float experience;

    FIDApplianceEmiRecipe(EmiRecipeCategory category, ResourceLocation id, ApplianceType appliance,
                          List<Ingredient> ingredients, List<ItemStack> results,
                          ItemStack container, int cookingTime, float experience) {
        super(category, id, 116, 56);
        this.layout = layoutOf(appliance);
        this.inputCount = Math.min(ingredients.size(), 6);
        this.container = container == null ? ItemStack.EMPTY : container;
        this.cookingTime = cookingTime;
        this.experience = experience;
        for (Ingredient ingredient : ingredients) {
            this.inputs.add(EmiIngredient.of(ingredient));
        }
        for (ItemStack stack : results) {
            if (!stack.isEmpty()) {
                this.outputs.add(EmiStack.of(stack));
            }
        }
    }

    private static Layout layoutOf(ApplianceType appliance) {
        return switch (appliance) {
            case MORTAR, SQUEEZINGMACHINE -> Layout.SINGLE;
            case FRYINGPAN -> Layout.FRYINGPAN;
            case JAR, TEAPOT -> Layout.FOUR_LEFT;
            case DRYER -> Layout.DRYER;
            default -> Layout.COOKING;
        };
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        switch (layout) {
            case SINGLE -> {
                if (!inputs.isEmpty()) widgets.addSlot(inputs.get(0), 11, 20);
                widgets.addFillingArrow(40, 20, arrowTime());
                addOutput(widgets, 81, 20);
            }
            case FRYINGPAN -> {
// 主料 + 固定油槽（与 JEI 侧一致，第二格固定展示食用油）
                if (!inputs.isEmpty()) widgets.addSlot(inputs.get(0), 11, 12);
                ItemStack oil = new ItemStack(BuiltInRegistries.ITEM.get(
                        ResourceLocation.fromNamespaceAndPath("flavor_immersed_daily", "cookingoil")));
                if (!oil.isEmpty()) {
                    widgets.addSlot(EmiStack.of(oil), 11, 32);
                }
                widgets.addFillingArrow(40, 20, arrowTime());
                addOutput(widgets, 81, 20);
            }
            case FOUR_LEFT -> {
                for (int i = 0; i < 4 && i < inputs.size(); i++) {
                    widgets.addSlot(inputs.get(i), 5 + i * 18, 12);
                }
                widgets.addFillingArrow(70, 20, arrowTime());
                addOutput(widgets, 89, 20);
            }
            case COOKING -> {
                addCookingInputs(widgets);
                widgets.addFillingArrow(59, 19, arrowTime());
                addOutput(widgets, 95, 19);
                addContainer(widgets);
            }
            case DRYER -> {
                addCookingInputs(widgets);
                widgets.addFillingArrow(49, 4, arrowTime());
                addOutput(widgets, 80, 6);
                addContainer(widgets);
            }
        }

        List<Component> lines = new ArrayList<>();
        if (cookingTime > 0) {
            lines.add(Component.translatable("gui.jei.category.smelting.time.seconds", cookingTime / 20));
        }
        if (experience > 0) {
            lines.add(Component.translatable("gui.jei.category.smelting.experience", experience));
        }
        if (!lines.isEmpty()) {
            widgets.addTooltipText(lines, 0, 0, widgets.getWidth(), widgets.getHeight());
        }
    }

// 多输入烹饪布局：按材料数取 JEI 的起始坐标与列数。
    private void addCookingInputs(WidgetHolder widgets) {
        int cols = INPUT_COLS[inputCount];
        for (int i = 0; i < inputCount; i++) {
            int row = i / cols;
            int col = i % cols;
            widgets.addSlot(inputs.get(i),
                    INPUT_START[inputCount][0] + col * 18 + PAD,
                    INPUT_START[inputCount][1] + row * 18 + PAD);
        }
    }

    private void addOutput(WidgetHolder widgets, int x, int y) {
        if (!outputs.isEmpty()) {
            widgets.addSlot(outputs.get(0), x, y).recipeContext(this);
        }
    }

    private void addContainer(WidgetHolder widgets) {
        if (!container.isEmpty()) {
            widgets.addSlot(EmiStack.of(container), 73, 37);
        }
    }

    private int arrowTime() {
        return cookingTime > 0 ? cookingTime * 50 : 1000;
    }
}
