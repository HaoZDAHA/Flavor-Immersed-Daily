package com.flavor_immersed_daily.mixin;

import com.flavor_immersed_daily.entity.Freezable;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// 生物冻结状态的 AI 暂停实现。
// 冻结期间跳过 serverAiStep：目标选择、寻路、攻击等全部停摆；
// 冻结结束后行为自然恢复，不写入任何持久化状态（对比旧的 NoAI 方案，
// 不会在区块卸载/重启后把 NoAi=true 留在实体存档里造成永久瘫痪）。
@Mixin(Mob.class)
public abstract class MobMixin {

    @Inject(method = "serverAiStep", at = @At("HEAD"), cancellable = true)
    private void fid$freezeAi(CallbackInfo ci) {
        Mob self = (Mob) (Object) this;
        if (self instanceof Freezable freezable && freezable.fid$isFrozen()) {
            self.getNavigation().stop();
            ci.cancel();
        }
    }
}
