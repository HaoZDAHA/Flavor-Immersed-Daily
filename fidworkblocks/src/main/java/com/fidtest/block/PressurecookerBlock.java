package com.fidtest.block;

import com.fidtest.block.entity.AbstractCookingBlockEntity;
import com.fidtest.block.entity.PressurecookerBlockEntity;
import com.fidtest.recipe.ApplianceType;
import com.fidtest.registration.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PressurecookerBlock extends AbstractCookingBlock {

    public PressurecookerBlock(BlockBehaviour.Properties properties) {
        super(properties, ApplianceType.PRESSURECOOKER);
    }

    public static final MapCodec<PressurecookerBlock> CODEC = simpleCodec(PressurecookerBlock::new);

    @Override
    protected MapCodec<PressurecookerBlock> codec() { return CODEC; }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PressurecookerBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<? extends AbstractCookingBlockEntity> getBlockEntityType() {
        return ModBlockEntities.PRESSURECOOKER.get();
    }
}
