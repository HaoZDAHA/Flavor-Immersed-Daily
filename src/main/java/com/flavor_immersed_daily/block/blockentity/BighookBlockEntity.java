package com.flavor_immersed_daily.block.blockentity;

import com.flavor_immersed_daily.butchering.ButcheringDefinition;
import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.gameplay.BighookCarryHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;

import java.util.UUID;

// 大挂钩方块实体 — 额外支持"抓取生物挂在挂钩上"的功能：
// 记录了挂在挂钩上的实体 UUID，每 tick 将其锁在挂钩处，并支持玩家潜行挣脱或其他玩家右键放下。
// 原有屠宰（悬挂动物胴体）走的是方块状态 ANIMAL/STAGE，与此互不干扰。
public class BighookBlockEntity extends BlockEntity {

// 挂在挂钩上的实体 UUID（服务端使用；为 null 表示没有挂住被抓取的生物）
    private UUID heldCarriedId;

// 数据包自定义屠宰（供其他模组 / 数据包扩展，与内置 4 种动物互不干扰）

// 当前自定义屠宰定义 ID；为 null 表示没有进行自定义屠宰
    @Nullable
    private ResourceLocation customButcheringId;
// 当前阶段（从 1 开始）；仅当 customButcheringId 非空时有效
    private int customStage;
// 挂钩上显示的物品（同步给客户端渲染）
    private ItemStack customDisplay = ItemStack.EMPTY;
// 当前阶段要替换显示的方块模型（供客户端渲染）；为 null 表示沿用 display 物品
    @Nullable
    private ResourceLocation customModel;
// 当前阶段所需刀具（同步给客户端渲染）
    private ItemStack customTool = ItemStack.EMPTY;

