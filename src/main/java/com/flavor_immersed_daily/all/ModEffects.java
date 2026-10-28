package com.flavor_immersed_daily.all;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.effect.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public final class ModEffects {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, FlavorImmersedDaily.MODID);

    public static final RegistryObject<FlatulenceEffect> FLATULENCE = REGISTRY.register("flatulence", FlatulenceEffect::new);
    public static final RegistryObject<SesameSlipEffect> SESAME_SLIP = REGISTRY.register("sesame_slip", SesameSlipEffect::new);
    public static final RegistryObject<AceticErosionEffect> ACETIC_EROSION = REGISTRY.register("acetic_erosion", AceticErosionEffect::new);
    public static final RegistryObject<ButterPitcherEffect> BUTTER_PITCHER = REGISTRY.register("butter_pitcher", ButterPitcherEffect::new);
    public static final RegistryObject<FrozenEffect> FROZEN = REGISTRY.register("frozen", FrozenEffect::new);
    public static final RegistryObject<BeanFuryEffect> BEAN_FURY = REGISTRY.register("bean_fury", BeanFuryEffect::new);
    public static final RegistryObject<FlavorBaseEffect> FLAVOR_BASE = REGISTRY.register("flavor_base", FlavorBaseEffect::new);
    public static final RegistryObject<SolarBrewEffect> SOLAR_BREW = REGISTRY.register("solar_brew", SolarBrewEffect::new);
    public static final RegistryObject<HulkLeekEffect> HULK_LEEK = REGISTRY.register("hulk_leek", HulkLeekEffect::new);
    public static final RegistryObject<FuryAssaultEffect> FURY_ASSAULT = REGISTRY.register("fury_assault", FuryAssaultEffect::new);
    public static final RegistryObject<CrimsonMambaEffect> CRIMSON_MAMBA = REGISTRY.register("crimson_mamba", CrimsonMambaEffect::new);
    public static final RegistryObject<ExposeEvilEffect> EXPOSE_EVIL = REGISTRY.register("expose_evil", ExposeEvilEffect::new);
    public static final RegistryObject<YiDeRoarEffect> YI_DE_ROAR = REGISTRY.register("yi_de_roar", YiDeRoarEffect::new);
    public static final RegistryObject<GuanYuStrikeEffect> GUAN_YU_STRIKE = REGISTRY.register("guan_yu_strike", GuanYuStrikeEffect::new);
    public static final RegistryObject<AniseedWardEffect> ANISEED_WARD = REGISTRY.register("aniseed_ward", AniseedWardEffect::new);
    public static final RegistryObject<ReunionRecoveryEffect> REUNION_RECOVERY = REGISTRY.register("reunion_recovery", ReunionRecoveryEffect::new);
    public static final RegistryObject<CrumblyCrustEffect> CRUMBLY_CRUST = REGISTRY.register("crumbly_crust", CrumblyCrustEffect::new);
    public static final RegistryObject<EclipseDevourEffect> ECLIPSE_DEVOUR = REGISTRY.register("eclipse_devour", EclipseDevourEffect::new);
    public static final RegistryObject<CoolSkinEffect> COOL_SKIN = REGISTRY.register("cool_skin", CoolSkinEffect::new);

    private ModEffects() {}

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

    public static Holder<MobEffect> holder(RegistryObject<? extends MobEffect> effect) {
        return (Holder<MobEffect>) effect.getHolder().orElseThrow();
    }
}
