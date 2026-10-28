package com.flavor_immersed_daily.screen;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.block.blockentity.RaddlesBlockEntity;
import com.flavor_immersed_daily.network.RaddlesSyncPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

// 灯谜牌放置者编辑界面 — 牌面四字 + 谜面（问题）+ 谜底（答案）+ 16 格奖励物品栏。
// 面板与全部槽位底图来自 textures/gui/raddles_owner.png；保存时通过 RaddlesSyncPayload 同步到服务端。
public class RaddlesOwnerScreen extends AbstractContainerScreen<RaddlesOwnerMenu> {

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "textures/gui/raddles_owner.png");

// 牌面单字输入框
    private static final int CHAR_BOX_WIDTH = 26;
    private static final int CHAR_BOX_HEIGHT = 18;
    private static final int CHAR_X = 56;
    private static final int CHAR_GAP = 30;
    private static final int CHAR_Y = 20;
// 单字输入框左侧标签
    private static final int CHAR_LABEL_Y = 25;

// 谜面 / 谜底输入框
    private static final int TEXT_X = 56;
    private static final int TEXT_WIDTH = 156;
    private static final int TEXT_HEIGHT = 18;
    private static final int QUESTION_Y = 46;
    private static final int ANSWER_Y = 70;
    private static final int QUESTION_LABEL_Y = 51;
    private static final int ANSWER_LABEL_Y = 75;
// 谜面 / 谜底最大长度
    private static final int TEXT_MAX_LENGTH = 64;

// 奖励物品栏标签与保存按钮
    private static final int REWARD_LABEL_Y = 92;
    private static final int CONFIRM_X = 8;
    private static final int CONFIRM_Y = 112;
    private static final int CONFIRM_WIDTH = 44;
    private static final int CONFIRM_HEIGHT = 20;

// 标签颜色（深灰，浅色面板上清晰）
    private static final int LABEL_COLOR = 0xFF_40_40_40;

    private final EditBox[] charBoxes = new EditBox[RaddlesBlockEntity.CHAR_COUNT];
    private EditBox questionBox;
    private EditBox answerBox;

    public RaddlesOwnerScreen(RaddlesOwnerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = RaddlesOwnerMenu.IMAGE_WIDTH;
        this.imageHeight = RaddlesOwnerMenu.IMAGE_HEIGHT;
        this.inventoryLabelY = RaddlesOwnerMenu.INVENTORY_LABEL_Y;
    }

    @Override
    protected void init() {
        super.init();

        RaddlesBlockEntity blockEntity = resolveBlockEntity();

// 牌面四字：输入一个字后自动跳到下一个
        for (int i = 0; i < RaddlesBlockEntity.CHAR_COUNT; i++) {
            EditBox box = new EditBox(this.font,
                    this.leftPos + CHAR_X + i * CHAR_GAP, this.topPos + CHAR_Y,
                    CHAR_BOX_WIDTH, CHAR_BOX_HEIGHT, Component.empty());
            box.setMaxLength(1);
            box.setValue(blockEntity != null ? blockEntity.getChar(i) : "");

            final int index = i;
            box.setResponder(text -> {
                if (!text.isEmpty() && index < RaddlesBlockEntity.CHAR_COUNT - 1) {
                    this.setFocused(this.charBoxes[index + 1]);
                }
            });

            this.addRenderableWidget(box);
            this.charBoxes[i] = box;
        }

        this.questionBox = new EditBox(this.font,
                this.leftPos + TEXT_X, this.topPos + QUESTION_Y, TEXT_WIDTH, TEXT_HEIGHT, Component.empty());
        this.questionBox.setMaxLength(TEXT_MAX_LENGTH);
        this.questionBox.setValue(blockEntity != null ? blockEntity.getQuestion() : "");
        this.addRenderableWidget(this.questionBox);

        this.answerBox = new EditBox(this.font,
                this.leftPos + TEXT_X, this.topPos + ANSWER_Y, TEXT_WIDTH, TEXT_HEIGHT, Component.empty());
        this.answerBox.setMaxLength(TEXT_MAX_LENGTH);
        this.answerBox.setValue(blockEntity != null ? blockEntity.getAnswer() : "");
        this.addRenderableWidget(this.answerBox);

        this.addRenderableWidget(Button.builder(
                        Component.translatable("gui.flavor_immersed_daily.raddles.confirm"),
                        button -> saveAndSync())
                .bounds(this.leftPos + CONFIRM_X, this.topPos + CONFIRM_Y, CONFIRM_WIDTH, CONFIRM_HEIGHT)
                .build());

        this.setInitialFocus(this.charBoxes[0]);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
// 面板与奖励槽位底图都在贴图里
        guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight,
                this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        guiGraphics.drawString(this.font,
                Component.translatable("gui.flavor_immersed_daily.raddles.chars"), 8, CHAR_LABEL_Y, LABEL_COLOR, false);
        guiGraphics.drawString(this.font,
                Component.translatable("gui.flavor_immersed_daily.raddles.question"), 8, QUESTION_LABEL_Y, LABEL_COLOR, false);
        guiGraphics.drawString(this.font,
                Component.translatable("gui.flavor_immersed_daily.raddles.answer"), 8, ANSWER_LABEL_Y, LABEL_COLOR, false);
        guiGraphics.drawString(this.font,
                Component.translatable("gui.flavor_immersed_daily.raddles.rewards"), 8, REWARD_LABEL_Y, LABEL_COLOR, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

// 取本地方块实体，界面显示的文字直接来自它
    private RaddlesBlockEntity resolveBlockEntity() {
        BlockPos pos = this.menu.getBlockPos();
        Level level = Minecraft.getInstance().level;
        if (pos == null || level == null) {
            return null;
        }
        return level.getBlockEntity(pos) instanceof RaddlesBlockEntity blockEntity ? blockEntity : null;
    }

// 保存牌面文字与谜面谜底：先改本地实体即时生效，再发到服务端
    private void saveAndSync() {
        BlockPos pos = this.menu.getBlockPos();
        if (pos == null) {
            return;
        }

        String[] chars = new String[RaddlesBlockEntity.CHAR_COUNT];
        for (int i = 0; i < RaddlesBlockEntity.CHAR_COUNT; i++) {
            chars[i] = this.charBoxes[i].getValue();
        }
        String question = this.questionBox.getValue();
        String answer = this.answerBox.getValue();

        RaddlesBlockEntity blockEntity = resolveBlockEntity();
        if (blockEntity != null) {
            blockEntity.setChars(chars);
            blockEntity.setRiddle(question, answer);
        }
        PacketDistributor.sendToServer(new RaddlesSyncPayload(pos, chars, question, answer));
    }
}
