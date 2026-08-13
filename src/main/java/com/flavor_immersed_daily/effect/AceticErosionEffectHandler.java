package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

/**
 * 閱嬮吀渚佃殌鏁堟灉澶勭悊鍣?
 * 瑙﹀彂瑙勫垯锛?
 *  - 鐜╁椋熺敤鐗╁搧鍚庯紝鍓墜鎸佹湁 fid:seasoning 鏍囩鐗╁搧锛屾垨椋熺墿 NBT 鏍囩 seasoning 闈炵┖
 *  - 鍏朵腑鍓墜涓?vinegar锛堥唻锛夋垨椋熺墿 NBT seasoning 涓?flavor_immersed_daily:vinegar 鏃讹紝
 *    缁欎簣 45 绉?acetic_erosion 鏁堟灉锛屽苟娑堣€楀壇鎵嬭皟鍛虫枡涓€涓?
 * 鏁堟灉锛氭嫢鏈?acetic_erosion 鐨勫疄浣撻€犳垚杩戞垬鏀诲嚮鏃讹紝琚敾鍑昏€呯殑姣忎欢鐩旂敳
 *       鑰愪箙鎹熻€楅澶栧鍔?config 涓殑鏁板€硷紙榛樿 1锛?
 */
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class AceticErosionEffectHandler {

    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD
    };

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Post event) {
        if (!Config.aceticErosionEnabled) return;
        if (event.getEntity().level().isClientSide) return;

        DamageSource source = event.getSource();
        // 浠呰繎鎴橈細鐩存帴鏀诲嚮鑰呬笌浼ゅ鏉ユ簮鐩稿悓锛屼笖鎸佹湁閱嬮吀渚佃殌鏁堟灉
        if (source.getDirectEntity() != source.getEntity()) return;
        if (!(source.getEntity() instanceof LivingEntity attacker)) return;
        if (!attacker.hasEffect(ModEffects.ACETIC_EROSION)) return;

        int extra = Config.aceticErosionExtraDurability;
        if (extra <= 0) return;

        // 琚敾鍑昏€呮瘡浠跺彲鎹熻€楃殑鐩旂敳棰濆鎹熻€?
        LivingEntity target = event.getEntity();
        int index = 0;
        for (ItemStack armor : target.getArmorSlots()) {
            if (!armor.isEmpty() && armor.isDamageableItem()) {
                armor.hurtAndBreak(extra, target, ARMOR_SLOTS[index]);
            }
            index++;
        }
    }
}
