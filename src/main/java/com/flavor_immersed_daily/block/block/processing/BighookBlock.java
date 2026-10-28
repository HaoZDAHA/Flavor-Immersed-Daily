package com.flavor_immersed_daily.block.block.processing;

import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.all.ModBlockEntities;
import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.block.blockentity.BighookBlockEntity;
import com.flavor_immersed_daily.block.blockentity.WoodBasinBlockEntity;
import com.flavor_immersed_daily.butchering.ButcheringDefinition;
import com.flavor_immersed_daily.butchering.ButcheringRegistry;
import com.flavor_immersed_daily.gameplay.BighookCarryHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;

import java.util.List;
import java.util.function.Supplier;

// 大挂钩方块 — 悬挂动物胴体进行屠宰加工
// 动物: 0=none, 1=cattle, 2=sheep, 3=pig, 4=chicken
// 屠宰的阶段: 0(空) → 1(挂上) → 2 → 3 → 4 → 5 → 回收回0

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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
// 登记"该玩家手拿的生物可放到这个挂钩坐标"，供 onPlace 转移使用（不依赖放置后手上是否还有挂钩）
        BighookCarryHandler.markPendingHang(context.getPlayer(), pos);
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);

        if (!(level.getBlockEntity(pos) instanceof BighookBlockEntity be)) {
            return InteractionResult.PASS;
        }

