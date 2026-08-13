package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.all.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.List;

@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class CrimsonMambaEffectHandler {
    private static final double MAIN_KNOCKBACK = 3.0;
    private static final double AOE_KNOCKBACK = 2.0;
    private static final double AOE_RADIUS = 3.0;
    private static final float SOUND_VOLUME = 2.0F;

    @SubscribeEvent
    public static void onDamagePost(LivingDamageEvent.Post event) {
        if (event.getEntity().level().isClientSide) return;
        DamageSource source = event.getSource();
        if (source.getDirectEntity() != source.getEntity() || !(source.getEntity() instanceof Player player)
                || !player.hasEffect(ModEffects.holder(ModEffects.CRIMSON_MAMBA))) return;
        Level level = player.level();
        level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.MANBAOUT.get(), SoundSource.PLAYERS, SOUND_VOLUME, 1.0F);
        LivingEntity victim = event.getEntity();
        Vec3 knockDir = player.position().subtract(victim.position()).normalize();
        victim.knockback(MAIN_KNOCKBACK, knockDir.x, knockDir.z);
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, victim.getBoundingBox().inflate(AOE_RADIUS),
                entity -> entity.isAlive() && entity != player && entity != victim)) {
            target.knockback(AOE_KNOCKBACK, knockDir.x, knockDir.z);
        }
    }
}
