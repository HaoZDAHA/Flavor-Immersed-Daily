package com.fidtest.block;

import com.fidtest.block.entity.WokBlockEntity;
import com.fidtest.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
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

import javax.annotation.Nullable;

public class WokBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OILED = BooleanProperty.create("oiled");
    private static final ResourceLocation OIL_ID = new ResourceLocation("flavor_immersed_daily", "cookingoil");
    private static final ResourceLocation SPATULA_ID = new ResourceLocation("flavor_immersed_daily", "spatula");

    public WokBlock(BlockBehaviour.Properties p) {
        super(p);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OILED, false));
    }

    @Override public RenderShape getRenderShape(BlockState s) { return RenderShape.MODEL; }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) { b.add(FACING, OILED); }
    @Nullable @Override public BlockState getStateForPlacement(BlockPlaceContext ctx) { return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()); }
    @Override public BlockState rotate(BlockState s, Rotation r) { return s.setValue(FACING, r.rotate(s.getValue(FACING))); }
    @Override public BlockState mirror(BlockState s, Mirror m) { return s.rotate(m.getRotation(s.getValue(FACING))); }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof WokBlockEntity wok)) return InteractionResult.PASS;
        if (level.isClientSide) return InteractionResult.CONSUME;

        ItemStack held = player.getItemInHand(hand);
        if (held.isEmpty()) return InteractionResult.PASS;

        boolean oiled = state.getValue(OILED);

        if (!oiled) {
            if (isCookingOil(held)) {
                level.setBlock(pos, state.setValue(OILED, true), 3);
                if (!player.isCreative()) held.shrink(1);
                level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 1.0F);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        if (isSpatula(held)) {
            if (wok.getItemCount() == 0) return InteractionResult.PASS;
            wok.stirFry(player);
            level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.4F, 1.2F);
            return InteractionResult.SUCCESS;
        }

        if (isSeasoning(held)) {
            if (wok.getItemCount() == 0) return InteractionResult.PASS;
            wok.setSeasoningName(held.getItem().builtInRegistryHolder().key().location().toString());
            wok.finishCooking(level, pos);
            if (!player.isCreative()) held.shrink(1);
            level.setBlock(pos, state.setValue(OILED, false), 3);
            level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        if (!held.isDamageableItem() && wok.canAddItem()) {
            ItemStack s = held.copy(); s.setCount(1);
            wok.addItem(s);
            if (!player.isCreative()) held.shrink(1);
            level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.3F, 1.5F);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof WokBlockEntity wok) {
            for (int i = 0; i < 6; i++) { ItemStack s = wok.getInventory().getStackInSlot(i); if (!s.isEmpty()) Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), s); }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    @Nullable @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new WokBlockEntity(pos, state); }
    @Nullable @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level l, BlockState s, BlockEntityType<T> t) {
        if (l.isClientSide) return null;
        return createTickerHelper(t, ModBlockEntities.WOK.get(), (lv, p, st, be) -> be.tick());
    }

    private boolean isCookingOil(ItemStack s) { return s.getItem().builtInRegistryHolder().key().location().equals(OIL_ID); }
    private boolean isSpatula(ItemStack s) { return s.getItem().builtInRegistryHolder().key().location().equals(SPATULA_ID); }
    private boolean isSeasoning(ItemStack s) {
        return s.getItem().builtInRegistryHolder().tags()
                .anyMatch(t -> t.location().toString().equals("fid:seasoning")
                        || t.location().toString().equals("forge:seasoning"));
    }

}
