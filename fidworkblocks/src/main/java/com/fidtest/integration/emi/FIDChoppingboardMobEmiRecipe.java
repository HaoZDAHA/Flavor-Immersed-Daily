package com.fidtest.integration.emi;

import com.fidtest.choppingboard.ChoppingboardMobDefinition;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// EMI 砧板小型生物屠宰配方 — 输入物品 + 刀具 → 合并所有阶段掉落的产物，每行4个自动换行。
final class FIDChoppingboardMobEmiRecipe extends BasicEmiRecipe {

    private static final int OUTPUTS_PER_ROW = 4;

    private final int stageCount;

    FIDChoppingboardMobEmiRecipe(ResourceLocation id, ChoppingboardMobDefinition definition) {
        super(FidworkEmiPlugin.CHOPPINGBOARD_MOB, id, 170, 58);
        this.stageCount = definition.stageCount();
        this.inputs.add(EmiStack.of(new ItemStack(definition.input())));
        this.inputs.add(toolIngredient(definition));
        this.outputs.addAll(mergeDrops(definition).stream().map(EmiStack::of).toList());
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(inputs.get(0), 3, 20);
        widgets.addSlot(inputs.get(1), 28, 5);
        widgets.addFillingArrow(50, 22, 1000);
// 提示需要右键的刀数
        widgets.addText(Component.translatable("jei.fidworkblock.choppingboard_mob.note.hits", stageCount),
                3, 44, 0xFF555555, false);

        int startX = 62, startY = 14;
        for (int i = 0; i < outputs.size(); i++) {
            int col = i % OUTPUTS_PER_ROW, row = i / OUTPUTS_PER_ROW;
            widgets.addSlot(outputs.get(i), startX + col * 21, startY + row * 26).recipeContext(this);
        }
    }

// 刀具：具体物品 → 单栈；标签 → 展开全部成员。
    private static EmiIngredient toolIngredient(ChoppingboardMobDefinition definition) {
        if (definition.toolItem() != null) {
            return EmiStack.of(new ItemStack(definition.toolItem()));
        }
        if (definition.toolTag() != null) {
            return EmiIngredient.of(Ingredient.of(definition.toolTag()));
        }
        return EmiStack.EMPTY;
    }

// 合并所有阶段掉落的物品并累加数量。
    private static List<ItemStack> mergeDrops(ChoppingboardMobDefinition definition) {
        Map<Item, Integer> merged = new LinkedHashMap<>();
        for (ChoppingboardMobDefinition.Stage stage : definition.stages()) {
            for (Item drop : stage.drops()) {
                merged.merge(drop, 1, Integer::sum);
            }
        }
        List<ItemStack> result = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : merged.entrySet()) {
            result.add(new ItemStack(entry.getKey(), entry.getValue()));
        }
        return result;
    }
}
