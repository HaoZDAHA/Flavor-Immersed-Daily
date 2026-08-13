package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.all.ModEntities;
import com.flavor_immersed_daily.entity.WindowPaperEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public class WindowPaperItem extends HangingEntityItem {
    private static final Component TOOLTIP = Component.translatable("tooltip.flavor_immersed_daily.windowpaper");

    public WindowPaperItem(Item.Properties properties) {
        super(ModEntities.WINDOW_PAPER_ENTITY.get(), properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(TOOLTIP);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos target = context.getClickedPos().relative(context.getClickedFace());
        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        Direction direction = context.getClickedFace();
        if (player != null && !mayPlace(player, direction, itemStack, target)) return InteractionResult.FAIL;
        Level level = context.getLevel();
        WindowPaperEntity entity = new WindowPaperEntity(ModEntities.WINDOW_PAPER_ENTITY.get(), level, target, direction);
        if (!entity.survives()) return InteractionResult.CONSUME;
        if (!level.isClientSide) {
            entity.playPlacementSound();
            level.gameEvent(player, GameEvent.ENTITY_PLACE, entity.position());
            level.addFreshEntity(entity);
        }
        itemStack.shrink(1);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