    public BighookBlockEntity(BlockPos pos, BlockState state) {
        super(com.flavor_immersed_daily.all.ModBlockEntities.BIGHOOK_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BighookBlockEntity be) {
        if (level.isClientSide) return;
        be.serverTick(level, pos);
    }

    private void serverTick(Level level, BlockPos pos) {
        if (heldCarriedId == null) return;
// 功能总开关被关闭时，自动释放挂住的生物
        if (!Config.bighookCarryEnabled) {
            releaseCarried();
            return;
        }
        Entity carried = ((net.minecraft.server.level.ServerLevel) level).getEntity(heldCarriedId);
        if (!(carried instanceof LivingEntity le) || !le.isAlive()) {
// 实体丢失（重启/被卸载/死亡）时清理记录与标记
            heldCarriedId = null;
            setChanged();
            return;
        }

// 持续锁定：挂住的生物无法自主移动/攻击
        BighookCarryHandler.lockCarried(le);

// 被挂住的玩家 shift 挣脱
        if (Config.bighookShiftEscapeEnabled && le instanceof Player p && p.isShiftKeyDown()) {
            releaseCarried();
            return;
        }

// 保持在挂钩处
        parkAtHook(le);
    }

// 设置挂住的实体，并立即把它放到挂钩处
    public void hangCarried(UUID carriedId, LivingEntity le) {
        this.heldCarriedId = carriedId;
        setChanged();
        parkAtHook(le);
    }

// 把实体锁在挂钩处固定悬挂。
// 智能安排：无论生物多高，都让它的"头顶"与上方方块之间保持固定空隙
// （头顶约在挂钩格内 0.35 格处，距上方承重方块 0.65 格），这样不会被上方方块遮挡变暗，
// 身体则按自然身高向下延伸（越高越往下垂，且恰好挂在挂钩正下方）。
    private void parkAtHook(LivingEntity le) {
        if (le.level().isClientSide) return;
        Vec3 c = Vec3.atCenterOf(worldPosition);
// 头顶统一保持在挂钩格内 0.35 格处（moveTo 设置的是实体中心，故中心 = 头顶 - 身高/2）
        double topY = worldPosition.getY() + 0.35;
        double centerY = topY - le.getBbHeight() / 2.0;
// 对玩家用 teleportTo 以同步其本人视角（moveTo 仅服务端生效，玩家客户端看不到）
        BighookCarryHandler.snapEntity(le, c.x, Math.max(le.level().getMinBuildHeight() + 1, centerY), c.z, 0, 0);
    }

// 是否有挂住被抓取的生物
    public boolean hasCarried() {
        return heldCarriedId != null;
    }

// 释放挂住的生物：放到挂钩前，清除控制锁
    public void releaseCarried() {
        if (heldCarriedId == null) return;
        UUID id = heldCarriedId;
        heldCarriedId = null;
        setChanged();
        if (level == null) return;
        Entity carried = ((net.minecraft.server.level.ServerLevel) level).getEntity(id);
        if (carried instanceof LivingEntity le) {
            BighookCarryHandler.clearCarryFlags(le);
            Vec3 c = Vec3.atCenterOf(worldPosition);
            BighookCarryHandler.snapEntity(le, c.x, Math.max(level.getMinBuildHeight() + 1, c.y - 0.5), c.z,
                    le.getYRot(), le.getXRot());
        }
    }

// 数据包自定义屠宰

// 是否正在进行自定义屠宰。
    public boolean hasCustomButchering() {
        return customButcheringId != null;
    }

    @Nullable
    public ResourceLocation getCustomButcheringId() {
        return customButcheringId;
    }

// 当前阶段（从 1 开始；无自定义屠宰时为 0）。
    public int getCustomStage() {
        return customStage;
    }

// 挂钩上显示的物品（供客户端渲染）。
    public ItemStack getCustomDisplay() {
        return customDisplay;
    }

// 当前阶段要替换显示的方块模型；为 null 表示沿用 display 物品。
    @Nullable
    public ResourceLocation getCustomModel() {
        return customModel;
    }

// 当前阶段所需刀具（供客户端渲染）。
    public ItemStack getCustomTool() {
        return customTool;
    }

// 开始一次自定义屠宰：记录定义并把阶段置为 1。
    public void startCustomButchering(ButcheringDefinition definition) {
        this.customButcheringId = definition.id();
        this.customStage = 1;
        applyCustomStage(definition, 1);
        sync();
    }

// 推进到下一阶段；已到末阶段则清空挂钩。
    public void advanceCustomButchering(ButcheringDefinition definition) {
        int next = customStage + 1;
        if (next > definition.stages().size()) {
            clearCustomButchering();
            return;
        }
        this.customStage = next;
        applyCustomStage(definition, next);
        sync();
    }

// 清空自定义屠宰状态。
    public void clearCustomButchering() {
        this.customButcheringId = null;
        this.customStage = 0;
        this.customDisplay = ItemStack.EMPTY;
        this.customModel = null;
        this.customTool = ItemStack.EMPTY;
        sync();
    }

    private void applyCustomStage(ButcheringDefinition definition, int stage) {
        ButcheringDefinition.Stage current = definition.stages().get(stage - 1);
        this.customDisplay = new ItemStack(current.display());
        this.customModel = current.model();
        this.customTool = current.toolItem() != null ? new ItemStack(current.toolItem()) : ItemStack.EMPTY;
    }

    private void sync() {
        setChanged();
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

// 网络同步

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

// NBT 持久化

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        heldCarriedId = tag.hasUUID("heldCarriedId") ? tag.getUUID("heldCarriedId") : null;
        customButcheringId = tag.contains("customButcheringId")
                ? ResourceLocation.tryParse(tag.getString("customButcheringId")) : null;
        customStage = tag.getInt("customStage");
        customDisplay = tag.contains("customDisplay")
                ? ItemStack.of(tag.getCompound("customDisplay")) : ItemStack.EMPTY;
        customModel = tag.contains("customModel")
                ? ResourceLocation.tryParse(tag.getString("customModel")) : null;
        customTool = tag.contains("customTool")
                ? ItemStack.of(tag.getCompound("customTool")) : ItemStack.EMPTY;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (heldCarriedId != null) {
            tag.putUUID("heldCarriedId", heldCarriedId);
        }
        if (customButcheringId != null) {
            tag.putString("customButcheringId", customButcheringId.toString());
            tag.putInt("customStage", customStage);
            if (!customDisplay.isEmpty()) {
                CompoundTag displayTag = new CompoundTag();
                customDisplay.save(displayTag);
                tag.put("customDisplay", displayTag);
            }
            if (customModel != null) {
                tag.putString("customModel", customModel.toString());
            }
            if (!customTool.isEmpty()) {
                CompoundTag toolTag = new CompoundTag();
                customTool.save(toolTag);
                tag.put("customTool", toolTag);
            }
        }
    }
}