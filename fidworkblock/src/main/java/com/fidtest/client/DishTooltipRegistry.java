package com.fidtest.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "fidworkblock", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DishTooltipRegistry {

    @SubscribeEvent
    public static void registerComponent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(DishIngredientsTooltip.Data.class, DishIngredientsTooltip.Client::new);
    }
}
