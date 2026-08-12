package com.flavor_immersed_daily;

import com.flavor_immersed_daily.block.FallingFruitBlock;
import com.flavor_immersed_daily.block.FruitingLeavesBlock;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = FlavorImmersedDaily.MODID)
public class FruitHarvestHandler {

    private static final int SEARCH_RADIUS = 7;
    private static final float EXHAUSTION_COST = 6.0F;

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        // 必须手持木棍
        if (!stack.is(Items.STICK)) return;

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        // 必须右键点击树干（原木）
        if (!(state.getBlock() instanceof RotatedPillarBlock)) return;

        // 检查玩家是否有足够的食物
        if (player.getFoodData().getFoodLevel() <= 0) return;

        // 客户端只播放挥动动画
        if (level.isClientSide) {
            player.swing(event.getHand());
            return;
        }

        // === 服务端逻辑 ===
        boolean harvested = false;

        // 扫描范围内所有方块，寻找结果子的树叶和悬挂的果子
        for (BlockPos searchPos : BlockPos.betweenClosed(
                pos.getX() - SEARCH_RADIUS, pos.getY() - SEARCH_RADIUS, pos.getZ() - SEARCH_RADIUS,
                pos.getX() + SEARCH_RADIUS, pos.getY() + SEARCH_RADIUS, pos.getZ() + SEARCH_RADIUS)) {
            BlockPos immutablePos = searchPos.immutable();
            BlockState searchState = level.getBlockState(immutablePos);
            Block searchBlock = searchState.getBlock();

            if (searchBlock instanceof FruitingLeavesBlock leavesBlock) {
                if (searchState.getValue(FruitingLeavesBlock.FRUITING)) {
                    // 采摘结果子的树叶
                    level.setBlock(immutablePos, searchState.setValue(FruitingLeavesBlock.FRUITING, false), 3);
                    Block.popResource(level, immutablePos, new ItemStack(leavesBlock.getFruitItem()));
                    harvested = true;
                    // 小概率掉落稀有水果变种
                    tryDropVariantFruit(level, immutablePos, searchBlock);
                }
            } else if (searchBlock instanceof FallingFruitBlock fallingBlock) {
                // 采摘悬挂的果子
                level.removeBlock(immutablePos, false);
                Block.popResource(level, immutablePos, new ItemStack(fallingBlock.getFruitItem()));
                harvested = true;
                // 小概率掉落稀有水果变种（检查上方是否有结果子树叶）
                Block aboveBlock = findFruitingLeavesAbove(level, immutablePos);
                if (aboveBlock != null) {
                    tryDropVariantFruit(level, immutablePos, aboveBlock);
                }
            }
        }

        if (harvested) {
            // 消耗饱食度
            player.getFoodData().addExhaustion(EXHAUSTION_COST);

            // 播放横扫特效
            ServerLevel serverLevel = (ServerLevel) level;
            serverLevel.playSound(null, pos, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.0F);
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    1, 0.0, 0.0, 0.0, 0.0);
        }

        event.setCanceled(true);
    }

    /**
     * 向上查找结果子树叶（用于悬挂果实掉落稀有变种）
     */
    private static Block findFruitingLeavesAbove(Level level, BlockPos pos) {
        for (int i = 0; i < 5; i++) {
            pos = pos.above();
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof FruitingLeavesBlock) {
                return state.getBlock();
            }
            if (!state.isAir() && !(state.getBlock() instanceof FruitingLeavesBlock)) {
                // 遇到非空气非树叶方块，停止查找
                return null;
            }
        }
        return null;
    }

    /**
     * 小概率掉落稀有水果变种（用于树叶收获事件）
     */
    public static void tryDropVariantFruit(Level level, BlockPos pos, Block block) {
        RandomSource random = level.getRandom();
        float chance = 0.15f; // 15%概率

        if (block == FlavorImmersedDaily.ORANGELEAVE_FRUITING_LEAVES.get()) {
            if (random.nextFloat() < chance) {
                Block.popResource(level, pos, new ItemStack(FlavorImmersedDaily.BLOODORANGE.get()));
            }
        } else if (block == FlavorImmersedDaily.TANGERINELEAVE_FRUITING_LEAVES.get()) {
            if (random.nextFloat() < chance) {
                // 丑橘或枳，各50%随机
                Item variant = random.nextBoolean() ? FlavorImmersedDaily.UGLYORANGE.get() : FlavorImmersedDaily.TANGERINE_1.get();
                Block.popResource(level, pos, new ItemStack(variant));
            }
        } else if (block == FlavorImmersedDaily.SWEETMELONLEAVE_FRUITING_LEAVES.get()) {
            if (random.nextFloat() < chance) {
                Block.popResource(level, pos, new ItemStack(FlavorImmersedDaily.SWEETMELON_1.get()));
            }
        } else if (block == FlavorImmersedDaily.APPLELEAVE_FRUITING_LEAVES.get()) {
            if (random.nextFloat() < chance) {
                Block.popResource(level, pos, new ItemStack(FlavorImmersedDaily.GREENAPPLE.get()));
            }
        } else if (block == FlavorImmersedDaily.HONEYPEACHLEAVE_FRUITING_LEAVES.get()) {
            if (random.nextFloat() < chance) {
                Block.popResource(level, pos, new ItemStack(FlavorImmersedDaily.LIFEPEACH.get()));
            }
        }
    }
}