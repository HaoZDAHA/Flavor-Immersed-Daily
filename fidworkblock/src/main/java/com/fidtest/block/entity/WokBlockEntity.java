package com.fidtest.block.entity;

import com.fidtest.item.DishItem;
import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.CookingRecipeInput;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WokBlockEntity extends BlockEntity {

    private static final int MAX_ITEMS = 6;
    private final ItemStackHandler inventory = new ItemStackHandler(MAX_ITEMS) {
        @Override protected void onContentsChanged(int s) { setChanged(); }
        @Override public int getSlotLimit(int s) { return 1; }
    };

    private int stirCount, tossTicks;
    private String seasoningName = "";

    public WokBlockEntity(BlockPos pos, BlockState state) { super(ModBlockEntities.WOK.get(), pos, state); }

    public ItemStackHandler getInventory() { return inventory; }
    public int getStirCount() { return stirCount; }
    public int getTossTicks() { return tossTicks; }
    public String getSeasoningName() { return seasoningName; }
    public void setSeasoningName(String n) { seasoningName = n; }
    public int getItemCount() { int c = 0; for (int i = 0; i < MAX_ITEMS; i++) if (!inventory.getStackInSlot(i).isEmpty()) c++; return c; }
    public boolean canAddItem() { return getItemCount() < MAX_ITEMS; }

    public void addItem(ItemStack s) {
        for (int i = 0; i < MAX_ITEMS; i++) if (inventory.getStackInSlot(i).isEmpty()) { inventory.setStackInSlot(i, s.copy()); sync(); return; }
    }

    public void stirFry(@Nullable Player player) {
        if (getItemCount() == 0) return; stirCount++; tossTicks = 12;
        if (level != null && !level.isClientSide)
            for (int i = 0; i < 8; i++) ((ServerLevel) level).sendParticles(ParticleTypes.SMOKE, worldPosition.getX()+0.3+level.random.nextDouble()*0.4, worldPosition.getY()+0.25, worldPosition.getZ()+0.3+level.random.nextDouble()*0.4, 1, 0.02, 0.05, 0.02, 0.01);
        sync();
    }

    public void finishCooking(Level level, BlockPos pos) {
        if (level.isClientSide) return;
        int sc = stirCount; String se = seasoningName;
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < MAX_ITEMS; i++) { ItemStack s = inventory.getStackInSlot(i); if (!s.isEmpty()) { items.add(s.copy()); inventory.setStackInSlot(i, ItemStack.EMPTY); } }
        ItemStack result = findRecipeResult(level, items);
        if (result == null) { result = DishItem.createProceduralDish(items); DishItem.setFoodProperties(result, items, sc); DishItem.setStirCount(result, sc); if (!se.isEmpty()) DishItem.setSeasoningEffect(result, se); }
        ItemEntity e = new ItemEntity(level, pos.getX()+0.5, pos.getY()+0.3, pos.getZ()+0.5, result); e.setDefaultPickUpDelay(); level.addFreshEntity(e);
        stirCount = 0; tossTicks = 0; seasoningName = ""; sync();
    }

    private ItemStack findRecipeResult(Level level, List<ItemStack> inputs) {
        for (CookingRecipe r : ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get())) {
            String a = r.getAppliance(); if (!a.isEmpty() && !a.equals("wok")) continue;
            if (r.matches(new CookingRecipeInput(inputs), level)) return r.getResult().copy();
        }
        return null;
    }

    public List<ItemStack> getStoredItems() {
        List<ItemStack> l = new ArrayList<>(); for (int i = 0; i < MAX_ITEMS; i++) { ItemStack s = inventory.getStackInSlot(i); if (!s.isEmpty()) l.add(s); } return l;
    }

    public void tick() { if (tossTicks > 0) { tossTicks--; sync(); } }

    private void sync() { setChanged(); if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3); }

    @Override public void saveAdditional(CompoundTag tag) { super.saveAdditional(tag); tag.put("Inventory", inventory.serializeNBT()); tag.putInt("StirCount", stirCount); tag.putInt("TossTicks", tossTicks); }
    @Override public void load(CompoundTag tag) { super.load(tag); inventory.deserializeNBT(tag.getCompound("Inventory")); stirCount = tag.getInt("StirCount"); tossTicks = tag.getInt("TossTicks"); }
    @Override public CompoundTag getUpdateTag() { CompoundTag t = new CompoundTag(); saveAdditional(t); return t; }
    @Nullable @Override public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
    @Override public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) { load(pkt.getTag()); }
}
