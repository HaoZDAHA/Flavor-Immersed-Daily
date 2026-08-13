package com.flavor_immersed_daily.client.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 通用 tooltip 方块物品 — 显示文字 + 多个物品图标
 */
public class TooltipBlockItem extends BlockItem {

    private final Component tooltipText;
    private final Supplier<List<ItemStack>> iconStacks;

    public TooltipBlockItem(Block block, Properties properties, Component tooltipText, Supplier<List<ItemStack>> iconStacks) {
        super(block, properties);
        this.tooltipText = tooltipText;
        this.iconStacks = iconStacks;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return Optional.of(new HarvestTooltip(tooltipText, iconStacks.get()));
    }
}