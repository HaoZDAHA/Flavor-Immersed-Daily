package com.fidtest.client;

import com.fidtest.item.SteamboxItem;
import com.fidtest.item.SteamboxPackedEntry;
import com.mojang.datafixers.util.Either;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

import java.util.List;

@EventBusSubscriber(modid = "fidworkblock", value = Dist.CLIENT)
public class SteamboxTooltipHandler {

    @SubscribeEvent
    public static void onGatherTooltip(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof SteamboxItem)) return;

        if (SteamboxItem.isPacked(stack)) {
            List<SteamboxPackedEntry> entries = SteamboxItem.getPackedContents(stack);
            if (!entries.isEmpty()) {
                event.getTooltipElements().add(
                        Either.right(new SteamboxContentsTooltip.Data(entries)));
            }
        }
    }
}
