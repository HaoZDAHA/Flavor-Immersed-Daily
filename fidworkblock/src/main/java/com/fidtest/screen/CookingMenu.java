package com.fidtest.screen;

import com.fidtest.block.entity.AbstractCookingBlockEntity;
import com.fidtest.recipe.ApplianceType;
import com.fidtest.registration.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CookingMenu extends AbstractContainerMenu {

    private final ApplianceType appliance;
    private final ContainerData data;
    private final ContainerLevelAccess access;
    private final AbstractCookingBlockEntity be;

    private static final int BORDER = 18;
    private static final int PLAYER_X = 8;
    private static final int PLAYER_INV_Y = 84;
    private static final int HOTBAR_Y = 142;

    private static final int[][] INPUT_BASE = {
            {80, 35}, {49, 22}, {62, 35}, {53, 35}, {53, 26}, {44, 35}, {30, 17}
    };
    private static final int[] INPUT_COLS = {1, 1, 2, 3, 2, 3, 3};

// Pot 槽位坐标
    private static int potSlotX(int idx) {
        return switch (idx) { case 0 -> 54; case 1 -> 73; case 2 -> 92; case 3 -> 54; case 4 -> 73; case 5 -> 92; case 6 -> 16; default -> 80; };
    }
    private static int potSlotY(int idx) {
        return switch (idx) { case 0,1,2 -> 18; case 3,4,5 -> 37; case 6 -> 40; default -> 35; };
    }

// Pressurecooker 专属槽位坐标
    private static int pcSlotX(int idx) {
        return switch (idx) { case 0 -> 21; case 1 -> 43; case 2 -> 43; default -> 80; };
    }
    private static int pcSlotY(int idx) {
        return switch (idx) { case 0 -> 32; case 1 -> 23; case 2 -> 42; default -> 35; };
    }

    private static int outX(ApplianceType t) {
        if (t == ApplianceType.DRYER) return 108;
        if (t == ApplianceType.PRESSURECOOKER) return 136;
        return t == ApplianceType.POT ? 136 : 124;
    }
    private static int outY(ApplianceType t) {
        if (t == ApplianceType.DRYER) return 21;
        if (t == ApplianceType.PRESSURECOOKER) return 35;
        return t == ApplianceType.POT ? 28 : 35;
    }
    private static int contX() { return 124; }
    private static int contY() { return 55; }

    public CookingMenu(int windowId, Inventory inv, AbstractCookingBlockEntity be) {
        super(ModMenuTypes.COOKING.get(), windowId);
        this.appliance = be.getApplianceType();
        this.data = be.getDataAccess();
        this.access = ContainerLevelAccess.create(be.getLevel(), be.getBlockPos());
        this.be = be;
        buildSlots(inv, be);
        this.addDataSlots(this.data);
    }

    public CookingMenu(int windowId, Inventory inv, FriendlyByteBuf buf) {
        super(ModMenuTypes.COOKING.get(), windowId);
        BlockEntity tbe = inv.player.level().getBlockEntity(buf.readBlockPos());
        this.be = (AbstractCookingBlockEntity) tbe;
        this.appliance = be.getApplianceType();
        this.data = be.getDataAccess();
        this.access = be.getLevel() != null ? ContainerLevelAccess.create(be.getLevel(), be.getBlockPos()) : ContainerLevelAccess.NULL;
        buildSlots(inv, be);
        this.addDataSlots(this.data);
    }

    private void buildSlots(Inventory inv, AbstractCookingBlockEntity be) {
        int n = appliance.getInputSlots();

        if (appliance == ApplianceType.POT) {
            for (int i = 0; i < n; i++)
                addSlot(new Slot(be, i, potSlotX(i), potSlotY(i)));
        } else if (appliance == ApplianceType.PRESSURECOOKER) {
            for (int i = 0; i < n; i++)
                addSlot(new Slot(be, i, pcSlotX(i), pcSlotY(i)));
        } else {
            int cols = INPUT_COLS[Math.min(n, 6)];
            for (int i = 0; i < n; i++) {
                int bx = INPUT_BASE[Math.min(n, 6)][0];
                int by = INPUT_BASE[Math.min(n, 6)][1];
                addSlot(new Slot(be, i, bx + (i % cols) * BORDER, by + (i / cols) * BORDER));
            }
        }

        addSlot(new Slot(be, appliance.getOutputSlot(), outX(appliance), outY(appliance)) {
            @Override public boolean mayPlace(ItemStack s) { return false; }
        });

        if (appliance.hasContainer()) {
            addSlot(new Slot(be, appliance.getContainerSlot(), contX(), contY()));
        }

        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(inv, 9 + row * 9 + col, PLAYER_X + col * BORDER, PLAYER_INV_Y + row * BORDER));
        for (int col = 0; col < 9; col++)
            addSlot(new Slot(inv, col, PLAYER_X + col * BORDER, HOTBAR_Y));
    }

    public ApplianceType getAppliance() { return appliance; }
    public int getCookingProgress() { return data.get(0); }
    public int getCookingTotalTime() { return data.get(1); }
    public int getHeatLevel() { return data.get(2); }

// 变温烤箱模式：0=烘干，1=烤箱
    public int getDryerMode() { return data.get(3); }

// 处理变温烤箱模式按钮：0=烘干模式，1=烤箱模式
    @Override
    public boolean clickMenuButton(Player p, int id) {
        if (appliance != ApplianceType.DRYER) return false;
        if (id != 0 && id != 1) return false;
        be.setDryerMode(id);
        return true;
    }

    @Override
    public boolean stillValid(Player p) {
        return stillValid(access, p, be.getBlockState().getBlock());
    }

    @Override
    public ItemStack quickMoveStack(Player p, int idx) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(idx);
        if (slot == null || !slot.hasItem()) return result;
        ItemStack s = slot.getItem();
        result = s.copy();

        int invStart = appliance.getInventorySize();
        int invEnd = invStart + 36;

        if (idx == appliance.getOutputSlot()) {
            if (!moveItemStackTo(s, invStart, invEnd, true)) return ItemStack.EMPTY;
        } else if (idx >= invStart) {
            if (!moveItemStackTo(s, 0, appliance.getInputSlots(), false)) return ItemStack.EMPTY;
        } else {
            if (!moveItemStackTo(s, invStart, invEnd, false)) return ItemStack.EMPTY;
        }

        if (s.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();
        if (s.getCount() == result.getCount()) return ItemStack.EMPTY;
        slot.onTake(p, s);
        return result;
    }
}
