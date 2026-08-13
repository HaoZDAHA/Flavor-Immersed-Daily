package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.datagen.tag.FIDItemTags;

import com.flavor_immersed_daily.all.ModEffects;

import com.flavor_immersed_daily.all.ModItems;

import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
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

/**
 * 鐏垎鐙傛敾锛坒ury_assault锛夋晥鏋滃鐞嗗櫒
 * 瑙﹀彂瑙勫垯锛?
 *  - 鐜╁椋熺敤鐗╁搧鍚庯紝鍓墜鎸佹湁 fid:seasoning 鏍囩鐗╁搧锛屾垨椋熺墿 NBT 鏍囩 seasoning 闈炵┖
 *  - 鍏朵腑鍓墜涓?chillipowder锛堣荆妞掔矇锛夋垨椋熺墿 NBT seasoning 涓?flavor_immersed_daily:chillipowder 鏃讹紝
 *    缁欎簣 45 绉?fury_assault 鏁堟灉锛屽苟娑堣€楀壇鎵嬭皟鍛虫枡涓€涓?
 * 鏁堟灉锛氭嫢鏈?fury_assault 鐨勭帺瀹舵瘡杩涜涓€娆¤繎鎴樻敾鍑伙細
 *  - 娑堣€楃帺瀹?config 鐐圭敓鍛藉€硷紙榛樿 1锛?
 *  - 鐜╁鍓嶆柟 config 鏍兼墖褰㈣寖鍥达紙鍗婅 30掳锛夊唴鐨勭敓鐗╄鐐圭噧锛屽苟鍙楀埌 config 鐐圭伀鐒颁激瀹筹紙榛樿 2锛?
 *  - 瑙﹀彂鍐插嚮绮掑瓙鐗规晥
 */
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class FuryAssaultEffectHandler {

    private static final int DURATION_TICKS = 45 * 20;
    /** 鎵囧舰鍗婅 30掳 鐨勪綑寮﹀€硷紙鐢ㄤ簬瑙掑害杩囨护锛?*/
    private static final double CONE_COS = Math.cos(Math.toRadians(30.0));
    /** 鐐圭噧鎸佺画鏃堕棿锛坱ick锛?*/
    private static final int FIRE_TICKS = 100;

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;
        if (!Config.furyAssaultEnabled) return;

        // 鍓墜鏄惁涓鸿皟鍛虫枡
        ItemStack offhand = player.getOffhandItem();
        boolean offhandIsSeasoning = offhand.is(FIDItemTags.SEASONING);

        // 椋熺敤鐨勯鐗?NBT 鏂囨湰鏍囩 seasoning锛?.21.1 瀛樹簬 CUSTOM_DATA 缁勪欢涓級
        CompoundTag tag = event.getItem().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        boolean foodHasSeasoning = tag.contains("seasoning", Tag.TAG_STRING);
        String foodSeasoning = foodHasSeasoning ? tag.getString("seasoning") : "";

        // 瑙﹀彂鏉′欢锛氬壇鎵嬫槸璋冨懗鏂?鎴?椋熺墿 seasoning 鏍囩闈炵┖
        if (!offhandIsSeasoning && !foodHasSeasoning) return;

        // 涓撳睘 buff锛氬壇鎵嬫槸杈ｆ绮夛紙chillipowder锛夋垨 椋熺墿 seasoning 鏍囩涓?flavor_immersed_daily:chillipowder 鈫?鐏垎鐙傛敾 45 绉?
        boolean isChiliPowder = offhand.is(ModItems.CHILLIPOWDER.get())
                || "flavor_immersed_daily:chillipowder".equals(foodSeasoning);
        if (isChiliPowder) {
            player.addEffect(new MobEffectInstance(ModEffects.FURY_ASSAULT, DURATION_TICKS, 0));
            // 鍓墜鎸佹湁璋冨懗鏂欐椂锛岃幏寰?buff 鐨勫悓鏃舵秷鑰椾竴涓?
            if (offhandIsSeasoning) {
                offhand.shrink(1);
            }
        }
    }

    @SubscribeEvent
    public static void onDamagePost(LivingDamageEvent.Post event) {
        if (!Config.furyAssaultEnabled) return;
        if (event.getEntity().level().isClientSide) return;

        DamageSource source = event.getSource();
        // 浠呰繎鎴橈細鐩存帴鏀诲嚮鑰呬笌浼ゅ鏉ユ簮鐩稿悓锛堟帓闄ょ鐭㈢瓑鎶曞皠鐗╋級
        if (source.getDirectEntity() != source.getEntity()) return;
        if (!(source.getEntity() instanceof Player player)) return;
        if (!player.hasEffect(ModEffects.FURY_ASSAULT)) return;

        Level level = player.level();
        if (!(level instanceof ServerLevel serverLevel)) return;

        // 1. 娑堣€楃帺瀹剁敓鍛藉€硷紙绮剧‘鎵ｈ锛屾棤瑙嗘姢鐢诧紱鑻ヤ笉瓒冲垯鐩存帴鍑绘潃锛?
        float health = player.getHealth() - (float) Config.furyAssaultHealthCost;
        if (health <= 0.0F) {
            player.hurt(player.damageSources().genericKill(), Float.MAX_VALUE);
        } else {
            player.setHealth(health);
        }

        // 2. 鍓嶆柟鎵囧舰鑼冨洿锛堣窛绂?鈮?鑼冨洿锛屽崐瑙?30掳锛夊唴鐨勭敓鐗╋細鐐圭噧 + 鐏劙浼ゅ
        double range = Config.furyAssaultRange;
        Vec3 facing = player.getLookAngle();
        Vec3 eye = player.getEyePosition();
        AABB area = player.getBoundingBox().inflate(range);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area, e ->
                e != player && e.isAlive() && e.distanceToSqr(player) <= range * range);

        for (LivingEntity target : targets) {
            Vec3 toTarget = target.position()
                    .add(0, target.getBbHeight() * 0.5, 0)
                    .subtract(eye)
                    .normalize();
            if (toTarget.dot(facing) < CONE_COS) continue;

            target.setRemainingFireTicks(FIRE_TICKS);
            target.hurt(serverLevel.damageSources().onFire(), (float) Config.furyAssaultFireDamage);
        }

        // 3. 鎵囧舰鐏劙绮掑瓙鏍囪瘑锛氬湪鎵囧舰鑼冨洿鍐呰鐩栫伀鐒扮壒鏁?
        double yaw = Math.toRadians(player.getYRot());
        double baseX = -Math.sin(yaw);
        double baseZ = Math.cos(yaw);
        double particleY = player.getY() + 0.5;
        for (double d = 1.0; d <= range; d += 1.0) {
            for (double ang = -30.0; ang <= 30.0; ang += 10.0) {
                double rad = Math.toRadians(ang);
                double dirX = baseX * Math.cos(rad) - baseZ * Math.sin(rad);
                double dirZ = baseZ * Math.cos(rad) + baseX * Math.sin(rad);
                serverLevel.sendParticles(ParticleTypes.FLAME,
                        player.getX() + dirX * d, particleY, player.getZ() + dirZ * d,
                        1, 0.1, 0.1, 0.1, 0);
            }
        }
    }
}
