package com.flavor_immersed_daily.integration.jade;

import com.flavor_immersed_daily.block.block.fruit.FruitingLeavesBlock;
import com.flavor_immersed_daily.block.block.machine.EggBreakingMachineBlock;
import com.flavor_immersed_daily.block.block.machine.FridgeBlock;
import com.flavor_immersed_daily.block.block.processing.WoodBasinBlock;
import com.flavor_immersed_daily.block.blockentity.EggBreakingMachineBlockEntity;
import com.flavor_immersed_daily.block.blockentity.FridgeBlockEntity;
import com.flavor_immersed_daily.block.blockentity.WoodBasinBlockEntity;
import com.flavor_immersed_daily.block.common.block.FIDCropBlock;
import com.flavor_immersed_daily.block.common.block.FIDLogMushroomBlock;
import com.flavor_immersed_daily.block.common.block.FIDWaterCropBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public final class FIDJadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(WorkstationProvider.INSTANCE, WoodBasinBlockEntity.class);
        registration.registerBlockDataProvider(WorkstationProvider.INSTANCE, EggBreakingMachineBlockEntity.class);
        registration.registerBlockDataProvider(WorkstationProvider.INSTANCE, FridgeBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(WorkstationProvider.INSTANCE, WoodBasinBlock.class);
        registration.registerBlockComponent(WorkstationProvider.INSTANCE, EggBreakingMachineBlock.class);
        registration.registerBlockComponent(WorkstationProvider.INSTANCE, FridgeBlock.class);
        registration.registerBlockComponent(GrowthProvider.INSTANCE, FIDCropBlock.class);
        registration.registerBlockComponent(GrowthProvider.INSTANCE, FIDWaterCropBlock.class);
        registration.registerBlockComponent(GrowthProvider.INSTANCE, FIDLogMushroomBlock.class);
        registration.registerBlockComponent(GrowthProvider.INSTANCE, FruitingLeavesBlock.class);
    }
}
