package com.flavor_immersed_daily.block.block.decorative;

import com.flavor_immersed_daily.block.blockentity.RaddlesBlockEntity;
import com.flavor_immersed_daily.client.ClientGuiHelper;
import com.flavor_immersed_daily.screen.RaddlesOwnerMenu;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

// 灯谜牌 — 悬挂装饰方块，牌面可写四个竖排汉字
// 放置者右键：打开编辑界面（文字 / 谜面 / 16 格奖励）
// 其他玩家右键：打开答题界面，答对可领取奖励（每人一次）
// 需上方方块支撑，失去支撑自动掉落；文字渲染见 RaddlesRenderer
public class RaddlesBlock extends BaseEntityBlock {

    public static final MapCodec<RaddlesBlock> CODEC = simpleCodec(RaddlesBlock::new);

// 牌面沿 Z 轴厚度（FACING 为南北时）
    private static final VoxelShape SHAPE = Block.box(4, 0, 7, 12, 16, 9);
// 牌面沿 X 轴厚度（FACING 为东西时，绕 Y 旋转 90° 后的碰撞箱）
    private static final VoxelShape SHAPE_EAST_WEST = Block.box(7, 0, 4, 9, 16, 12);

    public RaddlesBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HorizontalDirectionalBlock.FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(HorizontalDirectionalBlock.FACING).getAxis() == Direction.Axis.Z
                ? SHAPE : SHAPE_EAST_WEST;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RaddlesBlockEntity(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
// 只能贴在上方实心方块的底面悬挂，否则无法放置；朝向随玩家水平方向
        return canSurvive(defaultBlockState(), context.getLevel(), context.getClickedPos())
                ? defaultBlockState().setValue(HorizontalDirectionalBlock.FACING,
                        context.getHorizontalDirection().getOpposite())
                : null;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos above = pos.above();
        return level.getBlockState(above).isFaceSturdy(level, above, Direction.DOWN);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP && !canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(HorizontalDirectionalBlock.FACING,
                rot.rotate(state.getValue(HorizontalDirectionalBlock.FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(HorizontalDirectionalBlock.FACING)));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
// 记录放置者，只有放置者可以编辑
        if (!level.isClientSide && placer instanceof Player player
                && level.getBlockEntity(pos) instanceof RaddlesBlockEntity blockEntity) {
            blockEntity.setOwner(player.getUUID(), player.getName().getString());
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof RaddlesBlockEntity blockEntity)) {
            return InteractionResult.PASS;
        }

// 放置者：编辑界面（由服务端打开，方块坐标随界面一起发给客户端）
        if (blockEntity.canEdit(player)) {
            if (!level.isClientSide) {
                player.openMenu(new SimpleMenuProvider(
                        (containerId, playerInventory, p) -> new RaddlesOwnerMenu(containerId, playerInventory, blockEntity),
                        Component.translatable("gui.flavor_immersed_daily.raddles.title")), pos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

// 其他玩家：答题界面（纯客户端界面，答案由服务端校验并发放奖励）
        if (level.isClientSide) {
            if (blockEntity.hasRiddle()) {
                ClientGuiHelper.openRaddlesAnswer(blockEntity);
            } else {
                player.displayClientMessage(
                        Component.translatable("gui.flavor_immersed_daily.raddles.no_riddle.chat"), true);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
// 掉落牌子里存放的奖励物品，避免破坏时丢失
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof RaddlesBlockEntity blockEntity) {
            for (ItemStack stack : blockEntity.getItems()) {
                if (!stack.isEmpty()) {
                    Block.popResource(level, pos, stack);
                }
            }
            blockEntity.clearContent();
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
