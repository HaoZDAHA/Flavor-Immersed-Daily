package com.fidtest.block;

import com.fidtest.block.entity.AbstractCookingBlockEntity;
import com.fidtest.block.entity.DryerBlockEntity;
import com.fidtest.recipe.ApplianceType;
import com.fidtest.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class DryerBlock extends AbstractCookingBlock {

    public DryerBlock(BlockBehaviour.Properties properties) {
        super(properties, ApplianceType.DRYER);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DryerBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<? extends AbstractCookingBlockEntity> getBlockEntityType() {
        return ModBlockEntities.DRYER.get();
    }
}
