package com.flavor_immersed_daily.screen;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.item.FairySparklerItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FairySparklerConfigScreen extends Screen {

    private static final int COLOR_BUTTON_SIZE = 18;
    private static final int COLOR_SPACING = 20;
    private static final int COLORS_PER_ROW = 4;
    private static final int GUI_WIDTH = 160;
    private static final int GUI_HEIGHT = 210;

    private static final int SHAPE_BUTTON_WIDTH = 50;
    private static final int SHAPE_BUTTON_HEIGHT = 20;
    private static final int SHAPE_SPACING = 55;

    // 内圈
    private int innerColor;
    private int innerShape;
    // 外圈
    private int outerColor;
    private int outerShape;

    private int guiLeft;
    private int guiTop;

    public FairySparklerConfigScreen() {
        super(Component.translatable("item.flavor_immersed_daily.fairy_sparkler"));
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            if (!stack.is(ModItems.FAIRY_SPARKLER.get())) {
                stack = player.getOffhandItem();
            }
            if (stack.is(ModItems.FAIRY_SPARKLER.get())) {
                this.innerColor = FairySparklerItem.getColor(stack);
                this.innerShape = FairySparklerItem.getShape(stack);
                this.outerColor = FairySparklerItem.getColor2(stack);
                this.outerShape = FairySparklerItem.getShape2(stack);
            } else {
                this.innerColor = FairySparklerItem.COLOR_RED;
                this.innerShape = FairySparklerItem.SHAPE_CIRCLE;
                this.outerColor = FairySparklerItem.COLOR_BLUE;
                this.outerShape = FairySparklerItem.SHAPE_SPIRAL;
            }
        } else {
            this.innerColor = FairySparklerItem.COLOR_RED;
            this.innerShape = FairySparklerItem.SHAPE_CIRCLE;
            this.outerColor = FairySparklerItem.COLOR_BLUE;
            this.outerShape = FairySparklerItem.SHAPE_SPIRAL;
        }
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - GUI_WIDTH) / 2;
        this.guiTop = (this.height - GUI_HEIGHT) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        // ===== 内圈 =====
        guiGraphics.drawString(this.font,
                Component.translatable("gui.flavor_immersed_daily.fairy_sparkler.ring1"),
                guiLeft + 8, guiTop + 8, 0xFFFFAA, false);

        renderColorGrid(guiGraphics, guiTop + 24, innerColor, true);
        renderShapeBar(guiGraphics, guiTop + 68, innerShape, true);

        // ===== 外圈 =====
        guiGraphics.drawString(this.font,
                Component.translatable("gui.flavor_immersed_daily.fairy_sparkler.ring2"),
                guiLeft + 8, guiTop + 100, 0xAAFFFF, false);

        renderColorGrid(guiGraphics, guiTop + 116, outerColor, false);
        renderShapeBar(guiGraphics, guiTop + 160, outerShape, false);
    }

    private void renderColorGrid(GuiGraphics guiGraphics, int baseY, int currentColor, boolean isInner) {
        int colorGridWidth = (COLORS_PER_ROW - 1) * COLOR_SPACING + COLOR_BUTTON_SIZE;
        int startX = guiLeft + (GUI_WIDTH - colorGridWidth) / 2;

        for (int i = 0; i < FairySparklerItem.COLORS.length; i++) {
            int col = i % COLORS_PER_ROW;
            int row = i / COLORS_PER_ROW;
            int x = startX + col * COLOR_SPACING;
            int y = baseY + row * COLOR_SPACING;

            int color = FairySparklerItem.COLORS[i];
            boolean isSelected = color == currentColor;

            if (isSelected) {
                guiGraphics.fill(x - 2, y - 2, x + COLOR_BUTTON_SIZE + 2, y + COLOR_BUTTON_SIZE + 2, 0xFFFFFFFF);
            }
            guiGraphics.fill(x, y, x + COLOR_BUTTON_SIZE, y + COLOR_BUTTON_SIZE, 0xFF000000 | color);
        }
    }

    private void renderShapeBar(GuiGraphics guiGraphics, int baseY, int currentShape, boolean isInner) {
        int shapeGridWidth = 3 * SHAPE_SPACING;
        int startX = guiLeft + (GUI_WIDTH - shapeGridWidth) / 2;

        for (int i = 0; i < 3; i++) {
            int x = startX + i * SHAPE_SPACING;
            int y = baseY;

            boolean isSelected = i == currentShape;
            int bgColor = isSelected ? 0xFF_4444AA : 0xFF_333333;
            int textColor = isSelected ? 0xFFFFFF : 0xAAAAAA;

            guiGraphics.fill(x, y, x + SHAPE_BUTTON_WIDTH, y + SHAPE_BUTTON_HEIGHT, bgColor);
            guiGraphics.drawString(this.font,
                    Component.translatable("gui.flavor_immersed_daily.fairy_sparkler.shape." + FairySparklerItem.SHAPE_NAMES[i]),
                    x + 5, y + 5, textColor, false);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // 内圈颜色
        if (checkColorClick(mouseX, mouseY, guiTop + 24, innerColor, true)) return true;
        // 内圈形状
        if (checkShapeClick(mouseX, mouseY, guiTop + 68, innerShape, true)) return true;
        // 外圈颜色
        if (checkColorClick(mouseX, mouseY, guiTop + 116, outerColor, false)) return true;
        // 外圈形状
        if (checkShapeClick(mouseX, mouseY, guiTop + 160, outerShape, false)) return true;

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean checkColorClick(double mouseX, double mouseY, int baseY, int currentColor, boolean isInner) {
        int colorGridWidth = (COLORS_PER_ROW - 1) * COLOR_SPACING + COLOR_BUTTON_SIZE;
        int startX = guiLeft + (GUI_WIDTH - colorGridWidth) / 2;

        for (int i = 0; i < FairySparklerItem.COLORS.length; i++) {
            int col = i % COLORS_PER_ROW;
            int row = i / COLORS_PER_ROW;
            int x = startX + col * COLOR_SPACING;
            int y = baseY + row * COLOR_SPACING;

            if (mouseX >= x && mouseX < x + COLOR_BUTTON_SIZE && mouseY >= y && mouseY < y + COLOR_BUTTON_SIZE) {
                if (isInner) {
                    innerColor = FairySparklerItem.COLORS[i];
                } else {
                    outerColor = FairySparklerItem.COLORS[i];
                }
                saveToItem();
                return true;
            }
        }
        return false;
    }

    private boolean checkShapeClick(double mouseX, double mouseY, int baseY, int currentShape, boolean isInner) {
        int shapeGridWidth = 3 * SHAPE_SPACING;
        int startX = guiLeft + (GUI_WIDTH - shapeGridWidth) / 2;

        for (int i = 0; i < 3; i++) {
            int x = startX + i * SHAPE_SPACING;
            int y = baseY;

            if (mouseX >= x && mouseX < x + SHAPE_BUTTON_WIDTH && mouseY >= y && mouseY < y + SHAPE_BUTTON_HEIGHT) {
                if (isInner) {
                    innerShape = i;
                } else {
                    outerShape = i;
                }
                saveToItem();
                return true;
            }
        }
        return false;
    }

    private void saveToItem() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModItems.FAIRY_SPARKLER.get())) {
            stack = player.getOffhandItem();
        }
        if (stack.is(ModItems.FAIRY_SPARKLER.get())) {
            FairySparklerItem.setColor(stack, innerColor);
            FairySparklerItem.setShape(stack, innerShape);
            FairySparklerItem.setColor2(stack, outerColor);
            FairySparklerItem.setShape2(stack, outerShape);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
