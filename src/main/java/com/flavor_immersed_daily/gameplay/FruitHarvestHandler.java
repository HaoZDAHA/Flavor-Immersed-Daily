package com.flavor_immersed_daily.gameplay;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.block.block.fruit.FallingFruitBlock;
import com.flavor_immersed_daily.block.block.fruit.FruitingLeavesBlock;
import com.flavor_immersed_daily.config.Config;
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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class FruitHarvestHandler {
    private static final int SEARCH_RADIUS = 7;
    private static final int MAX_SEARCHED_BLOCKS = 512;
    private static final float EXHAUSTION_COST = 6.0F;

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

// 对树干使用骨粉：随机催熟整棵树的一部分树叶
        if (stack.is(Items.BONE_MEAL)) {
            if (!(state.getBlock() instanceof RotatedPillarBlock)) return;
            if (level.isClientSide) {
                player.swing(event.getHand());
                return;
            }
            ServerLevel serverLevel = (ServerLevel) level;
            int matured = matureConnectedLeaves(serverLevel, pos, state.getBlock());
            if (matured > 0) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                serverLevel.playSound(null, pos, SoundEvents.BONE_MEAL_USE,
                        SoundSource.BLOCKS, 1.0F, 1.0F);
                serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        10, 0.5, 0.5, 0.5, 0.0);
                event.setCanceled(true);
            }
            return;
        }

        if (!stack.is(Items.STICK)) return;
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

// 对树干使用骨粉时调用：以树干为中心收集相连的果树树叶，
// 每片未挂果的树叶按 Config#boneMealFruitMaturityChance 的概率推进一档成熟。
// @return 实际被催熟的树叶数量
    private static int matureConnectedLeaves(ServerLevel level, BlockPos origin, Block trunkBlock) {
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        List<BlockPos> leafPositions = new ArrayList<>();
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

            if (currentBlock instanceof FruitingLeavesBlock) {
                leafPositions.add(currentPos);
            }

            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = currentPos.relative(direction);
                if (!visited.contains(neighborPos) && isWithinSearchRadius(origin, neighborPos)) {
                    queue.addLast(neighborPos);
                }
            }
        }

        RandomSource random = level.getRandom();
        int matured = 0;
        for (BlockPos leafPos : leafPositions) {
            BlockState leafState = level.getBlockState(leafPos);
            if (leafState.getBlock() instanceof FruitingLeavesBlock leavesBlock
                    && leavesBlock.matureOnce(level, leafPos, leafState, random,
                            Config.boneMealFruitMaturityChance)) {
                matured++;
            }
        }
        return matured;
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
        } else if (block == ModBlocks.MANGOLEAVE_FRUITING_LEAVES.get()
                && random.nextFloat() < chance) {
            Block.popResource(level, pos, new ItemStack(ModItems.GREENMANGO.get()));
        } else if (block == ModBlocks.NECTARINELEAVE_FRUITING_LEAVES.get()
                && random.nextFloat() < chance) {
            Block.popResource(level, pos, new ItemStack(ModItems.YELLOWPEACH.get()));
        }
    }
}
