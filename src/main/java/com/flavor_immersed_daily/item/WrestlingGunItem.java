package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.client.ClientGuiHelper;
import com.flavor_immersed_daily.entity.FirecrackerEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WrestlingGunItem extends Item {

    public WrestlingGunItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            if (level.isClientSide) {
                ClientGuiHelper.openFirecrackerConfig(stack);
            }
            return InteractionResultHolder.pass(stack);
        }

        if (!level.isClientSide) {
            FirecrackerEntity entity = new FirecrackerEntity(level, player, stack);
            entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.5f, 1.0f);
            level.addFreshEntity(entity);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
