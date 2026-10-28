package com.flavor_immersed_daily.datagen;

import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.block.block.fruit.FallingFruitBlock;
import com.flavor_immersed_daily.block.common.block.FIDCropBlock;
import com.flavor_immersed_daily.block.common.block.FIDLogMushroomBlock;
import com.flavor_immersed_daily.block.common.block.FIDWaterCropBlock;
import com.flavor_immersed_daily.block.block.food.ColaBlock;
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
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;

import java.util.Set;
import java.util.stream.Collectors;

// Generates loot for DeferredRegister blocks only. Registrate BlockEntry loot stays on its own chain.
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
            } else if (block instanceof FIDCropBlock crop) {
                if (block == ModBlocks.GARLICSEED_CROP.get()) {
                    add(block, garlicCropDrops(crop));
                } else if (crop.getCropItem().asItem().equals(crop.getSeedItem().asItem())) {
// 无独立果实（果实即种子）的作物：参照原版胡萝卜/马铃薯的 loot 结构，
// 单池掉落并保证数量 > 1。不能用“果实 + 种子”多池结构——
// FTB Ultimine 的 VanillaCropLikeHandler 会把每个池产出的 ItemStack 各扣 1 个
// 当作“补种”，多池会导致连锁收割时种子被全部吞掉、零掉落。
                    add(block, seedOnlyCropDrops(crop, crop.getSeedItem(),
                            crop.getAgeProperty(), crop.getMaxAge()));
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
            } else if (block instanceof ColaBlock) {
// 可乐罐堆：方块本身不掉落，只通过右键升级/降级交互获得可乐物品
                add(block, LootTable.lootTable());
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
        Holder<Enchantment> fortune = registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
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

// 只有种子、无独立果实的作物（如咖啡豆、八角等香料作物）掉落。
// 参照原版胡萝卜/马铃薯：单池 + 固定基数 + 时运加成，保证连锁收割（FTB Ultimine）
// 时即便被扣掉 1 个当作“补种”，仍有种子掉落；不能用多池结构，否则 FTB 会把
// 每个池产出的 ItemStack 各扣 1 个，成熟时全部被吞、零掉落。
    private LootTable.Builder seedOnlyCropDrops(Block block, ItemLike seed,
                                                IntegerProperty ageProperty, int maxAge) {
        Holder<Enchantment> fortune = registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
        LootItemCondition.Builder mature = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ageProperty, maxAge));
        return applyExplosionDecay(block, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(seed))
                        .when(mature)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0f)))
                        .apply(ApplyBonusCount.addUniformBonusCount(fortune, 2))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.REGISTRY.getEntries().stream().map(entry -> entry.get()).collect(Collectors.toList());
    }
}
