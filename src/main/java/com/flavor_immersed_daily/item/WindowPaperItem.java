package com.flavor_immersed_daily.item;

import com.flavor_immersed_daily.FlavorImmersedDaily;
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

/**
 * 窗纸物品 — 右键墙面放置实体（参考原版画 PaintingItem 的放置逻辑）
 */
public class WindowPaperItem extends HangingEntityItem {

    private static final Component TOOLTIP = Component.translatable("tooltip.flavor_immersed_daily.windowpaper");

    public WindowPaperItem(Item.Properties properties) {
        super(FlavorImmersedDaily.WINDOW_PAPER_ENTITY.get(), properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(TOOLTIP);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos blockpos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockPos blockpos1 = blockpos.relative(direction);
        Player player = context.getPlayer();
        ItemStack itemstack = context.getItemInHand();

        if (player != null && !this.mayPlace(player, direction, itemstack, blockpos1)) {
            return InteractionResult.FAIL;
        }

        Level level = context.getLevel();
        WindowPaperEntity entity = new WindowPaperEntity(
                FlavorImmersedDaily.WINDOW_PAPER_ENTITY.get(), level, blockpos1, direction);

        if (entity.survives()) {
            if (!level.isClientSide) {
                entity.playPlacementSound();
                level.gameEvent(player, GameEvent.ENTITY_PLACE, entity.position());
                level.addFreshEntity(entity);
            }
            itemstack.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.CONSUME;
    }
}