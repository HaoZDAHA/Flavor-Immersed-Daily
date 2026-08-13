package com.flavor_immersed_daily.block.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
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

import java.util.function.Supplier;

/**
 * 水生作物方块 — 种植在深度为1的水中，必须waterlogged才能生长。
 */
public class FIDWaterCropBlock extends Block implements BonemealableBlock, SimpleWaterloggedBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_4;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.box(2, 0, 2, 14, 4, 14),
            Block.box(2, 0, 2, 14, 7, 14),
            Block.box(2, 0, 2, 14, 10, 14),
            Block.box(1, 0, 1, 15, 13, 15),
            Block.box(1, 0, 1, 15, 16, 15),
    };

    private final int maxAge;
    private final Supplier<? extends ItemLike> seedSupplier;
    private final Supplier<? extends ItemLike> cropSupplier;

    public FIDWaterCropBlock(Properties properties, int maxAge, Supplier<? extends ItemLike> seedSupplier,
                             Supplier<? extends ItemLike> cropSupplier) {
        super(properties);
        this.maxAge = maxAge;
        this.seedSupplier = seedSupplier;
        this.cropSupplier = cropSupplier;
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return simpleCodec(p -> new FIDWaterCropBlock(p, maxAge, seedSupplier, cropSupplier));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        FluidState fluidState = level.getFluidState(pos);
        return this.defaultBlockState()
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        // Must be waterlogged (in water)
        if (!state.getValue(WATERLOGGED)) {
            return false;
        }
        // Water depth must be exactly 1: below must be solid (not water)
        BlockPos belowPos = pos.below();
        FluidState belowFluid = level.getFluidState(belowPos);
        return !belowFluid.isSource() || belowFluid.isEmpty();
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(AGE)];
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < maxAge && state.getValue(WATERLOGGED);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        if (age < maxAge && level.getRawBrightness(pos.above(), 0) >= 9) {
            if (random.nextInt((int) (25.0F / 3.0F) + 1) == 0) {
                level.setBlock(pos, state.setValue(AGE, age + 1), 2);
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hitResult) {
        if (state.getValue(AGE) >= maxAge) {
            ItemLike crop = cropSupplier.get();
            if (crop != null) {
                Block.popResource(level, pos, new ItemStack(crop));
                // Also drop some seeds
                ItemLike seed = seedSupplier.get();
                if (seed != null) {
                    Block.popResource(level, pos, new ItemStack(seed));
                }
            }
            level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(pos, state.setValue(AGE, 0), Block.UPDATE_CLIENTS);
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(AGE) < maxAge;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int age = state.getValue(AGE);
        level.setBlock(pos, state.setValue(AGE, Math.min(age + 1, maxAge)), 2);
    }
}
