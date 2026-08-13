package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.client.tooltip.HarvestTooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

/**
 * 野生作物物品 — 在 tooltip 中显示厨房剪刀图标和获取方式
 */
public class WildHarvestItem extends Item {

    private final Component tooltipText;

    public WildHarvestItem(Properties properties, Component tooltipText) {
        super(properties);
        this.tooltipText = tooltipText;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return Optional.of(new HarvestTooltip(tooltipText, List.of(new ItemStack(ModItems.KITCHENSCISSOR.get()))));
    }
}
