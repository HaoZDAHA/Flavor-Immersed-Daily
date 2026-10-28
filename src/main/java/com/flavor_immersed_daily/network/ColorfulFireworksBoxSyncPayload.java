package com.flavor_immersed_daily.network;

import com.flavor_immersed_daily.block.blockentity.ColorfulFireworksBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

// 箱装烟花配置同步消息 — 客户端 → 服务端
public class ColorfulFireworksBoxSyncPayload {
    private final BlockPos pos;
    private final CompoundTag config;

    public ColorfulFireworksBoxSyncPayload(BlockPos pos, CompoundTag config) {
        this.pos = pos;
        this.config = config;
    }

    public BlockPos pos() {
        return pos;
    }

    public CompoundTag config() {
        return config;
    }

    public static void encode(ColorfulFireworksBoxSyncPayload msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeNbt(msg.config);
    }

    public static ColorfulFireworksBoxSyncPayload decode(FriendlyByteBuf buf) {
        return new ColorfulFireworksBoxSyncPayload(buf.readBlockPos(), buf.readNbt());
    }

    public static void handle(ColorfulFireworksBoxSyncPayload msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null && player.level() instanceof ServerLevel serverLevel
                    && serverLevel.getBlockEntity(msg.pos) instanceof ColorfulFireworksBoxBlockEntity blockEntity) {
                blockEntity.applyConfig(msg);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
