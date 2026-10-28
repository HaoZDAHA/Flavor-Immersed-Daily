package com.flavor_immersed_daily.client.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;

// 特色月饼 tooltip 渲染 — 复用馅料与调味料两套渲染：
// 上方渲染"馅料口味"，下方渲染 buff 图标/名称，按住 Shift 展开 buff 描述。
public class ClientMooncakeTooltip implements ClientTooltipComponent {

    private final ClientFillingTooltip fillingTooltip;
    private final ClientSeasoningTooltip effectTooltip;
    private final int fillingHeight;

    public ClientMooncakeTooltip(MooncakeTooltip tooltip) {
        this.fillingTooltip = tooltip.filling().isEmpty()
                ? null
                : new ClientFillingTooltip(new FillingTooltip(tooltip.filling()));
        this.effectTooltip = new ClientSeasoningTooltip(new SeasoningTooltip(tooltip.effects()));
        this.fillingHeight = this.fillingTooltip == null ? 0 : this.fillingTooltip.getHeight();
    }

    @Override
    public int getHeight() {
        return this.fillingHeight + this.effectTooltip.getHeight();
    }

    @Override
    public int getWidth(Font font) {
        int width = this.effectTooltip.getWidth(font);
        if (this.fillingTooltip != null) {
            width = Math.max(width, this.fillingTooltip.getWidth(font));
        }
        return width;
    }

    @Override
    public void renderImage(Font font, int mouseX, int mouseY, GuiGraphics guiGraphics) {
        if (this.fillingTooltip != null) {
            this.fillingTooltip.renderImage(font, mouseX, mouseY, guiGraphics);
        }
        this.effectTooltip.renderImage(font, mouseX, mouseY + this.fillingHeight, guiGraphics);
    }

    @Override
    public void renderText(Font font, int mouseX, int mouseY, Matrix4f matrix4f,
                           MultiBufferSource.BufferSource bufferSource) {
        if (this.fillingTooltip != null) {
            this.fillingTooltip.renderText(font, mouseX, mouseY, matrix4f, bufferSource);
        }
        this.effectTooltip.renderText(font, mouseX, mouseY + this.fillingHeight, matrix4f, bufferSource);
    }
}
