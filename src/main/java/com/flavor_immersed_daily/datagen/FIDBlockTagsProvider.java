package com.flavor_immersed_daily.datagen;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.block.block.fruit.FallingFruitBlock;
import com.flavor_immersed_daily.block.common.block.FIDCropBlock;
import com.flavor_immersed_daily.block.common.block.FIDLogMushroomBlock;
import com.flavor_immersed_daily.block.common.block.FIDWaterCropBlock;
import com.flavor_immersed_daily.block.block.fruit.FruitingLeavesBlock;
import com.flavor_immersed_daily.block.block.fruit.GrapeBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

/** Adds semantic block tags only; blockstate and model generation remains explicit in ModBlocks. */
public final class FIDBlockTagsProvider extends BlockTagsProvider {
    public FIDBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, FlavorImmersedDaily.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        ModBlocks.REGISTRY.getEntries().forEach(entry -> addSemanticTags(entry.get()));
        addSemanticTags(ModBlocks.BANANAWOOD.get());
        addSemanticTags(ModBlocks.CINNAMONWOOD.get());
        addSemanticTags(ModBlocks.SOLARWOOD_LOG.get());
        addSemanticTags(ModBlocks.ORCHARD_HEARTWOOD_LOG.get());
    }

    private void addSemanticTags(Block block) {
        if (block instanceof LeavesBlock || block instanceof FruitingLeavesBlock) {
            tag(BlockTags.LEAVES).add(block);
            tag(BlockTags.MINEABLE_WITH_HOE).add(block);
        } else if (block instanceof FIDCropBlock || block instanceof FIDWaterCropBlock
                || block instanceof FIDLogMushroomBlock || block instanceof GrapeBlock
                || block instanceof FallingFruitBlock) {
            tag(BlockTags.MINEABLE_WITH_HOE).add(block);
        } else if (block instanceof RotatedPillarBlock) {
            tag(BlockTags.LOGS).add(block);
            tag(BlockTags.LOGS_THAT_BURN).add(block);
            tag(BlockTags.MINEABLE_WITH_AXE).add(block);
        } else if (block instanceof StairBlock) {
            tag(BlockTags.WOODEN_STAIRS).add(block);
            tag(BlockTags.MINEABLE_WITH_AXE).add(block);
        } else if (block instanceof SlabBlock) {
            tag(BlockTags.WOODEN_SLABS).add(block);
            tag(BlockTags.MINEABLE_WITH_AXE).add(block);
        } else if (block instanceof FenceBlock) {
            tag(BlockTags.WOODEN_FENCES).add(block);
            tag(BlockTags.MINEABLE_WITH_AXE).add(block);
        } else if (block instanceof FenceGateBlock) {
            tag(BlockTags.FENCE_GATES).add(block);
            tag(BlockTags.MINEABLE_WITH_AXE).add(block);
        } else if (block instanceof ButtonBlock) {
            tag(BlockTags.WOODEN_BUTTONS).add(block);
            tag(BlockTags.MINEABLE_WITH_AXE).add(block);
        } else if (block instanceof PressurePlateBlock) {
            tag(BlockTags.WOODEN_PRESSURE_PLATES).add(block);
            tag(BlockTags.MINEABLE_WITH_AXE).add(block);
        }
    }
}
