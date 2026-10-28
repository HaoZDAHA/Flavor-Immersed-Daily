package com.fidtest.client;

import com.fidtest.block.entity.JarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

import java.util.List;

@EventBusSubscriber(modid = "fidworkblock", value = Dist.CLIENT)
public class JarHUDHandler {

    @SubscribeEvent
    public static void onRenderHUD(RenderGuiLayerEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        if (mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.BLOCK) return;

        BlockHitResult blockHit = (BlockHitResult) mc.hitResult;
        if (!(mc.level.getBlockEntity(blockHit.getBlockPos()) instanceof JarBlockEntity jar)) return;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int startX = screenWidth / 2 + 10;
        int startY = screenHeight / 2 - 32;

        GuiGraphics gui = event.getGuiGraphics();
        int yOffset = 0;
        boolean hasItems = false;

        if (!jar.isWorking() && !jar.isProcessingDone() && !jar.isSealed()) {
            for (int i = 0; i < 4; i++) {
                ItemStack stack = jar.getInputStack(i);
                if (!stack.isEmpty()) {
                    hasItems = true;
                    gui.renderItem(stack, startX, startY + yOffset, 0);
                    gui.renderItemDecorations(mc.font, stack, startX, startY + yOffset);
                    yOffset += 20;
                }
            }
        }

        if (jar.isWorking() || jar.isProcessingDone()) {
            List<ItemStack> renderItems = jar.getRenderInputItems();
            for (ItemStack stack : renderItems) {
                if (!stack.isEmpty()) {
                    hasItems = true;
                    gui.renderItem(stack, startX, startY + yOffset, 0);
                    gui.renderItemDecorations(mc.font, stack, startX, startY + yOffset);
                    yOffset += 20;
                }
            }

            if (jar.isWorking()) {
                gui.drawString(mc.font, "\u00a7e\u52a0\u5de5\u4e2d...", startX, startY + yOffset, 0xFFFFFF);
                yOffset += 12;
            }

            if (jar.isProcessingDone() || jar.hasOutput()) {
                ItemStack output = jar.getOutputItem();
                if (!output.isEmpty()) {
                    gui.renderItem(output, startX, startY + yOffset, 0);
                    gui.renderItemDecorations(mc.font, output, startX, startY + yOffset);
                    yOffset += 20;
                }
            }
        }

        if (jar.isSealed() && !jar.isWorking() && !jar.isProcessingDone()) {
            for (int i = 0; i < 4; i++) {
                ItemStack stack = jar.getInputStack(i);
                if (!stack.isEmpty()) {
                    hasItems = true;
                    gui.renderItem(stack, startX, startY + yOffset, 0);
                    gui.renderItemDecorations(mc.font, stack, startX, startY + yOffset);
                    yOffset += 20;
                }
            }
            if (jar.hasOutput()) {
                ItemStack output = jar.getOutputItem();
                if (!output.isEmpty()) {
                    gui.renderItem(output, startX, startY + yOffset, 0);
                    gui.renderItemDecorations(mc.font, output, startX, startY + yOffset);
                    yOffset += 20;
                }
            }
        }

        if (!hasItems && yOffset == 0) return;

        if (jar.isSealed()) {
            gui.drawString(mc.font, "\u00a7a\u5bc6\u5c01\u4e2d", startX + 20, startY - 12, 0xFFFFFF);
        }
    }
}
