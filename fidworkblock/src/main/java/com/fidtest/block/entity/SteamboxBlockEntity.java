package com.fidtest.block.entity;

import com.fidtest.block.SteamboxBlock;
import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.CookingRecipeInput;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlockEntities;
import com.fidtest.registration.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SteamboxBlockEntity extends BlockEntity {

    public static final int INPUT_SLOT_0 = 0, INPUT_SLOT_3 = 3, OUTPUT_SLOT = 4, TOTAL_SLOTS = 5, DEFAULT_PROCESS_TIME = 200;

    private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS) {
        @Override protected void onContentsChanged(int s) { setChanged(); }
        @Override public int getSlotLimit(int s) { return 64; }
    };

    private boolean working;
    private int processTick, totalProcessTime = DEFAULT_PROCESS_TIME;
    private List<ItemStack> renderInputItems = new ArrayList<>();
    private ItemStack resultTemplate = ItemStack.EMPTY;
    private int pendingResultTotal;

    public SteamboxBlockEntity(BlockPos pos, BlockState state) { super(ModBlockEntities.STEAMBOX.get(), pos, state); }

    public ItemStackHandler getInventory() { return inventory; }
    public boolean isWorking() { return working; }
    public int getProcessTick() { return processTick; }
    public int getTotalProcessTime() { return totalProcessTime; }
    public List<ItemStack> getRenderInputItems() { return renderInputItems; }
    public void setTotalProcessTime(int t) { totalProcessTime = t; setChanged(); }
    public boolean hasAnyInput() { for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) if (!inventory.getStackInSlot(i).isEmpty()) return true; return false; }
    public boolean hasOutput() { return !inventory.getStackInSlot(OUTPUT_SLOT).isEmpty() || pendingResultTotal > 0; }

    public List<ItemStack> getInputItems() {
        List<ItemStack> items = new ArrayList<>();
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) { ItemStack s = inventory.getStackInSlot(i); if (!s.isEmpty()) items.add(s.copy()); }
        return items;
    }

    public List<ItemStack> getAllItems() {
        List<ItemStack> all = getInputItems();
        ItemStack out = inventory.getStackInSlot(OUTPUT_SLOT); if (!out.isEmpty()) all.add(out.copy());
        return all;
    }

    public void flushPendingResults() {
        if (pendingResultTotal <= 0 || resultTemplate.isEmpty()) return;
        ItemStack out = inventory.getStackInSlot(OUTPUT_SLOT);
        int max = resultTemplate.getMaxStackSize(), total = pendingResultTotal;
        if (!out.isEmpty() && ItemStack.isSameItem(out, resultTemplate)) { int add = Math.min(total, max - out.getCount()); out.grow(add); total -= add; }
        if (total > 0 && (out.isEmpty() || ItemStack.isSameItem(out, resultTemplate))) { int put = Math.min(total, max); if (out.isEmpty()) { ItemStack ns = resultTemplate.copy(); ns.setCount(put); inventory.setStackInSlot(OUTPUT_SLOT, ns); } else out.grow(put); total -= put; }
        while (total > 0) { int dc = Math.min(total, max); ItemStack ds = resultTemplate.copy(); ds.setCount(dc); if (level != null) { ItemEntity e = new ItemEntity(level, worldPosition.getX()+0.5, worldPosition.getY()+0.5, worldPosition.getZ()+0.5, ds); e.setDefaultPickUpDelay(); level.addFreshEntity(e); } total -= dc; }
        pendingResultTotal = 0; resultTemplate = ItemStack.EMPTY; setChanged();
    }

    public boolean canAcceptItem(ItemStack held) {
        if (level == null || level.isClientSide) return true;
        int c = 0; for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) c += inventory.getStackInSlot(i).getCount();
        return c < 4 && isValidSteamIngredient(held);
    }

    private boolean isValidSteamIngredient(ItemStack held) {
        if (level == null || level.isClientSide) return true;
        List<ItemStack> test = new ArrayList<>();
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) { ItemStack s = inventory.getStackInSlot(i); if (!s.isEmpty()) test.add(s.copy()); }
        test.add(held.copy());
        return ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get()).stream()
                .filter(r -> { String a = r.getAppliance(); return a.isEmpty() || a.equals("steambox"); })
                .anyMatch(r -> r.matches(new CookingRecipeInput(test), level));
    }

    public int addItems(ItemStack held) {
        if (level == null) return 0;
        int c = 0; for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) c += inventory.getStackInSlot(i).getCount();
        int space = 4 - c; if (space <= 0 || !isValidSteamIngredient(held)) return 0;
        int toAdd = Math.min(held.getCount(), space);
        ItemStack ins = held.copy(); ins.setCount(toAdd);
        ItemStack rem = addToSlot(ins);
        int added = toAdd - rem.getCount();
        if (added > 0) { held.shrink(added); setChanged(); if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3); }
        return added;
    }

    private ItemStack addToSlot(ItemStack s) {
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) {
            ItemStack e = inventory.getStackInSlot(i);
            if (!e.isEmpty() && ItemStack.isSameItem(e, s) && e.getCount() < e.getMaxStackSize()) return inventory.insertItem(i, s, false);
        }
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) if (inventory.getStackInSlot(i).isEmpty()) return inventory.insertItem(i, s, false);
        return s;
    }

    public boolean startProcessing() {
        if (level == null || level.isClientSide) return false;
        List<ItemStack> inputs = getInputItems(); if (inputs.isEmpty()) return false;
        CookingRecipeInput in = new CookingRecipeInput(inputs);
        for (CookingRecipe recipe : ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get())) {
            String a = recipe.getAppliance(); if (!a.isEmpty() && !a.equals("steambox")) continue;
            if (!recipe.matches(in, level)) continue;
            List<Ingredient> ings = recipe.getIngredients();
            int batches = Integer.MAX_VALUE;
            List<ItemStack> matched = new ArrayList<>(); List<Integer> costs = new ArrayList<>();
            for (Ingredient ing : ings) {
                int bestCost = Integer.MAX_VALUE; ItemStack best = ItemStack.EMPTY;
                for (ItemStack opt : ing.getItems()) {
                    int c = 0; for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) if (ing.test(inventory.getStackInSlot(i))) c += inventory.getStackInSlot(i).getCount();
                    int oc = c / opt.getCount(); if (oc > 0 && oc < bestCost) { bestCost = oc; best = opt; }
                }
                if (bestCost == Integer.MAX_VALUE || best.isEmpty()) { batches = 0; break; }
                batches = Math.min(batches, bestCost); matched.add(best); costs.add(best.getCount());
            }
            if (batches <= 0) continue;
            List<ItemStack> rl = new ArrayList<>();
            int rc = Math.min(batches, 5);
            for (int r = 0; r < rc; r++) { ItemStack s = inputs.get(r % inputs.size()).copy(); s.setCount(1); rl.add(s); }
            renderInputItems = rl;
            resultTemplate = recipe.getResult().copy(); pendingResultTotal = resultTemplate.getCount() * batches;
            totalProcessTime = recipe.getCookingTime() > 0 ? recipe.getCookingTime() : DEFAULT_PROCESS_TIME;
            working = true; processTick = 0;
            for (int idx = 0; idx < ings.size(); idx++) {
                int toExtract = costs.get(idx) * batches;
                for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3 && toExtract > 0; i++)
                    if (ings.get(idx).test(inventory.getStackInSlot(i))) { int ex = Math.min(toExtract, inventory.getStackInSlot(i).getCount()); inventory.extractItem(i, ex, false); toExtract -= ex; }
            }
            setChanged();
            level.playSound(null, worldPosition, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 0.8F);
            return true;
        }
        return false;
    }

    public ItemStack collectOutput() {
        if (pendingResultTotal > 0) { int g = Math.min(pendingResultTotal, resultTemplate.getMaxStackSize()); ItemStack r = resultTemplate.copy(); r.setCount(g); pendingResultTotal -= g; setChanged(); return r; }
        return inventory.extractItem(OUTPUT_SLOT, 64, false);
    }

    public void clearItems() { for (int i = 0; i < TOTAL_SLOTS; i++) inventory.setStackInSlot(i, ItemStack.EMPTY); resetWorking(); }
    public void resetWorking() { working = false; processTick = 0; renderInputItems.clear(); pendingResultTotal = 0; resultTemplate = ItemStack.EMPTY; }

    public void saveToItemStack(ItemStack stack) {
        CompoundTag data = new CompoundTag();
        ListTag in = new ListTag();
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) { ItemStack s = inventory.getStackInSlot(i); if (!s.isEmpty()) in.add(s.save(new CompoundTag())); }
        data.put("Input", in);
        ListTag out = new ListTag(); ItemStack o = inventory.getStackInSlot(OUTPUT_SLOT); if (!o.isEmpty()) out.add(o.save(new CompoundTag()));
        data.put("Output", out);
        data.putInt("PendingResultTotal", pendingResultTotal);
        if (!resultTemplate.isEmpty()) data.put("ResultTemplate", resultTemplate.save(new CompoundTag()));
        stack.getOrCreateTag().put("SteamboxData", data);
    }

    public void loadFromItemStack(ItemStack stack) {
        for (int i = 0; i < TOTAL_SLOTS; i++) inventory.setStackInSlot(i, ItemStack.EMPTY);
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("SteamboxData")) {
            CompoundTag data = tag.getCompound("SteamboxData");
            ListTag in = data.getList("Input", 10);
            for (int i = 0; i < in.size() && i < 4; i++) inventory.setStackInSlot(INPUT_SLOT_0 + i, ItemStack.of(in.getCompound(i)));
            ListTag out = data.getList("Output", 10);
            if (!out.isEmpty()) inventory.setStackInSlot(OUTPUT_SLOT, ItemStack.of(out.getCompound(0)));
            pendingResultTotal = data.getInt("PendingResultTotal");
            resultTemplate = data.contains("ResultTemplate") ? ItemStack.of(data.getCompound("ResultTemplate")) : ItemStack.EMPTY;
        }
        setChanged(); if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    public static void serverTick(SteamboxBlockEntity be) {
        if (!be.working) return; be.processTick++;
        if (be.processTick >= be.totalProcessTime) {
            be.working = false; be.processTick = 0;
            if (be.level != null && !be.level.isClientSide) be.level.setBlock(be.worldPosition, be.getBlockState().setValue(SteamboxBlock.WORKING, false), 3);
            be.flushPendingResults(); be.renderInputItems.clear(); be.setChanged();
            if (be.getBlockState().getValue(SteamboxBlock.BOTTOM)) {
                BlockPos c = be.worldPosition.above();
                while (be.level.getBlockState(c).is(ModBlocks.STEAMBOX.get())) {
                    if (be.level.getBlockEntity(c) instanceof SteamboxBlockEntity ab && ab.working) { ab.finishProcessing(); if (be.level != null && !be.level.isClientSide) be.level.setBlock(c, be.level.getBlockState(c).setValue(SteamboxBlock.WORKING, false), 3); }
                    c = c.above();
                }
            }
            if (be.level != null) be.level.playSound(null, be.worldPosition, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    private void finishProcessing() {
        if (!working) return; working = false; processTick = 0;
        ItemStack out = inventory.getStackInSlot(OUTPUT_SLOT); int max = resultTemplate.getMaxStackSize(), total = pendingResultTotal;
        if (!out.isEmpty() && ItemStack.isSameItem(out, resultTemplate)) { int add = Math.min(total, max - out.getCount()); out.grow(add); total -= add; }
        if (total > 0 && (out.isEmpty() || ItemStack.isSameItem(out, resultTemplate))) { int put = Math.min(total, max); if (out.isEmpty()) { ItemStack ns = resultTemplate.copy(); ns.setCount(put); inventory.setStackInSlot(OUTPUT_SLOT, ns); } else out.grow(put); total -= put; }
        while (total > 0) { int dc = Math.min(total, max); ItemStack ds = resultTemplate.copy(); ds.setCount(dc); if (level != null) { ItemEntity e = new ItemEntity(level, worldPosition.getX()+0.5, worldPosition.getY()+0.5, worldPosition.getZ()+0.5, ds); e.setDefaultPickUpDelay(); level.addFreshEntity(e); } total -= dc; }
        pendingResultTotal = 0; renderInputItems.clear(); setChanged();
    }

    @Override public void saveAdditional(CompoundTag tag) { super.saveAdditional(tag); tag.put("Inventory", inventory.serializeNBT()); tag.putBoolean("Working", working); tag.putInt("ProcessTick", processTick); tag.putInt("TotalProcessTime", totalProcessTime); tag.putInt("PendingResultTotal", pendingResultTotal); if (!resultTemplate.isEmpty()) tag.put("ResultTemplate", resultTemplate.save(new CompoundTag())); ListTag rl = new ListTag(); for (ItemStack s : renderInputItems) { ItemStack c = s.copy(); c.setCount(1); rl.add(c.save(new CompoundTag())); } tag.put("RenderItems", rl); }
    @Override public void load(CompoundTag tag) { super.load(tag); inventory.deserializeNBT(tag.getCompound("Inventory")); working = tag.getBoolean("Working"); processTick = tag.getInt("ProcessTick"); totalProcessTime = tag.getInt("TotalProcessTime"); if (totalProcessTime <= 0) totalProcessTime = DEFAULT_PROCESS_TIME; pendingResultTotal = tag.getInt("PendingResultTotal"); resultTemplate = tag.contains("ResultTemplate") ? ItemStack.of(tag.getCompound("ResultTemplate")) : ItemStack.EMPTY; renderInputItems = new ArrayList<>(); ListTag rl = tag.getList("RenderItems", Tag.TAG_COMPOUND); for (int i = 0; i < rl.size(); i++) renderInputItems.add(ItemStack.of(rl.getCompound(i))); }
    @Override public CompoundTag getUpdateTag() { CompoundTag t = new CompoundTag(); saveAdditional(t); return t; }
    @Nullable @Override public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
    @Override public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) { load(pkt.getTag()); }
    @Override public void setChanged() { super.setChanged(); if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3); }
}
