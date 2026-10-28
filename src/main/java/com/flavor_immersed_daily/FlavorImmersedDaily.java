package com.flavor_immersed_daily;

import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.command.FidDebugCommands;
import com.flavor_immersed_daily.network.ModMessages;
import com.flavor_immersed_daily.all.ModCreateTab;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.all.ModBlockEntities;
import com.flavor_immersed_daily.all.ModEntities;
import com.flavor_immersed_daily.all.ModMenus;
import com.flavor_immersed_daily.recipe.ModRecipes;
import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.all.ModSounds;
import com.flavor_immersed_daily.butchering.ButcheringRegistry;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import static com.flavor_immersed_daily.all.ModItems.*;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;

@Mod(FlavorImmersedDaily.MODID)
public class FlavorImmersedDaily {
    public static final String MODID = "flavor_immersed_daily";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Registrate REGISTRATE = Registrate.create(MODID);

// 滚烫的糯米攻击玩家时的 y 轴加速度（猛跳高度）
    private static final double HOT_GLUTINOUS_JUMP_SPEED = 1.5;
// 滚烫的糯米攻击生物时附加邪祟暴露效果的时长（tick）
    private static final int EXPOSE_EVIL_DURATION_TICKS = 200;

    static {
        REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
        REGISTRATE.addDataGenerator(ProviderType.ITEM_MODEL,
                com.flavor_immersed_daily.datagen.FIDItemModelProvider::generateAllModels);
        REGISTRATE.addDataGenerator(ProviderType.LANG,
                com.flavor_immersed_daily.datagen.lang.ModLangs::addTranslations);
        ModBlocks.bootstrap();
    }

