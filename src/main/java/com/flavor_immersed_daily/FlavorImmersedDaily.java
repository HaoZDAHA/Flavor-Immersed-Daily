package com.flavor_immersed_daily;

import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.command.FidDebugCommands;
import com.flavor_immersed_daily.network.ModPayloads;
import com.flavor_immersed_daily.all.ModCreateTab;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.all.ModBlockEntities;
import com.flavor_immersed_daily.all.ModEntities;
import com.flavor_immersed_daily.all.ModMenus;
import com.flavor_immersed_daily.recipe.ModRecipes;
import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.all.ModSounds;
import com.flavor_immersed_daily.integration.thirst.FIDThirstIntegration;
import static com.flavor_immersed_daily.all.ModItems.*;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
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

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(FlavorImmersedDaily.MODID)
public class FlavorImmersedDaily {
    public static final String MODID = "flavor_immersed_daily";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Registrate REGISTRATE = Registrate.create(MODID);

    /** 滚烫的糯米攻击玩家时的 y 轴加速度（猛跳高度） */
    private static final double HOT_GLUTINOUS_JUMP_SPEED = 1.5;
    /** 滚烫的糯米攻击生物时附加邪祟暴露效果的时长（tick） */
    private static final int EXPOSE_EVIL_DURATION_TICKS = 200;

    static {
        REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
        REGISTRATE.addDataGenerator(ProviderType.ITEM_MODEL,
                com.flavor_immersed_daily.datagen.FIDItemModelProvider::generateAllModels);
        REGISTRATE.addDataGenerator(ProviderType.LANG,
                com.flavor_immersed_daily.datagen.lang.ModLangs::addTranslations);
        ModBlocks.bootstrap();
    }

    public FlavorImmersedDaily(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(ModPayloads::register);
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

        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // 可选集成：Thirst Was Reclaimed（软依赖）——仅当玩家装了口渴模组时才注册其事件监听。
        // 守卫保证未安装时 FIDThirstIntegration 类永远不会被加载（该类硬引用了 thirst 的类），
        // 注册发生在 mod 构造期，而 thirst 的 RegisterThirstValueEvent 在 ServerStartedEvent 才触发，
        // 因此这里注册一定赶得上。
        if (ModList.get().isLoaded("thirst")) {
            NeoForge.EVENT_BUS.register(FIDThirstIntegration.class);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Flavor Immersed Daily server starting");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        FidDebugCommands.register(event.getDispatcher());
    }

    private void setupCommon(FMLCommonSetupEvent event) {
        // 堆肥能力已在 data/neoforge/data_maps/item/compostables.json 中声明，
        // 由 NeoForge 的 Compostable datamap 在数据包加载时注册（运行时改 map 会被 bootStrap 覆盖）。
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
                    victim.addEffect(new MobEffectInstance(ModEffects.EXPOSE_EVIL, EXPOSE_EVIL_DURATION_TICKS, 0));
                } else {
                    target.igniteForSeconds(3);
                    target.addEffect(new MobEffectInstance(ModEffects.EXPOSE_EVIL, EXPOSE_EVIL_DURATION_TICKS, 0));
                }
            }
            return;
        }

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
}
