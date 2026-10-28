package com.fidtest.client;

import com.fidtest.item.DishItem;
import com.fidtest.network.DishOpenPayload;
import com.fidtest.screen.DishNamingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class DishItemClientHelper {

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