    public FlavorImmersedDaily() {
// 注意：所有 DeferredRegister 必须注册到 Mod 事件总线，而不是 MinecraftForge.EVENT_BUS（游戏事件总线）
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(com.flavor_immersed_daily.datagen.FIDDataGenerators::gatherData);
        modEventBus.addListener(this::setupCommon);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreateTab.register(modEventBus);
        ModEffects.register(modEventBus);
        ModEntities.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenus.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModSounds.register(modEventBus);
// 附魔（无限砍伐）
        com.flavor_immersed_daily.all.ModEnchantments.register(modEventBus);
// 吴刚之斧的地牢宝箱战利品注入（Global Loot Modifier）
        com.flavor_immersed_daily.loot.WugangAxeLootModifier.register(modEventBus);

// 注册 COMMON 配置（缺失此调用则 Config.onLoad 永不触发，屠宰/漂洗/鉴定掉落表会全部为空）
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        MinecraftForge.EVENT_BUS.register(this);

// 可选联动：本模组不强依赖以下模组，仅在对方已加载时注册
// 注意：只用 ModList.isLoaded 判断并不够。整合包可能装的是旧版本联动模组，缺少我们编译时
// 使用的 API 类，此时加载联动类会抛 NoClassDefFoundError，导致整个模组构造失败（模组显示为
// ERROR 且完全不生效）。因此这里统一用 try/catch(Throwable) 兜底：联动不可用时只记一条警告，
// 绝不影响本模组本体。
// Thirst Was Taken：注册食物/饮品的口渴值
        if (ModList.get().isLoaded("thirst")) {
            try {
                MinecraftForge.EVENT_BUS.register(com.flavor_immersed_daily.integration.thirst.FIDThirstIntegration.class);
            } catch (Throwable t) {
                LOGGER.warn("Thirst integration unavailable, skipped", t);
            }
        }
// FTB Ultimine：连锁右键收割适配（RightClickHandler API 是 FTB Ultimine 2001.1.8 才新增的，
// 旧版本没有 dev.ftb.mods.ftbultimine.api.rightclick 包）
        if (ModList.get().isLoaded("ftbultimine")) {
            try {
                com.flavor_immersed_daily.gameplay.FTBUltimineCropHarvestHandler.register();
            } catch (Throwable t) {
                LOGGER.warn("FTB Ultimine chain-harvest integration unavailable (requires FTB Ultimine 2001.1.8+), skipped", t);
            }
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Flavor Immersed Daily server starting");
// FID 调试：打印实际扫描到的配方文件数与成功加载数
        try {
            int scanned = event.getServer().getResourceManager().listResources("recipes",
                    p -> p.getPath().endsWith(".json")).size();
            var ids = event.getServer().getRecipeManager().getRecipeIds().toList();
            LOGGER.info("FID DEBUG: scanned recipe files = {}, loaded recipes = {}", scanned, ids.size());
            java.util.Map<String, Long> byNs = ids.stream()
                    .collect(java.util.stream.Collectors.groupingBy(rl -> rl.getNamespace(), java.util.stream.Collectors.counting()));
            LOGGER.info("FID DEBUG: loaded recipes by namespace = {}", byNs);
        } catch (Exception e) {
            LOGGER.error("FID DEBUG: recipe stats failed", e);
        }
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        FidDebugCommands.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onAddReloadListener(AddReloadListenerEvent event) {
// 数据包自定义屠宰定义：加载所有命名空间 data/<ns>/butchering/*.json
        event.addListener(new ButcheringRegistry.ReloadListener());
// 数据包自定义木盆泡洗定义：加载所有命名空间 data/<ns>/basin_wash/*.json
        event.addListener(new com.flavor_immersed_daily.basin.BasinWashRegistry.ReloadListener());
// 数据包自定义农产鉴定补充产物：加载所有命名空间 data/<ns>/appraisal_supplement/*.json
        event.addListener(new com.flavor_immersed_daily.appraisal.AppraisalSupplementRegistry.ReloadListener());
    }

    private void setupCommon(FMLCommonSetupEvent event) {
// 注册网络通道（forge 1.20.1 SimpleChannel）
        ModMessages.register();
// 堆肥能力已在 data/MinecraftForge/data_maps/item/compostables.json 中声明，
// 由 MinecraftForge 的 Compostable datamap 在数据包加载时注册（运行时改 map 会被 bootStrap 覆盖）。
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (!(event.getTarget() instanceof LivingEntity target)) return;
        Player player = event.getEntity();
        ItemStack weapon = player.getMainHandItem();

// 滚烫的糯米：消耗一个，对生物点燃并附加邪祟暴露，对玩家施加猛跳与惨叫音效
        if (weapon.is(HOT_GLUTINOUS.get())) {
            if (!target.level().isClientSide) {
                weapon.shrink(1);
                if (target instanceof Player victim) {
                    Vec3 motion = victim.getDeltaMovement();
                    victim.setDeltaMovement(motion.x, HOT_GLUTINOUS_JUMP_SPEED, motion.z);
                    victim.hurtMarked = true;
                    victim.level().playSound(null, victim.getX(), victim.getY(), victim.getZ(),
                            ModSounds.GIAO.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
// 对玩家同样附加邪祟暴露效果
                    victim.addEffect(new MobEffectInstance(ModEffects.EXPOSE_EVIL.get(), EXPOSE_EVIL_DURATION_TICKS, 0));
                } else {
                    target.setSecondsOnFire(3);
                    target.addEffect(new MobEffectInstance(ModEffects.EXPOSE_EVIL.get(), EXPOSE_EVIL_DURATION_TICKS, 0));
                }
            }
            return;
        }

// 斩骨刀击杀动物 → 掉落对应的尸体物品（后续在挂钩上按方块状态屠宰）
        if (!weapon.is(BONECUTTERKNIFE.get())) return;

// 检查是否是活动物，如果是则转换为死亡动物
        Integer animalType = null;
        if (target.getType() == EntityType.COW) {
            animalType = 1;
        } else if (target.getType() == EntityType.SHEEP) {
            animalType = 2;
        } else if (target.getType() == EntityType.PIG) {
            animalType = 3;
        } else if (target.getType() == EntityType.CHICKEN) {
            animalType = 4;
        }

        if (animalType != null) {
            if (!target.level().isClientSide) {
                ItemStack deadItem = switch (animalType) {
                    case 1 -> new ItemStack(DEADCATTLE.get());
                    case 2 -> new ItemStack(DEADSHEEP.get());
                    case 3 -> new ItemStack(DEADPIG.get());
                    case 4 -> new ItemStack(DEADCHICKEN.get());
                    default -> null;
                };

                if (deadItem != null) {
// 生成死亡动物物品（尸体），屠宰流程后续在 BighookBlock 上按方块状态推进
                    ItemEntity itemEntity = new ItemEntity(target.level(), target.getX(), target.getY(), target.getZ(), deadItem);
                    target.level().addFreshEntity(itemEntity);

// 移除原始实体
                    target.discard();
                }
            }
            event.setCanceled(true);
        }
    }
}
