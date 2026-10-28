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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SqueezingmachineBlockEntity extends BlockEntity {

    public static final int TOTAL_PROCESS_TIME = 80;

    private final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override protected void onContentsChanged(int s) { setChanged(); }
        @Override public int getSlotLimit(int s) { return 64; }
    };

    private boolean working;
    private int processTick;
    private List<ItemStack> renderInputItems = new ArrayList<>();
    private int pendingResultTotal;
    private ItemStack resultTemplate = ItemStack.EMPTY;

    public SqueezingmachineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SQUEEZINGMACHINE.get(), pos, state);
    }

    public ItemStackHandler getInventory() { return inventory; }
    public boolean hasInput() { return !inventory.getStackInSlot(0).isEmpty(); }
    public boolean hasOutput() { return !inventory.getStackInSlot(1).isEmpty() || pendingResultTotal > 0; }
    public boolean isWorking() { return working; }
    public int getProcessTick() { return processTick; }
    public List<ItemStack> getRenderInputItems() { return renderInputItems; }
    public ItemStack getStoredResult() { return resultTemplate; }
    public ItemStack getInputItem() { return inventory.getStackInSlot(0); }
    public ItemStack getOutputItem() { return inventory.getStackInSlot(1); }

    public boolean isRecipeItem(ItemStack stack) {
        if (level == null || level.isClientSide || stack.isEmpty()) return false;
        List<CookingRecipe> recipes = ((ServerLevel) level).getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.COOKING.get());
        for (CookingRecipe r : recipes) {
            String a = r.getAppliance();
            if (!a.isEmpty() && !a.equals("squeezingmachine")) continue;
            for (Ingredient ing : r.getIngredients()) if (ing.test(stack)) return true;
        }
        return false;
    }

    public ItemStack addInput(ItemStack stack) {
        return inventory.insertItem(0, stack, false);
    }

    public ItemStack removeInput() {
        return inventory.extractItem(0, 64, false);
    }

    public ItemStack collectOutput() {
        if (pendingResultTotal > 0) {
            int give = Math.min(pendingResultTotal, resultTemplate.getMaxStackSize());
            ItemStack r = resultTemplate.copy(); r.setCount(give);
            pendingResultTotal -= give; setChanged(); return r;
        }
        return inventory.extractItem(1, 64, false);
    }

    public void startProcessing() {
        if (level == null || level.isClientSide || !hasInput() || working) return;
        ItemStack input = getInputItem().copy();
        int count = input.getCount();
        CookingRecipeInput in = new CookingRecipeInput(List.of(input.copy()));

        List<CookingRecipe> recipes = ((ServerLevel) level).getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.COOKING.get());

        for (CookingRecipe recipe : recipes) {
            String a = recipe.getAppliance();
            if (!a.isEmpty() && !a.equals("squeezingmachine")) continue;
            if (!recipe.matches(in, level)) continue;

            int cost = 1;
            for (Ingredient ing : recipe.getIngredients())
                for (ItemStack test : ing.getItems())
                    if (test.getItem() == input.getItem()) { cost = Math.max(cost, test.getCount()); break; }

            int batches = count / cost;
            if (batches <= 0) return;

            List<ItemStack> rList = new ArrayList<>();
            ItemStack single = input.copy(); single.setCount(1);
            for (int i = 0; i < Math.min(batches, 5); i++) rList.add(single.copy());
            renderInputItems = rList;
            resultTemplate = recipe.getResult().copy();
            pendingResultTotal = resultTemplate.getCount() * batches;
            working = true; processTick = 0;
            inventory.extractItem(0, batches * cost, false);
            setChanged();
            level.playSound(null, worldPosition, SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return;
        }
    }

    public static void serverTick(SqueezingmachineBlockEntity be) {
        if (!be.working) return;
        be.processTick++;
        if (be.processTick >= TOTAL_PROCESS_TIME) {
            be.working = false; be.processTick = 0; be.renderInputItems.clear();

            ItemStack out = be.inventory.getStackInSlot(1);
            int max = be.resultTemplate.getMaxStackSize();
            int total = be.pendingResultTotal;
            if (!out.isEmpty() && ItemStack.isSameItem(out, be.resultTemplate)) {
                int add = Math.min(total, max - out.getCount()); out.grow(add); total -= add;
            }
            if (total > 0 && out.isEmpty()) {
                int put = Math.min(total, max);
                ItemStack ns = be.resultTemplate.copy(); ns.setCount(put);
                be.inventory.setStackInSlot(1, ns); total -= put;
            }
            while (total > 0) {
                int dc = Math.min(total, max);
                ItemStack ds = be.resultTemplate.copy(); ds.setCount(dc);
                ItemEntity e = new ItemEntity(be.level, be.worldPosition.getX()+0.5, be.worldPosition.getY()+0.5, be.worldPosition.getZ()+0.5, ds);
                e.setDefaultPickUpDelay(); be.level.addFreshEntity(e); total -= dc;
            }
            be.pendingResultTotal = 0;
            be.setChanged();
            if (be.level != null) be.level.playSound(null, be.worldPosition, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());
        tag.putBoolean("Working", working);
        tag.putInt("ProcessTick", processTick);
        tag.putInt("PendingResultTotal", pendingResultTotal);
        if (!resultTemplate.isEmpty()) tag.put("ResultTemplate", resultTemplate.save(new CompoundTag()));
        ListTag rl = new ListTag();
        for (ItemStack s : renderInputItems) { ItemStack c = s.copy(); c.setCount(1); rl.add(c.save(new CompoundTag())); }
        tag.put("RenderItems", rl);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        working = tag.getBoolean("Working");
        processTick = tag.getInt("ProcessTick");
        pendingResultTotal = tag.getInt("PendingResultTotal");
        resultTemplate = tag.contains("ResultTemplate") ? ItemStack.of(tag.getCompound("ResultTemplate")) : ItemStack.EMPTY;
        renderInputItems = new ArrayList<>();
        ListTag rl = tag.getList("RenderItems", Tag.TAG_COMPOUND);
        for (int i = 0; i < rl.size(); i++) renderInputItems.add(ItemStack.of(rl.getCompound(i)));
    }

    @Override public CompoundTag getUpdateTag() { CompoundTag t = new CompoundTag(); saveAdditional(t); return t; }
    @Nullable @Override public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
    @Override public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) { load(pkt.getTag()); }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
}
