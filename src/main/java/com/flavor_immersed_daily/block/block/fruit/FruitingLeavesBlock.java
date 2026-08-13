package com.flavor_immersed_daily.block.block.fruit;

import com.flavor_immersed_daily.gameplay.FruitHarvestHandler;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
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
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class FruitingLeavesBlock extends LeavesBlock {
    public static final BooleanProperty FRUITING = BooleanProperty.create("fruiting");
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
                .setValue(FRUITING, false));
    }

    @Override
    public MapCodec<? extends FruitingLeavesBlock> codec() {
        return simpleCodec(properties -> new FruitingLeavesBlock(fruitItem, hangingFruit, properties));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, PERSISTENT, WATERLOGGED, FRUITING);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return super.isRandomlyTicking(state) || (!state.getValue(FRUITING) && !state.getValue(PERSISTENT));
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);

        BlockState currentState = level.getBlockState(pos);
        if (currentState.is(this)
                && !currentState.getValue(FRUITING)
                && !currentState.getValue(PERSISTENT)
                && random.nextInt(5) == 0) {
            growFruit(level, pos, currentState);
        }
    }

    private void growFruit(ServerLevel level, BlockPos pos, BlockState state) {
        if (hangingFruit == null || trySpawnHangingFruit(level, pos)) {
            level.setBlock(pos, state.setValue(FRUITING, true), 3);
        }
    }

    private boolean trySpawnHangingFruit(ServerLevel level, BlockPos pos) {
        if (hangingFruit == null) return true;

        BlockPos targetPos = pos.below();
        if (!level.getBlockState(targetPos).isAir()) return false;

        level.setBlock(targetPos, hangingFruit.get().defaultBlockState(), 3);
        return level.getBlockState(targetPos).is(hangingFruit.get());
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
        level.setBlock(pos, state.setValue(FRUITING, false), 3);
        FruitHarvestHandler.tryDropVariantFruit(level, pos, this);
        return true;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, net.minecraft.world.InteractionHand hand,
                                              BlockHitResult hitResult) {
        if (stack.is(Items.BONE_MEAL) && !state.getValue(FRUITING)) {
            if (level instanceof ServerLevel serverLevel) {
                growFruit(serverLevel, pos, state);
                if (serverLevel.getBlockState(pos).getValue(FRUITING)) {
                    stack.consume(1, player);
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hitResult) {
        if (!state.getValue(FRUITING)) return InteractionResult.PASS;

        if (level instanceof ServerLevel serverLevel && harvestFruit(serverLevel, pos)) {
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
                    SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
