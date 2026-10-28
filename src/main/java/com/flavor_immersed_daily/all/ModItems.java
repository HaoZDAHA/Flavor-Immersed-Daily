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
import com.flavor_immersed_daily.item.FillingFoodItem;
import com.flavor_immersed_daily.item.MooncakeFoodItem;
import com.flavor_immersed_daily.item.MooncakeMoldItem;
import com.flavor_immersed_daily.item.KitchenScissorsItem;
import com.flavor_immersed_daily.item.ChineseLeavesItem;
import com.flavor_immersed_daily.item.WrestlingGunItem;
import com.flavor_immersed_daily.item.SeasoningItem;
import com.flavor_immersed_daily.item.WugangAxeItem;
import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.client.tooltip.TooltipItem;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public final class ModItems {
    private ModItems() {
    }

    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, FlavorImmersedDaily.MODID);

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

    private static RegistryObject<Item> item(String name) {
        return REGISTRY.register(name, () -> new Item(new Item.Properties()));
    }

    private static RegistryObject<Item> food(String name, int nutrition, float saturationModifier) {
        return REGISTRY.register(name, () -> new Item(new Item.Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition)
                        .saturationMod(saturationModifier)
                        .alwaysEat()
                        .build())));
    }

    private static RegistryObject<Item> drink(String name) {
        return food(name, 4, 0.3f);
    }

// 有馅食物：馅料记录在 NBT "filling" 中，tooltip 显示"馅料口味"；不预设默认馅料
    private static RegistryObject<Item> filledItem(String name) {
        return REGISTRY.register(name, () -> new FillingFoodItem(new Item.Properties()));
    }

// 有馅食物：馅料记录在 NBT "filling" 中，tooltip 显示"馅料口味"；不预设默认馅料
    private static RegistryObject<Item> filledFood(String name, int nutrition, float saturationModifier) {
        return REGISTRY.register(name, () -> new FillingFoodItem(new Item.Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition)
                        .saturationMod(saturationModifier)
                        .alwaysEat()
                        .build())));
    }

// 特色月饼：在馅料口味之外，食用后获得对应 buff 效果（tooltip 显示 buff，且按 Shift 展开详情）
    private static RegistryObject<Item> mooncakeFood(String name, int nutrition, float saturationModifier,
                                                     Supplier<? extends Holder<MobEffect>> effect,
                                                     BooleanSupplier enabled) {
        return REGISTRY.register(name, () -> new MooncakeFoodItem(new Item.Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition)
                        .saturationMod(saturationModifier)
                        .alwaysEat()
                        .build()), effect, enabled));
    }

    private static RegistryObject<Item> rareFruit(String name, int nutrition, float saturationModifier) {
        return REGISTRY.register(name, () -> new RareFruitVariantItem(new Item.Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition)
                        .saturationMod(saturationModifier)
                        .alwaysEat()
                        .build())));
    }

    private static RegistryObject<Item> seedableFruit(String name, String seedId) {
        return REGISTRY.register(name, () -> new SeedableFruitItem(new Item.Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(2)
                        .saturationMod(0.2f)
                        .alwaysEat()
                        .build()), seedId));
    }

    private static RegistryObject<Item> wildHarvest(String name, String tooltipKey) {
        return REGISTRY.register(name, () -> new WildHarvestItem(new Item.Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(3)
                        .saturationMod(0.3f)
                        .alwaysEat()
                        .build()), Component.translatable(tooltipKey)));
    }

// 创造模式标签页的图标物品（仅作图标使用，不列入任何标签页内容）
    public static final RegistryObject<Item> ALL = item("all");

