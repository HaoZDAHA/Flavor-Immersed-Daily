package com.fidtest.registration;

import com.fidtest.FidworkMod;
import com.fidtest.block.entity.ChoppingboardBlockEntity;
import com.fidtest.block.entity.DryerBlockEntity;
import com.fidtest.block.entity.FryingpanBlockEntity;
import com.fidtest.block.entity.JarBlockEntity;
import com.fidtest.block.entity.MeatmincerBlockEntity;
import com.fidtest.block.entity.MortarBlockEntity;
import com.fidtest.block.entity.PotBlockEntity;
import com.fidtest.block.entity.PressurecookerBlockEntity;
import com.fidtest.block.entity.SqueezingmachineBlockEntity;
import com.fidtest.block.entity.SteamboxBlockEntity;
import com.fidtest.block.entity.TeapotBlockEntity;
import com.fidtest.block.entity.WokBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FidworkMod.MODID);

    public static final RegistryObject<BlockEntityType<SteamboxBlockEntity>> STEAMBOX =
            BLOCK_ENTITIES.register("steambox",
                    () -> BlockEntityType.Builder.of(SteamboxBlockEntity::new, ModBlocks.STEAMBOX.get()).build(null));
    public static final RegistryObject<BlockEntityType<WokBlockEntity>> WOK =
            BLOCK_ENTITIES.register("wok",
                    () -> BlockEntityType.Builder.of(WokBlockEntity::new, ModBlocks.WOK.get()).build(null));
    public static final RegistryObject<BlockEntityType<ChoppingboardBlockEntity>> CHOPPINGBOARD =
            BLOCK_ENTITIES.register("choppingboard",
                    () -> BlockEntityType.Builder.of(ChoppingboardBlockEntity::new, ModBlocks.CHOPPINGBOARD.get()).build(null));
    public static final RegistryObject<BlockEntityType<MortarBlockEntity>> MORTAR =
            BLOCK_ENTITIES.register("mortar",
                    () -> BlockEntityType.Builder.of(MortarBlockEntity::new, ModBlocks.MORTAR.get()).build(null));
    public static final RegistryObject<BlockEntityType<PotBlockEntity>> POT =
            BLOCK_ENTITIES.register("pot",
                    () -> BlockEntityType.Builder.of(PotBlockEntity::new, ModBlocks.POT.get()).build(null));
    public static final RegistryObject<BlockEntityType<PressurecookerBlockEntity>> PRESSURECOOKER =
            BLOCK_ENTITIES.register("pressurecooker",
                    () -> BlockEntityType.Builder.of(PressurecookerBlockEntity::new, ModBlocks.PRESSURECOOKER.get()).build(null));
    public static final RegistryObject<BlockEntityType<SqueezingmachineBlockEntity>> SQUEEZINGMACHINE =
            BLOCK_ENTITIES.register("squeezingmachine",
                    () -> BlockEntityType.Builder.of(SqueezingmachineBlockEntity::new, ModBlocks.SQUEEZINGMACHINE.get()).build(null));
    public static final RegistryObject<BlockEntityType<FryingpanBlockEntity>> FRYINGPAN =
            BLOCK_ENTITIES.register("fryingpan",
                    () -> BlockEntityType.Builder.of(FryingpanBlockEntity::new, ModBlocks.FRYINGPAN.get()).build(null));
    public static final RegistryObject<BlockEntityType<DryerBlockEntity>> DRYER =
            BLOCK_ENTITIES.register("dryer",
                    () -> BlockEntityType.Builder.of(DryerBlockEntity::new, ModBlocks.DRYER.get()).build(null));
    public static final RegistryObject<BlockEntityType<JarBlockEntity>> JAR =
            BLOCK_ENTITIES.register("jar",
                    () -> BlockEntityType.Builder.of(JarBlockEntity::new, ModBlocks.JAR.get()).build(null));
    public static final RegistryObject<BlockEntityType<TeapotBlockEntity>> TEAPOT =
            BLOCK_ENTITIES.register("teapot",
                    () -> BlockEntityType.Builder.of(TeapotBlockEntity::new, ModBlocks.TEAPOT.get()).build(null));
    public static final RegistryObject<BlockEntityType<MeatmincerBlockEntity>> MEATMINCER =
            BLOCK_ENTITIES.register("fid_meatmincer",
                    () -> BlockEntityType.Builder.of(MeatmincerBlockEntity::new, ModBlocks.MEATMINCER.get()).build(null));
}
