package com.fidtest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record DishOpenPayload() implements CustomPacketPayload {

    public static final DishOpenPayload INSTANCE = new DishOpenPayload();

    public static final Type<DishOpenPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("fidworkblock", "dish_open"));

    public static final StreamCodec<ByteBuf, DishOpenPayload> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
