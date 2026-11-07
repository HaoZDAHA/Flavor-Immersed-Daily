package com.fidtest.block;

import com.fidtest.block.entity.PlatedDishBlockEntity;
import com.fidtest.item.ChopsticksItem;
import com.fidtest.item.DishFragments;
import com.fidtest.item.DishItem;
import com.fidtest.network.ChopsticksEatPayload;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class PlatedDishBlock extends BaseEntityBlock {
    public static final MapCodec<PlatedDishBlock> CODEC = simpleCodec(PlatedDishBlock::new);
    private static final VoxelShape SHAPE = box(2, 0, 2, 14, 3, 14);

    public PlatedDishBlock(Properties properties) { super(properties); }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    @Override
    protected RenderShape getRenderShape(BlockState state) { return RenderShape.ENTITYBLOCK_ANIMATED; }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlatedDishBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        eat(level, pos, player, hand);
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        return eat(level, pos, player, InteractionHand.MAIN_HAND);
    }

    private InteractionResult eat(Level level, BlockPos pos, Player player, InteractionHand hand) {
        if (!level.mayInteract(player, pos) || !player.getAbilities().mayBuild) return InteractionResult.FAIL;
        if (!(level.getBlockEntity(pos) instanceof PlatedDishBlockEntity plate)) return InteractionResult.PASS;
        ItemStack dish = plate.getDish();
        var food = dish.get(DataComponents.FOOD);
        if (dish.isEmpty() || food == null || !player.canEat(food.canAlwaysEat())) return InteractionResult.CONSUME;
        if (!level.isClientSide) {
            boolean notifyChopsticks = hand == InteractionHand.MAIN_HAND
                    && player.getMainHandItem().getItem() instanceof ChopsticksItem;
            CompoundTag bite = notifyChopsticks ? DishFragments.snapshotBite(dish) : null;
            boolean finished = DishItem.eatBite(dish, level, player);
            if (notifyChopsticks) {
                PacketDistributor.sendToPlayer((ServerPlayer) player,
                        new ChopsticksEatPayload(player.getInventory().selected, bite, ItemStack.EMPTY));
            }
            if (finished) {
                level.removeBlock(pos, false);
            } else {
                plate.setChanged();
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof PlatedDishBlockEntity plate) {
            ItemStack dish = plate.getDish();
            if (!dish.isEmpty() && DishFragments.data(dish).getInt("Bites") == 0) {
                return List.of(dish.copy());
            }
        }
        return List.of();
    }
}
