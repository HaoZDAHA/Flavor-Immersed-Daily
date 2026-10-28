package com.flavor_immersed_daily.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

// 芝麻滑行 — 拥有期间实体行走高度（台阶跨越能力）提升
public class SesameSlipEffect extends MobEffect {

    public SesameSlipEffect() {
        super(MobEffectCategory.NEUTRAL, 0xE8C540);
    }
}
