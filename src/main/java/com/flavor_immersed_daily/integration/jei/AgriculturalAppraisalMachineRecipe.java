package com.flavor_immersed_daily.jei;

import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * 农产鉴定配方 — 一个记录：输入野生采集物 → 掉落物列表
 */
public class AgriculturalAppraisalMachineRecipe {

    private final ItemStack input;
    private final List<ItemStack> outputs;

    public AgriculturalAppraisalMachineRecipe(ItemStack input, List<ItemStack> outputs) {
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