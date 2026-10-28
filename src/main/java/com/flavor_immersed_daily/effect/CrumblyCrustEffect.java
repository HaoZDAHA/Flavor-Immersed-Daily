package com.flavor_immersed_daily.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

// 酥到掉渣（crumbly_crust）— 拥有期间受到攻击时，
// 在原地掉落月饼渣粒子并吸引敌对生物前往此处（时长见 Config，默认 3 秒），
// 期间敌对生物放弃索敌，同时清除持有者的本效果，因此是一次性触发。
// 触发逻辑见 CrumblyCrustEffectHandler。
public class CrumblyCrustEffect extends MobEffect {

    public CrumblyCrustEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xD9A441);
    }
}