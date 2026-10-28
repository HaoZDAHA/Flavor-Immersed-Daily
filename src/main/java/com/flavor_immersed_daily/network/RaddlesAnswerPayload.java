package com.flavor_immersed_daily.network;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

// 灯谜答题包 — 客户端 → 服务端
// 玩家在答题界面提交答案，服务端负责校验并发放奖励
public record RaddlesAnswerPayload(BlockPos pos, String answer) implements CustomPacketPayload {

    public static final Type<RaddlesAnswerPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "raddles_answer"));

    public static final StreamCodec<ByteBuf, RaddlesAnswerPayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, RaddlesAnswerPayload::pos,
                    ByteBufCodecs.STRING_UTF8, RaddlesAnswerPayload::answer,
                    RaddlesAnswerPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
