package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.config.Config;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

// 咆哮（yi_de_roar）与武圣（guan_yu_strike）效果处理器：
// 武圣：拥有者手持食物攻击时，消耗一个食物，将饱食度转化为真实伤害（上限见 Config）
// 咆哮的"忽略无敌帧"机制由 EntityMixin 内部实现（@Shadow + @Inject isInvulnerableTo），无需在此处理。
@Mod.EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class CombatEffectHandler {

// 武圣真实伤害的自定义伤害类型
    public static final ResourceKey<DamageType> REAL_DAMAGE = ResourceKey.create(
            Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "real_damage"));

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        if (!(event.getTarget() instanceof LivingEntity target)) return;

// 咆哮的"忽略目标无敌帧"由 EntityMixin 内部实现（@Inject isInvulnerableTo），这里无需处理

// 武圣：手持食物攻击 → 消耗食物，饱食度转化为真实伤害
        if (player.hasEffect(ModEffects.GUAN_YU_STRIKE.get())) {
            ItemStack held = player.getMainHandItem();
            FoodProperties food = held.getFoodProperties(player);
            if (food != null) {
                float realDamage = Math.min(food.getNutrition(), (float) Config.guanYuStrikeMaxRealDamage);
                held.shrink(1);
                if (realDamage > 0) {
                    Registry<DamageType> damageTypes = player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
                    target.hurt(new DamageSource(damageTypes.getHolderOrThrow(REAL_DAMAGE)), realDamage);
                }
            }
        }
    }
}
