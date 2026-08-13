package com.flavor_immersed_daily.client;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.all.ModEntities;
import com.flavor_immersed_daily.datagen.tag.FIDItemTags;

import com.flavor_immersed_daily.all.ModItems;

import com.flavor_immersed_daily.item.FirecrackerHelper;
import com.flavor_immersed_daily.screen.EggBreakingMachineScreen;
import com.flavor_immersed_daily.screen.FridgeScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.Set;

@Mod(value = FlavorImmersedDaily.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID, value = Dist.CLIENT)
public class FlavorImmersedDailyClient {

    private static final Set<String> PADDY_SEEDS = Set.of(
            "lotusrootseed", "glutinousseeds", "paddyseeds"
    );

    private static final Set<String> MUSHROOM_SEEDS = Set.of(
            "white_mushroom_seed", "blackfungsseed", "pleurotusseed",
            "enokimushroomseed", "tremellaseed", "fragrantseed"
    );

    private static final Set<String> ALL_SEEDS = Set.of(
            "lotusrootseed", "glutinousseeds", "paddyseeds",

            "white_mushroom_seed", "blackfungsseed", "pleurotusseed",
            "enokimushroomseed", "tremellaseed", "fragrantseed",
            "chineseleavesseed", "aniseed_0", "radishseed",
            "kao_liang_seed", "blueberryseed", "dragonfruitseed", "greentealeavesseed",
            "hamimelonseed", "pineappleseed", "red_tea_seed", "strawberryseed",
            "broccoilseed", "buckwheatseed", "cabbageseed", "cassavaseeds",
            "celeryseed", "chinesechivesseed", "chineseyamseed", "coffeebeanseed",
            "cornseed", "cuminseed", "fennelseedstates", "garlicseed",
            "ginger_seed", "greenpepperseeds", "gumboseed", "millet",
            "mungbeanplant", "mustrad_seed", "nutmegseed", "oatseed",
            "oilrapeseed", "onionseed", "peaseed", "puplesweetpotatoseed",
            "sesameseed", "soy_bean_seed", "sweetgreenpepperseed", "zucchiniseed",
            "spinach_seed", "cauliflower_seed", "scallion_seed", "lilac_seed",
            "red_bean_block", "red_pepper_seed", "sweet_potato_seed",
            "si_chuan_pepper_seed", "pea_nut_seed",
            "grapeseed", "cucumberseeds", "wax_gourd_seed_block", "kidneybeanseed",
            "aubergineseedblock", "tomatoseed", "cowpeabeanseed", "greengrapeseed", "loofahseed"
    );

