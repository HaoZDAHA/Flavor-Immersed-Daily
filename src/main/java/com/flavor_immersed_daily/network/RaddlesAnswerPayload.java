package com.flavor_immersed_daily.network;

import com.flavor_immersed_daily.block.blockentity.RaddlesBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

// 灯谜答题消息 — 客户端 → 服务端
// 玩家在答题界面提交答案，服务端负责校验并发放奖励，处理完成后再回一个结果包
public class RaddlesAnswerPayload {
    private final BlockPos pos;
    private final String answer;

    public RaddlesAnswerPayload(BlockPos pos, String answer) {
        this.pos = pos;
        this.answer = answer;
    }

    public BlockPos pos() {
        return pos;
    }

    public String answer() {
        return answer;
    }

    public static void encode(RaddlesAnswerPayload msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeUtf(msg.answer);
    }

    public static RaddlesAnswerPayload decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        String answer = buf.readUtf();
        return new RaddlesAnswerPayload(pos, answer);
    }

    public static void handle(RaddlesAnswerPayload msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null || !(player.level() instanceof ServerLevel serverLevel)) {
                return;
            }
            if (!(serverLevel.getBlockEntity(msg.pos) instanceof RaddlesBlockEntity blockEntity)
                    || !blockEntity.hasRiddle() || blockEntity.isOwner(player)) {
                return;
            }

            int result;
            if (blockEntity.isClaimed(player.getUUID())) {
                result = RaddlesResultPayload.ALREADY_CLAIMED;
            } else if (!blockEntity.matchesAnswer(msg.answer)) {
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
            ModMessages.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                    new RaddlesResultPayload(msg.pos, result));
        });
        ctx.get().setPacketHandled(true);
    }
}