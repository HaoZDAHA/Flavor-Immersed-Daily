package com.fidtest.registration;

import com.fidtest.ExampleMod;
import com.fidtest.block.entity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ExampleMod.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SteamboxBlockEntity>> STEAMBOX =
            BLOCK_ENTITIES.register("steambox", () -> BlockEntityType.Builder.of(SteamboxBlockEntity::new, ModBlocks.STEAMBOX.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WokBlockEntity>> WOK =
            BLOCK_ENTITIES.register("wok", () -> BlockEntityType.Builder.of(WokBlockEntity::new, ModBlocks.WOK.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PressurecookerBlockEntity>> PRESSURECOOKER =
            BLOCK_ENTITIES.register("pressurecooker", () -> BlockEntityType.Builder.of(PressurecookerBlockEntity::new, ModBlocks.PRESSURECOOKER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ChoppingboardBlockEntity>> CHOPPINGBOARD =
            BLOCK_ENTITIES.register("choppingboard", () -> BlockEntityType.Builder.of(ChoppingboardBlockEntity::new, ModBlocks.CHOPPINGBOARD.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FryingpanBlockEntity>> FRYINGPAN =
            BLOCK_ENTITIES.register("fryingpan", () -> BlockEntityType.Builder.of(FryingpanBlockEntity::new, ModBlocks.FRYINGPAN.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DryerBlockEntity>> DRYER =
            BLOCK_ENTITIES.register("dryer", () -> BlockEntityType.Builder.of(DryerBlockEntity::new, ModBlocks.DRYER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SqueezingmachineBlockEntity>> SQUEEZINGMACHINE =
            BLOCK_ENTITIES.register("squeezingmachine", () -> BlockEntityType.Builder.of(SqueezingmachineBlockEntity::new, ModBlocks.SQUEEZINGMACHINE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<JarBlockEntity>> JAR =
            BLOCK_ENTITIES.register("jar", () -> BlockEntityType.Builder.of(JarBlockEntity::new, ModBlocks.JAR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TeapotBlockEntity>> TEAPOT =
            BLOCK_ENTITIES.register("teapot", () -> BlockEntityType.Builder.of(TeapotBlockEntity::new, ModBlocks.TEAPOT.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PotBlockEntity>> POT =
            BLOCK_ENTITIES.register("pot", () -> BlockEntityType.Builder.of(PotBlockEntity::new, ModBlocks.POT.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MortarBlockEntity>> MORTAR =
            BLOCK_ENTITIES.register("mortar", () -> BlockEntityType.Builder.of(MortarBlockEntity::new, ModBlocks.MORTAR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MeatmincerBlockEntity>> MEATMINCER =
            BLOCK_ENTITIES.register("meatmincer", () -> BlockEntityType.Builder.of(MeatmincerBlockEntity::new, ModBlocks.MEATMINCER.get()).build(null));
}
