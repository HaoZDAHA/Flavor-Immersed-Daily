package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.screen.MooncakeMoldMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.List;

// 月饼模具 — 内置两个槽位（饼皮 + 馅料），材料保存在模具 NBT 中。
//
// 交互流程：
// 1. 右键打开 GUI，放入饼皮（限 5 种面团）与馅料（任意物品）；
// 2. 关闭 GUI 后材料保留在模具中；
// 3. 再次右键即加工：按 min(饼皮数, 馅料数) 批量生成对应生月饼
// （带 "filling" NBT，记录馅料物品注册名），多余材料返回玩家背包。
public class MooncakeMoldItem extends Item {

// NBT 标签：饼皮物品注册名 / 数量
    private static final String TAG_DOUGH_ID = "mold_dough_id";
    private static final String TAG_DOUGH_COUNT = "mold_dough_count";
// NBT 标签：馅料物品注册名 / 数量
    private static final String TAG_FILLING_ID = "mold_filling_id";
    private static final String TAG_FILLING_COUNT = "mold_filling_count";

    public MooncakeMoldItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            ItemStack dough = getStoredDough(stack);
            ItemStack filling = getStoredFilling(stack);
            Item output = getRawMooncakeForDough(dough.getItem());

// 已有完整配对（饼皮为 5 种限定面团之一且馅料非空）→ 加工；否则打开 GUI 供调整/取回
            if (!dough.isEmpty() && !filling.isEmpty() && output != null) {
                process(stack, dough, filling, output, player, level);
            } else {
                openMenu(player, stack);
            }
        }
        return InteractionResultHolder.consume(stack);
    }

// 打开模具 GUI
    private static void openMenu(Player player, ItemStack mold) {
        player.openMenu(new SimpleMenuProvider(
                (containerId, playerInventory, p) -> new MooncakeMoldMenu(containerId, playerInventory, mold),
                Component.translatable("container.flavor_immersed_daily.mooncake_mold")));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.mooncake_mold.desc1")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.mooncake_mold.desc2")
                .withStyle(ChatFormatting.GRAY));

// 已放入的材料（仅在存在时显示）
        ItemStack dough = getStoredDough(stack);
        if (!dough.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.mooncake_mold.stored_dough",
                    dough.getHoverName(), dough.getCount()).withStyle(ChatFormatting.AQUA));
        }
        ItemStack filling = getStoredFilling(stack);
        if (!filling.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.mooncake_mold.stored_filling",
                    filling.getHoverName(), filling.getCount()).withStyle(ChatFormatting.AQUA));
        }
    }

// 加工：按材料配对数量生成生月饼，多余材料返回背包
    private static void process(ItemStack mold, ItemStack dough, ItemStack filling, Item output,
                                Player player, Level level) {
        int pairs = Math.min(dough.getCount(), filling.getCount());
        String fillingId = BuiltInRegistries.ITEM.getKey(filling.getItem()).toString();

        for (int i = 0; i < pairs; i++) {
            ItemStack result = new ItemStack(output, 1);
            result.set(DataComponents.CUSTOM_DATA, FillingFoodItem.ofFilling(fillingId));
            giveToPlayer(player, result);
        }

// 多余材料返回玩家背包
        int doughLeft = dough.getCount() - pairs;
        int fillingLeft = filling.getCount() - pairs;
        if (doughLeft > 0) giveToPlayer(player, dough.copyWithCount(doughLeft));
        if (fillingLeft > 0) giveToPlayer(player, filling.copyWithCount(fillingLeft));

        clearStored(mold);
        level.playSound(null, player.blockPosition(), SoundEvents.UI_STONECUTTER_TAKE_RESULT,
                SoundSource.PLAYERS, 1.0f, 1.0f);
    }

// 放入玩家背包，背包满则掉落在地上
    private static void giveToPlayer(Player player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

// 饼皮 → 生月饼 映射；非法饼皮返回 null
    public static Item getRawMooncakeForDough(Item dough) {
        if (dough == ModItems.RAWFLOURPANCAKE.get()) return ModItems.RAWMOONCAKE.get();
        if (dough == ModItems.SWEET_OIL_DOUGH.get()) return ModItems.RAWCANTONESE_MOONCAKE.get();
        if (dough == ModItems.WATER_SHORT_DOUGH.get()) return ModItems.RAWSUZHOU_MOONCAKE.get();
        if (dough == ModItems.SYRUP_DOUGH.get()) return ModItems.RAWBEIJING_MOONCAKE.get();
        if (dough == ModItems.GLUTINOUS_MILK_DOUGH.get()) return ModItems.RAWSNOW_SKIN_MOONCAKE.get();
        return null;
    }

// 模具 NBT 存取

    private static CompoundTag getData(ItemStack mold) {
        return mold.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    private static void putData(ItemStack mold, CompoundTag tag) {
// 客户端空模具不写 NBT
        if (mold.isEmpty()) return;
        if (tag.isEmpty()) {
            mold.remove(DataComponents.CUSTOM_DATA);
        } else {
            mold.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
    }

// 读取存储的饼皮
    public static ItemStack getStoredDough(ItemStack mold) {
        return readStack(getData(mold), TAG_DOUGH_ID, TAG_DOUGH_COUNT);
    }

// 读取存储的馅料
    public static ItemStack getStoredFilling(ItemStack mold) {
        return readStack(getData(mold), TAG_FILLING_ID, TAG_FILLING_COUNT);
    }

// 将两个槽位内容写回模具 NBT
    public static void saveStored(ItemStack mold, ItemStack dough, ItemStack filling) {
        CompoundTag tag = getData(mold);
        writeStack(tag, dough, TAG_DOUGH_ID, TAG_DOUGH_COUNT);
        writeStack(tag, filling, TAG_FILLING_ID, TAG_FILLING_COUNT);
        putData(mold, tag);
    }

// 清空模具存储
    public static void clearStored(ItemStack mold) {
        CompoundTag tag = getData(mold);
        tag.remove(TAG_DOUGH_ID);
        tag.remove(TAG_DOUGH_COUNT);
        tag.remove(TAG_FILLING_ID);
        tag.remove(TAG_FILLING_COUNT);
        putData(mold, tag);
    }

    private static ItemStack readStack(CompoundTag tag, String idKey, String countKey) {
        String id = tag.getString(idKey);
        if (id.isEmpty()) return ItemStack.EMPTY;
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(id));
        if (item == Items.AIR) return ItemStack.EMPTY;
        int count = Math.max(1, Math.min(tag.getInt(countKey), item.getDefaultMaxStackSize()));
        return new ItemStack(item, count);
    }

    private static void writeStack(CompoundTag tag, ItemStack stack, String idKey, String countKey) {
        if (stack.isEmpty()) {
            tag.remove(idKey);
            tag.remove(countKey);
        } else {
            tag.putString(idKey, BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            tag.putInt(countKey, stack.getCount());
        }
    }
}
