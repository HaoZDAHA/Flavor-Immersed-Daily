package com.flavor_immersed_daily.network;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

// forge 1.20.1 网络通道（SimpleChannel），替代 neoforge 的 payload 系统。
// 所有消息均为 客户端 → 服务端。
public final class ModMessages {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int nextId = 0;

    private ModMessages() {
    }

    public static void register() {
        CHANNEL.registerMessage(nextId++, ColorfulFireworksBoxSyncPayload.class,
                ColorfulFireworksBoxSyncPayload::encode,
                ColorfulFireworksBoxSyncPayload::decode,
                ColorfulFireworksBoxSyncPayload::handle);
        CHANNEL.registerMessage(nextId++, WindowPaperSyncPayload.class,
                WindowPaperSyncPayload::encode,
                WindowPaperSyncPayload::decode,
                WindowPaperSyncPayload::handle);
        CHANNEL.registerMessage(nextId++, CoupletSyncPayload.class,
                CoupletSyncPayload::encode,
                CoupletSyncPayload::decode,
                CoupletSyncPayload::handle);
        CHANNEL.registerMessage(nextId++, RaddlesSyncPayload.class,
                RaddlesSyncPayload::encode,
                RaddlesSyncPayload::decode,
                RaddlesSyncPayload::handle);
        CHANNEL.registerMessage(nextId++, RaddlesAnswerPayload.class,
                RaddlesAnswerPayload::encode,
                RaddlesAnswerPayload::decode,
                RaddlesAnswerPayload::handle);
        CHANNEL.registerMessage(nextId++, RaddlesResultPayload.class,
                RaddlesResultPayload::encode,
                RaddlesResultPayload::decode,
                RaddlesResultPayload::handle);
    }
}
