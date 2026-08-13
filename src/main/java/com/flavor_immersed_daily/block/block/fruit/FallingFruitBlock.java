package com.flavor_immersed_daily.block.block.fruit;

import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.entity.FallingFruitEntity;
import com.flavor_immersed_daily.all.ModEntities;
import com.flavor_immersed_daily.gameplay.FruitHarvestHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.AABB;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;

public class FallingFruitBlock extends Block {
    private static final int MAX_SUPPORT_SEARCH_DEPTH = 12;
    private static final Map<ServerLevel, FruitCountCache> FRUIT_COUNT_CACHE = new WeakHashMap<>();
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
                clearSupportingLeaf(serverLevel, pos);
                level.removeBlock(pos, false);
                spawnFallingFruit(serverLevel, pos, state);
            } else if (!canSurvive(state, level, pos)) {
                serverLevel.scheduleTick(pos, this, 1);
            }
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.above()).getBlock() instanceof LeavesBlock;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP && !canSurvive(state, level, pos)) {
            level.scheduleTick(pos, this, 1);
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canSurvive(state, level, pos)) {
            level.removeBlock(pos, false);
            tryDropFruit(state, level, pos, random);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            if (harvestFruit((ServerLevel) level, pos)) {
                level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private void tryDropFruit(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < 0.3f && countFruitEntitiesInChunk(level, pos) < Config.maxFruitsPerChunk) {
            spawnFallingFruit(level, pos, blockState);
        } else {
            popResource(level, pos, new ItemStack(fruitItem.get()));
        }
    }

    private void spawnFallingFruit(ServerLevel level, BlockPos pos, BlockState blockState) {
        FallingFruitEntity entity = new FallingFruitEntity(ModEntities.FALLING_FRUIT.get(), level);
        entity.setPos(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5);
        entity.setBlockState(blockState);
        entity.setDropItem(fruitItem.get());
        entity.setFruitId(BuiltInRegistries.ITEM.getKey(fruitItem.get()).toString());
        if (level.addFreshEntity(entity)) {
            incrementCachedFruitCount(level, pos);
        }
    }

    public boolean harvestFruit(ServerLevel level, BlockPos pos) {
        if (!level.getBlockState(pos).is(this)) return false;

        FruitingLeavesBlock supportingLeaf = clearSupportingLeaf(level, pos);
        popResource(level, pos, new ItemStack(fruitItem.get()));
        level.removeBlock(pos, false);
        if (supportingLeaf != null) {
            FruitHarvestHandler.tryDropVariantFruit(level, pos, supportingLeaf);
        }
        return true;
    }

    private FruitingLeavesBlock clearSupportingLeaf(ServerLevel level, BlockPos fruitPos) {
        BlockPos.MutableBlockPos mutable = fruitPos.above().mutable();
        for (int i = 0; i < MAX_SUPPORT_SEARCH_DEPTH; i++) {
            BlockState state = level.getBlockState(mutable);
            if (state.getBlock() instanceof FruitingLeavesBlock leavesBlock
                    && leavesBlock.getFruitItem() == fruitItem.get()
                    && state.getValue(FruitingLeavesBlock.FRUITING)) {
                level.setBlock(mutable, state.setValue(FruitingLeavesBlock.FRUITING, false), 3);
                return leavesBlock;
            }
            if (!(state.getBlock() instanceof LeavesBlock)) return null;
            mutable.move(Direction.UP);
        }
        return null;
    }

    public static int countFruitEntitiesInChunk(ServerLevel level, BlockPos pos) {
        FruitCountCache cache = getCurrentCache(level);
        long chunkKey = ChunkPos.asLong(pos);
        return cache.counts.computeIfAbsent(chunkKey, ignored -> {
            int chunkX = pos.getX() >> 4;
            int chunkZ = pos.getZ() >> 4;
            return level.getEntitiesOfClass(FallingFruitEntity.class,
                    new AABB(chunkX << 4, level.getMinBuildHeight(), chunkZ << 4,
                            (chunkX << 4) + 16, level.getMaxBuildHeight(), (chunkZ << 4) + 16)).size();
        });
    }

    private static void incrementCachedFruitCount(ServerLevel level, BlockPos pos) {
        FruitCountCache cache = getCurrentCache(level);
        long chunkKey = ChunkPos.asLong(pos);
        cache.counts.computeIfPresent(chunkKey, (ignored, count) -> count + 1);
    }

    private static FruitCountCache getCurrentCache(ServerLevel level) {
        FruitCountCache cache = FRUIT_COUNT_CACHE.computeIfAbsent(level, ignored -> new FruitCountCache());
        long gameTime = level.getGameTime();
        if (cache.gameTime != gameTime) {
            cache.gameTime = gameTime;
            cache.counts.clear();
        }
        return cache;
    }

    private static final class FruitCountCache {
        private long gameTime = Long.MIN_VALUE;
        private final Map<Long, Integer> counts = new HashMap<>();
    }
}
