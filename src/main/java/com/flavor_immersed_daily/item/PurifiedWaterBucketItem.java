package com.flavor_immersed_daily.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.BlockHitResult;
import java.util.List;

public class PurifiedWaterBucketItem extends Item {
    public static final int MAX_DURABILITY = 256;

    public PurifiedWaterBucketItem(Properties properties) {
        super(properties.durability(MAX_DURABILITY));  // 设置耐久
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getDamageValue() > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F - (float) stack.getDamageValue() * 13.0F / (float) getMaxDamage(stack));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float f = Math.max(0.0F, (float) (MAX_DURABILITY - stack.getDamageValue()) / (float) MAX_DURABILITY);
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairInput) {
        return false; // 不允许修复
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false; // 不可附魔
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true; // 设置为可损坏
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        // 获取玩家准星指向的位置
        BlockHitResult result = getPlayerPOVHitResult(world, player, net.minecraft.world.level.ClipContext.Fluid.SOURCE_ONLY);

        if (result.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
            BlockPos pos = result.getBlockPos();
            BlockState state = world.getBlockState(pos);

            // 检查是否是水源
            if (state.getBlock() == Blocks.WATER) {
                if (!world.isClientSide) {
                    // 检查是否还有耐久
                    if (stack.getDamageValue() < stack.getMaxDamage()) {
                        // 消耗一个耐久
                        stack.hurtAndBreak(1, player, net.minecraft.world.entity.EquipmentSlot.MAINHAND);

                        // 给玩家纯净水
                        ItemStack drinkingWater = new ItemStack(com.flavor_immersed_daily.FlavorImmersedDaily.TIDYWATER.get());
                        if (!player.getInventory().add(drinkingWater)) {
                            // 如果背包满了，掉落在玩家脚下
                            player.drop(drinkingWater, false);
                        }

                        // 播放净化声音
                        world.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS, 1.0F, 1.0F);
                    } else {
                        // 耐久已用完，播放损坏声音
                        world.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                }
                return InteractionResultHolder.success(stack);
            }
        }

        // 如果没有对着水，就正常处理
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.purified_water_bucket").withStyle(ChatFormatting.GRAY));
    }
}
