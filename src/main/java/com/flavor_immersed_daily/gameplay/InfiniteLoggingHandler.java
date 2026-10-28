package com.flavor_immersed_daily.gameplay;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEnchantments;
import com.flavor_immersed_daily.item.WugangAxeItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// 无限砍伐附魔效果 — 每次破坏原木后，按等级有概率在原位置还原被破坏的原木方块。
// 1 级 20%，2 级 50%，3 级 80%，每个方块独立判定。
@Mod.EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class InfiniteLoggingHandler {

// 各等级还原概率：1 级 20%，2 级 50%，3 级 80%
    private static final float[] RESTORE_CHANCES = {0.2F, 0.5F, 0.8F};

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.isCanceled()) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        BlockState state = event.getState();
        if (!state.is(BlockTags.LOGS)) return;

        Player player = event.getPlayer();
        int enchantLevel = getInfiniteLoggingLevel(player.getMainHandItem());
        if (enchantLevel <= 0) return;

        if (level.random.nextFloat() >= restoreChance(enchantLevel)) return;

        BlockPos pos = event.getPos();
// 延迟到本次破坏流程结束后再还原，否则会被原版的方块移除覆盖
        level.getServer().execute(() -> {
            if (level.getBlockState(pos).isAir()) {
                level.setBlockAndUpdate(pos, state);
            }
        });
    }

// 读取工具上的无限砍伐等级；未附魔返回 0。吴刚之斧保底按 3 级（天生附魔）
    private static int getInfiniteLoggingLevel(ItemStack tool) {
        if (tool.isEmpty()) return 0;
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.INFINITE_LOGGING.get(), tool);
        if (tool.getItem() instanceof WugangAxeItem && level < WugangAxeItem.INNATE_LEVEL) {
            return WugangAxeItem.INNATE_LEVEL;
        }
        return level;
    }

// 按等级取还原概率
    private static float restoreChance(int enchantLevel) {
        return RESTORE_CHANCES[Math.min(enchantLevel, RESTORE_CHANCES.length) - 1];
    }
}