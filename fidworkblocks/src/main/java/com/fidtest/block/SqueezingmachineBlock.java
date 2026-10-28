package com.fidtest.block;

import com.fidtest.block.entity.SqueezingmachineBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
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
import org.jetbrains.annotations.Nullable;

public class SqueezingmachineBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final MapCodec<SqueezingmachineBlock> CODEC = simpleCodec(SqueezingmachineBlock::new);

    private static final VoxelShape BASE = Block.box(2, 0, 2, 14, 4, 14);
    private static final VoxelShape CYLINDER = Block.box(3, 4, 3, 13, 21, 13);
    private static final VoxelShape SHAPE = Shapes.or(BASE, CYLINDER);

    public SqueezingmachineBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<SqueezingmachineBlock> codec() { return CODEC; }

    @Override
    public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof SqueezingmachineBlockEntity sm)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        ItemStack heldItem = player.getItemInHand(hand);

        if (sm.isWorking()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (heldItem.isEmpty()) {
            if (player.isShiftKeyDown() && sm.hasInput()) {
                if (level.isClientSide) return ItemInteractionResult.SUCCESS;
                ItemStack removed = sm.removeInput();
                if (!player.isCreative()) {
                    player.getInventory().add(removed);
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
                return ItemInteractionResult.SUCCESS;
            }

            if (sm.hasOutput()) {
                if (level.isClientSide) return ItemInteractionResult.SUCCESS;
                ItemStack output = sm.collectOutput();
                if (!output.isEmpty() && !player.isCreative()) {
                    player.getInventory().add(output);
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
                return ItemInteractionResult.SUCCESS;
            }

            if (sm.hasInput()) {
                if (level.isClientSide) return ItemInteractionResult.SUCCESS;
                sm.startProcessing();
                return ItemInteractionResult.SUCCESS;
            }

            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!sm.hasInput() && !sm.hasOutput()) {
            if (sm.isRecipeItem(heldItem)) {
                if (level.isClientSide) return ItemInteractionResult.SUCCESS;
                ItemStack remainder = sm.addInput(player.getAbilities().instabuild ? heldItem.copy() : heldItem);
                if (!player.isCreative()) {
                    player.setItemInHand(hand, remainder);
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F);
                return ItemInteractionResult.SUCCESS;
            } else {
                if (!level.isClientSide) {
                    player.displayClientMessage(
                            Component.translatable("message.fidworkblock.squeezingmachine_cant_add"), true);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof SqueezingmachineBlockEntity sm)) {
            return InteractionResult.PASS;
        }

        if (sm.isWorking()) {
            return InteractionResult.PASS;
        }

        if (player.isShiftKeyDown() && sm.hasInput()) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            ItemStack removed = sm.removeInput();
            if (!player.isCreative()) {
                player.getInventory().add(removed);
            }
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        if (sm.hasOutput()) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            ItemStack output = sm.collectOutput();
            if (!output.isEmpty() && !player.isCreative()) {
                player.getInventory().add(output);
            }
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

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof SqueezingmachineBlockEntity sm) {
                ItemStack input = sm.getInputItem();
                if (!input.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), input);
                }
                ItemStack output = sm.getOutputItem();
                if (!output.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), output);
                }
                ItemStack result = sm.getStoredResult();
                if (!result.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), result);
                }
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SqueezingmachineBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return createTickerHelper(type, com.fidtest.registration.ModBlockEntities.SQUEEZINGMACHINE.get(),
                (lvl, pos, st, be) -> SqueezingmachineBlockEntity.serverTick(be));
    }
}
