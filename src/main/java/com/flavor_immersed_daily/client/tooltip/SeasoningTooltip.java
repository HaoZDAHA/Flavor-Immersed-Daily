package com.flavor_immersed_daily.client.tooltip;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.List;

/**
 * 调味料 tooltip 组件 — 携带调味料对应的 buff 效果列表，
 * 客户端通过 {@link ClientSeasoningTooltip} 渲染图标、名称、时长与详情
 */
public class SeasoningTooltip implements TooltipComponent {

    private final List<MobEffectInstance> effects;

    public SeasoningTooltip(List<MobEffectInstance> effects) {
        this.effects = effects;
    }

    public List<MobEffectInstance> effects() {
        return this.effects;
    }
}
