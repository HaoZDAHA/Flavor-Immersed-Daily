package com.fidtest.block.entity;

import com.fidtest.FidworkMod;
import com.fidtest.recipe.ApplianceType;
import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.CookingRecipeInput;
import com.fidtest.recipe.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractCookingBlockEntity extends BlockEntity implements MenuProvider, WorldlyContainer {

    private final ApplianceType applianceType;
    private NonNullList<ItemStack> items;
    private int cookingProgress;
    private int cookingTotalTime;
    private int heatLevel;
// 变温烤箱模式：0=烘干，1=烤箱；其他设备始终为 0
    private int dryerMode;

    protected final ContainerData dataAccess = new ContainerData() {
        @Override public int get(int i) { return switch (i) { case 0 -> cookingProgress; case 1 -> cookingTotalTime; case 2 -> heatLevel; case 3 -> dryerMode; default -> 0; }; }
        @Override public void set(int i, int v) { switch (i) { case 0 -> cookingProgress = v; case 1 -> cookingTotalTime = v; case 2 -> heatLevel = v; case 3 -> dryerMode = v; } }
        @Override public int getCount() { return 4; }
    };

    public AbstractCookingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, ApplianceType at) {
        super(type, pos, state);
        this.applianceType = at;
        this.items = NonNullList.withSize(at.getInventorySize(), ItemStack.EMPTY);
    }

    public ApplianceType getApplianceType() { return applianceType; }
    public int getCookingProgress() { return cookingProgress; }
    public int getCookingTotalTime() { return cookingTotalTime; }
    public int getHeatLevel() { return heatLevel; }
    public int getDryerMode() { return dryerMode; }
    public ContainerData getDataAccess() { return dataAccess; }
    public ItemStack getStackInSlot(int i) { return items.get(i); }
    public void setStackInSlot(int i, ItemStack s) { items.set(i, s); }
    protected void setCookingTotalTime(int t) { this.cookingTotalTime = t; }

// 切换变温烤箱模式（0=烘干，1=烤箱），并重置烹饪进度
    public void setDryerMode(int mode) {
        if (applianceType != ApplianceType.DRYER || this.dryerMode == mode) return;
        this.dryerMode = mode;
        this.cookingProgress = 0;
        this.cookingTotalTime = 0;
        setChanged();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AbstractCookingBlockEntity be) {
        be.updateHeatLevel(level, pos);
        boolean dirty = false;
        CookingRecipe recipe = be.findMatchingRecipe();

        if (be.isHeated() && recipe != null && be.canCook(recipe)) {
            if (recipe.getCookingTime() == 0) {
                be.cookRecipe(recipe);
                dirty = true;
            } else {
                be.cookingProgress++;
                if (be.cookingProgress >= be.cookingTotalTime) {
                    be.cookingProgress = 0;
                    be.cookingTotalTime = recipe.getCookingTime();
                    be.cookRecipe(recipe);
                    dirty = true;
                }
            }
        } else if (be.cookingProgress > 0) {
            be.cookingProgress = Math.max(0, be.cookingProgress - 2);
        }
        if (recipe == null && be.cookingProgress > 0) be.cookingProgress = 0;
        if (dirty) setChanged(level, pos, state);
    }

    private void updateHeatLevel(Level level, BlockPos pos) {
        if (!applianceType.needsFuel()) { heatLevel = 1; return; }
        BlockState below = level.getBlockState(pos.below());
        if (below.getBlock() == Blocks.FIRE) heatLevel = 1;
        else if (below.getBlock() instanceof CampfireBlock && below.getValue(CampfireBlock.LIT)) heatLevel = 1;
        else if (below.getBlock() == Blocks.MAGMA_BLOCK) heatLevel = 1;
        else if (below.getBlock() == Blocks.LAVA) heatLevel = 1;
        else heatLevel = 0;
    }

    public boolean isHeated() { return !applianceType.needsFuel() || heatLevel > 0; }

    @Nullable
    protected CookingRecipe findMatchingRecipe() {
        if (level == null || level.isClientSide) return null;
        List<ItemStack> inputItems = new ArrayList<>();
        for (int i = 0; i < applianceType.getInputSlots(); i++) inputItems.add(items.get(i));
        CookingRecipeInput input = new CookingRecipeInput(inputItems);
        if (input.isEmpty()) return null;

        CookingRecipe own = ((ServerLevel) level).getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.COOKING.get())
                .stream()
                .filter(r -> {
                    String a = r.getAppliance();
                    if (!a.isEmpty() && !a.equals(applianceType.getName())) return false;
// 变温烤箱按当前模式过滤配方
                    if (applianceType == ApplianceType.DRYER) {
                        String mode = r.getMode();
                        if (dryerMode == 1) return "oven".equals(mode);
                        return mode.isEmpty() || "dry".equals(mode);
                    }
                    return true;
                })
                .filter(r -> r.matches(input, level))
                .findFirst()
                .orElse(null);
        if (own != null) return own;

// 变温烤箱烤箱模式：兼容所有营火（campfire）配方
        if (applianceType == ApplianceType.DRYER && dryerMode == 1) {
            return findCampfireRecipe(input);
        }
        return null;
    }

