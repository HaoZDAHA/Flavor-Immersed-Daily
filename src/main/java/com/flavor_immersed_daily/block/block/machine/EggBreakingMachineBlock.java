package com.flavor_immersed_daily.block.block.machine;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.block.blockentity.EggBreakingMachineBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class EggBreakingMachineBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final MapCodec<EggBreakingMachineBlock> CODEC =
            simpleCodec(EggBreakingMachineBlock::new);

    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 1);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape SHAPE = Shapes.join(
            Shapes.box(0.25, 0, 0.1875, 0.75, 0.4375, 0.8125),
            Shapes.box(0.375, 0.4375, 0.3125, 0.625, 0.90625, 0.5625),
            BooleanOp.OR
    );

    public EggBreakingMachineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    // ---- EntityBlock ----

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EggBreakingMachineBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (type == com.flavor_immersed_daily.all.ModBlockEntities.EGG_BREAKING_MACHINE_ENTITY.get()) {
            if (level.isClientSide()) {
                return (lvl, pos, st, be) -> EggBreakingMachineBlockEntity.clientTick(lvl, pos, st, (EggBreakingMachineBlockEntity) be);
            } else {
                return (lvl, pos, st, be) -> EggBreakingMachineBlockEntity.serverTick(lvl, pos, st, (EggBreakingMachineBlockEntity) be);
            }
        }
        return null;
    }

    // ---- 交互 ----

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (state.getValue(STAGE) != 0) return InteractionResult.PASS;
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (player instanceof ServerPlayer sp) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof EggBreakingMachineBlockEntity machine) {
                sp.openMenu(new SimpleMenuProvider(
                        (containerId, inv, p) -> new com.flavor_immersed_daily.screen.EggBreakingMachineMenu(
                                containerId, inv, machine.getInventory(), ContainerLevelAccess.create(level, pos)),
                        Component.translatable("block.flavor_immersed_daily.eggbreakingmachine")
                ), buf -> buf.writeBlockPos(pos));
            }
        }
        return InteractionResult.CONSUME;
    }

    // ---- 掉落 ----

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return super.getDrops(state, params);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof EggBreakingMachineBlockEntity machine) {
                machine.dropAllItems(level, pos);
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }
}