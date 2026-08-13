package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.client.tooltip.SeasoningTooltip;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class SeasoningItem extends Item {
    private static final int DURATION_TICKS = 45 * 20;
    private static final int ICED_BLACK_TEA_DURATION_TICKS = 60 * 20;

    private final Supplier<? extends Holder<MobEffect>> effect;
    private final BooleanSupplier enabled;

    public SeasoningItem(Properties properties, Supplier<? extends Holder<MobEffect>> effect,
                         BooleanSupplier enabled) {
        super(properties);
        this.effect = effect;
        this.enabled = enabled;
    }

    public boolean applyEffect(Player player) {
        if (!enabled.getAsBoolean()) return false;
        player.addEffect(new MobEffectInstance(effect.get(), DURATION_TICKS, 0));
        return true;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return Optional.of(new SeasoningTooltip(List.of(
                new MobEffectInstance(effect.get(), DURATION_TICKS, 0))));
    }

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) return;

        ItemStack food = event.getItem();
        if (!food.has(DataComponents.FOOD)) return;
        if (food.is(ModItems.ICEDBLACKTEA.get())) {
            player.addEffect(new MobEffectInstance(ModEffects.CRIMSON_MAMBA,
                    ICED_BLACK_TEA_DURATION_TICKS, 0));
            return;
        }

        ItemStack offhand = player.getOffhandItem();
        if (offhand.getItem() instanceof SeasoningItem seasoning && seasoning.applyEffect(player)) {
            offhand.shrink(1);
        }
    }
}
