package com.flavor_immersed_daily.gameplay;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.enchantment.ModEnchantments;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

// 吴刚之斧的村民交易获取途径：工具匠（Toolsmith）大师级可用大量绿宝石兑换。
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class WugangAxeTradeHandler {

// 工具匠大师级
    private static final int MASTER_LEVEL = 5;
// 兑换价格（绿宝石）
    private static final int PRICE_EMERALDS = 32;
// 可交易次数
    private static final int MAX_TRADES = 2;
// 交易经验
    private static final int XP = 30;

    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() != VillagerProfession.TOOLSMITH) return;

// 本事件由 NeoForge 在数据包加载时触发（服务端侧），早于模组自身的 TagsUpdatedEvent 监听，
// 因此这里先刷新服务端附魔 Holder，确保生成的斧头能带上天生附魔。
        ModEnchantments.bindServerHolder(event.getRegistryAccess());

        event.getTrades().get(MASTER_LEVEL).add(new BasicItemListing(
                PRICE_EMERALDS, new ItemStack(ModItems.WUGANG_AXE.get()), MAX_TRADES, XP, 0.05F));
    }
}
