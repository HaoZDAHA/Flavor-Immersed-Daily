package com.flavor_immersed_daily.config;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.appraisal.AppraisalSupplementRegistry;
import com.flavor_immersed_daily.datadriver.ModDrops;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// ModConfigEvent 属于 mod 事件总线（net.minecraftforge.fml.event.*），必须显式声明 bus = MOD，
// 否则默认注册到 FORGE 总线，onLoad 永远不会被调用。
@Mod.EventBusSubscriber(modid = FlavorImmersedDaily.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue MAX_FRUITS_PER_CHUNK = BUILDER
            .comment("每个区块内最多同时存在的果实实体数量（包括所有种类的水果）")
            .defineInRange("maxFruitsPerChunk", 5, 1, 100);
    public static final ForgeConfigSpec.DoubleValue NATURAL_FRUIT_MATURITY_CHANCE = BUILDER
            .comment("结果叶每次随机刻推进一阶段成熟进度的概率（0.0-1.0），默认 0.33")
            .defineInRange("naturalFruitMaturityChance", 0.33, 0.0, 1.0);
    public static final ForgeConfigSpec.DoubleValue BONE_MEAL_FRUIT_MATURITY_CHANCE = BUILDER
            .comment("每次使用骨粉推进一阶段成熟进度的概率（0.0-1.0），默认 0.35")
            .defineInRange("boneMealFruitMaturityChance", 0.35, 0.0, 1.0);
    public static final ForgeConfigSpec.DoubleValue HANGING_FRUIT_CHANCE = BUILDER
            .comment("结果叶成熟时额外生成悬挂果实方块的概率（0.0-1.0），默认 0.70")
            .defineInRange("hangingFruitChance", 0.70, 0.0, 1.0);
    public static final ForgeConfigSpec.DoubleValue FALLING_FRUIT_CHANCE = BUILDER
            .comment("悬挂果实生成或失去支撑时转为掉落果实实体的概率（0.0-1.0），默认 0.30")
            .defineInRange("fallingFruitChance", 0.30, 0.0, 1.0);

// 香油滑步效果配置
    public static final ForgeConfigSpec.BooleanValue SESAME_SLIP_ENABLED = BUILDER
            .comment("香油滑步（sesame_slip）效果总开关，关闭后食用黄油不再获得该效果")
            .define("sesameSlipEnabled", true);
    public static final ForgeConfigSpec.DoubleValue SESAME_SLIP_HEIGHT = BUILDER
            .comment("芝麻滑行效果下实体的行走高度（格），默认 2.1")
            .defineInRange("sesameSlipHeight", 2.1, 0.0, 10.0);

// 醋酸侵蚀效果配置
    public static final ForgeConfigSpec.BooleanValue ACETIC_EROSION_ENABLED = BUILDER
            .comment("醋酸侵蚀（acetic_erosion）效果总开关，关闭后食用醋不再获得该效果")
            .define("aceticErosionEnabled", true);
    public static final ForgeConfigSpec.IntValue ACETIC_EROSION_EXTRA_DURABILITY = BUILDER
            .comment("醋酸侵蚀效果下，被攻击者每次盔甲耐久损耗时额外增加的损耗值，默认 1")
            .defineInRange("aceticErosionExtraDurability", 1, 0, 100);

// 黄油投手效果配置
    public static final ForgeConfigSpec.BooleanValue BUTTER_PITCHER_ENABLED = BUILDER
            .comment("黄油投手（butter_pitcher）效果总开关，关闭后弹射物命中不再触发冻结")
            .define("butterPitcherEnabled", true);
    public static final ForgeConfigSpec.DoubleValue BUTTER_PITCHER_FREEZE_CHANCE = BUILDER
            .comment("黄油投手效果下，弹射物命中非玩家非Boss实体时触发冻结的概率（0.0-1.0），默认 0.25")
            .defineInRange("butterPitcherFreezeChance", 0.25, 0.0, 1.0);
    public static final ForgeConfigSpec.IntValue BUTTER_PITCHER_FREEZE_DURATION = BUILDER
            .comment("黄油投手效果下，被冻结的时长（秒），默认 5")
            .defineInRange("butterPitcherFreezeDuration", 5, 1, 60);
    public static final ForgeConfigSpec.BooleanValue BUTTER_PITCHER_EXCLUDE_BOSS = BUILDER
            .comment("黄油投手效果是否跳过Boss实体（末影龙、凋灵、循声守卫），默认 true")
            .define("butterPitcherExcludeBoss", true);

// 蘸豆，爽！效果配置
    public static final ForgeConfigSpec.BooleanValue BEAN_FURY_ENABLED = BUILDER
            .comment("蘸豆，爽！（bean_fury）效果总开关，关闭后近战攻击不再触发额外暴击")
            .define("beanFuryEnabled", true);
    public static final ForgeConfigSpec.DoubleValue BEAN_FURY_CRIT_CHANCE = BUILDER
            .comment("蘸豆，爽！效果下近战攻击触发暴击的概率（0.0-1.0），默认 0.25")
            .defineInRange("beanFuryCritChance", 0.25, 0.0, 1.0);

// 百味之基效果配置
    public static final ForgeConfigSpec.BooleanValue FLAVOR_BASE_ENABLED = BUILDER
            .comment("百味之基（flavor_base）效果总开关，关闭后不再触发与生效")
            .define("flavorBaseEnabled", true);
    public static final ForgeConfigSpec.DoubleValue FLAVOR_BASE_DAMAGE_BONUS = BUILDER
            .comment("百味之基效果下，身上每有 1 种本模组/附属模组 buff 时攻击伤害增加值，默认 1.0")
            .defineInRange("flavorBaseDamageBonus", 1.0, 0.0, 100.0);
    public static final ForgeConfigSpec.DoubleValue FLAVOR_BASE_SPEED_BONUS = BUILDER
            .comment("百味之基效果下，身上每有 1 种本模组/附属模组 buff 时移动速度增加值，默认 0.1")
            .defineInRange("flavorBaseSpeedBonus", 0.1, 0.0, 10.0);
    public static final ForgeConfigSpec.IntValue FLAVOR_BASE_MAX_STACKS = BUILDER
            .comment("百味之基效果下，加成最多叠加的次数，默认 10")
            .defineInRange("flavorBaseMaxStacks", 10, 1, 100);

// 晒足一百八十天效果配置
    public static final ForgeConfigSpec.BooleanValue SOLAR_BREW_ENABLED = BUILDER
            .comment("晒足一百八十天（solar_brew）效果总开关，关闭后不再触发与生效")
            .define("solarBrewEnabled", true);
    public static final ForgeConfigSpec.DoubleValue SOLAR_BREW_OPEN_SKY_FIRE = BUILDER
            .comment("晒足一百八十天效果下，攻击露天生物时的额外火焰伤害，默认 0.5")
            .defineInRange("solarBrewOpenSkyFireDamage", 0.5, 0.0, 100.0);
    public static final ForgeConfigSpec.DoubleValue SOLAR_BREW_UNDEAD_FIRE = BUILDER
            .comment("晒足一百八十天效果下，攻击亡灵生物时的额外火焰伤害，默认 0.5")
            .defineInRange("solarBrewUndeadFireDamage", 0.5, 0.0, 100.0);

// 浩克大葱效果配置
    public static final ForgeConfigSpec.BooleanValue HULK_LEEK_ENABLED = BUILDER
            .comment("浩克大葱（hulk_leek）效果总开关，关闭后不再触发与生效")
            .define("hulkLeekEnabled", true);

// 火爆狂攻效果配置
    public static final ForgeConfigSpec.BooleanValue FURY_ASSAULT_ENABLED = BUILDER
            .comment("火爆狂攻（fury_assault）效果总开关，关闭后不再触发与生效")
            .define("furyAssaultEnabled", true);
    public static final ForgeConfigSpec.DoubleValue FURY_ASSAULT_HEALTH_COST = BUILDER
            .comment("火爆狂攻效果下，每次近战攻击消耗的玩家生命值，默认 1.0")
            .defineInRange("furyAssaultHealthCost", 1.0, 0.0, 20.0);
    public static final ForgeConfigSpec.DoubleValue FURY_ASSAULT_RANGE = BUILDER
            .comment("火爆狂攻效果下，前方火焰攻击的直线范围（格），默认 10.0")
            .defineInRange("furyAssaultRange", 10.0, 1.0, 64.0);
    public static final ForgeConfigSpec.DoubleValue FURY_ASSAULT_FIRE_DAMAGE = BUILDER
            .comment("火爆狂攻效果下，范围内生物受到的火焰伤害，默认 2.0")
            .defineInRange("furyAssaultFireDamage", 2.0, 0.0, 100.0);

// 邪祟暴露效果配置
    public static final ForgeConfigSpec.BooleanValue EXPOSE_EVIL_ENABLED = BUILDER
            .comment("邪祟暴露（expose_evil）效果总开关，关闭后不再触发与生效")
            .define("exposeEvilEnabled", true);
    public static final ForgeConfigSpec.DoubleValue EXPOSE_EVIL_ZOMBIE_CHANCE = BUILDER
            .comment("邪祟暴露效果下，近战攻击村民转化为僵尸村民的概率（0.0-1.0），默认 0.15")
            .defineInRange("exposeEvilZombieChance", 0.15, 0.0, 1.0);
    public static final ForgeConfigSpec.DoubleValue EXPOSE_EVIL_BLOOD_FIRE_DAMAGE = BUILDER
            .comment("邪祟暴露效果下，被手持血液攻击时受到的灼烧伤害，默认 4.0")
            .defineInRange("exposeEvilBloodFireDamage", 4.0, 0.0, 100.0);

// 武圣效果配置
    public static final ForgeConfigSpec.DoubleValue GUAN_YU_STRIKE_MAX_REAL_DAMAGE = BUILDER
            .comment("武圣（guan_yu_strike）效果下，食物攻击转化的真实伤害上限，默认 8.0")
            .defineInRange("guanYuStrikeMaxRealDamage", 8.0, 0.0, 100.0);

// 八角镇香效果配置
    public static final ForgeConfigSpec.BooleanValue ANISEED_WARD_ENABLED = BUILDER
            .comment("八角镇香（aniseed_ward）效果总开关，关闭后不再触发与生效")
            .define("aniseedWardEnabled", true);
    public static final ForgeConfigSpec.DoubleValue ANISEED_WARD_RADIUS = BUILDER
            .comment("八角镇香效果下，周围亡灵生物被赋予减速的半径范围（格），默认 8")
            .defineInRange("aniseedWardRadius", 8.0, 1.0, 64.0);

// 团团圆圆效果配置
    public static final ForgeConfigSpec.BooleanValue REUNION_RECOVERY_ENABLED = BUILDER
            .comment("团团圆圆（reunion_recovery）效果总开关，关闭后食用广式月饼不再获得该效果")
            .define("reunionRecoveryEnabled", true);
    public static final ForgeConfigSpec.DoubleValue REUNION_RECOVERY_RADIUS = BUILDER
            .comment("团团圆圆效果下，周围玩家获得生命恢复的半径（格），默认 16")
            .defineInRange("reunionRecoveryRadius", 16.0, 1.0, 64.0);
    public static final ForgeConfigSpec.IntValue REUNION_RECOVERY_LEVEL = BUILDER
            .comment("团团圆圆效果下，赋予附近玩家的生命恢复等级，默认 1")
            .defineInRange("reunionRecoveryLevel", 1, 1, 10);

// 酥到掉渣效果配置
    public static final ForgeConfigSpec.BooleanValue CRUMBLY_CRUST_ENABLED = BUILDER
            .comment("酥到掉渣（crumbly_crust）效果总开关，关闭后食用苏式月饼不再获得该效果")
            .define("crumblyCrustEnabled", true);
    public static final ForgeConfigSpec.IntValue CRUMBLY_CRUST_TAUNT_SECONDS = BUILDER
            .comment("酥到掉渣效果下，吸引敌对生物前往掉落点持续时长（秒），默认 3")
            .defineInRange("crumblyCrustTauntSeconds", 3, 1, 60);

// 蔽日食月效果配置
    public static final ForgeConfigSpec.BooleanValue ECLIPSE_DEVOUR_ENABLED = BUILDER
            .comment("蔽日食月（eclipse_devour）效果总开关，关闭后食用京式月饼不再获得该效果")
            .define("eclipseDevourEnabled", true);

// 清凉冰皮效果配置
    public static final ForgeConfigSpec.BooleanValue COOL_SKIN_ENABLED = BUILDER
            .comment("清凉冰皮（cool_skin）效果总开关，关闭后食用冰皮月饼不再获得该效果")
            .define("coolSkinEnabled", true);
    public static final ForgeConfigSpec.DoubleValue COOL_SKIN_FIRE_DAMAGE_BONUS = BUILDER
            .comment("清凉冰皮效果下，对免疫火焰伤害的敌对生物造成的额外近战伤害，默认 1.0")
            .defineInRange("coolSkinFireDamageBonus", 1.0, 0.0, 100.0);

// bighook 抓取生物功能配置
    public static final ForgeConfigSpec.BooleanValue BIGHOOK_CARRY_ENABLED = BUILDER
            .comment("是否启用 bighook 抓取生物功能：手持挂勾攻击当前生命值低于自己且非敌对/非Boss的生物（含其他玩家）时，可将其拿起",
                    "玩家潜行（shift）无法挣脱时（此开关关闭）只能通过手里不再持有挂勾来放下生物")
            .define("bighookCarryEnabled", true);
    public static final ForgeConfigSpec.BooleanValue BIGHOOK_SHIFT_ESCAPE_ENABLED = BUILDER
            .comment("是否允许被抓取的生物或者挂在挂钩上的生物通过潜行（shift）挣脱/被放下")
            .define("bighookShiftEscapeEnabled", true);

// 屠宰战利品配置
// 基础掉落物已定义在模组自带 JSON（data/flavor_immersed_daily/drop_tables/butcher/*.json），
// 会随模组更新自动生效。下面这些 config 仅用于"额外添加"掉落物，默认留空。
    private static final String DROP_COMMENT =
            "屠宰阶段额外掉落物，逗号分隔的物品ID（格式: modid:item_id）。空字符串表示不额外添加。";

// 牛 cattle（基础掉落见 butcher/cattle.json，stage1→2 放血 ... stage5→0 切肉）
    public static final ForgeConfigSpec.ConfigValue<String> CATTLE_DROP_1 = BUILDER
            .comment("牛 - stage1→2 额外掉落", DROP_COMMENT)
            .define("cattleDrop1", "");
    public static final ForgeConfigSpec.ConfigValue<String> CATTLE_DROP_2 = BUILDER
            .comment("牛 - stage2→3 额外掉落", DROP_COMMENT)
            .define("cattleDrop2", "");
    public static final ForgeConfigSpec.ConfigValue<String> CATTLE_DROP_3 = BUILDER
            .comment("牛 - stage3→4 额外掉落", DROP_COMMENT)
            .define("cattleDrop3", "");
    public static final ForgeConfigSpec.ConfigValue<String> CATTLE_DROP_4 = BUILDER
            .comment("牛 - stage4→5 额外掉落", DROP_COMMENT)
            .define("cattleDrop4", "");
    public static final ForgeConfigSpec.ConfigValue<String> CATTLE_DROP_5 = BUILDER
            .comment("牛 - stage5→0 额外掉落", DROP_COMMENT)
            .define("cattleDrop5", "");

// 羊 sheep（基础掉落见 butcher/sheep.json）
    public static final ForgeConfigSpec.ConfigValue<String> SHEEP_DROP_1 = BUILDER
            .comment("羊 - stage1→2 额外掉落", DROP_COMMENT)
            .define("sheepDrop1", "");
    public static final ForgeConfigSpec.ConfigValue<String> SHEEP_DROP_2 = BUILDER
            .comment("羊 - stage2→3 额外掉落", DROP_COMMENT)
            .define("sheepDrop2", "");
    public static final ForgeConfigSpec.ConfigValue<String> SHEEP_DROP_3 = BUILDER
            .comment("羊 - stage3→4 额外掉落", DROP_COMMENT)
            .define("sheepDrop3", "");
    public static final ForgeConfigSpec.ConfigValue<String> SHEEP_DROP_4 = BUILDER
            .comment("羊 - stage4→5 额外掉落", DROP_COMMENT)
            .define("sheepDrop4", "");
    public static final ForgeConfigSpec.ConfigValue<String> SHEEP_DROP_5 = BUILDER
            .comment("羊 - stage5→0 额外掉落", DROP_COMMENT)
            .define("sheepDrop5", "");

// 猪 pig（基础掉落见 butcher/pig.json）
    public static final ForgeConfigSpec.ConfigValue<String> PIG_DROP_1 = BUILDER
            .comment("猪 - stage1→2 额外掉落", DROP_COMMENT)
            .define("pigDrop1", "");
    public static final ForgeConfigSpec.ConfigValue<String> PIG_DROP_2 = BUILDER
            .comment("猪 - stage2→3 额外掉落", DROP_COMMENT)
            .define("pigDrop2", "");
    public static final ForgeConfigSpec.ConfigValue<String> PIG_DROP_3 = BUILDER
            .comment("猪 - stage3→4 额外掉落", DROP_COMMENT)
            .define("pigDrop3", "");
    public static final ForgeConfigSpec.ConfigValue<String> PIG_DROP_4 = BUILDER
            .comment("猪 - stage4→5 额外掉落", DROP_COMMENT)
            .define("pigDrop4", "");
    public static final ForgeConfigSpec.ConfigValue<String> PIG_DROP_5 = BUILDER
            .comment("猪 - stage5→0 额外掉落", DROP_COMMENT)
            .define("pigDrop5", "");

// 鸡 chicken（基础掉落见 butcher/chicken.json，特殊：deadchicken→1→2→右键, chickenwithoutfeather→5→6→0）
    public static final ForgeConfigSpec.ConfigValue<String> CHICKEN_DROP_1 = BUILDER
            .comment("鸡 - deadchicken→stage1→2 额外掉落", DROP_COMMENT)
            .define("chickenDrop1", "");
    public static final ForgeConfigSpec.ConfigValue<String> CHICKEN_DROP_5 = BUILDER
            .comment("鸡 - chickenwithoutfeather→stage5→6 额外掉落", DROP_COMMENT)
            .define("chickenDrop5", "");
    public static final ForgeConfigSpec.ConfigValue<String> CHICKEN_DROP_6 = BUILDER
            .comment("鸡 - stage6→0 额外掉落", DROP_COMMENT)
            .define("chickenDrop6", "");

// 木盆漂洗战利品
// 基础掉落见 drop_tables/washed_chicken.json
    public static final ForgeConfigSpec.ConfigValue<String> WASHED_CHICKEN_DROPS = BUILDER
            .comment("chickenwithoutblood在木盆中漂洗后的额外战利品，逗号分隔的物品ID", DROP_COMMENT)
            .define("washedChickenDrops", "");

// 野生采集物鉴定战利品
// 基础掉落见 drop_tables/appraisal.json
    private static final String WILD_COMMENT =
            "野生采集物在农产鉴定机中鉴定时的额外掉落物，逗号分隔的物品ID（格式: modid:item_id）。会随机生成1-3个的掉落物。";

    public static final ForgeConfigSpec.ConfigValue<String> TEMPERATEWILDFRUIT_DROPS = BUILDER
            .comment("温带野果鉴定额外掉落物", WILD_COMMENT)
            .define("temperateWildFruitDrops", "");
    public static final ForgeConfigSpec.ConfigValue<String> TROPICALWILDFRUIT_DROPS = BUILDER
            .comment("热带野果鉴定额外掉落物", WILD_COMMENT)
            .define("tropicalWildFruitDrops", "");
    public static final ForgeConfigSpec.ConfigValue<String> WILDFLOWERANDLEAF_DROPS = BUILDER
            .comment("野花花叶鉴定额外掉落物", WILD_COMMENT)
            .define("wildFlowerAndLeafDrops", "");
    public static final ForgeConfigSpec.ConfigValue<String> WILDFRUITINCOLDZONE_DROPS = BUILDER
            .comment("寒带野果鉴定额外掉落物", WILD_COMMENT)
            .define("wildFruitInColdZoneDrops", "");
    public static final ForgeConfigSpec.ConfigValue<String> WILDGRAINPLANT_DROPS = BUILDER
            .comment("野生谷物鉴定额外掉落物", WILD_COMMENT)
            .define("wildGrainPlantDrops", "");
    public static final ForgeConfigSpec.ConfigValue<String> WILDMUSHROOMPLANT_DROPS = BUILDER
            .comment("野生菌菇鉴定额外掉落物", WILD_COMMENT)
            .define("wildMushroomPlantDrops", "");
    public static final ForgeConfigSpec.ConfigValue<String> WILDSEEDPLANT_DROPS = BUILDER
            .comment("野生籽叶鉴定额外掉落物", WILD_COMMENT)
            .define("wildSeedPlantDrops", "");
    public static final ForgeConfigSpec.ConfigValue<String> WILDTUBERPLANTS_DROPS = BUILDER
            .comment("野生块茎鉴定额外掉落物", WILD_COMMENT)
            .define("wildTuberPlantsDrops", "");

    public static final ForgeConfigSpec SPEC = BUILDER.build();

// 运行时缓存
    public static int maxFruitsPerChunk = 5;
    public static double naturalFruitMaturityChance = 0.33;
    public static double boneMealFruitMaturityChance = 0.35;
    public static double hangingFruitChance = 0.70;
    public static double fallingFruitChance = 0.30;
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
    public static boolean exposeEvilEnabled = true;
    public static double exposeEvilZombieChance = 0.15;
    public static double exposeEvilBloodFireDamage = 4.0;
    public static double guanYuStrikeMaxRealDamage = 8.0;
    public static boolean aniseedWardEnabled = true;
    public static double aniseedWardRadius = 8.0;
    public static boolean reunionRecoveryEnabled = true;
    public static double reunionRecoveryRadius = 16.0;
    public static int reunionRecoveryLevel = 1;
    public static boolean crumblyCrustEnabled = true;
    public static int crumblyCrustTauntSeconds = 3;
    public static boolean eclipseDevourEnabled = true;
    public static boolean coolSkinEnabled = true;
    public static double coolSkinFireDamageBonus = 1.0;
    public static boolean bighookCarryEnabled = true;
    public static boolean bighookShiftEscapeEnabled = true;
    public static Map<Integer, List<String>> cattleDrops = new HashMap<>();
    public static Map<Integer, List<String>> sheepDrops = new HashMap<>();
    public static Map<Integer, List<String>> pigDrops = new HashMap<>();
    public static Map<Integer, List<String>> chickenDrops = new HashMap<>();
    public static Map<String, List<String>> wildDrops = new HashMap<>();
    public static List<String> washedChickenDrops = Collections.emptyList();

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        maxFruitsPerChunk = MAX_FRUITS_PER_CHUNK.get();
        naturalFruitMaturityChance = NATURAL_FRUIT_MATURITY_CHANCE.get();
        boneMealFruitMaturityChance = BONE_MEAL_FRUIT_MATURITY_CHANCE.get();
        hangingFruitChance = HANGING_FRUIT_CHANCE.get();
        fallingFruitChance = FALLING_FRUIT_CHANCE.get();
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
        exposeEvilEnabled = EXPOSE_EVIL_ENABLED.get();
        exposeEvilZombieChance = EXPOSE_EVIL_ZOMBIE_CHANCE.get();
        exposeEvilBloodFireDamage = EXPOSE_EVIL_BLOOD_FIRE_DAMAGE.get();
        guanYuStrikeMaxRealDamage = GUAN_YU_STRIKE_MAX_REAL_DAMAGE.get();
        aniseedWardEnabled = ANISEED_WARD_ENABLED.get();
        aniseedWardRadius = ANISEED_WARD_RADIUS.get();
        reunionRecoveryEnabled = REUNION_RECOVERY_ENABLED.get();
        reunionRecoveryRadius = REUNION_RECOVERY_RADIUS.get();
        reunionRecoveryLevel = REUNION_RECOVERY_LEVEL.get();
        crumblyCrustEnabled = CRUMBLY_CRUST_ENABLED.get();
        crumblyCrustTauntSeconds = CRUMBLY_CRUST_TAUNT_SECONDS.get();
        eclipseDevourEnabled = ECLIPSE_DEVOUR_ENABLED.get();
        coolSkinEnabled = COOL_SKIN_ENABLED.get();
        coolSkinFireDamageBonus = COOL_SKIN_FIRE_DAMAGE_BONUS.get();
        bighookCarryEnabled = BIGHOOK_CARRY_ENABLED.get();
        bighookShiftEscapeEnabled = BIGHOOK_SHIFT_ESCAPE_ENABLED.get();

// 基础掉落物来自模组自带 JSON，config 只做"额外添加"
        ModDrops.init();
        cattleDrops = buildButcherDrops(ModDrops.cattleDefaults,
                CATTLE_DROP_1.get(), CATTLE_DROP_2.get(), CATTLE_DROP_3.get(), CATTLE_DROP_4.get(), CATTLE_DROP_5.get());
        sheepDrops = buildButcherDrops(ModDrops.sheepDefaults,
                SHEEP_DROP_1.get(), SHEEP_DROP_2.get(), SHEEP_DROP_3.get(), SHEEP_DROP_4.get(), SHEEP_DROP_5.get());
        pigDrops = buildButcherDrops(ModDrops.pigDefaults,
                PIG_DROP_1.get(), PIG_DROP_2.get(), PIG_DROP_3.get(), PIG_DROP_4.get(), PIG_DROP_5.get());
        chickenDrops = buildChickenDrops(CHICKEN_DROP_1.get(), CHICKEN_DROP_5.get(), CHICKEN_DROP_6.get());

        washedChickenDrops = mergeExtraList(ModDrops.washedChickenDefaults, parseDrops(WASHED_CHICKEN_DROPS.get()));

        wildDrops.put("flavor_immersed_daily:temperatewildfruit",
                mergeExtraList(ModDrops.appraisalDefaults.getOrDefault("flavor_immersed_daily:temperatewildfruit", List.of()), parseDrops(TEMPERATEWILDFRUIT_DROPS.get())));
        wildDrops.put("flavor_immersed_daily:tropicalwild_fruit",
                mergeExtraList(ModDrops.appraisalDefaults.getOrDefault("flavor_immersed_daily:tropicalwild_fruit", List.of()), parseDrops(TROPICALWILDFRUIT_DROPS.get())));
        wildDrops.put("flavor_immersed_daily:wildflowerandleaf",
                mergeExtraList(ModDrops.appraisalDefaults.getOrDefault("flavor_immersed_daily:wildflowerandleaf", List.of()), parseDrops(WILDFLOWERANDLEAF_DROPS.get())));
        wildDrops.put("flavor_immersed_daily:wildfruitincoldzone",
                mergeExtraList(ModDrops.appraisalDefaults.getOrDefault("flavor_immersed_daily:wildfruitincoldzone", List.of()), parseDrops(WILDFRUITINCOLDZONE_DROPS.get())));
        wildDrops.put("flavor_immersed_daily:wildgrainplant",
                mergeExtraList(ModDrops.appraisalDefaults.getOrDefault("flavor_immersed_daily:wildgrainplant", List.of()), parseDrops(WILDGRAINPLANT_DROPS.get())));
        wildDrops.put("flavor_immersed_daily:wildmushroomplant",
                mergeExtraList(ModDrops.appraisalDefaults.getOrDefault("flavor_immersed_daily:wildmushroomplant", List.of()), parseDrops(WILDMUSHROOMPLANT_DROPS.get())));
        wildDrops.put("flavor_immersed_daily:wildseedplant",
                mergeExtraList(ModDrops.appraisalDefaults.getOrDefault("flavor_immersed_daily:wildseedplant", List.of()), parseDrops(WILDSEEDPLANT_DROPS.get())));
        wildDrops.put("flavor_immersed_daily:wildtuberplants",
                mergeExtraList(ModDrops.appraisalDefaults.getOrDefault("flavor_immersed_daily:wildtuberplants", List.of()), parseDrops(WILDTUBERPLANTS_DROPS.get())));
    }

