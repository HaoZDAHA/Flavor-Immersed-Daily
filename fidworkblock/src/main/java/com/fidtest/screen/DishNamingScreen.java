package com.fidtest.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DishNamingScreen extends Screen {

    private static final int[] PALETTE = {0xFFFFFF, 0xFF5555, 0x55FF55, 0xFFFF55, 0x5555FF, 0xFF55FF, 0x55FFFF, 0xAA0000, 0x00AA00, 0xFFAA00, 0x0000AA, 0xAAAAAA};

    private final InteractionHand hand;
    private EditBox nameInput, descInput;
    private int selectedColor = 0xFFFFFF;

    public DishNamingScreen(InteractionHand hand) {
        super(Component.translatable("screen.fidworkblock.dish_naming"));
        this.hand = hand;
    }

    @Override
    protected void init() {
        ItemStack held = minecraft.player.getItemInHand(hand);

        nameInput = new EditBox(font, width / 2 - 100, height / 2 - 10, 200, 20, Component.translatable("screen.fidworkblock.dish_name_input"));
        nameInput.setMaxLength(50);
        nameInput.setValue(held.hasCustomHoverName() ? held.getHoverName().getString() : "");
        nameInput.setTextColor(selectedColor);
        nameInput.setFocused(true);
        addWidget(nameInput); setInitialFocus(nameInput);

        descInput = new EditBox(font, width / 2 - 100, height / 2 + 30, 200, 20, Component.translatable("screen.fidworkblock.dish_desc_input"));
        descInput.setMaxLength(100);
        descInput.setValue(com.fidtest.item.DishItem.getDishDescription(held));
        addWidget(descInput);
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float pt) {
        super.render(g, mx, my, pt);
        g.drawCenteredString(font, title, width / 2, height / 2 - 55, 0xFFFFFF);
        g.drawCenteredString(font, Component.translatable("screen.fidworkblock.dish_color"), width / 2, height / 2 - 42, 0xAAAAAA);
        int px = width / 2 - (PALETTE.length * 16) / 2, py = height / 2 - 35;
        for (int i = 0; i < PALETTE.length; i++) {
            int c = PALETTE[i];
            g.fill(px + i * 16, py, px + i * 16 + 12, py + 12, 0xFF000000 | c);
            if (c == selectedColor) g.renderOutline(px + i * 16 - 1, py - 1, 14, 14, 0xFFFFFFFF);
        }
        nameInput.render(g, mx, my, pt);
        g.drawString(font, Component.translatable("screen.fidworkblock.dish_desc"), width / 2 - 100, height / 2 + 18, 0xAAAAAA);
        descInput.render(g, mx, my, pt);
        g.drawCenteredString(font, Component.translatable("screen.fidworkblock.dish_naming_hint"), width / 2, height / 2 + 60, 0x888888);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        int px = width / 2 - (PALETTE.length * 16) / 2, py = height / 2 - 35;
        for (int i = 0; i < PALETTE.length; i++) {
            if (mx >= px + i * 16 && mx <= px + i * 16 + 12 && my >= py && my <= py + 12) {
                selectedColor = PALETTE[i]; nameInput.setTextColor(selectedColor); return true;
            }
        }
        return super.mouseClicked(mx, my, btn);
    }

    @Override
    public boolean keyPressed(int key, int scan, int mod) {
        if (key == 257 && !nameInput.getValue().isEmpty()) {
            ItemStack held = minecraft.player.getItemInHand(hand);
            com.fidtest.item.DishItem.setDishName(held, nameInput.getValue(), selectedColor);
            com.fidtest.item.DishItem.setDishDescription(held, descInput.getValue());
            com.fidtest.item.DishItem.setChefName(held, minecraft.player.getName().getString());
            minecraft.setScreen(null);
            return true;
        }
        if (key == 256) { minecraft.setScreen(null); return true; }
        return super.keyPressed(key, scan, mod);
    }

    @Override public boolean isPauseScreen() { return false; }
}