    public FlavorImmersedDailyClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        container.getEventBus().addListener(this::registerRenderers);
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.FALLING_FRUIT.get(), FallingFruitEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.SEAT_ENTITY.get(), SeatEntityRenderer::new);
        event.registerBlockEntityRenderer(com.flavor_immersed_daily.all.ModBlockEntities.WOODBASIN_ENTITY.get(), WoodBasinBlockEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.WINDOW_PAPER_ENTITY.get(), WindowPaperEntityRenderer::new);
        event.registerBlockEntityRenderer(com.flavor_immersed_daily.all.ModBlockEntities.COUPLET_ENTITY.get(), CoupletRenderer::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        FlavorImmersedDaily.LOGGER.info("???????????????????????");
        FlavorImmersedDaily.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        RenderType cutout = RenderType.cutout();
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BLUEBERRY_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.DRAGONFRUIT_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GREENTEALEAVES_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.HAMIMELON_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PINEAPPLE_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.RED_TEA_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.STRAWBERRY_CROP.get(), cutout);
        // ???????
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.LOTUSROOT_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GLUTINOUSRICE_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PADDY_CROP.get(), cutout);
        // ????????
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GRAPEBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CUCUMBERBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WAXGOURDBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.KIDNEYBEANBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.AUBERGINEBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.TOMATOBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.COWPEABLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GREENGRAEBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.LOOFAHBLOCK.get(), cutout);
        // ????
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BANANA_SAPLING.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.RAWBANANA.get(), cutout);
        // ?????
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.COCONUT_SAPLING.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.RAWCOCONUT.get(), cutout);
        // ????
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.FRIDGE.get(), cutout);
        // ????
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.EGGBREAKINGMACHINE.get(), cutout);
        // ????
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ANTITHETICAL_COUPLET_1.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ANTITHETICAL_COUPLET_2.get(), cutout);
        // ???????慰??????
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.REDLANTERN.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GOLDLANTERN.get(), cutout);
    }

    @SubscribeEvent
    static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(com.flavor_immersed_daily.all.ModMenus.FRIDGE_MENU.get(), FridgeScreen::new);
        event.register(com.flavor_immersed_daily.all.ModMenus.EGG_BREAKING_MACHINE_MENU.get(), EggBreakingMachineScreen::new);
    }

    @SubscribeEvent
    static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().is(FIDItemTags.RADISH)) {
            MobEffectInstance instance = new MobEffectInstance(ModEffects.FLATULENCE, 600, 0);
            Component name = Component.translatable(instance.getDescriptionId());
            if (instance.getAmplifier() > 0) {
                name = Component.translatable("potion.withAmplifier", name,
                        Component.translatable("potion.potency." + instance.getAmplifier()));
            }
            int s = instance.getDuration() / 20;
            Component line = name.copy()
                    .append(" (" + s / 60 + ":" + String.format("%02d", s % 60) + ")")
                    .withStyle(ChatFormatting.BLUE);
            event.getToolTip().add(line);
        }
        // ??味??????斜?????
        if (event.getItemStack().is(FIDItemTags.SEASONING)) {
            event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.seasoning")
                    .withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
        }
        // ????/?????? tooltip
        ItemStack tipStack = event.getItemStack();
        if (tipStack.is(ModItems.COCONUTMEAT.get())) {
            event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.coconut_meat.desc").withStyle(ChatFormatting.GRAY));
        } else if (tipStack.is(ModItems.COCONUT_SHELL.get())) {
            event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.coconut_shell.desc").withStyle(ChatFormatting.GRAY));
        } else if (tipStack.is(ModItems.DURIANMEAT.get())) {
            event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.durian_meat.desc").withStyle(ChatFormatting.GRAY));
        } else if (tipStack.is(ModItems.DURIANSHELLHAT.get())) {
            event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.durian_shell.desc").withStyle(ChatFormatting.GRAY));
        }

        // ??? tooltip
        if (event.getItemStack().is(ModItems.WRESTLING_GUN.get())) {
            ItemStack stack = event.getItemStack();
            if (FirecrackerHelper.hasConfig(stack)) {
                int shape = FirecrackerHelper.getShape(stack);
                String shapeName = shape >= 0 && shape < FirecrackerHelper.SHAPE_NAMES.length ? FirecrackerHelper.SHAPE_NAMES[shape] : "unknown";
                event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.firecracker.shape", shapeName).withStyle(ChatFormatting.GRAY));
                int color = FirecrackerHelper.getColor(stack);
                int fadeColor = FirecrackerHelper.getFadeColor(stack);
                event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.firecracker.color", String.format("#%06X", color & 0xFFFFFF)).withStyle(ChatFormatting.GRAY));
                event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.firecracker.fade_color", String.format("#%06X", fadeColor & 0xFFFFFF)).withStyle(ChatFormatting.GRAY));
            } else {
                event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.firecracker.desc1").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
                event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.firecracker.desc2").withStyle(ChatFormatting.GRAY));
            }
        }

        // ????????????????斜??????
        String itemId = event.getItemStack().getItemHolder().getKey().location().getPath();
        if (ALL_SEEDS.contains(itemId)) {
            if (PADDY_SEEDS.contains(itemId)) {
                event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.crop_type.paddy")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            } else if (MUSHROOM_SEEDS.contains(itemId)) {
                event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.crop_type.mushroom")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            } else {
                event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.crop_type.farmland")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }
        }
    }
}
