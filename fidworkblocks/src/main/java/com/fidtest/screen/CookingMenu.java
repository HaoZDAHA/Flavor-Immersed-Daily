package com.fidtest.screen;

import com.fidtest.ExampleMod;
import com.fidtest.block.entity.AbstractCookingBlockEntity;
import com.fidtest.recipe.ApplianceType;
import com.fidtest.registration.ModBlocks;
import com.fidtest.registration.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Objects;

public class CookingMenu extends AbstractContainerMenu {

    private final ApplianceType applianceType;
    private final ContainerData data;
    private final ContainerLevelAccess access;
    private final AbstractCookingBlockEntity be;

// 网络工厂（客户端调用）

    public static CookingMenu fromNetwork(int windowId, Inventory inv, FriendlyByteBuf buf) {
        BlockEntity be = inv.player.level().getBlockEntity(buf.readBlockPos());
        if (!(be instanceof AbstractCookingBlockEntity cooking)) {
            throw new IllegalStateException("Block entity not found or wrong type!");
        }
        return new CookingMenu(windowId, inv, cooking);
    }

// 槽位布局常量

    private static final int BORDER = 18;
    private static final int PLAYER_INV_Y = 84;
    private static final int HOTBAR_Y = 142;
    private static final int PLAYER_X = 8;

// 输入槽起始坐标（按槽数量）
    private static final int[][] INPUT_START = {
// 0 (fallback)
            {80, 35},
// 1 (dryer 等单槽)
            {49, 22},
// 2
            {62, 35},
// 3 (默认 3 槽位)
            {53, 35},
// 4
            {53, 26},
// 5
            {44, 35},
// 6 (pot 专用 2×3 网格)
            {30, 17},
    };
    private static final int[][] INPUT_COLS = {
            {1}, {1}, {2}, {3}, {2}, {3}, {3}
    };

    private static int inputX(int count, int col) {
        return INPUT_START[Math.min(count, 6)][0] + col * BORDER;
    }

    private static int inputY(int count, int row) {
        return INPUT_START[Math.min(count, 6)][1] + row * BORDER;
    }

    private static int inputCols(int count) {
        return INPUT_COLS[Math.min(count, 6)][0];
    }

// Pressurecooker 专属槽位坐标
    private static int pressurecookerInputX(int slotIndex) {
        return switch (slotIndex) {
            case 0 -> 21;
            case 1 -> 43;
            case 2 -> 43;
            default -> 80;
        };
    }

    private static int pressurecookerInputY(int slotIndex) {
        return switch (slotIndex) {
            case 0 -> 32;
            case 1 -> 23;
            case 2 -> 42;
            default -> 35;
        };
    }

// Pot 专属槽位坐标
    private static int potInputX(int slotIndex) {
        return switch (slotIndex) {
            case 0 -> 54;
            case 1 -> 73;
            case 2 -> 92;
            case 3 -> 54;
            case 4 -> 73;
            case 5 -> 92;
            case 6 -> 16;
            default -> 80;
        };
    }

    private static int potInputY(int slotIndex) {
        return switch (slotIndex) {
            case 0, 1, 2 -> 18;
            case 3, 4, 5 -> 37;
            case 6 -> 40;
            default -> 35;
        };
    }

// 输出槽 / 进度条 / 容器槽位置
    private static int outputX(ApplianceType type) {
        if (type.isInstant()) return 130;
        if (type == ApplianceType.DRYER) return 108;
        if (type == ApplianceType.PRESSURECOOKER) return 136;
        if (type == ApplianceType.POT) return 136;
        return 124;
    }

    private static int outputY(ApplianceType type) {
        if (type == ApplianceType.DRYER) return 21;
        if (type == ApplianceType.PRESSURECOOKER) return 35;
        if (type == ApplianceType.POT) return 28;
        return 35;
    }

    private static int progressX(ApplianceType type) {
        return switch (type) {
            case POT -> 100;
            case PRESSURECOOKER, STEAMBOX, WOK -> 95;
            default -> 100;
        };
    }

    private static int progressY() { return 35; }
    private static int containerX() { return 124; }
    private static int containerY() { return 55; }

// 构造函数（服务端/方块实体调用）

