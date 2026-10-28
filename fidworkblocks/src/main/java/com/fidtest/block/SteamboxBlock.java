package com.fidtest.block;

import com.fidtest.block.entity.SteamboxBlockEntity;
import com.fidtest.registration.ModBlockEntities;
import com.fidtest.registration.ModBlocks;
import com.fidtest.ExampleMod;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SteamboxBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final BooleanProperty PACKED = BooleanProperty.create("packed");
    public static final BooleanProperty WORKING = BooleanProperty.create("working");

    private static final VoxelShape EMPTY_SHAPE = Block.box(2, 0, 2, 14, 4, 14);
    private static final VoxelShape PACKED_SHAPE = Block.box(1, 0, 1, 15, 8, 15);
    private static final VoxelShape FULL_SHAPE = Shapes.block();

    private static final net.minecraft.resources.ResourceLocation TIDY_WATER_ID =
            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("flavor_immersed_daily", "tidywater");

    public static final MapCodec<SteamboxBlock> CODEC = simpleCodec(SteamboxBlock::new);

    public SteamboxBlock(BlockBehaviour.Properties properties) {
        super(properties.strength(0.5F, 3.0F));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(BOTTOM, true)
                .setValue(PACKED, false)
                .setValue(WORKING, false));
    }

    @Override protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }
    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BOTTOM, PACKED, WORKING);
    }

    @Override
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level,
                               BlockPos pos, CollisionContext ctx) {
        if (!state.getValue(PACKED) && !state.getValue(WORKING)) return EMPTY_SHAPE;
        return state.getValue(BOTTOM) ? FULL_SHAPE : PACKED_SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            if (held.getItem() == ModBlocks.STEAMBOX.asItem() && isPackedItem(held)) {
                return ItemInteractionResult.SUCCESS;
            }
            if (!state.getValue(BOTTOM)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            return ItemInteractionResult.SUCCESS;
        }

// 手持已打包的蒸笼：无论点在哪一层，都继续向上堆叠
        if (held.getItem() == ModBlocks.STEAMBOX.asItem() && isPackedItem(held)) {
            BlockPos bottomPos = findBottom(level, pos);
            if (bottomPos != null) {
                int height = getStackHeight(level, bottomPos);
                if (height < 16) {
                    BlockPos above = bottomPos.above(height);
                    BlockState aboveState = state.setValue(BOTTOM, false)
                            .setValue(PACKED, true)
                            .setValue(WORKING, false);
                    level.setBlock(above, aboveState, 3);

                    if (level.getBlockEntity(above) instanceof SteamboxBlockEntity newBe) {
                        newBe.loadFromItemStack(held);
                    }

                    if (!player.isCreative()) held.shrink(1);
                    player.displayClientMessage(Component.translatable("message.fidworkblock.steambox_use_water"), true);
                    return ItemInteractionResult.SUCCESS;
                }
            }
            return ItemInteractionResult.FAIL;
        }

        if (!state.getValue(BOTTOM)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!(level.getBlockEntity(pos) instanceof SteamboxBlockEntity be)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (state.getValue(WORKING)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!state.getValue(PACKED)) {
            if (held.isEmpty()) {
                if (be.hasAnyInput()) {
                    be.setPacked(true);
                    level.setBlock(pos, state.setValue(PACKED, true), 3);
                    return ItemInteractionResult.SUCCESS;
                }
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }

            int added = be.addItems(held);
            if (added > 0) {
                player.displayClientMessage(Component.translatable("message.fidworkblock.steambox_can_pack"), true);
                return ItemInteractionResult.SUCCESS;
            }
            player.displayClientMessage(Component.translatable("message.fidworkblock.steambox_cant_steam"), true);
            return ItemInteractionResult.FAIL;
        }

        if (state.getValue(PACKED) && !state.getValue(WORKING)) {
            if (isTidyWater(held)) {
                int height = getStackHeight(level, pos);
                if (held.getCount() >= height) {
                    if (!player.isCreative()) held.shrink(height);
                    processAllLayers(level, pos, height, player);
                    return ItemInteractionResult.SUCCESS;
                } else {
                    player.displayClientMessage(
                            Component.translatable("message.fidworkblock.steambox_need_water", height), true);
                    return ItemInteractionResult.FAIL;
                }
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockPos bottomPos = findBottom(level, pos);
        if (bottomPos == null) return InteractionResult.PASS;
        BlockState bottomState = level.getBlockState(bottomPos);
        if (!(level.getBlockEntity(bottomPos) instanceof SteamboxBlockEntity bottomBe)) return InteractionResult.PASS;

        if (bottomState.getValue(WORKING)) {
            return InteractionResult.PASS;
        }

        boolean anyDone = false;
        BlockPos check = bottomPos;
        while (level.getBlockState(check).is(ModBlocks.STEAMBOX.get())) {
            if (level.getBlockEntity(check) instanceof SteamboxBlockEntity checkBe && checkBe.hasOutput()) {
                anyDone = true;
            }
            check = check.above();
        }

        if (anyDone) {
            harvestAllLayers(level, bottomPos, player);
            return InteractionResult.SUCCESS;
        }

        if (bottomState.getValue(PACKED)) {
            BlockPos checkPos = bottomPos;
            while (level.getBlockState(checkPos).is(ModBlocks.STEAMBOX.get())) {
                if (level.getBlockEntity(checkPos) instanceof SteamboxBlockEntity layerBe) {
                    ItemStack packed = new ItemStack(ModBlocks.STEAMBOX.asItem());
                    layerBe.saveToItemStack(packed);
                    layerBe.clearItems();
                    giveOrDrop(player, packed);
                } else {
                    giveOrDrop(player, new ItemStack(ModBlocks.STEAMBOX.asItem()));
                }
                level.removeBlock(checkPos, false);
                checkPos = checkPos.above();
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private void harvestAllLayers(Level level, BlockPos bottomPos, Player player) {
        BlockPos check = bottomPos;
        while (level.getBlockState(check).is(ModBlocks.STEAMBOX.get())) {
            if (level.getBlockEntity(check) instanceof SteamboxBlockEntity layerBe) {
                for (ItemStack s : layerBe.getAllItems()) {
                    Containers.dropItemStack(level, check.getX() + 0.5, check.getY() + 0.5,
                            check.getZ() + 0.5, s);
                }
                layerBe.clearItems();
            }
            giveOrDrop(player, new ItemStack(ModBlocks.STEAMBOX.asItem()));
            level.removeBlock(check, false);
            check = check.above();
        }
        level.playSound(null, bottomPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
    }

    private BlockPos findBottom(Level level, BlockPos pos) {
        BlockPos check = pos;
        while (level.getBlockState(check.below()).is(ModBlocks.STEAMBOX.get())) {
            check = check.below();
        }
        if (level.getBlockState(check).is(ModBlocks.STEAMBOX.get())) {
            return check;
        }
        return null;
    }

    private void processAllLayers(Level level, BlockPos pos, int height, Player player) {
        int maxTime = 0;
        for (int i = 0; i < height; i++) {
            BlockPos layerPos = pos.above(i);
            if (level.getBlockEntity(layerPos) instanceof SteamboxBlockEntity layerBe) {
                if (!layerBe.startProcessing()) {
                    for (ItemStack s : layerBe.getAllItems()) {
                        Containers.dropItemStack(level, layerPos.getX() + 0.5, layerPos.getY() + 0.5,
                                layerPos.getZ() + 0.5, s);
                    }
                    layerBe.clearItems();
                    ItemStack box = new ItemStack(ModBlocks.STEAMBOX.asItem());
                    giveOrDrop(player, box);
                    level.removeBlock(layerPos, false);
                } else {
                    maxTime = Math.max(maxTime, layerBe.getTotalProcessTime());
                }
            }
        }
        for (int i = 0; i < height; i++) {
            BlockPos layerPos = pos.above(i);
            if (level.getBlockEntity(layerPos) instanceof SteamboxBlockEntity layerBe && layerBe.isWorking()) {
                layerBe.setTotalProcessTime(maxTime);
            }
        }
        for (int i = 0; i < height; i++) {
            BlockPos layerPos = pos.above(i);
            BlockState layerState = level.getBlockState(layerPos);
            if (layerState.is(ModBlocks.STEAMBOX.get())) {
                level.setBlock(layerPos, layerState.setValue(WORKING, true), 3);
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, net.minecraft.util.RandomSource rng) {
        if (!state.getValue(WORKING)) return;

        for (int i = 0; i < 4; i++) {
            double x = pos.getX() + 0.3 + rng.nextDouble() * 0.4;
            double y = pos.getY() + 1.05;
            double z = pos.getZ() + 0.3 + rng.nextDouble() * 0.4;
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.05, 0.0);
        }

        for (int i = 0; i < 3; i++) {
            double x = pos.getX() + 0.2 + rng.nextDouble() * 0.6;
            double y = pos.getY() + 1.05;
            double z = pos.getZ() + 0.2 + rng.nextDouble() * 0.6;
            level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y, z, 0.0, 0.06, 0.0);
        }

        if (rng.nextInt(2) == 0) {
            double x = pos.getX() + 0.2 + rng.nextDouble() * 0.6;
            double y = pos.getY() + 1.1;
            double z = pos.getZ() + 0.2 + rng.nextDouble() * 0.6;
            level.addParticle(ParticleTypes.CLOUD, x, y, z, 0.0, 0.04, 0.0);
        }

        int side = rng.nextInt(4);
        for (int i = 0; i < 2; i++) {
            double sx, sy, sz;
            sy = pos.getY() + 0.2 + rng.nextDouble() * 0.6;
            switch (side) {
                case 0 -> { sx = pos.getX() + 0.05; sz = pos.getZ() + 0.2 + rng.nextDouble() * 0.6; }
                case 1 -> { sx = pos.getX() + 0.95; sz = pos.getZ() + 0.2 + rng.nextDouble() * 0.6; }
                case 2 -> { sx = pos.getX() + 0.2 + rng.nextDouble() * 0.6; sz = pos.getZ() + 0.05; }
                default -> { sx = pos.getX() + 0.2 + rng.nextDouble() * 0.6; sz = pos.getZ() + 0.95; }
            }
            level.addParticle(ParticleTypes.WHITE_SMOKE, sx, sy, sz, 0.0, 0.02, 0.0);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && state.getValue(PACKED)) {
                if (level.getBlockEntity(pos) instanceof SteamboxBlockEntity be) {
                    if (be.hasAnyInput() || be.hasOutput()) {
                        ItemStack packed = new ItemStack(asItem());
                        be.saveToItemStack(packed);
                        Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, packed);
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SteamboxBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return createTickerHelper(type, ModBlockEntities.STEAMBOX.get(),
                (lvl, pos, st, be) -> SteamboxBlockEntity.serverTick(be));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            @Nullable net.minecraft.world.entity.LivingEntity placer,
                            ItemStack stack) {
        if (!level.isClientSide && !stack.isEmpty() && isPackedItem(stack)) {
            if (level.getBlockEntity(pos) instanceof SteamboxBlockEntity be) {
                be.loadFromItemStack(stack.copy());
                level.setBlock(pos, state.setValue(PACKED, true), 3);
            }
        }
    }

    public static int getStackHeight(Level level, BlockPos pos) {
        int h = 1;
        BlockPos check = pos.above();
        while (h < 16 && level.getBlockState(check).is(ModBlocks.STEAMBOX.get())) {
            h++;
            check = check.above();
        }
        return h;
    }

    private boolean isTidyWater(ItemStack stack) {
        return stack.getItem().builtInRegistryHolder().key().location().equals(TIDY_WATER_ID);
    }

    private void giveOrDrop(Player player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    public static boolean isPackedItem(ItemStack stack) {
        try {
            CustomData custom = stack.get(DataComponents.CUSTOM_DATA);
            if (custom != null) {
                CompoundTag root = custom.copyTag();
                if (root.contains("SteamboxData")) {
                    ListTag inputList = root.getCompound("SteamboxData").getList("Input", 10);
                    return inputList.size() > 0;
                }
            }
        } catch (Exception ignored) {}
        return false;
    }
}
