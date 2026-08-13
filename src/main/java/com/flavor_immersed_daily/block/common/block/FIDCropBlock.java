package com.flavor_immersed_daily.block.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
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

/**
 * 贴合原版的作物方块基类。参考 Farmer's Delight 的设计模式。<br>
 * 种子和产物依赖在构造时以延迟 Supplier 提供，避免注册顺序耦合。
 */
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

    /**
     * 获取成熟后收获的产物（用于外部收割逻辑，如猪踩踏）。
     */
    public ItemLike getCropItem() {
        ItemLike crop = cropSupplier.get();
        return crop != null ? crop : getBaseSeedId();
    }

    /**
     * 获取种子物品（供外部收割逻辑使用）。
     */
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

    /**
     * 右键收割：作物成熟时，掉落产物并将作物重置到初始阶段。
     */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hitResult) {
        if (state.getValue(AGE) >= this.getMaxAge()) {
            if (!level.isClientSide) {
                ItemLike crop = cropSupplier.get();
                if (crop != null) {
                    Block.popResource(level, pos, new ItemStack(crop));
                }
                level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlock(pos, this.getStateForAge(0), Block.UPDATE_CLIENTS);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
}
