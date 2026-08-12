package com.flavor_immersed_daily.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * 滤布 — 合成时保留不会被消耗
 */
public class CoarseClothItem extends Item {

    public CoarseClothItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.copyWithCount(1);
    }
}