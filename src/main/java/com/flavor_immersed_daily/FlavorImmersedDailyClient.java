package com.flavor_immersed_daily;

import com.flavor_immersed_daily.client.FallingFruitEntityRenderer;
import com.flavor_immersed_daily.client.SeatEntityRenderer;
import com.flavor_immersed_daily.client.WoodBasinBlockEntityRenderer;
import com.flavor_immersed_daily.client.WindowPaperEntityRenderer;
import com.flavor_immersed_daily.client.CoupletRenderer;
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

    // 水田作物种子（糯米、大米、莲藕）
    private static final Set<String> PADDY_SEEDS = Set.of(
            "lotusrootseed", "glutinousseeds", "paddyseeds"
    );

    // 菌菇作物种子（白蘑菇、木耳、银耳、杏鲍菇、金针菇、香菇）
    private static final Set<String> MUSHROOM_SEEDS = Set.of(
            "white_mushroom_seed", "blackfungsseed", "pleurotusseed",
            "enokimushroomseed", "tremellaseed", "fragrantseed"
    );

    // 所有种子（耕地 + 水田 + 菌菇 + 爬架）
    private static final Set<String> ALL_SEEDS = Set.of(
            // 水田作物
            "lotusrootseed", "glutinousseeds", "paddyseeds",
            // 菌菇作物
            "white_mushroom_seed", "blackfungsseed", "pleurotusseed",
            "enokimushroomseed", "tremellaseed", "fragrantseed",
            // 耕地作物 - FIDCropBlock
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
            // 爬架作物种子
            "grapeseed", "cucumberseeds", "wax_gourd_seed_block", "kidneybeanseed",
            "aubergineseedblock", "tomatoseed", "cowpeabeanseed", "greengrapeseed", "loofahseed"
    );

    public FlavorImmersedDailyClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        container.getEventBus().addListener(this::registerRenderers);
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(FlavorImmersedDaily.FALLING_FRUIT.get(), FallingFruitEntityRenderer::new);
        event.registerEntityRenderer(FlavorImmersedDaily.SEAT_ENTITY.get(), SeatEntityRenderer::new);
        event.registerBlockEntityRenderer(FlavorImmersedDaily.WOODBASIN_ENTITY.get(), WoodBasinBlockEntityRenderer::new);
        event.registerEntityRenderer(FlavorImmersedDaily.WINDOW_PAPER_ENTITY.get(), WindowPaperEntityRenderer::new);
        event.registerBlockEntityRenderer(FlavorImmersedDaily.COUPLET_ENTITY.get(), CoupletRenderer::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        FlavorImmersedDaily.LOGGER.info("烟火凡人心 —— 客户端初始化完成");
        FlavorImmersedDaily.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        // 注册所有作物方块的 cutout 渲染层，确保透明纹理正确显示
        RenderType cutout = RenderType.cutout();
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.BLUEBERRY_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.DRAGONFRUIT_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.GREENTEALEAVES_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.HAMIMELON_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.PINEAPPLE_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.RED_TEA_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.STRAWBERRY_CROP.get(), cutout);
        // 水生作物
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.LOTUSROOT_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.GLUTINOUSRICE_CROP.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.PADDY_CROP.get(), cutout);
        // 爬架作物
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.GRAPEBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.CUCUMBERBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.WAXGOURDBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.KIDNEYBEANBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.AUBERGINEBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.TOMATOBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.COWPEABLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.GREENGRAEBLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.LOOFAHBLOCK.get(), cutout);
        // 香蕉树
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.BANANA_SAPLING.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.RAWBANANA.get(), cutout);
        // 椰子树
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.COCONUT_SAPLING.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.RAWCOCONUT.get(), cutout);
        // 冰箱
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.FRIDGE.get(), cutout);
        // 打蛋机
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.EGGBREAKINGMACHINE.get(), cutout);
        // 对联
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.ANTITHETICAL_COUPLET_1.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.ANTITHETICAL_COUPLET_2.get(), cutout);
        // 灯笼（镂空纹理）
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.REDLANTERN.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(FlavorImmersedDaily.GOLDLANTERN.get(), cutout);
    }

    @SubscribeEvent
    static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(FlavorImmersedDaily.FRIDGE_MENU.get(), FridgeScreen::new);
        event.register(FlavorImmersedDaily.EGG_BREAKING_MACHINE_MENU.get(), EggBreakingMachineScreen::new);
    }

    @SubscribeEvent
    static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().is(FlavorImmersedDaily.RADISH_TAG)) {
            MobEffectInstance instance = new MobEffectInstance(FlavorImmersedDaily.FLATULENCE, 600, 0);
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
        // 调味料：橙色斜体提示
        if (event.getItemStack().is(FlavorImmersedDaily.SEASONING_TAG)) {
            event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.seasoning")
                    .withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
        }
        // 榴莲/椰子产物 tooltip
        ItemStack tipStack = event.getItemStack();
        if (tipStack.is(FlavorImmersedDaily.COCONUTMEAT.get())) {
            event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.coconut_meat.desc").withStyle(ChatFormatting.GRAY));
        } else if (tipStack.is(FlavorImmersedDaily.COCONUT_SHELL.get())) {
            event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.coconut_shell.desc").withStyle(ChatFormatting.GRAY));
        } else if (tipStack.is(FlavorImmersedDaily.DURIANMEAT.get())) {
            event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.durian_meat.desc").withStyle(ChatFormatting.GRAY));
        } else if (tipStack.is(FlavorImmersedDaily.DURIANSHELLHAT.get())) {
            event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.durian_shell.desc").withStyle(ChatFormatting.GRAY));
        }

        // 摔炮 tooltip
        if (event.getItemStack().is(FlavorImmersedDaily.WRESTLING_GUN.get())) {
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

        // 种子作物类型：灰色斜体提示
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
