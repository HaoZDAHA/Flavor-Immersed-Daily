package com.flavor_immersed_daily.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

// 清凉冰皮（cool_skin）— 免疫火焰伤害；
// 对免疫火焰伤害的敌对生物造成的近战伤害提升（Config，默认 +1）。
// 靠近火源（火焰、灵魂火、岩浆、营火）时立即清除。
//
// 免疫与加伤逻辑见 CoolSkinEffectHandler。
public class CoolSkinEffect extends MobEffect {

// 火源检测的水平/垂直半径（格）
    private static final int FIRE_CHECK_RADIUS = 2;

// 每 10 tick 检测一次火源，避免每 tick 扫描方块
    private static final int FIRE_CHECK_INTERVAL = 10;

    public CoolSkinEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x7FD8E8);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % FIRE_CHECK_INTERVAL == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        Level level = entity.level();
        if (level.isClientSide) return true;

        BlockPos origin = entity.blockPosition();
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-FIRE_CHECK_RADIUS, -1, -FIRE_CHECK_RADIUS),
                origin.offset(FIRE_CHECK_RADIUS, 1, FIRE_CHECK_RADIUS))) {
            if (isFireSource(level.getBlockState(pos))) {
// 返回 false 即由原版移除本效果
                return false;
            }
        }
        return true;
    }

    private static boolean isFireSource(BlockState state) {
        return state.is(Blocks.FIRE)
                || state.is(Blocks.SOUL_FIRE)
                || state.is(Blocks.LAVA)
                || state.is(Blocks.CAMPFIRE)
                || state.is(Blocks.SOUL_CAMPFIRE);
    }
}
