package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

/**
 * 榛勬补鎶曟墜鏁堟灉澶勭悊鍣?
 * 瑙﹀彂瑙勫垯锛?
 *  - 鐜╁椋熺敤鐗╁搧鍚庯紝鍓墜鎸佹湁 fid:seasoning 鏍囩鐗╁搧锛屾垨椋熺墿 NBT 鏍囩 seasoning 闈炵┖
 *  - 鍏朵腑鍓墜涓?butter锛堥粍娌癸級鎴栭鐗?NBT seasoning 涓?flavor_immersed_daily:butter 鏃讹紝
 *    缁欎簣 45 绉?butter_pitcher 鏁堟灉锛屽苟娑堣€楀壇鎵嬭皟鍛虫枡涓€涓?
 * 鏁堟灉锛氭寔鏈?butter_pitcher 鏁堟灉鐨勭帺瀹跺彂灏勫脊灏勭墿鍛戒腑鐩爣鏃讹細
 *  - 鐩爣闈炵帺瀹讹紙缁濆涓嶈兘鏄帺瀹讹級
 *  - 鐩爣闈?Boss锛堝彲閰嶇疆寮€鍏筹紝榛樿寮€鍚級
 *  - 鎸夐厤缃鐜囷紙榛樿 25%锛夊皢鐩爣娓告垙鍐荤粨锛堝啺鍐伙級鏁扮锛堥粯璁?5 绉掞級
 */
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class ButterPitcherEffectHandler {


    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (!Config.butterPitcherEnabled) return;

        HitResult ray = event.getRayTraceResult();
        if (!(ray instanceof EntityHitResult entityHit)) return;

        Projectile projectile = event.getProjectile();
        if (projectile.level().isClientSide) return;

        // 鎶曟幏鑰呭繀椤绘槸鎸佹湁鏁堟灉鐨勭帺瀹?
        if (!(projectile.getOwner() instanceof Player player)) return;
        if (!player.hasEffect(ModEffects.BUTTER_PITCHER)) return;

        // 鐩爣缁濅笉鑳芥槸鐜╁
        Entity target = entityHit.getEntity();
        if (target instanceof Player) return;
        if (!(target instanceof LivingEntity livingTarget)) return;

        // 闈?Boss 妫€娴嬶紙鍙紑鍏筹級
        if (Config.butterPitcherExcludeBoss && isBoss(target)) return;

        // 姒傜巼鍒ゅ畾
        if (player.getRandom().nextDouble() >= Config.butterPitcherFreezeChance) return;

        // 鏂藉姞鍐荤粨鏁堟灉锛氭殏鍋滅洰鏍?AI 琛屼负鎸囧畾绉掓暟
        livingTarget.addEffect(new MobEffectInstance(
                ModEffects.FROZEN,
                (int) (Config.butterPitcherFreezeDuration * 20),
                0), player);
    }

    /**
     * 鍒ゅ畾瀹炰綋鏄惁涓?Boss锛氭湯褰遍緳銆佸噵鐏点€佸惊澹板畧鍗?
     */
    private static boolean isBoss(Entity entity) {
        return entity instanceof EnderDragon
                || entity instanceof WitherBoss
                || entity.getType() == EntityType.WARDEN;
    }
}
