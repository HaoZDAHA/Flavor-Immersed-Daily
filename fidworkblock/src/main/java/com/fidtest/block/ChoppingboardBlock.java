package com.fidtest.block;

import com.fidtest.block.entity.ChoppingboardBlockEntity;
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

public class ChoppingboardBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 1, 16);

    public ChoppingboardBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override public RenderShape getRenderShape(BlockState s) { return RenderShape.MODEL; }

    @Override
    public VoxelShape getShape(BlockState s, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

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
        if (!(level.getBlockEntity(pos) instanceof ChoppingboardBlockEntity board))
            return InteractionResult.PASS;

        ItemStack held = player.getItemInHand(hand);

// 包裹优先：皮 + 馅料齐全时，任意右键都把两者包成对应生食
        if (board.canWrap()) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            if (board.wrap()) {
                player.displayClientMessage(
                        Component.translatable("message.fidworkblock.choppingboard_wrapped"), true);
            }
            return InteractionResult.SUCCESS;
        }

// 月饼：砧板上有生馅饼，手持月饼模具右键 → 压成生月饼（继承 NBT）
        if (board.canMakeMooncake() && ChoppingboardBlockEntity.isMooncakeMold(held)) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            if (board.makeMooncake()) {
                player.displayClientMessage(
                        Component.translatable("message.fidworkblock.choppingboard_mooncake_done"), true);
            }
            return InteractionResult.SUCCESS;
        }

// 粽子：芦苇叶 + 馅料齐全，手持糯米右键 → 包成生粽子
        if (board.canMakeZongzi() && ChoppingboardBlockEntity.isRice(held)) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            if (board.makeZongzi()) {
                if (!player.isCreative()) held.shrink(1);
                player.displayClientMessage(
                        Component.translatable("message.fidworkblock.choppingboard_zongzi_done"), true);
            }
            return InteractionResult.SUCCESS;
        }

// 空手右键 → 取出物品（顺序：馅料 → 皮 → 食材）
        if (held.isEmpty()) {
            if (board.isEmpty()) return InteractionResult.PASS;
            if (level.isClientSide) return InteractionResult.CONSUME;
            ItemStack removed = takeFromBoard(board);
            if (!player.isCreative()) player.getInventory().add(removed);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
            return InteractionResult.SUCCESS;
        }

// 擀面杖：把砧板上的面团/面剂子/面皮擀成下一步形态（原地替换，可连续擀制）；
// 独占本次交互，避免擀面杖被当作馅料/食材放上砧板
        if (ChoppingboardBlockEntity.isRollingPin(held)) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            board.processRollingPin(held, player);
            return InteractionResult.SUCCESS;
        }

// 粽子等待糯米：芦苇叶 + 馅料齐全，手持非糯米物品时提示放糯米
        if (board.canMakeZongzi()) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            player.displayClientMessage(
                    Component.translatable("message.fidworkblock.choppingboard_need_rice"), true);
            return InteractionResult.SUCCESS;
        }

// 手持皮类物品：放入皮槽（每次只消耗 1 个；皮槽已有不同种皮则忽略，绝不取出旧皮）
        if (ChoppingboardBlockEntity.isSkinItem(held)) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            ItemStack one = held.copy();
            one.setCount(1);
            ItemStack remainder = board.addSkin(one);
            if (!remainder.isEmpty()) {
// 皮槽已满或已有不同种皮，放不进去：什么都不做
                return InteractionResult.SUCCESS;
            }
            if (!player.isCreative()) held.shrink(1);
            boolean reed = ChoppingboardBlockEntity.isReedLeaf(held);
            player.displayClientMessage(Component.translatable(reed
                    ? "message.fidworkblock.choppingboard_skin_placed_reed"
                    : "message.fidworkblock.choppingboard_skin_placed"), true);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F);
            return InteractionResult.SUCCESS;
        }

// 皮槽有皮：把手中物品作为馅料放到皮上（每次只消耗 1 个）
        if (!board.getSkinItem().isEmpty()) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            ItemStack one = held.copy();
            one.setCount(1);
            ItemStack remainder = board.addFilling(one);
            if (!remainder.isEmpty() && !player.isCreative()) {
// 馅料槽已满或已有其他物品，放不进去：什么都不做，避免误放方块
                return InteractionResult.SUCCESS;
            }
            if (!player.isCreative()) held.shrink(1);
            boolean reed = ChoppingboardBlockEntity.isReedLeaf(board.getSkinItem());
            player.displayClientMessage(Component.translatable(reed
                    ? "message.fidworkblock.choppingboard_filling_placed_reed"
                    : "message.fidworkblock.choppingboard_filling_placed"), true);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F);
            return InteractionResult.SUCCESS;
        }

// 放入食材槽（每次只消耗 1 个）
        if (board.canAddItem(held)) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            ItemStack one = held.copy();
            one.setCount(1);
            ItemStack remainder = board.addItem(one);
            if (!remainder.isEmpty() && !player.isCreative()) return InteractionResult.SUCCESS;
            if (!player.isCreative()) held.shrink(1);
            if (ChoppingboardBlockEntity.RAW_PIE_ID.equals(
                    held.getItem().builtInRegistryHolder().key().location())) {
                player.displayClientMessage(
                        Component.translatable("message.fidworkblock.choppingboard_pie_placed"), true);
            }
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F);
            return InteractionResult.SUCCESS;
        }

// 小型生物屠宰优先于普通切菜配方：该食材在 choppingboard_mob 定义中登记时接管本次交互
        if (board.processChop(held, player)) {
            return InteractionResult.SUCCESS;
        }

// 尝试用厨刀切菜（必须手持厨刀）
        if (board.processWithTool(held, player)) {
            return InteractionResult.SUCCESS;
        }

// 手持物品时绝不取出砧板上的东西：取出只属于空手右键
        return InteractionResult.PASS;
    }

// 空手取出顺序：先馅料，再皮，最后食材
    private static ItemStack takeFromBoard(ChoppingboardBlockEntity board) {
        ItemStack removed = board.removeFilling();
        if (removed.isEmpty()) removed = board.removeSkin();
        if (removed.isEmpty()) removed = board.removeItem();
        return removed;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof ChoppingboardBlockEntity board) {
                dropSlot(level, pos, board.getStoredItem());
                dropSlot(level, pos, board.getSkinItem());
                dropSlot(level, pos, board.getFillingItem());
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    private static void dropSlot(Level level, BlockPos pos, ItemStack stack) {
        if (!stack.isEmpty()) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChoppingboardBlockEntity(pos, state);
    }
}
