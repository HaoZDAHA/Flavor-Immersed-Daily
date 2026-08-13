package com.flavor_immersed_daily.block.blockentity;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.block.block.processing.WoodBasinBlock;
import com.flavor_immersed_daily.recipe.ModRecipes;
import com.flavor_immersed_daily.recipe.WoodBasinRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 木盆方块实体 — 存储chickenwithoutblood/水果类型，驱动浮动动画和踩踏计数
 */
public class WoodBasinBlockEntity extends BlockEntity {

    private static final int STOMP_REQUIRED = 5;
    private static final int STOMP_COOLDOWN = 8;
    private static final double MOVE_THRESHOLD = 0.15;

    private int tickCount;
    private String fruitItemId = "";
    private int fruitCount;                // 放入的水果数量
    private int stompCount;
    private int stompCooldown;
    private final Map<UUID, EntityStompData> entityData = new HashMap<>();

    /** 每个实体的踩踏状态 */
    private static class EntityStompData {
        Vec3 lastPos = Vec3.ZERO;
        boolean wasOnGround;
        boolean wasAbove;
    }

    public WoodBasinBlockEntity(BlockPos pos, BlockState state) {
        super(com.flavor_immersed_daily.all.ModBlockEntities.WOODBASIN_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, WoodBasinBlockEntity be) {
        be.tickCount++;

        if (!state.getValue(WoodBasinBlock.HAS_FRUIT) || state.getValue(WoodBasinBlock.WATERED)
                || state.getValue(WoodBasinBlock.HAS_CHICKEN)) {
            be.entityData.clear();
            return;
        }

        if (be.stompCooldown > 0) {
            be.stompCooldown--;
        }

        var entities = level.getEntitiesOfClass(Entity.class,
                new AABB(pos).move(0, 1, 0), e -> true);
        var currentIds = new java.util.HashSet<UUID>();

        for (Entity e : entities) {
            UUID id = e.getUUID();
            currentIds.add(id);
            EntityStompData data = be.entityData.computeIfAbsent(id, k -> new EntityStompData());
            Vec3 currentPos = e.position();
            boolean nowOnGround = e.onGround();

            boolean triggered = false;

            // 1) 实体刚进入盆上方区域
            if (!data.wasAbove) {
                triggered = true;
            }
            // 2) 在盆上方，从空中落地（跳跃落地）
            else if (!data.wasOnGround && nowOnGround) {
                triggered = true;
            }
            // 3) 站在上面，但移动了一定距离（走路经过）
            else if (nowOnGround && be.stompCooldown <= 0
                    && currentPos.distanceToSqr(data.lastPos) > MOVE_THRESHOLD * MOVE_THRESHOLD) {
                triggered = true;
            }

            data.lastPos = currentPos;
            data.wasOnGround = nowOnGround;
            data.wasAbove = true;

            if (triggered && be.stompCooldown <= 0) {
                be.stompCount++;
                be.stompCooldown = STOMP_COOLDOWN;
                be.setChanged();

                level.playSound(null, pos, SoundEvents.SLIME_SQUISH, SoundSource.BLOCKS,
                        0.4f, 0.6f + be.stompCount * 0.15f);

                for (int i = 0; i < 5; i++) {
                    level.addParticle(ParticleTypes.SPLASH,
                            pos.getX() + 0.2 + level.random.nextDouble() * 0.6,
                            pos.getY() + 0.4 + level.random.nextDouble() * 0.15,
                            pos.getZ() + 0.2 + level.random.nextDouble() * 0.6,
                            0, 0.02, 0);
                }

                if (be.stompCount >= STOMP_REQUIRED && !level.isClientSide) {
                    be.convertFruitToJam(level, pos, state);
                }
            }
        }

        // 清理已离开的实体
        be.entityData.keySet().removeIf(id -> !currentIds.contains(id));
    }

    private void convertFruitToJam(Level level, BlockPos pos, BlockState state) {
        ItemStack fruit = getFruitStack();
        var recipe = level.getRecipeManager().getRecipeFor(ModRecipes.WOOD_BASIN_TYPE.get(),
                new WoodBasinRecipe.Input(fruit), level);
        if (recipe.isPresent() && fruitCount > 0) {
            ItemStack jam = recipe.get().value().getResult().copy();
            jam.setCount(jam.getCount() * fruitCount);
            // 产出的果酱数量 = 放入的水果数量
            level.addFreshEntity(new ItemEntity(level,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    jam));
            level.playSound(null, pos, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 0.8f, 1.2f);
        }
        fruitItemId = "";
        fruitCount = 0;
        stompCount = 0;
        stompCooldown = 0;
        entityData.clear();
        level.setBlock(pos, state.setValue(WoodBasinBlock.HAS_FRUIT, false), 3);
    }

    public void setFruit(Item item, int count) {
        this.fruitItemId = BuiltInRegistries.ITEM.getKey(item).toString();
        this.fruitCount = count;
        this.stompCount = 0;
        this.stompCooldown = 0;
        this.entityData.clear();
        setChanged();
    }

    public String getFruitItemId() {
        return fruitItemId;
    }

    public boolean hasFruit() {
        return !fruitItemId.isEmpty();
    }

    public ItemStack getFruitStack() {
        if (fruitItemId.isEmpty()) return ItemStack.EMPTY;
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(fruitItemId));
        return item != null ? new ItemStack(item, fruitCount) : ItemStack.EMPTY;
    }

    public int getTickCount() {
        return tickCount;
    }

    // ===== 网络同步 =====

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        this.loadWithComponents(tag, registries);
    }

    // ===== NBT持久化 =====

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        tickCount = tag.getInt("tickCount");
        fruitItemId = tag.getString("fruitItemId");
        fruitCount = tag.getInt("fruitCount");
        stompCount = tag.getInt("stompCount");
        stompCooldown = tag.getInt("stompCooldown");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("tickCount", tickCount);
        tag.putString("fruitItemId", fruitItemId);
        tag.putInt("fruitCount", fruitCount);
        tag.putInt("stompCount", stompCount);
        tag.putInt("stompCooldown", stompCooldown);
    }
}
