package com.fidtest.block.entity;

import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.CookingRecipeInput;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TeapotBlockEntity extends BlockEntity {

    public static final int INPUT_SLOT_0 = 0, INPUT_SLOT_3 = 3, OUTPUT_SLOT = 4, TOTAL_SLOTS = 5, DEFAULT_PROCESS_TIME = 400;

    private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS) {
        @Override protected void onContentsChanged(int s) { setChanged(); }
        @Override public int getSlotLimit(int s) { return 64; }
    };

    private boolean sealed, working, processingDone;
    private int processTick, totalProcessTime = DEFAULT_PROCESS_TIME;
    private List<ItemStack> renderInputItems = new ArrayList<>();
    private ItemStack resultTemplate = ItemStack.EMPTY;
    private int pendingResultTotal;

    public TeapotBlockEntity(BlockPos pos, BlockState state) { super(ModBlockEntities.TEAPOT.get(), pos, state); }

    public ItemStackHandler getInventory() { return inventory; }
    public boolean isSealed() { return sealed; }
    public boolean isWorking() { return working; }
    public int getProcessTick() { return processTick; }
    public List<ItemStack> getRenderInputItems() { return renderInputItems; }
    public boolean isProcessingDone() { return processingDone; }
    public boolean hasAnyInput() { for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) if (!inventory.getStackInSlot(i).isEmpty()) return true; return false; }
    public boolean hasOutput() { return !inventory.getStackInSlot(OUTPUT_SLOT).isEmpty() || pendingResultTotal > 0; }
    public ItemStack getInputStack(int i) { return i >= 0 && i <= 3 ? inventory.getStackInSlot(i) : ItemStack.EMPTY; }
    public ItemStack getOutputItem() { return inventory.getStackInSlot(OUTPUT_SLOT); }

    public ItemStack addItemToNextSlot(ItemStack stack) {
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++)
            if (inventory.getStackInSlot(i).isEmpty()) return inventory.insertItem(i, stack, false);
        return stack;
    }

    public ItemStack removeLastInput() {
        for (int i = INPUT_SLOT_3; i >= INPUT_SLOT_0; i--)
            if (!inventory.getStackInSlot(i).isEmpty()) return inventory.extractItem(i, 64, false);
        return ItemStack.EMPTY;
    }

    public boolean checkRecipe() {
        if (level == null || level.isClientSide) return false;
        List<ItemStack> inputs = new ArrayList<>();
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) { ItemStack s = inventory.getStackInSlot(i); if (!s.isEmpty()) inputs.add(s.copy()); }
        if (inputs.isEmpty()) return false;
        CookingRecipeInput in = new CookingRecipeInput(inputs);
        for (CookingRecipe r : ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get())) {
            String a = r.getAppliance(); if (!a.isEmpty() && !a.equals("teapot")) continue;
            if (r.matchesAccurate(in, level)) return true;
        }
        return false;
    }

    public void seal() { sealed = true; if (!startProcessing()) sealed = false; }

    private boolean startProcessing() {
        if (level == null || level.isClientSide) return false;
        List<ItemStack> inputs = new ArrayList<>();
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) { ItemStack s = inventory.getStackInSlot(i); if (!s.isEmpty()) inputs.add(s.copy()); }
        if (inputs.isEmpty()) return false;
        CookingRecipeInput in = new CookingRecipeInput(inputs);
        for (CookingRecipe recipe : ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get())) {
            String a = recipe.getAppliance(); if (!a.isEmpty() && !a.equals("teapot")) continue;
            if (!recipe.matchesAccurate(in, level)) continue;
// 记录本批要处理的材料（保留真实数量，供 HUD 显示）
            List<ItemStack> rl = new ArrayList<>();
            for (ItemStack inp : inputs) rl.add(inp.copy());
            renderInputItems = rl;
            resultTemplate = recipe.getCookedResult(inputs);
// 累加到账本：单槽放不下的产物保留在账本里，等玩家取出，绝不丢失
            pendingResultTotal += resultTemplate.getCount();
            totalProcessTime = recipe.getCookingTime() > 0 ? recipe.getCookingTime() : DEFAULT_PROCESS_TIME;
            working = true; processTick = 0; processingDone = false;
            for (Ingredient ing : recipe.getIngredients()) {
                int cost = 1; for (ItemStack t : ing.getItems()) cost = Math.max(cost, t.getCount());
                int toExtract = cost;
                for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3 && toExtract > 0; i++)
                    if (ing.test(inventory.getStackInSlot(i))) { int ex = Math.min(toExtract, inventory.getStackInSlot(i).getCount()); inventory.extractItem(i, ex, false); toExtract -= ex; }
            }
            setChanged();
            level.playSound(null, worldPosition, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.5F, 1.0F);
            return true;
        }
        return false;
    }

    public void unseal(Player player) {
        sealed = false; working = false; processingDone = false; processTick = 0; renderInputItems.clear();
// 返还账本中尚未落地的产物（防止满一组时产物滞留/丢失）
        for (ItemStack s : extractPendingResults()) { if (!s.isEmpty() && !player.isCreative()) { if (!player.getInventory().add(s)) player.drop(s, false); } }
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) { ItemStack s = inventory.extractItem(i, 64, false); if (!s.isEmpty() && !player.isCreative()) { if (!player.getInventory().add(s)) player.drop(s, false); } }
        ItemStack out = inventory.extractItem(OUTPUT_SLOT, 64, false);
        if (!out.isEmpty() && !player.isCreative()) { if (!player.getInventory().add(out)) player.drop(out, false); }
        setChanged();
    }

