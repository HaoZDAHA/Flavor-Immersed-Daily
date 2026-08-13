package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.datagen.tag.FIDItemTags;

import com.flavor_immersed_daily.all.ModEffects;

import com.flavor_immersed_daily.all.ModItems;

import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

/**
 * 鏅掕冻涓€鐧惧叓鍗佸ぉ锛坰olar_brew锛夋晥鏋滃鐞嗗櫒
 * 瑙﹀彂瑙勫垯锛?
 *  - 鐜╁椋熺敤鐗╁搧鍚庯紝鍓墜鎸佹湁 fid:seasoning 鏍囩鐗╁搧锛屾垨椋熺墿 NBT 鏍囩 seasoning 闈炵┖
 *  - 鍏朵腑鍓墜涓?soy锛堥叡娌癸級鎴栭鐗?NBT seasoning 涓?flavor_immersed_daily:soy 鏃讹紝
 *    缁欎簣 45 绉?solar_brew 鏁堟灉锛屽苟娑堣€楀壇鎵嬭皟鍛虫枡涓€涓?
 * 鏁堟灉锛氭嫢鏈?solar_brew 鐨勭帺瀹惰繎鎴樻敾鍑荤敓鐗╂椂锛?
 *  - 琚敾鍑荤敓鐗╁浜庨湶澶╃幆澧冿紙澶撮《鑳界湅瑙佸ぉ绌猴級鈫?棰濆鍙楀埌 config 鐐圭伀鐒颁激瀹筹紙榛樿 0.5锛?
 *  - 琚敾鍑荤敓鐗╀负浜＄伒鐢熺墿 鈫?鍐嶉澶栧彈鍒?config 鐐圭伀鐒颁激瀹筹紙榛樿 0.5锛?
 */
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class SolarBrewEffectHandler {

    private static final int DURATION_TICKS = 45 * 20;

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;
        if (!Config.solarBrewEnabled) return;

        // 鍓墜鏄惁涓鸿皟鍛虫枡
        ItemStack offhand = player.getOffhandItem();
        boolean offhandIsSeasoning = offhand.is(FIDItemTags.SEASONING);

        // 椋熺敤鐨勯鐗?NBT 鏂囨湰鏍囩 seasoning锛?.21.1 瀛樹簬 CUSTOM_DATA 缁勪欢涓級
        CompoundTag tag = event.getItem().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        boolean foodHasSeasoning = tag.contains("seasoning", Tag.TAG_STRING);
        String foodSeasoning = foodHasSeasoning ? tag.getString("seasoning") : "";

        // 瑙﹀彂鏉′欢锛氬壇鎵嬫槸璋冨懗鏂?鎴?椋熺墿 seasoning 鏍囩闈炵┖
        if (!offhandIsSeasoning && !foodHasSeasoning) return;

        // 涓撳睘 buff锛氬壇鎵嬫槸閰辨补锛坰oy锛夋垨 椋熺墿 seasoning 鏍囩涓?flavor_immersed_daily:soy 鈫?鏅掕冻涓€鐧惧叓鍗佸ぉ 45 绉?
        boolean isSoy = offhand.is(ModItems.SOY.get())
                || "flavor_immersed_daily:soy".equals(foodSeasoning);
        if (isSoy) {
            player.addEffect(new MobEffectInstance(ModEffects.SOLAR_BREW, DURATION_TICKS, 0));
            // 鍓墜鎸佹湁璋冨懗鏂欐椂锛岃幏寰?buff 鐨勫悓鏃舵秷鑰椾竴涓?
            if (offhandIsSeasoning) {
                offhand.shrink(1);
            }
        }
    }

    @SubscribeEvent
    public static void onDamagePost(LivingDamageEvent.Post event) {
        if (!Config.solarBrewEnabled) return;
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide) return;

        DamageSource source = event.getSource();
        // 浠呰繎鎴橈細鐩存帴鏀诲嚮鑰呬笌浼ゅ鏉ユ簮鐩稿悓锛堟帓闄ょ鐭㈢瓑鎶曞皠鐗╋級
        if (source.getDirectEntity() != source.getEntity()) return;
        if (!(source.getEntity() instanceof Player player)) return;
        if (!player.hasEffect(ModEffects.SOLAR_BREW)) return;

        // 闇插ぉ鐜锛堝ご椤惰兘鐪嬭澶╃┖锛?
        boolean openSky = target.level().canSeeSky(target.blockPosition());
        if (openSky) {
            target.hurt(target.level().damageSources().onFire(), (float) Config.solarBrewOpenSkyFireDamage);
        }

        // 浜＄伒鐢熺墿鍐嶉澶栦竴娆?
        if (target.getType().is(EntityTypeTags.UNDEAD)) {
            target.hurt(target.level().damageSources().onFire(), (float) Config.solarBrewUndeadFireDamage);
        }
    }
}
