package com.flavor_immersed_daily.network;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

// 对联文字同步包 — 客户端 → 服务端
// 发送 4 行文字和颜色选择
public record CoupletSyncPayload(BlockPos pos, String[] lines, int color) implements CustomPacketPayload {

    public static final Type<CoupletSyncPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "couplet_sync"));

    private static final StreamCodec<ByteBuf, String[]> STRING_ARRAY_4_CODEC = new StreamCodec<>() {
        @Override
        public String[] decode(ByteBuf buf) {
            String[] lines = new String[4];
            for (int i = 0; i < 4; i++) {
                int len = buf.readInt();
                byte[] bytes = new byte[len];
                buf.readBytes(bytes);
                lines[i] = new String(bytes);
            }
            return lines;
        }

        @Override
        public void encode(ByteBuf buf, String[] lines) {
            for (int i = 0; i < 4; i++) {
                String s = i < lines.length ? lines[i] : "";
                byte[] bytes = s.getBytes();
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);
            }
        }
    };

    public static final StreamCodec<ByteBuf, CoupletSyncPayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, CoupletSyncPayload::pos,
                    STRING_ARRAY_4_CODEC, CoupletSyncPayload::lines,
                    ByteBufCodecs.INT, CoupletSyncPayload::color,
                    CoupletSyncPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}