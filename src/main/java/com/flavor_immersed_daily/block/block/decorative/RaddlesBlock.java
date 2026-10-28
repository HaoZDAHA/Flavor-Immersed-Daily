package com.flavor_immersed_daily.block.block.decorative;

import com.flavor_immersed_daily.block.blockentity.RaddlesBlockEntity;
import com.flavor_immersed_daily.client.ClientGuiHelper;
import com.flavor_immersed_daily.screen.RaddlesOwnerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

// 灯谜牌 — 悬挂装饰方块，牌面可写四个竖排汉字
// 放置者右键：打开编辑界面（文字 / 谜面 / 16 格奖励）
// 其他玩家右键：打开答题界面，答对可领取奖励（每人一次）
// 需上方方块支撑，失去支撑自动掉落；文字渲染见 RaddlesRenderer
// 带水平朝向（FACING），放置时牌面朝向玩家
public class RaddlesBlock extends BaseEntityBlock {

// 水平朝向：牌面正对的方向
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

// 南北朝向（牌面薄边沿 Z 轴）
    private static final VoxelShape SHAPE_NS = Block.box(4, 0, 7, 12, 16, 9);
// 东西朝向（牌面薄边沿 X 轴）
    private static final VoxelShape SHAPE_EW = Block.box(7, 0, 4, 9, 16, 12);

    public RaddlesBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RaddlesBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING).getAxis() == Direction.Axis.Z ? SHAPE_NS : SHAPE_EW;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
// 牌面朝向玩家；只能贴在上方实心方块的底面悬挂，否则无法放置
        BlockState state = defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
        return canSurvive(state, context.getLevel(), context.getClickedPos())
                ? state
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
        if (direction == Direction.UP && !canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof RaddlesBlockEntity blockEntity)) {
            return InteractionResult.PASS;
        }

// 放置者：编辑界面（由服务端打开，方块坐标随界面一起发给客户端）
        if (blockEntity.canEdit(player)) {
            if (!level.isClientSide) {
                NetworkHooks.openScreen((ServerPlayer) player, new SimpleMenuProvider(
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
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
// 掉落牌子里存放的奖励物品，避免破坏时丢失
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof RaddlesBlockEntity blockEntity) {
            for (ItemStack stack : blockEntity.getItems()) {
                if (!stack.isEmpty()) {
                    Block.popResource(level, pos, stack);
                }
            }
            blockEntity.clearContent();
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}