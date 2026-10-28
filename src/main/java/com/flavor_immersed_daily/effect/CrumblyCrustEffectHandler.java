package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.config.Config;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// 酥到掉渣（crumbly_crust）效果处理器。
// 持有者受到攻击时：在原地掉落月饼渣粒子（用沙子的破坏粒子），
// 并把附近的敌对生物吸引到该点，持续 Config crumblyCrustTauntSeconds 秒（默认 3 秒）；
// 期间这些生物放弃索敌（每 tick 清空目标），随后清除持有者的本效果。
// 性能：被吸引的生物记录在 #TAUNTED 中，每 tick 只遍历这一小撮生物，
// 不做全图扫描；寻路指令按 #REPATH_INTERVAL 间隔重发，粒子只发一次。
@Mod.EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class CrumblyCrustEffectHandler {

// 吸引敌对生物的范围（格）
    private static final double TAUNT_RADIUS = 16.0;

// 月饼渣粒子数量
    private static final int CRUMB_PARTICLE_COUNT = 24;

// 每多少 tick 重新下发一次寻路指令
    private static final int REPATH_INTERVAL = 5;

// 被吸引的生物 -> 目的点与到期时间（所在维度的 gameTime）
    private static final Map<Mob, Taunt> TAUNTED = new HashMap<>();

    @SubscribeEvent
    public static void onHurt(LivingDamageEvent event) {
        if (!Config.crumblyCrustEnabled) return;
        if (event.getAmount() <= 0) return;
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;
        if (!player.hasEffect(ModEffects.CRUMBLY_CRUST.get())) return;
        if (!(player.level() instanceof ServerLevel level)) return;

        player.removeEffect(ModEffects.CRUMBLY_CRUST.get());

        Vec3 spot = player.position();
        spawnCrumbs(level, spot);
        tauntHostiles(level, player, spot);
    }

// 在原地下落月饼渣粒子（沙子的破坏粒子）
    private static void spawnCrumbs(ServerLevel level, Vec3 spot) {
        level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SAND.defaultBlockState()),
                spot.x, spot.y + 0.1, spot.z,
                CRUMB_PARTICLE_COUNT, 0.4, 0.1, 0.4, 0.05);
    }

// 吸引范围内的敌对生物前往月饼渣所在位置
    private static void tauntHostiles(ServerLevel level, Player holder, Vec3 spot) {
        long expireTick = level.getGameTime() + (long) Config.crumblyCrustTauntSeconds * 20L;
        List<Mob> mobs = level.getEntitiesOfClass(Mob.class, holder.getBoundingBox().inflate(TAUNT_RADIUS),
                mob -> mob instanceof Enemy
                        && mob.isAlive()
                        && mob.distanceToSqr(holder) <= TAUNT_RADIUS * TAUNT_RADIUS);

        for (Mob mob : mobs) {
// 立刻放弃对持有者的索敌
            mob.setTarget(null);
            mob.setLastHurtByMob(null);
            TAUNTED.put(mob, new Taunt(spot, expireTick));
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (TAUNTED.isEmpty()) return;

        Iterator<Map.Entry<Mob, Taunt>> iterator = TAUNTED.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Mob, Taunt> entry = iterator.next();
            Mob mob = entry.getKey();
            Taunt taunt = entry.getValue();

            if (!mob.isAlive() || mob.isRemoved() || mob.level().getGameTime() > taunt.expireTick) {
                iterator.remove();
                continue;
            }

// 期间持续放弃索敌
            mob.setTarget(null);
            mob.setLastHurtByMob(null);

            if (mob.level().getGameTime() % REPATH_INTERVAL == 0 || !mob.getNavigation().isInProgress()) {
                mob.getNavigation().moveTo(taunt.spot.x, taunt.spot.y, taunt.spot.z, 1.0D);
            }
        }
    }

// 一次吸引记录：目的点 + 到期时间
    private static final class Taunt {
        final Vec3 spot;
        final long expireTick;

        Taunt(Vec3 spot, long expireTick) {
            this.spot = spot;
            this.expireTick = expireTick;
        }
    }
}