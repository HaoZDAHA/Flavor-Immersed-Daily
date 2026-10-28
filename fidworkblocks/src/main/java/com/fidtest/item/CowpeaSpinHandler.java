package com.fidtest.item;

import com.fidtest.ExampleMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.function.Supplier;

@EventBusSubscriber(modid = ExampleMod.MODID)
public class CowpeaSpinHandler {

    private static final ResourceLocation COWPEA_ID =
            ResourceLocation.fromNamespaceAndPath("flavor_immersed_daily", "cowpea");

    private static final float SPEED_PER_HIT = 36.0F;
    private static final int TICKS_PER_HIT = 50;
    private static final float MAX_SPEED = 60.0F;
    private static final int MAX_TICKS = 200;
    private static final float DECELERATION = 0.5F;

    private static Item cachedCowpeaItem = null;

    private static Item getCowpeaItem() {
        if (cachedCowpeaItem == null) {
            cachedCowpeaItem = BuiltInRegistries.ITEM.get(COWPEA_ID);
        }
        return cachedCowpeaItem;
    }

    private static boolean isHoldingCowpea(Player player) {
        Item cowpea = getCowpeaItem();
        if (cowpea == null) return false;
        ItemStack held = player.getMainHandItem();
        return held.is(cowpea);
    }

    @SubscribeEvent
    public static void onLivingDamagePost(LivingDamageEvent.Post event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;
        if (!isHoldingCowpea(player)) return;

        LivingEntity target = event.getEntity();
        if (target.isShiftKeyDown()) return;
        if (target.level().isClientSide) return;

        Supplier<AttachmentType<CowpeaSpinData>> attachmentSupplier = ExampleMod.COWPEA_SPIN;
        CowpeaSpinData current = target.getData(attachmentSupplier.get());

        float newSpeed;
        int newTicks;
        int newCombo;

        if (current.isActive()) {
            newSpeed = Math.min(current.spinSpeed() + SPEED_PER_HIT, MAX_SPEED);
            newTicks = Math.min(current.remainingTicks() + TICKS_PER_HIT, MAX_TICKS);
            newCombo = current.comboCount() + 1;
        } else {
            newSpeed = SPEED_PER_HIT;
            newTicks = TICKS_PER_HIT;
            newCombo = 1;
        }

        target.setData(attachmentSupplier.get(), new CowpeaSpinData(newSpeed, newTicks, newCombo));

        Vec3 vel = target.getDeltaMovement();
        target.setDeltaMovement(vel.x * 0.15, vel.y, vel.z * 0.15);

        applySpinStep(target, newSpeed);
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        if (entity.level().isClientSide) return;

        CowpeaSpinData data = entity.getData(ExampleMod.COWPEA_SPIN.get());
        if (!data.isActive()) return;

        applySpinStep(entity, data.spinSpeed());

        float newSpeed = Math.max(0.0F, data.spinSpeed() - DECELERATION);
        int newTicks = data.remainingTicks() - 1;

        if (newSpeed <= 0.0F || newTicks <= 0) {
            entity.setData(ExampleMod.COWPEA_SPIN.get(), CowpeaSpinData.EMPTY);
        } else {
            entity.setData(ExampleMod.COWPEA_SPIN.get(),
                    new CowpeaSpinData(newSpeed, newTicks, data.comboCount()));
        }
    }

    private static void applySpinStep(LivingEntity entity, float speed) {
        RandomSource rand = entity.getRandom();

        entity.setYRot(entity.getYRot() + speed);
        entity.yBodyRot += speed;
        entity.yHeadRot += speed;

        float wobble = speed * 0.005F;
        entity.setDeltaMovement(
                entity.getDeltaMovement().add(
                        (rand.nextFloat() - 0.5F) * wobble,
                        0.0,
                        (rand.nextFloat() - 0.5F) * wobble
                )
        );
        entity.hasImpulse = true;
    }
}
