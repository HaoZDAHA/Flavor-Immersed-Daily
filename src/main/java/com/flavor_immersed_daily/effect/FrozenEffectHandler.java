package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEffects;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

/**
 * 冻结效果的实际逻辑已迁移到 Mixin（MobMixin 暂停 serverAiStep，
 * LivingEntityMixin 通过 isImmobile 禁止移动），不再依赖 NoAI 标记。
 * <p>
 * 本类仅保留存档修复：旧实现会把 NoAi=true 持久化进实体 NBT，
 * 若实体在冻结期间被卸载则永久瘫痪。实体加载时如果仍带有 frozen
 * 效果却处于 NoAI 状态，即为旧方案的遗留，予以恢复。
 */
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class FrozenEffectHandler {

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) return;
        if (event.getEntity() instanceof Mob mob
                && mob.isNoAi()
                && mob.hasEffect(ModEffects.FROZEN)) {
            mob.setNoAi(false);
        }
    }
}
