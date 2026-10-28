package com.fidtest.block.entity;

import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.CookingRecipeInput;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MeatmincerBlockEntity extends BlockEntity {

    public static final int INPUT_SLOT = 0, OUTPUT_SLOT = 1, TOTAL_SLOTS = 2, DEFAULT_PROCESS_TIME = 400;
    private static final ResourceLocation COVER_ID = new ResourceLocation("flavor_immersed_daily", "mincer_cover");

    private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS) {
        @Override protected void onContentsChanged(int s) { setChanged(); }
        @Override public int getSlotLimit(int s) { return 64; }
    };

    private boolean sealed, working, processingDone;
    private int processTick, totalProcessTime = DEFAULT_PROCESS_TIME;
    private List<ItemStack> renderInputItems = new ArrayList<>();
    private ItemStack resultTemplate = ItemStack.EMPTY;
    private int pendingResultTotal;

    public MeatmincerBlockEntity(BlockPos pos, BlockState state) { super(ModBlockEntities.MEATMINCER.get(), pos, state); }

    public ItemStackHandler getInventory() { return inventory; }
    public boolean isSealed() { return sealed; }
    public boolean isWorking() { return working; }
    public int getProcessTick() { return processTick; }
    public List<ItemStack> getRenderInputItems() { return renderInputItems; }
    public boolean isProcessingDone() { return processingDone; }
    public boolean hasAnyInput() { return !inventory.getStackInSlot(INPUT_SLOT).isEmpty(); }
    public boolean hasOutput() { return !inventory.getStackInSlot(OUTPUT_SLOT).isEmpty() || pendingResultTotal > 0; }
    public ItemStack getInputStack() { return inventory.getStackInSlot(INPUT_SLOT); }
    public ItemStack getOutputItem() { return inventory.getStackInSlot(OUTPUT_SLOT); }

    public ItemStack addItemToSlot(ItemStack stack) {
        if (inventory.getStackInSlot(INPUT_SLOT).isEmpty()) return inventory.insertItem(INPUT_SLOT, stack, false);
        return stack;
    }

    public ItemStack removeInput() {
        return inventory.extractItem(INPUT_SLOT, 64, false);
    }

    public boolean checkRecipe() {
        if (level == null || level.isClientSide) return false;
        List<ItemStack> inputs = new ArrayList<>();
        ItemStack s = inventory.getStackInSlot(INPUT_SLOT);
        if (!s.isEmpty()) inputs.add(s.copy());
        if (inputs.isEmpty()) return false;
        CookingRecipeInput in = new CookingRecipeInput(inputs);
        for (CookingRecipe r : ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get())) {
            String a = r.getAppliance(); if (!a.isEmpty() && !a.equals("meatmincer")) continue;
            if (r.matches(in, level)) return true;
        }
        return false;
    }

    public void seal() { sealed = true; if (!startProcessing()) sealed = false; }

    private boolean startProcessing() {
        if (level == null || level.isClientSide) return false;
        List<ItemStack> inputs = new ArrayList<>();
        ItemStack s = inventory.getStackInSlot(INPUT_SLOT);
        if (!s.isEmpty()) inputs.add(s.copy());
        if (inputs.isEmpty()) return false;
        CookingRecipeInput in = new CookingRecipeInput(inputs);
        for (CookingRecipe recipe : ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get())) {
            String a = recipe.getAppliance(); if (!a.isEmpty() && !a.equals("meatmincer")) continue;
            if (!recipe.matches(in, level)) continue;
            List<ItemStack> rl = new ArrayList<>();
            for (ItemStack inp : inputs) { ItemStack c = inp.copy(); c.setCount(1); rl.add(c); }
            renderInputItems = rl;
            resultTemplate = recipe.getResult().copy(); pendingResultTotal = resultTemplate.getCount();
            totalProcessTime = recipe.getCookingTime() > 0 ? recipe.getCookingTime() : DEFAULT_PROCESS_TIME;
            working = true; processTick = 0; processingDone = false;
            for (Ingredient ing : recipe.getIngredients()) {
                int cost = 1; for (ItemStack t : ing.getItems()) cost = Math.max(cost, t.getCount());
                int toExtract = cost;
                if (ing.test(inventory.getStackInSlot(INPUT_SLOT))) { int ex = Math.min(toExtract, inventory.getStackInSlot(INPUT_SLOT).getCount()); inventory.extractItem(INPUT_SLOT, ex, false); }
            }
            setChanged();
            level.playSound(null, worldPosition, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.5F, 1.0F);
            return true;
        }
        return false;
    }

    public void unseal(Player player) {
        sealed = working = processingDone = false; processTick = 0; renderInputItems.clear(); pendingResultTotal = 0; resultTemplate = ItemStack.EMPTY;
        for (int i = 0; i < TOTAL_SLOTS; i++) { ItemStack s = inventory.extractItem(i, 64, false); if (!s.isEmpty() && !player.isCreative()) { if (!player.getInventory().add(s)) player.drop(s, false); } }
        ItemStack cover = getCoverDrop(); if (!player.isCreative() && !player.getInventory().add(cover)) player.drop(cover, false);
        setChanged();
    }

    public ItemStack collectOutput() {
        if (pendingResultTotal > 0) { int g = Math.min(pendingResultTotal, resultTemplate.getMaxStackSize()); ItemStack r = resultTemplate.copy(); r.setCount(g); pendingResultTotal -= g; setChanged(); return r; }
        return inventory.extractItem(OUTPUT_SLOT, 64, false);
    }

    public ItemStack getCoverDrop() { return new ItemStack(BuiltInRegistries.ITEM.get(COVER_ID), 1); }

    public static void serverTick(MeatmincerBlockEntity be) {
        if (!be.working) return; be.processTick++;
        if (be.processTick >= be.totalProcessTime) {
            be.working = false; be.processTick = 0;
            ItemStack out = be.inventory.getStackInSlot(OUTPUT_SLOT); int max = be.resultTemplate.getMaxStackSize(), total = be.pendingResultTotal;
            if (!out.isEmpty() && ItemStack.isSameItem(out, be.resultTemplate)) { int add = Math.min(total, max - out.getCount()); out.grow(add); total -= add; }
            if (total > 0 && (out.isEmpty() || ItemStack.isSameItem(out, be.resultTemplate))) { int put = Math.min(total, max); if (out.isEmpty()) { ItemStack ns = be.resultTemplate.copy(); ns.setCount(put); be.inventory.setStackInSlot(OUTPUT_SLOT, ns); } else out.grow(put); total -= put; }
            while (total > 0) { int dc = Math.min(total, max); ItemStack ds = be.resultTemplate.copy(); ds.setCount(dc); ItemEntity e = new ItemEntity(be.level, be.worldPosition.getX()+0.5, be.worldPosition.getY()+0.5, be.worldPosition.getZ()+0.5, ds); e.setDefaultPickUpDelay(); be.level.addFreshEntity(e); total -= dc; }
            be.pendingResultTotal = 0;
            if (!be.startProcessing()) { be.processingDone = true; be.renderInputItems.clear(); }
            be.setChanged();
            if (be.level != null) be.level.playSound(null, be.worldPosition, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override public void saveAdditional(CompoundTag tag) { super.saveAdditional(tag); tag.put("Inventory", inventory.serializeNBT()); tag.putBoolean("Sealed", sealed); tag.putBoolean("Working", working); tag.putInt("ProcessTick", processTick); tag.putInt("TotalProcessTime", totalProcessTime); tag.putInt("PendingResultTotal", pendingResultTotal); if (!resultTemplate.isEmpty()) tag.put("ResultTemplate", resultTemplate.save(new CompoundTag())); ListTag rl = new ListTag(); for (ItemStack s : renderInputItems) { ItemStack c = s.copy(); c.setCount(1); rl.add(c.save(new CompoundTag())); } tag.put("RenderItems", rl); }
    @Override public void load(CompoundTag tag) { super.load(tag); inventory.deserializeNBT(tag.getCompound("Inventory")); sealed = tag.getBoolean("Sealed"); working = tag.getBoolean("Working"); processTick = tag.getInt("ProcessTick"); totalProcessTime = tag.getInt("TotalProcessTime"); if (totalProcessTime <= 0) totalProcessTime = DEFAULT_PROCESS_TIME; pendingResultTotal = tag.getInt("PendingResultTotal"); resultTemplate = tag.contains("ResultTemplate") ? ItemStack.of(tag.getCompound("ResultTemplate")) : ItemStack.EMPTY; renderInputItems = new ArrayList<>(); ListTag rl = tag.getList("RenderItems", Tag.TAG_COMPOUND); for (int i = 0; i < rl.size(); i++) renderInputItems.add(ItemStack.of(rl.getCompound(i))); }
    @Override public CompoundTag getUpdateTag() { CompoundTag t = new CompoundTag(); saveAdditional(t); return t; }
    @Nullable @Override public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
    @Override public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) { load(pkt.getTag()); }
    @Override public void setChanged() { super.setChanged(); if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3); }
}
