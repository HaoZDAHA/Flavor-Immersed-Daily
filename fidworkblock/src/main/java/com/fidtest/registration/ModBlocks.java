package com.fidtest.registration;

import com.fidtest.FidworkMod;
import com.fidtest.block.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, FidworkMod.MODID);

    private static BlockBehaviour.Properties props(MapColor color, SoundType sound) {
        return BlockBehaviour.Properties.of().mapColor(color)
                .strength(2.0F, 3.0F).sound(sound).noOcclusion();
    }

    public static final RegistryObject<SteamboxBlock> STEAMBOX =
            BLOCKS.register("steambox", () -> new SteamboxBlock(
                    BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<WokBlock> WOK =
            BLOCKS.register("wok", () -> new WokBlock(props(MapColor.METAL, SoundType.METAL)));
    public static final RegistryObject<ChoppingboardBlock> CHOPPINGBOARD =
            BLOCKS.register("choppingboard", () -> new ChoppingboardBlock(props(MapColor.WOOD, SoundType.WOOD)));
    public static final RegistryObject<PotBlock> POT =
            BLOCKS.register("pot", () -> new PotBlock(props(MapColor.METAL, SoundType.METAL)));
    public static final RegistryObject<PressurecookerBlock> PRESSURECOOKER =
            BLOCKS.register("pressurecooker", () -> new PressurecookerBlock(props(MapColor.METAL, SoundType.METAL)));
    public static final RegistryObject<MortarBlock> MORTAR =
            BLOCKS.register("mortar", () -> new MortarBlock(props(MapColor.STONE, SoundType.STONE)));
    public static final RegistryObject<SqueezingmachineBlock> SQUEEZINGMACHINE =
            BLOCKS.register("squeezingmachine", () -> new SqueezingmachineBlock(props(MapColor.METAL, SoundType.METAL)));
    public static final RegistryObject<DryerBlock> DRYER =
            BLOCKS.register("dryer", () -> new DryerBlock(props(MapColor.WOOD, SoundType.WOOD)));
    public static final RegistryObject<FryingpanBlock> FRYINGPAN =
            BLOCKS.register("fryingpan", () -> new FryingpanBlock(props(MapColor.METAL, SoundType.METAL)));
    public static final RegistryObject<JarBlock> JAR =
            BLOCKS.register("jar", () -> new JarBlock(props(MapColor.STONE, SoundType.GLASS)));
    public static final RegistryObject<TeapotBlock> TEAPOT =
            BLOCKS.register("teapot", () -> new TeapotBlock(props(MapColor.STONE, SoundType.STONE)));
    public static final RegistryObject<MeatmincerBlock> MEATMINCER =
            BLOCKS.register("fid_meatmincer", () -> new MeatmincerBlock(props(MapColor.METAL, SoundType.METAL)));
}
