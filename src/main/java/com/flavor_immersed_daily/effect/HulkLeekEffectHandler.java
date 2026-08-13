package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

/**
 * 娴╁厠澶ц懕锛坔ulk_leek锛夋晥鏋滃鐞嗗櫒
 * 瑙﹀彂瑙勫垯锛?
 *  - 鐜╁椋熺敤鐗╁搧鍚庯紝鍓墜鎸佹湁 fid:seasoning 鏍囩鐗╁搧锛屾垨椋熺墿 NBT 鏍囩 seasoning 闈炵┖
 *  - 鍏朵腑鍓墜涓?onionpowder锛堟磱钁辩矇锛夋垨椋熺墿 NBT seasoning 涓?flavor_immersed_daily:onionpowder 鏃讹紝
 *    缁欎簣 45 绉?hulk_leek 鏁堟灉锛屽苟娑堣€楀壇鎵嬭皟鍛虫枡涓€涓?
 * 鏁堟灉锛氭嫢鏈?hulk_leek 鐨勭帺瀹惰繎鎴樻敾鍑诲辜骞村疄浣撴椂锛屽皢鍏惰浆鍖栦负鎴愬勾鐘舵€?
 */
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class HulkLeekEffectHandler {


    @SubscribeEvent
    public static void onDamagePost(LivingDamageEvent.Post event) {
        if (!Config.hulkLeekEnabled) return;
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide) return;

        DamageSource source = event.getSource();
        // 浠呰繎鎴橈細鐩存帴鏀诲嚮鑰呬笌浼ゅ鏉ユ簮鐩稿悓锛堟帓闄ょ鐭㈢瓑鎶曞皠鐗╋級
        if (source.getDirectEntity() != source.getEntity()) return;
        if (!(source.getEntity() instanceof Player player)) return;
        if (!player.hasEffect(ModEffects.HULK_LEEK)) return;

        // 骞煎勾瀹炰綋杞寲涓烘垚骞寸姸鎬?
        if (target instanceof Mob mob && mob.isBaby()) {
            mob.setBaby(false);
        }
    }
}
