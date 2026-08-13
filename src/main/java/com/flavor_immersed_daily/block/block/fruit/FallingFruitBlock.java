package com.flavor_immersed_daily.block.block.fruit;

import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.entity.FallingFruitEntity;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.AABB;

import java.util.function.Supplier;

public class FallingFruitBlock extends Block {
    private final Supplier<Item> fruitItem;

    public Item getFruitItem() {
        return fruitItem.get();
    }

    public FallingFruitBlock(Supplier<Item> fruitItem, Properties properties) {
        super(properties);
        this.fruitItem = fruitItem;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            if (serverLevel.random.nextFloat() < 0.3f && countFruitEntitiesInChunk(serverLevel, pos) < Config.maxFruitsPerChunk) {
                FallingFruitEntity entity = new FallingFruitEntity(ModEntities.FALLING_FRUIT.get(), level);
                entity.setPos(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5);
                entity.setBlockState(state);
                entity.setDropItem(fruitItem.get());
                entity.setFruitId(net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(fruitItem.get()).toString());
                level.removeBlock(pos, false);
                level.addFreshEntity(entity);
            }
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        if (!(aboveState.getBlock() instanceof LeavesBlock)) {
            // No leaves above, fruit falls off
            level.removeBlock(pos, false);
            tryDropFruit(state, level, pos, random);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        popResource(level, pos, new ItemStack(fruitItem.get()));
        level.removeBlock(pos, false);
        level.playSound(player, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private void tryDropFruit(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < 0.3f && countFruitEntitiesInChunk(level, pos) < Config.maxFruitsPerChunk) {
            FallingFruitEntity entity = new FallingFruitEntity(ModEntities.FALLING_FRUIT.get(), level);
            entity.setPos(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5);
            entity.setBlockState(blockState);
            entity.setDropItem(fruitItem.get());
            level.addFreshEntity(entity);
        } else {
            popResource(level, pos, new ItemStack(fruitItem.get()));
        }
    }

    public static int countFruitEntitiesInChunk(ServerLevel level, BlockPos pos) {
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        return level.getEntitiesOfClass(FallingFruitEntity.class,
                new AABB(chunkX << 4, level.getMinBuildHeight(), chunkZ << 4, (chunkX << 4) + 16, level.getMaxBuildHeight(), (chunkZ << 4) + 16))
                .size();
    }
}
