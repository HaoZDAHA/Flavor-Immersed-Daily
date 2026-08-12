package com.flavor_immersed_daily.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 通用 tooltip 物品 — 显示文字 + 一组物品图标（可为空）
 */
public class TooltipItem extends Item {

    private final Component tooltipText;
    private final Supplier<List<ItemStack>> iconStacks;

    public TooltipItem(Properties properties, Component tooltipText, Supplier<List<ItemStack>> iconStacks) {
        super(properties);
        this.tooltipText = tooltipText;
        this.iconStacks = iconStacks;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return Optional.of(new HarvestTooltip(tooltipText, iconStacks.get()));
    }
}