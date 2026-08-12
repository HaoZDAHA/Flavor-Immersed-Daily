package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

/**
 * 调味料物品 — 在 tooltip 中附加对应 buff 的效果组件（图标 + 名称 + 时效）
 * 对应关系：butter → butter_pitcher，sesameoil → sesame_slip，vinegar → acetic_erosion，
 *           thickbroadbeansauce → bean_fury，salt → flavor_base，soy → solar_brew，
 *           onionpowder → hulk_leek，chillipowder → fury_assault，brownsugarsyrup → crimson_mamba
 */
public class SeasoningItem extends Item {

    public SeasoningItem(Properties properties) {
        super(properties);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        MobEffectInstance effect = null;
        if (stack.is(FlavorImmersedDaily.BUTTER.get())) {
            effect = new MobEffectInstance(FlavorImmersedDaily.BUTTER_PITCHER, 45 * 20);
        } else if (stack.is(FlavorImmersedDaily.SESAMEOIL.get())) {
            effect = new MobEffectInstance(FlavorImmersedDaily.SESAME_SLIP, 45 * 20);
        } else if (stack.is(FlavorImmersedDaily.VINEGAR.get())) {
            effect = new MobEffectInstance(FlavorImmersedDaily.ACETIC_EROSION, 45 * 20);
        } else if (stack.is(FlavorImmersedDaily.THICKBROADBEANSAUCE.get())) {
            effect = new MobEffectInstance(FlavorImmersedDaily.BEAN_FURY, 45 * 20);
        } else if (stack.is(FlavorImmersedDaily.SALT.get())) {
            effect = new MobEffectInstance(FlavorImmersedDaily.FLAVOR_BASE, 45 * 20);
        } else if (stack.is(FlavorImmersedDaily.SOY.get())) {
            effect = new MobEffectInstance(FlavorImmersedDaily.SOLAR_BREW, 45 * 20);
        } else if (stack.is(FlavorImmersedDaily.ONIONPOWDER.get())) {
            effect = new MobEffectInstance(FlavorImmersedDaily.HULK_LEEK, 45 * 20);
        } else if (stack.is(FlavorImmersedDaily.CHILLIPOWDER.get())) {
            effect = new MobEffectInstance(FlavorImmersedDaily.FURY_ASSAULT, 45 * 20);
        } else if (stack.is(FlavorImmersedDaily.BROWNSUGARSYRUP.get())) {
            effect = new MobEffectInstance(FlavorImmersedDaily.CRIMSON_MAMBA, 45 * 20);
        }
        if (effect == null) {
            return Optional.empty();
        }
        return Optional.of(new SeasoningTooltip(List.of(effect)));
    }
}
