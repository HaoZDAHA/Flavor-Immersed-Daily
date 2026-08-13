package com.flavor_immersed_daily.client.tooltip;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

/**
 * 调味料 tooltip 渲染 — 类似原版药水效果：
 * 渲染 buff 图标 + 效果名 + 时效；按住 Shift 显示 buff 详细描述
 */
public class ClientSeasoningTooltip implements ClientTooltipComponent {

    private static final int MARGIN = 4;
    private static final int ICON_SIZE = 18;
    private static final int PADDING = 2;
    private static final int LINE_HEIGHT = 10;
    private static final int MAX_DESC_WIDTH = 200;

    private final List<MobEffectInstance> effects;
    private final List<FormattedCharSequence> effectLines;
    private final List<FormattedCharSequence> descriptionLines;
    private final FormattedCharSequence holdShiftLine;

    public ClientSeasoningTooltip(SeasoningTooltip tooltip) {
        this.effects = tooltip.effects();
        this.effectLines = new ArrayList<>();
        this.descriptionLines = new ArrayList<>();
        Font font = Minecraft.getInstance().font;
        float tickRate = Minecraft.getInstance().level != null
                ? Minecraft.getInstance().level.tickRateManager().tickrate()
                : 20.0F;
        for (MobEffectInstance instance : this.effects) {
            MutableComponent name = Component.translatable(instance.getDescriptionId());
            if (instance.getAmplifier() > 0) {
                name = Component.translatable("potion.withAmplifier", name,
                        Component.translatable("potion.potency." + instance.getAmplifier()));
            }
            if (!instance.endsWithin(20)) {
                name = Component.translatable("potion.withDuration", name,
                        MobEffectUtil.formatDuration(instance, 1.0F, tickRate));
            }
            name = name.withStyle(instance.getEffect().value().getCategory().getTooltipFormatting());
            this.effectLines.add(name.getVisualOrderText());
            // Shift 详情：效果描述
            Component desc = Component.translatable(instance.getDescriptionId() + ".description")
                    .withStyle(ChatFormatting.GRAY);
            for (FormattedCharSequence line : font.split(desc, MAX_DESC_WIDTH)) {
                this.descriptionLines.add(line);
            }
        }
        // 未按 Shift 时显示提示，引导玩家查看详情
        this.holdShiftLine = this.descriptionLines.isEmpty()
                ? null
                : Component.translatable("tooltip.flavor_immersed_daily.hold_shift")
                        .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
                        .getVisualOrderText();
    }

    @Override
    public int getHeight() {
        int height = ICON_SIZE + 2 * MARGIN;
        if (Screen.hasShiftDown()) {
            if (!this.descriptionLines.isEmpty()) {
                height += MARGIN + this.descriptionLines.size() * LINE_HEIGHT;
            }
        } else if (this.holdShiftLine != null) {
            height += MARGIN + LINE_HEIGHT;
        }
        return height;
    }

    @Override
    public int getWidth(Font font) {
        int textWidth = 0;
        for (FormattedCharSequence line : this.effectLines) {
            textWidth = Math.max(textWidth, font.width(line));
        }
        int result = textWidth + ICON_SIZE + PADDING + 2 * MARGIN;
        if (Screen.hasShiftDown()) {
            for (FormattedCharSequence line : this.descriptionLines) {
                result = Math.max(result, font.width(line) + 2 * MARGIN);
            }
        } else if (this.holdShiftLine != null) {
            result = Math.max(result, font.width(this.holdShiftLine) + 2 * MARGIN);
        }
        return result;
    }

    @Override
    public void renderImage(Font font, int mouseX, int mouseY, GuiGraphics guiGraphics) {
        int x = mouseX + MARGIN;
        int y = mouseY + MARGIN;
        for (MobEffectInstance instance : this.effects) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getMobEffectTextures().get(instance.getEffect());
            guiGraphics.blit(x, y, 0, ICON_SIZE, ICON_SIZE, sprite);
            x += ICON_SIZE + PADDING;
        }
    }

    @Override
    public void renderText(Font font, int mouseX, int mouseY, Matrix4f matrix4f,
                           MultiBufferSource.BufferSource bufferSource) {
        int x = mouseX + MARGIN + ICON_SIZE + PADDING;
        int y = mouseY + MARGIN + (ICON_SIZE - LINE_HEIGHT) / 2;
        for (FormattedCharSequence line : this.effectLines) {
            font.drawInBatch(line, x, y, -1, false, matrix4f, bufferSource,
                    Font.DisplayMode.NORMAL, 0, 15728880);
            y += LINE_HEIGHT;
        }
        if (Screen.hasShiftDown()) {
            if (!this.descriptionLines.isEmpty()) {
                y = mouseY + MARGIN + ICON_SIZE + MARGIN;
                int descX = mouseX + MARGIN;
                for (FormattedCharSequence line : this.descriptionLines) {
                    font.drawInBatch(line, descX, y, -1, false, matrix4f, bufferSource,
                            Font.DisplayMode.NORMAL, 0, 15728880);
                    y += LINE_HEIGHT;
                }
            }
        } else if (this.holdShiftLine != null) {
            int hintY = mouseY + MARGIN + ICON_SIZE + MARGIN;
            font.drawInBatch(this.holdShiftLine, mouseX + MARGIN, hintY, -1, false,
                    matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        }
    }
}
