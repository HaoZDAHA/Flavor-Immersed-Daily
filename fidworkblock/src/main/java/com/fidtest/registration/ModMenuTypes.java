package com.fidtest.registration;

import com.fidtest.FidworkMod;
import com.fidtest.screen.CookingMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, FidworkMod.MODID);

    public static final RegistryObject<MenuType<CookingMenu>> COOKING =
            MENU_TYPES.register("cooking", () -> IForgeMenuType.create(CookingMenu::new));
}
