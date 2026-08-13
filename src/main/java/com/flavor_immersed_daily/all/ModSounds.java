package com.flavor_immersed_daily.all;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY =
            DeferredRegister.create(net.minecraft.core.registries.Registries.SOUND_EVENT, FlavorImmersedDaily.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> MANBAOUT = REGISTRY.register("manbaout",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "manbaout")));

    private ModSounds() {}

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
