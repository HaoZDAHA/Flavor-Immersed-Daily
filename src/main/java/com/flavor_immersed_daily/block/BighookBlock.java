package com.flavor_immersed_daily.block;

import com.flavor_immersed_daily.Config;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

//大挂钩方块 — 悬挂动物胴体进行屠宰加工
//动物: 0=none, 1=cattle, 2=sheep, 3=pig, 4=chicken
//屠宰的阶段: 0(空) → 1(挂上) → 2 → 3 → 4 → 5 → 回收回0

public class BighookBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {

    public static final IntegerProperty ANIMAL = IntegerProperty.create("animal", 0, 4);
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 6);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE = Shapes.or(
            box(2, 0, 2, 14, 16, 14)
    );

    private final Supplier<? extends Item> deadCattle;
    private final Supplier<? extends Item> deadSheep;
    private final Supplier<? extends Item> deadPig;
    private final Supplier<? extends Item> deadChicken;
    private final Supplier<? extends Item> pluckedChicken;
    private final Supplier<? extends Item> bledChicken;

    public BighookBlock(Supplier<? extends Item> deadCattle, Supplier<? extends Item> deadSheep,
                        Supplier<? extends Item> deadPig, Supplier<? extends Item> deadChicken,
                        Supplier<? extends Item> pluckedChicken, Supplier<? extends Item> bledChicken) {
        super(BlockBehaviour.Properties.of()
                .strength(2.0f, 6.0f)
                .noOcclusion()
                .noCollission());
        this.deadCattle = deadCattle;
        this.deadSheep = deadSheep;
        this.deadPig = deadPig;
        this.deadChicken = deadChicken;
        this.pluckedChicken = pluckedChicken;
        this.bledChicken = bledChicken;
        registerDefaultState(stateDefinition.any()
                .setValue(ANIMAL, 0)
                .setValue(STAGE, 0)
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ANIMAL, STAGE, FACING, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        FluidState fluidState = context.getLevel().getFluidState(pos);
        return canSurvive(defaultBlockState(), context.getLevel(), pos)
                ? defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER)
                : null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos above = pos.above();
        return level.getBlockState(above).isFaceSturdy(level, above, Direction.DOWN);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (direction == Direction.UP && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {
        int stage = state.getValue(STAGE);
        int animal = state.getValue(ANIMAL);

        //空挂钩：挂上动物
        if (stage == 0) {
            if (stack.is(deadCattle.get())) {
                return hangAnimal(level, pos, state, player, stack, 1, 1);
            }
            if (stack.is(deadSheep.get())) {
                return hangAnimal(level, pos, state, player, stack, 2, 1);
            }
            if (stack.is(deadPig.get())) {
                return hangAnimal(level, pos, state, player, stack, 3, 1);
            }
            if (stack.is(deadChicken.get())) {
                return hangAnimal(level, pos, state, player, stack, 4, 1);
            }
            if (stack.is(pluckedChicken.get())) {
                return hangAnimal(level, pos, state, player, stack, 4, 5);
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        //鸡特殊：stage2 右键取回
        if (animal == 4 && stage == 2) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(STAGE, 0).setValue(ANIMAL, 0), 3);
                ItemEntity entity = new ItemEntity(level,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        new ItemStack(bledChicken.get()));
                level.addFreshEntity(entity);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        //有动物：刀具屠宰
        if (stage >= 1 && stage <= 6) {
            Item tool = stack.getItem();

            // 鸡 stage5→6 (切割刀)
            if (animal == 4 && stage == 5 && tool == FlavorImmersedDaily.SHARPKNIFE.get()) {
                return processStage(level, pos, state, player, animal, 5);
            }
            // 鸡 stage6→0 (切割刀)
            if (animal == 4 && stage == 6 && tool == FlavorImmersedDaily.SHARPKNIFE.get()) {
                if (!level.isClientSide) {
                    spawnDrops(level, pos, animal, 6);
                    level.setBlock(pos, state.setValue(STAGE, 0).setValue(ANIMAL, 0), 3);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            //放血（鸡猪牛羊） stage1→2 (宽刃刀)
            if (stage == 1 && tool == FlavorImmersedDaily.WIDEEDGEDKNIFE.get()) {
                return processStage(level, pos, state, player, animal, 1);
            }
            //猪牛羊 stage2→3 (切割刀)
            if (stage == 2 && animal != 4 && tool == FlavorImmersedDaily.SHARPKNIFE.get()) {
                return processStage(level, pos, state, player, animal, 2);
            }
            //猪牛羊 stage3→4 (斩骨刀)
            if (stage == 3 && tool == FlavorImmersedDaily.BONECUTTERKNIFE.get()) {
                return processStage(level, pos, state, player, animal, 3);
            }
            //猪牛羊 stage4→5 (切割刀)
            if (stage == 4 && tool == FlavorImmersedDaily.SHARPKNIFE.get()) {
                return processStage(level, pos, state, player, animal, 4);
            }
            //猪牛羊 stage5→0 (切割刀)
            if (stage == 5 && animal != 4 && tool == FlavorImmersedDaily.SHARPKNIFE.get()) {
                if (!level.isClientSide) {
                    spawnDrops(level, pos, animal, 5);
                    level.setBlock(pos, state.setValue(STAGE, 0).setValue(ANIMAL, 0), 3);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    private ItemInteractionResult hangAnimal(Level level, BlockPos pos, BlockState state,
                                              Player player, ItemStack stack, int animalType, int startStage) {
        if (!level.isClientSide) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            level.setBlock(pos, state.setValue(STAGE, startStage).setValue(ANIMAL, animalType), 3);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    private ItemInteractionResult processStage(Level level, BlockPos pos, BlockState state,
                                                Player player, int animal, int stage) {
        if (!level.isClientSide) {
            spawnDrops(level, pos, animal, stage);
            level.setBlock(pos, state.setValue(STAGE, stage + 1), 3);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

//从config弄掉落物，玩家可以自行添加哦
    private void spawnDrops(Level level, BlockPos pos, int animal, int stage) {
        List<String> dropIds = Config.getDrops(animal, stage);
        for (String itemId : dropIds) {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemId));
            if (item != null) {
                ItemEntity entity = new ItemEntity(level,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        new ItemStack(item));
                level.addFreshEntity(entity);
            }
        }
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BighookBlockEntity(pos, state);
    }
}
