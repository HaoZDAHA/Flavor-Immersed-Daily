package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
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

    private static final ResourceLocation STEP_HEIGHT_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "sesame_slip_step_height");

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
