package com.fidtest.block.entity;

import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.CookingRecipeInput;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SteamboxBlockEntity extends BlockEntity {

    public static final int INPUT_SLOT_0 = 0;
    public static final int INPUT_SLOT_3 = 3;
    public static final int OUTPUT_SLOT = 4;
    public static final int TOTAL_SLOTS = 5;
    public static final int DEFAULT_PROCESS_TIME = 200;

    private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS) {
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
    private int totalProcessTime = DEFAULT_PROCESS_TIME;
    private List<ItemStack> renderInputItems = new ArrayList<>();
    private ItemStack resultTemplate = ItemStack.EMPTY;
    private int pendingResultTotal = 0;

    public SteamboxBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STEAMBOX.get(), pos, state);
    }

    public ItemStackHandler getInventory() { return inventory; }
    public boolean isWorking() { return working; }
    public int getProcessTick() { return processTick; }
    public int getTotalProcessTime() { return totalProcessTime; }
    public List<ItemStack> getRenderInputItems() { return renderInputItems; }

    public void setTotalProcessTime(int time) {
        this.totalProcessTime = time;
        setChanged();
    }

    public boolean hasAnyInput() {
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) return true;
        }
        return false;
    }

    public boolean hasOutput() {
        return !inventory.getStackInSlot(OUTPUT_SLOT).isEmpty() || pendingResultTotal > 0;
    }

    public List<ItemStack> getInputItems() {
        List<ItemStack> items = new ArrayList<>();
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) {
            ItemStack s = inventory.getStackInSlot(i);
            if (!s.isEmpty()) items.add(s.copy());
        }
        return items;
    }

    public List<ItemStack> getAllItems() {
        List<ItemStack> all = new ArrayList<>();
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) {
            ItemStack s = inventory.getStackInSlot(i);
            if (!s.isEmpty()) all.add(s.copy());
        }
        ItemStack out = inventory.getStackInSlot(OUTPUT_SLOT);
        if (!out.isEmpty()) all.add(out.copy());
        return all;
    }

    public void flushPendingResults() {
        if (pendingResultTotal <= 0 || resultTemplate.isEmpty()) return;
        ItemStack outSlot = inventory.getStackInSlot(OUTPUT_SLOT);
        int maxStack = resultTemplate.getMaxStackSize();
        int total = pendingResultTotal;

        if (!outSlot.isEmpty() && ItemStack.isSameItem(outSlot, resultTemplate)) {
            int canAdd = maxStack - outSlot.getCount();
            int toAdd = Math.min(total, canAdd);
            outSlot.grow(toAdd);
            total -= toAdd;
        }

        if (total > 0 && (outSlot.isEmpty() || ItemStack.isSameItem(outSlot, resultTemplate))) {
            int put = Math.min(total, maxStack);
            if (outSlot.isEmpty()) {
                ItemStack newStack = resultTemplate.copy();
                newStack.setCount(put);
                inventory.setStackInSlot(OUTPUT_SLOT, newStack);
            } else {
                outSlot.grow(put);
            }
            total -= put;
        }

        while (total > 0) {
            int dropCount = Math.min(total, maxStack);
            ItemStack dropStack = resultTemplate.copy();
            dropStack.setCount(dropCount);
            if (level != null) {
                net.minecraft.world.entity.item.ItemEntity entity =
                        new net.minecraft.world.entity.item.ItemEntity(level,
                                worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, dropStack);
                entity.setDefaultPickUpDelay();
                level.addFreshEntity(entity);
            }
            total -= dropCount;
        }
        pendingResultTotal = 0;
        resultTemplate = ItemStack.EMPTY;
        setChanged();
    }

    public boolean canAcceptItem(ItemStack held) {
        if (level == null || level.isClientSide) return true;
        int totalCount = 0;
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) {
            totalCount += inventory.getStackInSlot(i).getCount();
        }
        if (totalCount >= 4) return false;
        return isValidSteamIngredient(held);
    }

    private boolean isValidSteamIngredient(ItemStack held) {
        if (level == null || level.isClientSide) return true;
        List<ItemStack> testItems = new ArrayList<>();
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) {
            ItemStack s = inventory.getStackInSlot(i);
            if (!s.isEmpty()) testItems.add(s.copy());
        }
        testItems.add(held.copy());
        CookingRecipeInput input = new CookingRecipeInput(testItems);
        return ((ServerLevel) level).getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.COOKING.get())
                .stream()
                .filter(h -> {
                    String app = h.value().getAppliance();
                    return app.isEmpty() || app.equals("steambox");
                })
                .anyMatch(h -> h.value().matches(input, level));
    }

    public int addItems(ItemStack held) {
        if (level == null) return 0;

        int totalCount = 0;
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) {
            totalCount += inventory.getStackInSlot(i).getCount();
        }
        int space = 4 - totalCount;
        if (space <= 0) return 0;

        if (!isValidSteamIngredient(held)) return 0;

        int toAdd = Math.min(held.getCount(), space);
        ItemStack toInsert = held.copy();
        toInsert.setCount(toAdd);
        ItemStack remainder = addItemToNextSlot(toInsert);
        int added = toAdd - remainder.getCount();
        if (added > 0) {
            held.shrink(added);
            syncToClient();
        }
        return added;
    }

    private ItemStack addItemToNextSlot(ItemStack stack) {
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) {
            ItemStack existing = inventory.getStackInSlot(i);
            if (!existing.isEmpty() && ItemStack.isSameItemSameComponents(existing, stack)
                    && existing.getCount() < existing.getMaxStackSize()) {
                return inventory.insertItem(i, stack, false);
            }
        }
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                return inventory.insertItem(i, stack, false);
            }
        }
        return stack;
    }

    public void setPacked(boolean packed) {
        syncToClient();
    }

    public boolean startProcessing() {
        if (level == null || level.isClientSide) return false;

        List<ItemStack> inputs = new ArrayList<>();
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) {
            ItemStack s = inventory.getStackInSlot(i);
            if (!s.isEmpty()) inputs.add(s.copy());
        }
        if (inputs.isEmpty()) return false;

        CookingRecipeInput recipeInput = new CookingRecipeInput(inputs);
        List<RecipeHolder<CookingRecipe>> recipes =
                ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get());

        for (RecipeHolder<CookingRecipe> holder : recipes) {
            CookingRecipe recipe = holder.value();
            String app = recipe.getAppliance();
            if (!app.isEmpty() && !app.equals("steambox")) continue;
            if (!recipe.matches(recipeInput, level)) continue;

            List<Ingredient> ingredients = recipe.getIngredients();
            int batches = Integer.MAX_VALUE;
            List<ItemStack> matchedItems = new ArrayList<>();
            List<Integer> matchedCosts = new ArrayList<>();

            for (Ingredient ing : ingredients) {
                int ingCost = Integer.MAX_VALUE;
                ItemStack bestMatch = ItemStack.EMPTY;
                for (ItemStack option : ing.getItems()) {
                    int totalCount = 0;
                    for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) {
                        ItemStack slotStack = inventory.getStackInSlot(i);
                        if (ing.test(slotStack)) {
                            totalCount += slotStack.getCount();
                        }
                    }
                    if (totalCount > 0) {
                        int optionCost = totalCount / option.getCount();
                        if (optionCost > 0 && optionCost < ingCost) {
                            ingCost = optionCost;
                            bestMatch = option;
                        }
                    }
                }
                if (ingCost == Integer.MAX_VALUE || bestMatch.isEmpty()) {
                    batches = 0;
                    break;
                }
                batches = Math.min(batches, ingCost);
                matchedItems.add(bestMatch);
                matchedCosts.add(bestMatch.getCount());
            }

            if (batches <= 0) continue;

            List<ItemStack> renderList = new ArrayList<>();
            int renderCount = Math.min(batches, 5);
            for (int r = 0; r < renderCount; r++) {
                ItemStack single = inputs.get(r % inputs.size()).copy();
                single.setCount(1);
                renderList.add(single);
            }
            renderInputItems = renderList;

            resultTemplate = recipe.getCookedResult(inputs);
            pendingResultTotal = resultTemplate.getCount() * batches;
            totalProcessTime = recipe.getCookingTime() > 0 ? recipe.getCookingTime() : DEFAULT_PROCESS_TIME;

            working = true;
            processTick = 0;

            for (int ingIdx = 0; ingIdx < ingredients.size(); ingIdx++) {
                Ingredient ing = ingredients.get(ingIdx);
                int costPerBatch = matchedCosts.get(ingIdx);
                int toExtract = costPerBatch * batches;

                for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3 && toExtract > 0; i++) {
                    ItemStack slotStack = inventory.getStackInSlot(i);
                    if (ing.test(slotStack)) {
                        int extracted = Math.min(toExtract, slotStack.getCount());
                        inventory.extractItem(i, extracted, false);
                        toExtract -= extracted;
                    }
                }
            }

            setChanged();
            level.playSound(null, worldPosition, SoundEvents.LAVA_EXTINGUISH,
                    SoundSource.BLOCKS, 1.0F, 0.8F);
            return true;
        }
        return false;
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
        return inventory.extractItem(OUTPUT_SLOT, 64, false);
    }

    public void clearItems() {
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
        resetWorking();
    }

    public void resetWorking() {
        working = false;
        processTick = 0;
        renderInputItems = new ArrayList<>();
        pendingResultTotal = 0;
        resultTemplate = ItemStack.EMPTY;
    }

    public void saveToItemStack(ItemStack stack) {
        CompoundTag root = new CompoundTag();
        CompoundTag steambox = new CompoundTag();

        ListTag inputList = new ListTag();
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) {
            ItemStack s = inventory.getStackInSlot(i);
            if (!s.isEmpty()) {
                inputList.add(s.save(level.registryAccess()));
            }
        }
        steambox.put("Input", inputList);

        ListTag outputList = new ListTag();
        ItemStack out = inventory.getStackInSlot(OUTPUT_SLOT);
        if (!out.isEmpty()) {
            outputList.add(out.save(level.registryAccess()));
        }
        steambox.put("Output", outputList);

        steambox.putInt("PendingResultTotal", pendingResultTotal);
        if (!resultTemplate.isEmpty()) {
            steambox.put("ResultTemplate", resultTemplate.saveOptional(level.registryAccess()));
        }

        root.put("SteamboxData", steambox);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(root));
        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
    }

    public void loadFromItemStack(ItemStack stack) {
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
        try {
            CustomData custom = stack.get(DataComponents.CUSTOM_DATA);
            if (custom != null) {
                CompoundTag root = custom.copyTag();
                if (root.contains("SteamboxData")) {
                    CompoundTag data = root.getCompound("SteamboxData");
                    ListTag inputList = data.getList("Input", 10);
                    for (int i = 0; i < inputList.size() && i < 4; i++) {
                        inventory.setStackInSlot(INPUT_SLOT_0 + i,
                                ItemStack.parse(level.registryAccess(), inputList.getCompound(i)).orElse(ItemStack.EMPTY));
                    }
                    ListTag outputList = data.getList("Output", 10);
                    if (!outputList.isEmpty()) {
                        inventory.setStackInSlot(OUTPUT_SLOT,
                                ItemStack.parse(level.registryAccess(), outputList.getCompound(0)).orElse(ItemStack.EMPTY));
                    }
                    pendingResultTotal = data.getInt("PendingResultTotal");
                    if (data.contains("ResultTemplate")) {
                        resultTemplate = ItemStack.parseOptional(level.registryAccess(), data.getCompound("ResultTemplate"));
                    } else {
                        resultTemplate = ItemStack.EMPTY;
                    }
                }
            }
        } catch (Exception ignored) {}
        syncToClient();
    }

    public static void serverTick(SteamboxBlockEntity be) {
        if (!be.working) return;
        be.processTick++;

        if (be.processTick >= be.totalProcessTime) {
            be.working = false;
            be.processTick = 0;

            if (be.level != null && !be.level.isClientSide) {
                be.level.setBlock(be.worldPosition,
                        be.getBlockState().setValue(com.fidtest.block.SteamboxBlock.WORKING, false), 3);
            }

            be.flushPendingResults();
            be.renderInputItems = new ArrayList<>();
            be.setChanged();

            if (be.getBlockState().getValue(com.fidtest.block.SteamboxBlock.BOTTOM)) {
                BlockPos check = be.worldPosition.above();
                while (be.level.getBlockState(check).is(com.fidtest.registration.ModBlocks.STEAMBOX.get())) {
                    if (be.level.getBlockEntity(check) instanceof SteamboxBlockEntity aboveBe && aboveBe.working) {
                        aboveBe.finishProcessing();
                        if (be.level != null && !be.level.isClientSide) {
                            be.level.setBlock(check,
                                    be.level.getBlockState(check).setValue(com.fidtest.block.SteamboxBlock.WORKING, false), 3);
                        }
                    }
                    check = check.above();
                }
            }

            if (be.level != null) {
                be.level.playSound(null, be.worldPosition, SoundEvents.UI_STONECUTTER_TAKE_RESULT,
                        SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    private void finishProcessing() {
        if (!working) return;
        working = false;
        processTick = 0;

        ItemStack outSlot = inventory.getStackInSlot(OUTPUT_SLOT);
        int maxStack = resultTemplate.getMaxStackSize();
        int total = pendingResultTotal;

        if (!outSlot.isEmpty() && ItemStack.isSameItem(outSlot, resultTemplate)) {
            int canAdd = maxStack - outSlot.getCount();
            int toAdd = Math.min(total, canAdd);
            outSlot.grow(toAdd);
            total -= toAdd;
        }

        if (total > 0 && (outSlot.isEmpty() || ItemStack.isSameItem(outSlot, resultTemplate))) {
            int put = Math.min(total, maxStack);
            if (outSlot.isEmpty()) {
                ItemStack newStack = resultTemplate.copy();
                newStack.setCount(put);
                inventory.setStackInSlot(OUTPUT_SLOT, newStack);
            } else {
                outSlot.grow(put);
            }
            total -= put;
        }

        while (total > 0) {
            int dropCount = Math.min(total, maxStack);
            ItemStack dropStack = resultTemplate.copy();
            dropStack.setCount(dropCount);
            net.minecraft.world.entity.item.ItemEntity entity =
                    new net.minecraft.world.entity.item.ItemEntity(level,
                            worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, dropStack);
            entity.setDefaultPickUpDelay();
            level.addFreshEntity(entity);
            total -= dropCount;
        }

        pendingResultTotal = 0;
        renderInputItems = new ArrayList<>();
        setChanged();
    }

    public CompoundTag saveBEData(HolderLookup.Provider reg) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, reg);
        return tag;
    }

    public void loadBEData(CompoundTag tag, HolderLookup.Provider reg) {
        loadAdditional(tag, reg);
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider reg) {
        super.saveAdditional(tag, reg);
        tag.put("Inventory", inventory.serializeNBT(reg));
        tag.putBoolean("Working", working);
        tag.putInt("ProcessTick", processTick);
        tag.putInt("TotalProcessTime", totalProcessTime);
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
        inventoryTag.putInt("Size", TOTAL_SLOTS);
        inventory.deserializeNBT(reg, inventoryTag);
        working = tag.getBoolean("Working");
        processTick = tag.getInt("ProcessTick");
        totalProcessTime = tag.getInt("TotalProcessTime");
        if (totalProcessTime <= 0) totalProcessTime = DEFAULT_PROCESS_TIME;
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

    @Override public CompoundTag getUpdateTag(HolderLookup.Provider reg) { CompoundTag t = new CompoundTag(); saveAdditional(t, reg); return t; }
    @Nullable @Override public ClientboundBlockEntityDataPacket getUpdatePacket() {
        try {
            return ClientboundBlockEntityDataPacket.create(this);
        } catch (Exception e) {
            return null;
        }
    }
    @Override public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider reg) { loadAdditional(pkt.getTag(), reg); }

    private void syncToClient() {
        setChanged();
        if (level != null) {
            try {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            } catch (Exception ignored) {
            }
        }
    }
}
