package com.fidtest.registration;

import com.fidtest.ExampleMod;
import com.fidtest.block.*;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ExampleMod.MODID);

    private static BlockBehaviour.Properties props(MapColor color, SoundType sound) {
        return BlockBehaviour.Properties.of().mapColor(color).strength(2.0F, 3.0F).sound(sound).noOcclusion();
    }

    private static BlockBehaviour.Properties steamboxProps() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.0F, 3.0F).sound(SoundType.WOOD).noOcclusion();
    }

    public static final DeferredBlock<SteamboxBlock>             STEAMBOX         = BLOCKS.register("steambox",         () -> new SteamboxBlock(steamboxProps()));
    public static final DeferredBlock<WokBlock>                  WOK              = BLOCKS.register("wok",              () -> new WokBlock(props(MapColor.METAL, SoundType.METAL)));
    public static final DeferredBlock<PressurecookerBlock>       PRESSURECOOKER   = BLOCKS.register("pressurecooker",   () -> new PressurecookerBlock(props(MapColor.METAL, SoundType.METAL)));
    public static final DeferredBlock<ChoppingboardBlock>        CHOPPINGBOARD    = BLOCKS.register("choppingboard",    () -> new ChoppingboardBlock(props(MapColor.WOOD, SoundType.WOOD)));
    public static final DeferredBlock<FryingpanBlock>            FRYINGPAN        = BLOCKS.register("fryingpan",        () -> new FryingpanBlock(props(MapColor.METAL, SoundType.METAL)));
    public static final DeferredBlock<DryerBlock>                DRYER            = BLOCKS.register("dryer",            () -> new DryerBlock(props(MapColor.WOOD, SoundType.WOOD)));
    public static final DeferredBlock<SqueezingmachineBlock>    SQUEEZINGMACHINE = BLOCKS.register("squeezingmachine", () -> new SqueezingmachineBlock(props(MapColor.METAL, SoundType.METAL)));
    public static final DeferredBlock<JarBlock>                  JAR              = BLOCKS.register("jar",              () -> new JarBlock(props(MapColor.STONE, SoundType.GLASS)));
    public static final DeferredBlock<TeapotBlock>               TEAPOT           = BLOCKS.register("teapot",           () -> new TeapotBlock(props(MapColor.STONE, SoundType.STONE)));
    public static final DeferredBlock<PotBlock>                  POT              = BLOCKS.register("pot",              () -> new PotBlock(props(MapColor.METAL, SoundType.METAL)));
    public static final DeferredBlock<MortarBlock>               MORTAR           = BLOCKS.register("mortar",           () -> new MortarBlock(props(MapColor.STONE, SoundType.STONE)));
    public static final DeferredBlock<MeatmincerBlock>           MEATMINCER       = BLOCKS.register("meatmincer",       () -> new MeatmincerBlock(props(MapColor.METAL, SoundType.METAL)));
}
