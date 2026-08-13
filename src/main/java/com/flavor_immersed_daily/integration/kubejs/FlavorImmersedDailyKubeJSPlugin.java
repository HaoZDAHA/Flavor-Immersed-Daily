package com.flavor_immersed_daily.integration.kubejs;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import net.minecraft.resources.ResourceLocation;

/** Registers KubeJS recipe schemas when KubeJS discovers this optional plugin. */
public class FlavorImmersedDailyKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry event) {
        event.register(id("fridge_tempering"), FlavorImmersedDailyKubeJSRecipes.FRIDGE_TEMPERING);
        event.register(id("fridge_freezing"), FlavorImmersedDailyKubeJSRecipes.FRIDGE_FREEZING);
        event.register(id("wood_basin"), FlavorImmersedDailyKubeJSRecipes.WOOD_BASIN);
        event.register(id("egg_breaking"), FlavorImmersedDailyKubeJSRecipes.EGG_BREAKING);
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, path);
    }
}
