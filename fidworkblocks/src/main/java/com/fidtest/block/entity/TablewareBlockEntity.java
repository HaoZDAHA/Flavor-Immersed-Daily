package com.fidtest.block.entity;

import com.fidtest.registration.ModBlockEntities;
import com.fidtest.registration.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class TablewareBlockEntity extends BlockEntity {
    public enum Kind { PLATE, LONG_PLATE, SAUCER }

    public static class Food {
        public ItemStack stack;
        public double x, z;

        public Food(ItemStack stack, double x, double z) {
            this.stack = stack;
            this.x = x;
            this.z = z;
        }
    }

    public static class Part {
        public Kind kind;
        public double x, z;
        public int layer;
        public Direction facing;
        public List<Food> foods = new ArrayList<>();

        public Part(Kind kind, double x, double z, int layer, Direction facing) {
            this.kind = kind;
            this.x = x;
            this.z = z;
            this.layer = layer;
            this.facing = facing;
        }

        public double surfaceY() { return layer * 0.125 + (kind == Kind.SAUCER ? 0.0625 : 0.125); }

        public AABB bounds() {
            double rx = kind == Kind.PLATE ? 0.375 : kind == Kind.LONG_PLATE && facing.getAxis() == Direction.Axis.X ? 0.5 : 0.25;
            double rz = kind == Kind.PLATE ? 0.375 : kind == Kind.LONG_PLATE && facing.getAxis() == Direction.Axis.Z ? 0.5 : 0.25;
            return new AABB(x - rx, layer * 0.125, z - rz, x + rx, surfaceY(), z + rz);
        }

        public ItemStack item() {
            return new ItemStack(switch (kind) {
                case PLATE -> ModItems.PLATE.get();
                case LONG_PLATE -> ModItems.LONG_PLATE.get();
                case SAUCER -> ModItems.SMALL_PLATE.get();
            });
        }
    }

    private final List<Part> parts = new ArrayList<>();

    public TablewareBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TABLEWARE.get(), pos, state);
    }

    public List<Part> getParts() { return parts; }

    public Part createPart(Kind kind, double x, double z, Direction facing) {
        if (!parts.isEmpty() && (parts.getFirst().kind == Kind.PLATE) != (kind == Kind.PLATE)) return null;
        if (kind == Kind.PLATE) {
            if (parts.size() >= 8 || parts.stream().anyMatch(p -> !p.foods.isEmpty())) return null;
            return new Part(kind, Math.clamp(x, 0.375, 0.625), Math.clamp(z, 0.375, 0.625), parts.size(), facing);
        }
        double cx = x < 0.5 ? 0.25 : 0.75;
        double cz = z < 0.5 ? 0.25 : 0.75;
        if (kind == Kind.LONG_PLATE) {
            if (facing.getAxis() == Direction.Axis.X) cx = 0.5;
            else cz = 0.5;
        }
        Part part = new Part(kind, cx, cz, 0, facing);
        if (parts.stream().anyMatch(p -> p.bounds().intersects(part.bounds()))) return null;
        return part;
    }

    public Part hitPart(double x, double z) {
        Part result = null;
        for (Part part : parts) {
            AABB b = part.bounds();
            if (x >= b.minX - 0.0001 && x <= b.maxX + 0.0001 && z >= b.minZ - 0.0001 && z <= b.maxZ + 0.0001
                    && (result == null || part.layer > result.layer)) result = part;
        }
        // A lower plate's exposed rim still selects the top plate of the stack.
        if (result != null && result.kind == Kind.PLATE) return parts.getLast();
        return result;
    }

    public VoxelShape shape() {
        VoxelShape shape = Shapes.empty();
        for (Part part : parts) shape = Shapes.or(shape, Shapes.create(part.bounds()));
        return shape;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ListTag list = new ListTag();
        for (Part part : parts) {
            CompoundTag entry = new CompoundTag();
            entry.putString("Kind", part.kind.name());
            entry.putDouble("X", part.x);
            entry.putDouble("Z", part.z);
            entry.putInt("Layer", part.layer);
            entry.putInt("Facing", part.facing.get3DDataValue());
            ListTag foods = new ListTag();
            for (Food food : part.foods) {
                CompoundTag f = new CompoundTag();
                f.put("Stack", food.stack.saveOptional(registries));
                f.putDouble("X", food.x);
                f.putDouble("Z", food.z);
                foods.add(f);
            }
            entry.put("Foods", foods);
            list.add(entry);
        }
        tag.put("Parts", list);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        parts.clear();
        ListTag list = tag.getList("Parts", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            Part part = new Part(Kind.valueOf(entry.getString("Kind")), entry.getDouble("X"), entry.getDouble("Z"),
                    entry.getInt("Layer"), Direction.from3DDataValue(entry.getInt("Facing")));
            ListTag foods = entry.getList("Foods", Tag.TAG_COMPOUND);
            for (int j = 0; j < foods.size(); j++) {
                CompoundTag f = foods.getCompound(j);
                ItemStack stack = ItemStack.parseOptional(registries, f.getCompound("Stack"));
                if (!stack.isEmpty()) part.foods.add(new Food(stack, f.getDouble("X"), f.getDouble("Z")));
            }
            parts.add(part);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) { return saveWithoutMetadata(registries); }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && !level.isClientSide) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }
}
