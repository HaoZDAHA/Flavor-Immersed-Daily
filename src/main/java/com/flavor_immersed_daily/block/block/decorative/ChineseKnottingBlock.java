package com.flavor_immersed_daily.block.block.decorative;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

// 中国结，这个做的比较早，所以用的单独的一个java类，没啥特殊的
public class ChineseKnottingBlock extends HorizontalDirectionalBlock {

    private static final VoxelShape SHAPE_NORTH = Block.box(0, 0, 7, 16, 16, 9);
    private static final VoxelShape SHAPE_SOUTH = Block.box(0, 0, 7, 16, 16, 9);
    private static final VoxelShape SHAPE_EAST = Block.box(7, 0, 0, 9, 16, 16);
    private static final VoxelShape SHAPE_WEST = Block.box(7, 0, 0, 9, 16, 16);

    public ChineseKnottingBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
// 1.20.1 的 HorizontalDirectionalBlock 不再负责添加 FACING，需子类自行添加
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case EAST, WEST -> SHAPE_EAST;
            default -> SHAPE_NORTH;
        };
    }
}