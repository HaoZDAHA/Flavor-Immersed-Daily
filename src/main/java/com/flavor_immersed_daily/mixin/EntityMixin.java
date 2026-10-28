package com.flavor_immersed_daily.mixin;

import com.flavor_immersed_daily.all.ModEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// 咆哮效果（yi_de_roar）：攻击者拥有咆哮效果时，清零目标无敌帧（invulnerableTime），
// 使攻击每次都造成伤害。
// 实现要点（区别于此前导致启动失败的接口模式）：
// <ul>
// <li>不使用 @Accessor 接口对外暴露——<b>没有接口注入</b>，避免接口混入 Entity 引发启动失败</li>
// <li>逻辑全部在混入内部完成，<b>不对外暴露任何访问器/cast 目标</b></li>
// <li>invulnerableTime 是 Entity 基类<b>自身声明的 private 字段</b>，可安全 @Shadow</li>
// <li>注入 Entity.isInvulnerableTo()——LivingEntity.hurt() 第一步必然调用它，
// 在此清零 invulnerableTime 会先于后续 invulnerableTime > 0 检查生效</li>
// </ul>
@Mixin(Entity.class)
public abstract class EntityMixin {

// Entity 基类自身声明的私有字段（同一目标类内可安全 @Shadow）
    @Shadow
    private int invulnerableTime;

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"))
    private void fid$roarBypassInvulnerable(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (source != null && source.getEntity() instanceof Player player
                && player.hasEffect(ModEffects.YI_DE_ROAR)) {
            this.invulnerableTime = 0;
        }
    }
}
