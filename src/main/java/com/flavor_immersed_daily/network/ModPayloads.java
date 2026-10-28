package com.flavor_immersed_daily.network;

import com.flavor_immersed_daily.block.blockentity.ColorfulFireworksBoxBlockEntity;
import com.flavor_immersed_daily.block.blockentity.CoupletBlockEntity;
import com.flavor_immersed_daily.block.blockentity.RaddlesBlockEntity;
import com.flavor_immersed_daily.client.ClientGuiHelper;
import com.flavor_immersed_daily.entity.WindowPaperEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
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
// 灯谜牌设置：牌面四字 + 谜面 + 谜底，仅放置者可写入
        registrar.playToServer(RaddlesSyncPayload.TYPE, RaddlesSyncPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    var player = context.player();
                    if (player != null && player.level() instanceof ServerLevel serverLevel
                            && serverLevel.getBlockEntity(payload.pos()) instanceof RaddlesBlockEntity blockEntity
                            && blockEntity.canEdit(player)) {
                        blockEntity.setChars(payload.chars());
                        blockEntity.setRiddle(payload.question(), payload.answer());
                    }
                }));
// 灯谜答题：服务端校验答案并发放奖励，每个玩家每块牌子只能领一次
        registrar.playToServer(RaddlesAnswerPayload.TYPE, RaddlesAnswerPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    var player = context.player();
                    if (player == null || !(player.level() instanceof ServerLevel serverLevel)) {
                        return;
                    }
                    if (!(serverLevel.getBlockEntity(payload.pos()) instanceof RaddlesBlockEntity blockEntity)
                            || !blockEntity.hasRiddle() || blockEntity.isOwner(player)) {
                        return;
                    }

                    int result;
                    if (blockEntity.isClaimed(player.getUUID())) {
                        result = RaddlesResultPayload.ALREADY_CLAIMED;
                    } else if (!blockEntity.matchesAnswer(payload.answer())) {
                        result = RaddlesResultPayload.WRONG;
                    } else {
// 答对：先记录已领取，再从奖励物品栏随机取一件给玩家
                        blockEntity.markClaimed(player.getUUID());
                        ItemStack reward = blockEntity.takeRandomReward(serverLevel.random);
                        if (reward.isEmpty()) {
                            result = RaddlesResultPayload.NO_REWARD;
                        } else {
                            if (!player.getInventory().add(reward)) {
                                player.drop(reward, false);
                            }
                            result = RaddlesResultPayload.SUCCESS;
                        }
                    }
                    context.reply(new RaddlesResultPayload(payload.pos(), result));
                }));
// 答题结果回传，更新答题界面上的提示
        registrar.playToClient(RaddlesResultPayload.TYPE, RaddlesResultPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> ClientGuiHelper.handleRaddlesResult(payload.result())));
    }
}
