// 在文件顶部添加以下导入语句
package com.flavor_immersed_daily;

import com.flavor_immersed_daily.block.FIDCropBlock;
import com.flavor_immersed_daily.block.BighookBlock;
import com.flavor_immersed_daily.block.BighookBlockEntity;
import com.flavor_immersed_daily.block.WoodBasinBlock;
import com.flavor_immersed_daily.block.WoodBasinBlockEntity;
import com.flavor_immersed_daily.block.FridgeBlockEntity;
import com.flavor_immersed_daily.block.TrellisBlock;
import com.flavor_immersed_daily.block.AgriculturalAppraisalMachineBlock;
import com.flavor_immersed_daily.block.EggBreakingMachineBlock;
import com.flavor_immersed_daily.block.EggBreakingMachineBlockEntity;
import com.flavor_immersed_daily.block.FridgeBlock;
import com.flavor_immersed_daily.screen.FridgeMenu;
import com.flavor_immersed_daily.screen.EggBreakingMachineMenu;
import com.flavor_immersed_daily.recipe.FridgeTemperingRecipe;
import com.flavor_immersed_daily.recipe.FridgeFreezingRecipe;
import com.flavor_immersed_daily.recipe.EggBreakingRecipe;
import com.flavor_immersed_daily.recipe.EggBreakingRecipeSerializer;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import com.flavor_immersed_daily.block.CoconutSaplingBlock;
import com.flavor_immersed_daily.block.GrapeBlock;
import com.flavor_immersed_daily.block.FIDLogMushroomBlock;
import com.flavor_immersed_daily.block.FIDWaterCropBlock;
import com.flavor_immersed_daily.block.JuiceBlock;
import com.flavor_immersed_daily.block.FruitingLeavesBlock;
import com.flavor_immersed_daily.block.FallingFruitBlock;
import com.flavor_immersed_daily.block.BananaSaplingBlock;
import com.flavor_immersed_daily.block.RawBananaBlock;
import com.flavor_immersed_daily.block.CinnamonWoodBlock;
import com.flavor_immersed_daily.block.CinnamonSaplingBlock;
import com.flavor_immersed_daily.block.CinnamonLeavesBlock;
import com.flavor_immersed_daily.block.ChairBlock;
import com.flavor_immersed_daily.block.ColorfulFireworksBoxBlock;
import com.flavor_immersed_daily.block.ColorfulFireworksBoxBlockEntity;
import com.flavor_immersed_daily.block.DoorPaperBlock;
import com.flavor_immersed_daily.block.ChineseKnottingBlock;
import com.flavor_immersed_daily.block.LampCabinetBlock;
import com.flavor_immersed_daily.block.DecorativeBlock;
import com.flavor_immersed_daily.block.CoupletBlock;
import com.flavor_immersed_daily.block.CoupletBlockEntity;
import com.flavor_immersed_daily.item.JuiceBlockItem;
import com.flavor_immersed_daily.item.FairySparklerItem;
import com.flavor_immersed_daily.network.ColorfulFireworksBoxSyncPayload;
import com.flavor_immersed_daily.network.CoupletSyncPayload;
import com.flavor_immersed_daily.network.WindowPaperSyncPayload;
import com.flavor_immersed_daily.entity.FallingFruitEntity;
import com.flavor_immersed_daily.entity.FirecrackerEntity;
import com.flavor_immersed_daily.entity.SeatEntity;
import com.flavor_immersed_daily.entity.ThrownFruitEntity;
import com.flavor_immersed_daily.entity.WindowPaperEntity;
import com.flavor_immersed_daily.item.WindowPaperItem;
import com.flavor_immersed_daily.item.RareFruitVariantItem;
import com.flavor_immersed_daily.item.CoarseClothItem;
import com.flavor_immersed_daily.item.FirecrackerHelper;
import com.flavor_immersed_daily.item.SeedableFruitItem;
import com.flavor_immersed_daily.item.ThrowableFruitItem;
import com.flavor_immersed_daily.item.ColorfulFireworksBoxItem;
import com.flavor_immersed_daily.item.CoupletBlockItem;
import com.flavor_immersed_daily.item.KitchenScissorsItem;
import com.flavor_immersed_daily.item.WildHarvestItem;
import com.flavor_immersed_daily.item.TooltipItem;
import com.flavor_immersed_daily.item.TooltipBlockItem;
import com.flavor_immersed_daily.item.SeasoningItem;
import com.flavor_immersed_daily.effect.AceticErosionEffect;
import com.flavor_immersed_daily.effect.BeanFuryEffect;
import com.flavor_immersed_daily.effect.ButterPitcherEffect;
import com.flavor_immersed_daily.effect.CrimsonMambaEffect;
import com.flavor_immersed_daily.effect.FlatulenceEffect;
import com.flavor_immersed_daily.effect.FlavorBaseEffect;
import com.flavor_immersed_daily.effect.FrozenEffect;
import com.flavor_immersed_daily.effect.FuryAssaultEffect;
import com.flavor_immersed_daily.effect.HulkLeekEffect;
import com.flavor_immersed_daily.effect.SesameSlipEffect;
import com.flavor_immersed_daily.effect.SolarBrewEffect;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.List;
import java.util.Optional;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(FlavorImmersedDaily.MODID)
public class FlavorImmersedDaily {
    public static final String MODID = "flavor_immersed_daily";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MODID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, MODID);

    // ========== 实体 ==========
    public static final DeferredHolder<EntityType<?>, EntityType<FallingFruitEntity>> FALLING_FRUIT = ENTITY_TYPES.register("falling_fruit",
            () -> EntityType.Builder.of(FallingFruitEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(10)
                    .updateInterval(20)
                    .build("falling_fruit"));

    public static final DeferredHolder<EntityType<?>, EntityType<SeatEntity>> SEAT_ENTITY = ENTITY_TYPES.register("seat",
            () -> EntityType.Builder.<SeatEntity>of(SeatEntity::new, MobCategory.MISC)
                    .sized(0.0f, 0.0f)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("seat"));

    // ========== 音效 ==========
    public static final DeferredHolder<SoundEvent, SoundEvent> MANBAOUT = SOUND_EVENTS.register("manbaout",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "manbaout")));

    // ========== Tags ==========
    public static final TagKey<Item> RADISH_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("fid", "radish"));
    public static final TagKey<Item> SEASONING_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("fid", "seasoning"));

    // ========== 效果 ==========
    public static final DeferredHolder<MobEffect, FlatulenceEffect> FLATULENCE = MOB_EFFECTS.register("flatulence", FlatulenceEffect::new);
    public static final DeferredHolder<MobEffect, SesameSlipEffect> SESAME_SLIP = MOB_EFFECTS.register("sesame_slip", SesameSlipEffect::new);
    public static final DeferredHolder<MobEffect, AceticErosionEffect> ACETIC_EROSION = MOB_EFFECTS.register("acetic_erosion", AceticErosionEffect::new);
    public static final DeferredHolder<MobEffect, ButterPitcherEffect> BUTTER_PITCHER = MOB_EFFECTS.register("butter_pitcher", ButterPitcherEffect::new);
    public static final DeferredHolder<MobEffect, FrozenEffect> FROZEN = MOB_EFFECTS.register("frozen", FrozenEffect::new);
    public static final DeferredHolder<MobEffect, BeanFuryEffect> BEAN_FURY = MOB_EFFECTS.register("bean_fury", BeanFuryEffect::new);
    public static final DeferredHolder<MobEffect, FlavorBaseEffect> FLAVOR_BASE = MOB_EFFECTS.register("flavor_base", FlavorBaseEffect::new);
    public static final DeferredHolder<MobEffect, SolarBrewEffect> SOLAR_BREW = MOB_EFFECTS.register("solar_brew", SolarBrewEffect::new);
    public static final DeferredHolder<MobEffect, HulkLeekEffect> HULK_LEEK = MOB_EFFECTS.register("hulk_leek", HulkLeekEffect::new);
    public static final DeferredHolder<MobEffect, FuryAssaultEffect> FURY_ASSAULT = MOB_EFFECTS.register("fury_assault", FuryAssaultEffect::new);
    public static final DeferredHolder<MobEffect, CrimsonMambaEffect> CRIMSON_MAMBA = MOB_EFFECTS.register("crimson_mamba", CrimsonMambaEffect::new);

    // ========== 白菜作物 ==========
    public static final DeferredBlock<FIDCropBlock> CHINESE_LEAVES_CROP = BLOCKS.register("chineseleavesseed",
            () -> new FIDCropBlock(cropProperties(), 4));

    public static final DeferredItem<Item> CHINESE_LEAVES = ITEMS.register("chineseleaves",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.3f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<ItemNameBlockItem> CHINESE_LEAVES_SEEDS = ITEMS.register("chineseleavesseed",
            () -> new ItemNameBlockItem(CHINESE_LEAVES_CROP.get(), new Item.Properties()));

    //切好的白菜
    public static final DeferredItem<Item> CUT_CHINESE_CABBAGE = ITEMS.register("cutchinesecabbage",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.1f)
                            .alwaysEdible()
                            .build())));

    // ========== 八角作物 ==========
    public static final DeferredBlock<FIDCropBlock> ANISEED_0_CROP = BLOCKS.register("aniseed_0",
            () -> new FIDCropBlock(cropProperties(), 4));

    public static final DeferredItem<ItemNameBlockItem> ANISEED_0 = ITEMS.register("aniseed_0", // 物品名也使用aniseed_0
            () -> new ItemNameBlockItem(ANISEED_0_CROP.get(), new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.2f)
                            .alwaysEdible()
                            .build())));

    // ========== 萝卜作物 ==========
    
    public static final DeferredBlock<FIDCropBlock> RADISHSEED_CROP = BLOCKS.register("radishseed",
            () -> new FIDCropBlock(cropProperties(), 4));

    public static final DeferredItem<Item> RADISH = ITEMS.register("radish",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.3f)
                            .alwaysEdible()
                            .build())));

    
    public static final DeferredItem<ItemNameBlockItem> RADISHSEED = ITEMS.register("radishseed",
            () -> new ItemNameBlockItem(RADISHSEED_CROP.get(), new Item.Properties()));

    // ========== 冰棍 ==========
    public static final DeferredItem<Item> APPLEPOPSICLE = ITEMS.register("applepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> APRICOTPOPSICLE = ITEMS.register("apricotpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> BANANAPOPSICLE = ITEMS.register("bananapopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> BLUEBERRYPOPSICLE = ITEMS.register("blueberrypopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> CARAMBOLAPOPSICLE = ITEMS.register("carambolapopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> CHERRYPOPSICLE = ITEMS.register("cherrypopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> CHOCOLATEPOPSICLE = ITEMS.register("chocolatepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COCONUTPOPSICLE = ITEMS.register("coconutpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COFFEEPOPSICLE = ITEMS.register("coffeepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> DRAGONFRUITPOPSICLE = ITEMS.register("dragonfruitpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> DURIANPOPSICLE = ITEMS.register("durianpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> GRAPEPOPSICLE = ITEMS.register("grapepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> GREENGRAPEPOPSICLE = ITEMS.register("greengrapepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> GREENPLUMPOPSICLE = ITEMS.register("greenplumpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> HAMIMELONPOPSICLE = ITEMS.register("hamimelonpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> HAWTHORNPOPSICLE = ITEMS.register("hawthornpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> HONEYPEACHPOPSICLE = ITEMS.register("honeypeachpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> KIWIFRUITPOPSICLE = ITEMS.register("kiwifruitpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> LEMONPOPSICLE = ITEMS.register("lemonpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> LOQUATPOPSICLE = ITEMS.register("loquatpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> LYCHEEPOPSICLE = ITEMS.register("lycheepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> MANGOPOPSICLE = ITEMS.register("mangopopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> MANGOSTEEPOPSICLE = ITEMS.register("mangosteenpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> MULBERRYPOPSICLE = ITEMS.register("mulberrypopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> NECTARINEPOPSICLE = ITEMS.register("nectarinepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> ORANGEPOPSICLE = ITEMS.register("orangepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> PAWPAWPOPSICLE = ITEMS.register("pawpawpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> PEARPOPSICLE = ITEMS.register("pearpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> PINEAPPLEPOPSICLE = ITEMS.register("pineapplepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> PLUMPOPSICLE = ITEMS.register("plumpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> POMEGRANATEPOPSICLE = ITEMS.register("pomegranatepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> POPSICLE = ITEMS.register("popsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> STRAWBERRYPOPSICLE = ITEMS.register("strawberrypopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> SWEETBERRYPOPSICLE = ITEMS.register("sweetberrypopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> SWEETMELONPOPSICLE = ITEMS.register("sweetmelonpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> TANGERINEPOPSICLE = ITEMS.register("tangerinepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> WATERMELONPOPSICLE = ITEMS.register("watermelonpopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> WINTERJUJUBEPOPSICLE = ITEMS.register("winterjujubepopsicle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build())));

    // ========== Ice Cream 系列食品 ==========
    public static final DeferredItem<Item> APPLEICECREAM = ITEMS.register("appleicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> APRICOTICECREAM = ITEMS.register("apricoticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> BANANAICECREAM = ITEMS.register("bananaicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> BLUEBERRYICECREAM = ITEMS.register("blueberryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> CARAMBOLAICECREAM = ITEMS.register("carambolaicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> CHERRYICECREAM = ITEMS.register("cherryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> CHOCOLATEICECREAM = ITEMS.register("chocolateicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COCONUTICECREAM = ITEMS.register("coconuticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> COFFEEICECREAM = ITEMS.register("coffeeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> DRAGONFRUITICECREAM = ITEMS.register("dragonfruiticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> DURIANICECREAM = ITEMS.register("durianicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> GRAPEICECREAM = ITEMS.register("grapeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> GREENGRAPEICECREAM = ITEMS.register("greengrapeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> GREENPLUMICECREAM = ITEMS.register("greenplumicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> HAMIMELONICECREAM = ITEMS.register("hamimelonicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> HAWTHORNICECREAM = ITEMS.register("hawthornicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> HONEYPEACHICECREAM = ITEMS.register("honeypeachicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> KIWIFRUITICECREAM = ITEMS.register("kiwifruiticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> LEMONICECREAM = ITEMS.register("lemonicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> LOQUATICECREAM = ITEMS.register("loquaticecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> LYCHEEICECREAM = ITEMS.register("lycheeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> MANGOICECREAM = ITEMS.register("mangoicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> MANGOSTEENICECREAM = ITEMS.register("mangosteenicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> MILKICECREAM = ITEMS.register("milkicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> MULBERRYICECREAM = ITEMS.register("mulberryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> NECTARINEICECREAM = ITEMS.register("nectarineicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> ORANGEICECREAM = ITEMS.register("orangeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> PAWPAWICECREAM = ITEMS.register("pawpawicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> PEARICECREAM = ITEMS.register("pearicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> PINEAPPLEICECREAM = ITEMS.register("pineappleicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> PLUMICECREAM = ITEMS.register("plumicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> POMEGRANATEICECREAM = ITEMS.register("pomegranateicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> STRAWBERRYICECREAM = ITEMS.register("strawberryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> SWEETBERRYICECREAM = ITEMS.register("sweetberryicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> SWEETMELONICECREAM = ITEMS.register("sweetmelonicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> TANGERINEICECREAM = ITEMS.register("tangerineicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> WATERMELONICECREAM = ITEMS.register("watermelonicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<Item> WINTERJUJUBEICECREAM = ITEMS.register("winterjujubeicecream",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.7f)
                            .alwaysEdible()
                            .build())));

    // ========== 拔丝茄子相关 ==========
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> DRAWNEGGPLANT_BLOCK = BLOCKS.register("drawneggplant",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "drawneggplant", 
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .forceSolidOn()
                            .strength(0.2F)
                            .sound(net.minecraft.world.level.block.SoundType.CROP)));

    public static final DeferredItem<SpecialItems.PlaceableFoodItem> DRAWNEGGPLANT = ITEMS.register("drawneggplant",
            () -> new SpecialItems.PlaceableFoodItem(
                    DRAWNEGGPLANT_BLOCK.get(),
                    new Item.Properties(),
                    new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build()));

    // ========== 可放置菜肴 ==========
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> LINYIFRIEDCHICKEN_BLOCK = BLOCKS.register("linyifriedchicken",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "linyifriedchicken", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> LINYIFRIEDCHICKEN = ITEMS.register("linyifriedchicken",
            () -> new SpecialItems.PlaceableFoodItem(LINYIFRIEDCHICKEN_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> MEATBALLSOUP_BLOCK = BLOCKS.register("meatballsoup",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "meatballsoup", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> MEATBALLSOUP = ITEMS.register("meatballsoup",
            () -> new SpecialItems.PlaceableFoodItem(MEATBALLSOUP_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> PRESERVEDEGGSALAD_BLOCK = BLOCKS.register("preservedeggsalad",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "preservedeggsalad", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> PRESERVEDEGGSALAD = ITEMS.register("preservedeggsalad",
            () -> new SpecialItems.PlaceableFoodItem(PRESERVEDEGGSALAD_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> TOMATOSALAD_BLOCK = BLOCKS.register("tomatosalad",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "tomatosalad", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> TOMATOSALAD = ITEMS.register("tomatosalad",
            () -> new SpecialItems.PlaceableFoodItem(TOMATOSALAD_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> STEAMED_CHICKENWITH_CHILI_SAUCE_BLOCK = BLOCKS.register("steamed_chickenwith_chili_sauce",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "steamed_chickenwith_chili_sauce", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> STEAMED_CHICKENWITH_CHILI_SAUCE = ITEMS.register("steamed_chickenwith_chili_sauce",
            () -> new SpecialItems.PlaceableFoodItem(STEAMED_CHICKENWITH_CHILI_SAUCE_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> COLACHICKENWINGS_BLOCK = BLOCKS.register("colachickenwings",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "colachickenwings", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> COLACHICKENWINGS = ITEMS.register("colachickenwings",
            () -> new SpecialItems.PlaceableFoodItem(COLACHICKENWINGS_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> FOURJOYMEATBALLS_BLOCK = BLOCKS.register("fourjoymeatballs",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "fourjoymeatballs", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> FOURJOYMEATBALLS = ITEMS.register("fourjoymeatballs",
            () -> new SpecialItems.PlaceableFoodItem(FOURJOYMEATBALLS_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> STIRFRIEDBOILEDPORKSLICESINHOTSAUCE_BLOCK = BLOCKS.register("stirfriedboiledporkslicesinhotsauce",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "stirfriedboiledporkslicesinhotsauce", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> STIRFRIEDBOILEDPORKSLICESINHOTSAUCE = ITEMS.register("stirfriedboiledporkslicesinhotsauce",
            () -> new SpecialItems.PlaceableFoodItem(STIRFRIEDBOILEDPORKSLICESINHOTSAUCE_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> SAUTEED_POTATO_GREEN_PEPPER_EGGPLANT_BLOCK = BLOCKS.register("sauteed_potato_green_pepper_eggplant",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "sauteed_potato_green_pepper_eggplant", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> SAUTEED_POTATO_GREEN_PEPPER_EGGPLANT = ITEMS.register("sauteed_potato_green_pepper_eggplant",
            () -> new SpecialItems.PlaceableFoodItem(SAUTEED_POTATO_GREEN_PEPPER_EGGPLANT_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> FRIEDMEATWITHCUMINONION_BLOCK = BLOCKS.register("friedmeatwithcuminonion",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "friedmeatwithcuminonion", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> FRIEDMEATWITHCUMINONION = ITEMS.register("friedmeatwithcuminonion",
            () -> new SpecialItems.PlaceableFoodItem(FRIEDMEATWITHCUMINONION_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> KUNGPAOCHICKEN_BLOCK = BLOCKS.register("kungpaochicken",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "kungpaochicken", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> KUNGPAOCHICKEN = ITEMS.register("kungpaochicken",
            () -> new SpecialItems.PlaceableFoodItem(KUNGPAOCHICKEN_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> STIRFRIEDSTRINGBEANS_BLOCK = BLOCKS.register("stirfriedstringbeans",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "stirfriedstringbeans", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> STIRFRIEDSTRINGBEANS = ITEMS.register("stirfriedstringbeans",
            () -> new SpecialItems.PlaceableFoodItem(STIRFRIEDSTRINGBEANS_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> MIXEDCOLDDISHES_BLOCK = BLOCKS.register("mixedcolddishes",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "mixedcolddishes", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> MIXEDCOLDDISHES = ITEMS.register("mixedcolddishes",
            () -> new SpecialItems.PlaceableFoodItem(MIXEDCOLDDISHES_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> JAPANESEBRAISEDTOFU_BLOCK = BLOCKS.register("japanesebraisedtofu",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "japanesebraisedtofu", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> JAPANESEBRAISEDTOFU = ITEMS.register("japanesebraisedtofu",
            () -> new SpecialItems.PlaceableFoodItem(JAPANESEBRAISEDTOFU_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> SCRAMBLEDEGGSWITHFUNGUSANDCUCUMBER_BLOCK = BLOCKS.register("scrambledeggswithfungusandcucumber",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "scrambledeggswithfungusandcucumber", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> SCRAMBLEDEGGSWITHFUNGUSANDCUCUMBER = ITEMS.register("scrambledeggswithfungusandcucumber",
            () -> new SpecialItems.PlaceableFoodItem(SCRAMBLEDEGGSWITHFUNGUSANDCUCUMBER_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> PLEUROTUSERYNGIIWITHSALTANDPEPPER_BLOCK = BLOCKS.register("pleurotuseryngiiwithsaltandpepper",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "pleurotuseryngiiwithsaltandpepper", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> PLEUROTUSERYNGIIWITHSALTANDPEPPER = ITEMS.register("pleurotuseryngiiwithsaltandpepper",
            () -> new SpecialItems.PlaceableFoodItem(PLEUROTUSERYNGIIWITHSALTANDPEPPER_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> POACHED_SPICY_SLICESOF_PORK_BLOCK = BLOCKS.register("poached_spicy_slicesof_pork",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "poached_spicy_slicesof_pork", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> POACHED_SPICY_SLICESOF_PORK = ITEMS.register("poached_spicy_slicesof_pork",
            () -> new SpecialItems.PlaceableFoodItem(POACHED_SPICY_SLICESOF_PORK_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> SLICED_FISHIN_HOT_CHILI_OIL_BLOCK = BLOCKS.register("sliced_fishin_hot_chili_oil",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "sliced_fishin_hot_chili_oil", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> SLICED_FISHIN_HOT_CHILI_OIL = ITEMS.register("sliced_fishin_hot_chili_oil",
            () -> new SpecialItems.PlaceableFoodItem(SLICED_FISHIN_HOT_CHILI_OIL_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> SAUTEEDMUSHROOMSWITHRAPESEED_BLOCK = BLOCKS.register("sauteedmushroomswithrapeseed",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "sauteedmushroomswithrapeseed", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> SAUTEEDMUSHROOMSWITHRAPESEED = ITEMS.register("sauteedmushroomswithrapeseed",
            () -> new SpecialItems.PlaceableFoodItem(SAUTEEDMUSHROOMSWITHRAPESEED_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> STEAMEDFISH_BLOCK = BLOCKS.register("steamedfish",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "steamedfish", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> STEAMEDFISH = ITEMS.register("steamedfish",
            () -> new SpecialItems.PlaceableFoodItem(STEAMEDFISH_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> FRIEDCOWPEA_BLOCK = BLOCKS.register("friedcowpea",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "friedcowpea", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> FRIEDCOWPEA = ITEMS.register("friedcowpea",
            () -> new SpecialItems.PlaceableFoodItem(FRIEDCOWPEA_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> FRIEDSPICYCHICKEN_BLOCK = BLOCKS.register("friedspicychicken",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "friedspicychicken", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> FRIEDSPICYCHICKEN = ITEMS.register("friedspicychicken",
            () -> new SpecialItems.PlaceableFoodItem(FRIEDSPICYCHICKEN_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> BOILED_CHICKENWITH_SAUCE_BLOCK = BLOCKS.register("boiled_chickenwith_sauce",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "boiled_chickenwith_sauce", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> BOILED_CHICKENWITH_SAUCE = ITEMS.register("boiled_chickenwith_sauce",
            () -> new SpecialItems.PlaceableFoodItem(BOILED_CHICKENWITH_SAUCE_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> STEWEDPORKWITHBROWNSAUCE_BLOCK = BLOCKS.register("stewedporkwithbrownsauce",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "stewedporkwithbrownsauce", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> STEWEDPORKWITHBROWNSAUCE = ITEMS.register("stewedporkwithbrownsauce",
            () -> new SpecialItems.PlaceableFoodItem(STEWEDPORKWITHBROWNSAUCE_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> FRIEDLIVERTIPWITHSPINACH_BLOCK = BLOCKS.register("friedlivertipwithspinach",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "friedlivertipwithspinach", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> FRIEDLIVERTIPWITHSPINACH = ITEMS.register("friedlivertipwithspinach",
            () -> new SpecialItems.PlaceableFoodItem(FRIEDLIVERTIPWITHSPINACH_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> PINEAPPLE_SWEETAND_SOUR_PORK_BLOCK = BLOCKS.register("pineapple_sweetand_sour_pork",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "pineapple_sweetand_sour_pork", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> PINEAPPLE_SWEETAND_SOUR_PORK = ITEMS.register("pineapple_sweetand_sour_pork",
            () -> new SpecialItems.PlaceableFoodItem(PINEAPPLE_SWEETAND_SOUR_PORK_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> CHICKENWITH_SCALLION_OIL_BLOCK = BLOCKS.register("chickenwith_scallion_oil",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "chickenwith_scallion_oil", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> CHICKENWITH_SCALLION_OIL = ITEMS.register("chickenwith_scallion_oil",
            () -> new SpecialItems.PlaceableFoodItem(CHICKENWITH_SCALLION_OIL_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> SALTEDEGGYOLKFRIEDCAULIFLOWER_BLOCK = BLOCKS.register("saltedeggyolkfriedcauliflower",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "saltedeggyolkfriedcauliflower", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> SALTEDEGGYOLKFRIEDCAULIFLOWER = ITEMS.register("saltedeggyolkfriedcauliflower",
            () -> new SpecialItems.PlaceableFoodItem(SALTEDEGGYOLKFRIEDCAULIFLOWER_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> ZUCCHINISNACKMEAT_BLOCK = BLOCKS.register("zucchinisnackmeat",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "zucchinisnackmeat", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> ZUCCHINISNACKMEAT = ITEMS.register("zucchinisnackmeat",
            () -> new SpecialItems.PlaceableFoodItem(ZUCCHINISNACKMEAT_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> BOILED_FISHWITH_PICKLED_CABBAGEAND_CHILI_BLOCK = BLOCKS.register("boiled_fishwith_pickled_cabbageand_chili",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "boiled_fishwith_pickled_cabbageand_chili", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> BOILED_FISHWITH_PICKLED_CABBAGEAND_CHILI = ITEMS.register("boiled_fishwith_pickled_cabbageand_chili",
            () -> new SpecialItems.PlaceableFoodItem(BOILED_FISHWITH_PICKLED_CABBAGEAND_CHILI_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> FRIEDSHREDDEDPORKWITHSWEETANDSOURSAUCE_BLOCK = BLOCKS.register("friedshreddedporkwithsweetandsoursauce",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "friedshreddedporkwithsweetandsoursauce", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> FRIEDSHREDDEDPORKWITHSWEETANDSOURSAUCE = ITEMS.register("friedshreddedporkwithsweetandsoursauce",
            () -> new SpecialItems.PlaceableFoodItem(FRIEDSHREDDEDPORKWITHSWEETANDSOURSAUCE_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> SPICYTOFU_BLOCK = BLOCKS.register("spicytofu",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "spicytofu", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> SPICYTOFU = ITEMS.register("spicytofu",
            () -> new SpecialItems.PlaceableFoodItem(SPICYTOFU_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> BEANWITHSESAMESAUCE_BLOCK = BLOCKS.register("beanwithsesamesauce",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "beanwithsesamesauce", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> BEANWITHSESAMESAUCE = ITEMS.register("beanwithsesamesauce",
            () -> new SpecialItems.PlaceableFoodItem(BEANWITHSESAMESAUCE_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));
    public static final DeferredBlock<SpecialItems.MultiStageInteractiveBlock> SPICYCABBAGE_BLOCK = BLOCKS.register("spicycabbage",
            () -> new SpecialItems.MultiStageInteractiveBlock(2, 0.3f, "spicycabbage", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).forceSolidOn().strength(0.2F).sound(net.minecraft.world.level.block.SoundType.CROP)));
    public static final DeferredItem<SpecialItems.PlaceableFoodItem> SPICYCABBAGE = ITEMS.register("spicycabbage",
            () -> new SpecialItems.PlaceableFoodItem(SPICYCABBAGE_BLOCK.get(), new Item.Properties(), new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).alwaysEdible().build()));

    // ========== 简单菜肴（直接食用，不可放置） ==========
    public static final DeferredItem<Item> BAKEDWHITEMUSHROOMSWITHCREAM = ITEMS.register("bakedwhitemushroomswithcream",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> BRAISEDBEANSPROUTSWITHVERMICELLI = ITEMS.register("braisedbeansproutswithvermicelli",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> CANTONESERICEROLLS = ITEMS.register("cantonesericerolls",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> CONGEEWITH_MINCED_PORKAND_PRESERVED_EGG = ITEMS.register("congee_with_minced_pork_and_preserved_egg",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> CURRYSTEWEDCHICKEN = ITEMS.register("currystewedchicken",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> GUMBOSOUP = ITEMS.register("gumbosoup",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> REDDATESANDTREMELLAPORRIDGE = ITEMS.register("reddatesandtremellaporridge",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> SA = ITEMS.register("sa",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> SCRAMBLEDEGGWITHTOMATO = ITEMS.register("scrambledeggwithtomato",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> SHEEPGIBLETSSOUP = ITEMS.register("sheepgibletssoup",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEAMEDEGGCUSTARD = ITEMS.register("steamedeggcustard",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEWEDCHICKENSOUPWITHMUSHROOMS = ITEMS.register("stewedchickensoupwithmushrooms",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEWEDCHICKENWITHWAXGOURD = ITEMS.register("stewedchickenwithwaxgourd",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEWEDEGGSWITHLOOFAH = ITEMS.register("stewedeggswithloofah",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEWEDPORKOFFAL = ITEMS.register("stewedporkoffal",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> WINTERJUJUBEANDWAXGOURDSOUP = ITEMS.register("winterjujubeandwaxgourdsoup",
            () -> new Item(new Item.Properties().component(DataComponents.FOOD, new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));

    // ========== 高粱作物 ==========
    public static final DeferredBlock<FIDCropBlock> KAOLIANGGARIN_CROP = BLOCKS.register("kao_liang_seed",
            () -> new FIDCropBlock(cropProperties(), 4));

    public static final DeferredItem<Item> KAOLIANGGRAIN = ITEMS.register("kaolianggrain",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.3f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<ItemNameBlockItem> KAOLIANG_SEED = ITEMS.register("kao_liang_seed",
            () -> new ItemNameBlockItem(KAOLIANGGARIN_CROP.get(), new Item.Properties()));

    // ========== 白蘑菇作物 ==========
    public static final DeferredBlock<FIDLogMushroomBlock> WHITEMUSHROOM_CROP = BLOCKS.register("white_mushroom_seed",
            () -> new FIDLogMushroomBlock(mushroomProperties(), 4));

    public static final DeferredItem<Item> WHITEMUSHROOM = ITEMS.register("whitemushroom",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.2f)
                            .alwaysEdible()
                            .build())));

    public static final DeferredItem<ItemNameBlockItem> WHITE_MUSHROOM_SEED = ITEMS.register("white_mushroom_seed",
            () -> new ItemNameBlockItem(WHITEMUSHROOM_CROP.get(), new Item.Properties()));

    // ========== 木耳作物 ==========
    public static final DeferredBlock<FIDLogMushroomBlock> BLACKFUNGUS_CROP = BLOCKS.register("blackfungsseed",
            () -> new FIDLogMushroomBlock(mushroomProperties(), 4));
    public static final DeferredItem<Item> BLACKFUNGUS = ITEMS.register("blackfungus",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<ItemNameBlockItem> BLACKFUNGSSEED = ITEMS.register("blackfungsseed",
            () -> new ItemNameBlockItem(BLACKFUNGUS_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> DRYBLACKFUNGUS = ITEMS.register("dryblackfungus",
            () -> new Item(new Item.Properties()));

    // ========== 杏鲍菇作物 ==========
    public static final DeferredBlock<FIDLogMushroomBlock> PLEUROTUSERYNGII_CROP = BLOCKS.register("pleurotusseed",
            () -> new FIDLogMushroomBlock(mushroomProperties(), 4));
    public static final DeferredItem<Item> PLEUROTUSERYNGII = ITEMS.register("pleurotuseryngii",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<ItemNameBlockItem> PLEUROTUSSEED = ITEMS.register("pleurotusseed",
            () -> new ItemNameBlockItem(PLEUROTUSERYNGII_CROP.get(), new Item.Properties()));

    // ========== 金针菇作物 ==========
    public static final DeferredBlock<FIDLogMushroomBlock> ENOKIMUSHROOM_CROP = BLOCKS.register("enokimushroomseed",
            () -> new FIDLogMushroomBlock(mushroomProperties(), 4));
    public static final DeferredItem<Item> ENOKIMUSHROOM = ITEMS.register("enokimushroom",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    public static final DeferredItem<ItemNameBlockItem> ENOKIMUSHROOMSEED = ITEMS.register("enokimushroomseed",
            () -> new ItemNameBlockItem(ENOKIMUSHROOM_CROP.get(), new Item.Properties()));

    // ========== 银耳作物 ==========
    public static final DeferredBlock<FIDLogMushroomBlock> TREMELLA_CROP = BLOCKS.register("tremellaseed",
            () -> new FIDLogMushroomBlock(mushroomProperties(), 4));
    public static final DeferredItem<Item> TREMELLA = ITEMS.register("tremella",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<ItemNameBlockItem> TREMELLASEED = ITEMS.register("tremellaseed",
            () -> new ItemNameBlockItem(TREMELLA_CROP.get(), new Item.Properties()));

    // ========== 香菇作物 ==========
    public static final DeferredBlock<FIDLogMushroomBlock> FRAGRANTMUSHROOM_CROP = BLOCKS.register("fragrantseed",
            () -> new FIDLogMushroomBlock(mushroomProperties(), 4));
    public static final DeferredItem<Item> FRAGRANTMUSHROOM = ITEMS.register("fragrantmushroom",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<ItemNameBlockItem> FRAGRANTSEED = ITEMS.register("fragrantseed",
            () -> new ItemNameBlockItem(FRAGRANTMUSHROOM_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> DRYFRAGRANTMUSHROOM = ITEMS.register("dryfragrantmushroom",
            () -> new Item(new Item.Properties()));

    // ========== 蓝莓作物 ==========
    public static final DeferredBlock<FIDCropBlock> BLUEBERRY_CROP = BLOCKS.register("blueberryseed",
            () -> new FIDCropBlock(cropProperties(), 3));
    public static final DeferredItem<ItemNameBlockItem> BLUEBERRYSEED = ITEMS.register("blueberryseed",
            () -> new ItemNameBlockItem(BLUEBERRY_CROP.get(), new Item.Properties()));

    // ========== 火龙果作物 ==========
    public static final DeferredBlock<FIDCropBlock> DRAGONFRUIT_CROP = BLOCKS.register("dragonfruitseed",
            () -> new FIDCropBlock(cropProperties(), 3));
    public static final DeferredItem<ItemNameBlockItem> DRAGONFRUITSEED = ITEMS.register("dragonfruitseed",
            () -> new ItemNameBlockItem(DRAGONFRUIT_CROP.get(), new Item.Properties()));

    // ========== 绿茶作物 ==========
    public static final DeferredBlock<FIDCropBlock> GREENTEALEAVES_CROP = BLOCKS.register("greentealeavesseed",
            () -> new FIDCropBlock(cropProperties(), 3));
    public static final DeferredItem<ItemNameBlockItem> GREENTEALEAVESSEED = ITEMS.register("greentealeavesseed",
            () -> new ItemNameBlockItem(GREENTEALEAVES_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> GREENTEALEAVES = ITEMS.register("greentealeaves",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRYGREENTEA = ITEMS.register("drygreentea",
            () -> new Item(new Item.Properties()));

    // ========== 哈密瓜作物 ==========
    public static final DeferredBlock<FIDCropBlock> HAMIMELON_CROP = BLOCKS.register("hamimelonseed",
            () -> new FIDCropBlock(cropProperties(), 3));
    public static final DeferredItem<ItemNameBlockItem> HAMIMELONSEED = ITEMS.register("hamimelonseed",
            () -> new ItemNameBlockItem(HAMIMELON_CROP.get(), new Item.Properties()));
    // ========== 菠萝作物 ==========
    public static final DeferredBlock<FIDCropBlock> PINEAPPLE_CROP = BLOCKS.register("pineappleseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> PINEAPPLESEED = ITEMS.register("pineappleseed",
            () -> new ItemNameBlockItem(PINEAPPLE_CROP.get(), new Item.Properties()));

    // ========== 红茶作物 ==========
    public static final DeferredBlock<FIDCropBlock> RED_TEA_CROP = BLOCKS.register("red_tea_seed",
            () -> new FIDCropBlock(cropProperties(), 3));
    public static final DeferredItem<ItemNameBlockItem> RED_TEA_SEED = ITEMS.register("red_tea_seed",
            () -> new ItemNameBlockItem(RED_TEA_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> REDTEALEAVES = ITEMS.register("redtealeaves",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRYREDTEA = ITEMS.register("dryredtea",
            () -> new Item(new Item.Properties()));

    // ========== 草莓作物 ==========
    public static final DeferredBlock<FIDCropBlock> STRAWBERRY_CROP = BLOCKS.register("strawberryseed",
            () -> new FIDCropBlock(cropProperties(), 3));
    public static final DeferredItem<ItemNameBlockItem> STRAWBERRYSEED = ITEMS.register("strawberryseed",
            () -> new ItemNameBlockItem(STRAWBERRY_CROP.get(), new Item.Properties()));

    // ========== 莲藕作物（水生） ==========
    public static final DeferredBlock<FIDWaterCropBlock> LOTUSROOT_CROP = BLOCKS.register("lotusrootseed",
            () -> new FIDWaterCropBlock(waterCropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> LOTUSROOTSEED = ITEMS.register("lotusrootseed",
            () -> new ItemNameBlockItem(LOTUSROOT_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> LOTUSROOT = ITEMS.register("lotusroot",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.4f).alwaysEdible().build())));

    // ========== 糯米作物（水生） ==========
    public static final DeferredBlock<FIDWaterCropBlock> GLUTINOUSRICE_CROP = BLOCKS.register("glutinousseeds",
            () -> new FIDWaterCropBlock(waterCropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> GLUTINOUSSEEDS = ITEMS.register("glutinousseeds",
            () -> new ItemNameBlockItem(GLUTINOUSRICE_CROP.get(), new Item.Properties()));

    // ========== 水稻作物（水生） ==========
    public static final DeferredBlock<FIDWaterCropBlock> PADDY_CROP = BLOCKS.register("paddyseeds",
            () -> new FIDWaterCropBlock(waterCropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> PADDYSEEDS = ITEMS.register("paddyseeds",
            () -> new ItemNameBlockItem(PADDY_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> PADDYGRAIN = ITEMS.register("paddygrain",
            () -> new Item(new Item.Properties()));

    // ========== 西蓝花种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> BROCCOILSEED_CROP = BLOCKS.register("broccoilseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> BROCCOILSEED = ITEMS.register("broccoilseed",
            () -> new ItemNameBlockItem(BROCCOILSEED_CROP.get(), new Item.Properties()));

    // ========== 荞麦种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> BUCKWHEATSEED_CROP = BLOCKS.register("buckwheatseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> BUCKWHEATSEED = ITEMS.register("buckwheatseed",
            () -> new ItemNameBlockItem(BUCKWHEATSEED_CROP.get(), new Item.Properties()));

    // ========== 卷心菜种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> CABBAGESEED_CROP = BLOCKS.register("cabbageseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> CABBAGESEED = ITEMS.register("cabbageseed",
            () -> new ItemNameBlockItem(CABBAGESEED_CROP.get(), new Item.Properties()));

    // ========== 木薯种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> CASSAVASEEDS_CROP = BLOCKS.register("cassavaseeds",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> CASSAVASEEDS = ITEMS.register("cassavaseeds",
            () -> new ItemNameBlockItem(CASSAVASEEDS_CROP.get(), new Item.Properties()));

    // ========== 芹菜种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> CELERYSEED_CROP = BLOCKS.register("celeryseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> CELERYSEED = ITEMS.register("celeryseed",
            () -> new ItemNameBlockItem(CELERYSEED_CROP.get(), new Item.Properties()));

    // ========== 韭菜种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> CHINESECHIVESSEED_CROP = BLOCKS.register("chinesechivesseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> CHINESECHIVESSEED = ITEMS.register("chinesechivesseed",
            () -> new ItemNameBlockItem(CHINESECHIVESSEED_CROP.get(), new Item.Properties()));

    // ========== 山药种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> CHINESEYAMSEED_CROP = BLOCKS.register("chineseyamseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> CHINESEYAMSEED = ITEMS.register("chineseyamseed",
            () -> new ItemNameBlockItem(CHINESEYAMSEED_CROP.get(), new Item.Properties()));

    // ========== 咖啡豆种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> COFFEEBEANSEED_CROP = BLOCKS.register("coffeebeanseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> COFFEEBEANSEED = ITEMS.register("coffeebeanseed",
            () -> new ItemNameBlockItem(COFFEEBEANSEED_CROP.get(), new Item.Properties()));

    // ========== 玉米种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> CORNSEED_CROP = BLOCKS.register("cornseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> CORNSEED = ITEMS.register("cornseed",
            () -> new ItemNameBlockItem(CORNSEED_CROP.get(), new Item.Properties()));

    // ========== 孜然种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> CUMINSEED_CROP = BLOCKS.register("cuminseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> CUMINSEED = ITEMS.register("cuminseed",
            () -> new ItemNameBlockItem(CUMINSEED_CROP.get(), new Item.Properties()));

    // ========== 茴香种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> FENNELSEEDSTATES_CROP = BLOCKS.register("fennelseedstates",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> FENNELSEEDSTATES = ITEMS.register("fennelseedstates",
            () -> new ItemNameBlockItem(FENNELSEEDSTATES_CROP.get(), new Item.Properties()));

    // ========== 大蒜种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> GARLICSEED_CROP = BLOCKS.register("garlicseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> GARLICSEED = ITEMS.register("garlicseed",
            () -> new ItemNameBlockItem(GARLICSEED_CROP.get(), new Item.Properties()));

    // ========== 生姜种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> GINGER_SEED_CROP = BLOCKS.register("ginger_seed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> GINGER_SEED = ITEMS.register("ginger_seed",
            () -> new ItemNameBlockItem(GINGER_SEED_CROP.get(), new Item.Properties()));

    // ========== 青尖椒种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> GREENPEPPERSEEDS_CROP = BLOCKS.register("greenpepperseeds",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> GREENPEPPERSEEDS = ITEMS.register("greenpepperseeds",
            () -> new ItemNameBlockItem(GREENPEPPERSEEDS_CROP.get(), new Item.Properties()));

    // ========== 秋葵种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> GUMBOSEED_CROP = BLOCKS.register("gumboseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> GUMBOSEED = ITEMS.register("gumboseed",
            () -> new ItemNameBlockItem(GUMBOSEED_CROP.get(), new Item.Properties()));

    // ========== 小米种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> MILLET_CROP = BLOCKS.register("millet",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> MILLET = ITEMS.register("millet",
            () -> new ItemNameBlockItem(MILLET_CROP.get(), new Item.Properties()));

    // ========== 绿豆种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> MUNGBEANPLANT_CROP = BLOCKS.register("mungbeanplant",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> MUNGBEANPLANT = ITEMS.register("mungbeanplant",
            () -> new ItemNameBlockItem(MUNGBEANPLANT_CROP.get(), new Item.Properties()));

    // ========== 芥末种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> MUSTRAD_SEED_CROP = BLOCKS.register("mustrad_seed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> MUSTRAD_SEED = ITEMS.register("mustrad_seed",
            () -> new ItemNameBlockItem(MUSTRAD_SEED_CROP.get(), new Item.Properties()));

    // ========== 肉豆蔻种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> NUTMEGSEED_CROP = BLOCKS.register("nutmegseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> NUTMEGSEED = ITEMS.register("nutmegseed",
            () -> new ItemNameBlockItem(NUTMEGSEED_CROP.get(), new Item.Properties()));

    // ========== 燕麦种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> OATSEED_CROP = BLOCKS.register("oatseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> OATSEED = ITEMS.register("oatseed",
            () -> new ItemNameBlockItem(OATSEED_CROP.get(), new Item.Properties()));

    // ========== 油菜种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> OILRAPESEED_CROP = BLOCKS.register("oilrapeseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> OILRAPESEED = ITEMS.register("oilrapeseed",
            () -> new ItemNameBlockItem(OILRAPESEED_CROP.get(), new Item.Properties()));

    // ========== 洋葱种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> ONIONSEED_CROP = BLOCKS.register("onionseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> ONIONSEED = ITEMS.register("onionseed",
            () -> new ItemNameBlockItem(ONIONSEED_CROP.get(), new Item.Properties()));

    // ========== 豌豆种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> PEASEED_CROP = BLOCKS.register("peaseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> PEASEED = ITEMS.register("peaseed",
            () -> new ItemNameBlockItem(PEASEED_CROP.get(), new Item.Properties()));

    // ========== 紫薯种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> PUPLESWEETPOTATOSEED_CROP = BLOCKS.register("puplesweetpotatoseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> PUPLESWEETPOTATOSEED = ITEMS.register("puplesweetpotatoseed",
            () -> new ItemNameBlockItem(PUPLESWEETPOTATOSEED_CROP.get(), new Item.Properties()));

    // ========== 芝麻种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> SESAMESEED_CROP = BLOCKS.register("sesameseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> SESAMESEED = ITEMS.register("sesameseed",
            () -> new ItemNameBlockItem(SESAMESEED_CROP.get(), new Item.Properties()));

    // ========== 黄豆种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> SOY_BEAN_SEED_CROP = BLOCKS.register("soy_bean_seed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> SOY_BEAN_SEED = ITEMS.register("soy_bean_seed",
            () -> new ItemNameBlockItem(SOY_BEAN_SEED_CROP.get(), new Item.Properties()));

    // ========== 青椒种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> SWEETGREENPEPPERSEED_CROP = BLOCKS.register("sweetgreenpepperseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> SWEETGREENPEPPERSEED = ITEMS.register("sweetgreenpepperseed",
            () -> new ItemNameBlockItem(SWEETGREENPEPPERSEED_CROP.get(), new Item.Properties()));

    // ========== 西葫芦种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> ZUCCHINISEED_CROP = BLOCKS.register("zucchiniseed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> ZUCCHINISEED = ITEMS.register("zucchiniseed",
            () -> new ItemNameBlockItem(ZUCCHINISEED_CROP.get(), new Item.Properties()));

    // ========== 菠菜种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> SPINACH_SEED_CROP = BLOCKS.register("spinach_seed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> SPINACH_SEED = ITEMS.register("spinach_seed",
            () -> new ItemNameBlockItem(SPINACH_SEED_CROP.get(), new Item.Properties()));

    // ========== 菜花种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> CAULIFLOWER_SEED_CROP = BLOCKS.register("cauliflower_seed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> CAULIFLOWER_SEED = ITEMS.register("cauliflower_seed",
            () -> new ItemNameBlockItem(CAULIFLOWER_SEED_CROP.get(), new Item.Properties()));

    // ========== 葱种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> SCALLION_SEED_CROP = BLOCKS.register("scallion_seed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> SCALLION_SEED = ITEMS.register("scallion_seed",
            () -> new ItemNameBlockItem(SCALLION_SEED_CROP.get(), new Item.Properties()));

    // ========== 丁香种子作物（产物与种子为同一物品） ==========
    public static final DeferredBlock<FIDCropBlock> LILAC_SEED_CROP = BLOCKS.register("lilac_seed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> LILAC_SEED = ITEMS.register("lilac_seed",
            () -> new ItemNameBlockItem(LILAC_SEED_CROP.get(), new Item.Properties()));

    // ========== 红豆种子作物（产物与种子为同一物品） ==========
    public static final DeferredBlock<FIDCropBlock> RED_BEAN_BLOCK_CROP = BLOCKS.register("red_bean_block",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> RED_BEAN_BLOCK = ITEMS.register("red_bean_block",
            () -> new ItemNameBlockItem(RED_BEAN_BLOCK_CROP.get(), new Item.Properties()));

    // ========== 红尖椒种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> RED_PEPPER_SEED_CROP = BLOCKS.register("red_pepper_seed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> RED_PEPPER_SEED = ITEMS.register("red_pepper_seed",
            () -> new ItemNameBlockItem(RED_PEPPER_SEED_CROP.get(), new Item.Properties()));

    // ========== 红薯种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> SWEET_POTATO_SEED_CROP = BLOCKS.register("sweet_potato_seed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> SWEET_POTATO_SEED = ITEMS.register("sweet_potato_seed",
            () -> new ItemNameBlockItem(SWEET_POTATO_SEED_CROP.get(), new Item.Properties()));

    // ========== 花椒种子作物（产物与种子为同一物品） ==========
    public static final DeferredBlock<FIDCropBlock> SI_CHUAN_PEPPER_SEED_CROP = BLOCKS.register("si_chuan_pepper_seed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> SI_CHUAN_PEPPER_SEED = ITEMS.register("si_chuan_pepper_seed",
            () -> new ItemNameBlockItem(SI_CHUAN_PEPPER_SEED_CROP.get(), new Item.Properties()));

    // ========== 花生种子作物 ==========
    public static final DeferredBlock<FIDCropBlock> PEA_NUT_SEED_CROP = BLOCKS.register("pea_nut_seed",
            () -> new FIDCropBlock(cropProperties(), 4));
    public static final DeferredItem<ItemNameBlockItem> PEA_NUT_SEED = ITEMS.register("pea_nut_seed",
            () -> new ItemNameBlockItem(PEA_NUT_SEED_CROP.get(), new Item.Properties()));

    // ========== 西蓝花果实 ==========
    public static final DeferredItem<Item> BROCCOIL = ITEMS.register("broccoil", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    // ========== 荞麦果实 ==========
    public static final DeferredItem<Item> BUCKWHEAT = ITEMS.register("buckwheat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    // ========== 卷心菜果实 ==========
    public static final DeferredItem<Item> CABBAGE = ITEMS.register("cabbage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    // ========== 木薯果实 ==========
    public static final DeferredItem<Item> CASSAVA = ITEMS.register("cassava", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    // ========== 芹菜果实 ==========
    public static final DeferredItem<Item> CELERY = ITEMS.register("celery", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    // ========== 韭菜果实 ==========
    public static final DeferredItem<Item> CHINESECHIVES = ITEMS.register("chinesechives", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    // ========== 山药果实 ==========
    public static final DeferredItem<Item> CHINESEYAM = ITEMS.register("chineseyam", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    // ========== 玉米果实 ==========
    public static final DeferredItem<Item> CORN = ITEMS.register("corn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    // ========== 孜然果实 ==========
    public static final DeferredItem<Item> CUMIN = ITEMS.register("cumin", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    // ========== 茴香果实 ==========
    public static final DeferredItem<Item> FENNEL = ITEMS.register("fennel", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    // ========== 大蒜果实 ==========
    public static final DeferredItem<Item> GARLIC = ITEMS.register("garlic", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    // ========== 生姜果实 ==========
    public static final DeferredItem<Item> GINGER = ITEMS.register("ginger", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    // ========== 青尖椒果实 ==========
    public static final DeferredItem<Item> GREENPEPPER = ITEMS.register("greenpepper", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    // ========== 秋葵果实 ==========
    public static final DeferredItem<Item> GUMBO = ITEMS.register("gumbo", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    // ========== 小米果实 ==========
    public static final DeferredItem<Item> MILLETGRAIN_GRAIN = ITEMS.register("millet_grain", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    // ========== 绿豆果实 ==========
    public static final DeferredItem<Item> MUNGBEAN = ITEMS.register("mungbean", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    // ========== 芥末果实 ==========
    public static final DeferredItem<Item> MUSTARD = ITEMS.register("mustard", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    // ========== 燕麦果实 ==========
    public static final DeferredItem<Item> OAT = ITEMS.register("oat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    // ========== 油菜果实 ==========
    public static final DeferredItem<Item> OILSEEDRAPE = ITEMS.register("oilseedrape", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    // ========== 洋葱果实 ==========
    public static final DeferredItem<Item> ONION = ITEMS.register("onion", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    // ========== 豌豆果实 ==========
    public static final DeferredItem<Item> PEA = ITEMS.register("pea", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    // ========== 紫薯果实 ==========
    public static final DeferredItem<Item> PURPLESWEETPOTATO = ITEMS.register("purplesweetpotato", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    // ========== 芝麻果实 ==========
    public static final DeferredItem<Item> SESAME = ITEMS.register("sesame", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    // ========== 黄豆果实 ==========
    public static final DeferredItem<Item> SOYBEAN = ITEMS.register("soybean", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build())));
    // ========== 青椒果实 ==========
    public static final DeferredItem<Item> SWEETGREENPEPPER = ITEMS.register("sweetgreenpepper", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));
    // ========== 西葫芦果实 ==========
    public static final DeferredItem<Item> ZUCCHINI = ITEMS.register("zucchini", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

    // ========== 杂项材料 - 基础 ==========
    public static final DeferredItem<Item> SORBET = ITEMS.register("sorbet", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CASING = ITEMS.register("casing", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> HONEYCOMBBRIQUET = ITEMS.register("honeycombbriquet", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BRAN = ITEMS.register("bran", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> REEDLEAF = ITEMS.register("reedleaf", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.reedleaf_harvest"), () -> java.util.List.of(reedleafTooltipIcon())));
    public static final DeferredItem<Item> CASSAVAPEARL = ITEMS.register("cassavapearl", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SOAKEDSOYBEANS = ITEMS.register("soakedsoybeans", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MEATFLOSS = ITEMS.register("meatfloss", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> GRAVELPASTE = ITEMS.register("gravelpaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWSOYSHREDDEDMEAT = ITEMS.register("rawsoyshreddedmeat", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LANDPLASTER = ITEMS.register("landplaster", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WRESTLING_GUN = ITEMS.register("wrestling_gun", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> RAWSHEEPOFFAL = ITEMS.register("rawsheepoffal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TIDYREEDLEAF = ITEMS.register("tidyreedleaf", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NAHCO_3 = ITEMS.register("nahco_3", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PROBIOTICS = ITEMS.register("probiotics", () -> new Item(new Item.Properties()));
    // ========== 杂项材料 - 切过的菜 ==========
    public static final DeferredItem<Item> SCALLION_2 = ITEMS.register("scallion_2", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDCARROTS = ITEMS.register("dicedcarrots", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SHREDDEDCARROTS = ITEMS.register("shreddedcarrots", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDCUCUMBER = ITEMS.register("dicedcucumber", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SLICEDCUCUMBER = ITEMS.register("slicedcucumber", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SHREDDERCUCUMBER = ITEMS.register("shreddercucumber", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SLICEDABUERGINE = ITEMS.register("slicedabuergine", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SHREDDERABUERGINE = ITEMS.register("shredderabuergine", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CHOPPEDCOWPEA = ITEMS.register("choppedcowpea", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DIECEDRADISH = ITEMS.register("diecedradish", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDRADISH = ITEMS.register("dicedradish", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SLICEDRADISH = ITEMS.register("slicedradish", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SHREDDERRADISH = ITEMS.register("shredderradish", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDCASSAVA = ITEMS.register("dicedcassava", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SLICEDLEMON = ITEMS.register("slicedlemon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDLOTUSROOT = ITEMS.register("dicedlotusroot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SLICEDLOTUSROOT = ITEMS.register("slicedlotusroot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDONION = ITEMS.register("dicedonion", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDORANGE = ITEMS.register("dicedorange", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDWAXGOURD = ITEMS.register("dicedwaxgourd", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDPLEUROTUSERYNGII = ITEMS.register("dicedpleurotuseryngii", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDCELERY = ITEMS.register("dicedcelery", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDSWEETPEPPER = ITEMS.register("dicedsweetpepper", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDCHINESEYAM = ITEMS.register("dicedchineseyam", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDFISH = ITEMS.register("dicedfish", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDLOOFACH = ITEMS.register("dicedloofach", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDSAUTEEDGREENBEANS = ITEMS.register("dicedsauteedgreenbeans", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDPOTATO = ITEMS.register("dicedpotato", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SLICEDPOTATO = ITEMS.register("slicedpotato", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SHREDDEDPOTATO_2 = ITEMS.register("shreddedpotato_2", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CHOPPEDPOTATO_1 = ITEMS.register("choppedpotato_1", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDZUCCHINI = ITEMS.register("dicedzucchini", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDPURPLESWEETPOTATO = ITEMS.register("dicedpurplesweetpotato", () -> new Item(new Item.Properties()));
    // ========== 杂项材料 - 和面案板 ==========
    public static final DeferredItem<Item> BAOZI_SKIN = ITEMS.register("baozi_skin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> VERMICELLIROLL = ITEMS.register("vermicelliroll", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BEATGLUTINOUSRICEFLOURPASTE = ITEMS.register("beatglutinousriceflourpaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRYPOWDERSKIN = ITEMS.register("drypowderskin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRIEDVERMICELLI = ITEMS.register("driedvermicelli", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRYPOWDERSTRIP = ITEMS.register("drypowderstrip", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EGGNOODLESWRAPPEDINSYRUP = ITEMS.register("eggnoodleswrappedinsyrup", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NOODLESWRAPPEDINSYRUP = ITEMS.register("noodleswrappedinsyrup", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WALNUTFLOURPASTE = ITEMS.register("walnutflourpaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SWEETPOTATODOUGH = ITEMS.register("sweetpotatodough", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLOURSKIN = ITEMS.register("flourskin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EGGDOUGH = ITEMS.register("eggdough", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWEGGNOODLES = ITEMS.register("raweggnoodles", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DUMPLING_SKIN = ITEMS.register("dumpling_skin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWRICENOODLES = ITEMS.register("rawricenoodles", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RICENOODLES_2 = ITEMS.register("ricenoodles_2", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLOURPASTE = ITEMS.register("flourpaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DOUGH = ITEMS.register("dough", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SHUMAISKIN = ITEMS.register("shumaiskin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWCOARSENOODLES = ITEMS.register("rawcoarsenoodles", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRYRICECAKE = ITEMS.register("dryricecake", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWEGGPANCAKE = ITEMS.register("raweggpancake", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWEGGPASTE = ITEMS.register("raweggpaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWEGGSKIN = ITEMS.register("raweggskin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWFLOURPANCAKE = ITEMS.register("rawflourpancake", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWFLOURROLL = ITEMS.register("rawflourroll", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWNOODLES = ITEMS.register("rawnoodles", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWGLUTINOUSRICEFLOURPANCAKE = ITEMS.register("rawglutinousriceflourpancake", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWGLUTINOUSPASTE = ITEMS.register("rawglutinouspaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWGLUTINOUSSKIN = ITEMS.register("rawglutinousskin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWGLUTINOUSNOODLES = ITEMS.register("rawglutinousnoodles", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWGLUTINOUSDOUGH = ITEMS.register("rawglutinousdough", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWSHUMAI = ITEMS.register("rawshumai", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWPIE = ITEMS.register("rawpie", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWMOONCAKE = ITEMS.register("rawmooncake", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> OILEDFLOURSKIN = ITEMS.register("oiledflourskin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPACTEDRICEBRICK = ITEMS.register("compactedricebrick", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MACARONI = ITEMS.register("macaroni", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STEAMEDRICEBRICK = ITEMS.register("steamedricebrick", () -> new Item(new Item.Properties()));
    // ========== 杂项材料 - 泥，馅料，碎 ==========
    public static final DeferredItem<Item> BROKENWALNUT = ITEMS.register("brokenwalnut", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EGGSTUFFING = ITEMS.register("eggstuffing", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FRUITFLAVOREDMOONCAKESTUFFING = ITEMS.register("fruitflavoredmooncakestuffing", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SESAMEANDPEANUTBALLS = ITEMS.register("sesameandpeanutballs", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WAXGROUDPASTE = ITEMS.register("waxgroudpaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> REDBEANPASTE = ITEMS.register("redbeanpaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PEPPERANDSALTMASS = ITEMS.register("pepperandsaltmass", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MEATANDVEGETABLESTUFFING = ITEMS.register("meatandvegetablestuffing", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MEATANDEGGPASTE = ITEMS.register("meatandeggpaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MEATPASTE = ITEMS.register("meatpaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> VEGETABLEPASTE = ITEMS.register("vegetablepaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PEASTUFFING = ITEMS.register("peastuffing", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> JUJUBEPASTE = ITEMS.register("jujubepaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SEASAMEGLUTINOUSRICEBALLS = ITEMS.register("seasameglutinousriceballs", () -> new Item(new Item.Properties()));
    // ========== 杂项材料 - 液体，酱，糊 ==========
    public static final DeferredItem<Item> TIDYWATER = ITEMS.register("tidywater", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EGGLIQUID = ITEMS.register("eggliquid", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PEANUTJAM = ITEMS.register("peanutjam", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EGGBATTER = ITEMS.register("eggbatter", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COCOASAUCE = ITEMS.register("cocoasauce", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PANADA = ITEMS.register("panada", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MILKBOTTLE = ITEMS.register("milkbottle", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CREAM = ITEMS.register("cream", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CONCENTRATEDSYRUP = ITEMS.register("concentratedsyrup", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GLUTINOUSRICEBATTER = ITEMS.register("glutinousricebatter", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWSOYBEANMILK = ITEMS.register("rawsoybeanmilk", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GELATIN = ITEMS.register("gelatin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SWEETEGGLIQUID = ITEMS.register("sweeteggliquid", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MULTIGRAINBATTER = ITEMS.register("multigrainbatter", () -> new Item(new Item.Properties()));
    // ========== 杂项材料 - 煮蒸炸烤半成品 ==========
    public static final DeferredItem<Item> RAWORLEANSCHICKENWING = ITEMS.register("raworleanschickenwing", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWORLEANSCHICKENLEG = ITEMS.register("raworleanschickenleg", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COOKED_BAOZI = ITEMS.register("cooked_baozi", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FATBEEFROLL = ITEMS.register("fatbeefroll", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LAMBROLL = ITEMS.register("lambroll", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MUSHROOMSWRAPPEDINBATTER = ITEMS.register("mushroomswrappedinbatter", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWCHICKENWINGWITHBATTER = ITEMS.register("rawchickenwingwithbatter", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWCHICKENHALFLEGWITHBATTER = ITEMS.register("rawchickenhalflegwithbatter", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWCHICKENMEATWITHBATTER = ITEMS.register("rawchickenmeatwithbatter", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWSAUSAGE = ITEMS.register("rawsausage", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWMEATBALLWITHEGGBALL = ITEMS.register("rawmeatballwitheggball", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FROZENMILK = ITEMS.register("frozenmilk", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWDICEDCHICKENWITHBATTER = ITEMS.register("rawdicedchickenwithbatter", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WONTON = ITEMS.register("wonton", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NORMALMEATROLL = ITEMS.register("normalmeatroll", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CARAMELCORNKENNELS = ITEMS.register("caramelcornkennels", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COOKEDDUMPLING = ITEMS.register("cookeddumpling", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SALTYEGG = ITEMS.register("saltyegg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> SALTYRADDISH = ITEMS.register("saltyraddish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> TANGYUAN = ITEMS.register("tangyuan", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> THOUSAND_LAYER_TOFU_SKIN = ITEMS.register("thousand_layer_tofu_skin", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> SPAGHETTI = ITEMS.register("spaghetti", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> CREAMCORNKERNELS = ITEMS.register("creamcornkernels", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CHOCOLATECORNKERNELS = ITEMS.register("chocolatecornkernels", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWSPRINGROLL = ITEMS.register("rawspringroll", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWEGGTART = ITEMS.register("raweggtart", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SWEETREDBEANEGGTART = ITEMS.register("sweetredbeaneggtart", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWSPICYGLUTEN = ITEMS.register("rawspicygluten", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWCOUPLING = ITEMS.register("rawcoupling", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWAIKUI = ITEMS.register("rawaikui", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MEATFLOURROOL = ITEMS.register("meatflourrool", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> RAWFLOURPASTEWITHDRIEDMEATFLOSS = ITEMS.register("rawflourpastewithdriedmeatfloss", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWAZONGZI = ITEMS.register("rawazongzi", () -> new Item(new Item.Properties()));
    
    public static final DeferredItem<Item> RAW_TANGYUAN = ITEMS.register("raw_tangyuan", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWDORAYAKI = ITEMS.register("rawdorayaki", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWSESAMEGLUTINOUSPASTE = ITEMS.register("rawsesameglutinouspaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRISPYPORKBELLY = ITEMS.register("crispyporkbelly", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAW_STUFFEDGREENPEPPER = ITEMS.register("raw_stuffedgreenpepper", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWSALTYEGG = ITEMS.register("rawsaltyegg", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWWRAPPEDMILK = ITEMS.register("rawwrappedmilk", () -> new Item(new Item.Properties()));
    // ========== 杂项材料 - 粉类 ==========
    public static final DeferredItem<Item> MODULATEDWHEATFLOUR = ITEMS.register("modulatedwheatflour", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WALNUTPOWDER = ITEMS.register("walnutpowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SWEETPOTATSTARCH = ITEMS.register("sweetpotatstarch", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PEANUTPOWDER = ITEMS.register("peanutpowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PEANUTSESAMEPOWDER = ITEMS.register("peanutsesamepowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COFFEEPOWDER = ITEMS.register("coffeepowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COCOAPOWDER = ITEMS.register("cocoapowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WHEATFLOUR = ITEMS.register("wheatflour", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TAPIOCAFLOUR = ITEMS.register("tapiocaflour", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GULTINOUSRICEPOWDER = ITEMS.register("gultinousricepowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PEAMEAL = ITEMS.register("peameal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SESAMEPOWDER = ITEMS.register("sesamepowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GULTINOUSRICESASAMEPOWDER = ITEMS.register("gultinousricesasamepowder", () -> new Item(new Item.Properties()));
    // ========== 杂项材料 - 调味料 ==========
    public static final DeferredItem<Item> CRYSTALSUGAR = ITEMS.register("crystalsugar", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> VINEGAR = ITEMS.register("vinegar", () -> new SeasoningItem(new Item.Properties()));
    public static final DeferredItem<Item> THICKBROADBEANSAUCE = ITEMS.register("thickbroadbeansauce", () -> new SeasoningItem(new Item.Properties()));
    public static final DeferredItem<Item> TOMATO_HOT_POT_BASE = ITEMS.register("tomato_hot_pot_base", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BONESOUPESSENCE = ITEMS.register("bonesoupessence", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BROWNSUGAR = ITEMS.register("brownsugar", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BUTTER = ITEMS.register("butter", () -> new SeasoningItem(new Item.Properties()));
    public static final DeferredItem<Item> SOY = ITEMS.register("soy", () -> new SeasoningItem(new Item.Properties()));
    public static final DeferredItem<Item> CURRY = ITEMS.register("curry", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> HALOGENBAG = ITEMS.register("halogenbag", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SPICY_HOT_POT_BASE = ITEMS.register("spicy_hot_pot_base", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MAJUICE = ITEMS.register("majuice", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WHITESUGARSYRUP = ITEMS.register("whitesugarsyrup", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COOKINGOIL = ITEMS.register("cookingoil", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PEPPER_HOT_POT_BASE = ITEMS.register("pepper_hot_pot_base", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SWEETFLOURASUVE = ITEMS.register("sweetflourasuve", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SESAMEOIL = ITEMS.register("sesameoil", () -> new SeasoningItem(new Item.Properties()));
    public static final DeferredItem<Item> SALT = ITEMS.register("salt", () -> new SeasoningItem(new Item.Properties()));
    public static final DeferredItem<Item> SALTPIECE = ITEMS.register("saltpiece", () -> new Item(new Item.Properties()));
    // ========== 杂项材料 - 调味料粉 ==========
    public static final DeferredItem<Item> ORLEANSPOWDER = ITEMS.register("orleanspowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ANISEEDPOWDER = ITEMS.register("aniseedpowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ONIONPOWDER = ITEMS.register("onionpowder", () -> new SeasoningItem(new Item.Properties()));
    public static final DeferredItem<Item> LILACPOWDER = ITEMS.register("lilacpowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CINNAMONPOWDER = ITEMS.register("cinnamonpowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> REDTEAPOWDER = ITEMS.register("redteapowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CHINESEPICKLYASHPOWDER = ITEMS.register("chinesepicklyashpowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FENNELPOWDER = ITEMS.register("fennelpowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GROUNDPOWDER = ITEMS.register("groundpowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PEPPEREDSALT = ITEMS.register("pepperedsalt", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CHILLIPOWDER = ITEMS.register("chillipowder", () -> new SeasoningItem(new Item.Properties()));
    public static final DeferredItem<Item> GREENTEAPOWDER = ITEMS.register("greenteapowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GARLICPOWDER = ITEMS.register("garlicpowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FIVESPICEPOWDER = ITEMS.register("fivespicepowder", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ONIONPOWDER_2 = ITEMS.register("onionpowder_2", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CUMINPOWDER = ITEMS.register("cuminpowder", () -> new Item(new Item.Properties()));

    /**
     * 用于打破死动物 ↔ BIGHOOK_ITEM 之间的循环前向引用。
     * 提供两个图标：斩骨刀 + 大挂钩。
     */
    private static class DeadAnimalIcons implements java.util.function.Supplier<List<ItemStack>> {
        @Override
        public List<ItemStack> get() {
            return List.of(new ItemStack(BONECUTTERKNIFE.get()), new ItemStack(BIGHOOK_ITEM.get()));
        }
    }

    /**
     * 放血后鸡的图标：木盆 + 纯净水。
     */
    private static class ChickenBloodIcons implements java.util.function.Supplier<List<ItemStack>> {
        @Override
        public List<ItemStack> get() {
            return List.of(new ItemStack(WOODBASIN_ITEM.get()), new ItemStack(TIDYWATER.get()));
        }
    }

    /** 芦苇叶 tooltip 图标：除草剪刀（延迟解析以避免前向引用） */
    private static ItemStack reedleafTooltipIcon() {
        return new ItemStack(KITCHENSCISSOR.get());
    }

    // ===== 动物尸体物品 =====
    public static final DeferredItem<Item> DEADCATTLE = ITEMS.register("deadcattle", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.dead_animal_harvest"), () -> new DeadAnimalIcons().get()));
    public static final DeferredItem<Item> DEADSHEEP = ITEMS.register("deadsheep", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.dead_animal_harvest"), () -> new DeadAnimalIcons().get()));
    public static final DeferredItem<Item> DEADPIG = ITEMS.register("deadpig", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.dead_animal_harvest"), () -> new DeadAnimalIcons().get()));
    public static final DeferredItem<Item> DEADCHICKEN = ITEMS.register("deadchicken", () -> new TooltipItem(new Item.Properties(),
            Component.translatable("tooltip.flavor_immersed_daily.dead_animal_harvest"), () -> new DeadAnimalIcons().get()));
    public static final DeferredItem<Item> CHICKENWITHOUTFEATHER = ITEMS.register("chickenwithoutfeather",
            () -> new TooltipItem(new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.chicken_without_feather"), () -> java.util.List.of()));
    public static final DeferredItem<Item> CHICKENWITHOUTBLOOD = ITEMS.register("chickenwithoutblood",
            () -> new TooltipItem(new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.chicken_without_blood"), () -> new ChickenBloodIcons().get()));

    // ========== 工具方块 ==========
    public static final DeferredBlock<BighookBlock> BIGHOOK = BLOCKS.register("bighook",
            () -> new BighookBlock(DEADCATTLE, DEADSHEEP, DEADPIG, DEADCHICKEN, CHICKENWITHOUTFEATHER, CHICKENWITHOUTBLOOD));
    public static final DeferredItem<BlockItem> BIGHOOK_ITEM = ITEMS.register("bighook",
            () -> new TooltipBlockItem(BIGHOOK.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.bighook"),
                    () -> {
                        java.util.List<ItemStack> list = new java.util.ArrayList<>();
                        list.add(new ItemStack(DEADCATTLE.get()));
                        list.add(new ItemStack(DEADSHEEP.get()));
                        list.add(new ItemStack(DEADPIG.get()));
                        list.add(new ItemStack(DEADCHICKEN.get()));
                        list.add(new ItemStack(CHICKENWITHOUTFEATHER.get()));
                        list.add(new ItemStack(CHICKENWITHOUTBLOOD.get()));
                        return java.util.List.copyOf(list);
                    }));

    // ========== 木盆 ==========
    public static final DeferredBlock<WoodBasinBlock> WOODBASIN = BLOCKS.register("woodbasin",
            () -> new WoodBasinBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f, 3.0f)
                    .sound(SoundType.WOOD)));
    public static final DeferredItem<BlockItem> WOODBASIN_ITEM = ITEMS.register("woodbasin",
            () -> new BlockItem(WOODBASIN.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WoodBasinBlockEntity>> WOODBASIN_ENTITY = BLOCK_ENTITIES.register("woodbasin_entity",
            () -> BlockEntityType.Builder.of(WoodBasinBlockEntity::new, WOODBASIN.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BighookBlockEntity>> BIGHOOK_BE = BLOCK_ENTITIES.register("bighook_be",
            () -> BlockEntityType.Builder.of(BighookBlockEntity::new, BIGHOOK.get()).build(null));

    // ========== 果汁 ==========
    public static final DeferredBlock<JuiceBlock> HAMIMELONJUICE_BLOCK = BLOCKS.register("hamimelonjuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> HAWTHORNJUIVE_BLOCK = BLOCKS.register("hawthornjuive",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> DRAGONFRUIEJUICE_BLOCK = BLOCKS.register("dragonfruiejuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> CARAMBOLAJUICE_BLOCK = BLOCKS.register("carambolajuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> WINTERJUJUBEJUICE_BLOCK = BLOCKS.register("winterjujubejuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> POMEGRANATEJUICE_BLOCK = BLOCKS.register("pomegranatejuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> SWEETBERRYJUICE_BLOCK = BLOCKS.register("sweetberryjuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> DURIANJUICE_BLOCK = BLOCKS.register("durianjuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> LEMONJUICE_BLOCK = BLOCKS.register("lemonjuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> BLUEBERRYJUICE_BLOCK = BLOCKS.register("blueberryjuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> SWEETMELONJUICE_BLOCK = BLOCKS.register("sweetmelonjuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> GREENGRAPEJUICE_BLOCK = BLOCKS.register("greengrapejuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> TANGERINEJUICE_BLOCK = BLOCKS.register("tangerinejuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> MANGOSTEENJUICE_BLOCK = BLOCKS.register("mangosteenjuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> CHERRYJUICE_BLOCK = BLOCKS.register("cherryjuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> STRAWBERRYJUICE_BLOCK = BLOCKS.register("strawberryjuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> ORANGEJUICE_BLOCK = BLOCKS.register("orangejuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> GREENPLUMJUICE_BLOCK = BLOCKS.register("greenplumjuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> APPLEJUICE_BLOCK = BLOCKS.register("applejuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> HONEYPEACHJUIVE_BLOCK = BLOCKS.register("honeypeachjuive",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> BANANAJUICE_BLOCK = BLOCKS.register("bananajuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> LYCHEEJUICE_BLOCK = BLOCKS.register("lycheejuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> APRICOTJUICE_BLOCK = BLOCKS.register("apricotjuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> GRAPEJUICE_BLOCK = BLOCKS.register("grapejuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> MULBERRYJUIVE_BLOCK = BLOCKS.register("mulberryjuive",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> PKUMJUICE_BLOCK = BLOCKS.register("pkumjuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> MANGOJUICE_BLOCK = BLOCKS.register("mangojuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> WATERMELONJUICE_BLOCK = BLOCKS.register("watermelonjuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> NECTARINEJUICE_BLOCK = BLOCKS.register("nectarinejuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> PINEAPPLEJUICE_BLOCK = BLOCKS.register("pineapplejuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> LOQUATJUICE_BLOCK = BLOCKS.register("loquatjuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> KIWIFRUITJUICE_BLOCK = BLOCKS.register("kiwifruitjuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> PEARJUICE_BLOCK = BLOCKS.register("pearjuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> PAWPAWJUICE_BLOCK = BLOCKS.register("pawpawjuice",
            () -> new JuiceBlock(5, 0.8f));
    public static final DeferredBlock<JuiceBlock> COCONUTJUICE_BLOCK = BLOCKS.register("coconutjuice",
            () -> new JuiceBlock(5, 0.8f));

    public static final DeferredItem<Item> SHARPKNIFE = ITEMS.register("sharpknife", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WIDEEDGEDKNIFE = ITEMS.register("wideedgedknife", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BONECUTTERKNIFE = ITEMS.register("bonecutterknife", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> KITCHENKNIFE = ITEMS.register("kitchenknife", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> KITCHENSCISSOR = ITEMS.register("kitchenscissor", () -> new KitchenScissorsItem(new Item.Properties()));
    public static final DeferredItem<Item> SPATULA = ITEMS.register("spatula", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MALLET = ITEMS.register("mallet", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COARSECLOTH = ITEMS.register("coarsecloth", () -> new CoarseClothItem(new Item.Properties()));
    public static final DeferredItem<Item> MOONCAKEMOLD = ITEMS.register("mooncakemold", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ROLLINGPIN = ITEMS.register("rollingpin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<com.flavor_immersed_daily.item.PurifiedWaterBucketItem> FILTERVAT = ITEMS.register("filtervat", () -> new com.flavor_immersed_daily.item.PurifiedWaterBucketItem(new Item.Properties()));
    public static final DeferredItem<Item> EEGGPUFFSMOULD = ITEMS.register("eeggpuffsmould", () -> new Item(new Item.Properties()));

    // ========== 杂项：切过的菜 ==========
    public static final DeferredItem<Item> DIECEDPRESERVEDEGG = ITEMS.register("diecedpreservedegg", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CUTDIECEDPRESERVEEGG = ITEMS.register("cutdiecedpreserveegg", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDSWEETPOTATO = ITEMS.register("dicedsweetpotato", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PURPLEDICEDSWEETPOTATO = ITEMS.register("purpledicedsweetpotato", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PROCESSEDCABBAGE = ITEMS.register("processedcabbage", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDTOMATO = ITEMS.register("dicedtomato", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CUTCABBAGE = ITEMS.register("cutcabbage", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CASSAVACHUNKS = ITEMS.register("cassavachunks", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SLICEDZUCCHINI = ITEMS.register("slicedzucchini", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CARROTCHUNKS = ITEMS.register("carrotchunks", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CELERYLEAF = ITEMS.register("celeryleaf", () -> new Item(new Item.Properties()));

    // ========== 杂项：泥，馅料，碎 ==========
    public static final DeferredItem<Item> PEAPASTE = ITEMS.register("peapaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SESAMEGLUTINOUSRICEBALLS = ITEMS.register("sesameglutinousriceballs", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MEATANDEGGFILLING = ITEMS.register("meatandeggfilling", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MEATANDVEGETABLEFILLING = ITEMS.register("meatandvegetablefilling", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MUNGBEANPASTE = ITEMS.register("mungbeanpaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WAXGOURDPASTE = ITEMS.register("waxgourdpaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> UNCOMMONSTUFFING = ITEMS.register("uncommonstuffing", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> VEGETABLEANDEGGSTUFFING = ITEMS.register("vegetableandeggstuffing", () -> new Item(new Item.Properties()));

    // ========== 杂项：调味料 ==========
    public static final DeferredItem<Item> BROWNSUGARSYRUP = ITEMS.register("brownsugarsyrup", () -> new com.flavor_immersed_daily.item.SeasoningItem(new Item.Properties()));
    public static final DeferredItem<Item> HOTPOTBASETEMPLATE = ITEMS.register("hotpotbasetemplate", () -> new Item(new Item.Properties()));

    // ========== 杂项：液体，酱，糊 ==========
    public static final DeferredItem<Item> SALTYWATER = ITEMS.register("saltywater", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COCOAMASS = ITEMS.register("cocoamass", () -> new Item(new Item.Properties()));

    // ========== 杂项：煮蒸炸烤半成品 ==========
    public static final DeferredItem<Item> EGGCOATEDMEATBALLS = ITEMS.register("eggcoatedmeatballs", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BATTEREDCHICKENBREASTCHUNKS = ITEMS.register("batteredchickenbreastchunks", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAWMEATFLOSSDOUGH = ITEMS.register("rawmeatflossdough", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWMEATZONGZI = ITEMS.register("rawmeatzongzi", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BATTEREDMUSHROOMS = ITEMS.register("batteredmushrooms", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWSWEETZONGZI = ITEMS.register("rawsweetzongzi", () -> new Item(new Item.Properties()));

    // ========== 杂项：和面案板 ==========
    public static final DeferredItem<Item> CUTPANCAKE = ITEMS.register("cutpancake", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FERMENTEDFLOURPASTE = ITEMS.register("fermentedflourpaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CUTEGGPANCAKE = ITEMS.register("cuteggpancake", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RICESLURRYWRAPPEDINFILTERCLOTH = ITEMS.register("riceslurrywrappedinfiltercloth", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POUNDEDGLUTINOUSPASTE = ITEMS.register("poundedglutinouspaste", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LONGDOUGH = ITEMS.register("longdough", () -> new Item(new Item.Properties()));
    
    public static final DeferredItem<Item> LONGEGGPANCAKE = ITEMS.register("longeggpancake", () -> new Item(new Item.Properties()));

    // ========== 杂项：其他 ==========
    public static final DeferredItem<Item> SOYBEANPROTEIN = ITEMS.register("soybeanprotein", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAW_PIGOFFAL = ITEMS.register("raw_pigoffal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DEBONEDCHICKENFEET = ITEMS.register("debonedchickenfeet", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EGGWRAPPEDINGRAVEL = ITEMS.register("eggwrappedingravel", () -> new Item(new Item.Properties()));

    // ========== 杂项：粉类 ==========
    public static final DeferredItem<Item> PEAFLOUR = ITEMS.register("peaflour", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> FIVEPOINTEDCARAMBOLADELIGHT = ITEMS.register("fivepointedcaramboladelight", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> RIPEPEARWITHROCKSUGAR = ITEMS.register("ripepearwithrocksugar", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> SUGARCOATEDHAWS = ITEMS.register("sugarcoatedhaws", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEWEDPIGLEG = ITEMS.register("stewedpigleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEWEDHALFCHICKENLEG = ITEMS.register("stewedhalfchickenleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEWEDCHICKENHEART = ITEMS.register("stewedchickenheart", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEWEDCHICKENLIVERS = ITEMS.register("stewedchickenlivers", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEWEDCHICKENLEG = ITEMS.register("stewedchickenleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> SAUCINGBEEF = ITEMS.register("saucingbeef", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> YOGURT = ITEMS.register("yogurt", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> SALTEGGYOLK = ITEMS.register("salteggyolk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> SALTTOFUCURD = ITEMS.register("salttofucurd", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> DICEDHAMIMELON = ITEMS.register("dicedhamimelon", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRIED_TOFU_SKIN_ROLLS = ITEMS.register("fried_tofu_skin_rolls", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> CREAMPOPCORN = ITEMS.register("creampopcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> CHINESEYAMANDSUGAR = ITEMS.register("chineseyamandsugar", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> CHOCOLATEPOPCORN = ITEMS.register("chocolatepopcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> CHOCOLATEBEAN = ITEMS.register("chocolatebean", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> NEWYEARCAKE = ITEMS.register("newyearcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> BONELESSLEMONCHICKENFEET = ITEMS.register("bonelesslemonchickenfeet", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> BOILEDCORN = ITEMS.register("boiledcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> CHICKENFEETWITHPEPPERS = ITEMS.register("chickenfeetwithpeppers", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> COLONELCHICKENNUGGETS = ITEMS.register("colonelchickennuggets", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRIEDGULTINOUSRICESTRIPS = ITEMS.register("friedgultinousricestrips", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRIEDPEANUTS = ITEMS.register("friedpeanuts", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRIEDMUSHROOM = ITEMS.register("friedmushroom", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> DRIEDRICECAKE = ITEMS.register("driedricecake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> FIREDSPRINGROLL = ITEMS.register("firedspringroll", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRIEDDOUGHSTICK = ITEMS.register("frieddoughstick", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> DRIEDMILK = ITEMS.register("driedmilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> DRIEDMEATBALL = ITEMS.register("driedmeatball", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> DRIEDDICEDAUBERGINE = ITEMS.register("drieddicedaubergine", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRIEDGIZZARD = ITEMS.register("friedgizzard", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRENCHFRIES = ITEMS.register("frenchfries", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> DRIEDLOTUSROOT = ITEMS.register("driedlotusroot", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRIEDDRIEDTOUFU = ITEMS.register("frieddriedtoufu", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRIEDTOUFU = ITEMS.register("friedtoufu", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> DRIEDBREAD = ITEMS.register("driedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> DRIEDDICEDBREAD = ITEMS.register("drieddicedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> DRIEDDICEDSTEAMEDBREAD = ITEMS.register("drieddicedsteamedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRIEDSANZI = ITEMS.register("friedsanzi", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRIEDHEMPBALL = ITEMS.register("friedhempball", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRIEDDOUGHTWIST = ITEMS.register("frieddoughtwist", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> CRISPYPISTOLLEG = ITEMS.register("crispypistolleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> SOMKEDCHICKENBREAST = ITEMS.register("somkedchickenbreast", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> ROASTEDPURPLEPOTATO = ITEMS.register("roastedpurplepotato", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> ROASTEDPEANUT = ITEMS.register("roastedpeanut", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> ORLEANWING = ITEMS.register("orleanwing", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));
    public static final DeferredItem<Item> ORLEANLEG = ITEMS.register("orleanleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));
    public static final DeferredItem<Item> ROASTEDMUSHROOM = ITEMS.register("roastedmushroom", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));
    public static final DeferredItem<Item> ROASTED_FLAMMULINAVELUTIPES = ITEMS.register("roasted_flammulinavelutipes", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));
    public static final DeferredItem<Item> ROASTEDCHINESECHIVES = ITEMS.register("roastedchinesechives", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));
    public static final DeferredItem<Item> ROASTEDDICEDFISH = ITEMS.register("roasteddicedfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).alwaysEdible().build())));
    public static final DeferredItem<Item> CARAMELPOPCORN = ITEMS.register("caramelpopcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> COOKEDCHINESEYAM = ITEMS.register("cookedchineseyam", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> COOKEDVERMICELLI_0 = ITEMS.register("cookedvermicelli_0", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> COOKEDHAWTHORN = ITEMS.register("cookedhawthorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> POPCORN = ITEMS.register("popcorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> SWEETTOUFUCURD = ITEMS.register("sweettoufucurd", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> JELLY = ITEMS.register("jelly", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> COOKEDPRESERVEDEGG = ITEMS.register("cookedpreservedegg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> COOKEDZONGZI = ITEMS.register("cookedzongzi", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> COOKEDGLUTINOUSRICE = ITEMS.register("cookedglutinousrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> COOKEDGLUTINOUSRICEPOUNDEDINTOPASTE = ITEMS.register("cookedglutinousricepoundedintopaste", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> AIKUI = ITEMS.register("aikui", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> TANG_ZI_XIAO_ZAO = ITEMS.register("tang_zi_xiao_zao", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> SUGARCOATEDWALNUT = ITEMS.register("sugarcoatedwalnut", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> DICEDPICKLEDVEGETABLE = ITEMS.register("dicedpickledvegetable", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> CUREDMEAT = ITEMS.register("curedmeat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> CUREDSAUSAGE = ITEMS.register("curedsausage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> SALTYDICEDCURUMBER = ITEMS.register("saltydicedcurumber", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> PICKLEDVEGETABLE = ITEMS.register("pickledvegetable", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> SLICEDSALTYCUCUMBER = ITEMS.register("slicedsaltycucumber", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEAMEDBLOOD = ITEMS.register("steamedblood", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> TIGERGREENPEPPER = ITEMS.register("tigergreenpepper", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> SOYAMILK = ITEMS.register("soyamilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> PICEDTOUFU = ITEMS.register("picedtoufu", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> DRIEDTOUFU = ITEMS.register("driedtoufu", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> TOFU_PUFFS = ITEMS.register("tofu_puffs", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> TOFUCURD = ITEMS.register("tofucurd", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> CHINESESPICYSNACKFOOD = ITEMS.register("chinesespicysnackfood", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> SPICYPEANUT = ITEMS.register("spicypeanut", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> HOTANDSOURRICENOODLES = ITEMS.register("hotandsourricenoodles", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> GOLDRICECAKE = ITEMS.register("goldricecake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> GOLDENGRAPE = ITEMS.register("goldengrape", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> DICEDBREAD = ITEMS.register("dicedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> DRYCOOKEDNOODLES = ITEMS.register("drycookednoodles", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> COOKEDVERMICELLI = ITEMS.register("cookedvermicelli", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> MOONCAKE = ITEMS.register("mooncake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> GRAINSPANCAKE = ITEMS.register("grainspancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> HAMBURGERBREAD = ITEMS.register("hamburgerbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> YIMENGPANCAKES = ITEMS.register("yimengpancakes", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> SACHIMA = ITEMS.register("sachima", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> SHUMAI = ITEMS.register("shumai", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> HOTDOG = ITEMS.register("hotdog", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEAMEDGLUTINOUSRICECAKE = ITEMS.register("steamedglutinousricecake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> CRISPYEGGCAKE = ITEMS.register("crispyeggcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEAMEDTWISTEDROLL = ITEMS.register("steamedtwistedroll", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> SCALLIONOILPANCAKE = ITEMS.register("scallionoilpancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> NOODLES = ITEMS.register("noodles", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> PIE = ITEMS.register("pie", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEAMEDBREAD = ITEMS.register("steamedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> DICEDSTEAMEDBREAD = ITEMS.register("dicedsteamedbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> EGGPUFFS = ITEMS.register("eggpuffs", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> EGGVERMICELLI = ITEMS.register("eggvermicelli", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> DRINKINGWATER = ITEMS.register("drinkingwater", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> CAPPUCCINO = ITEMS.register("cappuccino", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> CAFELATTE = ITEMS.register("cafelatte", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> MOCHACAFE = ITEMS.register("mochacafe", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> HOTWATER = ITEMS.register("hotwater", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> CAFE_AMERICANO = ITEMS.register("cafe_americano", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> LEMONREDTEA = ITEMS.register("lemonredtea", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> GRENTEAMILKWITHPEARL = ITEMS.register("grenteamilkwithpearl", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> GREEN_TEA_LATTES = ITEMS.register("green_tea_lattes", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> LEMONTEAWITHGLUTINOUSRICEFLAVOR = ITEMS.register("lemonteawithglutinousriceflavor", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> REDTEA = ITEMS.register("redtea", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> GREENTEA = ITEMS.register("greentea", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> BUCKWHEATTEA = ITEMS.register("buckwheattea", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> ASSAMMILK_TEA = ITEMS.register("assammilk_tea", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> GREENPLUMTEA = ITEMS.register("greenplumtea", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> REDTEAMILKWITHPEARL = ITEMS.register("redteamilkwithpearl", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> KAOLIANGWINE = ITEMS.register("kaoliangwine", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> KWEICHOW_MOUTAI = ITEMS.register("kweichow_moutai", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> TSINGTAO_BEER = ITEMS.register("tsingtao_beer", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> ICEDBLACKTEABLUE = ITEMS.register("icedblackteablue", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> COLA = ITEMS.register("cola", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> SUGARFREICEDTEA = ITEMS.register("sugarfreicedtea", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> SPRITE = ITEMS.register("sprite", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> COOKEDSAUSAGE = ITEMS.register("cookedsausage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> M_SECRETDELIGHT = ITEMS.register("m_secretdelight", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> FISHSASHIMI = ITEMS.register("fishsashimi", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.8f).alwaysEdible().build())));

    // ========== 蕉类果实 ==========
    public static final DeferredItem<Item> APIECEOFBANANA = ITEMS.register("apieceofbanana", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> BANANA = ITEMS.register("banana", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> PULLEDBANANA_2 = ITEMS.register("pulledbanana_2", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6f).alwaysEdible().build())));

    // ========== 柑橘类果实 ==========
    public static final DeferredItem<Item> ORANGE = ITEMS.register("orange", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> BLOODORANGE = ITEMS.register("bloodorange", () -> new RareFruitVariantItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> TANGERINE = ITEMS.register("tangerine", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> TANGERINE_1 = ITEMS.register("tangerine_1", () -> new RareFruitVariantItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> UGLYORANGE = ITEMS.register("uglyorange", () -> new RareFruitVariantItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> LEMON = ITEMS.register("lemon", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.2f).alwaysEdible().build())));

    // ========== 瓜类果实 ==========
    public static final DeferredItem<Item> HAMIMELON = ITEMS.register("hamimelon", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> SWEETMELON = ITEMS.register("sweetmelon", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> SWEETMELON_1 = ITEMS.register("sweetmelon_1", () -> new RareFruitVariantItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));

    // ========== 椰子类 ==========
    public static final DeferredItem<Item> COCONUT = ITEMS.register("coconut",
            () -> new ThrowableFruitItem(new Item.Properties(),
                    "flavor_immersed_daily:coconut_shell", 1, "flavor_immersed_daily:coconutmeat", 2, "", 0, 4.0f));
    public static final DeferredItem<Item> COCONUTMEAT = ITEMS.register("coconutmeat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> COCONUT_SHELL = ITEMS.register("coconut_shell", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build())));

    // ========== 榴莲类 ==========
    public static final DeferredItem<Item> DURIAN = ITEMS.register("durian",
            () -> new ThrowableFruitItem(new Item.Properties(),
                    "flavor_immersed_daily:durianmeat", 2, "flavor_immersed_daily:durianshellhat", 1, "", 0, 6.0f));
    public static final DeferredItem<Item> DURIANMEAT = ITEMS.register("durianmeat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> DURIANSHELLHAT = ITEMS.register("durianshellhat", () -> new Item(new Item.Properties()));

    // ========== 浆果类果实 ==========
    public static final DeferredItem<Item> BLUEBERRY = ITEMS.register("blueberry", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).alwaysEdible().build())));
    public static final DeferredItem<Item> GRAPE = ITEMS.register("grape",
            () -> new SeedableFruitItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).alwaysEdible().build()), "flavor_immersed_daily:agrape"));
    public static final DeferredItem<Item> GREENGRAPE = ITEMS.register("greengrape",
            () -> new SeedableFruitItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).alwaysEdible().build()), "flavor_immersed_daily:agreengrape"));
    public static final DeferredItem<Item> MULBERRY = ITEMS.register("mulberry", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).alwaysEdible().build())));
    public static final DeferredItem<Item> STRAWBERRY = ITEMS.register("strawberry", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).alwaysEdible().build())));

    // ========== 核果类果实 ==========
    public static final DeferredItem<Item> APRICOT = ITEMS.register("apricot", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> CHERRY = ITEMS.register("cherry", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> GREENPLUM = ITEMS.register("greenplum", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> HONEYPEACH = ITEMS.register("honeypeach", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> LIFEPEACH = ITEMS.register("lifepeach", () -> new RareFruitVariantItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> NECTARINE = ITEMS.register("nectarine", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> PLUM = ITEMS.register("plum", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> WINTERJUJUBE = ITEMS.register("winterjujube", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));

    // ========== 热带类果实 ==========
    public static final DeferredItem<Item> CARAMBOLA = ITEMS.register("carambola", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> DRAGONFRUIT = ITEMS.register("dragonfruit", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> KIWIFRUIT = ITEMS.register("kiwifruit", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> LYCHEE = ITEMS.register("lychee", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> MANGO = ITEMS.register("mango", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> MANGOSTEEN = ITEMS.register("mangosteen", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> PAWPAW = ITEMS.register("pawpaw", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> PINEAPPLE = ITEMS.register("pineapple", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> PISTACHIONUT = ITEMS.register("pistachionut", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> REDDATE = ITEMS.register("reddate", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> WALNUT = ITEMS.register("walnut", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> DRY_PISTACHIONUT = ITEMS.register("dry_pistachionut", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> PISTACHIONUT_0 = ITEMS.register("pistachionut_0", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> PISTACHIONUTSWITHOPENSHELLS = ITEMS.register("pistachionutswithopenshells", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> WALNUTKINNEL = ITEMS.register("walnutkinnel", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredBlock<TrellisBlock> TRELLIS = BLOCKS.register("trellis", () -> new TrellisBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().ignitedByLava()));
    public static final DeferredItem<BlockItem> TRELLIS_ITEM = ITEMS.register("trellis", () -> new BlockItem(TRELLIS.get(), new Item.Properties()));
    public static final DeferredBlock<GrapeBlock> GRAPEBLOCK = BLOCKS.register("grapeblock", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks()));
    public static final DeferredItem<Item> GRAPESEED = ITEMS.register("grapeseed", () -> new Item(new Item.Properties()));

    // ========== 爬架作物 ==========
    public static final DeferredBlock<GrapeBlock> CUCUMBERBLOCK = BLOCKS.register("cucumberseeds", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks()));
    public static final DeferredBlock<GrapeBlock> WAXGOURDBLOCK = BLOCKS.register("wax_gourd_seed_block", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks()));
    public static final DeferredBlock<GrapeBlock> KIDNEYBEANBLOCK = BLOCKS.register("kidneybeanblock", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks()));
    public static final DeferredBlock<GrapeBlock> AUBERGINEBLOCK = BLOCKS.register("aubergineseedblock", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks()));
    public static final DeferredBlock<GrapeBlock> TOMATOBLOCK = BLOCKS.register("tomatoblock", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks()));
    public static final DeferredBlock<GrapeBlock> COWPEABLOCK = BLOCKS.register("cowpeabeanseed", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks()));
    public static final DeferredBlock<GrapeBlock> GREENGRAEBLOCK = BLOCKS.register("greengrapeblock", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks()));
    public static final DeferredBlock<GrapeBlock> LOOFAHBLOCK = BLOCKS.register("loofahblock", () -> new GrapeBlock(Block.Properties.of().strength(0.8F, 3.0F).sound(SoundType.BAMBOO).noOcclusion().randomTicks()));

    // 爬架作物种子
    public static final DeferredItem<Item> CUCUMBERSEEDS = ITEMS.register("cucumberseeds", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WAX_GOURD_SEED_BLOCK = ITEMS.register("wax_gourd_seed_block", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> KIDNEYBEANSEED = ITEMS.register("kidneybeanseed", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> AUBERGINESEEDBLOCK = ITEMS.register("aubergineseedblock", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TOMATOSEED = ITEMS.register("tomatoseed", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COWPEABEANSEED = ITEMS.register("cowpeabeanseed", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GREENGRAESEED = ITEMS.register("greengrapeseed", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LOOFAHSEED = ITEMS.register("loofahseed", () -> new Item(new Item.Properties()));

    // 爬架作物产物
    public static final DeferredItem<Item> CUCUMBER = ITEMS.register("cucumber", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> WAXGOURD = ITEMS.register("waxgourd", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> KIDNEYBEAN = ITEMS.register("kidneybean", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> AUBERGINE = ITEMS.register("aubergine", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> TOMATO = ITEMS.register("tomato", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> COWPEA = ITEMS.register("cowpea", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> LOOFAH = ITEMS.register("loofah", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));

    // ========== 香蕉树 ==========
    public static final DeferredBlock<RotatedPillarBlock> BANANAWOOD = BLOCKS.register("bananawood",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .strength(2.0f)
                    .sound(SoundType.WOOD)));
    public static final DeferredItem<BlockItem> BANANAWOOD_ITEM = ITEMS.register("bananawood",
            () -> new BlockItem(BANANAWOOD.get(), new Item.Properties()));
    public static final DeferredBlock<RawBananaBlock> RAWBANANA = BLOCKS.register("rawbanana",
            () -> new RawBananaBlock(BlockBehaviour.Properties.of()
                    .noCollission().noOcclusion().randomTicks()
                    .sound(SoundType.GRASS).instabreak(),
                    BANANAWOOD, BANANA));
    public static final DeferredBlock<BananaSaplingBlock> BANANA_SAPLING = BLOCKS.register("bananasapling",
            () -> new BananaSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT).noCollission().randomTicks()
                    .instabreak().sound(SoundType.GRASS),
                    BANANAWOOD, RAWBANANA));
    public static final DeferredItem<BlockItem> BANANA_SAPLING_ITEM = ITEMS.register("bananasapling",
            () -> new TooltipBlockItem(BANANA_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));
    public static final DeferredItem<BlockItem> RAWBANANA_ITEM = ITEMS.register("rawbanana",
            () -> new BlockItem(RAWBANANA.get(), new Item.Properties()));

    // ========== 桂皮树 ==========
    public static final DeferredItem<Item> CINNAMON = ITEMS.register("cinnamon",
            () -> new Item(new Item.Properties()));
    public static final DeferredBlock<CinnamonWoodBlock> CINNAMONWOOD = BLOCKS.register("cinnamonwood",
            () -> new CinnamonWoodBlock(CINNAMON, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.0f)
                    .randomTicks()
                    .sound(SoundType.WOOD)
                    .noOcclusion()));
    public static final DeferredItem<BlockItem> CINNAMONWOOD_ITEM = ITEMS.register("cinnamonwood",
            () -> new BlockItem(CINNAMONWOOD.get(), new Item.Properties()));
    public static final DeferredBlock<CinnamonLeavesBlock> CINNAMONLEAVES = BLOCKS.register("cinnamonleaves",
            () -> new CinnamonLeavesBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .strength(0.2f)
                    .randomTicks()
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .isSuffocating((s, l, p) -> false)
                    .isViewBlocking((s, l, p) -> false)));
    public static final DeferredItem<BlockItem> CINNAMONLEAVES_ITEM = ITEMS.register("cinnamonleaves",
            () -> new BlockItem(CINNAMONLEAVES.get(), new Item.Properties()));
    public static final DeferredBlock<CinnamonSaplingBlock> CINNAMON_SAPLING = BLOCKS.register("cinnamon_sapling",
            () -> new CinnamonSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT).noCollission().randomTicks()
                    .instabreak().sound(SoundType.GRASS),
                    CINNAMONWOOD, CINNAMONLEAVES));
    public static final DeferredItem<BlockItem> CINNAMON_SAPLING_ITEM = ITEMS.register("cinnamon_sapling",
            () -> new TooltipBlockItem(CINNAMON_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    // ========== 其他果实 ==========
    public static final DeferredItem<Item> GREENAPPLE = ITEMS.register("greenapple", () -> new RareFruitVariantItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> HAWTHORN = ITEMS.register("hawthorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> LOQUAT = ITEMS.register("loquat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> PEAR = ITEMS.register("pear", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> POMEGRANATE = ITEMS.register("pomegranate", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    // ========== 野生采集物 ==========
    public static final DeferredItem<Item> TEMPERATEWILDFRUIT = ITEMS.register("temperatewildfruit", () -> new WildHarvestItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build()), Component.translatable("tooltip.flavor_immersed_daily.wild_fruit_harvest")));
    public static final DeferredItem<Item> TROPICALWILD_FRUIT = ITEMS.register("tropicalwild_fruit", () -> new WildHarvestItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build()), Component.translatable("tooltip.flavor_immersed_daily.wild_fruit_harvest")));
    public static final DeferredItem<Item> WILDFLOWERANDLEAF = ITEMS.register("wildflowerandleaf", () -> new WildHarvestItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build()), Component.translatable("tooltip.flavor_immersed_daily.wild_crop_harvest")));
    public static final DeferredItem<Item> WILDFRUITINCOLDZONE = ITEMS.register("wildfruitincoldzone", () -> new WildHarvestItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build()), Component.translatable("tooltip.flavor_immersed_daily.wild_fruit_harvest")));
    public static final DeferredItem<Item> WILDGRAINPLANT = ITEMS.register("wildgrainplant", () -> new WildHarvestItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build()), Component.translatable("tooltip.flavor_immersed_daily.wild_crop_harvest")));
    public static final DeferredItem<Item> WILDMUSHROOMPLANT = ITEMS.register("wildmushroomplant", () -> new WildHarvestItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build()), Component.translatable("tooltip.flavor_immersed_daily.wild_crop_harvest")));
    public static final DeferredItem<Item> WILDSEEDPLANT = ITEMS.register("wildseedplant", () -> new WildHarvestItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build()), Component.translatable("tooltip.flavor_immersed_daily.wild_crop_harvest")));
    public static final DeferredItem<Item> WILDTUBERPLANTS = ITEMS.register("wildtuberplants", () -> new WildHarvestItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build()), Component.translatable("tooltip.flavor_immersed_daily.wild_crop_harvest")));

    // ========== 肉类 - 生肉 (牛) ==========
    // rawcattleblood
    public static final DeferredItem<Item> RAWCATTLEBLOOD = ITEMS.register("rawcattleblood",
            () -> new Item(new Item.Properties()));
    // rawcattleface
    public static final DeferredItem<Item> RAWCATTLEFACE = ITEMS.register("rawcattleface",
            () -> new Item(new Item.Properties()));
    // rawcattlejoint
    public static final DeferredItem<Item> RAWCATTLEJOINT = ITEMS.register("rawcattlejoint",
            () -> new Item(new Item.Properties()));
    // rawcattlestomach
    public static final DeferredItem<Item> RAWCATTLESTOMACH = ITEMS.register("rawcattlestomach",
            () -> new Item(new Item.Properties()));
    // rawcattlelung
    public static final DeferredItem<Item> RAWCATTLELUNG = ITEMS.register("rawcattlelung",
            () -> new Item(new Item.Properties()));
    // rawcattleliver
    public static final DeferredItem<Item> RAWCATTLELIVER = ITEMS.register("rawcattleliver",
            () -> new Item(new Item.Properties()));
    // rawcattleintestine
    public static final DeferredItem<Item> RAWCATTLEINTESTINE = ITEMS.register("rawcattleintestine",
            () -> new Item(new Item.Properties()));
    // rawcattleheart
    public static final DeferredItem<Item> RAWCATTLEHEART = ITEMS.register("rawcattleheart",
            () -> new Item(new Item.Properties()));
    // rawcattlefat
    public static final DeferredItem<Item> RAWCATTLEFAT = ITEMS.register("rawcattlefat",
            () -> new Item(new Item.Properties()));
    // rawdicedcattlemeat
    public static final DeferredItem<Item> RAWDICEDCATTLEMEAT = ITEMS.register("rawdicedcattlemeat",
            () -> new Item(new Item.Properties()));
    // RAWSNOWFLAKEBEEF
    public static final DeferredItem<Item> RAWSNOWFLAKEBEEF = ITEMS.register("rawsnowflakebeef",
            () -> new Item(new Item.Properties()));
    // rawcattleleg
    public static final DeferredItem<Item> RAWCATTLELEG = ITEMS.register("rawcattleleg",
            () -> new Item(new Item.Properties()));
    // rawcattlefeet
    public static final DeferredItem<Item> RAWCATTLEFEET = ITEMS.register("rawcattlefeet",
            () -> new Item(new Item.Properties()));
    // rawcattletendon
    public static final DeferredItem<Item> RAWCATTLETENDON = ITEMS.register("rawcattletendon",
            () -> new Item(new Item.Properties()));
    // BULLHORN
    public static final DeferredItem<Item> BULLHORN = ITEMS.register("bullhorn",
            () -> new Item(new Item.Properties()));
    // BOVINEBONE
    public static final DeferredItem<Item> BOVINEBONE = ITEMS.register("bovinebone",
            () -> new Item(new Item.Properties()));
    // animalskull
    public static final DeferredItem<Item> ANIMALSKULL = ITEMS.register("animalskull",
            () -> new Item(new Item.Properties()));

    // ========== 肉类 - 生肉 (猪) ==========
    // rawpigblood
    public static final DeferredItem<Item> RAWPIGBLOOD = ITEMS.register("rawpigblood",
            () -> new Item(new Item.Properties()));
    // rawdicedpigmeat
    public static final DeferredItem<Item> RAWDICEDPIGMEAT = ITEMS.register("rawdicedpigmeat",
            () -> new Item(new Item.Properties()));
    // RAWDICEDPIGMEAT_2
    public static final DeferredItem<Item> RAWDICEDPIGMEAT_2 = ITEMS.register("rawdicedpigmeat_2",
            () -> new Item(new Item.Properties()));
    // rawpighead
    public static final DeferredItem<Item> RAWPIGHEAD = ITEMS.register("rawpighead",
            () -> new Item(new Item.Properties()));
    // rawpigear
    public static final DeferredItem<Item> RAWPIGEAR = ITEMS.register("rawpigear",
            () -> new Item(new Item.Properties()));
    // rawpignose
    public static final DeferredItem<Item> RAWPIGNOSE = ITEMS.register("rawpignose",
            () -> new Item(new Item.Properties()));
    // rawpigtail
    public static final DeferredItem<Item> RAWPIGTAIL = ITEMS.register("rawpigtail",
            () -> new Item(new Item.Properties()));
    // rawpigskin
    public static final DeferredItem<Item> RAWPIGSKIN = ITEMS.register("rawpigskin",
            () -> new Item(new Item.Properties()));
    // rawpigstreakypork
    public static final DeferredItem<Item> RAWPIGSTREAKYPORK = ITEMS.register("rawpigstreakypork",
            () -> new Item(new Item.Properties()));
    // rawpigtenderloin
    public static final DeferredItem<Item> RAWPIGTENDERLOIN = ITEMS.register("rawpigtenderloin",
            () -> new Item(new Item.Properties()));
    // rawpigsparerib
    public static final DeferredItem<Item> RAWPIGSPARERIB = ITEMS.register("rawpigsparerib",
            () -> new Item(new Item.Properties()));
    // rawpigleg
    public static final DeferredItem<Item> RAWPIGLEG = ITEMS.register("rawpigleg",
            () -> new Item(new Item.Properties()));
    // rawpigfeet
    public static final DeferredItem<Item> RAWPIGFEET = ITEMS.register("rawpigfeet",
            () -> new Item(new Item.Properties()));
    // rawpigfat
    public static final DeferredItem<Item> RAWPIGFAT = ITEMS.register("rawpigfat",
            () -> new Item(new Item.Properties()));
    // rawpigstomach
    public static final DeferredItem<Item> RAWPIGSTOMACH = ITEMS.register("rawpigstomach",
            () -> new Item(new Item.Properties()));
    // rawpiglung
    public static final DeferredItem<Item> RAWPIGLUNG = ITEMS.register("rawpiglung",
            () -> new Item(new Item.Properties()));
    // rawpigliver
    public static final DeferredItem<Item> RAWPIGLIVER = ITEMS.register("rawpigliver",
            () -> new Item(new Item.Properties()));
    // rawpigkidney
    public static final DeferredItem<Item> RAWPIGKIDNEY = ITEMS.register("rawpigkidney",
            () -> new Item(new Item.Properties()));
    // rawpigheart
    public static final DeferredItem<Item> RAWPIGHEART = ITEMS.register("rawpigheart",
            () -> new Item(new Item.Properties()));
    // rawpigintestine
    public static final DeferredItem<Item> RAWPIGINTESTINE = ITEMS.register("rawpigintestine",
            () -> new Item(new Item.Properties()));
    // rawpigcerebrum
    public static final DeferredItem<Item> RAWPIGCEREBRUM = ITEMS.register("rawpigcerebrum",
            () -> new Item(new Item.Properties()));

    // ========== 肉类 - 生肉 (羊) ==========
    // rawsheepblood
    public static final DeferredItem<Item> RAWSHEEPBLOOD = ITEMS.register("rawsheepblood",
            () -> new Item(new Item.Properties()));
    // rawdicedsheepmeat
    public static final DeferredItem<Item> RAWDICEDSHEEPMEAT = ITEMS.register("rawdicedsheepmeat",
            () -> new Item(new Item.Properties()));
    // rawsheepface
    public static final DeferredItem<Item> RAWSHEEPFACE = ITEMS.register("rawsheepface",
            () -> new Item(new Item.Properties()));
    // rawsheeptailfat
    public static final DeferredItem<Item> RAWSHEEPTAILFAT = ITEMS.register("rawsheeptailfat",
            () -> new Item(new Item.Properties()));
    // rawsheepfat
    public static final DeferredItem<Item> RAWSHEEPFAT = ITEMS.register("rawsheepfat",
            () -> new Item(new Item.Properties()));
    // rawsheepeye
    public static final DeferredItem<Item> RAWSHEEPEYE = ITEMS.register("rawsheepeye",
            () -> new Item(new Item.Properties()));
    // rawsheepstomach
    public static final DeferredItem<Item> RAWSHEEPSTOMACH = ITEMS.register("rawsheepstomach",
            () -> new Item(new Item.Properties()));
    // rawsheepliver
    public static final DeferredItem<Item> RAWSHEEPLIVER = ITEMS.register("rawsheepliver",
            () -> new Item(new Item.Properties()));
    // rawsheepintestine
    public static final DeferredItem<Item> RAWSHEEPINTESTINE = ITEMS.register("rawsheepintestine",
            () -> new Item(new Item.Properties()));
    // rawsheepkidney
    public static final DeferredItem<Item> RAWSHEEPKIDNEY = ITEMS.register("rawsheepkidney",
            () -> new Item(new Item.Properties()));
    // rawsheepheart
    public static final DeferredItem<Item> RAWSHEEPHEART = ITEMS.register("rawsheepheart",
            () -> new Item(new Item.Properties()));
    // RAWSHEEPSPARERIB
    public static final DeferredItem<Item> RAWSHEEPSPARERIB = ITEMS.register("rawsheepsparerib",
            () -> new Item(new Item.Properties()));
    // rawsheepspine
    public static final DeferredItem<Item> RAWSHEEPSPINE = ITEMS.register("rawsheepspine",
            () -> new Item(new Item.Properties()));
    // rawsheepfeet
    public static final DeferredItem<Item> RAWSHEEPFEET = ITEMS.register("rawsheepfeet",
            () -> new Item(new Item.Properties()));
    // rawsheepleg
    public static final DeferredItem<Item> RAWSHEEPLEG = ITEMS.register("rawsheepleg",
            () -> new Item(new Item.Properties()));
    // SHEEPBREAD
    public static final DeferredItem<Item> SHEEPBREAD = ITEMS.register("sheepbread",
            () -> new Item(new Item.Properties()));

    // ========== 肉类 - 生肉 (鸡) ==========
    // rawchickenblood
    public static final DeferredItem<Item> RAWCHICKENBLOOD = ITEMS.register("rawchickenblood",
            () -> new Item(new Item.Properties()));
    // rawchickenhead
    public static final DeferredItem<Item> RAWCHICKENHEAD = ITEMS.register("rawchickenhead",
            () -> new Item(new Item.Properties()));
    // rawchickenneck
    public static final DeferredItem<Item> RAWCHICKENNECK = ITEMS.register("rawchickenneck",
            () -> new Item(new Item.Properties()));
    // RAWCHICKENPIECEPIECE
    public static final DeferredItem<Item> RAWCHICKENPIECEPIECE = ITEMS.register("rawchickenpiecepiece",
            () -> new Item(new Item.Properties()));
    // rawchickenbreast
    public static final DeferredItem<Item> RAWCHICKENBREAST = ITEMS.register("rawchickenbreast",
            () -> new Item(new Item.Properties()));
    // RAWCHICKENPIECE
    public static final DeferredItem<Item> RAWCHICKENPIECE = ITEMS.register("rawchickenpiece",
            () -> new Item(new Item.Properties()));
    // rawchickenwing
    public static final DeferredItem<Item> RAWCHICKENWING = ITEMS.register("rawchickenwing",
            () -> new Item(new Item.Properties()));
    // rawchickenwingtip
    public static final DeferredItem<Item> RAWCHICKENWINGTIP = ITEMS.register("rawchickenwingtip",
            () -> new Item(new Item.Properties()));
    // rawchickenfeet
    public static final DeferredItem<Item> RAWCHICKENFEET = ITEMS.register("rawchickenfeet",
            () -> new Item(new Item.Properties()));
    // rawchickenlegwithleg
    public static final DeferredItem<Item> RAWCHICKENLEGWITHLEG = ITEMS.register("rawchickenlegwithleg",
            () -> new Item(new Item.Properties()));
    // rawchickenleg
    public static final DeferredItem<Item> RAWCHICKENLEG = ITEMS.register("rawchickenleg",
            () -> new Item(new Item.Properties()));
    // rawchickenlean
    public static final DeferredItem<Item> RAWCHICKENLEAN = ITEMS.register("rawchickenlean",
            () -> new Item(new Item.Properties()));
    // rawchickenfork
    public static final DeferredItem<Item> RAWCHICKENFORK = ITEMS.register("rawchickenfork",
            () -> new Item(new Item.Properties()));
    // rawchickenass
    public static final DeferredItem<Item> RAWCHICKENASS = ITEMS.register("rawchickenass",
            () -> new Item(new Item.Properties()));
    // rawchickenheart
    public static final DeferredItem<Item> RAWCHICKENHEART = ITEMS.register("rawchickenheart",
            () -> new Item(new Item.Properties()));
    // rawchickenliver
    public static final DeferredItem<Item> RAWCHICKENLIVER = ITEMS.register("rawchickenliver",
            () -> new Item(new Item.Properties()));
    // rawchickengizzard
    public static final DeferredItem<Item> RAWCHICKENGIZZARD = ITEMS.register("rawchickengizzard",
            () -> new Item(new Item.Properties()));

    // ========== 肉类 - 熟肉 (牛) ==========
    // cookedcattleheart
    public static final DeferredItem<Item> COOKEDCATTLEHEART = ITEMS.register("cookedcattleheart",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedcattlejoint
    public static final DeferredItem<Item> COOKEDCATTLEJOINT = ITEMS.register("cookedcattlejoint",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedcattlestomach
    public static final DeferredItem<Item> COOKEDCATTLESTOMACH = ITEMS.register("cookedcattlestomach",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedcattlelung
    public static final DeferredItem<Item> COOKEDCATTLELUNG = ITEMS.register("cookedcattlelung",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedcattleliver
    public static final DeferredItem<Item> COOKEDCATTLELIVER = ITEMS.register("cookedcattleliver",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedcattleintestine
    public static final DeferredItem<Item> COOKEDCATTLEINTESTINE = ITEMS.register("cookedcattleintestine",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookeddicedcattle
    public static final DeferredItem<Item> COOKEDDICEDCATTLE = ITEMS.register("cookeddicedcattle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedcattleface
    public static final DeferredItem<Item> COOKEDCATTLEFACE = ITEMS.register("cookedcattleface",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedcattlemarbledbeef
    public static final DeferredItem<Item> COOKEDCATTLEMARBLEDBEEF = ITEMS.register("cookedcattlemarbledbeef",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedcattleleg
    public static final DeferredItem<Item> COOKEDCATTLELEG = ITEMS.register("cookedcattleleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedcattlefeet
    public static final DeferredItem<Item> COOKEDCATTLEFEET = ITEMS.register("cookedcattlefeet",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedcattletendon
    public static final DeferredItem<Item> COOKEDCATTLETENDON = ITEMS.register("cookedcattletendon",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    // ========== 肉类 - 熟肉 (猪) ==========
    // cookeddicedpigmeat
    public static final DeferredItem<Item> COOKEDDICEDPIGMEAT = ITEMS.register("cookeddicedpigmeat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpigshreds
    public static final DeferredItem<Item> COOKEDPIGSHREDS = ITEMS.register("cookedpigshreds",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpigear
    public static final DeferredItem<Item> COOKEDPIGEAR = ITEMS.register("cookedpigear",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpignose
    public static final DeferredItem<Item> COOKEDPIGNOSE = ITEMS.register("cookedpignose",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpigtail
    public static final DeferredItem<Item> COOKEDPIGTAIL = ITEMS.register("cookedpigtail",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpigskin
    public static final DeferredItem<Item> COOKEDPIGSKIN = ITEMS.register("cookedpigskin",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpigstreakypork
    public static final DeferredItem<Item> COOKEDPIGSTREAKYPORK = ITEMS.register("cookedpigstreakypork",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpigtenderloin
    public static final DeferredItem<Item> COOKEDPIGTENDERLOIN = ITEMS.register("cookedpigtenderloin",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpigsparerib
    public static final DeferredItem<Item> COOKEDPIGSPARERIB = ITEMS.register("cookedpigsparerib",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpigleg
    public static final DeferredItem<Item> COOKEDPIGLEG = ITEMS.register("cookedpigleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpigfeet
    public static final DeferredItem<Item> COOKEDPIGFEET = ITEMS.register("cookedpigfeet",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpigfat
    public static final DeferredItem<Item> COOKEDPIGFAT = ITEMS.register("cookedpigfat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpigstomach
    public static final DeferredItem<Item> COOKEDPIGSTOMACH = ITEMS.register("cookedpigstomach",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpiglung
    public static final DeferredItem<Item> COOKEDPIGLUNG = ITEMS.register("cookedpiglung",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpigliver
    public static final DeferredItem<Item> COOKEDPIGLIVER = ITEMS.register("cookedpigliver",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpigkidney
    public static final DeferredItem<Item> COOKEDPIGKIDNEY = ITEMS.register("cookedpigkidney",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpigheart
    public static final DeferredItem<Item> COOKEDPIGHEART = ITEMS.register("cookedpigheart",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpigcerebrum
    public static final DeferredItem<Item> COOKEDPIGCEREBRUM = ITEMS.register("cookedpigcerebrum",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpigintestine
    public static final DeferredItem<Item> COOKEDPIGINTESTINE = ITEMS.register("cookedpigintestine",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedpigface
    public static final DeferredItem<Item> COOKEDPIGFACE = ITEMS.register("cookedpigface",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    // ========== 肉类 - 熟肉 (羊) ==========
    // cookeddicedsheepmeat
    public static final DeferredItem<Item> COOKEDDICEDSHEEPMEAT = ITEMS.register("cookeddicedsheepmeat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedsheepface
    public static final DeferredItem<Item> COOKEDSHEEPFACE = ITEMS.register("cookedsheepface",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedsheepeye
    public static final DeferredItem<Item> COOKEDSHEEPEYE = ITEMS.register("cookedsheepeye",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedsheeptail
    public static final DeferredItem<Item> COOKEDSHEEPTAIL = ITEMS.register("cookedsheeptail",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedsheepstomach
    public static final DeferredItem<Item> COOKEDSHEEPSTOMACH = ITEMS.register("cookedsheepstomach",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedsheepliver
    public static final DeferredItem<Item> COOKEDSHEEPLIVER = ITEMS.register("cookedsheepliver",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedsheepintestine
    public static final DeferredItem<Item> COOKEDSHEEPINTESTINE = ITEMS.register("cookedsheepintestine",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedsheepkidney
    public static final DeferredItem<Item> COOKEDSHEEPKIDNEY = ITEMS.register("cookedsheepkidney",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedsheepheart
    public static final DeferredItem<Item> COOKEDSHEEPHEART = ITEMS.register("cookedsheepheart",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedsheepsparerlip
    public static final DeferredItem<Item> COOKEDSHEEPSPARERLIP = ITEMS.register("cookedsheepsparerlip",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedsheepspine
    public static final DeferredItem<Item> COOKEDSHEEPSPINE = ITEMS.register("cookedsheepspine",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedsheepfeet
    public static final DeferredItem<Item> COOKEDSHEEPFEET = ITEMS.register("cookedsheepfeet",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedsheepleg
    public static final DeferredItem<Item> COOKEDSHEEPLEG = ITEMS.register("cookedsheepleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedsheepfat
    public static final DeferredItem<Item> COOKEDSHEEPFAT = ITEMS.register("cookedsheepfat",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    // ========== 肉类 - 熟肉 (鸡) ==========
    // cookedchickendiced
    public static final DeferredItem<Item> COOKEDCHICKENDICED = ITEMS.register("cookedchickendiced",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedchickenpiecepiece
    public static final DeferredItem<Item> COOKEDCHICKENPIECEPIECE = ITEMS.register("cookedchickenpiecepiece",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedchickenhead
    public static final DeferredItem<Item> COOKEDCHICKENHEAD = ITEMS.register("cookedchickenhead",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedchickenneck
    public static final DeferredItem<Item> COOKEDCHICKENNECK = ITEMS.register("cookedchickenneck",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedchickenwing
    public static final DeferredItem<Item> COOKEDCHICKENWING = ITEMS.register("cookedchickenwing",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedchickenwingtip
    public static final DeferredItem<Item> COOKEDCHICKENWINGTIP = ITEMS.register("cookedchickenwingtip",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedchickenfeet
    public static final DeferredItem<Item> COOKEDCHICKENFEET = ITEMS.register("cookedchickenfeet",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedchickenlegwithleg
    public static final DeferredItem<Item> COOKEDCHICKENLEGWITHLEG = ITEMS.register("cookedchickenlegwithleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedchickenleg
    public static final DeferredItem<Item> COOKEDCHICKENLEG = ITEMS.register("cookedchickenleg",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedchickenpiece
    public static final DeferredItem<Item> COOKEDCHICKENPIECE = ITEMS.register("cookedchickenpiece",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedchickensteak
    public static final DeferredItem<Item> COOKEDCHICKENSTEAK = ITEMS.register("cookedchickensteak",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedchickenfork
    public static final DeferredItem<Item> COOKEDCHICKENFORK = ITEMS.register("cookedchickenfork",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedchickenass
    public static final DeferredItem<Item> COOKEDCHICKENASS = ITEMS.register("cookedchickenass",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedchickenheart
    public static final DeferredItem<Item> COOKEDCHICKENHEART = ITEMS.register("cookedchickenheart",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedchickenliver
    public static final DeferredItem<Item> COOKEDCHICKENLIVER = ITEMS.register("cookedchickenliver",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));
    // cookedchickengizzard
    public static final DeferredItem<Item> COOKEDCHICKENGIZZARD = ITEMS.register("cookedchickengizzard",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.8f)
                            .alwaysEdible()
                            .build())));

    // ========== 果汁物品 ==========
    public static final DeferredItem<JuiceBlockItem> HAMIMELONJUICE = ITEMS.register("hamimelonjuice",
            () -> new JuiceBlockItem(HAMIMELONJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> HAWTHORNJUIVE = ITEMS.register("hawthornjuive",
            () -> new JuiceBlockItem(HAWTHORNJUIVE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> DRAGONFRUIEJUICE = ITEMS.register("dragonfruiejuice",
            () -> new JuiceBlockItem(DRAGONFRUIEJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> CARAMBOLAJUICE = ITEMS.register("carambolajuice",
            () -> new JuiceBlockItem(CARAMBOLAJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> WINTERJUJUBEJUICE = ITEMS.register("winterjujubejuice",
            () -> new JuiceBlockItem(WINTERJUJUBEJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> POMEGRANATEJUICE = ITEMS.register("pomegranatejuice",
            () -> new JuiceBlockItem(POMEGRANATEJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> SWEETBERRYJUICE = ITEMS.register("sweetberryjuice",
            () -> new JuiceBlockItem(SWEETBERRYJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> DURIANJUICE = ITEMS.register("durianjuice",
            () -> new JuiceBlockItem(DURIANJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> LEMONJUICE = ITEMS.register("lemonjuice",
            () -> new JuiceBlockItem(LEMONJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> BLUEBERRYJUICE = ITEMS.register("blueberryjuice",
            () -> new JuiceBlockItem(BLUEBERRYJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> SWEETMELONJUICE = ITEMS.register("sweetmelonjuice",
            () -> new JuiceBlockItem(SWEETMELONJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> GREENGRAPEJUICE = ITEMS.register("greengrapejuice",
            () -> new JuiceBlockItem(GREENGRAPEJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> TANGERINEJUICE = ITEMS.register("tangerinejuice",
            () -> new JuiceBlockItem(TANGERINEJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> MANGOSTEENJUICE = ITEMS.register("mangosteenjuice",
            () -> new JuiceBlockItem(MANGOSTEENJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> CHERRYJUICE = ITEMS.register("cherryjuice",
            () -> new JuiceBlockItem(CHERRYJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> STRAWBERRYJUICE = ITEMS.register("strawberryjuice",
            () -> new JuiceBlockItem(STRAWBERRYJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> ORANGEJUICE = ITEMS.register("orangejuice",
            () -> new JuiceBlockItem(ORANGEJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> GREENPLUMJUICE = ITEMS.register("greenplumjuice",
            () -> new JuiceBlockItem(GREENPLUMJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> APPLEJUICE = ITEMS.register("applejuice",
            () -> new JuiceBlockItem(APPLEJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> HONEYPEACHJUIVE = ITEMS.register("honeypeachjuive",
            () -> new JuiceBlockItem(HONEYPEACHJUIVE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> BANANAJUICE = ITEMS.register("bananajuice",
            () -> new JuiceBlockItem(BANANAJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> LYCHEEJUICE = ITEMS.register("lycheejuice",
            () -> new JuiceBlockItem(LYCHEEJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> APRICOTJUICE = ITEMS.register("apricotjuice",
            () -> new JuiceBlockItem(APRICOTJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> GRAPEJUICE = ITEMS.register("grapejuice",
            () -> new JuiceBlockItem(GRAPEJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> MULBERRYJUIVE = ITEMS.register("mulberryjuive",
            () -> new JuiceBlockItem(MULBERRYJUIVE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> PKUMJUICE = ITEMS.register("pkumjuice",
            () -> new JuiceBlockItem(PKUMJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> MANGOJUICE = ITEMS.register("mangojuice",
            () -> new JuiceBlockItem(MANGOJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> WATERMELONJUICE = ITEMS.register("watermelonjuice",
            () -> new JuiceBlockItem(WATERMELONJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> NECTARINEJUICE = ITEMS.register("nectarinejuice",
            () -> new JuiceBlockItem(NECTARINEJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> PINEAPPLEJUICE = ITEMS.register("pineapplejuice",
            () -> new JuiceBlockItem(PINEAPPLEJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> LOQUATJUICE = ITEMS.register("loquatjuice",
            () -> new JuiceBlockItem(LOQUATJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> KIWIFRUITJUICE = ITEMS.register("kiwifruitjuice",
            () -> new JuiceBlockItem(KIWIFRUITJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> PEARJUICE = ITEMS.register("pearjuice",
            () -> new JuiceBlockItem(PEARJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> PAWPAWJUICE = ITEMS.register("pawpawjuice",
            () -> new JuiceBlockItem(PAWPAWJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));
    public static final DeferredItem<JuiceBlockItem> COCONUTJUICE = ITEMS.register("coconutjuice",
            () -> new JuiceBlockItem(COCONUTJUICE_BLOCK.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).alwaysEdible().build())
                    .stacksTo(16)));

    // ========== 果酱 ==========
    public static final DeferredItem<Item> PINEAPPLEJAM = ITEMS.register("pineapplejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> STRAWBERRYJAM = ITEMS.register("strawberryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> ORANGEJAM = ITEMS.register("orangejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> WINTERJUJUBEJAM = ITEMS.register("winterjujubejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> HAMIMELONJAM = ITEMS.register("hamimelonjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> DRAGONFRUITJAM = ITEMS.register("dragonfruitjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> TANGERINEJAM = ITEMS.register("tangerinejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> BLUEBERRYJAM = ITEMS.register("blueberryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> PEARJAM = ITEMS.register("pearjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> LYCHEEJAM = ITEMS.register("lycheejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> PLUMJAM = ITEMS.register("plumjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> DURIANJAM = ITEMS.register("durianjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> MANGOJAM = ITEMS.register("mangojam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> KIWIFRUITJAM = ITEMS.register("kiwifruitjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> PAWPAWJAM = ITEMS.register("pawpawjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> LEMONJAM = ITEMS.register("lemonjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> LOQUATJAM = ITEMS.register("loquatjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> APPLEJAM = ITEMS.register("applejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> GRAPEJAM = ITEMS.register("grapejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> GREEMPLUMJAM = ITEMS.register("greemplumjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> GREENGRAPEJAM = ITEMS.register("greengrapejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> MULBERRYJAM = ITEMS.register("mulberryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> HAWTHORNJAM = ITEMS.register("hawthornjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> MANGOSTEENJAM = ITEMS.register("mangosteenjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> POMEGRANATEJAM = ITEMS.register("pomegranatejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> HONEYPEACHJAM = ITEMS.register("honeypeachjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> SWEETMELONJAM = ITEMS.register("sweetmelonjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> SWEETBERRYJAM = ITEMS.register("sweetberryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> WATERMELONJAM = ITEMS.register("watermelonjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> BANANAJAM = ITEMS.register("bananajam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> APRICOTJAM = ITEMS.register("apricotjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> CARAMBOLAJAM = ITEMS.register("carambolajam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> COCONUTJAM = ITEMS.register("coconutjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> CHERRYJAM = ITEMS.register("cherryjam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> NECTARINEJAM = ITEMS.register("nectarinejam",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).alwaysEdible().build())));

    // ========== 清韵木树 ==========
    public static final ResourceKey<ConfiguredFeature<?, ?>> PLUM_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(MODID, "plum_tree"));

    public static final DeferredBlock<RotatedPillarBlock> VERDANT_GRACE_LOG = BLOCKS.register("verdant_grace_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(2.0f)
                    .sound(SoundType.WOOD)));
    public static final DeferredItem<BlockItem> VERDANT_GRACE_LOG_ITEM = ITEMS.register("verdant_grace_log",
            () -> new BlockItem(VERDANT_GRACE_LOG.get(), new Item.Properties()));

    public static final DeferredBlock<LeavesBlock> VERDANT_GRACE_LEAVES = BLOCKS.register("verdant_grace_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .strength(0.2f)
                    .randomTicks()
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .isValidSpawn((state, level, pos, type) -> false)
                    .isSuffocating((state, level, pos) -> false)
                    .isViewBlocking((state, level, pos) -> false)));
    public static final DeferredItem<BlockItem> VERDANT_GRACE_LEAVES_ITEM = ITEMS.register("verdant_grace_leaves",
            () -> new BlockItem(VERDANT_GRACE_LEAVES.get(), new Item.Properties()));

    public static final DeferredBlock<FallingFruitBlock> RAWPLUM = BLOCKS.register("rawplum",
            () -> new FallingFruitBlock(PLUM, BlockBehaviour.Properties.of()
                    .strength(0.2f)
                    .noCollission()
                    .noOcclusion()
                    .randomTicks()
                    .sound(SoundType.GRASS)
                    .instabreak()));

    public static final DeferredBlock<FruitingLeavesBlock> VERDANT_GRACE_FRUITING_LEAVES = BLOCKS.register("verdant_grace_fruiting_leaves",
            () -> new FruitingLeavesBlock(PLUM, RAWPLUM, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PINK)
                    .strength(0.2f)
                    .randomTicks()
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .isValidSpawn((state, level, pos, type) -> false)
                    .isSuffocating((state, level, pos) -> false)
                    .isViewBlocking((state, level, pos) -> false)));
    public static final DeferredItem<BlockItem> VERDANT_GRACE_FRUITING_LEAVES_ITEM = ITEMS.register("verdant_grace_fruiting_leaves",
            () -> new BlockItem(VERDANT_GRACE_FRUITING_LEAVES.get(), new Item.Properties()));

    public static final DeferredBlock<SaplingBlock> PLUM_SAPLING = BLOCKS.register("plumsapling",
            () -> new SaplingBlock(new TreeGrower(
                    "plum_tree",
                    0.0f,
                    Optional.empty(),
                    Optional.empty(),
                    Optional.of(PLUM_TREE),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty()
            ), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.GRASS)));
    public static final DeferredItem<BlockItem> PLUM_SAPLING_ITEM = ITEMS.register("plumsapling",
            () -> new TooltipBlockItem(PLUM_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    // ========== 灿阳木树 ==========
    public static final ResourceKey<ConfiguredFeature<?, ?>> SOLARWOOD_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(MODID, "solarwood_tree"));

    public static final DeferredBlock<RotatedPillarBlock> SOLARWOOD_LOG = BLOCKS.register("solarwood_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(2.0f)
                    .sound(SoundType.WOOD)));

    public static final DeferredItem<BlockItem> SOLARWOOD_LOG_ITEM = ITEMS.register("solarwood_log",
            () -> new BlockItem(SOLARWOOD_LOG.get(), new Item.Properties()));

    public static final DeferredBlock<LeavesBlock> SOLARWOOD_LEAVES = BLOCKS.register("solarwood_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .strength(0.2f)
                    .randomTicks()
                    .sound(SoundType.AZALEA_LEAVES)
                    .noOcclusion()
                    .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false)
                    .isSuffocating((blockState, blockGetter, blockPos) -> false)
                    .isViewBlocking((blockState, blockGetter, blockPos) -> false)));

    public static final DeferredItem<BlockItem> SOLARWOOD_LEAVES_ITEM = ITEMS.register("solarwood_leaves",
            () -> new BlockItem(SOLARWOOD_LEAVES.get(), new Item.Properties()));

    // ========== OrangeLeave ==========
    public static final DeferredItem<Item> ORANGELEAVE = ITEMS.register("orangeleave", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).alwaysEdible().build())));

    public static final DeferredBlock<FallingFruitBlock> RAWORANGELEAVE = BLOCKS.register("raworangeleave",
            () -> new FallingFruitBlock(ORANGE, BlockBehaviour.Properties.of()
                    .strength(0.2f)
                    .noCollission()
                    .noOcclusion()
                    .randomTicks()
                    .sound(SoundType.GRASS)
                    .instabreak()));

    public static final DeferredBlock<FruitingLeavesBlock> ORANGELEAVE_FRUITING_LEAVES = BLOCKS.register("orangeleave_fruiting_leaves",
            () -> new FruitingLeavesBlock(ORANGE, RAWORANGELEAVE, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.2f)
                    .randomTicks()
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .isValidSpawn((state, level, pos, type) -> false)
                    .isSuffocating((state, level, pos) -> false)
                    .isViewBlocking((state, level, pos) -> false)));

    public static final DeferredItem<BlockItem> ORANGELEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("orangeleave_fruiting_leaves",
            () -> new BlockItem(ORANGELEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredBlock<SaplingBlock> ORANGE_SAPLING = BLOCKS.register("orangesapling",
            () -> new SaplingBlock(new TreeGrower("orangesapling", 0.0f,
                    Optional.empty(), Optional.empty(),
                    Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(MODID, "orange_tree"))),
                    Optional.empty(), Optional.empty(), Optional.empty()),
                    BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final DeferredItem<BlockItem> ORANGE_SAPLING_ITEM = ITEMS.register("orangesapling",
            () -> new TooltipBlockItem(ORANGE_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    // ========== 蜜缘木树 ==========
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORCHARD_HEARTWOOD_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(MODID, "orchard_heartwood_tree"));

    public static final DeferredBlock<RotatedPillarBlock> ORCHARD_HEARTWOOD_LOG = BLOCKS.register("orchard_heartwood_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(2.0f)
                    .sound(SoundType.WOOD)));
    public static final DeferredItem<BlockItem> ORCHARD_HEARTWOOD_LOG_ITEM = ITEMS.register("orchard_heartwood_log",
            () -> new BlockItem(ORCHARD_HEARTWOOD_LOG.get(), new Item.Properties()));

    public static final DeferredBlock<LeavesBlock> ORCHARD_HEARTWOOD_LEAVES = BLOCKS.register("orchard_heartwood_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .strength(0.2f).randomTicks().sound(SoundType.AZALEA_LEAVES).noOcclusion()
                    .isValidSpawn((s, g, p, t) -> false).isSuffocating((s, g, p) -> false).isViewBlocking((s, g, p) -> false)));
    public static final DeferredItem<BlockItem> ORCHARD_HEARTWOOD_LEAVES_ITEM = ITEMS.register("orchard_heartwood_leaves",
            () -> new BlockItem(ORCHARD_HEARTWOOD_LEAVES.get(), new Item.Properties()));

    // ========== 核毅木树 ==========
    public static final ResourceKey<ConfiguredFeature<?, ?>> STONEBARK_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(MODID, "stonebark_tree"));

    public static final DeferredBlock<RotatedPillarBlock> STONEBARK_LOG = BLOCKS.register("stonebark_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_GRAY)
                    .strength(2.0f)
                    .sound(SoundType.WOOD)));
    public static final DeferredItem<BlockItem> STONEBARK_LOG_ITEM = ITEMS.register("stonebark_log",
            () -> new BlockItem(STONEBARK_LOG.get(), new Item.Properties()));

    public static final DeferredBlock<LeavesBlock> STONEBARK_LEAVES = BLOCKS.register("stonebark_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion()
                    .isValidSpawn((s, g, p, t) -> false).isSuffocating((s, g, p) -> false).isViewBlocking((s, g, p) -> false)));
    public static final DeferredItem<BlockItem> STONEBARK_LEAVES_ITEM = ITEMS.register("stonebark_leaves",
            () -> new BlockItem(STONEBARK_LEAVES.get(), new Item.Properties()));

    // ========== 虬藤木树 ==========
    public static final ResourceKey<ConfiguredFeature<?, ?>> VINEHEART_TIMBER_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(MODID, "vineheart_timber_tree"));

    public static final DeferredBlock<RotatedPillarBlock> VINEHEART_TIMBER_LOG = BLOCKS.register("vineheart_timber_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(2.0f)
                    .sound(SoundType.WOOD)));
    public static final DeferredItem<BlockItem> VINEHEART_TIMBER_LOG_ITEM = ITEMS.register("vineheart_timber_log",
            () -> new BlockItem(VINEHEART_TIMBER_LOG.get(), new Item.Properties()));

    public static final DeferredBlock<LeavesBlock> VINEHEART_TIMBER_LEAVES = BLOCKS.register("vineheart_timber_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion()
                    .isValidSpawn((s, g, p, t) -> false).isSuffocating((s, g, p) -> false).isViewBlocking((s, g, p) -> false)));
    public static final DeferredItem<BlockItem> VINEHEART_TIMBER_LEAVES_ITEM = ITEMS.register("vineheart_timber_leaves",
            () -> new BlockItem(VINEHEART_TIMBER_LEAVES.get(), new Item.Properties()));

    // ================================================================
    // 所有木质产品注册（5种树: solarwood, orchard_heartwood, stonebark, vineheart_timber, verdant_grace）
    // 每种: planks, stairs, slab, fence, fence_gate, button, pressure_plate
    // ================================================================
    private static BlockBehaviour.Properties woodProps() {
        return BlockBehaviour.Properties.of().strength(2.0f, 3.0f).sound(SoundType.WOOD);
    }

    // ===== 灿阳木木制品 =====
    public static final DeferredBlock<Block> SOLARWOOD_PLANKS = BLOCKS.register("solarwood_planks",
            () -> new Block(woodProps().mapColor(MapColor.TERRACOTTA_YELLOW)));
    public static final DeferredItem<BlockItem> SOLARWOOD_PLANKS_ITEM = ITEMS.register("solarwood_planks",
            () -> new BlockItem(SOLARWOOD_PLANKS.get(), new Item.Properties()));
    public static final DeferredBlock<StairBlock> SOLARWOOD_STAIRS = BLOCKS.register("solarwood_stairs",
            () -> new StairBlock(SOLARWOOD_PLANKS.get().defaultBlockState(), woodProps().mapColor(MapColor.TERRACOTTA_YELLOW)));
    public static final DeferredItem<BlockItem> SOLARWOOD_STAIRS_ITEM = ITEMS.register("solarwood_stairs",
            () -> new BlockItem(SOLARWOOD_STAIRS.get(), new Item.Properties()));
    public static final DeferredBlock<SlabBlock> SOLARWOOD_SLAB = BLOCKS.register("solarwood_slab",
            () -> new SlabBlock(woodProps().mapColor(MapColor.TERRACOTTA_YELLOW)));
    public static final DeferredItem<BlockItem> SOLARWOOD_SLAB_ITEM = ITEMS.register("solarwood_slab",
            () -> new BlockItem(SOLARWOOD_SLAB.get(), new Item.Properties()));
    public static final DeferredBlock<FenceBlock> SOLARWOOD_FENCE = BLOCKS.register("solarwood_fence",
            () -> new FenceBlock(woodProps().mapColor(MapColor.TERRACOTTA_YELLOW)));
    public static final DeferredItem<BlockItem> SOLARWOOD_FENCE_ITEM = ITEMS.register("solarwood_fence",
            () -> new BlockItem(SOLARWOOD_FENCE.get(), new Item.Properties()));
    public static final DeferredBlock<FenceGateBlock> SOLARWOOD_FENCE_GATE = BLOCKS.register("solarwood_fence_gate",
            () -> new FenceGateBlock(WoodType.OAK, woodProps().mapColor(MapColor.TERRACOTTA_YELLOW)));
    public static final DeferredItem<BlockItem> SOLARWOOD_FENCE_GATE_ITEM = ITEMS.register("solarwood_fence_gate",
            () -> new BlockItem(SOLARWOOD_FENCE_GATE.get(), new Item.Properties()));
    public static final DeferredBlock<ButtonBlock> SOLARWOOD_BUTTON = BLOCKS.register("solarwood_button",
            () -> new ButtonBlock(BlockSetType.OAK, 30, woodProps().noCollission().strength(0.5f)));
    public static final DeferredItem<BlockItem> SOLARWOOD_BUTTON_ITEM = ITEMS.register("solarwood_button",
            () -> new BlockItem(SOLARWOOD_BUTTON.get(), new Item.Properties()));
    public static final DeferredBlock<PressurePlateBlock> SOLARWOOD_PRESSURE_PLATE = BLOCKS.register("solarwood_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK, woodProps().noCollission().strength(0.5f)));
    public static final DeferredItem<BlockItem> SOLARWOOD_PRESSURE_PLATE_ITEM = ITEMS.register("solarwood_pressure_plate",
            () -> new BlockItem(SOLARWOOD_PRESSURE_PLATE.get(), new Item.Properties()));

    // ===== 蜜缘木木制品 =====
    public static final DeferredBlock<Block> ORCHARD_HEARTWOOD_PLANKS = BLOCKS.register("orchard_heartwood_planks",
            () -> new Block(woodProps().mapColor(MapColor.COLOR_BROWN)));
    public static final DeferredItem<BlockItem> ORCHARD_HEARTWOOD_PLANKS_ITEM = ITEMS.register("orchard_heartwood_planks",
            () -> new BlockItem(ORCHARD_HEARTWOOD_PLANKS.get(), new Item.Properties()));
    public static final DeferredBlock<StairBlock> ORCHARD_HEARTWOOD_STAIRS = BLOCKS.register("orchard_heartwood_stairs",
            () -> new StairBlock(ORCHARD_HEARTWOOD_PLANKS.get().defaultBlockState(), woodProps().mapColor(MapColor.COLOR_BROWN)));
    public static final DeferredItem<BlockItem> ORCHARD_HEARTWOOD_STAIRS_ITEM = ITEMS.register("orchard_heartwood_stairs",
            () -> new BlockItem(ORCHARD_HEARTWOOD_STAIRS.get(), new Item.Properties()));
    public static final DeferredBlock<SlabBlock> ORCHARD_HEARTWOOD_SLAB = BLOCKS.register("orchard_heartwood_slab",
            () -> new SlabBlock(woodProps().mapColor(MapColor.COLOR_BROWN)));
    public static final DeferredItem<BlockItem> ORCHARD_HEARTWOOD_SLAB_ITEM = ITEMS.register("orchard_heartwood_slab",
            () -> new BlockItem(ORCHARD_HEARTWOOD_SLAB.get(), new Item.Properties()));
    public static final DeferredBlock<FenceBlock> ORCHARD_HEARTWOOD_FENCE = BLOCKS.register("orchard_heartwood_fence",
            () -> new FenceBlock(woodProps().mapColor(MapColor.COLOR_BROWN)));
    public static final DeferredItem<BlockItem> ORCHARD_HEARTWOOD_FENCE_ITEM = ITEMS.register("orchard_heartwood_fence",
            () -> new BlockItem(ORCHARD_HEARTWOOD_FENCE.get(), new Item.Properties()));
    public static final DeferredBlock<FenceGateBlock> ORCHARD_HEARTWOOD_FENCE_GATE = BLOCKS.register("orchard_heartwood_fence_gate",
            () -> new FenceGateBlock(WoodType.OAK, woodProps().mapColor(MapColor.COLOR_BROWN)));
    public static final DeferredItem<BlockItem> ORCHARD_HEARTWOOD_FENCE_GATE_ITEM = ITEMS.register("orchard_heartwood_fence_gate",
            () -> new BlockItem(ORCHARD_HEARTWOOD_FENCE_GATE.get(), new Item.Properties()));
    public static final DeferredBlock<ButtonBlock> ORCHARD_HEARTWOOD_BUTTON = BLOCKS.register("orchard_heartwood_button",
            () -> new ButtonBlock(BlockSetType.OAK, 30, woodProps().noCollission().strength(0.5f)));
    public static final DeferredItem<BlockItem> ORCHARD_HEARTWOOD_BUTTON_ITEM = ITEMS.register("orchard_heartwood_button",
            () -> new BlockItem(ORCHARD_HEARTWOOD_BUTTON.get(), new Item.Properties()));
    public static final DeferredBlock<PressurePlateBlock> ORCHARD_HEARTWOOD_PRESSURE_PLATE = BLOCKS.register("orchard_heartwood_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK, woodProps().noCollission().strength(0.5f)));
    public static final DeferredItem<BlockItem> ORCHARD_HEARTWOOD_PRESSURE_PLATE_ITEM = ITEMS.register("orchard_heartwood_pressure_plate",
            () -> new BlockItem(ORCHARD_HEARTWOOD_PRESSURE_PLATE.get(), new Item.Properties()));

    // ===== 核毅木木制品 =====
    public static final DeferredBlock<Block> STONEBARK_PLANKS = BLOCKS.register("stonebark_planks",
            () -> new Block(woodProps().mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final DeferredItem<BlockItem> STONEBARK_PLANKS_ITEM = ITEMS.register("stonebark_planks",
            () -> new BlockItem(STONEBARK_PLANKS.get(), new Item.Properties()));
    public static final DeferredBlock<StairBlock> STONEBARK_STAIRS = BLOCKS.register("stonebark_stairs",
            () -> new StairBlock(STONEBARK_PLANKS.get().defaultBlockState(), woodProps().mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final DeferredItem<BlockItem> STONEBARK_STAIRS_ITEM = ITEMS.register("stonebark_stairs",
            () -> new BlockItem(STONEBARK_STAIRS.get(), new Item.Properties()));
    public static final DeferredBlock<SlabBlock> STONEBARK_SLAB = BLOCKS.register("stonebark_slab",
            () -> new SlabBlock(woodProps().mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final DeferredItem<BlockItem> STONEBARK_SLAB_ITEM = ITEMS.register("stonebark_slab",
            () -> new BlockItem(STONEBARK_SLAB.get(), new Item.Properties()));
    public static final DeferredBlock<FenceBlock> STONEBARK_FENCE = BLOCKS.register("stonebark_fence",
            () -> new FenceBlock(woodProps().mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final DeferredItem<BlockItem> STONEBARK_FENCE_ITEM = ITEMS.register("stonebark_fence",
            () -> new BlockItem(STONEBARK_FENCE.get(), new Item.Properties()));
    public static final DeferredBlock<FenceGateBlock> STONEBARK_FENCE_GATE = BLOCKS.register("stonebark_fence_gate",
            () -> new FenceGateBlock(WoodType.OAK, woodProps().mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final DeferredItem<BlockItem> STONEBARK_FENCE_GATE_ITEM = ITEMS.register("stonebark_fence_gate",
            () -> new BlockItem(STONEBARK_FENCE_GATE.get(), new Item.Properties()));
    public static final DeferredBlock<ButtonBlock> STONEBARK_BUTTON = BLOCKS.register("stonebark_button",
            () -> new ButtonBlock(BlockSetType.OAK, 30, woodProps().noCollission().strength(0.5f)));
    public static final DeferredItem<BlockItem> STONEBARK_BUTTON_ITEM = ITEMS.register("stonebark_button",
            () -> new BlockItem(STONEBARK_BUTTON.get(), new Item.Properties()));
    public static final DeferredBlock<PressurePlateBlock> STONEBARK_PRESSURE_PLATE = BLOCKS.register("stonebark_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK, woodProps().noCollission().strength(0.5f)));
    public static final DeferredItem<BlockItem> STONEBARK_PRESSURE_PLATE_ITEM = ITEMS.register("stonebark_pressure_plate",
            () -> new BlockItem(STONEBARK_PRESSURE_PLATE.get(), new Item.Properties()));

    // ===== 虬藤木木制品 =====
    public static final DeferredBlock<Block> VINEHEART_TIMBER_PLANKS = BLOCKS.register("vineheart_timber_planks",
            () -> new Block(woodProps().mapColor(MapColor.COLOR_BROWN)));
    public static final DeferredItem<BlockItem> VINEHEART_TIMBER_PLANKS_ITEM = ITEMS.register("vineheart_timber_planks",
            () -> new BlockItem(VINEHEART_TIMBER_PLANKS.get(), new Item.Properties()));
    public static final DeferredBlock<StairBlock> VINEHEART_TIMBER_STAIRS = BLOCKS.register("vineheart_timber_stairs",
            () -> new StairBlock(VINEHEART_TIMBER_PLANKS.get().defaultBlockState(), woodProps().mapColor(MapColor.COLOR_BROWN)));
    public static final DeferredItem<BlockItem> VINEHEART_TIMBER_STAIRS_ITEM = ITEMS.register("vineheart_timber_stairs",
            () -> new BlockItem(VINEHEART_TIMBER_STAIRS.get(), new Item.Properties()));
    public static final DeferredBlock<SlabBlock> VINEHEART_TIMBER_SLAB = BLOCKS.register("vineheart_timber_slab",
            () -> new SlabBlock(woodProps().mapColor(MapColor.COLOR_BROWN)));
    public static final DeferredItem<BlockItem> VINEHEART_TIMBER_SLAB_ITEM = ITEMS.register("vineheart_timber_slab",
            () -> new BlockItem(VINEHEART_TIMBER_SLAB.get(), new Item.Properties()));
    public static final DeferredBlock<FenceBlock> VINEHEART_TIMBER_FENCE = BLOCKS.register("vineheart_timber_fence",
            () -> new FenceBlock(woodProps().mapColor(MapColor.COLOR_BROWN)));
    public static final DeferredItem<BlockItem> VINEHEART_TIMBER_FENCE_ITEM = ITEMS.register("vineheart_timber_fence",
            () -> new BlockItem(VINEHEART_TIMBER_FENCE.get(), new Item.Properties()));
    public static final DeferredBlock<FenceGateBlock> VINEHEART_TIMBER_FENCE_GATE = BLOCKS.register("vineheart_timber_fence_gate",
            () -> new FenceGateBlock(WoodType.OAK, woodProps().mapColor(MapColor.COLOR_BROWN)));
    public static final DeferredItem<BlockItem> VINEHEART_TIMBER_FENCE_GATE_ITEM = ITEMS.register("vineheart_timber_fence_gate",
            () -> new BlockItem(VINEHEART_TIMBER_FENCE_GATE.get(), new Item.Properties()));
    public static final DeferredBlock<ButtonBlock> VINEHEART_TIMBER_BUTTON = BLOCKS.register("vineheart_timber_button",
            () -> new ButtonBlock(BlockSetType.OAK, 30, woodProps().noCollission().strength(0.5f)));
    public static final DeferredItem<BlockItem> VINEHEART_TIMBER_BUTTON_ITEM = ITEMS.register("vineheart_timber_button",
            () -> new BlockItem(VINEHEART_TIMBER_BUTTON.get(), new Item.Properties()));
    public static final DeferredBlock<PressurePlateBlock> VINEHEART_TIMBER_PRESSURE_PLATE = BLOCKS.register("vineheart_timber_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK, woodProps().noCollission().strength(0.5f)));
    public static final DeferredItem<BlockItem> VINEHEART_TIMBER_PRESSURE_PLATE_ITEM = ITEMS.register("vineheart_timber_pressure_plate",
            () -> new BlockItem(VINEHEART_TIMBER_PRESSURE_PLATE.get(), new Item.Properties()));

    // ===== 清韵木木制品 =====
    public static final DeferredBlock<Block> VERDANT_GRACE_PLANKS = BLOCKS.register("verdant_grace_planks",
            () -> new Block(woodProps().mapColor(MapColor.COLOR_GREEN)));
    public static final DeferredItem<BlockItem> VERDANT_GRACE_PLANKS_ITEM = ITEMS.register("verdant_grace_planks",
            () -> new BlockItem(VERDANT_GRACE_PLANKS.get(), new Item.Properties()));
    public static final DeferredBlock<StairBlock> VERDANT_GRACE_STAIRS = BLOCKS.register("verdant_grace_stairs",
            () -> new StairBlock(VERDANT_GRACE_PLANKS.get().defaultBlockState(), woodProps().mapColor(MapColor.COLOR_GREEN)));
    public static final DeferredItem<BlockItem> VERDANT_GRACE_STAIRS_ITEM = ITEMS.register("verdant_grace_stairs",
            () -> new BlockItem(VERDANT_GRACE_STAIRS.get(), new Item.Properties()));
    public static final DeferredBlock<SlabBlock> VERDANT_GRACE_SLAB = BLOCKS.register("verdant_grace_slab",
            () -> new SlabBlock(woodProps().mapColor(MapColor.COLOR_GREEN)));
    public static final DeferredItem<BlockItem> VERDANT_GRACE_SLAB_ITEM = ITEMS.register("verdant_grace_slab",
            () -> new BlockItem(VERDANT_GRACE_SLAB.get(), new Item.Properties()));
    public static final DeferredBlock<FenceBlock> VERDANT_GRACE_FENCE = BLOCKS.register("verdant_grace_fence",
            () -> new FenceBlock(woodProps().mapColor(MapColor.COLOR_GREEN)));
    public static final DeferredItem<BlockItem> VERDANT_GRACE_FENCE_ITEM = ITEMS.register("verdant_grace_fence",
            () -> new BlockItem(VERDANT_GRACE_FENCE.get(), new Item.Properties()));
    public static final DeferredBlock<FenceGateBlock> VERDANT_GRACE_FENCE_GATE = BLOCKS.register("verdant_grace_fence_gate",
            () -> new FenceGateBlock(WoodType.OAK, woodProps().mapColor(MapColor.COLOR_GREEN)));
    public static final DeferredItem<BlockItem> VERDANT_GRACE_FENCE_GATE_ITEM = ITEMS.register("verdant_grace_fence_gate",
            () -> new BlockItem(VERDANT_GRACE_FENCE_GATE.get(), new Item.Properties()));
    public static final DeferredBlock<ButtonBlock> VERDANT_GRACE_BUTTON = BLOCKS.register("verdant_grace_button",
            () -> new ButtonBlock(BlockSetType.OAK, 30, woodProps().noCollission().strength(0.5f)));
    public static final DeferredItem<BlockItem> VERDANT_GRACE_BUTTON_ITEM = ITEMS.register("verdant_grace_button",
            () -> new BlockItem(VERDANT_GRACE_BUTTON.get(), new Item.Properties()));
    public static final DeferredBlock<PressurePlateBlock> VERDANT_GRACE_PRESSURE_PLATE = BLOCKS.register("verdant_grace_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK, woodProps().noCollission().strength(0.5f)));
    public static final DeferredItem<BlockItem> VERDANT_GRACE_PRESSURE_PLATE_ITEM = ITEMS.register("verdant_grace_pressure_plate",
            () -> new BlockItem(VERDANT_GRACE_PRESSURE_PLATE.get(), new Item.Properties()));

    // ================================================================
    // 椅子
    // ================================================================
    public static final DeferredBlock<ChairBlock> SOLARWOODCHAIR = BLOCKS.register("solarwoodchair",
            () -> new ChairBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.WOOD).noOcclusion()));
    public static final DeferredItem<BlockItem> SOLARWOODCHAIR_ITEM = ITEMS.register("solarwoodchair",
            () -> new BlockItem(SOLARWOODCHAIR.get(), new Item.Properties()));
    public static final DeferredBlock<ChairBlock> ORCHARDCHAIR = BLOCKS.register("orchardchair",
            () -> new ChairBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.WOOD).noOcclusion()));
    public static final DeferredItem<BlockItem> ORCHARDCHAIR_ITEM = ITEMS.register("orchardchair",
            () -> new BlockItem(ORCHARDCHAIR.get(), new Item.Properties()));
    public static final DeferredBlock<ChairBlock> STONEBARKCHAIR = BLOCKS.register("stonebarkchair",
            () -> new ChairBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.WOOD).noOcclusion()));
    public static final DeferredItem<BlockItem> STONEBARKCHAIR_ITEM = ITEMS.register("stonebarkchair",
            () -> new BlockItem(STONEBARKCHAIR.get(), new Item.Properties()));
    public static final DeferredBlock<ChairBlock> VINEHEARTCHAIR = BLOCKS.register("vineheartchair",
            () -> new ChairBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.WOOD).noOcclusion()));
    public static final DeferredItem<BlockItem> VINEHEARTCHAIR_ITEM = ITEMS.register("vineheartchair",
            () -> new BlockItem(VINEHEARTCHAIR.get(), new Item.Properties()));
    public static final DeferredBlock<ChairBlock> VERDANTGRACECHAIR = BLOCKS.register("verdantgracechair",
            () -> new ChairBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.WOOD).noOcclusion()));
    public static final DeferredItem<BlockItem> VERDANTGRACECHAIR_ITEM = ITEMS.register("verdantgracechair",
            () -> new BlockItem(VERDANTGRACECHAIR.get(), new Item.Properties()));

    // ========== 农产鉴定机 ==========
    public static final DeferredBlock<AgriculturalAppraisalMachineBlock> AGRICULTURALAPPRAISALMACHINE = BLOCKS.register("agriculturalappraisalmachine",
            () -> new AgriculturalAppraisalMachineBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.METAL).noOcclusion()));
    public static final DeferredItem<BlockItem> AGRICULTURALAPPRAISALMACHINE_ITEM = ITEMS.register("agriculturalappraisalmachine",
            () -> new com.flavor_immersed_daily.item.TooltipBlockItem(AGRICULTURALAPPRAISALMACHINE.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.agriculturalappraisalmachine"), () -> java.util.List.of()));

    // ========== 冰箱 ==========
    public static final DeferredBlock<FridgeBlock> FRIDGE = BLOCKS.register("fridge",
            () -> new FridgeBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.METAL).noOcclusion()));
    public static final DeferredItem<BlockItem> FRIDGE_ITEM = ITEMS.register("fridge",
            () -> new BlockItem(FRIDGE.get(), new Item.Properties()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FridgeBlockEntity>> FRIDGE_ENTITY = BLOCK_ENTITIES.register("fridge_entity",
            () -> BlockEntityType.Builder.of(FridgeBlockEntity::new, FRIDGE.get()).build(null));

    public static final DeferredHolder<MenuType<?>, MenuType<FridgeMenu>> FRIDGE_MENU = MENU_TYPES.register("fridge_menu",
            () -> new MenuType<>(FridgeMenu::new, FeatureFlags.DEFAULT_FLAGS));

    // ========== 打蛋机 ==========
    public static final DeferredBlock<EggBreakingMachineBlock> EGGBREAKINGMACHINE = BLOCKS.register("eggbreakingmachine",
            () -> new EggBreakingMachineBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.METAL).noOcclusion()));
    public static final DeferredItem<BlockItem> EGGBREAKINGMACHINE_ITEM = ITEMS.register("eggbreakingmachine",
            () -> new BlockItem(EGGBREAKINGMACHINE.get(), new Item.Properties()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EggBreakingMachineBlockEntity>> EGG_BREAKING_MACHINE_ENTITY =
            BLOCK_ENTITIES.register("egg_breaking_machine_entity",
                    () -> BlockEntityType.Builder.of(EggBreakingMachineBlockEntity::new, EGGBREAKINGMACHINE.get()).build(null));

    public static final DeferredHolder<MenuType<?>, MenuType<EggBreakingMachineMenu>> EGG_BREAKING_MACHINE_MENU =
            MENU_TYPES.register("egg_breaking_machine_menu",
                    () -> new MenuType<>(EggBreakingMachineMenu::new, FeatureFlags.DEFAULT_FLAGS));

    // ========== 打蛋机·配方 ==========
    public static final DeferredHolder<RecipeType<?>, RecipeType<EggBreakingRecipe>> EGG_BREAKING_TYPE =
            RECIPE_TYPES.register("egg_breaking", () -> new RecipeType<>() {});
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<EggBreakingRecipe>> EGG_BREAKING_SERIALIZER =
            RECIPE_SERIALIZERS.register("egg_breaking", () -> EggBreakingRecipeSerializer.INSTANCE);

    // ========== 冰箱·配方类型 ==========
    public static final DeferredHolder<RecipeType<?>, RecipeType<FridgeTemperingRecipe>> FRIDGE_TEMPERING_TYPE =
            RECIPE_TYPES.register("fridge_tempering", () -> new RecipeType<>() {});
    public static final DeferredHolder<RecipeType<?>, RecipeType<FridgeFreezingRecipe>> FRIDGE_FREEZING_TYPE =
            RECIPE_TYPES.register("fridge_freezing", () -> new RecipeType<>() {});

    // ========== 冰箱·配方序列化器 ==========
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FridgeTemperingRecipe>> FRIDGE_TEMPERING_SERIALIZER =
            RECIPE_SERIALIZERS.register("fridge_tempering",
                    () -> new RecipeSerializer<FridgeTemperingRecipe>() {
                        @Override
                        public MapCodec<FridgeTemperingRecipe> codec() {
                            return FridgeTemperingRecipe.CODEC;
                        }

                        @Override
                        public StreamCodec<RegistryFriendlyByteBuf, FridgeTemperingRecipe> streamCodec() {
                            return FridgeTemperingRecipe.STREAM_CODEC;
                        }
                    });
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FridgeFreezingRecipe>> FRIDGE_FREEZING_SERIALIZER =
            RECIPE_SERIALIZERS.register("fridge_freezing",
                    () -> new RecipeSerializer<FridgeFreezingRecipe>() {
                        @Override
                        public MapCodec<FridgeFreezingRecipe> codec() {
                            return FridgeFreezingRecipe.CODEC;
                        }

                        @Override
                        public StreamCodec<RegistryFriendlyByteBuf, FridgeFreezingRecipe> streamCodec() {
                            return FridgeFreezingRecipe.STREAM_CODEC;
                        }
                    });

    // ================================================================
    // 果实注册（apricot, cherry, greenplum, hawthorn, loquat, pomegranate on verdant_grace）
    // (carambola, durian, lemon, lychee, mango, pawpaw, tangerine on solarwood)
    // (apple, honeypeach, nectarine, pear, sweetmelon on orchard_heartwood)
    // (pistachionut, reddate, walnut, winterjujube on stonebark)
    // (kiwifruit, mangosteen, mulberry on vineheart_timber)
    // ================================================================
    private static DeferredBlock<FallingFruitBlock> regRaw(String name, DeferredItem<Item> fruit) {
        return BLOCKS.register(name, () -> new FallingFruitBlock(fruit, BlockBehaviour.Properties.of()
                .strength(0.2f).noCollission().noOcclusion().randomTicks().sound(SoundType.GRASS).instabreak()));
    }
    private static DeferredBlock<FruitingLeavesBlock> regFL(String name, DeferredItem<Item> fruit, DeferredBlock<FallingFruitBlock> raw) {
        return BLOCKS.register(name, () -> new FruitingLeavesBlock(fruit, raw, BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT).strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion()
                .isValidSpawn((s, g, p, t) -> false).isSuffocating((s, g, p) -> false).isViewBlocking((s, g, p) -> false)));
    }
    private static DeferredBlock<SaplingBlock> regSap(String name, String treeKey) {
        return BLOCKS.register(name, () -> new SaplingBlock(new TreeGrower(name, 0.0f,
                Optional.empty(), Optional.empty(),
                Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(MODID, treeKey))),
                Optional.empty(), Optional.empty(), Optional.empty()),
                BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    }

    // verdant_grace fruits (excluding plum)
    public static final DeferredBlock<FallingFruitBlock> RAWAPRICOT = regRaw("rawapricot", APRICOT);
    public static final DeferredBlock<FruitingLeavesBlock> VERDANT_GRACE_FRUITING_LEAVES_APRICOT = regFL("verdant_grace_fruiting_leaves_apricot", APRICOT, RAWAPRICOT);
    public static final DeferredBlock<SaplingBlock> APRICOT_SAPLING = regSap("apricotsapling", "apricot_tree");
    public static final DeferredItem<BlockItem> VERDANT_GRACE_FRUITING_LEAVES_APRICOT_ITEM = ITEMS.register("verdant_grace_fruiting_leaves_apricot",
            () -> new BlockItem(VERDANT_GRACE_FRUITING_LEAVES_APRICOT.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> APRICOT_SAPLING_ITEM = ITEMS.register("apricotsapling",
            () -> new TooltipBlockItem(APRICOT_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWCHERRY = regRaw("rawcherry", CHERRY);
    public static final DeferredBlock<FruitingLeavesBlock> VERDANT_GRACE_FRUITING_LEAVES_CHERRY = regFL("verdant_grace_fruiting_leaves_cherry", CHERRY, RAWCHERRY);
    public static final DeferredBlock<SaplingBlock> CHERRY_SAPLING = regSap("cherrysapling", "cherry_tree");
    public static final DeferredItem<BlockItem> VERDANT_GRACE_FRUITING_LEAVES_CHERRY_ITEM = ITEMS.register("verdant_grace_fruiting_leaves_cherry",
            () -> new BlockItem(VERDANT_GRACE_FRUITING_LEAVES_CHERRY.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> CHERRY_SAPLING_ITEM = ITEMS.register("cherrysapling",
            () -> new TooltipBlockItem(CHERRY_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWGREENPLUM = regRaw("rawgreenplum", GREENPLUM);
    public static final DeferredBlock<FruitingLeavesBlock> VERDANT_GRACE_FRUITING_LEAVES_GREENPLUM = regFL("verdant_grace_fruiting_leaves_greenplum", GREENPLUM, RAWGREENPLUM);
    public static final DeferredBlock<SaplingBlock> GREENPLUM_SAPLING = regSap("greenplumsapling", "greenplum_tree");
    public static final DeferredItem<BlockItem> VERDANT_GRACE_FRUITING_LEAVES_GREENPLUM_ITEM = ITEMS.register("verdant_grace_fruiting_leaves_greenplum",
            () -> new BlockItem(VERDANT_GRACE_FRUITING_LEAVES_GREENPLUM.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> GREENPLUM_SAPLING_ITEM = ITEMS.register("greenplumsapling",
            () -> new TooltipBlockItem(GREENPLUM_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWHAWTHORN = regRaw("rawhawthorn", HAWTHORN);
    public static final DeferredBlock<FruitingLeavesBlock> VERDANT_GRACE_FRUITING_LEAVES_HAWTHORN = regFL("verdant_grace_fruiting_leaves_hawthorn", HAWTHORN, RAWHAWTHORN);
    public static final DeferredBlock<SaplingBlock> HAWTHORN_SAPLING = regSap("hawthornsapling", "hawthorn_tree");
    public static final DeferredItem<BlockItem> VERDANT_GRACE_FRUITING_LEAVES_HAWTHORN_ITEM = ITEMS.register("verdant_grace_fruiting_leaves_hawthorn",
            () -> new BlockItem(VERDANT_GRACE_FRUITING_LEAVES_HAWTHORN.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> HAWTHORN_SAPLING_ITEM = ITEMS.register("hawthornsapling",
            () -> new TooltipBlockItem(HAWTHORN_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWLOQUAT = regRaw("rawloquat", LOQUAT);
    public static final DeferredBlock<FruitingLeavesBlock> VERDANT_GRACE_FRUITING_LEAVES_LOQUAT = regFL("verdant_grace_fruiting_leaves_loquat", LOQUAT, RAWLOQUAT);
    public static final DeferredBlock<SaplingBlock> LOQUAT_SAPLING = regSap("loquatleavesapling", "loquat_tree");
    public static final DeferredItem<BlockItem> VERDANT_GRACE_FRUITING_LEAVES_LOQUAT_ITEM = ITEMS.register("verdant_grace_fruiting_leaves_loquat",
            () -> new BlockItem(VERDANT_GRACE_FRUITING_LEAVES_LOQUAT.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> LOQUAT_SAPLING_ITEM = ITEMS.register("loquatleavesapling",
            () -> new TooltipBlockItem(LOQUAT_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWPOMEGRANATE = regRaw("rawpomegranate", POMEGRANATE);
    public static final DeferredBlock<FruitingLeavesBlock> VERDANT_GRACE_FRUITING_LEAVES_POMEGRANATE = regFL("verdant_grace_fruiting_leaves_pomegranate", POMEGRANATE, RAWPOMEGRANATE);
    public static final DeferredBlock<SaplingBlock> POMEGRANATE_SAPLING = regSap("pomegranatesapling", "pomegranate_tree");
    public static final DeferredItem<BlockItem> VERDANT_GRACE_FRUITING_LEAVES_POMEGRANATE_ITEM = ITEMS.register("verdant_grace_fruiting_leaves_pomegranate",
            () -> new BlockItem(VERDANT_GRACE_FRUITING_LEAVES_POMEGRANATE.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> POMEGRANATE_SAPLING_ITEM = ITEMS.register("pomegranatesapling",
            () -> new TooltipBlockItem(POMEGRANATE_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    // solarwood fruits (excluding orange which is already done)
    public static final DeferredBlock<FallingFruitBlock> RAWCARAMBOLA = regRaw("rawcarambola", CARAMBOLA);
    public static final DeferredBlock<FruitingLeavesBlock> CARAMBOLEAVE_FRUITING_LEAVES = regFL("carambolaleave_fruiting_leaves", CARAMBOLA, RAWCARAMBOLA);
    public static final DeferredBlock<SaplingBlock> CARAMBOLA_SAPLING = regSap("carambolasapling", "carambola_tree");
    public static final DeferredItem<BlockItem> CARAMBOLEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("carambolaleave_fruiting_leaves",
            () -> new BlockItem(CARAMBOLEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> CARAMBOLA_SAPLING_ITEM = ITEMS.register("carambolasapling",
            () -> new TooltipBlockItem(CARAMBOLA_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWDURIAN = regRaw("rawdurian", DURIAN);
    public static final DeferredBlock<FruitingLeavesBlock> DURIANLEAVE_FRUITING_LEAVES = regFL("durianleave_fruiting_leaves", DURIAN, RAWDURIAN);
    public static final DeferredBlock<SaplingBlock> DURIAN_SAPLING = regSap("duriansapling", "durian_tree");
    public static final DeferredItem<BlockItem> DURIANLEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("durianleave_fruiting_leaves",
            () -> new BlockItem(DURIANLEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> DURIAN_SAPLING_ITEM = ITEMS.register("duriansapling",
            () -> new TooltipBlockItem(DURIAN_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWLEMON = regRaw("rawlemon", LEMON);
    public static final DeferredBlock<FruitingLeavesBlock> LEMONLEAVE_FRUITING_LEAVES = regFL("lemonleave_fruiting_leaves", LEMON, RAWLEMON);
    public static final DeferredBlock<SaplingBlock> LEMON_SAPLING = regSap("lemonsapling", "lemon_tree");
    public static final DeferredItem<BlockItem> LEMONLEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("lemonleave_fruiting_leaves",
            () -> new BlockItem(LEMONLEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> LEMON_SAPLING_ITEM = ITEMS.register("lemonsapling",
            () -> new TooltipBlockItem(LEMON_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWLYCHEE = regRaw("rawlychee", LYCHEE);
    public static final DeferredBlock<FruitingLeavesBlock> LYCHEELEAVE_FRUITING_LEAVES = regFL("lycheeleave_fruiting_leaves", LYCHEE, RAWLYCHEE);
    public static final DeferredBlock<SaplingBlock> LYCHEE_SAPLING = regSap("lycheesapling", "lychee_tree");
    public static final DeferredItem<BlockItem> LYCHEELEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("lycheeleave_fruiting_leaves",
            () -> new BlockItem(LYCHEELEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> LYCHEE_SAPLING_ITEM = ITEMS.register("lycheesapling",
            () -> new TooltipBlockItem(LYCHEE_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWMANG = regRaw("rawmango", MANGO);
    public static final DeferredBlock<FruitingLeavesBlock> MANGOLEAVE_FRUITING_LEAVES = regFL("mangoleave_fruiting_leaves", MANGO, RAWMANG);
    public static final DeferredBlock<SaplingBlock> MANGO_SAPLING = regSap("mangosapling", "mango_tree");
    public static final DeferredItem<BlockItem> MANGOLEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("mangoleave_fruiting_leaves",
            () -> new BlockItem(MANGOLEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> MANGO_SAPLING_ITEM = ITEMS.register("mangosapling",
            () -> new TooltipBlockItem(MANGO_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWPAWPAW = regRaw("rawpawpaw", PAWPAW);
    public static final DeferredBlock<FruitingLeavesBlock> PAWPAWLEAVE_FRUITING_LEAVES = regFL("pawpawleave_fruiting_leaves", PAWPAW, RAWPAWPAW);
    public static final DeferredBlock<SaplingBlock> PAWPAW_SAPLING = regSap("pawpawsapling", "pawpaw_tree");
    public static final DeferredItem<BlockItem> PAWPAWLEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("pawpawleave_fruiting_leaves",
            () -> new BlockItem(PAWPAWLEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> PAWPAW_SAPLING_ITEM = ITEMS.register("pawpawsapling",
            () -> new TooltipBlockItem(PAWPAW_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWTANGERINE = regRaw("rawtangerine", TANGERINE);
    public static final DeferredBlock<FruitingLeavesBlock> TANGERINELEAVE_FRUITING_LEAVES = regFL("tangerineleave_fruiting_leaves", TANGERINE, RAWTANGERINE);
    public static final DeferredBlock<SaplingBlock> TANGERINE_SAPLING = regSap("tangerinesapling", "tangerine_tree");
    public static final DeferredItem<BlockItem> TANGERINELEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("tangerineleave_fruiting_leaves",
            () -> new BlockItem(TANGERINELEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> TANGERINE_SAPLING_ITEM = ITEMS.register("tangerinesapling",
            () -> new TooltipBlockItem(TANGERINE_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    // orchard_heartwood fruits
    public static final DeferredBlock<FallingFruitBlock> RAWAPPLE = BLOCKS.register("rawapple",
            () -> new FallingFruitBlock(() -> Items.APPLE, BlockBehaviour.Properties.of()
                    .strength(0.2f).noCollission().noOcclusion().randomTicks().sound(SoundType.GRASS).instabreak()));
    public static final DeferredBlock<FruitingLeavesBlock> APPLELEAVE_FRUITING_LEAVES = BLOCKS.register("appleleave_fruiting_leaves",
            () -> new FruitingLeavesBlock(() -> Items.APPLE, RAWAPPLE, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT).strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion()
                    .isValidSpawn((s, g, p, t) -> false).isSuffocating((s, g, p) -> false).isViewBlocking((s, g, p) -> false)));
    public static final DeferredBlock<SaplingBlock> APPLE_SAPLING = regSap("applesapling", "apple_tree");
    public static final DeferredItem<BlockItem> APPLELEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("appleleave_fruiting_leaves",
            () -> new BlockItem(APPLELEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> APPLE_SAPLING_ITEM = ITEMS.register("applesapling",
            () -> new TooltipBlockItem(APPLE_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    // coconut
    public static final DeferredBlock<FallingFruitBlock> RAWCOCONUT = regRaw("rawcoconut", COCONUT);
    public static final DeferredBlock<FruitingLeavesBlock> COCONUTLEAVE_FRUITING_LEAVES = regFL("coconutleave_fruiting_leaves", COCONUT, RAWCOCONUT);
    public static final DeferredBlock<CoconutSaplingBlock> COCONUT_SAPLING = BLOCKS.register("coconutsapling",
            () -> new CoconutSaplingBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS),
                    ORCHARD_HEARTWOOD_LOG, ORCHARD_HEARTWOOD_LEAVES, COCONUTLEAVE_FRUITING_LEAVES, RAWCOCONUT));
    public static final DeferredItem<BlockItem> COCONUTLEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("coconutleave_fruiting_leaves",
            () -> new BlockItem(COCONUTLEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> COCONUT_SAPLING_ITEM = ITEMS.register("coconutsapling",
            () -> new TooltipBlockItem(COCONUT_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWHONEYPEACH = regRaw("rawhoneypeach", HONEYPEACH);
    public static final DeferredBlock<FruitingLeavesBlock> HONEYPEACHLEAVE_FRUITING_LEAVES = regFL("honeypeachleave_fruiting_leaves", HONEYPEACH, RAWHONEYPEACH);
    public static final DeferredBlock<SaplingBlock> HONEYPEACH_SAPLING = regSap("honeypeachsapling", "honeypeach_tree");
    public static final DeferredItem<BlockItem> HONEYPEACHLEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("honeypeachleave_fruiting_leaves",
            () -> new BlockItem(HONEYPEACHLEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> HONEYPEACH_SAPLING_ITEM = ITEMS.register("honeypeachsapling",
            () -> new TooltipBlockItem(HONEYPEACH_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWNECTARINE = regRaw("rawnectarine", NECTARINE);
    public static final DeferredBlock<FruitingLeavesBlock> NECTARINELEAVE_FRUITING_LEAVES = regFL("nectarineleave_fruiting_leaves", NECTARINE, RAWNECTARINE);
    public static final DeferredBlock<SaplingBlock> NECTARINE_SAPLING = regSap("nectarinesapling", "nectarine_tree");
    public static final DeferredItem<BlockItem> NECTARINELEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("nectarineleave_fruiting_leaves",
            () -> new BlockItem(NECTARINELEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> NECTARINE_SAPLING_ITEM = ITEMS.register("nectarinesapling",
            () -> new TooltipBlockItem(NECTARINE_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWPEAR = regRaw("rawpear", PEAR);
    public static final DeferredBlock<FruitingLeavesBlock> PEARLEAVE_FRUITING_LEAVES = regFL("pearleaves_fruiting_leaves", PEAR, RAWPEAR);
    public static final DeferredBlock<SaplingBlock> PEAR_SAPLING = regSap("pearsapling", "pear_tree");
    public static final DeferredItem<BlockItem> PEARLEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("pearleaves_fruiting_leaves",
            () -> new BlockItem(PEARLEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> PEAR_SAPLING_ITEM = ITEMS.register("pearsapling",
            () -> new TooltipBlockItem(PEAR_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWSWEETMELON = regRaw("rawsweetmelon", SWEETMELON);
    public static final DeferredBlock<FruitingLeavesBlock> SWEETMELONLEAVE_FRUITING_LEAVES = regFL("sweetmelonleave_fruiting_leaves", SWEETMELON, RAWSWEETMELON);
    public static final DeferredBlock<SaplingBlock> SWEETMELON_SAPLING = regSap("sweetmelonsapling", "sweetmelon_tree");
    public static final DeferredItem<BlockItem> SWEETMELONLEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("sweetmelonleave_fruiting_leaves",
            () -> new BlockItem(SWEETMELONLEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> SWEETMELON_SAPLING_ITEM = ITEMS.register("sweetmelonsapling",
            () -> new TooltipBlockItem(SWEETMELON_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    // stonebark fruits
    public static final DeferredBlock<FallingFruitBlock> RAWPISTACHIONUT = regRaw("rawpistachionut", PISTACHIONUT);
    public static final DeferredBlock<FruitingLeavesBlock> PISTACHIONUTLEAVE_FRUITING_LEAVES = regFL("pistachionutleave_fruiting_leaves", PISTACHIONUT, RAWPISTACHIONUT);
    public static final DeferredBlock<SaplingBlock> PISTACHIONUT_SAPLING = regSap("pistachionutsapling", "pistachionut_tree");
    public static final DeferredItem<BlockItem> PISTACHIONUTLEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("pistachionutleave_fruiting_leaves",
            () -> new BlockItem(PISTACHIONUTLEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> PISTACHIONUT_SAPLING_ITEM = ITEMS.register("pistachionutsapling",
            () -> new TooltipBlockItem(PISTACHIONUT_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWREDDATE = regRaw("rawreddate", REDDATE);
    public static final DeferredBlock<FruitingLeavesBlock> REDDATELEAVE_FRUITING_LEAVES = regFL("reddateleave_fruiting_leaves", REDDATE, RAWREDDATE);
    public static final DeferredBlock<SaplingBlock> REDDATE_SAPLING = regSap("reddatesapling", "reddate_tree");
    public static final DeferredItem<BlockItem> REDDATELEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("reddateleave_fruiting_leaves",
            () -> new BlockItem(REDDATELEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> REDDATE_SAPLING_ITEM = ITEMS.register("reddatesapling",
            () -> new TooltipBlockItem(REDDATE_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWWALNUT = regRaw("rawwalnut", WALNUT);
    public static final DeferredBlock<FruitingLeavesBlock> WALNUTLEAVE_FRUITING_LEAVES = regFL("walnutleaves_fruiting_leaves", WALNUT, RAWWALNUT);
    public static final DeferredBlock<SaplingBlock> WALNUT_SAPLING = regSap("walnutsapling", "walnut_tree");
    public static final DeferredItem<BlockItem> WALNUTLEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("walnutleaves_fruiting_leaves",
            () -> new BlockItem(WALNUTLEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> WALNUT_SAPLING_ITEM = ITEMS.register("walnutsapling",
            () -> new TooltipBlockItem(WALNUT_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWWINTERJUJUBE = regRaw("rawwinterjujube", WINTERJUJUBE);
    public static final DeferredBlock<FruitingLeavesBlock> WINTERJUJUBELEAVE_FRUITING_LEAVES = regFL("winterjujubeleave_fruiting_leaves", WINTERJUJUBE, RAWWINTERJUJUBE);
    public static final DeferredBlock<SaplingBlock> WINTERJUJUBE_SAPLING = regSap("winterjujubesapling", "winterjujube_tree");
    public static final DeferredItem<BlockItem> WINTERJUJUBELEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("winterjujubeleave_fruiting_leaves",
            () -> new BlockItem(WINTERJUJUBELEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> WINTERJUJUBE_SAPLING_ITEM = ITEMS.register("winterjujubesapling",
            () -> new TooltipBlockItem(WINTERJUJUBE_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    // vineheart_timber fruits
    public static final DeferredBlock<FallingFruitBlock> RAWKIWIFRUIT = regRaw("rawkiwifruit", KIWIFRUIT);
    public static final DeferredBlock<FruitingLeavesBlock> KIWIFRUITSSLEAVE_FRUITING_LEAVES = regFL("kiwifruitsleave_fruiting_leaves", KIWIFRUIT, RAWKIWIFRUIT);
    public static final DeferredBlock<SaplingBlock> KIWIFRUIT_SAPLING = regSap("kiwifruitsleavesapling", "kiwifruit_tree");
    public static final DeferredItem<BlockItem> KIWIFRUITSSLEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("kiwifruitsleave_fruiting_leaves",
            () -> new BlockItem(KIWIFRUITSSLEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> KIWIFRUIT_SAPLING_ITEM = ITEMS.register("kiwifruitsleavesapling",
            () -> new TooltipBlockItem(KIWIFRUIT_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWMANGOSTEEN = regRaw("rawmangosteen", MANGOSTEEN);
    public static final DeferredBlock<FruitingLeavesBlock> MANGOSTEENLEAVE_FRUITING_LEAVES = regFL("mangosteenleave_fruiting_leaves", MANGOSTEEN, RAWMANGOSTEEN);
    public static final DeferredBlock<SaplingBlock> MANGOSTEEN_SAPLING = regSap("mangosteensapling", "mangosteen_tree");
    public static final DeferredItem<BlockItem> MANGOSTEENLEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("mangosteenleave_fruiting_leaves",
            () -> new BlockItem(MANGOSTEENLEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> MANGOSTEEN_SAPLING_ITEM = ITEMS.register("mangosteensapling",
            () -> new TooltipBlockItem(MANGOSTEEN_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    public static final DeferredBlock<FallingFruitBlock> RAWMULBERRY = regRaw("rawmulberry", MULBERRY);
    public static final DeferredBlock<FruitingLeavesBlock> MULBERRYLEAVE_FRUITING_LEAVES = regFL("mulberryleaves_fruiting_leaves", MULBERRY, RAWMULBERRY);
    public static final DeferredBlock<SaplingBlock> MULBERRY_SAPLING = regSap("mulberrysapling", "mulberry_tree");
    public static final DeferredItem<BlockItem> MULBERRYLEAVE_FRUITING_LEAVES_ITEM = ITEMS.register("mulberryleaves_fruiting_leaves",
            () -> new BlockItem(MULBERRYLEAVE_FRUITING_LEAVES.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> MULBERRY_SAPLING_ITEM = ITEMS.register("mulberrysapling",
            () -> new TooltipBlockItem(MULBERRY_SAPLING.get(), new Item.Properties(),
                    Component.translatable("tooltip.flavor_immersed_daily.sapling_harvest"), () -> java.util.List.of()));

    // ========== 机器部件 ==========
    public static final DeferredItem<Item> MINCER_COVER = ITEMS.register("mincer_cover",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TEAPOTCOVER = ITEMS.register("teapotcover",
            () -> new Item(new Item.Properties()));

    // ========== 仙女棒 ==========
    public static final DeferredItem<FairySparklerItem> FAIRY_SPARKLER = ITEMS.register("fairy_sparkler",
            () -> new FairySparklerItem(new Item.Properties().stacksTo(1).durability(500)));

    // ========== 箱装烟花 ==========
    public static final DeferredBlock<ColorfulFireworksBoxBlock> COLORFUL_FIREWORKS_BOX = BLOCKS.register("colorful_fireworks_box",
            () -> new ColorfulFireworksBoxBlock(BlockBehaviour.Properties.of()
                    .strength(1.5f)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));
    public static final DeferredItem<BlockItem> COLORFUL_FIREWORKS_BOX_ITEM = ITEMS.register("colorful_fireworks_box",
            () -> new ColorfulFireworksBoxItem(COLORFUL_FIREWORKS_BOX.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ColorfulFireworksBoxBlockEntity>> COLORFUL_FIREWORKS_BOX_ENTITY = BLOCK_ENTITIES.register("colorful_fireworks_box_entity",
            () -> BlockEntityType.Builder.of(ColorfulFireworksBoxBlockEntity::new, COLORFUL_FIREWORKS_BOX.get()).build(null));

    // ========== 门纸 ==========
    public static final DeferredBlock<DoorPaperBlock> LEFT_DOOR_PAPER = BLOCKS.register("leftdoorpaper",
            () -> new DoorPaperBlock(BlockBehaviour.Properties.of().instabreak().noCollission().sound(SoundType.WOOL)));
    public static final DeferredItem<BlockItem> LEFT_DOOR_PAPER_ITEM = ITEMS.register("leftdoorpaper",
            () -> new BlockItem(LEFT_DOOR_PAPER.get(), new Item.Properties()));
    public static final DeferredBlock<DoorPaperBlock> RIGHT_DOOR_PAPER = BLOCKS.register("rightdoorpaper",
            () -> new DoorPaperBlock(BlockBehaviour.Properties.of().instabreak().noCollission().sound(SoundType.WOOL)));
    public static final DeferredItem<BlockItem> RIGHT_DOOR_PAPER_ITEM = ITEMS.register("rightdoorpaper",
            () -> new BlockItem(RIGHT_DOOR_PAPER.get(), new Item.Properties()));

    // ========== 中国结 ==========
    public static final DeferredBlock<ChineseKnottingBlock> CHINESE_KNOTTING = BLOCKS.register("chinese_knotting",
            () -> new ChineseKnottingBlock(BlockBehaviour.Properties.of().instabreak().noCollission().sound(SoundType.WOOL)));
    public static final DeferredItem<BlockItem> CHINESE_KNOTTING_ITEM = ITEMS.register("chinese_knotting",
            () -> new BlockItem(CHINESE_KNOTTING.get(), new Item.Properties()));

    // ========== 格栅灯 ==========
    public static final DeferredBlock<LampCabinetBlock> LAMP_CABINET = BLOCKS.register("lamp_cabinet",
            () -> new LampCabinetBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.GLASS).lightLevel(state -> 15)));
    public static final DeferredItem<BlockItem> LAMP_CABINET_ITEM = ITEMS.register("lamp_cabinet",
            () -> new BlockItem(LAMP_CABINET.get(), new Item.Properties()));

    // ========== 屏风 1 ==========
    public static final DeferredBlock<DecorativeBlock> CANVAS_SCREEN_1 = BLOCKS.register("canvas_screen_1",
            () -> new DecorativeBlock(BlockBehaviour.Properties.of().instabreak().noCollission().sound(SoundType.WOOL)));
    public static final DeferredItem<BlockItem> CANVAS_SCREEN_1_ITEM = ITEMS.register("canvas_screen_1",
            () -> new BlockItem(CANVAS_SCREEN_1.get(), new Item.Properties()));

    // ========== 屏风 2 ==========
    public static final DeferredBlock<DecorativeBlock> CANVAS_SCREEN_2 = BLOCKS.register("canvas_screen_2",
            () -> new DecorativeBlock(BlockBehaviour.Properties.of().instabreak().noCollission().sound(SoundType.WOOL)));
    public static final DeferredItem<BlockItem> CANVAS_SCREEN_2_ITEM = ITEMS.register("canvas_screen_2",
            () -> new BlockItem(CANVAS_SCREEN_2.get(), new Item.Properties()));

    // ========== 香炉 ==========
    public static final DeferredBlock<DecorativeBlock> INCENSE_BURNER = BLOCKS.register("incense_burner",
            () -> new DecorativeBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.STONE),
                    Block.box(3, 0, 3, 13, 10, 13)));
    public static final DeferredItem<BlockItem> INCENSE_BURNER_ITEM = ITEMS.register("incense_burner",
            () -> new BlockItem(INCENSE_BURNER.get(), new Item.Properties()));

    // ========== 木板挂灯 ==========
    public static final DeferredBlock<DecorativeBlock> PLANK_HANGING_LIGHT = BLOCKS.register("plank_hanging_light",
            () -> new DecorativeBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.GLASS).lightLevel(state -> 15),
                    Block.box(3, 0, 3, 13, 16, 13)));
    public static final DeferredItem<BlockItem> PLANK_HANGING_LIGHT_ITEM = ITEMS.register("plank_hanging_light",
            () -> new BlockItem(PLANK_HANGING_LIGHT.get(), new Item.Properties()));

    // ========== 红灯笼 ==========
    public static final DeferredBlock<DecorativeBlock> REDLANTERN = BLOCKS.register("redlantern",
            () -> new DecorativeBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.GLASS).lightLevel(state -> 15),
                    Block.box(1, 0, 1, 15, 16, 15)));
    public static final DeferredItem<BlockItem> REDLANTERN_ITEM = ITEMS.register("redlantern",
            () -> new BlockItem(REDLANTERN.get(), new Item.Properties()));

    // ========== 金灯笼 ==========
    public static final DeferredBlock<DecorativeBlock> GOLDLANTERN = BLOCKS.register("goldlantern",
            () -> new DecorativeBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.GLASS).lightLevel(state -> 15),
                    Block.box(1, 0, 1, 15, 16, 15)));
    public static final DeferredItem<BlockItem> GOLDLANTERN_ITEM = ITEMS.register("goldlantern",
            () -> new BlockItem(GOLDLANTERN.get(), new Item.Properties()));

    // ========== 石狮子 ==========
    public static final DeferredBlock<DecorativeBlock> STONE_LION = BLOCKS.register("stone_lion",
            () -> new DecorativeBlock(BlockBehaviour.Properties.of().strength(2.0f).sound(SoundType.STONE),
                    Block.box(0, 0, 0, 16, 29, 16)));
    public static final DeferredItem<BlockItem> STONE_LION_ITEM = ITEMS.register("stone_lion",
            () -> new BlockItem(STONE_LION.get(), new Item.Properties()));

    // ========== 窗纸（实体）==========
    public static final DeferredHolder<EntityType<?>, EntityType<WindowPaperEntity>> WINDOW_PAPER_ENTITY =
            ENTITY_TYPES.register("windowpaper_1",
                    () -> EntityType.Builder.<WindowPaperEntity>of(WindowPaperEntity::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f).clientTrackingRange(64).updateInterval(20).build("windowpaper_1"));
    public static final DeferredItem<Item> WINDOW_PAPER_ITEM = ITEMS.register("windowpaper_1",
            () -> new WindowPaperItem(new Item.Properties()));

    // ========== 摔炮实体 ==========
    public static final DeferredHolder<EntityType<?>, EntityType<FirecrackerEntity>> FIRECRACKER_ENTITY =
            ENTITY_TYPES.register("firecracker",
                    () -> EntityType.Builder.<FirecrackerEntity>of(FirecrackerEntity::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10).build("firecracker"));

    // ========== 投掷水果实体（榴莲/椰子）==========
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownFruitEntity>> THROWN_FRUIT_ENTITY =
            ENTITY_TYPES.register("thrown_fruit",
                    () -> EntityType.Builder.<ThrownFruitEntity>of(ThrownFruitEntity::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10).build("thrown_fruit"));

    // ========== 对联 1（横幅）==========
    public static final DeferredBlock<CoupletBlock> ANTITHETICAL_COUPLET_1 = BLOCKS.register("antithetical_couplet_1",
            () -> new CoupletBlock(BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.WOOL).noCollission()));
    public static final DeferredItem<BlockItem> ANTITHETICAL_COUPLET_1_ITEM = ITEMS.register("antithetical_couplet_1",
            () -> new CoupletBlockItem(ANTITHETICAL_COUPLET_1.get(), new Item.Properties()));

    // ========== 对联 2（竖联）==========
    public static final DeferredBlock<CoupletBlock> ANTITHETICAL_COUPLET_2 = BLOCKS.register("antithetical_couplet_2",
            () -> new CoupletBlock(BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.WOOL).noCollission()));
    public static final DeferredItem<BlockItem> ANTITHETICAL_COUPLET_2_ITEM = ITEMS.register("antithetical_couplet_2",
            () -> new CoupletBlockItem(ANTITHETICAL_COUPLET_2.get(), new Item.Properties()));

    // ========== 对联方块实体 ==========
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CoupletBlockEntity>> COUPLET_ENTITY =
            BLOCK_ENTITIES.register("couplet_entity",
                    () -> BlockEntityType.Builder.of(CoupletBlockEntity::new,
                            ANTITHETICAL_COUPLET_1.get(), ANTITHETICAL_COUPLET_2.get()).build(null));

    // ========== expand: 农业 ==========
    public static final DeferredItem<Item> GREENMANGO = ITEMS.register("greenmango", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> AGRAPE = ITEMS.register("agrape", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> AGREENGRAPE = ITEMS.register("agreengrape", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CAULIFLOWER = ITEMS.register("cauliflower", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GARLICPEDICEL = ITEMS.register("garlicpedicel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PEANUT = ITEMS.register("peanut", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POMEGRANATE_SEED = ITEMS.register("pomegranate_seed", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWHALFOFCHICKENLEG = ITEMS.register("rawhalfofchickenleg", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAWSHEEPTAIL = ITEMS.register("rawsheeptail", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> REDREPPER = ITEMS.register("redrepper", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SACLLION = ITEMS.register("sacllion", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SHEEPMELON = ITEMS.register("sheepmelon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SPINACH = ITEMS.register("spinach", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SWEETPOTATO = ITEMS.register("sweetpotato", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> YELLOWPEACH = ITEMS.register("yellowpeach", () -> new Item(new Item.Properties()));

    // ========== expand: 食材 ==========
    public static final DeferredItem<Item> DRYANISEED = ITEMS.register("dryaniseed", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRYCINNAMON = ITEMS.register("drycinnamon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRYCOFFEEBEAN = ITEMS.register("drycoffeebean", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRYLILAC = ITEMS.register("drylilac", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRYNUTMEG = ITEMS.register("drynutmeg", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRYSCALION = ITEMS.register("dryscalion", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRYSICHUANPEPPER = ITEMS.register("drysichuanpepper", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDBROCCOIL = ITEMS.register("dicedbroccoil", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DICEDCAULIFLOWER = ITEMS.register("dicedcauliflower", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EGGSHELL = ITEMS.register("eggshell", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> OATGRAIN = ITEMS.register("oatgrain", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> POLISHEDGLUTINOUSRICE_2 = ITEMS.register("polishedglutinousrice_2", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAW_BAOZI = ITEMS.register("raw_baozi", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAW_DUMPLING = ITEMS.register("raw_dumpling", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAW_WONTON = ITEMS.register("raw_wonton", () -> new Item(new Item.Properties()));

    // ========== expand: 食物 ==========
    public static final DeferredItem<Item> BAISED_TOFU_SLICES = ITEMS.register("baised_tofu_slices", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> BOILEDRICEFLOURNOODLES = ITEMS.register("boiledriceflournoodles", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> BONESOUP = ITEMS.register("bonesoup", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> BUCKWHEATRICE = ITEMS.register("buckwheatrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> CHOCOLATE = ITEMS.register("chocolate", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> CONGEEWITH_MINCE_PORKAND_PRESERVED_EGG = ITEMS.register("congeewith_minced_porkand_preserved_egg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> COOKEDCHICKENBREAST = ITEMS.register("cookedchickenbreast", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> COOKEDCHICKENLEAN = ITEMS.register("cookedchickenlean", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> COOKEDCORNBATTER = ITEMS.register("cookedcornbatter", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> COOKEDCRISPYPORK = ITEMS.register("cookedcrispypork", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.7f).alwaysEdible().build())));
    public static final DeferredItem<Item> COOKEDFLAKEBEEF = ITEMS.register("cookedflakebeef", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.7f).alwaysEdible().build())));
    public static final DeferredItem<Item> COOKEDHALFOFCHCIKENLEG = ITEMS.register("cookedhalfofchcikenleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> COOKEDPIGSTREAKMEAT = ITEMS.register("cookedpigstreakmeat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.7f).alwaysEdible().build())));
    public static final DeferredItem<Item> DRIED_TOFU_SKIN = ITEMS.register("dried_tofu_skin", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> BEANSPROUT = ITEMS.register("beansprout", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EATENPINEAPPLE = ITEMS.register("eatenpineapple", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.2f).alwaysEdible().build())));
    public static final DeferredItem<Item> EGGBISCUIT = ITEMS.register("eggbiscuit", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> EGGPANCAKE = ITEMS.register("eggpancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> EGGTART = ITEMS.register("eggtart", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> EIGHTTREASURECONGEE = ITEMS.register("eighttreasurecongee", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> ELECTROLYTEBEVERAGE = ITEMS.register("electrolytebeverage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.2f).alwaysEdible().build())));
    public static final DeferredItem<Item> FANHUAROLL = ITEMS.register("fanhuaroll", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRIEDCHICKENCHOP = ITEMS.register("friedchickenchop", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRIEDCHICKENCORN = ITEMS.register("friedchickencorn", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRIEDCHICKENLEG = ITEMS.register("friedchickenleg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRIEDCHICKENWING = ITEMS.register("friedchickenwing", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> FRIEDSAUSAGE = ITEMS.register("friedsausage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> GRAPEWINE = ITEMS.register("grapewine", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.2f).alwaysEdible().build())));
    public static final DeferredItem<Item> GREENBEANPORRIDGE = ITEMS.register("greenbeanporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> HANDGRABBEDPANCAKE = ITEMS.register("handgrabbedpancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> HEALTHLOQUATCREAM = ITEMS.register("healthloquatcream", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.2f).alwaysEdible().build())));
    public static final DeferredItem<Item> HEALTHPEANUTMILK = ITEMS.register("healthpeanutmilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> HEALTHWALNUTDEW = ITEMS.register("healthwalnutdew", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> ICEDBLACKTEA = ITEMS.register("icedblacktea", () -> new com.flavor_immersed_daily.item.DrinkItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> KAOLIANGPORRIDGE = ITEMS.register("kaoliangporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> KAOLIANGRICE = ITEMS.register("kaoliangrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> LABAPORRIDGE = ITEMS.register("labaporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> MILLIETPORRIDGE = ITEMS.register("millietporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> MILLIETRICE = ITEMS.register("millietrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> NOILEDRICENOODLE = ITEMS.register("noiledricenoodle", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> OAKPORRIDGE = ITEMS.register("oakporridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> OAKRICE = ITEMS.register("oakrice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> OMELETTE = ITEMS.register("omelette", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> PANADAPANCAKE = ITEMS.register("panadapancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> PEAFLOURCAKE = ITEMS.register("peaflourcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> POTATOCHIPS = ITEMS.register("potatochips", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> REDBEANEGGTART = ITEMS.register("redbeaneggtart", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> REDBEANSTUFFINGDORAYAKI = ITEMS.register("redbeanstuffingdorayaki", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> RICE = ITEMS.register("rice", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> RICE_PORRIDGE = ITEMS.register("rice_porridge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> RICEPASTESOUP = ITEMS.register("ricepastesoup", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> ROASTCHICKENFORK = ITEMS.register("roastchickenfork", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.6f).alwaysEdible().build())));
    public static final DeferredItem<Item> ROASTEDSWEETPOTATO = ITEMS.register("roastedsweetpotato", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> SHELLEDBOILEDEGG = ITEMS.register("shelledboiledegg", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> SHOU_KAI_XIN_GUO = ITEMS.register("shou_kai_xin_guo", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> SMALLMICEDMEATCAKE = ITEMS.register("smallmicedmeatcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEAMEDSALTORANGE = ITEMS.register("steamedsaltorange", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> STEAMEDVERMICELLIROLL_0 = ITEMS.register("steamedvermicelliroll_0", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> STRAWBERRYCAKEROLL = ITEMS.register("strawberrycakeroll", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> SWEETMILK = ITEMS.register("sweetmilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> TOFU_SAUSAGE = ITEMS.register("tofu_sausage", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> TOFU_STICKS = ITEMS.register("tofu_sticks", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));
    public static final DeferredItem<Item> WALNUTCAKE = ITEMS.register("walnutcake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> WALNUTSHORTBREAD = ITEMS.register("walnutshortbread", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).alwaysEdible().build())));
    public static final DeferredItem<Item> WHEATMILK = ITEMS.register("wheatmilk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build())));
    public static final DeferredItem<Item> YUBA = ITEMS.register("yuba", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).alwaysEdible().build())));

    // ========== 新注册食物 ==========
    public static final DeferredItem<Item> TOUFU = ITEMS.register("toufu", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.8f).alwaysEdible().build())));
    public static final DeferredItem<Item> EGGCAKE = ITEMS.register("eggcake", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.7f).alwaysEdible().build())));
    public static final DeferredItem<Item> GOLDRICECAKEMAX = ITEMS.register("goldricecakemax", () -> new Item(new Item.Properties()
            .rarity(Rarity.EPIC)
            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
            .food(new FoodProperties.Builder()
                    .nutrition(4).saturationModifier(9.6f)
                    .effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 1), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3), 1.0f)
                    .alwaysEdible().build())));
    public static final DeferredItem<Item> GOLDENGRAPEMAX = ITEMS.register("goldengrapemax", () -> new Item(new Item.Properties()
            .rarity(Rarity.EPIC)
            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
            .food(new FoodProperties.Builder()
                    .nutrition(4).saturationModifier(9.6f)
                    .effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 1), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3), 1.0f)
                    .alwaysEdible().build())));

    // ========== 创造模式标签页 ==========
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AGRICULTURE_TAB = CREATIVE_MODE_TABS.register("agriculture", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.flavor_immersed_daily.agriculture"))
            .withTabsBefore(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, "food"))
            .icon(() -> GREENAPPLE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                // ===== 果实杂物 =====
                output.accept(TEMPERATEWILDFRUIT.get());
                output.accept(TROPICALWILD_FRUIT.get());
                output.accept(WILDFLOWERANDLEAF.get());
                output.accept(WILDFRUITINCOLDZONE.get());
                output.accept(WILDGRAINPLANT.get());
                output.accept(WILDMUSHROOMPLANT.get());
                output.accept(WILDSEEDPLANT.get());
                output.accept(WILDTUBERPLANTS.get());
                output.accept(APIECEOFBANANA.get()); output.accept(BANANA.get()); output.accept(PULLEDBANANA_2.get());
                output.accept(ORANGE.get()); output.accept(BLOODORANGE.get()); output.accept(TANGERINE.get()); output.accept(TANGERINE_1.get()); output.accept(UGLYORANGE.get()); output.accept(LEMON.get());
                output.accept(HAMIMELON.get()); output.accept(SWEETMELON.get()); output.accept(SWEETMELON_1.get());
                output.accept(COCONUT.get()); output.accept(COCONUTMEAT.get()); output.accept(COCONUT_SHELL.get());
                output.accept(DURIAN.get()); output.accept(DURIANMEAT.get()); output.accept(DURIANSHELLHAT.get());
                output.accept(BLUEBERRY.get()); output.accept(GRAPE.get()); output.accept(GREENGRAPE.get()); output.accept(MULBERRY.get()); output.accept(STRAWBERRY.get());
                output.accept(APRICOT.get()); output.accept(CHERRY.get()); output.accept(GREENPLUM.get()); output.accept(HONEYPEACH.get()); output.accept(LIFEPEACH.get()); output.accept(NECTARINE.get()); output.accept(PLUM.get()); output.accept(WINTERJUJUBE.get());
                output.accept(CARAMBOLA.get()); output.accept(DRAGONFRUIT.get()); output.accept(KIWIFRUIT.get()); output.accept(LYCHEE.get()); output.accept(MANGO.get()); output.accept(MANGOSTEEN.get()); output.accept(PAWPAW.get()); output.accept(PINEAPPLE.get());
                output.accept(GREENAPPLE.get()); output.accept(HAWTHORN.get()); output.accept(LOQUAT.get()); output.accept(PEAR.get()); output.accept(POMEGRANATE.get());
                output.accept(PISTACHIONUT.get()); output.accept(REDDATE.get()); output.accept(WALNUT.get());
                output.accept(DRY_PISTACHIONUT.get()); output.accept(PISTACHIONUT_0.get()); output.accept(PISTACHIONUTSWITHOPENSHELLS.get()); output.accept(WALNUTKINNEL.get());
                output.accept(CHINESE_LEAVES.get()); // 添加白菜
                output.accept(CHINESE_LEAVES_SEEDS.get()); // 添加白菜种子
                output.accept(RADISH.get()); // 添加萝卜
                output.accept(RADISHSEED.get()); // 添加萝卜种子
                output.accept(WHITEMUSHROOM.get()); // 添加白蘑菇
                output.accept(WHITE_MUSHROOM_SEED.get()); // 添加白蘑菇种子
                output.accept(BLACKFUNGUS.get()); // 添加木耳
                output.accept(BLACKFUNGSSEED.get()); // 添加木耳种子
                output.accept(DRYBLACKFUNGUS.get()); // 添加干木耳
                output.accept(PLEUROTUSERYNGII.get()); // 添加杏鲍菇
                output.accept(PLEUROTUSSEED.get()); // 添加杏鲍菇种子
                output.accept(ENOKIMUSHROOM.get()); // 添加金针菇
                output.accept(ENOKIMUSHROOMSEED.get()); // 添加金针菇种子
                output.accept(TREMELLA.get()); // 添加银耳
                output.accept(TREMELLASEED.get()); // 添加银耳种子
                output.accept(FRAGRANTMUSHROOM.get()); // 添加香菇
                output.accept(FRAGRANTSEED.get()); // 添加香菇种子
                output.accept(DRYFRAGRANTMUSHROOM.get()); // 添加干香菇
                output.accept(BLUEBERRYSEED.get()); // 添加蓝莓种子
                output.accept(DRAGONFRUITSEED.get()); // 添加火龙果种子
                output.accept(GREENTEALEAVESSEED.get()); // 添加绿茶种子
                output.accept(GREENTEALEAVES.get()); // 添加绿茶叶
                output.accept(DRYGREENTEA.get()); // 添加干绿茶
                output.accept(HAMIMELONSEED.get()); // 添加哈密瓜种子
                output.accept(PINEAPPLESEED.get()); // 添加菠萝种子
                output.accept(RED_TEA_SEED.get()); // 添加红茶种子
                output.accept(REDTEALEAVES.get()); // 添加红茶叶
                output.accept(DRYREDTEA.get()); // 添加干红茶
                output.accept(STRAWBERRYSEED.get()); // 添加草莓种子
                output.accept(LOTUSROOTSEED.get()); // 添加莲藕种子
                output.accept(LOTUSROOT.get()); // 添加莲藕
                output.accept(GLUTINOUSSEEDS.get()); // 添加糯米种子
                // ===== 野生采集物 =====
                output.accept(POLISHEDGLUTINOUSRICE_2.get()); // 添加精制糯米
                output.accept(PADDYSEEDS.get()); // 添加水稻种子
                output.accept(PADDYGRAIN.get()); // 添加稻谷
                output.accept(ANISEED_0.get()); // 添加八角
                output.accept(KAOLIANGGRAIN.get()); // 添加高粱
                output.accept(KAOLIANG_SEED.get()); // 添加高粱种子
                output.accept(BROCCOIL.get()); // 添加西蓝花
                output.accept(BROCCOILSEED.get()); // 添加西蓝花种子
                output.accept(BUCKWHEAT.get()); // 添加荞麦
                output.accept(BUCKWHEATSEED.get()); // 添加荞麦种子
                output.accept(CABBAGE.get()); // 添加卷心菜
                output.accept(CABBAGESEED.get()); // 添加卷心菜种子
                output.accept(CASSAVA.get()); // 添加木薯
                output.accept(CASSAVASEEDS.get()); // 添加木薯种子
                output.accept(CELERY.get()); // 添加芹菜
                output.accept(CELERYSEED.get()); // 添加芹菜种子
                output.accept(CHINESECHIVES.get()); // 添加韭菜
                output.accept(CHINESECHIVESSEED.get()); // 添加韭菜种子
                output.accept(CHINESEYAM.get()); // 添加山药
                output.accept(CHINESEYAMSEED.get()); // 添加山药种子
                output.accept(COFFEEBEANSEED.get()); // 添加咖啡豆种子
                output.accept(CORN.get()); // 添加玉米
                output.accept(CORNSEED.get()); // 添加玉米种子
                output.accept(CUMIN.get()); // 添加孜然
                output.accept(CUMINSEED.get()); // 添加孜然种子
                output.accept(FENNEL.get()); // 添加茴香
                output.accept(FENNELSEEDSTATES.get()); // 添加茴香种子
                output.accept(GARLIC.get()); // 添加大蒜
                output.accept(GARLICSEED.get()); // 添加大蒜种子
                output.accept(GINGER.get()); // 添加生姜
                output.accept(GINGER_SEED.get()); // 添加生姜种子
                output.accept(GREENPEPPER.get()); // 添加青尖椒
                output.accept(GREENPEPPERSEEDS.get()); // 添加青尖椒种子
                output.accept(GUMBO.get()); // 添加秋葵
                output.accept(GUMBOSEED.get()); // 添加秋葵种子
                output.accept(MILLETGRAIN_GRAIN.get()); // 添加小米
                output.accept(MILLET.get()); // 添加小米种子
                output.accept(MUNGBEAN.get()); // 添加绿豆
                output.accept(MUNGBEANPLANT.get()); // 添加绿豆种子
                output.accept(MUSTARD.get()); // 添加芥末
                output.accept(MUSTRAD_SEED.get()); // 添加芥末种子
                output.accept(NUTMEGSEED.get()); // 添加肉豆蔻种子
                output.accept(OAT.get()); // 添加燕麦
                output.accept(OATSEED.get()); // 添加燕麦种子
                output.accept(OILSEEDRAPE.get()); // 添加油菜
                output.accept(OILRAPESEED.get()); // 添加油菜种子
                output.accept(ONION.get()); // 添加洋葱
                output.accept(ONIONSEED.get()); // 添加洋葱种子
                output.accept(PEA.get()); // 添加豌豆
                output.accept(PEASEED.get()); // 添加豌豆种子
                output.accept(PURPLESWEETPOTATO.get()); // 添加紫薯
                output.accept(PUPLESWEETPOTATOSEED.get()); // 添加紫薯种子
                output.accept(SESAME.get()); // 添加芝麻
                output.accept(SESAMESEED.get()); // 添加芝麻种子
                output.accept(SOYBEAN.get()); // 添加黄豆
                output.accept(SOY_BEAN_SEED.get()); // 添加黄豆种子
                output.accept(SWEETGREENPEPPER.get()); // 添加青椒
                output.accept(SWEETGREENPEPPERSEED.get()); // 添加青椒种子
                output.accept(ZUCCHINI.get()); // 添加西葫芦
                output.accept(ZUCCHINISEED.get()); // 添加西葫芦种子
                output.accept(SPINACH_SEED.get()); // 添加菠菜种子
                output.accept(CAULIFLOWER_SEED.get()); // 添加菜花种子
                output.accept(SCALLION_SEED.get()); // 添加葱种子
                output.accept(LILAC_SEED.get()); // 添加丁香（丁香种子即产物）
                output.accept(RED_BEAN_BLOCK.get()); // 添加红豆（红豆种子即产物）
                output.accept(RED_PEPPER_SEED.get()); // 添加红尖椒种子
                output.accept(SWEET_POTATO_SEED.get()); // 添加红薯种子
                output.accept(SI_CHUAN_PEPPER_SEED.get()); // 添加花椒（花椒种子即产物）
                output.accept(PEA_NUT_SEED.get()); // 添加花生种子
                // ===== 所有树种原木 =====
                output.accept(SOLARWOOD_LOG_ITEM.get());
                output.accept(ORCHARD_HEARTWOOD_LOG_ITEM.get());
                output.accept(STONEBARK_LOG_ITEM.get());
                output.accept(VINEHEART_TIMBER_LOG_ITEM.get());
                output.accept(VERDANT_GRACE_LOG_ITEM.get());
                // ===== 所有树叶 =====
                output.accept(SOLARWOOD_LEAVES_ITEM.get());
                output.accept(ORCHARD_HEARTWOOD_LEAVES_ITEM.get());
                output.accept(STONEBARK_LEAVES_ITEM.get());
                output.accept(VINEHEART_TIMBER_LEAVES_ITEM.get());
                output.accept(VERDANT_GRACE_LEAVES_ITEM.get());
                // ===== 所有果实树苗 =====
                output.accept(PLUM_SAPLING_ITEM.get());
                output.accept(APRICOT_SAPLING_ITEM.get());
                output.accept(CHERRY_SAPLING_ITEM.get());
                output.accept(GREENPLUM_SAPLING_ITEM.get());
                output.accept(HAWTHORN_SAPLING_ITEM.get());
                output.accept(LOQUAT_SAPLING_ITEM.get());
                output.accept(POMEGRANATE_SAPLING_ITEM.get());
                output.accept(CARAMBOLA_SAPLING_ITEM.get());
                output.accept(DURIAN_SAPLING_ITEM.get());
                output.accept(LEMON_SAPLING_ITEM.get());
                output.accept(LYCHEE_SAPLING_ITEM.get());
                output.accept(MANGO_SAPLING_ITEM.get());
                output.accept(PAWPAW_SAPLING_ITEM.get());
                output.accept(TANGERINE_SAPLING_ITEM.get());
                output.accept(ORANGE_SAPLING_ITEM.get());
                output.accept(APPLE_SAPLING_ITEM.get());
                output.accept(HONEYPEACH_SAPLING_ITEM.get());
                output.accept(NECTARINE_SAPLING_ITEM.get());
                output.accept(PEAR_SAPLING_ITEM.get());
                output.accept(SWEETMELON_SAPLING_ITEM.get());
                output.accept(PISTACHIONUT_SAPLING_ITEM.get());
                output.accept(REDDATE_SAPLING_ITEM.get());
                output.accept(WALNUT_SAPLING_ITEM.get());
                output.accept(WINTERJUJUBE_SAPLING_ITEM.get());
                output.accept(KIWIFRUIT_SAPLING_ITEM.get());
                output.accept(MANGOSTEEN_SAPLING_ITEM.get());
                output.accept(MULBERRY_SAPLING_ITEM.get());
                // ===== 椰子树 =====
                output.accept(COCONUT_SAPLING_ITEM.get());
                // ===== 香蕉树 =====
                output.accept(BANANA_SAPLING_ITEM.get());
                output.accept(BANANAWOOD_ITEM.get());
                output.accept(RAWBANANA_ITEM.get());
                // ===== 桂皮树 =====
                output.accept(CINNAMON.get());
                output.accept(CINNAMON_SAPLING_ITEM.get());
                output.accept(CINNAMONWOOD_ITEM.get());
                // ===== 所有果实结果树叶 =====
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_ITEM.get());
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_APRICOT_ITEM.get());
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_CHERRY_ITEM.get());
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_GREENPLUM_ITEM.get());
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_HAWTHORN_ITEM.get());
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_LOQUAT_ITEM.get());
                output.accept(VERDANT_GRACE_FRUITING_LEAVES_POMEGRANATE_ITEM.get());
                output.accept(CARAMBOLEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(DURIANLEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(LEMONLEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(LYCHEELEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(MANGOLEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(PAWPAWLEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(TANGERINELEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(ORANGELEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(APPLELEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(HONEYPEACHLEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(NECTARINELEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(PEARLEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(SWEETMELONLEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(PISTACHIONUTLEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(REDDATELEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(WALNUTLEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(WINTERJUJUBELEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(KIWIFRUITSSLEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(MANGOSTEENLEAVE_FRUITING_LEAVES_ITEM.get());
                output.accept(MULBERRYLEAVE_FRUITING_LEAVES_ITEM.get());
                // ===== 工具 =====
                output.accept(TRELLIS_ITEM.get());
                output.accept(GRAPESEED.get());
                // ===== 爬架作物 =====
                output.accept(CUCUMBERSEEDS.get()); output.accept(CUCUMBER.get());
                output.accept(WAX_GOURD_SEED_BLOCK.get()); output.accept(WAXGOURD.get());
                output.accept(KIDNEYBEANSEED.get()); output.accept(KIDNEYBEAN.get());
                output.accept(AUBERGINESEEDBLOCK.get()); output.accept(AUBERGINE.get());
                output.accept(TOMATOSEED.get()); output.accept(TOMATO.get());
                output.accept(COWPEABEANSEED.get()); output.accept(COWPEA.get());
                output.accept(GREENGRAESEED.get()); output.accept(GREENGRAPE.get());
                output.accept(LOOFAHSEED.get()); output.accept(LOOFAH.get());
                // ===== 生肉类 =====
                output.accept(DEADCATTLE.get());
                output.accept(DEADSHEEP.get());
                output.accept(DEADPIG.get());
                output.accept(DEADCHICKEN.get());
                output.accept(RAWCATTLEBLOOD.get()); output.accept(RAWCATTLEFACE.get()); output.accept(RAWCATTLEJOINT.get()); output.accept(RAWCATTLESTOMACH.get());
                output.accept(RAWCATTLELUNG.get()); output.accept(RAWCATTLELIVER.get()); output.accept(RAWCATTLEINTESTINE.get()); output.accept(RAWCATTLEHEART.get());
                output.accept(RAWCATTLEFAT.get()); output.accept(RAWDICEDCATTLEMEAT.get()); output.accept(RAWSNOWFLAKEBEEF.get()); output.accept(RAWCATTLELEG.get());
                output.accept(RAWCATTLEFEET.get()); output.accept(RAWCATTLETENDON.get()); output.accept(BULLHORN.get()); output.accept(BOVINEBONE.get());
                output.accept(ANIMALSKULL.get());
                output.accept(RAWPIGBLOOD.get()); output.accept(RAWDICEDPIGMEAT.get()); output.accept(RAWDICEDPIGMEAT_2.get()); output.accept(RAWPIGHEAD.get());
                output.accept(RAWPIGEAR.get()); output.accept(RAWPIGNOSE.get()); output.accept(RAWPIGTAIL.get()); output.accept(RAWPIGSKIN.get());
                output.accept(RAWPIGSTREAKYPORK.get()); output.accept(RAWPIGTENDERLOIN.get()); output.accept(RAWPIGSPARERIB.get()); output.accept(RAWPIGLEG.get());
                output.accept(RAWPIGFEET.get()); output.accept(RAWPIGFAT.get()); output.accept(RAWPIGSTOMACH.get()); output.accept(RAWPIGLUNG.get());
                output.accept(RAWPIGLIVER.get()); output.accept(RAWPIGKIDNEY.get()); output.accept(RAWPIGHEART.get()); output.accept(RAWPIGINTESTINE.get());
                output.accept(RAWPIGCEREBRUM.get());
                output.accept(RAWSHEEPBLOOD.get()); output.accept(RAWDICEDSHEEPMEAT.get()); output.accept(RAWSHEEPFACE.get()); output.accept(RAWSHEEPTAILFAT.get());
                output.accept(RAWSHEEPFAT.get()); output.accept(RAWSHEEPEYE.get()); output.accept(RAWSHEEPSTOMACH.get()); output.accept(RAWSHEEPLIVER.get());
                output.accept(RAWSHEEPINTESTINE.get()); output.accept(RAWSHEEPKIDNEY.get()); output.accept(RAWSHEEPHEART.get()); output.accept(RAWSHEEPSPARERIB.get());
                output.accept(RAWSHEEPSPINE.get()); output.accept(RAWSHEEPFEET.get()); output.accept(RAWSHEEPLEG.get()); output.accept(SHEEPBREAD.get());
                output.accept(RAWCHICKENBLOOD.get()); output.accept(RAWCHICKENHEAD.get()); output.accept(RAWCHICKENNECK.get()); output.accept(RAWCHICKENPIECEPIECE.get());
                output.accept(RAWCHICKENBREAST.get()); output.accept(RAWCHICKENPIECE.get()); output.accept(RAWCHICKENWING.get()); output.accept(RAWCHICKENWINGTIP.get());
                output.accept(RAWCHICKENFEET.get()); output.accept(RAWCHICKENLEGWITHLEG.get()); output.accept(RAWCHICKENLEG.get()); output.accept(RAWCHICKENLEAN.get());
                output.accept(RAWCHICKENFORK.get()); output.accept(RAWCHICKENASS.get()); output.accept(RAWCHICKENHEART.get()); output.accept(RAWCHICKENLIVER.get());
                output.accept(RAWCHICKENGIZZARD.get()); output.accept(CHICKENWITHOUTFEATHER.get()); output.accept(CHICKENWITHOUTBLOOD.get());
                output.accept(GREENMANGO.get());
                output.accept(AGRAPE.get()); output.accept(AGREENGRAPE.get()); output.accept(CAULIFLOWER.get()); output.accept(GARLICPEDICEL.get());
                output.accept(PEANUT.get()); output.accept(POMEGRANATE_SEED.get()); output.accept(RAWHALFOFCHICKENLEG.get()); output.accept(RAWSHEEPTAIL.get());
                output.accept(REDREPPER.get()); output.accept(SACLLION.get()); output.accept(SHEEPMELON.get()); output.accept(SPINACH.get());
                output.accept(SWEETPOTATO.get()); output.accept(YELLOWPEACH.get());
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FOOD_TAB = CREATIVE_MODE_TABS.register("food", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.flavor_immersed_daily.food"))
            .withTabsBefore(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, "ingredient"))
            .icon(() -> STIRFRIEDBOILEDPORKSLICESINHOTSAUCE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                // ===== 菜肴（最前面） =====
                output.accept(DRAWNEGGPLANT.get());
                // 可放置菜肴
                output.accept(LINYIFRIEDCHICKEN.get()); output.accept(MEATBALLSOUP.get()); output.accept(PRESERVEDEGGSALAD.get()); output.accept(TOMATOSALAD.get());
                output.accept(STEAMED_CHICKENWITH_CHILI_SAUCE.get()); output.accept(COLACHICKENWINGS.get()); output.accept(FOURJOYMEATBALLS.get()); output.accept(STIRFRIEDBOILEDPORKSLICESINHOTSAUCE.get());
                output.accept(SAUTEED_POTATO_GREEN_PEPPER_EGGPLANT.get()); output.accept(FRIEDMEATWITHCUMINONION.get()); output.accept(KUNGPAOCHICKEN.get()); output.accept(STIRFRIEDSTRINGBEANS.get());
                output.accept(MIXEDCOLDDISHES.get()); output.accept(JAPANESEBRAISEDTOFU.get()); output.accept(SCRAMBLEDEGGSWITHFUNGUSANDCUCUMBER.get()); output.accept(PLEUROTUSERYNGIIWITHSALTANDPEPPER.get());
                output.accept(POACHED_SPICY_SLICESOF_PORK.get()); output.accept(SLICED_FISHIN_HOT_CHILI_OIL.get()); output.accept(SAUTEEDMUSHROOMSWITHRAPESEED.get()); output.accept(STEAMEDFISH.get());
                output.accept(FRIEDCOWPEA.get()); output.accept(FRIEDSPICYCHICKEN.get()); output.accept(BOILED_CHICKENWITH_SAUCE.get()); output.accept(STEWEDPORKWITHBROWNSAUCE.get());
                output.accept(FRIEDLIVERTIPWITHSPINACH.get()); output.accept(PINEAPPLE_SWEETAND_SOUR_PORK.get()); output.accept(CHICKENWITH_SCALLION_OIL.get()); output.accept(SALTEDEGGYOLKFRIEDCAULIFLOWER.get());
                output.accept(ZUCCHINISNACKMEAT.get()); output.accept(BOILED_FISHWITH_PICKLED_CABBAGEAND_CHILI.get()); output.accept(FRIEDSHREDDEDPORKWITHSWEETANDSOURSAUCE.get()); output.accept(SPICYTOFU.get());
                output.accept(BEANWITHSESAMESAUCE.get()); output.accept(SPICYCABBAGE.get());
                // 简单菜肴
                output.accept(BAKEDWHITEMUSHROOMSWITHCREAM.get()); output.accept(BRAISEDBEANSPROUTSWITHVERMICELLI.get()); output.accept(CANTONESERICEROLLS.get()); output.accept(CONGEEWITH_MINCED_PORKAND_PRESERVED_EGG.get());
                output.accept(CURRYSTEWEDCHICKEN.get()); output.accept(GUMBOSOUP.get()); output.accept(REDDATESANDTREMELLAPORRIDGE.get()); output.accept(SA.get());
                output.accept(SCRAMBLEDEGGWITHTOMATO.get()); output.accept(SHEEPGIBLETSSOUP.get()); output.accept(STEAMEDEGGCUSTARD.get()); output.accept(STEWEDCHICKENSOUPWITHMUSHROOMS.get());
                output.accept(STEWEDCHICKENWITHWAXGOURD.get()); output.accept(STEWEDEGGSWITHLOOFAH.get()); output.accept(STEWEDPORKOFFAL.get()); output.accept(WINTERJUJUBEANDWAXGOURDSOUP.get());
                // ===== 雪糕 =====
                output.accept(APPLEPOPSICLE.get());
                output.accept(APRICOTPOPSICLE.get());
                output.accept(BANANAPOPSICLE.get());
                output.accept(BLUEBERRYPOPSICLE.get());
                output.accept(CARAMBOLAPOPSICLE.get());
                output.accept(CHERRYPOPSICLE.get());
                output.accept(CHOCOLATEPOPSICLE.get());
                output.accept(COCONUTPOPSICLE.get());
                output.accept(COFFEEPOPSICLE.get());
                output.accept(DRAGONFRUITPOPSICLE.get());
                output.accept(DURIANPOPSICLE.get());
                output.accept(GRAPEPOPSICLE.get());
                output.accept(GREENGRAPEPOPSICLE.get());
                output.accept(GREENPLUMPOPSICLE.get());
                output.accept(HAMIMELONPOPSICLE.get());
                output.accept(HAWTHORNPOPSICLE.get());
                output.accept(HONEYPEACHPOPSICLE.get());
                output.accept(KIWIFRUITPOPSICLE.get());
                output.accept(LEMONPOPSICLE.get());
                output.accept(LOQUATPOPSICLE.get());
                output.accept(LYCHEEPOPSICLE.get());
                output.accept(MANGOPOPSICLE.get());
                output.accept(MANGOSTEEPOPSICLE.get());
                output.accept(MULBERRYPOPSICLE.get());
                output.accept(NECTARINEPOPSICLE.get());
                output.accept(ORANGEPOPSICLE.get());
                output.accept(PAWPAWPOPSICLE.get());
                output.accept(PEARPOPSICLE.get());
                output.accept(PINEAPPLEPOPSICLE.get());
                output.accept(PLUMPOPSICLE.get());
                output.accept(POMEGRANATEPOPSICLE.get());
                output.accept(POPSICLE.get());
                output.accept(STRAWBERRYPOPSICLE.get());
                output.accept(SWEETBERRYPOPSICLE.get());
                output.accept(SWEETMELONPOPSICLE.get());
                output.accept(TANGERINEPOPSICLE.get());
                output.accept(WATERMELONPOPSICLE.get());
                output.accept(WINTERJUJUBEPOPSICLE.get());
                // 添加Ice Cream系列
                output.accept(APPLEICECREAM.get());
                output.accept(APRICOTICECREAM.get());
                output.accept(BANANAICECREAM.get());
                output.accept(BLUEBERRYICECREAM.get());
                output.accept(CARAMBOLAICECREAM.get());
                output.accept(CHERRYICECREAM.get());
                output.accept(CHOCOLATEICECREAM.get());
                output.accept(COCONUTICECREAM.get());
                output.accept(COFFEEICECREAM.get());
                output.accept(DRAGONFRUITICECREAM.get());
                output.accept(DURIANICECREAM.get());
                output.accept(GRAPEICECREAM.get());
                output.accept(GREENGRAPEICECREAM.get());
                output.accept(GREENPLUMICECREAM.get());
                output.accept(HAMIMELONICECREAM.get());
                output.accept(HAWTHORNICECREAM.get());
                output.accept(HONEYPEACHICECREAM.get());
                output.accept(KIWIFRUITICECREAM.get());
                output.accept(LEMONICECREAM.get());
                output.accept(LOQUATICECREAM.get());
                output.accept(LYCHEEICECREAM.get());
                output.accept(MANGOICECREAM.get());
                output.accept(MANGOSTEENICECREAM.get());
                output.accept(MILKICECREAM.get());
                output.accept(MULBERRYICECREAM.get());
                output.accept(NECTARINEICECREAM.get());
                output.accept(ORANGEICECREAM.get());
                output.accept(PAWPAWICECREAM.get());
                output.accept(PEARICECREAM.get());
                output.accept(PINEAPPLEICECREAM.get());
                output.accept(PLUMICECREAM.get());
                output.accept(POMEGRANATEICECREAM.get());
                output.accept(STRAWBERRYICECREAM.get());
                output.accept(SWEETBERRYICECREAM.get());
                output.accept(SWEETMELONICECREAM.get());
                output.accept(TANGERINEICECREAM.get());
                output.accept(WATERMELONICECREAM.get());
                output.accept(WINTERJUJUBEICECREAM.get());
                // ===== 果汁 =====
                output.accept(HAMIMELONJUICE.get());
                output.accept(HAWTHORNJUIVE.get());
                output.accept(DRAGONFRUIEJUICE.get());
                output.accept(CARAMBOLAJUICE.get());
                output.accept(WINTERJUJUBEJUICE.get());
                output.accept(POMEGRANATEJUICE.get());
                output.accept(SWEETBERRYJUICE.get());
                output.accept(DURIANJUICE.get());
                output.accept(LEMONJUICE.get());
                output.accept(BLUEBERRYJUICE.get());
                output.accept(SWEETMELONJUICE.get());
                output.accept(GREENGRAPEJUICE.get());
                output.accept(TANGERINEJUICE.get());
                output.accept(MANGOSTEENJUICE.get());
                output.accept(CHERRYJUICE.get());
                output.accept(STRAWBERRYJUICE.get());
                output.accept(ORANGEJUICE.get());
                output.accept(GREENPLUMJUICE.get());
                output.accept(APPLEJUICE.get());
                output.accept(HONEYPEACHJUIVE.get());
                output.accept(BANANAJUICE.get());
                output.accept(LYCHEEJUICE.get());
                output.accept(APRICOTJUICE.get());
                output.accept(GRAPEJUICE.get());
                output.accept(MULBERRYJUIVE.get());
                output.accept(PKUMJUICE.get());
                output.accept(MANGOJUICE.get());
                output.accept(WATERMELONJUICE.get());
                output.accept(NECTARINEJUICE.get());
                output.accept(PINEAPPLEJUICE.get());
                output.accept(LOQUATJUICE.get());
                output.accept(KIWIFRUITJUICE.get());
                output.accept(PEARJUICE.get());
                output.accept(PAWPAWJUICE.get());
                output.accept(COCONUTJUICE.get());
                // ===== 果酱 =====
                output.accept(PINEAPPLEJAM.get());
                output.accept(STRAWBERRYJAM.get());
                output.accept(ORANGEJAM.get());
                output.accept(WINTERJUJUBEJAM.get());
                output.accept(HAMIMELONJAM.get());
                output.accept(DRAGONFRUITJAM.get());
                output.accept(TANGERINEJAM.get());
                output.accept(BLUEBERRYJAM.get());
                output.accept(PEARJAM.get());
                output.accept(LYCHEEJAM.get());
                output.accept(PLUMJAM.get());
                output.accept(DURIANJAM.get());
                output.accept(MANGOJAM.get());
                output.accept(KIWIFRUITJAM.get());
                output.accept(PAWPAWJAM.get());
                output.accept(LEMONJAM.get());
                output.accept(LOQUATJAM.get());
                output.accept(APPLEJAM.get());
                output.accept(GRAPEJAM.get());
                output.accept(GREEMPLUMJAM.get());
                output.accept(GREENGRAPEJAM.get());
                output.accept(MULBERRYJAM.get());
                output.accept(HAWTHORNJAM.get());
                output.accept(MANGOSTEENJAM.get());
                output.accept(POMEGRANATEJAM.get());
                output.accept(HONEYPEACHJAM.get());
                output.accept(SWEETMELONJAM.get());
                output.accept(SWEETBERRYJAM.get());
                output.accept(WATERMELONJAM.get());
                output.accept(BANANAJAM.get());
                output.accept(APRICOTJAM.get());
                output.accept(CARAMBOLAJAM.get());
                output.accept(COCONUTJAM.get());
                output.accept(CHERRYJAM.get());
                output.accept(NECTARINEJAM.get());
                // ===== 熟肉类 =====
                output.accept(COOKEDCATTLEHEART.get()); output.accept(COOKEDCATTLEJOINT.get()); output.accept(COOKEDCATTLESTOMACH.get()); output.accept(COOKEDCATTLELUNG.get());
                output.accept(COOKEDCATTLELIVER.get()); output.accept(COOKEDCATTLEINTESTINE.get()); output.accept(COOKEDDICEDCATTLE.get()); output.accept(COOKEDCATTLEFACE.get());
                output.accept(COOKEDCATTLEMARBLEDBEEF.get()); output.accept(COOKEDCATTLELEG.get()); output.accept(COOKEDCATTLEFEET.get()); output.accept(COOKEDCATTLETENDON.get());
                output.accept(COOKEDDICEDPIGMEAT.get()); output.accept(COOKEDPIGSHREDS.get()); output.accept(COOKEDPIGEAR.get()); output.accept(COOKEDPIGNOSE.get());
                output.accept(COOKEDPIGTAIL.get()); output.accept(COOKEDPIGSKIN.get()); output.accept(COOKEDPIGSTREAKYPORK.get()); output.accept(COOKEDPIGTENDERLOIN.get());
                output.accept(COOKEDPIGSPARERIB.get()); output.accept(COOKEDPIGLEG.get()); output.accept(COOKEDPIGFEET.get()); output.accept(COOKEDPIGFAT.get());
                output.accept(COOKEDPIGSTOMACH.get()); output.accept(COOKEDPIGLUNG.get()); output.accept(COOKEDPIGLIVER.get()); output.accept(COOKEDPIGKIDNEY.get());
                output.accept(COOKEDPIGHEART.get()); output.accept(COOKEDPIGCEREBRUM.get()); output.accept(COOKEDPIGINTESTINE.get()); output.accept(COOKEDPIGFACE.get());
                output.accept(COOKEDDICEDSHEEPMEAT.get()); output.accept(COOKEDSHEEPFACE.get()); output.accept(COOKEDSHEEPEYE.get()); output.accept(COOKEDSHEEPTAIL.get());
                output.accept(COOKEDSHEEPSTOMACH.get()); output.accept(COOKEDSHEEPLIVER.get()); output.accept(COOKEDSHEEPINTESTINE.get()); output.accept(COOKEDSHEEPKIDNEY.get());
                output.accept(COOKEDSHEEPHEART.get()); output.accept(COOKEDSHEEPSPARERLIP.get()); output.accept(COOKEDSHEEPSPINE.get()); output.accept(COOKEDSHEEPFEET.get());
                output.accept(COOKEDSHEEPLEG.get()); output.accept(COOKEDSHEEPFAT.get());
                output.accept(COOKEDCHICKENDICED.get()); output.accept(COOKEDCHICKENPIECEPIECE.get()); output.accept(COOKEDCHICKENHEAD.get()); output.accept(COOKEDCHICKENNECK.get());
                output.accept(COOKEDCHICKENWING.get()); output.accept(COOKEDCHICKENWINGTIP.get()); output.accept(COOKEDCHICKENFEET.get()); output.accept(COOKEDCHICKENLEGWITHLEG.get());
                output.accept(COOKEDCHICKENLEG.get()); output.accept(COOKEDCHICKENPIECE.get()); output.accept(COOKEDCHICKENSTEAK.get()); output.accept(COOKEDCHICKENFORK.get());
                output.accept(COOKEDCHICKENASS.get()); output.accept(COOKEDCHICKENHEART.get()); output.accept(COOKEDCHICKENLIVER.get()); output.accept(COOKEDCHICKENGIZZARD.get());

                // ===== Snacks =====
                output.accept(FIVEPOINTEDCARAMBOLADELIGHT.get());
                output.accept(DICEDHAMIMELON.get());
                output.accept(FRIED_TOFU_SKIN_ROLLS.get());
                output.accept(CHICKENFEETWITHPEPPERS.get());
                output.accept(COOKEDHAWTHORN.get());
                output.accept(SWEETTOUFUCURD.get());
                output.accept(TIGERGREENPEPPER.get());
                output.accept(PICEDTOUFU.get());
                output.accept(DRIEDTOUFU.get());
                output.accept(TOFU_PUFFS.get());
                output.accept(TOFUCURD.get());
                output.accept(CHINESESPICYSNACKFOOD.get());
                output.accept(SPICYPEANUT.get());
                output.accept(COOKEDSAUSAGE.get());
                output.accept(M_SECRETDELIGHT.get());

                // ===== Desserts =====
                output.accept(RIPEPEARWITHROCKSUGAR.get());
                output.accept(SUGARCOATEDHAWS.get());
                output.accept(CHINESEYAMANDSUGAR.get());
                output.accept(CHOCOLATEPOPCORN.get());
                output.accept(CHOCOLATEBEAN.get());
                output.accept(CARAMELPOPCORN.get());
                output.accept(POPCORN.get());
                output.accept(JELLY.get());
                output.accept(TANG_ZI_XIAO_ZAO.get());
                output.accept(SUGARCOATEDWALNUT.get());
                output.accept(GOLDRICECAKE.get());

                // ===== Cooked Meat =====
                output.accept(STEWEDPIGLEG.get());
                output.accept(STEWEDHALFCHICKENLEG.get());
                output.accept(STEWEDCHICKENHEART.get());
                output.accept(STEWEDCHICKENLIVERS.get());
                output.accept(STEWEDCHICKENLEG.get());
                output.accept(SAUCINGBEEF.get());
                output.accept(BONELESSLEMONCHICKENFEET.get());
                output.accept(SOMKEDCHICKENBREAST.get());
                output.accept(COOKEDPRESERVEDEGG.get());
                output.accept(MEATFLOSS.get());
                output.accept(FISHSASHIMI.get());

                // ===== Cooked Food =====
                output.accept(NEWYEARCAKE.get());
                output.accept(COLONELCHICKENNUGGETS.get());
                output.accept(FRIEDGULTINOUSRICESTRIPS.get());
                output.accept(FRIEDPEANUTS.get());
                output.accept(FRIEDMUSHROOM.get());
                output.accept(DRIEDRICECAKE.get());
                output.accept(FIREDSPRINGROLL.get());
                output.accept(FRIEDDOUGHSTICK.get());
                output.accept(DRIEDMILK.get());
                output.accept(DRIEDMEATBALL.get());
                output.accept(DRIEDDICEDAUBERGINE.get());
                output.accept(FRIEDGIZZARD.get());
                output.accept(FRENCHFRIES.get());
                output.accept(DRIEDLOTUSROOT.get());
                output.accept(FRIEDDRIEDTOUFU.get());
                output.accept(FRIEDTOUFU.get());
                output.accept(DRIEDBREAD.get());
                output.accept(DRIEDDICEDBREAD.get());
                output.accept(DRIEDDICEDSTEAMEDBREAD.get());
                output.accept(FRIEDSANZI.get());
                output.accept(FRIEDHEMPBALL.get());
                output.accept(FRIEDDOUGHTWIST.get());
                output.accept(CRISPYPISTOLLEG.get());
                output.accept(ROASTEDPURPLEPOTATO.get());
                output.accept(ROASTEDPEANUT.get());
                output.accept(COOKEDCHINESEYAM.get());
                output.accept(COOKEDVERMICELLI_0.get());
                output.accept(COOKEDZONGZI.get());
                output.accept(COOKEDGLUTINOUSRICE.get());
                output.accept(COOKEDGLUTINOUSRICEPOUNDEDINTOPASTE.get());
                output.accept(AIKUI.get());
                output.accept(STEAMEDBLOOD.get());
                output.accept(DICEDBREAD.get());
                output.accept(DRYCOOKEDNOODLES.get());
                output.accept(COOKEDVERMICELLI.get());
                output.accept(MOONCAKE.get());
                output.accept(GRAINSPANCAKE.get());
                output.accept(HAMBURGERBREAD.get());
                output.accept(YIMENGPANCAKES.get());
                output.accept(SACHIMA.get());
                output.accept(SHUMAI.get());
                output.accept(HOTDOG.get());
                output.accept(STEAMEDGLUTINOUSRICECAKE.get());
                output.accept(MEATFLOURROOL.get());
                output.accept(CRISPYEGGCAKE.get());
                output.accept(STEAMEDTWISTEDROLL.get());
                output.accept(SCALLIONOILPANCAKE.get());
                output.accept(NOODLES.get());
                output.accept(PIE.get());
                output.accept(STEAMEDBREAD.get());
                output.accept(DICEDSTEAMEDBREAD.get());
                output.accept(EGGPUFFS.get());
                output.accept(EGGVERMICELLI.get());

                // ===== Baked Food =====
                output.accept(ORLEANWING.get());
                output.accept(ORLEANLEG.get());
                output.accept(ROASTEDMUSHROOM.get());
                output.accept(ROASTED_FLAMMULINAVELUTIPES.get());
                output.accept(ROASTEDCHINESECHIVES.get());
                output.accept(ROASTEDDICEDFISH.get());

                // ===== Drinks =====
                output.accept(CREAMPOPCORN.get());
                output.accept(BOILEDCORN.get());
                output.accept(SOYAMILK.get());
                output.accept(DRINKINGWATER.get());
                output.accept(CAPPUCCINO.get());
                output.accept(CAFELATTE.get());
                output.accept(MOCHACAFE.get());
                output.accept(HOTWATER.get());
                output.accept(CAFE_AMERICANO.get());
                output.accept(LEMONREDTEA.get());
                output.accept(GRENTEAMILKWITHPEARL.get());
                output.accept(GREEN_TEA_LATTES.get());
                output.accept(LEMONTEAWITHGLUTINOUSRICEFLAVOR.get());
                output.accept(REDTEA.get());
                output.accept(GREENTEA.get());
                output.accept(BUCKWHEATTEA.get());
                output.accept(ASSAMMILK_TEA.get());
                output.accept(GREENPLUMTEA.get());
                output.accept(REDTEAMILKWITHPEARL.get());
                output.accept(KAOLIANGWINE.get());
                output.accept(KWEICHOW_MOUTAI.get());
                output.accept(TSINGTAO_BEER.get());
                output.accept(ICEDBLACKTEABLUE.get());
                output.accept(COLA.get());
                output.accept(SUGARFREICEDTEA.get());
                output.accept(SPRITE.get());

                // ===== Pickled =====
                output.accept(YOGURT.get());
                output.accept(SALTEGGYOLK.get());
                output.accept(SALTTOFUCURD.get());
                output.accept(DICEDPICKLEDVEGETABLE.get());
                output.accept(CUREDMEAT.get());
                output.accept(CUREDSAUSAGE.get());
                output.accept(SALTYDICEDCURUMBER.get());
                output.accept(PICKLEDVEGETABLE.get());
                output.accept(SLICEDSALTYCUCUMBER.get());
                output.accept(HOTANDSOURRICENOODLES.get());

                // ===== Ingredients =====
                output.accept(GOLDENGRAPE.get());

                // ===== expand: 食物 =====
                output.accept(BAISED_TOFU_SLICES.get()); output.accept(BOILEDRICEFLOURNOODLES.get()); output.accept(BONESOUP.get()); output.accept(BUCKWHEATRICE.get()); output.accept(CHOCOLATE.get());
                output.accept(CONGEEWITH_MINCE_PORKAND_PRESERVED_EGG.get()); output.accept(COOKEDCHICKENBREAST.get()); output.accept(COOKEDCHICKENLEAN.get()); output.accept(COOKEDCORNBATTER.get()); output.accept(COOKEDCRISPYPORK.get());
                output.accept(COOKEDFLAKEBEEF.get()); output.accept(COOKEDHALFOFCHCIKENLEG.get()); output.accept(COOKEDPIGSTREAKMEAT.get()); output.accept(DRIED_TOFU_SKIN.get()); output.accept(EATENPINEAPPLE.get());
                output.accept(EGGBISCUIT.get()); output.accept(EGGPANCAKE.get()); output.accept(EGGTART.get()); output.accept(EIGHTTREASURECONGEE.get()); output.accept(ELECTROLYTEBEVERAGE.get());
                output.accept(FANHUAROLL.get()); output.accept(FRIEDCHICKENCHOP.get()); output.accept(FRIEDCHICKENCORN.get()); output.accept(FRIEDCHICKENLEG.get()); output.accept(FRIEDCHICKENWING.get());
                output.accept(FRIEDSAUSAGE.get()); output.accept(GRAPEWINE.get()); output.accept(GREENBEANPORRIDGE.get()); output.accept(HANDGRABBEDPANCAKE.get()); output.accept(HEALTHLOQUATCREAM.get());
                output.accept(HEALTHPEANUTMILK.get()); output.accept(HEALTHWALNUTDEW.get()); output.accept(ICEDBLACKTEA.get()); output.accept(KAOLIANGPORRIDGE.get()); output.accept(KAOLIANGRICE.get());
                output.accept(LABAPORRIDGE.get()); output.accept(MILLIETPORRIDGE.get()); output.accept(MILLIETRICE.get()); output.accept(NOILEDRICENOODLE.get()); output.accept(OAKPORRIDGE.get());
                output.accept(OAKRICE.get()); output.accept(OMELETTE.get()); output.accept(PANADAPANCAKE.get()); output.accept(PEAFLOURCAKE.get()); output.accept(POTATOCHIPS.get());
                output.accept(REDBEANEGGTART.get()); output.accept(REDBEANSTUFFINGDORAYAKI.get()); output.accept(RICE.get()); output.accept(RICE_PORRIDGE.get()); output.accept(RICEPASTESOUP.get());
                output.accept(ROASTCHICKENFORK.get()); output.accept(ROASTEDSWEETPOTATO.get()); output.accept(SHELLEDBOILEDEGG.get()); output.accept(SHOU_KAI_XIN_GUO.get()); output.accept(SMALLMICEDMEATCAKE.get());
                output.accept(STEAMEDSALTORANGE.get()); output.accept(STEAMEDVERMICELLIROLL_0.get()); output.accept(STRAWBERRYCAKEROLL.get()); output.accept(SWEETMILK.get()); output.accept(TOFU_SAUSAGE.get());
                output.accept(TOFU_STICKS.get()); output.accept(WALNUTCAKE.get()); output.accept(WALNUTSHORTBREAD.get()); output.accept(WHEATMILK.get()); output.accept(YUBA.get());
                output.accept(TOUFU.get()); output.accept(EGGCAKE.get()); output.accept(GOLDRICECAKEMAX.get()); output.accept(GOLDENGRAPEMAX.get());
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> INGREDIENT_TAB = CREATIVE_MODE_TABS.register("ingredient", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.flavor_immersed_daily.ingredient"))
            .withTabsBefore(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, "fi_dtools"))
            .icon(() -> WHEATFLOUR.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(CUT_CHINESE_CABBAGE.get());

                // 切过的菜
                output.accept(DICEDLOOFACH.get()); output.accept(DICEDWAXGOURD.get()); output.accept(DICEDORANGE.get()); output.accept(DICEDPLEUROTUSERYNGII.get()); output.accept(DICEDONION.get()); output.accept(DICEDSAUTEEDGREENBEANS.get()); output.accept(SHREDDEDPOTATO_2.get()); output.accept(DICEDPOTATO.get()); output.accept(CHOPPEDPOTATO_1.get()); output.accept(SLICEDPOTATO.get()); output.accept(DICEDCHINESEYAM.get()); output.accept(DICEDCASSAVA.get()); output.accept(SLICEDLEMON.get()); output.accept(DICEDFISH.get()); output.accept(DICEDPURPLESWEETPOTATO.get()); output.accept(SHREDDEDCARROTS.get()); output.accept(DICEDCARROTS.get()); output.accept(DICEDCELERY.get()); output.accept(SHREDDERABUERGINE.get()); output.accept(SLICEDABUERGINE.get()); output.accept(SHREDDERRADISH.get()); output.accept(DICEDRADISH.get()); output.accept(DIECEDRADISH.get()); output.accept(SLICEDRADISH.get()); output.accept(SCALLION_2.get()); output.accept(DICEDLOTUSROOT.get()); output.accept(SLICEDLOTUSROOT.get()); output.accept(DICEDZUCCHINI.get()); output.accept(CHOPPEDCOWPEA.get()); output.accept(DICEDSWEETPEPPER.get()); output.accept(SHREDDERCUCUMBER.get()); output.accept(DICEDCUCUMBER.get()); output.accept(SLICEDCUCUMBER.get()); output.accept(CHINESE_LEAVES.get());

                // 切过的菜-新增
                output.accept(DIECEDPRESERVEDEGG.get()); output.accept(CUTDIECEDPRESERVEEGG.get()); output.accept(DICEDSWEETPOTATO.get()); output.accept(PURPLEDICEDSWEETPOTATO.get()); output.accept(PROCESSEDCABBAGE.get()); output.accept(DICEDTOMATO.get()); output.accept(CUTCABBAGE.get()); output.accept(CASSAVACHUNKS.get()); output.accept(SLICEDZUCCHINI.get()); output.accept(CARROTCHUNKS.get()); output.accept(CELERYLEAF.get());

                // 和面案板
                output.accept(BAOZI_SKIN.get()); output.accept(COMPACTEDRICEBRICK.get()); output.accept(DRIEDVERMICELLI.get()); output.accept(DRYPOWDERSTRIP.get()); output.accept(DRYPOWDERSKIN.get()); output.accept(MACARONI.get()); output.accept(BEATGLUTINOUSRICEFLOURPASTE.get()); output.accept(WALNUTFLOURPASTE.get()); output.accept(OILEDFLOURSKIN.get()); output.accept(SHUMAISKIN.get()); output.accept(DRYRICECAKE.get()); output.accept(RAWMOONCAKE.get()); output.accept(RAWSHUMAI.get()); output.accept(RAWCOARSENOODLES.get()); output.accept(RAWGLUTINOUSPASTE.get()); output.accept(RAWGLUTINOUSDOUGH.get()); output.accept(RAWGLUTINOUSNOODLES.get()); output.accept(RAWGLUTINOUSSKIN.get()); output.accept(RAWGLUTINOUSRICEFLOURPANCAKE.get()); output.accept(RAWFLOURROLL.get()); output.accept(RAWNOODLES.get()); output.accept(RAWFLOURPANCAKE.get()); output.accept(RAWPIE.get()); output.accept(RAWEGGPASTE.get()); output.accept(RAWEGGSKIN.get()); output.accept(RAWEGGPANCAKE.get()); output.accept(RAWRICENOODLES.get()); output.accept(RICENOODLES_2.get()); output.accept(SWEETPOTATODOUGH.get()); output.accept(VERMICELLIROLL.get()); output.accept(STEAMEDRICEBRICK.get()); output.accept(NOODLESWRAPPEDINSYRUP.get()); output.accept(EGGNOODLESWRAPPEDINSYRUP.get()); output.accept(FLOURPASTE.get()); output.accept(DOUGH.get()); output.accept(DUMPLING_SKIN.get()); output.accept(FLOURSKIN.get()); output.accept(RAWEGGNOODLES.get()); output.accept(EGGDOUGH.get());

                // 和面案板-新增
                output.accept(CUTPANCAKE.get()); output.accept(FERMENTEDFLOURPASTE.get()); output.accept(CUTEGGPANCAKE.get()); output.accept(RICESLURRYWRAPPEDINFILTERCLOTH.get()); output.accept(POUNDEDGLUTINOUSPASTE.get()); output.accept(LONGDOUGH.get()); output.accept(LONGEGGPANCAKE.get());

                // 泥，馅料，碎
                output.accept(WAXGROUDPASTE.get()); output.accept(JUJUBEPASTE.get()); output.accept(BROKENWALNUT.get()); output.accept(PEPPERANDSALTMASS.get()); output.accept(FRUITFLAVOREDMOONCAKESTUFFING.get()); output.accept(MEATPASTE.get()); output.accept(VEGETABLEPASTE.get()); output.accept(REDBEANPASTE.get()); output.accept(MEATANDVEGETABLESTUFFING.get()); output.accept(MEATANDEGGPASTE.get()); output.accept(SEASAMEGLUTINOUSRICEBALLS.get()); output.accept(SESAMEANDPEANUTBALLS.get()); output.accept(PEASTUFFING.get()); output.accept(EGGSTUFFING.get());

                // 泥，馅料，碎-新增
                output.accept(PEAPASTE.get()); output.accept(SESAMEGLUTINOUSRICEBALLS.get()); output.accept(MEATANDEGGFILLING.get()); output.accept(MEATANDVEGETABLEFILLING.get()); output.accept(MUNGBEANPASTE.get()); output.accept(WAXGOURDPASTE.get()); output.accept(UNCOMMONSTUFFING.get()); output.accept(VEGETABLEANDEGGSTUFFING.get());

                // 液体，酱，糊
                output.accept(COCOASAUCE.get()); output.accept(CREAM.get()); output.accept(MILKBOTTLE.get()); output.accept(MULTIGRAINBATTER.get()); output.accept(CONCENTRATEDSYRUP.get()); output.accept(SWEETEGGLIQUID.get()); output.accept(RAWSOYBEANMILK.get()); output.accept(TIDYWATER.get()); output.accept(GLUTINOUSRICEBATTER.get()); output.accept(PEANUTJAM.get()); output.accept(EGGLIQUID.get()); output.accept(PANADA.get()); output.accept(GELATIN.get()); output.accept(EGGBATTER.get());

                // 液体，酱，糊-新增
                output.accept(SALTYWATER.get()); output.accept(COCOAMASS.get());

                // 煮蒸炸烤半成品
                output.accept(CRISPYPORKBELLY.get()); output.accept(COOKED_BAOZI.get()); output.accept(RAW_STUFFEDGREENPEPPER.get()); output.accept(CREAMCORNKERNELS.get()); output.accept(CHOCOLATECORNKERNELS.get()); output.accept(MUSHROOMSWRAPPEDINBATTER.get()); output.accept(RAWCHICKENHALFLEGWITHBATTER.get()); output.accept(RAWCHICKENWINGWITHBATTER.get()); output.accept(RAWCHICKENMEATWITHBATTER.get()); output.accept(RAWCHICKENLEGWITHLEG.get()); output.accept(RAWSAUSAGE.get()); output.accept(NORMALMEATROLL.get()); output.accept(CARAMELCORNKENNELS.get()); output.accept(RAWSPRINGROLL.get()); output.accept(RAW_TANGYUAN.get()); output.accept(RAWAZONGZI.get()); output.accept(SWEETREDBEANEGGTART.get()); output.accept(MEATFLOURROOL.get()); output.accept(RAWFLOURPASTEWITHDRIEDMEATFLOSS.get()); output.accept(RAWCOUPLING.get()); output.accept(RAWEGGTART.get()); output.accept(RAWSPICYGLUTEN.get()); output.accept(RAWDORAYAKI.get()); output.accept(RAWAIKUI.get()); output.accept(RAWSALTYEGG.get()); output.accept(LAMBROLL.get()); output.accept(FATBEEFROLL.get()); output.accept(RAWDICEDCHICKENWITHBATTER.get()); output.accept(RAWMEATBALLWITHEGGBALL.get()); output.accept(COOKEDDUMPLING.get()); output.accept(WONTON.get());

                // 煮蒸炸烤半成品-新增
                output.accept(EGGCOATEDMEATBALLS.get()); output.accept(BATTEREDCHICKENBREASTCHUNKS.get()); output.accept(RAWMEATFLOSSDOUGH.get()); output.accept(RAWMEATZONGZI.get()); output.accept(BATTEREDMUSHROOMS.get()); output.accept(RAWSWEETZONGZI.get()); output.accept(RAWWRAPPEDMILK.get());

                // 熟食成品
                output.accept(SALTYEGG.get()); output.accept(SALTYRADDISH.get()); output.accept(TANGYUAN.get()); output.accept(THOUSAND_LAYER_TOFU_SKIN.get()); output.accept(SPAGHETTI.get());

                // 粉类
                output.accept(COCOAPOWDER.get()); output.accept(COFFEEPOWDER.get()); output.accept(TAPIOCAFLOUR.get()); output.accept(WALNUTPOWDER.get()); output.accept(GULTINOUSRICEPOWDER.get()); output.accept(SWEETPOTATSTARCH.get()); output.accept(SESAMEPOWDER.get()); output.accept(GULTINOUSRICESASAMEPOWDER.get()); output.accept(PEANUTPOWDER.get()); output.accept(PEANUTSESAMEPOWDER.get()); output.accept(MODULATEDWHEATFLOUR.get()); output.accept(PEAMEAL.get()); output.accept(WHEATFLOUR.get());

                // 粉类-新增
                output.accept(PEAFLOUR.get());

                // 调味料
                output.accept(CRYSTALSUGAR.get()); output.accept(HALOGENBAG.get()); output.accept(CURRY.get()); output.accept(WHITESUGARSYRUP.get()); output.accept(SWEETFLOURASUVE.get()); output.accept(TOMATO_HOT_POT_BASE.get()); output.accept(SALT.get()); output.accept(SALTPIECE.get()); output.accept(BROWNSUGAR.get()); output.accept(PEPPER_HOT_POT_BASE.get()); output.accept(THICKBROADBEANSAUCE.get()); output.accept(SOY.get()); output.accept(VINEGAR.get()); output.accept(COOKINGOIL.get()); output.accept(SESAMEOIL.get()); output.accept(BONESOUPESSENCE.get()); output.accept(MAJUICE.get()); output.accept(SPICY_HOT_POT_BASE.get()); output.accept(BUTTER.get());

                // 调味料-新增
                output.accept(BROWNSUGARSYRUP.get()); output.accept(HOTPOTBASETEMPLATE.get());

                // 调味料粉
                output.accept(LILACPOWDER.get()); output.accept(FIVESPICEPOWDER.get()); output.accept(ANISEEDPOWDER.get()); output.accept(ORLEANSPOWDER.get()); output.accept(GROUNDPOWDER.get()); output.accept(CUMINPOWDER.get()); output.accept(CINNAMONPOWDER.get()); output.accept(PEPPEREDSALT.get()); output.accept(ONIONPOWDER_2.get()); output.accept(REDTEAPOWDER.get()); output.accept(GREENTEAPOWDER.get()); output.accept(CHINESEPICKLYASHPOWDER.get()); output.accept(FENNELPOWDER.get()); output.accept(ONIONPOWDER.get()); output.accept(GARLICPOWDER.get()); output.accept(CHILLIPOWDER.get());

                // 杂项其他
                output.accept(SORBET.get()); output.accept(FROZENMILK.get()); output.accept(NAHCO_3.get()); output.accept(WRESTLING_GUN.get()); output.accept(CASSAVAPEARL.get()); output.accept(SOAKEDSOYBEANS.get()); output.accept(TIDYREEDLEAF.get()); output.accept(RAWSOYSHREDDEDMEAT.get()); output.accept(PROBIOTICS.get()); output.accept(LANDPLASTER.get()); output.accept(GRAVELPASTE.get()); output.accept(RAWSHEEPOFFAL.get()); output.accept(MEATFLOSS.get()); output.accept(CASING.get()); output.accept(REEDLEAF.get()); output.accept(BRAN.get());

                // 杂项其他-新增
                output.accept(SOYBEANPROTEIN.get()); output.accept(RAW_PIGOFFAL.get()); output.accept(DEBONEDCHICKENFEET.get()); output.accept(EGGWRAPPEDINGRAVEL.get()); output.accept(BEANSPROUT.get());

                // ===== expand: 食材 =====
                output.accept(DRYANISEED.get()); output.accept(DRYCINNAMON.get()); output.accept(DRYCOFFEEBEAN.get()); output.accept(DRYLILAC.get()); output.accept(DRYNUTMEG.get());
                output.accept(DRYSCALION.get()); output.accept(DRYSICHUANPEPPER.get()); output.accept(DICEDBROCCOIL.get()); output.accept(DICEDCAULIFLOWER.get()); output.accept(EGGSHELL.get());
                output.accept(OATGRAIN.get()); output.accept(POLISHEDGLUTINOUSRICE_2.get()); output.accept(RAW_BAOZI.get()); output.accept(RAW_DUMPLING.get()); output.accept(RAW_WONTON.get());
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DTOOLS_TAB = CREATIVE_MODE_TABS.register("fi_dtools", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.flavor_immersed_daily.fi_dtools"))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> KITCHENSCISSOR.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(BIGHOOK_ITEM.get());
                output.accept(WOODBASIN_ITEM.get());
                output.accept(SHARPKNIFE.get());
                output.accept(WIDEEDGEDKNIFE.get());
                output.accept(BONECUTTERKNIFE.get());
                output.accept(KITCHENKNIFE.get());
                output.accept(KITCHENSCISSOR.get());
                output.accept(SPATULA.get());
                output.accept(MALLET.get());
                output.accept(COARSECLOTH.get());
                output.accept(MOONCAKEMOLD.get());
                output.accept(ROLLINGPIN.get());
                output.accept(FILTERVAT.get());
                output.accept(EEGGPUFFSMOULD.get());
                output.accept(AGRICULTURALAPPRAISALMACHINE_ITEM.get());
                output.accept(FRIDGE_ITEM.get());
                output.accept(EGGBREAKINGMACHINE_ITEM.get());
                output.accept(MINCER_COVER.get());
                output.accept(TEAPOTCOVER.get());
                //装饰
                output.accept(FAIRY_SPARKLER.get());
                output.accept(COLORFUL_FIREWORKS_BOX_ITEM.get());
                output.accept(WRESTLING_GUN.get());
                output.accept(HONEYCOMBBRIQUET.get());
                output.accept(LEFT_DOOR_PAPER_ITEM.get());
                output.accept(RIGHT_DOOR_PAPER_ITEM.get());
                output.accept(CHINESE_KNOTTING_ITEM.get());
                output.accept(LAMP_CABINET_ITEM.get());
                output.accept(CANVAS_SCREEN_1_ITEM.get());
                output.accept(CANVAS_SCREEN_2_ITEM.get());
                output.accept(INCENSE_BURNER_ITEM.get());
                output.accept(PLANK_HANGING_LIGHT_ITEM.get());
                output.accept(REDLANTERN_ITEM.get());
                output.accept(GOLDLANTERN_ITEM.get());
                output.accept(STONE_LION_ITEM.get());
                output.accept(WINDOW_PAPER_ITEM.get());
                output.accept(ANTITHETICAL_COUPLET_1_ITEM.get());
                output.accept(ANTITHETICAL_COUPLET_2_ITEM.get());
                //木材
                output.accept(SOLARWOOD_PLANKS_ITEM.get());
                output.accept(SOLARWOOD_STAIRS_ITEM.get());
                output.accept(SOLARWOOD_SLAB_ITEM.get());
                output.accept(SOLARWOOD_FENCE_ITEM.get());
                output.accept(SOLARWOOD_FENCE_GATE_ITEM.get());
                output.accept(SOLARWOOD_BUTTON_ITEM.get());
                output.accept(SOLARWOOD_PRESSURE_PLATE_ITEM.get());
                output.accept(ORCHARD_HEARTWOOD_PLANKS_ITEM.get());
                output.accept(ORCHARD_HEARTWOOD_STAIRS_ITEM.get());
                output.accept(ORCHARD_HEARTWOOD_SLAB_ITEM.get());
                output.accept(ORCHARD_HEARTWOOD_FENCE_ITEM.get());
                output.accept(ORCHARD_HEARTWOOD_FENCE_GATE_ITEM.get());
                output.accept(ORCHARD_HEARTWOOD_BUTTON_ITEM.get());
                output.accept(ORCHARD_HEARTWOOD_PRESSURE_PLATE_ITEM.get());
                output.accept(STONEBARK_PLANKS_ITEM.get());
                output.accept(STONEBARK_STAIRS_ITEM.get());
                output.accept(STONEBARK_SLAB_ITEM.get());
                output.accept(STONEBARK_FENCE_ITEM.get());
                output.accept(STONEBARK_FENCE_GATE_ITEM.get());
                output.accept(STONEBARK_BUTTON_ITEM.get());
                output.accept(STONEBARK_PRESSURE_PLATE_ITEM.get());
                output.accept(VINEHEART_TIMBER_PLANKS_ITEM.get());
                output.accept(VINEHEART_TIMBER_STAIRS_ITEM.get());
                output.accept(VINEHEART_TIMBER_SLAB_ITEM.get());
                output.accept(VINEHEART_TIMBER_FENCE_ITEM.get());
                output.accept(VINEHEART_TIMBER_FENCE_GATE_ITEM.get());
                output.accept(VINEHEART_TIMBER_BUTTON_ITEM.get());
                output.accept(VINEHEART_TIMBER_PRESSURE_PLATE_ITEM.get());
                output.accept(VERDANT_GRACE_PLANKS_ITEM.get());
                output.accept(VERDANT_GRACE_STAIRS_ITEM.get());
                output.accept(VERDANT_GRACE_SLAB_ITEM.get());
                output.accept(VERDANT_GRACE_FENCE_ITEM.get());
                output.accept(VERDANT_GRACE_FENCE_GATE_ITEM.get());
                output.accept(VERDANT_GRACE_BUTTON_ITEM.get());
                output.accept(VERDANT_GRACE_PRESSURE_PLATE_ITEM.get());
                output.accept(SOLARWOODCHAIR_ITEM.get());
                output.accept(ORCHARDCHAIR_ITEM.get());
                output.accept(STONEBARKCHAIR_ITEM.get());
                output.accept(VINEHEARTCHAIR_ITEM.get());
                output.accept(VERDANTGRACECHAIR_ITEM.get());

            }).build());

    public FlavorImmersedDaily(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerPayloads);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        MOB_EFFECTS.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        MENU_TYPES.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    /**
     * 注册网络包 — 箱装烟花配置同步
     */
    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(ColorfulFireworksBoxSyncPayload.TYPE, ColorfulFireworksBoxSyncPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        var player = context.player();
                        if (player != null && player.level() instanceof ServerLevel serverLevel) {
                            if (serverLevel.getBlockEntity(payload.pos()) instanceof ColorfulFireworksBoxBlockEntity be) {
                                be.applyConfig(payload);
                            }
                        }
                    });
                });
        registrar.playToServer(WindowPaperSyncPayload.TYPE, WindowPaperSyncPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        var player = context.player();
                        if (player != null && player.level() instanceof ServerLevel serverLevel) {
                            if (serverLevel.getEntity(payload.entityId()) instanceof WindowPaperEntity entity) {
                                entity.applyConfig(payload);
                            }
                        }
                    });
                });
        registrar.playToServer(CoupletSyncPayload.TYPE, CoupletSyncPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        var player = context.player();
                        if (player != null && player.level() instanceof ServerLevel serverLevel) {
                            if (serverLevel.getBlockEntity(payload.pos()) instanceof CoupletBlockEntity be) {
                                be.setLines(payload.lines());
                                be.setColor(payload.color());
                            }
                        }
                    });
                });
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("烟火凡人心 —— 模组初始化完成");
        // 使用 initCrop 一次性注入种子和产物，防止重复调用
        CHINESE_LEAVES_CROP.get().initCrop(() -> CHINESE_LEAVES_SEEDS.get(), () -> CHINESE_LEAVES.get());
        RADISHSEED_CROP.get().initCrop(() -> RADISHSEED.get(), () -> RADISH.get());
        ANISEED_0_CROP.get().initCrop(() -> ANISEED_0.get(), () -> ANISEED_0.get());
        WHITEMUSHROOM_CROP.get().initCrop(() -> WHITE_MUSHROOM_SEED.get(), () -> WHITEMUSHROOM.get());
        BLACKFUNGUS_CROP.get().initCrop(() -> BLACKFUNGSSEED.get(), () -> BLACKFUNGUS.get());
        PLEUROTUSERYNGII_CROP.get().initCrop(() -> PLEUROTUSSEED.get(), () -> PLEUROTUSERYNGII.get());
        ENOKIMUSHROOM_CROP.get().initCrop(() -> ENOKIMUSHROOMSEED.get(), () -> ENOKIMUSHROOM.get());
        TREMELLA_CROP.get().initCrop(() -> TREMELLASEED.get(), () -> TREMELLA.get());
        FRAGRANTMUSHROOM_CROP.get().initCrop(() -> FRAGRANTSEED.get(), () -> FRAGRANTMUSHROOM.get());
        BLUEBERRY_CROP.get().initCrop(() -> BLUEBERRYSEED.get(), () -> BLUEBERRY.get());
        DRAGONFRUIT_CROP.get().initCrop(() -> DRAGONFRUITSEED.get(), () -> DRAGONFRUIT.get());
        GREENTEALEAVES_CROP.get().initCrop(() -> GREENTEALEAVESSEED.get(), () -> GREENTEALEAVES.get());
        HAMIMELON_CROP.get().initCrop(() -> HAMIMELONSEED.get(), () -> HAMIMELON.get());
        PINEAPPLE_CROP.get().initCrop(() -> PINEAPPLESEED.get(), () -> PINEAPPLE.get());
        RED_TEA_CROP.get().initCrop(() -> RED_TEA_SEED.get(), () -> REDTEALEAVES.get());
        STRAWBERRY_CROP.get().initCrop(() -> STRAWBERRYSEED.get(), () -> STRAWBERRY.get());
        LOTUSROOT_CROP.get().initCrop(() -> LOTUSROOTSEED.get(), () -> LOTUSROOT.get());
        GLUTINOUSRICE_CROP.get().initCrop(() -> GLUTINOUSSEEDS.get(), () -> POLISHEDGLUTINOUSRICE_2.get());
        PADDY_CROP.get().initCrop(() -> PADDYSEEDS.get(), () -> PADDYGRAIN.get());
        KAOLIANGGARIN_CROP.get().initCrop(() -> KAOLIANG_SEED.get(), () -> KAOLIANGGRAIN.get());
        BROCCOILSEED_CROP.get().initCrop(() -> BROCCOILSEED.get(), () -> BROCCOIL.get());
        BUCKWHEATSEED_CROP.get().initCrop(() -> BUCKWHEATSEED.get(), () -> BUCKWHEAT.get());
        CABBAGESEED_CROP.get().initCrop(() -> CABBAGESEED.get(), () -> CABBAGE.get());
        CASSAVASEEDS_CROP.get().initCrop(() -> CASSAVASEEDS.get(), () -> CASSAVA.get());
        CELERYSEED_CROP.get().initCrop(() -> CELERYSEED.get(), () -> CELERY.get());
        CHINESECHIVESSEED_CROP.get().initCrop(() -> CHINESECHIVESSEED.get(), () -> CHINESECHIVES.get());
        CHINESEYAMSEED_CROP.get().initCrop(() -> CHINESEYAMSEED.get(), () -> CHINESEYAM.get());
        COFFEEBEANSEED_CROP.get().initCrop(() -> COFFEEBEANSEED.get(), () -> COFFEEBEANSEED.get());
        CORNSEED_CROP.get().initCrop(() -> CORNSEED.get(), () -> CORN.get());
        CUMINSEED_CROP.get().initCrop(() -> CUMINSEED.get(), () -> CUMIN.get());
        FENNELSEEDSTATES_CROP.get().initCrop(() -> FENNELSEEDSTATES.get(), () -> FENNEL.get());
        GARLICSEED_CROP.get().initCrop(() -> GARLICSEED.get(), () -> GARLIC.get());
        GINGER_SEED_CROP.get().initCrop(() -> GINGER_SEED.get(), () -> GINGER.get());
        GREENPEPPERSEEDS_CROP.get().initCrop(() -> GREENPEPPERSEEDS.get(), () -> GREENPEPPER.get());
        GUMBOSEED_CROP.get().initCrop(() -> GUMBOSEED.get(), () -> GUMBO.get());
        MILLET_CROP.get().initCrop(() -> MILLET.get(), () -> MILLETGRAIN_GRAIN.get());
        MUNGBEANPLANT_CROP.get().initCrop(() -> MUNGBEANPLANT.get(), () -> MUNGBEAN.get());
        MUSTRAD_SEED_CROP.get().initCrop(() -> MUSTRAD_SEED.get(), () -> MUSTARD.get());
        NUTMEGSEED_CROP.get().initCrop(() -> NUTMEGSEED.get(), () -> NUTMEGSEED.get());
        OATSEED_CROP.get().initCrop(() -> OATSEED.get(), () -> OAT.get());
        OILRAPESEED_CROP.get().initCrop(() -> OILRAPESEED.get(), () -> OILSEEDRAPE.get());
        ONIONSEED_CROP.get().initCrop(() -> ONIONSEED.get(), () -> ONION.get());
        PEASEED_CROP.get().initCrop(() -> PEASEED.get(), () -> PEA.get());
        PUPLESWEETPOTATOSEED_CROP.get().initCrop(() -> PUPLESWEETPOTATOSEED.get(), () -> PURPLESWEETPOTATO.get());
        SESAMESEED_CROP.get().initCrop(() -> SESAMESEED.get(), () -> SESAME.get());
        SOY_BEAN_SEED_CROP.get().initCrop(() -> SOY_BEAN_SEED.get(), () -> SOYBEAN.get());
        SWEETGREENPEPPERSEED_CROP.get().initCrop(() -> SWEETGREENPEPPERSEED.get(), () -> SWEETGREENPEPPER.get());
        ZUCCHINISEED_CROP.get().initCrop(() -> ZUCCHINISEED.get(), () -> ZUCCHINI.get());
        SPINACH_SEED_CROP.get().initCrop(() -> SPINACH_SEED.get(), () -> SPINACH.get());
        CAULIFLOWER_SEED_CROP.get().initCrop(() -> CAULIFLOWER_SEED.get(), () -> CAULIFLOWER.get());
        SCALLION_SEED_CROP.get().initCrop(() -> SCALLION_SEED.get(), () -> SACLLION.get());
        LILAC_SEED_CROP.get().initCrop(() -> LILAC_SEED.get(), () -> LILAC_SEED.get());
        RED_BEAN_BLOCK_CROP.get().initCrop(() -> RED_BEAN_BLOCK.get(), () -> RED_BEAN_BLOCK.get());
        RED_PEPPER_SEED_CROP.get().initCrop(() -> RED_PEPPER_SEED.get(), () -> REDREPPER.get());
        SWEET_POTATO_SEED_CROP.get().initCrop(() -> SWEET_POTATO_SEED.get(), () -> SWEETPOTATO.get());
        SI_CHUAN_PEPPER_SEED_CROP.get().initCrop(() -> SI_CHUAN_PEPPER_SEED.get(), () -> SI_CHUAN_PEPPER_SEED.get());
        PEA_NUT_SEED_CROP.get().initCrop(() -> PEA_NUT_SEED.get(), () -> PEANUT.get());
        // 爬架作物 initCrop
        GRAPEBLOCK.get().initCrop(() -> GRAPESEED.get(), () -> GRAPE.get(), () -> GRAPEBLOCK.get());
        CUCUMBERBLOCK.get().initCrop(() -> CUCUMBERSEEDS.get(), () -> CUCUMBER.get(), () -> CUCUMBERBLOCK.get());
        WAXGOURDBLOCK.get().initCrop(() -> WAX_GOURD_SEED_BLOCK.get(), () -> WAXGOURD.get(), () -> WAXGOURDBLOCK.get());
        KIDNEYBEANBLOCK.get().initCrop(() -> KIDNEYBEANSEED.get(), () -> KIDNEYBEAN.get(), () -> KIDNEYBEANBLOCK.get());
        AUBERGINEBLOCK.get().initCrop(() -> AUBERGINESEEDBLOCK.get(), () -> AUBERGINE.get(), () -> AUBERGINEBLOCK.get());
        TOMATOBLOCK.get().initCrop(() -> TOMATOSEED.get(), () -> TOMATO.get(), () -> TOMATOBLOCK.get());
        COWPEABLOCK.get().initCrop(() -> COWPEABEANSEED.get(), () -> COWPEA.get(), () -> COWPEABLOCK.get());
        GREENGRAEBLOCK.get().initCrop(() -> GREENGRAESEED.get(), () -> GREENGRAPE.get(), () -> GREENGRAEBLOCK.get());
        LOOFAHBLOCK.get().initCrop(() -> LOOFAHSEED.get(), () -> LOOFAH.get(), () -> LOOFAHBLOCK.get());
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof ItemNameBlockItem) {
            event.getToolTip().add(Component.translatable("tooltip.flavor_immersed_daily.seed"));
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("烟火凡人心 —— 服务器启动中");
    }

    // 斩骨刀攻击动物 → 直接生成 dead 物品并移除实体，不经过掉落物系统，避免与其他模组冲突
    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (!(event.getTarget() instanceof LivingEntity target)) return;
        Player player = event.getEntity();
        ItemStack weapon = player.getMainHandItem();
        if (!weapon.is(BONECUTTERKNIFE.get())) return;

        Item deadItem = null;
        if (target instanceof Cow) {
            deadItem = DEADCATTLE.get();
        } else if (target instanceof Sheep) {
            deadItem = DEADSHEEP.get();
        } else if (target instanceof Pig) {
            deadItem = DEADPIG.get();
        } else if (target instanceof Chicken) {
            deadItem = DEADCHICKEN.get();
        }

        if (deadItem != null) {
            if (!target.level().isClientSide) {
                target.level().addFreshEntity(new ItemEntity(
                        target.level(),
                        target.getX(), target.getY(), target.getZ(),
                        new ItemStack(deadItem)));
                target.discard();
            }
            event.setCanceled(true);
        }
    }

    private static BlockBehaviour.Properties waterCropProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .randomTicks()
                .noCollission()
                .noOcclusion()
                .instabreak()
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY);
    }

    private static BlockBehaviour.Properties mushroomProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .randomTicks()
                .noCollission()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY);
    }

    private static BlockBehaviour.Properties cropProperties() {
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
