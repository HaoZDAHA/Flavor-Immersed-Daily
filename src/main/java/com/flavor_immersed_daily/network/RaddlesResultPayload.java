package com.flavor_immersed_daily.network;

import com.flavor_immersed_daily.client.ClientGuiHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

// 灯谜答题结果消息 — 服务端 → 客户端
// 用于在答题界面上给出反馈（答对 / 没奖励 / 答案错误 / 已领过）
public class RaddlesResultPayload {
// 答对并拿到了奖励
    public static final int SUCCESS = 0;
// 答对，但牌子里的奖励已经发完
    public static final int NO_REWARD = 1;
// 答案不对
    public static final int WRONG = 2;
// 该玩家已经领过这个灯谜的奖励
    public static final int ALREADY_CLAIMED = 3;

    private final BlockPos pos;
    private final int result;

    public RaddlesResultPayload(BlockPos pos, int result) {
        this.pos = pos;
        this.result = result;
    }

    public BlockPos pos() {
        return pos;
    }

    public int result() {
        return result;
    }

    public static void encode(RaddlesResultPayload msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeInt(msg.result);
    }

    public static RaddlesResultPayload decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        int result = buf.readInt();
        return new RaddlesResultPayload(pos, result);
    }

    public static void handle(RaddlesResultPayload msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientGuiHelper.handleRaddlesResult(msg.result));
        ctx.get().setPacketHandled(true);
    }
}