package com.flavor_immersed_daily.client;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.block.block.processing.WoodBasinBlock;
import com.flavor_immersed_daily.block.blockentity.WoodBasinBlockEntity;
import com.flavor_immersed_daily.datagen.tag.FIDItemTags;
import com.flavor_immersed_daily.item.FirecrackerHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Set;

// Forge 总线上的客户端事件（tooltip、木盆内容物 HUD）
@Mod.EventBusSubscriber(modid = FlavorImmersedDaily.MODID, value = Dist.CLIENT)
public class ClientForgeEvents {

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

    @SubscribeEvent
    static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().is(FIDItemTags.RADISH)) {
            MobEffectInstance instance = new MobEffectInstance(ModEffects.FLATULENCE.get(), 600, 0);
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
        if (event.getItemStack().is(FIDItemTags.SEASONING)) {
            event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.seasoning")
                    .withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
        }
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
            String itemId = BuiltInRegistries.ITEM.getKey(event.getItemStack().getItem()).getPath();
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
        BlockState state = mc.level.getBlockState(hit.getBlockPos());
        if (!(state.getBlock() instanceof WoodBasinBlock)) {
            return;
        }
        if (mc.level.getBlockEntity(hit.getBlockPos()) instanceof WoodBasinBlockEntity be && be.hasStorage()) {
            renderStorageOverlay(event.getGuiGraphics(), mc, be);
        }
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