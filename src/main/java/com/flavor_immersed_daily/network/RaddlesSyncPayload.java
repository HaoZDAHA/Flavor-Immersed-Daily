package com.flavor_immersed_daily.network;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

// 灯谜牌设置同步包 — 客户端 → 服务端
// 发送牌面四个汉字 + 谜面（问题）+ 谜底（答案），服务端校验放置者身份后写入
public record RaddlesSyncPayload(BlockPos pos, String[] chars, String question, String answer)
        implements CustomPacketPayload {

    public static final Type<RaddlesSyncPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "raddles_sync"));

    private static final StreamCodec<ByteBuf, String[]> STRING_ARRAY_4_CODEC = new StreamCodec<>() {
        @Override
        public String[] decode(ByteBuf buf) {
            String[] chars = new String[4];
            for (int i = 0; i < 4; i++) {
                int len = buf.readInt();
                byte[] bytes = new byte[len];
                buf.readBytes(bytes);
                chars[i] = new String(bytes);
            }
            return chars;
        }

        @Override
        public void encode(ByteBuf buf, String[] chars) {
            for (int i = 0; i < 4; i++) {
                String s = i < chars.length ? chars[i] : "";
                byte[] bytes = s.getBytes();
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);
            }
        }
    };

    public static final StreamCodec<ByteBuf, RaddlesSyncPayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, RaddlesSyncPayload::pos,
                    STRING_ARRAY_4_CODEC, RaddlesSyncPayload::chars,
                    ByteBufCodecs.STRING_UTF8, RaddlesSyncPayload::question,
                    ByteBufCodecs.STRING_UTF8, RaddlesSyncPayload::answer,
                    RaddlesSyncPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
