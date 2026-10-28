package com.flavor_immersed_daily.command;

import com.flavor_immersed_daily.block.block.fruit.FallingFruitBlock;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public final class FidDebugCommands {
    private static final int DROP_RADIUS = 5;

    private FidDebugCommands() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("fid_debug")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("drop_fruits")
                        .executes(context -> dropNearbyFruits(context.getSource()))));
    }

    private static int dropNearbyFruits(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        BlockPos center = BlockPos.containing(source.getPosition());
        int dropped = 0;

        for (BlockPos mutablePos : BlockPos.betweenClosed(
                center.offset(-DROP_RADIUS, -DROP_RADIUS, -DROP_RADIUS),
                center.offset(DROP_RADIUS, DROP_RADIUS, DROP_RADIUS))) {
            if (center.distSqr(mutablePos) > DROP_RADIUS * DROP_RADIUS) continue;

            BlockPos pos = mutablePos.immutable();
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof FallingFruitBlock fruitBlock
                    && fruitBlock.forceDrop(level, pos, state)) {
                dropped++;
            }
        }

        int result = dropped;
        source.sendSuccess(() -> Component.literal(
                "已让周围 5 格内的 " + result + " 个挂果直接掉落。"), true);
        return dropped;
    }
}
