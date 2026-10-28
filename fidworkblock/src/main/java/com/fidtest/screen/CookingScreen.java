package com.fidtest.screen;

import com.fidtest.FidworkMod;
import com.fidtest.recipe.ApplianceType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CookingScreen extends AbstractContainerScreen<CookingMenu> {

    private final ApplianceType appliance;
    private final ResourceLocation texture;
    private final ResourceLocation progressTex;

// 变温烤箱模式按钮（位于容器主体与背包之间的空白带，坐标尺寸沿用 1.21.1 版）
    private static final int MODE_BUTTON_W = 36;
    private static final int MODE_BUTTON_H = 13;
// 烘干模式按钮
    private static final int MODE_BUTTON_X1 = 50;
// 烤箱模式按钮
    private static final int MODE_BUTTON_X2 = 90;
    private static final int MODE_BUTTON_Y = 60;

    public CookingScreen(CookingMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.appliance = menu.getAppliance();
        this.texture = new ResourceLocation(FidworkMod.MODID,
                "textures/gui/container/" + appliance.getName() + ".png");
        this.progressTex = new ResourceLocation(FidworkMod.MODID,
                "textures/gui/container/" + appliance.getName() + "_progress.png");
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
// 仅变温烤箱需要模式切换按钮
        if (appliance == ApplianceType.DRYER) {
            addRenderableWidget(new ModeButton(this.leftPos + MODE_BUTTON_X1, this.topPos + MODE_BUTTON_Y, 0,
                    Component.translatable("screen.fidworkblock.dryer_mode_dry")));
            addRenderableWidget(new ModeButton(this.leftPos + MODE_BUTTON_X2, this.topPos + MODE_BUTTON_Y, 1,
                    Component.translatable("screen.fidworkblock.dryer_mode_oven")));
        }
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float pt) {
        super.render(g, mx, my, pt);
        this.renderTooltip(g, mx, my);
    }

    @Override
    protected void renderBg(GuiGraphics g, float pt, int mx, int my) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        g.blit(texture, x, y, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

        if (!appliance.isInstant()) {
            int p = this.menu.getCookingProgress();
            int t = Math.max(this.menu.getCookingTotalTime(), 1);
            int w = (int) (24F * p / t);
            int ax = appliance == ApplianceType.PRESSURECOOKER ? 80 : appliance == ApplianceType.POT ? 104 : 79;
            int ay = getArrowY(appliance);
            if (w > 0) g.blit(progressTex, x + ax, y + ay, 0, 0, w, 17, 24, 17);
        }

        if (appliance.needsFuel() && this.menu.getHeatLevel() > 0) {
            g.blit(texture, x + 20, y + 38, 176, 17, 14, 14, this.imageWidth, this.imageHeight);
        }
    }

    private static int getArrowY(ApplianceType type) {
        return switch (type) {
            case DRYER -> 21;
            case POT -> 28;
            case PRESSURECOOKER -> 28;
            default -> 35;
        };
    }

// 变温烤箱模式按钮：选中的模式用亮黄色文字高亮
    private class ModeButton extends Button {

        private final int mode;

        private ModeButton(int x, int y, int mode, Component label) {
            super(x, y, MODE_BUTTON_W, MODE_BUTTON_H, label,
                    b -> CookingScreen.this.minecraft.gameMode
                            .handleInventoryButtonClick(CookingScreen.this.menu.containerId, mode),
                    Button.DEFAULT_NARRATION);
            this.mode = mode;
        }

        @Override
        public void renderString(GuiGraphics g, Font font, int color) {
            super.renderString(g, font,
                    CookingScreen.this.menu.getDryerMode() == this.mode ? 0xFFFFA0 : color);
        }
    }

}
