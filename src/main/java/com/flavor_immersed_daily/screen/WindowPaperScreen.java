package com.flavor_immersed_daily.screen;

import com.flavor_immersed_daily.entity.WindowPaperEntity;
import com.flavor_immersed_daily.network.WindowPaperSyncPayload;
import com.flavor_immersed_daily.network.ModMessages;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

// 窗纸像素编辑器 — 16×16 像素自由绘画
// 使用 RGB 滑块选择颜色，支持拖拽连续调节
public class WindowPaperScreen extends Screen {

    private static final int PIXEL_SIZE = 10;
// 160
    private static final int GRID_SIZE = 16 * PIXEL_SIZE;

    private final WindowPaperEntity entity;

// 像素颜色数据 (ARGB)
    private final int[] pixelData = new int[256];

// 当前选中的颜色 (ARGB)
    private int selectedColor = 0xFF000000;

// RGB 分量
    private int red = 0;
    private int green = 0;
    private int blue = 0;

    private int hoverIndex = -1;
    private int gridLeft;
    private int gridTop;
    private boolean erasing = false;

// RGB 滑块布局
    private int sliderX;
    private int sliderY;
    private static final int SLIDER_WIDTH = 100;
    private static final int SLIDER_HEIGHT = 10;
    private static final int SLIDER_GAP = 6;
    private static final int SLIDER_LABEL_W = 12;
// 滑块轨道左边
    private int sliderAreaLeft;

// 拖拽状态
    private boolean draggingSlider = false;
// 0=R, 1=G, 2=B
    private int draggingIndex = -1;

// 颜色预览
    private int previewSize = 40;
    private int previewX;
    private int previewY;

// 按钮
    private Button cutterButton;
    private Button confirmButton;

    public WindowPaperScreen(WindowPaperEntity entity) {
        super(Component.translatable("block.flavor_immersed_daily.windowpaper_1"));
        this.entity = entity;

        int[] data = entity.getPixelData();
        System.arraycopy(data, 0, this.pixelData, 0, Math.min(data.length, 256));
    }

