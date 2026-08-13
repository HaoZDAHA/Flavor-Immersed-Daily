package com.flavor_immersed_daily.block.block.machine;

import com.flavor_immersed_daily.block.blockentity.FridgeBlockEntity;
import com.mojang.serialization.MapCodec;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class FridgeBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final MapCodec<FridgeBlock> CODEC = simpleCodec(FridgeBlock::new);

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = EnumProperty.create("half", DoubleBlockHalf.class);

    public FridgeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }

    // ==================== EntityBlock ====================

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return new FridgeBlockEntity(pos, state);
        }
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        if (type == com.flavor_immersed_daily.all.ModBlockEntities.FRIDGE_ENTITY.get()) {
            return (lvl, pos, st, be) -> FridgeBlockEntity.serverTick(lvl, pos, st, (FridgeBlockEntity) be);
        }
        return null;
    }

    // ==================== 放置逻辑 ====================

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
            return this.defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), Block.UPDATE_ALL);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockState below = level.getBlockState(pos.below());
            return below.is(this) && below.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
        return true;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                      LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf half = state.getValue(HALF);
        if (direction.getAxis() == Direction.Axis.Y) {
            boolean isUp = direction == Direction.UP;
            if ((half == DoubleBlockHalf.LOWER && isUp) || (half == DoubleBlockHalf.UPPER && !isUp)) {
                if (neighborState.is(this) && neighborState.getValue(HALF) != half) {
                    return state;
                }
                return Blocks.AIR.defaultBlockState();
            }
        }
        return state;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        // 所有模式下都先移除伙伴方块（带 SUPPRESS_DROPS），
        // 避免伙伴方块随后因失去支撑变为空气时触发第二次掉落（导致一台冰箱掉两份）
        if (!level.isClientSide()) {
            DoubleBlockHalf half = state.getValue(HALF);
            if (half == DoubleBlockHalf.LOWER) {
                BlockPos upperPos = pos.above();
                BlockState upperState = level.getBlockState(upperPos);
                if (upperState.is(this) && upperState.getValue(HALF) == DoubleBlockHalf.UPPER) {
                    level.setBlock(upperPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_ALL);
                    level.levelEvent(null, 2001, upperPos, Block.getId(upperState));
                }
            } else {
                BlockPos lowerPos = pos.below();
                BlockState lowerState = level.getBlockState(lowerPos);
                if (lowerState.is(this) && lowerState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                    level.setBlock(lowerPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_ALL);
                    level.levelEvent(null, 2001, lowerPos, Block.getId(lowerState));
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER && !state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof FridgeBlockEntity fridge) {
                Containers.dropContents(level, pos, fridge.getItems());
            }
        }
        super.onRemove(state, level, pos, newState, moved);
    }

    // ==================== 交互 ====================

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                                Player player, BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        // 找到下半部分的BlockEntity
        BlockPos bePos = pos;
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            bePos = pos.below();
        }

        BlockEntity be = level.getBlockEntity(bePos);
        if (be instanceof FridgeBlockEntity fridge) {
            player.openMenu(new SimpleMenuProvider(
                    (containerId, playerInventory, p) ->
                            new com.flavor_immersed_daily.screen.FridgeMenu(
                                    containerId, playerInventory, fridge),
                    Component.translatable("block.flavor_immersed_daily.fridge")));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    // ==================== 旋转 ====================

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}
