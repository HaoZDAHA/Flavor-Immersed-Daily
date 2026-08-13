package com.flavor_immersed_daily.screen;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FridgeMenu extends AbstractContainerMenu {

    private final Container fridgeContainer;

    // 槽位索引常量（与 FridgeBlockEntity 保持一致）
    private static final int STORAGE_SLOTS = 27;
    private static final int TEMPERING_INPUT = 27;
    private static final int TEMPERING_OUTPUT = 28;
    private static final int FREEZING_INPUT = 29;
    private static final int FREEZING_OUTPUT = 30;
    private static final int TOTAL_FRIDGE_SLOTS = 31;
    private static final int PLAYER_INV_START = 31;
    private static final int PLAYER_HOTBAR_START = 58;

    public FridgeMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(TOTAL_FRIDGE_SLOTS));
    }

    public FridgeMenu(int containerId, Inventory playerInventory, Container fridgeContainer) {
        super(com.flavor_immersed_daily.all.ModMenus.FRIDGE_MENU.get(), containerId);
        this.fridgeContainer = fridgeContainer;
        fridgeContainer.startOpen(playerInventory.player);

        // 冰箱储物间 3行×9列，GUI坐标(8,16)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(fridgeContainer, col + row * 9, 8 + col * 18, 16 + row * 18));
            }
        }

        // 变温室 — 输入槽 (39,77)
        this.addSlot(new Slot(fridgeContainer, TEMPERING_INPUT, 39, 77));
        // 变温室 — 输出槽 (138,77)，只能取出
        this.addSlot(new Slot(fridgeContainer, TEMPERING_OUTPUT, 138, 77) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        // 冷冻室 — 输入槽 (39,110)
        this.addSlot(new Slot(fridgeContainer, FREEZING_INPUT, 39, 110));
        // 冷冻室 — 输出槽 (138,110)，只能取出
        this.addSlot(new Slot(fridgeContainer, FREEZING_OUTPUT, 138, 110) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        // 玩家背包 3行×9列，GUI坐标(8,139)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 139 + row * 18));
            }
        }

        // 玩家快捷栏 1行×9列，GUI坐标(8,197)
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 197));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();

            if (index == TEMPERING_OUTPUT || index == FREEZING_OUTPUT) {
                // 从输出槽移到玩家背包
                if (!this.moveItemStackTo(stack, PLAYER_INV_START, PLAYER_HOTBAR_START + 9, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < TOTAL_FRIDGE_SLOTS) {
                // 从冰箱任意槽（不含输出槽）移到玩家背包
                if (!this.moveItemStackTo(stack, PLAYER_INV_START, PLAYER_HOTBAR_START + 9, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // 从玩家背包移到冰箱：优先储物间，其次处理输入槽
                if (!this.moveItemStackTo(stack, 0, STORAGE_SLOTS, false) &&
                        !this.moveItemStackTo(stack, TEMPERING_INPUT, TEMPERING_INPUT + 1, false) &&
                        !this.moveItemStackTo(stack, FREEZING_INPUT, FREEZING_INPUT + 1, false)) {
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
        return this.fridgeContainer.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.fridgeContainer.stopOpen(player);
    }

    public Container getFridgeContainer() {
        return fridgeContainer;
    }
}