// 挂钩上挂着被抓取的生物：其他玩家（或任意玩家）右键即可放下
        if (be.hasCarried()) {
            if (!level.isClientSide) {
                be.releaseCarried();
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

// 数据包自定义屠宰进行中：按定义推进（与内置 4 种动物互不干扰）
        if (be.hasCustomButchering()) {
            return handleCustomButchering(level, pos, be, stack);
        }

        int stage = state.getValue(STAGE);
        int animal = state.getValue(ANIMAL);

// 空挂钩：挂上动物
        if (stage == 0) {
// 数据包自定义定义优先（其他模组 / 数据包的接入点）
            ButcheringDefinition custom = ButcheringRegistry.getByInput(stack.getItem());
            if (custom != null) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    be.startCustomButchering(custom);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
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
            return InteractionResult.PASS;
        }

// 鸡特殊：stage2 右键取回放血后的鸡
        if (animal == 4 && stage == 2) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(STAGE, 0).setValue(ANIMAL, 0), 3);
                ItemEntity entity = new ItemEntity(level,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        new ItemStack(bledChicken.get()));
                level.addFreshEntity(entity);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

// 有动物：刀具屠宰
        if (stage >= 1 && stage <= 6) {
            Item tool = stack.getItem();

// 鸡 stage5→6 (切割刀)
            if (animal == 4 && stage == 5 && tool == ModItems.SHARPKNIFE.get()) {
                return processStage(level, pos, state, player, animal, 5);
            }
// 鸡 stage6→0 (切割刀)
            if (animal == 4 && stage == 6 && tool == ModItems.SHARPKNIFE.get()) {
                if (!level.isClientSide) {
                    spawnDrops(level, pos, animal, 6);
                    level.setBlock(pos, state.setValue(STAGE, 0).setValue(ANIMAL, 0), 3);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

// 放血（鸡猪牛羊） stage1→2 (宽刃刀)
            if (stage == 1 && tool == ModItems.WIDEEDGEDKNIFE.get()) {
                return processStage(level, pos, state, player, animal, 1);
            }
// 猪牛羊 stage2→3 (切割刀)
            if (stage == 2 && animal != 4 && tool == ModItems.SHARPKNIFE.get()) {
                return processStage(level, pos, state, player, animal, 2);
            }
// 猪牛羊 stage3→4 (斩骨刀)
            if (stage == 3 && tool == ModItems.BONECUTTERKNIFE.get()) {
                return processStage(level, pos, state, player, animal, 3);
            }
// 猪牛羊 stage4→5 (切割刀)
            if (stage == 4 && tool == ModItems.SHARPKNIFE.get()) {
                return processStage(level, pos, state, player, animal, 4);
            }
// 猪牛羊 stage5→0 (切割刀)
            if (stage == 5 && animal != 4 && tool == ModItems.SHARPKNIFE.get()) {
                if (!level.isClientSide) {
                    spawnDrops(level, pos, animal, 5);
                    level.setBlock(pos, state.setValue(STAGE, 0).setValue(ANIMAL, 0), 3);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }

    private InteractionResult hangAnimal(Level level, BlockPos pos, BlockState state,
                                              Player player, ItemStack stack, int animalType, int startStage) {
        if (!level.isClientSide) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            level.setBlock(pos, state.setValue(STAGE, startStage).setValue(ANIMAL, animalType), 3);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private InteractionResult processStage(Level level, BlockPos pos, BlockState state,
                                                Player player, int animal, int stage) {
        if (!level.isClientSide) {
            spawnDrops(level, pos, animal, stage);
            level.setBlock(pos, state.setValue(STAGE, stage + 1), 3);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

// 从config弄掉落物，玩家可以自行添加哦
// 若挂钩下方有木盆，则产物直接存入木盆；否则在挂钩处生成掉落物
    private void spawnDrops(Level level, BlockPos pos, int animal, int stage) {
        List<String> dropIds = Config.getDrops(animal, stage);
        WoodBasinBlockEntity basin = findBasinBelow(level, pos);
        if (basin != null) {
            basin.addDrops(dropIds);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, 0.8f);
            return;
        }
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

// 查找挂钩下方的木盆（向下最多4格，遇到非空气非木盆的方块即停止搜索）。
    private WoodBasinBlockEntity findBasinBelow(Level level, BlockPos pos) {
        for (int dy = 1; dy <= 4; dy++) {
            BlockPos p = pos.below(dy);
            BlockState s = level.getBlockState(p);
            if (s.is(Blocks.AIR)) {
                continue;
            }
            if (level.getBlockEntity(p) instanceof WoodBasinBlockEntity basin) {
                return basin;
            }
            break;
        }
        return null;
    }

// 数据包自定义屠宰：手持当前阶段所需刀具右键，产出掉落并推进阶段。
    private InteractionResult handleCustomButchering(Level level, BlockPos pos,
                                                     BighookBlockEntity be, ItemStack stack) {
        ButcheringDefinition definition = ButcheringRegistry.get(be.getCustomButcheringId());
        int index = be.getCustomStage() - 1;
        if (definition == null || index < 0 || index >= definition.stages().size()) {
// 定义在数据包重载后消失，或阶段越界：归还显示物并清空，避免卡住
            if (!level.isClientSide) {
                dropCustomDisplay(level, pos, be);
                be.clearCustomButchering();
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        ButcheringDefinition.Stage current = definition.stages().get(index);
        if (!current.matches(stack)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            spawnDropItems(level, pos, current.drops());
            be.advanceCustomButchering(definition);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

// 按物品列表产出掉落；若挂钩下方有木盆则直接存入木盆。
    private void spawnDropItems(Level level, BlockPos pos, List<Item> items) {
        if (items.isEmpty()) {
            return;
        }
        WoodBasinBlockEntity basin = findBasinBelow(level, pos);
        if (basin != null) {
            basin.addDrops(items.stream()
                    .map(item -> BuiltInRegistries.ITEM.getKey(item).toString())
                    .toList());
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, 0.8f);
            return;
        }
        for (Item item : items) {
            level.addFreshEntity(new ItemEntity(level,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    new ItemStack(item)));
        }
    }

// 归还当前挂在挂钩上的自定义显示物（拆除挂钩或定义失效时调用）。
    private void dropCustomDisplay(Level level, BlockPos pos, BighookBlockEntity be) {
        ItemStack display = be.getCustomDisplay();
        if (!display.isEmpty()) {
            level.addFreshEntity(new ItemEntity(level,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    display.copy()));
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BighookBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        if (type != ModBlockEntities.BIGHOOK_BE.get()) return null;
        return (l, p, s, be) -> BighookBlockEntity.tick(l, p, s, (BighookBlockEntity) be);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
// 玩家拿着被拿起的生物放置挂钩时，把该生物转移到挂钩上挂住
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            BighookCarryHandler.tryHangOnPlace(serverLevel, pos);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
// 挂钩被拆除时释放挂在上面的被抓取生物，避免实体被永久锁在原地
        if (!state.is(newState.getBlock()) && !isMoving
                && level.getBlockEntity(pos) instanceof BighookBlockEntity be) {
// 拆除时归还自定义屠宰的当前显示物，避免物品丢失
            if (be.hasCustomButchering() && !level.isClientSide) {
                dropCustomDisplay(level, pos, be);
                be.clearCustomButchering();
            }
            be.releaseCarried();
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}