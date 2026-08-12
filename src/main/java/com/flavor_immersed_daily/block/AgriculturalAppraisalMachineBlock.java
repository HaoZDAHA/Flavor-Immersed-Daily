package com.flavor_immersed_daily.block;

import com.flavor_immersed_daily.Config;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;
import java.util.Random;

//农产鉴定机 玩家手持野生采集物右键，消耗物品，根据配置战利品表生成掉落物
public class AgriculturalAppraisalMachineBlock extends HorizontalDirectionalBlock {

    public static final MapCodec<AgriculturalAppraisalMachineBlock> CODEC =
            simpleCodec(AgriculturalAppraisalMachineBlock::new);

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final Random RANDOM = new Random();

    public AgriculturalAppraisalMachineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                               BlockPos pos, Player player, InteractionHand hand,
                                               BlockHitResult hit) {
        if (level.isClientSide()) {
            return ItemInteractionResult.SUCCESS;
        }

        Item heldItem = stack.getItem();
        String heldId = BuiltInRegistries.ITEM.getKey(heldItem).toString();

        // 检查是否是野生采集物
        List<String> dropIds = Config.getWildDrops(heldId);
        if (dropIds == null || dropIds.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // 消耗一个野生采集物
        stack.shrink(1);

        // 从战利品表中随机挑选 1-3 次物品掉落
        int pickCount = RANDOM.nextInt(3) + 1;
        for (int i = 0; i < pickCount; i++) {
            String itemId = dropIds.get(RANDOM.nextInt(dropIds.size()));
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemId));
            if (item != null) {
                ItemEntity drop = new ItemEntity(level,
                        pos.getX() + 0.5,
                        pos.getY() + 1.0,
                        pos.getZ() + 0.5,
                        new ItemStack(item));
                drop.setDeltaMovement(
                        (RANDOM.nextDouble() - 0.5) * 0.2,
                        0.2,
                        (RANDOM.nextDouble() - 0.5) * 0.2);
                level.addFreshEntity(drop);
            } else {
                FlavorImmersedDaily.LOGGER.warn("AgriculturalAppraisalMachine: unknown item '{}' in wild drops config", itemId);
            }
        }

        // 音效
        level.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 0.8f, 1.0f);

        return ItemInteractionResult.SUCCESS;
    }
}
