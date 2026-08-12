package com.flavor_immersed_daily.block;

import com.flavor_immersed_daily.FruitHarvestHandler;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
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

    private final Supplier<Item> fruitItem;
    private final Supplier<? extends Block> hangingFruit;

    public Item getFruitItem() {
        return fruitItem.get();
    }

    public FruitingLeavesBlock(Supplier<Item> fruitItem, BlockBehaviour.Properties properties) {
        this(fruitItem, null, properties);
    }

    public FruitingLeavesBlock(Supplier<Item> fruitItem, Supplier<? extends Block> hangingFruit, BlockBehaviour.Properties properties) {
        super(properties);
        this.fruitItem = fruitItem;
        this.hangingFruit = hangingFruit;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(DISTANCE, 1)
                .setValue(PERSISTENT, false)
                .setValue(WATERLOGGED, false)
                .setValue(FRUITING, false));
    }

    @Override
    public MapCodec<? extends FruitingLeavesBlock> codec() {
        return simpleCodec(p -> new FruitingLeavesBlock(this.fruitItem, this.hangingFruit, p));
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
        // 让原版 LeavesBlock 处理距离计算和腐烂逻辑
        super.randomTick(state, level, pos, random);

        // 获取更新后的方块状态（原版可能已经销毁了方块）
        BlockState currentState = level.getBlockState(pos);
        if (!(currentState.getBlock() instanceof FruitingLeavesBlock)) return;

        // 处理结果化逻辑
        if (!currentState.getValue(FRUITING) && !currentState.getValue(PERSISTENT)) {
            if (random.nextInt(5) == 0) {
                level.setBlock(pos, currentState.setValue(FRUITING, true), 3);
                trySpawnHangingFruit(level, pos);
            }
        }
    }
    
    private void trySpawnHangingFruit(ServerLevel level, BlockPos pos) {
        if (hangingFruit == null) return;

        // 找到悬挂果实应该放置的位置
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);

        if (belowState.isAir()) {
            // 下方是空气，直接放置
            level.setBlock(below, hangingFruit.get().defaultBlockState(), 3);
        } else if (belowState.getBlock() instanceof LeavesBlock) {
            // 下方是树叶，找到最底端的树叶
            BlockPos bottomPos = findBottomLeaves(level, below);
            if (bottomPos != null) {
                BlockPos targetPos = bottomPos.below();
                if (level.getBlockState(targetPos).isAir()) {
                    level.setBlock(targetPos, hangingFruit.get().defaultBlockState(), 3);
                }
            }
        }
    }

    private BlockPos findBottomLeaves(ServerLevel level, BlockPos start) {
        BlockPos.MutableBlockPos mutable = start.mutable();
        int maxDepth = 20;
        BlockPos lastLeaf = null;

        for (int i = 0; i < maxDepth; i++) {
            BlockState state = level.getBlockState(mutable);
            if (state.getBlock() instanceof LeavesBlock) {
                lastLeaf = mutable.immutable();
                mutable.move(0, -1, 0);
            } else {
                break;
            }
        }
        return lastLeaf;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, net.minecraft.world.InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(Items.BONE_MEAL) && !state.getValue(FRUITING)) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(FRUITING, true), 3);
                stack.consume(1, player);
                if (level instanceof ServerLevel serverLevel) {
                    trySpawnHangingFruit(serverLevel, pos);
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (state.getValue(FRUITING)) {
            popResource(level, pos, new ItemStack(fruitItem.get()));
            level.setBlock(pos, state.setValue(FRUITING, false), 3);
            level.playSound(player, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            // 小概率掉落稀有水果变种
            if (!level.isClientSide) {
                FruitHarvestHandler.tryDropVariantFruit(level, pos, this);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}
