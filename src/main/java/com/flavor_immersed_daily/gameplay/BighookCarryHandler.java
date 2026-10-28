package com.flavor_immersed_daily.gameplay;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.block.blockentity.BighookBlockEntity;
import com.flavor_immersed_daily.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// bighook 抓取生物功能处理器。
// 核心思路：抓取/挂住的生物是真实存在的实体，通过每 tick 将其锁定在玩家面前（或挂钩前方）实现"被拿起"，
// 同时用 noAi/noGravity 使其无法自主移动与攻击，配合 config 开关控制潜行（shift）挣脱与整功能开关。
// 实体渲染交给原版（实体在玩家面前/挂钩处正常渲染），无需额外渲染/网络同步逻辑，尽量不动整体架构。
@Mod.EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public final class BighookCarryHandler {

// 实体验证数据：被拿起/挂住的标记
// "hand" 玩家手拿 | "hook" 挂在挂钩上
    private static final String TAG_MODE = "fid_carry_mode";
// 抓取者玩家 UUID
    private static final String TAG_CARRIER = "fid_carrier";

// server: 抓取者 UUID -> 被抓取实体 UUID
    private static final Map<UUID, UUID> CARRIER_TO_CARRIED = new ConcurrentHashMap<>();
// server: 被抓取实体 UUID -> 抓取者 UUID（便于反向清理）
    private static final Map<UUID, UUID> CARRIED_TO_CARRIER = new ConcurrentHashMap<>();

// server: 待挂到挂钩的生物 UUID -> 放置挂钩的目标坐标（由 getStateForPlacement 登记，onPlace 消费）
    private static final Map<UUID, BlockPos> PENDING_HANG = new ConcurrentHashMap<>();

    private BighookCarryHandler() {
    }

// 抓取

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (!Config.bighookCarryEnabled) return;
        Player carrier = event.getEntity();
        if (carrier.level().isClientSide) return;
        if (!(event.getTarget() instanceof LivingEntity target)) return;
        if (!canGrab(carrier, target)) return;

// 成功抓取：取消本次攻击伤害，把目标拿到手里
        event.setCanceled(true);
        grab(carrier, target);
    }

    private static boolean canGrab(Player carrier, LivingEntity target) {
        if (target == carrier) return false;
        if (!target.isAlive()) return false;
        if (target.isPassenger()) return false;
        if (target.level().isClientSide) return false;
// 已被拿起/挂住的不能重复抓
        if (isCarried(target)) return false;
// 已经抓着一个了
        if (CARRIER_TO_CARRIED.containsKey(carrier.getUUID())) return false;
// 手里没有挂勾
        if (!holdingBighook(carrier)) return false;
// 不能抓Boss
        if (target instanceof EnderDragon || target instanceof WitherBoss) return false;
// 敌对怪物不能抓
        if (target instanceof Monster) return false;
// 红眼中立不能抓
        if (target instanceof NeutralMob neutral && neutral.isAngry()) return false;
// 只有当前生命值低于玩家的才能抓
        return target.getHealth() < carrier.getHealth();
    }

    private static void grab(Player carrier, LivingEntity target) {
        UUID carriedId = target.getUUID();
        UUID carrierId = carrier.getUUID();
        CARRIER_TO_CARRIED.put(carrierId, carriedId);
        CARRIED_TO_CARRIER.put(carriedId, carrierId);
        target.getPersistentData().putString(TAG_MODE, "hand");
        target.getPersistentData().putUUID(TAG_CARRIER, carrierId);
        applyCarryLock(target);
        holdInHand(carrier, target);
    }

