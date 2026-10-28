package com.fidtest.block.entity;

import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.CookingRecipeInput;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SqueezingmachineBlockEntity extends BlockEntity {

    public static final int TOTAL_PROCESS_TIME = 80;

    private final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }
    };

    private boolean working = false;
    private int processTick = 0;
    private List<ItemStack> renderInputItems = new ArrayList<>();
    private int pendingResultTotal = 0;
    private ItemStack resultTemplate = ItemStack.EMPTY;

    public SqueezingmachineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SQUEEZINGMACHINE.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public boolean hasInput() {
        return !inventory.getStackInSlot(0).isEmpty();
    }

    public boolean hasOutput() {
        return !inventory.getStackInSlot(1).isEmpty() || pendingResultTotal > 0;
    }

    public boolean isEmpty() {
        return !hasInput() && !hasOutput() && !working;
    }

    public boolean isWorking() {
        return working;
    }

    public int getProcessTick() {
        return processTick;
    }

    public List<ItemStack> getRenderInputItems() {
        return renderInputItems;
    }

    public ItemStack getStoredResult() {
        return resultTemplate;
    }

    public ItemStack getInputItem() {
        return inventory.getStackInSlot(0);
    }

    public ItemStack getOutputItem() {
        return inventory.getStackInSlot(1);
    }

    public boolean isRecipeItem(ItemStack stack) {
        if (level == null || level.isClientSide || stack.isEmpty()) return false;
        List<RecipeHolder<CookingRecipe>> recipes =
                ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get());
        for (RecipeHolder<CookingRecipe> holder : recipes) {
            CookingRecipe recipe = holder.value();
            String app = recipe.getAppliance();
            if (!app.isEmpty() && !app.equals("squeezingmachine")) continue;
            for (Ingredient ing : recipe.getIngredients()) {
                if (ing.test(stack)) return true;
            }
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
            ItemStack result = resultTemplate.copy();
            result.setCount(give);
            pendingResultTotal -= give;
            setChanged();
            return result;
        }
        return inventory.extractItem(1, 64, false);
    }

    public boolean startProcessing() {
        if (level == null || level.isClientSide || !hasInput() || working) return false;

        ItemStack inputCopy = getInputItem().copy();
        int inputCount = inputCopy.getCount();
        CookingRecipeInput input = new CookingRecipeInput(List.of(inputCopy.copy()));

        List<RecipeHolder<CookingRecipe>> recipes =
                ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get());

        for (RecipeHolder<CookingRecipe> holder : recipes) {
            CookingRecipe recipe = holder.value();
            String app = recipe.getAppliance();
            if (!app.isEmpty() && !app.equals("squeezingmachine")) continue;
            if (recipe.matches(input, level)) {
                int recipeCost = 1;
                for (Ingredient ing : recipe.getIngredients()) {
                    for (ItemStack testStack : ing.getItems()) {
                        if (testStack.getItem() == inputCopy.getItem()) {
                            recipeCost = Math.max(recipeCost, testStack.getCount());
                            break;
                        }
                    }
                }

                int batches = inputCount / recipeCost;
                if (batches <= 0) return false;

                List<ItemStack> renderList = new ArrayList<>();
                ItemStack singleItem = inputCopy.copy();
                singleItem.setCount(1);
                int renderCount = Math.min(batches, 5);
                for (int r = 0; r < renderCount; r++) {
                    renderList.add(singleItem.copy());
                }
                renderInputItems = renderList;

                resultTemplate = recipe.getCookedResult(List.of(inputCopy));
                pendingResultTotal = resultTemplate.getCount() * batches;

                working = true;
                processTick = 0;
                inventory.extractItem(0, batches * recipeCost, false);
                setChanged();
                level.playSound(null, worldPosition, SoundEvents.GENERIC_DRINK,
                        SoundSource.BLOCKS, 1.0F, 1.0F);
                return true;
            }
        }
        return false;
    }

    public static void serverTick(SqueezingmachineBlockEntity be) {
        if (!be.working) return;
        be.processTick++;
        if (be.processTick >= TOTAL_PROCESS_TIME) {
            be.working = false;
            be.processTick = 0;
            be.renderInputItems = new ArrayList<>();

            ItemStack outSlot = be.inventory.getStackInSlot(1);
            int maxStack = be.resultTemplate.getMaxStackSize();
            int total = be.pendingResultTotal;

            if (!outSlot.isEmpty() && ItemStack.isSameItem(outSlot, be.resultTemplate)) {
                int canAdd = maxStack - outSlot.getCount();
                int toAdd = Math.min(total, canAdd);
                outSlot.grow(toAdd);
                total -= toAdd;
            }

            if (total > 0 && outSlot.isEmpty()) {
                int put = Math.min(total, maxStack);
                ItemStack newStack = be.resultTemplate.copy();
                newStack.setCount(put);
                be.inventory.setStackInSlot(1, newStack);
                total -= put;
            }

            while (total > 0) {
                int dropCount = Math.min(total, maxStack);
                ItemStack dropStack = be.resultTemplate.copy();
                dropStack.setCount(dropCount);
                double x = be.worldPosition.getX() + 0.5;
                double y = be.worldPosition.getY() + 0.5;
                double z = be.worldPosition.getZ() + 0.5;
                net.minecraft.world.entity.item.ItemEntity entity =
                        new net.minecraft.world.entity.item.ItemEntity(be.level, x, y, z, dropStack);
                entity.setDefaultPickUpDelay();
                be.level.addFreshEntity(entity);
                total -= dropCount;
            }

            be.pendingResultTotal = 0;
            be.setChanged();
            if (be.level != null) {
                be.level.playSound(null, be.worldPosition, SoundEvents.UI_STONECUTTER_TAKE_RESULT,
                        SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider reg) {
        super.saveAdditional(tag, reg);
        tag.put("Inventory", inventory.serializeNBT(reg));
        tag.putBoolean("Working", working);
        tag.putInt("ProcessTick", processTick);
        tag.putInt("PendingResultTotal", pendingResultTotal);
        if (!resultTemplate.isEmpty()) {
            tag.put("ResultTemplate", resultTemplate.saveOptional(reg));
        }
        ListTag renderList = new ListTag();
        for (ItemStack s : renderInputItems) {
            ItemStack copy = s.copy();
            copy.setCount(1);
            renderList.add(copy.saveOptional(reg));
        }
        tag.put("RenderItems", renderList);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider reg) {
        super.loadAdditional(tag, reg);
// deserializeNBT 会按其 "Size" 重建槽位列表，需先把 Size 归一化为当前槽位数再反序列化；
// 若在其之后再调用 setSize，会把刚反序列化出来的物品整体清空（setSize 是换成全新的空列表）
        CompoundTag inventoryTag = tag.getCompound("Inventory").copy();
        inventoryTag.putInt("Size", 2);
        inventory.deserializeNBT(reg, inventoryTag);
        working = tag.getBoolean("Working");
        processTick = tag.getInt("ProcessTick");
        pendingResultTotal = tag.getInt("PendingResultTotal");
        if (tag.contains("ResultTemplate")) {
            resultTemplate = ItemStack.parseOptional(reg, tag.getCompound("ResultTemplate"));
        } else {
            resultTemplate = ItemStack.EMPTY;
        }
        renderInputItems = new ArrayList<>();
        ListTag renderList = tag.getList("RenderItems", Tag.TAG_COMPOUND);
        for (int i = 0; i < renderList.size(); i++) {
            renderInputItems.add(ItemStack.parseOptional(reg, renderList.getCompound(i)));
        }
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
        try {
            return ClientboundBlockEntityDataPacket.create(this);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider reg) {
        loadAdditional(pkt.getTag(), reg);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null) {
            try {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            } catch (Exception ignored) {
            }
        }
    }
}
