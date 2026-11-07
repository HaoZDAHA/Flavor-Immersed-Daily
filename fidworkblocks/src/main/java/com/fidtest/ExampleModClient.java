package com.fidtest;

import com.fidtest.block.MeatmincerBlock;
import com.fidtest.client.ChoppingboardRenderer;
import com.fidtest.client.ChopsticksHandRenderer;
import com.fidtest.client.DishIngredientsTooltip;
import com.fidtest.client.WokRenderer;
import com.fidtest.client.SteamboxRenderer;
import com.fidtest.client.SqueezingmachineRenderer;
import com.fidtest.client.FryingpanRenderer;
import com.fidtest.client.JarRenderer;
import com.fidtest.client.MeatmincerRenderer;
import com.fidtest.client.model.DishModelLoader;
import com.fidtest.registration.ModBlockEntities;
import com.fidtest.registration.ModBlocks;
import com.fidtest.registration.ModMenuTypes;
import com.fidtest.screen.CookingScreen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = ExampleMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ExampleModClient {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.COOKING.get(), CookingScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.PLATED_DISH.get(), com.fidtest.client.PlatedDishRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.TABLEWARE.get(), com.fidtest.client.TablewareRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CHOPPINGBOARD.get(), ChoppingboardRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.WOK.get(), WokRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.STEAMBOX.get(), SteamboxRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SQUEEZINGMACHINE.get(), SqueezingmachineRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FRYINGPAN.get(), FryingpanRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.JAR.get(), JarRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MEATMINCER.get(), MeatmincerRenderer::new);
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(net.minecraft.client.resources.model.ModelResourceLocation.standalone(
                com.fidtest.client.model.DishModelGeometry.PLATE));
        event.register(net.minecraft.client.resources.model.ModelResourceLocation.standalone(
                com.fidtest.client.TablewareRenderer.PLATE));
        event.register(net.minecraft.client.resources.model.ModelResourceLocation.standalone(
                com.fidtest.client.TablewareRenderer.LONG_PLATE));
        event.register(net.minecraft.client.resources.model.ModelResourceLocation.standalone(
                com.fidtest.client.TablewareRenderer.SAUCER));
        event.register(net.minecraft.client.resources.model.ModelResourceLocation.standalone(
                com.fidtest.client.TablewareRenderer.SAUCER_DRY_SEASONING));
        event.register(net.minecraft.client.resources.model.ModelResourceLocation.standalone(
                com.fidtest.client.TablewareRenderer.SAUCER_WET_SEASONING));
        event.register(net.minecraft.client.resources.model.ModelResourceLocation.standalone(ChopsticksHandRenderer.CHOPSTICK_A));
        event.register(net.minecraft.client.resources.model.ModelResourceLocation.standalone(ChopsticksHandRenderer.CHOPSTICK_B));
    }

    @SubscribeEvent
    public static void registerReloadListeners(net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((net.minecraft.server.packs.resources.ResourceManagerReloadListener)
                manager -> com.fidtest.client.DishRenderCache.clear());
    }

    @SubscribeEvent
    public static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register(ResourceLocation.fromNamespaceAndPath("fidworkblock", "dish"), DishModelLoader.INSTANCE);
    }

    @SubscribeEvent
    public static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(DishIngredientsTooltip.Data.class, DishIngredientsTooltip.Client::new);
        event.register(com.fidtest.client.SteamboxContentsTooltip.Data.class,
                com.fidtest.client.SteamboxContentsTooltip.Client::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.addListener(ChopsticksHandRenderer::onRenderHand);
        NeoForge.EVENT_BUS.addListener(ChopsticksHandRenderer::onClientTick);
        NeoForge.EVENT_BUS.addListener(com.fidtest.network.ChopsticksEatPayload.ReceivedEvent.class,
                e -> ChopsticksHandRenderer.startEating(e.slot(), e.bite(), e.food()));

        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CHOPPINGBOARD.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WOK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.STEAMBOX.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SQUEEZINGMACHINE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.FRYINGPAN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.JAR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.MEATMINCER.get(), RenderType.cutout());

        NeoForge.EVENT_BUS.addListener(ItemTooltipEvent.class, e -> {
            if (e.getItemStack().getItem() instanceof BlockItem bi && bi.getBlock() instanceof MeatmincerBlock) {
                e.getToolTip().add(Component.translatable("tooltip.fidworkblock.meatmincer_desc1"));
                e.getToolTip().add(Component.translatable("tooltip.fidworkblock.meatmincer_desc2"));
            }
        });
    }
}
