package com.fidtest.registration;

import com.fidtest.ExampleMod;
import com.fidtest.screen.CookingMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, ExampleMod.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<CookingMenu>> COOKING =
            MENU_TYPES.register("cooking", () -> IMenuTypeExtension.create(CookingMenu::fromNetwork));
}