    @Override
    protected void init() {
        super.init();
        this.gridLeft = (this.width - GRID_SIZE) / 2;
        this.gridTop = 30;

// 滑块起始位置：网格右侧 + 间距
        this.sliderX = gridLeft + GRID_SIZE + 12;
        this.sliderY = gridTop + 4;
        this.sliderAreaLeft = sliderX + SLIDER_LABEL_W + 4;

// 颜色预览
        this.previewX = sliderAreaLeft;
        this.previewY = sliderY + 3 * (SLIDER_HEIGHT + SLIDER_GAP) + 6;

// 裁纸器按钮
        int cutterWidth = 80;
        int cutterX = sliderAreaLeft;
        int cutterY = previewY + previewSize + 8;
        this.cutterButton = Button.builder(
                Component.translatable("gui.flavor_immersed_daily.windowpaper.cutter"),
                btn -> erasing = !erasing)
                .bounds(cutterX, cutterY, cutterWidth, 18)
                .build();
        this.addRenderableWidget(cutterButton);

// 确认按钮
        int confirmX = sliderAreaLeft + cutterWidth + 6;
        this.confirmButton = Button.builder(
                Component.translatable("gui.flavor_immersed_daily.fireworks_box.confirm"),
                btn -> {
                    sendSync();
                    this.onClose();
                })
                .bounds(confirmX, cutterY, 60, 18)
                .build();
        this.addRenderableWidget(confirmButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);

// 标题
        guiGraphics.drawString(this.font,
                Component.translatable("block.flavor_immersed_daily.windowpaper_1"),
                (this.width - this.font.width(Component.translatable("block.flavor_immersed_daily.windowpaper_1"))) / 2,
                8, 0xFFFFFF, false);

// 网格边框
        guiGraphics.fill(gridLeft - 1, gridTop - 1,
                gridLeft + GRID_SIZE + 1, gridTop + GRID_SIZE + 1, 0xFF444444);

// 绘制每个像素
        for (int i = 0; i < 256; i++) {
            int px = i % 16;
            int py = i / 16;
            int x = gridLeft + px * PIXEL_SIZE;
            int y = gridTop + py * PIXEL_SIZE;

            int color = pixelData[i];
            if ((color >>> 24) != 0) {
                guiGraphics.fill(x, y, x + PIXEL_SIZE, y + PIXEL_SIZE, color);
            }

// 网格线
            guiGraphics.fill(x, y, x + 1, y + PIXEL_SIZE, 0xFF333333);
            guiGraphics.fill(x, y, x + PIXEL_SIZE, y + 1, 0xFF333333);
        }

// RGB 滑块
        drawSlider(guiGraphics, sliderX, sliderY, "R", red, 0xFFFF0000);
        drawSlider(guiGraphics, sliderX, sliderY + (SLIDER_HEIGHT + SLIDER_GAP), "G", green, 0xFF00FF00);
        drawSlider(guiGraphics, sliderX, sliderY + 2 * (SLIDER_HEIGHT + SLIDER_GAP), "B", blue, 0xFF0000FF);

// 颜色预览
// 预览框边框
        guiGraphics.fill(previewX - 1, previewY - 1,
                previewX + previewSize + 1, previewY + previewSize + 1, 0xFF444444);
// 预览框内容
        guiGraphics.fill(previewX, previewY,
                previewX + previewSize, previewY + previewSize, selectedColor);

// 显示 Hex 值
        String hex = String.format("#%06X", (selectedColor & 0xFFFFFF));
        guiGraphics.drawString(this.font, hex,
                previewX + previewSize + 6, previewY + 4, 0xFFFFFF, false);

// 显示当前颜色名（RGB值）
        String rgbText = "R:" + red + " G:" + green + " B:" + blue;
        guiGraphics.drawString(this.font, rgbText,
                previewX + previewSize + 6, previewY + 16, 0xAAAAAA, false);

// 鼠标悬停高亮
        if (hoverIndex >= 0) {
            int hx = hoverIndex % 16;
            int hy = hoverIndex / 16;
            guiGraphics.fill(gridLeft + hx * PIXEL_SIZE, gridTop + hy * PIXEL_SIZE,
                    gridLeft + (hx + 1) * PIXEL_SIZE, gridTop + (hy + 1) * PIXEL_SIZE,
                    0x55FFFFFF);
        }

// 绘制裁纸器/确认按钮
        for (var renderable : this.renderables) {
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

// 绘制单个 RGB 滑块
    private void drawSlider(GuiGraphics guiGraphics, int x, int y, String label, int value, int color) {
// 标签
        guiGraphics.drawString(this.font, label, x, y + 1, 0xFFFFFF, false);

// 滑块轨道背景
        guiGraphics.fill(sliderAreaLeft, y, sliderAreaLeft + SLIDER_WIDTH, y + SLIDER_HEIGHT, 0xFF333333);

// 轨道渐变效果：从低到高显示颜色
        int steps = 16;
        int stepW = SLIDER_WIDTH / steps;
        for (int s = 0; s < steps; s++) {
            int t = (s * 255) / (steps - 1);
            int r = (color >> 16 & 0xFF) * t / 255;
            int g = (color >> 8 & 0xFF) * t / 255;
            int b = (color & 0xFF) * t / 255;
            int gradColor = 0xFF000000 | (r << 16) | (g << 8) | b;
            int sx = sliderAreaLeft + s * stepW;
            guiGraphics.fill(sx, y, sx + stepW, y + SLIDER_HEIGHT, gradColor);
        }

// 当前值标记（白色竖线）
        int markerX = sliderAreaLeft + (int) ((value / 255.0) * (SLIDER_WIDTH - 2)) + 1;
        guiGraphics.fill(markerX - 1, y - 1, markerX + 2, y + SLIDER_HEIGHT + 1, 0xFFFFFFFF);
        guiGraphics.fill(markerX, y - 2, markerX + 1, y + SLIDER_HEIGHT + 2, 0xFFFFFFFF);

// 值文字
        String valStr = String.valueOf(value);
        guiGraphics.drawString(this.font, valStr,
                sliderAreaLeft + SLIDER_WIDTH + 4, y + 1, 0xFFFFFF, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
// 像素网格点击
            int px = (int) ((mouseX - gridLeft) / PIXEL_SIZE);
            int py = (int) ((mouseY - gridTop) / PIXEL_SIZE);
            if (px >= 0 && px < 16 && py >= 0 && py < 16) {
                int index = py * 16 + px;
                if (erasing) {
                    pixelData[index] = 0x00000000;
                } else {
                    pixelData[index] = selectedColor;
                }
                return true;
            }

// 滑块点击
            for (int i = 0; i < 3; i++) {
                int sy = sliderY + i * (SLIDER_HEIGHT + SLIDER_GAP);
                if (mouseX >= sliderAreaLeft && mouseX < sliderAreaLeft + SLIDER_WIDTH &&
                        mouseY >= sy && mouseY < sy + SLIDER_HEIGHT) {
                    int value = (int) Math.round(((mouseX - sliderAreaLeft) / (double) SLIDER_WIDTH) * 255);
                    value = Math.max(0, Math.min(255, value));
                    setRgbValue(i, value);
                    draggingSlider = true;
                    draggingIndex = i;
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggingSlider && button == 0 && draggingIndex >= 0) {
            int sy = sliderY + draggingIndex * (SLIDER_HEIGHT + SLIDER_GAP);
            if (mouseX >= sliderAreaLeft - 4 && mouseX < sliderAreaLeft + SLIDER_WIDTH + 4 &&
                    mouseY >= sy - 4 && mouseY < sy + SLIDER_HEIGHT + 4) {
                int value = (int) Math.round(((mouseX - sliderAreaLeft) / (double) SLIDER_WIDTH) * 255);
                value = Math.max(0, Math.min(255, value));
                setRgbValue(draggingIndex, value);
            }
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            draggingSlider = false;
            draggingIndex = -1;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

// 设置 RGB 分量值并更新 selectedColor
    private void setRgbValue(int index, int value) {
        switch (index) {
            case 0 -> red = value;
            case 1 -> green = value;
            case 2 -> blue = value;
        }
        selectedColor = 0xFF000000 | (red << 16) | (green << 8) | blue;
        erasing = false;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        int px = (int) ((mouseX - gridLeft) / PIXEL_SIZE);
        int py = (int) ((mouseY - gridTop) / PIXEL_SIZE);
        if (px >= 0 && px < 16 && py >= 0 && py < 16) {
            hoverIndex = py * 16 + px;
        } else {
            hoverIndex = -1;
        }
    }

    private void sendSync() {
        entity.setPixelData(pixelData);
        ModMessages.CHANNEL.sendToServer(new WindowPaperSyncPayload(entity.getId(), pixelData));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}