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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class FlatulenceEffectHandler {

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity().level().isClientSide) return;

        if (event.getItem().is(FIDItemTags.RADISH)) {
            event.getEntity().addEffect(new MobEffectInstance(
                    ModEffects.FLATULENCE, 600, 0));
        }
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        if (entity.level().isClientSide) return;

        MobEffectInstance instance = entity.getEffect(ModEffects.FLATULENCE);
        if (instance == null) return;

        int amplifier = instance.getAmplifier();

        if (entity.getRandom().nextFloat() < 0.03f) {
            Vec3 motion = entity.getDeltaMovement();
            double strength = 0.3 + amplifier * 0.05;
            entity.setDeltaMovement(motion.x, motion.y + strength, motion.z);
            entity.hurtMarked = true;
        }

        AABB area = entity.getBoundingBox().inflate(8.0);

        if (entity.isInWater()) {
            entity.level().getEntitiesOfClass(WaterAnimal.class, area, e -> e != entity)
                    .forEach(waterMob -> {
                        Vec3 towards = entity.position()
                                .subtract(waterMob.position())
                                .normalize()
                                .scale(0.02);
                        waterMob.setDeltaMovement(waterMob.getDeltaMovement().add(towards));
                        waterMob.hurtMarked = true;
                    });
        }

        entity.level().getEntitiesOfClass(Animal.class, area, e -> e != entity)
                .forEach(landAnimal -> {
                    Vec3 away = landAnimal.position()
                            .subtract(entity.position())
                            .normalize()
                            .scale(0.12);
                    landAnimal.setDeltaMovement(landAnimal.getDeltaMovement().add(away));
                    landAnimal.hurtMarked = true;
                });

        entity.level().getEntitiesOfClass(Creeper.class, area, e -> e != entity)
                .forEach(creeper -> {
                    Vec3 away = creeper.position()
                            .subtract(entity.position())
                            .normalize()
                            .scale(0.12);
                    creeper.setDeltaMovement(creeper.getDeltaMovement().add(away));
                    creeper.hurtMarked = true;
                });
    }
}
