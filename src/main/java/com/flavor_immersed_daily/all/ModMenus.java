package com.flavor_immersed_daily.all;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.screen.CropConversionMenu;
import com.flavor_immersed_daily.screen.EggBreakingMachineMenu;
import com.flavor_immersed_daily.screen.FridgeMenu;
import com.flavor_immersed_daily.screen.MooncakeMoldMenu;
import com.flavor_immersed_daily.screen.RaddlesOwnerMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> REGISTRY =
            DeferredRegister.create(Registries.MENU, FlavorImmersedDaily.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<FridgeMenu>> FRIDGE_MENU =
            REGISTRY.register("fridge_menu", () -> new MenuType<>(FridgeMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<EggBreakingMachineMenu>> EGG_BREAKING_MACHINE_MENU =
            REGISTRY.register("egg_breaking_machine_menu", () -> new MenuType<>(EggBreakingMachineMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<CropConversionMenu>> CROP_CONVERSION_MENU =
            REGISTRY.register("crop_conversion_menu", () -> new MenuType<>(CropConversionMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<MooncakeMoldMenu>> MOONCAKE_MOLD_MENU =
            REGISTRY.register("mooncake_mold_menu", () -> new MenuType<>(MooncakeMoldMenu::new, FeatureFlags.DEFAULT_FLAGS));
// 灯谜牌编辑界面：需要把方块坐标随界面一起发给客户端
    public static final DeferredHolder<MenuType<?>, MenuType<RaddlesOwnerMenu>> RADDLES_OWNER_MENU =
            REGISTRY.register("raddles_owner_menu", () -> IMenuTypeExtension.create(RaddlesOwnerMenu::new));

    private ModMenus() {
    }

    public static void register(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }
}
