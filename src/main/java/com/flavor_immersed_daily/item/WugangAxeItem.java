package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.all.ModEnchantments;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;

// 吴刚之斧 — 基础性能与原版钻石斧完全一致，并天生自带 3 级无限砍伐附魔。
// 1.20.1 的附魔以 NBT 存在物品堆上，且没有 createComponents/verifyAfterLoad 之类的通用钩子，
// 因此「天生附魔」由各产出点（创造标签页、村民交易、地牢战利品）通过 #makeEnchanted
// 写入附魔 NBT，配合 InfiniteLoggingHandler 中对本物品的保底判断共同保证。
public class WugangAxeItem extends AxeItem {

// 天生附魔等级
    public static final int INNATE_LEVEL = 3;

    public WugangAxeItem(Properties properties) {
// 与原版钻石斧一致：攻击伤害 6.0，攻速 -3.1
        super(Tiers.DIAMOND, 6.0F, -3.1F, properties);
    }

// 制造一柄天生带 3 级无限砍伐附魔的吴刚之斧，供创造标签页/村民交易等地使用
    public static ItemStack makeEnchanted(Item item) {
        ItemStack stack = new ItemStack(item);
        stack.enchant(ModEnchantments.INFINITE_LOGGING.get(), INNATE_LEVEL);
        return stack;
    }
}