package com.flavor_immersed_daily.block.blockentity;

import com.flavor_immersed_daily.all.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// 烤全羊方块实体 — 仅用于挂载渲染器，阶段数据存于方块状态中
public class CookedWholeSheepBlockEntity extends BlockEntity {

    public CookedWholeSheepBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COOKED_WHOLE_SHEEP_ENTITY.get(), pos, state);
    }
}
