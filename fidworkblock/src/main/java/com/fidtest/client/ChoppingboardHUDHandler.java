package com.fidtest.client;

import com.fidtest.FidworkMod;
import com.fidtest.block.entity.ChoppingboardBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// 砧板工序提示 HUD：当玩家看向砧板时，在准星旁渲染当前需要的工具/材料图标。
// 生馅饼在砧板 → 月饼模具；芦苇叶 + 馅料 → 糯米。
@Mod.EventBusSubscriber(modid = FidworkMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ChoppingboardHUDHandler {

    @SubscribeEvent
    public static void onRenderHUD(RenderGuiOverlayEvent.Post event) {
// 只在准星图层绘制，避免每层 HUD 都重复渲染
        if (!event.getOverlay().id().equals(VanillaGuiOverlay.CROSSHAIR.id())) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        if (mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.BLOCK) return;

        BlockHitResult blockHit = (BlockHitResult) mc.hitResult;
        if (!(mc.level.getBlockEntity(blockHit.getBlockPos()) instanceof ChoppingboardBlockEntity board)) return;

        ItemStack hint = board.getHintItem();
        if (hint.isEmpty()) return;

        int startX = mc.getWindow().getGuiScaledWidth() / 2 + 10;
        int startY = mc.getWindow().getGuiScaledHeight() / 2 - 32;

        GuiGraphics gui = event.getGuiGraphics();
        gui.renderItem(hint, startX, startY);
        gui.renderItemDecorations(mc.font, hint, startX, startY);

        String textKey;
        if (board.canMakeMooncake()) {
            textKey = "hud.fidworkblock.choppingboard_use_mold";
        } else if (board.canMakeZongzi()) {
            textKey = "hud.fidworkblock.choppingboard_put_rice";
        } else {
            return;
        }
        gui.drawString(mc.font, Component.translatable(textKey), startX + 20, startY + 5, 0xFFFFFF);
    }
}
