package com.fidtest.block.entity;

import com.fidtest.item.DishItem;
import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.CookingRecipeInput;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WokBlockEntity extends BlockEntity {

    private static final int MAX_ITEMS = 6;
    private static final int REQUIRED_STIRS = 3;

    private final ItemStackHandler inventory = new ItemStackHandler(MAX_ITEMS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    private int stirCount = 0;
    private int tossTicks = 0;
    private String seasoningName = "";

    public WokBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WOK.get(), pos, state);
    }

    public ItemStackHandler getInventory() { return inventory; }
    public int getStirCount() { return stirCount; }
    public int getRequiredStirs() { return REQUIRED_STIRS; }
    public int getTossTicks() { return tossTicks; }
    public String getSeasoningName() { return seasoningName; }
    public void setSeasoningName(String name) { this.seasoningName = name; }

    public int getItemCount() {
        int count = 0;
        for (int i = 0; i < MAX_ITEMS; i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) count++;
        }
        return count;
    }

    public boolean canAddItem() {
        return getItemCount() < MAX_ITEMS;
    }

    public void addItem(ItemStack stack) {
        for (int i = 0; i < MAX_ITEMS; i++) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                inventory.setStackInSlot(i, stack.copy());
                syncToClient();
                return;
            }
        }
    }

    public void stirFry(@Nullable Player player) {
        if (getItemCount() == 0) return;
        stirCount++;
        tossTicks = 12;

        if (level != null && !level.isClientSide) {
            for (int i = 0; i < 8; i++) {
                double x = worldPosition.getX() + 0.3 + level.random.nextDouble() * 0.4;
                double y = worldPosition.getY() + 0.25;
                double z = worldPosition.getZ() + 0.3 + level.random.nextDouble() * 0.4;
                ((ServerLevel) level).sendParticles(
                        net.minecraft.core.particles.ParticleTypes.SMOKE,
                        x, y, z, 1,
                        0.02, 0.05, 0.02, 0.01);
            }
        }

        syncToClient();
    }

    public void finishCooking(Level level, BlockPos pos) {
        if (level.isClientSide) return;

        int finalStirCount = stirCount;
        String finalSeasoning = seasoningName;

        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < MAX_ITEMS; i++) {
            ItemStack s = inventory.getStackInSlot(i);
            if (!s.isEmpty()) {
                items.add(s.copy());
                inventory.setStackInSlot(i, ItemStack.EMPTY);
            }
        }

        ItemStack result = findRecipeResult(level, items);
        boolean isProceduralDish = result == null;
        if (isProceduralDish) {
            result = DishItem.createProceduralDish(items);
            DishItem.setFoodProperties(result, items, finalStirCount);
            DishItem.setStirCount(result, finalStirCount);
            result.set(DataComponents.CUSTOM_NAME, DishItem.getDefaultDishName(result));
        }

// 无论配方产物还是工序菜，都把调味料注册名写入 seasoning，供前置模组在食用时给予 buff
        if (!finalSeasoning.isEmpty()) {
            DishItem.setSeasoningEffect(result, finalSeasoning);
        }

        ItemEntity entity = new ItemEntity(level,
                pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5, result);
        entity.setDefaultPickUpDelay();
        level.addFreshEntity(entity);

        stirCount = 0;
        tossTicks = 0;
        seasoningName = "";
        syncToClient();
    }

    private ItemStack findRecipeResult(Level level, List<ItemStack> inputs) {
        CookingRecipeInput recipeInput = new CookingRecipeInput(inputs);
        List<RecipeHolder<CookingRecipe>> recipes =
                ((ServerLevel) level).getRecipeManager()
                        .getAllRecipesFor(ModRecipeTypes.COOKING.get());

        for (RecipeHolder<CookingRecipe> holder : recipes) {
            CookingRecipe recipe = holder.value();
            String app = recipe.getAppliance();
            if (!app.isEmpty() && !app.equals("wok")) continue;
            if (recipe.matches(recipeInput, level)) {
                return recipe.getCookedResult(inputs);
            }
        }
        return null;
    }

    public List<ItemStack> getStoredItems() {
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < MAX_ITEMS; i++) {
            ItemStack s = inventory.getStackInSlot(i);
            if (!s.isEmpty()) list.add(s);
        }
        return list;
    }

    public void tick() {
        if (tossTicks > 0) {
            tossTicks--;
            syncToClient();
        }
    }

    private void syncToClient() {
        setChanged();
        if (level != null) {
            try {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider reg) {
        super.saveAdditional(tag, reg);
        tag.put("Inventory", inventory.serializeNBT(reg));
        tag.putInt("StirCount", stirCount);
        tag.putInt("TossTicks", tossTicks);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider reg) {
        super.loadAdditional(tag, reg);
// deserializeNBT 会按其 "Size" 重建槽位列表，需先把 Size 归一化为当前槽位数再反序列化；
// 若在其之后再调用 setSize，会把刚反序列化出来的物品整体清空（setSize 是换成全新的空列表）
        CompoundTag inventoryTag = tag.getCompound("Inventory").copy();
        inventoryTag.putInt("Size", MAX_ITEMS);
        inventory.deserializeNBT(reg, inventoryTag);
        stirCount = tag.getInt("StirCount");
        tossTicks = tag.getInt("TossTicks");
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
}
