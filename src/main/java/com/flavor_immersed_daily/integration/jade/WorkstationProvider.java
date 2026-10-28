package com.flavor_immersed_daily.integration.jade;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.block.blockentity.EggBreakingMachineBlockEntity;
import com.flavor_immersed_daily.block.blockentity.FridgeBlockEntity;
import com.flavor_immersed_daily.block.blockentity.WoodBasinBlockEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

final class WorkstationProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    static final WorkstationProvider INSTANCE = new WorkstationProvider();
    private static final ResourceLocation UID = new ResourceLocation(
            FlavorImmersedDaily.MODID, "workstation_contents");
    private static final String ITEMS = "FIDItems";

    private WorkstationProvider() {
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        HolderLookup.Provider registries = accessor.getLevel().registryAccess();
        ListTag items = new ListTag();
        if (accessor.getBlockEntity() instanceof WoodBasinBlockEntity basin) {
            addStack(items, basin.getFruitStack(), registries);
        } else if (accessor.getBlockEntity() instanceof EggBreakingMachineBlockEntity machine) {
            addContainer(items, machine.getInventory(), registries);
            putProgress(data, machine.getProgress(), machine.getTotalTime());
        } else if (accessor.getBlockEntity() instanceof FridgeBlockEntity fridge) {
            addContainer(items, fridge, registries);
            data.putInt("TemperingProgress", fridge.getTemperingProgress());
            data.putInt("TemperingTotal", fridge.getTemperingTotalTime());
            data.putInt("FreezingProgress", fridge.getFreezingProgress());
            data.putInt("FreezingTotal", fridge.getFreezingTotalTime());
        }
        if (!items.isEmpty()) {
            data.put(ITEMS, items);
        }
    }

    private static void addContainer(ListTag target, Container container, HolderLookup.Provider registries) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            addStack(target, container.getItem(i), registries);
        }
    }

    private static void addStack(ListTag target, ItemStack stack, HolderLookup.Provider registries) {
        if (!stack.isEmpty()) {
            CompoundTag saved = new CompoundTag();
            stack.save(saved);
            target.add(saved);
        }
    }

    private static void putProgress(CompoundTag data, int progress, int total) {
        data.putInt("Progress", progress);
        data.putInt("Total", total);
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();
        if (data.contains(ITEMS)) {
            ListTag items = data.getList(ITEMS, Tag.TAG_COMPOUND);
            if (!items.isEmpty()) {
                tooltip.add(Component.translatable("jade.flavor_immersed_daily.contents"));
                for (int i = 0; i < items.size(); i++) {
                    int index = i;
                    ItemStack stack = ItemStack.of(items.getCompound(i));
                    if (!stack.isEmpty() && index % 9 == 0) {
                        tooltip.add(IElementHelper.get().item(stack));
                    } else if (!stack.isEmpty()) {
                        tooltip.append(IElementHelper.get().item(stack));
                    }
                }
            }
        }

        addProgress(tooltip, "jade.flavor_immersed_daily.progress", data.getInt("Progress"), data.getInt("Total"));
        addProgress(tooltip, "jade.flavor_immersed_daily.tempering", data.getInt("TemperingProgress"),
                data.getInt("TemperingTotal"));
        addProgress(tooltip, "jade.flavor_immersed_daily.freezing", data.getInt("FreezingProgress"),
                data.getInt("FreezingTotal"));
    }

    private static void addProgress(ITooltip tooltip, String key, int progress, int total) {
        if (total > 0) {
            float ratio = Math.min(1.0F, (float) progress / total);
            int percent = Math.round(ratio * 100.0F);
            tooltip.add(Component.translatable(key, percent));
            tooltip.append(IElementHelper.get().progress(ratio, null, IElementHelper.get().progressStyle(), snownee.jade.api.ui.BoxStyle.DEFAULT, false));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
