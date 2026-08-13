package com.flavor_immersed_daily.event;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemNameBlockItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public final class SeedTooltipHandler {
    private SeedTooltipHandler() {}

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof ItemNameBlockItem) {
            event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.seed"));
        }
    }
}