// Popsicles
    public static final RegistryObject<Item> APPLEPOPSICLE = REGISTRY.register("applepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> APRICOTPOPSICLE = REGISTRY.register("apricotpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> BANANAPOPSICLE = REGISTRY.register("bananapopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> BLUEBERRYPOPSICLE = REGISTRY.register("blueberrypopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> CARAMBOLAPOPSICLE = REGISTRY.register("carambolapopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> CHERRYPOPSICLE = REGISTRY.register("cherrypopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> CHOCOLATEPOPSICLE = REGISTRY.register("chocolatepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COCONUTPOPSICLE = REGISTRY.register("coconutpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COFFEEPOPSICLE = REGISTRY.register("coffeepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> DRAGONFRUITPOPSICLE = REGISTRY.register("dragonfruitpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> DURIANPOPSICLE = REGISTRY.register("durianpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> GRAPEPOPSICLE = REGISTRY.register("grapepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> GREENGRAPEPOPSICLE = REGISTRY.register("greengrapepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> GREENPLUMPOPSICLE = REGISTRY.register("greenplumpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> HAMIMELONPOPSICLE = REGISTRY.register("hamimelonpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> HAWTHORNPOPSICLE = REGISTRY.register("hawthornpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> HONEYPEACHPOPSICLE = REGISTRY.register("honeypeachpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> KIWIFRUITPOPSICLE = REGISTRY.register("kiwifruitpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> LEMONPOPSICLE = REGISTRY.register("lemonpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> LOQUATPOPSICLE = REGISTRY.register("loquatpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> LYCHEEPOPSICLE = REGISTRY.register("lycheepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> MANGOPOPSICLE = REGISTRY.register("mangopopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> MANGOSTEEPOPSICLE = REGISTRY.register("mangosteenpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> MULBERRYPOPSICLE = REGISTRY.register("mulberrypopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> NECTARINEPOPSICLE = REGISTRY.register("nectarinepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> ORANGEPOPSICLE = REGISTRY.register("orangepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> PAWPAWPOPSICLE = REGISTRY.register("pawpawpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> PEARPOPSICLE = REGISTRY.register("pearpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> PINEAPPLEPOPSICLE = REGISTRY.register("pineapplepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> PLUMPOPSICLE = REGISTRY.register("plumpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> POMEGRANATEPOPSICLE = REGISTRY.register("pomegranatepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> POPSICLE = REGISTRY.register("popsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> STRAWBERRYPOPSICLE = REGISTRY.register("strawberrypopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> SWEETBERRYPOPSICLE = REGISTRY.register("sweetberrypopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> SWEETMELONPOPSICLE = REGISTRY.register("sweetmelonpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> TANGERINEPOPSICLE = REGISTRY.register("tangerinepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> WATERMELONPOPSICLE = REGISTRY.register("watermelonpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> WINTERJUJUBEPOPSICLE = REGISTRY.register("winterjujubepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6f)
                            .alwaysEat()
                            .build())));

// Ice Cream ϵ��ʳƷ
    public static final RegistryObject<Item> APPLEICECREAM = REGISTRY.register("appleicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> APRICOTICECREAM = REGISTRY.register("apricoticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> BANANAICECREAM = REGISTRY.register("bananaicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> BLUEBERRYICECREAM = REGISTRY.register("blueberryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> CARAMBOLAICECREAM = REGISTRY.register("carambolaicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> CHERRYICECREAM = REGISTRY.register("cherryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> CHOCOLATEICECREAM = REGISTRY.register("chocolateicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COCONUTICECREAM = REGISTRY.register("coconuticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COFFEEICECREAM = REGISTRY.register("coffeeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> DRAGONFRUITICECREAM = REGISTRY.register("dragonfruiticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> DURIANICECREAM = REGISTRY.register("durianicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> GRAPEICECREAM = REGISTRY.register("grapeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> GREENGRAPEICECREAM = REGISTRY.register("greengrapeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> GREENPLUMICECREAM = REGISTRY.register("greenplumicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> HAMIMELONICECREAM = REGISTRY.register("hamimelonicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> HAWTHORNICECREAM = REGISTRY.register("hawthornicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> HONEYPEACHICECREAM = REGISTRY.register("honeypeachicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> KIWIFRUITICECREAM = REGISTRY.register("kiwifruiticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> LEMONICECREAM = REGISTRY.register("lemonicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> LOQUATICECREAM = REGISTRY.register("loquaticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> LYCHEEICECREAM = REGISTRY.register("lycheeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> MANGOICECREAM = REGISTRY.register("mangoicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> MANGOSTEENICECREAM = REGISTRY.register("mangosteenicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> MILKICECREAM = REGISTRY.register("milkicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> MULBERRYICECREAM = REGISTRY.register("mulberryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> NECTARINEICECREAM = REGISTRY.register("nectarineicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> ORANGEICECREAM = REGISTRY.register("orangeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> PAWPAWICECREAM = REGISTRY.register("pawpawicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> PEARICECREAM = REGISTRY.register("pearicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> PINEAPPLEICECREAM = REGISTRY.register("pineappleicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> PLUMICECREAM = REGISTRY.register("plumicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> POMEGRANATEICECREAM = REGISTRY.register("pomegranateicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> STRAWBERRYICECREAM = REGISTRY.register("strawberryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> SWEETBERRYICECREAM = REGISTRY.register("sweetberryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> SWEETMELONICECREAM = REGISTRY.register("sweetmelonicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> TANGERINEICECREAM = REGISTRY.register("tangerineicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> WATERMELONICECREAM = REGISTRY.register("watermelonicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> WINTERJUJUBEICECREAM = REGISTRY.register("winterjujubeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())));

// Grains, mushrooms, tea, and crop products
    public static final RegistryObject<Item> KAOLIANGGRAIN = REGISTRY.register("kaolianggrain",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.3f).alwaysEat().build())));
    public static final RegistryObject<Item> WHITEMUSHROOM = REGISTRY.register("whitemushroom",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.2f).alwaysEat().build())));
    public static final RegistryObject<Item> BLACKFUNGUS = REGISTRY.register("blackfungus",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.3f).alwaysEat().build())));
    public static final RegistryObject<Item> DRYBLACKFUNGUS = REGISTRY.register("dryblackfungus", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PLEUROTUSERYNGII = REGISTRY.register("pleurotuseryngii",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(3).saturationMod(0.4f).alwaysEat().build())));
    public static final RegistryObject<Item> ENOKIMUSHROOM = REGISTRY.register("enokimushroom",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.2f).alwaysEat().build())));
    public static final RegistryObject<Item> TREMELLA = REGISTRY.register("tremella",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.3f).alwaysEat().build())));
    public static final RegistryObject<Item> FRAGRANTMUSHROOM = REGISTRY.register("fragrantmushroom",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(3).saturationMod(0.4f).alwaysEat().build())));
    public static final RegistryObject<Item> DRYFRAGRANTMUSHROOM = REGISTRY.register("dryfragrantmushroom", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREENTEALEAVES = REGISTRY.register("greentealeaves", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DRYGREENTEA = REGISTRY.register("drygreentea", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> REDTEALEAVES = REGISTRY.register("redtealeaves", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DRYREDTEA = REGISTRY.register("dryredtea", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LOTUSROOT = REGISTRY.register("lotusroot",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(3).saturationMod(0.4f).alwaysEat().build())));
    public static final RegistryObject<Item> PADDYGRAIN = REGISTRY.register("paddygrain", () -> new Item(new Item.Properties()));

// Drinks
    public static final RegistryObject<Item> DRINKINGWATER = drink("drinkingwater");
    public static final RegistryObject<Item> CAPPUCCINO = drink("cappuccino");
    public static final RegistryObject<Item> CAFELATTE = drink("cafelatte");
    public static final RegistryObject<Item> MOCHACAFE = drink("mochacafe");
    public static final RegistryObject<Item> HOTWATER = drink("hotwater");
    public static final RegistryObject<Item> CAFE_AMERICANO = drink("cafe_americano");
    public static final RegistryObject<Item> LEMONREDTEA = drink("lemonredtea");
    public static final RegistryObject<Item> GRENTEAMILKWITHPEARL = drink("grenteamilkwithpearl");
    public static final RegistryObject<Item> GREEN_TEA_LATTES = drink("green_tea_lattes");
    public static final RegistryObject<Item> LEMONTEAWITHGLUTINOUSRICEFLAVOR = drink("lemonteawithglutinousriceflavor");
    public static final RegistryObject<Item> REDTEA = drink("redtea");
    public static final RegistryObject<Item> GREENTEA = drink("greentea");
    public static final RegistryObject<Item> BUCKWHEATTEA = drink("buckwheattea");
    public static final RegistryObject<Item> ASSAMMILK_TEA = drink("assammilk_tea");
    public static final RegistryObject<Item> GREENPLUMTEA = drink("greenplumtea");
    public static final RegistryObject<Item> REDTEAMILKWITHPEARL = drink("redteamilkwithpearl");
    public static final RegistryObject<Item> KAOLIANGWINE = drink("kaoliangwine");
    public static final RegistryObject<Item> KWEICHOW_MOUTAI = drink("kweichow_moutai");
    public static final RegistryObject<Item> TSINGTAO_BEER = drink("tsingtao_beer");
    public static final RegistryObject<Item> ICEDBLACKTEABLUE = drink("icedblackteablue");
    public static final RegistryObject<Item> SUGARFREICEDTEA = drink("sugarfreicedtea");
    public static final RegistryObject<Item> SPRITE = drink("sprite");

// Fruit
    public static final RegistryObject<Item> APIECEOFBANANA = food("apieceofbanana", 4, 0.3f);
    public static final RegistryObject<Item> BANANA = food("banana", 4, 0.3f);
    public static final RegistryObject<Item> PULLEDBANANA_2 = food("pulledbanana_2", 6, 0.6f);
    public static final RegistryObject<Item> ORANGE = food("orange", 4, 0.3f);
    public static final RegistryObject<Item> BLOODORANGE = rareFruit("bloodorange", 6, 0.5f);
    public static final RegistryObject<Item> TANGERINE = food("tangerine", 4, 0.3f);
    public static final RegistryObject<Item> TANGERINE_1 = rareFruit("tangerine_1", 6, 0.5f);
    public static final RegistryObject<Item> UGLYORANGE = rareFruit("uglyorange", 6, 0.5f);
    public static final RegistryObject<Item> LEMON = food("lemon", 3, 0.2f);
    public static final RegistryObject<Item> HAMIMELON = food("hamimelon", 6, 0.6f);
    public static final RegistryObject<Item> SWEETMELON = food("sweetmelon", 6, 0.6f);
    public static final RegistryObject<Item> SWEETMELON_1 = rareFruit("sweetmelon_1", 8, 0.8f);
    public static final RegistryObject<Item> COCONUT = REGISTRY.register("coconut", () -> new ThrowableFruitItem(new Item.Properties(), "flavor_immersed_daily:coconut_shell", 1, "flavor_immersed_daily:coconutmeat", 2, "", 0, 4.0f));
    public static final RegistryObject<Item> COCONUTMEAT = food("coconutmeat", 6, 0.6f);
    public static final RegistryObject<Item> COCONUT_SHELL = food("coconut_shell", 2, 0.3f);
    public static final RegistryObject<Item> DURIAN = REGISTRY.register("durian", () -> new ThrowableFruitItem(new Item.Properties(), "flavor_immersed_daily:durianmeat", 2, "flavor_immersed_daily:durianshellhat", 1, "", 0, 6.0f));
    public static final RegistryObject<Item> DURIANMEAT = food("durianmeat", 8, 0.8f);
    public static final RegistryObject<Item> DURIANSHELLHAT = REGISTRY.register("durianshellhat", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLUEBERRY = food("blueberry", 2, 0.2f);
    public static final RegistryObject<Item> GRAPE = seedableFruit("grape", "flavor_immersed_daily:agrape");
    public static final RegistryObject<Item> GREENGRAPE = seedableFruit("greengrape", "flavor_immersed_daily:agreengrape");
    public static final RegistryObject<Item> MULBERRY = food("mulberry", 2, 0.2f);
    public static final RegistryObject<Item> STRAWBERRY = food("strawberry", 2, 0.2f);
    public static final RegistryObject<Item> APRICOT = food("apricot", 4, 0.3f);
    public static final RegistryObject<Item> CHERRY = food("cherry", 3, 0.3f);
    public static final RegistryObject<Item> GREENPLUM = food("greenplum", 3, 0.3f);
    public static final RegistryObject<Item> HONEYPEACH = food("honeypeach", 4, 0.3f);
    public static final RegistryObject<Item> LIFEPEACH = rareFruit("lifepeach", 6, 0.5f);
    public static final RegistryObject<Item> NECTARINE = food("nectarine", 4, 0.3f);
    public static final RegistryObject<Item> PLUM = food("plum", 3, 0.3f);
    public static final RegistryObject<Item> WINTERJUJUBE = food("winterjujube", 4, 0.3f);
    public static final RegistryObject<Item> CARAMBOLA = food("carambola", 4, 0.3f);
    public static final RegistryObject<Item> DRAGONFRUIT = food("dragonfruit", 4, 0.3f);
    public static final RegistryObject<Item> KIWIFRUIT = food("kiwifruit", 4, 0.3f);
    public static final RegistryObject<Item> LYCHEE = food("lychee", 3, 0.3f);
    public static final RegistryObject<Item> MANGO = food("mango", 4, 0.3f);
    public static final RegistryObject<Item> MANGOSTEEN = food("mangosteen", 4, 0.3f);
    public static final RegistryObject<Item> PAWPAW = food("pawpaw", 4, 0.3f);
    public static final RegistryObject<Item> PINEAPPLE = food("pineapple", 5, 0.4f);
    public static final RegistryObject<Item> PISTACHIONUT = food("pistachionut", 3, 0.3f);
    public static final RegistryObject<Item> REDDATE = food("reddate", 4, 0.3f);
    public static final RegistryObject<Item> WALNUT = food("walnut", 3, 0.3f);
    public static final RegistryObject<Item> DRY_PISTACHIONUT = food("dry_pistachionut", 5, 0.5f);
    public static final RegistryObject<Item> PISTACHIONUT_0 = food("pistachionut_0", 3, 0.3f);
    public static final RegistryObject<Item> PISTACHIONUTSWITHOPENSHELLS = food("pistachionutswithopenshells", 3, 0.3f);
    public static final RegistryObject<Item> WALNUTKINNEL = food("walnutkinnel", 4, 0.4f);
    public static final RegistryObject<Item> GRAPESEED = REGISTRY.register("grapeseed", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CINNAMON = REGISTRY.register("cinnamon", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREENAPPLE = rareFruit("greenapple", 4, 0.3f);
    public static final RegistryObject<Item> HAWTHORN = food("hawthorn", 3, 0.3f);
    public static final RegistryObject<Item> LOQUAT = food("loquat", 4, 0.3f);
    public static final RegistryObject<Item> PEAR = food("pear", 4, 0.3f);
    public static final RegistryObject<Item> POMEGRANATE = food("pomegranate", 5, 0.4f);

// Vine crop seeds and produce
    public static final RegistryObject<Item> CUCUMBERSEEDS = REGISTRY.register("cucumberseeds", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WAX_GOURD_SEED_BLOCK = REGISTRY.register("wax_gourd_seed_block", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> KIDNEYBEANSEED = REGISTRY.register("kidneybeanseed", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AUBERGINESEEDBLOCK = REGISTRY.register("aubergineseedblock", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TOMATOSEED = REGISTRY.register("tomatoseed", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COWPEABEANSEED = REGISTRY.register("cowpeabeanseed", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREENGRAESEED = REGISTRY.register("greengrapeseed", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LOOFAHSEED = REGISTRY.register("loofahseed", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CUCUMBER = food("cucumber", 3, 0.3f);
    public static final RegistryObject<Item> WAXGOURD = food("waxgourd", 3, 0.3f);
    public static final RegistryObject<Item> KIDNEYBEAN = food("kidneybean", 3, 0.3f);
    public static final RegistryObject<Item> AUBERGINE = food("aubergine", 3, 0.3f);
    public static final RegistryObject<Item> TOMATO = food("tomato", 3, 0.3f);
    public static final RegistryObject<Item> COWPEA = food("cowpea", 3, 0.3f);
    public static final RegistryObject<Item> LOOFAH = food("loofah", 3, 0.3f);

// Wild harvests
    public static final RegistryObject<Item> TEMPERATEWILDFRUIT = wildHarvest("temperatewildfruit", "tooltip.flavor_immersed_daily.wild_fruit_harvest");
    public static final RegistryObject<Item> TROPICALWILD_FRUIT = wildHarvest("tropicalwild_fruit", "tooltip.flavor_immersed_daily.wild_fruit_harvest");
    public static final RegistryObject<Item> WILDFLOWERANDLEAF = wildHarvest("wildflowerandleaf", "tooltip.flavor_immersed_daily.wild_crop_harvest");
    public static final RegistryObject<Item> WILDFRUITINCOLDZONE = wildHarvest("wildfruitincoldzone", "tooltip.flavor_immersed_daily.wild_fruit_harvest");
    public static final RegistryObject<Item> WILDGRAINPLANT = wildHarvest("wildgrainplant", "tooltip.flavor_immersed_daily.wild_crop_harvest");
    public static final RegistryObject<Item> WILDMUSHROOMPLANT = wildHarvest("wildmushroomplant", "tooltip.flavor_immersed_daily.wild_crop_harvest");
    public static final RegistryObject<Item> WILDSEEDPLANT = wildHarvest("wildseedplant", "tooltip.flavor_immersed_daily.wild_crop_harvest");
    public static final RegistryObject<Item> WILDTUBERPLANTS = wildHarvest("wildtuberplants", "tooltip.flavor_immersed_daily.wild_crop_harvest");

// Simple foods
    public static final RegistryObject<Item> BAKEDWHITEMUSHROOMSWITHCREAM = REGISTRY.register(
            "bakedwhitemushroomswithcream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(8)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));
    public static final RegistryObject<Item> BRAISEDBEANSPROUTSWITHVERMICELLI = REGISTRY.register("braisedbeansproutswithvermicelli",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).alwaysEat().build())));
    public static final RegistryObject<Item> CANTONESERICEROLLS = REGISTRY.register("cantonesericerolls",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).alwaysEat().build())));
    public static final RegistryObject<Item> CURRYSTEWEDCHICKEN = REGISTRY.register("currystewedchicken",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).alwaysEat().build())));
    public static final RegistryObject<Item> GUMBOSOUP = REGISTRY.register("gumbosoup",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).alwaysEat().build())));
    public static final RegistryObject<Item> REDDATESANDTREMELLAPORRIDGE = REGISTRY.register("reddatesandtremellaporridge",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).alwaysEat().build())));
    public static final RegistryObject<Item> SA = REGISTRY.register("sa",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).alwaysEat().build())));
    public static final RegistryObject<Item> SCRAMBLEDEGGWITHTOMATO = REGISTRY.register("scrambledeggwithtomato",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).alwaysEat().build())));
    public static final RegistryObject<Item> SHEEPGIBLETSSOUP = REGISTRY.register("sheepgibletssoup",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).alwaysEat().build())));
    public static final RegistryObject<Item> STEAMEDEGGCUSTARD = REGISTRY.register("steamedeggcustard",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).alwaysEat().build())));
    public static final RegistryObject<Item> STEWEDCHICKENSOUPWITHMUSHROOMS = REGISTRY.register("stewedchickensoupwithmushrooms",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).alwaysEat().build())));
    public static final RegistryObject<Item> STEWEDCHICKENWITHWAXGOURD = REGISTRY.register("stewedchickenwithwaxgourd",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).alwaysEat().build())));
    public static final RegistryObject<Item> STEWEDEGGSWITHLOOFAH = REGISTRY.register("stewedeggswithloofah",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).alwaysEat().build())));
    public static final RegistryObject<Item> STEWEDPORKOFFAL = REGISTRY.register("stewedporkoffal",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).alwaysEat().build())));
    public static final RegistryObject<Item> WINTERJUJUBEANDWAXGOURDSOUP = REGISTRY.register("winterjujubeandwaxgourdsoup",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).alwaysEat().build())));

