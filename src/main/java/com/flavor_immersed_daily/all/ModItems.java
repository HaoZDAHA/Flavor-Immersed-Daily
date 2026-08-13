package com.flavor_immersed_daily.all;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.item.RareFruitVariantItem;
import com.flavor_immersed_daily.item.SeedableFruitItem;
import com.flavor_immersed_daily.item.ThrowableFruitItem;
import com.flavor_immersed_daily.item.WildHarvestItem;
import com.flavor_immersed_daily.item.WindowPaperItem;
import com.flavor_immersed_daily.item.FairySparklerItem;
import com.flavor_immersed_daily.item.PurifiedWaterBucketItem;
import com.flavor_immersed_daily.item.CoarseClothItem;
import com.flavor_immersed_daily.item.KitchenScissorsItem;
import com.flavor_immersed_daily.item.ChineseLeavesItem;
import com.flavor_immersed_daily.item.WrestlingGunItem;
import com.flavor_immersed_daily.item.SeasoningItem;
import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.client.tooltip.TooltipItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public final class ModItems {
    private ModItems() {
    }

    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(FlavorImmersedDaily.MODID);

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

    private static DeferredItem<Item> item(String name) {
        return REGISTRY.register(name, () -> new Item(new Item.Properties()));
    }

    private static DeferredItem<Item> food(String name, int nutrition, float saturationModifier) {
        return REGISTRY.register(name, () -> new Item(new Item.Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition)
                        .saturationModifier(saturationModifier)
                        .alwaysEdible()
                        .build())));
    }

    private static DeferredItem<Item> drink(String name) {
        return food(name, 4, 0.3f);
    }

    private static DeferredItem<Item> rareFruit(String name, int nutrition, float saturationModifier) {
        return REGISTRY.register(name, () -> new RareFruitVariantItem(new Item.Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition)
                        .saturationModifier(saturationModifier)
                        .alwaysEdible()
                        .build())));
    }

    private static DeferredItem<Item> seedableFruit(String name, String seedId) {
        return REGISTRY.register(name, () -> new SeedableFruitItem(new Item.Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(2)
                        .saturationModifier(0.2f)
                        .alwaysEdible()
                        .build()), seedId));
    }

    private static DeferredItem<Item> wildHarvest(String name, String tooltipKey) {
        return REGISTRY.register(name, () -> new WildHarvestItem(new Item.Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(3)
                        .saturationModifier(0.3f)
                        .alwaysEdible()
                        .build()), Component.translatable(tooltipKey)));
    }

    // Popsicles
    public static final DeferredItem<Item> APPLEPOPSICLE = REGISTRY.register("applepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> APRICOTPOPSICLE = REGISTRY.register("apricotpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> BANANAPOPSICLE = REGISTRY.register("bananapopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> BLUEBERRYPOPSICLE = REGISTRY.register("blueberrypopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> CARAMBOLAPOPSICLE = REGISTRY.register("carambolapopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> CHERRYPOPSICLE = REGISTRY.register("cherrypopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> CHOCOLATEPOPSICLE = REGISTRY.register("chocolatepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COCONUTPOPSICLE = REGISTRY.register("coconutpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COFFEEPOPSICLE = REGISTRY.register("coffeepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> DRAGONFRUITPOPSICLE = REGISTRY.register("dragonfruitpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> DURIANPOPSICLE = REGISTRY.register("durianpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> GRAPEPOPSICLE = REGISTRY.register("grapepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> GREENGRAPEPOPSICLE = REGISTRY.register("greengrapepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> GREENPLUMPOPSICLE = REGISTRY.register("greenplumpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> HAMIMELONPOPSICLE = REGISTRY.register("hamimelonpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> HAWTHORNPOPSICLE = REGISTRY.register("hawthornpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> HONEYPEACHPOPSICLE = REGISTRY.register("honeypeachpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> KIWIFRUITPOPSICLE = REGISTRY.register("kiwifruitpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> LEMONPOPSICLE = REGISTRY.register("lemonpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> LOQUATPOPSICLE = REGISTRY.register("loquatpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> LYCHEEPOPSICLE = REGISTRY.register("lycheepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> MANGOPOPSICLE = REGISTRY.register("mangopopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> MANGOSTEEPOPSICLE = REGISTRY.register("mangosteenpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> MULBERRYPOPSICLE = REGISTRY.register("mulberrypopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> NECTARINEPOPSICLE = REGISTRY.register("nectarinepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> ORANGEPOPSICLE = REGISTRY.register("orangepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> PAWPAWPOPSICLE = REGISTRY.register("pawpawpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> PEARPOPSICLE = REGISTRY.register("pearpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> PINEAPPLEPOPSICLE = REGISTRY.register("pineapplepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> PLUMPOPSICLE = REGISTRY.register("plumpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> POMEGRANATEPOPSICLE = REGISTRY.register("pomegranatepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> POPSICLE = REGISTRY.register("popsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> STRAWBERRYPOPSICLE = REGISTRY.register("strawberrypopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> SWEETBERRYPOPSICLE = REGISTRY.register("sweetberrypopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> SWEETMELONPOPSICLE = REGISTRY.register("sweetmelonpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> TANGERINEPOPSICLE = REGISTRY.register("tangerinepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> WATERMELONPOPSICLE = REGISTRY.register("watermelonpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> WINTERJUJUBEPOPSICLE = REGISTRY.register("winterjujubepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    // ========== Ice Cream ϵ��ʳƷ ==========
    public static final DeferredItem<Item> APPLEICECREAM = REGISTRY.register("appleicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> APRICOTICECREAM = REGISTRY.register("apricoticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> BANANAICECREAM = REGISTRY.register("bananaicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> BLUEBERRYICECREAM = REGISTRY.register("blueberryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> CARAMBOLAICECREAM = REGISTRY.register("carambolaicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> CHERRYICECREAM = REGISTRY.register("cherryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> CHOCOLATEICECREAM = REGISTRY.register("chocolateicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COCONUTICECREAM = REGISTRY.register("coconuticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COFFEEICECREAM = REGISTRY.register("coffeeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> DRAGONFRUITICECREAM = REGISTRY.register("dragonfruiticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> DURIANICECREAM = REGISTRY.register("durianicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> GRAPEICECREAM = REGISTRY.register("grapeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> GREENGRAPEICECREAM = REGISTRY.register("greengrapeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> GREENPLUMICECREAM = REGISTRY.register("greenplumicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> HAMIMELONICECREAM = REGISTRY.register("hamimelonicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> HAWTHORNICECREAM = REGISTRY.register("hawthornicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> HONEYPEACHICECREAM = REGISTRY.register("honeypeachicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> KIWIFRUITICECREAM = REGISTRY.register("kiwifruiticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> LEMONICECREAM = REGISTRY.register("lemonicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> LOQUATICECREAM = REGISTRY.register("loquaticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> LYCHEEICECREAM = REGISTRY.register("lycheeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> MANGOICECREAM = REGISTRY.register("mangoicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> MANGOSTEENICECREAM = REGISTRY.register("mangosteenicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> MILKICECREAM = REGISTRY.register("milkicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> MULBERRYICECREAM = REGISTRY.register("mulberryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> NECTARINEICECREAM = REGISTRY.register("nectarineicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> ORANGEICECREAM = REGISTRY.register("orangeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> PAWPAWICECREAM = REGISTRY.register("pawpawicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> PEARICECREAM = REGISTRY.register("pearicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> PINEAPPLEICECREAM = REGISTRY.register("pineappleicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> PLUMICECREAM = REGISTRY.register("plumicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> POMEGRANATEICECREAM = REGISTRY.register("pomegranateicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> STRAWBERRYICECREAM = REGISTRY.register("strawberryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> SWEETBERRYICECREAM = REGISTRY.register("sweetberryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> SWEETMELONICECREAM = REGISTRY.register("sweetmelonicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> TANGERINEICECREAM = REGISTRY.register("tangerineicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> WATERMELONICECREAM = REGISTRY.register("watermelonicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> WINTERJUJUBEICECREAM = REGISTRY.register("winterjujubeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    // Grains, mushrooms, tea, and crop products
    public static final DeferredItem<Item> KAOLIANGGRAIN = REGISTRY.register("kaolianggrain",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> WHITEMUSHROOM = REGISTRY.register("whitemushroom",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    public static final DeferredItem<Item> BLACKFUNGUS = REGISTRY.register("blackfungus",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> DRYBLACKFUNGUS = REGISTRY.register("dryblackfungus", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLEUROTUSERYNGII = REGISTRY.register("pleurotuseryngii",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(3).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> ENOKIMUSHROOM = REGISTRY.register("enokimushroom",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    public static final DeferredItem<Item> TREMELLA = REGISTRY.register("tremella",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRAGRANTMUSHROOM = REGISTRY.register("fragrantmushroom",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(3).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> DRYFRAGRANTMUSHROOM = REGISTRY.register("dryfragrantmushroom", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GREENTEALEAVES = REGISTRY.register("greentealeaves", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRYGREENTEA = REGISTRY.register("drygreentea", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> REDTEALEAVES = REGISTRY.register("redtealeaves", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRYREDTEA = REGISTRY.register("dryredtea", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LOTUSROOT = REGISTRY.register("lotusroot",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(3).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> PADDYGRAIN = REGISTRY.register("paddygrain", () -> new Item(new Item.Properties()));

    // Drinks
    public static final DeferredItem<Item> DRINKINGWATER = drink("drinkingwater");
    public static final DeferredItem<Item> CAPPUCCINO = drink("cappuccino");
    public static final DeferredItem<Item> CAFELATTE = drink("cafelatte");
    public static final DeferredItem<Item> MOCHACAFE = drink("mochacafe");
    public static final DeferredItem<Item> HOTWATER = drink("hotwater");
    public static final DeferredItem<Item> CAFE_AMERICANO = drink("cafe_americano");
    public static final DeferredItem<Item> LEMONREDTEA = drink("lemonredtea");
    public static final DeferredItem<Item> GRENTEAMILKWITHPEARL = drink("grenteamilkwithpearl");
    public static final DeferredItem<Item> GREEN_TEA_LATTES = drink("green_tea_lattes");
    public static final DeferredItem<Item> LEMONTEAWITHGLUTINOUSRICEFLAVOR = drink("lemonteawithglutinousriceflavor");
    public static final DeferredItem<Item> REDTEA = drink("redtea");
    public static final DeferredItem<Item> GREENTEA = drink("greentea");
    public static final DeferredItem<Item> BUCKWHEATTEA = drink("buckwheattea");
    public static final DeferredItem<Item> ASSAMMILK_TEA = drink("assammilk_tea");
    public static final DeferredItem<Item> GREENPLUMTEA = drink("greenplumtea");
    public static final DeferredItem<Item> REDTEAMILKWITHPEARL = drink("redteamilkwithpearl");
    public static final DeferredItem<Item> KAOLIANGWINE = drink("kaoliangwine");
    public static final DeferredItem<Item> KWEICHOW_MOUTAI = drink("kweichow_moutai");
    public static final DeferredItem<Item> TSINGTAO_BEER = drink("tsingtao_beer");
    public static final DeferredItem<Item> ICEDBLACKTEABLUE = drink("icedblackteablue");
    public static final DeferredItem<Item> COLA = drink("cola");
    public static final DeferredItem<Item> SUGARFREICEDTEA = drink("sugarfreicedtea");
    public static final DeferredItem<Item> SPRITE = drink("sprite");

    // Fruit
    public static final DeferredItem<Item> APIECEOFBANANA = food("apieceofbanana", 4, 0.3f);
    public static final DeferredItem<Item> BANANA = food("banana", 4, 0.3f);
    public static final DeferredItem<Item> PULLEDBANANA_2 = food("pulledbanana_2", 6, 0.6f);
    public static final DeferredItem<Item> ORANGE = food("orange", 4, 0.3f);
    public static final DeferredItem<Item> BLOODORANGE = rareFruit("bloodorange", 6, 0.5f);
    public static final DeferredItem<Item> TANGERINE = food("tangerine", 4, 0.3f);
    public static final DeferredItem<Item> TANGERINE_1 = rareFruit("tangerine_1", 6, 0.5f);
    public static final DeferredItem<Item> UGLYORANGE = rareFruit("uglyorange", 6, 0.5f);
    public static final DeferredItem<Item> LEMON = food("lemon", 3, 0.2f);
    public static final DeferredItem<Item> HAMIMELON = food("hamimelon", 6, 0.6f);
    public static final DeferredItem<Item> SWEETMELON = food("sweetmelon", 6, 0.6f);
    public static final DeferredItem<Item> SWEETMELON_1 = rareFruit("sweetmelon_1", 8, 0.8f);
    public static final DeferredItem<Item> COCONUT = REGISTRY.register("coconut", () -> new ThrowableFruitItem(new Item.Properties(), "flavor_immersed_daily:coconut_shell", 1, "flavor_immersed_daily:coconutmeat", 2, "", 0, 4.0f));
    public static final DeferredItem<Item> COCONUTMEAT = food("coconutmeat", 6, 0.6f);
    public static final DeferredItem<Item> COCONUT_SHELL = food("coconut_shell", 2, 0.3f);
    public static final DeferredItem<Item> DURIAN = REGISTRY.register("durian", () -> new ThrowableFruitItem(new Item.Properties(), "flavor_immersed_daily:durianmeat", 2, "flavor_immersed_daily:durianshellhat", 1, "", 0, 6.0f));
    public static final DeferredItem<Item> DURIANMEAT = food("durianmeat", 8, 0.8f);
    public static final DeferredItem<Item> DURIANSHELLHAT = REGISTRY.register("durianshellhat", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLUEBERRY = food("blueberry", 2, 0.2f);
    public static final DeferredItem<Item> GRAPE = seedableFruit("grape", "flavor_immersed_daily:agrape");
    public static final DeferredItem<Item> GREENGRAPE = seedableFruit("greengrape", "flavor_immersed_daily:agreengrape");
    public static final DeferredItem<Item> MULBERRY = food("mulberry", 2, 0.2f);
    public static final DeferredItem<Item> STRAWBERRY = food("strawberry", 2, 0.2f);
    public static final DeferredItem<Item> APRICOT = food("apricot", 4, 0.3f);
    public static final DeferredItem<Item> CHERRY = food("cherry", 3, 0.3f);
    public static final DeferredItem<Item> GREENPLUM = food("greenplum", 3, 0.3f);
    public static final DeferredItem<Item> HONEYPEACH = food("honeypeach", 4, 0.3f);
    public static final DeferredItem<Item> LIFEPEACH = rareFruit("lifepeach", 6, 0.5f);
    public static final DeferredItem<Item> NECTARINE = food("nectarine", 4, 0.3f);
    public static final DeferredItem<Item> PLUM = food("plum", 3, 0.3f);
    public static final DeferredItem<Item> WINTERJUJUBE = food("winterjujube", 4, 0.3f);
    public static final DeferredItem<Item> CARAMBOLA = food("carambola", 4, 0.3f);
    public static final DeferredItem<Item> DRAGONFRUIT = food("dragonfruit", 4, 0.3f);
    public static final DeferredItem<Item> KIWIFRUIT = food("kiwifruit", 4, 0.3f);
    public static final DeferredItem<Item> LYCHEE = food("lychee", 3, 0.3f);
    public static final DeferredItem<Item> MANGO = food("mango", 4, 0.3f);
    public static final DeferredItem<Item> MANGOSTEEN = food("mangosteen", 4, 0.3f);
    public static final DeferredItem<Item> PAWPAW = food("pawpaw", 4, 0.3f);
    public static final DeferredItem<Item> PINEAPPLE = food("pineapple", 5, 0.4f);
    public static final DeferredItem<Item> PISTACHIONUT = food("pistachionut", 3, 0.3f);
    public static final DeferredItem<Item> REDDATE = food("reddate", 4, 0.3f);
    public static final DeferredItem<Item> WALNUT = food("walnut", 3, 0.3f);
    public static final DeferredItem<Item> DRY_PISTACHIONUT = food("dry_pistachionut", 5, 0.5f);
    public static final DeferredItem<Item> PISTACHIONUT_0 = food("pistachionut_0", 3, 0.3f);
    public static final DeferredItem<Item> PISTACHIONUTSWITHOPENSHELLS = food("pistachionutswithopenshells", 3, 0.3f);
    public static final DeferredItem<Item> WALNUTKINNEL = food("walnutkinnel", 4, 0.4f);
    public static final DeferredItem<Item> GRAPESEED = REGISTRY.register("grapeseed", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CINNAMON = REGISTRY.register("cinnamon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GREENAPPLE = rareFruit("greenapple", 4, 0.3f);
    public static final DeferredItem<Item> HAWTHORN = food("hawthorn", 3, 0.3f);
    public static final DeferredItem<Item> LOQUAT = food("loquat", 4, 0.3f);
    public static final DeferredItem<Item> PEAR = food("pear", 4, 0.3f);
    public static final DeferredItem<Item> POMEGRANATE = food("pomegranate", 5, 0.4f);

    // Vine crop seeds and produce
    public static final DeferredItem<Item> CUCUMBERSEEDS = REGISTRY.register("cucumberseeds", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WAX_GOURD_SEED_BLOCK = REGISTRY.register("wax_gourd_seed_block", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> KIDNEYBEANSEED = REGISTRY.register("kidneybeanseed", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> AUBERGINESEEDBLOCK = REGISTRY.register("aubergineseedblock", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TOMATOSEED = REGISTRY.register("tomatoseed", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COWPEABEANSEED = REGISTRY.register("cowpeabeanseed", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GREENGRAESEED = REGISTRY.register("greengrapeseed", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LOOFAHSEED = REGISTRY.register("loofahseed", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CUCUMBER = food("cucumber", 3, 0.3f);
    public static final DeferredItem<Item> WAXGOURD = food("waxgourd", 3, 0.3f);
    public static final DeferredItem<Item> KIDNEYBEAN = food("kidneybean", 3, 0.3f);
    public static final DeferredItem<Item> AUBERGINE = food("aubergine", 3, 0.3f);
    public static final DeferredItem<Item> TOMATO = food("tomato", 3, 0.3f);
    public static final DeferredItem<Item> COWPEA = food("cowpea", 3, 0.3f);
    public static final DeferredItem<Item> LOOFAH = food("loofah", 3, 0.3f);

    // Wild harvests
    public static final DeferredItem<Item> TEMPERATEWILDFRUIT = wildHarvest("temperatewildfruit", "tooltip.flavor_immersed_daily.wild_fruit_harvest");
    public static final DeferredItem<Item> TROPICALWILD_FRUIT = wildHarvest("tropicalwild_fruit", "tooltip.flavor_immersed_daily.wild_fruit_harvest");
    public static final DeferredItem<Item> WILDFLOWERANDLEAF = wildHarvest("wildflowerandleaf", "tooltip.flavor_immersed_daily.wild_crop_harvest");
    public static final DeferredItem<Item> WILDFRUITINCOLDZONE = wildHarvest("wildfruitincoldzone", "tooltip.flavor_immersed_daily.wild_fruit_harvest");
    public static final DeferredItem<Item> WILDGRAINPLANT = wildHarvest("wildgrainplant", "tooltip.flavor_immersed_daily.wild_crop_harvest");
    public static final DeferredItem<Item> WILDMUSHROOMPLANT = wildHarvest("wildmushroomplant", "tooltip.flavor_immersed_daily.wild_crop_harvest");
    public static final DeferredItem<Item> WILDSEEDPLANT = wildHarvest("wildseedplant", "tooltip.flavor_immersed_daily.wild_crop_harvest");
    public static final DeferredItem<Item> WILDTUBERPLANTS = wildHarvest("wildtuberplants", "tooltip.flavor_immersed_daily.wild_crop_harvest");

    // Simple foods
    public static final DeferredItem<Item> BAKEDWHITEMUSHROOMSWITHCREAM = REGISTRY.register(
            "bakedwhitemushroomswithcream",
            () -> new Item(new Item.Properties()
                    .component(DataComponents.FOOD, new FoodProperties.Builder()
                            .nutrition(8)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    public static final DeferredItem<Item> BRAISEDBEANSPROUTSWITHVERMICELLI = REGISTRY.register("braisedbeansproutswithvermicelli",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> CANTONESERICEROLLS = REGISTRY.register("cantonesericerolls",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> CONGEEWITH_MINCED_PORKAND_PRESERVED_EGG = REGISTRY.register("congee_with_minced_pork_and_preserved_egg",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> CURRYSTEWEDCHICKEN = REGISTRY.register("currystewedchicken",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> GUMBOSOUP = REGISTRY.register("gumbosoup",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> REDDATESANDTREMELLAPORRIDGE = REGISTRY.register("reddatesandtremellaporridge",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> SA = REGISTRY.register("sa",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> SCRAMBLEDEGGWITHTOMATO = REGISTRY.register("scrambledeggwithtomato",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> SHEEPGIBLETSSOUP = REGISTRY.register("sheepgibletssoup",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEAMEDEGGCUSTARD = REGISTRY.register("steamedeggcustard",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEWEDCHICKENSOUPWITHMUSHROOMS = REGISTRY.register("stewedchickensoupwithmushrooms",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEWEDCHICKENWITHWAXGOURD = REGISTRY.register("stewedchickenwithwaxgourd",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEWEDEGGSWITHLOOFAH = REGISTRY.register("stewedeggswithloofah",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEWEDPORKOFFAL = REGISTRY.register("stewedporkoffal",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> WINTERJUJUBEANDWAXGOURDSOUP = REGISTRY.register("winterjujubeandwaxgourdsoup",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));

    // Crop products
    public static final DeferredItem<Item> ONION = food("onion", 2, 0.3f);
    public static final DeferredItem<Item> PEA = food("pea", 1, 0.2f);
    public static final DeferredItem<Item> PURPLESWEETPOTATO = food("purplesweetpotato", 2, 0.3f);
    public static final DeferredItem<Item> SESAME = food("sesame", 1, 0.2f);
    public static final DeferredItem<Item> SOYBEAN = food("soybean", 1, 0.2f);
    public static final DeferredItem<Item> SWEETGREENPEPPER = food("sweetgreenpepper", 2, 0.3f);
    public static final DeferredItem<Item> ZUCCHINI = food("zucchini", 2, 0.3f);

    // Basic ingredients
    public static final DeferredItem<Item> SORBET = item("sorbet");
    public static final DeferredItem<Item> CASING = item("casing");
    public static final DeferredItem<Item> HONEYCOMBBRIQUET = item("honeycombbriquet");
    public static final DeferredItem<Item> BRAN = item("bran");
    public static final DeferredItem<Item> CASSAVAPEARL = item("cassavapearl");
    public static final DeferredItem<Item> SOAKEDSOYBEANS = item("soakedsoybeans");
    public static final DeferredItem<Item> MEATFLOSS = food("meatfloss", 12, 0.8f);
    public static final DeferredItem<Item> GRAVELPASTE = item("gravelpaste");
    public static final DeferredItem<Item> RAWSOYSHREDDEDMEAT = item("rawsoyshreddedmeat");
    public static final DeferredItem<Item> LANDPLASTER = item("landplaster");
    public static final DeferredItem<Item> WRESTLING_GUN = REGISTRY.register("wrestling_gun",
            () -> new WrestlingGunItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> RAWSHEEPOFFAL = item("rawsheepoffal");
    public static final DeferredItem<Item> TIDYREEDLEAF = item("tidyreedleaf");
    public static final DeferredItem<Item> NAHCO_3 = item("nahco_3");
    public static final DeferredItem<Item> PROBIOTICS = item("probiotics");

    // Prepared vegetables and meat
    public static final DeferredItem<Item> SCALLION_2 = item("scallion_2");
    public static final DeferredItem<Item> DICEDCARROTS = item("dicedcarrots");
    public static final DeferredItem<Item> SHREDDEDCARROTS = item("shreddedcarrots");
    public static final DeferredItem<Item> DICEDCUCUMBER = item("dicedcucumber");
    public static final DeferredItem<Item> SLICEDCUCUMBER = item("slicedcucumber");
    public static final DeferredItem<Item> SHREDDERCUCUMBER = item("shreddercucumber");
    public static final DeferredItem<Item> SLICEDABUERGINE = item("slicedabuergine");
    public static final DeferredItem<Item> SHREDDERABUERGINE = item("shredderabuergine");
    public static final DeferredItem<Item> CHOPPEDCOWPEA = item("choppedcowpea");
    public static final DeferredItem<Item> DIECEDRADISH = item("diecedradish");
    public static final DeferredItem<Item> DICEDRADISH = item("dicedradish");
    public static final DeferredItem<Item> SLICEDRADISH = item("slicedradish");
    public static final DeferredItem<Item> SHREDDERRADISH = item("shredderradish");
    public static final DeferredItem<Item> DICEDCASSAVA = item("dicedcassava");
    public static final DeferredItem<Item> SLICEDLEMON = item("slicedlemon");
    public static final DeferredItem<Item> DICEDLOTUSROOT = item("dicedlotusroot");
    public static final DeferredItem<Item> SLICEDLOTUSROOT = item("slicedlotusroot");
    public static final DeferredItem<Item> DICEDONION = item("dicedonion");
    public static final DeferredItem<Item> DICEDORANGE = item("dicedorange");
    public static final DeferredItem<Item> DICEDWAXGOURD = item("dicedwaxgourd");
    public static final DeferredItem<Item> DICEDPLEUROTUSERYNGII = item("dicedpleurotuseryngii");
    public static final DeferredItem<Item> DICEDCELERY = item("dicedcelery");
    public static final DeferredItem<Item> DICEDSWEETPEPPER = item("dicedsweetpepper");
    public static final DeferredItem<Item> DICEDCHINESEYAM = item("dicedchineseyam");
    public static final DeferredItem<Item> DICEDFISH = item("dicedfish");
    public static final DeferredItem<Item> DICEDLOOFACH = item("dicedloofach");
    public static final DeferredItem<Item> DICEDSAUTEEDGREENBEANS = item("dicedsauteedgreenbeans");
    public static final DeferredItem<Item> DICEDPOTATO = item("dicedpotato");
    public static final DeferredItem<Item> SLICEDPOTATO = item("slicedpotato");
    public static final DeferredItem<Item> SHREDDEDPOTATO_2 = item("shreddedpotato_2");
    public static final DeferredItem<Item> CHOPPEDPOTATO_1 = item("choppedpotato_1");
    public static final DeferredItem<Item> DICEDZUCCHINI = item("dicedzucchini");
    public static final DeferredItem<Item> DICEDPURPLESWEETPOTATO = item("dicedpurplesweetpotato");

    // Dough, wrappers, and noodles
    public static final DeferredItem<Item> BAOZI_SKIN = item("baozi_skin");
    public static final DeferredItem<Item> VERMICELLIROLL = item("vermicelliroll");
    public static final DeferredItem<Item> BEATGLUTINOUSRICEFLOURPASTE = item("beatglutinousriceflourpaste");
    public static final DeferredItem<Item> DRYPOWDERSKIN = item("drypowderskin");
    public static final DeferredItem<Item> DRIEDVERMICELLI = item("driedvermicelli");
    public static final DeferredItem<Item> DRYPOWDERSTRIP = item("drypowderstrip");
    public static final DeferredItem<Item> EGGNOODLESWRAPPEDINSYRUP = item("eggnoodleswrappedinsyrup");
    public static final DeferredItem<Item> NOODLESWRAPPEDINSYRUP = item("noodleswrappedinsyrup");
    public static final DeferredItem<Item> WALNUTFLOURPASTE = item("walnutflourpaste");
    public static final DeferredItem<Item> SWEETPOTATODOUGH = item("sweetpotatodough");
    public static final DeferredItem<Item> FLOURSKIN = item("flourskin");
    public static final DeferredItem<Item> EGGDOUGH = item("eggdough");
    public static final DeferredItem<Item> RAWEGGNOODLES = item("raweggnoodles");
    public static final DeferredItem<Item> DUMPLING_SKIN = item("dumpling_skin");
    public static final DeferredItem<Item> RAWRICENOODLES = item("rawricenoodles");
    public static final DeferredItem<Item> RICENOODLES_2 = item("ricenoodles_2");
    public static final DeferredItem<Item> FLOURPASTE = item("flourpaste");
    public static final DeferredItem<Item> DOUGH = item("dough");
    public static final DeferredItem<Item> SHUMAISKIN = item("shumaiskin");
    public static final DeferredItem<Item> RAWCOARSENOODLES = item("rawcoarsenoodles");
    public static final DeferredItem<Item> DRYRICECAKE = item("dryricecake");
    public static final DeferredItem<Item> RAWEGGPANCAKE = item("raweggpancake");
    public static final DeferredItem<Item> RAWEGGPASTE = item("raweggpaste");
    public static final DeferredItem<Item> RAWEGGSKIN = item("raweggskin");
    public static final DeferredItem<Item> RAWFLOURPANCAKE = item("rawflourpancake");
    public static final DeferredItem<Item> RAWFLOURROLL = item("rawflourroll");
    public static final DeferredItem<Item> RAWNOODLES = item("rawnoodles");
    public static final DeferredItem<Item> RAWGLUTINOUSRICEFLOURPANCAKE = item("rawglutinousriceflourpancake");
    public static final DeferredItem<Item> RAWGLUTINOUSPASTE = item("rawglutinouspaste");
    public static final DeferredItem<Item> RAWGLUTINOUSSKIN = item("rawglutinousskin");
    public static final DeferredItem<Item> RAWGLUTINOUSNOODLES = item("rawglutinousnoodles");
    public static final DeferredItem<Item> RAWGLUTINOUSDOUGH = item("rawglutinousdough");
    public static final DeferredItem<Item> RAWSHUMAI = item("rawshumai");
    public static final DeferredItem<Item> RAWPIE = item("rawpie");
    public static final DeferredItem<Item> RAWMOONCAKE = item("rawmooncake");
    public static final DeferredItem<Item> OILEDFLOURSKIN = item("oiledflourskin");
    public static final DeferredItem<Item> COMPACTEDRICEBRICK = item("compactedricebrick");
    public static final DeferredItem<Item> MACARONI = item("macaroni");
    public static final DeferredItem<Item> STEAMEDRICEBRICK = item("steamedricebrick");

    // Fillings, pastes, and liquids
    public static final DeferredItem<Item> BROKENWALNUT = item("brokenwalnut");
    public static final DeferredItem<Item> EGGSTUFFING = item("eggstuffing");
    public static final DeferredItem<Item> FRUITFLAVOREDMOONCAKESTUFFING = item("fruitflavoredmooncakestuffing");
    public static final DeferredItem<Item> SESAMEANDPEANUTBALLS = item("sesameandpeanutballs");
    public static final DeferredItem<Item> WAXGROUDPASTE = item("waxgroudpaste");
    public static final DeferredItem<Item> REDBEANPASTE = item("redbeanpaste");
    public static final DeferredItem<Item> PEPPERANDSALTMASS = item("pepperandsaltmass");
    public static final DeferredItem<Item> MEATANDVEGETABLESTUFFING = item("meatandvegetablestuffing");
    public static final DeferredItem<Item> MEATANDEGGPASTE = item("meatandeggpaste");
    public static final DeferredItem<Item> MEATPASTE = item("meatpaste");
    public static final DeferredItem<Item> VEGETABLEPASTE = item("vegetablepaste");
    public static final DeferredItem<Item> PEASTUFFING = item("peastuffing");
    public static final DeferredItem<Item> JUJUBEPASTE = item("jujubepaste");
    public static final DeferredItem<Item> SEASAMEGLUTINOUSRICEBALLS = item("seasameglutinousriceballs");
    public static final DeferredItem<Item> TIDYWATER = item("tidywater");
    public static final DeferredItem<Item> EGGLIQUID = item("eggliquid");
    public static final DeferredItem<Item> PEANUTJAM = item("peanutjam");
    public static final DeferredItem<Item> EGGBATTER = item("eggbatter");
    public static final DeferredItem<Item> COCOASAUCE = item("cocoasauce");
    public static final DeferredItem<Item> PANADA = item("panada");
    public static final DeferredItem<Item> MILKBOTTLE = item("milkbottle");
    public static final DeferredItem<Item> CREAM = item("cream");
    public static final DeferredItem<Item> CONCENTRATEDSYRUP = item("concentratedsyrup");
    public static final DeferredItem<Item> GLUTINOUSRICEBATTER = item("glutinousricebatter");
    public static final DeferredItem<Item> RAWSOYBEANMILK = item("rawsoybeanmilk");
    public static final DeferredItem<Item> GELATIN = item("gelatin");
    public static final DeferredItem<Item> SWEETEGGLIQUID = item("sweeteggliquid");
    public static final DeferredItem<Item> MULTIGRAINBATTER = item("multigrainbatter");

    // Remaining non-block item registrations migrated from FlavorImmersedDaily.
    public static final DeferredItem<Item> CHINESE_LEAVES = REGISTRY.register("chineseleaves",
            () -> new ChineseLeavesItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.3f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> CUT_CHINESE_CABBAGE = REGISTRY.register("cutchinesecabbage",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.1f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> RADISH = REGISTRY.register("radish",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.3f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> BROCCOIL = REGISTRY.register("broccoil", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> BUCKWHEAT = REGISTRY.register("buckwheat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

    public static final DeferredItem<Item> CABBAGE = REGISTRY.register("cabbage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> CASSAVA = REGISTRY.register("cassava", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> CELERY = REGISTRY.register("celery", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

    public static final DeferredItem<Item> CHINESECHIVES = REGISTRY.register("chinesechives", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

    public static final DeferredItem<Item> CHINESEYAM = REGISTRY.register("chineseyam", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> CORN = REGISTRY.register("corn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> CUMIN = REGISTRY.register("cumin", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

    public static final DeferredItem<Item> FENNEL = REGISTRY.register("fennel", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

    public static final DeferredItem<Item> GARLIC = REGISTRY.register("garlic", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

    public static final DeferredItem<Item> GINGER = REGISTRY.register("ginger", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

    public static final DeferredItem<Item> GREENPEPPER = REGISTRY.register("greenpepper", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> GUMBO = REGISTRY.register("gumbo", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> MILLETGRAIN_GRAIN = REGISTRY.register("millet_grain", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

    public static final DeferredItem<Item> MUNGBEAN = REGISTRY.register("mungbean", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

    public static final DeferredItem<Item> MUSTARD = REGISTRY.register("mustard", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

    public static final DeferredItem<Item> OAT = REGISTRY.register("oat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

    public static final DeferredItem<Item> OILSEEDRAPE = REGISTRY.register("oilseedrape", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

    public static final DeferredItem<Item> REEDLEAF = REGISTRY.register("reedleaf", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.reedleaf_harvest"), ModItems::reedleafTooltipIcon));

    public static final DeferredItem<Item> RAWORLEANSCHICKENWING = REGISTRY.register("raworleanschickenwing", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWORLEANSCHICKENLEG = REGISTRY.register("raworleanschickenleg", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COOKED_BAOZI = REGISTRY.register("cooked_baozi", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> FATBEEFROLL = REGISTRY.register("fatbeefroll", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> LAMBROLL = REGISTRY.register("lambroll", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MUSHROOMSWRAPPEDINBATTER = REGISTRY.register("mushroomswrappedinbatter", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENWINGWITHBATTER = REGISTRY.register("rawchickenwingwithbatter", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENHALFLEGWITHBATTER = REGISTRY.register("rawchickenhalflegwithbatter", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENMEATWITHBATTER = REGISTRY.register("rawchickenmeatwithbatter", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSAUSAGE = REGISTRY.register("rawsausage", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWMEATBALLWITHEGGBALL = REGISTRY.register("rawmeatballwitheggball", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> FROZENMILK = REGISTRY.register("frozenmilk", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWDICEDCHICKENWITHBATTER = REGISTRY.register("rawdicedchickenwithbatter", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> WONTON = REGISTRY.register("wonton", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> NORMALMEATROLL = REGISTRY.register("normalmeatroll", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CARAMELCORNKENNELS = REGISTRY.register("caramelcornkennels", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COOKEDDUMPLING = REGISTRY.register("cookeddumpling", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SALTYEGG = REGISTRY.register("saltyegg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> SALTYRADDISH = REGISTRY.register("saltyraddish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> TANGYUAN = REGISTRY.register("tangyuan", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> THOUSAND_LAYER_TOFU_SKIN = REGISTRY.register("thousand_layer_tofu_skin", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> SPAGHETTI = REGISTRY.register("spaghetti", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> CREAMCORNKERNELS = REGISTRY.register("creamcornkernels", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CHOCOLATECORNKERNELS = REGISTRY.register("chocolatecornkernels", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSPRINGROLL = REGISTRY.register("rawspringroll", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWEGGTART = REGISTRY.register("raweggtart", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SWEETREDBEANEGGTART = REGISTRY.register("sweetredbeaneggtart", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSPICYGLUTEN = REGISTRY.register("rawspicygluten", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCOUPLING = REGISTRY.register("rawcoupling", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWAIKUI = REGISTRY.register("rawaikui", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MEATFLOURROOL = REGISTRY.register("meatflourrool", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> RAWFLOURPASTEWITHDRIEDMEATFLOSS = REGISTRY.register("rawflourpastewithdriedmeatfloss", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWAZONGZI = REGISTRY.register("rawazongzi", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAW_TANGYUAN = REGISTRY.register("raw_tangyuan", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWDORAYAKI = REGISTRY.register("rawdorayaki", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSESAMEGLUTINOUSPASTE = REGISTRY.register("rawsesameglutinouspaste", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CRISPYPORKBELLY = REGISTRY.register("crispyporkbelly", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAW_STUFFEDGREENPEPPER = REGISTRY.register("raw_stuffedgreenpepper", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSALTYEGG = REGISTRY.register("rawsaltyegg", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWWRAPPEDMILK = REGISTRY.register("rawwrappedmilk", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MODULATEDWHEATFLOUR = REGISTRY.register("modulatedwheatflour", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> WALNUTPOWDER = REGISTRY.register("walnutpowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SWEETPOTATSTARCH = REGISTRY.register("sweetpotatstarch", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PEANUTPOWDER = REGISTRY.register("peanutpowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PEANUTSESAMEPOWDER = REGISTRY.register("peanutsesamepowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COFFEEPOWDER = REGISTRY.register("coffeepowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COCOAPOWDER = REGISTRY.register("cocoapowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> WHEATFLOUR = REGISTRY.register("wheatflour", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> TAPIOCAFLOUR = REGISTRY.register("tapiocaflour", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GULTINOUSRICEPOWDER = REGISTRY.register("gultinousricepowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PEAMEAL = REGISTRY.register("peameal", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SESAMEPOWDER = REGISTRY.register("sesamepowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GULTINOUSRICESASAMEPOWDER = REGISTRY.register("gultinousricesasamepowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CRYSTALSUGAR = REGISTRY.register("crystalsugar", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> VINEGAR = REGISTRY.register("vinegar", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.ACETIC_EROSION, () -> Config.aceticErosionEnabled));

    public static final DeferredItem<Item> THICKBROADBEANSAUCE = REGISTRY.register("thickbroadbeansauce", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.BEAN_FURY, () -> Config.beanFuryEnabled));

    public static final DeferredItem<Item> TOMATO_HOT_POT_BASE = REGISTRY.register("tomato_hot_pot_base", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> BONESOUPESSENCE = REGISTRY.register("bonesoupessence", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> BROWNSUGAR = REGISTRY.register("brownsugar", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> BUTTER = REGISTRY.register("butter", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.BUTTER_PITCHER, () -> Config.butterPitcherEnabled));

    public static final DeferredItem<Item> SOY = REGISTRY.register("soy", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.SOLAR_BREW, () -> Config.solarBrewEnabled));

    public static final DeferredItem<Item> CURRY = REGISTRY.register("curry", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> HALOGENBAG = REGISTRY.register("halogenbag", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SPICY_HOT_POT_BASE = REGISTRY.register("spicy_hot_pot_base", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MAJUICE = REGISTRY.register("majuice", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> WHITESUGARSYRUP = REGISTRY.register("whitesugarsyrup", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COOKINGOIL = REGISTRY.register("cookingoil", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PEPPER_HOT_POT_BASE = REGISTRY.register("pepper_hot_pot_base", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SWEETFLOURASUVE = REGISTRY.register("sweetflourasuve", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SESAMEOIL = REGISTRY.register("sesameoil", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.SESAME_SLIP, () -> Config.sesameSlipEnabled));

    public static final DeferredItem<Item> SALT = REGISTRY.register("salt", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.FLAVOR_BASE, () -> Config.flavorBaseEnabled));

    public static final DeferredItem<Item> SALTPIECE = REGISTRY.register("saltpiece", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ORLEANSPOWDER = REGISTRY.register("orleanspowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ANISEEDPOWDER = REGISTRY.register("aniseedpowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ONIONPOWDER = REGISTRY.register("onionpowder", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.HULK_LEEK, () -> Config.hulkLeekEnabled));

    public static final DeferredItem<Item> LILACPOWDER = REGISTRY.register("lilacpowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CINNAMONPOWDER = REGISTRY.register("cinnamonpowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> REDTEAPOWDER = REGISTRY.register("redteapowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CHINESEPICKLYASHPOWDER = REGISTRY.register("chinesepicklyashpowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> FENNELPOWDER = REGISTRY.register("fennelpowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GROUNDPOWDER = REGISTRY.register("groundpowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PEPPEREDSALT = REGISTRY.register("pepperedsalt", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CHILLIPOWDER = REGISTRY.register("chillipowder", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.FURY_ASSAULT, () -> Config.furyAssaultEnabled));

    public static final DeferredItem<Item> GREENTEAPOWDER = REGISTRY.register("greenteapowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GARLICPOWDER = REGISTRY.register("garlicpowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> FIVESPICEPOWDER = REGISTRY.register("fivespicepowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ONIONPOWDER_2 = REGISTRY.register("onionpowder_2", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CUMINPOWDER = REGISTRY.register("cuminpowder", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DEADCATTLE = REGISTRY.register("deadcattle", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.dead_animal_harvest"), ModItems::deadAnimalIcons));

    public static final DeferredItem<Item> DEADSHEEP = REGISTRY.register("deadsheep", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.dead_animal_harvest"), ModItems::deadAnimalIcons));

    public static final DeferredItem<Item> DEADPIG = REGISTRY.register("deadpig", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.dead_animal_harvest"), ModItems::deadAnimalIcons));

    public static final DeferredItem<Item> DEADCHICKEN = REGISTRY.register("deadchicken", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.dead_animal_harvest"), ModItems::deadAnimalIcons));

    public static final DeferredItem<Item> CHICKENWITHOUTFEATHER = REGISTRY.register("chickenwithoutfeather",
            () -> new TooltipItem(new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.chicken_without_feather"), () -> java.util.List.of()));

    public static final DeferredItem<Item> CHICKENWITHOUTBLOOD = REGISTRY.register("chickenwithoutblood",
            () -> new TooltipItem(new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.chicken_without_blood"), ModItems::chickenBloodIcons));

    public static final DeferredItem<Item> SHARPKNIFE = REGISTRY.register("sharpknife", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> WIDEEDGEDKNIFE = REGISTRY.register("wideedgedknife", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> BONECUTTERKNIFE = REGISTRY.register("bonecutterknife", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> KITCHENKNIFE = REGISTRY.register("kitchenknife", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> KITCHENSCISSOR = REGISTRY.register("kitchenscissor", () -> new KitchenScissorsItem(new Item.Properties()));

    public static final DeferredItem<Item> SPATULA = REGISTRY.register("spatula", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MALLET = REGISTRY.register("mallet", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COARSECLOTH = REGISTRY.register("coarsecloth", () -> new CoarseClothItem(new Item.Properties()));

    public static final DeferredItem<Item> MOONCAKEMOLD = REGISTRY.register("mooncakemold", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ROLLINGPIN = REGISTRY.register("rollingpin", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EEGGPUFFSMOULD = REGISTRY.register("eeggpuffsmould", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DIECEDPRESERVEDEGG = REGISTRY.register("diecedpreservedegg", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CUTDIECEDPRESERVEEGG = REGISTRY.register("cutdiecedpreserveegg", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DICEDSWEETPOTATO = REGISTRY.register("dicedsweetpotato", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PURPLEDICEDSWEETPOTATO = REGISTRY.register("purpledicedsweetpotato", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PROCESSEDCABBAGE = REGISTRY.register("processedcabbage", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DICEDTOMATO = REGISTRY.register("dicedtomato", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CUTCABBAGE = REGISTRY.register("cutcabbage", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CASSAVACHUNKS = REGISTRY.register("cassavachunks", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SLICEDZUCCHINI = REGISTRY.register("slicedzucchini", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CARROTCHUNKS = REGISTRY.register("carrotchunks", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CELERYLEAF = REGISTRY.register("celeryleaf", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PEAPASTE = REGISTRY.register("peapaste", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SESAMEGLUTINOUSRICEBALLS = REGISTRY.register("sesameglutinousriceballs", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MEATANDEGGFILLING = REGISTRY.register("meatandeggfilling", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MEATANDVEGETABLEFILLING = REGISTRY.register("meatandvegetablefilling", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MUNGBEANPASTE = REGISTRY.register("mungbeanpaste", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> WAXGOURDPASTE = REGISTRY.register("waxgourdpaste", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> UNCOMMONSTUFFING = REGISTRY.register("uncommonstuffing", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> VEGETABLEANDEGGSTUFFING = REGISTRY.register("vegetableandeggstuffing", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> BROWNSUGARSYRUP = REGISTRY.register("brownsugarsyrup", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.CRIMSON_MAMBA, () -> true));

    public static final DeferredItem<Item> HOTPOTBASETEMPLATE = REGISTRY.register("hotpotbasetemplate", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SALTYWATER = REGISTRY.register("saltywater", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COCOAMASS = REGISTRY.register("cocoamass", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EGGCOATEDMEATBALLS = REGISTRY.register("eggcoatedmeatballs", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> BATTEREDCHICKENBREASTCHUNKS = REGISTRY.register("batteredchickenbreastchunks", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWMEATFLOSSDOUGH = REGISTRY.register("rawmeatflossdough", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWMEATZONGZI = REGISTRY.register("rawmeatzongzi", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> BATTEREDMUSHROOMS = REGISTRY.register("batteredmushrooms", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSWEETZONGZI = REGISTRY.register("rawsweetzongzi", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CUTPANCAKE = REGISTRY.register("cutpancake", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> FERMENTEDFLOURPASTE = REGISTRY.register("fermentedflourpaste", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CUTEGGPANCAKE = REGISTRY.register("cuteggpancake", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RICESLURRYWRAPPEDINFILTERCLOTH = REGISTRY.register("riceslurrywrappedinfiltercloth", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> POUNDEDGLUTINOUSPASTE = REGISTRY.register("poundedglutinouspaste", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> LONGDOUGH = REGISTRY.register("longdough", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> LONGEGGPANCAKE = REGISTRY.register("longeggpancake", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SOYBEANPROTEIN = REGISTRY.register("soybeanprotein", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAW_PIGOFFAL = REGISTRY.register("raw_pigoffal", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DEBONEDCHICKENFEET = REGISTRY.register("debonedchickenfeet", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EGGWRAPPEDINGRAVEL = REGISTRY.register("eggwrappedingravel", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PEAFLOUR = REGISTRY.register("peaflour", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> FIVEPOINTEDCARAMBOLADELIGHT = REGISTRY.register("fivepointedcaramboladelight", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> RIPEPEARWITHROCKSUGAR = REGISTRY.register("ripepearwithrocksugar", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> SUGARCOATEDHAWS = REGISTRY.register("sugarcoatedhaws", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> STEWEDPIGLEG = REGISTRY.register("stewedpigleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> STEWEDHALFCHICKENLEG = REGISTRY.register("stewedhalfchickenleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> STEWEDCHICKENHEART = REGISTRY.register("stewedchickenheart", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> STEWEDCHICKENLIVERS = REGISTRY.register("stewedchickenlivers", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> STEWEDCHICKENLEG = REGISTRY.register("stewedchickenleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> SAUCINGBEEF = REGISTRY.register("saucingbeef", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> YOGURT = REGISTRY.register("yogurt", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> SALTEGGYOLK = REGISTRY.register("salteggyolk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> SALTTOFUCURD = REGISTRY.register("salttofucurd", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> DICEDHAMIMELON = REGISTRY.register("dicedhamimelon", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> FRIED_TOFU_SKIN_ROLLS = REGISTRY.register("fried_tofu_skin_rolls", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> CREAMPOPCORN = REGISTRY.register("creampopcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> CHINESEYAMANDSUGAR = REGISTRY.register("chineseyamandsugar", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> CHOCOLATEPOPCORN = REGISTRY.register("chocolatepopcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> CHOCOLATEBEAN = REGISTRY.register("chocolatebean", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> NEWYEARCAKE = REGISTRY.register("newyearcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> BONELESSLEMONCHICKENFEET = REGISTRY.register("bonelesslemonchickenfeet", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> BOILEDCORN = REGISTRY.register("boiledcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> CHICKENFEETWITHPEPPERS = REGISTRY.register("chickenfeetwithpeppers", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> COLONELCHICKENNUGGETS = REGISTRY.register("colonelchickennuggets", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> FRIEDGULTINOUSRICESTRIPS = REGISTRY.register("friedgultinousricestrips", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> FRIEDPEANUTS = REGISTRY.register("friedpeanuts", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> FRIEDMUSHROOM = REGISTRY.register("friedmushroom", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> DRIEDRICECAKE = REGISTRY.register("driedricecake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> FIREDSPRINGROLL = REGISTRY.register("firedspringroll", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> FRIEDDOUGHSTICK = REGISTRY.register("frieddoughstick", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> DRIEDMILK = REGISTRY.register("driedmilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> DRIEDMEATBALL = REGISTRY.register("driedmeatball", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> DRIEDDICEDAUBERGINE = REGISTRY.register("drieddicedaubergine", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> FRIEDGIZZARD = REGISTRY.register("friedgizzard", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> FRENCHFRIES = REGISTRY.register("frenchfries", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> DRIEDLOTUSROOT = REGISTRY.register("driedlotusroot", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> FRIEDDRIEDTOUFU = REGISTRY.register("frieddriedtoufu", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> FRIEDTOUFU = REGISTRY.register("friedtoufu", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> DRIEDBREAD = REGISTRY.register("driedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> DRIEDDICEDBREAD = REGISTRY.register("drieddicedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> DRIEDDICEDSTEAMEDBREAD = REGISTRY.register("drieddicedsteamedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> FRIEDSANZI = REGISTRY.register("friedsanzi", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> FRIEDHEMPBALL = REGISTRY.register("friedhempball", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> FRIEDDOUGHTWIST = REGISTRY.register("frieddoughtwist", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> CRISPYPISTOLLEG = REGISTRY.register("crispypistolleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> SOMKEDCHICKENBREAST = REGISTRY.register("somkedchickenbreast", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> ROASTEDPURPLEPOTATO = REGISTRY.register("roastedpurplepotato", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> ROASTEDPEANUT = REGISTRY.register("roastedpeanut", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> ORLEANWING = REGISTRY.register("orleanwing", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));

    public static final DeferredItem<Item> ORLEANLEG = REGISTRY.register("orleanleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));

    public static final DeferredItem<Item> ROASTEDMUSHROOM = REGISTRY.register("roastedmushroom", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));

    public static final DeferredItem<Item> ROASTED_FLAMMULINAVELUTIPES = REGISTRY.register("roasted_flammulinavelutipes", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));

    public static final DeferredItem<Item> ROASTEDCHINESECHIVES = REGISTRY.register("roastedchinesechives", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));

    public static final DeferredItem<Item> ROASTEDDICEDFISH = REGISTRY.register("roasteddicedfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));

    public static final DeferredItem<Item> CARAMELPOPCORN = REGISTRY.register("caramelpopcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> COOKEDCHINESEYAM = REGISTRY.register("cookedchineseyam", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> COOKEDVERMICELLI_0 = REGISTRY.register("cookedvermicelli_0", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> COOKEDHAWTHORN = REGISTRY.register("cookedhawthorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> POPCORN = REGISTRY.register("popcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> SWEETTOUFUCURD = REGISTRY.register("sweettoufucurd", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> JELLY = REGISTRY.register("jelly", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> COOKEDPRESERVEDEGG = REGISTRY.register("cookedpreservedegg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> COOKEDZONGZI = REGISTRY.register("cookedzongzi", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> COOKEDGLUTINOUSRICE = REGISTRY.register("cookedglutinousrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> COOKEDGLUTINOUSRICEPOUNDEDINTOPASTE = REGISTRY.register("cookedglutinousricepoundedintopaste", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> AIKUI = REGISTRY.register("aikui", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> TANG_ZI_XIAO_ZAO = REGISTRY.register("tang_zi_xiao_zao", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> SUGARCOATEDWALNUT = REGISTRY.register("sugarcoatedwalnut", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> DICEDPICKLEDVEGETABLE = REGISTRY.register("dicedpickledvegetable", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> CUREDMEAT = REGISTRY.register("curedmeat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> CUREDSAUSAGE = REGISTRY.register("curedsausage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> SALTYDICEDCURUMBER = REGISTRY.register("saltydicedcurumber", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> PICKLEDVEGETABLE = REGISTRY.register("pickledvegetable", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> SLICEDSALTYCUCUMBER = REGISTRY.register("slicedsaltycucumber", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> STEAMEDBLOOD = REGISTRY.register("steamedblood", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> TIGERGREENPEPPER = REGISTRY.register("tigergreenpepper", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> SOYAMILK = REGISTRY.register("soyamilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> PICEDTOUFU = REGISTRY.register("picedtoufu", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> DRIEDTOUFU = REGISTRY.register("driedtoufu", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> TOFU_PUFFS = REGISTRY.register("tofu_puffs", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> TOFUCURD = REGISTRY.register("tofucurd", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> CHINESESPICYSNACKFOOD = REGISTRY.register("chinesespicysnackfood", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> SPICYPEANUT = REGISTRY.register("spicypeanut", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> HOTANDSOURRICENOODLES = REGISTRY.register("hotandsourricenoodles", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> GOLDRICECAKE = REGISTRY.register("goldricecake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> GOLDENGRAPE = REGISTRY.register("goldengrape", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> DICEDBREAD = REGISTRY.register("dicedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> DRYCOOKEDNOODLES = REGISTRY.register("drycookednoodles", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> COOKEDVERMICELLI = REGISTRY.register("cookedvermicelli", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> MOONCAKE = REGISTRY.register("mooncake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> GRAINSPANCAKE = REGISTRY.register("grainspancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> HAMBURGERBREAD = REGISTRY.register("hamburgerbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> YIMENGPANCAKES = REGISTRY.register("yimengpancakes", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> SACHIMA = REGISTRY.register("sachima", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> SHUMAI = REGISTRY.register("shumai", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> HOTDOG = REGISTRY.register("hotdog", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> STEAMEDGLUTINOUSRICECAKE = REGISTRY.register("steamedglutinousricecake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> CRISPYEGGCAKE = REGISTRY.register("crispyeggcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> STEAMEDTWISTEDROLL = REGISTRY.register("steamedtwistedroll", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> SCALLIONOILPANCAKE = REGISTRY.register("scallionoilpancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> NOODLES = REGISTRY.register("noodles", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> PIE = REGISTRY.register("pie", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> STEAMEDBREAD = REGISTRY.register("steamedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> DICEDSTEAMEDBREAD = REGISTRY.register("dicedsteamedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> EGGPUFFS = REGISTRY.register("eggpuffs", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> EGGVERMICELLI = REGISTRY.register("eggvermicelli", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> COOKEDSAUSAGE = REGISTRY.register("cookedsausage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> M_SECRETDELIGHT = REGISTRY.register("m_secretdelight", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> FISHSASHIMI = REGISTRY.register("fishsashimi", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> RAWCATTLEBLOOD = REGISTRY.register("rawcattleblood",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCATTLEFACE = REGISTRY.register("rawcattleface",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCATTLEJOINT = REGISTRY.register("rawcattlejoint",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCATTLESTOMACH = REGISTRY.register("rawcattlestomach",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCATTLELUNG = REGISTRY.register("rawcattlelung",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCATTLELIVER = REGISTRY.register("rawcattleliver",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCATTLEINTESTINE = REGISTRY.register("rawcattleintestine",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCATTLEHEART = REGISTRY.register("rawcattleheart",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCATTLEFAT = REGISTRY.register("rawcattlefat",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWDICEDCATTLEMEAT = REGISTRY.register("rawdicedcattlemeat",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSNOWFLAKEBEEF = REGISTRY.register("rawsnowflakebeef",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCATTLELEG = REGISTRY.register("rawcattleleg",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCATTLEFEET = REGISTRY.register("rawcattlefeet",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCATTLETENDON = REGISTRY.register("rawcattletendon",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> BULLHORN = REGISTRY.register("bullhorn",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> BOVINEBONE = REGISTRY.register("bovinebone",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ANIMALSKULL = REGISTRY.register("animalskull",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGBLOOD = REGISTRY.register("rawpigblood",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWDICEDPIGMEAT = REGISTRY.register("rawdicedpigmeat",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWDICEDPIGMEAT_2 = REGISTRY.register("rawdicedpigmeat_2",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGHEAD = REGISTRY.register("rawpighead",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGEAR = REGISTRY.register("rawpigear",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGNOSE = REGISTRY.register("rawpignose",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGTAIL = REGISTRY.register("rawpigtail",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGSKIN = REGISTRY.register("rawpigskin",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGSTREAKYPORK = REGISTRY.register("rawpigstreakypork",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGTENDERLOIN = REGISTRY.register("rawpigtenderloin",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGSPARERIB = REGISTRY.register("rawpigsparerib",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGLEG = REGISTRY.register("rawpigleg",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGFEET = REGISTRY.register("rawpigfeet",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGFAT = REGISTRY.register("rawpigfat",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGSTOMACH = REGISTRY.register("rawpigstomach",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGLUNG = REGISTRY.register("rawpiglung",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGLIVER = REGISTRY.register("rawpigliver",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGKIDNEY = REGISTRY.register("rawpigkidney",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGHEART = REGISTRY.register("rawpigheart",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGINTESTINE = REGISTRY.register("rawpigintestine",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWPIGCEREBRUM = REGISTRY.register("rawpigcerebrum",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSHEEPBLOOD = REGISTRY.register("rawsheepblood",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWDICEDSHEEPMEAT = REGISTRY.register("rawdicedsheepmeat",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSHEEPFACE = REGISTRY.register("rawsheepface",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSHEEPTAILFAT = REGISTRY.register("rawsheeptailfat",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSHEEPFAT = REGISTRY.register("rawsheepfat",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSHEEPEYE = REGISTRY.register("rawsheepeye",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSHEEPSTOMACH = REGISTRY.register("rawsheepstomach",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSHEEPLIVER = REGISTRY.register("rawsheepliver",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSHEEPINTESTINE = REGISTRY.register("rawsheepintestine",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSHEEPKIDNEY = REGISTRY.register("rawsheepkidney",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSHEEPHEART = REGISTRY.register("rawsheepheart",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSHEEPSPARERIB = REGISTRY.register("rawsheepsparerib",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSHEEPSPINE = REGISTRY.register("rawsheepspine",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSHEEPFEET = REGISTRY.register("rawsheepfeet",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSHEEPLEG = REGISTRY.register("rawsheepleg",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SHEEPBREAD = REGISTRY.register("sheepbread",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENBLOOD = REGISTRY.register("rawchickenblood",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENHEAD = REGISTRY.register("rawchickenhead",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENNECK = REGISTRY.register("rawchickenneck",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENPIECEPIECE = REGISTRY.register("rawchickenpiecepiece",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENBREAST = REGISTRY.register("rawchickenbreast",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENPIECE = REGISTRY.register("rawchickenpiece",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENWING = REGISTRY.register("rawchickenwing",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENWINGTIP = REGISTRY.register("rawchickenwingtip",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENFEET = REGISTRY.register("rawchickenfeet",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENLEGWITHLEG = REGISTRY.register("rawchickenlegwithleg",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENLEG = REGISTRY.register("rawchickenleg",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENLEAN = REGISTRY.register("rawchickenlean",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENFORK = REGISTRY.register("rawchickenfork",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENASS = REGISTRY.register("rawchickenass",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENHEART = REGISTRY.register("rawchickenheart",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENLIVER = REGISTRY.register("rawchickenliver",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWCHICKENGIZZARD = REGISTRY.register("rawchickengizzard",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COOKEDCATTLEHEART = REGISTRY.register("cookedcattleheart",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCATTLEJOINT = REGISTRY.register("cookedcattlejoint",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCATTLESTOMACH = REGISTRY.register("cookedcattlestomach",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCATTLELUNG = REGISTRY.register("cookedcattlelung",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCATTLELIVER = REGISTRY.register("cookedcattleliver",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCATTLEINTESTINE = REGISTRY.register("cookedcattleintestine",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDDICEDCATTLE = REGISTRY.register("cookeddicedcattle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCATTLEFACE = REGISTRY.register("cookedcattleface",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCATTLEMARBLEDBEEF = REGISTRY.register("cookedcattlemarbledbeef",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCATTLELEG = REGISTRY.register("cookedcattleleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCATTLEFEET = REGISTRY.register("cookedcattlefeet",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCATTLETENDON = REGISTRY.register("cookedcattletendon",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDDICEDPIGMEAT = REGISTRY.register("cookeddicedpigmeat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGSHREDS = REGISTRY.register("cookedpigshreds",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGEAR = REGISTRY.register("cookedpigear",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGNOSE = REGISTRY.register("cookedpignose",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGTAIL = REGISTRY.register("cookedpigtail",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGSKIN = REGISTRY.register("cookedpigskin",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGSTREAKYPORK = REGISTRY.register("cookedpigstreakypork",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGTENDERLOIN = REGISTRY.register("cookedpigtenderloin",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGSPARERIB = REGISTRY.register("cookedpigsparerib",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGLEG = REGISTRY.register("cookedpigleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGFEET = REGISTRY.register("cookedpigfeet",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGFAT = REGISTRY.register("cookedpigfat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGSTOMACH = REGISTRY.register("cookedpigstomach",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGLUNG = REGISTRY.register("cookedpiglung",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGLIVER = REGISTRY.register("cookedpigliver",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGKIDNEY = REGISTRY.register("cookedpigkidney",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGHEART = REGISTRY.register("cookedpigheart",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGCEREBRUM = REGISTRY.register("cookedpigcerebrum",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGINTESTINE = REGISTRY.register("cookedpigintestine",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDPIGFACE = REGISTRY.register("cookedpigface",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDDICEDSHEEPMEAT = REGISTRY.register("cookeddicedsheepmeat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDSHEEPFACE = REGISTRY.register("cookedsheepface",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDSHEEPEYE = REGISTRY.register("cookedsheepeye",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDSHEEPTAIL = REGISTRY.register("cookedsheeptail",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDSHEEPSTOMACH = REGISTRY.register("cookedsheepstomach",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDSHEEPLIVER = REGISTRY.register("cookedsheepliver",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDSHEEPINTESTINE = REGISTRY.register("cookedsheepintestine",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDSHEEPKIDNEY = REGISTRY.register("cookedsheepkidney",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDSHEEPHEART = REGISTRY.register("cookedsheepheart",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDSHEEPSPARERLIP = REGISTRY.register("cookedsheepsparerlip",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDSHEEPSPINE = REGISTRY.register("cookedsheepspine",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDSHEEPFEET = REGISTRY.register("cookedsheepfeet",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDSHEEPLEG = REGISTRY.register("cookedsheepleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDSHEEPFAT = REGISTRY.register("cookedsheepfat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCHICKENDICED = REGISTRY.register("cookedchickendiced",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCHICKENPIECEPIECE = REGISTRY.register("cookedchickenpiecepiece",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCHICKENHEAD = REGISTRY.register("cookedchickenhead",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCHICKENNECK = REGISTRY.register("cookedchickenneck",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCHICKENWING = REGISTRY.register("cookedchickenwing",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCHICKENWINGTIP = REGISTRY.register("cookedchickenwingtip",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCHICKENFEET = REGISTRY.register("cookedchickenfeet",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCHICKENLEGWITHLEG = REGISTRY.register("cookedchickenlegwithleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCHICKENLEG = REGISTRY.register("cookedchickenleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCHICKENPIECE = REGISTRY.register("cookedchickenpiece",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCHICKENSTEAK = REGISTRY.register("cookedchickensteak",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCHICKENFORK = REGISTRY.register("cookedchickenfork",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCHICKENASS = REGISTRY.register("cookedchickenass",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCHICKENHEART = REGISTRY.register("cookedchickenheart",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCHICKENLIVER = REGISTRY.register("cookedchickenliver",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COOKEDCHICKENGIZZARD = REGISTRY.register("cookedchickengizzard",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> PINEAPPLEJAM = REGISTRY.register("pineapplejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> STRAWBERRYJAM = REGISTRY.register("strawberryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> ORANGEJAM = REGISTRY.register("orangejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> WINTERJUJUBEJAM = REGISTRY.register("winterjujubejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> HAMIMELONJAM = REGISTRY.register("hamimelonjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> DRAGONFRUITJAM = REGISTRY.register("dragonfruitjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> TANGERINEJAM = REGISTRY.register("tangerinejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> BLUEBERRYJAM = REGISTRY.register("blueberryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> PEARJAM = REGISTRY.register("pearjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> LYCHEEJAM = REGISTRY.register("lycheejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> PLUMJAM = REGISTRY.register("plumjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> DURIANJAM = REGISTRY.register("durianjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> MANGOJAM = REGISTRY.register("mangojam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> KIWIFRUITJAM = REGISTRY.register("kiwifruitjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> PAWPAWJAM = REGISTRY.register("pawpawjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> LEMONJAM = REGISTRY.register("lemonjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> LOQUATJAM = REGISTRY.register("loquatjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> APPLEJAM = REGISTRY.register("applejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> GRAPEJAM = REGISTRY.register("grapejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> GREEMPLUMJAM = REGISTRY.register("greemplumjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> GREENGRAPEJAM = REGISTRY.register("greengrapejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> MULBERRYJAM = REGISTRY.register("mulberryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> HAWTHORNJAM = REGISTRY.register("hawthornjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> MANGOSTEENJAM = REGISTRY.register("mangosteenjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> POMEGRANATEJAM = REGISTRY.register("pomegranatejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> HONEYPEACHJAM = REGISTRY.register("honeypeachjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> SWEETMELONJAM = REGISTRY.register("sweetmelonjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> SWEETBERRYJAM = REGISTRY.register("sweetberryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> WATERMELONJAM = REGISTRY.register("watermelonjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> BANANAJAM = REGISTRY.register("bananajam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> APRICOTJAM = REGISTRY.register("apricotjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> CARAMBOLAJAM = REGISTRY.register("carambolajam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> COCONUTJAM = REGISTRY.register("coconutjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> CHERRYJAM = REGISTRY.register("cherryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> NECTARINEJAM = REGISTRY.register("nectarinejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> ORANGELEAVE = REGISTRY.register("orangeleave", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> MINCER_COVER = REGISTRY.register("mincer_cover",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> TEAPOTCOVER = REGISTRY.register("teapotcover",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> WINDOW_PAPER_ITEM = REGISTRY.register("windowpaper_1",
            () -> new WindowPaperItem(new Item.Properties()));

    public static final DeferredItem<Item> GREENMANGO = REGISTRY.register("greenmango", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> AGRAPE = REGISTRY.register("agrape", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> AGREENGRAPE = REGISTRY.register("agreengrape", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CAULIFLOWER = REGISTRY.register("cauliflower", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GARLICPEDICEL = REGISTRY.register("garlicpedicel", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PEANUT = REGISTRY.register("peanut", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> POMEGRANATE_SEED = REGISTRY.register("pomegranate_seed", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWHALFOFCHICKENLEG = REGISTRY.register("rawhalfofchickenleg", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWSHEEPTAIL = REGISTRY.register("rawsheeptail", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> REDREPPER = REGISTRY.register("redrepper", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SACLLION = REGISTRY.register("sacllion", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SHEEPMELON = REGISTRY.register("sheepmelon", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SPINACH = REGISTRY.register("spinach", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SWEETPOTATO = REGISTRY.register("sweetpotato", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> YELLOWPEACH = REGISTRY.register("yellowpeach", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DRYANISEED = REGISTRY.register("dryaniseed", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DRYCINNAMON = REGISTRY.register("drycinnamon", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DRYCOFFEEBEAN = REGISTRY.register("drycoffeebean", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DRYLILAC = REGISTRY.register("drylilac", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DRYNUTMEG = REGISTRY.register("drynutmeg", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DRYSCALION = REGISTRY.register("dryscalion", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DRYSICHUANPEPPER = REGISTRY.register("drysichuanpepper", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DICEDBROCCOIL = REGISTRY.register("dicedbroccoil", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DICEDCAULIFLOWER = REGISTRY.register("dicedcauliflower", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EGGSHELL = REGISTRY.register("eggshell", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> OATGRAIN = REGISTRY.register("oatgrain", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> POLISHEDGLUTINOUSRICE_2 = REGISTRY.register("polishedglutinousrice_2", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAW_BAOZI = REGISTRY.register("raw_baozi", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAW_DUMPLING = REGISTRY.register("raw_dumpling", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAW_WONTON = REGISTRY.register("raw_wonton", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> BAISED_TOFU_SLICES = REGISTRY.register("baised_tofu_slices", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> BOILEDRICEFLOURNOODLES = REGISTRY.register("boiledriceflournoodles", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> BONESOUP = REGISTRY.register("bonesoup", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> BUCKWHEATRICE = REGISTRY.register("buckwheatrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> CHOCOLATE = REGISTRY.register("chocolate", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> CONGEEWITH_MINCE_PORKAND_PRESERVED_EGG = REGISTRY.register("congeewith_minced_porkand_preserved_egg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> COOKEDCHICKENBREAST = REGISTRY.register("cookedchickenbreast", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> COOKEDCHICKENLEAN = REGISTRY.register("cookedchickenlean", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> COOKEDCORNBATTER = REGISTRY.register("cookedcornbatter", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> COOKEDCRISPYPORK = REGISTRY.register("cookedcrispypork", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.7f).alwaysEdible().build())));

    public static final DeferredItem<Item> COOKEDFLAKEBEEF = REGISTRY.register("cookedflakebeef", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.7f).alwaysEdible().build())));

    public static final DeferredItem<Item> COOKEDHALFOFCHCIKENLEG = REGISTRY.register("cookedhalfofchcikenleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> COOKEDPIGSTREAKMEAT = REGISTRY.register("cookedpigstreakmeat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.7f).alwaysEdible().build())));

    public static final DeferredItem<Item> DRIED_TOFU_SKIN = REGISTRY.register("dried_tofu_skin", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> BEANSPROUT = REGISTRY.register("beansprout", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EATENPINEAPPLE = REGISTRY.register("eatenpineapple", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.2f).alwaysEdible().build())));

    public static final DeferredItem<Item> EGGBISCUIT = REGISTRY.register("eggbiscuit", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> EGGPANCAKE = REGISTRY.register("eggpancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> EGGTART = REGISTRY.register("eggtart", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> EIGHTTREASURECONGEE = REGISTRY.register("eighttreasurecongee", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> ELECTROLYTEBEVERAGE = REGISTRY.register("electrolytebeverage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.2f).alwaysEdible().build())));

    public static final DeferredItem<Item> FANHUAROLL = REGISTRY.register("fanhuaroll", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> FRIEDCHICKENCHOP = REGISTRY.register("friedchickenchop", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> FRIEDCHICKENCORN = REGISTRY.register("friedchickencorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> FRIEDCHICKENLEG = REGISTRY.register("friedchickenleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> FRIEDCHICKENWING = REGISTRY.register("friedchickenwing", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> FRIEDSAUSAGE = REGISTRY.register("friedsausage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> GRAPEWINE = REGISTRY.register("grapewine", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.2f).alwaysEdible().build())));

    public static final DeferredItem<Item> GREENBEANPORRIDGE = REGISTRY.register("greenbeanporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> HANDGRABBEDPANCAKE = REGISTRY.register("handgrabbedpancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> HEALTHLOQUATCREAM = REGISTRY.register("healthloquatcream", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.2f).alwaysEdible().build())));

    public static final DeferredItem<Item> HEALTHPEANUTMILK = REGISTRY.register("healthpeanutmilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> HEALTHWALNUTDEW = REGISTRY.register("healthwalnutdew", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> ICEDBLACKTEA = REGISTRY.register("icedblacktea", () -> new com.flavor_immersed_daily.item.DrinkItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> KAOLIANGPORRIDGE = REGISTRY.register("kaoliangporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> KAOLIANGRICE = REGISTRY.register("kaoliangrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> LABAPORRIDGE = REGISTRY.register("labaporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> MILLIETPORRIDGE = REGISTRY.register("millietporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> MILLIETRICE = REGISTRY.register("millietrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> NOILEDRICENOODLE = REGISTRY.register("noiledricenoodle", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> OAKPORRIDGE = REGISTRY.register("oakporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> OAKRICE = REGISTRY.register("oakrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> OMELETTE = REGISTRY.register("omelette", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> PANADAPANCAKE = REGISTRY.register("panadapancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> PEAFLOURCAKE = REGISTRY.register("peaflourcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> POTATOCHIPS = REGISTRY.register("potatochips", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> REDBEANEGGTART = REGISTRY.register("redbeaneggtart", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> REDBEANSTUFFINGDORAYAKI = REGISTRY.register("redbeanstuffingdorayaki", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> RICE = REGISTRY.register("rice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> RICE_PORRIDGE = REGISTRY.register("rice_porridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> RICEPASTESOUP = REGISTRY.register("ricepastesoup", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> ROASTCHICKENFORK = REGISTRY.register("roastchickenfork", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<Item> ROASTEDSWEETPOTATO = REGISTRY.register("roastedsweetpotato", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> SHELLEDBOILEDEGG = REGISTRY.register("shelledboiledegg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> SHOU_KAI_XIN_GUO = REGISTRY.register("shou_kai_xin_guo", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> SMALLMICEDMEATCAKE = REGISTRY.register("smallmicedmeatcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> STEAMEDSALTORANGE = REGISTRY.register("steamedsaltorange", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> STEAMEDVERMICELLIROLL_0 = REGISTRY.register("steamedvermicelliroll_0", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> STRAWBERRYCAKEROLL = REGISTRY.register("strawberrycakeroll", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> SWEETMILK = REGISTRY.register("sweetmilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> TOFU_SAUSAGE = REGISTRY.register("tofu_sausage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> TOFU_STICKS = REGISTRY.register("tofu_sticks", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> WALNUTCAKE = REGISTRY.register("walnutcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> WALNUTSHORTBREAD = REGISTRY.register("walnutshortbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

    public static final DeferredItem<Item> WHEATMILK = REGISTRY.register("wheatmilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredItem<Item> YUBA = REGISTRY.register("yuba", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    public static final DeferredItem<Item> TOUFU = REGISTRY.register("toufu", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.8f).alwaysEdible().build())));

    public static final DeferredItem<Item> EGGCAKE = REGISTRY.register("eggcake", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.7f).alwaysEdible().build())));

    public static final DeferredItem<Item> GOLDRICECAKEMAX = REGISTRY.register("goldricecakemax", () -> new Item(new Item.Properties()
            .rarity(Rarity.EPIC)
            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
            .food(new FoodProperties.Builder()
                    .nutrition(4).saturationModifier(9.6f)
                    .effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 1), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3), 1.0f)
                    .alwaysEdible().build())));

    public static final DeferredItem<Item> GOLDENGRAPEMAX = REGISTRY.register("goldengrapemax", () -> new Item(new Item.Properties()
            .rarity(Rarity.EPIC)
            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
            .food(new FoodProperties.Builder()
                    .nutrition(4).saturationModifier(9.6f)
                    .effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 1), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3), 1.0f)
                    .alwaysEdible().build())));

    public static final DeferredItem<PurifiedWaterBucketItem> FILTERVAT = REGISTRY.register("filtervat",
            () -> new PurifiedWaterBucketItem(new Item.Properties()));

    public static final DeferredItem<FairySparklerItem> FAIRY_SPARKLER = REGISTRY.register("fairy_sparkler",
            () -> new FairySparklerItem(new Item.Properties().stacksTo(1).durability(500)));

    private static List<ItemStack> deadAnimalIcons() {
        return List.of(new ItemStack(BONECUTTERKNIFE.get()), new ItemStack(ModBlocks.BIGHOOK.asItem()));
    }

    private static List<ItemStack> chickenBloodIcons() {
        return List.of(new ItemStack(ModBlocks.WOODBASIN.asItem()), new ItemStack(TIDYWATER.get()));
    }

    private static List<ItemStack> reedleafTooltipIcon() {
        return List.of(new ItemStack(KITCHENSCISSOR.get()));
    }
}
