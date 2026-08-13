package com.flavor_immersed_daily.block.blockentity;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 对联方块实体 — 存储文字内容和颜色
 */
public class CoupletBlockEntity extends BlockEntity {

    private static final String TAG_LINE_PREFIX = "line_";
    private static final String TAG_COLOR = "color";

    // 4行文字（横幅每行一句，竖联每字一行）
    private String[] lines = new String[]{"", "", "", ""};
    // 0=黑色, 1=黄色
    private int color = 0;

    public CoupletBlockEntity(BlockPos pos, BlockState state) {
        super(com.flavor_immersed_daily.all.ModBlockEntities.COUPLET_ENTITY.get(), pos, state);
    }

    public String[] getLines() {
        return lines;
    }

    public String getLine(int index) {
        if (index >= 0 && index < 4) {
            return lines[index];
        }
        return "";
    }

    public void setLines(String[] lines) {
        if (lines.length >= 4) {
            this.lines = lines;
        } else {
            this.lines = new String[]{"", "", "", ""};
            System.arraycopy(lines, 0, this.lines, 0, lines.length);
        }
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        for (int i = 0; i < 4; i++) {
            tag.putString(TAG_LINE_PREFIX + i, lines[i] != null ? lines[i] : "");
        }
        tag.putInt(TAG_COLOR, color);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        for (int i = 0; i < 4; i++) {
            lines[i] = tag.getString(TAG_LINE_PREFIX + i);
        }
        color = tag.getInt(TAG_COLOR);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }
}