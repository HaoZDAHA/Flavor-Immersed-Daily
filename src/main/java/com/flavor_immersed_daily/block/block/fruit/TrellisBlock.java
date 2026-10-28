package com.flavor_immersed_daily.block.block.fruit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TrellisBlock extends Block implements SimpleWaterloggedBlock {

    public static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 0, 3);
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE_FULL = Block.box(0, 0, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_STAGE2 = Block.box(0, 13, 0, 16, 15, 16);

    public TrellisBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(DISTANCE, 0)
                .setValue(BOTTOM, true)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, BOTTOM, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
// 所有形态的点击指示区均为整方块，避免误触
        return SHAPE_FULL;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(DISTANCE) == 3 ? SHAPE_STAGE2 : SHAPE_FULL;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

// Check what's below
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        boolean bottom = belowState.isFaceSturdy(level, belowPos, Direction.UP);

// Count consecutive trellis or grape blocks below
        int dist = 0;
        BlockPos checkPos = pos.below();
        while (true) {
            Block belowBlock = level.getBlockState(checkPos).getBlock();
            if (belowBlock instanceof TrellisBlock) {
                int belowDist = level.getBlockState(checkPos).getValue(DISTANCE);
                if (belowDist != 3) {
                    dist++;
                }
            } else if (belowBlock instanceof GrapeBlock) {
                dist++;
            } else {
                break;
            }
            checkPos = checkPos.below();
        }
        dist = Math.min(dist, 2);

        return this.defaultBlockState()
                .setValue(DISTANCE, dist)
                .setValue(BOTTOM, bottom)
                .setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
// Any registered seed on trellis stage0 → convert to corresponding GrapeBlock stage0
        if (state.getValue(DISTANCE) == 0) {
            for (GrapeBlock grapeBlock : GrapeBlock.REGISTERED_CROPS) {
                if (grapeBlock.isSeed(stack)) {
                    if (!level.isClientSide) {
                        level.setBlock(pos, grapeBlock.defaultBlockState()
                                .setValue(GrapeBlock.STAGE, 0)
                                .setValue(GrapeBlock.WATERLOGGED, state.getValue(WATERLOGGED)), 3);
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }

        if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() == this) {
            Direction face = hitResult.getDirection();
            int currentDist = state.getValue(DISTANCE);

// Horizontal extension: from layer 3 (distance=2) or other horizontal pieces (distance=3)
            if (face.getAxis().isHorizontal() && (currentDist == 2 || currentDist == 3)) {
                BlockPos sidePos = pos.relative(face);
                if (level.isEmptyBlock(sidePos) || level.getBlockState(sidePos).canBeReplaced()) {
                    if (!level.isClientSide) {
                        level.setBlock(sidePos,
                                this.defaultBlockState()
                                        .setValue(DISTANCE, 3)
                                        .setValue(BOTTOM, false)
                                        .setValue(WATERLOGGED, level.getFluidState(sidePos).getType() == Fluids.WATER),
                                3);
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
                return InteractionResult.SUCCESS;
            }

// Stack upward: any face click, if distance < 2
            if (currentDist < 2) {
                BlockPos abovePos = pos.above();
                if (level.isEmptyBlock(abovePos) || level.getBlockState(abovePos).canBeReplaced()) {
                    if (!level.isClientSide) {
                        level.setBlock(abovePos,
                                this.defaultBlockState()
                                        .setValue(DISTANCE, currentDist + 1)
                                        .setValue(BOTTOM, false)
                                        .setValue(WATERLOGGED, level.getFluidState(abovePos).getType() == Fluids.WATER),
                                3);
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return state;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
// 作物架不受重力影响：可浮空放置、自由拆解，不会因支撑消失而掉落
        return true;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
