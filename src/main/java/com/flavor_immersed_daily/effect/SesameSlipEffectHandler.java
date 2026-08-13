package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.datagen.tag.FIDItemTags;

import com.flavor_immersed_daily.all.ModEffects;

import com.flavor_immersed_daily.all.ModItems;

import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

/**
 * 璋冨懗鏂?buff 瑙﹀彂涓庤姖楹绘粦琛屾晥鏋滃鐞嗗櫒
 * 瑙﹀彂瑙勫垯锛?
 *  - 鐜╁椋熺敤鐗╁搧鍚庯紝鍓墜鎸佹湁 fid:seasoning 鏍囩鐗╁搧锛屾垨椋熺墿 NBT 鏍囩 seasoning 闈炵┖
 *  - 鍏朵腑鍓墜涓?sesameoil锛堥娌癸級鎴栭鐗?NBT seasoning 涓?flavor_immersed_daily:sesameoil 鏃讹紝
 *    缁欎簣 45 绉?sesame_slip 鏁堟灉
 * 鏁堟灉锛氭嫢鏈?sesame_slip 鏈熼棿锛屽疄浣撶殑琛岃蛋楂樺害鎻愬崌涓?config 涓殑鏁板€硷紙榛樿 2.1 鏍硷級
 */
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class SesameSlipEffectHandler {

    private static final int DURATION_TICKS = 45 * 20;
    private static final ResourceLocation STEP_HEIGHT_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "sesame_slip_step_height");

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;
        if (!Config.sesameSlipEnabled) return;

        // 鍓墜鏄惁涓鸿皟鍛虫枡
        ItemStack offhand = player.getOffhandItem();
        boolean offhandIsSeasoning = offhand.is(FIDItemTags.SEASONING);

        // 椋熺敤鐨勯鐗?NBT 鏂囨湰鏍囩 seasoning锛?.21.1 瀛樹簬 CUSTOM_DATA 缁勪欢涓級
        CompoundTag tag = event.getItem().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        boolean foodHasSeasoning = tag.contains("seasoning", Tag.TAG_STRING);
        String foodSeasoning = foodHasSeasoning ? tag.getString("seasoning") : "";

        // 瑙﹀彂鏉′欢锛氬壇鎵嬫槸璋冨懗鏂?鎴?椋熺墿 seasoning 鏍囩闈炵┖
        if (!offhandIsSeasoning && !foodHasSeasoning) return;

        // 涓撳睘 buff锛氬壇鎵嬫槸棣欐补锛坰esameoil锛夋垨 椋熺墿 seasoning 鏍囩涓?flavor_immersed_daily:sesameoil 鈫?棣欐补婊戞 45 绉?
        boolean isSesameOil = offhand.is(ModItems.SESAMEOIL.get())
                || "flavor_immersed_daily:sesameoil".equals(foodSeasoning);
        if (isSesameOil) {
            player.addEffect(new MobEffectInstance(ModEffects.SESAME_SLIP, DURATION_TICKS, 0));
            // 鍓墜鎸佹湁璋冨懗鏂欐椂锛岃幏寰?buff 鐨勫悓鏃舵秷鑰椾竴涓?
            if (offhandIsSeasoning) {
                offhand.shrink(1);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        if (entity.level().isClientSide) return;

        AttributeInstance stepHeight = entity.getAttribute(Attributes.STEP_HEIGHT);
        if (stepHeight == null) return;

        boolean active = Config.sesameSlipEnabled && entity.hasEffect(ModEffects.SESAME_SLIP);
        if (active && !stepHeight.hasModifier(STEP_HEIGHT_MODIFIER_ID)) {
            // 鍘熺増榛樿姝ラ珮 0.6锛屼慨姝ｅ€?= 鐩爣楂樺害 - 0.6
            double value = Config.sesameSlipHeight - 0.6;
            stepHeight.addTransientModifier(new AttributeModifier(
                    STEP_HEIGHT_MODIFIER_ID, value, AttributeModifier.Operation.ADD_VALUE));
        } else if (!active && stepHeight.hasModifier(STEP_HEIGHT_MODIFIER_ID)) {
            stepHeight.removeModifier(STEP_HEIGHT_MODIFIER_ID);
        }
    }
}
