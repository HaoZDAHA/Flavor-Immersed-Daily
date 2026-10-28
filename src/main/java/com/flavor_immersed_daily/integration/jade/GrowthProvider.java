package com.flavor_immersed_daily.integration.jade;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.block.block.fruit.FruitingLeavesBlock;
import com.flavor_immersed_daily.block.common.block.FIDCropBlock;
import com.flavor_immersed_daily.block.common.block.FIDLogMushroomBlock;
import com.flavor_immersed_daily.block.common.block.FIDWaterCropBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

final class GrowthProvider implements IBlockComponentProvider {
    static final GrowthProvider INSTANCE = new GrowthProvider();
    private static final ResourceLocation UID = new ResourceLocation(
            FlavorImmersedDaily.MODID, "growth");
    private GrowthProvider() {
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        BlockState state = accessor.getBlockState();
        int age;
        int maxAge;
        if (state.getBlock() instanceof FIDCropBlock crop) {
            age = crop.getAge(state);
            maxAge = crop.getMaxAge();
        } else if (state.getBlock() instanceof FIDWaterCropBlock crop) {
            age = state.getValue(crop.getAgeProperty());
            maxAge = crop.getMaxAge();
        } else if (state.getBlock() instanceof FIDLogMushroomBlock crop) {
            age = state.getValue(FIDLogMushroomBlock.AGE);
            maxAge = crop.getMaxAge();
        } else if (state.getBlock() instanceof FruitingLeavesBlock) {
            boolean mature = state.getValue(FruitingLeavesBlock.FRUITING);
            int maturity = mature ? FruitingLeavesBlock.MAX_MATURITY
                    : state.getValue(FruitingLeavesBlock.MATURITY);
            int percent = Math.round(maturity * 100.0F / FruitingLeavesBlock.MAX_MATURITY);
            tooltip.add(Component.translatable("jade.flavor_immersed_daily.fruit_maturity", percent));
            return;
        } else {
            return;
        }

        int percent = maxAge == 0 ? 100 : Math.round(age * 100.0F / maxAge);
        tooltip.add(Component.translatable(age >= maxAge
                ? "jade.flavor_immersed_daily.mature"
                : "jade.flavor_immersed_daily.growth", percent));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

}