// 维护（玩家 tick）

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player carrier = event.player;
        if (carrier.level().isClientSide) return;
        UUID carriedId = CARRIER_TO_CARRIED.get(carrier.getUUID());
        if (carriedId == null) return;
        tickCarry(carrier, carriedId);
    }

    private static void tickCarry(Player carrier, UUID carriedId) {
        Entity carried = ((ServerLevel) carrier.level()).getEntity(carriedId);
        boolean release = carried == null || !carried.isAlive() || !Config.bighookCarryEnabled;
        if (!release) {
            if (Config.bighookShiftEscapeEnabled) {
// 抓取者潜行 -> 放下；或被抓取的玩家自己潜行 -> 挣脱
                if (carrier.isShiftKeyDown() || (carried instanceof Player victim && victim.isShiftKeyDown())) {
                    release = true;
                }
            }
// 手里没有挂勾则放下
            if (!release && !holdingBighook(carrier)) {
                release = true;
            }
        }

        if (release) {
            releaseCarriedByPlayer(carrier, carriedId);
            return;
        }

// 继续控制在玩家面前
        applyCarryLock(carried);
        holdInHand(carrier, carried);
    }

// 被拿起/挂住的生物不能伤害任何目标（防止被抓取的玩家攻击抓取者等）
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide) return;
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof LivingEntity le && isCarried(le)) {
            event.setCanceled(true);
        }
    }

// 玩家登出清理

// 玩家登出时放下手中被拿起的生物，避免实体残留被永久锁定
    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player carrier = event.getEntity();
        if (carrier == null || carrier.level().isClientSide) return;
        UUID carriedId = CARRIER_TO_CARRIED.remove(carrier.getUUID());
        if (carriedId == null) return;
        CARRIED_TO_CARRIER.remove(carriedId);
        Entity carried = ((ServerLevel) carrier.level()).getEntity(carriedId);
        if (carried instanceof LivingEntity le) {
            clearCarryFlags(le);
        }
    }

    @SubscribeEvent
    public static void onEntityPlace(BlockEvent.EntityPlaceEvent event) {
        if (!Config.bighookCarryEnabled) return;
        if (!(event.getEntity() instanceof Player carrier)) return;
        Level level = event.getEntity().level();
        if (level == null || level.isClientSide) return;

        BlockState placed = event.getPlacedBlock();
        if (placed.getBlock() != ModBlocks.BIGHOOK.get()) return;

        UUID carriedId = CARRIER_TO_CARRIED.remove(carrier.getUUID());
        if (carriedId == null) return;
        CARRIED_TO_CARRIER.remove(carriedId);

        Entity carried = ((ServerLevel) level).getEntity(carriedId);
        if (!(carried instanceof LivingEntity le) || !le.isAlive()) {
            return;
        }

        transferToHook((ServerLevel) level, le, carriedId, event.getPos());
    }

// 在 getStateForPlacement 中调用：预登记"这个玩家正手拿的生物可放到该挂钩坐标"。
// 这样在生存模式（放置即消耗挂钩方块）下，也不依赖手上是否还有挂钩来判断归属。
    public static void markPendingHang(Player player, BlockPos pos) {
        if (player == null || player.level().isClientSide) return;
        UUID carriedId = CARRIER_TO_CARRIED.get(player.getUUID());
        if (carriedId != null) {
            PENDING_HANG.put(carriedId, pos.immutable());
        }
    }

// onPlace 中调用：真正放置挂钩成功后，把本次要挂的生物转移到挂钩上。
// 仅在目标坐标与 getStateForPlacement 登记一致时消费，避免误转旁边其他玩家正在拿的生物。
    public static void tryHangOnPlace(ServerLevel level, BlockPos pos) {
        if (!Config.bighookCarryEnabled) return;
        for (Map.Entry<UUID, BlockPos> entry : java.util.List.copyOf(PENDING_HANG.entrySet())) {
            if (!entry.getValue().equals(pos)) continue;
            UUID carriedId = entry.getKey();
            Entity carried = level.getEntity(carriedId);
            if (!(carried instanceof LivingEntity le) || !le.isAlive()) {
                PENDING_HANG.remove(carriedId);
                continue;
            }
            if (!"hand".equals(le.getPersistentData().getString(TAG_MODE))) {
                PENDING_HANG.remove(carriedId);
                continue;
            }

            PENDING_HANG.remove(carriedId);
            UUID carrierId = CARRIED_TO_CARRIER.remove(carriedId);
            if (carrierId != null) CARRIER_TO_CARRIED.remove(carrierId);
            le.getPersistentData().remove(TAG_CARRIER);
            transferToHook(level, le, carriedId, pos);
        }
    }

