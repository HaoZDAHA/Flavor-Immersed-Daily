package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.datagen.tag.FIDItemTags;
import com.flavor_immersed_daily.all.ModSounds;
import com.flavor_immersed_daily.all.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

import java.util.List;

@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class CrimsonMambaEffectHandler {
    private static final int DURATION_TICKS = 45 * 20;
    private static final int ICED_BLACK_TEA_DURATION_TICKS = 60 * 20;
    private static final double MAIN_KNOCKBACK = 3.0;
    private static final double AOE_KNOCKBACK = 2.0;
    private static final double AOE_RADIUS = 3.0;
    private static final float SOUND_VOLUME = 2.0F;

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) return;
        if (event.getItem().is(ModItems.ICEDBLACKTEA.get())) {
            player.addEffect(new MobEffectInstance(ModEffects.holder(ModEffects.CRIMSON_MAMBA), ICED_BLACK_TEA_DURATION_TICKS, 0));
            return;
        }
        ItemStack offhand = player.getOffhandItem();
        boolean offhandIsSeasoning = offhand.is(FIDItemTags.SEASONING);
        CompoundTag tag = event.getItem().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        boolean foodHasSeasoning = tag.contains("seasoning", Tag.TAG_STRING);
        String foodSeasoning = foodHasSeasoning ? tag.getString("seasoning") : "";
        if (!offhandIsSeasoning && !foodHasSeasoning) return;
        if (offhand.is(ModItems.BROWNSUGARSYRUP.get())
                || "flavor_immersed_daily:brownsugarsyrup".equals(foodSeasoning)) {
            player.addEffect(new MobEffectInstance(ModEffects.holder(ModEffects.CRIMSON_MAMBA), DURATION_TICKS, 0));
            if (offhandIsSeasoning) offhand.shrink(1);
        }
    }

    @SubscribeEvent
    public static void onDamagePost(LivingDamageEvent.Post event) {
        if (event.getEntity().level().isClientSide) return;
        DamageSource source = event.getSource();
        if (source.getDirectEntity() != source.getEntity() || !(source.getEntity() instanceof Player player)
                || !player.hasEffect(ModEffects.holder(ModEffects.CRIMSON_MAMBA))) return;
        Level level = player.level();
        level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.MANBAOUT.get(), SoundSource.PLAYERS, SOUND_VOLUME, 1.0F);
        LivingEntity victim = event.getEntity();
        Vec3 knockDir = player.position().subtract(victim.position()).normalize();
        victim.knockback(MAIN_KNOCKBACK, knockDir.x, knockDir.z);
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, victim.getBoundingBox().inflate(AOE_RADIUS),
                entity -> entity.isAlive() && entity != player && entity != victim)) {
            target.knockback(AOE_KNOCKBACK, knockDir.x, knockDir.z);
        }
    }
}
