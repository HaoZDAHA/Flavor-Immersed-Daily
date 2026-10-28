package com.flavor_immersed_daily.integration.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

// EMI 作物转换配方 — 显示转换组内物品的互相转换：输入 → 组内其他已安装物品，每行4个产物自动换行。
final class FIDCropConversionEmiRecipe extends BasicEmiRecipe {

    private static final int OUTPUTS_PER_ROW = 4;

    FIDCropConversionEmiRecipe(ResourceLocation id, ItemStack input, List<ItemStack> outputs) {
        super(FIDEmiPlugin.CROP_CONVERSION, id, 176, 150);
        this.inputs.add(EmiStack.of(input));
        for (ItemStack stack : outputs) {
            if (!stack.isEmpty()) {
                this.outputs.add(EmiStack.of(stack));
            }
        }
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
