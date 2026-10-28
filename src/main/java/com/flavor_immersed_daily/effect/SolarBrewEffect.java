package com.flavor_immersed_daily.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

// 晒足一百八十天（solar_brew）— 拥有期间，玩家近战攻击露天生物时附加火焰伤害，
// 目标为亡灵生物时伤害更高（逻辑见 SolarBrewEffectHandler）
public class SolarBrewEffect extends MobEffect {

    public SolarBrewEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFD700);
    }
}
