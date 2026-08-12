package com.flavor_immersed_daily;

import com.flavor_immersed_daily.client.BighookRenderer;
import com.flavor_immersed_daily.client.ClientSeasoningTooltip;
import com.flavor_immersed_daily.client.ClientHarvestTooltip;
import com.flavor_immersed_daily.item.SeasoningTooltip;
import com.flavor_immersed_daily.item.HarvestTooltip;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

//客户端TooltipComponent渲染

@EventBusSubscriber(modid = FlavorImmersedDaily.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FlavorImmersedDailyClientModEvents {

    @SubscribeEvent
    static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(SeasoningTooltip.class, ClientSeasoningTooltip::new);
        event.register(HarvestTooltip.class, ClientHarvestTooltip::new);
    }

    @SubscribeEvent
    static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(FlavorImmersedDaily.BIGHOOK_BE.get(), BighookRenderer::new);
    }

    @SubscribeEvent
    static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(FlavorImmersedDaily.FIRECRACKER_ENTITY.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(FlavorImmersedDaily.THROWN_FRUIT_ENTITY.get(), ThrownItemRenderer::new);
    }
}