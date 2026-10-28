package com.flavor_immersed_daily.client;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.all.ModEntities;
import com.flavor_immersed_daily.block.block.decorative.RaddlesBlock;
import com.flavor_immersed_daily.block.blockentity.RaddlesBlockEntity;
import com.flavor_immersed_daily.block.blockentity.WoodBasinBlockEntity;
import com.flavor_immersed_daily.datagen.tag.FIDItemTags;

import com.flavor_immersed_daily.all.ModItems;

import com.flavor_immersed_daily.item.FirecrackerHelper;
import com.flavor_immersed_daily.screen.CropConversionScreen;
import com.flavor_immersed_daily.screen.EggBreakingMachineScreen;
import com.flavor_immersed_daily.screen.FridgeScreen;
import com.flavor_immersed_daily.screen.MooncakeMoldScreen;
import com.flavor_immersed_daily.screen.RaddlesOwnerScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
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
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import com.flavor_immersed_daily.block.block.processing.WoodBasinBlock;

import java.util.List;
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

    private static final Set<String> TRELLIS_SEEDS = Set.of(
            "grapeseed", "cucumberseeds", "wax_gourd_seed_block", "kidneybeanseed",
            "aubergineseedblock", "tomatoseed", "cowpeabeanseed", "greengrapeseed", "loofahseed"
    );

    public FlavorImmersedDailyClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        container.getEventBus().addListener(this::registerRenderers);
        container.getEventBus().addListener(this::registerLayerDefinitions);
        container.getEventBus().addListener(MooncakeFillingColors::register);
        container.getEventBus().addListener(this::onBakingCompleted);
    }

// 模型重载完成后：让月饼馅料颜色缓存失效（纹理像素数据已变）
    private void onBakingCompleted(ModelEvent.BakingCompleted event) {
        MooncakeFillingColors.onReloaded();
    }

    private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        for (int i = 0; i < CookedWholeSheepRenderer.LAYER_LOCATIONS.length; i++) {
            event.registerLayerDefinition(CookedWholeSheepRenderer.LAYER_LOCATIONS[i],
                    CookedWholeSheepRenderer.LAYER_DEFINITIONS[i]);
        }
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
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
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.JADE_LANTERN.get(), cutout);
// 灯谜牌：半透明渲染，贴图中的透明区域不再被填黑
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.RADDLES.get(), RenderType.translucent());
// 石狮子：透明渲染，避免靠墙放置时透视
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.STONE_LION.get(), cutout);
    }

    @SubscribeEvent
    static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(com.flavor_immersed_daily.all.ModMenus.FRIDGE_MENU.get(), FridgeScreen::new);
        event.register(com.flavor_immersed_daily.all.ModMenus.EGG_BREAKING_MACHINE_MENU.get(), EggBreakingMachineScreen::new);
        event.register(com.flavor_immersed_daily.all.ModMenus.CROP_CONVERSION_MENU.get(), CropConversionScreen::new);
        event.register(com.flavor_immersed_daily.all.ModMenus.MOONCAKE_MOLD_MENU.get(), MooncakeMoldScreen::new);
        event.register(com.flavor_immersed_daily.all.ModMenus.RADDLES_OWNER_MENU.get(), RaddlesOwnerScreen::new);
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

// 种子分类 tooltip（只对 fid:seeds 标签内的真种子显示，按种植方式分类）
        if (event.getItemStack().is(FIDItemTags.SEEDS)) {
            String itemId = event.getItemStack().getItemHolder().getKey().location().getPath();
            if (PADDY_SEEDS.contains(itemId)) {
                event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.crop_type.paddy")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            } else if (MUSHROOM_SEEDS.contains(itemId)) {
                event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.crop_type.mushroom")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            } else if (TRELLIS_SEEDS.contains(itemId)) {
                event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.crop_type.trellis")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            } else {
                event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.crop_type.farmland")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }
        }
    }

// 鼠标指向木盆且盆内存有屠宰产物时，屏幕左上显示内容物与数量
    @SubscribeEvent
    static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || !(mc.hitResult instanceof BlockHitResult hit)) {
            return;
        }
        BlockPos hitPos = hit.getBlockPos();
        BlockState state = mc.level.getBlockState(hitPos);

        if (state.getBlock() instanceof WoodBasinBlock) {
            if (mc.level.getBlockEntity(hitPos) instanceof WoodBasinBlockEntity be && be.hasStorage()) {
                renderStorageOverlay(event.getGuiGraphics(), mc, be);
            }
            return;
        }

// 灯谜牌：未设置谜底时提示无谜底，设置好后提示可以右键答题
        if (state.getBlock() instanceof RaddlesBlock
                && mc.level.getBlockEntity(hitPos) instanceof RaddlesBlockEntity be) {
            renderRaddlesHint(event.getGuiGraphics(), mc, be);
        }
    }

// 看向灯谜牌时在屏幕下方显示一行提示
    private static void renderRaddlesHint(GuiGraphics g, Minecraft mc, RaddlesBlockEntity be) {
        Component hint;
        if (be.hasRiddle()) {
            hint = Component.translatable("gui.flavor_immersed_daily.raddles.hint");
        } else if (be.canEdit(mc.player)) {
// 放置者自己看着它：提示可以设置谜底与奖励
            hint = Component.translatable("gui.flavor_immersed_daily.raddles.no_riddle.owner");
        } else {
            hint = Component.translatable("gui.flavor_immersed_daily.raddles.no_riddle");
        }
        int x = (mc.getWindow().getGuiScaledWidth() - mc.font.width(hint)) / 2;
        int y = mc.getWindow().getGuiScaledHeight() - 68;
        g.drawString(mc.font, hint, x, y, 0xFF_FF_FF_55, true);
    }

    private static void renderStorageOverlay(GuiGraphics g, Minecraft mc, WoodBasinBlockEntity be) {
        List<ItemStack> stacks = be.getStorageStacks();
        if (stacks.isEmpty()) return;

        int x = 8;
        int y = 8;
        g.drawString(mc.font,
                Component.translatable("tooltip.flavor_immersed_daily.basin_contents"), x, y, 0xFFFFFF);
        y += 12;

        for (ItemStack s : stacks) {
            g.renderFakeItem(s, x, y);
            g.renderItemDecorations(mc.font, s, x, y);
            String line = s.getHoverName().getString() + "  x" + s.getCount();
            g.drawString(mc.font, Component.literal(line), x + 22, y + 5, 0xFFFFFF);
            y += 20;
        }
    }
}
