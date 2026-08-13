package com.flavor_immersed_daily.screen;

import com.flavor_immersed_daily.all.ModBlocks;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.block.blockentity.EggBreakingMachineBlockEntity;
import com.flavor_immersed_daily.recipe.EggBreakingRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class EggBreakingMachineMenu extends AbstractContainerMenu {

    private static final int CRAFT_SLOTS = 9;
    private static final int PLAYER_INV_START = 9;
    private static final int PLAYER_HOTBAR_START = 36;

    private final SimpleContainer craftContainer;   // 9 槽：3×3 输入（引用 BlockEntity 的库存）
    private final ContainerLevelAccess access;
    private final Player player;

    private final DataSlot selectedRecipeIndex = DataSlot.standalone();
    private List<RecipeHolder<EggBreakingRecipe>> matchingRecipes = List.of();

    // 客户端构造
    public EggBreakingMachineMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(9), ContainerLevelAccess.NULL);
    }

    // 服务端构造 — 传入 BlockEntity 的库存
    public EggBreakingMachineMenu(int containerId, Inventory playerInventory,
                                  SimpleContainer craftContainer, ContainerLevelAccess access) {
        super(com.flavor_immersed_daily.all.ModMenus.EGG_BREAKING_MACHINE_MENU.get(), containerId);
        this.craftContainer = craftContainer;
        this.access = access;
        this.player = playerInventory.player;
        craftContainer.startOpen(player);
        craftContainer.addListener(container -> EggBreakingMachineMenu.this.slotsChanged(container));

        // 3×3 输入网格 — 引用 craftContainer（即 BlockEntity 的库存）
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new Slot(craftContainer, col + row * 3, 30 + col * 18, 17 + row * 18));
            }
        }

        // 玩家背包
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        // 快捷栏
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        addDataSlot(selectedRecipeIndex);
    }

    public int getSelectedRecipeIndex() {
        return selectedRecipeIndex.get();
    }

    public List<RecipeHolder<EggBreakingRecipe>> getMatchingRecipes() {
        return matchingRecipes;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id >= 0 && id < matchingRecipes.size()) {
            if (!player.level().isClientSide()) {
                access.execute((level, pos) -> {
                    if (level.getBlockEntity(pos) instanceof EggBreakingMachineBlockEntity be) {
                        EggBreakingRecipe recipe = matchingRecipes.get(id).value();
                        int sets = be.countCompleteSets(recipe);
                        if (sets > 0) {
                            be.consumeSets(recipe, sets);
                            be.startProcessing(recipe, sets);
                        }
                    }
                });
                player.closeContainer();
            }
            return true;
        }
        return false;
    }

    @Override
    public void slotsChanged(Container container) {
        if (container != this.craftContainer) {
            super.slotsChanged(container);
            return;
        }

        // 构建 CraftingInput
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            items.add(craftContainer.getItem(i));
        }
        CraftingInput input = CraftingInput.of(3, 3, items);

        // 搜索匹配配方
        Level level = player.level();
        matchingRecipes = new ArrayList<>();
        for (RecipeHolder<?> holder : level.getRecipeManager()
                .getAllRecipesFor(com.flavor_immersed_daily.recipe.ModRecipes.EGG_BREAKING_TYPE.get())) {
            if (holder.value() instanceof EggBreakingRecipe recipe && recipe.matches(input, level)) {
                @SuppressWarnings("unchecked")
                RecipeHolder<EggBreakingRecipe> typed = (RecipeHolder<EggBreakingRecipe>) holder;
                matchingRecipes.add(typed);
            }
        }

        // 服务端自动选择第一个
        if (!level.isClientSide()) {
            if (!matchingRecipes.isEmpty()) {
                if (selectedRecipeIndex.get() < 0 || selectedRecipeIndex.get() >= matchingRecipes.size()) {
                    selectedRecipeIndex.set(0);
                }
            } else {
                selectedRecipeIndex.set(-1);
            }
        }

        super.slotsChanged(container);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();

            if (index < CRAFT_SLOTS) {
                if (!this.moveItemStackTo(stack, PLAYER_INV_START, PLAYER_HOTBAR_START + 9, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(stack, 0, CRAFT_SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, ModBlocks.EGGBREAKINGMACHINE.get());
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        // 物品存储在 BlockEntity 的库存中，不丢弃
    }
}
