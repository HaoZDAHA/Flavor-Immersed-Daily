package com.flavor_immersed_daily.block.block.tree;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

//桂皮树叶

public class CinnamonLeavesBlock extends LeavesBlock {

    public CinnamonLeavesBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends LeavesBlock> codec() {
        return simpleCodec(CinnamonLeavesBlock::new);
    }
}