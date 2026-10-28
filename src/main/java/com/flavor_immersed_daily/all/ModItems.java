package com.flavor_immersed_daily.all;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.item.DrinkItem;
import com.flavor_immersed_daily.item.FillingFoodItem;
import com.flavor_immersed_daily.item.MooncakeFoodItem;
import com.flavor_immersed_daily.item.RareFruitVariantItem;
import com.flavor_immersed_daily.item.SeedableFruitItem;
import com.flavor_immersed_daily.item.ThrowableFruitItem;
import com.flavor_immersed_daily.item.WildHarvestItem;
import com.flavor_immersed_daily.item.WindowPaperItem;
import com.flavor_immersed_daily.item.FairySparklerItem;
import com.flavor_immersed_daily.item.PurifiedWaterBucketItem;
import com.flavor_immersed_daily.item.CoarseClothItem;
import com.flavor_immersed_daily.item.MooncakeMoldItem;
import com.flavor_immersed_daily.item.KitchenScissorsItem;
import com.flavor_immersed_daily.item.ChineseLeavesItem;
import com.flavor_immersed_daily.item.WrestlingGunItem;
import com.flavor_immersed_daily.item.SeasoningItem;
import com.flavor_immersed_daily.item.WugangAxeItem;
import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.client.tooltip.TooltipItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

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

// 有馅食物：馅料记录在 NBT "filling" 中，tooltip 显示"馅料口味"；不预设默认馅料
    private static DeferredItem<Item> filledItem(String name) {
        return REGISTRY.register(name, () -> new FillingFoodItem(new Item.Properties()));
    }

// 有馅食物：馅料记录在 NBT "filling" 中，tooltip 显示"馅料口味"；不预设默认馅料
    private static DeferredItem<Item> filledFood(String name, int nutrition, float saturationModifier) {
        return REGISTRY.register(name, () -> new FillingFoodItem(new Item.Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition)
                        .saturationModifier(saturationModifier)
                        .alwaysEdible()
                        .build())));
    }

// 特色月饼：在馅料口味之外，食用后获得对应 buff 效果（tooltip 显示 buff，且按 Shift 展开详情）
    private static DeferredItem<Item> mooncakeFood(String name, int nutrition, float saturationModifier,
                                                   Supplier<? extends Holder<MobEffect>> effect,
                                                   BooleanSupplier enabled) {
        return REGISTRY.register(name, () -> new MooncakeFoodItem(new Item.Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition)
                        .saturationModifier(saturationModifier)
                        .alwaysEdible()
                        .build()), effect, enabled));
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

