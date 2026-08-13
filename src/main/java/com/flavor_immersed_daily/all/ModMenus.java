package com.flavor_immersed_daily.all;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.screen.EggBreakingMachineMenu;
import com.flavor_immersed_daily.screen.FridgeMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> REGISTRY =
            DeferredRegister.create(Registries.MENU, FlavorImmersedDaily.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<FridgeMenu>> FRIDGE_MENU =
            REGISTRY.register("fridge_menu", () -> new MenuType<>(FridgeMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<EggBreakingMachineMenu>> EGG_BREAKING_MACHINE_MENU =
            REGISTRY.register("egg_breaking_machine_menu", () -> new MenuType<>(EggBreakingMachineMenu::new, FeatureFlags.DEFAULT_FLAGS));

    private ModMenus() {
    }

    public static void register(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }
}
