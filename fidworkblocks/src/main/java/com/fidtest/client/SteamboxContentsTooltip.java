package com.fidtest.client;

import com.fidtest.item.SteamboxPackedEntry;
import com.mojang.blaze3d.vertex.PoseStack;
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

public class SteamboxContentsTooltip {

    public record Data(List<SteamboxPackedEntry> entries) implements TooltipComponent {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof Data d && d.entries.equals(this.entries);
        }

        @Override
        public int hashCode() {
            return entries.hashCode();
        }
    }

    public static class Client implements ClientTooltipComponent {
        private final List<ItemStack> items;
        private final List<Integer> counts;

        public Client(Data data) {
            this.items = new ArrayList<>();
            this.counts = new ArrayList<>();
            for (SteamboxPackedEntry entry : data.entries()) {
                try {
                    ResourceLocation id = ResourceLocation.parse(entry.itemId());
                    var item = BuiltInRegistries.ITEM.get(id);
                    if (item != null) {
                        items.add(new ItemStack(item));
                        counts.add(entry.count());
                    }
                } catch (Exception ignored) {}
            }
        }

        @Override
        public int getHeight() {
            return items.isEmpty() ? 0 : 20;
        }

        @Override
        public int getWidth(Font font) {
            return items.size() * 20;
        }

        @Override
        public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
            for (int i = 0; i < items.size(); i++) {
                int slotX = x + i * 20;
                guiGraphics.renderItem(items.get(i), slotX, y, 0);

                int count = counts.get(i);
                if (count > 1) {
                    String text = String.valueOf(count);
                    Font mcFont = Minecraft.getInstance().font;
                    int textWidth = mcFont.width(text);

                    PoseStack pose = guiGraphics.pose();
                    pose.pushPose();
                    pose.translate(slotX + 17 - textWidth * 0.5f, y + 11, 200);
                    pose.scale(0.5f, 0.5f, 0.5f);
                    guiGraphics.drawString(mcFont, text, 0, 0, 0xFFFFFF, true);
                    pose.popPose();
                }
            }
        }
    }
}
