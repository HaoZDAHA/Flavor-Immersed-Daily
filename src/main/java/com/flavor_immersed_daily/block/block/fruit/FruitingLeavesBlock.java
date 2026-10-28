package com.flavor_immersed_daily.block.block.fruit;

import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.gameplay.FruitHarvestHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class FruitingLeavesBlock extends LeavesBlock {
    public static final BooleanProperty FRUITING = BooleanProperty.create("fruiting");
    public static final IntegerProperty MATURITY = IntegerProperty.create("maturity", 0, 4);
    public static final int MAX_MATURITY = 4;
    private static final int LEGACY_HANGING_FRUIT_SEARCH_DEPTH = 12;

    private final Supplier<Item> fruitItem;
    private final Supplier<? extends Block> hangingFruit;

    public Item getFruitItem() {
        return fruitItem.get();
    }

    public FruitingLeavesBlock(Supplier<Item> fruitItem, BlockBehaviour.Properties properties) {
        this(fruitItem, null, properties);
    }

    public FruitingLeavesBlock(Supplier<Item> fruitItem, Supplier<? extends Block> hangingFruit,
                               BlockBehaviour.Properties properties) {
        super(properties);
        this.fruitItem = fruitItem;
        this.hangingFruit = hangingFruit;
        registerDefaultState(stateDefinition.any()
                .setValue(DISTANCE, 1)
                .setValue(PERSISTENT, false)
                .setValue(WATERLOGGED, false)
                .setValue(FRUITING, false)
                .setValue(MATURITY, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, PERSISTENT, WATERLOGGED, FRUITING, MATURITY);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
// 自然树叶（persistent=false）需要随机刻做距离/枯萎检查；
// 未挂果的树叶（含玩家手动放置的 persistent 树叶）需要随机刻自然成熟
        return !state.getValue(PERSISTENT) || !state.getValue(FRUITING);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
// 只有非 persistent（自然生成）的树叶参与原版距离/枯萎检查，玩家放置的 persistent 树叶不枯萎
        if (!state.getValue(PERSISTENT)) {
            super.randomTick(state, level, pos, random);
        }

        BlockState currentState = level.getBlockState(pos);
        if (currentState.is(this) && !currentState.getValue(FRUITING)) {
            tryMatureFruit(level, pos, currentState, random, Config.naturalFruitMaturityChance);
        }
    }

    private boolean tryMatureFruit(ServerLevel level, BlockPos pos, BlockState state, RandomSource random,
                                   double maturityChance) {
        if (random.nextDouble() >= maturityChance) return false;

        int maturity = Math.min(MAX_MATURITY, state.getValue(MATURITY) + 1);
        BlockState maturedState = state.setValue(MATURITY, maturity);
        if (maturity < MAX_MATURITY) {
            level.setBlock(pos, maturedState, 3);
            return true;
        }

        level.setBlock(pos, maturedState.setValue(FRUITING, true), 3);
        if (hangingFruit != null && random.nextDouble() < Config.hangingFruitChance) {
            trySpawnHangingFruit(level, pos);
        }
        return true;
    }

// 树干催熟：由 FruitHarvestHandler 在对树干使用骨粉时调用，
// 按给定概率让本片树叶推进一档成熟（未挂果时）。
    public boolean matureOnce(ServerLevel level, BlockPos pos, BlockState state, RandomSource random, double chance) {
        if (!state.is(this) || state.getValue(FRUITING)) return false;
        return tryMatureFruit(level, pos, state, random, chance);
    }

    private void trySpawnHangingFruit(ServerLevel level, BlockPos pos) {
        BlockPos targetPos = pos.below();
        if (!level.getBlockState(targetPos).isAir()) return;

        level.setBlock(targetPos, hangingFruit.get().defaultBlockState(), 3);
    }

    private BlockPos findHangingFruit(Level level, BlockPos leafPos) {
        if (hangingFruit == null) return null;

        BlockPos.MutableBlockPos mutable = leafPos.below().mutable();
        for (int i = 0; i < LEGACY_HANGING_FRUIT_SEARCH_DEPTH; i++) {
            BlockState state = level.getBlockState(mutable);
            if (state.getBlock() instanceof LeavesBlock) {
                mutable.move(Direction.DOWN);
                continue;
            }
            return state.is(hangingFruit.get()) ? mutable.immutable() : null;
        }
        return null;
    }

    public boolean harvestFruit(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!state.is(this) || !state.getValue(FRUITING)) return false;

        BlockPos hangingFruitPos = findHangingFruit(level, pos);
        if (hangingFruitPos != null) {
            level.removeBlock(hangingFruitPos, false);
        }
        popResource(level, pos, new ItemStack(fruitItem.get()));
        level.setBlock(pos, state.setValue(FRUITING, false).setValue(MATURITY, 0), 3);
        FruitHarvestHandler.tryDropVariantFruit(level, pos, this);
        return true;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
// 对树叶直接使用骨粉：单独催熟这一块挂果树叶（整树催熟走 FruitHarvestHandler 的树干分支）
        if (stack.is(Items.BONE_MEAL) && !state.getValue(FRUITING)) {
            if (level instanceof ServerLevel serverLevel) {
                RandomSource random = serverLevel.random;
                if (tryMatureFruit(serverLevel, pos, state, random, Config.boneMealFruitMaturityChance)) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    serverLevel.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            15, 0.5, 0.5, 0.5, 0.0);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (!state.getValue(FRUITING)) return InteractionResult.PASS;

        if (level instanceof ServerLevel serverLevel && harvestFruit(serverLevel, pos)) {
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
                    SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}