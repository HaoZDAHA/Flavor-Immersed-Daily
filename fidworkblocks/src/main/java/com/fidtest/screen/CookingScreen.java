package com.fidtest.screen;

import com.fidtest.ExampleMod;
import com.fidtest.recipe.ApplianceType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CookingScreen extends AbstractContainerScreen<CookingMenu> {

    private final ApplianceType applianceType;
    private final ResourceLocation texture;
    private final ResourceLocation progressTexture;

// 烘干机模式按钮（位于容器主体与背包之间的空白带）
    private static final int MODE_BUTTON_W = 36;
    private static final int MODE_BUTTON_H = 13;
// 烘干模式按钮
    private static final int MODE_BUTTON_X1 = 50;
// 烤箱模式按钮
    private static final int MODE_BUTTON_X2 = 90;
    private static final int MODE_BUTTON_Y = 60;

    private static final ResourceLocation BUTTON_SPRITE =
            ResourceLocation.withDefaultNamespace("widget/button");
    private static final ResourceLocation BUTTON_HIGHLIGHTED_SPRITE =
            ResourceLocation.withDefaultNamespace("widget/button_highlighted");

    public CookingScreen(CookingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.applianceType = menu.getApplianceType();
        this.texture = ResourceLocation.fromNamespaceAndPath(ExampleMod.MODID,
                "textures/gui/container/" + applianceType.getName() + ".png");
        this.progressTexture = ResourceLocation.fromNamespaceAndPath(ExampleMod.MODID,
                "textures/gui/container/" + applianceType.getName() + "_progress.png");
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        if (applianceType == ApplianceType.DRYER) {
            int x = (this.width - this.imageWidth) / 2;
            int y = (this.height - this.imageHeight) / 2;
            int mode = this.menu.getDryerMode();
            drawModeButton(g, x + MODE_BUTTON_X1, y + MODE_BUTTON_Y, 0, mode, mouseX, mouseY);
            drawModeButton(g, x + MODE_BUTTON_X2, y + MODE_BUTTON_Y, 1, mode, mouseX, mouseY);
        }
        this.renderTooltip(g, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        g.blit(texture, x, y, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

        if (!applianceType.isInstant()) {
            int progress = this.menu.getCookingProgress();
            int total = Math.max(this.menu.getCookingTotalTime(), 1);
            int w = (int) (24.0F * progress / total);

            int arrowX = getArrowX(applianceType);
            int arrowY = getArrowY(applianceType);
            if (w > 0) {
                g.blit(progressTexture, x + arrowX, y + arrowY, 0, 0, w, 17, 24, 17);
            }
        }

        if (applianceType.needsFuel() && this.menu.getHeatLevel() > 0) {
            g.blit(texture, x + 20, y + 38, 176, 17, 14, 14, this.imageWidth, this.imageHeight);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (applianceType == ApplianceType.DRYER && button == 0) {
            int x = (this.width - this.imageWidth) / 2;
            int y = (this.height - this.imageHeight) / 2;
            if (isInside(mouseX, mouseY, x + MODE_BUTTON_X1, y + MODE_BUTTON_Y)) {
                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0);
                return true;
            }
            if (isInside(mouseX, mouseY, x + MODE_BUTTON_X2, y + MODE_BUTTON_Y)) {
                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 1);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

// 绘制模式按钮：选中态用高亮贴图 + 亮色文字
    private void drawModeButton(GuiGraphics g, int x, int y, int mode, int currentMode, int mouseX, int mouseY) {
        boolean selected = mode == currentMode;
        boolean hovered = isInside(mouseX, mouseY, x, y);
        ResourceLocation sprite = (selected || hovered) ? BUTTON_HIGHLIGHTED_SPRITE : BUTTON_SPRITE;
        g.blitSprite(sprite, x, y, MODE_BUTTON_W, MODE_BUTTON_H);

        Component label = mode == 0
                ? Component.translatable("screen.fidworkblock.dryer_mode_dry")
                : Component.translatable("screen.fidworkblock.dryer_mode_oven");
        int textX = x + (MODE_BUTTON_W - this.font.width(label)) / 2;
        int textY = y + (MODE_BUTTON_H - this.font.lineHeight) / 2;
        g.drawString(this.font, label, textX, textY, selected ? 0xFFFFA0 : 0x404040);
    }

    private boolean isInside(double mouseX, double mouseY, int x, int y) {
        return mouseX >= x && mouseX < x + MODE_BUTTON_W
                && mouseY >= y && mouseY < y + MODE_BUTTON_H;
    }

    private static int getArrowX(ApplianceType type) {
        return switch (type) {
            case DRYER -> 79;
            case POT -> 104;
            case PRESSURECOOKER -> 80;
            default -> 79;
        };
    }

    private static int getArrowY(ApplianceType type) {
        return switch (type) {
            case DRYER -> 21;
            case POT -> 28;
            case PRESSURECOOKER -> 28;
            default -> 35;
        };
    }
}
