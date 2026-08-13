package com.flavor_immersed_daily.datagen.tag;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.item.SeasoningItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

/** Generates item tags from the registered item types. */
public final class FIDItemTagsProvider extends ItemTagsProvider {
    public FIDItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
                               ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, FlavorImmersedDaily.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(FIDItemTags.RADISH).add(ModItems.RADISH.get());

        ModItems.REGISTRY.getEntries().forEach(entry -> {
            Item item = entry.get();
            if (item instanceof SeasoningItem) {
                tag(FIDItemTags.SEASONING).add(item);
            }
        });
    }
}
