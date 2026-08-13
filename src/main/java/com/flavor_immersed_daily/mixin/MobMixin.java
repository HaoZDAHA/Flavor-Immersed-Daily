package com.flavor_immersed_daily.mixin;

import com.flavor_immersed_daily.all.ModEffects;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 冻结效果（frozen）的 AI 暂停实现。
 * 拥有 frozen 效果期间跳过 serverAiStep：目标选择、寻路、攻击等全部停摆；
 * 效果消失后行为自然恢复，不写入任何持久化状态（对比旧的 NoAI 方案，
 * 不会在区块卸载/重启后把 NoAi=true 留在实体存档里造成永久瘫痪）。
 */
@Mixin(Mob.class)
public abstract class MobMixin {

    @Inject(method = "serverAiStep", at = @At("HEAD"), cancellable = true)
    private void fid$freezeAi(CallbackInfo ci) {
        Mob self = (Mob) (Object) this;
        if (self.hasEffect(ModEffects.FROZEN)) {
            self.getNavigation().stop();
            ci.cancel();
        }
    }
}
