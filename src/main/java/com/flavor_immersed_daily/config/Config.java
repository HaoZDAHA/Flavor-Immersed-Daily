package com.flavor_immersed_daily.config;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue MAX_FRUITS_PER_CHUNK = BUILDER
            .comment("每个区块内最多同时存在的果实实体数量（包括所有种类的水果）")
            .defineInRange("maxFruitsPerChunk", 5, 1, 100);

    //香油滑步效果配置
    public static final ModConfigSpec.BooleanValue SESAME_SLIP_ENABLED = BUILDER
            .comment("香油滑步（sesame_slip）效果总开关，关闭后食用黄油不再获得该效果")
            .define("sesameSlipEnabled", true);
    public static final ModConfigSpec.DoubleValue SESAME_SLIP_HEIGHT = BUILDER
            .comment("芝麻滑行效果下实体的行走高度（格），默认 2.1")
            .defineInRange("sesameSlipHeight", 2.1, 0.0, 10.0);

    //醋酸侵蚀效果配置
    public static final ModConfigSpec.BooleanValue ACETIC_EROSION_ENABLED = BUILDER
            .comment("醋酸侵蚀（acetic_erosion）效果总开关，关闭后食用醋不再获得该效果")
            .define("aceticErosionEnabled", true);
    public static final ModConfigSpec.IntValue ACETIC_EROSION_EXTRA_DURABILITY = BUILDER
            .comment("醋酸侵蚀效果下，被攻击者每次盔甲耐久损耗时额外增加的损耗值，默认 1")
            .defineInRange("aceticErosionExtraDurability", 1, 0, 100);

    //黄油投手效果配置
    public static final ModConfigSpec.BooleanValue BUTTER_PITCHER_ENABLED = BUILDER
            .comment("黄油投手（butter_pitcher）效果总开关，关闭后弹射物命中不再触发冻结")
            .define("butterPitcherEnabled", true);
    public static final ModConfigSpec.DoubleValue BUTTER_PITCHER_FREEZE_CHANCE = BUILDER
            .comment("黄油投手效果下，弹射物命中非玩家非Boss实体时触发冻结的概率（0.0-1.0），默认 0.25")
            .defineInRange("butterPitcherFreezeChance", 0.25, 0.0, 1.0);
    public static final ModConfigSpec.IntValue BUTTER_PITCHER_FREEZE_DURATION = BUILDER
            .comment("黄油投手效果下，被冻结的时长（秒），默认 5")
            .defineInRange("butterPitcherFreezeDuration", 5, 1, 60);
    public static final ModConfigSpec.BooleanValue BUTTER_PITCHER_EXCLUDE_BOSS = BUILDER
            .comment("黄油投手效果是否跳过Boss实体（末影龙、凋灵、循声守卫），默认 true")
            .define("butterPitcherExcludeBoss", true);

    //蘸豆，爽！效果配置
    public static final ModConfigSpec.BooleanValue BEAN_FURY_ENABLED = BUILDER
            .comment("蘸豆，爽！（bean_fury）效果总开关，关闭后近战攻击不再触发额外暴击")
            .define("beanFuryEnabled", true);
    public static final ModConfigSpec.DoubleValue BEAN_FURY_CRIT_CHANCE = BUILDER
            .comment("蘸豆，爽！效果下近战攻击触发暴击的概率（0.0-1.0），默认 0.25")
            .defineInRange("beanFuryCritChance", 0.25, 0.0, 1.0);

    //百味之基效果配置
    public static final ModConfigSpec.BooleanValue FLAVOR_BASE_ENABLED = BUILDER
            .comment("百味之基（flavor_base）效果总开关，关闭后不再触发与生效")
            .define("flavorBaseEnabled", true);
    public static final ModConfigSpec.DoubleValue FLAVOR_BASE_DAMAGE_BONUS = BUILDER
            .comment("百味之基效果下，身上每有 1 种本模组/附属模组 buff 时攻击伤害增加值，默认 1.0")
            .defineInRange("flavorBaseDamageBonus", 1.0, 0.0, 100.0);
    public static final ModConfigSpec.DoubleValue FLAVOR_BASE_SPEED_BONUS = BUILDER
            .comment("百味之基效果下，身上每有 1 种本模组/附属模组 buff 时移动速度增加值，默认 0.1")
            .defineInRange("flavorBaseSpeedBonus", 0.1, 0.0, 10.0);
    public static final ModConfigSpec.IntValue FLAVOR_BASE_MAX_STACKS = BUILDER
            .comment("百味之基效果下，加成最多叠加的次数，默认 10")
            .defineInRange("flavorBaseMaxStacks", 10, 1, 100);

    //晒足一百八十天效果配置
    public static final ModConfigSpec.BooleanValue SOLAR_BREW_ENABLED = BUILDER
            .comment("晒足一百八十天（solar_brew）效果总开关，关闭后不再触发与生效")
            .define("solarBrewEnabled", true);
    public static final ModConfigSpec.DoubleValue SOLAR_BREW_OPEN_SKY_FIRE = BUILDER
            .comment("晒足一百八十天效果下，攻击露天生物时的额外火焰伤害，默认 0.5")
            .defineInRange("solarBrewOpenSkyFireDamage", 0.5, 0.0, 100.0);
    public static final ModConfigSpec.DoubleValue SOLAR_BREW_UNDEAD_FIRE = BUILDER
            .comment("晒足一百八十天效果下，攻击亡灵生物时的额外火焰伤害，默认 0.5")
            .defineInRange("solarBrewUndeadFireDamage", 0.5, 0.0, 100.0);

    //浩克大葱效果配置
    public static final ModConfigSpec.BooleanValue HULK_LEEK_ENABLED = BUILDER
            .comment("浩克大葱（hulk_leek）效果总开关，关闭后不再触发与生效")
            .define("hulkLeekEnabled", true);

    //火爆狂攻效果配置
    public static final ModConfigSpec.BooleanValue FURY_ASSAULT_ENABLED = BUILDER
            .comment("火爆狂攻（fury_assault）效果总开关，关闭后不再触发与生效")
            .define("furyAssaultEnabled", true);
    public static final ModConfigSpec.DoubleValue FURY_ASSAULT_HEALTH_COST = BUILDER
            .comment("火爆狂攻效果下，每次近战攻击消耗的玩家生命值，默认 1.0")
            .defineInRange("furyAssaultHealthCost", 1.0, 0.0, 20.0);
    public static final ModConfigSpec.DoubleValue FURY_ASSAULT_RANGE = BUILDER
            .comment("火爆狂攻效果下，前方火焰攻击的直线范围（格），默认 10.0")
            .defineInRange("furyAssaultRange", 10.0, 1.0, 64.0);
    public static final ModConfigSpec.DoubleValue FURY_ASSAULT_FIRE_DAMAGE = BUILDER
            .comment("火爆狂攻效果下，范围内生物受到的火焰伤害，默认 2.0")
            .defineInRange("furyAssaultFireDamage", 2.0, 0.0, 100.0);

    //屠宰战利品配置 butcher item
    private static final String DROP_COMMENT =
            "屠宰阶段掉落物，逗号分隔的物品ID（格式: modid:item_id）。空字符串表示不掉落。";

    // 牛 cattle
    public static final ModConfigSpec.ConfigValue<String> CATTLE_DROP_1 = BUILDER
            .comment("牛 - stage1→2 (wideedgedknife放血)", DROP_COMMENT)
            .define("cattleDrop1", "flavor_immersed_daily:rawcattleblood");
    public static final ModConfigSpec.ConfigValue<String> CATTLE_DROP_2 = BUILDER
            .comment("牛 - stage2→3 (sharpknife剥皮)")
            .define("cattleDrop2", "minecraft:leather,minecraft:leather,flavor_immersed_daily:rawcattlejoint,flavor_immersed_daily:rawcattlefat");
    public static final ModConfigSpec.ConfigValue<String> CATTLE_DROP_3 = BUILDER
            .comment("牛 - stage3→4 (bonecutterknife斩骨)")
            .define("cattleDrop3", "flavor_immersed_daily:rawcattlefeet,flavor_immersed_daily:rawcattleleg,flavor_immersed_daily:rawcattlefeet,flavor_immersed_daily:rawcattleleg,flavor_immersed_daily:rawcattlefeet,flavor_immersed_daily:rawcattleleg,flavor_immersed_daily:rawcattlefeet,flavor_immersed_daily:rawcattleleg,flavor_immersed_daily:rawcattleface,flavor_immersed_daily:bullhorn,flavor_immersed_daily:bullhorn,flavor_immersed_daily:animalskull");
    public static final ModConfigSpec.ConfigValue<String> CATTLE_DROP_4 = BUILDER
            .comment("牛 - stage4→5 (sharpknife掏空)")
            .define("cattleDrop4", "flavor_immersed_daily:rawcattleintestine,flavor_immersed_daily:rawcattleliver,flavor_immersed_daily:rawcattlelung,flavor_immersed_daily:rawcattlestomach,flavor_immersed_daily:rawcattleheart");
    public static final ModConfigSpec.ConfigValue<String> CATTLE_DROP_5 = BUILDER
            .comment("牛 - stage5→0 (sharpknife切肉)")
            .define("cattleDrop5", "minecraft:beef,minecraft:beef,minecraft:beef,flavor_immersed_daily:rawcattletendon,flavor_immersed_daily:rawcattletendon,flavor_immersed_daily:rawsnowflakebeef,flavor_immersed_daily:bovinebone");

    // 羊 sheep
    public static final ModConfigSpec.ConfigValue<String> SHEEP_DROP_1 = BUILDER
            .comment("羊 - stage1→2 (wideedgedknife放血)")
            .define("sheepDrop1", "flavor_immersed_daily:rawsheepblood");
    public static final ModConfigSpec.ConfigValue<String> SHEEP_DROP_2 = BUILDER
            .comment("羊 - stage2→3 (sharpknife剥皮)")
            .define("sheepDrop2", "flavor_immersed_daily:sheepbread,flavor_immersed_daily:sheepbread,minecraft:leather,minecraft:wool,minecraft:wool,minecraft:wool");
    public static final ModConfigSpec.ConfigValue<String> SHEEP_DROP_3 = BUILDER
            .comment("羊 - stage3→4 (bonecutterknife斩骨)")
            .define("sheepDrop3", "minecraft:bone,minecraft:bone,flavor_immersed_daily:rawsheepface,flavor_immersed_daily:rawsheepfeet,flavor_immersed_daily:rawsheepfeet,flavor_immersed_daily:rawsheepfeet,flavor_immersed_daily:rawsheepfeet,flavor_immersed_daily:rawsheepleg,flavor_immersed_daily:rawsheepleg,flavor_immersed_daily:rawsheepleg,flavor_immersed_daily:rawsheepleg,flavor_immersed_daily:rawsheeptail,flavor_immersed_daily:rawsheepeye");
    public static final ModConfigSpec.ConfigValue<String> SHEEP_DROP_4 = BUILDER
            .comment("羊 - stage4→5 (sharpknife掏空)")
            .define("sheepDrop4", "flavor_immersed_daily:rawsheepintestine,flavor_immersed_daily:rawsheepstomach,flavor_immersed_daily:rawsheepliver,flavor_immersed_daily:rawsheepheart,flavor_immersed_daily:rawsheepkidney");
    public static final ModConfigSpec.ConfigValue<String> SHEEP_DROP_5 = BUILDER
            .comment("羊 - stage5→0 (sharpknife切肉)")
            .define("sheepDrop5", ",minecraft:mutton,minecraft:mutton,minecraft:mutton,flavor_immersed_daily:rawsheepsparerib,,flavor_immersed_daily:rawsheepsparerib,flavor_immersed_daily:rawsheepspine");

    // 猪 pig
    public static final ModConfigSpec.ConfigValue<String> PIG_DROP_1 = BUILDER
            .comment("猪 - stage1→2 (wideedgedknife放血)")
            .define("pigDrop1", "flavor_immersed_daily:rawpigblood");
    public static final ModConfigSpec.ConfigValue<String> PIG_DROP_2 = BUILDER
            .comment("猪 - stage2→3 (sharpknife剥皮)")
            .define("pigDrop2", "flavor_immersed_daily:rawpigskin,flavor_immersed_daily:rawpigskin,flavor_immersed_daily:rawpigfat");
    public static final ModConfigSpec.ConfigValue<String> PIG_DROP_3 = BUILDER
            .comment("猪 - stage3→4 (bonecutterknife斩骨)")
            .define("pigDrop3", "flavor_immersed_daily:rawpigear,flavor_immersed_daily:rawpignose,flavor_immersed_daily:rawpighead,flavor_immersed_daily:rawpigtail,flavor_immersed_daily:rawpigcerebrum,flavor_immersed_daily:rawpigfeet,flavor_immersed_daily:rawpigfeet,flavor_immersed_daily:rawpigfeet,flavor_immersed_daily:rawpigfeet,flavor_immersed_daily:rawpigleg,flavor_immersed_daily:rawpigleg,flavor_immersed_daily:rawpigleg,flavor_immersed_daily:rawpigleg");
    public static final ModConfigSpec.ConfigValue<String> PIG_DROP_4 = BUILDER
            .comment("猪 - stage4→5 (sharpknife掏空)")
            .define("pigDrop4", "flavor_immersed_daily:rawpigintestine,flavor_immersed_daily:rawpigstomach,flavor_immersed_daily:rawpiglung,flavor_immersed_daily:rawpigliver,flavor_immersed_daily:rawpigheart,flavor_immersed_daily:rawpigkidney,flavor_immersed_daily:rawpigkidney");
    public static final ModConfigSpec.ConfigValue<String> PIG_DROP_5 = BUILDER
            .comment("猪 - stage5→0 (sharpknife切肉)")
            .define("pigDrop5", "flavor_immersed_daily:rawpigtenderloin,flavor_immersed_daily:rawpigstreakypork,flavor_immersed_daily:rawpigstreakypork,minecraft:porkchop,minecraft:porkchop,minecraft:porkchop,flavor_immersed_daily:rawpigsparerib,flavor_immersed_daily:rawpigsparerib");

    // 鸡 chicken（特殊：deadchicken→1→2→右键, chickenwithoutfeather→5→6→0）
    public static final ModConfigSpec.ConfigValue<String> CHICKEN_DROP_1 = BUILDER
            .comment("鸡 - deadchicken→stage1→2 (wideedgedknife放血)")
            .define("chickenDrop1", "flavor_immersed_daily:rawchickenblood");
    public static final ModConfigSpec.ConfigValue<String> CHICKEN_DROP_5 = BUILDER
            .comment("鸡 - chickenwithoutfeather→stage5→6 (sharpknife掏空)")
            .define("chickenDrop5", "flavor_immersed_daily:rawchickenliver,flavor_immersed_daily:rawchickenheart");
    public static final ModConfigSpec.ConfigValue<String> CHICKEN_DROP_6 = BUILDER
            .comment("鸡 - stage6→0 (sharpknife切割)")
            .define("chickenDrop6", "flavor_immersed_daily:rawchickenneck,flavor_immersed_daily:rawchickenwing,flavor_immersed_daily:rawchickenwing,flavor_immersed_daily:rawchickenwingtip,flavor_immersed_daily:rawchickenwingtip,flavor_immersed_daily:rawhalfofchickenleg,flavor_immersed_daily:rawhalfofchickenleg,flavor_immersed_daily:rawchickenlean,flavor_immersed_daily:rawchickenhead,flavor_immersed_daily:rawchickenleg,flavor_immersed_daily:rawchickenleg,flavor_immersed_daily:rawchickenass,flavor_immersed_daily:rawchickenfeet,flavor_immersed_daily:rawchickenfeet,flavor_immersed_daily:rawchickenbreast,flavor_immersed_daily:rawchickenbreast,minecraft:chicken");

    // ===== 木盆漂洗战利品 =====
    public static final ModConfigSpec.ConfigValue<String> WASHED_CHICKEN_DROPS = BUILDER
            .comment("chickenwithoutblood在木盆中漂洗后的额外战利品，逗号分隔的物品ID", DROP_COMMENT)
            .define("washedChickenDrops", "minecraft:feather,minecraft:feather,minecraft:feather");

    // ===== 野生采集物鉴定战利品 =====
    private static final String WILD_COMMENT =
            "野生采集物在农产鉴定机中鉴定时的掉落物，逗号分隔的物品ID（格式: modid:item_id）。会随机生成1-3个的掉落物。";

    public static final ModConfigSpec.ConfigValue<String> TEMPERATEWILDFRUIT_DROPS = BUILDER
            .comment("温带野果鉴定掉落物", WILD_COMMENT)
            .define("temperateWildFruitDrops", "flavor_immersed_daily:strawberry,flavor_immersed_daily:winterjujube,flavor_immersed_daily:pear,flavor_immersed_daily:plum,flavor_immersed_daily:kiwifruit,flavor_immersed_daily:lemon,flavor_immersed_daily:grape,flavor_immersed_daily:greengrape,flavor_immersed_daily:loquat,flavor_immersed_daily:apricot,flavor_immersed_daily:honeypeach,flavor_immersed_daily:nectarine,flavor_immersed_daily:pomegranate,flavor_immersed_daily:mulberry,flavor_immersed_daily:tomato,flavor_immersed_daily:tangerine,flavor_immersed_daily:sweetmelon,minecraft:apple");
    public static final ModConfigSpec.ConfigValue<String> TROPICALWILDFRUIT_DROPS = BUILDER
            .comment("热带野果鉴定掉落物", WILD_COMMENT)
            .define("tropicalWildFruitDrops", "flavor_immersed_daily:pineapple,flavor_immersed_daily:orange,flavor_immersed_daily:hamimelon,flavor_immersed_daily:dragonfruit,flavor_immersed_daily:lychee,flavor_immersed_daily:durian,flavor_immersed_daily:mango,flavor_immersed_daily:pawpaw,flavor_immersed_daily:banana,flavor_immersed_daily:carambola,flavor_immersed_daily:mangosteen,flavor_immersed_daily:coconut,minecraft:melon");
    public static final ModConfigSpec.ConfigValue<String> WILDFLOWERANDLEAF_DROPS = BUILDER
            .comment("野花花叶鉴定掉落物", WILD_COMMENT)
            .define("wildFlowerAndLeafDrops", "flavor_immersed_daily:chineseleaves,flavor_immersed_daily:spinach,flavor_immersed_daily:sacllion,flavor_immersed_daily:cucumber,flavor_immersed_daily:chinesechives,flavor_immersed_daily:waxgourd,flavor_immersed_daily:greentealeaves,flavor_immersed_daily:redtealeaves,flavor_immersed_daily:cauliflower,flavor_immersed_daily:zucchini,flavor_immersed_daily:broccoil,flavor_immersed_daily:cowpea,flavor_immersed_daily:loofah,flavor_immersed_daily:kidneybean,flavor_immersed_daily:tomato,flavor_immersed_daily:aubergine,flavor_immersed_daily:celery,flavor_immersed_daily:gumbo,flavor_immersed_daily:oilseedrape,flavor_immersed_daily:cabbage");
    public static final ModConfigSpec.ConfigValue<String> WILDFRUITINCOLDZONE_DROPS = BUILDER
            .comment("寒带野果鉴定掉落物", WILD_COMMENT)
            .define("wildFruitInColdZoneDrops", "flavor_immersed_daily:blueberry,flavor_immersed_daily:greenplum,flavor_immersed_daily:hawthorn,flavor_immersed_daily:cherry,flavor_immersed_daily:reddate,flavor_immersed_daily:walnut,flavor_immersed_daily:pistachionut");
    public static final ModConfigSpec.ConfigValue<String> WILDGRAINPLANT_DROPS = BUILDER
            .comment("野生谷物鉴定掉落物", WILD_COMMENT)
            .define("wildGrainPlantDrops", "flavor_immersed_daily:paddyseeds,flavor_immersed_daily:millet,flavor_immersed_daily:oat,flavor_immersed_daily:glutinousseeds,flavor_immersed_daily:red_bean_block,flavor_immersed_daily:mungbean,flavor_immersed_daily:peanut,flavor_immersed_daily:kao_liang_seed,flavor_immersed_daily:soybean,flavor_immersed_daily:buckwheat,flavor_immersed_daily:corn,flavor_immersed_daily:pea,flavor_immersed_daily:coffeebeanseed,minecraft:wheat_seeds");
    public static final ModConfigSpec.ConfigValue<String> WILDMUSHROOMPLANT_DROPS = BUILDER
            .comment("野生菌菇鉴定掉落物", WILD_COMMENT)
            .define("wildMushroomPlantDrops", "minecraft:red_mushroom,minecraft:brown_mushroom,flavor_immersed_daily:whitemushroom,flavor_immersed_daily:enokimushroom,flavor_immersed_daily:tremella,flavor_immersed_daily:blackfungus,flavor_immersed_daily:fragrantmushroom,flavor_immersed_daily:pleurotuseryngii");
    public static final ModConfigSpec.ConfigValue<String> WILDSEEDPLANT_DROPS = BUILDER
            .comment("野生籽叶鉴定掉落物", WILD_COMMENT)
            .define("wildSeedPlantDrops", "flavor_immersed_daily:aniseed_0,flavor_immersed_daily:lilac_seed,flavor_immersed_daily:cinnamon,flavor_immersed_daily:si_chuan_pepper_seed,flavor_immersed_daily:fennel,flavor_immersed_daily:greenpepper,flavor_immersed_daily:redrepper,flavor_immersed_daily:sweetgreenpepper,flavor_immersed_daily:nutmeg,flavor_immersed_daily:sesame,flavor_immersed_daily:cumin,flavor_immersed_daily:garlic,flavor_immersed_daily:ginger,flavor_immersed_daily:onion,flavor_immersed_daily:nutmegseed");
    public static final ModConfigSpec.ConfigValue<String> WILDTUBERPLANTS_DROPS = BUILDER
            .comment("野生块茎鉴定掉落物", WILD_COMMENT)
            .define("wildTuberPlantsDrops", "flavor_immersed_daily:sweetpotato,flavor_immersed_daily:cassava,flavor_immersed_daily:radish,flavor_immersed_daily:lotusroot,flavor_immersed_daily:chineseyam,flavor_immersed_daily:purplesweetpotato,flavor_immersed_daily:mustard,flavor_immersed_daily:onion,minecraft:potato,minecraft:carrot");

    public static final ModConfigSpec SPEC = BUILDER.build();

    // 运行时缓存
    public static int maxFruitsPerChunk = 5;
    public static boolean sesameSlipEnabled = true;
    public static double sesameSlipHeight = 2.1;
    public static boolean aceticErosionEnabled = true;
    public static int aceticErosionExtraDurability = 1;
    public static boolean butterPitcherEnabled = true;
    public static double butterPitcherFreezeChance = 0.25;
    public static int butterPitcherFreezeDuration = 5;
    public static boolean butterPitcherExcludeBoss = true;
    public static boolean beanFuryEnabled = true;
    public static double beanFuryCritChance = 0.25;
    public static boolean flavorBaseEnabled = true;
    public static double flavorBaseDamageBonus = 1.0;
    public static double flavorBaseSpeedBonus = 0.1;
    public static int flavorBaseMaxStacks = 10;
    public static boolean solarBrewEnabled = true;
    public static double solarBrewOpenSkyFireDamage = 0.5;
    public static double solarBrewUndeadFireDamage = 0.5;
    public static boolean hulkLeekEnabled = true;
    public static boolean furyAssaultEnabled = true;
    public static double furyAssaultHealthCost = 1.0;
    public static double furyAssaultRange = 10.0;
    public static double furyAssaultFireDamage = 2.0;
    public static Map<Integer, List<String>> cattleDrops = new HashMap<>();
    public static Map<Integer, List<String>> sheepDrops = new HashMap<>();
    public static Map<Integer, List<String>> pigDrops = new HashMap<>();
    public static Map<Integer, List<String>> chickenDrops = new HashMap<>();
    public static Map<String, List<String>> wildDrops = new HashMap<>();
    public static List<String> washedChickenDrops = Collections.emptyList();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        maxFruitsPerChunk = MAX_FRUITS_PER_CHUNK.get();
        sesameSlipEnabled = SESAME_SLIP_ENABLED.get();
        sesameSlipHeight = SESAME_SLIP_HEIGHT.get();
        aceticErosionEnabled = ACETIC_EROSION_ENABLED.get();
        aceticErosionExtraDurability = ACETIC_EROSION_EXTRA_DURABILITY.get();
        butterPitcherEnabled = BUTTER_PITCHER_ENABLED.get();
        butterPitcherFreezeChance = BUTTER_PITCHER_FREEZE_CHANCE.get();
        butterPitcherFreezeDuration = BUTTER_PITCHER_FREEZE_DURATION.get();
        butterPitcherExcludeBoss = BUTTER_PITCHER_EXCLUDE_BOSS.get();
        beanFuryEnabled = BEAN_FURY_ENABLED.get();
        beanFuryCritChance = BEAN_FURY_CRIT_CHANCE.get();
        flavorBaseEnabled = FLAVOR_BASE_ENABLED.get();
        flavorBaseDamageBonus = FLAVOR_BASE_DAMAGE_BONUS.get();
        flavorBaseSpeedBonus = FLAVOR_BASE_SPEED_BONUS.get();
        flavorBaseMaxStacks = FLAVOR_BASE_MAX_STACKS.get();
        solarBrewEnabled = SOLAR_BREW_ENABLED.get();
        solarBrewOpenSkyFireDamage = SOLAR_BREW_OPEN_SKY_FIRE.get();
        solarBrewUndeadFireDamage = SOLAR_BREW_UNDEAD_FIRE.get();
        hulkLeekEnabled = HULK_LEEK_ENABLED.get();
        furyAssaultEnabled = FURY_ASSAULT_ENABLED.get();
        furyAssaultHealthCost = FURY_ASSAULT_HEALTH_COST.get();
        furyAssaultRange = FURY_ASSAULT_RANGE.get();
        furyAssaultFireDamage = FURY_ASSAULT_FIRE_DAMAGE.get();

        cattleDrops.put(1, parseDrops(CATTLE_DROP_1.get()));
        cattleDrops.put(2, parseDrops(CATTLE_DROP_2.get()));
        cattleDrops.put(3, parseDrops(CATTLE_DROP_3.get()));
        cattleDrops.put(4, parseDrops(CATTLE_DROP_4.get()));
        cattleDrops.put(5, parseDrops(CATTLE_DROP_5.get()));

        sheepDrops.put(1, parseDrops(SHEEP_DROP_1.get()));
        sheepDrops.put(2, parseDrops(SHEEP_DROP_2.get()));
        sheepDrops.put(3, parseDrops(SHEEP_DROP_3.get()));
        sheepDrops.put(4, parseDrops(SHEEP_DROP_4.get()));
        sheepDrops.put(5, parseDrops(SHEEP_DROP_5.get()));

        pigDrops.put(1, parseDrops(PIG_DROP_1.get()));
        pigDrops.put(2, parseDrops(PIG_DROP_2.get()));
        pigDrops.put(3, parseDrops(PIG_DROP_3.get()));
        pigDrops.put(4, parseDrops(PIG_DROP_4.get()));
        pigDrops.put(5, parseDrops(PIG_DROP_5.get()));

        chickenDrops.put(1, parseDrops(CHICKEN_DROP_1.get()));
        chickenDrops.put(5, parseDrops(CHICKEN_DROP_5.get()));
        chickenDrops.put(6, parseDrops(CHICKEN_DROP_6.get()));

        washedChickenDrops = parseDrops(WASHED_CHICKEN_DROPS.get());

        wildDrops.put("flavor_immersed_daily:temperatewildfruit", parseDrops(TEMPERATEWILDFRUIT_DROPS.get()));
        wildDrops.put("flavor_immersed_daily:tropicalwild_fruit", parseDrops(TROPICALWILDFRUIT_DROPS.get()));
        wildDrops.put("flavor_immersed_daily:wildflowerandleaf", parseDrops(WILDFLOWERANDLEAF_DROPS.get()));
        wildDrops.put("flavor_immersed_daily:wildfruitincoldzone", parseDrops(WILDFRUITINCOLDZONE_DROPS.get()));
        wildDrops.put("flavor_immersed_daily:wildgrainplant", parseDrops(WILDGRAINPLANT_DROPS.get()));
        wildDrops.put("flavor_immersed_daily:wildmushroomplant", parseDrops(WILDMUSHROOMPLANT_DROPS.get()));
        wildDrops.put("flavor_immersed_daily:wildseedplant", parseDrops(WILDSEEDPLANT_DROPS.get()));
        wildDrops.put("flavor_immersed_daily:wildtuberplants", parseDrops(WILDTUBERPLANTS_DROPS.get()));
    }

    private static List<String> parseDrops(String raw) {
        if (raw == null || raw.trim().isEmpty()) return Collections.emptyList();
        return List.of(raw.trim().split("\\s*,\\s*"));
    }

    public static List<String> getDrops(int animal, int stage) {
        return switch (animal) {
            case 1 -> cattleDrops.getOrDefault(stage, Collections.emptyList());
            case 2 -> sheepDrops.getOrDefault(stage, Collections.emptyList());
            case 3 -> pigDrops.getOrDefault(stage, Collections.emptyList());
            case 4 -> chickenDrops.getOrDefault(stage, Collections.emptyList());
            default -> Collections.emptyList();
        };
    }

    public static List<String> getWildDrops(String itemId) {
        return wildDrops.getOrDefault(itemId, null);
    }

    public static Map<String, List<String>> getWildDropsMap() {
        return Collections.unmodifiableMap(wildDrops);
    }
}
