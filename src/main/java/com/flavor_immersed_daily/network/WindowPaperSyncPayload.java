package com.flavor_immersed_daily.network;

import com.flavor_immersed_daily.entity.WindowPaperEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

// 窗纸像素数据同步消息 — 客户端 → 服务端
// 使用 int[] 存储每个像素的 ARGB 颜色值
public class WindowPaperSyncPayload {
    private final int entityId;
    private final int[] pixelData;

    public WindowPaperSyncPayload(int entityId, int[] pixelData) {
        this.entityId = entityId;
        this.pixelData = pixelData;
    }

    public int entityId() {
        return entityId;
    }

    public int[] pixelData() {
        return pixelData;
    }

    public static void encode(WindowPaperSyncPayload msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        for (int i = 0; i < 256; i++) {
            buf.writeInt(msg.pixelData[i]);
        }
    }

    public static WindowPaperSyncPayload decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        int[] data = new int[256];
        for (int i = 0; i < 256; i++) {
            data[i] = buf.readInt();
        }
        return new WindowPaperSyncPayload(entityId, data);
    }

    public static void handle(WindowPaperSyncPayload msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null && player.level() instanceof ServerLevel serverLevel
                    && serverLevel.getEntity(msg.entityId) instanceof WindowPaperEntity entity) {
                entity.applyConfig(msg);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
