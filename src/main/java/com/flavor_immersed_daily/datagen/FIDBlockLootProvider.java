package com.flavor_immersed_daily.datagen;

import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.block.block.fruit.FallingFruitBlock;
import com.flavor_immersed_daily.block.common.block.FIDCropBlock;
import com.flavor_immersed_daily.block.common.block.FIDLogMushroomBlock;
import com.flavor_immersed_daily.block.block.fruit.FruitingLeavesBlock;
import com.flavor_immersed_daily.block.block.fruit.GrapeBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;

import java.util.Set;
import java.util.stream.Collectors;

/** Generates loot for DeferredRegister blocks only. Registrate BlockEntry loot stays on its own chain. */
public final class FIDBlockLootProvider extends BlockLootSubProvider {
    public FIDBlockLootProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected void generate() {
        for (Block block : getKnownBlocks()) {
            if (block instanceof FallingFruitBlock fruit) {
                add(block, createSingleItemTable(fruit.getFruitItem()));
            } else if (block instanceof FruitingLeavesBlock || block instanceof LeavesBlock) {
                add(block, createSilkTouchOrShearsDispatchTable(block, EmptyLootItem.emptyItem()));
            } else if (block instanceof GrapeBlock) {
                add(block, createSingleItemTable(ModBlocks.TRELLIS.get()));
            } else if (block == ModBlocks.ANISEED_0_CROP.get() && block instanceof FIDCropBlock crop) {
                add(block, createCropDrops(block, crop.getCropItem().asItem(), crop.getSeedItem().asItem(),
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(crop.getAgeProperty(), crop.getMaxAge()))));
            } else if (block instanceof FIDLogMushroomBlock mushroom) {
                add(block, createCropDrops(block, mushroom.getCropItem().asItem(), mushroom.getSeedItem().asItem(),
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(FIDLogMushroomBlock.AGE, mushroom.getMaxAge()))));
            } else if (block instanceof FIDCropBlock crop) {
                add(block, createSingleItemTable(crop.getSeedItem()));
            } else {
                dropSelf(block);
            }
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.REGISTRY.getEntries().stream().map(entry -> entry.get()).collect(Collectors.toList());
    }
}
