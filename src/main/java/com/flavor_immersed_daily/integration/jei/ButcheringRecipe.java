package com.flavor_immersed_daily.integration.jei;

import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * 屠宰配方 — 一个记录：输入死动物 + 所需刀具 → 掉落物列表
 */
public class ButcheringRecipe {

    private final ItemStack input;
    private final ItemStack tool;
    private final String toolName;
    private final List<ItemStack> outputs;

    public ButcheringRecipe(ItemStack input, ItemStack tool, String toolName, List<ItemStack> outputs) {
        this.input = input;
        this.tool = tool;
        this.toolName = toolName;
        this.outputs = outputs;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getTool() {
        return tool;
    }

    public String getToolName() {
        return toolName;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }
}
