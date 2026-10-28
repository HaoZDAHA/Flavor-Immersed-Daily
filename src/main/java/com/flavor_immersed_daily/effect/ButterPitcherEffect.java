package com.flavor_immersed_daily.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

// 黄油投手 — 持有期间发射弹射物命中目标时，有概率将目标游戏冻结（冰冻）数秒
public class ButterPitcherEffect extends MobEffect {

    public ButterPitcherEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xF5D93C);
    }
}
