package com.flavor_immersed_daily.block.block.decorative;

import com.flavor_immersed_daily.block.blockentity.ColorfulFireworksBoxBlockEntity;
import com.flavor_immersed_daily.client.ClientGuiHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

//箱装烟花

public class ColorfulFireworksBoxBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final MapCodec<ColorfulFireworksBoxBlock> CODEC =
            simpleCodec(ColorfulFireworksBoxBlock::new);

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public ColorfulFireworksBoxBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
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

    // 交互————————————————————————————————————————————————————————————————————————

//无论是生存模式还是创造模式都有掉落物

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ColorfulFireworksBoxBlockEntity fwBe) {
                CompoundTag beTag = fwBe.saveWithId(level.registryAccess());
                if (!beTag.isEmpty()) {
                    ItemStack drop = new ItemStack(this);
                    drop.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(beTag));
                    popResource(level, pos, drop);
                }
            }
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (player.isShiftKeyDown()) {
            // 潜行右键：打开配置界面（仅客户端）
            if (level.isClientSide) {
                ClientGuiHelper.openColorfulFireworksBoxConfig(pos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // 非潜行右键：发射烟花
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof ColorfulFireworksBoxBlockEntity be) {
                be.launch(level, state.getValue(FACING));
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    //方块实体————————————————————————————————————————

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ColorfulFireworksBoxBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return (lvl, pos, st, be) -> ColorfulFireworksBoxBlockEntity.serverTick(lvl, pos, st,
                (ColorfulFireworksBoxBlockEntity) be);
    }
}
