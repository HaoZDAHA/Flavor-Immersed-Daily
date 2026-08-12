package com.flavor_immersed_daily;

import com.flavor_immersed_daily.client.ClientGuiHelper;
import com.flavor_immersed_daily.entity.FirecrackerEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class ItemInteractionHandler {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        // 大白菜处理
        if (stack.is(FlavorImmersedDaily.CHINESE_LEAVES.get()) && player.isShiftKeyDown()) {
            if (!player.level().isClientSide) {
                stack.shrink(1);
                ItemStack result = new ItemStack(FlavorImmersedDaily.CUT_CHINESE_CABBAGE.get(), 3);
                if (!player.getInventory().add(result)) {
                    player.drop(result, false);
                }
            }
            event.setCanceled(true);
            return;
        }

        // 摔炮处理
        if (stack.is(FlavorImmersedDaily.WRESTLING_GUN.get())) {
            if (player.isShiftKeyDown()) {
                // 潜行右键：打开配置界面（仅客户端）
                if (player.level().isClientSide) {
                    ClientGuiHelper.openFirecrackerConfig(stack);
                }
                event.setCanceled(true);
                return;
            }

            // 非潜行右键：投掷摔炮
            if (!player.level().isClientSide) {
                FirecrackerEntity entity = new FirecrackerEntity(player.level(), player, stack);
                entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.5f, 1.0f);
                player.level().addFreshEntity(entity);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            event.setCanceled(true);
        }
    }
}