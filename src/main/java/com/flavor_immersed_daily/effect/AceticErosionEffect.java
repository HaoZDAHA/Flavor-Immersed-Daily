package com.flavor_immersed_daily.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

// 醋酸侵蚀 — 拥有期间攻击时，被攻击者的盔甲耐久损耗额外增加
public class AceticErosionEffect extends MobEffect {

    public AceticErosionEffect() {
        super(MobEffectCategory.NEUTRAL, 0xC8B088);
    }
}
