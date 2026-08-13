package com.flavor_immersed_daily.all;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.effect.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEffects {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, FlavorImmersedDaily.MODID);

    public static final DeferredHolder<MobEffect, FlatulenceEffect> FLATULENCE = REGISTRY.register("flatulence", FlatulenceEffect::new);
    public static final DeferredHolder<MobEffect, SesameSlipEffect> SESAME_SLIP = REGISTRY.register("sesame_slip", SesameSlipEffect::new);
    public static final DeferredHolder<MobEffect, AceticErosionEffect> ACETIC_EROSION = REGISTRY.register("acetic_erosion", AceticErosionEffect::new);
    public static final DeferredHolder<MobEffect, ButterPitcherEffect> BUTTER_PITCHER = REGISTRY.register("butter_pitcher", ButterPitcherEffect::new);
    public static final DeferredHolder<MobEffect, FrozenEffect> FROZEN = REGISTRY.register("frozen", FrozenEffect::new);
    public static final DeferredHolder<MobEffect, BeanFuryEffect> BEAN_FURY = REGISTRY.register("bean_fury", BeanFuryEffect::new);
    public static final DeferredHolder<MobEffect, FlavorBaseEffect> FLAVOR_BASE = REGISTRY.register("flavor_base", FlavorBaseEffect::new);
    public static final DeferredHolder<MobEffect, SolarBrewEffect> SOLAR_BREW = REGISTRY.register("solar_brew", SolarBrewEffect::new);
    public static final DeferredHolder<MobEffect, HulkLeekEffect> HULK_LEEK = REGISTRY.register("hulk_leek", HulkLeekEffect::new);
    public static final DeferredHolder<MobEffect, FuryAssaultEffect> FURY_ASSAULT = REGISTRY.register("fury_assault", FuryAssaultEffect::new);
    public static final DeferredHolder<MobEffect, CrimsonMambaEffect> CRIMSON_MAMBA = REGISTRY.register("crimson_mamba", CrimsonMambaEffect::new);

    private ModEffects() {}

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

    public static Holder<MobEffect> holder(DeferredHolder<MobEffect, ? extends MobEffect> effect) {
        return effect;
    }
}
