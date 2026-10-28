package com.fidtest.block;

import com.fidtest.block.entity.SteamboxBlockEntity;
import com.fidtest.registration.ModBlockEntities;
import com.fidtest.registration.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class SteamboxBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final BooleanProperty PACKED = BooleanProperty.create("packed");
    public static final BooleanProperty WORKING = BooleanProperty.create("working");

    private static final VoxelShape EMPTY_SHAPE = Block.box(2, 0, 2, 14, 4, 14);
    private static final VoxelShape PACKED_SHAPE = Block.box(1, 0, 1, 15, 8, 15);
    private static final ResourceLocation TIDY_WATER_ID = new ResourceLocation("flavor_immersed_daily", "tidywater");

    public SteamboxBlock(BlockBehaviour.Properties p) {
        super(p.strength(0.5F, 3.0F));
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(BOTTOM, true).setValue(PACKED, false).setValue(WORKING, false));
    }

    @Override public RenderShape getRenderShape(BlockState s) { return RenderShape.MODEL; }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) { b.add(FACING, BOTTOM, PACKED, WORKING); }

    @Override
    public VoxelShape getShape(BlockState s, BlockGetter l, BlockPos p, CollisionContext c) {
        if (!s.getValue(PACKED) && !s.getValue(WORKING)) return EMPTY_SHAPE;
        return s.getValue(BOTTOM) ? Shapes.block() : PACKED_SHAPE;
    }

    @Nullable @Override public BlockState getStateForPlacement(BlockPlaceContext ctx) { return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()); }
    @Override public BlockState rotate(BlockState s, Rotation r) { return s.setValue(FACING, r.rotate(s.getValue(FACING))); }
    @Override public BlockState mirror(BlockState s, Mirror m) { return s.rotate(m.getRotation(s.getValue(FACING))); }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (!state.getValue(BOTTOM)) return InteractionResult.PASS;
        if (!(level.getBlockEntity(pos) instanceof SteamboxBlockEntity be)) return InteractionResult.PASS;

        ItemStack held = player.getItemInHand(hand);

// 工作中 → 不理
        if (state.getValue(WORKING)) return InteractionResult.PASS;

