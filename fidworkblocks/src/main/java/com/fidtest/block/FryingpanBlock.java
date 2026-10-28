package com.fidtest.block;

import com.fidtest.block.entity.FryingpanBlockEntity;
import com.fidtest.registration.ModBlocks;
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

public class FryingpanBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final MapCodec<FryingpanBlock> CODEC = simpleCodec(FryingpanBlock::new);

    private static final VoxelShape BASE = Block.box(3, 0, 3, 13, 2, 13);
    private static final VoxelShape WALL_N = Block.box(3, 2, 3, 13, 5, 4);
    private static final VoxelShape WALL_S = Block.box(3, 2, 12, 13, 5, 13);
    private static final VoxelShape WALL_E = Block.box(12, 2, 4, 13, 5, 12);
    private static final VoxelShape WALL_W = Block.box(3, 2, 4, 4, 5, 12);
    private static final VoxelShape SHAPE = Shapes.or(BASE, WALL_N, WALL_S, WALL_E, WALL_W);

    public FryingpanBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<FryingpanBlock> codec() { return CODEC; }

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
        BlockPos belowPos = context.getClickedPos().below();
        BlockState belowState = context.getLevel().getBlockState(belowPos);
        if (!belowState.is(ModBlocks.DRYER.get())) {
            return null;
        }
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
        if (!(level.getBlockEntity(pos) instanceof FryingpanBlockEntity fp)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        ItemStack heldItem = player.getItemInHand(hand);

        if (fp.isWorking()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!heldItem.isEmpty()) {
            if (fp.isOilItem(heldItem)) {
                if (level.isClientSide) return ItemInteractionResult.SUCCESS;
                ItemStack remainder = fp.addOil(player.getAbilities().instabuild ? heldItem.copy() : heldItem);
                if (!player.isCreative()) {
                    player.setItemInHand(hand, remainder);
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F);
                return ItemInteractionResult.SUCCESS;
            }

            if (!fp.hasIngredient() && !fp.hasOutput()) {
                if (fp.isRecipeIngredient(heldItem)) {
                    if (fp.getOilCount() < heldItem.getCount()) {
                        if (!level.isClientSide) {
                            player.displayClientMessage(
                                    Component.translatable("message.fidworkblock.fryingpan_oil_not_enough"), true);
                        }
                        return ItemInteractionResult.SUCCESS;
                    }
                    if (level.isClientSide) return ItemInteractionResult.SUCCESS;
                    ItemStack remainder = fp.addIngredient(player.getAbilities().instabuild ? heldItem.copy() : heldItem);
                    if (!player.isCreative()) {
                        player.setItemInHand(hand, remainder);
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F);
                    return ItemInteractionResult.SUCCESS;
                } else {
                    if (!level.isClientSide) {
                        player.displayClientMessage(
                                Component.translatable("message.fidworkblock.fryingpan_cant_fry"), true);
                    }
                    return ItemInteractionResult.SUCCESS;
                }
            }

            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (player.isShiftKeyDown()) {
            if (fp.hasIngredient()) {
                if (level.isClientSide) return ItemInteractionResult.SUCCESS;
                ItemStack removed = fp.removeIngredient();
                if (!player.isCreative()) {
                    player.getInventory().add(removed);
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
                return ItemInteractionResult.SUCCESS;
            }
            if (fp.hasOil()) {
                if (level.isClientSide) return ItemInteractionResult.SUCCESS;
                ItemStack removed = fp.removeOil();
                if (!player.isCreative()) {
                    player.getInventory().add(removed);
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
                return ItemInteractionResult.SUCCESS;
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (fp.hasOutput()) {
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            fp.collectOutput(player);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }

        if (fp.hasIngredient()) {
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            fp.startFrying();
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof FryingpanBlockEntity fp)) {
            return InteractionResult.PASS;
        }

        if (fp.isWorking()) {
            return InteractionResult.PASS;
        }

        if (player.isShiftKeyDown()) {
            if (fp.hasIngredient()) {
                if (level.isClientSide) return InteractionResult.CONSUME;
                ItemStack removed = fp.removeIngredient();
                if (!player.isCreative()) {
                    player.getInventory().add(removed);
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
                return InteractionResult.SUCCESS;
            }
            if (fp.hasOil()) {
                if (level.isClientSide) return InteractionResult.CONSUME;
                ItemStack removed = fp.removeOil();
                if (!player.isCreative()) {
                    player.getInventory().add(removed);
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
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
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof FryingpanBlockEntity fp) {
                ItemStack oil = fp.getOilItem();
                if (!oil.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), oil);
                }
                ItemStack ingredient = fp.getIngredientItem();
                if (!ingredient.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), ingredient);
                }
                ItemStack output = fp.getOutputItem();
                if (!output.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), output);
                }
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FryingpanBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return createTickerHelper(type, com.fidtest.registration.ModBlockEntities.FRYINGPAN.get(),
                (lvl, pos, st, be) -> FryingpanBlockEntity.serverTick(be));
    }
}
