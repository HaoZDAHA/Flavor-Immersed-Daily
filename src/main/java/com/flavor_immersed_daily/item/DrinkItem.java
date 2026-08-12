package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

import java.util.List;
import java.util.Optional;

/**
 * 可饮用物品 — 使用 drink 动画食用（与普通食物的 eat 动画区分）
 * icedblacktea（冰红茶）饮用后可获得 60 秒 赤色曼巴肘击（crimson_mamba）效果，
 * tooltip 显示方式与其他调味料一致（SeasoningTooltip 组件）
 */
public class DrinkItem extends Item {
    public DrinkItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if (stack.is(FlavorImmersedDaily.ICEDBLACKTEA.get())) {
            return Optional.of(new SeasoningTooltip(List.of(new MobEffectInstance(FlavorImmersedDaily.CRIMSON_MAMBA, 60 * 20))));
        }
        return Optional.empty();
    }
}
