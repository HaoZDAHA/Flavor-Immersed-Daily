package com.flavor_immersed_daily.network;

import com.flavor_immersed_daily.block.blockentity.ColorfulFireworksBoxBlockEntity;
import com.flavor_immersed_daily.block.blockentity.CoupletBlockEntity;
import com.flavor_immersed_daily.entity.WindowPaperEntity;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ModPayloads {
    private ModPayloads() {
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(ColorfulFireworksBoxSyncPayload.TYPE, ColorfulFireworksBoxSyncPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    var player = context.player();
                    if (player != null && player.level() instanceof ServerLevel serverLevel
                            && serverLevel.getBlockEntity(payload.pos()) instanceof ColorfulFireworksBoxBlockEntity blockEntity) {
                        blockEntity.applyConfig(payload);
                    }
                }));
        registrar.playToServer(WindowPaperSyncPayload.TYPE, WindowPaperSyncPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    var player = context.player();
                    if (player != null && player.level() instanceof ServerLevel serverLevel
                            && serverLevel.getEntity(payload.entityId()) instanceof WindowPaperEntity entity) {
                        entity.applyConfig(payload);
                    }
                }));
        registrar.playToServer(CoupletSyncPayload.TYPE, CoupletSyncPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    var player = context.player();
                    if (player != null && player.level() instanceof ServerLevel serverLevel
                            && serverLevel.getBlockEntity(payload.pos()) instanceof CoupletBlockEntity blockEntity) {
                        blockEntity.setLines(payload.lines());
                        blockEntity.setColor(payload.color());
                    }
                }));
    }
}
