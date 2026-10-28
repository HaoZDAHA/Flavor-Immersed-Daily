package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.enchantment.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

// 吴刚之斧 — 性能与钻石斧完全一致，且天生自带 3 级无限砍伐附魔。
//
// 附魔是数据驱动注册表，无法在物品注册时写入默认组件，因此在每次物品堆生成后
// （#verifyComponentsAfterLoad）补上附魔组件，这样创造模式标签页、地牢战利品、
// 村民交易等所有途径获得的斧头都自带附魔，并且能正常显示在 tooltip 上。
public class WugangAxeItem extends AxeItem {

// 天生附魔等级
    private static final int INNATE_LEVEL = 3;

    public WugangAxeItem(Properties properties) {
        super(Tiers.DIAMOND, properties);
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack stack) {
// 必须取当前端注册表里的 Holder：客户端与服务端的附魔注册表互不相通，
// 用错端的 Holder 会让物品在存档/发包序列化时报 Can't find id for ... 而崩溃。
        Holder<Enchantment> holder = ModEnchantments.holderForCurrentSide();
        if (holder == null) return;

        ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (enchantments.getLevel(holder) >= INNATE_LEVEL) return;

        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(enchantments);
        mutable.set(holder, INNATE_LEVEL);
        stack.set(DataComponents.ENCHANTMENTS, mutable.toImmutable());
    }
}
