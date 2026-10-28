package com.flavor_immersed_daily.client;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.all.ModEntities;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.all.ModMenus;
import com.flavor_immersed_daily.screen.EggBreakingMachineScreen;
import com.flavor_immersed_daily.screen.FridgeScreen;
import com.flavor_immersed_daily.screen.MooncakeMoldScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = FlavorImmersedDaily.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FlavorImmersedDailyClient {

    @SubscribeEvent
    static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        for (int i = 0; i < CookedWholeSheepRenderer.LAYER_LOCATIONS.length; i++) {
            event.registerLayerDefinition(CookedWholeSheepRenderer.LAYER_LOCATIONS[i],
                    CookedWholeSheepRenderer.LAYER_DEFINITIONS[i]);
        }
    }

    @SubscribeEvent
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.FALLING_FRUIT.get(), FallingFruitEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.SEAT_ENTITY.get(), SeatEntityRenderer::new);
        event.registerBlockEntityRenderer(com.flavor_immersed_daily.all.ModBlockEntities.WOODBASIN_ENTITY.get(), WoodBasinBlockEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.WINDOW_PAPER_ENTITY.get(), WindowPaperEntityRenderer::new);
        event.registerBlockEntityRenderer(com.flavor_immersed_daily.all.ModBlockEntities.COUPLET_ENTITY.get(), CoupletRenderer::new);
        event.registerBlockEntityRenderer(com.flavor_immersed_daily.all.ModBlockEntities.RADDLES_ENTITY.get(), RaddlesRenderer::new);
        event.registerBlockEntityRenderer(com.flavor_immersed_daily.all.ModBlockEntities.COOKED_WHOLE_SHEEP_ENTITY.get(), CookedWholeSheepRenderer::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        net.minecraftforge.fml.ModList.get().getModContainerById(FlavorImmersedDaily.MODID).ifPresent(mc -> mc.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parent) ->
                        new Screen(Component.literal("Flavor Immersed Daily")) {
                            @Override
                            public void render(net.minecraft.client.gui.GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                                this.renderBackground(guiGraphics);
                                guiGraphics.drawCenteredString(this.font,
                                        Component.translatable("config.flavor_immersed_daily.placeholder"),
                                        this.width / 2, this.height / 2, 0xFFFFFF);
                                super.render(guiGraphics, mouseX, mouseY, partialTick);
                            }
                        })));

        event.enqueueWork(() -> {
            MenuScreens.register(ModMenus.FRIDGE_MENU.get(), FridgeScreen::new);
            MenuScreens.register(ModMenus.EGG_BREAKING_MACHINE_MENU.get(), EggBreakingMachineScreen::new);
            MenuScreens.register(ModMenus.CROP_CONVERSION_MENU.get(), com.flavor_immersed_daily.screen.CropConversionScreen::new);
            MenuScreens.register(ModMenus.MOONCAKE_MOLD_MENU.get(), MooncakeMoldScreen::new);
            MenuScreens.register(ModMenus.RADDLES_OWNER_MENU.get(), com.flavor_immersed_daily.screen.RaddlesOwnerScreen::new);
        });

        FlavorImmersedDaily.LOGGER.info("MINECRAFT NAME >> {}", net.minecraft.client.Minecraft.getInstance().getUser().getName());

        RenderType cutout = RenderType.cutout();
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BLUEBERRY_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.DRAGONFRUIT_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GREENTEALEAVES_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.HAMIMELON_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PINEAPPLE_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.RED_TEA_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.STRAWBERRY_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.LOTUSROOT_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GLUTINOUSRICE_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PADDY_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GRAPEBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CUCUMBERBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WAXGOURDBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.KIDNEYBEANBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.AUBERGINEBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.TOMATOBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.COWPEABLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GREENGRAEBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.LOOFAHBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BANANA_SAPLING.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.RAWBANANA.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.COCONUT_SAPLING.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.RAWCOCONUT.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.FRIDGE.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.EGGBREAKINGMACHINE.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ANTITHETICAL_COUPLET_1.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ANTITHETICAL_COUPLET_2.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.REDLANTERN.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GOLDLANTERN.get(), cutout);
    }

    @SubscribeEvent
    static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        MooncakeFillingColors.register(event);
    }

// 模型重载完成后：让月饼馅料颜色缓存失效（纹理像素数据已变）
    @SubscribeEvent
    static void onBakingCompleted(ModelEvent.BakingCompleted event) {
        MooncakeFillingColors.onReloaded();
    }
}
