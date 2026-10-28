package com.fidtest.block.entity;

import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.CookingRecipeInput;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MeatmincerBlockEntity extends BlockEntity {

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public static final int TOTAL_SLOTS = 2;
    public static final int DEFAULT_PROCESS_TIME = 400;

    private static final ResourceLocation COVER_ID =
            ResourceLocation.fromNamespaceAndPath("flavor_immersed_daily", "mincer_cover");

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

    private boolean sealed = false;
    private boolean working = false;
    private int processTick = 0;
    private int totalProcessTime = DEFAULT_PROCESS_TIME;
    private List<ItemStack> renderInputItems = new ArrayList<>();
    private ItemStack resultTemplate = ItemStack.EMPTY;
    private int pendingResultTotal = 0;
    private boolean processingDone = false;

    public MeatmincerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MEATMINCER.get(), pos, state);
    }

    public ItemStackHandler getInventory() { return inventory; }
    public boolean isSealed() { return sealed; }
    public boolean isWorking() { return working; }
    public int getProcessTick() { return processTick; }
    public int getTotalProcessTime() { return totalProcessTime; }
    public List<ItemStack> getRenderInputItems() { return renderInputItems; }
    public boolean isProcessingDone() { return processingDone; }

    public boolean hasAnyInput() {
        return !inventory.getStackInSlot(INPUT_SLOT).isEmpty();
    }

    public boolean hasOutput() {
        return !inventory.getStackInSlot(OUTPUT_SLOT).isEmpty() || pendingResultTotal > 0;
    }

    public ItemStack getInputStack() {
        return inventory.getStackInSlot(INPUT_SLOT);
    }

    public ItemStack getOutputItem() {
        return inventory.getStackInSlot(OUTPUT_SLOT);
    }

    public ItemStack addItemToSlot(ItemStack stack) {
        return inventory.insertItem(INPUT_SLOT, stack, false);
    }

    public ItemStack removeInput() {
        return inventory.extractItem(INPUT_SLOT, 64, false);
    }

    public boolean checkRecipe() {
        if (level == null || level.isClientSide) return false;

        ItemStack input = inventory.getStackInSlot(INPUT_SLOT);
        if (input.isEmpty()) return false;

        List<ItemStack> inputs = List.of(input.copy());
        CookingRecipeInput recipeInput = new CookingRecipeInput(inputs);
        List<RecipeHolder<CookingRecipe>> recipes =
                ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get());

        for (RecipeHolder<CookingRecipe> holder : recipes) {
            CookingRecipe recipe = holder.value();
            String app = recipe.getAppliance();
            if (!app.isEmpty() && !app.equals("meatmincer")) continue;
            if (recipe.matches(recipeInput, level)) {
                return true;
            }
        }
        return false;
    }

    public void seal() {
        sealed = true;
        if (!startProcessing()) {
            sealed = false;
        }
    }

    private boolean startProcessing() {
        if (level == null || level.isClientSide) return false;

        ItemStack input = inventory.getStackInSlot(INPUT_SLOT);
        if (input.isEmpty()) return false;

        List<ItemStack> inputs = List.of(input.copy());
        CookingRecipeInput recipeInput = new CookingRecipeInput(inputs);
        List<RecipeHolder<CookingRecipe>> recipes =
                ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get());

        for (RecipeHolder<CookingRecipe> holder : recipes) {
            CookingRecipe recipe = holder.value();
            String app = recipe.getAppliance();
            if (!app.isEmpty() && !app.equals("meatmincer")) continue;
            if (recipe.matches(recipeInput, level)) {
                List<ItemStack> renderList = new ArrayList<>();
                ItemStack copy = input.copy();
                copy.setCount(1);
                renderList.add(copy);
                renderInputItems = renderList;

                resultTemplate = recipe.getCookedResult(inputs);
                pendingResultTotal = resultTemplate.getCount();
                totalProcessTime = recipe.getCookingTime() > 0 ? recipe.getCookingTime() : DEFAULT_PROCESS_TIME;

                working = true;
                processTick = 0;
                processingDone = false;

                for (int ingIdx = 0; ingIdx < recipe.getIngredients().size(); ingIdx++) {
                    Ingredient ing = recipe.getIngredients().get(ingIdx);
                    int recipeCost = 1;
                    for (ItemStack testStack : ing.getItems()) {
                        recipeCost = Math.max(recipeCost, testStack.getCount());
                    }
                    int toExtract = recipeCost;

                    ItemStack slotStack = inventory.getStackInSlot(INPUT_SLOT);
                    if (ing.test(slotStack)) {
                        int extracted = Math.min(toExtract, slotStack.getCount());
                        inventory.extractItem(INPUT_SLOT, extracted, false);
                        toExtract -= extracted;
                    }
                }

                setChanged();
                level.playSound(null, worldPosition, SoundEvents.BREWING_STAND_BREW,
                        SoundSource.BLOCKS, 0.5F, 1.0F);
                return true;
            }
        }
        return false;
    }

    public void unseal(Player player) {
        sealed = false;
        working = false;
        processTick = 0;
        renderInputItems = new ArrayList<>();
        pendingResultTotal = 0;
        resultTemplate = ItemStack.EMPTY;

        ItemStack inputStack = inventory.extractItem(INPUT_SLOT, 64, false);
        if (!inputStack.isEmpty() && !player.isCreative()) {
            if (!player.getInventory().add(inputStack)) {
                player.drop(inputStack, false);
            }
        }

        ItemStack outputStack = inventory.extractItem(OUTPUT_SLOT, 64, false);
        if (!outputStack.isEmpty() && !player.isCreative()) {
            if (!player.getInventory().add(outputStack)) {
                player.drop(outputStack, false);
            }
        }

        ItemStack cover = getCoverDrop();
        if (!player.isCreative()) {
            if (!player.getInventory().add(cover)) {
                player.drop(cover, false);
            }
        }

        setChanged();
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

    public ItemStack getCoverDrop() {
        ItemStack cover = new ItemStack(BuiltInRegistries.ITEM.get(COVER_ID));
        cover.setCount(1);
        return cover;
    }

    public static void serverTick(MeatmincerBlockEntity be) {
        if (!be.working) return;
        be.processTick++;

        if (be.processTick >= be.totalProcessTime) {
            be.working = false;
            be.processTick = 0;

            ItemStack outSlot = be.inventory.getStackInSlot(OUTPUT_SLOT);
            int maxStack = be.resultTemplate.getMaxStackSize();
            int total = be.pendingResultTotal;

            if (!outSlot.isEmpty() && ItemStack.isSameItem(outSlot, be.resultTemplate)) {
                int canAdd = maxStack - outSlot.getCount();
                int toAdd = Math.min(total, canAdd);
                outSlot.grow(toAdd);
                total -= toAdd;
            }

            if (total > 0 && (outSlot.isEmpty() || ItemStack.isSameItem(outSlot, be.resultTemplate))) {
                int put = Math.min(total, maxStack);
                if (outSlot.isEmpty()) {
                    ItemStack newStack = be.resultTemplate.copy();
                    newStack.setCount(put);
                    be.inventory.setStackInSlot(OUTPUT_SLOT, newStack);
                } else {
                    outSlot.grow(put);
                }
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

            if (be.startProcessing()) {
            } else {
                be.processingDone = true;
                be.renderInputItems = new ArrayList<>();
            }

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
        tag.putBoolean("Sealed", sealed);
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
        sealed = tag.getBoolean("Sealed");
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
