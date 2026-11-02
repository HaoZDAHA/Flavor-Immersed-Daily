package com.fidtest.client;

import com.fidtest.ExampleMod;
import com.fidtest.item.DishItem;
import com.fidtest.network.DishOpenPayload;
import com.fidtest.screen.DishNamingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = ExampleMod.MODID, value = Dist.CLIENT)
public class DishItemClientHelper {

// 潜行右键菜品时在客户端直接拦截「使用物品」输入，避免同时发送使用包导致服务端开始食用
    @SubscribeEvent
    public static void onInteractionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event) {
        if (!event.isUseItem()) return;
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || player.isSpectator() || !player.isShiftKeyDown()) return;
        ItemStack held = player.getItemInHand(event.getHand());
        if (!(held.getItem() instanceof DishItem)) return;
        event.setCanceled(true);
        event.setSwingHand(false);
        openNamingScreen(player, held);
    }

    public static void openNamingScreen(Player player, ItemStack held) {
        String currentName = DishItem.getDishName(held);
        String currentDesc = DishItem.getDishDescription(held);
        int currentColor = DishItem.getNameColor(held);
        int slot = player.getInventory().selected;
        Minecraft.getInstance().setScreen(
                new DishNamingScreen(slot, currentName, currentDesc, currentColor));
        PacketDistributor.sendToServer(new DishOpenPayload());
    }
}
