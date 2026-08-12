package com.flavor_immersed_daily.block;

import com.flavor_immersed_daily.Config;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 木盆 — 6px碰撞体积，右键纯净水装水 / chickenwithoutblood漂洗 / 放入水果踩踏成果酱
 * WATERED: false=空盆, true=有水
 * HAS_CHICKEN: false=无鸡, true=盆中有chickenwithoutblood
 * HAS_FRUIT: false=无水果, true=盆中有水果
 */
public class WoodBasinBlock extends BaseEntityBlock {

    public static final BooleanProperty WATERED = BooleanProperty.create("watered");
    public static final BooleanProperty HAS_CHICKEN = BooleanProperty.create("has_chicken");
    public static final BooleanProperty HAS_FRUIT = BooleanProperty.create("has_fruit");

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 6, 16);

    /** 水果 → 果酱 的映射表 */
    private static final Map<String, String> FRUIT_TO_JAM = new LinkedHashMap<>();

    static {
        FRUIT_TO_JAM.put("flavor_immersed_daily:pineapple", "flavor_immersed_daily:pineapplejam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:strawberry", "flavor_immersed_daily:strawberryjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:orange", "flavor_immersed_daily:orangejam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:winterjujube", "flavor_immersed_daily:winterjujubejam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:hamimelonseed", "flavor_immersed_daily:hamimelonjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:dragonfruit", "flavor_immersed_daily:dragonfruitjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:tangerine", "flavor_immersed_daily:tangerinejam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:blueberry", "flavor_immersed_daily:blueberryjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:pear", "flavor_immersed_daily:pearjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:lychee", "flavor_immersed_daily:lycheejam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:plum", "flavor_immersed_daily:plumjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:durian", "flavor_immersed_daily:durianjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:mango", "flavor_immersed_daily:mangojam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:kiwifruit", "flavor_immersed_daily:kiwifruitjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:pawpaw", "flavor_immersed_daily:pawpawjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:lemon", "flavor_immersed_daily:lemonjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:loquat", "flavor_immersed_daily:loquatjam");
        FRUIT_TO_JAM.put("minecraft:apple", "flavor_immersed_daily:applejam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:grape", "flavor_immersed_daily:grapejam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:greenplum", "flavor_immersed_daily:greemplumjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:greengrape", "flavor_immersed_daily:greengrapejam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:mulberry", "flavor_immersed_daily:mulberryjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:hawthorn", "flavor_immersed_daily:hawthornjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:mangosteen", "flavor_immersed_daily:mangosteenjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:pomegranate", "flavor_immersed_daily:pomegranatejam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:honeypeach", "flavor_immersed_daily:honeypeachjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:sweetmelon", "flavor_immersed_daily:sweetmelonjam");
        FRUIT_TO_JAM.put("minecraft:sweet_berries", "flavor_immersed_daily:sweetberryjam");
        FRUIT_TO_JAM.put("minecraft:melon_slice", "flavor_immersed_daily:watermelonjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:banana", "flavor_immersed_daily:bananajam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:apricot", "flavor_immersed_daily:apricotjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:carambola", "flavor_immersed_daily:carambolajam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:coconut", "flavor_immersed_daily:coconutjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:cherry", "flavor_immersed_daily:cherryjam");
        FRUIT_TO_JAM.put("flavor_immersed_daily:nectarine", "flavor_immersed_daily:nectarinejam");
    }

    public WoodBasinBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(WATERED, false)
                .setValue(HAS_CHICKEN, false)
                .setValue(HAS_FRUIT, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(WoodBasinBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERED, HAS_CHICKEN, HAS_FRUIT);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WoodBasinBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                    BlockEntityType<T> type) {
        return createTickerHelper(type, FlavorImmersedDaily.WOODBASIN_ENTITY.get(),
                WoodBasinBlockEntity::tick);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        boolean watered = state.getValue(WATERED);
        boolean hasChicken = state.getValue(HAS_CHICKEN);
        boolean hasFruit = state.getValue(HAS_FRUIT);

        // === chickenwithoutblood 放上水盆 ===
        if (watered && !hasChicken && !hasFruit && stack.is(FlavorImmersedDaily.CHICKENWITHOUTBLOOD.get())) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(HAS_CHICKEN, true), 3);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            level.playSound(player, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.1f);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        // === 漂洗完，取回chickenwithoutfeather + 战利品 ===
        if (hasChicken) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(WATERED, false).setValue(HAS_CHICKEN, false), 3);
                level.addFreshEntity(new ItemEntity(level,
                        pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5,
                        new ItemStack(FlavorImmersedDaily.CHICKENWITHOUTFEATHER.get())));
                for (String itemId : Config.washedChickenDrops) {
                    Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemId));
                    if (item != null) {
                        level.addFreshEntity(new ItemEntity(level,
                                pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5,
                                new ItemStack(item)));
                    }
                }
            }
            level.playSound(player, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        // === 空盆 → 装水 ===
        if (!watered && !hasFruit) {
            if (stack.is(FlavorImmersedDaily.TIDYWATER.get())) {
                if (!level.isClientSide) {
                    level.setBlock(pos, state.setValue(WATERED, true), 3);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
                level.playSound(player, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        // === 空盆（无水无鸡无水果）→ 放入水果 ===
        if (!watered && !hasChicken && !hasFruit) {
            String heldItemId = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
            if (FRUIT_TO_JAM.containsKey(heldItemId)) {
                if (!level.isClientSide) {
                    BlockState newState = state.setValue(HAS_FRUIT, true);
                    level.setBlock(pos, newState, 3);
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof WoodBasinBlockEntity basinBe) {
                        basinBe.setFruit(heldItemId, stack.getCount());
                    }
                    // 强制同步BlockEntity数据到客户端
                    level.sendBlockUpdated(pos, state, newState, Block.UPDATE_ALL);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(stack.getCount());
                    }
                }
                level.playSound(player, pos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0f, 1.0f);
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        // === 有水盆 → 潜行右键倒水 ===
        if (watered && !hasChicken && !hasFruit && player.isShiftKeyDown() && stack.isEmpty()) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(WATERED, false), 3);
            }
            level.playSound(player, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            if (state.getValue(HAS_CHICKEN)) {
                level.addFreshEntity(new ItemEntity(level,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        new ItemStack(FlavorImmersedDaily.CHICKENWITHOUTBLOOD.get())));
            }
            if (state.getValue(HAS_FRUIT)) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof WoodBasinBlockEntity basinBe && basinBe.hasFruit()) {
                    ItemStack fruitStack = basinBe.getFruitStack();
                    if (!fruitStack.isEmpty()) {
                        level.addFreshEntity(new ItemEntity(level,
                                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                                fruitStack));
                    }
                }
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    /** 根据水果注册名查找对应的果酱物品 */
    @Nullable
    public static Item getJamForFruit(String fruitId) {
        String jamId = FRUIT_TO_JAM.get(fruitId);
        if (jamId != null) {
            return BuiltInRegistries.ITEM.get(ResourceLocation.parse(jamId));
        }
        return null;
    }

    /** 获取所有水果→果酱映射，供JEI使用 */
    public static Map<String, String> getFruitToJamMap() {
        return FRUIT_TO_JAM;
    }
}
