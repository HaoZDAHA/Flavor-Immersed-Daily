package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.config.Config;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

// 团团圆圆（reunion_recovery）— 拥有期间，周围半径（Config 可调，默认 16）格内的
// 所有玩家持续获得原版生命恢复效果（等级见 Config，默认 1 级）；
// 每次 tick 刷新，离开范围后快速消退。
public class ReunionRecoveryEffect extends MobEffect {

// 赋予附近玩家的生命恢复时长（tick），每次 tick 刷新
    private static final int REGEN_REFRESH_TICKS = 40;

    public ReunionRecoveryEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xE4572E);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!Config.reunionRecoveryEnabled) return;
        Level level = entity.level();
        if (level.isClientSide) return;

        double radius = Config.reunionRecoveryRadius;
        int regenAmplifier = Math.max(0, Config.reunionRecoveryLevel - 1);
        AABB area = entity.getBoundingBox().inflate(radius);
        List<Player> nearby = level.getEntitiesOfClass(Player.class, area,
                p -> p.isAlive() && p.distanceToSqr(entity) <= radius * radius);

        for (Player target : nearby) {
            target.addEffect(new MobEffectInstance(MobEffects.REGENERATION, REGEN_REFRESH_TICKS, regenAmplifier));
        }
    }
}