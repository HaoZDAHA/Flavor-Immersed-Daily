package com.flavor_immersed_daily.block.block.tree;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

//椰子树苗，树形并不好看，大家不要参考形状

public class CoconutSaplingBlock extends BushBlock implements BonemealableBlock {

    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 12, 14);

    private final Supplier<? extends Block> woodSupplier;
    private final Supplier<? extends Block> leavesSupplier;
    private final Supplier<? extends Block> fruitingLeavesSupplier;
    private final Supplier<? extends Block> rawCoconutSupplier;

    public CoconutSaplingBlock(Properties properties,
                                Supplier<? extends Block> wood,
                                Supplier<? extends Block> leaves,
                                Supplier<? extends Block> fruitingLeaves,
                                Supplier<? extends Block> rawCoconut) {
        super(properties);
        this.woodSupplier = wood;
        this.leavesSupplier = leaves;
        this.fruitingLeavesSupplier = fruitingLeaves;
        this.rawCoconutSupplier = rawCoconut;
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return simpleCodec(p -> new CoconutSaplingBlock(p, woodSupplier, leavesSupplier, fruitingLeavesSupplier, rawCoconutSupplier));
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.DIRT) || state.is(Blocks.SAND) || state.is(Blocks.GRASS_BLOCK);
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
        Block fruitingLeaves = fruitingLeavesSupplier.get();
        Block rawCoconut = rawCoconutSupplier.get();
        BlockState woodState = wood.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);

        int trunkHeight = 6 + random.nextInt(3); // 6-8
        Direction[] horizDirs = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        Direction bendDir = horizDirs[random.nextInt(4)];

        // 检查空间：直干部分
        for (int i = 1; i <= trunkHeight - 3; i++) {
            if (!level.getBlockState(pos.above(i)).canBeReplaced()) return;
        }
        // 检查空间：弯曲部分 + 树顶
        for (int i = trunkHeight - 3; i < trunkHeight; i++) {
            int distFromTop = trunkHeight - 1 - i;
            int bend = distFromTop <= 2 ? (2 - distFromTop) + 1 : 0; // 3rd-last=1, 2nd-last=2, top=3
            BlockPos checkPos = pos.above(i).relative(bendDir, bend);
            if (!level.getBlockState(checkPos).canBeReplaced()) return;
        }
        // 检查树顶叶子空间
        {
            BlockPos topPos = pos.above(trunkHeight - 1).relative(bendDir, 3);
            if (!level.getBlockState(topPos.above()).canBeReplaced()) return;
            for (Direction dir : horizDirs) {
                if (!level.getBlockState(topPos.above().relative(dir)).canBeReplaced()) return;
            }
        }

        // 放置树干
        for (int i = 0; i < trunkHeight; i++) {
            int distFromTop = trunkHeight - 1 - i;
            BlockPos trunkPos;
            if (distFromTop <= 2) {
                int bend = (2 - distFromTop) + 1;
                trunkPos = pos.above(i).relative(bendDir, bend);
            } else {
                trunkPos = pos.above(i);
            }
            level.setBlock(trunkPos, woodState, 3);
        }

        // 树顶位置
        BlockPos topPos = pos.above(trunkHeight - 1).relative(bendDir, 3);
        // 放置普通树叶：中心 + 十字
        level.setBlock(topPos.above(), leaves.defaultBlockState(), 3);
        for (Direction dir : horizDirs) {
            level.setBlock(topPos.above().relative(dir), leaves.defaultBlockState(), 3);
        }

        // 随机选一个方向放结果树叶 + 挂椰子
        Direction fruitDir = horizDirs[random.nextInt(4)];
        BlockPos fruitLeafPos = topPos.above().relative(fruitDir);
        level.setBlock(fruitLeafPos, fruitingLeaves.defaultBlockState(), 3);
        // 结果树叶下方悬挂 rawcoconut
        BlockPos coconutPos = fruitLeafPos.below();
        if (level.getBlockState(coconutPos).canBeReplaced()) {
            level.setBlock(coconutPos, rawCoconut.defaultBlockState(), 3);
        }
    }
}
