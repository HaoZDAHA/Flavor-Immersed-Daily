package com.fidtest.client;

import com.fidtest.block.entity.JarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = "fidworkblock", value = Dist.CLIENT)
public class JarHUDHandler {

    @SubscribeEvent
    public static void onRenderHUD(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        if (mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.BLOCK) return;

        BlockHitResult hit = (BlockHitResult) mc.hitResult;
        if (!(mc.level.getBlockEntity(hit.getBlockPos()) instanceof JarBlockEntity jar)) return;

        int startX = mc.getWindow().getGuiScaledWidth() / 2 + 10;
        int startY = mc.getWindow().getGuiScaledHeight() / 2 - 32;
        GuiGraphics gui = event.getGuiGraphics();
        int yOff = 0;

        if (!jar.isWorking() && !jar.isProcessingDone() && !jar.isSealed()) {
            for (int i = 0; i < 4; i++) {
                ItemStack s = jar.getInputStack(i);
                if (!s.isEmpty()) { gui.renderItem(s, startX, startY + yOff, 0); gui.renderItemDecorations(mc.font, s, startX, startY + yOff); yOff += 20; }
            }
        }

        if (jar.isWorking() || jar.isProcessingDone()) {
            for (ItemStack s : jar.getRenderInputItems())
                if (!s.isEmpty()) { gui.renderItem(s, startX, startY + yOff, 0); yOff += 20; }
            if (jar.isWorking()) { gui.drawString(mc.font, "§e加工中...", startX, startY + yOff, 0xFFFFFF); yOff += 12; }
            if (jar.isProcessingDone() || jar.hasOutput()) {
                ItemStack out = jar.getOutputItem();
                if (!out.isEmpty()) { gui.renderItem(out, startX, startY + yOff, 0); gui.renderItemDecorations(mc.font, out, startX, startY + yOff); }
            }
        }

        if (jar.isSealed() && !jar.isWorking() && !jar.isProcessingDone()) {
            for (int i = 0; i < 4; i++) {
                ItemStack s = jar.getInputStack(i);
                if (!s.isEmpty()) { gui.renderItem(s, startX, startY + yOff, 0); gui.renderItemDecorations(mc.font, s, startX, startY + yOff); yOff += 20; }
            }
            ItemStack out = jar.getOutputItem();
            if (!out.isEmpty()) { gui.renderItem(out, startX, startY + yOff, 0); gui.renderItemDecorations(mc.font, out, startX, startY + yOff); }
            gui.drawString(mc.font, "§a密封中", startX + 20, startY - 12, 0xFFFFFF);
        }
    }
}
