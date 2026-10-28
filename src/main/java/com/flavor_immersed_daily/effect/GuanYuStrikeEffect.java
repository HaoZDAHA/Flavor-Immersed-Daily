package com.flavor_immersed_daily.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

// 武圣（guan_yu_strike）— 拥有期间，可将手中的食物转化为一次性的近战武器，
// 饱食度转化为真实伤害（上限由 Config.guanYuStrikeMaxRealDamage 控制）
// （逻辑见 CombatEffectHandler）
public class GuanYuStrikeEffect extends MobEffect {

    public GuanYuStrikeEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xF0C75E);
    }
}
