package com.fidtest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record DishNamePayload(int slot, String name, String description, int nameColor) implements CustomPacketPayload {

    public static final Type<DishNamePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("fidworkblock", "dish_name"));

    public static final StreamCodec<ByteBuf, DishNamePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, DishNamePayload::slot,
            ByteBufCodecs.STRING_UTF8, DishNamePayload::name,
            ByteBufCodecs.STRING_UTF8, DishNamePayload::description,
            ByteBufCodecs.INT, DishNamePayload::nameColor,
            DishNamePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
