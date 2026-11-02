package com.flavor_immersed_daily.entity;

// 生物 AI 冻结状态（由 LivingEntityMixin 实装）。
// 黄油投手命中后直接给实体写入冻结剩余刻数，不再通过 mob effect 实现。
// 放在非 mixin 包下，供普通类（如 ButterPitcherEffectHandler）直接引用；
// mixin 包内的类只能由 mixin 引用，不能在外部直接引用。
public interface Freezable {

// 冻结指定刻数，取与当前剩余刻数的较大值
    void fid$freeze(int ticks);

    boolean fid$isFrozen();
}
