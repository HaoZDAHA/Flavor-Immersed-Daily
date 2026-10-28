package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.config.Config;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// 清凉冰皮（cool_skin）效果处理器。
// 1. 持有者免疫火焰伤害（直接取消火焰类伤害）；
// 2. 持有者近战攻击免疫火焰伤害的敌对生物时，额外造成 Config coolSkinFireDamageBonus 点伤害（默认 1）。
// "靠近火源即清除"由 CoolSkinEffect#applyEffectTick 处理。
@Mod.EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class CoolSkinEffectHandler {

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {
        if (!Config.coolSkinEnabled) return;

        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide) return;

        DamageSource source = event.getSource();

// 免疫火焰伤害
        if (victim.hasEffect(ModEffects.COOL_SKIN.get()) && source.is(DamageTypeTags.IS_FIRE)) {
            event.setCanceled(true);
            return;
        }

// 仅近战：攻击者与直接来源相同（排除箭矢等投掷物）
        if (source.getDirectEntity() != source.getEntity()) return;
        if (!(source.getEntity() instanceof Player player)) return;
        if (!player.hasEffect(ModEffects.COOL_SKIN.get())) return;
        if (!(victim instanceof Enemy) || !victim.fireImmune()) return;

        event.setAmount(event.getAmount() + (float) Config.coolSkinFireDamageBonus);
    }
}