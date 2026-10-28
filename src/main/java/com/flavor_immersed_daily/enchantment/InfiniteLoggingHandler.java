package com.flavor_immersed_daily.enchantment;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

// 无限砍伐附魔效果 — 每次破坏原木后，按等级有概率在原位置还原被破坏的原木方块。
//
// 1 级 20%，2 级 50%，3 级 80%，每个方块独立判定。
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class InfiniteLoggingHandler {

// 各等级还原概率：1 级 20%，2 级 50%，3 级 80%
    private static final float[] RESTORE_CHANCES = {0.2F, 0.5F, 0.8F};

// 刷新附魔 Holder，供吴刚之斧写入天生附魔使用。
// 本事件在客户端与服务端各触发一次，两端的附魔注册表互不相通，因此必须按触发来源
// 分别刷新：服务端加载数据包时刷新服务端 Holder，客户端收到同步数据包时刷新客户端 Holder。
    @SubscribeEvent
    public static void onTagsUpdated(TagsUpdatedEvent event) {
        if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) {
            ModEnchantments.bindServerHolder(event.getRegistryAccess());
        } else {
            ModEnchantments.bindClientHolder(event.getRegistryAccess());
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.isCanceled()) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        BlockState state = event.getState();
        if (!state.is(BlockTags.LOGS)) return;

        Player player = event.getPlayer();
        int enchantLevel = getInfiniteLoggingLevel(player.getMainHandItem(), level);
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

// 读取工具上的无限砍伐等级；未附魔返回 0
    private static int getInfiniteLoggingLevel(ItemStack tool, Level level) {
        if (tool.isEmpty()) return 0;
        return level.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(ModEnchantments.INFINITE_LOGGING)
                .map(holder -> tool.getEnchantments().getLevel(holder))
                .orElse(0);
    }

// 按等级取还原概率
    private static float restoreChance(int enchantLevel) {
        return RESTORE_CHANCES[Math.min(enchantLevel, RESTORE_CHANCES.length) - 1];
    }
}
