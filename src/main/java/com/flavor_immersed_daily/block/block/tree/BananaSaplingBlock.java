package com.flavor_immersed_daily.block.block.tree;

import com.flavor_immersed_daily.block.block.fruit.RawBananaBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class BananaSaplingBlock extends BushBlock implements BonemealableBlock {

    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 12, 14);

    private final Supplier<? extends Block> woodSupplier;
    private final Supplier<? extends Block> fruitSupplier;

    public BananaSaplingBlock(Properties properties, Supplier<? extends Block> wood, Supplier<? extends Block> fruit) {
        super(properties);
        this.woodSupplier = wood;
        this.fruitSupplier = fruit;
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return simpleCodec(p -> new BananaSaplingBlock(p, woodSupplier, fruitSupplier));
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.DIRT) || state.is(Blocks.GRASS_BLOCK) || state.is(woodSupplier.get());
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
        // Only grow when on dirt/grass, not when on bananawood (crown)
        BlockState below = level.getBlockState(pos.below());
        if (!below.is(woodSupplier.get()) && random.nextInt(7) == 0) {
            growTree(level, pos, state, random);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        // Can't bonemeal when on bananawood crown
        BlockState below = level.getBlockState(pos.below());
        return !below.is(woodSupplier.get());
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return (double) random.nextFloat() < 0.45;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        growTree(level, pos, state, random);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        //香蕉生长的时候 没有依靠的方块就破坏掉
        if (!level.isClientSide) {
            BlockState below = level.getBlockState(pos.below());
            if (below.is(woodSupplier.get())) {
                level.removeBlock(pos, false);
                return state;
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
        //香蕉木
        BlockState below = level.getBlockState(pos.below());
        if (!below.is(woodSupplier.get())) {
            super.spawnAfterBreak(state, level, pos, tool, dropExperience);
        }
    }

    private void growTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        Block wood = woodSupplier.get();
        Block fruit = fruitSupplier.get();
        BlockState woodState = wood.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);

        //检查生长有没有足够的空间
        for (int i = 1; i <= 4; i++) {
            BlockPos abovePos = pos.above(i);
            BlockState aboveState = level.getBlockState(abovePos);
            if (i <= 3 && !aboveState.canBeReplaced()) return;
            if (i == 4 && !aboveState.isAir()) return;
        }

        //生成木头
        level.setBlock(pos, woodState, 3);            //第一层
        level.setBlock(pos.above(1), woodState, 3);   //第二层
        level.setBlock(pos.above(2), woodState, 3);   //第三层

        //四边的生成
        BlockPos fruitBase = pos.above(2);
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos fruitPos = fruitBase.relative(dir);
            if (level.getBlockState(fruitPos).canBeReplaced()) {
                level.setBlock(fruitPos, fruit.defaultBlockState()
                        .setValue(RawBananaBlock.FACING, dir.getOpposite()), 3);
            }
        }

        //顶上的叶子
        level.setBlock(pos.above(3), this.defaultBlockState(), 3);
    }
}
