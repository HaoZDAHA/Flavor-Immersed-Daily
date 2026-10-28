package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

// 百味之基（flavor_base）效果处理器
// 效果：拥有 flavor_base 期间，玩家身上每同时有 1 种注册名以 "flavor" 开头的
// 本模组或附属模组 buff，则：
// 攻击伤害 +config（默认 +1）
// 移动速度 +config（默认 +0.1）
// 最多叠加 config 次（默认 10 次）
@Mod.EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class FlavorBaseEffectHandler {

    private static final UUID ATTACK_DAMAGE_MODIFIER_UUID =
            UUID.nameUUIDFromBytes(("flavor_base_attack_damage").getBytes(StandardCharsets.UTF_8));
    private static final UUID MOVEMENT_SPEED_MODIFIER_UUID =
            UUID.nameUUIDFromBytes(("flavor_base_movement_speed").getBytes(StandardCharsets.UTF_8));

    private static final String ATTACK_DAMAGE_MODIFIER_NAME = "flavor_base_attack_damage";
    private static final String MOVEMENT_SPEED_MODIFIER_NAME = "flavor_base_movement_speed";

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;

        boolean active = Config.flavorBaseEnabled && entity.hasEffect(ModEffects.FLAVOR_BASE.get());
        if (!active) {
            removeModifiers(entity);
            return;
        }

// 统计身上注册名以 "flavor" 开头的 buff 数量（本模组及附属模组），最多叠加 config 上限
        int count = 0;
        for (MobEffectInstance instance : entity.getActiveEffects()) {
            ResourceLocation key = ForgeRegistries.MOB_EFFECTS.getKey(instance.getEffect());
            if (key != null && key.toString().startsWith("flavor")) {
                count++;
            }
        }
        int stacks = Math.min(count, Config.flavorBaseMaxStacks);

        if (stacks > 0) {
            updateModifier(entity, ATTACK_DAMAGE_MODIFIER_UUID, ATTACK_DAMAGE_MODIFIER_NAME, Attributes.ATTACK_DAMAGE,
                    stacks * Config.flavorBaseDamageBonus);
            updateModifier(entity, MOVEMENT_SPEED_MODIFIER_UUID, MOVEMENT_SPEED_MODIFIER_NAME, Attributes.MOVEMENT_SPEED,
                    stacks * Config.flavorBaseSpeedBonus);
        } else {
            removeModifiers(entity);
        }
    }

    private static void updateModifier(LivingEntity entity, UUID uuid, String name, Attribute attribute, double value) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance == null) return;
        AttributeModifier modifier = instance.getModifier(uuid);
        if (modifier == null) {
            instance.addTransientModifier(new AttributeModifier(uuid, name, value, AttributeModifier.Operation.ADDITION));
        } else if (modifier.getAmount() != value) {
            instance.removeModifier(uuid);
            instance.addTransientModifier(new AttributeModifier(uuid, name, value, AttributeModifier.Operation.ADDITION));
        }
    }

    private static void removeModifiers(LivingEntity entity) {
        AttributeInstance attackDamage = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamage != null && attackDamage.getModifier(ATTACK_DAMAGE_MODIFIER_UUID) != null) {
            attackDamage.removeModifier(ATTACK_DAMAGE_MODIFIER_UUID);
        }
        AttributeInstance movementSpeed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed != null && movementSpeed.getModifier(MOVEMENT_SPEED_MODIFIER_UUID) != null) {
            movementSpeed.removeModifier(MOVEMENT_SPEED_MODIFIER_UUID);
        }
    }
}
