package com.flavor_immersed_daily.block.block.fruit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(DISTANCE) == 3 ? SHAPE_STAGE2 : SHAPE_FULL;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(DISTANCE) == 3 ? SHAPE_STAGE2 : SHAPE_FULL;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
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
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {
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
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
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
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
                return ItemInteractionResult.SUCCESS;
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
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, 1);
        }

        return state;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
            return;
        }
        int dist = getDistance(level, pos);
        boolean bottom = isBottom(level, pos, dist);
        BlockState newState = state.setValue(DISTANCE, dist).setValue(BOTTOM, bottom);
        if (newState.getValue(DISTANCE) != state.getValue(DISTANCE) || newState.getValue(BOTTOM) != state.getValue(BOTTOM)) {
            level.setBlock(pos, newState, 3);
        }
    }

    private int getDistance(Level level, BlockPos pos) {
        int verticalDist = 0;
        BlockPos checkPos = pos.below();

        while (true) {
            Block belowBlock = level.getBlockState(checkPos).getBlock();
            if (belowBlock instanceof TrellisBlock) {
                int belowDist = level.getBlockState(checkPos).getValue(DISTANCE);
                if (belowDist != 3) {
                    verticalDist++;
                }
            } else if (belowBlock instanceof GrapeBlock) {
                verticalDist++;
            } else {
                break;
            }
            checkPos = checkPos.below();
        }

        if (verticalDist == 0) {
            BlockState belowState = level.getBlockState(pos.below());
            if (!belowState.isFaceSturdy(level, pos.below(), Direction.UP)) {
                // Check horizontal neighbors
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    BlockState sideState = level.getBlockState(pos.relative(dir));
                    if (sideState.getBlock() instanceof TrellisBlock) {
                        int sideDist = sideState.getValue(DISTANCE);
                        if (sideDist == 2 || sideDist == 3) {
                            return 3;
                        }
                    }
                }
            }
        }

        return Math.min(verticalDist, 2);
    }

    private boolean isBottom(Level level, BlockPos pos, int distance) {
        if (distance == 3) return false;
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        return belowState.isFaceSturdy(level, belowPos, Direction.UP)
                || (belowState.getBlock() instanceof TrellisBlock && belowState.getValue(BOTTOM));
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        // Supported by solid block below
        if (belowState.isFaceSturdy(level, belowPos, Direction.UP)) {
            return true;
        }

        // Supported by trellis or grapeblock below
        if (belowState.getBlock() instanceof TrellisBlock || belowState.getBlock() instanceof GrapeBlock) {
            return true;
        }

        // Horizontal pieces (distance=3) can be supported by side neighbors
        if (state.getValue(DISTANCE) == 3) {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockState sideState = level.getBlockState(pos.relative(dir));
                if (sideState.getBlock() instanceof TrellisBlock) {
                    int sideDist = sideState.getValue(DISTANCE);
                    if (sideDist == 2 || sideDist == 3) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            // Schedule block above to re-check canSurvive
            if (!level.isClientSide) {
                BlockPos abovePos = pos.above();
                BlockState aboveState = level.getBlockState(abovePos);
                if (aboveState.getBlock() instanceof TrellisBlock) {
                    level.scheduleTick(abovePos, this, 1);
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
