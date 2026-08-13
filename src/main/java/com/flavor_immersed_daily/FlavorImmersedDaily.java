package com.flavor_immersed_daily;

import com.flavor_immersed_daily.config.Config;
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
import static com.flavor_immersed_daily.all.ModItems.*;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceKey;
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

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(FlavorImmersedDaily.MODID)
public class FlavorImmersedDaily {
    public static final String MODID = "flavor_immersed_daily";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Registrate REGISTRATE = Registrate.create(MODID);

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
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Flavor Immersed Daily server starting");
    }

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
}
