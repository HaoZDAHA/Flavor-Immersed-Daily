package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// 调味料 buff 触发与芝麻滑行效果处理器
// 效果：拥有 sesame_slip 期间，实体的行走高度提升为 config 中的数值（默认 2.1 格）
@Mod.EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class SesameSlipEffectHandler {

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;

        boolean active = Config.sesameSlipEnabled && entity.hasEffect(ModEffects.SESAME_SLIP.get());
// 原版默认步高 0.6
        float target = active ? (float) Config.sesameSlipHeight : 0.6F;
        if (entity.maxUpStep() != target) {
            entity.setMaxUpStep(target);
        }
    }
}