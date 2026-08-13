package com.flavor_immersed_daily.block.block.furniture;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEntities;
import com.flavor_immersed_daily.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChairBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);

    public ChairBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        // 如果玩家正潜行，允许正常交互（如放置方块等）
        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        // 如果已经有玩家坐在椅子上，不允许再坐
        if (SeatEntity.hasSeatEntity(level, pos)) {
            return InteractionResult.PASS;
        }

        // 玩家必须在地面上，不能在空中
        if (!level.isClientSide) {
            SeatEntity seat = new SeatEntity(ModEntities.SEAT_ENTITY.get(), level, pos);
            level.addFreshEntity(seat);
            player.startRiding(seat);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
