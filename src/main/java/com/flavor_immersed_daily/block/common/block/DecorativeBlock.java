package com.flavor_immersed_daily.block.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 通用装饰方块 — 仅需指定碰撞箱
 */
public class DecorativeBlock extends Block {

    public static final MapCodec<DecorativeBlock> CODEC = simpleCodec(DecorativeBlock::new);

    private final VoxelShape shape;

    public DecorativeBlock(Properties properties, VoxelShape shape) {
        super(properties);
        this.shape = shape;
    }

    public DecorativeBlock(Properties properties) {
        this(properties, Block.box(0, 0, 0, 16, 16, 16));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shape;
    }
}