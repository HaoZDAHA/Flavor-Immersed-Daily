package com.flavor_immersed_daily.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

// 灯谜牌物品 — tooltip 说明悬挂方式、放置者编辑内容与答题领奖规则
public class RaddlesBlockItem extends BlockItem {

    public RaddlesBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.raddles"));
        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.raddles.owner"));
        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.raddles.answer"));
    }
}
