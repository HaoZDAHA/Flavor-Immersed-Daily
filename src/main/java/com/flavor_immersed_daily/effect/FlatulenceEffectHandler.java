package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.datagen.tag.FIDItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

@Mod.EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class FlatulenceEffectHandler {

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity().level().isClientSide) return;

        if (event.getItem().is(FIDItemTags.RADISH)) {
            event.getEntity().addEffect(new MobEffectInstance(
                    ModEffects.FLATULENCE.get(), 600, 0));
        }
    }

    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;

        MobEffectInstance instance = entity.getEffect(ModEffects.FLATULENCE.get());
        if (instance == null) return;

        int amplifier = instance.getAmplifier();

        if (entity.getRandom().nextFloat() < 0.03f) {
            Vec3 motion = entity.getDeltaMovement();
            double strength = 0.3 + amplifier * 0.05;
            entity.setDeltaMovement(motion.x, motion.y + strength, motion.z);
            entity.hurtMarked = true;
        }

        if (entity.isInWater()) {
            AABB area = entity.getBoundingBox().inflate(5.0);
            entity.level().getEntitiesOfClass(Animal.class, area, e -> e != entity).forEach(landAnimal -> {
                Vec3 away = landAnimal.position().subtract(entity.position()).normalize().scale(0.5);
                landAnimal.setDeltaMovement(landAnimal.getDeltaMovement().add(away));
                landAnimal.hurtMarked = true;
            });

            entity.level().getEntitiesOfClass(Creeper.class, area, e -> e != entity).forEach(creeper -> {
                Vec3 away = creeper.position().subtract(entity.position()).normalize().scale(0.5);
                creeper.setDeltaMovement(creeper.getDeltaMovement().add(away));
                creeper.hurtMarked = true;
            });
        }
    }
}