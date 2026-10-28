package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEffects;
import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.datagen.tag.FIDItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// 邪祟暴露（expose_evil）效果处理器
// 触发规则（拥有该效果的实体）：
// 受到攻击时，攻击者主手亡灵杀手附魔对其生效（视为亡灵）
// 被手持 fid:blood 标签物品攻击时，效果消失并受到灼烧伤害
// 近战攻击村民时，有概率将其转化为僵尸村民
// （白天露天燃烧逻辑见 ExposeEvilEffect.applyEffectTick）
@Mod.EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class ExposeEvilEffectHandler {

// 原版亡灵杀手对亡灵生物的每级额外伤害
    private static final float SMITE_DAMAGE_PER_LEVEL = 2.5F;

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {
        if (!Config.exposeEvilEnabled) return;
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide) return;
        if (!victim.hasEffect(ModEffects.EXPOSE_EVIL.get())) return;

// 亡灵杀手附魔对拥有者同样生效（把拥有者当作亡灵生物处理）
        DamageSource source = event.getSource();
        if (source.getDirectEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();
            int smiteLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SMITE, weapon);
            if (smiteLevel > 0) {
                event.setAmount(event.getAmount() + SMITE_DAMAGE_PER_LEVEL * smiteLevel);
            }
        }

// 被手持血液攻击：效果消失并受到灼烧伤害
        if (source.getDirectEntity() instanceof LivingEntity attacker
                && attacker.getMainHandItem().is(FIDItemTags.BLOOD)) {
            victim.removeEffect(ModEffects.EXPOSE_EVIL.get());
            victim.hurt(victim.level().damageSources().onFire(), (float) Config.exposeEvilBloodFireDamage);
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (!Config.exposeEvilEnabled) return;
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide) return;
        DamageSource source = event.getSource();

// 拥有者近战攻击村民：有概率转化为僵尸村民
        if (source.getDirectEntity() == source.getEntity()
                && source.getEntity() instanceof Player player
                && player.hasEffect(ModEffects.EXPOSE_EVIL.get())
                && victim instanceof Villager villager
                && villager.isAlive()
                && player.level().random.nextDouble() < Config.exposeEvilZombieChance) {
            ZombieVillager zombieVillager = EntityType.ZOMBIE_VILLAGER.create(villager.level());
            if (zombieVillager != null) {
                zombieVillager.moveTo(villager.getX(), villager.getY(), villager.getZ(),
                        villager.getYRot(), villager.getXRot());
                zombieVillager.setVillagerData(villager.getVillagerData());
                zombieVillager.setBaby(villager.isBaby());
                zombieVillager.setCustomName(villager.getCustomName());
                zombieVillager.setCanPickUpLoot(true);
                villager.level().addFreshEntity(zombieVillager);
                villager.discard();
            }
        }
    }
}