// 组装屠宰掉落表：以 JSON 默认为基础，追加 config 额外产物。
// 为了兼容老玩家 config 中残留的旧默认值，与默认掉落重复的额外项会被忽略。
    private static Map<Integer, List<String>> buildButcherDrops(Map<Integer, List<String>> defaults, String... extrasRaw) {
        Map<Integer, List<String>> map = copyButcher(defaults);
        Set<String> defaultsIds = collectIds(map);
        for (int stage = 0; stage < extrasRaw.length; stage++) {
            addStageExtra(map, stage + 1, extrasRaw[stage], defaultsIds);
        }
        return map;
    }

// 鸡的屠宰流程阶段并不连续（1→2→右键，5→6→回收），逐阶段合并额外产物。
    private static Map<Integer, List<String>> buildChickenDrops(String drop1, String drop5, String drop6) {
        Map<Integer, List<String>> map = copyButcher(ModDrops.chickenDefaults);
        Set<String> defaultsIds = collectIds(map);
        addStageExtra(map, 1, drop1, defaultsIds);
        addStageExtra(map, 5, drop5, defaultsIds);
        addStageExtra(map, 6, drop6, defaultsIds);
        return map;
    }

    private static Map<Integer, List<String>> copyButcher(Map<Integer, List<String>> defaults) {
        Map<Integer, List<String>> map = new HashMap<>();
        for (Map.Entry<Integer, List<String>> entry : defaults.entrySet()) {
            map.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return map;
    }

    private static Set<String> collectIds(Map<Integer, List<String>> map) {
        Set<String> ids = new HashSet<>();
        for (List<String> list : map.values()) {
            ids.addAll(list);
        }
        return ids;
    }

    private static void addStageExtra(Map<Integer, List<String>> map, int stage, String raw, Set<String> defaultsIds) {
        map.computeIfAbsent(stage, ignored -> new ArrayList<>())
                .addAll(extraOnly(parseDrops(raw), defaultsIds));
    }

// 在默认列表基础上追加额外产物，并去除与默认重复的项。
    private static List<String> mergeExtraList(List<String> defaults, List<String> extras) {
        List<String> result = new ArrayList<>(defaults);
        Set<String> defaultsIds = new HashSet<>(defaults);
        result.addAll(extraOnly(extras, defaultsIds));
        return result;
    }

// 只保留不在 defaultsIds 中的额外项（空串忽略）。
    private static List<String> extraOnly(List<String> extras, Set<String> defaultsIds) {
        List<String> out = new ArrayList<>();
        for (String id : extras) {
            if (!id.isEmpty() && !defaultsIds.contains(id)) {
                out.add(id);
            }
        }
        return out;
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
        return mergeWildDrops(itemId);
    }

    public static Map<String, List<String>> getWildDropsMap() {
        Set<String> keys = new LinkedHashSet<>(wildDrops.keySet());
        keys.addAll(AppraisalSupplementRegistry.all().keySet());
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (String key : keys) {
            List<String> merged = mergeWildDrops(key);
            if (merged != null && !merged.isEmpty()) {
                result.put(key, merged);
            }
        }
        return Collections.unmodifiableMap(result);
    }

// 合并单个野生采集物的鉴定产物：本模组基础掉落（含玩家 config 额外项）
// + 数据包 appraisal_supplement 补充产物；与基础掉落重复的补充项忽略。
// 两者皆空时返回 null，调用方据此判定"不是可鉴定的野生采集物"。
    private static List<String> mergeWildDrops(String itemId) {
        List<String> base = wildDrops.get(itemId);
        List<String> supplements = AppraisalSupplementRegistry.getSupplements(itemId);
        if ((base == null || base.isEmpty()) && supplements.isEmpty()) {
            return null;
        }
        List<String> merged = new ArrayList<>();
        if (base != null) {
            merged.addAll(base);
        }
        Set<String> baseIds = base == null ? Collections.emptySet() : new HashSet<>(base);
        for (String id : supplements) {
            if (!id.isEmpty() && !baseIds.contains(id)) {
                merged.add(id);
            }
        }
        return merged;
    }
}
