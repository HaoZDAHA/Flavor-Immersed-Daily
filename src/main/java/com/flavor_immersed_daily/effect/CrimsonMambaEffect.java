package com.flavor_immersed_daily.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * 赤色曼巴肘击（crimson_mamba）— 拥有期间，玩家近战攻击时击退效果提升并对周围生物造成群体击退，
 * 每次攻击附带特殊音效（逻辑见 CrimsonMambaEffectHandler）
 */
public class CrimsonMambaEffect extends MobEffect {

    public CrimsonMambaEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xDC143C);
    }
}
