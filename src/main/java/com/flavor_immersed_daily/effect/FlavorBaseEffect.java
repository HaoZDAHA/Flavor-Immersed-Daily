package com.flavor_immersed_daily.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

// 百味之基（flavor_base）— 拥有期间，玩家身上每有 1 种注册名以 "flavor" 开头的
// 本模组或附属模组 buff，则攻击伤害与移动速度获得加成（属性 modifier 由 FlavorBaseEffectHandler 管理）
public class FlavorBaseEffect extends MobEffect {

    public FlavorBaseEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xD8D8D8);
    }
}
