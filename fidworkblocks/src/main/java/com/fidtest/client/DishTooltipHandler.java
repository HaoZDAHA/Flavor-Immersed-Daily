package com.fidtest.client;

import com.fidtest.item.DishItem;
import com.mojang.datafixers.util.Either;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

import java.util.List;

@EventBusSubscriber(modid = "fidworkblock", value = Dist.CLIENT)
public class DishTooltipHandler {

    @SubscribeEvent
    public static void onGatherTooltip(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof DishItem)) return;

        List<String> names = DishItem.getItemNames(stack);
        if (!names.isEmpty()) {
            event.getTooltipElements().add(
                    Either.right(new DishIngredientsTooltip.Data(names)));
        }
    }
}
