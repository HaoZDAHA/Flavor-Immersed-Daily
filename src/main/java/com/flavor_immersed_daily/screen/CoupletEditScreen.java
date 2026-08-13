package com.flavor_immersed_daily.screen;

import com.flavor_immersed_daily.block.blockentity.CoupletBlockEntity;
import com.flavor_immersed_daily.network.CoupletSyncPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * 对联编辑界面 — 参考原版告示牌，支持4行文字输入和颜色选择
 * 颜色仅支持黑色和黄色两种
 */
public class CoupletEditScreen extends Screen {

    private static final int BUTTON_WIDTH = 60;
    private static final int BUTTON_HEIGHT = 20;

    private final CoupletBlockEntity blockEntity;
    private final boolean isVertical;

    private EditBox[] editBoxes = new EditBox[4];
    private int currentColor = 0; // 0=黑色, 1=黄色
    private Button colorButton;
    private Button confirmButton;

    // 保存原始文字，用于取消时恢复
    private final String[] originalLines = new String[4];

    public CoupletEditScreen(CoupletBlockEntity blockEntity) {
        super(Component.translatable("gui.flavor_immersed_daily.couplet.title"));
        this.blockEntity = blockEntity;

        String path = blockEntity.getBlockState().getBlock().builtInRegistryHolder().key().location().getPath();
        this.isVertical = path.contains("antithetical_couplet_2");

        String[] lines = blockEntity.getLines();
        for (int i = 0; i < 4; i++) {
            originalLines[i] = lines[i] != null ? lines[i] : "";
        }
        this.currentColor = blockEntity.getColor();
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int startY = this.height / 2 - 50;

        // 创建4个输入框
        int boxWidth = 180;
        int boxHeight = 18;
        int gap = 22;

        for (int i = 0; i < 4; i++) {
            int y = startY + i * gap;

            // 输入框标签
            String label = isVertical ? "第" + (i + 1) + "字" : "行" + (i + 1);

            EditBox editBox = new EditBox(this.font, centerX - boxWidth / 2, y, boxWidth, boxHeight,
                    Component.literal(label));
            editBox.setMaxLength(isVertical ? 1 : 16);
            editBox.setValue(originalLines[i] != null ? originalLines[i] : "");

            if (isVertical) {
                // 竖联：每行只输入一个字，自动跳转到下一个输入框
                editBox.setMaxLength(1);
                final int idx = i;
                editBox.setResponder(text -> {
                    if (text.length() >= 1 && idx < 3) {
                        editBoxes[idx + 1].setFocused(true);
                    }
                });
            }

            this.addRenderableWidget(editBox);
            editBoxes[i] = editBox;
        }

        // 颜色选择按钮
        int colorBtnX = centerX - boxWidth / 2;
        int colorBtnY = startY + 4 * gap + 10;
        this.colorButton = Button.builder(
                        Component.translatable("gui.flavor_immersed_daily.couplet.color." + currentColor),
                        btn -> {
                            currentColor = (currentColor == 0) ? 1 : 0;
                            btn.setMessage(Component.translatable("gui.flavor_immersed_daily.couplet.color." + currentColor));
                        })
                .bounds(colorBtnX, colorBtnY, boxWidth, BUTTON_HEIGHT)
                .build();
        this.addRenderableWidget(colorButton);

        // 确认按钮
        int confirmBtnX = centerX - boxWidth / 2;
        int confirmBtnY = colorBtnY + BUTTON_HEIGHT + 6;
        this.confirmButton = Button.builder(
                        Component.translatable("gui.flavor_immersed_daily.fireworks_box.confirm"),
                        btn -> {
                            saveAndSync();
                            this.onClose();
                        })
                .bounds(confirmBtnX, confirmBtnY, boxWidth, BUTTON_HEIGHT)
                .build();
        this.addRenderableWidget(confirmButton);

        // 默认聚焦第一个输入框
        editBoxes[0].setFocused(true);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        // 标题
        guiGraphics.drawString(this.font,
                Component.translatable("gui.flavor_immersed_daily.couplet.title"),
                (this.width - this.font.width(Component.translatable("gui.flavor_immersed_daily.couplet.title"))) / 2,
                this.height / 2 - 75, 0xFFFFFF, false);

        // 绘制输入框标签
        int centerX = this.width / 2;
        int startY = this.height / 2 - 50;
        int gap = 22;
        for (int i = 0; i < 4; i++) {
            String label = isVertical ? "第" + (i + 1) + "字" : "行" + (i + 1);
            guiGraphics.drawString(this.font, label,
                    centerX - 90 - this.font.width(label) - 4, startY + i * gap + 4,
                    0xAAAAAA, false);
        }

        // 绘制所有组件
        for (var renderable : this.renderables) {
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    private void saveAndSync() {
        String[] lines = new String[4];
        for (int i = 0; i < 4; i++) {
            lines[i] = editBoxes[i].getValue();
        }
        // 更新本地BlockEntity
        blockEntity.setLines(lines);
        blockEntity.setColor(currentColor);
        // 发送到服务端
        PacketDistributor.sendToServer(new CoupletSyncPayload(blockEntity.getBlockPos(), lines, currentColor));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        // 恢复输入框焦点状态
        for (EditBox box : editBoxes) {
            box.setFocused(false);
        }
        super.onClose();
    }
}