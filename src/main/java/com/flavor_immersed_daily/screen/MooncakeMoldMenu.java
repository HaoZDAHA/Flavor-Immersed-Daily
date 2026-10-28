package com.flavor_immersed_daily.screen;

import com.flavor_immersed_daily.all.ModMenus;
import com.flavor_immersed_daily.item.MooncakeMoldItem;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

// 月饼模具 GUI 菜单 — 双槽位（饼皮 + 馅料）。
// 服务端持有模具物品的活引用，槽位内容经 MooncakeMoldContainer
// 实时写回模具 NBT，因此关闭 GUI 后材料仍然保留。
public class MooncakeMoldMenu extends AbstractContainerMenu {

// 模具槽位数量
    private static final int MOLD_SLOTS = 2;
    private static final int PLAYER_INV_START = 2;
    private static final int PLAYER_INV_END = PLAYER_INV_START + 36;

    private final ItemStack moldStack;
    private final Container moldContainer;

// 客户端构造 — 仅用于显示，槽位内容由服务端同步
    public MooncakeMoldMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ItemStack.EMPTY, new SimpleContainer(MOLD_SLOTS));
    }

// 服务端构造 — 槽位内容直接读写模具 NBT
    public MooncakeMoldMenu(int containerId, Inventory playerInventory, ItemStack moldStack) {
        this(containerId, playerInventory, moldStack, new MooncakeMoldContainer(moldStack));
    }

    private MooncakeMoldMenu(int containerId, Inventory playerInventory, ItemStack moldStack, Container moldContainer) {
        super(ModMenus.MOONCAKE_MOLD_MENU.get(), containerId);
        this.moldStack = moldStack;
        this.moldContainer = moldContainer;

// 槽 0：饼皮（限 5 种面团）
        this.addSlot(new Slot(moldContainer, 0, 44, 20) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return MooncakeMoldItem.getRawMooncakeForDough(stack.getItem()) != null;
            }
        });
// 槽 1：馅料（任意物品）
        this.addSlot(new Slot(moldContainer, 1, 116, 20));

// 玩家背包 3 行 x 9 列
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
// 玩家快捷栏 1 行 x 9 列
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();

            if (index < MOLD_SLOTS) {
// 从模具槽移到玩家背包
                if (!this.moveItemStackTo(stack, PLAYER_INV_START, PLAYER_INV_END, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
// 从背包移到模具槽：先饼皮（限 5 种面团）后馅料
                if (!this.moveItemStackTo(stack, 0, 1, false) &&
                        !this.moveItemStackTo(stack, 1, 2, false)) {
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
// 客户端
        if (moldStack.isEmpty()) return true;
// 模具仍握在主/副手才有效
        return player.getMainHandItem().is(moldStack.getItem()) || player.getOffhandItem().is(moldStack.getItem());
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
// 槽位内容已通过 MooncakeMoldContainer 实时写入 NBT，无需额外处理
    }

// 模具槽位容器 — 把两个槽位的物品同步写入模具物品 NBT。
// 仅在服务端使用（客户端构造走 SimpleContainer）。
    private static class MooncakeMoldContainer implements Container {

        private final ItemStack mold;
        private ItemStack dough;
        private ItemStack filling;

        MooncakeMoldContainer(ItemStack mold) {
            this.mold = mold;
            this.dough = MooncakeMoldItem.getStoredDough(mold);
            this.filling = MooncakeMoldItem.getStoredFilling(mold);
        }

        @Override
        public int getContainerSize() {
            return 2;
        }

        @Override
        public boolean isEmpty() {
            return dough.isEmpty() && filling.isEmpty();
        }

        @Override
        public ItemStack getItem(int index) {
            return index == 0 ? dough : filling;
        }

        @Override
        public ItemStack removeItem(int index, int amount) {
            ItemStack stack = getItem(index);
            if (stack.isEmpty() || amount <= 0) {
                return ItemStack.EMPTY;
            }
            ItemStack removed = stack.split(amount);
            setChanged();
            return removed;
        }

        @Override
        public ItemStack removeItemNoUpdate(int index) {
            ItemStack stack = getItem(index);
            setItem(index, ItemStack.EMPTY);
            return stack;
        }

        @Override
        public void setItem(int index, ItemStack stack) {
            if (index == 0) {
                dough = stack;
            } else {
                filling = stack;
            }
            setChanged();
        }

        @Override
        public void setChanged() {
            MooncakeMoldItem.saveStored(mold, dough, filling);
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }

        @Override
        public void clearContent() {
            dough = ItemStack.EMPTY;
            filling = ItemStack.EMPTY;
            setChanged();
        }
    }
}