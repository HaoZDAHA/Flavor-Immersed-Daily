package com.flavor_immersed_daily.block.block.food;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

// 可乐罐堆 — 4 个状态（stage 0~3，对应 1~4 罐）。
// 手持本可乐物品右键：升级（消耗 1 罐，最多 4 罐）；
// 手持其他物品或空手右键：降级（拿走 1 罐，最后 1 罐被拿走时方块移除）。
public class ColaBlock extends Block {
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 3);
    private static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 12.0D, 12.0D);

    private final String itemId;

    public ColaBlock(String itemId, Properties properties) {
        super(properties.sound(SoundType.METAL));
        this.itemId = itemId;
        registerDefaultState(stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    private Item colaItem() {
        return BuiltInRegistries.ITEM.get(new ResourceLocation(FlavorImmersedDaily.MODID, itemId));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
// 手持本可乐：升级（消耗 1 罐，最多 4 罐）
        if (stack.is(colaItem())) {
            if (!level.isClientSide) {
                int stage = state.getValue(STAGE);
                if (stage < 3) {
                    level.setBlock(pos, state.setValue(STAGE, stage + 1), 3);
                    level.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    stack.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
// 手持的不是本可乐（含空手）：降级拿走一罐
        if (!level.isClientSide) {
            downgrade(state, level, pos, player);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

// 降级：少一罐并给玩家 1 个可乐；最后 1 罐被拿走时方块移除。
    private void downgrade(BlockState state, Level level, BlockPos pos, Player player) {
        int stage = state.getValue(STAGE);
        if (stage > 0) {
            level.setBlock(pos, state.setValue(STAGE, stage - 1), 3);
        } else {
            level.removeBlock(pos, false);
        }
        level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F);
        ItemStack stack = new ItemStack(colaItem());
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }
}
