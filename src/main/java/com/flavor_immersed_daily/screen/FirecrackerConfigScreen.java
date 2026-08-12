package com.flavor_immersed_daily.screen;

import com.flavor_immersed_daily.item.FirecrackerHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class FirecrackerConfigScreen extends Screen {

    private final ItemStack stack;
    private int shape;
    private int color;
    private int fadeColor;

    private int guiLeft, guiTop;
    private static final int GUI_WIDTH = 200;
    private static final int GUI_HEIGHT = 160;

    public FirecrackerConfigScreen(ItemStack stack) {
        super(Component.translatable("screen.flavor_immersed_daily.firecracker"));
        this.stack = stack;
        this.shape = FirecrackerHelper.getShape(stack);
        this.color = FirecrackerHelper.getColor(stack);
        this.fadeColor = FirecrackerHelper.getFadeColor(stack);
    }

    @Override
    protected void init() {
        super.init();
        guiLeft = (this.width - GUI_WIDTH) / 2;
        guiTop = (this.height - GUI_HEIGHT) / 2;

        // 形状按钮
        addRenderableWidget(Button.builder(
                Component.translatable("gui.flavor_immersed_daily.firecracker.shape", getShapeName(shape)),
                btn -> {
                    shape = (shape + 1) % 5;
                    btn.setMessage(Component.translatable("gui.flavor_immersed_daily.firecracker.shape", getShapeName(shape)));
                    save();
                }
        ).bounds(guiLeft + 10, guiTop + 10, 180, 20).build());

        // 颜色按钮
        addRenderableWidget(Button.builder(
                Component.translatable("gui.flavor_immersed_daily.firecracker.color", String.format("#%06X", color)),
                btn -> {
                    color = cycleColor(color);
                    btn.setMessage(Component.translatable("gui.flavor_immersed_daily.firecracker.color", String.format("#%06X", color)));
                    save();
                }
        ).bounds(guiLeft + 10, guiTop + 40, 180, 20).build());

        // 渐变色按钮
        addRenderableWidget(Button.builder(
                Component.translatable("gui.flavor_immersed_daily.firecracker.fade_color", String.format("#%06X", fadeColor)),
                btn -> {
                    fadeColor = cycleColor(fadeColor);
                    btn.setMessage(Component.translatable("gui.flavor_immersed_daily.firecracker.fade_color", String.format("#%06X", fadeColor)));
                    save();
                }
        ).bounds(guiLeft + 10, guiTop + 70, 180, 20).build());

        // 关闭按钮
        addRenderableWidget(Button.builder(
                Component.translatable("gui.flavor_immersed_daily.firecracker.done"),
                btn -> this.onClose()
        ).bounds(guiLeft + 10, guiTop + 110, 180, 20).build());
    }

    private String getShapeName(int idx) {
        return Component.translatable("gui.flavor_immersed_daily.firecracker.shape." + idx).getString();
    }

    private int cycleColor(int current) {
        int[] presets = {0xFF0000, 0x00FF00, 0x0000FF, 0xFFFF00, 0xFF00FF, 0x00FFFF, 0xFF8800, 0xFFFFFF, 0xFF69B4};
        for (int i = 0; i < presets.length; i++) {
            if (presets[i] == current) return presets[(i + 1) % presets.length];
        }
        return presets[0];
    }

    private void save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("fw_shape", shape);
        tag.putInt("fw_color", color);
        tag.putInt("fw_fade_color", fadeColor);
        FirecrackerHelper.putData(stack, tag);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.drawString(this.font, this.title, guiLeft + 10, guiTop - 10, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}