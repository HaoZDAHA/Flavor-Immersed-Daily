package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.config.Config;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

// 八角镇香（aniseed_ward）— 拥有期间，周围半径（Config 可调，默认 8）格内的
// 亡灵生物被赋予减速效果；每 20 tick 刷新一次减速，离开范围后约 1 秒内消退。
// 总开关与半径见 Config。
public class AniseedWardEffect extends MobEffect {

// 赋予亡灵生物的减速时长（tick），与轮询间隔一致，离开范围后至多残留一轮约 1 秒
    private static final int SLOWNESS_TICKS = 20;

    public AniseedWardEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x6B8E23);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
// 每 20 tick（约 1 秒）轮询一次周围亡灵，避免每 tick 做 8 格范围实体扫描
        return duration % 20 == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!Config.aniseedWardEnabled) return true;
        Level level = entity.level();
        if (level.isClientSide) return true;

        double radius = Config.aniseedWardRadius;
        AABB area = entity.getBoundingBox().inflate(radius);
        List<LivingEntity> undead = level.getEntitiesOfClass(LivingEntity.class, area, e ->
                e != entity && e.isAlive() && e.getType().is(EntityTypeTags.UNDEAD)
                        && e.distanceToSqr(entity) <= radius * radius);

        for (LivingEntity target : undead) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, SLOWNESS_TICKS, 0));
        }
        return true;
    }
}
