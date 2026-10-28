package com.flavor_immersed_daily.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

// 浩克大葱（hulk_leek）— 拥有期间，玩家近战攻击幼年实体时将其转化为成年（逻辑见 HulkLeekEffectHandler）
public class HulkLeekEffect extends MobEffect {

    public HulkLeekEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x00A100);
    }
}
