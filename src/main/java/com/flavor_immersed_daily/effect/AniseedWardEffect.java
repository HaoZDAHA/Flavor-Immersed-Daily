package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.config.Config;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

// 八角镇香（aniseed_ward）— 拥有期间，周围半径（Config 可调，默认 8）格内的
// 亡灵生物被赋予减速效果；每次 tick 刷新减速，离开范围后快速消退。
// 总开关与半径见 Config。
public class AniseedWardEffect extends MobEffect {

    private static final TagKey<EntityType<?>> UNDEAD =
            TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("minecraft", "undead"));

// 赋予亡灵生物的减速时长（tick），每次刷新
    private static final int SLOWNESS_TICKS = 40;

// 实体轮询间隔（tick），每 20 tick 刷新一次即可，避免每 tick 扫描周围实体
    private static final int REFRESH_INTERVAL = 20;

    public AniseedWardEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x6B8E23);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % REFRESH_INTERVAL == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!Config.aniseedWardEnabled) return;
        Level level = entity.level();
        if (level.isClientSide) return;

        double radius = Config.aniseedWardRadius;
        AABB area = entity.getBoundingBox().inflate(radius);
        List<LivingEntity> undead = level.getEntitiesOfClass(LivingEntity.class, area, e ->
                e != entity && e.isAlive() && e.getType().is(UNDEAD)
                        && e.distanceToSqr(entity) <= radius * radius);

        for (LivingEntity target : undead) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, SLOWNESS_TICKS, 0));
        }
    }
}