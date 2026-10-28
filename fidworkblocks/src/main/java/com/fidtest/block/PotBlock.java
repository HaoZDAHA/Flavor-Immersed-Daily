package com.fidtest.block;

import com.fidtest.block.entity.AbstractCookingBlockEntity;
import com.fidtest.block.entity.PotBlockEntity;
import com.fidtest.recipe.ApplianceType;
import com.fidtest.registration.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PotBlock extends AbstractCookingBlock {

    public PotBlock(BlockBehaviour.Properties properties) {
        super(properties, ApplianceType.POT);
    }

    public static final MapCodec<PotBlock> CODEC = simpleCodec(PotBlock::new);

    @Override
    protected MapCodec<PotBlock> codec() { return CODEC; }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PotBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<? extends AbstractCookingBlockEntity> getBlockEntityType() {
        return ModBlockEntities.POT.get();
    }
}
