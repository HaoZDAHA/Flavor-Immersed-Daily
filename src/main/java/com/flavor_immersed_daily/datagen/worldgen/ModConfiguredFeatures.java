package com.flavor_immersed_daily.datagen.worldgen;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModBlocks;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;

public final class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PLUM_TREE = key("plum_tree");

    private ModConfiguredFeatures() {}

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        context.register(PLUM_TREE, new ConfiguredFeature<>(Feature.TREE, plumTree().build()));
    }

    private static TreeConfiguration.TreeConfigurationBuilder plumTree() {
        BlockState leaves = ModBlocks.VERDANT_GRACE_LEAVES.getDefaultState();
        BlockState fruitingLeaves = ModBlocks.VERDANT_GRACE_FRUITING_LEAVES.getDefaultState();

        return new TreeConfiguration.TreeConfigurationBuilder(
                SimpleStateProvider.simple(ModBlocks.VERDANT_GRACE_LOG.getDefaultState()),
                new StraightTrunkPlacer(4, 2, 0),
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(leaves, 7)
                        .add(fruitingLeaves, 3)),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 2),
                new TwoLayersFeatureSize(1, 0, 1))
                .ignoreVines();
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String path) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, path));
    }
}