// 未打包 → 放入材料空手打包
        if (!state.getValue(PACKED)) {
            if (held.isEmpty()) {
                if (be.hasAnyInput()) {
                    if (!level.isClientSide) level.setBlock(pos, state.setValue(PACKED, true), 3);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;
            }
            int added = be.addItems(held);
            if (added > 0) {
                if (!level.isClientSide) player.displayClientMessage(Component.translatable("message.fidworkblock.steambox_can_pack"), true);
                return InteractionResult.SUCCESS;
            }
            if (!level.isClientSide) player.displayClientMessage(Component.translatable("message.fidworkblock.steambox_cant_steam"), true);
            return InteractionResult.CONSUME;
        }

// 已打包 → 堆叠 / 加水 / 空手解包
        if (!held.isEmpty()) {
            if (held.getItem() == ModBlocks.STEAMBOX.get().asItem() && isPackedItem(held)) {
                int height = getStackHeight(level, pos);
                if (height < 16) {
                    if (!level.isClientSide) {
                        BlockPos above = pos.above(height);
                        level.setBlock(above, state.setValue(BOTTOM, false).setValue(PACKED, true), 3);
                        if (level.getBlockEntity(above) instanceof SteamboxBlockEntity newBe) newBe.loadFromItemStack(held);
                        if (!player.isCreative()) held.shrink(1);
                        player.displayClientMessage(Component.translatable("message.fidworkblock.steambox_use_water"), true);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
            if (isTidyWater(held)) {
                int height = getStackHeight(level, pos);
                if (held.getCount() >= height) {
                    if (!level.isClientSide) { if (!player.isCreative()) held.shrink(height); processAllLayers(level, pos, height, player); }
                    return InteractionResult.SUCCESS;
                } else {
                    if (!level.isClientSide) player.displayClientMessage(Component.translatable("message.fidworkblock.steambox_need_water", height), true);
                }
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        }

// 空手 + 已打包 → 解包或收获
        if (!level.isClientSide) {
            BlockPos check = pos;
            boolean anyDone = false;
            while (level.getBlockState(check).is(ModBlocks.STEAMBOX.get())) {
                if (level.getBlockEntity(check) instanceof SteamboxBlockEntity ck && ck.hasOutput()) anyDone = true;
                check = check.above();
            }
            if (anyDone) { harvestAllLayers(level, pos, player); return InteractionResult.SUCCESS; }
            if (state.getValue(PACKED)) {
                BlockPos cp = pos;
                while (level.getBlockState(cp).is(ModBlocks.STEAMBOX.get())) {
                    if (level.getBlockEntity(cp) instanceof SteamboxBlockEntity lb) {
                        ItemStack packed = new ItemStack(ModBlocks.STEAMBOX.get().asItem());
                        lb.saveToItemStack(packed); lb.clearItems();
                        giveOrDrop(player, packed);
                    } else giveOrDrop(player, new ItemStack(ModBlocks.STEAMBOX.get().asItem()));
                    level.removeBlock(cp, false);
                    cp = cp.above();
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void harvestAllLayers(Level level, BlockPos bp, Player player) {
        BlockPos c = bp;
        while (level.getBlockState(c).is(ModBlocks.STEAMBOX.get())) {
            if (level.getBlockEntity(c) instanceof SteamboxBlockEntity lb) {
                for (ItemStack s : lb.getAllItems()) Containers.dropItemStack(level, c.getX()+0.5, c.getY()+0.5, c.getZ()+0.5, s);
                lb.clearItems();
            }
            giveOrDrop(player, new ItemStack(ModBlocks.STEAMBOX.get().asItem()));
            level.removeBlock(c, false);
            c = c.above();
        }
        level.playSound(null, bp, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.0F);
    }

    private void processAllLayers(Level level, BlockPos pos, int height, Player player) {
        int maxTime = 0;
        for (int i = 0; i < height; i++) {
            BlockPos lp = pos.above(i);
            if (level.getBlockEntity(lp) instanceof SteamboxBlockEntity lb) {
                if (!lb.startProcessing()) {
                    for (ItemStack s : lb.getAllItems()) Containers.dropItemStack(level, lp.getX()+0.5, lp.getY()+0.5, lp.getZ()+0.5, s);
                    lb.clearItems(); giveOrDrop(player, new ItemStack(ModBlocks.STEAMBOX.get().asItem())); level.removeBlock(lp, false);
                } else maxTime = Math.max(maxTime, lb.getTotalProcessTime());
            }
        }
        for (int i = 0; i < height; i++) {
            BlockPos lp = pos.above(i);
            if (level.getBlockState(lp).is(ModBlocks.STEAMBOX.get()) && level.getBlockState(lp).getValue(WORKING)) {
                if (level.getBlockEntity(lp) instanceof SteamboxBlockEntity lb) lb.setTotalProcessTime(maxTime);
            }
        }
        for (int i = 0; i < height; i++) {
            BlockPos lp = pos.above(i);
            if (level.getBlockState(lp).is(ModBlocks.STEAMBOX.get()) && level.getBlockState(lp).getValue(WORKING))
                level.setBlock(lp, level.getBlockState(lp).setValue(WORKING, true), 3);
        }
    }

    @Override
    public void animateTick(BlockState s, Level level, BlockPos pos, RandomSource rng) {
        if (!s.getValue(WORKING)) return;
        for (int i = 0; i < 4; i++) level.addParticle(ParticleTypes.SMOKE, pos.getX()+0.3+rng.nextDouble()*0.4, pos.getY()+1.05, pos.getZ()+0.3+rng.nextDouble()*0.4, 0, 0.05, 0);
        for (int i = 0; i < 3; i++) level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX()+0.2+rng.nextDouble()*0.6, pos.getY()+1.05, pos.getZ()+0.2+rng.nextDouble()*0.6, 0, 0.06, 0);
        if (rng.nextInt(2) == 0) level.addParticle(ParticleTypes.CLOUD, pos.getX()+0.2+rng.nextDouble()*0.6, pos.getY()+1.1, pos.getZ()+0.2+rng.nextDouble()*0.6, 0, 0.04, 0);
        int side = rng.nextInt(4);
        for (int i = 0; i < 2; i++) {
            double sx, sz, sy = pos.getY()+0.2+rng.nextDouble()*0.6;
            if (side == 0) { sx = pos.getX()+0.05; sz = pos.getZ()+0.2+rng.nextDouble()*0.6; }
            else if (side == 1) { sx = pos.getX()+0.95; sz = pos.getZ()+0.2+rng.nextDouble()*0.6; }
            else if (side == 2) { sx = pos.getX()+0.2+rng.nextDouble()*0.6; sz = pos.getZ()+0.05; }
            else { sx = pos.getX()+0.2+rng.nextDouble()*0.6; sz = pos.getZ()+0.95; }
            level.addParticle(ParticleTypes.CLOUD, sx, sy, sz, 0, 0.02, 0);
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

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide && !stack.isEmpty() && isPackedItem(stack)) {
            if (level.getBlockEntity(pos) instanceof SteamboxBlockEntity be) { be.loadFromItemStack(stack.copy()); level.setBlock(pos, state.setValue(PACKED, true), 3); }
        }
    }

    @Nullable @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new SteamboxBlockEntity(pos, state); }
    @Nullable @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level l, BlockState s, BlockEntityType<T> t) {
        if (l.isClientSide) return null;
        return createTickerHelper(t, ModBlockEntities.STEAMBOX.get(), (lv, p, st, be) -> SteamboxBlockEntity.serverTick(be));
    }

    public static int getStackHeight(Level level, BlockPos pos) { int h = 1; BlockPos c = pos.above(); while (h < 16 && level.getBlockState(c).is(ModBlocks.STEAMBOX.get())) { h++; c = c.above(); } return h; }
    private boolean isTidyWater(ItemStack s) { return !s.isEmpty() && s.getItem().builtInRegistryHolder().key().location().equals(TIDY_WATER_ID); }
    private void giveOrDrop(Player p, ItemStack s) { if (!p.getInventory().add(s)) p.drop(s, false); }

    public static boolean isPackedItem(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("SteamboxData") && !tag.getCompound("SteamboxData").getList("Input", 10).isEmpty();
    }
}
