package com.flavor_immersed_daily.integration.jei;

import net.minecraft.world.item.ItemStack;

import java.util.List;

// 作物转换 JEI 配方 — 一条记录：输入物品 → 可转换的其他物品列表
public class CropConversionJeiRecipe {

    private final ItemStack input;
    private final List<ItemStack> outputs;

    public CropConversionJeiRecipe(ItemStack input, List<ItemStack> outputs) {
        this.input = input;
        this.outputs = outputs;
    }

    public ItemStack getInput() {
        return input;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }
}
