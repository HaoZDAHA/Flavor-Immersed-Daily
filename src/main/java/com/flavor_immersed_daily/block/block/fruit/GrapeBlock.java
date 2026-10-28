package com.flavor_immersed_daily.block.block.fruit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GrapeBlock extends Block implements SimpleWaterloggedBlock, BonemealableBlock {

    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 4);
    public static final int MAX_STAGE = 4;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE_FULL = Block.box(0, 0, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_STAGE2 = Block.box(0, 13, 0, 16, 15, 16);

// Seed-to-crop bindings used by TrellisBlock for planting.
    public static final List<GrapeBlock> REGISTERED_CROPS = new ArrayList<>();

    private final Supplier<? extends ItemLike> seedSupplier;
    private final Supplier<? extends ItemLike> cropSupplier;

    public GrapeBlock(Properties properties, Supplier<? extends ItemLike> seedSupplier,
                      Supplier<? extends ItemLike> cropSupplier) {
        super(properties);
        this.seedSupplier = seedSupplier;
        this.cropSupplier = cropSupplier;
        REGISTERED_CROPS.add(this);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(STAGE, 0)
                .setValue(WATERLOGGED, false));
    }

    public boolean isSeed(ItemStack stack) {
        return stack.is(seedSupplier.get().asItem());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(STAGE) == 2 ? SHAPE_STAGE2 : SHAPE_FULL;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(STAGE) == 2 ? SHAPE_STAGE2 : SHAPE_FULL;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int stage = state.getValue(STAGE);

        switch (stage) {
            case 0:
// Try to grow upward: convert trellis above
                tryGrowUpward(state, level, pos, random);
                break;
            case 1:
// Spread to surrounding trellis stage2, then try grow upward
                spreadToSurroundingTrellis(state, level, pos);
                tryGrowUpward(state, level, pos, random);
                break;
            case 2:
// Spread, then grow to stage 3
                spreadToSurroundingTrellis(state, level, pos);
                if (random.nextInt(4) == 0) {
                    level.setBlock(pos, state.setValue(STAGE, 3), 3);
                }
                break;
            case 3:
// Spread, then grow to stage 4
                spreadToSurroundingTrellis(state, level, pos);
                if (random.nextInt(4) == 0) {
                    level.setBlock(pos, state.setValue(STAGE, 4), 3);
                }
                break;
            case 4:
// Spread only
                spreadToSurroundingTrellis(state, level, pos);
                break;
        }
    }

    private void tryGrowUpward(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);

        if (aboveState.getBlock() instanceof TrellisBlock trellis) {
            int trellisDist = aboveState.getValue(TrellisBlock.DISTANCE);
// trellis distance=0,1 → grapeblock stage0 (前两层)
            if (trellisDist == 0 || trellisDist == 1) {
                if (random.nextInt(4) == 0) {
                    level.setBlock(abovePos, this.defaultBlockState()
                            .setValue(STAGE, 0)
                            .setValue(WATERLOGGED, aboveState.getValue(TrellisBlock.WATERLOGGED)), 3);
                }
            }
// trellis distance=2 → grapeblock stage1 (第三层，可横向扩展)
            else if (trellisDist == 2) {
                if (random.nextInt(4) == 0) {
                    level.setBlock(abovePos, this.defaultBlockState()
                            .setValue(STAGE, 1)
                            .setValue(WATERLOGGED, aboveState.getValue(TrellisBlock.WATERLOGGED)), 3);
                }
            }
        }
    }

    private void spreadToSurroundingTrellis(BlockState state, Level level, BlockPos pos) {
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos sidePos = pos.relative(dir);
            BlockState sideState = level.getBlockState(sidePos);

            if (sideState.getBlock() instanceof TrellisBlock && sideState.getValue(TrellisBlock.DISTANCE) == 3) {
                level.setBlock(sidePos, this.defaultBlockState()
                        .setValue(STAGE, 2)
                        .setValue(WATERLOGGED, sideState.getValue(TrellisBlock.WATERLOGGED)), 3);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        int stage = state.getValue(STAGE);

// 手持作物架点击侧面 → 在最高层横向拓展一段作物架（distance=3）
        if (stack.getItem() instanceof net.minecraft.world.item.BlockItem blockItem
                && blockItem.getBlock() instanceof TrellisBlock) {
            Direction face = hitResult.getDirection();
            if (face.getAxis().isHorizontal()) {
                BlockPos sidePos = pos.relative(face);
                if (level.isEmptyBlock(sidePos) || level.getBlockState(sidePos).canBeReplaced()) {
                    if (!level.isClientSide) {
                        level.setBlock(sidePos, blockItem.getBlock().defaultBlockState()
                                .setValue(TrellisBlock.DISTANCE, 3)
                                .setValue(TrellisBlock.BOTTOM, false)
                                .setValue(TrellisBlock.WATERLOGGED,
                                        level.getFluidState(sidePos).getType() == Fluids.WATER), 3);
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
                return InteractionResult.SUCCESS;
            }
        }

// Harvest at stage 4: drop crop item, revert to stage 2
        if (stage == MAX_STAGE) {
            this.harvest(state, level, pos);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.use(state, level, pos, player, hand, hitResult);
    }

// 成熟收获：掉落果实并把方块退回 stage 2（可继续生长）。
// 该方法为 public，供右键交互与 FTB Ultimine 连锁收获共用。
    public void harvest(BlockState state, Level level, BlockPos pos) {
        if (level.isClientSide) return;
        ItemLike crop = cropSupplier.get();
        ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                new ItemStack(crop != null ? crop : seedSupplier.get()));
        level.addFreshEntity(itemEntity);
        level.setBlock(pos, state.setValue(STAGE, 2), 3);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide) {
// 掉落作物架 + 对应种子
                Item trellisItem = BuiltInRegistries.ITEM.get(new ResourceLocation("flavor_immersed_daily", "trellis"));
                if (trellisItem != null) {
                    popResource(level, pos, new ItemStack(trellisItem));
                }
                ItemLike seed = seedSupplier.get();
                if (seed != null) {
                    popResource(level, pos, new ItemStack(seed));
                }
                BlockPos abovePos = pos.above();
                BlockState aboveState = level.getBlockState(abovePos);
                if (aboveState.getBlock() instanceof TrellisBlock) {
                    level.scheduleTick(abovePos, aboveState.getBlock(), 1);
                }
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
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
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

// BonemealableBlock

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClientSide) {
        return state.getValue(STAGE) < MAX_STAGE;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int stage = state.getValue(STAGE);

        if (stage < 2) {
// Accelerate upward growth
            tryGrowUpward(state, level, pos, random);
            tryGrowUpward(state, level, pos, random);
        } else if (stage < MAX_STAGE) {
// Grow one stage
            level.setBlock(pos, state.setValue(STAGE, stage + 1), 3);
            spreadToSurroundingTrellis(state, level, pos);
        }

        spreadToSurroundingTrellis(state, level, pos);
    }
}
