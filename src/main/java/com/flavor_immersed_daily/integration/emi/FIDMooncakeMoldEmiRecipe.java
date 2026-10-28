package com.flavor_immersed_daily.integration.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

// EMI 月饼模具配方页 — 展示「月饼模具 + 饼皮 → 生月饼 → 成品月饼」的压制链路。
final class FIDMooncakeMoldEmiRecipe extends BasicEmiRecipe {

    FIDMooncakeMoldEmiRecipe(ResourceLocation id, ItemStack mold, ItemStack dough,
                             ItemStack raw, ItemStack finished) {
        super(FIDEmiPlugin.MOONCAKE_MOLD, id, 146, 30);
        this.inputs.add(EmiStack.of(mold));
        this.inputs.add(EmiStack.of(dough));
        this.outputs.add(EmiStack.of(raw));
        this.outputs.add(EmiStack.of(finished));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(inputs.get(0), 1, 6);
        widgets.addSlot(inputs.get(1), 20, 6);
        widgets.addFillingArrow(42, 7, 1000);
        widgets.addSlot(outputs.get(0), 70, 6).recipeContext(this);
        widgets.addFillingArrow(92, 7, 1000);
        widgets.addSlot(outputs.get(1), 120, 6).recipeContext(this);
    }
}
