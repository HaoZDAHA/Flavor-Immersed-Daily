package com.flavor_immersed_daily.event;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.item.KitchenScissorsItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 厨房剪刀事件 — 破坏草和树叶时概率掉落野生作物
 */
@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class KitchenScissorsEventHandler {

    private static final Random RANDOM = new Random();
    private static final double CHANCE = 0.3;
    private static final double REEDLEAF_CHANCE = 0.15;

    private static List<ItemStack> wildCrops = null;
    private static List<ItemStack> wildFruits = null;

    private static List<ItemStack> getWildCrops() {
        if (wildCrops == null) {
            List<ItemStack> list = new ArrayList<>();
            list.add(new ItemStack(ModItems.WILDFLOWERANDLEAF.get()));
            list.add(new ItemStack(ModItems.WILDGRAINPLANT.get()));
            list.add(new ItemStack(ModItems.WILDMUSHROOMPLANT.get()));
            list.add(new ItemStack(ModItems.WILDSEEDPLANT.get()));
            list.add(new ItemStack(ModItems.WILDTUBERPLANTS.get()));
            wildCrops = List.copyOf(list);
        }
        return wildCrops;
    }

    private static List<ItemStack> getWildFruits() {
        if (wildFruits == null) {
            List<ItemStack> list = new ArrayList<>();
            list.add(new ItemStack(ModItems.TEMPERATEWILDFRUIT.get()));
            list.add(new ItemStack(ModItems.TROPICALWILD_FRUIT.get()));
            list.add(new ItemStack(ModItems.WILDFRUITINCOLDZONE.get()));
            wildFruits = List.copyOf(list);
        }
        return wildFruits;
    }

    @SubscribeEvent
    static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        if (!(event.getPlayer().getMainHandItem().getItem() instanceof KitchenScissorsItem)) {
            return;
        }

        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        Block block = state.getBlock();

        if (isGrass(block)) {
            if (RANDOM.nextDouble() < CHANCE) {
                List<ItemStack> crops = getWildCrops();
                ItemStack drop = crops.get(RANDOM.nextInt(crops.size())).copy();
                spawnItem(level, pos, drop);
            }
            if (RANDOM.nextDouble() < REEDLEAF_CHANCE) {
                spawnItem(level, pos, new ItemStack(ModItems.REEDLEAF.get()));
            }
        } else if (isLeaves(state)) {
            if (RANDOM.nextDouble() < CHANCE) {
                List<ItemStack> fruits = getWildFruits();
                ItemStack drop = fruits.get(RANDOM.nextInt(fruits.size())).copy();
                spawnItem(level, pos, drop);
            }
        }
    }

    private static boolean isGrass(Block block) {
        return block == Blocks.SHORT_GRASS
                || block == Blocks.TALL_GRASS
                || block == Blocks.FERN
                || block == Blocks.LARGE_FERN;
    }

    private static boolean isLeaves(BlockState state) {
        return state.is(net.minecraft.tags.BlockTags.LEAVES);
    }

    private static void spawnItem(Level level, BlockPos pos, ItemStack stack) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        ItemEntity itemEntity = new ItemEntity(level, x, y, z, stack);
        itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);
    }
}

