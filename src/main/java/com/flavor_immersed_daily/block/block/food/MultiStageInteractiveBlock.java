package com.flavor_immersed_daily.block.block.food;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MultiStageInteractiveBlock extends Block {
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 2);
    private static final VoxelShape COLLISION_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D);

    private final int nutrition;
    private final float saturation;
    private final String itemId;

    public MultiStageInteractiveBlock(int nutrition, float saturation, String itemId, Properties properties) {
        super(properties.sound(SoundType.WOOD));
        this.nutrition = nutrition;
        this.saturation = saturation;
        this.itemId = itemId;
        registerDefaultState(stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos,
                               CollisionContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hitResult) {
        if (state.getValue(STAGE) < 2) {
            level.setBlock(pos, state.cycle(STAGE), 3);
            level.playSound(null, pos, SoundEvents.HONEY_DRINK, SoundSource.PLAYERS, 0.5F, 1.0F);
            player.eat(level, new ItemStack(foodItem()).copyWithCount(1));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        level.destroyBlock(pos, true);
        level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.BLOCKS, 0.8F, 1.0F);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private Item foodItem() {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, itemId);
        return BuiltInRegistries.ITEM.getOptional(id).orElse(Items.AIR);
    }
}
