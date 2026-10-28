package com.fidtest.block;

import com.fidtest.block.entity.JarBlockEntity;
import com.fidtest.registration.ModBlockEntities;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class JarBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty SEALED = BooleanProperty.create("sealed");
    public static final MapCodec<JarBlock> CODEC = simpleCodec(JarBlock::new);

    private static final VoxelShape BASE = Block.box(4, 0, 4, 12, 1, 12);
    private static final VoxelShape BODY = Block.box(3, 1, 3, 13, 11, 13);
    private static final VoxelShape NECK = Block.box(5, 11, 5, 11, 14, 11);
    private static final VoxelShape SHAPE = Shapes.or(BASE, BODY, NECK);

    public JarBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(SEALED, false));
    }

    @Override
    protected MapCodec<JarBlock> codec() { return CODEC; }

    @Override
    public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SEALED);
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
    protected ItemInteractionResult useItemOn(ItemStack heldItem, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof JarBlockEntity jar)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        boolean sealed = state.getValue(SEALED);

        if (sealed) {
            if (player.isShiftKeyDown()) {
                if (level.isClientSide) return ItemInteractionResult.SUCCESS;
                jar.unseal(player);
                level.setBlock(pos, state.setValue(SEALED, false), 3);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
                return ItemInteractionResult.SUCCESS;
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (jar.isWorking()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!heldItem.isEmpty()) {
// 放入材料：不再需要滤布，任意物品都直接进输入槽
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            ItemStack remainder = jar.addItemToNextSlot(
                    player.getAbilities().instabuild ? heldItem.copy() : heldItem);
            if (!player.isCreative()) {
                player.setItemInHand(hand, remainder);
            }
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F);
            return ItemInteractionResult.SUCCESS;
        }

        if (player.isShiftKeyDown()) {
            if (jar.hasAnyInput()) {
                if (level.isClientSide) return ItemInteractionResult.SUCCESS;
                ItemStack removed = jar.removeLastInput();
                if (!player.isCreative()) {
                    player.getInventory().add(removed);
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
                return ItemInteractionResult.SUCCESS;
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (jar.hasOutput()) {
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            ItemStack output = jar.collectOutput();
            if (!output.isEmpty()) {
                if (!player.getInventory().add(output)) {
                    player.drop(output, false);
                }
            }
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof JarBlockEntity jar)) {
            return InteractionResult.PASS;
        }

        boolean sealed = state.getValue(SEALED);

        if (sealed) {
            if (player.isShiftKeyDown()) {
                if (level.isClientSide) return InteractionResult.CONSUME;
                jar.unseal(player);
                level.setBlock(pos, state.setValue(SEALED, false), 3);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        if (jar.isWorking()) return InteractionResult.PASS;

        if (player.isShiftKeyDown()) {
            if (jar.hasAnyInput()) {
                if (level.isClientSide) return InteractionResult.CONSUME;
                ItemStack removed = jar.removeLastInput();
                if (!player.isCreative()) {
                    player.getInventory().add(removed);
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }

        if (jar.hasOutput()) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            ItemStack output = jar.collectOutput();
            if (!output.isEmpty()) {
                if (!player.getInventory().add(output)) {
                    player.drop(output, false);
                }
            }
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
            return InteractionResult.SUCCESS;
        }

// 空手右键：不再需要滤布，直接识别配方并开始腌制
        if (jar.hasAnyInput()) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            if (jar.checkRecipe()) {
                level.setBlock(pos, state.setValue(SEALED, true), 3);
                jar.seal();
                player.displayClientMessage(
                        Component.translatable("message.fidworkblock.jar_recipe_correct"), true);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.6F);
                return InteractionResult.SUCCESS;
            } else {
                player.displayClientMessage(
                        Component.translatable("message.fidworkblock.jar_recipe_incorrect"), true);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof JarBlockEntity jar) {
                for (int i = 0; i < 5; i++) {
                    ItemStack stack = jar.getInventory().getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                    }
                }
// 账本中尚未落地的产物也要掉落，避免满一组时被吞掉
                for (ItemStack pending : jar.extractPendingResults()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), pending);
                }
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new JarBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return createTickerHelper(type, ModBlockEntities.JAR.get(),
                (lvl, p, st, be) -> JarBlockEntity.serverTick(be));
    }
}
