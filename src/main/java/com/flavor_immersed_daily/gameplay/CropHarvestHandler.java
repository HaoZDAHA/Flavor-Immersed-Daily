package com.flavor_immersed_daily.gameplay;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.block.common.block.FIDCropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// 作物右键收割。
// 玩家手持物品右键成熟作物时，物品自身的 useOn 可能在方块交互之前抢占并返回 FAIL，
// 导致方块层的收获逻辑根本不被触发（表现为零掉落）。这里用 PlayerInteractEvent.RightClickBlock
// ——它是交互管线的第一步，先于任何物品动作——直接让成熟作物掉战利品（即该作物的产物，例如这些
// 香料作物掉落种子），从而保证无论手里拿的是什么（种子、工具、空手）都能正常收获。
@Mod.EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class CropHarvestHandler {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        Level level = event.getLevel();
        if (level.isClientSide) return;

        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

// 只有本模组的作物才参与收获，其余方块直接放行，避免与其他模组（如机械动力）的交互冲突
        if (!(state.getBlock() instanceof FIDCropBlock crop)) return;

// 未成熟的作物交给默认交互（例如催熟/种植），不在此处理
        if (state.getValue(crop.getAgeProperty()) < crop.getMaxAge()) return;

        event.getEntity().swing(InteractionHand.MAIN_HAND);
// 直接掉战利品（产物即种子），并重置作物
        crop.harvest(state, level, pos);
// 取消后续默认交互，避免物品放置抢占或重复触发
        event.setCanceled(true);
    }
}
