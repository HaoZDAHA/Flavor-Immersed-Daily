package com.flavor_immersed_daily.client.tooltip;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

// 特色月饼 tooltip 组件 — 同时携带馅料物品与月饼 buff 列表，
// 客户端通过 ClientMooncakeTooltip 依次渲染馅料口味与 buff 信息。
public class MooncakeTooltip implements TooltipComponent {

    private final ItemStack filling;
    private final List<MobEffectInstance> effects;

    public MooncakeTooltip(ItemStack filling, List<MobEffectInstance> effects) {
        this.filling = filling;
        this.effects = effects;
    }

    public ItemStack filling() {
        return this.filling;
    }

    public List<MobEffectInstance> effects() {
        return this.effects;
    }
}