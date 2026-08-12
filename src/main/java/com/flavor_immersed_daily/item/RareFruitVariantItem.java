package com.flavor_immersed_daily.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * 稀有水果变种物品 — 带有 tooltip 提示
 */
public class RareFruitVariantItem extends Item {

    public RareFruitVariantItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.rare_fruit_variant"));
    }
}