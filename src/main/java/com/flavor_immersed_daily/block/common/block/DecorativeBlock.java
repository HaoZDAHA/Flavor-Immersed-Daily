package com.flavor_immersed_daily.block.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

// 通用装饰方块 — 仅需指定碰撞箱
public class DecorativeBlock extends Block {

    private final VoxelShape shape;

    public DecorativeBlock(Properties properties, VoxelShape shape) {
        super(properties);
        this.shape = shape;
    }

    public DecorativeBlock(Properties properties) {
        this(properties, Block.box(0, 0, 0, 16, 16, 16));
    }
}