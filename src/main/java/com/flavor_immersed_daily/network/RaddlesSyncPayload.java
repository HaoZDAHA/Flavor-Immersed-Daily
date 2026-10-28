package com.flavor_immersed_daily.network;

import com.flavor_immersed_daily.block.blockentity.RaddlesBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

// 灯谜牌设置同步消息 — 客户端 → 服务端
// 发送牌面四个汉字 + 谜面（问题）+ 谜底（答案），服务端校验放置者身份后写入
public class RaddlesSyncPayload {
    private final BlockPos pos;
    private final String[] chars;
    private final String question;
    private final String answer;

    public RaddlesSyncPayload(BlockPos pos, String[] chars, String question, String answer) {
        this.pos = pos;
        this.chars = chars;
        this.question = question;
        this.answer = answer;
    }

    public BlockPos pos() {
        return pos;
    }

    public String[] chars() {
        return chars;
    }

    public String question() {
        return question;
    }

    public String answer() {
        return answer;
    }

    public static void encode(RaddlesSyncPayload msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        for (int i = 0; i < 4; i++) {
            String s = i < msg.chars.length ? msg.chars[i] : "";
            buf.writeUtf(s);
        }
        buf.writeUtf(msg.question);
        buf.writeUtf(msg.answer);
    }

    public static RaddlesSyncPayload decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        String[] chars = new String[4];
        for (int i = 0; i < 4; i++) {
            chars[i] = buf.readUtf();
        }
        String question = buf.readUtf();
        String answer = buf.readUtf();
        return new RaddlesSyncPayload(pos, chars, question, answer);
    }

    public static void handle(RaddlesSyncPayload msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null && player.level() instanceof ServerLevel serverLevel
                    && serverLevel.getBlockEntity(msg.pos) instanceof RaddlesBlockEntity blockEntity
                    && blockEntity.canEdit(player)) {
                blockEntity.setChars(msg.chars);
                blockEntity.setRiddle(msg.question, msg.answer);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}