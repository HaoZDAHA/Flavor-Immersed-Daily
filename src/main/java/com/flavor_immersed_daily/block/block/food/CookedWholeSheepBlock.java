package com.flavor_immersed_daily.block.block.food;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.block.blockentity.CookedWholeSheepBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

// 烤全羊 — 2x2x2 连体方块，可食用的放置方块。
// STAGE 0~9 共 10 个阶段，放置后羊骨架绕烤叉持续旋转；
// 每次右键食用进入下一个阶段，最后一个阶段再右键即整个消失。
// ORIGIN 记录该方块在 2x2x2 区域中的位置（bit0=X, bit1=Y, bit2=Z），0 为主方块。
public class CookedWholeSheepBlock extends Block implements EntityBlock {

    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 9);
    public static final IntegerProperty ORIGIN = IntegerProperty.create("origin", 0, 7);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape[] REGION_SHAPES = new VoxelShape[8];

    public CookedWholeSheepBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(STAGE, 0)
                .setValue(ORIGIN, 0)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE, ORIGIN, FACING);
    }

// 每个 part 的碰撞箱覆盖整个 2x2x2 区域（相对自身位置偏移）
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getRegionShape(state.getValue(ORIGIN));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getRegionShape(state.getValue(ORIGIN));
    }

    private static VoxelShape getRegionShape(int origin) {
        VoxelShape cached = REGION_SHAPES[origin];
        if (cached == null) {
            int dx = origin & 1;
            int dy = (origin >> 1) & 1;
            int dz = (origin >> 2) & 1;
            cached = Block.box(-dx * 16.0, -dy * 16.0, -dz * 16.0,
                    (2 - dx) * 16.0, (2 - dy) * 16.0, (2 - dz) * 16.0);
            REGION_SHAPES[origin] = cached;
        }
        return cached;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
// 2x2x2 区域向 +X +Y +Z 延伸，检查 8 格都可放置
        for (int i = 0; i < 8; i++) {
            BlockPos p = pos.offset(i & 1, (i >> 1) & 1, (i >> 2) & 1);
            if (!context.getLevel().getBlockState(p).canBeReplaced(context)) {
                return null;
            }
        }
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (level.isClientSide) return;
// 填充其余 7 格为连体 part
        for (int i = 1; i < 8; i++) {
            BlockPos p = pos.offset(i & 1, (i >> 1) & 1, (i >> 2) & 1);
            level.setBlock(p, state.setValue(ORIGIN, i), 3);
        }
    }

// 任意一格被破坏时，连锁破坏其余 7 格
    private static boolean destroying = false;

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && !state.is(newState.getBlock()) && !destroying) {
            destroying = true;
            try {
                int origin = state.getValue(ORIGIN);
                int ox = origin & 1;
                int oy = (origin >> 1) & 1;
                int oz = (origin >> 2) & 1;
                for (int i = 0; i < 8; i++) {
                    if (i == origin) continue;
                    BlockPos p = pos.offset((i & 1) - ox, ((i >> 1) & 1) - oy, ((i >> 2) & 1) - oz);
                    if (level.getBlockState(p).getBlock() == this) {
                        level.destroyBlock(p, false);
                    }
                }
            } finally {
                destroying = false;
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hitResult) {
// 定位主方块
        int origin = state.getValue(ORIGIN);
        BlockPos originPos = pos.offset(-(origin & 1), -((origin >> 1) & 1), -((origin >> 2) & 1));
        BlockState originState = level.getBlockState(originPos);
        if (originState.getBlock() != this) {
            return InteractionResult.FAIL;
        }

        int stage = originState.getValue(STAGE);
        if (stage < 9) {
            if (!level.isClientSide) {
// 吃一口，进入下一阶段
                level.setBlock(originPos, originState.setValue(STAGE, stage + 1), 3);
                level.playSound(null, originPos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 1.0F);
                player.eat(level, new ItemStack(foodItem()).copyWithCount(1));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

// 最后一口：整个消失（不掉落）
        if (!level.isClientSide) {
            level.playSound(null, originPos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 1.0F);
            level.destroyBlock(originPos, false);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CookedWholeSheepBlockEntity(pos, state);
    }

// 手持物品（带食物属性），用于 player.eat 结算营养
    private Item foodItem() {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "cooked_whole_sheep");
        return BuiltInRegistries.ITEM.getOptional(id).orElse(Items.AIR);
    }
}
