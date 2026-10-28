package com.flavor_immersed_daily.gameplay;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.item.WugangAxeItem;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

// 吴刚之斧的村民交易获取途径：工具匠（Toolsmith）大师级可用大量绿宝石兑换。
@Mod.EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
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

        event.getTrades().computeIfAbsent(MASTER_LEVEL, k -> new ArrayList<>())
                .add(new BasicItemListing(PRICE_EMERALDS,
                        WugangAxeItem.makeEnchanted(ModItems.WUGANG_AXE.get()), MAX_TRADES, XP, 0.05F));
    }
}