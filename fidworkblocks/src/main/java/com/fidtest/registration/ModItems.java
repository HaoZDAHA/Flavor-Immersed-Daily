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

    public static final DeferredItem<com.fidtest.item.TablewareItem> PLATE = ITEMS.register("plate",
            () -> new com.fidtest.item.TablewareItem(new Item.Properties(), com.fidtest.block.entity.TablewareBlockEntity.Kind.PLATE));
    public static final DeferredItem<com.fidtest.item.TablewareItem> LONG_PLATE = ITEMS.register("long_plate",
            () -> new com.fidtest.item.TablewareItem(new Item.Properties(), com.fidtest.block.entity.TablewareBlockEntity.Kind.LONG_PLATE));
    public static final DeferredItem<com.fidtest.item.TablewareItem> SMALL_PLATE = ITEMS.register("small_plate",
            () -> new com.fidtest.item.TablewareItem(new Item.Properties(), com.fidtest.block.entity.TablewareBlockEntity.Kind.SAUCER));
    public static final DeferredItem<com.fidtest.item.ChopsticksItem> CHOPSTICKS = ITEMS.register("chopsticks",
            () -> new com.fidtest.item.ChopsticksItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<DishItem> DISH =
            ITEMS.register("dish", () -> new DishItem(new Item.Properties()));

    public static final DeferredItem<StartBookItem> STARTBOOK =
            ITEMS.register("startbook", StartBookItem::new);
}
