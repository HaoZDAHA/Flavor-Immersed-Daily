package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.all.ModEffects;

import com.flavor_immersed_daily.all.ModItems;

import com.flavor_immersed_daily.client.tooltip.SeasoningTooltip;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

/**
 * 璋冨懗鏂欑墿鍝?鈥?鍦?tooltip 涓檮鍔犲搴?buff 鐨勬晥鏋滅粍浠讹紙鍥炬爣 + 鍚嶇О + 鏃舵晥锛?
 * 瀵瑰簲鍏崇郴锛歜utter 鈫?butter_pitcher锛宻esameoil 鈫?sesame_slip锛寁inegar 鈫?acetic_erosion锛?
 *           thickbroadbeansauce 鈫?bean_fury锛宻alt 鈫?flavor_base锛宻oy 鈫?solar_brew锛?
 *           onionpowder 鈫?hulk_leek锛宑hillipowder 鈫?fury_assault锛宐rownsugarsyrup 鈫?crimson_mamba
 */
public class SeasoningItem extends Item {

    public SeasoningItem(Properties properties) {
        super(properties);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        MobEffectInstance effect = null;
        if (stack.is(ModItems.BUTTER.get())) {
            effect = new MobEffectInstance(ModEffects.BUTTER_PITCHER, 45 * 20);
        } else if (stack.is(ModItems.SESAMEOIL.get())) {
            effect = new MobEffectInstance(ModEffects.SESAME_SLIP, 45 * 20);
        } else if (stack.is(ModItems.VINEGAR.get())) {
            effect = new MobEffectInstance(ModEffects.ACETIC_EROSION, 45 * 20);
        } else if (stack.is(ModItems.THICKBROADBEANSAUCE.get())) {
            effect = new MobEffectInstance(ModEffects.BEAN_FURY, 45 * 20);
        } else if (stack.is(ModItems.SALT.get())) {
            effect = new MobEffectInstance(ModEffects.FLAVOR_BASE, 45 * 20);
        } else if (stack.is(ModItems.SOY.get())) {
            effect = new MobEffectInstance(ModEffects.SOLAR_BREW, 45 * 20);
        } else if (stack.is(ModItems.ONIONPOWDER.get())) {
            effect = new MobEffectInstance(ModEffects.HULK_LEEK, 45 * 20);
        } else if (stack.is(ModItems.CHILLIPOWDER.get())) {
            effect = new MobEffectInstance(ModEffects.FURY_ASSAULT, 45 * 20);
        } else if (stack.is(ModItems.BROWNSUGARSYRUP.get())) {
            effect = new MobEffectInstance(ModEffects.CRIMSON_MAMBA, 45 * 20);
        }
        if (effect == null) {
            return Optional.empty();
        }
        return Optional.of(new SeasoningTooltip(List.of(effect)));
    }
}
