package com.flavor_immersed_daily.block.blockentity;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.block.block.decorative.ColorfulFireworksBoxBlock;
import com.flavor_immersed_daily.network.ColorfulFireworksBoxSyncPayload;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//箱装烟花 存储烟花配置，处理红石触发与烟花飞行粒子

public class ColorfulFireworksBoxBlockEntity extends BlockEntity {

    // 原版烟花形状（对应烟火之星的Type）
    // 0=small_ball 1=large_ball 2=star 3=creeper 4=burst
    public static final int SHAPE_SMALL_BALL = 0;
    public static final int SHAPE_LARGE_BALL = 1;
    public static final int SHAPE_STAR = 2;
    public static final int SHAPE_CREEPER = 3;
    public static final int SHAPE_BURST = 4;
    public static final String[] SHAPE_NAMES = {"small_ball", "large_ball", "star", "creeper", "burst"};

    private static final String TAG_COLOR = "fw_color";
    private static final String TAG_SHAPE = "fw_shape";
    private static final String TAG_TRAIL = "fw_trail";
    private static final String TAG_ANGLE = "fw_angle";
    private static final String TAG_SPEED = "fw_speed";
    private static final String TAG_DISTANCE = "fw_distance";
    private static final String TAG_CURVE_A = "fw_curve_a";
    private static final String TAG_CURVE_B = "fw_curve_b";
    private static final String TAG_FADE_COLOR = "fw_fade_color";

    //配置
    private int color = 0xFF0000;
    private int fadeColor = 0xFFFFFF;
    private int shape = SHAPE_SMALL_BALL;
    private boolean trail = false;
    private float angleDeg = 0.0f;
    private float speed = 1.0f;
    private float distance = 5.0f;
    private float curveA = 0.0f;
    private float curveB = 0.0f;

    //红石检测
    private boolean wasPowered = false;

    //烟花飞行状态（支持多次并发）
    private final List<FlightState> activeFlights = new ArrayList<>();

    private static class FlightState {
        int launchTicks;
        final int totalLaunchTicks;
        final Vec3 startPos;
        final Direction facing;

        FlightState(Direction facing, Vec3 startPos, int totalLaunchTicks) {
            this.facing = facing;
            this.startPos = startPos;
            this.totalLaunchTicks = totalLaunchTicks;
            this.launchTicks = 0;
        }
    }

    public ColorfulFireworksBoxBlockEntity(BlockPos pos, BlockState state) {
        super(com.flavor_immersed_daily.all.ModBlockEntities.COLORFUL_FIREWORKS_BOX_ENTITY.get(), pos, state);
    }

    //配置访问

    public int getColor() { return color; }
    public int getFadeColor() { return fadeColor; }
    public int getShape() { return shape; }
    public boolean hasTrail() { return trail; }
    public float getAngleDeg() { return angleDeg; }
    public float getSpeed() { return speed; }
    public float getDistance() { return distance; }
    public float getCurveA() { return curveA; }
    public float getCurveB() { return curveB; }

//从网络包应用配置

    public void applyConfig(ColorfulFireworksBoxSyncPayload payload) {
        CompoundTag tag = payload.config();
        this.color = tag.getInt(TAG_COLOR);
        this.fadeColor = tag.getInt(TAG_FADE_COLOR);
        this.shape = tag.getInt(TAG_SHAPE);
        this.trail = tag.getBoolean(TAG_TRAIL);
        this.angleDeg = tag.getFloat(TAG_ANGLE);
        this.speed = tag.getFloat(TAG_SPEED);
        this.distance = tag.getFloat(TAG_DISTANCE);
        this.curveA = tag.getFloat(TAG_CURVE_A);
        this.curveB = tag.getFloat(TAG_CURVE_B);
        setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 3);
        }
    }

//TICK触发相关的

    public static void serverTick(Level level, BlockPos pos, BlockState state, ColorfulFireworksBoxBlockEntity be) {
        if (level.isClientSide) return;

        // 红石边沿触发
        boolean powered = level.hasNeighborSignal(pos);
        if (powered && !be.wasPowered) {
            be.launch(level, state.getValue(ColorfulFireworksBoxBlock.FACING));
        }
        be.wasPowered = powered;

        // 烟花飞行（所有并发任务）
        if (!be.activeFlights.isEmpty()) {
            be.tickFlights((ServerLevel) level);
        }
    }