// 把实体从"手拿"改成"挂在挂钩上"并定位
    private static void transferToHook(ServerLevel level, LivingEntity le, UUID carriedId, BlockPos pos) {
        le.getPersistentData().remove(TAG_CARRIER);
        le.getPersistentData().putString(TAG_MODE, "hook");
        applyCarryLock(le);
        if (level.getBlockEntity(pos) instanceof BighookBlockEntity be) {
            be.hangCarried(carriedId, le);
        }
    }

// 工具方法

// 是否手持挂勾（主手或副手）
    public static boolean holdingBighook(Player player) {
        Item hook = ModBlocks.BIGHOOK.get().asItem();
        return player.getMainHandItem().is(hook) || player.getOffhandItem().is(hook);
    }

// 是否为被拿起/挂住的生物（通过实体验证数据中的标记判断）
    public static boolean isCarried(Entity entity) {
        return entity instanceof LivingEntity le && le.getPersistentData().contains(TAG_MODE);
    }

// 施加"被控制"锁：无法自主移动/攻击
    private static void applyCarryLock(Entity carried) {
        if (carried instanceof Mob mob) {
            mob.setNoAi(true);
        }
        carried.setNoGravity(true);
        carried.setInvulnerable(true);
    }

// 同 applyCarryLock，但为公开静态，供方块实体在使用时复用
    public static void lockCarried(Entity carried) {
        applyCarryLock(carried);
    }

// 清除控制锁（松手/挣脱/挂勾被拆除时调用）
    public static void clearCarryFlags(LivingEntity le) {
        le.getPersistentData().remove(TAG_MODE);
        le.getPersistentData().remove(TAG_CARRIER);
        if (le instanceof Mob mob) {
            mob.setNoAi(false);
        }
        le.setNoGravity(false);
        le.setInvulnerable(false);
    }

// 把实体强制放到指定位置（用于挂起/手拿锁定）。
// 普通生物 moveTo 即可由原版实体同步覆盖；玩家必须用 teleportTo，
// 否则服务端只改数值、玩家自己的客户端看不到（本地位置由自己预测），
// 表现为"只有抓取者看到被挂、被挂的玩家却原地不动/偶尔抖动"。
    public static void snapEntity(LivingEntity le, double x, double y, double z, float yaw, float pitch) {
        if (le.level().isClientSide) return;
        if (le instanceof ServerPlayer sp) {
// 对被搬玩家用绝对传送并向其本人发包，否则客户端看不到自己被移动
            sp.teleportTo(x, y, z);
        } else {
            le.moveTo(x, y, z, yaw, pitch);
        }
        le.setDeltaMovement(0, 0, 0);
    }

// 把实体放到玩家面前一拳处（高度大致在胸口，保持可见）
    private static void holdInHand(Player carrier, Entity carried) {
        if (!(carried instanceof LivingEntity le)) return;
        Vec3 look = carrier.getLookAngle();
        Vec3 target = carrier.getEyePosition().add(look.x * 1.1, -0.7, look.z * 1.1);
        double footY = Math.max(carrier.level().getMinBuildHeight() + 1, target.y - carried.getBbHeight() / 2.0);
        snapEntity(le, target.x, footY, target.z, carrier.getYRot(), 0);
    }

// 玩家放下手中生物
    private static void releaseCarriedByPlayer(Player carrier, UUID carriedId) {
        CARRIER_TO_CARRIED.remove(carrier.getUUID());
        CARRIED_TO_CARRIER.remove(carriedId);
        Entity carried = ((ServerLevel) carrier.level()).getEntity(carriedId);
        if (carried instanceof LivingEntity le) {
            clearCarryFlags(le);
            Vec3 drop = carrier.getEyePosition().add(carrier.getLookAngle().scale(1.2));
            snapEntity(le, drop.x, Math.max(carrier.level().getMinBuildHeight() + 1, drop.y - 0.5), drop.z,
                    carrier.getYRot(), 0);
        }
    }
}
