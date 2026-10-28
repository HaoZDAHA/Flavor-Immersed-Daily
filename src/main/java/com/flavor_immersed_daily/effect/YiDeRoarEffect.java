package com.flavor_immersed_daily.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

// 咆哮（yi_de_roar）— 拥有期间，攻击可以忽略生物的无敌帧
// （逻辑见 CombatEffectHandler）
public class YiDeRoarEffect extends MobEffect {

    public YiDeRoarEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xC63B3B);
    }
}
