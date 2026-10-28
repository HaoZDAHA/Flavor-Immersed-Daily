package com.fidtest.block;

import com.fidtest.block.entity.AbstractCookingBlockEntity;
import com.fidtest.block.entity.PressurecookerBlockEntity;
import com.fidtest.recipe.ApplianceType;
import com.fidtest.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class PressurecookerBlock extends AbstractCookingBlock {

    public PressurecookerBlock(BlockBehaviour.Properties properties) {
        super(properties, ApplianceType.PRESSURECOOKER);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PressurecookerBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<? extends AbstractCookingBlockEntity> getBlockEntityType() {
        return ModBlockEntities.PRESSURECOOKER.get();
    }
}
