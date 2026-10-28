package com.flavor_immersed_daily.gameplay;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.block.block.fruit.FruitingLeavesBlock;
import com.flavor_immersed_daily.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import java.util.HashSet;
import java.util.Set;

// 果树催熟：对树干（任意原木方块）使用骨粉，会按骨粉催熟概率随机让
// 整棵与树干相连的挂果树叶提升一级成熟度，替代原来直接对树叶使用骨粉。
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class FruitTreeRipeningHandler {
    private static final int SEARCH_RADIUS = 7;
    private static final int MAX_SEARCHED_BLOCKS = 512;

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getItemStack().is(Items.BONE_MEAL)) return;

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof RotatedPillarBlock)) return;

        if (level.isClientSide) {
            event.getEntity().swing(event.getHand());
            return;
        }

        ServerLevel serverLevel = (ServerLevel) level;
        if (ripenConnectedFruit(serverLevel, pos, state.getBlock())) {
            event.getItemStack().consume(1, event.getEntity());
            serverLevel.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    15, 0.5, 0.5, 0.5, 0.0);
            event.setCanceled(true);
        }
    }

    private static boolean ripenConnectedFruit(ServerLevel level, BlockPos origin, Block trunkBlock) {
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        queue.add(origin);
        boolean ripened = false;

        while (!queue.isEmpty() && visited.size() < MAX_SEARCHED_BLOCKS) {
            BlockPos currentPos = queue.removeFirst();
            if (!visited.add(currentPos) || !isWithinSearchRadius(origin, currentPos)) continue;

            BlockState currentState = level.getBlockState(currentPos);
            Block currentBlock = currentState.getBlock();
            boolean traversable = currentBlock == trunkBlock || currentBlock instanceof LeavesBlock;
            if (!traversable) continue;

            if (currentBlock instanceof FruitingLeavesBlock leavesBlock
                    && !currentState.getValue(FruitingLeavesBlock.FRUITING)) {
                ripened |= leavesBlock.tryMatureFruit(level, currentPos, currentState,
                        level.getRandom(), Config.boneMealFruitMaturityChance);
            }

            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = currentPos.relative(direction);
                if (!visited.contains(neighborPos) && isWithinSearchRadius(origin, neighborPos)) {
                    queue.addLast(neighborPos);
                }
            }
        }
        return ripened;
    }

    private static boolean isWithinSearchRadius(BlockPos origin, BlockPos pos) {
        return Math.abs(pos.getX() - origin.getX()) <= SEARCH_RADIUS
                && Math.abs(pos.getY() - origin.getY()) <= SEARCH_RADIUS
                && Math.abs(pos.getZ() - origin.getZ()) <= SEARCH_RADIUS;
    }
}
