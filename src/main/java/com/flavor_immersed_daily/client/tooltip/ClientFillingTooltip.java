package com.flavor_immersed_daily.client.tooltip;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

// 馅料口味 tooltip 渲染 — 一行内渲染 "馅料口味：" + 馅料物品图标 + 馅料名称
public class ClientFillingTooltip implements ClientTooltipComponent {

    private static final int MARGIN = 2;
    private static final int ICON_SIZE = 16;
    private static final int PADDING = 2;
    private static final int FONT_HEIGHT = 8;

    private final ItemStack filling;
    private final FormattedCharSequence label;
    private final FormattedCharSequence fillingName;

    public ClientFillingTooltip(FillingTooltip tooltip) {
        this.filling = tooltip.filling();
        this.label = Component.translatable("tooltip.flavor_immersed_daily.filling")
                .withStyle(ChatFormatting.GRAY)
                .getVisualOrderText();
        this.fillingName = this.filling.getHoverName().copy()
                .withStyle(ChatFormatting.WHITE)
                .getVisualOrderText();
    }

    @Override
    public int getHeight() {
        return ICON_SIZE + MARGIN * 2;
    }

    @Override
    public int getWidth(Font font) {
        return MARGIN * 2 + font.width(this.label) + PADDING + ICON_SIZE + PADDING + font.width(this.fillingName);
    }

    @Override
    public void renderImage(Font font, int mouseX, int mouseY, GuiGraphics guiGraphics) {
        int iconX = mouseX + MARGIN + font.width(this.label) + PADDING;
        guiGraphics.renderFakeItem(this.filling, iconX, mouseY + MARGIN);
    }

    @Override
    public void renderText(Font font, int mouseX, int mouseY, Matrix4f matrix4f,
                           MultiBufferSource.BufferSource bufferSource) {
        int textY = mouseY + MARGIN + (ICON_SIZE - FONT_HEIGHT) / 2;
        int labelX = mouseX + MARGIN;
        font.drawInBatch(this.label, labelX, textY, -1, false, matrix4f, bufferSource,
                Font.DisplayMode.NORMAL, 0, 15728880);

        int nameX = labelX + font.width(this.label) + PADDING + ICON_SIZE + PADDING;
        font.drawInBatch(this.fillingName, nameX, textY, -1, false, matrix4f, bufferSource,
                Font.DisplayMode.NORMAL, 0, 15728880);
    }
}