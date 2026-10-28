package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.config.Config;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// 蔽日食月（eclipse_devour）效果处理器。
//
// 持有者攻击其它实体时，把该实体记为自己的"对战目标"；
// 之后持有者每次恢复生命值，就对对战目标造成等量伤害（复用模组的真实伤害类型）。
//
// 对战目标只保留最后一次攻击的实体，与效果描述一致。
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class EclipseDevourEffectHandler {

// 持有者 UUID -> 对战目标 UUID
    private static final Map<UUID, UUID> COMBAT_TARGETS = new HashMap<>();

    @SubscribeEvent
    public static void onAttack(LivingIncomingDamageEvent event) {
        if (!Config.eclipseDevourEnabled) return;
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof Player player)) return;
        if (player.level().isClientSide) return;

        LivingEntity victim = event.getEntity();
        if (victim == player) return;

        if (!player.hasEffect(ModEffects.ECLIPSE_DEVOUR)) {
            COMBAT_TARGETS.remove(player.getUUID());
            return;
        }
        COMBAT_TARGETS.put(player.getUUID(), victim.getUUID());
    }

    @SubscribeEvent
    public static void onHeal(LivingHealEvent event) {
        if (!Config.eclipseDevourEnabled) return;
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        UUID holderId = player.getUUID();
        if (!player.hasEffect(ModEffects.ECLIPSE_DEVOUR)) {
            COMBAT_TARGETS.remove(holderId);
            return;
        }

        UUID targetId = COMBAT_TARGETS.get(holderId);
        if (targetId == null) return;

        if (!(player.level() instanceof ServerLevel level)) return;
        Entity target = level.getEntity(targetId);
        if (!(target instanceof LivingEntity living) || !living.isAlive()) {
            COMBAT_TARGETS.remove(holderId);
            return;
        }

        float amount = event.getAmount();
        if (amount <= 0) return;

        Registry<DamageType> damageTypes = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        living.hurt(new DamageSource(damageTypes.getHolderOrThrow(CombatEffectHandler.REAL_DAMAGE)), amount);
    }
}
