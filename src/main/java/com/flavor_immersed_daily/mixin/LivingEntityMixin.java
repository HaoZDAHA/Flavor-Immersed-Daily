package com.flavor_immersed_daily.mixin;

import com.flavor_immersed_daily.entity.Freezable;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// 生物冻结状态的存储与移动禁止。
// isImmobile 为 true 时原版会清零移动输入并禁止跳跃/转向，
// 重力与击退仍然生效（可以被打飞，符合"只能被攻击"的设计）。
// 冻结计时只在服务端推进（端上不同步），AI 本身也只跑在服务端。
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements Freezable {

    @Unique
    private int fid$frozenTicks;

    @Override
    public void fid$freeze(int ticks) {
        this.fid$frozenTicks = Math.max(this.fid$frozenTicks, ticks);
    }

    @Override
    public boolean fid$isFrozen() {
        return this.fid$frozenTicks > 0;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void fid$tickFreeze(CallbackInfo ci) {
        if (this.fid$frozenTicks > 0) {
            this.fid$frozenTicks--;
        }
    }

    @Inject(method = "isImmobile", at = @At("HEAD"), cancellable = true)
    private void fid$freezeMovement(CallbackInfoReturnable<Boolean> cir) {
        if (this.fid$frozenTicks > 0) {
            cir.setReturnValue(true);
        }
    }
}
