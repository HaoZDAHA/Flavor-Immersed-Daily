package com.flavor_immersed_daily.all;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY =
            DeferredRegister.create(net.minecraft.core.registries.Registries.SOUND_EVENT, FlavorImmersedDaily.MODID);

    public static final RegistryObject<SoundEvent> MANBAOUT = REGISTRY.register("manbaout",
            () -> SoundEvent.createVariableRangeEvent(
                    new ResourceLocation(FlavorImmersedDaily.MODID, "manbaout")));

    public static final RegistryObject<SoundEvent> GIAO = REGISTRY.register("giao",
            () -> SoundEvent.createVariableRangeEvent(
                    new ResourceLocation(FlavorImmersedDaily.MODID, "giao")));

    private ModSounds() {}

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
