package com.flavor_immersed_daily.entity;

import com.flavor_immersed_daily.block.block.furniture.ChairBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * 座椅实体 - 玩家骑乘此实体来实现坐在椅子上的效果。
 * 实体不可见、无碰撞箱、无重力，仅在玩家坐下时存在。
 */
public class SeatEntity extends Entity {

    public SeatEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public SeatEntity(EntityType<?> type, Level level, BlockPos pos) {
        super(type, level);
        this.noPhysics = true;
        // 座位在方块中心，高度为 0.5 格
        this.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        // 如果没有乘客，移除实体
        if (this.getPassengers().isEmpty()) {
            this.discard();
            return;
        }

        // 检查椅子方块是否还在，如果被破坏则移除
        if (!(this.level().getBlockState(this.blockPosition()).getBlock() instanceof ChairBlock)) {
            this.ejectPassengers();
            this.discard();
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        // 玩家下马时出现在椅子位置上方
        BlockPos pos = this.blockPosition();
        return new Vec3(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return false;
    }

    /**
     * 检查指定位置是否已有座椅实体。
     */
    public static boolean hasSeatEntity(Level level, BlockPos pos) {
        List<SeatEntity> seats = level.getEntitiesOfClass(SeatEntity.class,
                new AABB(pos).inflate(0.5));
        return !seats.isEmpty();
    }
}