// 在营火配方中查找匹配的配方，包装成变温烤箱烤箱模式配方
    @Nullable
    private CookingRecipe findCampfireRecipe(CookingRecipeInput input) {
        if (level == null || level.isClientSide) return null;
        return ((ServerLevel) level).getRecipeManager()
                .getAllRecipesFor(RecipeType.CAMPFIRE_COOKING)
                .stream()
                .map((CampfireCookingRecipe recipe) -> new CookingRecipe(
                        new ResourceLocation(FidworkMod.MODID, "campfire_compat/" + recipe.getId().getPath()),
                        ApplianceType.DRYER.getName(),
                        recipe.getIngredients(),
                        recipe.getResultItem(level.registryAccess()),
                        Optional.empty(),
                        recipe.getCookingTime(),
                        recipe.getExperience(),
                        0,
                        "oven"))
                .filter(r -> r.matches(input, level))
                .findFirst()
                .orElse(null);
    }

    protected boolean canCook(CookingRecipe recipe) {
        if (recipe.getMinHeat() > this.heatLevel) return false;
        ItemStack out = items.get(applianceType.getOutputSlot());
        ItemStack result = recipe.getResult();
        if (out.isEmpty()) return true;
        if (!ItemStack.isSameItem(out, result)) return false;
        return out.getCount() + result.getCount() <= out.getMaxStackSize();
    }

    protected void cookRecipe(CookingRecipe recipe) {
// 先复制一份输入（含 NBT），供产物继承 filling——必须在消耗输入之前
        List<ItemStack> inputItems = new ArrayList<>();
        for (int i = 0; i < applianceType.getInputSlots(); i++) inputItems.add(items.get(i).copy());

        List<Ingredient> remaining = new ArrayList<>(recipe.getIngredients());
        for (int i = 0; i < applianceType.getInputSlots(); i++) {
            ItemStack s = items.get(i);
            if (s.isEmpty()) continue;
            for (int j = 0; j < remaining.size(); j++) {
                if (remaining.get(j).test(s)) { s.shrink(1); remaining.remove(j); break; }
            }
        }
        ItemStack result = recipe.getCookedResult(inputItems);
        ItemStack outSlot = items.get(applianceType.getOutputSlot());
        if (outSlot.isEmpty()) items.set(applianceType.getOutputSlot(), result);
        else outSlot.grow(result.getCount());

        if (applianceType.hasContainer() && recipe.getContainer().isPresent()) {
            ItemStack c = recipe.getContainer().get().copy();
            if (!c.isEmpty()) {
                ItemStack cSlot = items.get(applianceType.getContainerSlot());
                if (cSlot.isEmpty()) items.set(applianceType.getContainerSlot(), c);
                else if (ItemStack.isSameItem(cSlot, c)) cSlot.grow(c.getCount());
            }
        }
        if (recipe.getExperience() > 0 && level != null && !level.isClientSide) {
            ExperienceOrb.award((ServerLevel) level, getBlockPos().getCenter(), (int) recipe.getExperience());
        }
    }

    @Override public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            if (applianceType.hasContainer()) return new int[]{applianceType.getOutputSlot(), applianceType.getContainerSlot()};
            return new int[]{applianceType.getOutputSlot()};
        }
        int[] s = new int[applianceType.getInputSlots()];
        for (int i = 0; i < s.length; i++) s[i] = applianceType.getInputStartSlot() + i;
        return s;
    }
    @Override public boolean canPlaceItemThroughFace(int i, ItemStack s, @Nullable Direction d) {
        return i >= applianceType.getInputStartSlot() && i < applianceType.getInputStartSlot() + applianceType.getInputSlots();
    }
    @Override public boolean canTakeItemThroughFace(int i, ItemStack s, Direction d) {
        return i == applianceType.getOutputSlot() || (applianceType.hasContainer() && i == applianceType.getContainerSlot());
    }

    @Override public int getContainerSize() { return items.size(); }
    @Override public boolean isEmpty() { return items.stream().allMatch(ItemStack::isEmpty); }
    @Override public ItemStack getItem(int i) { return i >= 0 && i < items.size() ? items.get(i) : ItemStack.EMPTY; }
    @Override public ItemStack removeItem(int i, int c) { ItemStack r = ContainerHelper.removeItem(items, i, c); if (!r.isEmpty()) setChanged(); return r; }
    @Override public ItemStack removeItemNoUpdate(int i) { return ContainerHelper.takeItem(items, i); }
    @Override public void setItem(int i, ItemStack s) { if (i >= 0 && i < items.size()) { items.set(i, s); if (s.getCount() > getMaxStackSize()) s.setCount(getMaxStackSize()); setChanged(); } }
    @Override public boolean stillValid(Player p) { return level != null && level.getBlockEntity(worldPosition) == this && p.distanceToSqr(worldPosition.getX()+0.5, worldPosition.getY()+0.5, worldPosition.getZ()+0.5) <= 64.0; }
    @Override public void clearContent() { items.clear(); setChanged(); }

    @Override public Component getDisplayName() { return getBlockState().getBlock().getName(); }
    @Nullable @Override public abstract AbstractContainerMenu createMenu(int id, Inventory inv, Player player);

    @Override public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("CookingProgress", cookingProgress);
        tag.putInt("CookingTotalTime", cookingTotalTime);
        if (applianceType == ApplianceType.DRYER) tag.putInt("DryerMode", dryerMode);
        ContainerHelper.saveAllItems(tag, items);
    }
    @Override public void load(CompoundTag tag) {
        super.load(tag);
        cookingProgress = tag.getInt("CookingProgress");
        cookingTotalTime = tag.getInt("CookingTotalTime");
        if (applianceType == ApplianceType.DRYER) dryerMode = tag.getInt("DryerMode");
        ContainerHelper.loadAllItems(tag, items);
    }
    @Override public CompoundTag getUpdateTag() { CompoundTag t = new CompoundTag(); saveAdditional(t); return t; }
    @Nullable @Override public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
    @Override public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) { load(pkt.getTag()); }

    private final IItemHandler itemHandler = new InvWrapper(this);
    public IItemHandler getItemHandler() { return itemHandler; }
}
