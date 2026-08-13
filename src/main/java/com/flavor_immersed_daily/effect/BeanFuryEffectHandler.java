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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

/**
 * 铇歌眴锛岀埥锛侊紙bean_fury锛夋晥鏋滃鐞嗗櫒
 * 瑙﹀彂瑙勫垯锛?
 *  - 鐜╁椋熺敤鐗╁搧鍚庯紝鍓墜鎸佹湁 fid:seasoning 鏍囩鐗╁搧锛屾垨椋熺墿 NBT 鏍囩 seasoning 闈炵┖
 *  - 鍏朵腑鍓墜涓?thickbroadbeansauce锛堣眴鐡ｉ叡锛夋垨椋熺墿 NBT seasoning 涓?
 *    flavor_immersed_daily:thickbroadbeansauce 鏃讹紝缁欎簣 45 绉?bean_fury 鏁堟灉锛屽苟娑堣€楀壇鎵嬭皟鍛虫枡涓€涓?
 * 鏁堟灉锛氭嫢鏈夎鏁堟灉鐨勭帺瀹惰繘琛岃繎鎴樻敾鍑绘椂锛屾寜閰嶇疆姒傜巼锛堥粯璁?25%锛夎Е鍙戞毚鍑伙細
 *  - 浼ゅ鎻愬崌 1.5 鍊嶏紙涓庡師鐗堣烦璺冩毚鍑荤浉鍚岋級
 *  - 鎾斁鏆村嚮绮掑瓙涓庨煶鏁?
 *  - 鏃犻渶璺宠穬鏀诲嚮
 */
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class BeanFuryEffectHandler {

    /** 鍘熺増璺宠穬鏆村嚮鐨勪激瀹冲€嶇巼 */
    private static final float CRIT_MULTIPLIER = 1.5F;
    private static final int DURATION_TICKS = 45 * 20;

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;
        if (!Config.beanFuryEnabled) return;

        // 鍓墜鏄惁涓鸿皟鍛虫枡
        ItemStack offhand = player.getOffhandItem();
        boolean offhandIsSeasoning = offhand.is(FIDItemTags.SEASONING);

        // 椋熺敤鐨勯鐗?NBT 鏂囨湰鏍囩 seasoning锛?.21.1 瀛樹簬 CUSTOM_DATA 缁勪欢涓級
        CompoundTag tag = event.getItem().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        boolean foodHasSeasoning = tag.contains("seasoning", Tag.TAG_STRING);
        String foodSeasoning = foodHasSeasoning ? tag.getString("seasoning") : "";

        // 瑙﹀彂鏉′欢锛氬壇鎵嬫槸璋冨懗鏂?鎴?椋熺墿 seasoning 鏍囩闈炵┖
        if (!offhandIsSeasoning && !foodHasSeasoning) return;

        // 涓撳睘 buff锛氬壇鎵嬫槸璞嗙摚閰?鎴?椋熺墿 seasoning 鏍囩涓?flavor_immersed_daily:thickbroadbeansauce 鈫?铇歌眴锛岀埥锛?45 绉?
        boolean isBeanPaste = offhand.is(ModItems.THICKBROADBEANSAUCE.get())
                || "flavor_immersed_daily:thickbroadbeansauce".equals(foodSeasoning);
        if (isBeanPaste) {
            player.addEffect(new MobEffectInstance(ModEffects.BEAN_FURY, DURATION_TICKS, 0));
            // 鍓墜鎸佹湁璋冨懗鏂欐椂锛岃幏寰?buff 鐨勫悓鏃舵秷鑰椾竴涓?
            if (offhandIsSeasoning) {
                offhand.shrink(1);
            }
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingIncomingDamageEvent event) {
        if (!Config.beanFuryEnabled) return;
        if (event.getEntity().level().isClientSide) return;

        DamageSource source = event.getSource();
        // 浠呰繎鎴橈細鐩存帴鏀诲嚮鑰呬笌浼ゅ鏉ユ簮鐩稿悓锛堟帓闄ょ鐭㈢瓑鎶曞皠鐗╋級
        if (source.getDirectEntity() != source.getEntity()) return;
        if (!(source.getEntity() instanceof Player player)) return;
        if (!player.hasEffect(ModEffects.BEAN_FURY)) return;

        // 姒傜巼鍒ゅ畾
        if (player.getRandom().nextDouble() >= Config.beanFuryCritChance) return;

        // 鏆村嚮锛?.5 鍊嶄激瀹?
        event.setAmount(event.getAmount() * CRIT_MULTIPLIER);

        // 鏆村嚮绮掑瓙涓庨煶鏁?
        LivingEntity target = event.getEntity();
        if (target.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.CRIT,
                    target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ(),
                    10, 0.2, 0.2, 0.2, 0.1);
            serverLevel.playSound(null, target.blockPosition(),
                    SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }
}
