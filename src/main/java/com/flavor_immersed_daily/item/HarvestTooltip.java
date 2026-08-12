package com.flavor_immersed_daily.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * 收获提示 tooltip 组件 — 携带一段文字 + 一组物品图标
 */
public class HarvestTooltip implements TooltipComponent {

    private final Component text;
    private final List<ItemStack> items;

    public HarvestTooltip(Component text, List<ItemStack> items) {
        this.text = text;
        this.items = items;
    }

    public Component text() {
        return text;
    }

    public List<ItemStack> items() {
        return items;
    }
}