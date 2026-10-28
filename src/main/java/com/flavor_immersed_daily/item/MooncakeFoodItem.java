package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.client.tooltip.MooncakeTooltip;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

// 特色月饼 — 在馅料口味之外，食用后还会获得对应的月饼 buff。
// 与调味料一致：食用时按配置开关给予自身对应效果，
// tooltip 同时显示馅料口味与 buff（按住 Shift 查看 buff 详情）。
public class MooncakeFoodItem extends FillingFoodItem {

// 月饼 buff 的持续时长（tick）
    private static final int EFFECT_DURATION_TICKS = 30 * 20;

    private final Supplier<? extends Holder<MobEffect>> effect;
    private final BooleanSupplier enabled;

    public MooncakeFoodItem(Properties properties, Supplier<? extends Holder<MobEffect>> effect,
                            BooleanSupplier enabled) {
        super(properties);
        this.effect = effect;
        this.enabled = enabled;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);

        if (!level.isClientSide && this.enabled.getAsBoolean() && livingEntity instanceof Player player) {
            player.addEffect(new MobEffectInstance(this.effect.get().value(), EFFECT_DURATION_TICKS, 0));
        }
        return result;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        Item filling = getFillingItem(stack);
        return Optional.of(new MooncakeTooltip(
                filling == null ? ItemStack.EMPTY : new ItemStack(filling),
                List.of(new MobEffectInstance(this.effect.get().value(), EFFECT_DURATION_TICKS, 0))));
    }
}