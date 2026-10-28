package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.client.tooltip.SeasoningTooltip;
import com.flavor_immersed_daily.datagen.tag.FIDItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

// 调味料物品 — 持有对应 buff 效果与配置开关。
//
// buff 给予逻辑（#onFoodEaten）：
// 1. 冰红茶特例：直接给予赤色曼巴 60 秒；
// 2. 食物自身 NBT 标签 "seasoning"（内容为调味料注册名，如 flavor_immersed_daily:salt）非空时，
// 给予对应调味料 buff，不消耗任何物品；
// 3. 否则若副手持有调味料，给予对应 buff 并消耗副手 1 个。
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class SeasoningItem extends Item {
    public static final String SEASONING_NBT_KEY = "seasoning";
    private static final int DURATION_TICKS = 45 * 20;
    private static final int ICED_BLACK_TEA_DURATION_TICKS = 60 * 20;

// 调味料注册名 -> 调味料物品 的懒加载映射（由 fid:seasoning 标签动态构建，无需硬编码）
    private static Map<ResourceLocation, SeasoningItem> seasoningMap;

    private final Supplier<? extends Holder<MobEffect>> effect;
    private final BooleanSupplier enabled;

    public SeasoningItem(Properties properties, Supplier<? extends Holder<MobEffect>> effect,
                         BooleanSupplier enabled) {
        super(properties);
        this.effect = effect;
        this.enabled = enabled;
    }

// 应用对应 buff；受配置开关限制。返回是否成功施加（未启用时不施放）。
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

// 食物自身 NBT "seasoning" = 调味料注册名时，给予对应 buff（不消耗调味料）
        if (tryApplyFromNbt(player, food)) return;

        ItemStack offhand = player.getOffhandItem();
        if (offhand.getItem() instanceof SeasoningItem seasoning && seasoning.applyEffect(player)) {
            offhand.shrink(1);
        }
    }

// 读取食物 CUSTOM_DATA 中 seasoning 标签，解析注册名并给予对应 buff。
    private static boolean tryApplyFromNbt(Player player, ItemStack food) {
        CompoundTag tag = food.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        String seasoningName = tag.getString(SEASONING_NBT_KEY);
        if (seasoningName.isEmpty()) return false;

        ResourceLocation id = ResourceLocation.tryParse(seasoningName);
        if (id == null) return false;

        SeasoningItem seasoning = getSeasoningMap().get(id);
        if (seasoning == null) return false;
        return seasoning.applyEffect(player);
    }

// 懒加载：遍历 fid:seasoning 标签构建 注册名 -> SeasoningItem 映射。
    private static Map<ResourceLocation, SeasoningItem> getSeasoningMap() {
        if (seasoningMap == null) {
            Map<ResourceLocation, SeasoningItem> map = new HashMap<>();
            for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(FIDItemTags.SEASONING)) {
                Item item = holder.value();
                if (item instanceof SeasoningItem seasoning) {
                    map.put(BuiltInRegistries.ITEM.getKey(item), seasoning);
                }
            }
            seasoningMap = map;
        }
        return seasoningMap;
    }
}
