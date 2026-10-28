package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.client.tooltip.FillingTooltip;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.Optional;

// 带馅料口味的食物（生/熟包子、饺子、馄饨、汤圆、月饼、馅饼、粽子等）。
//
// NBT 标签 "filling"（存于 CUSTOM_DATA）记录馅料的物品注册名，
// tooltip 中渲染为 "馅料口味：&lt;馅料图标&gt; &lt;馅料名称&gt;"。
public class FillingFoodItem extends Item {

// NBT 标签名：馅料物品注册名，如 flavor_immersed_daily:meatpaste
    public static final String FILLING_NBT_KEY = "filling";

    public FillingFoodItem(Properties properties) {
        super(properties);
    }

// 生成带 "filling" 标签的默认组件数据
    public static CustomData ofFilling(String fillingId) {
        CompoundTag tag = new CompoundTag();
        tag.putString(FILLING_NBT_KEY, fillingId);
        return CustomData.of(tag);
    }

// 读取物品记录的馅料物品；标签为空、格式错误或物品未安装时返回 null
    public static Item getFillingItem(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        String fillingId = tag.getString(FILLING_NBT_KEY);
        if (fillingId.isEmpty()) return null;

        ResourceLocation id = ResourceLocation.tryParse(fillingId);
        if (id == null) return null;

        Item item = BuiltInRegistries.ITEM.get(id);
        return item == Items.AIR ? null : item;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        ItemStack fillingStack = createFillingStack(stack);
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);
        if (fillingStack != null) {
            eatFillingOnce(fillingStack, level, livingEntity);
        }
        return result;
    }

// 构造馅料的物品堆；无馅料时返回 null
    private static ItemStack createFillingStack(ItemStack stack) {
        Item filling = getFillingItem(stack);
        return filling == null ? null : new ItemStack(filling);
    }

// 馅料可食用时，额外附加一次馅料的食用效果。
//
// 复用原版食用流程（音效、馅料自带状态效果、GameEvent.EAT），
// 但把饱食度固定为 0，因此只加饱和度和食用事件、不加饱食度。
    private static void eatFillingOnce(ItemStack fillingStack, Level level, LivingEntity entity) {
        FoodProperties filling = fillingStack.get(DataComponents.FOOD);
        if (filling == null) return;

        FoodProperties saturationOnly = new FoodProperties(
                0, filling.saturation(), filling.canAlwaysEat(), filling.eatSeconds(),
                Optional.empty(), filling.effects());
        entity.eat(level, fillingStack, saturationOnly);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        Item filling = getFillingItem(stack);
        if (filling == null) return Optional.empty();
        return Optional.of(new FillingTooltip(new ItemStack(filling)));
    }
}
