package com.fidtest.integration.emi;

import com.fidtest.block.entity.ChoppingboardBlockEntity;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

// EMI 砧板切割配方 —— 输入物品 + 刀具（本模组菜刀 / 农夫乐事刀具标签）→ 产物（可能多个）。
// 既展示本模组砧板自带的切割配方，也展示农夫乐事（含附属）引入的 farmersdelight:cutting
// 刀具配方；与本模组配方重叠的农夫乐事配方不会注册（优先本模组）。
final class FIDChoppingboardCuttingEmiRecipe extends BasicEmiRecipe {

    private static final int OUTPUTS_PER_ROW = 2;

    private final List<ItemStack> results;

    private FIDChoppingboardCuttingEmiRecipe(ResourceLocation id, Ingredient input,
                                             List<ItemStack> results) {
        super(FidworkEmiPlugin.CHOPPINGBOARD_CUTTING, id, 116, 56);
        this.results = results;
        this.inputs.add(EmiIngredient.of(input));
// 刀具：农夫乐事刀具标签（本模组菜刀已加入该标签，故一并展示）
        this.inputs.add(EmiIngredient.of(Ingredient.of(ChoppingboardBlockEntity.FD_KNIFE_TAG)));
        for (ItemStack stack : results) {
            this.outputs.add(EmiStack.of(stack));
        }
    }

// 本模组砧板自带配方。
    static FIDChoppingboardCuttingEmiRecipe ofOwn(ResourceLocation id, Ingredient input,
                                                  List<ItemStack> results) {
        return new FIDChoppingboardCuttingEmiRecipe(id, input, results);
    }

// 农夫乐事切割配方。
    static FIDChoppingboardCuttingEmiRecipe ofFd(ResourceLocation id, Ingredient input,
                                                 List<ItemStack> results) {
        return new FIDChoppingboardCuttingEmiRecipe(id, input, results);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(inputs.get(0), 11, 11);
        widgets.addSlot(inputs.get(1), 11, 35);
        widgets.addFillingArrow(40, 20, 1000);

// 产物：单个居中展示；多个产物按 2 列排布
        if (results.size() == 1) {
            widgets.addSlot(outputs.get(0), 81, 20).recipeContext(this);
        } else {
            for (int i = 0; i < results.size(); i++) {
                int col = i % OUTPUTS_PER_ROW;
                int row = i / OUTPUTS_PER_ROW;
                widgets.addSlot(outputs.get(i), 78 + col * 18, 2 + row * 18).recipeContext(this);
            }
        }
    }
}
