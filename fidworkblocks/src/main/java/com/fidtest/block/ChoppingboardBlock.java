package com.fidtest.block;

import com.fidtest.block.entity.ChoppingboardBlockEntity;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ChoppingboardBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 1, 16);

    public static final MapCodec<ChoppingboardBlock> CODEC = simpleCodec(ChoppingboardBlock::new);

    public ChoppingboardBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
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
        if (!(level.getBlockEntity(pos) instanceof ChoppingboardBlockEntity board)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        ItemStack heldItem = player.getItemInHand(hand);

// 包裹优先：皮 + 馅料齐全时，任意右键都把两者包成对应生食
        if (board.canWrap()) {
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            if (board.wrap()) {
                player.displayClientMessage(Component.literal("包裹完成，生食落在砧板上"), true);
            }
            return ItemInteractionResult.SUCCESS;
        }

// 月饼：砧板上有生馅饼，手持月饼模具右键 → 压成生月饼（继承 NBT）
        if (board.canMakeMooncake() && ChoppingboardBlockEntity.isMooncakeMold(heldItem)) {
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            if (board.makeMooncake()) {
                player.displayClientMessage(Component.literal("月饼做好了，生月饼落在砧板上"), true);
            }
            return ItemInteractionResult.SUCCESS;
        }

// 粽子：芦苇叶 + 馅料齐全，手持糯米右键 → 包成生粽子
        if (board.canMakeZongzi() && ChoppingboardBlockEntity.isRice(heldItem)) {
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            if (board.makeZongzi()) {
                if (!player.isCreative()) {
                    heldItem.shrink(1);
                }
                player.displayClientMessage(Component.literal("粽子做好了"), true);
            }
            return ItemInteractionResult.SUCCESS;
        }

        if (heldItem.isEmpty()) {
            if (board.isEmpty()) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            ItemStack removed = takeFromBoard(board);
            if (!player.isCreative()) {
                player.getInventory().add(removed);
            }
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }

// 擀面杖：把砧板上的面团/面剂子/面皮擀成下一步形态（原地替换，可连续擀制）；
// 独占本次交互，避免擀面杖被当作馅料/食材放上砧板
        if (ChoppingboardBlockEntity.isRollingPin(heldItem)) {
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            board.processRollingPin(heldItem, player);
            return ItemInteractionResult.SUCCESS;
        }

// 粽子等待糯米：芦苇叶 + 馅料齐全，手持非糯米物品时提示放糯米
        if (board.canMakeZongzi()) {
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            player.displayClientMessage(Component.literal("放入糯米（polishedglutinousrice_2）即可包裹"), true);
            return ItemInteractionResult.SUCCESS;
        }
// 手持皮类物品：放入皮槽（每次只消耗 1 个；皮槽已有不同种皮则忽略，绝不取出旧皮）
        if (ChoppingboardBlockEntity.isSkinItem(heldItem)) {
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            ItemStack remainder = board.addSkin(heldItem.copyWithCount(1));
            if (!remainder.isEmpty()) {
// 皮槽已满或已有不同种皮，放不进去：什么都不做
                return ItemInteractionResult.SUCCESS;
            }
            if (!player.isCreative()) {
                heldItem.shrink(1);
            }
            boolean reed = ChoppingboardBlockEntity.isReedLeaf(heldItem);
            player.displayClientMessage(Component.literal(
                    reed ? "芦苇叶放好了，可以放入任意物品作为馅料" : "皮放好了，可以放入任意物品作为馅料"), true);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F);
            return ItemInteractionResult.SUCCESS;
        }

// 皮槽有皮：把手中物品作为馅料放到皮上（每次只消耗 1 个）
        if (!board.getSkinItem().isEmpty()) {
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            ItemStack remainder = board.addFilling(heldItem.copyWithCount(1));
            if (!remainder.isEmpty() && !player.isCreative()) {
// 馅料槽已满或已有其他物品，放不进去：什么都不做，避免误放方块
                return ItemInteractionResult.SUCCESS;
            }
            if (!player.isCreative()) {
                heldItem.shrink(1);
            }
            boolean reed = ChoppingboardBlockEntity.isReedLeaf(board.getSkinItem());
            player.displayClientMessage(Component.literal(
                    reed ? "馅料放好了，再放入糯米（polishedglutinousrice_2）即可包裹"
                         : "馅料放好了，右键即可包裹"), true);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F);
            return ItemInteractionResult.SUCCESS;
        }

        if (board.canAddItem(heldItem)) {
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            ItemStack remainder = board.addItem(heldItem.copyWithCount(1));
            if (!remainder.isEmpty() && !player.isCreative()) {
                return ItemInteractionResult.SUCCESS;
            }
            if (!player.isCreative()) {
                heldItem.shrink(1);
            }
            if (ChoppingboardBlockEntity.RAW_PIE_ID.equals(
                    heldItem.getItem().builtInRegistryHolder().key().location())) {
                player.displayClientMessage(Component.literal("馅饼放好了，用月饼模具右键制作月饼"), true);
            }
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F);
            return ItemInteractionResult.SUCCESS;
        }

// 小型生物屠宰优先于普通切菜配方：该食材在 choppingboard_mob 定义中登记时接管本次交互
        if (board.processChop(heldItem, player)) {
            return ItemInteractionResult.SUCCESS;
        }

        if (board.processWithTool(heldItem, player)) {
            return ItemInteractionResult.SUCCESS;
        }

// 手持物品时绝不取出砧板上的东西：取出只属于空手右键
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof ChoppingboardBlockEntity board)) {
            return InteractionResult.PASS;
        }

// 空手右键：皮 + 馅料齐全时优先包裹
        if (board.canWrap()) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            if (board.wrap()) {
                player.displayClientMessage(Component.literal("包裹完成，生食落在砧板上"), true);
            }
            return InteractionResult.SUCCESS;
        }

        if (board.isEmpty()) return InteractionResult.PASS;
        if (level.isClientSide) return InteractionResult.CONSUME;

        ItemStack removed = takeFromBoard(board);
        if (!player.isCreative()) {
            player.getInventory().add(removed);
        }
        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
        return InteractionResult.SUCCESS;
    }

// 空手取出顺序：先馅料，再皮，最后食材
    private static ItemStack takeFromBoard(ChoppingboardBlockEntity board) {
        ItemStack removed = board.removeFilling();
        if (removed.isEmpty()) removed = board.removeSkin();
        if (removed.isEmpty()) removed = board.removeItem();
        return removed;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof ChoppingboardBlockEntity board) {
                ItemStack stored = board.getStoredItem();
                if (!stored.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stored);
                }
                ItemStack skin = board.getSkinItem();
                if (!skin.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), skin);
                }
                ItemStack filling = board.getFillingItem();
                if (!filling.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), filling);
                }
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChoppingboardBlockEntity(pos, state);
    }
}