    public CookingMenu(int windowId, Inventory inv, AbstractCookingBlockEntity be) {
        super(ModMenuTypes.COOKING.get(), windowId);
        this.applianceType = be.getApplianceType();
        this.data = be.getDataAccess();
        this.access = ContainerLevelAccess.create(be.getLevel(), be.getBlockPos());
        this.be = be;
        buildSlots(inv, be);
        this.addDataSlots(this.data);
    }

// 构造函数（客户端/网络调用）

    private CookingMenu(int windowId, Inventory inv, ApplianceType type,
                        AbstractCookingBlockEntity be) {
        super(ModMenuTypes.COOKING.get(), windowId);
        this.applianceType = type;
        this.data = new SimpleContainerData(4);
        this.access = be != null
                ? ContainerLevelAccess.create(be.getLevel(), be.getBlockPos())
                : ContainerLevelAccess.NULL;
        this.be = be;
        buildSlots(inv, be);
        this.addDataSlots(this.data);
    }

// 构建槽位

    private void buildSlots(Inventory inv, AbstractCookingBlockEntity be) {
        int inputCount = applianceType.getInputSlots();

// —— 输入槽 ——
        if (applianceType == ApplianceType.PRESSURECOOKER) {
            for (int i = 0; i < inputCount; i++) {
                addSlot(new Slot(be, i,
                    pressurecookerInputX(i),
                    pressurecookerInputY(i)));
            }
        } else if (applianceType == ApplianceType.POT) {
            for (int i = 0; i < inputCount; i++) {
                addSlot(new Slot(be, i,
                    potInputX(i),
                    potInputY(i)));
            }
        } else {
            for (int i = 0; i < inputCount; i++) {
                int cols = inputCols(inputCount);
                int row = i / cols;
                int col = i % cols;
                addSlot(new Slot(be, i, inputX(inputCount, col), inputY(inputCount, row)));
            }
        }

// —— 输出槽 ——
        addSlot(new Slot(be, applianceType.getOutputSlot(),
                outputX(applianceType), outputY(applianceType)) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
        });

// —— 容器槽（仅 pot 等有容器的厨具）——
        if (applianceType.hasContainer()) {
            addSlot(new Slot(be, applianceType.getContainerSlot(),
                    containerX(), containerY()));
        }

// —— 玩家背包 ——
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                addSlot(new Slot(inv, 9 + row * 9 + col,
                        PLAYER_X + col * BORDER, PLAYER_INV_Y + row * BORDER));
            }
        }

// —— 快捷栏 ——
        for (int col = 0; col < 9; ++col) {
            addSlot(new Slot(inv, col, PLAYER_X + col * BORDER, HOTBAR_Y));
        }
    }

// 公共接口（供 Screen 调用）

    public ApplianceType getApplianceType() { return applianceType; }

    public int getCookingProgress() { return data.get(0); }

    public int getCookingTotalTime() { return data.get(1); }

    public int getHeatLevel() { return data.get(2); }

// 烘干机模式：0=烘干，1=烤箱
    public int getDryerMode() { return data.get(3); }

// 容器逻辑

// 处理烘干机模式按钮：0=烘干模式，1=烤箱模式
    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (applianceType != ApplianceType.DRYER) return false;
        if (id == 0 || id == 1) {
            be.setDryerMode(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, be.getBlockState().getBlock());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            result = slotStack.copy();

            int containerStart = applianceType.getInventorySize();
            int containerEnd = containerStart + 36;
            int outputIdx = applianceType.getOutputSlot();

            if (index == outputIdx) {
                if (!moveItemStackTo(slotStack, containerStart, containerEnd, true))
                    return ItemStack.EMPTY;
            } else if (index >= containerStart) {
                if (!moveItemStackTo(slotStack, 0, applianceType.getInputSlots(), false))
                    return ItemStack.EMPTY;
            } else {
                if (!moveItemStackTo(slotStack, containerStart, containerEnd, false))
                    return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
            if (slotStack.getCount() == result.getCount()) return ItemStack.EMPTY;
            slot.onTake(player, slotStack);
        }
        return result;
    }
}
