package com.fidtest.client;

import com.fidtest.block.entity.ChoppingboardBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

// 砧板工序提示 HUD：当玩家看向砧板时，在屏幕上渲染当前需要的工具/材料图标。
// 生馅饼在砧板 → 月饼模具；芦苇叶+馅料 → 糯米。
@EventBusSubscriber(modid = "fidworkblock", value = Dist.CLIENT)
public class ChoppingboardHUDHandler {

    @SubscribeEvent
    public static void onRenderHUD(RenderGuiLayerEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        if (mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.BLOCK) return;

        BlockHitResult blockHit = (BlockHitResult) mc.hitResult;
        if (!(mc.level.getBlockEntity(blockHit.getBlockPos()) instanceof ChoppingboardBlockEntity board)) return;

        ItemStack hint = board.getHintItem();
        if (hint.isEmpty()) return;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int startX = screenWidth / 2 + 10;
        int startY = screenHeight / 2 - 32;

        GuiGraphics gui = event.getGuiGraphics();
        gui.renderItem(hint, startX, startY, 0);
        gui.renderItemDecorations(mc.font, hint, startX, startY);

        String text;
        if (board.canMakeMooncake()) {
// 用月饼模具右键
            text = "\u00a7e\u7528\u6708\u997c\u6a21\u5177\u53f3\u952e";
        } else if (board.canMakeZongzi()) {
// 放入糯米
            text = "\u00a7e\u653e\u5165\u7cef\u7c73";
        } else {
            return;
        }
        gui.drawString(mc.font, Component.literal(text), startX + 20, startY + 5, 0xFFFFFF);
    }
}
