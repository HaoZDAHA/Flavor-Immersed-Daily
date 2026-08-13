package com.flavor_immersed_daily.block.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class JuiceBlock extends Block {
    private final int nutrition;
    private final float saturation;

    public JuiceBlock(int nutrition, float saturation) {
        super(BlockBehaviour.Properties.of()
                .strength(0.3f)
                .sound(SoundType.GLASS)
                .noOcclusion());
        this.nutrition = nutrition;
        this.saturation = saturation;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.canEat(false)) {
            player.getFoodData().eat(nutrition, saturation);
            level.playSound(null, pos, SoundEvents.GENERIC_DRINK, SoundSource.BLOCKS, 1.0f, 1.0f);
            if (level.isClientSide) {
                for (int i = 0; i < 5; i++) {
                    level.addParticle(ParticleTypes.SPLASH,
                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            (level.random.nextDouble() - 0.5) * 0.2,
                            level.random.nextDouble() * 0.1,
                            (level.random.nextDouble() - 0.5) * 0.2);
                }
            }
            level.removeBlock(pos, false);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
