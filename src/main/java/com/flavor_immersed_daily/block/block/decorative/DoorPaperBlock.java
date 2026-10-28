package com.flavor_immersed_daily.block.block.decorative;

import com.flavor_immersed_daily.all.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

// 门纸 — 可贴挂在墙上的装饰方块，类似原版画的放置方式
// 手持食物右键：消耗一个食物，获得"食物饱食度 × 10 秒"的 buff
// 左门纸 → 咆哮（yi_de_roar），右门纸 → 武圣（guan_yu_strike）
public class DoorPaperBlock extends HorizontalDirectionalBlock {

    private final boolean isLeft;

// 不同朝向的碰撞箱厚度（薄片，贴墙）— 匹配模型 from[0,0,15.99] to[16,24,15.99]
    private static final VoxelShape SHAPE_NORTH = Block.box(0, 0, 15, 16, 16, 16);
    private static final VoxelShape SHAPE_SOUTH = Block.box(0, 0, 0, 16, 16, 1);
    private static final VoxelShape SHAPE_EAST = Block.box(0, 0, 0, 1, 16, 16);
    private static final VoxelShape SHAPE_WEST = Block.box(15, 0, 0, 16, 16, 16);

    public DoorPaperBlock(Properties properties, boolean isLeft) {
        super(properties);
        this.isLeft = isLeft;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();
// 点击水平面时，纸贴在被点击的面上
        if (clickedFace.getAxis().isHorizontal()) {
            return this.defaultBlockState().setValue(FACING, clickedFace);
        }
// 点击顶部或底部时，按玩家朝向
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SHAPE_SOUTH;
            case EAST -> SHAPE_EAST;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                  Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack held = player.getItemInHand(hand);
        FoodProperties food = held.getFoodProperties(player);
        if (food == null) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            held.shrink(1);
            net.minecraft.world.effect.MobEffect effect = isLeft
                    ? ModEffects.YI_DE_ROAR.get()
                    : ModEffects.GUAN_YU_STRIKE.get();
// 饱食度 × 10 秒
            player.addEffect(new MobEffectInstance(effect, food.getNutrition() * 10 * 20));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}