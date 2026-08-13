package com.flavor_immersed_daily.block.blockentity;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.recipe.FridgeFreezingRecipe;
import com.flavor_immersed_daily.recipe.FridgeTemperingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Optional;

public class FridgeBlockEntity extends BlockEntity implements Container {

    // 0-26: 储物间, 27: 变温室输入, 28: 变温室输出, 29: 冷冻室输入, 30: 冷冻室输出
    public static final int STORAGE_SLOTS = 27;
    public static final int TEMPERING_INPUT = 27;
    public static final int TEMPERING_OUTPUT = 28;
    public static final int FREEZING_INPUT = 29;
    public static final int FREEZING_OUTPUT = 30;
    public static final int TOTAL_SLOTS = 31;

    private NonNullList<ItemStack> items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);

    // 处理进度
    private int temperingProgress;
    private int temperingTotalTime;
    private int freezingProgress;
    private int freezingTotalTime;

    public FridgeBlockEntity(BlockPos pos, BlockState state) {
        super(com.flavor_immersed_daily.all.ModBlockEntities.FRIDGE_ENTITY.get(), pos, state);
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    // ==================== Container 实现 ====================

    @Override
    public int getContainerSize() {
        return TOTAL_SLOTS;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack s : items) {
            if (!s.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack result = ContainerHelper.removeItem(items, index, count);
        if (!result.isEmpty()) setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        items.set(index, stack);
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        items.clear();
        setChanged();
    }

    // ==================== Tick ====================

    public static void serverTick(Level level, BlockPos pos, BlockState state, FridgeBlockEntity be) {
        be.processTempering(level);
        be.processFreezing(level);
    }

    private void processTempering(Level level) {
        ItemStack input = items.get(TEMPERING_INPUT);
        if (input.isEmpty()) {
            temperingProgress = 0;
            temperingTotalTime = 0;
            return;
        }

        Optional<RecipeHolder<FridgeTemperingRecipe>> recipeOpt = level.getRecipeManager()
                .getRecipeFor(com.flavor_immersed_daily.recipe.ModRecipes.FRIDGE_TEMPERING_TYPE.get(),
                        new SingleSlotInput(input), level);

        if (recipeOpt.isEmpty()) {
            temperingProgress = 0;
            temperingTotalTime = 0;
            return;
        }

        FridgeTemperingRecipe recipe = recipeOpt.get().value();
        ItemStack result = recipe.getResult();
        ItemStack output = items.get(TEMPERING_OUTPUT);

        if (!output.isEmpty() && (!ItemStack.isSameItem(output, result) ||
                output.getCount() + result.getCount() > output.getMaxStackSize())) {
            temperingProgress = 0;
            return;
        }

        temperingTotalTime = recipe.getCookingTime();
        temperingProgress++;
        setChanged();

        if (temperingProgress >= temperingTotalTime) {
            input.shrink(1);
            if (output.isEmpty()) {
                items.set(TEMPERING_OUTPUT, result.copy());
            } else {
                output.grow(result.getCount());
            }
            temperingProgress = 0;
            temperingTotalTime = 0;
            setChanged();
        }
    }

    private void processFreezing(Level level) {
        ItemStack input = items.get(FREEZING_INPUT);
        if (input.isEmpty()) {
            freezingProgress = 0;
            freezingTotalTime = 0;
            return;
        }

        Optional<RecipeHolder<FridgeFreezingRecipe>> recipeOpt = level.getRecipeManager()
                .getRecipeFor(com.flavor_immersed_daily.recipe.ModRecipes.FRIDGE_FREEZING_TYPE.get(),
                        new SingleSlotInput(input), level);

        if (recipeOpt.isEmpty()) {
            freezingProgress = 0;
            freezingTotalTime = 0;
            return;
        }

        FridgeFreezingRecipe recipe = recipeOpt.get().value();
        ItemStack result = recipe.getResult();
        ItemStack output = items.get(FREEZING_OUTPUT);

        if (!output.isEmpty() && (!ItemStack.isSameItem(output, result) ||
                output.getCount() + result.getCount() > output.getMaxStackSize())) {
            freezingProgress = 0;
            return;
        }

        freezingTotalTime = recipe.getCookingTime();
        freezingProgress++;
        setChanged();

        if (freezingProgress >= freezingTotalTime) {
            input.shrink(1);
            if (output.isEmpty()) {
                items.set(FREEZING_OUTPUT, result.copy());
            } else {
                output.grow(result.getCount());
            }
            freezingProgress = 0;
            freezingTotalTime = 0;
            setChanged();
        }
    }

    public int getTemperingProgress() {
        return temperingProgress;
    }

    public int getTemperingTotalTime() {
        return temperingTotalTime;
    }

    public int getFreezingProgress() {
        return freezingProgress;
    }

    public int getFreezingTotalTime() {
        return freezingTotalTime;
    }

    // ==================== NBT ====================

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
        tag.putInt("TemperingProgress", temperingProgress);
        tag.putInt("TemperingTotalTime", temperingTotalTime);
        tag.putInt("FreezingProgress", freezingProgress);
        tag.putInt("FreezingTotalTime", freezingTotalTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
        this.temperingProgress = tag.getInt("TemperingProgress");
        this.temperingTotalTime = tag.getInt("TemperingTotalTime");
        this.freezingProgress = tag.getInt("FreezingProgress");
        this.freezingTotalTime = tag.getInt("FreezingTotalTime");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // ==================== RecipeInput 适配 ====================

    private record SingleSlotInput(ItemStack item) implements RecipeInput {
        @Override
        public ItemStack getItem(int index) {
            if (index != 0) throw new IllegalArgumentException("No item for index " + index);
            return item;
        }

        @Override
        public int size() {
            return 1;
        }
    }
}
