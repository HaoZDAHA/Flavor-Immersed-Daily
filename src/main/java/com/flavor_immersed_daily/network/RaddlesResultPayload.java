package com.flavor_immersed_daily.network;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

// 灯谜答题结果包 — 服务端 → 客户端，用于在答题界面上给出反馈
public record RaddlesResultPayload(BlockPos pos, int result) implements CustomPacketPayload {

// 答对并拿到了奖励
    public static final int SUCCESS = 0;
// 答对，但牌子里的奖励已经发完
    public static final int NO_REWARD = 1;
// 答案不对
    public static final int WRONG = 2;
// 该玩家已经领过这个灯谜的奖励
    public static final int ALREADY_CLAIMED = 3;

    public static final Type<RaddlesResultPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "raddles_result"));

    public static final StreamCodec<ByteBuf, RaddlesResultPayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, RaddlesResultPayload::pos,
                    ByteBufCodecs.VAR_INT, RaddlesResultPayload::result,
                    RaddlesResultPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
