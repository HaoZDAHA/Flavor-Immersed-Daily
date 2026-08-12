package com.flavor_immersed_daily.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

/**
 * 对联物品 — 带有红色加粗 tooltip 说明用法
 */
public class CoupletBlockItem extends BlockItem {

    private static final Component TOOLTIP = Component.translatable("tooltip.flavor_immersed_daily.couplet");

    public CoupletBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(TOOLTIP);
    }
}