// 取出账本（pendingResultTotal）中尚未落地的产物。
// 用于取消密封、破坏方块等场景，确保这些产物被返还/掉落，绝不丢失。
    public List<ItemStack> extractPendingResults() {
        List<ItemStack> out = new ArrayList<>();
        if (pendingResultTotal <= 0 || resultTemplate.isEmpty()) { pendingResultTotal = 0; return out; }
        int max = resultTemplate.getMaxStackSize();
        while (pendingResultTotal > 0) {
            int give = Math.min(pendingResultTotal, max);
            ItemStack stack = resultTemplate.copy(); stack.setCount(give);
            pendingResultTotal -= give; out.add(stack);
        }
        resultTemplate = ItemStack.EMPTY;
        return out;
    }

    public ItemStack collectOutput() {
// 先把账本中还没落地的产物尽量补进输出槽，然后一次全部取出，防止满一组时取不完导致产物滞留/丢失
        if (pendingResultTotal > 0 && !resultTemplate.isEmpty()) {
            ItemStack outSlot = inventory.getStackInSlot(OUTPUT_SLOT);
            if (outSlot.isEmpty() || ItemStack.isSameItem(outSlot, resultTemplate)) {
                int maxStack = resultTemplate.getMaxStackSize();
                int space = outSlot.isEmpty() ? maxStack : maxStack - outSlot.getCount();
                int put = Math.min(pendingResultTotal, space);
                if (put > 0) {
                    if (outSlot.isEmpty()) { ItemStack ns = resultTemplate.copy(); ns.setCount(put); inventory.setStackInSlot(OUTPUT_SLOT, ns); }
                    else outSlot.grow(put);
                    pendingResultTotal -= put;
                }
            }
        }
        ItemStack result = inventory.extractItem(OUTPUT_SLOT, 64, false);
        setChanged();
        return result;
    }

    public static void serverTick(TeapotBlockEntity be) {
        if (!be.working) return;
        be.processTick++;
        if (be.processTick >= be.totalProcessTime) {
            be.working = false; be.processTick = 0;
// 本批产物的数量已在 startProcessing 时计入账本，这里只负责落地到输出槽
// （账本单槽放不下的部分继续保留，等玩家取出；切勿再次累加，否则每批会出两份）
            if (be.pendingResultTotal > 0 && !be.resultTemplate.isEmpty()) {
                ItemStack outSlot = be.inventory.getStackInSlot(OUTPUT_SLOT);
                if (outSlot.isEmpty() || ItemStack.isSameItem(outSlot, be.resultTemplate)) {
                    int maxStack = be.resultTemplate.getMaxStackSize();
                    int space = outSlot.isEmpty() ? maxStack : maxStack - outSlot.getCount();
                    int put = Math.min(be.pendingResultTotal, space);
                    if (put > 0) {
                        if (outSlot.isEmpty()) { ItemStack ns = be.resultTemplate.copy(); ns.setCount(put); be.inventory.setStackInSlot(OUTPUT_SLOT, ns); }
                        else outSlot.grow(put);
                        be.pendingResultTotal -= put;
                    }
                }
            }
// 输出槽已满（或账本仍有剩余放不下）：停止工作，等玩家取出后重新密封继续，剩余产物保留在账本中不丢失
            boolean outputFull = !be.inventory.getStackInSlot(OUTPUT_SLOT).isEmpty()
                    && be.inventory.getStackInSlot(OUTPUT_SLOT).getCount() >= (be.resultTemplate.isEmpty() ? 64 : be.resultTemplate.getMaxStackSize());
            if (be.pendingResultTotal > 0 || outputFull) {
                be.processingDone = true; be.renderInputItems.clear();
            } else if (!be.startProcessing()) {
                be.processingDone = true; be.renderInputItems.clear();
            }
            be.setChanged();
            if (be.level != null) be.level.playSound(null, be.worldPosition, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override public void saveAdditional(CompoundTag tag) { super.saveAdditional(tag); tag.put("Inventory", inventory.serializeNBT()); tag.putBoolean("Sealed", sealed); tag.putBoolean("Working", working); tag.putInt("ProcessTick", processTick); tag.putInt("TotalProcessTime", totalProcessTime); tag.putInt("PendingResultTotal", pendingResultTotal); if (!resultTemplate.isEmpty()) tag.put("ResultTemplate", resultTemplate.save(new CompoundTag())); ListTag rl = new ListTag(); for (ItemStack s : renderInputItems) { ItemStack c = s.copy(); c.setCount(1); rl.add(c.save(new CompoundTag())); } tag.put("RenderItems", rl); }
    @Override public void load(CompoundTag tag) {
        super.load(tag);
// deserializeNBT 会按其 "Size" 重建槽位列表，需先把 Size 归一化为当前槽位数再反序列化，防止越界崩溃
        CompoundTag inv = tag.getCompound("Inventory").copy();
        inv.putInt("Size", TOTAL_SLOTS);
        inventory.deserializeNBT(inv);
// 防御：修复历史存档中超过 maxStackSize 的非法堆叠，防止再次崩服
        for (int i = 0; i < inventory.getSlots(); i++) { ItemStack s = inventory.getStackInSlot(i); if (!s.isEmpty() && s.getCount() > s.getMaxStackSize()) s.setCount(s.getMaxStackSize()); }
        sealed = tag.getBoolean("Sealed"); working = tag.getBoolean("Working"); processTick = tag.getInt("ProcessTick"); totalProcessTime = tag.getInt("TotalProcessTime"); if (totalProcessTime <= 0) totalProcessTime = DEFAULT_PROCESS_TIME; pendingResultTotal = tag.getInt("PendingResultTotal"); resultTemplate = tag.contains("ResultTemplate") ? ItemStack.of(tag.getCompound("ResultTemplate")) : ItemStack.EMPTY; renderInputItems = new ArrayList<>(); ListTag rl = tag.getList("RenderItems", Tag.TAG_COMPOUND); for (int i = 0; i < rl.size(); i++) renderInputItems.add(ItemStack.of(rl.getCompound(i)));
    }
    @Override public CompoundTag getUpdateTag() { CompoundTag t = new CompoundTag(); saveAdditional(t); return t; }
    @Nullable @Override public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
    @Override public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) { load(pkt.getTag()); }
    @Override public void setChanged() { super.setChanged(); if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3); }
}
