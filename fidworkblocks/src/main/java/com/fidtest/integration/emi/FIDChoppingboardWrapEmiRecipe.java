package com.fidtest.integration.emi;

import com.fidtest.choppingboard.ChoppingboardWrapRecipes;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

// EMI 砧板「包裹/成型」配方 —— 主料 + 该工序工具 → 产物；悬停显示工序说明。
// 数据与 JEI 侧共用 ChoppingboardWrapRecipes，保证 JEI 与 EMI 始终同步。
final class FIDChoppingboardWrapEmiRecipe extends BasicEmiRecipe {

    private final String mode;
    private final ItemStack tool;

    FIDChoppingboardWrapEmiRecipe(ResourceLocation id, ChoppingboardWrapRecipes.Entry entry) {
        super(FidworkEmiPlugin.CHOPPINGBOARD_WRAP, id, 116, 56);
        this.mode = entry.mode();
        this.tool = ChoppingboardWrapRecipes.toolFor(entry.mode());
        this.inputs.add(EmiStack.of(entry.input()));
        if (!this.tool.isEmpty()) {
            this.inputs.add(EmiStack.of(this.tool));
        }
        this.outputs.add(EmiStack.of(entry.output()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(inputs.get(0), 11, 11);
        if (tool.isEmpty()) {
// 包裹工序无额外工具，保留空槽位占位
            widgets.addSlot(11, 35);
        } else {
            widgets.addSlot(inputs.get(1), 11, 35);
        }
        widgets.addFillingArrow(40, 20, 1000);
        widgets.addSlot(outputs.get(0), 81, 20).recipeContext(this);

// 工序说明：悬停配方任意位置显示
        widgets.addTooltipText(List.of(Component.translatable(ChoppingboardWrapRecipes.noteKey(mode))),
                0, 0, widgets.getWidth(), widgets.getHeight());
    }
}
