package com.fidtest.block;

import com.fidtest.block.entity.TablewareBlockEntity;
import com.fidtest.block.entity.TablewareBlockEntity.*;
import com.fidtest.item.ChopsticksItem;
import com.fidtest.item.DishFragments;
import com.fidtest.item.DishItem;
import com.fidtest.item.TablewareItem;
import com.fidtest.network.ChopsticksEatPayload;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.PacketDistributor;

public class TablewareBlock extends BaseEntityBlock {
    public static final MapCodec<TablewareBlock> CODEC = simpleCodec(TablewareBlock::new);
    private static final TagKey<Item> SEASONING = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("fid", "seasoning"));

    public TablewareBlock(Properties properties) { super(properties); }
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }
    @Override
    protected RenderShape getRenderShape(BlockState state) { return RenderShape.ENTITYBLOCK_ANIMATED; }
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new TablewareBlockEntity(pos, state); }
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return level.getBlockEntity(pos) instanceof TablewareBlockEntity table ? table.shape() : Shapes.empty();
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof TablewareItem) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!canInteract(level, pos, player)) return ItemInteractionResult.FAIL;
        if (!(level.getBlockEntity(pos) instanceof TablewareBlockEntity table)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        Part part = table.hitPart(hit.getLocation().x - pos.getX(), hit.getLocation().z - pos.getZ());
        if (part == null) return ItemInteractionResult.FAIL;
        if (stack.getItem() instanceof ChopsticksItem) {
            if (level.isClientSide) return ItemInteractionResult.CONSUME;
            if (!eat(table, part, player, hand, hit)) return ItemInteractionResult.FAIL;
            return ItemInteractionResult.CONSUME;
        }
        if (part.kind == Kind.SAUCER) {
            if (stack.is(SEASONING)) {
                if (!part.foods.isEmpty() && (!ItemStack.isSameItemSameComponents(part.foods.getFirst().stack, stack)
                        || part.foods.getFirst().stack.getCount() >= 64)) return ItemInteractionResult.FAIL;
                if (!level.isClientSide) {
                    if (part.foods.isEmpty()) part.foods.add(new Food(stack.copyWithCount(1), part.x, part.z));
                    else part.foods.getFirst().stack.grow(1);
                    if (!player.isCreative()) stack.shrink(1);
                    table.setChanged();
                }
                return ItemInteractionResult.SUCCESS;
            }
            if (!stack.has(DataComponents.FOOD) || part.foods.isEmpty() || !DishItem.getSeasoningEffect(stack).isEmpty()) return ItemInteractionResult.FAIL;
            if (!level.isClientSide) {
                ItemStack seasoned = stack.copyWithCount(1);
                ItemStack seasoning = part.foods.getFirst().stack;
                DishItem.setSeasoningEffect(seasoned, seasoning.getItem().builtInRegistryHolder().key().location().toString());
                if (!player.isCreative() && stack.getCount() == 1) {
                    player.setItemInHand(hand, seasoned);
                } else {
                    if (!player.isCreative()) stack.shrink(1);
                    give(player, seasoned);
                }
                seasoning.shrink(1);
                if (seasoning.isEmpty()) part.foods.clear();
                table.setChanged();
            }
            return ItemInteractionResult.SUCCESS;
        }
        if (!stack.has(DataComponents.FOOD) || part.foods.size() >= (part.kind == Kind.PLATE ? 16 : 8)) return ItemInteractionResult.FAIL;
        if (!level.isClientSide) {
            double x = Math.clamp(hit.getLocation().x - pos.getX(), part.bounds().minX + 0.0625, part.bounds().maxX - 0.0625);
            double z = Math.clamp(hit.getLocation().z - pos.getZ(), part.bounds().minZ + 0.0625, part.bounds().maxZ - 0.0625);
            if (part.kind == Kind.LONG_PLATE) {
                int slot = 0;
                for (; slot < 8; slot++) {
                    double offset = (slot - 3.5) * 0.105;
                    double sx = part.x + part.facing.getStepX() * offset;
                    double sz = part.z + part.facing.getStepZ() * offset;
                    if (part.foods.stream().noneMatch(f -> Math.abs(f.x - sx) < 0.001 && Math.abs(f.z - sz) < 0.001)) {
                        x = sx;
                        z = sz;
                        break;
                    }
                }
            }
            part.foods.add(new Food(stack.copyWithCount(1), x, z));
            if (!player.isCreative()) stack.shrink(1);
            table.setChanged();
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!player.getMainHandItem().isEmpty()) return InteractionResult.PASS;
        if (!canInteract(level, pos, player)) return InteractionResult.FAIL;
        if (!(level.getBlockEntity(pos) instanceof TablewareBlockEntity table)) return InteractionResult.PASS;
        Part part = table.hitPart(hit.getLocation().x - pos.getX(), hit.getLocation().z - pos.getZ());
        if (part == null) return InteractionResult.PASS;
        if (!level.isClientSide) {
            if (!part.foods.isEmpty()) give(player, part.foods.remove(foodIndex(part, hit, pos)).stack);
            else {
                table.getParts().remove(part);
                give(player, part.item());
            }
            table.setChanged();
            if (table.getParts().isEmpty()) level.removeBlock(pos, false);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static int foodIndex(Part part, BlockHitResult hit, BlockPos pos) {
        int best = -1;
        double distance = Double.MAX_VALUE;
        for (int i = part.foods.size() - 1; i >= 0; i--) {
            Food food = part.foods.get(i);
            double x = part.kind == Kind.LONG_PLATE ? part.x + (food.x - part.x) * .65 : food.x;
            double z = part.kind == Kind.LONG_PLATE ? part.z + (food.z - part.z) * .65 : food.z;
            double dx = hit.getLocation().x - pos.getX() - x;
            double dz = hit.getLocation().z - pos.getZ() - z;
            double d = dx * dx + dz * dz;
            if (d < distance) { best = i; distance = d; }
        }
        return best;
    }

    private static boolean eat(TablewareBlockEntity table, Part part, Player player, InteractionHand hand, BlockHitResult hit) {
        if (part.foods.isEmpty() || part.kind == Kind.SAUCER || table.getLevel().isClientSide) return false;
        int index = foodIndex(part, hit, table.getBlockPos());
        if (index < 0 || index >= part.foods.size()) return false;
        Food food = part.foods.get(index);
        var properties = food.stack.get(DataComponents.FOOD);
        if (properties == null || !player.canEat(properties.canAlwaysEat())) return false;
        ItemStack serving = food.stack.copyWithCount(1);
        ItemStack preCopy = serving.copy();
        boolean isDish = preCopy.getItem() instanceof DishItem;
        CompoundTag bite = isDish ? DishFragments.snapshotBite(preCopy) : new CompoundTag();
        ItemStack biteFood = isDish ? ItemStack.EMPTY : preCopy.copyWithCount(1);
        ItemStack result = serving.finishUsingItem(table.getLevel(), player);
        result = EventHooks.onItemUseFinish(player, preCopy, 0, result);
        if (preCopy.getItem() instanceof DishItem && result.getItem() instanceof DishItem && !result.isEmpty()) {
            food.stack = result.copyWithCount(1);
        } else {
            part.foods.remove(index);
            // Creative consumption can return the unchanged food, not a container.
            if (!result.is(preCopy.getItem())) give(player, result);
        }
        table.setChanged();
        if (hand == InteractionHand.MAIN_HAND) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new ChopsticksEatPayload(player.getInventory().selected, bite, biteFood));
        }
        return true;
    }

    public static boolean canInteract(Level level, BlockPos pos, Player player) {
        return player.getAbilities().mayBuild && level.mayInteract(player, pos) && level.getWorldBorder().isWithinBounds(pos);
    }

    public static void give(Player player, ItemStack stack) {
        if (!stack.isEmpty() && !player.getInventory().add(stack)) player.drop(stack, false);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()
                && level.getBlockEntity(pos) instanceof TablewareBlockEntity table) {
            table.getParts().clear();
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (!state.is(newState.getBlock()) && !level.isClientSide && level.getBlockEntity(pos) instanceof TablewareBlockEntity table) {
            for (Part part : table.getParts()) {
                popResource(level, pos, part.item());
                for (Food food : part.foods) popResource(level, pos, food.stack);
            }
            table.getParts().clear();
        }
        super.onRemove(state, level, pos, newState, moving);
    }
}
