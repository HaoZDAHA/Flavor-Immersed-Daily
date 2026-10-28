package com.fidtest.block;

import com.fidtest.block.entity.FryingpanBlockEntity;
import com.fidtest.registration.ModBlockEntities;
import com.fidtest.registration.ModBlocks;
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

public class FryingpanBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape BASE = Block.box(3, 0, 3, 13, 2, 13);
    private static final VoxelShape WALL_N = Block.box(3, 2, 3, 13, 5, 4);
    private static final VoxelShape WALL_S = Block.box(3, 2, 12, 13, 5, 13);
    private static final VoxelShape WALL_E = Block.box(12, 2, 4, 13, 5, 12);
    private static final VoxelShape WALL_W = Block.box(3, 2, 4, 4, 5, 12);
    private static final VoxelShape SHAPE = Shapes.or(BASE, WALL_N, WALL_S, WALL_E, WALL_W);

    public FryingpanBlock(BlockBehaviour.Properties p) {
        super(p);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override public RenderShape getRenderShape(BlockState s) { return RenderShape.MODEL; }
    @Override public VoxelShape getShape(BlockState s, BlockGetter l, BlockPos p, CollisionContext c) { return SHAPE; }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) { b.add(FACING); }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        if (!ctx.getLevel().getBlockState(ctx.getClickedPos().below()).is(ModBlocks.DRYER.get())) return null;
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override public BlockState rotate(BlockState s, Rotation r) { return s.setValue(FACING, r.rotate(s.getValue(FACING))); }
    @Override public BlockState mirror(BlockState s, Mirror m) { return s.rotate(m.getRotation(s.getValue(FACING))); }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof FryingpanBlockEntity fp)) return InteractionResult.PASS;
        if (fp.isWorking()) return InteractionResult.PASS;

        ItemStack held = player.getItemInHand(hand);

        if (!held.isEmpty()) {
            if (fp.isOilItem(held)) {
                if (level.isClientSide) return InteractionResult.CONSUME;
                ItemStack r = fp.addOil(player.isCreative() ? held.copy() : held);
                if (!player.isCreative()) player.setItemInHand(hand, r);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F);
                return InteractionResult.SUCCESS;
            }
            if (!fp.hasIngredient() && !fp.hasOutput()) {
                if (fp.isRecipeIngredient(held)) {
                    if (fp.getOilCount() < held.getCount()) {
                        if (!level.isClientSide) player.displayClientMessage(
                                Component.translatable("message.fidworkblock.fryingpan_oil_not_enough"), true);
                        return InteractionResult.CONSUME;
                    }
                    if (level.isClientSide) return InteractionResult.CONSUME;
                    ItemStack r = fp.addIngredient(player.isCreative() ? held.copy() : held);
                    if (!player.isCreative()) player.setItemInHand(hand, r);
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F);
                    return InteractionResult.SUCCESS;
                } else {
                    if (!level.isClientSide) player.displayClientMessage(
                            Component.translatable("message.fidworkblock.fryingpan_cant_fry"), true);
                    return InteractionResult.CONSUME;
                }
            }
            return InteractionResult.PASS;
        }

        if (player.isShiftKeyDown()) {
            if (fp.hasIngredient()) {
                if (level.isClientSide) return InteractionResult.CONSUME;
                ItemStack r = fp.removeIngredient();
                if (!player.isCreative()) player.getInventory().add(r);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
                return InteractionResult.SUCCESS;
            }
            if (fp.hasOil()) {
                if (level.isClientSide) return InteractionResult.CONSUME;
                ItemStack r = fp.removeOil();
                if (!player.isCreative()) player.getInventory().add(r);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        if (fp.hasOutput()) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            fp.collectOutput(player);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
            return InteractionResult.SUCCESS;
        }
        if (fp.hasIngredient()) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            fp.startFrying();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof FryingpanBlockEntity fp) {
                if (!fp.getOilItem().isEmpty()) Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), fp.getOilItem());
                if (!fp.getIngredientItem().isEmpty()) Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), fp.getIngredientItem());
                if (!fp.getOutputItem().isEmpty()) Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), fp.getOutputItem());
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    @Nullable @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new FryingpanBlockEntity(pos, state); }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return createTickerHelper(type, ModBlockEntities.FRYINGPAN.get(),
                (l, p, s, be) -> FryingpanBlockEntity.serverTick(be));
    }
}
