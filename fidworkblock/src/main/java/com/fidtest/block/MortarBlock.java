package com.fidtest.block;

import com.fidtest.block.entity.MortarBlockEntity;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class MortarBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 6, 12);

    public MortarBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override public RenderShape getRenderShape(BlockState s) { return RenderShape.MODEL; }

    @Override
    public VoxelShape getShape(BlockState s, BlockGetter level, BlockPos pos, CollisionContext ctx) { return SHAPE; }

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
        if (!(level.getBlockEntity(pos) instanceof MortarBlockEntity mortar))
            return InteractionResult.PASS;

        ItemStack held = player.getItemInHand(hand);

// 空手 + 潜行 + 有物品 → 取出
        if (held.isEmpty() && player.isShiftKeyDown()) {
            if (mortar.isEmpty()) return InteractionResult.PASS;
            if (level.isClientSide) return InteractionResult.CONSUME;
            ItemStack removed = mortar.removeItem();
            if (!player.isCreative()) player.getInventory().add(removed);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
            return InteractionResult.SUCCESS;
        }

// 空手 + 有物品 → 加工
        if (held.isEmpty()) {
            if (mortar.isEmpty()) return InteractionResult.PASS;
            if (level.isClientSide) return InteractionResult.CONSUME;
            if (mortar.processStored(player)) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

// 手里有物品 + 研钵为空 → 尝试放入
        if (mortar.isEmpty()) {
            if (mortar.isRecipeItem(held)) {
                if (level.isClientSide) return InteractionResult.CONSUME;
                ItemStack remainder = mortar.addItem(player.isCreative() ? held.copy() : held);
                if (!player.isCreative()) player.setItemInHand(hand, remainder);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F);
                return InteractionResult.SUCCESS;
            } else {
                if (!level.isClientSide) player.displayClientMessage(
                        Component.translatable("message.fidworkblock.mortar_cant_add"), true);
                return InteractionResult.CONSUME;
            }
        }

// 手里有物品 + 研钵已有物品 → 尝试加工
        if (!level.isClientSide && mortar.processStored(player)) {
            return InteractionResult.SUCCESS;
        }
        return level.isClientSide ? InteractionResult.CONSUME : InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof MortarBlockEntity mortar && !mortar.isEmpty()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), mortar.removeItem());
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MortarBlockEntity(pos, state);
    }
}
