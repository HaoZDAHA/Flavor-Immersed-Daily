package com.flavor_immersed_daily.block.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

// 贴合原版的作物方块基类。参考 Farmer's Delight 的设计模式。<br>
// 种子和产物依赖在构造时以延迟 Supplier 提供，避免注册顺序耦合。
public class FIDCropBlock extends CropBlock {

    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(0, 0, 0, 16, 4, 16),
            Block.box(0, 0, 0, 16, 6, 16),
            Block.box(0, 0, 0, 16, 8, 16),
            Block.box(0, 0, 0, 16, 10, 16),
            Block.box(0, 0, 0, 16, 12, 16),
            Block.box(0, 0, 0, 16, 14, 16),
            Block.box(0, 0, 0, 16, 16, 16)
    };

    private final int maxAge;
    private final Supplier<? extends ItemLike> seedSupplier;
    private final Supplier<? extends ItemLike> cropSupplier;

    public FIDCropBlock(Properties properties, int maxAge, Supplier<? extends ItemLike> seedSupplier,
                        Supplier<? extends ItemLike> cropSupplier) {
        super(properties);
        this.maxAge = maxAge;
        this.seedSupplier = seedSupplier;
        this.cropSupplier = cropSupplier;
    }

    @Override
    public int getMaxAge() {
        return maxAge;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        ItemLike seed = seedSupplier.get();
        return seed != null ? seed : super.getBaseSeedId();
    }

// 获取成熟后收获的产物（用于外部收割逻辑，如猪踩踏）。
    public ItemLike getCropItem() {
        ItemLike crop = cropSupplier.get();
        return crop != null ? crop : getBaseSeedId();
    }

// 获取种子物品（供外部收割逻辑使用）。
    public ItemLike getSeedItem() {
        return getBaseSeedId();
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return super.getAgeProperty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int age = this.getAge(state);
        return SHAPES[Math.min(age, SHAPES.length - 1)];
    }

// 右键收割：作物成熟时，掉落 3 个果实 + 1 个种子，生成破坏粒子并将作物重置到初始阶段。
// 该方法为 public，供外部（如 RightClickBlock 事件）在手持可放置物品时也能触发收获，
// 保证无论手持何种物品，右键逻辑都与空手完全一致。
    public void harvest(BlockState state, Level level, BlockPos pos) {
        if (!level.isClientSide) {
// 右键收获：一次掉落 3 个果实 + 1 个种子
            ItemLike crop = cropSupplier.get();
            ItemLike seed = seedSupplier.get();
            if (crop != null) {
                Block.popResource(level, pos, new ItemStack(crop, 3));
            }
            if (seed != null) {
                Block.popResource(level, pos, new ItemStack(seed));
            }
// 破坏粒子 + 方块破坏音效（2001 事件，客户端会播放对应音效并生成破坏粒子）
            level.levelEvent(null, 2001, pos, Block.getId(state));
            level.setBlock(pos, this.getStateForAge(0), Block.UPDATE_CLIENTS);
        }
    }

// 覆盖 useItemOn，使作物成熟时即使主手持其他物品也必定进入收获，
// 避免 SeedItem 等植物物品的 useOn 抢占右键导致“有时没有掉落物”。
// （NeoForge 1.21.1 没有 Block.use(...hand...)，手持物品走 useItemOn）
    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                           Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (state.getValue(AGE) >= this.getMaxAge()) {
            this.harvest(state, level, pos);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hitResult) {
        if (state.getValue(AGE) >= this.getMaxAge()) {
            this.harvest(state, level, pos);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
}
