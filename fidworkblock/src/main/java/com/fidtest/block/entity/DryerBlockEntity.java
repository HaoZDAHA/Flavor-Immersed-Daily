package com.fidtest.block.entity;

import com.fidtest.recipe.ApplianceType;
import com.fidtest.registration.ModBlockEntities;
import com.fidtest.screen.CookingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class DryerBlockEntity extends AbstractCookingBlockEntity {

    public DryerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DRYER.get(), pos, blockState, ApplianceType.DRYER);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new CookingMenu(id, inv, this);
    }
}
