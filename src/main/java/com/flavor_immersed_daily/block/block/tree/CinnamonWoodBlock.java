package com.flavor_immersed_daily.block.block.tree;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

/**
 * 桂皮树干
 * RIPE=true  (stage0)：树上长有桂皮，右键可收获桂皮并变为光秃
 * RIPE=false (stage1)：光秃树干，可自然生长或被骨粉催熟变回有桂皮
 */
public class CinnamonWoodBlock extends Block implements BonemealableBlock {
    public static final BooleanProperty RIPE = BooleanProperty.create("ripe");

    private final Supplier<Item> cinnamonItem;

    public CinnamonWoodBlock(Supplier<Item> cinnamonItem, BlockBehaviour.Properties properties) {
        super(properties);
        this.cinnamonItem = cinnamonItem;
        this.registerDefaultState(this.stateDefinition.any().setValue(RIPE, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return simpleCodec(p -> new CinnamonWoodBlock(cinnamonItem, p));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(RIPE);
    }

    // 碰撞箱贴合模型大小（模型为 10x16x10，即 3/16 ~ 13/16），不遮挡底下的方块
    private static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 16, 13);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // 光秃的树干有几率长出桂皮
        if (!state.getValue(RIPE) && random.nextInt(10) == 0) {
            level.setBlock(pos, state.setValue(RIPE, true), 3);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hitResult) {
        return harvest(state, level, pos, player);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {
        // 骨粉催熟由 BonemealItem 处理，不会走到这里
        if (harvest(state, level, pos, player).consumesAction()) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private InteractionResult harvest(BlockState state, Level level, BlockPos pos, Player player) {
        if (state.getValue(RIPE)) {
            if (!level.isClientSide) {
                popResource(level, pos, new ItemStack(cinnamonItem.get()));
                level.setBlock(pos, state.setValue(RIPE, false), 3);
                level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    // ==================== 骨粉催熟 ====================

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !state.getValue(RIPE);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return !state.getValue(RIPE);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(RIPE, true), 3);
    }
}
