package com.flavor_immersed_daily.block.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

// 通用悬挂装饰方块 — 需要上方方块支撑才能放置，失去支撑时自动破坏；可指定碰撞箱。
// 适用于青玉灯笼、灯谜牌等挂件。
public class HangingDecorativeBlock extends Block {

    public static final MapCodec<HangingDecorativeBlock> CODEC = simpleCodec(HangingDecorativeBlock::new);

    private final VoxelShape shape;

    public HangingDecorativeBlock(Properties properties, VoxelShape shape) {
        super(properties);
        this.shape = shape;
    }

    public HangingDecorativeBlock(Properties properties) {
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

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
// 只能贴在上方实心方块的底面悬挂，否则无法放置
        return canSurvive(defaultBlockState(), context.getLevel(), context.getClickedPos())
                ? defaultBlockState()
                : null;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos above = pos.above();
        return level.getBlockState(above).isFaceSturdy(level, above, Direction.DOWN);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP && !canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }
}
