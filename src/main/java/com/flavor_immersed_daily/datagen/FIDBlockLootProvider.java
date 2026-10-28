package com.flavor_immersed_daily.datagen;

import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.block.block.fruit.FallingFruitBlock;
import com.flavor_immersed_daily.block.common.block.FIDCropBlock;
import com.flavor_immersed_daily.block.common.block.FIDLogMushroomBlock;
import com.flavor_immersed_daily.block.common.block.FIDWaterCropBlock;
import com.flavor_immersed_daily.block.block.fruit.FruitingLeavesBlock;
import com.flavor_immersed_daily.block.block.fruit.GrapeBlock;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;

import java.util.Set;
import java.util.stream.Collectors;

// Generates loot for DeferredRegister blocks only. Registrate BlockEntry loot stays on its own chain.
public final class FIDBlockLootProvider extends BlockLootSubProvider {
    public FIDBlockLootProvider() {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS);
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
            } else if (block instanceof FIDCropBlock crop) {
                if (block == ModBlocks.GARLICSEED_CROP.get()) {
                    add(block, garlicCropDrops(crop));
                } else {
                    add(block, fortuneCropDrops(block, crop.getCropItem(), crop.getSeedItem(),
                            crop.getAgeProperty(), crop.getMaxAge()));
                }
            } else if (block instanceof FIDLogMushroomBlock mushroom) {
                add(block, fortuneCropDrops(block, mushroom.getCropItem(), mushroom.getSeedItem(),
                        FIDLogMushroomBlock.AGE, mushroom.getMaxAge()));
            } else if (block instanceof FIDWaterCropBlock crop) {
                add(block, fortuneCropDrops(block, crop.getCropItem(), crop.getSeedItem(),
                        crop.getAgeProperty(), crop.getMaxAge()));
            } else {
                dropSelf(block);
            }
        }
    }

// 大蒜作物：成熟时额外掉落 1 个蒜薹（garlicpedicel）作为副产物。
    private LootTable.Builder garlicCropDrops(FIDCropBlock crop) {
        LootItemCondition.Builder mature = LootItemBlockStatePropertyCondition.hasBlockStateProperties(crop)
                .setProperties(StatePropertiesPredicate.Builder.properties()
                        .hasProperty(crop.getAgeProperty(), crop.getMaxAge()));
        return fortuneCropDrops(crop, crop.getCropItem(), crop.getSeedItem(),
                crop.getAgeProperty(), crop.getMaxAge())
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(ModItems.GARLICPEDICEL.get()))
                        .when(mature));
    }

// 作物掉落：成熟时掉作物+种子，未成熟只掉种子。
// 成熟掉落受时运（fortune）加成，使用附魔时运的锄头破坏时产物数量明显增加。
    private LootTable.Builder fortuneCropDrops(Block block, ItemLike crop, ItemLike seed,
                                               IntegerProperty ageProperty, int maxAge) {
        Enchantment fortune = Enchantments.BLOCK_FORTUNE;
        LootItemCondition.Builder mature = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ageProperty, maxAge));
        return applyExplosionDecay(block, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(seed))
                        .when(mature)
                        .apply(ApplyBonusCount.addUniformBonusCount(fortune, 2)))
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(crop))
                        .when(mature)
                        .apply(ApplyBonusCount.addUniformBonusCount(fortune, 2)))
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(seed))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.REGISTRY.getEntries().stream().map(entry -> entry.get()).collect(Collectors.toList());
    }
}
