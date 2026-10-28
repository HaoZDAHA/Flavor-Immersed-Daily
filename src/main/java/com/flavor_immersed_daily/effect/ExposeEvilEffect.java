package com.flavor_immersed_daily.effect;

import com.flavor_immersed_daily.config.Config;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

// 邪祟暴露（expose_evil）— 拥有期间：
// 白天露天场景下会被太阳灼烧（逻辑见本类 applyEffectTick）
// 亡灵杀手附魔对拥有者依然生效（视为亡灵）
// 每次近战攻击有概率将村民转化为僵尸村民
// 被手持血液攻击后，效果消失并受到灼烧伤害
// （后三条逻辑见 ExposeEvilEffectHandler）
public class ExposeEvilEffect extends MobEffect {

    public ExposeEvilEffect() {
        super(MobEffectCategory.HARMFUL, 0x4A0E4E);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!Config.exposeEvilEnabled) return;
        Level level = entity.level();
// 白天 + 头顶露天 + 未被雨水淋湿/浸水时，持续灼烧（类似僵尸白天晒伤）
        if (!level.isClientSide && level.isDay() && level.canSeeSky(entity.blockPosition())
                && !entity.isInWaterRainOrBubble()) {
            entity.setSecondsOnFire(8);
        }
    }
}