// 苹果棒冰
    public static final DeferredItem<Item> APPLEPOPSICLE = REGISTRY.register("applepopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 杏子棒冰
    public static final DeferredItem<Item> APRICOTPOPSICLE = REGISTRY.register("apricotpopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 香蕉棒冰
    public static final DeferredItem<Item> BANANAPOPSICLE = REGISTRY.register("bananapopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 蓝莓棒冰
    public static final DeferredItem<Item> BLUEBERRYPOPSICLE = REGISTRY.register("blueberrypopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 杨桃棒冰
    public static final DeferredItem<Item> CARAMBOLAPOPSICLE = REGISTRY.register("carambolapopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 樱桃棒冰
    public static final DeferredItem<Item> CHERRYPOPSICLE = REGISTRY.register("cherrypopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 巧克力棒冰
    public static final DeferredItem<Item> CHOCOLATEPOPSICLE = REGISTRY.register("chocolatepopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 椰子棒冰
    public static final DeferredItem<Item> COCONUTPOPSICLE = REGISTRY.register("coconutpopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 咖啡棒冰
    public static final DeferredItem<Item> COFFEEPOPSICLE = REGISTRY.register("coffeepopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 火龙果棒冰
    public static final DeferredItem<Item> DRAGONFRUITPOPSICLE = REGISTRY.register("dragonfruitpopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 榴莲棒冰
    public static final DeferredItem<Item> DURIANPOPSICLE = REGISTRY.register("durianpopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 葡萄棒冰
    public static final DeferredItem<Item> GRAPEPOPSICLE = REGISTRY.register("grapepopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 青葡萄棒冰
    public static final DeferredItem<Item> GREENGRAPEPOPSICLE = REGISTRY.register("greengrapepopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 青李子棒冰
    public static final DeferredItem<Item> GREENPLUMPOPSICLE = REGISTRY.register("greenplumpopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 哈密瓜棒冰
    public static final DeferredItem<Item> HAMIMELONPOPSICLE = REGISTRY.register("hamimelonpopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 山楂棒冰
    public static final DeferredItem<Item> HAWTHORNPOPSICLE = REGISTRY.register("hawthornpopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 水蜜桃棒冰
    public static final DeferredItem<Item> HONEYPEACHPOPSICLE = REGISTRY.register("honeypeachpopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 猕猴桃棒冰
    public static final DeferredItem<Item> KIWIFRUITPOPSICLE = REGISTRY.register("kiwifruitpopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 柠檬棒冰
    public static final DeferredItem<Item> LEMONPOPSICLE = REGISTRY.register("lemonpopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 枇杷棒冰
    public static final DeferredItem<Item> LOQUATPOPSICLE = REGISTRY.register("loquatpopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 荔枝棒冰
    public static final DeferredItem<Item> LYCHEEPOPSICLE = REGISTRY.register("lycheepopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 芒果棒冰
    public static final DeferredItem<Item> MANGOPOPSICLE = REGISTRY.register("mangopopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 山竹棒冰
    public static final DeferredItem<Item> MANGOSTEEPOPSICLE = REGISTRY.register("mangosteenpopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 桑葚棒冰
    public static final DeferredItem<Item> MULBERRYPOPSICLE = REGISTRY.register("mulberrypopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 油桃棒冰
    public static final DeferredItem<Item> NECTARINEPOPSICLE = REGISTRY.register("nectarinepopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 橙子棒冰
    public static final DeferredItem<Item> ORANGEPOPSICLE = REGISTRY.register("orangepopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 木瓜棒冰
    public static final DeferredItem<Item> PAWPAWPOPSICLE = REGISTRY.register("pawpawpopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 梨子棒冰
    public static final DeferredItem<Item> PEARPOPSICLE = REGISTRY.register("pearpopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 菠萝棒冰
    public static final DeferredItem<Item> PINEAPPLEPOPSICLE = REGISTRY.register("pineapplepopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 李子棒冰
    public static final DeferredItem<Item> PLUMPOPSICLE = REGISTRY.register("plumpopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 石榴棒冰
    public static final DeferredItem<Item> POMEGRANATEPOPSICLE = REGISTRY.register("pomegranatepopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 棒冰
    public static final DeferredItem<Item> POPSICLE = REGISTRY.register("popsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 草莓棒冰
    public static final DeferredItem<Item> STRAWBERRYPOPSICLE = REGISTRY.register("strawberrypopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 甜浆果棒冰
    public static final DeferredItem<Item> SWEETBERRYPOPSICLE = REGISTRY.register("sweetberrypopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 甜瓜棒冰
    public static final DeferredItem<Item> SWEETMELONPOPSICLE = REGISTRY.register("sweetmelonpopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 橘子棒冰
    public static final DeferredItem<Item> TANGERINEPOPSICLE = REGISTRY.register("tangerinepopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 西瓜棒冰
    public static final DeferredItem<Item> WATERMELONPOPSICLE = REGISTRY.register("watermelonpopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// 冬枣棒冰
    public static final DeferredItem<Item> WINTERJUJUBEPOPSICLE = REGISTRY.register("winterjujubepopsicle",
            () -> new DrinkItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

// Ice Cream ϵ��ʳƷ

// 苹果冰淇淋
    public static final DeferredItem<Item> APPLEICECREAM = REGISTRY.register("appleicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 杏子冰淇淋
    public static final DeferredItem<Item> APRICOTICECREAM = REGISTRY.register("apricoticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 香蕉冰淇淋
    public static final DeferredItem<Item> BANANAICECREAM = REGISTRY.register("bananaicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 蓝莓冰淇淋
    public static final DeferredItem<Item> BLUEBERRYICECREAM = REGISTRY.register("blueberryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 杨桃冰淇淋
    public static final DeferredItem<Item> CARAMBOLAICECREAM = REGISTRY.register("carambolaicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 樱桃冰淇淋
    public static final DeferredItem<Item> CHERRYICECREAM = REGISTRY.register("cherryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 巧克力冰淇淋
    public static final DeferredItem<Item> CHOCOLATEICECREAM = REGISTRY.register("chocolateicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 椰子冰淇淋
    public static final DeferredItem<Item> COCONUTICECREAM = REGISTRY.register("coconuticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 咖啡冰淇淋
    public static final DeferredItem<Item> COFFEEICECREAM = REGISTRY.register("coffeeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 火龙果冰淇淋
    public static final DeferredItem<Item> DRAGONFRUITICECREAM = REGISTRY.register("dragonfruiticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 榴莲冰淇淋
    public static final DeferredItem<Item> DURIANICECREAM = REGISTRY.register("durianicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 葡萄冰淇淋
    public static final DeferredItem<Item> GRAPEICECREAM = REGISTRY.register("grapeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 青葡萄冰淇淋
    public static final DeferredItem<Item> GREENGRAPEICECREAM = REGISTRY.register("greengrapeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 青李子冰淇淋
    public static final DeferredItem<Item> GREENPLUMICECREAM = REGISTRY.register("greenplumicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 哈密瓜冰淇淋
    public static final DeferredItem<Item> HAMIMELONICECREAM = REGISTRY.register("hamimelonicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 山楂冰淇淋
    public static final DeferredItem<Item> HAWTHORNICECREAM = REGISTRY.register("hawthornicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 水蜜桃冰淇淋
    public static final DeferredItem<Item> HONEYPEACHICECREAM = REGISTRY.register("honeypeachicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 猕猴桃冰淇淋
    public static final DeferredItem<Item> KIWIFRUITICECREAM = REGISTRY.register("kiwifruiticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 柠檬冰淇淋
    public static final DeferredItem<Item> LEMONICECREAM = REGISTRY.register("lemonicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 枇杷冰淇淋
    public static final DeferredItem<Item> LOQUATICECREAM = REGISTRY.register("loquaticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 荔枝冰淇淋
    public static final DeferredItem<Item> LYCHEEICECREAM = REGISTRY.register("lycheeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 芒果冰淇淋
    public static final DeferredItem<Item> MANGOICECREAM = REGISTRY.register("mangoicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 山竹冰淇淋
    public static final DeferredItem<Item> MANGOSTEENICECREAM = REGISTRY.register("mangosteenicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 牛奶冰淇淋
    public static final DeferredItem<Item> MILKICECREAM = REGISTRY.register("milkicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 桑葚冰淇淋
    public static final DeferredItem<Item> MULBERRYICECREAM = REGISTRY.register("mulberryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 油桃冰淇淋
    public static final DeferredItem<Item> NECTARINEICECREAM = REGISTRY.register("nectarineicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 橙子冰淇淋
    public static final DeferredItem<Item> ORANGEICECREAM = REGISTRY.register("orangeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 木瓜冰淇淋
    public static final DeferredItem<Item> PAWPAWICECREAM = REGISTRY.register("pawpawicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 梨子冰淇淋
    public static final DeferredItem<Item> PEARICECREAM = REGISTRY.register("pearicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 菠萝冰淇淋
    public static final DeferredItem<Item> PINEAPPLEICECREAM = REGISTRY.register("pineappleicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 李子冰淇淋
    public static final DeferredItem<Item> PLUMICECREAM = REGISTRY.register("plumicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 石榴冰淇淋
    public static final DeferredItem<Item> POMEGRANATEICECREAM = REGISTRY.register("pomegranateicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 草莓冰淇淋
    public static final DeferredItem<Item> STRAWBERRYICECREAM = REGISTRY.register("strawberryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 甜浆果冰淇淋
    public static final DeferredItem<Item> SWEETBERRYICECREAM = REGISTRY.register("sweetberryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 甜瓜冰淇淋
    public static final DeferredItem<Item> SWEETMELONICECREAM = REGISTRY.register("sweetmelonicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 橘子冰淇淋
    public static final DeferredItem<Item> TANGERINEICECREAM = REGISTRY.register("tangerineicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 西瓜冰淇淋
    public static final DeferredItem<Item> WATERMELONICECREAM = REGISTRY.register("watermelonicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// 冬枣冰淇淋
    public static final DeferredItem<Item> WINTERJUJUBEICECREAM = REGISTRY.register("winterjujubeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

// Grains, mushrooms, tea, and crop products

// 高粱
    public static final DeferredItem<Item> KAOLIANGGRAIN = REGISTRY.register("kaolianggrain",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(0.3f).alwaysEdible().build())));
// 白蘑菇
    public static final DeferredItem<Item> WHITEMUSHROOM = REGISTRY.register("whitemushroom",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
// 木耳
    public static final DeferredItem<Item> BLACKFUNGUS = REGISTRY.register("blackfungus",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
// 干木耳
    public static final DeferredItem<Item> DRYBLACKFUNGUS = REGISTRY.register("dryblackfungus", () -> new Item(new Item.Properties()));
// 杏鲍菇
    public static final DeferredItem<Item> PLEUROTUSERYNGII = REGISTRY.register("pleurotuseryngii",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(3).saturationModifier(0.4f).alwaysEdible().build())));
// 金针菇
    public static final DeferredItem<Item> ENOKIMUSHROOM = REGISTRY.register("enokimushroom",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
// 银耳
    public static final DeferredItem<Item> TREMELLA = REGISTRY.register("tremella",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
// 香菇
    public static final DeferredItem<Item> FRAGRANTMUSHROOM = REGISTRY.register("fragrantmushroom",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(3).saturationModifier(0.4f).alwaysEdible().build())));
// 干香菇
    public static final DeferredItem<Item> DRYFRAGRANTMUSHROOM = REGISTRY.register("dryfragrantmushroom", () -> new Item(new Item.Properties()));
// 绿茶叶
    public static final DeferredItem<Item> GREENTEALEAVES = REGISTRY.register("greentealeaves", () -> new Item(new Item.Properties()));
// 干绿茶叶
    public static final DeferredItem<Item> DRYGREENTEA = REGISTRY.register("drygreentea", () -> new Item(new Item.Properties()));
// 红茶叶
    public static final DeferredItem<Item> REDTEALEAVES = REGISTRY.register("redtealeaves", () -> new Item(new Item.Properties()));
// 干红茶叶
    public static final DeferredItem<Item> DRYREDTEA = REGISTRY.register("dryredtea", () -> new Item(new Item.Properties()));
// 莲藕
    public static final DeferredItem<Item> LOTUSROOT = REGISTRY.register("lotusroot",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(3).saturationModifier(0.4f).alwaysEdible().build())));
// 大米
    public static final DeferredItem<Item> PADDYGRAIN = REGISTRY.register("paddygrain", () -> new Item(new Item.Properties()));

// Drinks

// 凉白开水
    public static final DeferredItem<Item> DRINKINGWATER = drink("drinkingwater");
// 卡布奇诺咖啡
    public static final DeferredItem<Item> CAPPUCCINO = drink("cappuccino");
// 拿铁咖啡
    public static final DeferredItem<Item> CAFELATTE = drink("cafelatte");
// 摩卡咖啡
    public static final DeferredItem<Item> MOCHACAFE = drink("mochacafe");
// 热水
    public static final DeferredItem<Item> HOTWATER = drink("hotwater");
// 美式咖啡
    public static final DeferredItem<Item> CAFE_AMERICANO = drink("cafe_americano");
// 柠檬红茶
    public static final DeferredItem<Item> LEMONREDTEA = drink("lemonredtea");
// 波霸奶绿
    public static final DeferredItem<Item> GRENTEAMILKWITHPEARL = drink("grenteamilkwithpearl");
// 清新奶绿
    public static final DeferredItem<Item> GREEN_TEA_LATTES = drink("green_tea_lattes");
// 糯香柠檬茶
    public static final DeferredItem<Item> LEMONTEAWITHGLUTINOUSRICEFLAVOR = drink("lemonteawithglutinousriceflavor");
// 红茶
    public static final DeferredItem<Item> REDTEA = drink("redtea");
// 绿茶
    public static final DeferredItem<Item> GREENTEA = drink("greentea");
// 苦荞茶
    public static final DeferredItem<Item> BUCKWHEATTEA = drink("buckwheattea");
// 阿萨姆奶茶
    public static final DeferredItem<Item> ASSAMMILK_TEA = drink("assammilk_tea");
// 青梅绿茶
    public static final DeferredItem<Item> GREENPLUMTEA = drink("greenplumtea");
// 黑霸红奶
    public static final DeferredItem<Item> REDTEAMILKWITHPEARL = drink("redteamilkwithpearl");
// 三十里红
    public static final DeferredItem<Item> KAOLIANGWINE = drink("kaoliangwine");
// 贵州茅台
    public static final DeferredItem<Item> KWEICHOW_MOUTAI = drink("kweichow_moutai");
// 青岛啤酒
    public static final DeferredItem<Item> TSINGTAO_BEER = drink("tsingtao_beer");
// 劲凉冰红茶
    public static final DeferredItem<Item> ICEDBLACKTEABLUE = drink("icedblackteablue");
// 无糖冰红茶
    public static final DeferredItem<Item> SUGARFREICEDTEA = drink("sugarfreicedtea");
// 雪碧
    public static final DeferredItem<Item> SPRITE = drink("sprite");

// Fruit

// 一根香蕉
    public static final DeferredItem<Item> APIECEOFBANANA = food("apieceofbanana", 4, 0.3f);
// 香蕉
    public static final DeferredItem<Item> BANANA = food("banana", 4, 0.3f);
// 香蕉果肉
    public static final DeferredItem<Item> PULLEDBANANA_2 = food("pulledbanana_2", 6, 0.6f);
// 橙子
    public static final DeferredItem<Item> ORANGE = food("orange", 4, 0.3f);
// §6§l血橙
    public static final DeferredItem<Item> BLOODORANGE = rareFruit("bloodorange", 6, 0.5f);
// 橘子
    public static final DeferredItem<Item> TANGERINE = food("tangerine", 4, 0.3f);
// §6§l枳
    public static final DeferredItem<Item> TANGERINE_1 = rareFruit("tangerine_1", 6, 0.5f);
// §6§l丑橘
    public static final DeferredItem<Item> UGLYORANGE = rareFruit("uglyorange", 6, 0.5f);
// 柠檬
    public static final DeferredItem<Item> LEMON = food("lemon", 3, 0.2f);
// 哈密瓜
    public static final DeferredItem<Item> HAMIMELON = food("hamimelon", 6, 0.6f);
// 甜瓜
    public static final DeferredItem<Item> SWEETMELON = food("sweetmelon", 6, 0.6f);
// §6§l羊角蜜甜瓜
    public static final DeferredItem<Item> SWEETMELON_1 = rareFruit("sweetmelon_1", 8, 0.8f);
// 椰子
    public static final DeferredItem<Item> COCONUT = REGISTRY.register("coconut", () -> new ThrowableFruitItem(new Item.Properties(), "flavor_immersed_daily:coconut_shell", 1, "flavor_immersed_daily:coconutmeat", 2, "", 0, 4.0f));
// 椰子肉
    public static final DeferredItem<Item> COCONUTMEAT = food("coconutmeat", 6, 0.6f);
// 椰子壳
    public static final DeferredItem<Item> COCONUT_SHELL = food("coconut_shell", 2, 0.3f);
// 榴莲
    public static final DeferredItem<Item> DURIAN = REGISTRY.register("durian", () -> new ThrowableFruitItem(new Item.Properties(), "flavor_immersed_daily:durianmeat", 2, "flavor_immersed_daily:durianshellhat", 1, "", 0, 6.0f));
// 榴莲果肉
    public static final DeferredItem<Item> DURIANMEAT = food("durianmeat", 8, 0.8f);
// 榴莲壳帽子
    public static final DeferredItem<Item> DURIANSHELLHAT = REGISTRY.register("durianshellhat", () -> new Item(new Item.Properties()));
// 蓝莓
    public static final DeferredItem<Item> BLUEBERRY = food("blueberry", 2, 0.2f);
// 葡萄
    public static final DeferredItem<Item> GRAPE = seedableFruit("grape", "flavor_immersed_daily:agrape");
// 青提
    public static final DeferredItem<Item> GREENGRAPE = seedableFruit("greengrape", "flavor_immersed_daily:agreengrape");
// 桑葚
    public static final DeferredItem<Item> MULBERRY = food("mulberry", 2, 0.2f);
// 草莓
    public static final DeferredItem<Item> STRAWBERRY = food("strawberry", 2, 0.2f);
// 杏子
    public static final DeferredItem<Item> APRICOT = food("apricot", 4, 0.3f);
// 樱桃
    public static final DeferredItem<Item> CHERRY = food("cherry", 3, 0.3f);
// 青李子
    public static final DeferredItem<Item> GREENPLUM = food("greenplum", 3, 0.3f);
// 水蜜桃
    public static final DeferredItem<Item> HONEYPEACH = food("honeypeach", 4, 0.3f);
// §6§l福寿蟠桃
    public static final DeferredItem<Item> LIFEPEACH = rareFruit("lifepeach", 6, 0.5f);
// 油桃
    public static final DeferredItem<Item> NECTARINE = food("nectarine", 4, 0.3f);
// 李子
    public static final DeferredItem<Item> PLUM = food("plum", 3, 0.3f);
// 冬枣
    public static final DeferredItem<Item> WINTERJUJUBE = food("winterjujube", 4, 0.3f);
// 杨桃
    public static final DeferredItem<Item> CARAMBOLA = food("carambola", 4, 0.3f);
// 火龙果
    public static final DeferredItem<Item> DRAGONFRUIT = food("dragonfruit", 4, 0.3f);
// 猕猴桃
    public static final DeferredItem<Item> KIWIFRUIT = food("kiwifruit", 4, 0.3f);
// 荔枝
    public static final DeferredItem<Item> LYCHEE = food("lychee", 3, 0.3f);
// 芒果
    public static final DeferredItem<Item> MANGO = food("mango", 4, 0.3f);
// 山竹
    public static final DeferredItem<Item> MANGOSTEEN = food("mangosteen", 4, 0.3f);
// 木瓜
    public static final DeferredItem<Item> PAWPAW = food("pawpaw", 4, 0.3f);
// 菠萝
    public static final DeferredItem<Item> PINEAPPLE = food("pineapple", 5, 0.4f);
// 开心果
    public static final DeferredItem<Item> PISTACHIONUT = food("pistachionut", 3, 0.3f);
// 红枣
    public static final DeferredItem<Item> REDDATE = food("reddate", 4, 0.3f);
// 核桃
    public static final DeferredItem<Item> WALNUT = food("walnut", 3, 0.3f);
// 干烤开心果
    public static final DeferredItem<Item> DRY_PISTACHIONUT = food("dry_pistachionut", 5, 0.5f);
// 开心果
    public static final DeferredItem<Item> PISTACHIONUT_0 = food("pistachionut_0", 3, 0.3f);
// 开口开心果
    public static final DeferredItem<Item> PISTACHIONUTSWITHOPENSHELLS = food("pistachionutswithopenshells", 3, 0.3f);
// 核桃仁
    public static final DeferredItem<Item> WALNUTKINNEL = food("walnutkinnel", 4, 0.4f);
// 葡萄籽
    public static final DeferredItem<Item> GRAPESEED = REGISTRY.register("grapeseed", () -> new Item(new Item.Properties()));
// 桂皮
    public static final DeferredItem<Item> CINNAMON = REGISTRY.register("cinnamon", () -> new Item(new Item.Properties()));
// §6§l青苹果
    public static final DeferredItem<Item> GREENAPPLE = rareFruit("greenapple", 4, 0.3f);
// 山楂
    public static final DeferredItem<Item> HAWTHORN = food("hawthorn", 3, 0.3f);
// 枇杷
    public static final DeferredItem<Item> LOQUAT = food("loquat", 4, 0.3f);
// 梨
    public static final DeferredItem<Item> PEAR = food("pear", 4, 0.3f);
// 石榴
    public static final DeferredItem<Item> POMEGRANATE = food("pomegranate", 5, 0.4f);

// Vine crop seeds and produce

// 黄瓜籽
    public static final DeferredItem<Item> CUCUMBERSEEDS = REGISTRY.register("cucumberseeds", () -> new Item(new Item.Properties()));
// 冬瓜籽
    public static final DeferredItem<Item> WAX_GOURD_SEED_BLOCK = REGISTRY.register("wax_gourd_seed_block", () -> new Item(new Item.Properties()));
// 四季豆籽
    public static final DeferredItem<Item> KIDNEYBEANSEED = REGISTRY.register("kidneybeanseed", () -> new Item(new Item.Properties()));
// 茄子籽
    public static final DeferredItem<Item> AUBERGINESEEDBLOCK = REGISTRY.register("aubergineseedblock", () -> new Item(new Item.Properties()));
// 番茄籽
    public static final DeferredItem<Item> TOMATOSEED = REGISTRY.register("tomatoseed", () -> new Item(new Item.Properties()));
// 豇豆籽
    public static final DeferredItem<Item> COWPEABEANSEED = REGISTRY.register("cowpeabeanseed", () -> new Item(new Item.Properties()));
// 青葡萄籽
    public static final DeferredItem<Item> GREENGRAESEED = REGISTRY.register("greengrapeseed", () -> new Item(new Item.Properties()));
// 丝瓜籽
    public static final DeferredItem<Item> LOOFAHSEED = REGISTRY.register("loofahseed", () -> new Item(new Item.Properties()));
// 黄瓜
    public static final DeferredItem<Item> CUCUMBER = food("cucumber", 3, 0.3f);
// 冬瓜
    public static final DeferredItem<Item> WAXGOURD = food("waxgourd", 3, 0.3f);
// 四季豆
    public static final DeferredItem<Item> KIDNEYBEAN = food("kidneybean", 3, 0.3f);
// 茄子
    public static final DeferredItem<Item> AUBERGINE = food("aubergine", 3, 0.3f);
// 番茄
    public static final DeferredItem<Item> TOMATO = food("tomato", 3, 0.3f);
// 豇豆
    public static final DeferredItem<Item> COWPEA = food("cowpea", 3, 0.3f);
// 丝瓜
    public static final DeferredItem<Item> LOOFAH = food("loofah", 3, 0.3f);

// Wild harvests

// 温带野果作物堆
    public static final DeferredItem<Item> TEMPERATEWILDFRUIT = wildHarvest("temperatewildfruit", "tooltip.flavor_immersed_daily.wild_fruit_harvest");
// 热带野果作物堆
    public static final DeferredItem<Item> TROPICALWILD_FRUIT = wildHarvest("tropicalwild_fruit", "tooltip.flavor_immersed_daily.wild_fruit_harvest");
// 野生花叶作物堆
    public static final DeferredItem<Item> WILDFLOWERANDLEAF = wildHarvest("wildflowerandleaf", "tooltip.flavor_immersed_daily.wild_crop_harvest");
// 寒带野果作物堆
    public static final DeferredItem<Item> WILDFRUITINCOLDZONE = wildHarvest("wildfruitincoldzone", "tooltip.flavor_immersed_daily.wild_fruit_harvest");
// 野生谷物作物堆
    public static final DeferredItem<Item> WILDGRAINPLANT = wildHarvest("wildgrainplant", "tooltip.flavor_immersed_daily.wild_crop_harvest");
// 野生菌菇作物堆
    public static final DeferredItem<Item> WILDMUSHROOMPLANT = wildHarvest("wildmushroomplant", "tooltip.flavor_immersed_daily.wild_crop_harvest");
// 野生籽叶作物堆
    public static final DeferredItem<Item> WILDSEEDPLANT = wildHarvest("wildseedplant", "tooltip.flavor_immersed_daily.wild_crop_harvest");
// 野生块茎作物堆
    public static final DeferredItem<Item> WILDTUBERPLANTS = wildHarvest("wildtuberplants", "tooltip.flavor_immersed_daily.wild_crop_harvest");

// Simple foods

    public static final DeferredItem<Item> BAKEDWHITEMUSHROOMSWITHCREAM = REGISTRY.register(
            "bakedwhitemushroomswithcream",
            () -> new Item(new Item.Properties()
                    .component(DataComponents.FOOD, new FoodProperties.Builder()
                            .nutrition(8)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));
// 豆芽焖粉丝
    public static final DeferredItem<Item> BRAISEDBEANSPROUTSWITHVERMICELLI = REGISTRY.register("braisedbeansproutswithvermicelli",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.6f).alwaysEdible().build())));
// 粤式肠粉
    public static final DeferredItem<Item> CANTONESERICEROLLS = REGISTRY.register("cantonesericerolls",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.6f).alwaysEdible().build())));
// 咖喱鸡
    public static final DeferredItem<Item> CURRYSTEWEDCHICKEN = REGISTRY.register("currystewedchicken",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.6f).alwaysEdible().build())));
// 秋葵浓汤
    public static final DeferredItem<Item> GUMBOSOUP = REGISTRY.register("gumbosoup",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.6f).alwaysEdible().build())));
// 红枣银耳粥
    public static final DeferredItem<Item> REDDATESANDTREMELLAPORRIDGE = REGISTRY.register("reddatesandtremellaporridge",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.6f).alwaysEdible().build())));
// 临沂牛肉糁
    public static final DeferredItem<Item> SA = REGISTRY.register("sa",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.6f).alwaysEdible().build())));
// 番茄炒蛋
    public static final DeferredItem<Item> SCRAMBLEDEGGWITHTOMATO = REGISTRY.register("scrambledeggwithtomato",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.6f).alwaysEdible().build())));
// 大集羊杂汤
    public static final DeferredItem<Item> SHEEPGIBLETSSOUP = REGISTRY.register("sheepgibletssoup",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.6f).alwaysEdible().build())));
// 蒸蛋羹
    public static final DeferredItem<Item> STEAMEDEGGCUSTARD = REGISTRY.register("steamedeggcustard",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.6f).alwaysEdible().build())));
// 香菇炖鸡汤
    public static final DeferredItem<Item> STEWEDCHICKENSOUPWITHMUSHROOMS = REGISTRY.register("stewedchickensoupwithmushrooms",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.6f).alwaysEdible().build())));
// 冬瓜炖鸡
    public static final DeferredItem<Item> STEWEDCHICKENWITHWAXGOURD = REGISTRY.register("stewedchickenwithwaxgourd",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.6f).alwaysEdible().build())));
// 丝瓜炒蛋
    public static final DeferredItem<Item> STEWEDEGGSWITHLOOFAH = REGISTRY.register("stewedeggswithloofah",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.6f).alwaysEdible().build())));
// 炖猪杂
    public static final DeferredItem<Item> STEWEDPORKOFFAL = REGISTRY.register("stewedporkoffal",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.6f).alwaysEdible().build())));
// 冬枣冬瓜汤
    public static final DeferredItem<Item> WINTERJUJUBEANDWAXGOURDSOUP = REGISTRY.register("winterjujubeandwaxgourdsoup",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.6f).alwaysEdible().build())));

// Crop products

// 洋葱
    public static final DeferredItem<Item> ONION = food("onion", 2, 0.3f);
// 豌豆
    public static final DeferredItem<Item> PEA = food("pea", 1, 0.2f);
// 紫薯
    public static final DeferredItem<Item> PURPLESWEETPOTATO = food("purplesweetpotato", 2, 0.3f);
// 芝麻
    public static final DeferredItem<Item> SESAME = food("sesame", 1, 0.2f);
// 黄豆荚
    public static final DeferredItem<Item> SOYBEAN = food("soybean", 1, 0.2f);
// 青椒
    public static final DeferredItem<Item> SWEETGREENPEPPER = food("sweetgreenpepper", 2, 0.3f);
// 西葫芦
    public static final DeferredItem<Item> ZUCCHINI = food("zucchini", 2, 0.3f);

// Basic ingredients

// 冰沙
    public static final DeferredItem<Item> SORBET = item("sorbet");
// 肠衣
    public static final DeferredItem<Item> CASING = item("casing");
// 蜂窝煤
    public static final DeferredItem<Item> HONEYCOMBBRIQUET = item("honeycombbriquet");
// 麸
    public static final DeferredItem<Item> BRAN = item("bran");
// 木薯珍珠
    public static final DeferredItem<Item> CASSAVAPEARL = item("cassavapearl");
// 泡发的黄豆
    public static final DeferredItem<Item> SOAKEDSOYBEANS = item("soakedsoybeans");
// 肉松
    public static final DeferredItem<Item> MEATFLOSS = food("meatfloss", 12, 0.8f);
// 砂砾膏
    public static final DeferredItem<Item> GRAVELPASTE = item("gravelpaste");
// 生酱油肉丝
    public static final DeferredItem<Item> RAWSOYSHREDDEDMEAT = item("rawsoyshreddedmeat");
// 石膏粉
    public static final DeferredItem<Item> LANDPLASTER = item("landplaster");
// 摔炮
    public static final DeferredItem<Item> WRESTLING_GUN = REGISTRY.register("wrestling_gun",
            () -> new WrestlingGunItem(new Item.Properties().stacksTo(16)));
// 碎羊杂
    public static final DeferredItem<Item> RAWSHEEPOFFAL = item("rawsheepoffal");
// 洗净的芦苇叶
    public static final DeferredItem<Item> TIDYREEDLEAF = item("tidyreedleaf");
// 食用小苏打
    public static final DeferredItem<Item> NAHCO_3 = item("nahco_3");
// 益生菌落
    public static final DeferredItem<Item> PROBIOTICS = item("probiotics");

// Prepared vegetables and meat

// 葱段
    public static final DeferredItem<Item> SCALLION_2 = food("scallion_2", 1, 0.6f);
// 切块的胡萝卜
    public static final DeferredItem<Item> DICEDCARROTS = food("dicedcarrots", 1, 0.6f);
// 胡萝卜丝
    public static final DeferredItem<Item> SHREDDEDCARROTS = food("shreddedcarrots", 1, 0.6f);
// 黄瓜滚刀块
    public static final DeferredItem<Item> DICEDCUCUMBER = food("dicedcucumber", 1, 0.6f);
// 黄瓜片
    public static final DeferredItem<Item> SLICEDCUCUMBER = food("slicedcucumber", 1, 0.6f);
// 黄瓜丝
    public static final DeferredItem<Item> SHREDDERCUCUMBER = food("shreddercucumber", 1, 0.6f);
// 茄子片
    public static final DeferredItem<Item> SLICEDABUERGINE = food("slicedabuergine", 1, 0.6f);
// 茄子条
    public static final DeferredItem<Item> SHREDDERABUERGINE = food("shredderabuergine", 1, 0.6f);
// 豇豆段
    public static final DeferredItem<Item> CHOPPEDCOWPEA = food("choppedcowpea", 1, 0.6f);
// 萝卜段
    public static final DeferredItem<Item> DIECEDRADISH = food("diecedradish", 1, 0.6f);
// 萝卜块
    public static final DeferredItem<Item> DICEDRADISH = food("dicedradish", 1, 0.6f);
// 萝卜片
    public static final DeferredItem<Item> SLICEDRADISH = food("slicedradish", 1, 0.6f);
// 萝卜丝
    public static final DeferredItem<Item> SHREDDERRADISH = food("shredderradish", 1, 0.6f);
// 木薯段
    public static final DeferredItem<Item> DICEDCASSAVA = food("dicedcassava", 1, 0.6f);
// 柠檬片
    public static final DeferredItem<Item> SLICEDLEMON = food("slicedlemon", 1, 0.6f);
// 藕段
    public static final DeferredItem<Item> DICEDLOTUSROOT = food("dicedlotusroot", 1, 0.6f);
// 藕片
    public static final DeferredItem<Item> SLICEDLOTUSROOT = food("slicedlotusroot", 1, 0.6f);
// 切过的洋葱
    public static final DeferredItem<Item> DICEDONION = food("dicedonion", 1, 0.6f);
// 切开的橙子
    public static final DeferredItem<Item> DICEDORANGE = food("dicedorange", 1, 0.6f);
// 切开的冬瓜
    public static final DeferredItem<Item> DICEDWAXGOURD = food("dicedwaxgourd", 1, 0.6f);
// 切片的杏鲍菇
    public static final DeferredItem<Item> DICEDPLEUROTUSERYNGII = food("dicedpleurotuseryngii", 1, 0.6f);
// 芹菜段
    public static final DeferredItem<Item> DICEDCELERY = food("dicedcelery", 1, 0.6f);
// 青椒圈
    public static final DeferredItem<Item> DICEDSWEETPEPPER = food("dicedsweetpepper", 1, 0.6f);
// 山药段
    public static final DeferredItem<Item> DICEDCHINESEYAM = food("dicedchineseyam", 1, 0.6f);
// 生鱼片
    public static final DeferredItem<Item> DICEDFISH = food("dicedfish", 1, 0.6f);
// 丝瓜段
    public static final DeferredItem<Item> DICEDLOOFACH = food("dicedloofach", 1, 0.6f);
// 四季豆段
    public static final DeferredItem<Item> DICEDSAUTEEDGREENBEANS = food("dicedsauteedgreenbeans", 1, 0.6f);
// 土豆块
    public static final DeferredItem<Item> DICEDPOTATO = food("dicedpotato", 1, 0.6f);
// 土豆片
    public static final DeferredItem<Item> SLICEDPOTATO = food("slicedpotato", 1, 0.6f);
// 土豆丝
    public static final DeferredItem<Item> SHREDDEDPOTATO_2 = food("shreddedpotato_2", 1, 0.6f);
// 土豆条
    public static final DeferredItem<Item> CHOPPEDPOTATO_1 = food("choppedpotato_1", 1, 0.6f);
// 西葫芦片
    public static final DeferredItem<Item> DICEDZUCCHINI = food("dicedzucchini", 1, 0.6f);
// 紫薯段
    public static final DeferredItem<Item> DICEDPURPLESWEETPOTATO = food("dicedpurplesweetpotato", 1, 0.6f);

// Dough, wrappers, and noodles

// 包子皮
    public static final DeferredItem<Item> BAOZI_SKIN = food("baozi_skin", 1, 0.6f);
// 生肠粉
    public static final DeferredItem<Item> VERMICELLIROLL = food("vermicelliroll", 1, 0.6f);
// 捶打后的糯米面剂子
    public static final DeferredItem<Item> BEATGLUTINOUSRICEFLOURPASTE = food("beatglutinousriceflourpaste", 1, 0.6f);
// 干粉皮
    public static final DeferredItem<Item> DRYPOWDERSKIN = food("drypowderskin", 1, 0.6f);
// 干粉丝
    public static final DeferredItem<Item> DRIEDVERMICELLI = food("driedvermicelli", 1, 0.6f);
// 干粉条
    public static final DeferredItem<Item> DRYPOWDERSTRIP = food("drypowderstrip", 1, 0.6f);
// 裹上糖浆的鸡蛋面条
    public static final DeferredItem<Item> EGGNOODLESWRAPPEDINSYRUP = food("eggnoodleswrappedinsyrup", 1, 0.6f);
// 裹上糖浆的面条
    public static final DeferredItem<Item> NOODLESWRAPPEDINSYRUP = food("noodleswrappedinsyrup", 1, 0.6f);
// 核桃仁面剂子
    public static final DeferredItem<Item> WALNUTFLOURPASTE = food("walnutflourpaste", 1, 0.6f);
// 红薯淀粉面团
    public static final DeferredItem<Item> SWEETPOTATODOUGH = food("sweetpotatodough", 1, 0.6f);
// 馄饨皮
    public static final DeferredItem<Item> FLOURSKIN = food("flourskin", 1, 0.6f);
// 鸡蛋面团
    public static final DeferredItem<Item> EGGDOUGH = food("eggdough", 1, 0.6f);
// 糖油面团
    public static final DeferredItem<Item> SWEET_OIL_DOUGH = food("sweet_oil_dough", 1, 0.6f);
// 油酥面团
    public static final DeferredItem<Item> WATER_SHORT_DOUGH = food("water_short_dough", 1, 0.6f);
// 提浆面团
    public static final DeferredItem<Item> SYRUP_DOUGH = food("syrup_dough", 1, 0.6f);
// 糯奶面团
    public static final DeferredItem<Item> GLUTINOUS_MILK_DOUGH = food("glutinous_milk_dough", 1, 0.6f);
// 生鸡蛋面条
    public static final DeferredItem<Item> RAWEGGNOODLES = food("raweggnoodles", 1, 0.6f);
// 饺子皮
    public static final DeferredItem<Item> DUMPLING_SKIN = food("dumpling_skin", 1, 0.6f);
// 米粉
    public static final DeferredItem<Item> RAWRICENOODLES = food("rawricenoodles", 1, 0.6f);
// 米线
    public static final DeferredItem<Item> RICENOODLES_2 = food("ricenoodles_2", 1, 0.6f);
// 面剂子
    public static final DeferredItem<Item> FLOURPASTE = food("flourpaste", 1, 0.6f);
// 面团
    public static final DeferredItem<Item> DOUGH = food("dough", 1, 0.6f);
// 烧麦皮
    public static final DeferredItem<Item> SHUMAISKIN = food("shumaiskin", 1, 0.6f);
// 生粗面条
    public static final DeferredItem<Item> RAWCOARSENOODLES = food("rawcoarsenoodles", 1, 0.6f);
// 干米糕
    public static final DeferredItem<Item> DRYRICECAKE = food("dryricecake", 1, 0.6f);
// 生鸡蛋饼
    public static final DeferredItem<Item> RAWEGGPANCAKE = food("raweggpancake", 1, 0.6f);
// 生鸡蛋面剂子
    public static final DeferredItem<Item> RAWEGGPASTE = food("raweggpaste", 1, 0.6f);
// 生鸡蛋面皮
    public static final DeferredItem<Item> RAWEGGSKIN = food("raweggskin", 1, 0.6f);
// 生面饼
    public static final DeferredItem<Item> RAWFLOURPANCAKE = food("rawflourpancake", 1, 0.6f);
// 生面卷
    public static final DeferredItem<Item> RAWFLOURROLL = food("rawflourroll", 1, 0.6f);
// 生面条
    public static final DeferredItem<Item> RAWNOODLES = food("rawnoodles", 1, 0.6f);
// 生糯米面饼
    public static final DeferredItem<Item> RAWGLUTINOUSRICEFLOURPANCAKE = food("rawglutinousriceflourpancake", 1, 0.6f);
// 生糯米面剂子
    public static final DeferredItem<Item> RAWGLUTINOUSPASTE = food("rawglutinouspaste", 1, 0.6f);
// 生糯米面皮
    public static final DeferredItem<Item> RAWGLUTINOUSSKIN = food("rawglutinousskin", 1, 0.6f);
// 生糯米面条
    public static final DeferredItem<Item> RAWGLUTINOUSNOODLES = food("rawglutinousnoodles", 1, 0.6f);
// 生糯米面团
    public static final DeferredItem<Item> RAWGLUTINOUSDOUGH = food("rawglutinousdough", 1, 0.6f);
// 生烧麦
    public static final DeferredItem<Item> RAWSHUMAI = filledFood("rawshumai", 1, 0.6f);
// 生馅饼
    public static final DeferredItem<Item> RAWPIE = filledFood("rawpie", 1, 0.6f);
// 生月饼
    public static final DeferredItem<Item> RAWMOONCAKE = filledFood("rawmooncake", 1, 0.6f);
// 生的广式月饼
    public static final DeferredItem<Item> RAWCANTONESE_MOONCAKE = filledFood("rawcantonese_mooncake", 1, 0.6f);
// 生的苏式月饼
    public static final DeferredItem<Item> RAWSUZHOU_MOONCAKE = filledFood("rawsuzhou_mooncake", 1, 0.6f);
// 生的京式月饼
    public static final DeferredItem<Item> RAWBEIJING_MOONCAKE = filledFood("rawbeijing_mooncake", 1, 0.6f);
// 生的冰皮月饼
    public static final DeferredItem<Item> RAWSNOW_SKIN_MOONCAKE = filledFood("rawsnow_skin_mooncake", 1, 0.6f);
// 涂了油的小面皮
    public static final DeferredItem<Item> OILEDFLOURSKIN = food("oiledflourskin", 1, 0.6f);
// 压实的米浆
    public static final DeferredItem<Item> COMPACTEDRICEBRICK = food("compactedricebrick", 1, 0.6f);
// 意大利通心粉
    public static final DeferredItem<Item> MACARONI = food("macaroni", 1, 0.6f);
// 蒸米糕
    public static final DeferredItem<Item> STEAMEDRICEBRICK = food("steamedricebrick", 1, 0.6f);

// Fillings, pastes, and liquids

// 核桃碎
    public static final DeferredItem<Item> BROKENWALNUT = food("brokenwalnut", 1, 0.6f);
// 鸡蛋馅
    public static final DeferredItem<Item> EGGSTUFFING = food("eggstuffing", 1, 0.6f);
// 水果馅
    public static final DeferredItem<Item> FRUITFLAVOREDMOONCAKESTUFFING = food("fruitflavoredmooncakestuffing", 1, 0.6f);
// 芝麻花生丸
    public static final DeferredItem<Item> SESAMEANDPEANUTBALLS = food("sesameandpeanutballs", 1, 0.6f);
// 冬瓜馅
    public static final DeferredItem<Item> WAXGROUDPASTE = food("waxgroudpaste", 1, 0.6f);
// 红豆馅
    public static final DeferredItem<Item> REDBEANPASTE = food("redbeanpaste", 1, 0.6f);
// 椒盐馅
    public static final DeferredItem<Item> PEPPERANDSALTMASS = food("pepperandsaltmass", 1, 0.6f);
// 蔬菜肉馅
    public static final DeferredItem<Item> MEATANDVEGETABLESTUFFING = food("meatandvegetablestuffing", 1, 0.6f);
// 鸡蛋肉馅
    public static final DeferredItem<Item> MEATANDEGGPASTE = food("meatandeggpaste", 1, 0.6f);
// 肉馅
    public static final DeferredItem<Item> MEATPASTE = food("meatpaste", 1, 0.6f);
// 蔬菜馅
    public static final DeferredItem<Item> VEGETABLEPASTE = food("vegetablepaste", 1, 0.6f);
// 豌豆馅
    public static final DeferredItem<Item> PEASTUFFING = food("peastuffing", 1, 0.6f);
// 枣泥馅
    public static final DeferredItem<Item> JUJUBEPASTE = food("jujubepaste", 1, 0.6f);
// 纯净水
    public static final DeferredItem<Item> TIDYWATER = food("tidywater", 1, 0.6f);
// 蛋液
    public static final DeferredItem<Item> EGGLIQUID = food("eggliquid", 1, 0.6f);
// 花生酱
    public static final DeferredItem<Item> PEANUTJAM = food("peanutjam", 1, 0.6f);
// 鸡蛋面糊
    public static final DeferredItem<Item> EGGBATTER = food("eggbatter", 1, 0.6f);
// 可可酱
    public static final DeferredItem<Item> COCOASAUCE = food("cocoasauce", 1, 0.6f);
// 面糊
    public static final DeferredItem<Item> PANADA = food("panada", 1, 0.6f);
// 新鲜牛奶
    public static final DeferredItem<Item> MILKBOTTLE = food("milkbottle", 1, 0.6f);
// 奶油
    public static final DeferredItem<Item> CREAM = food("cream", 1, 0.6f);
// 浓缩糖浆
    public static final DeferredItem<Item> CONCENTRATEDSYRUP = food("concentratedsyrup", 1, 0.6f);
// 糯米面糊
    public static final DeferredItem<Item> GLUTINOUSRICEBATTER = food("glutinousricebatter", 1, 0.6f);
// 生豆浆
    public static final DeferredItem<Item> RAWSOYBEANMILK = food("rawsoybeanmilk", 1, 0.6f);
// 食用明胶
    public static final DeferredItem<Item> GELATIN = food("gelatin", 1, 0.6f);
// 甜蛋液
    public static final DeferredItem<Item> SWEETEGGLIQUID = food("sweeteggliquid", 1, 0.6f);
// 杂粮面糊
    public static final DeferredItem<Item> MULTIGRAINBATTER = food("multigrainbatter", 1, 0.6f);

// Remaining non-block item registrations migrated from FlavorImmersedDaily.

// 白菜
    public static final DeferredItem<Item> CHINESE_LEAVES = REGISTRY.register("chineseleaves",
            () -> new ChineseLeavesItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.3f)
                            .alwaysEdible()
                            .build())));

// 切好的白菜
    public static final DeferredItem<Item> CUT_CHINESE_CABBAGE = REGISTRY.register("cutchinesecabbage",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.1f)
                            .alwaysEdible()
                            .build())));

// 白萝卜
    public static final DeferredItem<Item> RADISH = REGISTRY.register("radish",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.3f)
                            .alwaysEdible()
                            .build())));

// 西蓝花
    public static final DeferredItem<Item> BROCCOIL = REGISTRY.register("broccoil", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

// 荞麦
    public static final DeferredItem<Item> BUCKWHEAT = REGISTRY.register("buckwheat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

// 卷心菜
    public static final DeferredItem<Item> CABBAGE = REGISTRY.register("cabbage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

// 木薯
    public static final DeferredItem<Item> CASSAVA = REGISTRY.register("cassava", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

// 芹菜
    public static final DeferredItem<Item> CELERY = REGISTRY.register("celery", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

// 韭菜
    public static final DeferredItem<Item> CHINESECHIVES = REGISTRY.register("chinesechives", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

// 山药
    public static final DeferredItem<Item> CHINESEYAM = REGISTRY.register("chineseyam", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

// 玉米
    public static final DeferredItem<Item> CORN = REGISTRY.register("corn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

// 孜然
    public static final DeferredItem<Item> CUMIN = REGISTRY.register("cumin", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

// 茴香
    public static final DeferredItem<Item> FENNEL = REGISTRY.register("fennel", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

// 大蒜
    public static final DeferredItem<Item> GARLIC = REGISTRY.register("garlic", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

// 生姜
    public static final DeferredItem<Item> GINGER = REGISTRY.register("ginger", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

// 青尖椒
    public static final DeferredItem<Item> GREENPEPPER = REGISTRY.register("greenpepper", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

// 秋葵
    public static final DeferredItem<Item> GUMBO = REGISTRY.register("gumbo", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

// 小米
    public static final DeferredItem<Item> MILLETGRAIN_GRAIN = REGISTRY.register("millet_grain", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

// 绿豆荚
    public static final DeferredItem<Item> MUNGBEAN = REGISTRY.register("mungbean", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

// 芥末
    public static final DeferredItem<Item> MUSTARD = REGISTRY.register("mustard", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

// 燕麦
    public static final DeferredItem<Item> OAT = REGISTRY.register("oat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

// 油菜
    public static final DeferredItem<Item> OILSEEDRAPE = REGISTRY.register("oilseedrape", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));

// 芦苇叶
    public static final DeferredItem<Item> REEDLEAF = REGISTRY.register("reedleaf", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.reedleaf_harvest"), ModItems::reedleafTooltipIcon));

// 生奥尔良腌制鸡翅
    public static final DeferredItem<Item> RAWORLEANSCHICKENWING = REGISTRY.register("raworleanschickenwing", () -> new Item(new Item.Properties()));

// 生奥尔良腌制鸡腿
    public static final DeferredItem<Item> RAWORLEANSCHICKENLEG = REGISTRY.register("raworleanschickenleg", () -> new Item(new Item.Properties()));

// 包子
    public static final DeferredItem<Item> COOKED_BAOZI = filledFood("cooked_baozi", 6, 0.6f);

// 雪花肥牛卷
    public static final DeferredItem<Item> FATBEEFROLL = food("fatbeefroll", 1, 0.6f);

// 羔羊嫩肉卷
    public static final DeferredItem<Item> LAMBROLL = food("lambroll", 1, 0.6f);

// 挂上面糊的蘑菇
    public static final DeferredItem<Item> MUSHROOMSWRAPPEDINBATTER = REGISTRY.register("mushroomswrappedinbatter", () -> new Item(new Item.Properties()));

// 挂糊裹粉的鸡翅
    public static final DeferredItem<Item> RAWCHICKENWINGWITHBATTER = REGISTRY.register("rawchickenwingwithbatter", () -> new Item(new Item.Properties()));

// 挂糊裹粉的鸡大腿
    public static final DeferredItem<Item> RAWCHICKENHALFLEGWITHBATTER = REGISTRY.register("rawchickenhalflegwithbatter", () -> new Item(new Item.Properties()));

// 挂糊裹粉的鸡胸肉
    public static final DeferredItem<Item> RAWCHICKENMEATWITHBATTER = REGISTRY.register("rawchickenmeatwithbatter", () -> new Item(new Item.Properties()));

// 灌好的肠
    public static final DeferredItem<Item> RAWSAUSAGE = food("rawsausage", 1, 0.6f);

// 裹了蛋液的生肉丸
    public static final DeferredItem<Item> RAWMEATBALLWITHEGGBALL = REGISTRY.register("rawmeatballwitheggball", () -> new Item(new Item.Properties()));

// 冷冻牛奶
    public static final DeferredItem<Item> FROZENMILK = REGISTRY.register("frozenmilk", () -> new Item(new Item.Properties()));

// 裹上面糊的鸡肉丁
    public static final DeferredItem<Item> RAWDICEDCHICKENWITHBATTER = REGISTRY.register("rawdicedchickenwithbatter", () -> new Item(new Item.Properties()));

// 馄饨
    public static final DeferredItem<Item> WONTON = filledFood("wonton", 6, 0.6f);

// 火锅肉卷
    public static final DeferredItem<Item> NORMALMEATROLL = food("normalmeatroll", 1, 0.6f);

// 焦糖玉米粒
    public static final DeferredItem<Item> CARAMELCORNKENNELS = REGISTRY.register("caramelcornkennels", () -> new Item(new Item.Properties()));

// 饺子
    public static final DeferredItem<Item> COOKEDDUMPLING = filledFood("cookeddumpling", 6, 0.6f);

// 咸蛋
    public static final DeferredItem<Item> SALTYEGG = REGISTRY.register("saltyegg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.5f).alwaysEdible().build())));

// 咸萝卜
    public static final DeferredItem<Item> SALTYRADDISH = REGISTRY.register("saltyraddish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.4f).alwaysEdible().build())));

// 汤圆
    public static final DeferredItem<Item> TANGYUAN = filledFood("tangyuan", 6, 0.6f);

// 千层豆腐皮
    public static final DeferredItem<Item> THOUSAND_LAYER_TOFU_SKIN = REGISTRY.register("thousand_layer_tofu_skin", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.5f).alwaysEdible().build())));

// 意大利面
    public static final DeferredItem<Item> SPAGHETTI = REGISTRY.register("spaghetti", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 奶油玉米粒
    public static final DeferredItem<Item> CREAMCORNKERNELS = REGISTRY.register("creamcornkernels", () -> new Item(new Item.Properties()));

// 巧克力玉米粒
    public static final DeferredItem<Item> CHOCOLATECORNKERNELS = REGISTRY.register("chocolatecornkernels", () -> new Item(new Item.Properties()));

// 生春卷
    public static final DeferredItem<Item> RAWSPRINGROLL = food("rawspringroll", 1, 0.6f);

// 生蛋挞
    public static final DeferredItem<Item> RAWEGGTART = food("raweggtart", 1, 0.6f);

// 生红豆蛋挞
    public static final DeferredItem<Item> SWEETREDBEANEGGTART = food("sweetredbeaneggtart", 1, 0.6f);

// 生辣条
    public static final DeferredItem<Item> RAWSPICYGLUTEN = food("rawspicygluten", 1, 0.6f);

// 生藕合
    public static final DeferredItem<Item> RAWCOUPLING = food("rawcoupling", 1, 0.6f);

// 生青色面团
    public static final DeferredItem<Item> RAWAIKUI = food("rawaikui", 1, 0.6f);

// 肉卷
    public static final DeferredItem<Item> MEATFLOURROOL = REGISTRY.register("meatflourrool", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 生肉松鸡蛋面剂子
    public static final DeferredItem<Item> RAWFLOURPASTEWITHDRIEDMEATFLOSS = REGISTRY.register("rawflourpastewithdriedmeatfloss", () -> new Item(new Item.Properties()));

// 生粽子
    public static final DeferredItem<Item> RAWAZONGZI = filledFood("rawazongzi", 1, 0.6f);

// 生汤圆
    public static final DeferredItem<Item> RAW_TANGYUAN = filledFood("raw_tangyuan", 1, 0.6f);

// 生铜锣烧
    public static final DeferredItem<Item> RAWDORAYAKI = food("rawdorayaki", 1, 0.6f);

// 生的撒芝麻糯米面剂子
    public static final DeferredItem<Item> RAWSESAMEGLUTINOUSPASTE = food("rawsesameglutinouspaste", 1, 0.6f);

// 刷了脆皮水的五花肉
    public static final DeferredItem<Item> CRISPYPORKBELLY = food("crispyporkbelly", 1, 0.6f);

// 填馅青椒
    public static final DeferredItem<Item> RAW_STUFFEDGREENPEPPER = food("raw_stuffedgreenpepper", 1, 0.6f);

// 盐腌鸡蛋
    public static final DeferredItem<Item> RAWSALTYEGG = food("rawsaltyegg", 1, 0.6f);

// 裹起来的牛奶条
    public static final DeferredItem<Item> RAWWRAPPEDMILK = food("rawwrappedmilk", 1, 0.6f);

// 调制小麦粉
    public static final DeferredItem<Item> MODULATEDWHEATFLOUR = REGISTRY.register("modulatedwheatflour", () -> new Item(new Item.Properties()));

// 核桃粉
    public static final DeferredItem<Item> WALNUTPOWDER = REGISTRY.register("walnutpowder", () -> new Item(new Item.Properties()));

// 红薯淀粉
    public static final DeferredItem<Item> SWEETPOTATSTARCH = REGISTRY.register("sweetpotatstarch", () -> new Item(new Item.Properties()));

// 花生粉
    public static final DeferredItem<Item> PEANUTPOWDER = REGISTRY.register("peanutpowder", () -> new Item(new Item.Properties()));

// 花生芝麻粉
    public static final DeferredItem<Item> PEANUTSESAMEPOWDER = REGISTRY.register("peanutsesamepowder", () -> new Item(new Item.Properties()));

// 咖啡粉
    public static final DeferredItem<Item> COFFEEPOWDER = REGISTRY.register("coffeepowder", () -> new Item(new Item.Properties()));

// 可可粉
    public static final DeferredItem<Item> COCOAPOWDER = REGISTRY.register("cocoapowder", () -> new Item(new Item.Properties()));

// 面粉
    public static final DeferredItem<Item> WHEATFLOUR = REGISTRY.register("wheatflour", () -> new Item(new Item.Properties()));

// 木薯粉
    public static final DeferredItem<Item> TAPIOCAFLOUR = REGISTRY.register("tapiocaflour", () -> new Item(new Item.Properties()));

// 糯米粉
    public static final DeferredItem<Item> GULTINOUSRICEPOWDER = REGISTRY.register("gultinousricepowder", () -> new Item(new Item.Properties()));

// 豌豆粉
    public static final DeferredItem<Item> PEAMEAL = REGISTRY.register("peameal", () -> new Item(new Item.Properties()));

// 芝麻粉
    public static final DeferredItem<Item> SESAMEPOWDER = REGISTRY.register("sesamepowder", () -> new Item(new Item.Properties()));

// 芝麻糯米粉
    public static final DeferredItem<Item> GULTINOUSRICESASAMEPOWDER = REGISTRY.register("gultinousricesasamepowder", () -> new Item(new Item.Properties()));

// 冰糖块
    public static final DeferredItem<Item> CRYSTALSUGAR = REGISTRY.register("crystalsugar", () -> new Item(new Item.Properties()));

// 食醋
    public static final DeferredItem<Item> VINEGAR = REGISTRY.register("vinegar", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.ACETIC_EROSION, () -> Config.aceticErosionEnabled));

// 豆瓣酱
    public static final DeferredItem<Item> THICKBROADBEANSAUCE = REGISTRY.register("thickbroadbeansauce", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.BEAN_FURY, () -> Config.beanFuryEnabled));

// 番茄火锅底料
    public static final DeferredItem<Item> TOMATO_HOT_POT_BASE = REGISTRY.register("tomato_hot_pot_base", () -> new Item(new Item.Properties()));

// 骨汤精华
    public static final DeferredItem<Item> BONESOUPESSENCE = REGISTRY.register("bonesoupessence", () -> new Item(new Item.Properties()));

// 红糖
    public static final DeferredItem<Item> BROWNSUGAR = REGISTRY.register("brownsugar", () -> new Item(new Item.Properties()));

// 黄油
    public static final DeferredItem<Item> BUTTER = REGISTRY.register("butter", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.BUTTER_PITCHER, () -> Config.butterPitcherEnabled));

// 酿造酱油
    public static final DeferredItem<Item> SOY = REGISTRY.register("soy", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.SOLAR_BREW, () -> Config.solarBrewEnabled));

// 咖喱酱汁
    public static final DeferredItem<Item> CURRY = REGISTRY.register("curry", () -> new Item(new Item.Properties()));

// 卤料包
    public static final DeferredItem<Item> HALOGENBAG = REGISTRY.register("halogenbag", () -> new Item(new Item.Properties()));

// 麻辣火锅底料
    public static final DeferredItem<Item> SPICY_HOT_POT_BASE = REGISTRY.register("spicy_hot_pot_base", () -> new Item(new Item.Properties()));

// 麻汁
    public static final DeferredItem<Item> MAJUICE = REGISTRY.register("majuice", () -> new Item(new Item.Properties()));

// 清糖浆
    public static final DeferredItem<Item> WHITESUGARSYRUP = REGISTRY.register("whitesugarsyrup", () -> new Item(new Item.Properties()));

// 食用油
    public static final DeferredItem<Item> COOKINGOIL = REGISTRY.register("cookingoil", () -> new Item(new Item.Properties()));

// 藤椒火锅底料
    public static final DeferredItem<Item> PEPPER_HOT_POT_BASE = REGISTRY.register("pepper_hot_pot_base", () -> new Item(new Item.Properties()));

// 甜面酱
    public static final DeferredItem<Item> SWEETFLOURASUVE = REGISTRY.register("sweetflourasuve", () -> new Item(new Item.Properties()));

// 香油
    public static final DeferredItem<Item> SESAMEOIL = REGISTRY.register("sesameoil", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.SESAME_SLIP, () -> Config.sesameSlipEnabled));

// 食用盐
    public static final DeferredItem<Item> SALT = REGISTRY.register("salt", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.FLAVOR_BASE, () -> Config.flavorBaseEnabled));

// 盐矿渣
    public static final DeferredItem<Item> SALTPIECE = REGISTRY.register("saltpiece", () -> new Item(new Item.Properties()));

// 奥尔良粉
    public static final DeferredItem<Item> ORLEANSPOWDER = REGISTRY.register("orleanspowder", () -> new Item(new Item.Properties()));

// 八角粉
    public static final DeferredItem<Item> ANISEEDPOWDER = REGISTRY.register("aniseedpowder", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.ANISEED_WARD, () -> Config.aniseedWardEnabled));

// 葱粉
    public static final DeferredItem<Item> ONIONPOWDER = REGISTRY.register("onionpowder", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.HULK_LEEK, () -> Config.hulkLeekEnabled));

// 丁香粉
    public static final DeferredItem<Item> LILACPOWDER = REGISTRY.register("lilacpowder", () -> new Item(new Item.Properties()));

// 桂皮粉
    public static final DeferredItem<Item> CINNAMONPOWDER = REGISTRY.register("cinnamonpowder", () -> new Item(new Item.Properties()));

// 红茶粉
    public static final DeferredItem<Item> REDTEAPOWDER = REGISTRY.register("redteapowder", () -> new Item(new Item.Properties()));

// 花椒粉
    public static final DeferredItem<Item> CHINESEPICKLYASHPOWDER = REGISTRY.register("chinesepicklyashpowder", () -> new Item(new Item.Properties()));

// 茴香粉
    public static final DeferredItem<Item> FENNELPOWDER = REGISTRY.register("fennelpowder", () -> new Item(new Item.Properties()));

// 姜粉
    public static final DeferredItem<Item> GROUNDPOWDER = REGISTRY.register("groundpowder", () -> new Item(new Item.Properties()));

// 椒盐粉
    public static final DeferredItem<Item> PEPPEREDSALT = REGISTRY.register("pepperedsalt", () -> new Item(new Item.Properties()));

// 辣椒粉
    public static final DeferredItem<Item> CHILLIPOWDER = REGISTRY.register("chillipowder", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.FURY_ASSAULT, () -> Config.furyAssaultEnabled));

// 绿茶粉
    public static final DeferredItem<Item> GREENTEAPOWDER = REGISTRY.register("greenteapowder", () -> new Item(new Item.Properties()));

// 蒜粉
    public static final DeferredItem<Item> GARLICPOWDER = REGISTRY.register("garlicpowder", () -> new Item(new Item.Properties()));

// 五香粉
    public static final DeferredItem<Item> FIVESPICEPOWDER = REGISTRY.register("fivespicepowder", () -> new Item(new Item.Properties()));

// 洋葱粉
    public static final DeferredItem<Item> ONIONPOWDER_2 = REGISTRY.register("onionpowder_2", () -> new Item(new Item.Properties()));

// 孜然粉
    public static final DeferredItem<Item> CUMINPOWDER = REGISTRY.register("cuminpowder", () -> new Item(new Item.Properties()));

// 死牛
    public static final DeferredItem<Item> DEADCATTLE = REGISTRY.register("deadcattle", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.dead_animal_harvest"), ModItems::deadAnimalIcons));

// 死羊
    public static final DeferredItem<Item> DEADSHEEP = REGISTRY.register("deadsheep", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.dead_animal_harvest"), ModItems::deadAnimalIcons));

// 死猪
    public static final DeferredItem<Item> DEADPIG = REGISTRY.register("deadpig", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.dead_animal_harvest"), ModItems::deadAnimalIcons));

// 死鸡
    public static final DeferredItem<Item> DEADCHICKEN = REGISTRY.register("deadchicken", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.dead_animal_harvest"), ModItems::deadAnimalIcons));

// 拔好毛的鸡
    public static final DeferredItem<Item> CHICKENWITHOUTFEATHER = REGISTRY.register("chickenwithoutfeather",
            () -> new TooltipItem(new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.chicken_without_feather"), () -> java.util.List.of()));

// 放血后的鸡
    public static final DeferredItem<Item> CHICKENWITHOUTBLOOD = REGISTRY.register("chickenwithoutblood",
            () -> new TooltipItem(new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.chicken_without_blood"), ModItems::chickenBloodIcons));

// 切割刀
    public static final DeferredItem<Item> SHARPKNIFE = REGISTRY.register("sharpknife", () -> new Item(new Item.Properties()));

// 宽刃刀
    public static final DeferredItem<Item> WIDEEDGEDKNIFE = REGISTRY.register("wideedgedknife", () -> new Item(new Item.Properties()));

// 创造模式标签页图标（贴图 item/all.png，只做图标，不进任何创造栏）
    public static final DeferredItem<Item> ALL = item("all");

// 斩骨刀
    public static final DeferredItem<Item> BONECUTTERKNIFE = REGISTRY.register("bonecutterknife", () -> new Item(new Item.Properties()));

// 菜刀
    public static final DeferredItem<Item> KITCHENKNIFE = REGISTRY.register("kitchenknife", () -> new Item(new Item.Properties()));

// 除草剪刀
    public static final DeferredItem<Item> KITCHENSCISSOR = REGISTRY.register("kitchenscissor", () -> new KitchenScissorsItem(new Item.Properties()));

// 锅铲
    public static final DeferredItem<Item> SPATULA = REGISTRY.register("spatula", () -> new Item(new Item.Properties()));

// 木槌
    public static final DeferredItem<Item> MALLET = REGISTRY.register("mallet", () -> new CoarseClothItem(new Item.Properties()));

// 滤布
    public static final DeferredItem<Item> COARSECLOTH = REGISTRY.register("coarsecloth", () -> new CoarseClothItem(new Item.Properties()));

// 月饼模具
    public static final DeferredItem<Item> MOONCAKEMOLD = REGISTRY.register("mooncakemold", () -> new MooncakeMoldItem(new Item.Properties()));

// 擀面杖
    public static final DeferredItem<Item> ROLLINGPIN = REGISTRY.register("rollingpin", () -> new Item(new Item.Properties()));

// 吴刚之斧（性能等同钻石斧，天生自带 3 级无限砍伐）
    public static final DeferredItem<Item> WUGANG_AXE = REGISTRY.register("wugang_axe",
            () -> new WugangAxeItem(new Item.Properties()
                    .attributes(AxeItem.createAttributes(Tiers.DIAMOND, 5.0F, -3.0F))));

// 鸡蛋仔模具
    public static final DeferredItem<Item> EEGGPUFFSMOULD = REGISTRY.register("eeggpuffsmould", () -> new CoarseClothItem(new Item.Properties()));

// 皮蛋块
    public static final DeferredItem<Item> DIECEDPRESERVEDEGG = REGISTRY.register("diecedpreservedegg", () -> new Item(new Item.Properties()));

// 皮蛋丁
    public static final DeferredItem<Item> CUTDIECEDPRESERVEEGG = REGISTRY.register("cutdiecedpreserveegg", () -> new Item(new Item.Properties()));

// 红薯块
    public static final DeferredItem<Item> DICEDSWEETPOTATO = REGISTRY.register("dicedsweetpotato", () -> new Item(new Item.Properties()));

// 紫薯块
    public static final DeferredItem<Item> PURPLEDICEDSWEETPOTATO = REGISTRY.register("purpledicedsweetpotato", () -> new Item(new Item.Properties()));

// 处理后的卷心菜
    public static final DeferredItem<Item> PROCESSEDCABBAGE = REGISTRY.register("processedcabbage", () -> new Item(new Item.Properties()));

// 切块西红柿
    public static final DeferredItem<Item> DICEDTOMATO = REGISTRY.register("dicedtomato", () -> new Item(new Item.Properties()));

// 切好的卷心菜
    public static final DeferredItem<Item> CUTCABBAGE = REGISTRY.register("cutcabbage", () -> new Item(new Item.Properties()));

// 木薯块
    public static final DeferredItem<Item> CASSAVACHUNKS = REGISTRY.register("cassavachunks", () -> new Item(new Item.Properties()));

// 西葫芦切片
    public static final DeferredItem<Item> SLICEDZUCCHINI = REGISTRY.register("slicedzucchini", () -> new Item(new Item.Properties()));

// 胡萝卜块
    public static final DeferredItem<Item> CARROTCHUNKS = REGISTRY.register("carrotchunks", () -> new Item(new Item.Properties()));

// 芹菜叶
    public static final DeferredItem<Item> CELERYLEAF = REGISTRY.register("celeryleaf", () -> new Item(new Item.Properties()));

// 豌豆泥
    public static final DeferredItem<Item> PEAPASTE = REGISTRY.register("peapaste", () -> new Item(new Item.Properties()));

// 芝麻糯米丸
    public static final DeferredItem<Item> SESAMEGLUTINOUSRICEBALLS = REGISTRY.register("sesameglutinousriceballs", () -> new Item(new Item.Properties()));

// 肉蛋馅
    public static final DeferredItem<Item> MEATANDEGGFILLING = REGISTRY.register("meatandeggfilling", () -> new Item(new Item.Properties()));

// 肉菜馅
    public static final DeferredItem<Item> MEATANDVEGETABLEFILLING = REGISTRY.register("meatandvegetablefilling", () -> new Item(new Item.Properties()));

// 绿豆泥
    public static final DeferredItem<Item> MUNGBEANPASTE = REGISTRY.register("mungbeanpaste", () -> new Item(new Item.Properties()));

// 冬瓜泥
    public static final DeferredItem<Item> WAXGOURDPASTE = REGISTRY.register("waxgourdpaste", () -> new Item(new Item.Properties()));

// 不太常见的馅料
    public static final DeferredItem<Item> UNCOMMONSTUFFING = REGISTRY.register("uncommonstuffing", () -> new Item(new Item.Properties()));

// 菜蛋馅
    public static final DeferredItem<Item> VEGETABLEANDEGGSTUFFING = REGISTRY.register("vegetableandeggstuffing", () -> new Item(new Item.Properties()));

// 糖浆
    public static final DeferredItem<Item> BROWNSUGARSYRUP = REGISTRY.register("brownsugarsyrup", () -> new SeasoningItem(
            new Item.Properties(), () -> ModEffects.CRIMSON_MAMBA, () -> true));

// 盐水
    public static final DeferredItem<Item> SALTYWATER = REGISTRY.register("saltywater", () -> new Item(new Item.Properties()));

// 可可浆
    public static final DeferredItem<Item> COCOAMASS = REGISTRY.register("cocoamass", () -> new Item(new Item.Properties()));

// 裹了蛋液的肉丸
    public static final DeferredItem<Item> EGGCOATEDMEATBALLS = REGISTRY.register("eggcoatedmeatballs", () -> new Item(new Item.Properties()));

// 裹上面糊的鸡胸块
    public static final DeferredItem<Item> BATTEREDCHICKENBREASTCHUNKS = REGISTRY.register("batteredchickenbreastchunks", () -> new Item(new Item.Properties()));

// 生肉松面剂子
    public static final DeferredItem<Item> RAWMEATFLOSSDOUGH = REGISTRY.register("rawmeatflossdough", () -> new Item(new Item.Properties()));

// 挂糊的蘑菇
    public static final DeferredItem<Item> BATTEREDMUSHROOMS = REGISTRY.register("batteredmushrooms", () -> new Item(new Item.Properties()));

// 切开的面饼
    public static final DeferredItem<Item> CUTPANCAKE = REGISTRY.register("cutpancake", () -> new Item(new Item.Properties()));

// 发面剂子
    public static final DeferredItem<Item> FERMENTEDFLOURPASTE = REGISTRY.register("fermentedflourpaste", () -> new Item(new Item.Properties()));

// 切开的鸡蛋面饼
    public static final DeferredItem<Item> CUTEGGPANCAKE = REGISTRY.register("cuteggpancake", () -> new Item(new Item.Properties()));

// 滤布裹起来的米浆
    public static final DeferredItem<Item> RICESLURRYWRAPPEDINFILTERCLOTH = REGISTRY.register("riceslurrywrappedinfiltercloth", () -> new Item(new Item.Properties()));

// 捶打后的糯米剂子
    public static final DeferredItem<Item> POUNDEDGLUTINOUSPASTE = REGISTRY.register("poundedglutinouspaste", () -> new Item(new Item.Properties()));

// 长剂子
    public static final DeferredItem<Item> LONGDOUGH = REGISTRY.register("longdough", () -> new Item(new Item.Properties()));

// 长鸡蛋面饼
    public static final DeferredItem<Item> LONGEGGPANCAKE = REGISTRY.register("longeggpancake", () -> new Item(new Item.Properties()));

// 大豆蛋白
    public static final DeferredItem<Item> SOYBEANPROTEIN = REGISTRY.register("soybeanprotein", () -> new Item(new Item.Properties()));

// 生猪杂
    public static final DeferredItem<Item> RAW_PIGOFFAL = REGISTRY.register("raw_pigoffal", () -> new Item(new Item.Properties()));

// 脱骨的鸡爪
    public static final DeferredItem<Item> DEBONEDCHICKENFEET = REGISTRY.register("debonedchickenfeet", () -> new Item(new Item.Properties()));

// 砂砾裹起来的蛋
    public static final DeferredItem<Item> EGGWRAPPEDINGRAVEL = REGISTRY.register("eggwrappedingravel", () -> new Item(new Item.Properties()));

// 豌豆粉
    public static final DeferredItem<Item> PEAFLOUR = REGISTRY.register("peaflour", () -> new Item(new Item.Properties()));

// 五角杨桃的乐趣
    public static final DeferredItem<Item> FIVEPOINTEDCARAMBOLADELIGHT = REGISTRY.register("fivepointedcaramboladelight", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 冰糖熟梨
    public static final DeferredItem<Item> RIPEPEARWITHROCKSUGAR = REGISTRY.register("ripepearwithrocksugar", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 冰糖葫芦
    public static final DeferredItem<Item> SUGARCOATEDHAWS = REGISTRY.register("sugarcoatedhaws", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 卤猪肘子
    public static final DeferredItem<Item> STEWEDPIGLEG = REGISTRY.register("stewedpigleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

// 卤鸡大腿
    public static final DeferredItem<Item> STEWEDHALFCHICKENLEG = REGISTRY.register("stewedhalfchickenleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

// 卤鸡心
    public static final DeferredItem<Item> STEWEDCHICKENHEART = REGISTRY.register("stewedchickenheart", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

// 卤鸡肝
    public static final DeferredItem<Item> STEWEDCHICKENLIVERS = REGISTRY.register("stewedchickenlivers", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

// 卤鸡腿
    public static final DeferredItem<Item> STEWEDCHICKENLEG = REGISTRY.register("stewedchickenleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

// 酱牛肉
    public static final DeferredItem<Item> SAUCINGBEEF = REGISTRY.register("saucingbeef", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

// 原味酸奶
    public static final DeferredItem<Item> YOGURT = REGISTRY.register("yogurt", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 咸蛋黄
    public static final DeferredItem<Item> SALTEGGYOLK = REGISTRY.register("salteggyolk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 咸豆腐脑
    public static final DeferredItem<Item> SALTTOFUCURD = REGISTRY.register("salttofucurd", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 哈密瓜片
    public static final DeferredItem<Item> DICEDHAMIMELON = REGISTRY.register("dicedhamimelon", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 响铃卷
    public static final DeferredItem<Item> FRIED_TOFU_SKIN_ROLLS = REGISTRY.register("fried_tofu_skin_rolls", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 奶油爆米花
    public static final DeferredItem<Item> CREAMPOPCORN = REGISTRY.register("creampopcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

// 山药蘸白糖
    public static final DeferredItem<Item> CHINESEYAMANDSUGAR = REGISTRY.register("chineseyamandsugar", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 巧克力爆米花
    public static final DeferredItem<Item> CHOCOLATEPOPCORN = REGISTRY.register("chocolatepopcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 巧克力豆
    public static final DeferredItem<Item> CHOCOLATEBEAN = REGISTRY.register("chocolatebean", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 年糕
    public static final DeferredItem<Item> NEWYEARCAKE = REGISTRY.register("newyearcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 无骨柠檬凤爪
    public static final DeferredItem<Item> BONELESSLEMONCHICKENFEET = REGISTRY.register("bonelesslemonchickenfeet", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

// 水煮玉米
    public static final DeferredItem<Item> BOILEDCORN = REGISTRY.register("boiledcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

// 泡椒凤爪
    public static final DeferredItem<Item> CHICKENFEETWITHPEPPERS = REGISTRY.register("chickenfeetwithpeppers", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 上校鸡块
    public static final DeferredItem<Item> COLONELCHICKENNUGGETS = REGISTRY.register("colonelchickennuggets", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 油炸糯米条
    public static final DeferredItem<Item> FRIEDGULTINOUSRICESTRIPS = REGISTRY.register("friedgultinousricestrips", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 油炸花生米
    public static final DeferredItem<Item> FRIEDPEANUTS = REGISTRY.register("friedpeanuts", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸干蘑
    public static final DeferredItem<Item> FRIEDMUSHROOM = REGISTRY.register("friedmushroom", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸年糕
    public static final DeferredItem<Item> DRIEDRICECAKE = REGISTRY.register("driedricecake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸春卷
    public static final DeferredItem<Item> FIREDSPRINGROLL = REGISTRY.register("firedspringroll", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸油条
    public static final DeferredItem<Item> FRIEDDOUGHSTICK = REGISTRY.register("frieddoughstick", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸牛奶
    public static final DeferredItem<Item> DRIEDMILK = REGISTRY.register("driedmilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸肉丸
    public static final DeferredItem<Item> DRIEDMEATBALL = REGISTRY.register("driedmeatball", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸茄子片
    public static final DeferredItem<Item> DRIEDDICEDAUBERGINE = REGISTRY.register("drieddicedaubergine", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸薄脆
    public static final DeferredItem<Item> FRIEDGIZZARD = REGISTRY.register("friedgizzard", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸薯条
    public static final DeferredItem<Item> FRENCHFRIES = REGISTRY.register("frenchfries", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸藕合
    public static final DeferredItem<Item> DRIEDLOTUSROOT = REGISTRY.register("driedlotusroot", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸豆干
    public static final DeferredItem<Item> FRIEDDRIEDTOUFU = REGISTRY.register("frieddriedtoufu", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸豆腐
    public static final DeferredItem<Item> FRIEDTOUFU = REGISTRY.register("friedtoufu", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸面包
    public static final DeferredItem<Item> DRIEDBREAD = REGISTRY.register("driedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸面包片
    public static final DeferredItem<Item> DRIEDDICEDBREAD = REGISTRY.register("drieddicedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸馒头片
    public static final DeferredItem<Item> DRIEDDICEDSTEAMEDBREAD = REGISTRY.register("drieddicedsteamedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸馓子
    public static final DeferredItem<Item> FRIEDSANZI = REGISTRY.register("friedsanzi", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸麻团
    public static final DeferredItem<Item> FRIEDHEMPBALL = REGISTRY.register("friedhempball", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 炸麻花
    public static final DeferredItem<Item> FRIEDDOUGHTWIST = REGISTRY.register("frieddoughtwist", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 脆皮手枪腿
    public static final DeferredItem<Item> CRISPYPISTOLLEG = REGISTRY.register("crispypistolleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 烟熏鸡胸肉
    public static final DeferredItem<Item> SOMKEDCHICKENBREAST = REGISTRY.register("somkedchickenbreast", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

// 烤紫薯
    public static final DeferredItem<Item> ROASTEDPURPLEPOTATO = REGISTRY.register("roastedpurplepotato", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 烤花生
    public static final DeferredItem<Item> ROASTEDPEANUT = REGISTRY.register("roastedpeanut", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 奥尔良烤翅
    public static final DeferredItem<Item> ORLEANWING = REGISTRY.register("orleanwing", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));

// 奥尔良烤鸡腿
    public static final DeferredItem<Item> ORLEANLEG = REGISTRY.register("orleanleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));

// 烤蘑菇
    public static final DeferredItem<Item> ROASTEDMUSHROOM = REGISTRY.register("roastedmushroom", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));

// 烤金针菇
    public static final DeferredItem<Item> ROASTED_FLAMMULINAVELUTIPES = REGISTRY.register("roasted_flammulinavelutipes", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));

// 烤韭菜
    public static final DeferredItem<Item> ROASTEDCHINESECHIVES = REGISTRY.register("roastedchinesechives", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));

// 烤鱼片
    public static final DeferredItem<Item> ROASTEDDICEDFISH = REGISTRY.register("roasteddicedfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));

// 焦糖爆米花
    public static final DeferredItem<Item> CARAMELPOPCORN = REGISTRY.register("caramelpopcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 煮山药
    public static final DeferredItem<Item> COOKEDCHINESEYAM = REGISTRY.register("cookedchineseyam", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 煮粉丝
    public static final DeferredItem<Item> COOKEDVERMICELLI_0 = REGISTRY.register("cookedvermicelli_0", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 熟山楂
    public static final DeferredItem<Item> COOKEDHAWTHORN = REGISTRY.register("cookedhawthorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 爆米花
    public static final DeferredItem<Item> POPCORN = REGISTRY.register("popcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 甜豆腐脑
    public static final DeferredItem<Item> SWEETTOUFUCURD = REGISTRY.register("sweettoufucurd", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 白糖果冻
    public static final DeferredItem<Item> JELLY = REGISTRY.register("jelly", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 皮蛋
    public static final DeferredItem<Item> COOKEDPRESERVEDEGG = REGISTRY.register("cookedpreservedegg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

// 熟粽子
    public static final DeferredItem<Item> COOKEDZONGZI = filledFood("cookedzongzi", 9, 0.6f);

// 熟糯米团
    public static final DeferredItem<Item> COOKEDGLUTINOUSRICE = REGISTRY.register("cookedglutinousrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 糍粑
    public static final DeferredItem<Item> COOKEDGLUTINOUSRICEPOUNDEDINTOPASTE = REGISTRY.register("cookedglutinousricepoundedintopaste", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 艾糍
    public static final DeferredItem<Item> AIKUI = REGISTRY.register("aikui", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 糖渍小枣
    public static final DeferredItem<Item> TANG_ZI_XIAO_ZAO = REGISTRY.register("tang_zi_xiao_zao", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 糖霜核桃
    public static final DeferredItem<Item> SUGARCOATEDWALNUT = REGISTRY.register("sugarcoatedwalnut", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 切酸菜
    public static final DeferredItem<Item> DICEDPICKLEDVEGETABLE = REGISTRY.register("dicedpickledvegetable", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 腊肉
    public static final DeferredItem<Item> CUREDMEAT = REGISTRY.register("curedmeat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 腊肠
    public static final DeferredItem<Item> CUREDSAUSAGE = REGISTRY.register("curedsausage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 腌黄瓜片
    public static final DeferredItem<Item> SALTYDICEDCURUMBER = REGISTRY.register("saltydicedcurumber", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 酸菜
    public static final DeferredItem<Item> PICKLEDVEGETABLE = REGISTRY.register("pickledvegetable", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 黄瓜丝咸菜
    public static final DeferredItem<Item> SLICEDSALTYCUCUMBER = REGISTRY.register("slicedsaltycucumber", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 蒸血糕
    public static final DeferredItem<Item> STEAMEDBLOOD = REGISTRY.register("steamedblood", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 虎皮青椒
    public static final DeferredItem<Item> TIGERGREENPEPPER = REGISTRY.register("tigergreenpepper", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 豆浆
    public static final DeferredItem<Item> SOYAMILK = REGISTRY.register("soyamilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

// 豆腐块
    public static final DeferredItem<Item> PICEDTOUFU = REGISTRY.register("picedtoufu", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 豆腐干
    public static final DeferredItem<Item> DRIEDTOUFU = REGISTRY.register("driedtoufu", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 豆腐泡
    public static final DeferredItem<Item> TOFU_PUFFS = REGISTRY.register("tofu_puffs", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 豆腐脑
    public static final DeferredItem<Item> TOFUCURD = REGISTRY.register("tofucurd", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 辣条
    public static final DeferredItem<Item> CHINESESPICYSNACKFOOD = REGISTRY.register("chinesespicysnackfood", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 酒鬼麻辣花生
    public static final DeferredItem<Item> SPICYPEANUT = REGISTRY.register("spicypeanut", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 酸辣粉
    public static final DeferredItem<Item> HOTANDSOURRICENOODLES = REGISTRY.register("hotandsourricenoodles", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 金米糕
    public static final DeferredItem<Item> GOLDRICECAKE = REGISTRY.register("goldricecake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 金葡萄
    public static final DeferredItem<Item> GOLDENGRAPE = REGISTRY.register("goldengrape", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));

// 切面包片
    public static final DeferredItem<Item> DICEDBREAD = REGISTRY.register("dicedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 干面条
    public static final DeferredItem<Item> DRYCOOKEDNOODLES = REGISTRY.register("drycookednoodles", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 挂面
    public static final DeferredItem<Item> COOKEDVERMICELLI = REGISTRY.register("cookedvermicelli", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 月饼
    public static final DeferredItem<Item> MOONCAKE = filledFood("mooncake", 9, 0.6f);
// 广式月饼 — 团团圆圆：附近玩家获得生命恢复
    public static final DeferredItem<Item> CANTONESE_MOONCAKE = mooncakeFood("cantonese_mooncake", 9, 0.0f,
            () -> ModEffects.REUNION_RECOVERY, () -> Config.reunionRecoveryEnabled);
// 苏式月饼 — 酥到掉渣：受击时掉落月饼渣并吸引敌对生物
    public static final DeferredItem<Item> SUZHOU_MOONCAKE = mooncakeFood("suzhou_mooncake", 9, 0.0f,
            () -> ModEffects.CRUMBLY_CRUST, () -> Config.crumblyCrustEnabled);
// 京式月饼 — 蔽日食月：攻击选定对战目标，恢复体力时对其造成等量伤害
    public static final DeferredItem<Item> BEIJING_MOONCAKE = mooncakeFood("beijing_mooncake", 9, 0.0f,
            () -> ModEffects.ECLIPSE_DEVOUR, () -> Config.eclipseDevourEnabled);
// 冰皮月饼 — 清凉冰皮：免疫火焰伤害并强化对火免敌对生物的近战伤害
    public static final DeferredItem<Item> SNOW_SKIN_MOONCAKE = mooncakeFood("snow_skin_mooncake", 9, 0.0f,
            () -> ModEffects.COOL_SKIN, () -> Config.coolSkinEnabled);

// 杂粮煎饼
    public static final DeferredItem<Item> GRAINSPANCAKE = REGISTRY.register("grainspancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 汉堡包
    public static final DeferredItem<Item> HAMBURGERBREAD = REGISTRY.register("hamburgerbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 沂蒙煎饼
    public static final DeferredItem<Item> YIMENGPANCAKES = REGISTRY.register("yimengpancakes", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 沙琪玛
    public static final DeferredItem<Item> SACHIMA = REGISTRY.register("sachima", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 烧麦
    public static final DeferredItem<Item> SHUMAI = filledFood("shumai", 9, 0.6f);

// 热狗
    public static final DeferredItem<Item> HOTDOG = REGISTRY.register("hotdog", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 糯米饼
    public static final DeferredItem<Item> STEAMEDGLUTINOUSRICECAKE = REGISTRY.register("steamedglutinousricecake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 脆皮鸡蛋小蛋糕
    public static final DeferredItem<Item> CRISPYEGGCAKE = REGISTRY.register("crispyeggcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 花卷
    public static final DeferredItem<Item> STEAMEDTWISTEDROLL = REGISTRY.register("steamedtwistedroll", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 葱油烙饼
    public static final DeferredItem<Item> SCALLIONOILPANCAKE = REGISTRY.register("scallionoilpancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 面条
    public static final DeferredItem<Item> NOODLES = REGISTRY.register("noodles", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 馅饼
    public static final DeferredItem<Item> PIE = filledFood("pie", 9, 0.6f);

// 馒头
    public static final DeferredItem<Item> STEAMEDBREAD = REGISTRY.register("steamedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 馒头片
    public static final DeferredItem<Item> DICEDSTEAMEDBREAD = REGISTRY.register("dicedsteamedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 鸡蛋仔
    public static final DeferredItem<Item> EGGPUFFS = REGISTRY.register("eggpuffs", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 鸡蛋挂面
    public static final DeferredItem<Item> EGGVERMICELLI = REGISTRY.register("eggvermicelli", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));

// 香肠
    public static final DeferredItem<Item> COOKEDSAUSAGE = REGISTRY.register("cookedsausage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 马丞相的乐趣
    public static final DeferredItem<Item> M_SECRETDELIGHT = REGISTRY.register("m_secretdelight", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 鱼片刺身
    public static final DeferredItem<Item> FISHSASHIMI = REGISTRY.register("fishsashimi", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

// 生牛血
    public static final DeferredItem<Item> RAWCATTLEBLOOD = REGISTRY.register("rawcattleblood",
            () -> new Item(new Item.Properties()));

// 生牛脸
    public static final DeferredItem<Item> RAWCATTLEFACE = REGISTRY.register("rawcattleface",
            () -> new Item(new Item.Properties()));

// 生牛筋
    public static final DeferredItem<Item> RAWCATTLEJOINT = REGISTRY.register("rawcattlejoint",
            () -> new Item(new Item.Properties()));

// 生牛肚
    public static final DeferredItem<Item> RAWCATTLESTOMACH = REGISTRY.register("rawcattlestomach",
            () -> new Item(new Item.Properties()));

// 生牛肺
    public static final DeferredItem<Item> RAWCATTLELUNG = REGISTRY.register("rawcattlelung",
            () -> new Item(new Item.Properties()));

// 生牛肝
    public static final DeferredItem<Item> RAWCATTLELIVER = REGISTRY.register("rawcattleliver",
            () -> new Item(new Item.Properties()));

// 生牛肠
    public static final DeferredItem<Item> RAWCATTLEINTESTINE = REGISTRY.register("rawcattleintestine",
            () -> new Item(new Item.Properties()));

// 生牛心
    public static final DeferredItem<Item> RAWCATTLEHEART = REGISTRY.register("rawcattleheart",
            () -> new Item(new Item.Properties()));

// 牛油
    public static final DeferredItem<Item> RAWCATTLEFAT = REGISTRY.register("rawcattlefat",
            () -> new Item(new Item.Properties()));

// 生牛肉片
    public static final DeferredItem<Item> RAWDICEDCATTLEMEAT = REGISTRY.register("rawdicedcattlemeat",
            () -> new Item(new Item.Properties()));

// 生牛雪花
    public static final DeferredItem<Item> RAWSNOWFLAKEBEEF = REGISTRY.register("rawsnowflakebeef",
            () -> new Item(new Item.Properties()));

// 生牛肘
    public static final DeferredItem<Item> RAWCATTLELEG = REGISTRY.register("rawcattleleg",
            () -> new Item(new Item.Properties()));

// 生牛蹄
    public static final DeferredItem<Item> RAWCATTLEFEET = REGISTRY.register("rawcattlefeet",
            () -> new Item(new Item.Properties()));

// 生牛腱子肉
    public static final DeferredItem<Item> RAWCATTLETENDON = REGISTRY.register("rawcattletendon",
            () -> new Item(new Item.Properties()));

// 牛角
    public static final DeferredItem<Item> BULLHORN = REGISTRY.register("bullhorn",
            () -> new Item(new Item.Properties()));

// 牛大骨
    public static final DeferredItem<Item> BOVINEBONE = REGISTRY.register("bovinebone",
            () -> new Item(new Item.Properties()));

// 动物头骨
    public static final DeferredItem<Item> ANIMALSKULL = REGISTRY.register("animalskull",
            () -> new Item(new Item.Properties()));

// 生猪血
    public static final DeferredItem<Item> RAWPIGBLOOD = REGISTRY.register("rawpigblood",
            () -> new Item(new Item.Properties()));

// 生猪肉片
    public static final DeferredItem<Item> RAWDICEDPIGMEAT = REGISTRY.register("rawdicedpigmeat",
            () -> new Item(new Item.Properties()));

// 生猪肉丝
    public static final DeferredItem<Item> RAWDICEDPIGMEAT_2 = REGISTRY.register("rawdicedpigmeat_2",
            () -> new Item(new Item.Properties()));

// 生猪头肉
    public static final DeferredItem<Item> RAWPIGHEAD = REGISTRY.register("rawpighead",
            () -> new Item(new Item.Properties()));

// 生猪耳朵
    public static final DeferredItem<Item> RAWPIGEAR = REGISTRY.register("rawpigear",
            () -> new Item(new Item.Properties()));

// 生猪拱嘴
    public static final DeferredItem<Item> RAWPIGNOSE = REGISTRY.register("rawpignose",
            () -> new Item(new Item.Properties()));

// 生猪尾巴
    public static final DeferredItem<Item> RAWPIGTAIL = REGISTRY.register("rawpigtail",
            () -> new Item(new Item.Properties()));

// 生猪皮
    public static final DeferredItem<Item> RAWPIGSKIN = REGISTRY.register("rawpigskin",
            () -> new Item(new Item.Properties()));

// 生猪五花肉
    public static final DeferredItem<Item> RAWPIGSTREAKYPORK = REGISTRY.register("rawpigstreakypork",
            () -> new Item(new Item.Properties()));

// 生猪里脊肉
    public static final DeferredItem<Item> RAWPIGTENDERLOIN = REGISTRY.register("rawpigtenderloin",
            () -> new Item(new Item.Properties()));

// 生猪排骨
    public static final DeferredItem<Item> RAWPIGSPARERIB = REGISTRY.register("rawpigsparerib",
            () -> new Item(new Item.Properties()));

// 生猪肘子
    public static final DeferredItem<Item> RAWPIGLEG = REGISTRY.register("rawpigleg",
            () -> new Item(new Item.Properties()));

// 生猪蹄
    public static final DeferredItem<Item> RAWPIGFEET = REGISTRY.register("rawpigfeet",
            () -> new Item(new Item.Properties()));

// 生猪板油
    public static final DeferredItem<Item> RAWPIGFAT = REGISTRY.register("rawpigfat",
            () -> new Item(new Item.Properties()));

// 生猪肚
    public static final DeferredItem<Item> RAWPIGSTOMACH = REGISTRY.register("rawpigstomach",
            () -> new Item(new Item.Properties()));

// 生猪肺
    public static final DeferredItem<Item> RAWPIGLUNG = REGISTRY.register("rawpiglung",
            () -> new Item(new Item.Properties()));

// 生猪肝
    public static final DeferredItem<Item> RAWPIGLIVER = REGISTRY.register("rawpigliver",
            () -> new Item(new Item.Properties()));

// 生猪肾
    public static final DeferredItem<Item> RAWPIGKIDNEY = REGISTRY.register("rawpigkidney",
            () -> new Item(new Item.Properties()));

// 生猪心
    public static final DeferredItem<Item> RAWPIGHEART = REGISTRY.register("rawpigheart",
            () -> new Item(new Item.Properties()));

// 生猪肥肠
    public static final DeferredItem<Item> RAWPIGINTESTINE = REGISTRY.register("rawpigintestine",
            () -> new Item(new Item.Properties()));

// 生猪脑花
    public static final DeferredItem<Item> RAWPIGCEREBRUM = REGISTRY.register("rawpigcerebrum",
            () -> new Item(new Item.Properties()));

// 生羊血
    public static final DeferredItem<Item> RAWSHEEPBLOOD = REGISTRY.register("rawsheepblood",
            () -> new Item(new Item.Properties()));

// 生羊肉片
    public static final DeferredItem<Item> RAWDICEDSHEEPMEAT = REGISTRY.register("rawdicedsheepmeat",
            () -> new Item(new Item.Properties()));

// 生羊脸
    public static final DeferredItem<Item> RAWSHEEPFACE = REGISTRY.register("rawsheepface",
            () -> new Item(new Item.Properties()));

// 生羊尾油
    public static final DeferredItem<Item> RAWSHEEPTAILFAT = REGISTRY.register("rawsheeptailfat",
            () -> new Item(new Item.Properties()));

// 生羊油
    public static final DeferredItem<Item> RAWSHEEPFAT = REGISTRY.register("rawsheepfat",
            () -> new Item(new Item.Properties()));

// 生羊眼
    public static final DeferredItem<Item> RAWSHEEPEYE = REGISTRY.register("rawsheepeye",
            () -> new Item(new Item.Properties()));

// 生羊肚
    public static final DeferredItem<Item> RAWSHEEPSTOMACH = REGISTRY.register("rawsheepstomach",
            () -> new Item(new Item.Properties()));

// 生羊肝
    public static final DeferredItem<Item> RAWSHEEPLIVER = REGISTRY.register("rawsheepliver",
            () -> new Item(new Item.Properties()));

// 生羊肠
    public static final DeferredItem<Item> RAWSHEEPINTESTINE = REGISTRY.register("rawsheepintestine",
            () -> new Item(new Item.Properties()));

// 生羊肾
    public static final DeferredItem<Item> RAWSHEEPKIDNEY = REGISTRY.register("rawsheepkidney",
            () -> new Item(new Item.Properties()));

// 生羊心
    public static final DeferredItem<Item> RAWSHEEPHEART = REGISTRY.register("rawsheepheart",
            () -> new Item(new Item.Properties()));

// 生羊排
    public static final DeferredItem<Item> RAWSHEEPSPARERIB = REGISTRY.register("rawsheepsparerib",
            () -> new Item(new Item.Properties()));

// 生羊脊骨
    public static final DeferredItem<Item> RAWSHEEPSPINE = REGISTRY.register("rawsheepspine",
            () -> new Item(new Item.Properties()));

// 生羊蹄
    public static final DeferredItem<Item> RAWSHEEPFEET = REGISTRY.register("rawsheepfeet",
            () -> new Item(new Item.Properties()));

// 生羊腿
    public static final DeferredItem<Item> RAWSHEEPLEG = REGISTRY.register("rawsheepleg",
            () -> new Item(new Item.Properties()));

// 羊胡须
    public static final DeferredItem<Item> SHEEPBREAD = REGISTRY.register("sheepbread",
            () -> new Item(new Item.Properties()));

// 生鸡血
    public static final DeferredItem<Item> RAWCHICKENBLOOD = REGISTRY.register("rawchickenblood",
            () -> new Item(new Item.Properties()));

// 生鸡头
    public static final DeferredItem<Item> RAWCHICKENHEAD = REGISTRY.register("rawchickenhead",
            () -> new Item(new Item.Properties()));

// 生鸡脖
    public static final DeferredItem<Item> RAWCHICKENNECK = REGISTRY.register("rawchickenneck",
            () -> new Item(new Item.Properties()));

// 生鸡肉丁
    public static final DeferredItem<Item> RAWCHICKENPIECEPIECE = REGISTRY.register("rawchickenpiecepiece",
            () -> new Item(new Item.Properties()));

// 生鸡胸肉
    public static final DeferredItem<Item> RAWCHICKENBREAST = REGISTRY.register("rawchickenbreast",
            () -> new Item(new Item.Properties()));

// 生鸡胸块
    public static final DeferredItem<Item> RAWCHICKENPIECE = REGISTRY.register("rawchickenpiece",
            () -> new Item(new Item.Properties()));

// 生鸡翅
    public static final DeferredItem<Item> RAWCHICKENWING = REGISTRY.register("rawchickenwing",
            () -> new Item(new Item.Properties()));

// 生鸡翅尖
    public static final DeferredItem<Item> RAWCHICKENWINGTIP = REGISTRY.register("rawchickenwingtip",
            () -> new Item(new Item.Properties()));

// 生鸡爪
    public static final DeferredItem<Item> RAWCHICKENFEET = REGISTRY.register("rawchickenfeet",
            () -> new Item(new Item.Properties()));

// 挂糊裹粉的鸡腿
    public static final DeferredItem<Item> RAWCHICKENLEGWITHLEG = REGISTRY.register("rawchickenlegwithleg",
            () -> new Item(new Item.Properties()));

// 生鸡腿
    public static final DeferredItem<Item> RAWCHICKENLEG = REGISTRY.register("rawchickenleg",
            () -> new Item(new Item.Properties()));

// 生鸡瘦肉
    public static final DeferredItem<Item> RAWCHICKENLEAN = REGISTRY.register("rawchickenlean",
            () -> new Item(new Item.Properties()));

// 生鸡叉骨
    public static final DeferredItem<Item> RAWCHICKENFORK = REGISTRY.register("rawchickenfork",
            () -> new Item(new Item.Properties()));

// 生鸡尾尖
    public static final DeferredItem<Item> RAWCHICKENASS = REGISTRY.register("rawchickenass",
            () -> new Item(new Item.Properties()));

// 生鸡心
    public static final DeferredItem<Item> RAWCHICKENHEART = REGISTRY.register("rawchickenheart",
            () -> new Item(new Item.Properties()));

// 生鸡肝
    public static final DeferredItem<Item> RAWCHICKENLIVER = REGISTRY.register("rawchickenliver",
            () -> new Item(new Item.Properties()));

// 生鸡胗
    public static final DeferredItem<Item> RAWCHICKENGIZZARD = REGISTRY.register("rawchickengizzard",
            () -> new Item(new Item.Properties()));

// 烤牛心
    public static final DeferredItem<Item> COOKEDCATTLEHEART = REGISTRY.register("cookedcattleheart",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤牛筋
    public static final DeferredItem<Item> COOKEDCATTLEJOINT = REGISTRY.register("cookedcattlejoint",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤牛肚
    public static final DeferredItem<Item> COOKEDCATTLESTOMACH = REGISTRY.register("cookedcattlestomach",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤牛肺
    public static final DeferredItem<Item> COOKEDCATTLELUNG = REGISTRY.register("cookedcattlelung",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤牛肝
    public static final DeferredItem<Item> COOKEDCATTLELIVER = REGISTRY.register("cookedcattleliver",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤牛肠
    public static final DeferredItem<Item> COOKEDCATTLEINTESTINE = REGISTRY.register("cookedcattleintestine",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤牛肉片
    public static final DeferredItem<Item> COOKEDDICEDCATTLE = REGISTRY.register("cookeddicedcattle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烟熏牛头
    public static final DeferredItem<Item> COOKEDCATTLEFACE = REGISTRY.register("cookedcattleface",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤牛雪花
    public static final DeferredItem<Item> COOKEDCATTLEMARBLEDBEEF = REGISTRY.register("cookedcattlemarbledbeef",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤牛肘
    public static final DeferredItem<Item> COOKEDCATTLELEG = REGISTRY.register("cookedcattleleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤牛蹄
    public static final DeferredItem<Item> COOKEDCATTLEFEET = REGISTRY.register("cookedcattlefeet",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 香烤牛腱子
    public static final DeferredItem<Item> COOKEDCATTLETENDON = REGISTRY.register("cookedcattletendon",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪肉片
    public static final DeferredItem<Item> COOKEDDICEDPIGMEAT = REGISTRY.register("cookeddicedpigmeat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪肉丝
    public static final DeferredItem<Item> COOKEDPIGSHREDS = REGISTRY.register("cookedpigshreds",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪耳朵
    public static final DeferredItem<Item> COOKEDPIGEAR = REGISTRY.register("cookedpigear",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪鼻子
    public static final DeferredItem<Item> COOKEDPIGNOSE = REGISTRY.register("cookedpignose",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪尾巴
    public static final DeferredItem<Item> COOKEDPIGTAIL = REGISTRY.register("cookedpigtail",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪皮
    public static final DeferredItem<Item> COOKEDPIGSKIN = REGISTRY.register("cookedpigskin",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪五花
    public static final DeferredItem<Item> COOKEDPIGSTREAKYPORK = REGISTRY.register("cookedpigstreakypork",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪里脊
    public static final DeferredItem<Item> COOKEDPIGTENDERLOIN = REGISTRY.register("cookedpigtenderloin",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪排骨
    public static final DeferredItem<Item> COOKEDPIGSPARERIB = REGISTRY.register("cookedpigsparerib",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪肘子
    public static final DeferredItem<Item> COOKEDPIGLEG = REGISTRY.register("cookedpigleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪蹄
    public static final DeferredItem<Item> COOKEDPIGFEET = REGISTRY.register("cookedpigfeet",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪油渣
    public static final DeferredItem<Item> COOKEDPIGFAT = REGISTRY.register("cookedpigfat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪肚
    public static final DeferredItem<Item> COOKEDPIGSTOMACH = REGISTRY.register("cookedpigstomach",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪肺
    public static final DeferredItem<Item> COOKEDPIGLUNG = REGISTRY.register("cookedpiglung",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪肝
    public static final DeferredItem<Item> COOKEDPIGLIVER = REGISTRY.register("cookedpigliver",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪腰子
    public static final DeferredItem<Item> COOKEDPIGKIDNEY = REGISTRY.register("cookedpigkidney",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪心
    public static final DeferredItem<Item> COOKEDPIGHEART = REGISTRY.register("cookedpigheart",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪脑花
    public static final DeferredItem<Item> COOKEDPIGCEREBRUM = REGISTRY.register("cookedpigcerebrum",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪肥肠
    public static final DeferredItem<Item> COOKEDPIGINTESTINE = REGISTRY.register("cookedpigintestine",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤猪脸
    public static final DeferredItem<Item> COOKEDPIGFACE = REGISTRY.register("cookedpigface",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤羊肉片
    public static final DeferredItem<Item> COOKEDDICEDSHEEPMEAT = REGISTRY.register("cookeddicedsheepmeat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤羊脸
    public static final DeferredItem<Item> COOKEDSHEEPFACE = REGISTRY.register("cookedsheepface",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤羊眼
    public static final DeferredItem<Item> COOKEDSHEEPEYE = REGISTRY.register("cookedsheepeye",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤羊尾
    public static final DeferredItem<Item> COOKEDSHEEPTAIL = REGISTRY.register("cookedsheeptail",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤羊肚
    public static final DeferredItem<Item> COOKEDSHEEPSTOMACH = REGISTRY.register("cookedsheepstomach",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤羊肝
    public static final DeferredItem<Item> COOKEDSHEEPLIVER = REGISTRY.register("cookedsheepliver",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤羊肠
    public static final DeferredItem<Item> COOKEDSHEEPINTESTINE = REGISTRY.register("cookedsheepintestine",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤羊肾
    public static final DeferredItem<Item> COOKEDSHEEPKIDNEY = REGISTRY.register("cookedsheepkidney",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤羊心
    public static final DeferredItem<Item> COOKEDSHEEPHEART = REGISTRY.register("cookedsheepheart",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 煎烤羊排
    public static final DeferredItem<Item> COOKEDSHEEPSPARERLIP = REGISTRY.register("cookedsheepsparerlip",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤羊脊骨
    public static final DeferredItem<Item> COOKEDSHEEPSPINE = REGISTRY.register("cookedsheepspine",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤羊蹄
    public static final DeferredItem<Item> COOKEDSHEEPFEET = REGISTRY.register("cookedsheepfeet",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 美味烤羊腿
    public static final DeferredItem<Item> COOKEDSHEEPLEG = REGISTRY.register("cookedsheepleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 羊油渣
    public static final DeferredItem<Item> COOKEDSHEEPFAT = REGISTRY.register("cookedsheepfat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤鸡肉丁
    public static final DeferredItem<Item> COOKEDCHICKENDICED = REGISTRY.register("cookedchickendiced",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 碎烤鸡肉
    public static final DeferredItem<Item> COOKEDCHICKENPIECEPIECE = REGISTRY.register("cookedchickenpiecepiece",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤鸡头
    public static final DeferredItem<Item> COOKEDCHICKENHEAD = REGISTRY.register("cookedchickenhead",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤鸡脖
    public static final DeferredItem<Item> COOKEDCHICKENNECK = REGISTRY.register("cookedchickenneck",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤鸡翅
    public static final DeferredItem<Item> COOKEDCHICKENWING = REGISTRY.register("cookedchickenwing",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤鸡翅尖
    public static final DeferredItem<Item> COOKEDCHICKENWINGTIP = REGISTRY.register("cookedchickenwingtip",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤鸡爪
    public static final DeferredItem<Item> COOKEDCHICKENFEET = REGISTRY.register("cookedchickenfeet",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤鸡大腿
    public static final DeferredItem<Item> COOKEDCHICKENLEGWITHLEG = REGISTRY.register("cookedchickenlegwithleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤鸡腿
    public static final DeferredItem<Item> COOKEDCHICKENLEG = REGISTRY.register("cookedchickenleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤鸡块
    public static final DeferredItem<Item> COOKEDCHICKENPIECE = REGISTRY.register("cookedchickenpiece",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤鸡排
    public static final DeferredItem<Item> COOKEDCHICKENSTEAK = REGISTRY.register("cookedchickensteak",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤鸡叉骨
    public static final DeferredItem<Item> COOKEDCHICKENFORK = REGISTRY.register("cookedchickenfork",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤鸡尾尖
    public static final DeferredItem<Item> COOKEDCHICKENASS = REGISTRY.register("cookedchickenass",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤鸡心
    public static final DeferredItem<Item> COOKEDCHICKENHEART = REGISTRY.register("cookedchickenheart",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤鸡肝
    public static final DeferredItem<Item> COOKEDCHICKENLIVER = REGISTRY.register("cookedchickenliver",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 烤鸡胗
    public static final DeferredItem<Item> COOKEDCHICKENGIZZARD = REGISTRY.register("cookedchickengizzard",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

// 菠萝果酱
    public static final DeferredItem<Item> PINEAPPLEJAM = REGISTRY.register("pineapplejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 草莓果酱
    public static final DeferredItem<Item> STRAWBERRYJAM = REGISTRY.register("strawberryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 橙子果酱
    public static final DeferredItem<Item> ORANGEJAM = REGISTRY.register("orangejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 冬枣果酱
    public static final DeferredItem<Item> WINTERJUJUBEJAM = REGISTRY.register("winterjujubejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 哈密瓜果酱
    public static final DeferredItem<Item> HAMIMELONJAM = REGISTRY.register("hamimelonjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 火龙果果酱
    public static final DeferredItem<Item> DRAGONFRUITJAM = REGISTRY.register("dragonfruitjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 橘子果酱
    public static final DeferredItem<Item> TANGERINEJAM = REGISTRY.register("tangerinejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 蓝莓果酱
    public static final DeferredItem<Item> BLUEBERRYJAM = REGISTRY.register("blueberryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 梨果酱
    public static final DeferredItem<Item> PEARJAM = REGISTRY.register("pearjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 荔枝果酱
    public static final DeferredItem<Item> LYCHEEJAM = REGISTRY.register("lycheejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 李子果酱
    public static final DeferredItem<Item> PLUMJAM = REGISTRY.register("plumjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 榴莲果酱
    public static final DeferredItem<Item> DURIANJAM = REGISTRY.register("durianjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 芒果果酱
    public static final DeferredItem<Item> MANGOJAM = REGISTRY.register("mangojam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 猕猴桃果酱
    public static final DeferredItem<Item> KIWIFRUITJAM = REGISTRY.register("kiwifruitjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 木瓜果酱
    public static final DeferredItem<Item> PAWPAWJAM = REGISTRY.register("pawpawjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 柠檬果酱
    public static final DeferredItem<Item> LEMONJAM = REGISTRY.register("lemonjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 枇杷果酱
    public static final DeferredItem<Item> LOQUATJAM = REGISTRY.register("loquatjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 苹果果酱
    public static final DeferredItem<Item> APPLEJAM = REGISTRY.register("applejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 葡萄果酱
    public static final DeferredItem<Item> GRAPEJAM = REGISTRY.register("grapejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 青梅果酱
    public static final DeferredItem<Item> GREEMPLUMJAM = REGISTRY.register("greemplumjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 青提果酱
    public static final DeferredItem<Item> GREENGRAPEJAM = REGISTRY.register("greengrapejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 桑葚果酱
    public static final DeferredItem<Item> MULBERRYJAM = REGISTRY.register("mulberryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 山楂果酱
    public static final DeferredItem<Item> HAWTHORNJAM = REGISTRY.register("hawthornjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 山竹果酱
    public static final DeferredItem<Item> MANGOSTEENJAM = REGISTRY.register("mangosteenjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 石榴果酱
    public static final DeferredItem<Item> POMEGRANATEJAM = REGISTRY.register("pomegranatejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 水蜜桃果酱
    public static final DeferredItem<Item> HONEYPEACHJAM = REGISTRY.register("honeypeachjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 香瓜果酱
    public static final DeferredItem<Item> SWEETMELONJAM = REGISTRY.register("sweetmelonjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 甜浆果果酱
    public static final DeferredItem<Item> SWEETBERRYJAM = REGISTRY.register("sweetberryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 西瓜果酱
    public static final DeferredItem<Item> WATERMELONJAM = REGISTRY.register("watermelonjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 香蕉果酱
    public static final DeferredItem<Item> BANANAJAM = REGISTRY.register("bananajam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 杏果酱
    public static final DeferredItem<Item> APRICOTJAM = REGISTRY.register("apricotjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 杨桃果酱
    public static final DeferredItem<Item> CARAMBOLAJAM = REGISTRY.register("carambolajam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 椰子果酱
    public static final DeferredItem<Item> COCONUTJAM = REGISTRY.register("coconutjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 樱桃果酱
    public static final DeferredItem<Item> CHERRYJAM = REGISTRY.register("cherryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 油桃果酱
    public static final DeferredItem<Item> NECTARINEJAM = REGISTRY.register("nectarinejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

// 橙子叶
    public static final DeferredItem<Item> ORANGELEAVE = REGISTRY.register("orangeleave", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));

// 绞肉机盖
    public static final DeferredItem<Item> MINCER_COVER = REGISTRY.register("mincer_cover",
            () -> new Item(new Item.Properties()));

// 窗纸
    public static final DeferredItem<Item> WINDOW_PAPER_ITEM = REGISTRY.register("windowpaper_1",
            () -> new WindowPaperItem(new Item.Properties()));

// 青芒果
    public static final DeferredItem<Item> GREENMANGO = rareFruit("greenmango", 6, 0.5f);

// 葡萄
    public static final DeferredItem<Item> AGRAPE = REGISTRY.register("agrape", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationModifier(0.8f)
                    .alwaysEdible()
                    .build())));

// 青葡萄
    public static final DeferredItem<Item> AGREENGRAPE = REGISTRY.register("agreengrape", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationModifier(0.8f)
                    .alwaysEdible()
                    .build())));

// 菜花
    public static final DeferredItem<Item> CAULIFLOWER = REGISTRY.register("cauliflower", () -> new Item(new Item.Properties()));

// 蒜薹
    public static final DeferredItem<Item> GARLICPEDICEL = REGISTRY.register("garlicpedicel", () -> new Item(new Item.Properties()));

// 花生
    public static final DeferredItem<Item> PEANUT = REGISTRY.register("peanut", () -> new Item(new Item.Properties()));

// 石榴籽
    public static final DeferredItem<Item> POMEGRANATE_SEED = REGISTRY.register("pomegranate_seed", () -> new Item(new Item.Properties()));

// 生鸡大腿
    public static final DeferredItem<Item> RAWHALFOFCHICKENLEG = REGISTRY.register("rawhalfofchickenleg", () -> new Item(new Item.Properties()));

// 生羊尾
    public static final DeferredItem<Item> RAWSHEEPTAIL = REGISTRY.register("rawsheeptail", () -> new Item(new Item.Properties()));

// 红尖椒
    public static final DeferredItem<Item> REDREPPER = REGISTRY.register("redrepper", () -> new Item(new Item.Properties()));

// 葱
    public static final DeferredItem<Item> SACLLION = REGISTRY.register("sacllion", () -> new Item(new Item.Properties()));

// 菠菜
    public static final DeferredItem<Item> SPINACH = REGISTRY.register("spinach", () -> new Item(new Item.Properties()));

// 红薯
    public static final DeferredItem<Item> SWEETPOTATO = REGISTRY.register("sweetpotato", () -> new Item(new Item.Properties()));

// 黄桃
    public static final DeferredItem<Item> YELLOWPEACH = rareFruit("yellowpeach", 6, 0.5f);

// 干八角
    public static final DeferredItem<Item> DRYANISEED = REGISTRY.register("dryaniseed", () -> new Item(new Item.Properties()));

// 干桂皮
    public static final DeferredItem<Item> DRYCINNAMON = REGISTRY.register("drycinnamon", () -> new Item(new Item.Properties()));

// 干咖啡豆
    public static final DeferredItem<Item> DRYCOFFEEBEAN = REGISTRY.register("drycoffeebean", () -> new Item(new Item.Properties()));

// 干丁香
    public static final DeferredItem<Item> DRYLILAC = REGISTRY.register("drylilac", () -> new Item(new Item.Properties()));

// 干肉豆蔻
    public static final DeferredItem<Item> DRYNUTMEG = REGISTRY.register("drynutmeg", () -> new Item(new Item.Properties()));

// 干葱
    public static final DeferredItem<Item> DRYSCALION = REGISTRY.register("dryscalion", () -> new Item(new Item.Properties()));

// 干花椒
    public static final DeferredItem<Item> DRYSICHUANPEPPER = REGISTRY.register("drysichuanpepper", () -> new Item(new Item.Properties()));

// 西蓝花块
    public static final DeferredItem<Item> DICEDBROCCOIL = REGISTRY.register("dicedbroccoil", () -> new Item(new Item.Properties()));

// 菜花块
    public static final DeferredItem<Item> DICEDCAULIFLOWER = REGISTRY.register("dicedcauliflower", () -> new Item(new Item.Properties()));

// 蛋壳
    public static final DeferredItem<Item> EGGSHELL = REGISTRY.register("eggshell", () -> new Item(new Item.Properties()));

// 燕麦粒
    public static final DeferredItem<Item> OATGRAIN = REGISTRY.register("oatgrain", () -> new Item(new Item.Properties()));

// 糯米
    public static final DeferredItem<Item> POLISHEDGLUTINOUSRICE_2 = REGISTRY.register("polishedglutinousrice_2", () -> new Item(new Item.Properties()));

// 滚烫的糯米
    public static final DeferredItem<Item> HOT_GLUTINOUS = REGISTRY.register("hot_glutinous", () -> new TooltipItem(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build()),
            Component.translatable("tooltip.flavor_immersed_daily.hot_glutinous"),
            () -> List.of()));

// 生包子
    public static final DeferredItem<Item> RAW_BAOZI = filledItem("raw_baozi");

// 生饺子
    public static final DeferredItem<Item> RAW_DUMPLING = filledItem("raw_dumpling");

// 生馄饨
    public static final DeferredItem<Item> RAW_WONTON = filledItem("raw_wonton");

// 白切豆腐片
    public static final DeferredItem<Item> BAISED_TOFU_SLICES = REGISTRY.register("baised_tofu_slices", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 煮米粉
    public static final DeferredItem<Item> BOILEDRICEFLOURNOODLES = REGISTRY.register("boiledriceflournoodles", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 骨头汤
    public static final DeferredItem<Item> BONESOUP = REGISTRY.register("bonesoup", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 荞麦饭
    public static final DeferredItem<Item> BUCKWHEATRICE = REGISTRY.register("buckwheatrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 巧克力
    public static final DeferredItem<Item> CHOCOLATE = REGISTRY.register("chocolate", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

// 熟鸡胸肉
    public static final DeferredItem<Item> COOKEDCHICKENBREAST = REGISTRY.register("cookedchickenbreast", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 熟鸡瘦肉
    public static final DeferredItem<Item> COOKEDCHICKENLEAN = REGISTRY.register("cookedchickenlean", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 熟玉米糊
    public static final DeferredItem<Item> COOKEDCORNBATTER = REGISTRY.register("cookedcornbatter", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 脆皮烤肉
    public static final DeferredItem<Item> COOKEDCRISPYPORK = REGISTRY.register("cookedcrispypork", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.7f).alwaysEdible().build())));

// 熟肥牛片
    public static final DeferredItem<Item> COOKEDFLAKEBEEF = REGISTRY.register("cookedflakebeef", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.7f).alwaysEdible().build())));

// 熟鸡大腿
    public static final DeferredItem<Item> COOKEDHALFOFCHCIKENLEG = REGISTRY.register("cookedhalfofchcikenleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 熟猪五花肉
    public static final DeferredItem<Item> COOKEDPIGSTREAKMEAT = REGISTRY.register("cookedpigstreakmeat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.7f).alwaysEdible().build())));

// 干豆腐皮
    public static final DeferredItem<Item> DRIED_TOFU_SKIN = REGISTRY.register("dried_tofu_skin", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 豆芽
    public static final DeferredItem<Item> BEANSPROUT = REGISTRY.register("beansprout", () -> new Item(new Item.Properties()));

// 盐水菠萝片
    public static final DeferredItem<Item> EATENPINEAPPLE = REGISTRY.register("eatenpineapple", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.2f).alwaysEdible().build())));

// 鸡蛋饼干
    public static final DeferredItem<Item> EGGBISCUIT = REGISTRY.register("eggbiscuit", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 鸡蛋饼
    public static final DeferredItem<Item> EGGPANCAKE = REGISTRY.register("eggpancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 蛋挞
    public static final DeferredItem<Item> EGGTART = REGISTRY.register("eggtart", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 八宝粥
    public static final DeferredItem<Item> EIGHTTREASURECONGEE = REGISTRY.register("eighttreasurecongee", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 皮蛋瘦肉粥
    public static final DeferredItem<Item> CONGEE_WITH_MINCE_PORK_AND_PRESERVED_EGG = REGISTRY.register("congee_with_minced_pork_and_preserved_egg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.6f).alwaysEdible().build())));

// 电解质饮料
    public static final DeferredItem<Item> ELECTROLYTEBEVERAGE = REGISTRY.register("electrolytebeverage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.2f).alwaysEdible().build())));

// 繁花卷
    public static final DeferredItem<Item> FANHUAROLL = REGISTRY.register("fanhuaroll", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 炸鸡排
    public static final DeferredItem<Item> FRIEDCHICKENCHOP = REGISTRY.register("friedchickenchop", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 炸鸡米花
    public static final DeferredItem<Item> FRIEDCHICKENCORN = REGISTRY.register("friedchickencorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 炸鸡腿
    public static final DeferredItem<Item> FRIEDCHICKENLEG = REGISTRY.register("friedchickenleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 炸鸡翅
    public static final DeferredItem<Item> FRIEDCHICKENWING = REGISTRY.register("friedchickenwing", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 炸香肠
    public static final DeferredItem<Item> FRIEDSAUSAGE = REGISTRY.register("friedsausage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 葡萄酒
    public static final DeferredItem<Item> GRAPEWINE = REGISTRY.register("grapewine", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.2f).alwaysEdible().build())));

// 绿豆粥
    public static final DeferredItem<Item> GREENBEANPORRIDGE = REGISTRY.register("greenbeanporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 手抓饼
    public static final DeferredItem<Item> HANDGRABBEDPANCAKE = REGISTRY.register("handgrabbedpancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 枇杷膏
    public static final DeferredItem<Item> HEALTHLOQUATCREAM = REGISTRY.register("healthloquatcream", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.2f).alwaysEdible().build())));

// 花生牛奶
    public static final DeferredItem<Item> HEALTHPEANUTMILK = REGISTRY.register("healthpeanutmilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

// 核桃露
    public static final DeferredItem<Item> HEALTHWALNUTDEW = REGISTRY.register("healthwalnutdew", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

// 冰红茶
    public static final DeferredItem<Item> ICEDBLACKTEA = REGISTRY.register("icedblacktea", () -> new com.flavor_immersed_daily.item.DrinkItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

// 高粱粥
    public static final DeferredItem<Item> KAOLIANGPORRIDGE = REGISTRY.register("kaoliangporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 高粱饭
    public static final DeferredItem<Item> KAOLIANGRICE = REGISTRY.register("kaoliangrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 腊八粥
    public static final DeferredItem<Item> LABAPORRIDGE = REGISTRY.register("labaporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 小米粥
    public static final DeferredItem<Item> MILLIETPORRIDGE = REGISTRY.register("millietporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 小米饭
    public static final DeferredItem<Item> MILLIETRICE = REGISTRY.register("millietrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 拌米粉
    public static final DeferredItem<Item> NOILEDRICENOODLE = REGISTRY.register("noiledricenoodle", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 燕麦粥
    public static final DeferredItem<Item> OAKPORRIDGE = REGISTRY.register("oakporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 燕麦饭
    public static final DeferredItem<Item> OAKRICE = REGISTRY.register("oakrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 煎蛋卷
    public static final DeferredItem<Item> OMELETTE = REGISTRY.register("omelette", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 面糊饼
    public static final DeferredItem<Item> PANADAPANCAKE = REGISTRY.register("panadapancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 豌豆粉糕
    public static final DeferredItem<Item> PEAFLOURCAKE = REGISTRY.register("peaflourcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 薯片
    public static final DeferredItem<Item> POTATOCHIPS = REGISTRY.register("potatochips", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

// 红豆蛋挞
    public static final DeferredItem<Item> REDBEANEGGTART = REGISTRY.register("redbeaneggtart", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 红豆铜锣烧
    public static final DeferredItem<Item> REDBEANSTUFFINGDORAYAKI = REGISTRY.register("redbeanstuffingdorayaki", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 米饭
    public static final DeferredItem<Item> RICE = REGISTRY.register("rice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 大米粥
    public static final DeferredItem<Item> RICE_PORRIDGE = REGISTRY.register("rice_porridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 米浆汤
    public static final DeferredItem<Item> RICEPASTESOUP = REGISTRY.register("ricepastesoup", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 烤鸡叉骨
    public static final DeferredItem<Item> ROASTCHICKENFORK = REGISTRY.register("roastchickenfork", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));

// 烤红薯
    public static final DeferredItem<Item> ROASTEDSWEETPOTATO = REGISTRY.register("roastedsweetpotato", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 剥壳煮蛋
    public static final DeferredItem<Item> SHELLEDBOILEDEGG = REGISTRY.register("shelledboiledegg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 手开心果
    public static final DeferredItem<Item> SHOU_KAI_XIN_GUO = REGISTRY.register("shou_kai_xin_guo", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 小肉饼
    public static final DeferredItem<Item> SMALLMICEDMEATCAKE = REGISTRY.register("smallmicedmeatcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 蒸盐橙
    public static final DeferredItem<Item> STEAMEDSALTORANGE = REGISTRY.register("steamedsaltorange", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 蒸肠粉
    public static final DeferredItem<Item> STEAMEDVERMICELLIROLL_0 = REGISTRY.register("steamedvermicelliroll_0", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 草莓蛋糕卷
    public static final DeferredItem<Item> STRAWBERRYCAKEROLL = REGISTRY.register("strawberrycakeroll", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 甜牛奶
    public static final DeferredItem<Item> SWEETMILK = REGISTRY.register("sweetmilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

// 豆腐肠
    public static final DeferredItem<Item> TOFU_SAUSAGE = REGISTRY.register("tofu_sausage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 豆腐棒
    public static final DeferredItem<Item> TOFU_STICKS = REGISTRY.register("tofu_sticks", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 核桃糕
    public static final DeferredItem<Item> WALNUTCAKE = REGISTRY.register("walnutcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 核桃酥
    public static final DeferredItem<Item> WALNUTSHORTBREAD = REGISTRY.register("walnutshortbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));

// 小麦奶
    public static final DeferredItem<Item> WHEATMILK = REGISTRY.register("wheatmilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

// 腐竹
    public static final DeferredItem<Item> YUBA = REGISTRY.register("yuba", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

// 豆腐
    public static final DeferredItem<Item> TOUFU = REGISTRY.register("toufu", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.8f).alwaysEdible().build())));

// 鸡蛋糕
    public static final DeferredItem<Item> EGGCAKE = REGISTRY.register("eggcake", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.7f).alwaysEdible().build())));

// 金米糕
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

// 金葡萄
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
