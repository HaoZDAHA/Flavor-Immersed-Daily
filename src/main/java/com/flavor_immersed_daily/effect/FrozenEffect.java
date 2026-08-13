package com.flavor_immersed_daily.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * 冻结 — 标记实体处于冻结状态（AI 行为暂停，只能被攻击）
 * 实际逻辑由 Mixin 实现：MobMixin 暂停 serverAiStep，LivingEntityMixin 禁止移动
 */
public class FrozenEffect extends MobEffect {

    public FrozenEffect() {
        super(MobEffectCategory.HARMFUL, 0x92F2F2);
    }
}
