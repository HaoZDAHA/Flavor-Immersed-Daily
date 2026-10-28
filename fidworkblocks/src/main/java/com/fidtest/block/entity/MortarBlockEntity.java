package com.fidtest.block.entity;

import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.CookingRecipeInput;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MortarBlockEntity extends BlockEntity {

    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }
    };

    public MortarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MORTAR.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ItemStack getStoredItem() {
        return inventory.getStackInSlot(0);
    }

    public boolean isEmpty() {
        return inventory.getStackInSlot(0).isEmpty();
    }

    public boolean isRecipeItem(ItemStack stack) {
        if (level == null || level.isClientSide || stack.isEmpty()) return false;
        List<RecipeHolder<CookingRecipe>> recipes =
                ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get());
        for (RecipeHolder<CookingRecipe> holder : recipes) {
            CookingRecipe recipe = holder.value();
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

        List<RecipeHolder<CookingRecipe>> recipes =
                ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get());

        for (RecipeHolder<CookingRecipe> holder : recipes) {
            CookingRecipe recipe = holder.value();
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

        ItemStack drop = result.copy();
        double x = worldPosition.getX() + 0.5;
        double y = worldPosition.getY() + 0.5;
        double z = worldPosition.getZ() + 0.5;
        net.minecraft.world.entity.item.ItemEntity entity =
                new net.minecraft.world.entity.item.ItemEntity(level, x, y, z, drop);
        entity.setDefaultPickUpDelay();
        level.addFreshEntity(entity);

        inventory.extractItem(0, 1, false);

        level.playSound(null, worldPosition, SoundEvents.UI_STONECUTTER_TAKE_RESULT,
                SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider reg) {
        super.saveAdditional(tag, reg);
        tag.put("Inventory", inventory.serializeNBT(reg));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider reg) {
        super.loadAdditional(tag, reg);
// deserializeNBT 会按其 "Size" 重建槽位列表，需先把 Size 归一化为当前槽位数再反序列化；
// 若在其之后再调用 setSize，会把刚反序列化出来的物品整体清空（setSize 是换成全新的空列表）
        CompoundTag inventoryTag = tag.getCompound("Inventory").copy();
        inventoryTag.putInt("Size", 1);
        inventory.deserializeNBT(reg, inventoryTag);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider reg) {
        CompoundTag t = new CompoundTag();
        saveAdditional(t, reg);
        return t;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider reg) {
        loadAdditional(pkt.getTag(), reg);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
}
