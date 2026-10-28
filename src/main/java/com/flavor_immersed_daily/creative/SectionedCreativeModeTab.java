package com.flavor_immersed_daily.creative;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

// 带横幅分区的创造模式标签页，效果参考捕星者（Starcatcher）的物品栏横幅。
// 原版 CreativeModeTab.ItemDisplayBuilder#accept 要求物品数量必须为 1
// （直接塞 ItemStack#EMPTY 会抛异常），也没有替换 displayItems 的入口。
// 因此这里换一条路：用 Builder#withTabFactory 让平台构造本子类，
// 再覆写公开的 #getDisplayItems()——创造模式界面取物品走的正是它——
// 在原有物品之间插入「横幅行」与整行留白。
public class SectionedCreativeModeTab extends CreativeModeTab {

// 创造模式物品栏每行 9 格
    private static final int SLOTS_PER_ROW = 9;

// 横幅占位栈 → 所属分区。
// 占位栈的物品是空气，isEmpty() 为 true，而 ItemStack#getComponents()
// 对空栈一律返回空表——组件写进去也读不出来，所以这里改用对象同一性来认。
// 占位栈会被原样塞进创造界面的槽位（列表 → ItemPickerMenu#items →
// CONTAINER.setItem → Slot#getItem 全程传引用、不拷贝），
// 渲染时拿槽位里的那个对象就能查回分区。弱键，占位栈被回收后条目自动消失。
    private static final Map<ItemStack, Section> BANNERS =
            Collections.synchronizedMap(new WeakHashMap<>());

// 已展开的分区 id。
// 空白集合 = 所有分区进来时都是收敛的：只看见几条分区标题，想翻哪个再点开。
// 纯粹是界面状态：折叠的分区物品不再进入物品列表，客户端下一次取列表时生效。
// 与存档无关，退出游戏即失效。
    private static final Set<String> EXPANDED = ConcurrentHashMap.newKeySet();

    private final List<Section> sections;

    public SectionedCreativeModeTab(CreativeModeTab.Builder builder, List<Section> sections) {
        super(builder);
        this.sections = List.copyOf(sections);
    }

// 一个分区。
// @param id 分区标识，同时也是折叠状态的键，需在同一模组内唯一
// @param title 横幅上显示的分区名
// @param texture 该分区横幅专用的纹理；宽 162、高 18（整行 9 格 × 18）
// @param itemCount 紧随横幅的物品数量；传负数表示「余下的全部物品」
    public record Section(String id, Component title, ResourceLocation texture, int itemCount) {
    }

// 该分区是否折叠着（默认就是折叠的，点开才算展开）
    public static boolean isCollapsed(Section section) {
        return !EXPANDED.contains(section.id());
    }

// 折叠／展开该分区
    public static void toggleCollapsed(Section section) {
        if (!EXPANDED.remove(section.id())) {
            EXPANDED.add(section.id());
        }
    }

    @Override
    public Collection<ItemStack> getDisplayItems() {
        return this.layout(super.getDisplayItems());
    }

// 把扁平的物品列表重排成「横幅行 + 该分区物品 + 补齐整行」的形式
    private List<ItemStack> layout(Collection<ItemStack> original) {
        List<ItemStack> source = new ArrayList<>(original);
        List<ItemStack> result = new ArrayList<>(source.size() + this.sections.size() * (SLOTS_PER_ROW + 1));
        int index = 0;
        for (int i = 0; i < this.sections.size(); i++) {
            Section section = this.sections.get(i);
// 横幅独占一整行：只有最左格放标记，其余补空槽
            result.add(bannerMarker(section));
            for (int slot = 1; slot < SLOTS_PER_ROW; slot++) {
                result.add(ItemStack.EMPTY);
            }
            int count = section.itemCount() < 0 ? source.size() - index : section.itemCount();
// 折叠的分区只留横幅：物品照样从源列表里跳过，后面的分区才不会错位
            int emitted = 0;
            if (isCollapsed(section)) {
                index += count;
            } else {
                for (int taken = 0; taken < count && index < source.size(); taken++) {
                    result.add(source.get(index++));
                    emitted++;
                }
            }
// 分区之间补齐整行，保证下一条横幅从新的一行开始
            if (i < this.sections.size() - 1) {
                int remainder = emitted % SLOTS_PER_ROW;
                int padding = remainder == 0 ? 0 : SLOTS_PER_ROW - remainder;
                for (int slot = 0; slot < padding; slot++) {
                    result.add(ItemStack.EMPTY);
                }
            }
        }
// 声明数量没覆盖到的物品原样接在最后，避免漏掉
        while (index < source.size()) {
            result.add(source.get(index++));
        }
        return result;
    }

// 造一个横幅占位栈：空气物品，与原版空槽表现完全一致——不渲染图标、不出提示框、
// 也不可拾取，只是让客户端认出「这一格是横幅」。
    private static ItemStack bannerMarker(Section section) {
        ItemStack stack = new ItemStack(Items.AIR);
        BANNERS.put(stack, section);
        return stack;
    }

// 取横幅占位栈对应的分区。
// @return 所属分区；不是横幅占位时返回 null
    @Nullable
    public static Section bannerOf(ItemStack stack) {
        return BANNERS.get(stack);
    }
}
