package com.fidtest.screen;

import com.fidtest.network.DishNamePayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

public class DishNamingScreen extends Screen {

    private static final int[] PALETTE = {
            0xFFFFFF, 0xFF5555, 0x55FF55, 0xFFFF55,
            0x5555FF, 0xFF55FF, 0x55FFFF, 0xAA0000,
            0x00AA00, 0xFFAA00, 0x0000AA, 0xAAAAAA
    };

    private final int slot;
    private final String currentName;
    private final String currentDesc;
    private final int currentColor;

    private EditBox nameInput;
    private EditBox descInput;
    private int selectedColor;

    public DishNamingScreen(int slot, String currentName, String currentDesc, int currentColor) {
        super(Component.translatable("screen.fidworkblock.dish_naming"));
        this.slot = slot;
        this.currentName = currentName;
        this.currentDesc = currentDesc;
        this.currentColor = currentColor;
    }

    @Override
    protected void init() {
        super.init();
        this.selectedColor = currentColor;

        this.nameInput = new EditBox(this.font, this.width / 2 - 100, this.height / 2 - 10, 200, 20,
                Component.translatable("screen.fidworkblock.dish_name_input"));
        this.nameInput.setMaxLength(50);
        this.nameInput.setValue(currentName);
        this.nameInput.setTextColor(selectedColor);
        this.nameInput.setFocused(true);
        this.addWidget(this.nameInput);
        this.setInitialFocus(this.nameInput);

        this.descInput = new EditBox(this.font, this.width / 2 - 100, this.height / 2 + 30, 200, 20,
                Component.translatable("screen.fidworkblock.dish_desc_input"));
        this.descInput.setMaxLength(100);
        this.descInput.setValue(currentDesc);
        this.addWidget(this.descInput);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(this.font,
                Component.translatable("screen.fidworkblock.dish_naming"),
                this.width / 2, this.height / 2 - 55, 0xFFFFFF);

        guiGraphics.drawCenteredString(this.font,
                Component.translatable("screen.fidworkblock.dish_color"),
                this.width / 2, this.height / 2 - 42, 0xAAAAAA);

        int paletteStartX = this.width / 2 - (PALETTE.length * 16) / 2;
        int paletteY = this.height / 2 - 35;
        for (int i = 0; i < PALETTE.length; i++) {
            int bx = paletteStartX + i * 16;
            int color = PALETTE[i];
            guiGraphics.fill(bx, paletteY, bx + 12, paletteY + 12, 0xFF000000 | color);
            if (color == selectedColor) {
                guiGraphics.renderOutline(bx - 1, paletteY - 1, 14, 14, 0xFFFFFFFF);
            }
        }

        this.nameInput.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawString(this.font,
                Component.translatable("screen.fidworkblock.dish_desc"),
                this.width / 2 - 100, this.height / 2 + 18, 0xAAAAAA);

        this.descInput.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(this.font,
                Component.translatable("screen.fidworkblock.dish_naming_hint"),
                this.width / 2, this.height / 2 + 60, 0x888888);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int paletteStartX = this.width / 2 - (PALETTE.length * 16) / 2;
        int paletteY = this.height / 2 - 35;
        for (int i = 0; i < PALETTE.length; i++) {
            int bx = paletteStartX + i * 16;
            if (mouseX >= bx && mouseX <= bx + 12 && mouseY >= paletteY && mouseY <= paletteY + 12) {
                selectedColor = PALETTE[i];
                nameInput.setTextColor(selectedColor);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 && !this.nameInput.getValue().isEmpty()) {
            PacketDistributor.sendToServer(new DishNamePayload(
                    slot, this.nameInput.getValue(), this.descInput.getValue(), selectedColor));
            this.minecraft.setScreen(null);
            return true;
        }
        if (keyCode == 256) {
            this.minecraft.setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
