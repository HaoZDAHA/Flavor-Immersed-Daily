package com.fidtest.block;

import com.fidtest.block.entity.WokBlockEntity;
import com.fidtest.item.DishItem;
import com.fidtest.registration.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WokBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OILED = BooleanProperty.create("oiled");

// 碰撞箱收窄到锅体本身（模型锅体约 x/z 2~14、高 6），避免右键时误触到锅外的空气格
    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 6, 14);

    private static final ResourceLocation COOKING_OIL_ID =
            ResourceLocation.fromNamespaceAndPath("flavor_immersed_daily", "cookingoil");
    private static final ResourceLocation SPATULA_ID =
            ResourceLocation.fromNamespaceAndPath("flavor_immersed_daily", "spatula");

    public static final MapCodec<WokBlock> CODEC = simpleCodec(WokBlock::new);

    public WokBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OILED, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    @Override
    public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OILED);
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
        if (!(level.getBlockEntity(pos) instanceof WokBlockEntity wok)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;

        ItemStack held = player.getItemInHand(hand);
        if (held.isEmpty()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        boolean oiled = state.getValue(OILED);

// 已出锅的自定义菜品禁止再次入锅炒制：直接吞掉交互，避免把菜品放到锅上或造成嵌套菜品数据
        if (held.getItem() instanceof DishItem) {
            return ItemInteractionResult.SUCCESS;
        }

// 锅铲：锅里有食材时只能手持锅铲翻炒，不允许放置；空锅且锅上没锅铲时可把锅铲常态搁在锅上
        if (isSpatula(held)) {
            if (wok.getItemCount() > 0) {
                if (!oiled) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                wok.stirFry(player);
                level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.4F, 1.2F);
                return ItemInteractionResult.SUCCESS;
            }
            if (wok.hasSpatula()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            wok.setSpatula(held);
            if (!player.isCreative()) held.shrink(1);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.1F);
            return ItemInteractionResult.SUCCESS;
        }

        if (!oiled) {
            if (isCookingOil(held)) {
                level.setBlock(pos, state.setValue(OILED, true), 3);
                if (!player.isCreative()) held.shrink(1);
                level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 1.0F);
                return ItemInteractionResult.SUCCESS;
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (isSeasoning(held)) {
            if (wok.getItemCount() == 0) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            String seasoningId = held.getItem().builtInRegistryHolder().key().location().toString();
            wok.setSeasoningName(seasoningId);
            wok.finishCooking(level, pos);
            if (!player.isCreative()) held.shrink(1);
            level.setBlock(pos, state.setValue(OILED, false), 3);
            level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }

        if (!held.isDamageableItem() && wok.canAddItem()) {
            ItemStack single = held.copy();
            single.setCount(1);
            if (wok.addItem(single)) {
                if (!player.isCreative()) held.shrink(1);
                level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.3F, 1.5F);
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof WokBlockEntity wok)) {
            return InteractionResult.PASS;
        }
// 空手右键：把搁在锅上的锅铲取回
        if (wok.hasSpatula() && wok.getItemCount() == 0) {
            if (!level.isClientSide) {
                ItemStack spatula = wok.takeSpatula();
                if (!player.getInventory().add(spatula)) {
                    player.drop(spatula, false);
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof WokBlockEntity wok) {
                for (int i = 0; i < 6; i++) {
                    ItemStack s = wok.getInventory().getStackInSlot(i);
                    if (!s.isEmpty()) {
                        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), s);
                    }
                }
                if (wok.hasSpatula()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), wok.takeSpatula());
                }
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WokBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return createTickerHelper(type, ModBlockEntities.WOK.get(),
                (lvl, pos, st, be) -> be.tick());
    }

    private boolean isCookingOil(ItemStack stack) {
        return stack.getItem().builtInRegistryHolder().key().location().equals(COOKING_OIL_ID);
    }

    private boolean isSpatula(ItemStack stack) {
        return stack.getItem().builtInRegistryHolder().key().location().equals(SPATULA_ID);
    }

    private boolean isSeasoning(ItemStack stack) {
        return stack.getItem().builtInRegistryHolder().tags()
                .anyMatch(t -> t.location().toString().equals("fid:seasoning"));
    }
}
