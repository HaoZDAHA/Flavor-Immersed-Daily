package com.flavor_immersed_daily.block.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

/**
 * 菌菇作物方块 — 种植在木头/原木上，非耕地。
 * 生长阶段 0 到 maxAge，成熟后可右键收获产物并重置。
 */
public class FIDLogMushroomBlock extends BushBlock implements BonemealableBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_4;

    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.box(4, 0, 4, 12, 4, 12),
            Block.box(4, 0, 4, 12, 7, 12),
            Block.box(3, 0, 3, 13, 10, 13),
            Block.box(2, 0, 2, 14, 13, 14),
            Block.box(2, 0, 2, 14, 15, 14),
    };

    private final int maxAge;
    private final Supplier<? extends ItemLike> seedSupplier;
    private final Supplier<? extends ItemLike> cropSupplier;

    public FIDLogMushroomBlock(Properties properties, int maxAge, Supplier<? extends ItemLike> seedSupplier,
                               Supplier<? extends ItemLike> cropSupplier) {
        super(properties);
        this.maxAge = maxAge;
        this.seedSupplier = seedSupplier;
        this.cropSupplier = cropSupplier;
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    public int getMaxAge() {
        return maxAge;
    }

    public ItemLike getCropItem() {
        return cropSupplier.get();
    }

    public ItemLike getSeedItem() {
        return seedSupplier.get();
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return simpleCodec(p -> new FIDLogMushroomBlock(p, maxAge, seedSupplier, cropSupplier));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.LOGS) || state.is(BlockTags.PLANKS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int age = state.getValue(AGE);
        return SHAPES[Math.min(age, SHAPES.length - 1)];
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < maxAge;
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
            if (!level.isClientSide) {
                ItemLike crop = cropSupplier.get();
                if (crop != null) {
                    Block.popResource(level, pos, new ItemStack(crop));
                    ItemLike seed = seedSupplier.get();
                    if (seed != null) {
                        Block.popResource(level, pos, new ItemStack(seed));
                    }
                }
                level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlock(pos, state.setValue(AGE, 0), Block.UPDATE_CLIENTS);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    // ===== BonemealableBlock =====

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
