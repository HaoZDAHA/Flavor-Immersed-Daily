package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.client.tooltip.MooncakeTooltip;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

// 特色月饼 — 在馅料口味之外，食用后还会获得对应的月饼 buff。
//
// 与调味料一致：食用时按配置开关给予自身对应效果，
// tooltip 同时显示馅料口味与 buff（按住 Shift 查看 buff 详情）。
public class MooncakeFoodItem extends FillingFoodItem {

// 月饼 buff 的持续时长（tick）
    private static final int EFFECT_DURATION_TICKS = 30 * 20;

// TNT 馅月饼的爆炸半径
    private static final float TNT_EXPLOSION_RADIUS = 4.0F;
// TNT 馅月饼把人炸飞天的向上初速度（方块/tick）
    private static final double TNT_LAUNCH_SPEED = 2.0D;

// 只放烟花不伤人的爆炸判定：TNT 馅只负责把人炸上天，不造成伤害
    private static final ExplosionDamageCalculator TNT_MOONCAKE_DAMAGE_CALCULATOR = new ExplosionDamageCalculator() {
        @Override
        public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
            return false;
        }
    };

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
// 馅料是否为原版 TNT，需在 super 消耗物品前读取
        boolean tntFilling = getFillingItem(stack) == Items.TNT;

        ItemStack result = super.finishUsingItem(stack, level, livingEntity);

        if (!level.isClientSide) {
            if (tntFilling && livingEntity instanceof Player player) {
// TNT 馅：原地爆一下（不伤人不破坏地形），再把吃的人整个炸上天
                level.explode(player, null, TNT_MOONCAKE_DAMAGE_CALCULATOR,
                        player.getX(), player.getY(), player.getZ(),
                        TNT_EXPLOSION_RADIUS, false, Level.ExplosionInteraction.NONE);
                player.push(0.0D, TNT_LAUNCH_SPEED, 0.0D);
                player.hurtMarked = true;
            }
            if (this.enabled.getAsBoolean() && livingEntity instanceof Player player) {
                player.addEffect(new MobEffectInstance(this.effect.get(), EFFECT_DURATION_TICKS, 0));
            }
        }
        return result;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        Item filling = getFillingItem(stack);
        return Optional.of(new MooncakeTooltip(
                filling == null ? ItemStack.EMPTY : new ItemStack(filling),
                List.of(new MobEffectInstance(this.effect.get(), EFFECT_DURATION_TICKS, 0))));
    }
}
