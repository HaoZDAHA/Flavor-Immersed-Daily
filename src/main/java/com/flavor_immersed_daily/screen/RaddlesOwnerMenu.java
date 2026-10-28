package com.flavor_immersed_daily.screen;

import com.flavor_immersed_daily.all.ModMenus;
import com.flavor_immersed_daily.block.blockentity.RaddlesBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;

// 灯谜牌放置者编辑界面菜单 — 16 格奖励物品栏 + 玩家背包。
// 服务端持有灯谜牌方块实体的活引用，槽位内容直接读写该实体，关闭界面后奖励仍然保留；
// 客户端用占位容器接收服务端同步的槽位内容，方块坐标经额外数据传入，
// 界面通过 #getBlockPos() 定位本地方块实体读取牌面文字与谜面。
public class RaddlesOwnerMenu extends AbstractContainerMenu {

// 奖励物品栏格数
    public static final int REWARD_SLOTS = RaddlesBlockEntity.REWARD_SLOTS;
// 奖励格每行个数
    public static final int REWARD_COLUMNS = 8;

// GUI 布局（界面与菜单共用，避免两处硬编码不一致）
    public static final int IMAGE_WIDTH = 220;
    public static final int IMAGE_HEIGHT = 240;
// 奖励格起点
    public static final int REWARD_X = 56;
    public static final int REWARD_Y = 106;
// 玩家背包起点
    public static final int PLAYER_INV_X = 8;
    public static final int PLAYER_INV_Y = 160;
// 快捷栏 Y
    public static final int HOTBAR_Y = 218;
    public static final int INVENTORY_LABEL_Y = 148;

    private static final int PLAYER_INV_START = REWARD_SLOTS;
    private static final int PLAYER_INV_END = PLAYER_INV_START + 36;

    private final Container rewardContainer;
    @Nullable
    private final BlockPos pos;

// 客户端构造 — 服务端把方块坐标写在额外数据里
    public RaddlesOwnerMenu(int containerId, Inventory playerInventory, FriendlyByteBuf data) {
        this(containerId, playerInventory, new SimpleContainer(REWARD_SLOTS),
                data != null ? data.readBlockPos() : null);
    }

// 服务端构造 — 槽位内容直接读写灯谜牌方块实体
    public RaddlesOwnerMenu(int containerId, Inventory playerInventory, RaddlesBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity, blockEntity.getBlockPos());
    }

    private RaddlesOwnerMenu(int containerId, Inventory playerInventory, Container rewardContainer,
                             @Nullable BlockPos pos) {
        super(ModMenus.RADDLES_OWNER_MENU.get(), containerId);
        this.rewardContainer = rewardContainer;
        this.pos = pos;

// 奖励物品栏：8 列 × 2 行
        for (int i = 0; i < REWARD_SLOTS; i++) {
            int row = i / REWARD_COLUMNS;
            int col = i % REWARD_COLUMNS;
            this.addSlot(new Slot(rewardContainer, i,
                    REWARD_X + col * 18, REWARD_Y + row * 18));
        }

// 玩家背包 3 行 × 9 列
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9,
                        PLAYER_INV_X + col * 18, PLAYER_INV_Y + row * 18));
            }
        }

// 玩家快捷栏 1 行 × 9 列
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col,
                    PLAYER_INV_X + col * 18, HOTBAR_Y));
        }
    }

    @Nullable
    public BlockPos getBlockPos() {
        return pos;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();

            if (index < REWARD_SLOTS) {
// 奖励格 → 玩家背包
                if (!this.moveItemStackTo(stack, PLAYER_INV_START, PLAYER_INV_END, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
// 玩家背包 → 奖励格
                if (!this.moveItemStackTo(stack, 0, REWARD_SLOTS, false)) {
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
        return this.rewardContainer.stillValid(player);
    }
}