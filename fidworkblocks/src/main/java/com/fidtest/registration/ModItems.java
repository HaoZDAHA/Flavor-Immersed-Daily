package com.fidtest.registration;

import com.fidtest.ExampleMod;
import com.fidtest.item.DishItem;
import com.fidtest.item.StartBookItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(ExampleMod.MODID);

    public static final DeferredItem<DishItem> DISH =
            ITEMS.register("dish", () -> new DishItem(new Item.Properties()));

    public static final DeferredItem<StartBookItem> STARTBOOK =
            ITEMS.register("startbook", StartBookItem::new);
}
