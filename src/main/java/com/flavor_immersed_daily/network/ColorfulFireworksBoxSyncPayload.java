package com.flavor_immersed_daily.network;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

// 箱装烟花配置同步包 — 客户端 → 服务端
public record ColorfulFireworksBoxSyncPayload(BlockPos pos, CompoundTag config) implements CustomPacketPayload {

    public static final Type<ColorfulFireworksBoxSyncPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "fireworks_box_config"));

    public static final StreamCodec<ByteBuf, ColorfulFireworksBoxSyncPayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, ColorfulFireworksBoxSyncPayload::pos,
                    ByteBufCodecs.COMPOUND_TAG, ColorfulFireworksBoxSyncPayload::config,
                    ColorfulFireworksBoxSyncPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