// Crop products
    public static final RegistryObject<Item> ONION = food("onion", 2, 0.3f);
    public static final RegistryObject<Item> PEA = food("pea", 1, 0.2f);
    public static final RegistryObject<Item> PURPLESWEETPOTATO = food("purplesweetpotato", 2, 0.3f);
    public static final RegistryObject<Item> SESAME = food("sesame", 1, 0.2f);
    public static final RegistryObject<Item> SOYBEAN = food("soybean", 1, 0.2f);
    public static final RegistryObject<Item> SWEETGREENPEPPER = food("sweetgreenpepper", 2, 0.3f);
    public static final RegistryObject<Item> ZUCCHINI = food("zucchini", 2, 0.3f);

// Basic ingredients
    public static final RegistryObject<Item> SORBET = item("sorbet");
    public static final RegistryObject<Item> CASING = item("casing");
    public static final RegistryObject<Item> HONEYCOMBBRIQUET = item("honeycombbriquet");
    public static final RegistryObject<Item> BRAN = item("bran");
    public static final RegistryObject<Item> CASSAVAPEARL = item("cassavapearl");
    public static final RegistryObject<Item> SOAKEDSOYBEANS = item("soakedsoybeans");
    public static final RegistryObject<Item> MEATFLOSS = food("meatfloss", 12, 0.8f);
    public static final RegistryObject<Item> GRAVELPASTE = item("gravelpaste");
    public static final RegistryObject<Item> RAWSOYSHREDDEDMEAT = item("rawsoyshreddedmeat");
    public static final RegistryObject<Item> LANDPLASTER = item("landplaster");
    public static final RegistryObject<Item> WRESTLING_GUN = REGISTRY.register("wrestling_gun",
            () -> new WrestlingGunItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> RAWSHEEPOFFAL = item("rawsheepoffal");
    public static final RegistryObject<Item> TIDYREEDLEAF = item("tidyreedleaf");
    public static final RegistryObject<Item> NAHCO_3 = item("nahco_3");
    public static final RegistryObject<Item> PROBIOTICS = item("probiotics");

// Prepared vegetables and meat
    public static final RegistryObject<Item> SCALLION_2 = item("scallion_2");
    public static final RegistryObject<Item> DICEDCARROTS = item("dicedcarrots");
    public static final RegistryObject<Item> SHREDDEDCARROTS = item("shreddedcarrots");
    public static final RegistryObject<Item> DICEDCUCUMBER = item("dicedcucumber");
    public static final RegistryObject<Item> SLICEDCUCUMBER = item("slicedcucumber");
    public static final RegistryObject<Item> SHREDDERCUCUMBER = item("shreddercucumber");
    public static final RegistryObject<Item> SLICEDABUERGINE = item("slicedabuergine");
    public static final RegistryObject<Item> SHREDDERABUERGINE = item("shredderabuergine");
    public static final RegistryObject<Item> CHOPPEDCOWPEA = item("choppedcowpea");
    public static final RegistryObject<Item> DIECEDRADISH = item("diecedradish");
    public static final RegistryObject<Item> DICEDRADISH = item("dicedradish");
    public static final RegistryObject<Item> SLICEDRADISH = item("slicedradish");
    public static final RegistryObject<Item> SHREDDERRADISH = item("shredderradish");
    public static final RegistryObject<Item> DICEDCASSAVA = item("dicedcassava");
    public static final RegistryObject<Item> SLICEDLEMON = item("slicedlemon");
    public static final RegistryObject<Item> DICEDLOTUSROOT = item("dicedlotusroot");
    public static final RegistryObject<Item> SLICEDLOTUSROOT = item("slicedlotusroot");
    public static final RegistryObject<Item> DICEDONION = item("dicedonion");
    public static final RegistryObject<Item> DICEDORANGE = item("dicedorange");
    public static final RegistryObject<Item> DICEDWAXGOURD = item("dicedwaxgourd");
    public static final RegistryObject<Item> DICEDPLEUROTUSERYNGII = item("dicedpleurotuseryngii");
    public static final RegistryObject<Item> DICEDCELERY = item("dicedcelery");
    public static final RegistryObject<Item> DICEDSWEETPEPPER = item("dicedsweetpepper");
    public static final RegistryObject<Item> DICEDCHINESEYAM = item("dicedchineseyam");
    public static final RegistryObject<Item> DICEDFISH = item("dicedfish");
    public static final RegistryObject<Item> DICEDLOOFACH = item("dicedloofach");
    public static final RegistryObject<Item> DICEDSAUTEEDGREENBEANS = item("dicedsauteedgreenbeans");
    public static final RegistryObject<Item> DICEDPOTATO = item("dicedpotato");
    public static final RegistryObject<Item> SLICEDPOTATO = item("slicedpotato");
    public static final RegistryObject<Item> SHREDDEDPOTATO_2 = item("shreddedpotato_2");
    public static final RegistryObject<Item> CHOPPEDPOTATO_1 = item("choppedpotato_1");
    public static final RegistryObject<Item> DICEDZUCCHINI = item("dicedzucchini");
    public static final RegistryObject<Item> DICEDPURPLESWEETPOTATO = item("dicedpurplesweetpotato");

// Dough, wrappers, and noodles
    public static final RegistryObject<Item> BAOZI_SKIN = item("baozi_skin");
    public static final RegistryObject<Item> VERMICELLIROLL = item("vermicelliroll");
    public static final RegistryObject<Item> BEATGLUTINOUSRICEFLOURPASTE = item("beatglutinousriceflourpaste");
    public static final RegistryObject<Item> DRYPOWDERSKIN = item("drypowderskin");
    public static final RegistryObject<Item> DRIEDVERMICELLI = item("driedvermicelli");
    public static final RegistryObject<Item> DRYPOWDERSTRIP = item("drypowderstrip");
    public static final RegistryObject<Item> EGGNOODLESWRAPPEDINSYRUP = item("eggnoodleswrappedinsyrup");
    public static final RegistryObject<Item> NOODLESWRAPPEDINSYRUP = item("noodleswrappedinsyrup");
    public static final RegistryObject<Item> WALNUTFLOURPASTE = item("walnutflourpaste");
    public static final RegistryObject<Item> SWEETPOTATODOUGH = item("sweetpotatodough");
    public static final RegistryObject<Item> FLOURSKIN = item("flourskin");
    public static final RegistryObject<Item> EGGDOUGH = item("eggdough");
    public static final RegistryObject<Item> RAWEGGNOODLES = item("raweggnoodles");
    public static final RegistryObject<Item> DUMPLING_SKIN = item("dumpling_skin");
    public static final RegistryObject<Item> RAWRICENOODLES = item("rawricenoodles");
    public static final RegistryObject<Item> RICENOODLES_2 = item("ricenoodles_2");
    public static final RegistryObject<Item> FLOURPASTE = item("flourpaste");
    public static final RegistryObject<Item> DOUGH = item("dough");
    public static final RegistryObject<Item> SHUMAISKIN = item("shumaiskin");
    public static final RegistryObject<Item> RAWCOARSENOODLES = item("rawcoarsenoodles");
    public static final RegistryObject<Item> DRYRICECAKE = item("dryricecake");
    public static final RegistryObject<Item> RAWEGGPANCAKE = item("raweggpancake");
    public static final RegistryObject<Item> RAWEGGPASTE = item("raweggpaste");
    public static final RegistryObject<Item> RAWEGGSKIN = item("raweggskin");
    public static final RegistryObject<Item> RAWFLOURPANCAKE = item("rawflourpancake");
    public static final RegistryObject<Item> RAWFLOURROLL = item("rawflourroll");
    public static final RegistryObject<Item> RAWNOODLES = item("rawnoodles");
    public static final RegistryObject<Item> RAWGLUTINOUSRICEFLOURPANCAKE = item("rawglutinousriceflourpancake");
    public static final RegistryObject<Item> RAWGLUTINOUSPASTE = item("rawglutinouspaste");
    public static final RegistryObject<Item> RAWGLUTINOUSSKIN = item("rawglutinousskin");
    public static final RegistryObject<Item> RAWGLUTINOUSNOODLES = item("rawglutinousnoodles");
    public static final RegistryObject<Item> RAWGLUTINOUSDOUGH = item("rawglutinousdough");
    public static final RegistryObject<Item> RAWSHUMAI = item("rawshumai");
    public static final RegistryObject<Item> RAWPIE = item("rawpie");
    public static final RegistryObject<Item> RAWMOONCAKE = filledFood("rawmooncake", 1, 0.6f);
// 糖油面剂子
    public static final RegistryObject<Item> SWEET_OIL_DOUGH = food("sweet_oil_dough", 1, 0.6f);
// 水酥面剂子
    public static final RegistryObject<Item> WATER_SHORT_DOUGH = food("water_short_dough", 1, 0.6f);
// 提浆面剂子
    public static final RegistryObject<Item> SYRUP_DOUGH = food("syrup_dough", 1, 0.6f);
// 糯奶面剂子
    public static final RegistryObject<Item> GLUTINOUS_MILK_DOUGH = food("glutinous_milk_dough", 1, 0.6f);
// 生广式月饼
    public static final RegistryObject<Item> RAWCANTONESE_MOONCAKE = filledFood("rawcantonese_mooncake", 1, 0.6f);
// 生苏式月饼
    public static final RegistryObject<Item> RAWSUZHOU_MOONCAKE = filledFood("rawsuzhou_mooncake", 1, 0.6f);
// 生京式月饼
    public static final RegistryObject<Item> RAWBEIJING_MOONCAKE = filledFood("rawbeijing_mooncake", 1, 0.6f);
// 生冰皮月饼
    public static final RegistryObject<Item> RAWSNOW_SKIN_MOONCAKE = filledFood("rawsnow_skin_mooncake", 1, 0.6f);
    public static final RegistryObject<Item> OILEDFLOURSKIN = item("oiledflourskin");
    public static final RegistryObject<Item> COMPACTEDRICEBRICK = item("compactedricebrick");
    public static final RegistryObject<Item> MACARONI = item("macaroni");
    public static final RegistryObject<Item> STEAMEDRICEBRICK = item("steamedricebrick");

// Fillings, pastes, and liquids
    public static final RegistryObject<Item> BROKENWALNUT = item("brokenwalnut");
    public static final RegistryObject<Item> EGGSTUFFING = item("eggstuffing");
    public static final RegistryObject<Item> FRUITFLAVOREDMOONCAKESTUFFING = item("fruitflavoredmooncakestuffing");
    public static final RegistryObject<Item> SESAMEANDPEANUTBALLS = item("sesameandpeanutballs");
    public static final RegistryObject<Item> WAXGROUDPASTE = item("waxgroudpaste");
    public static final RegistryObject<Item> REDBEANPASTE = item("redbeanpaste");
    public static final RegistryObject<Item> PEPPERANDSALTMASS = item("pepperandsaltmass");
    public static final RegistryObject<Item> MEATANDVEGETABLESTUFFING = item("meatandvegetablestuffing");
    public static final RegistryObject<Item> MEATANDEGGPASTE = item("meatandeggpaste");
    public static final RegistryObject<Item> MEATPASTE = item("meatpaste");
    public static final RegistryObject<Item> VEGETABLEPASTE = item("vegetablepaste");
    public static final RegistryObject<Item> PEASTUFFING = item("peastuffing");
    public static final RegistryObject<Item> JUJUBEPASTE = item("jujubepaste");
    public static final RegistryObject<Item> TIDYWATER = item("tidywater");
    public static final RegistryObject<Item> EGGLIQUID = item("eggliquid");
    public static final RegistryObject<Item> PEANUTJAM = item("peanutjam");
    public static final RegistryObject<Item> EGGBATTER = item("eggbatter");
    public static final RegistryObject<Item> COCOASAUCE = item("cocoasauce");
    public static final RegistryObject<Item> PANADA = item("panada");
    public static final RegistryObject<Item> MILKBOTTLE = item("milkbottle");
    public static final RegistryObject<Item> CREAM = item("cream");
    public static final RegistryObject<Item> CONCENTRATEDSYRUP = item("concentratedsyrup");
    public static final RegistryObject<Item> GLUTINOUSRICEBATTER = item("glutinousricebatter");
    public static final RegistryObject<Item> RAWSOYBEANMILK = item("rawsoybeanmilk");
    public static final RegistryObject<Item> GELATIN = item("gelatin");
    public static final RegistryObject<Item> SWEETEGGLIQUID = item("sweeteggliquid");
    public static final RegistryObject<Item> MULTIGRAINBATTER = item("multigrainbatter");

// Remaining non-block item registrations migrated from FlavorImmersedDaily.
    public static final RegistryObject<Item> CHINESE_LEAVES = REGISTRY.register("chineseleaves",
            () -> new ChineseLeavesItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationMod(0.3f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> CUT_CHINESE_CABBAGE = REGISTRY.register("cutchinesecabbage",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationMod(0.1f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> RADISH = REGISTRY.register("radish",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationMod(0.3f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> BROCCOIL = REGISTRY.register("broccoil", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> BUCKWHEAT = REGISTRY.register("buckwheat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2f).alwaysEat().build())));

    public static final RegistryObject<Item> CABBAGE = REGISTRY.register("cabbage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> CASSAVA = REGISTRY.register("cassava", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> CELERY = REGISTRY.register("celery", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2f).alwaysEat().build())));

    public static final RegistryObject<Item> CHINESECHIVES = REGISTRY.register("chinesechives", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2f).alwaysEat().build())));

    public static final RegistryObject<Item> CHINESEYAM = REGISTRY.register("chineseyam", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> CORN = REGISTRY.register("corn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> CUMIN = REGISTRY.register("cumin", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2f).alwaysEat().build())));

    public static final RegistryObject<Item> FENNEL = REGISTRY.register("fennel", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2f).alwaysEat().build())));

    public static final RegistryObject<Item> GARLIC = REGISTRY.register("garlic", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2f).alwaysEat().build())));

    public static final RegistryObject<Item> GINGER = REGISTRY.register("ginger", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2f).alwaysEat().build())));

    public static final RegistryObject<Item> GREENPEPPER = REGISTRY.register("greenpepper", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> GUMBO = REGISTRY.register("gumbo", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> MILLETGRAIN_GRAIN = REGISTRY.register("millet_grain", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2f).alwaysEat().build())));

    public static final RegistryObject<Item> MUNGBEAN = REGISTRY.register("mungbean", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2f).alwaysEat().build())));

    public static final RegistryObject<Item> MUSTARD = REGISTRY.register("mustard", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2f).alwaysEat().build())));

    public static final RegistryObject<Item> OAT = REGISTRY.register("oat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2f).alwaysEat().build())));

    public static final RegistryObject<Item> OILSEEDRAPE = REGISTRY.register("oilseedrape", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2f).alwaysEat().build())));

    public static final RegistryObject<Item> REEDLEAF = REGISTRY.register("reedleaf", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.reedleaf_harvest"), ModItems::reedleafTooltipIcon));

    public static final RegistryObject<Item> RAWORLEANSCHICKENWING = REGISTRY.register("raworleanschickenwing", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWORLEANSCHICKENLEG = REGISTRY.register("raworleanschickenleg", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COOKED_BAOZI = REGISTRY.register("cooked_baozi", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FATBEEFROLL = REGISTRY.register("fatbeefroll", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LAMBROLL = REGISTRY.register("lambroll", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MUSHROOMSWRAPPEDINBATTER = REGISTRY.register("mushroomswrappedinbatter", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENWINGWITHBATTER = REGISTRY.register("rawchickenwingwithbatter", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENHALFLEGWITHBATTER = REGISTRY.register("rawchickenhalflegwithbatter", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENMEATWITHBATTER = REGISTRY.register("rawchickenmeatwithbatter", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSAUSAGE = REGISTRY.register("rawsausage", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWMEATBALLWITHEGGBALL = REGISTRY.register("rawmeatballwitheggball", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FROZENMILK = REGISTRY.register("frozenmilk", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWDICEDCHICKENWITHBATTER = REGISTRY.register("rawdicedchickenwithbatter", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WONTON = REGISTRY.register("wonton", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> NORMALMEATROLL = REGISTRY.register("normalmeatroll", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CARAMELCORNKENNELS = REGISTRY.register("caramelcornkennels", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COOKEDDUMPLING = REGISTRY.register("cookeddumpling", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SALTYEGG = REGISTRY.register("saltyegg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> SALTYRADDISH = REGISTRY.register("saltyraddish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> TANGYUAN = REGISTRY.register("tangyuan", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> THOUSAND_LAYER_TOFU_SKIN = REGISTRY.register("thousand_layer_tofu_skin", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> SPAGHETTI = REGISTRY.register("spaghetti", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> CREAMCORNKERNELS = REGISTRY.register("creamcornkernels", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CHOCOLATECORNKERNELS = REGISTRY.register("chocolatecornkernels", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSPRINGROLL = REGISTRY.register("rawspringroll", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWEGGTART = REGISTRY.register("raweggtart", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SWEETREDBEANEGGTART = REGISTRY.register("sweetredbeaneggtart", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSPICYGLUTEN = REGISTRY.register("rawspicygluten", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCOUPLING = REGISTRY.register("rawcoupling", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWAIKUI = REGISTRY.register("rawaikui", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MEATFLOURROOL = REGISTRY.register("meatflourrool", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> RAWFLOURPASTEWITHDRIEDMEATFLOSS = REGISTRY.register("rawflourpastewithdriedmeatfloss", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWAZONGZI = REGISTRY.register("rawazongzi", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_TANGYUAN = REGISTRY.register("raw_tangyuan", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWDORAYAKI = REGISTRY.register("rawdorayaki", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSESAMEGLUTINOUSPASTE = REGISTRY.register("rawsesameglutinouspaste", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CRISPYPORKBELLY = REGISTRY.register("crispyporkbelly", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_STUFFEDGREENPEPPER = REGISTRY.register("raw_stuffedgreenpepper", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSALTYEGG = REGISTRY.register("rawsaltyegg", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWWRAPPEDMILK = REGISTRY.register("rawwrappedmilk", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MODULATEDWHEATFLOUR = REGISTRY.register("modulatedwheatflour", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WALNUTPOWDER = REGISTRY.register("walnutpowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SWEETPOTATSTARCH = REGISTRY.register("sweetpotatstarch", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PEANUTPOWDER = REGISTRY.register("peanutpowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PEANUTSESAMEPOWDER = REGISTRY.register("peanutsesamepowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COFFEEPOWDER = REGISTRY.register("coffeepowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COCOAPOWDER = REGISTRY.register("cocoapowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WHEATFLOUR = REGISTRY.register("wheatflour", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TAPIOCAFLOUR = REGISTRY.register("tapiocaflour", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GULTINOUSRICEPOWDER = REGISTRY.register("gultinousricepowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PEAMEAL = REGISTRY.register("peameal", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SESAMEPOWDER = REGISTRY.register("sesamepowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GULTINOUSRICESASAMEPOWDER = REGISTRY.register("gultinousricesasamepowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CRYSTALSUGAR = REGISTRY.register("crystalsugar", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> VINEGAR = REGISTRY.register("vinegar", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.ACETIC_EROSION.get(), () -> Config.aceticErosionEnabled));

    public static final RegistryObject<Item> THICKBROADBEANSAUCE = REGISTRY.register("thickbroadbeansauce", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.BEAN_FURY.get(), () -> Config.beanFuryEnabled));

    public static final RegistryObject<Item> TOMATO_HOT_POT_BASE = REGISTRY.register("tomato_hot_pot_base", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BONESOUPESSENCE = REGISTRY.register("bonesoupessence", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BROWNSUGAR = REGISTRY.register("brownsugar", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BUTTER = REGISTRY.register("butter", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.BUTTER_PITCHER.get(), () -> Config.butterPitcherEnabled));

    public static final RegistryObject<Item> SOY = REGISTRY.register("soy", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.SOLAR_BREW.get(), () -> Config.solarBrewEnabled));

    public static final RegistryObject<Item> CURRY = REGISTRY.register("curry", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HALOGENBAG = REGISTRY.register("halogenbag", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SPICY_HOT_POT_BASE = REGISTRY.register("spicy_hot_pot_base", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MAJUICE = REGISTRY.register("majuice", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WHITESUGARSYRUP = REGISTRY.register("whitesugarsyrup", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COOKINGOIL = REGISTRY.register("cookingoil", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PEPPER_HOT_POT_BASE = REGISTRY.register("pepper_hot_pot_base", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SWEETFLOURASUVE = REGISTRY.register("sweetflourasuve", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SESAMEOIL = REGISTRY.register("sesameoil", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.SESAME_SLIP.get(), () -> Config.sesameSlipEnabled));

    public static final RegistryObject<Item> SALT = REGISTRY.register("salt", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.FLAVOR_BASE.get(), () -> Config.flavorBaseEnabled));

    public static final RegistryObject<Item> SALTPIECE = REGISTRY.register("saltpiece", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ORLEANSPOWDER = REGISTRY.register("orleanspowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ANISEEDPOWDER = REGISTRY.register("aniseedpowder", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.ANISEED_WARD.get(), () -> Config.aniseedWardEnabled));

    public static final RegistryObject<Item> ONIONPOWDER = REGISTRY.register("onionpowder", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.HULK_LEEK.get(), () -> Config.hulkLeekEnabled));

    public static final RegistryObject<Item> LILACPOWDER = REGISTRY.register("lilacpowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CINNAMONPOWDER = REGISTRY.register("cinnamonpowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> REDTEAPOWDER = REGISTRY.register("redteapowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CHINESEPICKLYASHPOWDER = REGISTRY.register("chinesepicklyashpowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FENNELPOWDER = REGISTRY.register("fennelpowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GROUNDPOWDER = REGISTRY.register("groundpowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PEPPEREDSALT = REGISTRY.register("pepperedsalt", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CHILLIPOWDER = REGISTRY.register("chillipowder", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.FURY_ASSAULT.get(), () -> Config.furyAssaultEnabled));

    public static final RegistryObject<Item> GREENTEAPOWDER = REGISTRY.register("greenteapowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GARLICPOWDER = REGISTRY.register("garlicpowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FIVESPICEPOWDER = REGISTRY.register("fivespicepowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ONIONPOWDER_2 = REGISTRY.register("onionpowder_2", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CUMINPOWDER = REGISTRY.register("cuminpowder", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DEADCATTLE = REGISTRY.register("deadcattle", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.dead_animal_harvest"), ModItems::deadAnimalIcons));

    public static final RegistryObject<Item> DEADSHEEP = REGISTRY.register("deadsheep", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.dead_animal_harvest"), ModItems::deadAnimalIcons));

    public static final RegistryObject<Item> DEADPIG = REGISTRY.register("deadpig", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.dead_animal_harvest"), ModItems::deadAnimalIcons));

    public static final RegistryObject<Item> DEADCHICKEN = REGISTRY.register("deadchicken", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.dead_animal_harvest"), ModItems::deadAnimalIcons));

    public static final RegistryObject<Item> CHICKENWITHOUTFEATHER = REGISTRY.register("chickenwithoutfeather",
            () -> new TooltipItem(new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.chicken_without_feather"), () -> java.util.List.of()));

    public static final RegistryObject<Item> CHICKENWITHOUTBLOOD = REGISTRY.register("chickenwithoutblood",
            () -> new TooltipItem(new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.chicken_without_blood"), ModItems::chickenBloodIcons));

    public static final RegistryObject<Item> SHARPKNIFE = REGISTRY.register("sharpknife", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WIDEEDGEDKNIFE = REGISTRY.register("wideedgedknife", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BONECUTTERKNIFE = REGISTRY.register("bonecutterknife", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> KITCHENKNIFE = REGISTRY.register("kitchenknife", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> KITCHENSCISSOR = REGISTRY.register("kitchenscissor", () -> new KitchenScissorsItem(new Item.Properties()));

    public static final RegistryObject<Item> SPATULA = REGISTRY.register("spatula", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MALLET = REGISTRY.register("mallet", () -> new CoarseClothItem(new Item.Properties()));

    public static final RegistryObject<Item> COARSECLOTH = REGISTRY.register("coarsecloth", () -> new CoarseClothItem(new Item.Properties()));

// 吴刚之斧：钻石斧性能，天生自带 3 级无限砍伐，可通过村民交易与地牢宝箱获取
    public static final RegistryObject<Item> WUGANG_AXE = REGISTRY.register("wugang_axe", () -> new WugangAxeItem(new Item.Properties()));

    public static final RegistryObject<Item> MOONCAKEMOLD = REGISTRY.register("mooncakemold", () -> new MooncakeMoldItem(new Item.Properties()));

    public static final RegistryObject<Item> ROLLINGPIN = REGISTRY.register("rollingpin", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EEGGPUFFSMOULD = REGISTRY.register("eeggpuffsmould", () -> new CoarseClothItem(new Item.Properties()));

    public static final RegistryObject<Item> DIECEDPRESERVEDEGG = REGISTRY.register("diecedpreservedegg", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CUTDIECEDPRESERVEEGG = REGISTRY.register("cutdiecedpreserveegg", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DICEDSWEETPOTATO = REGISTRY.register("dicedsweetpotato", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PURPLEDICEDSWEETPOTATO = REGISTRY.register("purpledicedsweetpotato", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PROCESSEDCABBAGE = REGISTRY.register("processedcabbage", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DICEDTOMATO = REGISTRY.register("dicedtomato", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CUTCABBAGE = REGISTRY.register("cutcabbage", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CASSAVACHUNKS = REGISTRY.register("cassavachunks", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SLICEDZUCCHINI = REGISTRY.register("slicedzucchini", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CARROTCHUNKS = REGISTRY.register("carrotchunks", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CELERYLEAF = REGISTRY.register("celeryleaf", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PEAPASTE = REGISTRY.register("peapaste", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SESAMEGLUTINOUSRICEBALLS = REGISTRY.register("sesameglutinousriceballs", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MEATANDEGGFILLING = REGISTRY.register("meatandeggfilling", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MEATANDVEGETABLEFILLING = REGISTRY.register("meatandvegetablefilling", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MUNGBEANPASTE = REGISTRY.register("mungbeanpaste", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WAXGOURDPASTE = REGISTRY.register("waxgourdpaste", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> UNCOMMONSTUFFING = REGISTRY.register("uncommonstuffing", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> VEGETABLEANDEGGSTUFFING = REGISTRY.register("vegetableandeggstuffing", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BROWNSUGARSYRUP = REGISTRY.register("brownsugarsyrup", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.CRIMSON_MAMBA.get(), () -> true));

    public static final RegistryObject<Item> SALTYWATER = REGISTRY.register("saltywater", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COCOAMASS = REGISTRY.register("cocoamass", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EGGCOATEDMEATBALLS = REGISTRY.register("eggcoatedmeatballs", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BATTEREDCHICKENBREASTCHUNKS = REGISTRY.register("batteredchickenbreastchunks", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWMEATFLOSSDOUGH = REGISTRY.register("rawmeatflossdough", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BATTEREDMUSHROOMS = REGISTRY.register("batteredmushrooms", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CUTPANCAKE = REGISTRY.register("cutpancake", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FERMENTEDFLOURPASTE = REGISTRY.register("fermentedflourpaste", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CUTEGGPANCAKE = REGISTRY.register("cuteggpancake", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RICESLURRYWRAPPEDINFILTERCLOTH = REGISTRY.register("riceslurrywrappedinfiltercloth", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> POUNDEDGLUTINOUSPASTE = REGISTRY.register("poundedglutinouspaste", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LONGDOUGH = REGISTRY.register("longdough", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LONGEGGPANCAKE = REGISTRY.register("longeggpancake", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SOYBEANPROTEIN = REGISTRY.register("soybeanprotein", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_PIGOFFAL = REGISTRY.register("raw_pigoffal", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DEBONEDCHICKENFEET = REGISTRY.register("debonedchickenfeet", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EGGWRAPPEDINGRAVEL = REGISTRY.register("eggwrappedingravel", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PEAFLOUR = REGISTRY.register("peaflour", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FIVEPOINTEDCARAMBOLADELIGHT = REGISTRY.register("fivepointedcaramboladelight", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> RIPEPEARWITHROCKSUGAR = REGISTRY.register("ripepearwithrocksugar", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> SUGARCOATEDHAWS = REGISTRY.register("sugarcoatedhaws", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> STEWEDPIGLEG = REGISTRY.register("stewedpigleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> STEWEDHALFCHICKENLEG = REGISTRY.register("stewedhalfchickenleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> STEWEDCHICKENHEART = REGISTRY.register("stewedchickenheart", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> STEWEDCHICKENLIVERS = REGISTRY.register("stewedchickenlivers", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> STEWEDCHICKENLEG = REGISTRY.register("stewedchickenleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> SAUCINGBEEF = REGISTRY.register("saucingbeef", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> YOGURT = REGISTRY.register("yogurt", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> SALTEGGYOLK = REGISTRY.register("salteggyolk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> SALTTOFUCURD = REGISTRY.register("salttofucurd", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> DICEDHAMIMELON = REGISTRY.register("dicedhamimelon", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> FRIED_TOFU_SKIN_ROLLS = REGISTRY.register("fried_tofu_skin_rolls", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> CREAMPOPCORN = REGISTRY.register("creampopcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> CHINESEYAMANDSUGAR = REGISTRY.register("chineseyamandsugar", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> CHOCOLATEPOPCORN = REGISTRY.register("chocolatepopcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> CHOCOLATEBEAN = REGISTRY.register("chocolatebean", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> NEWYEARCAKE = REGISTRY.register("newyearcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> BONELESSLEMONCHICKENFEET = REGISTRY.register("bonelesslemonchickenfeet", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> BOILEDCORN = REGISTRY.register("boiledcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> CHICKENFEETWITHPEPPERS = REGISTRY.register("chickenfeetwithpeppers", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> COLONELCHICKENNUGGETS = REGISTRY.register("colonelchickennuggets", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> FRIEDGULTINOUSRICESTRIPS = REGISTRY.register("friedgultinousricestrips", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> FRIEDPEANUTS = REGISTRY.register("friedpeanuts", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> FRIEDMUSHROOM = REGISTRY.register("friedmushroom", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> DRIEDRICECAKE = REGISTRY.register("driedricecake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> FIREDSPRINGROLL = REGISTRY.register("firedspringroll", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> FRIEDDOUGHSTICK = REGISTRY.register("frieddoughstick", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> DRIEDMILK = REGISTRY.register("driedmilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> DRIEDMEATBALL = REGISTRY.register("driedmeatball", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> DRIEDDICEDAUBERGINE = REGISTRY.register("drieddicedaubergine", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> FRIEDGIZZARD = REGISTRY.register("friedgizzard", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> FRENCHFRIES = REGISTRY.register("frenchfries", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> DRIEDLOTUSROOT = REGISTRY.register("driedlotusroot", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> FRIEDDRIEDTOUFU = REGISTRY.register("frieddriedtoufu", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> FRIEDTOUFU = REGISTRY.register("friedtoufu", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> DRIEDBREAD = REGISTRY.register("driedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> DRIEDDICEDBREAD = REGISTRY.register("drieddicedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> DRIEDDICEDSTEAMEDBREAD = REGISTRY.register("drieddicedsteamedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> FRIEDSANZI = REGISTRY.register("friedsanzi", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> FRIEDHEMPBALL = REGISTRY.register("friedhempball", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> FRIEDDOUGHTWIST = REGISTRY.register("frieddoughtwist", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> CRISPYPISTOLLEG = REGISTRY.register("crispypistolleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> SOMKEDCHICKENBREAST = REGISTRY.register("somkedchickenbreast", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> ROASTEDPURPLEPOTATO = REGISTRY.register("roastedpurplepotato", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> ROASTEDPEANUT = REGISTRY.register("roastedpeanut", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> ORLEANWING = REGISTRY.register("orleanwing", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.7f).alwaysEat().build())));

    public static final RegistryObject<Item> ORLEANLEG = REGISTRY.register("orleanleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.7f).alwaysEat().build())));

    public static final RegistryObject<Item> ROASTEDMUSHROOM = REGISTRY.register("roastedmushroom", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.7f).alwaysEat().build())));

    public static final RegistryObject<Item> ROASTED_FLAMMULINAVELUTIPES = REGISTRY.register("roasted_flammulinavelutipes", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.7f).alwaysEat().build())));

    public static final RegistryObject<Item> ROASTEDCHINESECHIVES = REGISTRY.register("roastedchinesechives", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.7f).alwaysEat().build())));

    public static final RegistryObject<Item> ROASTEDDICEDFISH = REGISTRY.register("roasteddicedfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.7f).alwaysEat().build())));

    public static final RegistryObject<Item> CARAMELPOPCORN = REGISTRY.register("caramelpopcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> COOKEDCHINESEYAM = REGISTRY.register("cookedchineseyam", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> COOKEDVERMICELLI_0 = REGISTRY.register("cookedvermicelli_0", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> COOKEDHAWTHORN = REGISTRY.register("cookedhawthorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> POPCORN = REGISTRY.register("popcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> SWEETTOUFUCURD = REGISTRY.register("sweettoufucurd", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> JELLY = REGISTRY.register("jelly", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> COOKEDPRESERVEDEGG = REGISTRY.register("cookedpreservedegg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> COOKEDZONGZI = REGISTRY.register("cookedzongzi", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> COOKEDGLUTINOUSRICE = REGISTRY.register("cookedglutinousrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> COOKEDGLUTINOUSRICEPOUNDEDINTOPASTE = REGISTRY.register("cookedglutinousricepoundedintopaste", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> AIKUI = REGISTRY.register("aikui", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> TANG_ZI_XIAO_ZAO = REGISTRY.register("tang_zi_xiao_zao", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> SUGARCOATEDWALNUT = REGISTRY.register("sugarcoatedwalnut", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> DICEDPICKLEDVEGETABLE = REGISTRY.register("dicedpickledvegetable", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> CUREDMEAT = REGISTRY.register("curedmeat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> CUREDSAUSAGE = REGISTRY.register("curedsausage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> SALTYDICEDCURUMBER = REGISTRY.register("saltydicedcurumber", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> PICKLEDVEGETABLE = REGISTRY.register("pickledvegetable", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> SLICEDSALTYCUCUMBER = REGISTRY.register("slicedsaltycucumber", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> STEAMEDBLOOD = REGISTRY.register("steamedblood", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> TIGERGREENPEPPER = REGISTRY.register("tigergreenpepper", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> SOYAMILK = REGISTRY.register("soyamilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> PICEDTOUFU = REGISTRY.register("picedtoufu", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> DRIEDTOUFU = REGISTRY.register("driedtoufu", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> TOFU_PUFFS = REGISTRY.register("tofu_puffs", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> TOFUCURD = REGISTRY.register("tofucurd", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> CHINESESPICYSNACKFOOD = REGISTRY.register("chinesespicysnackfood", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> SPICYPEANUT = REGISTRY.register("spicypeanut", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> HOTANDSOURRICENOODLES = REGISTRY.register("hotandsourricenoodles", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> GOLDRICECAKE = REGISTRY.register("goldricecake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> GOLDENGRAPE = REGISTRY.register("goldengrape", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> DICEDBREAD = REGISTRY.register("dicedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> DRYCOOKEDNOODLES = REGISTRY.register("drycookednoodles", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> COOKEDVERMICELLI = REGISTRY.register("cookedvermicelli", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> HARD_MOONCAKE = REGISTRY.register("hard_mooncake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));
// 广式月饼—团团圆圆
    public static final RegistryObject<Item> CANTONESE_MOONCAKE = mooncakeFood("cantonese_mooncake", 9, 0.0f,
            () -> ModEffects.holder(ModEffects.REUNION_RECOVERY), () -> Config.reunionRecoveryEnabled);
// 苏式月饼—酥到掉渣
    public static final RegistryObject<Item> SUZHOU_MOONCAKE = mooncakeFood("suzhou_mooncake", 9, 0.0f,
            () -> ModEffects.holder(ModEffects.CRUMBLY_CRUST), () -> Config.crumblyCrustEnabled);
// 京式月饼—蔽日食月
    public static final RegistryObject<Item> BEIJING_MOONCAKE = mooncakeFood("beijing_mooncake", 9, 0.0f,
            () -> ModEffects.holder(ModEffects.ECLIPSE_DEVOUR), () -> Config.eclipseDevourEnabled);
// 冰皮月饼—清凉冰皮
    public static final RegistryObject<Item> SNOW_SKIN_MOONCAKE = mooncakeFood("snow_skin_mooncake", 9, 0.0f,
            () -> ModEffects.holder(ModEffects.COOL_SKIN), () -> Config.coolSkinEnabled);

    public static final RegistryObject<Item> GRAINSPANCAKE = REGISTRY.register("grainspancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> HAMBURGERBREAD = REGISTRY.register("hamburgerbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> YIMENGPANCAKES = REGISTRY.register("yimengpancakes", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> SACHIMA = REGISTRY.register("sachima", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> SHUMAI = REGISTRY.register("shumai", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> HOTDOG = REGISTRY.register("hotdog", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> STEAMEDGLUTINOUSRICECAKE = REGISTRY.register("steamedglutinousricecake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> CRISPYEGGCAKE = REGISTRY.register("crispyeggcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> STEAMEDTWISTEDROLL = REGISTRY.register("steamedtwistedroll", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> SCALLIONOILPANCAKE = REGISTRY.register("scallionoilpancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> NOODLES = REGISTRY.register("noodles", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> PIE = REGISTRY.register("pie", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> STEAMEDBREAD = REGISTRY.register("steamedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> DICEDSTEAMEDBREAD = REGISTRY.register("dicedsteamedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> EGGPUFFS = REGISTRY.register("eggpuffs", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> EGGVERMICELLI = REGISTRY.register("eggvermicelli", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> COOKEDSAUSAGE = REGISTRY.register("cookedsausage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> M_SECRETDELIGHT = REGISTRY.register("m_secretdelight", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> FISHSASHIMI = REGISTRY.register("fishsashimi", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> RAWCATTLEBLOOD = REGISTRY.register("rawcattleblood",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCATTLEFACE = REGISTRY.register("rawcattleface",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCATTLEJOINT = REGISTRY.register("rawcattlejoint",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCATTLESTOMACH = REGISTRY.register("rawcattlestomach",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCATTLELUNG = REGISTRY.register("rawcattlelung",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCATTLELIVER = REGISTRY.register("rawcattleliver",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCATTLEINTESTINE = REGISTRY.register("rawcattleintestine",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCATTLEHEART = REGISTRY.register("rawcattleheart",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCATTLEFAT = REGISTRY.register("rawcattlefat",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWDICEDCATTLEMEAT = REGISTRY.register("rawdicedcattlemeat",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSNOWFLAKEBEEF = REGISTRY.register("rawsnowflakebeef",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCATTLELEG = REGISTRY.register("rawcattleleg",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCATTLEFEET = REGISTRY.register("rawcattlefeet",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCATTLETENDON = REGISTRY.register("rawcattletendon",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BULLHORN = REGISTRY.register("bullhorn",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BOVINEBONE = REGISTRY.register("bovinebone",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ANIMALSKULL = REGISTRY.register("animalskull",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CHICKENFEATHERS = REGISTRY.register("chickenfeathers",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGBLOOD = REGISTRY.register("rawpigblood",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWDICEDPIGMEAT = REGISTRY.register("rawdicedpigmeat",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWDICEDPIGMEAT_2 = REGISTRY.register("rawdicedpigmeat_2",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGHEAD = REGISTRY.register("rawpighead",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGEAR = REGISTRY.register("rawpigear",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGNOSE = REGISTRY.register("rawpignose",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGTAIL = REGISTRY.register("rawpigtail",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGSKIN = REGISTRY.register("rawpigskin",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGSTREAKYPORK = REGISTRY.register("rawpigstreakypork",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGTENDERLOIN = REGISTRY.register("rawpigtenderloin",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGSPARERIB = REGISTRY.register("rawpigsparerib",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGLEG = REGISTRY.register("rawpigleg",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGFEET = REGISTRY.register("rawpigfeet",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGFAT = REGISTRY.register("rawpigfat",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGSTOMACH = REGISTRY.register("rawpigstomach",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGLUNG = REGISTRY.register("rawpiglung",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGLIVER = REGISTRY.register("rawpigliver",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGKIDNEY = REGISTRY.register("rawpigkidney",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGHEART = REGISTRY.register("rawpigheart",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGINTESTINE = REGISTRY.register("rawpigintestine",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWPIGCEREBRUM = REGISTRY.register("rawpigcerebrum",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSHEEPBLOOD = REGISTRY.register("rawsheepblood",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWDICEDSHEEPMEAT = REGISTRY.register("rawdicedsheepmeat",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSHEEPFACE = REGISTRY.register("rawsheepface",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSHEEPTAILFAT = REGISTRY.register("rawsheeptailfat",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSHEEPFAT = REGISTRY.register("rawsheepfat",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSHEEPEYE = REGISTRY.register("rawsheepeye",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSHEEPSTOMACH = REGISTRY.register("rawsheepstomach",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSHEEPLIVER = REGISTRY.register("rawsheepliver",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSHEEPINTESTINE = REGISTRY.register("rawsheepintestine",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSHEEPKIDNEY = REGISTRY.register("rawsheepkidney",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSHEEPHEART = REGISTRY.register("rawsheepheart",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSHEEPSPARERIB = REGISTRY.register("rawsheepsparerib",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSHEEPSPINE = REGISTRY.register("rawsheepspine",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSHEEPFEET = REGISTRY.register("rawsheepfeet",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSHEEPLEG = REGISTRY.register("rawsheepleg",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SHEEPBREAD = REGISTRY.register("sheepbread",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENBLOOD = REGISTRY.register("rawchickenblood",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENHEAD = REGISTRY.register("rawchickenhead",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENNECK = REGISTRY.register("rawchickenneck",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENPIECEPIECE = REGISTRY.register("rawchickenpiecepiece",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENBREAST = REGISTRY.register("rawchickenbreast",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENPIECE = REGISTRY.register("rawchickenpiece",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENWING = REGISTRY.register("rawchickenwing",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENWINGTIP = REGISTRY.register("rawchickenwingtip",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENFEET = REGISTRY.register("rawchickenfeet",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENLEGWITHLEG = REGISTRY.register("rawchickenlegwithleg",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENLEG = REGISTRY.register("rawchickenleg",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENLEAN = REGISTRY.register("rawchickenlean",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENFORK = REGISTRY.register("rawchickenfork",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENASS = REGISTRY.register("rawchickenass",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENHEART = REGISTRY.register("rawchickenheart",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENLIVER = REGISTRY.register("rawchickenliver",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWCHICKENGIZZARD = REGISTRY.register("rawchickengizzard",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COOKEDCATTLEHEART = REGISTRY.register("cookedcattleheart",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCATTLEJOINT = REGISTRY.register("cookedcattlejoint",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCATTLESTOMACH = REGISTRY.register("cookedcattlestomach",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCATTLELUNG = REGISTRY.register("cookedcattlelung",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCATTLELIVER = REGISTRY.register("cookedcattleliver",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCATTLEINTESTINE = REGISTRY.register("cookedcattleintestine",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDDICEDCATTLE = REGISTRY.register("cookeddicedcattle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCATTLEFACE = REGISTRY.register("cookedcattleface",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCATTLEMARBLEDBEEF = REGISTRY.register("cookedcattlemarbledbeef",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCATTLELEG = REGISTRY.register("cookedcattleleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCATTLEFEET = REGISTRY.register("cookedcattlefeet",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCATTLETENDON = REGISTRY.register("cookedcattletendon",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDDICEDPIGMEAT = REGISTRY.register("cookeddicedpigmeat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGSHREDS = REGISTRY.register("cookedpigshreds",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGEAR = REGISTRY.register("cookedpigear",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGNOSE = REGISTRY.register("cookedpignose",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGTAIL = REGISTRY.register("cookedpigtail",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGSKIN = REGISTRY.register("cookedpigskin",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGSTREAKYPORK = REGISTRY.register("cookedpigstreakypork",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGTENDERLOIN = REGISTRY.register("cookedpigtenderloin",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGSPARERIB = REGISTRY.register("cookedpigsparerib",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGLEG = REGISTRY.register("cookedpigleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGFEET = REGISTRY.register("cookedpigfeet",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGFAT = REGISTRY.register("cookedpigfat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGSTOMACH = REGISTRY.register("cookedpigstomach",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGLUNG = REGISTRY.register("cookedpiglung",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGLIVER = REGISTRY.register("cookedpigliver",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGKIDNEY = REGISTRY.register("cookedpigkidney",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGHEART = REGISTRY.register("cookedpigheart",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGCEREBRUM = REGISTRY.register("cookedpigcerebrum",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGINTESTINE = REGISTRY.register("cookedpigintestine",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDPIGFACE = REGISTRY.register("cookedpigface",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDDICEDSHEEPMEAT = REGISTRY.register("cookeddicedsheepmeat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDSHEEPFACE = REGISTRY.register("cookedsheepface",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDSHEEPEYE = REGISTRY.register("cookedsheepeye",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDSHEEPTAIL = REGISTRY.register("cookedsheeptail",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDSHEEPSTOMACH = REGISTRY.register("cookedsheepstomach",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDSHEEPLIVER = REGISTRY.register("cookedsheepliver",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDSHEEPINTESTINE = REGISTRY.register("cookedsheepintestine",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDSHEEPKIDNEY = REGISTRY.register("cookedsheepkidney",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDSHEEPHEART = REGISTRY.register("cookedsheepheart",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDSHEEPSPARERLIP = REGISTRY.register("cookedsheepsparerlip",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDSHEEPSPINE = REGISTRY.register("cookedsheepspine",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDSHEEPFEET = REGISTRY.register("cookedsheepfeet",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDSHEEPLEG = REGISTRY.register("cookedsheepleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDSHEEPFAT = REGISTRY.register("cookedsheepfat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCHICKENDICED = REGISTRY.register("cookedchickendiced",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCHICKENPIECEPIECE = REGISTRY.register("cookedchickenpiecepiece",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCHICKENHEAD = REGISTRY.register("cookedchickenhead",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCHICKENNECK = REGISTRY.register("cookedchickenneck",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCHICKENWING = REGISTRY.register("cookedchickenwing",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCHICKENWINGTIP = REGISTRY.register("cookedchickenwingtip",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCHICKENFEET = REGISTRY.register("cookedchickenfeet",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCHICKENLEGWITHLEG = REGISTRY.register("cookedchickenlegwithleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCHICKENLEG = REGISTRY.register("cookedchickenleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCHICKENPIECE = REGISTRY.register("cookedchickenpiece",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCHICKENSTEAK = REGISTRY.register("cookedchickensteak",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCHICKENFORK = REGISTRY.register("cookedchickenfork",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCHICKENASS = REGISTRY.register("cookedchickenass",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCHICKENHEART = REGISTRY.register("cookedchickenheart",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCHICKENLIVER = REGISTRY.register("cookedchickenliver",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> COOKEDCHICKENGIZZARD = REGISTRY.register("cookedchickengizzard",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> PINEAPPLEJAM = REGISTRY.register("pineapplejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> STRAWBERRYJAM = REGISTRY.register("strawberryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> ORANGEJAM = REGISTRY.register("orangejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> WINTERJUJUBEJAM = REGISTRY.register("winterjujubejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> HAMIMELONJAM = REGISTRY.register("hamimelonjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> DRAGONFRUITJAM = REGISTRY.register("dragonfruitjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> TANGERINEJAM = REGISTRY.register("tangerinejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> BLUEBERRYJAM = REGISTRY.register("blueberryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> PEARJAM = REGISTRY.register("pearjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> LYCHEEJAM = REGISTRY.register("lycheejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> PLUMJAM = REGISTRY.register("plumjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> DURIANJAM = REGISTRY.register("durianjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> MANGOJAM = REGISTRY.register("mangojam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> KIWIFRUITJAM = REGISTRY.register("kiwifruitjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> PAWPAWJAM = REGISTRY.register("pawpawjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> LEMONJAM = REGISTRY.register("lemonjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> LOQUATJAM = REGISTRY.register("loquatjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> APPLEJAM = REGISTRY.register("applejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> GRAPEJAM = REGISTRY.register("grapejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> GREEMPLUMJAM = REGISTRY.register("greemplumjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> GREENGRAPEJAM = REGISTRY.register("greengrapejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> MULBERRYJAM = REGISTRY.register("mulberryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> HAWTHORNJAM = REGISTRY.register("hawthornjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> MANGOSTEENJAM = REGISTRY.register("mangosteenjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> POMEGRANATEJAM = REGISTRY.register("pomegranatejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> HONEYPEACHJAM = REGISTRY.register("honeypeachjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> SWEETMELONJAM = REGISTRY.register("sweetmelonjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> SWEETBERRYJAM = REGISTRY.register("sweetberryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> WATERMELONJAM = REGISTRY.register("watermelonjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> BANANAJAM = REGISTRY.register("bananajam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> APRICOTJAM = REGISTRY.register("apricotjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> CARAMBOLAJAM = REGISTRY.register("carambolajam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> COCONUTJAM = REGISTRY.register("coconutjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> CHERRYJAM = REGISTRY.register("cherryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> NECTARINEJAM = REGISTRY.register("nectarinejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> ORANGELEAVE = REGISTRY.register("orangeleave", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> MINCER_COVER = REGISTRY.register("mincer_cover",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WINDOW_PAPER_ITEM = REGISTRY.register("windowpaper_1",
            () -> new WindowPaperItem(new Item.Properties()));

    public static final RegistryObject<Item> GREENMANGO = rareFruit("greenmango", 6, 0.5f);

    public static final RegistryObject<Item> AGRAPE = REGISTRY.register("agrape", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> AGREENGRAPE = REGISTRY.register("agreengrape", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CAULIFLOWER = REGISTRY.register("cauliflower", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GARLICPEDICEL = REGISTRY.register("garlicpedicel", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PEANUT = REGISTRY.register("peanut", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> POMEGRANATE_SEED = REGISTRY.register("pomegranate_seed", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWHALFOFCHICKENLEG = REGISTRY.register("rawhalfofchickenleg", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWSHEEPTAIL = REGISTRY.register("rawsheeptail", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> REDREPPER = REGISTRY.register("redrepper", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SACLLION = REGISTRY.register("sacllion", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SPINACH = REGISTRY.register("spinach", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SWEETPOTATO = REGISTRY.register("sweetpotato", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> YELLOWPEACH = rareFruit("yellowpeach", 6, 0.5f);

    public static final RegistryObject<Item> DRYANISEED = REGISTRY.register("dryaniseed", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DRYCINNAMON = REGISTRY.register("drycinnamon", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DRYCOFFEEBEAN = REGISTRY.register("drycoffeebean", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DRYLILAC = REGISTRY.register("drylilac", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DRYNUTMEG = REGISTRY.register("drynutmeg", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DRYSCALION = REGISTRY.register("dryscalion", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DRYSICHUANPEPPER = REGISTRY.register("drysichuanpepper", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DICEDBROCCOIL = REGISTRY.register("dicedbroccoil", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DICEDCAULIFLOWER = REGISTRY.register("dicedcauliflower", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EGGSHELL = REGISTRY.register("eggshell", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> OATGRAIN = REGISTRY.register("oatgrain", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> POLISHEDGLUTINOUSRICE_2 = REGISTRY.register("polishedglutinousrice_2", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HOT_GLUTINOUS = REGISTRY.register("hot_glutinous", () -> new TooltipItem(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.3f).alwaysEat().build()),
            Component.translatable("tooltip.flavor_immersed_daily.hot_glutinous"),
            () -> List.of()));

    public static final RegistryObject<Item> RAW_BAOZI = REGISTRY.register("raw_baozi", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_DUMPLING = REGISTRY.register("raw_dumpling", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_WONTON = REGISTRY.register("raw_wonton", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BAISED_TOFU_SLICES = REGISTRY.register("baised_tofu_slices", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> BOILEDRICEFLOURNOODLES = REGISTRY.register("boiledriceflournoodles", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> BONESOUP = REGISTRY.register("bonesoup", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> BUCKWHEATRICE = REGISTRY.register("buckwheatrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> CHOCOLATE = REGISTRY.register("chocolate", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> COOKEDCHICKENBREAST = REGISTRY.register("cookedchickenbreast", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> COOKEDCHICKENLEAN = REGISTRY.register("cookedchickenlean", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> COOKEDCORNBATTER = REGISTRY.register("cookedcornbatter", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> COOKEDCRISPYPORK = REGISTRY.register("cookedcrispypork", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.7f).alwaysEat().build())));

    public static final RegistryObject<Item> COOKEDFLAKEBEEF = REGISTRY.register("cookedflakebeef", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.7f).alwaysEat().build())));

    public static final RegistryObject<Item> COOKEDHALFOFCHCIKENLEG = REGISTRY.register("cookedhalfofchcikenleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> COOKEDPIGSTREAKMEAT = REGISTRY.register("cookedpigstreakmeat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.7f).alwaysEat().build())));

    public static final RegistryObject<Item> DRIED_TOFU_SKIN = REGISTRY.register("dried_tofu_skin", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> BEANSPROUT = REGISTRY.register("beansprout", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EATENPINEAPPLE = REGISTRY.register("eatenpineapple", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.2f).alwaysEat().build())));

    public static final RegistryObject<Item> EGGBISCUIT = REGISTRY.register("eggbiscuit", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> EGGPANCAKE = REGISTRY.register("eggpancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> EGGTART = REGISTRY.register("eggtart", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> EIGHTTREASURECONGEE = REGISTRY.register("eighttreasurecongee", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> CONGEE_WITH_MINCE_PORK_AND_PRESERVED_EGG = REGISTRY.register("congee_with_minced_pork_and_preserved_egg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> ELECTROLYTEBEVERAGE = REGISTRY.register("electrolytebeverage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.2f).alwaysEat().build())));

    public static final RegistryObject<Item> FANHUAROLL = REGISTRY.register("fanhuaroll", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> FRIEDCHICKENCHOP = REGISTRY.register("friedchickenchop", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> FRIEDCHICKENCORN = REGISTRY.register("friedchickencorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> FRIEDCHICKENLEG = REGISTRY.register("friedchickenleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> FRIEDCHICKENWING = REGISTRY.register("friedchickenwing", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> FRIEDSAUSAGE = REGISTRY.register("friedsausage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> GRAPEWINE = REGISTRY.register("grapewine", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.2f).alwaysEat().build())));

    public static final RegistryObject<Item> GREENBEANPORRIDGE = REGISTRY.register("greenbeanporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> HANDGRABBEDPANCAKE = REGISTRY.register("handgrabbedpancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> HEALTHLOQUATCREAM = REGISTRY.register("healthloquatcream", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.2f).alwaysEat().build())));

    public static final RegistryObject<Item> HEALTHPEANUTMILK = REGISTRY.register("healthpeanutmilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> HEALTHWALNUTDEW = REGISTRY.register("healthwalnutdew", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> ICEDBLACKTEA = REGISTRY.register("icedblacktea", () -> new com.flavor_immersed_daily.item.DrinkItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> KAOLIANGPORRIDGE = REGISTRY.register("kaoliangporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> KAOLIANGRICE = REGISTRY.register("kaoliangrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> LABAPORRIDGE = REGISTRY.register("labaporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> MILLIETPORRIDGE = REGISTRY.register("millietporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> MILLIETRICE = REGISTRY.register("millietrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> NOILEDRICENOODLE = REGISTRY.register("noiledricenoodle", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> OAKPORRIDGE = REGISTRY.register("oakporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> OAKRICE = REGISTRY.register("oakrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> OMELETTE = REGISTRY.register("omelette", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> PANADAPANCAKE = REGISTRY.register("panadapancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> PEAFLOURCAKE = REGISTRY.register("peaflourcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> POTATOCHIPS = REGISTRY.register("potatochips", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> REDBEANEGGTART = REGISTRY.register("redbeaneggtart", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> REDBEANSTUFFINGDORAYAKI = REGISTRY.register("redbeanstuffingdorayaki", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> RICE = REGISTRY.register("rice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> RICE_PORRIDGE = REGISTRY.register("rice_porridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> RICEPASTESOUP = REGISTRY.register("ricepastesoup", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> ROASTCHICKENFORK = REGISTRY.register("roastchickenfork", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6f).alwaysEat().build())));

    public static final RegistryObject<Item> ROASTEDSWEETPOTATO = REGISTRY.register("roastedsweetpotato", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> SHELLEDBOILEDEGG = REGISTRY.register("shelledboiledegg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> SHOU_KAI_XIN_GUO = REGISTRY.register("shou_kai_xin_guo", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> SMALLMICEDMEATCAKE = REGISTRY.register("smallmicedmeatcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> STEAMEDSALTORANGE = REGISTRY.register("steamedsaltorange", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> STEAMEDVERMICELLIROLL_0 = REGISTRY.register("steamedvermicelliroll_0", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> STRAWBERRYCAKEROLL = REGISTRY.register("strawberrycakeroll", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> SWEETMILK = REGISTRY.register("sweetmilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> TOFU_SAUSAGE = REGISTRY.register("tofu_sausage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> TOFU_STICKS = REGISTRY.register("tofu_sticks", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> WALNUTCAKE = REGISTRY.register("walnutcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> WALNUTSHORTBREAD = REGISTRY.register("walnutshortbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).alwaysEat().build())));

    public static final RegistryObject<Item> WHEATMILK = REGISTRY.register("wheatmilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3f).alwaysEat().build())));

    public static final RegistryObject<Item> YUBA = REGISTRY.register("yuba", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).alwaysEat().build())));

    public static final RegistryObject<Item> TOUFU = REGISTRY.register("toufu", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(6).saturationMod(0.8f).alwaysEat().build())));

    public static final RegistryObject<Item> EGGCAKE = REGISTRY.register("eggcake", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(7).saturationMod(0.7f).alwaysEat().build())));

    public static final RegistryObject<Item> GOLDRICECAKEMAX = REGISTRY.register("goldricecakemax", () -> new Item(new Item.Properties()
            .rarity(Rarity.EPIC)
            .food(new FoodProperties.Builder()
                    .nutrition(4).saturationMod(9.6f)
                    .effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 1), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3), 1.0f)
                    .alwaysEat().build())));

    public static final RegistryObject<Item> GOLDENGRAPEMAX = REGISTRY.register("goldengrapemax", () -> new Item(new Item.Properties()
            .rarity(Rarity.EPIC)
            .food(new FoodProperties.Builder()
                    .nutrition(4).saturationMod(9.6f)
                    .effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 1), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3), 1.0f)
                    .alwaysEat().build())));

    public static final RegistryObject<PurifiedWaterBucketItem> FILTERVAT = REGISTRY.register("filtervat",
            () -> new PurifiedWaterBucketItem(new Item.Properties()));

    public static final RegistryObject<FairySparklerItem> FAIRY_SPARKLER = REGISTRY.register("fairy_sparkler",
            () -> new FairySparklerItem(new Item.Properties().stacksTo(1).durability(500)));

    private static List<ItemStack> deadAnimalIcons() {
        return List.of(new ItemStack(BONECUTTERKNIFE.get()), new ItemStack(ModBlocks.BIGHOOK.get().asItem()));
    }

    private static List<ItemStack> chickenBloodIcons() {
        return List.of(new ItemStack(ModBlocks.WOODBASIN.get().asItem()), new ItemStack(TIDYWATER.get()));
    }

    private static List<ItemStack> reedleafTooltipIcon() {
        return List.of(new ItemStack(KITCHENSCISSOR.get()));
    }
}
