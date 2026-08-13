package com.flavor_immersed_daily.mixin;

import com.flavor_immersed_daily.all.ModEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 冻结效果（frozen）的移动禁止实现。
 * isImmobile 为 true 时原版会清零移动输入并禁止跳跃/转向，
 * 重力与击退仍然生效（可以被打飞，符合"只能被攻击"的设计）。
 * 效果由服务端同步到客户端，两端判断结果一致。
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "isImmobile", at = @At("HEAD"), cancellable = true)
    private void fid$freezeMovement(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.hasEffect(ModEffects.FROZEN)) {
            cir.setReturnValue(true);
        }
    }
}
