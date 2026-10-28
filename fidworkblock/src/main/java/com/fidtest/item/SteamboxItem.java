package com.fidtest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class SteamboxItem extends BlockItem {
    public SteamboxItem(Block block, Properties p) { super(block, p); }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tips, TooltipFlag flag) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("SteamboxData")) {
            CompoundTag data = tag.getCompound("SteamboxData");
            ListTag inputList = data.getList("Input", 10);
            if (!inputList.isEmpty()) {
                tips.add(Component.translatable("tooltip.fidworkblock.steambox_packed"));
                tips.add(Component.translatable("tooltip.fidworkblock.steambox_contents").withStyle(ChatFormatting.GRAY));
                for (int i = 0; i < inputList.size(); i++) {
                    ItemStack s = ItemStack.of(inputList.getCompound(i));
                    tips.add(Component.literal("  ").append(s.getHoverName()).append(" x" + s.getCount()).withStyle(ChatFormatting.GRAY));
                }
                tips.add(Component.translatable("tooltip.fidworkblock.steambox_usage").withStyle(ChatFormatting.DARK_GRAY));
            }
        } else {
            tips.add(Component.translatable("tooltip.fidworkblock.steambox_stage1_hint").withStyle(ChatFormatting.GRAY));
        }
    }
}
