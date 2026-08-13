package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.datagen.tag.FIDItemTags;

import com.flavor_immersed_daily.all.ModEffects;

import com.flavor_immersed_daily.all.ModItems;

import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

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

    private static final int DURATION_TICKS = 45 * 20;
    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD
    };

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;
        if (!Config.aceticErosionEnabled) return;

        // 鍓墜鏄惁涓鸿皟鍛虫枡
        ItemStack offhand = player.getOffhandItem();
        boolean offhandIsSeasoning = offhand.is(FIDItemTags.SEASONING);

        // 椋熺敤鐨勯鐗?NBT 鏂囨湰鏍囩 seasoning锛?.21.1 瀛樹簬 CUSTOM_DATA 缁勪欢涓級
        CompoundTag tag = event.getItem().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        boolean foodHasSeasoning = tag.contains("seasoning", Tag.TAG_STRING);
        String foodSeasoning = foodHasSeasoning ? tag.getString("seasoning") : "";

        // 瑙﹀彂鏉′欢锛氬壇鎵嬫槸璋冨懗鏂?鎴?椋熺墿 seasoning 鏍囩闈炵┖
        if (!offhandIsSeasoning && !foodHasSeasoning) return;

        // 涓撳睘 buff锛氬壇鎵嬫槸閱?鎴?椋熺墿 seasoning 鏍囩涓?flavor_immersed_daily:vinegar 鈫?閱嬮吀渚佃殌 45 绉?
        boolean isVinegar = offhand.is(ModItems.VINEGAR.get())
                || "flavor_immersed_daily:vinegar".equals(foodSeasoning);
        if (isVinegar) {
            player.addEffect(new MobEffectInstance(ModEffects.ACETIC_EROSION, DURATION_TICKS, 0));
            // 鍓墜鎸佹湁璋冨懗鏂欐椂锛岃幏寰?buff 鐨勫悓鏃舵秷鑰椾竴涓?
            if (offhandIsSeasoning) {
                offhand.shrink(1);
            }
        }
    }

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
