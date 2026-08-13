package com.flavor_immersed_daily.event;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEntities;
import com.flavor_immersed_daily.client.BighookRenderer;
import com.flavor_immersed_daily.client.tooltip.ClientSeasoningTooltip;
import com.flavor_immersed_daily.client.tooltip.ClientHarvestTooltip;
import com.flavor_immersed_daily.client.tooltip.SeasoningTooltip;
import com.flavor_immersed_daily.client.tooltip.HarvestTooltip;
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
        event.registerBlockEntityRenderer(com.flavor_immersed_daily.all.ModBlockEntities.BIGHOOK_BE.get(), BighookRenderer::new);
    }

    @SubscribeEvent
    static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.FIRECRACKER_ENTITY.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.THROWN_FRUIT_ENTITY.get(), ThrownItemRenderer::new);
    }
}
