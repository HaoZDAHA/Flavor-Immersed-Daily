package com.flavor_immersed_daily.gameplay;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.block.common.block.FIDCropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

/**
 * 猪踩踏成熟作物时自动收割（类似 Farmer's Delight 的猪收获机制）。
 * 猪站在成熟的作物上时触发，掉落作物产物并重置生长阶段。
 */
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class PigHarvestHandler {

    private static final int COOLDOWN_TICKS = 20;

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof Pig pig)) return;
        if (pig.level().isClientSide) return;
        if (pig.tickCount % COOLDOWN_TICKS != 0) return;

        Level level = pig.level();
        BlockPos pos = pig.blockPosition();
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof FIDCropBlock crop
                && state.getValue(FIDCropBlock.AGE) >= crop.getMaxAge()) {
            level.setBlock(pos, crop.getStateForAge(0), Block.UPDATE_CLIENTS);

            // 掉落作物产物和种子
            Block.popResource(level, pos, new ItemStack(crop.getCropItem()));
            Block.popResource(level, pos, new ItemStack(crop.getSeedItem()));

            level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }
}
