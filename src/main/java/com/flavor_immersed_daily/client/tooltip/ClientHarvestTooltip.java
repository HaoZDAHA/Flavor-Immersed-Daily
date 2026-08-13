package com.flavor_immersed_daily.client.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

/**
 * 收获提示 tooltip 客户端渲染 — 文字 + 一行物品图标（可为空）
 */
public class ClientHarvestTooltip implements ClientTooltipComponent {

    private static final int MARGIN = 4;
    private static final int ICON_SIZE = 18;
    private static final int PADDING = 2;
    private static final int LINE_HEIGHT = 10;

    private final Component text;
    private final List<ItemStack> items;
    private final FormattedCharSequence textLine;

    public ClientHarvestTooltip(HarvestTooltip tooltip) {
        this.text = tooltip.text();
        this.items = tooltip.items();
        this.textLine = this.text.getVisualOrderText();
    }

    @Override
    public int getHeight() {
        int iconHeight = items.isEmpty() ? 0 : MARGIN + ICON_SIZE;
        return MARGIN + LINE_HEIGHT + iconHeight + MARGIN;
    }

    @Override
    public int getWidth(Font font) {
        int textWidth = font.width(textLine);
        int iconRowWidth = items.isEmpty() ? 0 : items.size() * (ICON_SIZE + PADDING) - PADDING;
        return Math.max(textWidth, iconRowWidth) + 2 * MARGIN;
    }

    @Override
    public void renderImage(Font font, int mouseX, int mouseY, GuiGraphics guiGraphics) {
        if (items.isEmpty()) return;
        int totalIconWidth = items.size() * (ICON_SIZE + PADDING) - PADDING;
        int startX = mouseX + MARGIN + Math.max(0, (getWidth(font) - 2 * MARGIN - totalIconWidth) / 2);
        int y = mouseY + MARGIN + LINE_HEIGHT + MARGIN;
        for (int i = 0; i < items.size(); i++) {
            int x = startX + i * (ICON_SIZE + PADDING);
            guiGraphics.renderItem(items.get(i), x, y);
            guiGraphics.renderItemDecorations(font, items.get(i), x, y);
        }
    }

    @Override
    public void renderText(Font font, int mouseX, int mouseY, Matrix4f matrix4f,
                           MultiBufferSource.BufferSource bufferSource) {
        int textX = mouseX + MARGIN;
        int textY = mouseY + MARGIN;
        font.drawInBatch(textLine, textX, textY, -1, false, matrix4f, bufferSource,
                Font.DisplayMode.NORMAL, 0, 15728880);
    }
}