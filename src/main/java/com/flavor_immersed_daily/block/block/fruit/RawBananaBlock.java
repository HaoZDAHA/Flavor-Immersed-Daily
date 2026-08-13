package com.flavor_immersed_daily.block.block.fruit;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class RawBananaBlock extends Block implements BonemealableBlock {

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 1);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE_STAGE0 = Block.box(0, 0, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_STAGE1 = Block.box(0, 0, 0, 16, 16, 16);

    private final Supplier<? extends Block> woodSupplier;
    private final Supplier<? extends Item> fruitItem;

    public RawBananaBlock(Properties properties, Supplier<? extends Block> wood, Supplier<? extends Item> fruit) {
        super(properties);
        this.woodSupplier = wood;
        this.fruitItem = fruit;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AGE, 0)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return simpleCodec(p -> new RawBananaBlock(p, woodSupplier, fruitItem));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(AGE) == 1 ? SHAPE_STAGE1 : SHAPE_STAGE0;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Growth from stage 0 → stage 1
        if (state.getValue(AGE) == 0 && random.nextInt(4) == 0) {
            level.setBlock(pos, state.setValue(AGE, 1), 3);
        }

        // Check support: if no bananawood adjacent, break naturally with no drops
        if (!hasSupport(level, pos)) {
            level.destroyBlock(pos, false);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!hasSupport(level, pos)) {
            level.scheduleTick(pos, this, 1);
        }
        return state;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!hasSupport(level, pos)) {
            level.destroyBlock(pos, false);
        }
    }

    private boolean hasSupport(LevelReader level, BlockPos pos) {
        Block wood = woodSupplier.get();
        BlockState state = level.getBlockState(pos);
        Direction facing = state.getValue(FACING);
        // FACING points toward the supporting bananawood
        BlockPos supportPos = pos.relative(facing);
        return level.getBlockState(supportPos).is(wood);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Banana faces toward the block it's placed against (toward the wood)
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (state.getValue(AGE) == 1) {
            if (!level.isClientSide) {
                // Drop banana and reset to stage 0
                ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        new ItemStack(fruitItem.get()));
                level.addFreshEntity(itemEntity);
                level.setBlock(pos, state.setValue(AGE, 0), 3);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        // Only shears can break rawbanana
        if (player.getMainHandItem().is(Items.SHEARS) || player.getOffhandItem().is(Items.SHEARS)) {
            return super.getDestroyProgress(state, player, level, pos);
        }
        return 0.0F;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        // No loot drops
        return super.playerWillDestroy(level, pos, state, player);
    }

    // ===== BonemealableBlock =====

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(AGE) == 0;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (state.getValue(AGE) == 0) {
            level.setBlock(pos, state.setValue(AGE, 1), 3);
        }
    }
}
