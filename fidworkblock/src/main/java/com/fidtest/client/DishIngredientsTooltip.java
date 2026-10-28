package com.fidtest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DishIngredientsTooltip {

    public record Data(List<String> itemIds) implements TooltipComponent {}

    public static class Client implements ClientTooltipComponent {
        private final List<ItemStack> items = new ArrayList<>();

        public Client(Data data) {
            for (String id : data.itemIds) try {
                var item = BuiltInRegistries.ITEM.get(new ResourceLocation(id));
                if (item != null) items.add(new ItemStack(item));
            } catch (Exception ignored) {}
        }

        @Override public int getHeight() { return items.isEmpty() ? 0 : 18; }
        @Override public int getWidth(Font font) { return items.size() * 18; }

        @Override
        public void renderImage(Font font, int x, int y, GuiGraphics g) {
            int off = 0;
            for (ItemStack s : items) { g.renderItem(s, x + off, y, 0); g.renderItemDecorations(font, s, x + off, y); off += 18; }
        }
    }
}
