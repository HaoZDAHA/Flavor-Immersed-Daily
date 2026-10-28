package com.flavor_immersed_daily.network;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

// 窗纸像素数据同步包 — 客户端 → 服务端
// 使用 int[] 存储每个像素的 ARGB 颜色值
public record WindowPaperSyncPayload(int entityId, int[] pixelData) implements CustomPacketPayload {

    public static final Type<WindowPaperSyncPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "window_paper_config"));

// 自定义 int[] 编解码器：写入/读取 256 个 int
    private static final StreamCodec<ByteBuf, int[]> INT_ARRAY_CODEC = new StreamCodec<>() {
        @Override
        public int[] decode(ByteBuf buf) {
            int[] data = new int[256];
            for (int i = 0; i < 256; i++) {
                data[i] = buf.readInt();
            }
            return data;
        }

        @Override
        public void encode(ByteBuf buf, int[] data) {
            for (int i = 0; i < 256; i++) {
                buf.writeInt(data[i]);
            }
        }
    };

    public static final StreamCodec<ByteBuf, WindowPaperSyncPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, WindowPaperSyncPayload::entityId,
                    INT_ARRAY_CODEC, WindowPaperSyncPayload::pixelData,
                    WindowPaperSyncPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}