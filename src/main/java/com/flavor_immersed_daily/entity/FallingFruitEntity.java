package com.flavor_immersed_daily.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class FallingFruitEntity extends Entity {
// 定义水果配置的静态数据
    private static final Map<String, FruitConfig> FRUIT_CONFIGS = new HashMap<>();

// 静态初始化水果配置 — 所有水果高度统一为 0.15f，避免碰撞箱浮空
    static {
// 李子
        FRUIT_CONFIGS.put("flavor_immersed_daily:plum", new FruitConfig(0.25f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:greenplum", new FruitConfig(0.25f, 0.15f));

// 核果类果实
        FRUIT_CONFIGS.put("flavor_immersed_daily:apricot", new FruitConfig(0.2f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:cherry", new FruitConfig(0.15f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:honeypeach", new FruitConfig(0.3f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:lifepeach", new FruitConfig(0.3f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:nectarine", new FruitConfig(0.28f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:winterjujube", new FruitConfig(0.2f, 0.15f));

// 热带类果实
        FRUIT_CONFIGS.put("flavor_immersed_daily:carambola", new FruitConfig(0.3f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:dragonfruit", new FruitConfig(0.35f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:kiwifruit", new FruitConfig(0.25f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:lychee", new FruitConfig(0.18f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:mango", new FruitConfig(0.32f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:mangosteen", new FruitConfig(0.25f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:pawpaw", new FruitConfig(0.35f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:pineapple", new FruitConfig(0.3f, 0.15f));

// 其他果实
        FRUIT_CONFIGS.put("flavor_immersed_daily:greenapple", new FruitConfig(0.28f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:hawthorn", new FruitConfig(0.18f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:loquat", new FruitConfig(0.22f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:pear", new FruitConfig(0.3f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:pomegranate", new FruitConfig(0.32f, 0.15f));
        FRUIT_CONFIGS.put("flavor_immersed_daily:coconut", new FruitConfig(0.32f, 0.15f));
    }

    private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE =
            SynchedEntityData.defineId(FallingFruitEntity.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<ItemStack> DATA_DROP_ITEM =
            SynchedEntityData.defineId(FallingFruitEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Float> DATA_WIDTH =
            SynchedEntityData.defineId(FallingFruitEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_HEIGHT =
            SynchedEntityData.defineId(FallingFruitEntity.class, EntityDataSerializers.FLOAT);
// 添加水果类型的标识
    private static final EntityDataAccessor<String> DATA_FRUIT_ID =
            SynchedEntityData.defineId(FallingFruitEntity.class, EntityDataSerializers.STRING);
// 是否已落地（落地逻辑在服务端 tick 中置位，需同步到客户端供渲染层附加旋转翻滚）
    private static final EntityDataAccessor<Boolean> DATA_SETTLED =
            SynchedEntityData.defineId(FallingFruitEntity.class, EntityDataSerializers.BOOLEAN);

    private int age = 0;

    public FallingFruitEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

// 添加设置水果ID的方法
    public void setFruitId(String fruitId) {
        this.entityData.set(DATA_FRUIT_ID, fruitId);
// 根据水果ID设置默认尺寸
        FruitConfig config = FRUIT_CONFIGS.get(fruitId);
        if (config != null) {
            this.entityData.set(DATA_WIDTH, config.width);
            this.entityData.set(DATA_HEIGHT, config.height);
        } else {
// 默认尺寸
            this.entityData.set(DATA_WIDTH, 0.25f);
            this.entityData.set(DATA_HEIGHT, 0.25f);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_BLOCK_STATE, Blocks.AIR.defaultBlockState());
        builder.define(DATA_DROP_ITEM, ItemStack.EMPTY);
// 默认宽度
        builder.define(DATA_WIDTH, 0.25f);
// 默认高度
        builder.define(DATA_HEIGHT, 0.25f);
// 默认空ID
        builder.define(DATA_FRUIT_ID, "");
// 默认未落地
        builder.define(DATA_SETTLED, false);
    }

    public void setBlockState(BlockState state) {
        this.entityData.set(DATA_BLOCK_STATE, state);
    }

    public BlockState getBlockState() {
        return this.entityData.get(DATA_BLOCK_STATE);
    }

    public void setDropItem(Item item) {
        this.entityData.set(DATA_DROP_ITEM, new ItemStack(item));
    }

    public ItemStack getDropItem() {
        return this.entityData.get(DATA_DROP_ITEM);
    }

// 获取实体尺寸的方法
    public float getCustomWidth() {
        return this.entityData.get(DATA_WIDTH);
    }

    public float getCustomHeight() {
        return this.entityData.get(DATA_HEIGHT);
    }

// 获取水果ID
    public String getFruitId() {
        return this.entityData.get(DATA_FRUIT_ID);
    }

// 是否已落地（落地后渲染层会附加随机的旋转翻滚角度）
    public boolean isSettled() {
        return this.entityData.get(DATA_SETTLED);
    }

    @Override
    public void tick() {
        super.tick();

// 添加重力效果
        if (!this.level().isClientSide) {
            if (!this.entityData.get(DATA_SETTLED)) {
// 下落期间保持完整挂果方块的实体渲染，触地后停留在地面上。
                this.setDeltaMovement(this.getDeltaMovement().add(0, -0.04, 0));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.98, 0.98, 0.98));
                this.move(MoverType.SELF, this.getDeltaMovement());

                if (this.onGround()) {
                    this.entityData.set(DATA_SETTLED, true);
                    this.setDeltaMovement(0, 0, 0);
// 落地后随机给一个水平偏移，让果子散落在落点周围，而不是挤在正下方
                    double dx = (this.random.nextDouble() - 0.5) * 0.4;
                    double dz = (this.random.nextDouble() - 0.5) * 0.4;
                    this.setPos(this.getX() + dx, this.getY(), this.getZ() + dz);
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                            SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 0.3F, 1.0F);
                }
            }

            age++;
// 5分钟后自动消失 (6000 ticks)
            if (age > 6000) {
                this.discard();
            }
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (this.level().isClientSide) {
            return InteractionResult.CONSUME;
        }
        ItemStack drop = getDropItem();
        if (!drop.isEmpty()) {
            ItemStack remaining = drop.copy();
            player.getInventory().add(remaining);
            if (!remaining.isEmpty()) {
                player.drop(remaining, false);
            }
        }
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 1.0F);
        this.discard();
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("BlockState")) {
            BlockState state = NbtUtils.readBlockState(
                    this.level().holderLookup(Registries.BLOCK),
                    tag.getCompound("BlockState"));
            this.setBlockState(state);
        }
        if (tag.contains("DropItem")) {
            ItemStack.parse(this.level().registryAccess(), tag.getCompound("DropItem"))
                    .ifPresent(stack -> this.entityData.set(DATA_DROP_ITEM, stack));
        }
        if (tag.contains("Age")) {
            this.age = tag.getInt("Age");
        }
        this.entityData.set(DATA_SETTLED, tag.getBoolean("Settled"));
// 读取实体尺寸数据
        if (tag.contains("Width")) {
            this.entityData.set(DATA_WIDTH, tag.getFloat("Width"));
        }
        if (tag.contains("Height")) {
            this.entityData.set(DATA_HEIGHT, tag.getFloat("Height"));
        }
// 读取水果ID
        if (tag.contains("FruitId")) {
            this.entityData.set(DATA_FRUIT_ID, tag.getString("FruitId"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.put("BlockState", NbtUtils.writeBlockState(this.getBlockState()));
        if (!getDropItem().isEmpty()) {
            CompoundTag itemTag = new CompoundTag();
            getDropItem().save(this.level().registryAccess(), itemTag);
            tag.put("DropItem", itemTag);
        }
        tag.putInt("Age", this.age);
        tag.putBoolean("Settled", this.entityData.get(DATA_SETTLED));
// 保存实体尺寸数据
        tag.putFloat("Width", getCustomWidth());
        tag.putFloat("Height", getCustomHeight());
// 保存水果ID
        tag.putString("FruitId", getFruitId());
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

// 动态返回实体的宽度

    public float getWidth() {
        return getCustomWidth();
    }

// 动态返回实体的高度
    public float getHeight() {
        return getCustomHeight();
    }

// 水果配置内部类
    private static class FruitConfig {
        public final float width;
        public final float height;

        public FruitConfig(float width, float height) {
            this.width = width;
            this.height = height;
        }
    }
}
