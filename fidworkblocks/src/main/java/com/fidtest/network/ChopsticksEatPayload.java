package com.fidtest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

public record ChopsticksEatPayload(int slot, CompoundTag bite, ItemStack food) implements CustomPacketPayload {
    public static final Type<ChopsticksEatPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("fidworkblock", "chopsticks_eat"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ChopsticksEatPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, ChopsticksEatPayload::slot,
                    ByteBufCodecs.COMPOUND_TAG, ChopsticksEatPayload::bite,
                    ItemStack.OPTIONAL_STREAM_CODEC, ChopsticksEatPayload::food,
                    ChopsticksEatPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final class ReceivedEvent extends Event {
        private final int slot;
        private final CompoundTag bite;
        private final ItemStack food;

        public ReceivedEvent(int slot, CompoundTag bite, ItemStack food) {
            this.slot = slot;
            this.bite = bite;
            this.food = food;
        }

        public int slot() {
            return slot;
        }

        public CompoundTag bite() {
            return bite;
        }

        public ItemStack food() {
            return food;
        }
    }
}
