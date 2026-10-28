package com.fidtest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class JarItem extends BlockItem {
    public JarItem(Block block, Properties p) { super(block, p); }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tips, TooltipFlag flag) {
        tips.add(Component.translatable("tooltip.fidworkblock.jar_desc1").withStyle(ChatFormatting.DARK_GRAY));
        tips.add(Component.translatable("tooltip.fidworkblock.jar_desc2").withStyle(ChatFormatting.DARK_GRAY));
    }
}
