package com.flavor_immersed_daily.entity;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.client.ClientGuiHelper;
import com.flavor_immersed_daily.network.WindowPaperSyncPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

/**
 * 窗纸实体 — 贴墙挂画，可编辑像素涂鸦
 * 继承 HangingEntity 复用原版画的放置逻辑，使用 MCPaint 风格逐像素渲染
 */
public class WindowPaperEntity extends HangingEntity {

    private static final String TAG_PIXEL_DATA = "pixel_data";
    // 256个像素，每个为ARGB颜色值 (0x00000000 = 透明, 0xFFFFFFFF = 白色)
    private int[] pixelData = new int[256];

    // 数据同步器：用于服务端→客户端同步像素数据（解决退出重进后数据重置的问题）
    // 使用 COMPOUND_TAG 序列化 int[] 数组（原版没有 INT_ARRAY 序列化器）
    private static final EntityDataAccessor<CompoundTag> DATA_PIXEL_DATA =
            SynchedEntityData.defineId(WindowPaperEntity.class, EntityDataSerializers.COMPOUND_TAG);

    public WindowPaperEntity(EntityType<? extends HangingEntity> type, Level level) {
        super(type, level);
    }

    public WindowPaperEntity(EntityType<? extends HangingEntity> type, Level level, BlockPos pos, Direction facing) {
        super(type, level, pos);
        this.setDirection(facing);
    }

    public int[] getPixelData() {
        // 客户端优先使用数据同步器中的值（与服务端保持一致）
        if (this.level() != null && this.level().isClientSide) {
            CompoundTag tag = this.getEntityData().get(DATA_PIXEL_DATA);
            int[] synced = tag.getIntArray("pixels");
            if (synced.length == 256) {
                return synced;
            }
        }
        return pixelData;
    }

    public void setPixelData(int[] data) {
        this.pixelData = data.length == 256 ? data : new int[256];
        // 更新数据同步器，服务端会自动同步到所有客户端
        CompoundTag tag = new CompoundTag();
        tag.putIntArray("pixels", this.pixelData);
        this.getEntityData().set(DATA_PIXEL_DATA, tag);
    }

    public void applyConfig(WindowPaperSyncPayload payload) {
        this.setPixelData(payload.pixelData());
    }

    @Override
    protected AABB calculateBoundingBox(BlockPos pos, Direction direction) {
        double d1 = pos.getX() + 0.5D - direction.getStepX() * 0.46875D;
        double d2 = pos.getY() + 0.5D - direction.getStepY() * 0.46875D;
        double d3 = pos.getZ() + 0.5D - direction.getStepZ() * 0.46875D;

        double d6 = 16.0D;
        double d7 = 16.0D;
        double d8 = 16.0D;

        switch (direction.getAxis()) {
            case X -> d6 = 1.0D;
            case Y -> d7 = 1.0D;
            case Z -> d8 = 1.0D;
        }

        d6 = d6 / 32.0D;
        d7 = d7 / 32.0D;
        d8 = d8 / 32.0D;

        return new AABB(d1 - d6, d2 - d7, d3 - d8, d1 + d6, d2 + d7, d3 + d8);
    }

    @Override
    public void playPlacementSound() {
        this.playSound(SoundEvents.PAINTING_PLACE, 1.0F, 1.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        CompoundTag defaultTag = new CompoundTag();
        defaultTag.putIntArray("pixels", new int[256]);
        builder.define(DATA_PIXEL_DATA, defaultTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        int[] loaded = tag.getIntArray(TAG_PIXEL_DATA);
        if (loaded.length == 256) {
            this.pixelData = loaded;
            // 同步到数据同步器，确保客户端能收到
            CompoundTag syncTag = new CompoundTag();
            syncTag.putIntArray("pixels", loaded);
            this.getEntityData().set(DATA_PIXEL_DATA, syncTag);
        }
        // 直接设置 direction 字段并重新计算包围盒，避免调用 setDirection() 触发 setYRot()
        // 与原版 ItemFrame 的做法一致，防止与实体系统加载时的位置设置冲突
        if (tag.contains("Facing")) {
            this.direction = Direction.from3DDataValue(tag.getByte("Facing"));
            this.recalculateBoundingBox();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putIntArray(TAG_PIXEL_DATA, this.pixelData);
        tag.putByte("Facing", (byte) this.direction.get3DDataValue());
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (level().isClientSide) {
            ClientGuiHelper.openWindowPaper(this);
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double dist) {
        return dist < 4096;
    }

    @Override
    public void dropItem(@Nullable Entity brokenEntity) {
        this.spawnAtLocation(new ItemStack(FlavorImmersedDaily.WINDOW_PAPER_ITEM.get()));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        return new ClientboundAddEntityPacket(this, this.direction.get3DDataValue(), this.getPos());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        // 直接设置 direction 并重新计算包围盒，避免 setDirection() 覆盖 super 已设置的 yRot
        this.direction = Direction.from3DDataValue(packet.getData());
        this.recalculateBoundingBox();
    }
}