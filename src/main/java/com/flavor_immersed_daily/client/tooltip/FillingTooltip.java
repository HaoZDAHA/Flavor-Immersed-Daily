package com.flavor_immersed_daily.client.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

// 馅料口味 tooltip 组件 — 携带馅料物品，
// 客户端通过 ClientFillingTooltip 渲染 "馅料口味：&lt;图标&gt; &lt;名称&gt;"
public class FillingTooltip implements TooltipComponent {

    private final ItemStack filling;

    public FillingTooltip(ItemStack filling) {
        this.filling = filling;
    }

    public ItemStack filling() {
        return this.filling;
    }
}