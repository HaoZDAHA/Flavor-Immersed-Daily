package com.flavor_immersed_daily.integration.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

// EMI 屠宰配方 — 显示 死动物 + 刀具 → 掉落物，每行4个产物自动换行。
final class FIDButcheringEmiRecipe extends BasicEmiRecipe {

    private static final int OUTPUTS_PER_ROW = 4;
    private final String toolName;

    FIDButcheringEmiRecipe(ResourceLocation id, ItemStack input, ItemStack tool, String toolName,
                           List<ItemStack> outputs) {
        super(FIDEmiPlugin.BUTCHERING, id, 170, 58);
        this.toolName = toolName;
        this.inputs.add(EmiStack.of(input));
        this.inputs.add(EmiStack.of(tool));
        this.outputs.addAll(FIDEmiPlugin.mergeDrops(outputs).stream().map(EmiStack::of).toList());
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(inputs.get(0), 3, 20);
        widgets.addSlot(inputs.get(1), 28, 5);
        widgets.addFillingArrow(50, 22, 1000);
        widgets.addText(Component.literal(toolName), 28, 27, 0xFF555555, false);

        int startX = 62, startY = 14;
        for (int i = 0; i < outputs.size(); i++) {
            int col = i % OUTPUTS_PER_ROW, row = i / OUTPUTS_PER_ROW;
            widgets.addSlot(outputs.get(i), startX + col * 21, startY + row * 26).recipeContext(this);
        }
    }
}