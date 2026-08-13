package com.flavor_immersed_daily.datagen;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import com.flavor_immersed_daily.datagen.tag.FIDItemTagsProvider;
import com.flavor_immersed_daily.datagen.recipe.WoodBasinRecipe;
import com.flavor_immersed_daily.datagen.worldgen.ModConfiguredFeatures;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.List;
import java.util.Set;

/** Server-side block data generation. Model generation remains opt-in through Registrate registrations. */
public final class FIDDataGenerators {
    private static final RegistrySetBuilder WORLDGEN = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);

    private FIDDataGenerators() {
    }

    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new LootTableProvider(output, Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(FIDBlockLootProvider::new, LootContextParamSets.BLOCK)),
                lookupProvider));
        var blockTags = new FIDBlockTagsProvider(output, lookupProvider, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new FIDItemTagsProvider(output, lookupProvider,
                blockTags.contentsGetter(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new WoodBasinRecipe(output, lookupProvider));
        generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, lookupProvider,
                WORLDGEN, Set.of(FlavorImmersedDaily.MODID)));
    }
}
