package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.all.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ChineseLeavesItem extends Item {

    public ChineseLeavesItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isShiftKeyDown()) {
            return super.use(level, player, hand);
        }

        if (!level.isClientSide) {
            stack.shrink(1);
            ItemStack result = new ItemStack(ModItems.CUT_CHINESE_CABBAGE.get(), 3);
            if (!player.getInventory().add(result)) {
                player.drop(result, false);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
