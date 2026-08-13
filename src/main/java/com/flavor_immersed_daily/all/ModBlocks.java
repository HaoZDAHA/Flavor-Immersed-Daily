package com.flavor_immersed_daily.all;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.block.block.food.MultiStageInteractiveBlock;
import com.flavor_immersed_daily.item.ColorfulFireworksBoxItem;
import com.flavor_immersed_daily.item.CoupletBlockItem;
import com.flavor_immersed_daily.client.tooltip.TooltipBlockItem;
import com.flavor_immersed_daily.block.common.block.*;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.providers.ProviderType;
import com.flavor_immersed_daily.block.block.fruit.*;
import com.flavor_immersed_daily.block.block.tree.*;
import com.flavor_immersed_daily.block.block.furniture.*;
import com.flavor_immersed_daily.block.block.decorative.*;
import com.flavor_immersed_daily.block.block.machine.*;
import com.flavor_immersed_daily.block.block.processing.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;
import java.util.function.Function;

import static com.flavor_immersed_daily.FlavorImmersedDaily.*;
import static com.flavor_immersed_daily.all.ModItems.*;
import static com.flavor_immersed_daily.datagen.worldgen.ModConfiguredFeatures.PLUM_TREE;

public final class ModBlocks {
    /** Forces Registrate block entries to initialize before datagen providers are locked. */
    public static void bootstrap() {
    }

    public static final com.tterrag.registrate.Registrate REGISTRATE = FlavorImmersedDaily.REGISTRATE;
    public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(FlavorImmersedDaily.MODID);
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(FlavorImmersedDaily.MODID);

    private ModBlocks() {
    }

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
        BLOCK_ITEMS.register(eventBus);
    }

    private static Block block(String id) {
        return BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, id));
    }

    // Block-bound items: plantable seeds, placeable dishes, and juices.

    public static final DeferredItem<ItemNameBlockItem> CHINESE_LEAVES_SEEDS = BLOCK_ITEMS.register("chineseleavesseed",
            () -> new ItemNameBlockItem(block("chineseleavesseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> ANISEED_0 = BLOCK_ITEMS.register("aniseed_0", // 闂備浇娉曢崰鎰板几婵犳艾绠€瑰嫮澧楅幆鍌炴⒑鐠恒劌鏋戦柡瀣煼楠炲繘鎽庨崒婊呮婵炶揪绲藉锟犲极閹捐妫橀柕鍫濇椤忕Ψniseed_0
            () -> new ItemNameBlockItem(block("aniseed_0"), new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.2f)
                            .alwaysEdible()
                            .build())));

