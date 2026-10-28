package com.flavor_immersed_daily.integration.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

// EMI 农产鉴定配方 — 显示 野生采集物 → 掉落物，每行4个产物自动换行。
final class FIDAppraisalEmiRecipe extends BasicEmiRecipe {

    private static final int OUTPUTS_PER_ROW = 4;

    FIDAppraisalEmiRecipe(ResourceLocation id, ItemStack input, List<ItemStack> outputs) {
        super(FIDEmiPlugin.APPRAISAL, id, 176, 150);
        this.inputs.add(EmiStack.of(input));
        this.outputs.addAll(FIDEmiPlugin.mergeDrops(outputs).stream().map(EmiStack::of).toList());
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(inputs.get(0), 3, 20);
        widgets.addFillingArrow(28, 22, 1000);

        int startX = 62, startY = 14;
        for (int i = 0; i < outputs.size(); i++) {
            int col = i % OUTPUTS_PER_ROW, row = i / OUTPUTS_PER_ROW;
            widgets.addSlot(outputs.get(i), startX + col * 21, startY + row * 26).recipeContext(this);
        }
    }
}