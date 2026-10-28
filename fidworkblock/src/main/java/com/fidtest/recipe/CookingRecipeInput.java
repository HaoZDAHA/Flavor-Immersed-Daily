package com.fidtest.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record CookingRecipeInput(List<ItemStack> items) implements Container {

    @Override
    public int getContainerSize() { return items.size(); }

    @Override
    public boolean isEmpty() { return items.stream().allMatch(ItemStack::isEmpty); }

    @Override
    public ItemStack getItem(int index) {
        if (index < 0 || index >= items.size()) return ItemStack.EMPTY;
        return items.get(index);
    }

    @Override public ItemStack removeItem(int i, int count) { return ItemStack.EMPTY; }
    @Override public ItemStack removeItemNoUpdate(int i) { return ItemStack.EMPTY; }
    @Override public void setItem(int i, ItemStack stack) {}
    @Override public void setChanged() {}
    @Override public boolean stillValid(Player player) { return true; }
    @Override public void clearContent() {}
}