public static final DeferredItem<ItemNameBlockItem> RADISHSEED = BLOCK_ITEMS.register("radishseed",
            () -> new ItemNameBlockItem(block("radishseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> KAOLIANG_SEED = BLOCK_ITEMS.register("kao_liang_seed",
            () -> new ItemNameBlockItem(block("kao_liang_seed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> WHITE_MUSHROOM_SEED = BLOCK_ITEMS.register("white_mushroom_seed",
            () -> new ItemNameBlockItem(block("white_mushroom_seed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> BLACKFUNGSSEED = BLOCK_ITEMS.register("blackfungsseed",
            () -> new ItemNameBlockItem(block("blackfungsseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> PLEUROTUSSEED = BLOCK_ITEMS.register("pleurotusseed",
            () -> new ItemNameBlockItem(block("pleurotusseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> ENOKIMUSHROOMSEED = BLOCK_ITEMS.register("enokimushroomseed",
            () -> new ItemNameBlockItem(block("enokimushroomseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> TREMELLASEED = BLOCK_ITEMS.register("tremellaseed",
            () -> new ItemNameBlockItem(block("tremellaseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> FRAGRANTSEED = BLOCK_ITEMS.register("fragrantseed",
            () -> new ItemNameBlockItem(block("fragrantseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> BLUEBERRYSEED = BLOCK_ITEMS.register("blueberryseed",
            () -> new ItemNameBlockItem(block("blueberryseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> DRAGONFRUITSEED = BLOCK_ITEMS.register("dragonfruitseed",
            () -> new ItemNameBlockItem(block("dragonfruitseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> GREENTEALEAVESSEED = BLOCK_ITEMS.register("greentealeavesseed",
            () -> new ItemNameBlockItem(block("greentealeavesseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> HAMIMELONSEED = BLOCK_ITEMS.register("hamimelonseed",
            () -> new ItemNameBlockItem(block("hamimelonseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> PINEAPPLESEED = BLOCK_ITEMS.register("pineappleseed",
            () -> new ItemNameBlockItem(block("pineappleseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> RED_TEA_SEED = BLOCK_ITEMS.register("red_tea_seed",
            () -> new ItemNameBlockItem(block("red_tea_seed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> STRAWBERRYSEED = BLOCK_ITEMS.register("strawberryseed",
            () -> new ItemNameBlockItem(block("strawberryseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> LOTUSROOTSEED = BLOCK_ITEMS.register("lotusrootseed",
            () -> new ItemNameBlockItem(block("lotusrootseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> GLUTINOUSSEEDS = BLOCK_ITEMS.register("glutinousseeds",
            () -> new ItemNameBlockItem(block("glutinousseeds"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> PADDYSEEDS = BLOCK_ITEMS.register("paddyseeds",
            () -> new ItemNameBlockItem(block("paddyseeds"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> BROCCOILSEED = BLOCK_ITEMS.register("broccoilseed",
            () -> new ItemNameBlockItem(block("broccoilseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> BUCKWHEATSEED = BLOCK_ITEMS.register("buckwheatseed",
            () -> new ItemNameBlockItem(block("buckwheatseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> CABBAGESEED = BLOCK_ITEMS.register("cabbageseed",
            () -> new ItemNameBlockItem(block("cabbageseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> CASSAVASEEDS = BLOCK_ITEMS.register("cassavaseeds",
            () -> new ItemNameBlockItem(block("cassavaseeds"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> CELERYSEED = BLOCK_ITEMS.register("celeryseed",
            () -> new ItemNameBlockItem(block("celeryseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> CHINESECHIVESSEED = BLOCK_ITEMS.register("chinesechivesseed",
            () -> new ItemNameBlockItem(block("chinesechivesseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> CHINESEYAMSEED = BLOCK_ITEMS.register("chineseyamseed",
            () -> new ItemNameBlockItem(block("chineseyamseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> COFFEEBEANSEED = BLOCK_ITEMS.register("coffeebeanseed",
            () -> new ItemNameBlockItem(block("coffeebeanseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> CORNSEED = BLOCK_ITEMS.register("cornseed",
            () -> new ItemNameBlockItem(block("cornseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> CUMINSEED = BLOCK_ITEMS.register("cuminseed",
            () -> new ItemNameBlockItem(block("cuminseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> FENNELSEEDSTATES = BLOCK_ITEMS.register("fennelseedstates",
            () -> new ItemNameBlockItem(block("fennelseedstates"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> GARLICSEED = BLOCK_ITEMS.register("garlicseed",
            () -> new ItemNameBlockItem(block("garlicseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> GINGER_SEED = BLOCK_ITEMS.register("ginger_seed",
            () -> new ItemNameBlockItem(block("ginger_seed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> GREENPEPPERSEEDS = BLOCK_ITEMS.register("greenpepperseeds",
            () -> new ItemNameBlockItem(block("greenpepperseeds"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> GUMBOSEED = BLOCK_ITEMS.register("gumboseed",
            () -> new ItemNameBlockItem(block("gumboseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> MILLET = BLOCK_ITEMS.register("millet",
            () -> new ItemNameBlockItem(block("millet"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> MUNGBEANPLANT = BLOCK_ITEMS.register("mungbeanplant",
            () -> new ItemNameBlockItem(block("mungbeanplant"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> MUSTRAD_SEED = BLOCK_ITEMS.register("mustrad_seed",
            () -> new ItemNameBlockItem(block("mustrad_seed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> NUTMEGSEED = BLOCK_ITEMS.register("nutmegseed",
            () -> new ItemNameBlockItem(block("nutmegseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> OATSEED = BLOCK_ITEMS.register("oatseed",
            () -> new ItemNameBlockItem(block("oatseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> OILRAPESEED = BLOCK_ITEMS.register("oilrapeseed",
            () -> new ItemNameBlockItem(block("oilrapeseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> ONIONSEED = BLOCK_ITEMS.register("onionseed",
            () -> new ItemNameBlockItem(block("onionseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> PEASEED = BLOCK_ITEMS.register("peaseed",
            () -> new ItemNameBlockItem(block("peaseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> PUPLESWEETPOTATOSEED = BLOCK_ITEMS.register("puplesweetpotatoseed",
            () -> new ItemNameBlockItem(block("puplesweetpotatoseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> SESAMESEED = BLOCK_ITEMS.register("sesameseed",
            () -> new ItemNameBlockItem(block("sesameseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> SOY_BEAN_SEED = BLOCK_ITEMS.register("soy_bean_seed",
            () -> new ItemNameBlockItem(block("soy_bean_seed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> SWEETGREENPEPPERSEED = BLOCK_ITEMS.register("sweetgreenpepperseed",
            () -> new ItemNameBlockItem(block("sweetgreenpepperseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> ZUCCHINISEED = BLOCK_ITEMS.register("zucchiniseed",
            () -> new ItemNameBlockItem(block("zucchiniseed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> SPINACH_SEED = BLOCK_ITEMS.register("spinach_seed",
            () -> new ItemNameBlockItem(block("spinach_seed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> CAULIFLOWER_SEED = BLOCK_ITEMS.register("cauliflower_seed",
            () -> new ItemNameBlockItem(block("cauliflower_seed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> SCALLION_SEED = BLOCK_ITEMS.register("scallion_seed",
            () -> new ItemNameBlockItem(block("scallion_seed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> LILAC_SEED = BLOCK_ITEMS.register("lilac_seed",
            () -> new ItemNameBlockItem(block("lilac_seed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> RED_BEAN_BLOCK = BLOCK_ITEMS.register("red_bean_block",
            () -> new ItemNameBlockItem(block("red_bean_block"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> RED_PEPPER_SEED = BLOCK_ITEMS.register("red_pepper_seed",
            () -> new ItemNameBlockItem(block("red_pepper_seed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> SWEET_POTATO_SEED = BLOCK_ITEMS.register("sweet_potato_seed",
            () -> new ItemNameBlockItem(block("sweet_potato_seed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> SI_CHUAN_PEPPER_SEED = BLOCK_ITEMS.register("si_chuan_pepper_seed",
            () -> new ItemNameBlockItem(block("si_chuan_pepper_seed"), new Item.Properties()));

public static final DeferredItem<ItemNameBlockItem> PEA_NUT_SEED = BLOCK_ITEMS.register("pea_nut_seed",
            () -> new ItemNameBlockItem(block("pea_nut_seed"), new Item.Properties()));

    public static final DeferredItem<BlockItem> DRAWNEGGPLANT = BLOCK_ITEMS.register("drawneggplant",             () -> new BlockItem(block("drawneggplant"), new Item.Properties().food(new FoodProperties.Builder()                             .nutrition(4)                             .saturationModifier(0.6f)                             .alwaysEdible()                             .build())));

public static final DeferredItem<BlockItem> LINYIFRIEDCHICKEN = BLOCK_ITEMS.register("linyifriedchicken",             () -> new BlockItem(block("linyifriedchicken"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> MEATBALLSOUP = BLOCK_ITEMS.register("meatballsoup",             () -> new BlockItem(block("meatballsoup"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> PRESERVEDEGGSALAD = BLOCK_ITEMS.register("preservedeggsalad",             () -> new BlockItem(block("preservedeggsalad"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> TOMATOSALAD = BLOCK_ITEMS.register("tomatosalad",             () -> new BlockItem(block("tomatosalad"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> STEAMED_CHICKENWITH_CHILI_SAUCE = BLOCK_ITEMS.register("steamed_chickenwith_chili_sauce",             () -> new BlockItem(block("steamed_chickenwith_chili_sauce"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> COLACHICKENWINGS = BLOCK_ITEMS.register("colachickenwings",             () -> new BlockItem(block("colachickenwings"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> FOURJOYMEATBALLS = BLOCK_ITEMS.register("fourjoymeatballs",             () -> new BlockItem(block("fourjoymeatballs"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> STIRFRIEDBOILEDPORKSLICESINHOTSAUCE = BLOCK_ITEMS.register("stirfriedboiledporkslicesinhotsauce",             () -> new BlockItem(block("stirfriedboiledporkslicesinhotsauce"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> SAUTEED_POTATO_GREEN_PEPPER_EGGPLANT = BLOCK_ITEMS.register("sauteed_potato_green_pepper_eggplant",             () -> new BlockItem(block("sauteed_potato_green_pepper_eggplant"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> FRIEDMEATWITHCUMINONION = BLOCK_ITEMS.register("friedmeatwithcuminonion",             () -> new BlockItem(block("friedmeatwithcuminonion"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> KUNGPAOCHICKEN = BLOCK_ITEMS.register("kungpaochicken",             () -> new BlockItem(block("kungpaochicken"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> STIRFRIEDSTRINGBEANS = BLOCK_ITEMS.register("stirfriedstringbeans",             () -> new BlockItem(block("stirfriedstringbeans"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> MIXEDCOLDDISHES = BLOCK_ITEMS.register("mixedcolddishes",             () -> new BlockItem(block("mixedcolddishes"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> JAPANESEBRAISEDTOFU = BLOCK_ITEMS.register("japanesebraisedtofu",             () -> new BlockItem(block("japanesebraisedtofu"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> SCRAMBLEDEGGSWITHFUNGUSANDCUCUMBER = BLOCK_ITEMS.register("scrambledeggswithfungusandcucumber",             () -> new BlockItem(block("scrambledeggswithfungusandcucumber"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> PLEUROTUSERYNGIIWITHSALTANDPEPPER = BLOCK_ITEMS.register("pleurotuseryngiiwithsaltandpepper",             () -> new BlockItem(block("pleurotuseryngiiwithsaltandpepper"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> POACHED_SPICY_SLICESOF_PORK = BLOCK_ITEMS.register("poached_spicy_slicesof_pork",             () -> new BlockItem(block("poached_spicy_slicesof_pork"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> SLICED_FISHIN_HOT_CHILI_OIL = BLOCK_ITEMS.register("sliced_fishin_hot_chili_oil",             () -> new BlockItem(block("sliced_fishin_hot_chili_oil"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> SAUTEEDMUSHROOMSWITHRAPESEED = BLOCK_ITEMS.register("sauteedmushroomswithrapeseed",             () -> new BlockItem(block("sauteedmushroomswithrapeseed"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> STEAMEDFISH = BLOCK_ITEMS.register("steamedfish",             () -> new BlockItem(block("steamedfish"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> FRIEDCOWPEA = BLOCK_ITEMS.register("friedcowpea",             () -> new BlockItem(block("friedcowpea"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> FRIEDSPICYCHICKEN = BLOCK_ITEMS.register("friedspicychicken",             () -> new BlockItem(block("friedspicychicken"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> BOILED_CHICKENWITH_SAUCE = BLOCK_ITEMS.register("boiled_chickenwith_sauce",             () -> new BlockItem(block("boiled_chickenwith_sauce"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> STEWEDPORKWITHBROWNSAUCE = BLOCK_ITEMS.register("stewedporkwithbrownsauce",             () -> new BlockItem(block("stewedporkwithbrownsauce"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> FRIEDLIVERTIPWITHSPINACH = BLOCK_ITEMS.register("friedlivertipwithspinach",             () -> new BlockItem(block("friedlivertipwithspinach"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> PINEAPPLE_SWEETAND_SOUR_PORK = BLOCK_ITEMS.register("pineapple_sweetand_sour_pork",             () -> new BlockItem(block("pineapple_sweetand_sour_pork"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> CHICKENWITH_SCALLION_OIL = BLOCK_ITEMS.register("chickenwith_scallion_oil",             () -> new BlockItem(block("chickenwith_scallion_oil"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> SALTEDEGGYOLKFRIEDCAULIFLOWER = BLOCK_ITEMS.register("saltedeggyolkfriedcauliflower",             () -> new BlockItem(block("saltedeggyolkfriedcauliflower"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> ZUCCHINISNACKMEAT = BLOCK_ITEMS.register("zucchinisnackmeat",             () -> new BlockItem(block("zucchinisnackmeat"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> BOILED_FISHWITH_PICKLED_CABBAGEAND_CHILI = BLOCK_ITEMS.register("boiled_fishwith_pickled_cabbageand_chili",             () -> new BlockItem(block("boiled_fishwith_pickled_cabbageand_chili"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> FRIEDSHREDDEDPORKWITHSWEETANDSOURSAUCE = BLOCK_ITEMS.register("friedshreddedporkwithsweetandsoursauce",             () -> new BlockItem(block("friedshreddedporkwithsweetandsoursauce"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> SPICYTOFU = BLOCK_ITEMS.register("spicytofu",             () -> new BlockItem(block("spicytofu"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> BEANWITHSESAMESAUCE = BLOCK_ITEMS.register("beanwithsesamesauce",             () -> new BlockItem(block("beanwithsesamesauce"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

public static final DeferredItem<BlockItem> SPICYCABBAGE = BLOCK_ITEMS.register("spicycabbage",             () -> new BlockItem(block("spicycabbage"), new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build())));

    public static final DeferredItem<BlockItem> HAMIMELONJUICE = BLOCK_ITEMS.register("hamimelonjuice",             () -> new BlockItem(block("hamimelonjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> HAWTHORNJUIVE = BLOCK_ITEMS.register("hawthornjuive",             () -> new BlockItem(block("hawthornjuive"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> DRAGONFRUIEJUICE = BLOCK_ITEMS.register("dragonfruiejuice",             () -> new BlockItem(block("dragonfruiejuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> CARAMBOLAJUICE = BLOCK_ITEMS.register("carambolajuice",             () -> new BlockItem(block("carambolajuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> WINTERJUJUBEJUICE = BLOCK_ITEMS.register("winterjujubejuice",             () -> new BlockItem(block("winterjujubejuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> POMEGRANATEJUICE = BLOCK_ITEMS.register("pomegranatejuice",             () -> new BlockItem(block("pomegranatejuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> SWEETBERRYJUICE = BLOCK_ITEMS.register("sweetberryjuice",             () -> new BlockItem(block("sweetberryjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> DURIANJUICE = BLOCK_ITEMS.register("durianjuice",             () -> new BlockItem(block("durianjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> LEMONJUICE = BLOCK_ITEMS.register("lemonjuice",             () -> new BlockItem(block("lemonjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> BLUEBERRYJUICE = BLOCK_ITEMS.register("blueberryjuice",             () -> new BlockItem(block("blueberryjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> SWEETMELONJUICE = BLOCK_ITEMS.register("sweetmelonjuice",             () -> new BlockItem(block("sweetmelonjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> GREENGRAPEJUICE = BLOCK_ITEMS.register("greengrapejuice",             () -> new BlockItem(block("greengrapejuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> TANGERINEJUICE = BLOCK_ITEMS.register("tangerinejuice",             () -> new BlockItem(block("tangerinejuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> MANGOSTEENJUICE = BLOCK_ITEMS.register("mangosteenjuice",             () -> new BlockItem(block("mangosteenjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> CHERRYJUICE = BLOCK_ITEMS.register("cherryjuice",             () -> new BlockItem(block("cherryjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> STRAWBERRYJUICE = BLOCK_ITEMS.register("strawberryjuice",             () -> new BlockItem(block("strawberryjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> ORANGEJUICE = BLOCK_ITEMS.register("orangejuice",             () -> new BlockItem(block("orangejuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> GREENPLUMJUICE = BLOCK_ITEMS.register("greenplumjuice",             () -> new BlockItem(block("greenplumjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> APPLEJUICE = BLOCK_ITEMS.register("applejuice",             () -> new BlockItem(block("applejuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> HONEYPEACHJUIVE = BLOCK_ITEMS.register("honeypeachjuive",             () -> new BlockItem(block("honeypeachjuive"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> BANANAJUICE = BLOCK_ITEMS.register("bananajuice",             () -> new BlockItem(block("bananajuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> LYCHEEJUICE = BLOCK_ITEMS.register("lycheejuice",             () -> new BlockItem(block("lycheejuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> APRICOTJUICE = BLOCK_ITEMS.register("apricotjuice",             () -> new BlockItem(block("apricotjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> GRAPEJUICE = BLOCK_ITEMS.register("grapejuice",             () -> new BlockItem(block("grapejuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> MULBERRYJUIVE = BLOCK_ITEMS.register("mulberryjuive",             () -> new BlockItem(block("mulberryjuive"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> PKUMJUICE = BLOCK_ITEMS.register("pkumjuice",             () -> new BlockItem(block("pkumjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> MANGOJUICE = BLOCK_ITEMS.register("mangojuice",             () -> new BlockItem(block("mangojuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> WATERMELONJUICE = BLOCK_ITEMS.register("watermelonjuice",             () -> new BlockItem(block("watermelonjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> NECTARINEJUICE = BLOCK_ITEMS.register("nectarinejuice",             () -> new BlockItem(block("nectarinejuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> PINEAPPLEJUICE = BLOCK_ITEMS.register("pineapplejuice",             () -> new BlockItem(block("pineapplejuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> LOQUATJUICE = BLOCK_ITEMS.register("loquatjuice",             () -> new BlockItem(block("loquatjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> KIWIFRUITJUICE = BLOCK_ITEMS.register("kiwifruitjuice",             () -> new BlockItem(block("kiwifruitjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> PEARJUICE = BLOCK_ITEMS.register("pearjuice",             () -> new BlockItem(block("pearjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> PAWPAWJUICE = BLOCK_ITEMS.register("pawpawjuice",             () -> new BlockItem(block("pawpawjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));

public static final DeferredItem<BlockItem> COCONUTJUICE = BLOCK_ITEMS.register("coconutjuice",             () -> new BlockItem(block("coconutjuice"), new Item.Properties()                     .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())                     .stacksTo(16)));


    private static BlockBehaviour.Properties woodProps() {
        return BlockBehaviour.Properties.of().strength(2.0f, 3.0f).sound(SoundType.WOOD);
    }

    // Registrate block entries migrated from FlavorImmersedDaily.
    public static final BlockEntry<WoodBasinBlock> WOODBASIN = REGISTRATE
            .block("woodbasin", WoodBasinBlock::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_PLANKS)
            .properties(properties -> properties.strength(2.0f, 3.0f).sound(SoundType.WOOD))
            .item().build()
            .blockstate((ctx, provider) -> provider.getVariantBuilder(ctx.get())
                    .partialState().with(WoodBasinBlock.WATERED, false).with(WoodBasinBlock.HAS_CHICKEN, false).with(WoodBasinBlock.HAS_FRUIT, false)
                    .modelForState().modelFile(provider.models().getExistingFile(provider.modLoc("block/woodbasin"))).addModel()
                    .partialState().with(WoodBasinBlock.WATERED, true).with(WoodBasinBlock.HAS_CHICKEN, false).with(WoodBasinBlock.HAS_FRUIT, false)
                    .modelForState().modelFile(provider.models().getExistingFile(provider.modLoc("block/woodbasinwater"))).addModel()
                    .partialState().with(WoodBasinBlock.WATERED, true).with(WoodBasinBlock.HAS_CHICKEN, true).with(WoodBasinBlock.HAS_FRUIT, false)
                    .modelForState().modelFile(provider.models().getExistingFile(provider.modLoc("block/woodbasinwater"))).addModel()
                    .partialState().with(WoodBasinBlock.WATERED, false).with(WoodBasinBlock.HAS_CHICKEN, false).with(WoodBasinBlock.HAS_FRUIT, true)
                    .modelForState().modelFile(provider.models().getExistingFile(provider.modLoc("block/woodbasin"))).addModel()
                    .partialState().with(WoodBasinBlock.WATERED, false).with(WoodBasinBlock.HAS_CHICKEN, true).with(WoodBasinBlock.HAS_FRUIT, false)
                    .modelForState().modelFile(provider.models().getExistingFile(provider.modLoc("block/woodbasin"))).addModel()
                    .partialState().with(WoodBasinBlock.WATERED, false).with(WoodBasinBlock.HAS_CHICKEN, true).with(WoodBasinBlock.HAS_FRUIT, true)
                    .modelForState().modelFile(provider.models().getExistingFile(provider.modLoc("block/woodbasin"))).addModel()
                    .partialState().with(WoodBasinBlock.WATERED, true).with(WoodBasinBlock.HAS_CHICKEN, false).with(WoodBasinBlock.HAS_FRUIT, true)
                    .modelForState().modelFile(provider.models().getExistingFile(provider.modLoc("block/woodbasinwater"))).addModel()
                    .partialState().with(WoodBasinBlock.WATERED, true).with(WoodBasinBlock.HAS_CHICKEN, true).with(WoodBasinBlock.HAS_FRUIT, true)
                    .modelForState().modelFile(provider.models().getExistingFile(provider.modLoc("block/woodbasinwater"))).addModel())
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static final BlockEntry<RotatedPillarBlock> BANANAWOOD = REGISTRATE
            .block("bananawood", RotatedPillarBlock::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_LOG)
            .properties(properties -> properties.mapColor(MapColor.COLOR_YELLOW)
                    .strength(2.0f)
                    .sound(SoundType.WOOD))
            .item().build()
            .blockstate((ctx, provider) -> provider.getVariantBuilder(ctx.get())
                    .partialState().with(RotatedPillarBlock.AXIS, net.minecraft.core.Direction.Axis.Y)
                    .modelForState().modelFile(provider.models().getExistingFile(provider.modLoc("block/bananawood"))).addModel()
                    .partialState().with(RotatedPillarBlock.AXIS, net.minecraft.core.Direction.Axis.X)
                    .modelForState().modelFile(provider.models().getExistingFile(provider.modLoc("block/bananawood_horizontal"))).rotationX(90).rotationY(90).addModel()
                    .partialState().with(RotatedPillarBlock.AXIS, net.minecraft.core.Direction.Axis.Z)
                    .modelForState().modelFile(provider.models().getExistingFile(provider.modLoc("block/bananawood_horizontal"))).rotationX(90).addModel())
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static final BlockEntry<CinnamonWoodBlock> CINNAMONWOOD = REGISTRATE
            .block("cinnamonwood", properties -> new CinnamonWoodBlock(CINNAMON, properties))
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_LOG)
            .properties(properties -> properties.mapColor(MapColor.WOOD)
                    .strength(2.0f)
                    .randomTicks()
                    .sound(SoundType.WOOD)
                    .noOcclusion())
            .item()
            .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(),
                    provider.modLoc("block/cinnamonwood_stage0"))).build()
            .blockstate((ctx, provider) -> provider.getVariantBuilder(ctx.get())
                    .partialState().with(CinnamonWoodBlock.RIPE, true)
                    .modelForState().modelFile(provider.models().getExistingFile(provider.modLoc("block/cinnamonwood_stage0"))).addModel()
                    .partialState().with(CinnamonWoodBlock.RIPE, false)
                    .modelForState().modelFile(provider.models().getExistingFile(provider.modLoc("block/cinnamonwood_stage1"))).addModel())
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static final BlockEntry<RotatedPillarBlock> SOLARWOOD_LOG = REGISTRATE
            .block("solarwood_log", RotatedPillarBlock::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_LOG)
            .properties(properties -> properties.mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(2.0f).sound(SoundType.WOOD))
            .item()
            .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(),
                    provider.modLoc("block/solarwood_log"))).build()
            .blockstate((ctx, provider) -> provider.axisBlock(ctx.get(),
                    provider.models().getExistingFile(provider.modLoc("block/solarwood_log")),
                    provider.models().getExistingFile(provider.modLoc("block/solarwood_log_horizontal"))))
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static final BlockEntry<RotatedPillarBlock> ORCHARD_HEARTWOOD_LOG = REGISTRATE
            .block("orchard_heartwood_log", RotatedPillarBlock::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_LOG)
            .properties(properties -> properties.mapColor(MapColor.COLOR_BROWN)
                    .strength(2.0f)
                    .sound(SoundType.WOOD))
            .item().build()
            .blockstate((ctx, provider) -> provider.getVariantBuilder(ctx.get())
                    .partialState().with(RotatedPillarBlock.AXIS, net.minecraft.core.Direction.Axis.Y)
                    .modelForState().modelFile(provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_log"))).addModel()
                    .partialState().with(RotatedPillarBlock.AXIS, net.minecraft.core.Direction.Axis.X)
                    .modelForState().modelFile(provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_log_horizontal"))).rotationX(90).rotationY(90).addModel()
                    .partialState().with(RotatedPillarBlock.AXIS, net.minecraft.core.Direction.Axis.Z)
                    .modelForState().modelFile(provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_log_horizontal"))).rotationX(90).addModel())
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static BlockEntry<Block> woodPlanks(String name, MapColor color) {
        return REGISTRATE.block(name, Block::new)
                .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_PLANKS)
                .properties(properties -> properties.mapColor(color).strength(2.0f, 3.0f).sound(SoundType.WOOD))
                .item().build()
                .blockstate((ctx, provider) -> provider.simpleBlock(ctx.get()))
                .loot((loot, block) -> loot.dropSelf(block))
                .register();
    }

    public static BlockEntry<StairBlock> woodStairs(String name, java.util.function.Supplier<Block> planks, MapColor color) {
        return REGISTRATE.block(name, properties -> new StairBlock(planks.get().defaultBlockState(), properties))
                .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_STAIRS)
                .properties(properties -> properties.mapColor(color).strength(2.0f, 3.0f).sound(SoundType.WOOD))
                .item().build()
                .blockstate((ctx, provider) -> provider.stairsBlock(ctx.get(),
                        provider.models().getExistingFile(provider.modLoc("block/" + ctx.getName())),
                        provider.models().getExistingFile(provider.modLoc("block/" + ctx.getName() + "_inner")),
                        provider.models().getExistingFile(provider.modLoc("block/" + ctx.getName() + "_outer"))))
                .loot((loot, block) -> loot.dropSelf(block))
                .register();
    }

    public static BlockEntry<SlabBlock> woodSlab(String name, String planksName, MapColor color) {
        return REGISTRATE.block(name, SlabBlock::new)
                .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_SLAB)
                .properties(properties -> properties.mapColor(color).strength(2.0f, 3.0f).sound(SoundType.WOOD))
                .item().build()
                .blockstate((ctx, provider) -> provider.slabBlock(ctx.get(),
                        provider.models().getExistingFile(provider.modLoc("block/" + ctx.getName())),
                        provider.models().getExistingFile(provider.modLoc("block/" + ctx.getName() + "_top")),
                        provider.models().getExistingFile(provider.modLoc("block/" + planksName))))
                .loot((loot, block) -> loot.add(block, loot.createSlabItemTable(block)))
                .register();
    }

    public static BlockEntry<FenceBlock> woodFence(String name, String planksName, MapColor color) {
        return REGISTRATE.block(name, FenceBlock::new)
                .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_FENCE)
                .properties(properties -> properties.mapColor(color).strength(2.0f, 3.0f).sound(SoundType.WOOD))
                .item()
                .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + ctx.getName() + "_inventory"))).build()
                .blockstate((ctx, provider) -> provider.fenceBlock(ctx.get(), provider.modLoc("block/" + planksName)))
                .loot((loot, block) -> loot.dropSelf(block))
                .register();
    }

    public static BlockEntry<FenceGateBlock> woodFenceGate(String name, String planksName, MapColor color) {
        return REGISTRATE.block(name, properties -> new FenceGateBlock(WoodType.OAK, properties))
                .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_FENCE_GATE)
                .properties(properties -> properties.mapColor(color).strength(2.0f, 3.0f).sound(SoundType.WOOD))
                .item()
                .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + ctx.getName()))).build()
                .blockstate((ctx, provider) -> provider.fenceGateBlock(ctx.get(), provider.modLoc("block/" + planksName)))
                .loot((loot, block) -> loot.dropSelf(block))
                .register();
    }

    public static BlockEntry<ButtonBlock> woodButton(String name) {
        return REGISTRATE.block(name, properties -> new ButtonBlock(BlockSetType.OAK, 30, properties))
                .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_BUTTON)
                .properties(properties -> properties.noCollission().strength(0.5f).sound(SoundType.WOOD))
                .item()
                .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + ctx.getName() + "_inventory"))).build()
                .blockstate((ctx, provider) -> provider.buttonBlock(ctx.get(),
                        provider.models().getExistingFile(provider.modLoc("block/" + ctx.getName())),
                        provider.models().getExistingFile(provider.modLoc("block/" + ctx.getName() + "_pressed"))))
                .loot((loot, block) -> loot.dropSelf(block))
                .register();
    }

    public static BlockEntry<PressurePlateBlock> woodPressurePlate(String name) {
        return REGISTRATE.block(name, properties -> new PressurePlateBlock(BlockSetType.OAK, properties))
                .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_PRESSURE_PLATE)
                .properties(properties -> properties.noCollission().strength(0.5f).sound(SoundType.WOOD))
                .item()
                .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + ctx.getName()))).build()
                .blockstate((ctx, provider) -> provider.pressurePlateBlock(ctx.get(),
                        provider.models().getExistingFile(provider.modLoc("block/" + ctx.getName())),
                        provider.models().getExistingFile(provider.modLoc("block/" + ctx.getName() + "_down"))))
                .loot((loot, block) -> loot.dropSelf(block))
                .register();
    }

    public static final BlockEntry<Block> SOLARWOOD_PLANKS = REGISTRATE
            .block("solarwood_planks", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_PLANKS)
            .properties(properties -> woodProps().mapColor(MapColor.TERRACOTTA_YELLOW))
            .item()
            .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/solarwood_planks"))).build()
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.get(), provider.models().getExistingFile(provider.modLoc("block/solarwood_planks"))))
            .loot((loot, block) -> loot.dropSelf(block)).register();

    public static final BlockEntry<StairBlock> SOLARWOOD_STAIRS = REGISTRATE
            .block("solarwood_stairs", properties -> new StairBlock(SOLARWOOD_PLANKS.get().defaultBlockState(), properties))
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_STAIRS)
            .properties(properties -> woodProps().mapColor(MapColor.TERRACOTTA_YELLOW))
            .item()
            .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/solarwood_stairs"))).build()
            .blockstate((ctx, provider) -> provider.stairsBlock(ctx.get(), provider.models().getExistingFile(provider.modLoc("block/solarwood_stairs")), provider.models().getExistingFile(provider.modLoc("block/solarwood_stairs_inner")), provider.models().getExistingFile(provider.modLoc("block/solarwood_stairs_outer"))))
            .loot((loot, block) -> loot.dropSelf(block)).register();

    public static final BlockEntry<SlabBlock> SOLARWOOD_SLAB = REGISTRATE
            .block("solarwood_slab", SlabBlock::new).initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_SLAB)
            .properties(properties -> woodProps().mapColor(MapColor.TERRACOTTA_YELLOW))
            .item()
            .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/solarwood_slab"))).build()
            .blockstate((ctx, provider) -> provider.slabBlock(ctx.get(), provider.models().getExistingFile(provider.modLoc("block/solarwood_slab")), provider.models().getExistingFile(provider.modLoc("block/solarwood_slab_top")), provider.models().getExistingFile(provider.modLoc("block/solarwood_planks"))))
            .loot((loot, block) -> loot.add(block, loot.createSlabItemTable(block))).register();

    public static final BlockEntry<FenceBlock> SOLARWOOD_FENCE = REGISTRATE
            .block("solarwood_fence", FenceBlock::new).initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_FENCE)
            .properties(properties -> woodProps().mapColor(MapColor.TERRACOTTA_YELLOW))
            .item()
            .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/solarwood_fence_inventory"))).build()
            .blockstate((ctx, provider) -> provider.fenceBlock(ctx.get(), provider.modLoc("block/solarwood_planks")))
            .loot((loot, block) -> loot.dropSelf(block)).register();

    public static final BlockEntry<FenceGateBlock> SOLARWOOD_FENCE_GATE = REGISTRATE
            .block("solarwood_fence_gate", properties -> new FenceGateBlock(WoodType.OAK, properties))
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_FENCE_GATE)
            .properties(properties -> woodProps().mapColor(MapColor.TERRACOTTA_YELLOW))
            .item()
            .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/solarwood_fence_gate"))).build()
            .blockstate((ctx, provider) -> provider.fenceGateBlock(ctx.get(), provider.models().getExistingFile(provider.modLoc("block/solarwood_fence_gate")), provider.models().getExistingFile(provider.modLoc("block/solarwood_fence_gate_open")), provider.models().getExistingFile(provider.modLoc("block/solarwood_fence_gate_wall")), provider.models().getExistingFile(provider.modLoc("block/solarwood_fence_gate_wall_open"))))
            .loot((loot, block) -> loot.dropSelf(block)).register();

    public static final BlockEntry<ButtonBlock> SOLARWOOD_BUTTON = REGISTRATE
            .block("solarwood_button", properties -> new ButtonBlock(BlockSetType.OAK, 30, properties))
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_BUTTON)
            .properties(properties -> woodProps().noCollission().strength(0.5f))
            .item()
            .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/solarwood_button_inventory"))).build()
            .blockstate((ctx, provider) -> provider.buttonBlock(ctx.get(), provider.models().getExistingFile(provider.modLoc("block/solarwood_button")), provider.models().getExistingFile(provider.modLoc("block/solarwood_button_pressed"))))
            .loot((loot, block) -> loot.dropSelf(block)).register();

    public static final BlockEntry<PressurePlateBlock> SOLARWOOD_PRESSURE_PLATE = REGISTRATE
            .block("solarwood_pressure_plate", properties -> new PressurePlateBlock(BlockSetType.OAK, properties))
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_PRESSURE_PLATE)
            .properties(properties -> woodProps().noCollission().strength(0.5f))
            .item()
            .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/solarwood_pressure_plate"))).build()
            .blockstate((ctx, provider) -> provider.pressurePlateBlock(ctx.get(), provider.models().getExistingFile(provider.modLoc("block/solarwood_pressure_plate")), provider.models().getExistingFile(provider.modLoc("block/solarwood_pressure_plate_down"))))
            .loot((loot, block) -> loot.dropSelf(block)).register();

    public static final BlockEntry<Block> ORCHARD_HEARTWOOD_PLANKS = REGISTRATE
            .block("orchard_heartwood_planks", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_PLANKS)
            .properties(properties -> properties.mapColor(MapColor.COLOR_BROWN))
            .item()
            .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + ctx.getName()))).build()
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.get(), provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_planks"))))
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static final BlockEntry<StairBlock> ORCHARD_HEARTWOOD_STAIRS = REGISTRATE
            .block("orchard_heartwood_stairs", properties -> new StairBlock(ORCHARD_HEARTWOOD_PLANKS.get().defaultBlockState(), properties))
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_STAIRS)
            .properties(properties -> properties.mapColor(MapColor.COLOR_BROWN))
            .item()
            .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + ctx.getName()))).build()
            .blockstate((ctx, provider) -> provider.stairsBlock(ctx.get(),
                    provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_stairs")),
                    provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_stairs_inner")),
                    provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_stairs_outer"))))
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static final BlockEntry<SlabBlock> ORCHARD_HEARTWOOD_SLAB = REGISTRATE
            .block("orchard_heartwood_slab", SlabBlock::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_SLAB)
            .properties(properties -> properties.mapColor(MapColor.COLOR_BROWN))
            .item()
            .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + ctx.getName()))).build()
            .blockstate((ctx, provider) -> provider.slabBlock(ctx.get(), provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_slab")), provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_slab_top")), provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_planks"))))
            .loot((loot, block) -> loot.add(block, loot.createSlabItemTable(block)))
            .register();

    public static final BlockEntry<FenceBlock> ORCHARD_HEARTWOOD_FENCE = REGISTRATE
            .block("orchard_heartwood_fence", FenceBlock::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_FENCE)
            .properties(properties -> properties.mapColor(MapColor.COLOR_BROWN))
            .item()
            .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/orchard_heartwood_fence_inventory"))).build()
            .blockstate((ctx, provider) -> provider.fenceBlock(ctx.get(), provider.modLoc("block/orchard_heartwood_planks")))
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static final BlockEntry<FenceGateBlock> ORCHARD_HEARTWOOD_FENCE_GATE = REGISTRATE
            .block("orchard_heartwood_fence_gate", properties -> new FenceGateBlock(WoodType.OAK, properties))
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_FENCE_GATE)
            .properties(properties -> properties.mapColor(MapColor.COLOR_BROWN))
            .item()
            .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/orchard_heartwood_fence_gate"))).build()
            .blockstate((ctx, provider) -> provider.fenceGateBlock(ctx.get(), provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_fence_gate")), provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_fence_gate_open")), provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_fence_gate_wall")), provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_fence_gate_wall_open"))))
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static final BlockEntry<ButtonBlock> ORCHARD_HEARTWOOD_BUTTON = REGISTRATE
            .block("orchard_heartwood_button", properties -> new ButtonBlock(BlockSetType.OAK, 30, properties))
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_BUTTON)
            .properties(properties -> properties.noCollission().strength(0.5f))
            .item()
            .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/orchard_heartwood_button_inventory"))).build()
            .blockstate((ctx, provider) -> provider.buttonBlock(ctx.get(), provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_button")), provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_button_pressed"))))
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static final BlockEntry<PressurePlateBlock> ORCHARD_HEARTWOOD_PRESSURE_PLATE = REGISTRATE
            .block("orchard_heartwood_pressure_plate", properties -> new PressurePlateBlock(BlockSetType.OAK, properties))
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_PRESSURE_PLATE)
            .properties(properties -> properties.noCollission().strength(0.5f))
            .item()
            .setData(ProviderType.ITEM_MODEL, (ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/orchard_heartwood_pressure_plate"))).build()
            .blockstate((ctx, provider) -> provider.pressurePlateBlock(ctx.get(), provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_pressure_plate")), provider.models().getExistingFile(provider.modLoc("block/orchard_heartwood_pressure_plate_down"))))
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static final BlockEntry<Block> STONEBARK_PLANKS = woodPlanks("stonebark_planks", MapColor.TERRACOTTA_GRAY);

    public static final BlockEntry<StairBlock> STONEBARK_STAIRS = woodStairs("stonebark_stairs", STONEBARK_PLANKS, MapColor.TERRACOTTA_GRAY);

    public static final BlockEntry<SlabBlock> STONEBARK_SLAB = woodSlab("stonebark_slab", "stonebark_planks", MapColor.TERRACOTTA_GRAY);

    public static final BlockEntry<FenceBlock> STONEBARK_FENCE = woodFence("stonebark_fence", "stonebark_planks", MapColor.TERRACOTTA_GRAY);

    public static final BlockEntry<FenceGateBlock> STONEBARK_FENCE_GATE = woodFenceGate("stonebark_fence_gate", "stonebark_planks", MapColor.TERRACOTTA_GRAY);

    public static final BlockEntry<ButtonBlock> STONEBARK_BUTTON = woodButton("stonebark_button");

    public static final BlockEntry<PressurePlateBlock> STONEBARK_PRESSURE_PLATE = woodPressurePlate("stonebark_pressure_plate");

    public static final BlockEntry<Block> VINEHEART_TIMBER_PLANKS = woodPlanks("vineheart_timber_planks", MapColor.COLOR_BROWN);

    public static final BlockEntry<StairBlock> VINEHEART_TIMBER_STAIRS = woodStairs("vineheart_timber_stairs", VINEHEART_TIMBER_PLANKS, MapColor.COLOR_BROWN);

    public static final BlockEntry<SlabBlock> VINEHEART_TIMBER_SLAB = woodSlab("vineheart_timber_slab", "vineheart_timber_planks", MapColor.COLOR_BROWN);

    public static final BlockEntry<FenceBlock> VINEHEART_TIMBER_FENCE = woodFence("vineheart_timber_fence", "vineheart_timber_planks", MapColor.COLOR_BROWN);

    public static final BlockEntry<FenceGateBlock> VINEHEART_TIMBER_FENCE_GATE = woodFenceGate("vineheart_timber_fence_gate", "vineheart_timber_planks", MapColor.COLOR_BROWN);

    public static final BlockEntry<ButtonBlock> VINEHEART_TIMBER_BUTTON = woodButton("vineheart_timber_button");

    public static final BlockEntry<PressurePlateBlock> VINEHEART_TIMBER_PRESSURE_PLATE = woodPressurePlate("vineheart_timber_pressure_plate");

    public static final BlockEntry<Block> VERDANT_GRACE_PLANKS = woodPlanks("verdant_grace_planks", MapColor.COLOR_GREEN);

    public static final BlockEntry<StairBlock> VERDANT_GRACE_STAIRS = woodStairs("verdant_grace_stairs", VERDANT_GRACE_PLANKS, MapColor.COLOR_GREEN);

    public static final BlockEntry<SlabBlock> VERDANT_GRACE_SLAB = woodSlab("verdant_grace_slab", "verdant_grace_planks", MapColor.COLOR_GREEN);

    public static final BlockEntry<FenceBlock> VERDANT_GRACE_FENCE = woodFence("verdant_grace_fence", "verdant_grace_planks", MapColor.COLOR_GREEN);

    public static final BlockEntry<FenceGateBlock> VERDANT_GRACE_FENCE_GATE = woodFenceGate("verdant_grace_fence_gate", "verdant_grace_planks", MapColor.COLOR_GREEN);

    public static final BlockEntry<ButtonBlock> VERDANT_GRACE_BUTTON = woodButton("verdant_grace_button");

    public static final BlockEntry<PressurePlateBlock> VERDANT_GRACE_PRESSURE_PLATE = woodPressurePlate("verdant_grace_pressure_plate");

    public static final BlockEntry<ChairBlock> SOLARWOODCHAIR = decorativeChair("solarwoodchair");

    public static final BlockEntry<ChairBlock> ORCHARDCHAIR = decorativeChair("orchardchair");

    public static final BlockEntry<ChairBlock> STONEBARKCHAIR = decorativeChair("stonebarkchair");

    public static final BlockEntry<ChairBlock> VINEHEARTCHAIR = decorativeChair("vineheartchair");

    public static final BlockEntry<ChairBlock> VERDANTGRACECHAIR = decorativeChair("verdantgracechair");

    public static BlockEntry<ChairBlock> decorativeChair(String name) {
        return REGISTRATE.block(name, ChairBlock::new)
                .initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_PLANKS)
                .properties(properties -> properties.strength(1.5f).sound(SoundType.WOOD).noOcclusion())
                .item().build()
                .blockstate((ctx, provider) -> provider.simpleBlock(ctx.get(),
                        provider.models().getExistingFile(provider.modLoc("block/" + ctx.getName()))))
                .loot((loot, block) -> loot.dropSelf(block))
                .register();
    }

    public static <T extends Block> BlockEntry<T> horizontalDecorativeBlock(
            String name, Function<BlockBehaviour.Properties, T> factory, Block propertiesSource,
            Function<BlockBehaviour.Properties, BlockBehaviour.Properties> properties) {
        return REGISTRATE.block(name, factory::apply)
                .initialProperties(() -> propertiesSource)
                .properties(properties::apply)
                .item().build()
                .blockstate((ctx, provider) -> provider.horizontalBlock(ctx.get(),
                        provider.models().getExistingFile(provider.modLoc("block/" + ctx.getName()))))
                .loot((loot, block) -> loot.dropSelf(block))
                .register();
    }

    public static final BlockEntry<ChineseKnottingBlock> CHINESE_KNOTTING = horizontalDecorativeBlock(
            "chinese_knotting", ChineseKnottingBlock::new, net.minecraft.world.level.block.Blocks.WHITE_WOOL,
            properties -> properties.instabreak().noCollission().sound(SoundType.WOOL));

    public static final BlockEntry<LampCabinetBlock> LAMP_CABINET = horizontalDecorativeBlock(
            "lamp_cabinet", LampCabinetBlock::new, net.minecraft.world.level.block.Blocks.GLASS,
            properties -> properties.strength(1.5f).sound(SoundType.GLASS).lightLevel(state -> 15));

    public static final BlockEntry<DecorativeBlock> CANVAS_SCREEN_1 = REGISTRATE
            .block("canvas_screen_1", DecorativeBlock::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.WHITE_WOOL)
            .properties(properties -> properties.instabreak().noCollission().sound(SoundType.WOOL))
            .item().build()
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.get(), provider.models().getExistingFile(provider.modLoc("block/canvas_screen_1"))))
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static final BlockEntry<DecorativeBlock> CANVAS_SCREEN_2 = REGISTRATE
            .block("canvas_screen_2", DecorativeBlock::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.WHITE_WOOL)
            .properties(properties -> properties.instabreak().noCollission().sound(SoundType.WOOL))
            .item().build()
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.get(), provider.models().getExistingFile(provider.modLoc("block/canvas_screen_2"))))
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static final BlockEntry<DecorativeBlock> INCENSE_BURNER = REGISTRATE
            .block("incense_burner", properties -> new DecorativeBlock(properties, Block.box(3, 0, 3, 13, 10, 13)))
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.STONE)
            .properties(properties -> properties.strength(1.5f).sound(SoundType.STONE))
            .item().build()
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.get(), provider.models().getExistingFile(provider.modLoc("block/incense_burner"))))
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static final BlockEntry<DecorativeBlock> PLANK_HANGING_LIGHT = REGISTRATE
            .block("plank_hanging_light", properties -> new DecorativeBlock(properties.lightLevel(state -> 15), Block.box(3, 0, 3, 13, 16, 13)))
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.GLASS)
            .properties(properties -> properties.strength(1.5f).sound(SoundType.GLASS))
            .item().build()
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.get(), provider.models().getExistingFile(provider.modLoc("block/plank_hanging_light"))))
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static final BlockEntry<DecorativeBlock> REDLANTERN = REGISTRATE
            .block("redlantern", properties -> new DecorativeBlock(
                    properties.lightLevel(state -> 15), Block.box(1, 0, 1, 15, 16, 15)))
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.GLASS)
            .properties(properties -> properties.strength(1.5f).sound(SoundType.GLASS))
            .item()
            .build()
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.get(),
                    provider.models().getExistingFile(provider.modLoc("block/redlantern"))))
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static final BlockEntry<DecorativeBlock> GOLDLANTERN = REGISTRATE
            .block("goldlantern", properties -> new DecorativeBlock(
                    properties.lightLevel(state -> 15), Block.box(1, 0, 1, 15, 16, 15)))
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.GLASS)
            .properties(properties -> properties.strength(1.5f).sound(SoundType.GLASS))
            .item()
            .build()
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.get(),
                    provider.models().getExistingFile(provider.modLoc("block/goldlantern"))))
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static final BlockEntry<DecorativeBlock> STONE_LION = REGISTRATE
            .block("stone_lion", properties -> new DecorativeBlock(properties, Block.box(0, 0, 0, 16, 29, 16)))
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.STONE)
            .properties(properties -> properties.strength(2.0f).sound(SoundType.STONE))
            .item()
            .build()
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.get(),
                    provider.models().getExistingFile(provider.modLoc("block/stone_lion"))))
            .loot((loot, block) -> loot.dropSelf(block))
            .register();

    public static final DeferredBlock<FIDCropBlock> CHINESE_LEAVES_CROP = REGISTRY.register("chineseleavesseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> CHINESE_LEAVES_SEEDS.get(), () -> CHINESE_LEAVES.get()));

    public static final DeferredBlock<FIDCropBlock> ANISEED_0_CROP = REGISTRY.register("aniseed_0",
            () -> new FIDCropBlock(cropProperties(), 4, () -> ANISEED_0.get(), () -> ANISEED_0.get()));

    public static final DeferredBlock<FIDCropBlock> RADISHSEED_CROP = REGISTRY.register("radishseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> RADISHSEED.get(), () -> RADISH.get()));

    public static final DeferredBlock<MultiStageInteractiveBlock> DRAWNEGGPLANT_BLOCK = REGISTRY.register("drawneggplant",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "drawneggplant", 
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .forceSolidOn()
                            .strength(0.2F)
                            .sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> LINYIFRIEDCHICKEN_BLOCK = REGISTRY.register("linyifriedchicken",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "linyifriedchicken", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> MEATBALLSOUP_BLOCK = REGISTRY.register("meatballsoup",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "meatballsoup", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> PRESERVEDEGGSALAD_BLOCK = REGISTRY.register("preservedeggsalad",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "preservedeggsalad", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> TOMATOSALAD_BLOCK = REGISTRY.register("tomatosalad",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "tomatosalad", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> STEAMED_CHICKENWITH_CHILI_SAUCE_BLOCK = REGISTRY.register("steamed_chickenwith_chili_sauce",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "steamed_chickenwith_chili_sauce", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> COLACHICKENWINGS_BLOCK = REGISTRY.register("colachickenwings",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "colachickenwings", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> FOURJOYMEATBALLS_BLOCK = REGISTRY.register("fourjoymeatballs",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "fourjoymeatballs", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> STIRFRIEDBOILEDPORKSLICESINHOTSAUCE_BLOCK = REGISTRY.register("stirfriedboiledporkslicesinhotsauce",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "stirfriedboiledporkslicesinhotsauce", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> SAUTEED_POTATO_GREEN_PEPPER_EGGPLANT_BLOCK = REGISTRY.register("sauteed_potato_green_pepper_eggplant",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "sauteed_potato_green_pepper_eggplant", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> FRIEDMEATWITHCUMINONION_BLOCK = REGISTRY.register("friedmeatwithcuminonion",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "friedmeatwithcuminonion", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> KUNGPAOCHICKEN_BLOCK = REGISTRY.register("kungpaochicken",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "kungpaochicken", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> STIRFRIEDSTRINGBEANS_BLOCK = REGISTRY.register("stirfriedstringbeans",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "stirfriedstringbeans", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> MIXEDCOLDDISHES_BLOCK = REGISTRY.register("mixedcolddishes",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "mixedcolddishes", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> JAPANESEBRAISEDTOFU_BLOCK = REGISTRY.register("japanesebraisedtofu",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "japanesebraisedtofu", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> SCRAMBLEDEGGSWITHFUNGUSANDCUCUMBER_BLOCK = REGISTRY.register("scrambledeggswithfungusandcucumber",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "scrambledeggswithfungusandcucumber", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> PLEUROTUSERYNGIIWITHSALTANDPEPPER_BLOCK = REGISTRY.register("pleurotuseryngiiwithsaltandpepper",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "pleurotuseryngiiwithsaltandpepper", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> POACHED_SPICY_SLICESOF_PORK_BLOCK = REGISTRY.register("poached_spicy_slicesof_pork",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "poached_spicy_slicesof_pork", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> SLICED_FISHIN_HOT_CHILI_OIL_BLOCK = REGISTRY.register("sliced_fishin_hot_chili_oil",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "sliced_fishin_hot_chili_oil", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> SAUTEEDMUSHROOMSWITHRAPESEED_BLOCK = REGISTRY.register("sauteedmushroomswithrapeseed",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "sauteedmushroomswithrapeseed", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> STEAMEDFISH_BLOCK = REGISTRY.register("steamedfish",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "steamedfish", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> FRIEDCOWPEA_BLOCK = REGISTRY.register("friedcowpea",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "friedcowpea", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> FRIEDSPICYCHICKEN_BLOCK = REGISTRY.register("friedspicychicken",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "friedspicychicken", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> BOILED_CHICKENWITH_SAUCE_BLOCK = REGISTRY.register("boiled_chickenwith_sauce",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "boiled_chickenwith_sauce", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> STEWEDPORKWITHBROWNSAUCE_BLOCK = REGISTRY.register("stewedporkwithbrownsauce",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "stewedporkwithbrownsauce", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> FRIEDLIVERTIPWITHSPINACH_BLOCK = REGISTRY.register("friedlivertipwithspinach",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "friedlivertipwithspinach", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> PINEAPPLE_SWEETAND_SOUR_PORK_BLOCK = REGISTRY.register("pineapple_sweetand_sour_pork",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "pineapple_sweetand_sour_pork", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> CHICKENWITH_SCALLION_OIL_BLOCK = REGISTRY.register("chickenwith_scallion_oil",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "chickenwith_scallion_oil", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> SALTEDEGGYOLKFRIEDCAULIFLOWER_BLOCK = REGISTRY.register("saltedeggyolkfriedcauliflower",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "saltedeggyolkfriedcauliflower", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> ZUCCHINISNACKMEAT_BLOCK = REGISTRY.register("zucchinisnackmeat",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "zucchinisnackmeat", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> BOILED_FISHWITH_PICKLED_CABBAGEAND_CHILI_BLOCK = REGISTRY.register("boiled_fishwith_pickled_cabbageand_chili",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "boiled_fishwith_pickled_cabbageand_chili", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> FRIEDSHREDDEDPORKWITHSWEETANDSOURSAUCE_BLOCK = REGISTRY.register("friedshreddedporkwithsweetandsoursauce",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "friedshreddedporkwithsweetandsoursauce", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> SPICYTOFU_BLOCK = REGISTRY.register("spicytofu",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "spicytofu", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> BEANWITHSESAMESAUCE_BLOCK = REGISTRY.register("beanwithsesamesauce",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "beanwithsesamesauce", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<MultiStageInteractiveBlock> SPICYCABBAGE_BLOCK = REGISTRY.register("spicycabbage",
            () -> new MultiStageInteractiveBlock(2, 0.3f, "spicycabbage", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredBlock<FIDCropBlock> KAOLIANGGARIN_CROP = REGISTRY.register("kao_liang_seed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> KAOLIANG_SEED.get(), () -> KAOLIANGGRAIN.get()));

    public static final DeferredBlock<FIDLogMushroomBlock> WHITEMUSHROOM_CROP = REGISTRY.register("white_mushroom_seed",
            () -> new FIDLogMushroomBlock(mushroomProperties(), 4, () -> WHITE_MUSHROOM_SEED.get(), () -> WHITEMUSHROOM.get()));

    public static final DeferredBlock<FIDLogMushroomBlock> BLACKFUNGUS_CROP = REGISTRY.register("blackfungsseed",
            () -> new FIDLogMushroomBlock(mushroomProperties(), 4, () -> BLACKFUNGSSEED.get(), () -> BLACKFUNGUS.get()));

    public static final DeferredBlock<FIDLogMushroomBlock> PLEUROTUSERYNGII_CROP = REGISTRY.register("pleurotusseed",
            () -> new FIDLogMushroomBlock(mushroomProperties(), 4, () -> PLEUROTUSSEED.get(), () -> PLEUROTUSERYNGII.get()));

    public static final DeferredBlock<FIDLogMushroomBlock> ENOKIMUSHROOM_CROP = REGISTRY.register("enokimushroomseed",
            () -> new FIDLogMushroomBlock(mushroomProperties(), 4, () -> ENOKIMUSHROOMSEED.get(), () -> ENOKIMUSHROOM.get()));

    public static final DeferredBlock<FIDLogMushroomBlock> TREMELLA_CROP = REGISTRY.register("tremellaseed",
            () -> new FIDLogMushroomBlock(mushroomProperties(), 4, () -> TREMELLASEED.get(), () -> TREMELLA.get()));

    public static final DeferredBlock<FIDLogMushroomBlock> FRAGRANTMUSHROOM_CROP = REGISTRY.register("fragrantseed",
            () -> new FIDLogMushroomBlock(mushroomProperties(), 4, () -> FRAGRANTSEED.get(), () -> FRAGRANTMUSHROOM.get()));

    public static final DeferredBlock<FIDCropBlock> BLUEBERRY_CROP = REGISTRY.register("blueberryseed",
            () -> new FIDCropBlock(cropProperties(), 3, () -> BLUEBERRYSEED.get(), () -> BLUEBERRY.get()));

    public static final DeferredBlock<FIDCropBlock> DRAGONFRUIT_CROP = REGISTRY.register("dragonfruitseed",
            () -> new FIDCropBlock(cropProperties(), 3, () -> DRAGONFRUITSEED.get(), () -> DRAGONFRUIT.get()));

    public static final DeferredBlock<FIDCropBlock> GREENTEALEAVES_CROP = REGISTRY.register("greentealeavesseed",
            () -> new FIDCropBlock(cropProperties(), 3, () -> GREENTEALEAVESSEED.get(), () -> GREENTEALEAVES.get()));

    public static final DeferredBlock<FIDCropBlock> HAMIMELON_CROP = REGISTRY.register("hamimelonseed",
            () -> new FIDCropBlock(cropProperties(), 3, () -> HAMIMELONSEED.get(), () -> HAMIMELON.get()));

    public static final DeferredBlock<FIDCropBlock> PINEAPPLE_CROP = REGISTRY.register("pineappleseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> PINEAPPLESEED.get(), () -> PINEAPPLE.get()));

    public static final DeferredBlock<FIDCropBlock> RED_TEA_CROP = REGISTRY.register("red_tea_seed",
            () -> new FIDCropBlock(cropProperties(), 3, () -> RED_TEA_SEED.get(), () -> REDTEALEAVES.get()));

    public static final DeferredBlock<FIDCropBlock> STRAWBERRY_CROP = REGISTRY.register("strawberryseed",
            () -> new FIDCropBlock(cropProperties(), 3, () -> STRAWBERRYSEED.get(), () -> STRAWBERRY.get()));

    public static final DeferredBlock<FIDWaterCropBlock> LOTUSROOT_CROP = REGISTRY.register("lotusrootseed",
            () -> new FIDWaterCropBlock(waterCropProperties(), 4, () -> LOTUSROOTSEED.get(), () -> LOTUSROOT.get()));

    public static final DeferredBlock<FIDWaterCropBlock> GLUTINOUSRICE_CROP = REGISTRY.register("glutinousseeds",
            () -> new FIDWaterCropBlock(waterCropProperties(), 4, () -> GLUTINOUSSEEDS.get(), () -> POLISHEDGLUTINOUSRICE_2.get()));

    public static final DeferredBlock<FIDWaterCropBlock> PADDY_CROP = REGISTRY.register("paddyseeds",
            () -> new FIDWaterCropBlock(waterCropProperties(), 4, () -> PADDYSEEDS.get(), () -> PADDYGRAIN.get()));

    public static final DeferredBlock<FIDCropBlock> BROCCOILSEED_CROP = REGISTRY.register("broccoilseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> BROCCOILSEED.get(), () -> BROCCOIL.get()));

    public static final DeferredBlock<FIDCropBlock> BUCKWHEATSEED_CROP = REGISTRY.register("buckwheatseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> BUCKWHEATSEED.get(), () -> BUCKWHEAT.get()));

    public static final DeferredBlock<FIDCropBlock> CABBAGESEED_CROP = REGISTRY.register("cabbageseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> CABBAGESEED.get(), () -> CABBAGE.get()));

    public static final DeferredBlock<FIDCropBlock> CASSAVASEEDS_CROP = REGISTRY.register("cassavaseeds",
            () -> new FIDCropBlock(cropProperties(), 4, () -> CASSAVASEEDS.get(), () -> CASSAVA.get()));

    public static final DeferredBlock<FIDCropBlock> CELERYSEED_CROP = REGISTRY.register("celeryseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> CELERYSEED.get(), () -> CELERY.get()));

    public static final DeferredBlock<FIDCropBlock> CHINESECHIVESSEED_CROP = REGISTRY.register("chinesechivesseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> CHINESECHIVESSEED.get(), () -> CHINESECHIVES.get()));

    public static final DeferredBlock<FIDCropBlock> CHINESEYAMSEED_CROP = REGISTRY.register("chineseyamseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> CHINESEYAMSEED.get(), () -> CHINESEYAM.get()));

    public static final DeferredBlock<FIDCropBlock> COFFEEBEANSEED_CROP = REGISTRY.register("coffeebeanseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> COFFEEBEANSEED.get(), () -> COFFEEBEANSEED.get()));

    public static final DeferredBlock<FIDCropBlock> CORNSEED_CROP = REGISTRY.register("cornseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> CORNSEED.get(), () -> CORN.get()));

    public static final DeferredBlock<FIDCropBlock> CUMINSEED_CROP = REGISTRY.register("cuminseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> CUMINSEED.get(), () -> CUMIN.get()));

    public static final DeferredBlock<FIDCropBlock> FENNELSEEDSTATES_CROP = REGISTRY.register("fennelseedstates",
            () -> new FIDCropBlock(cropProperties(), 4, () -> FENNELSEEDSTATES.get(), () -> FENNEL.get()));

    public static final DeferredBlock<FIDCropBlock> GARLICSEED_CROP = REGISTRY.register("garlicseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> GARLICSEED.get(), () -> GARLIC.get()));

    public static final DeferredBlock<FIDCropBlock> GINGER_SEED_CROP = REGISTRY.register("ginger_seed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> GINGER_SEED.get(), () -> GINGER.get()));

    public static final DeferredBlock<FIDCropBlock> GREENPEPPERSEEDS_CROP = REGISTRY.register("greenpepperseeds",
            () -> new FIDCropBlock(cropProperties(), 4, () -> GREENPEPPERSEEDS.get(), () -> GREENPEPPER.get()));

    public static final DeferredBlock<FIDCropBlock> GUMBOSEED_CROP = REGISTRY.register("gumboseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> GUMBOSEED.get(), () -> GUMBO.get()));

    public static final DeferredBlock<FIDCropBlock> MILLET_CROP = REGISTRY.register("millet",
            () -> new FIDCropBlock(cropProperties(), 4, () -> MILLET.get(), () -> MILLETGRAIN_GRAIN.get()));

    public static final DeferredBlock<FIDCropBlock> MUNGBEANPLANT_CROP = REGISTRY.register("mungbeanplant",
            () -> new FIDCropBlock(cropProperties(), 4, () -> MUNGBEANPLANT.get(), () -> MUNGBEAN.get()));

    public static final DeferredBlock<FIDCropBlock> MUSTRAD_SEED_CROP = REGISTRY.register("mustrad_seed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> MUSTRAD_SEED.get(), () -> MUSTARD.get()));

    public static final DeferredBlock<FIDCropBlock> NUTMEGSEED_CROP = REGISTRY.register("nutmegseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> NUTMEGSEED.get(), () -> NUTMEGSEED.get()));

    public static final DeferredBlock<FIDCropBlock> OATSEED_CROP = REGISTRY.register("oatseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> OATSEED.get(), () -> OAT.get()));

    public static final DeferredBlock<FIDCropBlock> OILRAPESEED_CROP = REGISTRY.register("oilrapeseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> OILRAPESEED.get(), () -> OILSEEDRAPE.get()));

    public static final DeferredBlock<FIDCropBlock> ONIONSEED_CROP = REGISTRY.register("onionseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> ONIONSEED.get(), () -> ONION.get()));

    public static final DeferredBlock<FIDCropBlock> PEASEED_CROP = REGISTRY.register("peaseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> PEASEED.get(), () -> PEA.get()));

    public static final DeferredBlock<FIDCropBlock> PUPLESWEETPOTATOSEED_CROP = REGISTRY.register("puplesweetpotatoseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> PUPLESWEETPOTATOSEED.get(), () -> PURPLESWEETPOTATO.get()));

    public static final DeferredBlock<FIDCropBlock> SESAMESEED_CROP = REGISTRY.register("sesameseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> SESAMESEED.get(), () -> SESAME.get()));

    public static final DeferredBlock<FIDCropBlock> SOY_BEAN_SEED_CROP = REGISTRY.register("soy_bean_seed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> SOY_BEAN_SEED.get(), () -> SOYBEAN.get()));

    public static final DeferredBlock<FIDCropBlock> SWEETGREENPEPPERSEED_CROP = REGISTRY.register("sweetgreenpepperseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> SWEETGREENPEPPERSEED.get(), () -> SWEETGREENPEPPER.get()));

    public static final DeferredBlock<FIDCropBlock> ZUCCHINISEED_CROP = REGISTRY.register("zucchiniseed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> ZUCCHINISEED.get(), () -> ZUCCHINI.get()));

    public static final DeferredBlock<FIDCropBlock> SPINACH_SEED_CROP = REGISTRY.register("spinach_seed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> SPINACH_SEED.get(), () -> SPINACH.get()));

    public static final DeferredBlock<FIDCropBlock> CAULIFLOWER_SEED_CROP = REGISTRY.register("cauliflower_seed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> CAULIFLOWER_SEED.get(), () -> CAULIFLOWER.get()));

    public static final DeferredBlock<FIDCropBlock> SCALLION_SEED_CROP = REGISTRY.register("scallion_seed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> SCALLION_SEED.get(), () -> SACLLION.get()));

    public static final DeferredBlock<FIDCropBlock> LILAC_SEED_CROP = REGISTRY.register("lilac_seed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> LILAC_SEED.get(), () -> LILAC_SEED.get()));

    public static final DeferredBlock<FIDCropBlock> RED_BEAN_BLOCK_CROP = REGISTRY.register("red_bean_block",
            () -> new FIDCropBlock(cropProperties(), 4, () -> RED_BEAN_BLOCK.get(), () -> RED_BEAN_BLOCK.get()));

    public static final DeferredBlock<FIDCropBlock> RED_PEPPER_SEED_CROP = REGISTRY.register("red_pepper_seed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> RED_PEPPER_SEED.get(), () -> REDREPPER.get()));

    public static final DeferredBlock<FIDCropBlock> SWEET_POTATO_SEED_CROP = REGISTRY.register("sweet_potato_seed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> SWEET_POTATO_SEED.get(), () -> SWEETPOTATO.get()));

    public static final DeferredBlock<FIDCropBlock> SI_CHUAN_PEPPER_SEED_CROP = REGISTRY.register("si_chuan_pepper_seed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> SI_CHUAN_PEPPER_SEED.get(), () -> SI_CHUAN_PEPPER_SEED.get()));

    public static final DeferredBlock<FIDCropBlock> PEA_NUT_SEED_CROP = REGISTRY.register("pea_nut_seed",
            () -> new FIDCropBlock(cropProperties(), 4, () -> PEA_NUT_SEED.get(), () -> PEANUT.get()));

    public static final BlockEntry<BighookBlock> BIGHOOK = REGISTRATE
            .block("bighook", properties -> new BighookBlock(DEADCATTLE, DEADSHEEP, DEADPIG, DEADCHICKEN, CHICKENWITHOUTFEATHER, CHICKENWITHOUTBLOOD))
            .item((block, properties) -> new TooltipBlockItem(block, properties,
                    Component.translatable("tooltip.flavor_immersed_daily.bighook"),
                    () -> java.util.List.of(new ItemStack(DEADCATTLE.get()), new ItemStack(DEADSHEEP.get()),
                            new ItemStack(DEADPIG.get()), new ItemStack(DEADCHICKEN.get()),
                            new ItemStack(CHICKENWITHOUTFEATHER.get()), new ItemStack(CHICKENWITHOUTBLOOD.get()))))
            .build()
            .register();

    public static final DeferredBlock<JuiceBlock> HAMIMELONJUICE_BLOCK = REGISTRY.register("hamimelonjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> HAWTHORNJUIVE_BLOCK = REGISTRY.register("hawthornjuive",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> DRAGONFRUIEJUICE_BLOCK = REGISTRY.register("dragonfruiejuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> CARAMBOLAJUICE_BLOCK = REGISTRY.register("carambolajuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> WINTERJUJUBEJUICE_BLOCK = REGISTRY.register("winterjujubejuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> POMEGRANATEJUICE_BLOCK = REGISTRY.register("pomegranatejuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> SWEETBERRYJUICE_BLOCK = REGISTRY.register("sweetberryjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> DURIANJUICE_BLOCK = REGISTRY.register("durianjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> LEMONJUICE_BLOCK = REGISTRY.register("lemonjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> BLUEBERRYJUICE_BLOCK = REGISTRY.register("blueberryjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> SWEETMELONJUICE_BLOCK = REGISTRY.register("sweetmelonjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> GREENGRAPEJUICE_BLOCK = REGISTRY.register("greengrapejuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> TANGERINEJUICE_BLOCK = REGISTRY.register("tangerinejuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> MANGOSTEENJUICE_BLOCK = REGISTRY.register("mangosteenjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> CHERRYJUICE_BLOCK = REGISTRY.register("cherryjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> STRAWBERRYJUICE_BLOCK = REGISTRY.register("strawberryjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> ORANGEJUICE_BLOCK = REGISTRY.register("orangejuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> GREENPLUMJUICE_BLOCK = REGISTRY.register("greenplumjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> APPLEJUICE_BLOCK = REGISTRY.register("applejuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> HONEYPEACHJUIVE_BLOCK = REGISTRY.register("honeypeachjuive",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> BANANAJUICE_BLOCK = REGISTRY.register("bananajuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> LYCHEEJUICE_BLOCK = REGISTRY.register("lycheejuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> APRICOTJUICE_BLOCK = REGISTRY.register("apricotjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> GRAPEJUICE_BLOCK = REGISTRY.register("grapejuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> MULBERRYJUIVE_BLOCK = REGISTRY.register("mulberryjuive",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> PKUMJUICE_BLOCK = REGISTRY.register("pkumjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> MANGOJUICE_BLOCK = REGISTRY.register("mangojuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> WATERMELONJUICE_BLOCK = REGISTRY.register("watermelonjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> NECTARINEJUICE_BLOCK = REGISTRY.register("nectarinejuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> PINEAPPLEJUICE_BLOCK = REGISTRY.register("pineapplejuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> LOQUATJUICE_BLOCK = REGISTRY.register("loquatjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> KIWIFRUITJUICE_BLOCK = REGISTRY.register("kiwifruitjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> PEARJUICE_BLOCK = REGISTRY.register("pearjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> PAWPAWJUICE_BLOCK = REGISTRY.register("pawpawjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredBlock<JuiceBlock> COCONUTJUICE_BLOCK = REGISTRY.register("coconutjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final BlockEntry<TrellisBlock> TRELLIS = REGISTRATE
            .block("trellis", TrellisBlock::new)
            .properties(properties -> properties.strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().ignitedByLava())
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item()
            .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/trellis_stage0")))
            .build()
            .register();

    public static final DeferredBlock<GrapeBlock> GRAPEBLOCK = REGISTRY.register("grapeblock", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks(), () -> GRAPESEED.get(), () -> GRAPE.get()));

    public static final DeferredBlock<GrapeBlock> CUCUMBERBLOCK = REGISTRY.register("cucumberseeds", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks(), () -> CUCUMBERSEEDS.get(), () -> CUCUMBER.get()));

    public static final DeferredBlock<GrapeBlock> WAXGOURDBLOCK = REGISTRY.register("wax_gourd_seed_block", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks(), () -> WAX_GOURD_SEED_BLOCK.get(), () -> WAXGOURD.get()));

    public static final DeferredBlock<GrapeBlock> KIDNEYBEANBLOCK = REGISTRY.register("kidneybeanblock", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks(), () -> KIDNEYBEANSEED.get(), () -> KIDNEYBEAN.get()));

    public static final DeferredBlock<GrapeBlock> AUBERGINEBLOCK = REGISTRY.register("aubergineseedblock", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks(), () -> AUBERGINESEEDBLOCK.get(), () -> AUBERGINE.get()));

    public static final DeferredBlock<GrapeBlock> TOMATOBLOCK = REGISTRY.register("tomatoblock", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks(), () -> TOMATOSEED.get(), () -> TOMATO.get()));

    public static final DeferredBlock<GrapeBlock> COWPEABLOCK = REGISTRY.register("cowpeabeanseed", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks(), () -> COWPEABEANSEED.get(), () -> COWPEA.get()));

    public static final DeferredBlock<GrapeBlock> GREENGRAEBLOCK = REGISTRY.register("greengrapeblock", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks(), () -> GREENGRAESEED.get(), () -> GREENGRAPE.get()));

    public static final DeferredBlock<GrapeBlock> LOOFAHBLOCK = REGISTRY.register("loofahblock", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks(), () -> LOOFAHSEED.get(), () -> LOOFAH.get()));

    public static final BlockEntry<RawBananaBlock> RAWBANANA = REGISTRATE.block("rawbanana",
            properties -> new RawBananaBlock(properties
                    .noCollission().noOcclusion().randomTicks()
                    .sound(SoundType.GRASS).instabreak(),
                    BANANAWOOD, BANANA))
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item()
            .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/rawbanana_stage0")))
            .build()
            .register();

    public static final BlockEntry<BananaSaplingBlock> BANANA_SAPLING = REGISTRATE.block("bananasapling",
            properties -> new BananaSaplingBlock(properties
                    .mapColor(MapColor.PLANT).noCollission().randomTicks()
                    .instabreak().sound(SoundType.GRASS),
                    BANANAWOOD, RAWBANANA))
            .item((block, properties) -> new TooltipBlockItem(block, properties,
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), java.util.List::of))
            .build()
            .register();

    public static final BlockEntry<CinnamonLeavesBlock> CINNAMONLEAVES = REGISTRATE.block("cinnamonleaves",
            properties -> new CinnamonLeavesBlock(properties
                    .mapColor(MapColor.PLANT)
                    .strength(0.2f)
                    .randomTicks()
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .isSuffocating((s, l, p) -> false)
                    .isViewBlocking((s, l, p) -> false)))
            .item().build()
            .register();

    public static final BlockEntry<CinnamonSaplingBlock> CINNAMON_SAPLING = REGISTRATE.block("cinnamon_sapling",
            properties -> new CinnamonSaplingBlock(properties
                    .mapColor(MapColor.PLANT).noCollission().randomTicks()
                    .instabreak().sound(SoundType.GRASS),
                    CINNAMONWOOD, CINNAMONLEAVES))
            .item((block, itemProperties) -> new TooltipBlockItem(block, itemProperties,
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), java.util.List::of))
            .build()
            .register();

    public static final BlockEntry<RotatedPillarBlock> VERDANT_GRACE_LOG = REGISTRATE.block("verdant_grace_log",
            properties -> new RotatedPillarBlock(properties
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(2.0f)
                    .sound(SoundType.WOOD)))
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item().build()
            .register();

    public static final BlockEntry<LeavesBlock> VERDANT_GRACE_LEAVES = REGISTRATE.block("verdant_grace_leaves",
            properties -> new LeavesBlock(properties
                    .mapColor(MapColor.PLANT)
                    .strength(0.2f)
                    .randomTicks()
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .isValidSpawn((state, level, pos, type) -> false)
                    .isSuffocating((state, level, pos) -> false)
                    .isViewBlocking((state, level, pos) -> false)))
            .item().build()
            .register();

    public static final DeferredBlock<FallingFruitBlock> RAWPLUM = REGISTRY.register("rawplum",
            () -> new FallingFruitBlock(PLUM, BlockBehaviour.Properties.of()
                    .strength(0.2f)
                    .noCollission()
                    .noOcclusion()
                    .randomTicks()
                    .sound(SoundType.GRASS)
                    .instabreak()));

    public static final BlockEntry<FruitingLeavesBlock> VERDANT_GRACE_FRUITING_LEAVES = REGISTRATE.block("verdant_grace_fruiting_leaves",
            properties -> new FruitingLeavesBlock(PLUM, RAWPLUM, properties
                    .mapColor(MapColor.COLOR_PINK)
                    .strength(0.2f)
                    .randomTicks()
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .isValidSpawn((state, level, pos, type) -> false)
                    .isSuffocating((state, level, pos) -> false)
                    .isViewBlocking((state, level, pos) -> false)))
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item().build()
            .register();

    public static final BlockEntry<SaplingBlock> PLUM_SAPLING = REGISTRATE.block("plumsapling",
            properties -> new SaplingBlock(new TreeGrower(
                    "plum_tree",
                    0.0f,
                    Optional.empty(),
                    Optional.empty(),
                    Optional.of(PLUM_TREE),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty()
            ), properties
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.GRASS)))
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item((block, itemProperties) -> new TooltipBlockItem(block, itemProperties,
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), java.util.List::of))
            .build()
            .register();

    public static final BlockEntry<LeavesBlock> SOLARWOOD_LEAVES = REGISTRATE.block("solarwood_leaves",
            properties -> new LeavesBlock(properties
                    .mapColor(MapColor.COLOR_YELLOW)
                    .strength(0.2f)
                    .randomTicks()
                    .sound(SoundType.AZALEA_LEAVES)
                    .noOcclusion()
                    .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false)
                    .isSuffocating((blockState, blockGetter, blockPos) -> false)
                    .isViewBlocking((blockState, blockGetter, blockPos) -> false)))
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item().build()
            .register();

    public static final DeferredBlock<FallingFruitBlock> RAWORANGELEAVE = REGISTRY.register("raworangeleave",
            () -> new FallingFruitBlock(ORANGE, BlockBehaviour.Properties.of()
                    .strength(0.2f)
                    .noCollission()
                    .noOcclusion()
                    .randomTicks()
                    .sound(SoundType.GRASS)
                    .instabreak()));

    public static final BlockEntry<FruitingLeavesBlock> ORANGELEAVE_FRUITING_LEAVES = REGISTRATE.block("orangeleave_fruiting_leaves",
            properties -> new FruitingLeavesBlock(ORANGE, RAWORANGELEAVE, properties
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.2f)
                    .randomTicks()
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .isValidSpawn((state, level, pos, type) -> false)
                    .isSuffocating((state, level, pos) -> false)
                    .isViewBlocking((state, level, pos) -> false)))
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item().build()
            .register();

    public static final BlockEntry<SaplingBlock> ORANGE_SAPLING = REGISTRATE.block("orangesapling",
            properties -> new SaplingBlock(new TreeGrower("orangesapling", 0.0f,
                    Optional.empty(), Optional.empty(),
                    Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(MODID, "orange_tree"))),
                    Optional.empty(), Optional.empty(), Optional.empty()),
                    properties.mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)))
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item((block, itemProperties) -> new TooltipBlockItem(block, itemProperties,
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), java.util.List::of))
            .build()
            .register();

    public static final BlockEntry<LeavesBlock> ORCHARD_HEARTWOOD_LEAVES = REGISTRATE.block("orchard_heartwood_leaves",
            properties -> new LeavesBlock(properties
                    .mapColor(MapColor.PLANT)
                    .strength(0.2f).randomTicks().sound(SoundType.AZALEA_LEAVES).noOcclusion()
                    .isValidSpawn((s, g, p, t) -> false).isSuffocating((s, g, p) -> false).isViewBlocking((s, g, p) -> false)))
            .item().build()
            .register();

    public static final BlockEntry<RotatedPillarBlock> STONEBARK_LOG = REGISTRATE.block("stonebark_log",
            properties -> new RotatedPillarBlock(properties
                    .mapColor(MapColor.TERRACOTTA_GRAY)
                    .strength(2.0f)
                    .sound(SoundType.WOOD)))
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item().build()
            .register();

    public static final BlockEntry<LeavesBlock> STONEBARK_LEAVES = REGISTRATE.block("stonebark_leaves",
            properties -> new LeavesBlock(properties
                    .mapColor(MapColor.PLANT)
                    .strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion()
                    .isValidSpawn((s, g, p, t) -> false).isSuffocating((s, g, p) -> false).isViewBlocking((s, g, p) -> false)))
            .item().build()
            .register();

    public static final BlockEntry<RotatedPillarBlock> VINEHEART_TIMBER_LOG = REGISTRATE.block("vineheart_timber_log",
            properties -> new RotatedPillarBlock(properties
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(2.0f)
                    .sound(SoundType.WOOD)))
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item().build()
            .register();

    public static final BlockEntry<LeavesBlock> VINEHEART_TIMBER_LEAVES = REGISTRATE.block("vineheart_timber_leaves",
            properties -> new LeavesBlock(properties
                    .mapColor(MapColor.PLANT)
                    .strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion()
                    .isValidSpawn((s, g, p, t) -> false).isSuffocating((s, g, p) -> false).isViewBlocking((s, g, p) -> false)))
            .item().build()
            .register();

    public static final BlockEntry<AgriculturalAppraisalMachineBlock> AGRICULTURALAPPRAISALMACHINE = REGISTRATE
            .block("agriculturalappraisalmachine", AgriculturalAppraisalMachineBlock::new)
            .properties(properties -> properties.strength(1.5f).sound(SoundType.METAL).noOcclusion())
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item((block, properties) -> new TooltipBlockItem(block, properties,
                    Component.translatable("tooltip.flavor_immersed_daily.agriculturalappraisalmachine"), java.util.List::of))
            .build()
            .register();

    public static final BlockEntry<FridgeBlock> FRIDGE = REGISTRATE
            .block("fridge", FridgeBlock::new)
            .properties(properties -> properties.strength(1.5f).sound(SoundType.METAL).noOcclusion())
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item().build()
            .register();

    public static final BlockEntry<EggBreakingMachineBlock> EGGBREAKINGMACHINE = REGISTRATE
            .block("eggbreakingmachine", EggBreakingMachineBlock::new)
            .properties(properties -> properties.strength(1.5f).sound(SoundType.METAL).noOcclusion())
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item().build()
            .register();

    public static DeferredBlock<FallingFruitBlock> regRaw(String name, DeferredItem<Item> fruit) {
        return REGISTRY.register(name, () -> new FallingFruitBlock(fruit, BlockBehaviour.Properties.of()
                .strength(0.2f).noCollission().noOcclusion().randomTicks().sound(SoundType.GRASS).instabreak()));
    }

    private static BlockEntry<FruitingLeavesBlock> fruitingLeaves(String name, DeferredItem<Item> fruit,
                                                                    DeferredBlock<FallingFruitBlock> raw) {
        return REGISTRATE.block(name, properties -> new FruitingLeavesBlock(fruit, raw, properties
                        .mapColor(MapColor.PLANT).strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion()
                        .isValidSpawn((s, g, p, t) -> false).isSuffocating((s, g, p) -> false).isViewBlocking((s, g, p) -> false)))
                .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
                .item().build()
                .register();
    }

    private static BlockEntry<SaplingBlock> fruitSapling(String name, String treeKey) {
        return REGISTRATE.block(name, properties -> new SaplingBlock(new TreeGrower(name, 0.0f,
                Optional.empty(), Optional.empty(),
                Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(MODID, treeKey))),
                Optional.empty(), Optional.empty(), Optional.empty()),
                properties.mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)))
                .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
                .item((block, itemProperties) -> new TooltipBlockItem(block, itemProperties,
                        Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), java.util.List::of))
                .build()
                .register();
    }

    public static final DeferredBlock<FallingFruitBlock> RAWAPRICOT = regRaw("rawapricot", APRICOT);

    public static final BlockEntry<FruitingLeavesBlock> VERDANT_GRACE_FRUITING_LEAVES_APRICOT = fruitingLeaves("verdant_grace_fruiting_leaves_apricot", APRICOT, RAWAPRICOT);

    public static final BlockEntry<SaplingBlock> APRICOT_SAPLING = fruitSapling("apricotsapling", "apricot_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWCHERRY = regRaw("rawcherry", CHERRY);

    public static final BlockEntry<FruitingLeavesBlock> VERDANT_GRACE_FRUITING_LEAVES_CHERRY = fruitingLeaves("verdant_grace_fruiting_leaves_cherry", CHERRY, RAWCHERRY);

    public static final BlockEntry<SaplingBlock> CHERRY_SAPLING = fruitSapling("cherrysapling", "cherry_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWGREENPLUM = regRaw("rawgreenplum", GREENPLUM);

    public static final BlockEntry<FruitingLeavesBlock> VERDANT_GRACE_FRUITING_LEAVES_GREENPLUM = fruitingLeaves("verdant_grace_fruiting_leaves_greenplum", GREENPLUM, RAWGREENPLUM);

    public static final BlockEntry<SaplingBlock> GREENPLUM_SAPLING = fruitSapling("greenplumsapling", "greenplum_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWHAWTHORN = regRaw("rawhawthorn", HAWTHORN);

    public static final BlockEntry<FruitingLeavesBlock> VERDANT_GRACE_FRUITING_LEAVES_HAWTHORN = fruitingLeaves("verdant_grace_fruiting_leaves_hawthorn", HAWTHORN, RAWHAWTHORN);

    public static final BlockEntry<SaplingBlock> HAWTHORN_SAPLING = fruitSapling("hawthornsapling", "hawthorn_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWLOQUAT = regRaw("rawloquat", LOQUAT);

    public static final BlockEntry<FruitingLeavesBlock> VERDANT_GRACE_FRUITING_LEAVES_LOQUAT = fruitingLeaves("verdant_grace_fruiting_leaves_loquat", LOQUAT, RAWLOQUAT);

    public static final BlockEntry<SaplingBlock> LOQUAT_SAPLING = fruitSapling("loquatleavesapling", "loquat_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWPOMEGRANATE = regRaw("rawpomegranate", POMEGRANATE);

    public static final BlockEntry<FruitingLeavesBlock> VERDANT_GRACE_FRUITING_LEAVES_POMEGRANATE = fruitingLeaves("verdant_grace_fruiting_leaves_pomegranate", POMEGRANATE, RAWPOMEGRANATE);

    public static final BlockEntry<SaplingBlock> POMEGRANATE_SAPLING = fruitSapling("pomegranatesapling", "pomegranate_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWCARAMBOLA = regRaw("rawcarambola", CARAMBOLA);

    public static final BlockEntry<FruitingLeavesBlock> CARAMBOLEAVE_FRUITING_LEAVES = fruitingLeaves("carambolaleave_fruiting_leaves", CARAMBOLA, RAWCARAMBOLA);

    public static final BlockEntry<SaplingBlock> CARAMBOLA_SAPLING = fruitSapling("carambolasapling", "carambola_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWDURIAN = regRaw("rawdurian", DURIAN);

    public static final BlockEntry<FruitingLeavesBlock> DURIANLEAVE_FRUITING_LEAVES = fruitingLeaves("durianleave_fruiting_leaves", DURIAN, RAWDURIAN);

    public static final BlockEntry<SaplingBlock> DURIAN_SAPLING = fruitSapling("duriansapling", "durian_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWLEMON = regRaw("rawlemon", LEMON);

    public static final BlockEntry<FruitingLeavesBlock> LEMONLEAVE_FRUITING_LEAVES = fruitingLeaves("lemonleave_fruiting_leaves", LEMON, RAWLEMON);

    public static final BlockEntry<SaplingBlock> LEMON_SAPLING = fruitSapling("lemonsapling", "lemon_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWLYCHEE = regRaw("rawlychee", LYCHEE);

    public static final BlockEntry<FruitingLeavesBlock> LYCHEELEAVE_FRUITING_LEAVES = fruitingLeaves("lycheeleave_fruiting_leaves", LYCHEE, RAWLYCHEE);

    public static final BlockEntry<SaplingBlock> LYCHEE_SAPLING = fruitSapling("lycheesapling", "lychee_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWMANG = regRaw("rawmango", MANGO);

    public static final BlockEntry<FruitingLeavesBlock> MANGOLEAVE_FRUITING_LEAVES = fruitingLeaves("mangoleave_fruiting_leaves", MANGO, RAWMANG);

    public static final BlockEntry<SaplingBlock> MANGO_SAPLING = fruitSapling("mangosapling", "mango_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWPAWPAW = regRaw("rawpawpaw", PAWPAW);

    public static final BlockEntry<FruitingLeavesBlock> PAWPAWLEAVE_FRUITING_LEAVES = fruitingLeaves("pawpawleave_fruiting_leaves", PAWPAW, RAWPAWPAW);

    public static final BlockEntry<SaplingBlock> PAWPAW_SAPLING = fruitSapling("pawpawsapling", "pawpaw_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWTANGERINE = regRaw("rawtangerine", TANGERINE);

    public static final BlockEntry<FruitingLeavesBlock> TANGERINELEAVE_FRUITING_LEAVES = fruitingLeaves("tangerineleave_fruiting_leaves", TANGERINE, RAWTANGERINE);

    public static final BlockEntry<SaplingBlock> TANGERINE_SAPLING = fruitSapling("tangerinesapling", "tangerine_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWAPPLE = REGISTRY.register("rawapple",
            () -> new FallingFruitBlock(() -> Items.APPLE, BlockBehaviour.Properties.of()
                    .strength(0.2f).noCollission().noOcclusion().randomTicks().sound(SoundType.GRASS).instabreak()));

    public static final BlockEntry<FruitingLeavesBlock> APPLELEAVE_FRUITING_LEAVES = REGISTRATE.block("appleleave_fruiting_leaves",
            properties -> new FruitingLeavesBlock(() -> Items.APPLE, RAWAPPLE, properties
                    .mapColor(MapColor.PLANT).strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion()
                    .isValidSpawn((s, g, p, t) -> false).isSuffocating((s, g, p) -> false).isViewBlocking((s, g, p) -> false)))
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item().build()
            .register();

    public static final BlockEntry<SaplingBlock> APPLE_SAPLING = fruitSapling("applesapling", "apple_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWCOCONUT = regRaw("rawcoconut", COCONUT);

    public static final BlockEntry<FruitingLeavesBlock> COCONUTLEAVE_FRUITING_LEAVES = fruitingLeaves("coconutleave_fruiting_leaves", COCONUT, RAWCOCONUT);

    public static final BlockEntry<CoconutSaplingBlock> COCONUT_SAPLING = REGISTRATE.block("coconutsapling",
            properties -> new CoconutSaplingBlock(properties.mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS),
                    ORCHARD_HEARTWOOD_LOG, ORCHARD_HEARTWOOD_LEAVES, COCONUTLEAVE_FRUITING_LEAVES, RAWCOCONUT))
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item((block, itemProperties) -> new TooltipBlockItem(block, itemProperties,
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), java.util.List::of))
            .build()
            .register();

    public static final DeferredBlock<FallingFruitBlock> RAWHONEYPEACH = regRaw("rawhoneypeach", HONEYPEACH);

    public static final BlockEntry<FruitingLeavesBlock> HONEYPEACHLEAVE_FRUITING_LEAVES = fruitingLeaves("honeypeachleave_fruiting_leaves", HONEYPEACH, RAWHONEYPEACH);

    public static final BlockEntry<SaplingBlock> HONEYPEACH_SAPLING = fruitSapling("honeypeachsapling", "honeypeach_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWNECTARINE = regRaw("rawnectarine", NECTARINE);

    public static final BlockEntry<FruitingLeavesBlock> NECTARINELEAVE_FRUITING_LEAVES = fruitingLeaves("nectarineleave_fruiting_leaves", NECTARINE, RAWNECTARINE);

    public static final BlockEntry<SaplingBlock> NECTARINE_SAPLING = fruitSapling("nectarinesapling", "nectarine_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWPEAR = regRaw("rawpear", PEAR);

    public static final BlockEntry<FruitingLeavesBlock> PEARLEAVE_FRUITING_LEAVES = fruitingLeaves("pearleaves_fruiting_leaves", PEAR, RAWPEAR);

    public static final BlockEntry<SaplingBlock> PEAR_SAPLING = fruitSapling("pearsapling", "pear_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWSWEETMELON = regRaw("rawsweetmelon", SWEETMELON);

    public static final BlockEntry<FruitingLeavesBlock> SWEETMELONLEAVE_FRUITING_LEAVES = fruitingLeaves("sweetmelonleave_fruiting_leaves", SWEETMELON, RAWSWEETMELON);

    public static final BlockEntry<SaplingBlock> SWEETMELON_SAPLING = fruitSapling("sweetmelonsapling", "sweetmelon_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWPISTACHIONUT = regRaw("rawpistachionut", PISTACHIONUT);

    public static final BlockEntry<FruitingLeavesBlock> PISTACHIONUTLEAVE_FRUITING_LEAVES = fruitingLeaves("pistachionutleave_fruiting_leaves", PISTACHIONUT, RAWPISTACHIONUT);

    public static final BlockEntry<SaplingBlock> PISTACHIONUT_SAPLING = fruitSapling("pistachionutsapling", "pistachionut_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWREDDATE = regRaw("rawreddate", REDDATE);

    public static final BlockEntry<FruitingLeavesBlock> REDDATELEAVE_FRUITING_LEAVES = fruitingLeaves("reddateleave_fruiting_leaves", REDDATE, RAWREDDATE);

    public static final BlockEntry<SaplingBlock> REDDATE_SAPLING = fruitSapling("reddatesapling", "reddate_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWWALNUT = regRaw("rawwalnut", WALNUT);

    public static final BlockEntry<FruitingLeavesBlock> WALNUTLEAVE_FRUITING_LEAVES = fruitingLeaves("walnutleaves_fruiting_leaves", WALNUT, RAWWALNUT);

    public static final BlockEntry<SaplingBlock> WALNUT_SAPLING = fruitSapling("walnutsapling", "walnut_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWWINTERJUJUBE = regRaw("rawwinterjujube", WINTERJUJUBE);

    public static final BlockEntry<FruitingLeavesBlock> WINTERJUJUBELEAVE_FRUITING_LEAVES = fruitingLeaves("winterjujubeleave_fruiting_leaves", WINTERJUJUBE, RAWWINTERJUJUBE);

    public static final BlockEntry<SaplingBlock> WINTERJUJUBE_SAPLING = fruitSapling("winterjujubesapling", "winterjujube_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWKIWIFRUIT = regRaw("rawkiwifruit", KIWIFRUIT);

    public static final BlockEntry<FruitingLeavesBlock> KIWIFRUITSSLEAVE_FRUITING_LEAVES = fruitingLeaves("kiwifruitsleave_fruiting_leaves", KIWIFRUIT, RAWKIWIFRUIT);

    public static final BlockEntry<SaplingBlock> KIWIFRUIT_SAPLING = fruitSapling("kiwifruitsleavesapling", "kiwifruit_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWMANGOSTEEN = regRaw("rawmangosteen", MANGOSTEEN);

    public static final BlockEntry<FruitingLeavesBlock> MANGOSTEENLEAVE_FRUITING_LEAVES = fruitingLeaves("mangosteenleave_fruiting_leaves", MANGOSTEEN, RAWMANGOSTEEN);

    public static final BlockEntry<SaplingBlock> MANGOSTEEN_SAPLING = fruitSapling("mangosteensapling", "mangosteen_tree");

    public static final DeferredBlock<FallingFruitBlock> RAWMULBERRY = regRaw("rawmulberry", MULBERRY);

    public static final BlockEntry<FruitingLeavesBlock> MULBERRYLEAVE_FRUITING_LEAVES = fruitingLeaves("mulberryleaves_fruiting_leaves", MULBERRY, RAWMULBERRY);

    public static final BlockEntry<SaplingBlock> MULBERRY_SAPLING = fruitSapling("mulberrysapling", "mulberry_tree");

    public static final BlockEntry<ColorfulFireworksBoxBlock> COLORFUL_FIREWORKS_BOX = REGISTRATE.block("colorful_fireworks_box",
            properties -> new ColorfulFireworksBoxBlock(properties
                    .strength(1.5f)
                    .sound(SoundType.WOOD)
                    .noOcclusion()))
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item(ColorfulFireworksBoxItem::new).build()
            .register();

    public static final BlockEntry<DoorPaperBlock> LEFT_DOOR_PAPER = REGISTRATE.block("leftdoorpaper",
            properties -> new DoorPaperBlock(properties.instabreak().noCollission().sound(SoundType.WOOL)))
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item().build()
            .register();

    public static final BlockEntry<DoorPaperBlock> RIGHT_DOOR_PAPER = REGISTRATE.block("rightdoorpaper",
            properties -> new DoorPaperBlock(properties.instabreak().noCollission().sound(SoundType.WOOL)))
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item().build()
            .register();

    public static final BlockEntry<CoupletBlock> ANTITHETICAL_COUPLET_1 = REGISTRATE.block("antithetical_couplet_1",
            properties -> new CoupletBlock(properties.strength(0.5f).sound(SoundType.WOOL).noCollission()))
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item(CoupletBlockItem::new).build()
            .register();

    public static final BlockEntry<CoupletBlock> ANTITHETICAL_COUPLET_2 = REGISTRATE.block("antithetical_couplet_2",
            properties -> new CoupletBlock(properties.strength(0.5f).sound(SoundType.WOOL).noCollission()))
            .setData(ProviderType.BLOCKSTATE, (ctx, provider) -> {})
            .item(CoupletBlockItem::new).build()
            .register();

    public static BlockBehaviour.Properties waterCropProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .randomTicks()
                .noCollission()
                .noOcclusion()
                .instabreak()
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY);
    }

    public static BlockBehaviour.Properties mushroomProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .randomTicks()
                .noCollission()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY);
    }

    public static BlockBehaviour.Properties cropProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .randomTicks()
                .noCollission()
                .noOcclusion()
                .instabreak()
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY);
    }


}
