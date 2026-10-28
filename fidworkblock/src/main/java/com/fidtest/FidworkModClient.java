package com.fidtest;

import com.fidtest.client.ChoppingboardRenderer;
import com.fidtest.client.FryingpanRenderer;
import com.fidtest.client.JarRenderer;
import com.fidtest.client.MeatmincerRenderer;
import com.fidtest.client.SqueezingmachineRenderer;
import com.fidtest.client.SteamboxRenderer;
import com.fidtest.client.TeapotRenderer;
import com.fidtest.client.WokRenderer;
import com.fidtest.client.model.DishModelLoader;
import com.fidtest.registration.ModBlockEntities;
import com.fidtest.registration.ModBlocks;
import com.fidtest.registration.ModMenuTypes;
import com.fidtest.screen.CookingScreen;
import com.fidtest.screen.DishNamingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = FidworkMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FidworkModClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.COOKING.get(), CookingScreen::new);
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.MEATMINCER.get(), RenderType.cutout());
        });
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.CHOPPINGBOARD.get(), ChoppingboardRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FRYINGPAN.get(), FryingpanRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.JAR.get(), JarRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SQUEEZINGMACHINE.get(), SqueezingmachineRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.STEAMBOX.get(), SteamboxRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.TEAPOT.get(), TeapotRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MEATMINCER.get(), MeatmincerRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.WOK.get(), WokRenderer::new);
    }

    @SubscribeEvent
    public static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register("dish", DishModelLoader.INSTANCE);
    }

    public static void openDishNamingScreen(InteractionHand hand) {
        Minecraft.getInstance().setScreen(new DishNamingScreen(hand));
    }
}
