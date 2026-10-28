package com.fidtest.client;

import com.fidtest.block.entity.TeapotBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "fidworkblock", value = Dist.CLIENT)
public class TeapotHUDHandler {

    @SubscribeEvent
    public static void onRenderHUD(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        if (mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.BLOCK) return;

        BlockHitResult hit = (BlockHitResult) mc.hitResult;
        if (!(mc.level.getBlockEntity(hit.getBlockPos()) instanceof TeapotBlockEntity teapot)) return;

        int startX = mc.getWindow().getGuiScaledWidth() / 2 + 10;
        int startY = mc.getWindow().getGuiScaledHeight() / 2 - 32;
        GuiGraphics gui = event.getGuiGraphics();
        int yOff = 0;

        if (!teapot.isWorking() && !teapot.isProcessingDone() && !teapot.isSealed()) {
            for (int i = 0; i < 4; i++) {
                ItemStack s = teapot.getInputStack(i);
                if (!s.isEmpty()) { gui.renderItem(s, startX, startY + yOff, 0); gui.renderItemDecorations(mc.font, s, startX, startY + yOff); yOff += 20; }
            }
        }
        if (teapot.isWorking() || teapot.isProcessingDone()) {
            for (ItemStack s : teapot.getRenderInputItems())
                if (!s.isEmpty()) { gui.renderItem(s, startX, startY + yOff, 0); yOff += 20; }
            if (teapot.isWorking()) { gui.drawString(mc.font, "§e冲泡中...", startX, startY + yOff, 0xFFFFFF); yOff += 12; }
            if (teapot.isProcessingDone() || teapot.hasOutput()) {
                ItemStack out = teapot.getOutputItem();
                if (!out.isEmpty()) { gui.renderItem(out, startX, startY + yOff, 0); gui.renderItemDecorations(mc.font, out, startX, startY + yOff); }
            }
        }
        if (teapot.isSealed() && !teapot.isWorking() && !teapot.isProcessingDone()) {
            for (int i = 0; i < 4; i++) {
                ItemStack s = teapot.getInputStack(i);
                if (!s.isEmpty()) { gui.renderItem(s, startX, startY + yOff, 0); gui.renderItemDecorations(mc.font, s, startX, startY + yOff); yOff += 20; }
            }
            ItemStack out = teapot.getOutputItem();
            if (!out.isEmpty()) { gui.renderItem(out, startX, startY + yOff, 0); gui.renderItemDecorations(mc.font, out, startX, startY + yOff); }
            gui.drawString(mc.font, "§a加盖中", startX + 20, startY - 12, 0xFFFFFF);
        }
    }
}
