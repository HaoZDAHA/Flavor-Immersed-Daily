package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundSource;
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
 * 赤色曼巴肘击（crimson_mamba）效果处理器
 * 触发规则：
 *  - 玩家食用物品后，副手持有 fid:seasoning 标签物品，或食物 NBT 标签 seasoning 非空
 *  - 其中副手为 brownsugarsyrup（赤糖浆）或食物 NBT seasoning 为 flavor_immersed_daily:brownsugarsyrup 时，
 *    给予 45 秒 crimson_mamba 效果，并消耗副手调味料一个
 *  - 饮用 icedblacktea（冰红茶）直接获得 60 秒 crimson_mamba 效果
 * 效果：拥有 crimson_mamba 的玩家每进行一次近战攻击：
 *  - 主目标受到强化击退（击退效果提升）
 *  - 攻击点周围 4 格内的其他生物受到群体击退
 *  - 播放特殊音效 manbaout
 */
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class CrimsonMambaEffectHandler {

    /** 副手调味料触发的 buff 时长（45 秒，与其他调味料一致） */
    private static final int DURATION_TICKS = 45 * 20;
    /** 冰红茶触发的 buff 时长（60 秒） */
    private static final int ICED_BLACK_TEA_DURATION_TICKS = 60 * 20;
    /** 主目标强化击退强度（原版玩家攻击基击退约为 0.4） */
    private static final double MAIN_KNOCKBACK = 3.0;
    /** 群体击退强度 */
    private static final double AOE_KNOCKBACK = 2.0;
    /** 群体击退扫描半径（格，以被攻击生物为中心） */
    private static final double AOE_RADIUS = 3.0;
    /** 特殊音效音量 */
    private static final float SOUND_VOLUME = 2.0F;

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        // 饮用冰红茶直接获得 60 秒（独立于调味料触发，放在最前保证优先）
        if (event.getItem().is(FlavorImmersedDaily.ICEDBLACKTEA.get())) {
            player.addEffect(new MobEffectInstance(FlavorImmersedDaily.CRIMSON_MAMBA, ICED_BLACK_TEA_DURATION_TICKS, 0));
            return;
        }

        // 副手是否为调味料
        ItemStack offhand = player.getOffhandItem();
        boolean offhandIsSeasoning = offhand.is(FlavorImmersedDaily.SEASONING_TAG);

        // 食用的食物 NBT 文本标签 seasoning（1.21.1 存于 CUSTOM_DATA 组件中）
        CompoundTag tag = event.getItem().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        boolean foodHasSeasoning = tag.contains("seasoning", Tag.TAG_STRING);
        String foodSeasoning = foodHasSeasoning ? tag.getString("seasoning") : "";

        // 触发条件：副手是调味料 或 食物 seasoning 标签非空
        if (!offhandIsSeasoning && !foodHasSeasoning) return;

        // 专属 buff：副手是赤糖浆（brownsugarsyrup）或 食物 seasoning 标签为 flavor_immersed_daily:brownsugarsyrup → 赤色曼巴肘击 45 秒
        boolean isBrownSugarSyrup = offhand.is(FlavorImmersedDaily.BROWNSUGARSYRUP.get())
                || "flavor_immersed_daily:brownsugarsyrup".equals(foodSeasoning);
        if (isBrownSugarSyrup) {
            player.addEffect(new MobEffectInstance(FlavorImmersedDaily.CRIMSON_MAMBA, DURATION_TICKS, 0));
            // 副手持有调味料时，获得 buff 的同时消耗一个
            if (offhandIsSeasoning) {
                offhand.shrink(1);
            }
        }
    }

    @SubscribeEvent
    public static void onDamagePost(LivingDamageEvent.Post event) {
        if (event.getEntity().level().isClientSide) return;

        DamageSource source = event.getSource();
        // 仅近战：直接攻击者与伤害来源相同（排除箭矢等投射物）
        if (source.getDirectEntity() != source.getEntity()) return;
        if (!(source.getEntity() instanceof Player player)) return;
        if (!player.hasEffect(FlavorImmersedDaily.CRIMSON_MAMBA)) return;

        Level level = player.level();

        // 1. 播放特殊音效（音量加大）
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                FlavorImmersedDaily.MANBAOUT.get(), SoundSource.PLAYERS, SOUND_VOLUME, 1.0F);

        // 2. 主目标强化击退（knockback 内部会对方向取反，需传"从目标指向玩家"的方向 → 实际击退为远离玩家）
        LivingEntity victim = event.getEntity();
        Vec3 knockDir = player.position().subtract(victim.position()).normalize();
        victim.knockback(MAIN_KNOCKBACK, knockDir.x, knockDir.z);

        // 3. 群体击退：被攻击生物周围小范围内的生物，统一朝远离玩家的方向被一同击退
        AABB area = victim.getBoundingBox().inflate(AOE_RADIUS);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area, e ->
                e.isAlive() && e != player && e != victim);
        for (LivingEntity target : targets) {
            target.knockback(AOE_KNOCKBACK, knockDir.x, knockDir.z);
        }
    }
}
