package com.flavor_immersed_daily.network;

import com.flavor_immersed_daily.block.blockentity.CoupletBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

// 对联文字同步消息 — 客户端 → 服务端
// 发送 4 行文字和颜色选择
public class CoupletSyncPayload {
    private final BlockPos pos;
    private final String[] lines;
    private final int color;

    public CoupletSyncPayload(BlockPos pos, String[] lines, int color) {
        this.pos = pos;
        this.lines = lines;
        this.color = color;
    }

    public BlockPos pos() {
        return pos;
    }

    public String[] lines() {
        return lines;
    }

    public int color() {
        return color;
    }

    public static void encode(CoupletSyncPayload msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        for (int i = 0; i < 4; i++) {
            String s = i < msg.lines.length ? msg.lines[i] : "";
            buf.writeUtf(s);
        }
        buf.writeInt(msg.color);
    }

    public static CoupletSyncPayload decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        String[] lines = new String[4];
        for (int i = 0; i < 4; i++) {
            lines[i] = buf.readUtf();
        }
        int color = buf.readInt();
        return new CoupletSyncPayload(pos, lines, color);
    }

    public static void handle(CoupletSyncPayload msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null && player.level() instanceof ServerLevel serverLevel
                    && serverLevel.getBlockEntity(msg.pos) instanceof CoupletBlockEntity blockEntity) {
                blockEntity.setLines(msg.lines);
                blockEntity.setColor(msg.color);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