//触发发射烟花（每次调用都添加新的飞行任务，不打断已有的）

    public void launch(Level level, Direction dir) {
        if (level.isClientSide) return;
        int totalTicks = Math.max(4, Math.round(20.0f / Math.max(0.1f, speed)));
        Vec3 start = new Vec3(worldPosition.getX() + 0.5, worldPosition.getY() + 1.0, worldPosition.getZ() + 0.5);
        activeFlights.add(new FlightState(dir, start, totalTicks));
        level.playSound(null, worldPosition, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.BLOCKS, 1.0f, 1.2f);
        setChanged();
    }

    private void tickFlights(ServerLevel level) {
        Iterator<FlightState> it = activeFlights.iterator();
        while (it.hasNext()) {
            FlightState flight = it.next();
            double progress = distance / Math.max(1, flight.totalLaunchTicks);
            Vec3 pos = getFlightPosition(flight, progress);

            // 飞行轨迹粒子 — 带颜色的粒子
            float r = ((color >> 16) & 0xFF) / 255.0f;
            float g = ((color >> 8) & 0xFF) / 255.0f;
            float b = (color & 0xFF) / 255.0f;
            DustParticleOptions trailParticle = new DustParticleOptions(new Vector3f(r, g, b), 1.0f);
            int count = trail ? 3 : 1;
            level.sendParticles(trailParticle, pos.x, pos.y, pos.z, count,
                    trail ? 0.15 : 0.02, trail ? 0.15 : 0.02, trail ? 0.15 : 0.02, 0);

            flight.launchTicks++;
            if (flight.launchTicks >= flight.totalLaunchTicks) {
                spawnExplosion(level, pos);
                it.remove();
                setChanged();
            }
        }
    }

//飞行位置计算：倾斜角度 + 曲线公式 y = a*x2 + b*x
//angleDeg=0 时垂直升空，A/B=0 时按倾斜角度直线飞行

    private Vec3 getFlightPosition(FlightState flight, double progress) {
        double t = progress * flight.launchTicks;
        double angleRad = Math.toRadians(angleDeg);
        Vec3 facingVec = new Vec3(flight.facing.getOpposite().getStepX(), 0, flight.facing.getOpposite().getStepZ()).normalize();

        double horiz = Math.sin(angleRad);
        double vert = Math.cos(angleRad);

        double hx = facingVec.x * horiz * t;
        double hz = facingVec.z * horiz * t;
        double y = vert * t + curveA * t * t + curveB * t;

        return flight.startPos.add(hx, y, hz);
    }

    private void spawnExplosion(ServerLevel level, Vec3 center) {

        // 使用原版 FireworkExplosion 创建真正的烟花爆炸效果
        FireworkExplosion.Shape fireworkShape = FireworkExplosion.Shape.values()[shape];
        FireworkExplosion explosion = new FireworkExplosion(
                fireworkShape,
                IntList.of(color, fadeColor),
                IntList.of(), // 无渐变色
                trail,
                false        // 无闪烁
        );

        ItemStack fireworkItem = new ItemStack(Items.FIREWORK_ROCKET);
        fireworkItem.set(DataComponents.FIREWORKS, new Fireworks(0, List.of(explosion)));

        FireworkRocketEntity rocket = new FireworkRocketEntity(
                level, null,
                center.x, center.y, center.z,
                fireworkItem
        );

        level.addFreshEntity(rocket);
        rocket.level().broadcastEntityEvent(rocket, (byte) 17);
        rocket.gameEvent(GameEvent.EXPLODE, rocket.getOwner());
        rocket.discard();

        level.playSound(null, worldPosition, SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.BLOCKS, 1.2f, 1.0f);
    }

    //NBT / 同步

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt(TAG_COLOR, color);
        tag.putInt(TAG_FADE_COLOR, fadeColor);
        tag.putInt(TAG_SHAPE, shape);
        tag.putBoolean(TAG_TRAIL, trail);
        tag.putFloat(TAG_ANGLE, angleDeg);
        tag.putFloat(TAG_SPEED, speed);
        tag.putFloat(TAG_DISTANCE, distance);
        tag.putFloat(TAG_CURVE_A, curveA);
        tag.putFloat(TAG_CURVE_B, curveB);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.color = tag.getInt(TAG_COLOR);
        this.fadeColor = tag.getInt(TAG_FADE_COLOR);
        this.shape = tag.getInt(TAG_SHAPE);
        this.trail = tag.getBoolean(TAG_TRAIL);
        this.angleDeg = tag.getFloat(TAG_ANGLE);
        this.speed = tag.getFloat(TAG_SPEED);
        this.distance = tag.getFloat(TAG_DISTANCE);
        this.curveA = tag.getFloat(TAG_CURVE_A);
        this.curveB = tag.getFloat(TAG_CURVE_B);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}