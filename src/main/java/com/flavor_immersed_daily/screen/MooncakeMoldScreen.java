package com.flavor_immersed_daily.screen;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

// 月饼模具 GUI — 双槽位（饼皮 + 馅料）。
// 使用与原工作区其它屏幕一致的原版容器风格：一张容器背景纹理
// 经 GuiGraphics#blit 绘制，槽位（含玩家物品栏）已烘焙进贴图。
public class MooncakeMoldScreen extends AbstractContainerScreen<MooncakeMoldMenu> {

    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "textures/gui/mooncake_mold.png");

// 模具槽位坐标（须与 MooncakeMoldMenu 中的槽位坐标一致）
    private static final int DOUGH_SLOT_X = 44;
    private static final int FILLING_SLOT_X = 116;
    private static final int SLOT_Y = 20;

// 配色
    private static final int LABEL_COLOR = 0xFF_40_40_40;
    private static final int PLUS_COLOR = 0xFF_60_60_60;

    public MooncakeMoldScreen(MooncakeMoldMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
// 标题居中，正好落在压制区顶部
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
// 原版容器背景（槽位已烘焙进贴图）
        guiGraphics.blit(GUI_TEXTURE, this.leftPos, this.topPos, 0, 0,
                this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

// 两槽之间的加号
        int x = this.leftPos;
        int y = this.topPos;
        int plusX = x + (DOUGH_SLOT_X + 16 + FILLING_SLOT_X) / 2 - 3;
        guiGraphics.drawString(this.font, "+", plusX, y + SLOT_Y + 4, PLUS_COLOR, false);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);

// 槽位标签
        guiGraphics.drawString(this.font,
                Component.translatable("gui.flavor_immersed_daily.mooncake_mold.dough"),
                DOUGH_SLOT_X, SLOT_Y + 22, LABEL_COLOR, false);
        guiGraphics.drawString(this.font,
                Component.translatable("gui.flavor_immersed_daily.mooncake_mold.filling"),
                FILLING_SLOT_X, SLOT_Y + 22, LABEL_COLOR, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}