package com.fidtest.block.entity;

import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.CookingRecipeInput;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
import java.util.List;

public class MortarBlockEntity extends BlockEntity {

    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) { setChanged(); }
        @Override
        public int getSlotLimit(int slot) { return 64; }
    };

    public MortarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MORTAR.get(), pos, state);
    }

    public ItemStackHandler getInventory() { return inventory; }
    public ItemStack getStoredItem() { return inventory.getStackInSlot(0); }
    public boolean isEmpty() { return inventory.getStackInSlot(0).isEmpty(); }

    public boolean isRecipeItem(ItemStack stack) {
        if (level == null || level.isClientSide || stack.isEmpty()) return false;
        List<CookingRecipe> recipes = ((ServerLevel) level).getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.COOKING.get());
        for (CookingRecipe recipe : recipes) {
            String app = recipe.getAppliance();
            if (!app.isEmpty() && !app.equals("mortar")) continue;
            for (Ingredient ing : recipe.getIngredients()) {
                if (ing.test(stack)) return true;
            }
        }
        return false;
    }

    public ItemStack addItem(ItemStack stack) {
        return inventory.insertItem(0, stack, false);
    }

    public ItemStack removeItem() {
        return inventory.extractItem(0, 64, false);
    }

    public boolean processStored(@Nullable Player player) {
        if (level == null || level.isClientSide || isEmpty()) return false;

        ItemStack stored = getStoredItem().copy();
        CookingRecipeInput input = new CookingRecipeInput(List.of(stored.copy()));

        List<CookingRecipe> recipes = ((ServerLevel) level).getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.COOKING.get());

        for (CookingRecipe recipe : recipes) {
            String app = recipe.getAppliance();
            if (!app.isEmpty() && !app.equals("mortar")) continue;
            if (recipe.matches(input, level)) {
                doProcess(recipe.getResult().copy());
                return true;
            }
        }
        return false;
    }

    private void doProcess(ItemStack result) {
        if (level == null) return;
        ItemEntity entity = new ItemEntity(level,
                worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, result.copy());
        entity.setDefaultPickUpDelay();
        level.addFreshEntity(entity);
        inventory.extractItem(0, 1, false);
        level.playSound(null, worldPosition, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
    }

    @Override public CompoundTag getUpdateTag() { CompoundTag t = new CompoundTag(); saveAdditional(t); return t; }
    @Nullable @Override public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
    @Override public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) { load(pkt.getTag()); }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }
}
