package com.flavor_immersed_daily.block.block.tree;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

/**
 * 桂皮树苗 — 生长为两格高树干 + 顶部一个树叶的桂皮树
 */
public class CinnamonSaplingBlock extends BushBlock implements BonemealableBlock {

    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 12, 14);

    private final Supplier<? extends Block> woodSupplier;
    private final Supplier<? extends Block> leavesSupplier;

    public CinnamonSaplingBlock(Properties properties,
                                Supplier<? extends Block> wood,
                                Supplier<? extends Block> leaves) {
        super(properties);
        this.woodSupplier = wood;
        this.leavesSupplier = leaves;
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return simpleCodec(p -> new CinnamonSaplingBlock(p, woodSupplier, leavesSupplier));
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.DIRT) || state.is(Blocks.GRASS_BLOCK);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(7) == 0) {
            growTree(level, pos, state, random);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return (double) random.nextFloat() < 0.45;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        growTree(level, pos, state, random);
    }

    private void growTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        Block wood = woodSupplier.get();
        Block leaves = leavesSupplier.get();
        BlockState woodState = wood.defaultBlockState();

        // Check space: 2 blocks for trunk + 1 block for leaves above
        for (int i = 1; i <= 3; i++) {
            BlockPos abovePos = pos.above(i);
            if (!level.getBlockState(abovePos).canBeReplaced()) return;
        }

        // Place 2 trunk blocks
        level.setBlock(pos, woodState, 3);
        level.setBlock(pos.above(1), woodState, 3);

        // Place leaf on top
        level.setBlock(pos.above(2), leaves.defaultBlockState()
                .setValue(CinnamonLeavesBlock.PERSISTENT, true), 3);
    }
}