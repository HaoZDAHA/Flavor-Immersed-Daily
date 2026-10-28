package com.fidtest.client;

import com.fidtest.block.FryingpanBlock;
import com.mojang.datafixers.util.Either;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

@EventBusSubscriber(modid = "fidworkblock", value = Dist.CLIENT)
public class FryingpanTooltipHandler {

    @SubscribeEvent
    public static void onGatherTooltip(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof FryingpanBlock) {
                event.getTooltipElements().add(
                        Either.left(Component.translatable("tooltip.fidworkblock.fryingpan_desc1")));
                event.getTooltipElements().add(
                        Either.left(Component.translatable("tooltip.fidworkblock.fryingpan_desc2")));
            }
        }
    }
}
