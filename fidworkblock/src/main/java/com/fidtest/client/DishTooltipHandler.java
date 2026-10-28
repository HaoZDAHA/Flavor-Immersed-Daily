package com.fidtest.client;

import com.fidtest.item.DishItem;
import com.mojang.datafixers.util.Either;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = "fidworkblock", value = Dist.CLIENT)
public class DishTooltipHandler {

    @SubscribeEvent
    public static void onGatherTooltip(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof DishItem)) return;
        List<String> names = DishItem.getItemNames(stack);
        if (!names.isEmpty()) event.getTooltipElements().add(1,
                Either.right(new DishIngredientsTooltip.Data(names)));
    }
}
