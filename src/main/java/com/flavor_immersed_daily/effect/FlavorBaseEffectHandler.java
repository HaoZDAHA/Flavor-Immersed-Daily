package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.datagen.tag.FIDItemTags;

import com.flavor_immersed_daily.all.ModEffects;

import com.flavor_immersed_daily.all.ModItems;

import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
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
 * 鐧惧懗涔嬪熀锛坒lavor_base锛夋晥鏋滃鐞嗗櫒
 * 瑙﹀彂瑙勫垯锛?
 *  - 鐜╁椋熺敤鐗╁搧鍚庯紝鍓墜鎸佹湁 fid:seasoning 鏍囩鐗╁搧锛屾垨椋熺墿 NBT 鏍囩 seasoning 闈炵┖
 *  - 鍏朵腑鍓墜涓?salt锛堢洂锛夋垨椋熺墿 NBT seasoning 涓?flavor_immersed_daily:salt 鏃讹紝
 *    缁欎簣 45 绉?flavor_base 鏁堟灉锛屽苟娑堣€楀壇鎵嬭皟鍛虫枡涓€涓?
 * 鏁堟灉锛氭嫢鏈?flavor_base 鏈熼棿锛岀帺瀹惰韩涓婃瘡鍚屾椂鏈?1 绉嶆敞鍐屽悕浠?"flavor" 寮€澶寸殑
 * 鏈ā缁勬垨闄勫睘妯＄粍 buff锛屽垯锛?
 *  - 鏀诲嚮浼ゅ +config锛堥粯璁?+1锛?
 *  - 绉诲姩閫熷害 +config锛堥粯璁?+0.1锛?
 * 鏈€澶氬彔鍔?config 娆★紙榛樿 10 娆★級
 */
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class FlavorBaseEffectHandler {

    private static final int DURATION_TICKS = 45 * 20;
    private static final ResourceLocation ATTACK_DAMAGE_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "flavor_base_attack_damage");
    private static final ResourceLocation MOVEMENT_SPEED_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "flavor_base_movement_speed");

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;
        if (!Config.flavorBaseEnabled) return;

        // 鍓墜鏄惁涓鸿皟鍛虫枡
        ItemStack offhand = player.getOffhandItem();
        boolean offhandIsSeasoning = offhand.is(FIDItemTags.SEASONING);

        // 椋熺敤鐨勯鐗?NBT 鏂囨湰鏍囩 seasoning锛?.21.1 瀛樹簬 CUSTOM_DATA 缁勪欢涓級
        CompoundTag tag = event.getItem().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        boolean foodHasSeasoning = tag.contains("seasoning", Tag.TAG_STRING);
        String foodSeasoning = foodHasSeasoning ? tag.getString("seasoning") : "";

        // 瑙﹀彂鏉′欢锛氬壇鎵嬫槸璋冨懗鏂?鎴?椋熺墿 seasoning 鏍囩闈炵┖
        if (!offhandIsSeasoning && !foodHasSeasoning) return;

        // 涓撳睘 buff锛氬壇鎵嬫槸鐩愶紙salt锛夋垨 椋熺墿 seasoning 鏍囩涓?flavor_immersed_daily:salt 鈫?鐧惧懗涔嬪熀 45 绉?
        boolean isSalt = offhand.is(ModItems.SALT.get())
                || "flavor_immersed_daily:salt".equals(foodSeasoning);
        if (isSalt) {
            player.addEffect(new MobEffectInstance(ModEffects.FLAVOR_BASE, DURATION_TICKS, 0));
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

        boolean active = Config.flavorBaseEnabled && entity.hasEffect(ModEffects.FLAVOR_BASE);
        if (!active) {
            removeModifiers(entity);
            return;
        }

        // 缁熻韬笂娉ㄥ唽鍚嶄互 "flavor" 寮€澶寸殑 buff 鏁伴噺锛堟湰妯＄粍鍙婇檮灞炴ā缁勶級锛屾渶澶氬彔鍔?config 涓婇檺
        int count = 0;
        for (MobEffectInstance instance : entity.getActiveEffects()) {
            ResourceLocation key = instance.getEffect().unwrapKey().map(rk -> rk.location()).orElse(null);
            if (key != null && key.toString().startsWith("flavor")) {
                count++;
            }
        }
        int stacks = Math.min(count, Config.flavorBaseMaxStacks);

        if (stacks > 0) {
            updateModifier(entity, ATTACK_DAMAGE_MODIFIER_ID, Attributes.ATTACK_DAMAGE,
                    stacks * Config.flavorBaseDamageBonus);
            updateModifier(entity, MOVEMENT_SPEED_MODIFIER_ID, Attributes.MOVEMENT_SPEED,
                    stacks * Config.flavorBaseSpeedBonus);
        } else {
            removeModifiers(entity);
        }
    }

    private static void updateModifier(LivingEntity entity, ResourceLocation id, Holder<Attribute> attribute, double value) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance == null) return;
        AttributeModifier modifier = instance.getModifier(id);
        if (modifier == null) {
            instance.addTransientModifier(new AttributeModifier(id, value, AttributeModifier.Operation.ADD_VALUE));
        } else if (modifier.amount() != value) {
            instance.removeModifier(id);
            instance.addTransientModifier(new AttributeModifier(id, value, AttributeModifier.Operation.ADD_VALUE));
        }
    }

    private static void removeModifiers(LivingEntity entity) {
        AttributeInstance attackDamage = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamage != null && attackDamage.hasModifier(ATTACK_DAMAGE_MODIFIER_ID)) {
            attackDamage.removeModifier(ATTACK_DAMAGE_MODIFIER_ID);
        }
        AttributeInstance movementSpeed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed != null && movementSpeed.hasModifier(MOVEMENT_SPEED_MODIFIER_ID)) {
            movementSpeed.removeModifier(MOVEMENT_SPEED_MODIFIER_ID);
        }
    }
}
