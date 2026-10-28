package com.flavor_immersed_daily.entity;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEntities;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.item.FirecrackerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FirecrackerEntity extends ThrowableItemProjectile {

    private ItemStack firecrackerStack = ItemStack.EMPTY;

    public FirecrackerEntity(EntityType<? extends FirecrackerEntity> type, Level level) {
        super(type, level);
    }

    public FirecrackerEntity(Level level, LivingEntity shooter, ItemStack stack) {
        super(ModEntities.FIRECRACKER_ENTITY.get(), shooter, level);
        this.firecrackerStack = stack.copy();
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.WRESTLING_GUN.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level().isClientSide) return;

        ServerLevel serverLevel = (ServerLevel) this.level();
        Vec3 pos = this.position();

// 读取配置
        int shape = 0;
        int color = 0xFF0000;
        int fadeColor = 0xFFFFFF;
        if (!firecrackerStack.isEmpty()) {
            shape = FirecrackerHelper.getShape(firecrackerStack);
            color = FirecrackerHelper.getColor(firecrackerStack);
            fadeColor = FirecrackerHelper.getFadeColor(firecrackerStack);
        }

// 创建烟花爆炸效果（1.20.1 使用 NBT 组件）
        ItemStack fireworkItem = new ItemStack(Items.FIREWORK_ROCKET);
        CompoundTag fireworkTag = fireworkItem.getOrCreateTag();
        CompoundTag fireworks = new CompoundTag();
        fireworks.putByte("Flight", (byte) 1);
        ListTag explosions = new ListTag();
        CompoundTag explosionTag = new CompoundTag();
        explosionTag.putByte("Type", (byte) shape);
        explosionTag.putIntArray("Colors", new int[]{color, fadeColor});
        explosionTag.putIntArray("FadeColors", new int[]{});
        explosionTag.putBoolean("Flicker", false);
        explosionTag.putBoolean("Trail", false);
        explosions.add(explosionTag);
        fireworks.put("Explosions", explosions);
        fireworkTag.put("Fireworks", fireworks);

        net.minecraft.world.entity.projectile.FireworkRocketEntity rocket = new net.minecraft.world.entity.projectile.FireworkRocketEntity(
                serverLevel, null,
                pos.x, pos.y, pos.z,
                fireworkItem
        );
        serverLevel.addFreshEntity(rocket);
        rocket.level().broadcastEntityEvent(rocket, (byte) 17);
        rocket.gameEvent(net.minecraft.world.level.gameevent.GameEvent.EXPLODE, rocket.getOwner());
        rocket.discard();

        serverLevel.playSound(null, pos.x, pos.y, pos.z,
                SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 1.2f, 1.0f);

        this.discard();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (!firecrackerStack.isEmpty()) {
            tag.put("firecracker_stack", firecrackerStack.save(tag));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("firecracker_stack")) {
            firecrackerStack = ItemStack.of(tag.getCompound("firecracker_stack"));
        }
    }
}
