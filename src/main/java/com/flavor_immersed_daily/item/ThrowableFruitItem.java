package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.entity.ThrownFruitEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ThrowableFruitItem extends Item {

    private final String dropItem1;
    private final int dropCount1;
    private final String dropItem2;
    private final int dropCount2;
    private final String dropItem3;
    private final int dropCount3;
    private final float damage;

    public ThrowableFruitItem(Properties properties,
                              String dropItem1, int dropCount1,
                              String dropItem2, int dropCount2,
                              String dropItem3, int dropCount3,
                              float damage) {
        super(properties);
        this.dropItem1 = dropItem1;
        this.dropCount1 = dropCount1;
        this.dropItem2 = dropItem2;
        this.dropCount2 = dropCount2;
        this.dropItem3 = dropItem3;
        this.dropCount3 = dropCount3;
        this.damage = damage;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            // 潜行右键：投掷
            if (!level.isClientSide) {
                ThrownFruitEntity entity = new ThrownFruitEntity(
                        level, player, stack,
                        dropItem1, dropCount1,
                        dropItem2, dropCount2,
                        dropItem3, dropCount3,
                        damage
                );
                entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.2f, 1.0f);
                level.addFreshEntity(entity);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        // 非潜行右键：正常食用
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.throwable_fruit.desc1").withStyle(net.minecraft.ChatFormatting.GRAY));
    }
}