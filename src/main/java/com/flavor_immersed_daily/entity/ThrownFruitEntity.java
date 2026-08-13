package com.flavor_immersed_daily.entity;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEntities;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownFruitEntity extends ThrowableItemProjectile {

    private static final String TAG_DROP_ITEM_1 = "drop_item_1";
    private static final String TAG_DROP_COUNT_1 = "drop_count_1";
    private static final String TAG_DROP_ITEM_2 = "drop_item_2";
    private static final String TAG_DROP_COUNT_2 = "drop_count_2";
    private static final String TAG_DROP_ITEM_3 = "drop_item_3";
    private static final String TAG_DROP_COUNT_3 = "drop_count_3";
    private static final String TAG_DAMAGE = "damage";

    private String dropItem1 = "";
    private int dropCount1 = 0;
    private String dropItem2 = "";
    private int dropCount2 = 0;
    private String dropItem3 = "";
    private int dropCount3 = 0;
    private float damage = 0.0f;

    public ThrownFruitEntity(EntityType<? extends ThrownFruitEntity> type, Level level) {
        super(type, level);
    }

    public ThrownFruitEntity(Level level, LivingEntity shooter, ItemStack stack,
                             String dropItem1, int dropCount1,
                             String dropItem2, int dropCount2,
                             String dropItem3, int dropCount3,
                             float damage) {
        super(ModEntities.THROWN_FRUIT_ENTITY.get(), shooter, level);
        this.setItem(stack);
        this.dropItem1 = dropItem1;
        this.dropCount1 = dropCount1;
        this.dropItem2 = dropItem2;
        this.dropCount2 = dropCount2;
        this.dropItem3 = dropItem3;
        this.dropCount3 = dropCount3;
        this.damage = damage;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.AIR;
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level().isClientSide) return;

        ServerLevel serverLevel = (ServerLevel) this.level();
        Vec3 pos = this.position();

        // 鏂瑰潡鐮村潖闊虫晥
        serverLevel.playSound(null, pos.x, pos.y, pos.z,
                SoundEvents.STONE_BREAK, SoundSource.PLAYERS, 1.0f, 0.8f + this.random.nextFloat() * 0.4f);

        // 绮掑瓙鐗规晥锛堢墿鍝佺汗鐞嗙牬纰庯級
        ItemStack thrownItem = this.getItem();
        if (!thrownItem.isEmpty()) {
            serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, thrownItem),
                    pos.x, pos.y, pos.z, 20, 0.5, 0.5, 0.5, 0.1);
        }

        // 浼ゅ鍜屽嚮閫€
        if (result instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity target) {
            target.hurt(target.damageSources().thrown(this, this.getOwner()), damage);
            Vec3 knockback = this.getDeltaMovement().normalize().scale(1.5);
            target.setDeltaMovement(target.getDeltaMovement().add(knockback.x, 0.3, knockback.z));
            target.hurtMarked = true;
        }

        // 鎺夎惤鐗?
        spawnDrop(serverLevel, pos, dropItem1, dropCount1);
        spawnDrop(serverLevel, pos, dropItem2, dropCount2);
        spawnDrop(serverLevel, pos, dropItem3, dropCount3);

        this.discard();
    }

    private void spawnDrop(ServerLevel level, Vec3 pos, String itemId, int count) {
        if (itemId.isEmpty() || count <= 0) return;
        net.minecraft.core.registries.BuiltInRegistries.ITEM.getOptional(net.minecraft.resources.ResourceLocation.parse(itemId))
                .ifPresent(item -> {
                    ItemEntity entity = new ItemEntity(level, pos.x, pos.y, pos.z, new ItemStack(item, count));
                    entity.setDeltaMovement(
                            (this.random.nextDouble() - 0.5) * 0.3,
                            0.2,
                            (this.random.nextDouble() - 0.5) * 0.3
                    );
                    level.addFreshEntity(entity);
                });
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString(TAG_DROP_ITEM_1, dropItem1);
        tag.putInt(TAG_DROP_COUNT_1, dropCount1);
        tag.putString(TAG_DROP_ITEM_2, dropItem2);
        tag.putInt(TAG_DROP_COUNT_2, dropCount2);
        tag.putString(TAG_DROP_ITEM_3, dropItem3);
        tag.putInt(TAG_DROP_COUNT_3, dropCount3);
        tag.putFloat(TAG_DAMAGE, damage);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.dropItem1 = tag.getString(TAG_DROP_ITEM_1);
        this.dropCount1 = tag.getInt(TAG_DROP_COUNT_1);
        this.dropItem2 = tag.getString(TAG_DROP_ITEM_2);
        this.dropCount2 = tag.getInt(TAG_DROP_COUNT_2);
        this.dropItem3 = tag.getString(TAG_DROP_ITEM_3);
        this.dropCount3 = tag.getInt(TAG_DROP_COUNT_3);
        this.damage = tag.getFloat(TAG_DAMAGE);
    }
}
