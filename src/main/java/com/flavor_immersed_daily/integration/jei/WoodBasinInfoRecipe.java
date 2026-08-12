package com.flavor_immersed_daily.integration.jei;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * 木盆信息页配方 — 展示输入→产物 + 操作描述
 */
public class WoodBasinInfoRecipe {

    private final ItemStack input;       // 输入物品（可为空表示纯信息页）
    private final List<ItemStack> outputs;
    private final String description;

    public WoodBasinInfoRecipe(ItemStack input, List<ItemStack> outputs, String description) {
        this.input = input;
        this.outputs = outputs;
        this.description = description;
    }

    public ItemStack getInput() {
        return input;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }

    public String getDescription() {
        return description;
    }

    /** 从字符串创建输入物品 */
    public static ItemStack itemFromId(String itemId) {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemId));
        return item != null ? new ItemStack(item) : ItemStack.EMPTY;
    }
}
