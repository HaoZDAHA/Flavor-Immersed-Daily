package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 厨房剪刀 — 破坏草和树叶时可获得野生作物
 */
public class KitchenScissorsItem extends Item {

    private static final Component TOOLTIP_TEXT = Component.translatable("tooltip.flavor_immersed_daily.kitchen_scissors");
    private static List<ItemStack> wildItems = null;

    private static List<ItemStack> getWildItems() {
        if (wildItems == null) {
            List<ItemStack> list = new ArrayList<>();
            list.add(new ItemStack(FlavorImmersedDaily.TEMPERATEWILDFRUIT.get()));
            list.add(new ItemStack(FlavorImmersedDaily.TROPICALWILD_FRUIT.get()));
            list.add(new ItemStack(FlavorImmersedDaily.WILDFRUITINCOLDZONE.get()));
            list.add(new ItemStack(FlavorImmersedDaily.WILDFLOWERANDLEAF.get()));
            list.add(new ItemStack(FlavorImmersedDaily.WILDGRAINPLANT.get()));
            list.add(new ItemStack(FlavorImmersedDaily.WILDMUSHROOMPLANT.get()));
            list.add(new ItemStack(FlavorImmersedDaily.WILDSEEDPLANT.get()));
            list.add(new ItemStack(FlavorImmersedDaily.WILDTUBERPLANTS.get()));
            wildItems = List.copyOf(list);
        }
        return wildItems;
    }

    public KitchenScissorsItem(Properties properties) {
        super(properties);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return Optional.of(new HarvestTooltip(TOOLTIP_TEXT, getWildItems()));
    }
}