package com.fidtest.block;

import com.fidtest.block.entity.SqueezingmachineBlockEntity;
import com.fidtest.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class SqueezingmachineBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape BASE = Block.box(2, 0, 2, 14, 4, 14);
    private static final VoxelShape CYLINDER = Block.box(3, 4, 3, 13, 21, 13);
    private static final VoxelShape SHAPE = Shapes.or(BASE, CYLINDER);

    public SqueezingmachineBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override public RenderShape getRenderShape(BlockState s) { return RenderShape.MODEL; }

    @Override
    public VoxelShape getShape(BlockState s, BlockGetter l, BlockPos p, CollisionContext c) { return SHAPE; }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) { b.add(FACING); }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override public BlockState rotate(BlockState s, Rotation r) { return s.setValue(FACING, r.rotate(s.getValue(FACING))); }
    @Override public BlockState mirror(BlockState s, Mirror m) { return s.rotate(m.getRotation(s.getValue(FACING))); }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof SqueezingmachineBlockEntity sm))
            return InteractionResult.PASS;

        ItemStack held = player.getItemInHand(hand);

// 工作中 → 不理
        if (sm.isWorking()) return InteractionResult.PASS;

// 空手
        if (held.isEmpty()) {
            if (player.isShiftKeyDown() && sm.hasInput()) {
                if (level.isClientSide) return InteractionResult.CONSUME;
                ItemStack removed = sm.removeInput();
                if (!player.isCreative()) player.getInventory().add(removed);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
                return InteractionResult.SUCCESS;
            }
            if (sm.hasOutput()) {
                if (level.isClientSide) return InteractionResult.CONSUME;
                ItemStack output = sm.collectOutput();
                if (!output.isEmpty() && !player.isCreative()) player.getInventory().add(output);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
                return InteractionResult.SUCCESS;
            }
            if (sm.hasInput()) {
                if (level.isClientSide) return InteractionResult.CONSUME;
                sm.startProcessing();
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

// 手里有物品 + 无输入无输出 → 放入
        if (!sm.hasInput() && !sm.hasOutput()) {
            if (sm.isRecipeItem(held)) {
                if (level.isClientSide) return InteractionResult.CONSUME;
                ItemStack remainder = sm.addInput(player.isCreative() ? held.copy() : held);
                if (!player.isCreative()) player.setItemInHand(hand, remainder);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F);
                return InteractionResult.SUCCESS;
            } else {
                if (!level.isClientSide) player.displayClientMessage(
                        Component.translatable("message.fidworkblock.squeezingmachine_cant_add"), true);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof SqueezingmachineBlockEntity sm) {
                if (!sm.getInputItem().isEmpty()) Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), sm.getInputItem());
                if (!sm.getOutputItem().isEmpty()) Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), sm.getOutputItem());
                if (!sm.getStoredResult().isEmpty()) Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), sm.getStoredResult());
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    @Nullable @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new SqueezingmachineBlockEntity(pos, state); }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return createTickerHelper(type, ModBlockEntities.SQUEEZINGMACHINE.get(),
                (lvl, pos, st, be) -> SqueezingmachineBlockEntity.serverTick(be));
    }
}
