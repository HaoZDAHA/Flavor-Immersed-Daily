package com.flavor_immersed_daily.gameplay;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.block.block.fruit.FallingFruitBlock;
import com.flavor_immersed_daily.block.block.fruit.FruitingLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class FruitHarvestHandler {
    private static final int SEARCH_RADIUS = 7;
    private static final int MAX_SEARCHED_BLOCKS = 512;
    private static final float EXHAUSTION_COST = 6.0F;

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (!event.getItemStack().is(Items.STICK)) return;

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof RotatedPillarBlock) || player.getFoodData().getFoodLevel() <= 0) return;

        if (level.isClientSide) {
            player.swing(event.getHand());
            return;
        }

        ServerLevel serverLevel = (ServerLevel) level;
        boolean harvested = harvestConnectedFruit(serverLevel, pos, state.getBlock());
        if (harvested) {
            player.getFoodData().addExhaustion(EXHAUSTION_COST);
            serverLevel.playSound(null, pos, SoundEvents.PLAYER_ATTACK_SWEEP,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    1, 0.0, 0.0, 0.0, 0.0);
            event.setCanceled(true);
        }
    }

    private static boolean harvestConnectedFruit(ServerLevel level, BlockPos origin, Block trunkBlock) {
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        List<BlockPos> fruitPositions = new ArrayList<>();
        queue.add(origin);

        while (!queue.isEmpty() && visited.size() < MAX_SEARCHED_BLOCKS) {
            BlockPos currentPos = queue.removeFirst();
            if (!visited.add(currentPos) || !isWithinSearchRadius(origin, currentPos)) continue;

            BlockState currentState = level.getBlockState(currentPos);
            Block currentBlock = currentState.getBlock();
            boolean traversable = currentBlock == trunkBlock
                    || currentBlock instanceof LeavesBlock
                    || currentBlock instanceof FallingFruitBlock;
            if (!traversable) continue;

            if (currentBlock instanceof FruitingLeavesBlock || currentBlock instanceof FallingFruitBlock) {
                fruitPositions.add(currentPos);
            }

            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = currentPos.relative(direction);
                if (!visited.contains(neighborPos) && isWithinSearchRadius(origin, neighborPos)) {
                    queue.addLast(neighborPos);
                }
            }
        }

        boolean harvested = false;
        for (BlockPos fruitPos : fruitPositions) {
            BlockState fruitState = level.getBlockState(fruitPos);
            if (fruitState.getBlock() instanceof FruitingLeavesBlock leavesBlock) {
                harvested |= leavesBlock.harvestFruit(level, fruitPos);
            } else if (fruitState.getBlock() instanceof FallingFruitBlock fallingBlock) {
                harvested |= fallingBlock.harvestFruit(level, fruitPos);
            }
        }
        return harvested;
    }

    private static boolean isWithinSearchRadius(BlockPos origin, BlockPos pos) {
        return Math.abs(pos.getX() - origin.getX()) <= SEARCH_RADIUS
                && Math.abs(pos.getY() - origin.getY()) <= SEARCH_RADIUS
                && Math.abs(pos.getZ() - origin.getZ()) <= SEARCH_RADIUS;
    }

    public static void tryDropVariantFruit(Level level, BlockPos pos, Block block) {
        RandomSource random = level.getRandom();
        float chance = 0.15F;

        if (block == ModBlocks.ORANGELEAVE_FRUITING_LEAVES.get()) {
            if (random.nextFloat() < chance) {
                Block.popResource(level, pos, new ItemStack(ModItems.BLOODORANGE.get()));
            }
        } else if (block == ModBlocks.TANGERINELEAVE_FRUITING_LEAVES.get()) {
            if (random.nextFloat() < chance) {
                Item variant = random.nextBoolean() ? ModItems.UGLYORANGE.get() : ModItems.TANGERINE_1.get();
                Block.popResource(level, pos, new ItemStack(variant));
            }
        } else if (block == ModBlocks.SWEETMELONLEAVE_FRUITING_LEAVES.get()) {
            if (random.nextFloat() < chance) {
                Block.popResource(level, pos, new ItemStack(ModItems.SWEETMELON_1.get()));
            }
        } else if (block == ModBlocks.APPLELEAVE_FRUITING_LEAVES.get()) {
            if (random.nextFloat() < chance) {
                Block.popResource(level, pos, new ItemStack(ModItems.GREENAPPLE.get()));
            }
        } else if (block == ModBlocks.HONEYPEACHLEAVE_FRUITING_LEAVES.get()
                && random.nextFloat() < chance) {
            Block.popResource(level, pos, new ItemStack(ModItems.LIFEPEACH.get()));
        }
    }
}
