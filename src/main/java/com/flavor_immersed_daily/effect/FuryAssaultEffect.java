package com.flavor_immersed_daily.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

// 火爆狂攻（fury_assault）— 拥有期间，玩家近战攻击时消耗自身生命值，
// 对前方扇形范围的生物造成火焰伤害并点燃（逻辑见 FuryAssaultEffectHandler）
public class FuryAssaultEffect extends MobEffect {

    public FuryAssaultEffect() {
        super(MobEffectCategory.NEUTRAL, 0xFF4500);
    }
}
