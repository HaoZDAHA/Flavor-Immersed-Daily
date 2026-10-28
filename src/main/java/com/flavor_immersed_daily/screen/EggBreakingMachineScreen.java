package com.flavor_immersed_daily.screen;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.recipe.EggBreakingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class EggBreakingMachineScreen extends AbstractContainerScreen<EggBreakingMachineMenu> {

    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "textures/gui/egg_breaking_machine.png");

// 配方列表区域偏移
    private static final int LIST_X = 98;
    private static final int LIST_Y = 17;
    private static final int ENTRY_SIZE = 20;
    private static final int VISIBLE_ENTRIES = 3;

    private float scrollOffset;
    private boolean scrolling;

    public EggBreakingMachineScreen(EggBreakingMachineMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
        renderRecipeList(guiGraphics, leftPos, topPos, mouseX, mouseY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

// 配方条目 tooltip
        List<EggBreakingRecipe> recipes = this.menu.getMatchingRecipes();
        int offset = (int) this.scrollOffset;
        for (int i = offset; i < offset + VISIBLE_ENTRIES && i < recipes.size(); i++) {
            int x = leftPos + LIST_X;
            int y = topPos + LIST_Y + (i - offset) * ENTRY_SIZE;
            if (mouseX >= x && mouseX < x + 18 && mouseY >= y && mouseY < y + 18) {
                ItemStack result = recipes.get(i).getResults().get(0);
                guiGraphics.renderTooltip(this.font, result, mouseX, mouseY);
            }
        }
    }

    private void renderRecipeList(GuiGraphics guiGraphics, int left, int top, int mouseX, int mouseY) {
        List<EggBreakingRecipe> recipes = this.menu.getMatchingRecipes();
        int selectedIdx = this.menu.getSelectedRecipeIndex();
        int maxOffset = Math.max(0, recipes.size() - VISIBLE_ENTRIES);

// clamp scroll
        if (this.scrollOffset > maxOffset) {
            this.scrollOffset = maxOffset;
        }
        if (this.scrollOffset < 0) {
            this.scrollOffset = 0;
        }

        int offset = (int) this.scrollOffset;
        for (int i = offset; i < offset + VISIBLE_ENTRIES && i < recipes.size(); i++) {
            int x = left + LIST_X;
            int y = top + LIST_Y + (i - offset) * ENTRY_SIZE;

// 选中高亮
            if (i == selectedIdx) {
                guiGraphics.fill(x - 1, y - 1, x + 17, y + 17, 0x80_FF_FF_00);
            }

            ItemStack result = recipes.get(i).getResults().get(0);
            guiGraphics.renderItem(result, x, y);
            guiGraphics.renderItemDecorations(this.font, result, x, y);
        }

// 滚动条指示
        if (recipes.size() > VISIBLE_ENTRIES) {
            int scrollAreaHeight = VISIBLE_ENTRIES * ENTRY_SIZE;
            int barX = left + LIST_X + 20;
            int barY = top + LIST_Y;
            float ratio = recipes.size() > VISIBLE_ENTRIES
                    ? (float) offset / (recipes.size() - VISIBLE_ENTRIES) : 0;
            int barTop = barY + (int) (ratio * (scrollAreaHeight - 8));
            guiGraphics.fill(barX, barY, barX + 2, barY + scrollAreaHeight, 0xFF_55_55_55);
            guiGraphics.fill(barX, barTop, barX + 2, barTop + 8, 0xFF_CC_CC_CC);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrolling = false;

        List<EggBreakingRecipe> recipes = this.menu.getMatchingRecipes();
        if (recipes.isEmpty()) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        int offset = (int) this.scrollOffset;
        for (int i = offset; i < offset + VISIBLE_ENTRIES && i < recipes.size(); i++) {
            int x = leftPos + LIST_X;
            int y = topPos + LIST_Y + (i - offset) * ENTRY_SIZE;
            if (mouseX >= x && mouseX < x + 18 && mouseY >= y && mouseY < y + 18) {
                if (this.menu.clickMenuButton(this.minecraft.player, i)) {
                    Minecraft.getInstance().gameMode.handleInventoryButtonClick(this.menu.containerId, i);
                    return true;
                }
            }
        }

// 滚轮区域检测
        int barX = leftPos + LIST_X + 20;
        int barY = topPos + LIST_Y;
        int scrollAreaHeight = VISIBLE_ENTRIES * ENTRY_SIZE;
        if (mouseX >= barX && mouseX < barX + 6 && mouseY >= barY && mouseY < barY + scrollAreaHeight) {
            this.scrolling = true;
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrolling && this.menu.getMatchingRecipes().size() > VISIBLE_ENTRIES) {
            int scrollAreaHeight = VISIBLE_ENTRIES * ENTRY_SIZE;
            int barY = topPos + LIST_Y;
            int maxOffset = this.menu.getMatchingRecipes().size() - VISIBLE_ENTRIES;
            float ratio = (float) (mouseY - barY) / scrollAreaHeight;
            this.scrollOffset = Math.round(ratio * maxOffset);
            this.scrollOffset = Mth.clamp(this.scrollOffset, 0, maxOffset);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
        int maxOffset = Math.max(0, this.menu.getMatchingRecipes().size() - VISIBLE_ENTRIES);
        if (maxOffset > 0) {
            this.scrollOffset = (float) (this.scrollOffset - scrollY);
            this.scrollOffset = Mth.clamp(this.scrollOffset, 0, maxOffset);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollY);
    }
}
