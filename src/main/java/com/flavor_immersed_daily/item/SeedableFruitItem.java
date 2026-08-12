package com.flavor_immersed_daily.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeedableFruitItem extends Item {

    private final String seedItemId;

    public SeedableFruitItem(Properties properties, String seedItemId) {
        super(properties);
        this.seedItemId = seedItemId;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                Item seedItem = BuiltInRegistries.ITEM.get(ResourceLocation.parse(seedItemId));
                if (seedItem == null || seedItem == BuiltInRegistries.ITEM.get(ResourceLocation.parse("air"))) {
                    return InteractionResultHolder.pass(stack);
                }
                ItemStack seedStack = new ItemStack(seedItem, 4);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                if (!player.addItem(seedStack)) {
                    player.drop(seedStack, false);
                }
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.flavor_immersed_daily.seedable_fruit.desc1").withStyle(net.minecraft.ChatFormatting.GRAY));
    }
}