package com.flavor_immersed_daily.screen;

import com.flavor_immersed_daily.block.blockentity.ColorfulFireworksBoxBlockEntity;
import com.flavor_immersed_daily.network.ColorfulFireworksBoxSyncPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 箱装烟花配置界面 — 纯客户端 Screen，通过网络包同步到服务端 BlockEntity
 */
public class ColorfulFireworksBoxConfigScreen extends Screen {

    private static final int[] COLORS = {
            0xFF0000, 0x0000FF, 0x00FF00, 0xFFFF00,
            0xFF00FF, 0xFF8800, 0xFFFFFF, 0xFF69B4
    };

    private static final int GUI_WIDTH = 280;
    private static final int GUI_HEIGHT = 260;
    private static final int COLOR_BUTTON_SIZE = 16;
    private static final int COLOR_SPACING = 18;
    private static final int COLORS_PER_ROW = 4;

    private static final int SHAPE_BUTTON_WIDTH = 48;
    private static final int SHAPE_BUTTON_HEIGHT = 16;
    private static final int SHAPE_SPACING = 51;
    private static final int COL1_X = 8;
    private static final int COL2_X = 152;

    private static final Predicate<String> NUMBER_FILTER = s -> s.matches("-?[0-9]*\\.?[0-9]*");

    private final BlockPos pos;

    private int guiLeft;
    private int guiTop;

    // 配置
    private int color;
    private int fadeColor;
    private int shape;
    private boolean trail;
    private float angleDeg;
    private float speed;
    private float distance;
    private float curveA;
    private float curveB;

    // 控件
    private EditBox angleBox;
    private EditBox speedBox;
    private EditBox distanceBox;
    private EditBox curveABox;
    private EditBox curveBBox;

    public ColorfulFireworksBoxConfigScreen(BlockPos pos) {
        super(Component.translatable("block.flavor_immersed_daily.colorful_fireworks_box"));
        this.pos = pos;

        // 从客户端 BlockEntity 读取当前配置
        Minecraft mc = Minecraft.getInstance();
        BlockEntity be = mc.level != null ? mc.level.getBlockEntity(pos) : null;
        if (be instanceof ColorfulFireworksBoxBlockEntity box) {
            this.color = box.getColor();
            this.fadeColor = box.getFadeColor();
            this.shape = box.getShape();
            this.trail = box.hasTrail();
            this.angleDeg = box.getAngleDeg();
            this.speed = box.getSpeed();
            this.distance = box.getDistance();
            this.curveA = box.getCurveA();
            this.curveB = box.getCurveB();
        } else {
            this.color = COLORS[0];
            this.fadeColor = COLORS[6]; // 白色
            this.shape = ColorfulFireworksBoxBlockEntity.SHAPE_SMALL_BALL;
            this.trail = false;
            this.angleDeg = 0.0f;
            this.speed = 1.0f;
            this.distance = 5.0f;
            this.curveA = 0.0f;
            this.curveB = 0.0f;
        }
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - GUI_WIDTH) / 2;
        this.guiTop = (this.height - GUI_HEIGHT) / 2;

        int inputX = guiLeft + COL2_X + 50;
        int inputW = 70;

        this.angleBox = createNumberBox(inputX, guiTop + 28, inputW, Float.toString(angleDeg), value -> angleDeg = value);
        this.speedBox = createNumberBox(inputX, guiTop + 50, inputW, Float.toString(speed), value -> speed = value);
        this.distanceBox = createNumberBox(inputX, guiTop + 72, inputW, Float.toString(distance), value -> distance = value);
        this.curveABox = createNumberBox(inputX, guiTop + 100, inputW, Float.toString(curveA), value -> curveA = value);
        this.curveBBox = createNumberBox(inputX, guiTop + 122, inputW, Float.toString(curveB), value -> curveB = value);

