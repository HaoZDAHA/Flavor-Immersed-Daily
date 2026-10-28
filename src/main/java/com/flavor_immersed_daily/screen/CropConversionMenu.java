package com.flavor_immersed_daily.screen;

import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.all.ModMenus;
import com.flavor_immersed_daily.recipe.CropConversionRecipe;
import com.flavor_immersed_daily.recipe.ModRecipes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// 作物鉴定机 - 作物转换 UI（外观与交互和原版切石机一致）。
// 左侧放入任意转换组内的物品后，右侧列出该组内“当前已安装”的所有其他物品，
// 点击选择、再点结果槽即可消耗 1 个输入转换为选中的物品。
public class CropConversionMenu extends AbstractContainerMenu {

    public static final int INPUT_SLOT = 0;
    public static final int RESULT_SLOT = 1;
    private static final int INV_SLOT_START = 2;
    private static final int INV_SLOT_END = 29;
    private static final int USE_ROW_SLOT_START = 29;
    private static final int USE_ROW_SLOT_END = 38;

    private final ContainerLevelAccess access;
    private final DataSlot selectedRecipeIndex = DataSlot.standalone();
    private final Level level;
// 输入物品对应的可转换候选输出
    private List<ItemStack> candidates = new ArrayList<>();
    private ItemStack input = ItemStack.EMPTY;
    long lastSoundTime;
    final Slot inputSlot;
    final Slot resultSlot;
    Runnable slotUpdateListener = () -> {
    };
    public final Container container = new SimpleContainer(1) {
        @Override
        public void setChanged() {
            super.setChanged();
            CropConversionMenu.this.slotsChanged(this);
            CropConversionMenu.this.slotUpdateListener.run();
        }
    };
    final ResultContainer resultContainer = new ResultContainer();

    public CropConversionMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public CropConversionMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenus.CROP_CONVERSION_MENU.get(), containerId);
        this.access = access;
        this.level = playerInventory.player.level();
        this.inputSlot = this.addSlot(new Slot(this.container, 0, 20, 33));
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 33) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                stack.onCraftedBy(player.level(), player, stack.getCount());
                ItemStack itemstack = CropConversionMenu.this.inputSlot.remove(1);
                if (!itemstack.isEmpty()) {
                    CropConversionMenu.this.setupResultSlot();
                }
                access.execute((pLevel, pPos) -> {
                    long l = pLevel.getGameTime();
                    if (CropConversionMenu.this.lastSoundTime != l) {
                        pLevel.playSound(null, pPos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        CropConversionMenu.this.lastSoundTime = l;
                    }
                });
                super.onTake(player, stack);
            }
        });

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
        this.addDataSlot(this.selectedRecipeIndex);
    }

    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex.get();
    }

    public List<ItemStack> getCandidates() {
        return this.candidates;
    }

    public int getNumRecipes() {
        return this.candidates.size();
    }

    public boolean hasInputItem() {
        return this.inputSlot.hasItem() && !this.candidates.isEmpty();
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.AGRICULTURALAPPRAISALMACHINE.get());
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (this.isValidRecipeIndex(id)) {
            this.selectedRecipeIndex.set(id);
            this.setupResultSlot();
        }
        return true;
    }

    private boolean isValidRecipeIndex(int recipeIndex) {
        return recipeIndex >= 0 && recipeIndex < this.candidates.size();
    }

    @Override
    public void slotsChanged(Container inventory) {
        ItemStack itemstack = this.inputSlot.getItem();
        if (!itemstack.is(this.input.getItem())) {
            this.input = itemstack.copy();
            this.setupRecipeList(inventory, itemstack);
        }
    }

    private void setupRecipeList(Container container, ItemStack stack) {
        this.candidates.clear();
        this.selectedRecipeIndex.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);
        if (!stack.isEmpty()) {
            Set<Item> seen = new LinkedHashSet<>();
            for (CropConversionRecipe recipe :
                    this.level.getRecipeManager().getAllRecipesFor(ModRecipes.CROP_CONVERSION_TYPE.get())) {
                if (recipe.contains(stack.getItem())) {
                    seen.addAll(recipe.getLoadedItems(stack.getItem()));
                }
            }
            for (Item item : seen) {
                this.candidates.add(new ItemStack(item));
            }
        }
    }

    void setupResultSlot() {
        if (!this.candidates.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
            this.resultSlot.set(this.candidates.get(this.selectedRecipeIndex.get()).copy());
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
        }
        this.broadcastChanges();
    }

    @Override
    public MenuType<?> getType() {
        return ModMenus.CROP_CONVERSION_MENU.get();
    }

    public void registerUpdateListener(Runnable listener) {
        this.slotUpdateListener = listener;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultContainer && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public boolean canDragTo(Slot slot) {
        return slot.container != this.resultContainer;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            Item item = itemstack1.getItem();
            itemstack = itemstack1.copy();
            if (index == 1) {
                item.onCraftedBy(itemstack1, player.level(), player);
                if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index == 0) {
                if (!this.moveItemStackTo(itemstack1, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.isValidInput(itemstack1)) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 2 && index < 29) {
                if (!this.moveItemStackTo(itemstack1, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 29 && index < 38 && !this.moveItemStackTo(itemstack1, 2, 29, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            }
            slot.setChanged();
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemstack1);
            this.broadcastChanges();
        }
        return itemstack;
    }

    private boolean isValidInput(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        Item item = stack.getItem();
        for (CropConversionRecipe recipe :
                this.level.getRecipeManager().getAllRecipesFor(ModRecipes.CROP_CONVERSION_TYPE.get())) {
            if (recipe.contains(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((pLevel, pPos) -> {
            this.clearContainer(player, this.container);
        });
    }
}
