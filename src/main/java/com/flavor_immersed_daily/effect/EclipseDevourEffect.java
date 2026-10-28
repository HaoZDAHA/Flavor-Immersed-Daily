package com.flavor_immersed_daily.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

// 蔽日食月（eclipse_devour）— 拥有期间攻击其它实体时，把该实体选为自己的"对战目标"；
// 期间持有者恢复体力（生命值）时，对对战目标造成等量伤害。
//
// 触发逻辑见 EclipseDevourEffectHandler。
public class EclipseDevourEffect extends MobEffect {

    public EclipseDevourEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x4B2E83);
    }
}
