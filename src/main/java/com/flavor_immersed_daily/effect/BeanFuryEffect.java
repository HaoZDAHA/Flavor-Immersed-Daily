package com.flavor_immersed_daily.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

// 蘸豆，爽！（bean_fury）— 拥有期间，玩家近战攻击造成暴击（无需跳跃攻击）的概率提升
public class BeanFuryEffect extends MobEffect {

    public BeanFuryEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x8FBC3F);
    }
}
