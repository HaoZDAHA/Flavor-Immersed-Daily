package com.fidtest.block.entity;

import com.fidtest.block.DryerBlock;
import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.CookingRecipeInput;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FryingpanBlockEntity extends BlockEntity {

    public static final int OIL_SLOT = 0;
    public static final int INGREDIENT_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;
    public static final int DEFAULT_FRY_TIME = 60;

    private static final ResourceLocation OIL_ID =
            ResourceLocation.fromNamespaceAndPath("flavor_immersed_daily", "cookingoil");

    private final ItemStackHandler inventory = new ItemStackHandler(3) {
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
    private int totalProcessTime = DEFAULT_FRY_TIME;
    private List<ItemStack> renderInputItems = new ArrayList<>();
    private ItemStack resultTemplate = ItemStack.EMPTY;
    private int pendingResultTotal = 0;

    public FryingpanBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FRYINGPAN.get(), pos, state);
    }

    public ItemStackHandler getInventory() { return inventory; }

    public boolean isWorking() { return working; }
    public int getProcessTick() { return processTick; }
    public int getTotalProcessTime() { return totalProcessTime; }
    public List<ItemStack> getRenderInputItems() { return renderInputItems; }

    public boolean hasOil() { return !inventory.getStackInSlot(OIL_SLOT).isEmpty(); }
    public boolean hasIngredient() { return !inventory.getStackInSlot(INGREDIENT_SLOT).isEmpty(); }
    public boolean hasOutput() { return !inventory.getStackInSlot(OUTPUT_SLOT).isEmpty() || pendingResultTotal > 0; }

    public int getOilCount() { return inventory.getStackInSlot(OIL_SLOT).getCount(); }

    public ItemStack getOilItem() { return inventory.getStackInSlot(OIL_SLOT); }
    public ItemStack getIngredientItem() { return inventory.getStackInSlot(INGREDIENT_SLOT); }
    public ItemStack getOutputItem() { return inventory.getStackInSlot(OUTPUT_SLOT); }

    public boolean isOilItem(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem().builtInRegistryHolder().key().location().equals(OIL_ID);
    }

    public boolean isRecipeIngredient(ItemStack stack) {
        if (level == null || level.isClientSide || stack.isEmpty()) return false;
        List<RecipeHolder<CookingRecipe>> recipes =
                ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get());
        for (RecipeHolder<CookingRecipe> holder : recipes) {
            CookingRecipe recipe = holder.value();
            String app = recipe.getAppliance();
            if (!app.isEmpty() && !app.equals("fryingpan")) continue;
            for (Ingredient ing : recipe.getIngredients()) {
                if (ing.test(stack)) return true;
            }
        }
        return false;
    }

    public ItemStack addOil(ItemStack stack) {
        return inventory.insertItem(OIL_SLOT, stack, false);
    }

    public ItemStack addIngredient(ItemStack stack) {
        return inventory.insertItem(INGREDIENT_SLOT, stack, false);
    }

    public ItemStack removeOil() {
        return inventory.extractItem(OIL_SLOT, 64, false);
    }

    public ItemStack removeIngredient() {
        return inventory.extractItem(INGREDIENT_SLOT, 64, false);
    }

    public boolean isOnDryer() {
        if (level == null) return false;
        return level.getBlockState(worldPosition.below()).getBlock() instanceof DryerBlock;
    }

    public boolean startFrying() {
        if (level == null || level.isClientSide || working) return false;
        if (!hasIngredient() || !hasOil()) return false;
        if (!isOnDryer()) return false;

        int oilCount = getOilCount();
        ItemStack ingredientCopy = getIngredientItem().copy();
        int ingredientCount = ingredientCopy.getCount();

        if (oilCount < ingredientCount) return false;

        CookingRecipeInput input = new CookingRecipeInput(List.of(ingredientCopy.copy()));

        List<RecipeHolder<CookingRecipe>> recipes =
                ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get());

        for (RecipeHolder<CookingRecipe> holder : recipes) {
            CookingRecipe recipe = holder.value();
            String app = recipe.getAppliance();
            if (!app.isEmpty() && !app.equals("fryingpan")) continue;
            if (recipe.matches(input, level)) {
                int recipeCost = 1;
                for (Ingredient ing : recipe.getIngredients()) {
                    for (ItemStack testStack : ing.getItems()) {
                        if (testStack.getItem() == ingredientCopy.getItem()) {
                            recipeCost = Math.max(recipeCost, testStack.getCount());
                            break;
                        }
                    }
                }

                int batches = ingredientCount / recipeCost;
                if (batches <= 0) return false;
                batches = Math.min(batches, oilCount);

                List<ItemStack> renderList = new ArrayList<>();
                ItemStack singleItem = ingredientCopy.copy();
                singleItem.setCount(1);
                int renderCount = Math.min(batches, 5);
                for (int r = 0; r < renderCount; r++) {
                    renderList.add(singleItem.copy());
                }
                renderInputItems = renderList;

                resultTemplate = recipe.getCookedResult(List.of(ingredientCopy));
                pendingResultTotal = resultTemplate.getCount() * batches;
                totalProcessTime = recipe.getCookingTime() > 0 ? recipe.getCookingTime() : DEFAULT_FRY_TIME;

                working = true;
                processTick = 0;
                inventory.extractItem(INGREDIENT_SLOT, batches * recipeCost, false);
                inventory.extractItem(OIL_SLOT, batches, false);

                setChanged();
                level.playSound(null, worldPosition, SoundEvents.LAVA_EXTINGUISH,
                        SoundSource.BLOCKS, 0.5F, 1.0F);
                return true;
            }
        }
        return false;
    }

    public void collectOutput(Player player) {
        if (level == null || level.isClientSide) return;

        ItemStack result;
        if (pendingResultTotal > 0) {
            int give = Math.min(pendingResultTotal, resultTemplate.getMaxStackSize());
            result = resultTemplate.copy();
            result.setCount(give);
            pendingResultTotal -= give;
        } else {
            result = inventory.extractItem(OUTPUT_SLOT, 64, false);
        }

        if (!result.isEmpty()) {
            if (!player.isCreative()) {
                player.getInventory().add(result);
            }
            int remainingOil = getOilCount();
            if (remainingOil > 0) {
                player.displayClientMessage(
                        Component.translatable("message.fidworkblock.fryingpan_oil_remaining", remainingOil), true);
            } else {
                player.displayClientMessage(
                        Component.translatable("message.fidworkblock.fryingpan_no_oil"), true);
            }
        }
        setChanged();
    }

    public static void serverTick(FryingpanBlockEntity be) {
        if (!be.working) return;
        be.processTick++;

        if (be.level instanceof ServerLevel serverLevel && be.processTick % 8 == 0) {
            double x = be.worldPosition.getX() + 0.5;
            double y = be.worldPosition.getY() + 0.3;
            double z = be.worldPosition.getZ() + 0.5;
            serverLevel.sendParticles(ParticleTypes.LAVA,
                    x, y, z, 4, 0.2, 0.05, 0.2, 0.02);
        }

        if (be.processTick % 20 == 0 && be.level != null) {
            be.level.playSound(null, be.worldPosition, SoundEvents.LAVA_EXTINGUISH,
                    SoundSource.BLOCKS, 0.3F, 1.0F + (be.level.random.nextFloat() * 0.2F));
        }

        if (be.processTick >= be.totalProcessTime) {
            be.working = false;
            be.processTick = 0;
            be.renderInputItems = new ArrayList<>();

            ItemStack outSlot = be.inventory.getStackInSlot(OUTPUT_SLOT);
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
                be.inventory.setStackInSlot(OUTPUT_SLOT, newStack);
                total -= put;
            }

            while (total > 0) {
                int dropCount = Math.min(total, maxStack);
                ItemStack dropStack = be.resultTemplate.copy();
                dropStack.setCount(dropCount);
                double dx = be.worldPosition.getX() + 0.5;
                double dy = be.worldPosition.getY() + 0.5;
                double dz = be.worldPosition.getZ() + 0.5;
                net.minecraft.world.entity.item.ItemEntity entity =
                        new net.minecraft.world.entity.item.ItemEntity(be.level, dx, dy, dz, dropStack);
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
        inventoryTag.putInt("Size", 3);
        inventory.deserializeNBT(reg, inventoryTag);
        working = tag.getBoolean("Working");
        processTick = tag.getInt("ProcessTick");
        totalProcessTime = tag.getInt("TotalProcessTime");
        if (totalProcessTime <= 0) totalProcessTime = DEFAULT_FRY_TIME;
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
