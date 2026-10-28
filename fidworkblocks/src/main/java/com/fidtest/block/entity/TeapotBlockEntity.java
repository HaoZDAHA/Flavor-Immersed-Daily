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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeapotBlockEntity extends BlockEntity {

    public static final int INPUT_SLOT_0 = 0;
    public static final int INPUT_SLOT_1 = 1;
    public static final int INPUT_SLOT_2 = 2;
    public static final int INPUT_SLOT_3 = 3;
    public static final int OUTPUT_SLOT = 4;
    public static final int TOTAL_SLOTS = 5;
    public static final int DEFAULT_PROCESS_TIME = 400;

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

    public TeapotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TEAPOT.get(), pos, state);
    }

    public ItemStackHandler getInventory() { return inventory; }
    public boolean isSealed() { return sealed; }
    public boolean isWorking() { return working; }
    public int getProcessTick() { return processTick; }
    public int getTotalProcessTime() { return totalProcessTime; }
    public List<ItemStack> getRenderInputItems() { return renderInputItems; }
    public boolean isProcessingDone() { return processingDone; }

    public boolean hasAnyInput() {
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) return true;
        }
        return false;
    }

    public boolean hasOutput() {
        return !inventory.getStackInSlot(OUTPUT_SLOT).isEmpty() || pendingResultTotal > 0;
    }

    public ItemStack getInputStack(int index) {
        if (index < 0 || index > 3) return ItemStack.EMPTY;
        return inventory.getStackInSlot(index);
    }

    public ItemStack getOutputItem() {
        return inventory.getStackInSlot(OUTPUT_SLOT);
    }

    public ItemStack addItemToNextSlot(ItemStack stack) {
// 只放入空槽位，绝不与其他槽位堆叠合并，避免配方因合并而错乱
        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                return inventory.insertItem(i, stack, false);
            }
        }
        return stack;
    }

    public ItemStack removeLastInput() {
        for (int i = INPUT_SLOT_3; i >= INPUT_SLOT_0; i--) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return inventory.extractItem(i, 64, false);
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean checkRecipe() {
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
            if (!app.isEmpty() && !app.equals("teapot")) continue;
            if (recipe.matchesAccurate(recipeInput, level)) {
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
            if (!app.isEmpty() && !app.equals("teapot")) continue;
            if (recipe.matchesAccurate(recipeInput, level)) {
// 记录本批要处理的材料（保留真实数量，供 HUD 显示）
                List<ItemStack> renderList = new ArrayList<>();
                for (ItemStack inp : inputs) {
                    renderList.add(inp.copy());
                }
                renderInputItems = renderList;

                resultTemplate = recipe.getCookedResult(inputs);
                pendingResultTotal += resultTemplate.getCount();
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

// 返还账本中尚未落地的产物（防止满一组时产物滞留/丢失）
        for (ItemStack stack : extractPendingResults()) {
            if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
            }
        }

        for (int i = INPUT_SLOT_0; i <= INPUT_SLOT_3; i++) {
            ItemStack stack = inventory.extractItem(i, 64, false);
            if (!stack.isEmpty()) {
                if (!player.getInventory().add(stack)) {
                    player.drop(stack, false);
                }
            }
        }

        ItemStack outputStack = inventory.extractItem(OUTPUT_SLOT, 64, false);
        if (!outputStack.isEmpty()) {
            if (!player.getInventory().add(outputStack)) {
                player.drop(outputStack, false);
            }
        }

        setChanged();
    }

// 取出账本（pendingResultTotal）中尚未落地的产物。
// 用于取消密封、破坏方块等场景，确保这些产物被返还/掉落，绝不丢失。
    public List<ItemStack> extractPendingResults() {
        List<ItemStack> out = new ArrayList<>();
        if (pendingResultTotal <= 0 || resultTemplate.isEmpty()) {
            pendingResultTotal = 0;
            return out;
        }
        int max = resultTemplate.getMaxStackSize();
        while (pendingResultTotal > 0) {
            int give = Math.min(pendingResultTotal, max);
            ItemStack stack = resultTemplate.copy();
            stack.setCount(give);
            pendingResultTotal -= give;
            out.add(stack);
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
                    if (outSlot.isEmpty()) {
                        ItemStack newStack = resultTemplate.copy();
                        newStack.setCount(put);
                        inventory.setStackInSlot(OUTPUT_SLOT, newStack);
                    } else {
                        outSlot.grow(put);
                    }
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
            be.working = false;
            be.processTick = 0;

// 本批产物的数量在 startProcessing 时已计入账本，这里只负责落地到输出槽
// （账本单槽放不下的部分继续保留，等玩家取出；切勿再次累加，否则每批会出两份）

// 尽量把账本中的产物写入输出槽（单槽不超过一组，防止溢出导致存档/同步崩溃）
            if (be.pendingResultTotal > 0 && !be.resultTemplate.isEmpty()) {
                ItemStack outSlot = be.inventory.getStackInSlot(OUTPUT_SLOT);
                if (outSlot.isEmpty() || ItemStack.isSameItem(outSlot, be.resultTemplate)) {
                    int maxStack = be.resultTemplate.getMaxStackSize();
                    int space = outSlot.isEmpty() ? maxStack : maxStack - outSlot.getCount();
                    int put = Math.min(be.pendingResultTotal, space);
                    if (put > 0) {
                        if (outSlot.isEmpty()) {
                            ItemStack newStack = be.resultTemplate.copy();
                            newStack.setCount(put);
                            be.inventory.setStackInSlot(OUTPUT_SLOT, newStack);
                        } else {
                            outSlot.grow(put);
                        }
                        be.pendingResultTotal -= put;
                    }
                }
            }

// 输出槽已满（或账本仍有剩余放不下）：停止工作，等玩家取出后重新密封继续，剩余产物保留在账本中不丢失
            boolean outputFull = !be.inventory.getStackInSlot(OUTPUT_SLOT).isEmpty()
                    && be.inventory.getStackInSlot(OUTPUT_SLOT).getCount()
                        >= (be.resultTemplate.isEmpty() ? 64 : be.resultTemplate.getMaxStackSize());
            if (be.pendingResultTotal > 0 || outputFull) {
                be.processingDone = true;
                be.renderInputItems = new ArrayList<>();
            } else if (be.pendingResultTotal == 0) {
                if (be.startProcessing()) {
// 自动开始下一批
                } else {
                    be.processingDone = true;
                    be.renderInputItems = new ArrayList<>();
                }
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
            renderList.add(s.copy().saveOptional(reg));
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
// 防御：修复历史存档中超过 maxStackSize 的非法堆叠（如 128 个可乐），防止再次崩服
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack s = inventory.getStackInSlot(i);
            if (!s.isEmpty() && s.getCount() > s.getMaxStackSize()) {
                s.setCount(s.getMaxStackSize());
            }
        }
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
