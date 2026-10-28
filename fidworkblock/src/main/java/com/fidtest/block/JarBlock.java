package com.fidtest.block;

import com.fidtest.block.entity.JarBlockEntity;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class JarBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty SEALED = BooleanProperty.create("sealed");

    private static final VoxelShape BASE = Block.box(4, 0, 4, 12, 1, 12);
    private static final VoxelShape BODY = Block.box(3, 1, 3, 13, 11, 13);
    private static final VoxelShape NECK = Block.box(5, 11, 5, 11, 14, 11);
    private static final VoxelShape SHAPE = Shapes.or(BASE, BODY, NECK);

    public JarBlock(BlockBehaviour.Properties p) {
        super(p);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SEALED, false));
    }

    @Override public RenderShape getRenderShape(BlockState s) { return RenderShape.MODEL; }
    @Override public VoxelShape getShape(BlockState s, BlockGetter l, BlockPos p, CollisionContext c) { return SHAPE; }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) { b.add(FACING, SEALED); }

    @Nullable @Override public BlockState getStateForPlacement(BlockPlaceContext ctx) { return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()); }
    @Override public BlockState rotate(BlockState s, Rotation r) { return s.setValue(FACING, r.rotate(s.getValue(FACING))); }
    @Override public BlockState mirror(BlockState s, Mirror m) { return s.rotate(m.getRotation(s.getValue(FACING))); }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof JarBlockEntity jar)) return InteractionResult.PASS;
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

        ItemStack held = player.getItemInHand(hand);

        if (!held.isEmpty()) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            ItemStack r = jar.addItemToNextSlot(player.isCreative() ? held.copy() : held);
            if (!player.isCreative()) player.setItemInHand(hand, r);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F);
            return InteractionResult.SUCCESS;
        }

        if (player.isShiftKeyDown() && jar.hasAnyInput()) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            ItemStack r = jar.removeLastInput();
            if (!player.isCreative()) player.getInventory().add(r);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        if (jar.hasOutput()) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            ItemStack out = jar.collectOutput();
            if (!out.isEmpty() && !player.isCreative()) player.getInventory().add(out);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
            return InteractionResult.SUCCESS;
        }

// 空手右键：识别配方并开始腌制
        if (jar.hasAnyInput()) {
            if (level.isClientSide) return InteractionResult.CONSUME;
            if (jar.checkRecipe()) {
                level.setBlock(pos, state.setValue(SEALED, true), 3);
                jar.seal();
                player.displayClientMessage(Component.translatable("message.fidworkblock.jar_recipe_correct"), true);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.6F);
            } else {
                player.displayClientMessage(Component.translatable("message.fidworkblock.jar_recipe_incorrect"), true);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof JarBlockEntity jar) {
            for (int i = 0; i < 5; i++) { ItemStack s = jar.getInventory().getStackInSlot(i); if (!s.isEmpty()) Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), s); }
// 账本中尚未落地的产物也要掉落，避免满一组时被吞掉
            for (ItemStack pending : jar.extractPendingResults()) { if (!pending.isEmpty()) Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), pending); }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    @Nullable @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new JarBlockEntity(pos, state); }
    @Nullable @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level l, BlockState s, BlockEntityType<T> t) {
        if (l.isClientSide) return null;
        return createTickerHelper(t, ModBlockEntities.JAR.get(), (lv, p, st, be) -> JarBlockEntity.serverTick(be));
    }
}