        // 确认按钮
        this.addRenderableWidget(Button.builder(
                Component.translatable("gui.flavor_immersed_daily.fireworks_box.confirm"),
                btn -> {
                    sendSync();
                    this.onClose();
                })
                .bounds(guiLeft + GUI_WIDTH / 2 - 30, guiTop + GUI_HEIGHT - 28, 60, 18)
                .build());
    }

    private EditBox createNumberBox(int x, int y, int width, String initial, Consumer<Float> setter) {
        EditBox box = new EditBox(this.font, x, y, width, 16, Component.empty());
        box.setFilter(NUMBER_FILTER);
        box.setValue(initial);
        box.setResponder(s -> {
            if (!s.isEmpty()) {
                try {
                    setter.accept(Float.parseFloat(s));
                    sendSync();
                } catch (NumberFormatException ignored) {
                }
            }
        });
        this.addRenderableWidget(box);
        return box;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        // 标题
        guiGraphics.drawString(this.font,
                Component.translatable("block.flavor_immersed_daily.colorful_fireworks_box"),
                guiLeft + 8, guiTop + 8, 0xFFFFFF, false);

        // ===== 左列：主颜色 =====
        guiGraphics.drawString(this.font,
                Component.translatable("gui.flavor_immersed_daily.fireworks_box.color"),
                guiLeft + COL1_X, guiTop + 28, 0xAAAAAA, false);
        int colorGridWidth = (COLORS_PER_ROW - 1) * COLOR_SPACING + COLOR_BUTTON_SIZE;
        int colorStartX = guiLeft + COL1_X + (152 - colorGridWidth) / 2;
        int colorGridY = guiTop + 44;
        drawColorGrid(guiGraphics, colorStartX, colorGridY, color, false);

        // ===== 左列：第二颜色 =====
        guiGraphics.drawString(this.font,
                Component.translatable("gui.flavor_immersed_daily.fireworks_box.fade_color"),
                guiLeft + COL1_X, guiTop + 86, 0xAAAAAA, false);
        int fadeColorGridY = guiTop + 102;
        drawColorGrid(guiGraphics, colorStartX, fadeColorGridY, fadeColor, true);

        // ===== 左列：形状（两行：3+2）=====
        guiGraphics.drawString(this.font,
                Component.translatable("gui.flavor_immersed_daily.fireworks_box.shape"),
                guiLeft + COL1_X, guiTop + 140, 0xAAAAAA, false);
        int[] shapeRow1 = {0, 1, 2}; // small_ball, large_ball, star
        int[] shapeRow2 = {3, 4};    // creeper, burst
        int shapeRowY1 = guiTop + 156;
        int shapeRowY2 = shapeRowY1 + SHAPE_BUTTON_HEIGHT + 4;
        int row1Width = (shapeRow1.length - 1) * SHAPE_SPACING + SHAPE_BUTTON_WIDTH;
        int row1StartX = guiLeft + COL1_X + (152 - row1Width) / 2;
        int row2Width = (shapeRow2.length - 1) * SHAPE_SPACING + SHAPE_BUTTON_WIDTH;
        int row2StartX = guiLeft + COL1_X + (152 - row2Width) / 2;
        for (int i : shapeRow1) {
            int x = row1StartX + i * SHAPE_SPACING;
            drawShapeButton(guiGraphics, i, x, shapeRowY1);
        }
        for (int i : shapeRow2) {
            int x = row2StartX + (i - shapeRow2[0]) * SHAPE_SPACING;
            drawShapeButton(guiGraphics, i, x, shapeRowY2);
        }

        // ===== 左列：拖尾 ======
        guiGraphics.drawString(this.font,
                Component.translatable("gui.flavor_immersed_daily.fireworks_box.trail"),
                guiLeft + COL1_X, guiTop + 196, 0xAAAAAA, false);
        int trailBg = trail ? 0xFF44AA44 : 0xFF333333;
        guiGraphics.fill(guiLeft + COL1_X + 70, guiTop + 192, guiLeft + COL1_X + 115, guiTop + 208, trailBg);
        guiGraphics.drawString(this.font,
                Component.translatable(trail ? "gui.flavor_immersed_daily.fireworks_box.on" : "gui.flavor_immersed_daily.fireworks_box.off"),
                guiLeft + COL1_X + 77, guiTop + 196, 0xFFFFFF, false);

        // ===== 右列：参数 =====
        drawRightLabel(guiGraphics, "gui.flavor_immersed_daily.fireworks_box.angle", 28);
        drawRightLabel(guiGraphics, "gui.flavor_immersed_daily.fireworks_box.speed", 50);
        drawRightLabel(guiGraphics, "gui.flavor_immersed_daily.fireworks_box.distance", 72);
        drawRightLabel(guiGraphics, "gui.flavor_immersed_daily.fireworks_box.curveA", 100);
        drawRightLabel(guiGraphics, "gui.flavor_immersed_daily.fireworks_box.curveB", 122);

        // 手动渲染 EditBox，不调用 super.render() 避免重复渲染背景
        for (var renderable : this.renderables) {
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    private void drawRightLabel(GuiGraphics guiGraphics, String key, int y) {
        guiGraphics.drawString(this.font, Component.translatable(key), guiLeft + COL2_X, guiTop + y, 0xAAAAAA, false);
    }

    private void drawColorGrid(GuiGraphics guiGraphics, int startX, int startY, int selectedColor, boolean isFade) {
        for (int i = 0; i < COLORS.length; i++) {
            int col = i % COLORS_PER_ROW;
            int row = i / COLORS_PER_ROW;
            int x = startX + col * COLOR_SPACING;
            int y = startY + row * COLOR_SPACING;
            if (COLORS[i] == selectedColor) {
                guiGraphics.fill(x - 2, y - 2, x + COLOR_BUTTON_SIZE + 2, y + COLOR_BUTTON_SIZE + 2, 0xFFFFFFFF);
            }
            guiGraphics.fill(x, y, x + COLOR_BUTTON_SIZE, y + COLOR_BUTTON_SIZE, 0xFF000000 | COLORS[i]);
        }
    }

    private void drawShapeButton(GuiGraphics guiGraphics, int index, int x, int y) {
        boolean selected = index == shape;
        int bg = selected ? 0xFF4444AA : 0xFF333333;
        int textColor = selected ? 0xFFFFFF : 0xAAAAAA;
        guiGraphics.fill(x, y, x + SHAPE_BUTTON_WIDTH, y + SHAPE_BUTTON_HEIGHT, bg);
        guiGraphics.drawString(this.font,
                Component.translatable("gui.flavor_immersed_daily.fireworks_box.shape." + ColorfulFireworksBoxBlockEntity.SHAPE_NAMES[index]),
                x + 3, y + 3, textColor, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int colorGridWidth = (COLORS_PER_ROW - 1) * COLOR_SPACING + COLOR_BUTTON_SIZE;
        int colorStartX = guiLeft + COL1_X + (152 - colorGridWidth) / 2;

        // 主颜色按钮
        int colorGridY = guiTop + 44;
        for (int i = 0; i < COLORS.length; i++) {
            int col = i % COLORS_PER_ROW;
            int row = i / COLORS_PER_ROW;
            int x = colorStartX + col * COLOR_SPACING;
            int y = colorGridY + row * COLOR_SPACING;
            if (mouseX >= x && mouseX < x + COLOR_BUTTON_SIZE && mouseY >= y && mouseY < y + COLOR_BUTTON_SIZE) {
                color = COLORS[i];
                sendSync();
                return true;
            }
        }

        // 第二颜色按钮
        int fadeColorGridY = guiTop + 102;
        for (int i = 0; i < COLORS.length; i++) {
            int col = i % COLORS_PER_ROW;
            int row = i / COLORS_PER_ROW;
            int x = colorStartX + col * COLOR_SPACING;
            int y = fadeColorGridY + row * COLOR_SPACING;
            if (mouseX >= x && mouseX < x + COLOR_BUTTON_SIZE && mouseY >= y && mouseY < y + COLOR_BUTTON_SIZE) {
                fadeColor = COLORS[i];
                sendSync();
                return true;
            }
        }

        // 形状按钮（两行）
        int[] clickRow1 = {0, 1, 2};
        int[] clickRow2 = {3, 4};
        int clickRowY1 = guiTop + 156;
        int clickRowY2 = clickRowY1 + SHAPE_BUTTON_HEIGHT + 4;
        int row1Width_c = (clickRow1.length - 1) * SHAPE_SPACING + SHAPE_BUTTON_WIDTH;
        int row1StartX_c = guiLeft + COL1_X + (152 - row1Width_c) / 2;
        int row2Width_c = (clickRow2.length - 1) * SHAPE_SPACING + SHAPE_BUTTON_WIDTH;
        int row2StartX_c = guiLeft + COL1_X + (152 - row2Width_c) / 2;
        for (int i : clickRow1) {
            int x = row1StartX_c + i * SHAPE_SPACING;
            if (mouseX >= x && mouseX < x + SHAPE_BUTTON_WIDTH && mouseY >= clickRowY1 && mouseY < clickRowY1 + SHAPE_BUTTON_HEIGHT) {
                shape = i;
                sendSync();
                return true;
            }
        }
        for (int i : clickRow2) {
            int x = row2StartX_c + (i - clickRow2[0]) * SHAPE_SPACING;
            if (mouseX >= x && mouseX < x + SHAPE_BUTTON_WIDTH && mouseY >= clickRowY2 && mouseY < clickRowY2 + SHAPE_BUTTON_HEIGHT) {
                shape = i;
                sendSync();
                return true;
            }
        }

        // 拖尾开关
        if (mouseX >= guiLeft + COL1_X + 70 && mouseX < guiLeft + COL1_X + 115 && mouseY >= guiTop + 192 && mouseY < guiTop + 208) {
            trail = !trail;
            sendSync();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void sendSync() {
        CompoundTag config = new CompoundTag();
        config.putInt("fw_color", color);
        config.putInt("fw_fade_color", fadeColor);
        config.putInt("fw_shape", shape);
        config.putBoolean("fw_trail", trail);
        config.putFloat("fw_angle", angleDeg);
        config.putFloat("fw_speed", speed);
        config.putFloat("fw_distance", distance);
        config.putFloat("fw_curve_a", curveA);
        config.putFloat("fw_curve_b", curveB);
        PacketDistributor.sendToServer(new ColorfulFireworksBoxSyncPayload(pos, config));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}