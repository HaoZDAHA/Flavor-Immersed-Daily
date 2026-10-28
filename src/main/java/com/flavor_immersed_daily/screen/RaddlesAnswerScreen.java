package com.flavor_immersed_daily.screen;

import com.flavor_immersed_daily.block.blockentity.RaddlesBlockEntity;
import com.flavor_immersed_daily.network.RaddlesAnswerPayload;
import com.flavor_immersed_daily.network.RaddlesResultPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

// 灯谜答题界面 — 其他玩家右键灯谜牌时打开。
// 显示放置者的问题、奖励物品图标与答案输入框，答案由服务端校验并发放奖励。
public class RaddlesAnswerScreen extends Screen {

    private static final int PANEL_WIDTH = 240;
    private static final int PANEL_HEIGHT = 178;
    private static final int PADDING = 8;

// 问题最多显示行数
    private static final int MAX_QUESTION_LINES = 3;
// 奖励图标每行个数与间距
    private static final int ICONS_PER_ROW = 8;
    private static final int ICON_SPACING = 18;

    private static final int QUESTION_PREFIX_Y = 26;
    private static final int QUESTION_TEXT_Y = 38;
    private static final int REWARD_ICON_Y = 82;
    private static final int ANSWER_Y = 130;
    private static final int SUBMIT_WIDTH = 56;

    private static final int PANEL_INNER = 0xFF_8B_8B_8B;
    private static final int PANEL_OUTER = 0xFF_C6_C6_C6;
    private static final int LABEL_COLOR = 0xFF_40_40_40;
    private static final int QUESTION_COLOR = 0xFF_20_20_20;

    private final RaddlesBlockEntity blockEntity;

    private int left;
    private int top;

    private EditBox answerBox;
    private Button submitButton;
    private Component status = Component.empty();
    private int statusColor = 0xFF_FF_FF_55;
// 已答对 / 已领过奖励，界面不再允许提交
    private boolean finished;

    public RaddlesAnswerScreen(RaddlesBlockEntity blockEntity) {
        super(Component.translatable("gui.flavor_immersed_daily.raddles.answer_title"));
        this.blockEntity = blockEntity;
    }

    @Override
    protected void init() {
        super.init();
        this.left = (this.width - PANEL_WIDTH) / 2;
        this.top = (this.height - PANEL_HEIGHT) / 2;

// 输入框是“写答案”的，占位文本只作为提示
        this.answerBox = new EditBox(this.font,
                this.left + PADDING, this.top + ANSWER_Y,
                PANEL_WIDTH - PADDING * 2 - SUBMIT_WIDTH - 4, 20,
                Component.translatable("gui.flavor_immersed_daily.raddles.answer_hint"));
        this.answerBox.setMaxLength(64);
        this.addRenderableWidget(this.answerBox);

        this.submitButton = Button.builder(
                        Component.translatable("gui.flavor_immersed_daily.raddles.submit"),
                        button -> submitAnswer())
                .bounds(this.left + PANEL_WIDTH - PADDING - SUBMIT_WIDTH, this.top + ANSWER_Y, SUBMIT_WIDTH, 20)
                .build();
        this.addRenderableWidget(this.submitButton);

        this.setInitialFocus(this.answerBox);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

// 面板背景
        guiGraphics.fill(this.left, this.top, this.left + PANEL_WIDTH, this.top + PANEL_HEIGHT, PANEL_OUTER);
        guiGraphics.fill(this.left + 1, this.top + 1, this.left + PANEL_WIDTH - 1, this.top + PANEL_HEIGHT - 1,
                PANEL_INNER);

// 标题
        guiGraphics.drawString(this.font, this.title, this.left + PADDING, this.top + PADDING, LABEL_COLOR, false);

// 放置者的问题（自动换行，最多显示若干行）
        guiGraphics.drawString(this.font,
                Component.translatable("gui.flavor_immersed_daily.raddles.question_prefix"),
                this.left + PADDING, this.top + QUESTION_PREFIX_Y, LABEL_COLOR, false);
        List<FormattedCharSequence> questionLines = this.font.split(
                Component.literal(this.blockEntity.getQuestion()), PANEL_WIDTH - PADDING * 2);
        int textY = this.top + QUESTION_TEXT_Y;
        for (int i = 0; i < questionLines.size() && i < MAX_QUESTION_LINES; i++) {
            guiGraphics.drawString(this.font, questionLines.get(i), this.left + PADDING, textY, QUESTION_COLOR, false);
            textY += 10;
        }

// 奖励物品图标
        guiGraphics.drawString(this.font,
                Component.translatable("gui.flavor_immersed_daily.raddles.reward_prefix"),
                this.left + PADDING, this.top + REWARD_ICON_Y - 10, LABEL_COLOR, false);
        renderRewardIcons(guiGraphics);

// 答题结果反馈
        if (!this.status.getString().isEmpty()) {
            guiGraphics.drawString(this.font, this.status,
                    this.left + PADDING, this.top + PANEL_HEIGHT - 18, this.statusColor, false);
        }

// 绘制控件
        for (var renderable : this.renderables) {
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

// 逐行居中绘制奖励物品图标（只画图标，不显示数量）
    private void renderRewardIcons(GuiGraphics guiGraphics) {
        List<ItemStack> rewards = this.blockEntity.getRewardPreview();
        for (int i = 0; i < rewards.size(); i++) {
            int row = i / ICONS_PER_ROW;
            int col = i % ICONS_PER_ROW;
            int rowCount = Math.min(rewards.size() - row * ICONS_PER_ROW, ICONS_PER_ROW);
            int startX = this.left + (PANEL_WIDTH - rowCount * ICON_SPACING) / 2;
            guiGraphics.renderFakeItem(rewards.get(i), startX + col * ICON_SPACING,
                    this.top + REWARD_ICON_Y + row * ICON_SPACING);
        }
    }

    private void submitAnswer() {
        if (this.finished) {
            return;
        }
        String answer = this.answerBox.getValue().trim();
        if (answer.isEmpty()) {
            return;
        }
        PacketDistributor.sendToServer(new RaddlesAnswerPayload(this.blockEntity.getBlockPos(), answer));
    }

// 服务端校验结果回到客户端后更新界面
    public void onResult(int result) {
        switch (result) {
            case RaddlesResultPayload.SUCCESS -> {
                this.status = Component.translatable("gui.flavor_immersed_daily.raddles.result.success");
                this.statusColor = 0xFF_55_FF_55;
                finish();
            }
            case RaddlesResultPayload.NO_REWARD -> {
                this.status = Component.translatable("gui.flavor_immersed_daily.raddles.result.no_reward");
                this.statusColor = 0xFF_FF_AA_00;
                finish();
            }
            case RaddlesResultPayload.ALREADY_CLAIMED -> {
                this.status = Component.translatable("gui.flavor_immersed_daily.raddles.result.claimed");
                this.statusColor = 0xFF_FF_AA_00;
                finish();
            }
            default -> {
                this.status = Component.translatable("gui.flavor_immersed_daily.raddles.result.wrong");
                this.statusColor = 0xFF_FF_55_55;
            }
        }
    }

    private void finish() {
        this.finished = true;
        if (this.submitButton != null) {
            this.submitButton.active = false;
        }
        if (this.answerBox != null) {
            this.answerBox.setEditable(false);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        this.setFocused(null);
        super.onClose();
    }
}
