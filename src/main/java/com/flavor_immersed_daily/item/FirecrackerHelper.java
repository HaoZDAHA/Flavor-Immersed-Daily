package com.flavor_immersed_daily.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class FirecrackerHelper {

    private static final String TAG_SHAPE = "fw_shape";
    private static final String TAG_COLOR = "fw_color";
    private static final String TAG_FADE_COLOR = "fw_fade_color";

    public static final String[] SHAPE_NAMES = {"small_ball", "large_ball", "star", "creeper", "burst"};

    private static CompoundTag getData(ItemStack stack) {
        return stack.getOrCreateTag();
    }

    public static void putData(ItemStack stack, CompoundTag tag) {
        stack.setTag(tag);
    }

    public static int getShape(ItemStack stack) {
        CompoundTag tag = getData(stack);
        return tag.contains(TAG_SHAPE) ? tag.getInt(TAG_SHAPE) : 0;
    }

    public static int getColor(ItemStack stack) {
        CompoundTag tag = getData(stack);
        return tag.contains(TAG_COLOR) ? tag.getInt(TAG_COLOR) : 0xFF0000;
    }

    public static int getFadeColor(ItemStack stack) {
        CompoundTag tag = getData(stack);
        return tag.contains(TAG_FADE_COLOR) ? tag.getInt(TAG_FADE_COLOR) : 0xFFFFFF;
    }

    public static boolean hasConfig(ItemStack stack) {
        return getData(stack).contains(TAG_SHAPE);
    }
}