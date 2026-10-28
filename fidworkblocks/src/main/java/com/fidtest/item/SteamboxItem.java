package com.fidtest.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class SteamboxItem extends BlockItem {

    public SteamboxItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (isPacked(stack)) {
            tooltipComponents.add(Component.translatable("tooltip.fidworkblock.steambox_packed"));
            tooltipComponents.add(Component.translatable("tooltip.fidworkblock.steambox_contents")
                    .withColor(0x888888));
            tooltipComponents.add(Component.translatable("tooltip.fidworkblock.steambox_usage")
                    .withColor(0x555555));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.fidworkblock.steambox_stage1_hint")
                    .withColor(0x888888));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        Boolean override = stack.get(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
        if (override != null) return override;
        return false;
    }

    public static boolean isPacked(ItemStack stack) {
        try {
            CustomData custom = stack.get(DataComponents.CUSTOM_DATA);
            if (custom != null) {
                CompoundTag root = custom.copyTag();
                if (root.contains("SteamboxData")) {
                    ListTag inputList = root.getCompound("SteamboxData").getList("Input", 10);
                    return inputList.size() > 0;
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    public static List<SteamboxPackedEntry> getPackedContents(ItemStack stack) {
        List<SteamboxPackedEntry> entries = new ArrayList<>();
        try {
            CustomData custom = stack.get(DataComponents.CUSTOM_DATA);
            if (custom != null) {
                CompoundTag root = custom.copyTag();
                if (root.contains("SteamboxData")) {
                    ListTag inputList = root.getCompound("SteamboxData").getList("Input", 10);
                    for (int i = 0; i < inputList.size(); i++) {
                        CompoundTag itemTag = inputList.getCompound(i);
                        String id = itemTag.getString("id");
                        int count = itemTag.getInt("count");
                        if (count == 0) count = 1;
                        if (!id.isEmpty()) {
                            entries.add(new SteamboxPackedEntry(id, count));
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return entries;
    }
}
