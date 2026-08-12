package com.flavor_immersed_daily.block;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

//这个没用，我本来想着是在大挂钩前面渲染一个悬浮的刀具，但是没弄好，这个java类的文件没有参考价值，大家不要看。

public class BighookBlockEntity extends BlockEntity {

    public BighookBlockEntity(BlockPos pos, BlockState state) {
        super(FlavorImmersedDaily.BIGHOOK_BE.get(), pos, state);
    }
}