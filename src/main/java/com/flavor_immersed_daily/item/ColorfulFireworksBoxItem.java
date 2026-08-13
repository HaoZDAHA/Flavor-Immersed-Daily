package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.block.blockentity.ColorfulFireworksBoxBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

/**
 * 箱装烟花物品 — tooltip 显示内部烟花参数
 */
public class ColorfulFireworksBoxItem extends BlockItem {

    public ColorfulFireworksBoxItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        CustomData customData = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (customData == null || customData.copyTag().isEmpty()) {
            // 无数据时显示特色说明
            tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.fireworks.desc_line1")
                    .withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.fireworks.desc_line2")
                    .withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.fireworks.desc_line3")
                    .withStyle(ChatFormatting.GRAY));
            return;
        }
        CompoundTag beTag = customData.copyTag();

        // 形状
        int shape = beTag.getInt("fw_shape");
        String shapeName = shape >= 0 && shape < ColorfulFireworksBoxBlockEntity.SHAPE_NAMES.length
                ? ColorfulFireworksBoxBlockEntity.SHAPE_NAMES[shape] : "unknown";
        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.fireworks.shape", shapeName)
                .withStyle(ChatFormatting.GRAY));

        // 颜色
        int color = beTag.getInt("fw_color");
        int fadeColor = beTag.getInt("fw_fade_color");
        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.fireworks.color",
                        String.format("#%06X", color & 0xFFFFFF))
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.fireworks.fade_color",
                        String.format("#%06X", fadeColor & 0xFFFFFF))
                .withStyle(ChatFormatting.GRAY));

        // 轨迹
        boolean trail = beTag.getBoolean("fw_trail");
        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.fireworks.trail",
                        Component.translatable(trail ? "gui.yes" : "gui.no"))
                .withStyle(ChatFormatting.GRAY));

        // 飞行参数
        float angle = beTag.getFloat("fw_angle");
        float speed = beTag.getFloat("fw_speed");
        float distance = beTag.getFloat("fw_distance");
        float curveA = beTag.getFloat("fw_curve_a");
        float curveB = beTag.getFloat("fw_curve_b");
        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.fireworks.angle", String.format("%.1f", angle))
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.fireworks.speed", String.format("%.1f", speed))
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.fireworks.distance", String.format("%.1f", distance))
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.fireworks.curve_a", String.format("%.1f", curveA))
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.fireworks.curve_b", String.format("%.1f", curveB))
                .withStyle(ChatFormatting.GRAY));
    }
}