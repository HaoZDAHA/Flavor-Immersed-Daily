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

    public record Data(List<String> itemIds) implements TooltipComponent {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof Data d && d.itemIds.equals(this.itemIds);
        }

        @Override
        public int hashCode() {
            return itemIds.hashCode();
        }
    }

    public static class Client implements ClientTooltipComponent {
        private final List<ItemStack> items;

        public Client(Data data) {
            this.items = new ArrayList<>();
            for (String idStr : data.itemIds()) {
                try {
                    ResourceLocation id = ResourceLocation.parse(idStr);
                    var item = BuiltInRegistries.ITEM.get(id);
                    if (item != null) {
                        items.add(new ItemStack(item));
                    }
                } catch (Exception ignored) {}
            }
        }

        @Override
        public int getHeight() {
            return items.isEmpty() ? 0 : 18;
        }

        @Override
        public int getWidth(Font font) {
            return items.size() * 18;
        }

        @Override
        public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
            int offsetX = 0;
            for (ItemStack stack : items) {
                guiGraphics.renderItem(stack, x + offsetX, y, 0);
                offsetX += 18;
            }
        }
    }
}
