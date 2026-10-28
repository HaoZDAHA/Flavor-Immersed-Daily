package com.fidtest.block.entity;

import com.fidtest.choppingboard.ChoppingboardMobDefinition;
import com.fidtest.choppingboard.ChoppingboardMobRegistry;
import com.fidtest.integration.FdCuttingCompat;
import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.CookingRecipeInput;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChoppingboardBlockEntity extends BlockEntity {

    private static final ResourceLocation KITCHEN_KNIFE_ID =
            new ResourceLocation("flavor_immersed_daily", "kitchenknife");

// 农夫乐事刀具标签（含其附属模组注册的刀具）：砧板切割配方接受该标签内的物品作为刀具
    public static final TagKey<Item> FD_KNIFE_TAG =
            ItemTags.create(new ResourceLocation("farmersdelight", "tools/knives"));

// 是否为砧板可用刀具：本模组菜刀，或农夫乐事刀具标签内的刀具
    public static boolean isKnife(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (KITCHEN_KNIFE_ID.equals(stack.getItem().builtInRegistryHolder().key().location())) return true;
        return stack.is(FD_KNIFE_TAG);
    }

// 擀面杖（把面团/面剂子/面皮擀成下一步形态）
    public static final ResourceLocation ROLLING_PIN_ID =
            new ResourceLocation("flavor_immersed_daily", "rollingpin");

// 是否为擀面杖
    public static boolean isRollingPin(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return ROLLING_PIN_ID.equals(stack.getItem().builtInRegistryHolder().key().location());
    }

// 月饼模具（把生馅饼压成生月饼）
    public static final ResourceLocation MOONCAKE_MOLD_ID =
            new ResourceLocation("flavor_immersed_daily", "mooncakemold");
// 糯米（粽子最后一步材料）
    public static final ResourceLocation RICE_ID =
            new ResourceLocation("flavor_immersed_daily", "polishedglutinousrice_2");
// 生馅饼（月饼前置）
    public static final ResourceLocation RAW_PIE_ID =
            new ResourceLocation("flavor_immersed_daily", "rawpie");
// 生月饼
    public static final ResourceLocation RAW_MOONCAKE_ID =
            new ResourceLocation("flavor_immersed_daily", "rawmooncake");
// 生粽子
    public static final ResourceLocation RAW_ZONGZI_ID =
            new ResourceLocation("flavor_immersed_daily", "rawazongzi");
// 洗净的芦苇叶（粽子皮）
    public static final ResourceLocation REED_LEAF_ID =
            new ResourceLocation("flavor_immersed_daily", "tidyreedleaf");

// 食材槽（原砧板上被切的东西）
    public static final int SLOT_INGREDIENT = 0;
// 皮槽：存放包子皮/饺子皮等
    public static final int SLOT_SKIN = 1;
// 馅料槽：皮上放的物品
    public static final int SLOT_FILLING = 2;

// 砧板可放的各种皮
    public static final List<ResourceLocation> SKIN_IDS = List.of(
            new ResourceLocation("flavor_immersed_daily", "baozi_skin"),
            new ResourceLocation("flavor_immersed_daily", "dumpling_skin"),
            new ResourceLocation("flavor_immersed_daily", "shumaiskin"),
            new ResourceLocation("flavor_immersed_daily", "rawglutinousskin"),
            new ResourceLocation("flavor_immersed_daily", "rawflourpancake"),
            new ResourceLocation("flavor_immersed_daily", "flourskin"),
            new ResourceLocation("flavor_immersed_daily", "tidyreedleaf")
    );

// 皮 → 对应生食的映射（用于包裹工序，芦苇叶暂无对应生食）
    public static final Map<ResourceLocation, ResourceLocation> RAW_FOOD_MAP = Map.of(
            new ResourceLocation("flavor_immersed_daily", "baozi_skin"),
            new ResourceLocation("flavor_immersed_daily", "raw_baozi"),
            new ResourceLocation("flavor_immersed_daily", "dumpling_skin"),
            new ResourceLocation("flavor_immersed_daily", "raw_dumpling"),
            new ResourceLocation("flavor_immersed_daily", "shumaiskin"),
            new ResourceLocation("flavor_immersed_daily", "rawshumai"),
            new ResourceLocation("flavor_immersed_daily", "flourskin"),
            new ResourceLocation("flavor_immersed_daily", "raw_wonton"),
            new ResourceLocation("flavor_immersed_daily", "rawglutinousskin"),
            new ResourceLocation("flavor_immersed_daily", "raw_tangyuan"),
            new ResourceLocation("flavor_immersed_daily", "rawflourpancake"),
            new ResourceLocation("flavor_immersed_daily", "rawpie")
    );

// 擀面杖配方：把砧板上的面团/面剂子/面皮原地擀成下一步形态（输入物品 → 输出物品）。
// 每类面团擀成对应面饼、每类面剂子擀成对应面皮；小麦系面皮可继续层层擀薄
// （生面饼 → 包子皮 → 饺子皮 → 馄饨皮）。原地替换食材槽，不改动槽位结构。
    public static final Map<ResourceLocation, ResourceLocation> ROLLING_PIN_MAP = buildRollingPinMap();

    private static Map<ResourceLocation, ResourceLocation> buildRollingPinMap() {
        Map<ResourceLocation, ResourceLocation> map = new LinkedHashMap<>();
// 面团 → 面饼
        rollingPinEntry(map, "dough", "rawflourpancake");
        rollingPinEntry(map, "eggdough", "raweggpancake");
        rollingPinEntry(map, "rawglutinousdough", "rawglutinousriceflourpancake");
// 面剂子 → 面皮
        rollingPinEntry(map, "flourpaste", "rawflourpancake");
        rollingPinEntry(map, "raweggpaste", "raweggskin");
        rollingPinEntry(map, "rawglutinouspaste", "rawglutinousskin");
// 面皮 → 层层擀薄（小麦系）
        rollingPinEntry(map, "rawflourpancake", "baozi_skin");
        rollingPinEntry(map, "baozi_skin", "dumpling_skin");
        rollingPinEntry(map, "dumpling_skin", "flourskin");
        return Map.copyOf(map);
    }

    private static void rollingPinEntry(Map<ResourceLocation, ResourceLocation> map, String in, String out) {
        map.put(new ResourceLocation("flavor_immersed_daily", in),
                new ResourceLocation("flavor_immersed_daily", out));
    }

    private final ItemStackHandler inventory = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }
    };

// 屠宰进度：当前食材已被砍下的刀数（写入 NBT 并随方块更新同步到客户端）。
    private int chopHits = 0;

    public ChoppingboardBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHOPPINGBOARD.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ItemStack getStoredItem() {
        return inventory.getStackInSlot(SLOT_INGREDIENT);
    }

    public ItemStack getSkinItem() {
        return inventory.getStackInSlot(SLOT_SKIN);
    }

    public ItemStack getFillingItem() {
        return inventory.getStackInSlot(SLOT_FILLING);
    }

    public boolean isEmpty() {
        return inventory.getStackInSlot(SLOT_INGREDIENT).isEmpty()
                && inventory.getStackInSlot(SLOT_SKIN).isEmpty()
                && inventory.getStackInSlot(SLOT_FILLING).isEmpty();
    }

// 是否为砧板可放的皮之一
    public static boolean isSkinItem(ItemStack stack) {
        if (stack.isEmpty()) return false;
        ResourceLocation id = stack.getItem().builtInRegistryHolder().key().location();
        return SKIN_IDS.contains(id);
    }

// 放一张皮到皮槽（只放 1 个），放不进返回原栈
    public ItemStack addSkin(ItemStack stack) {
        if (stack.isEmpty()) return stack;
        return inventory.insertItem(SLOT_SKIN, stack.copyWithCount(1), false);
    }

// 放 1 个馅料到馅料槽，放不进返回原栈
    public ItemStack addFilling(ItemStack stack) {
        if (stack.isEmpty()) return stack;
        return inventory.insertItem(SLOT_FILLING, stack.copyWithCount(1), false);
    }

    public ItemStack removeSkin() {
        return inventory.extractItem(SLOT_SKIN, 64, false);
    }

    public ItemStack removeFilling() {
        return inventory.extractItem(SLOT_FILLING, 64, false);
    }

// 皮是否能在砧板上包裹出对应生食（芦苇叶暂无对应生食）
    @Nullable
    public ResourceLocation getRawFoodForSkin(ItemStack skin) {
        if (skin.isEmpty()) return null;
        ResourceLocation id = skin.getItem().builtInRegistryHolder().key().location();
        return RAW_FOOD_MAP.get(id);
    }

// 皮 + 馅料齐全且该皮可包裹
    public boolean canWrap() {
        return !getSkinItem().isEmpty()
                && !getFillingItem().isEmpty()
                && getRawFoodForSkin(getSkinItem()) != null;
    }

// 包裹工序：把皮和馅料包成对应的生食掉落物。
// 生食的 NBT 里写入 filling 标签（值为馅料物品注册名），然后清空皮槽和馅料槽。
    public boolean wrap() {
        if (level == null || level.isClientSide || !canWrap()) return false;
        ResourceLocation rawId = getRawFoodForSkin(getSkinItem());
        if (rawId == null) return false;
        ItemStack raw = new ItemStack(BuiltInRegistries.ITEM.get(rawId));
        if (raw.isEmpty()) return false;

// 写入 filling 标签
        raw.getOrCreateTag().putString("filling",
                getFillingItem().getItem().builtInRegistryHolder().key().location().toString());

// 原地生成生食掉落物
        spawnDrop(raw);

// 清空皮槽和馅料槽
        inventory.setStackInSlot(SLOT_SKIN, ItemStack.EMPTY);
        inventory.setStackInSlot(SLOT_FILLING, ItemStack.EMPTY);

        level.playSound(null, worldPosition, SoundEvents.UI_STONECUTTER_TAKE_RESULT,
                SoundSource.BLOCKS, 1.0F, 1.0F);
        return true;
    }

// 是否为月饼模具
    public static boolean isMooncakeMold(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return MOONCAKE_MOLD_ID.equals(stack.getItem().builtInRegistryHolder().key().location());
    }

// 是否为糯米（粽子最后一步材料）
    public static boolean isRice(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return RICE_ID.equals(stack.getItem().builtInRegistryHolder().key().location());
    }

// 是否为洗净的芦苇叶
    public static boolean isReedLeaf(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return REED_LEAF_ID.equals(stack.getItem().builtInRegistryHolder().key().location());
    }

// 砧板上放的是生馅饼（月饼前置工序）
    public boolean canMakeMooncake() {
        return !getStoredItem().isEmpty()
                && RAW_PIE_ID.equals(getStoredItem().getItem().builtInRegistryHolder().key().location());
    }

// 芦苇叶 + 馅料齐全（粽子前置工序，等待放糯米）
    public boolean canMakeZongzi() {
        return !getSkinItem().isEmpty()
                && !getFillingItem().isEmpty()
                && REED_LEAF_ID.equals(getSkinItem().getItem().builtInRegistryHolder().key().location());
    }

// 用月饼模具把生馅饼压成生月饼，继承馅饼的 NBT
    public boolean makeMooncake() {
        if (level == null || level.isClientSide || !canMakeMooncake()) return false;
        ItemStack mooncake = new ItemStack(BuiltInRegistries.ITEM.get(RAW_MOONCAKE_ID));
        if (mooncake.isEmpty()) return false;
// 继承生馅饼的 NBT（filling 标签）
        CompoundTag tag = getStoredItem().getTag();
        if (tag != null) mooncake.setTag(tag.copy());
        spawnDrop(mooncake);
        inventory.setStackInSlot(SLOT_INGREDIENT, ItemStack.EMPTY);
        level.playSound(null, worldPosition, SoundEvents.UI_STONECUTTER_TAKE_RESULT,
                SoundSource.BLOCKS, 1.0F, 1.0F);
        return true;
    }

// 芦苇叶 + 馅料 + 糯米 → 生粽子，NBT 写馅料
    public boolean makeZongzi() {
        if (level == null || level.isClientSide || !canMakeZongzi()) return false;
        ItemStack zongzi = new ItemStack(BuiltInRegistries.ITEM.get(RAW_ZONGZI_ID));
        if (zongzi.isEmpty()) return false;
// 写入 filling 标签
        zongzi.getOrCreateTag().putString("filling",
                getFillingItem().getItem().builtInRegistryHolder().key().location().toString());
        spawnDrop(zongzi);
// 清空芦苇叶和馅料
        inventory.setStackInSlot(SLOT_SKIN, ItemStack.EMPTY);
        inventory.setStackInSlot(SLOT_FILLING, ItemStack.EMPTY);
        level.playSound(null, worldPosition, SoundEvents.UI_STONECUTTER_TAKE_RESULT,
                SoundSource.BLOCKS, 1.0F, 1.0F);
        return true;
    }

// 需要提示玩家使用的工具/材料图标（月饼模具或糯米），无则返回空
    public ItemStack getHintItem() {
        if (canMakeMooncake()) {
            return new ItemStack(BuiltInRegistries.ITEM.get(MOONCAKE_MOLD_ID));
        }
        if (canMakeZongzi()) {
            return new ItemStack(BuiltInRegistries.ITEM.get(RICE_ID));
        }
        return ItemStack.EMPTY;
    }

    private void spawnDrop(ItemStack stack) {
        if (level == null) return;
        double x = worldPosition.getX() + 0.5;
        double y = worldPosition.getY() + 0.2;
        double z = worldPosition.getZ() + 0.5;
        ItemEntity entity = new ItemEntity(level, x, y, z, stack);
        entity.setDefaultPickUpDelay();
        level.addFreshEntity(entity);
    }

    public boolean canAddItem(ItemStack stack) {
        if (stack.isEmpty()) return false;
// 不按堆叠上限拦截：原版蛋糕等堆叠数为 1 的食材同样需要能放上砧板；
// 工具已由下方「可损坏物品」「刀具」判断排除
        if (stack.isDamageableItem()) return false;
// 刀具只作为切割工具，不作为食材放上砧板
        if (isKnife(stack)) return false;
// 擀面杖只作为擀制工具，不作为食材放上砧板
        if (isRollingPin(stack)) return false;
// 屠宰用小型生物：食材槽只放 1 个，避免堆叠时模型与刀数语义混乱
        ItemStack stored = getStoredItem();
        if (!stored.isEmpty() && ChoppingboardMobRegistry.getByInput(stored.getItem()) != null) return false;
        ItemStack result = inventory.insertItem(SLOT_INGREDIENT, stack.copy(), true);
        return result.getCount() != stack.getCount();
    }

// 放 1 个食材到食材槽，放不进返回原栈
    public ItemStack addItem(ItemStack stack) {
        if (stack.isEmpty()) return stack;
// 换上新食材时屠宰进度归零
        chopHits = 0;
        return inventory.insertItem(SLOT_INGREDIENT, stack.copyWithCount(1), false);
    }

    public ItemStack removeItem() {
// 取走食材时屠宰进度归零
        chopHits = 0;
        return inventory.extractItem(SLOT_INGREDIENT, 64, false);
    }

// 当前食材已砍下的刀数（0 表示尚未开刀）。
    public int getChopHits() {
        return chopHits;
    }

// 砧板小型生物屠宰：手持刀具多次右键，按定义中的阶段依次爆出产物。
// 放上砧板的物品若在 ChoppingboardMobRegistry 中有定义则接管本次交互：
// 校验刀具 → 掉落当前阶段的产物 → 刀耐久 -1 → 进度 +1；砍满所有阶段后消耗 1 个食材并把进度归零。
// @return 是否由屠宰流程处理了本次交互（false 时调用方继续走普通切菜逻辑）
    public boolean processChop(ItemStack toolStack, @Nullable Player player) {
        if (level == null || level.isClientSide) return false;
        ItemStack stored = getStoredItem();
        if (stored.isEmpty()) return false;

        ChoppingboardMobDefinition definition = ChoppingboardMobRegistry.getByInput(stored.getItem());
        if (definition == null) return false;
        if (!definition.toolMatches(toolStack)) return false;

        int stageIndex = Math.min(chopHits, definition.stageCount() - 1);
        ChoppingboardMobDefinition.Stage stage = definition.stages().get(stageIndex);

        for (Item drop : stage.drops()) {
            spawnDrop(new ItemStack(drop));
        }

        if (player != null) {
            toolStack.hurtAndBreak(1, player, (p) -> {});
        }

        chopHits++;
        if (chopHits >= definition.stageCount()) {
            chopHits = 0;
            inventory.extractItem(SLOT_INGREDIENT, 1, false);
        }
        setChanged();

        level.playSound(null, worldPosition, SoundEvents.WOOD_HIT,
                SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK,
                    worldPosition.getX() + 0.5, worldPosition.getY() + 0.15, worldPosition.getZ() + 0.5,
                    1, 0.0, 0.0, 0.0, 0.0);
        }
        return true;
    }

// 擀制工序：手持擀面杖右键，把砧板上的面团/面剂子/面皮原地擀成下一步形态。
// 原地替换食材槽（slot 0），不改动槽位数与 NBT 结构，旧存档完全兼容；
// 连续右键即可把 生面饼 一路擀成 包子皮 → 饺子皮 → 馄饨皮。
// @return 是否由擀制流程处理了本次交互（false 时调用方继续走其它逻辑）
    public boolean processRollingPin(ItemStack toolStack, @Nullable Player player) {
        if (level == null || level.isClientSide) return false;
        if (!isRollingPin(toolStack)) return false;

        ItemStack stored = getStoredItem();
        if (stored.isEmpty()) return false;

        ResourceLocation outputId = ROLLING_PIN_MAP.get(
                stored.getItem().builtInRegistryHolder().key().location());
        if (outputId == null) return false;

        Item outputItem = BuiltInRegistries.ITEM.get(outputId);
        if (outputItem == Items.AIR) return false;

// 原地替换为下一步形态，保留原有数量（正常情况食材槽只有 1 个）
        int count = stored.getCount();
        chopHits = 0;
        inventory.setStackInSlot(SLOT_INGREDIENT, new ItemStack(outputItem, count));
        setChanged();

        level.playSound(null, worldPosition, SoundEvents.UI_STONECUTTER_TAKE_RESULT,
                SoundSource.BLOCKS, 0.8F, 1.2F);
        return true;
    }

    public boolean processWithTool(ItemStack toolStack, @Nullable Player player) {
        if (level == null || level.isClientSide || isEmpty()) return false;
        if (!isKnife(toolStack)) return false;

        ItemStack stored = getStoredItem().copy();
        if (stored.isEmpty()) return false;

        CookingRecipeInput input = new CookingRecipeInput(List.of(stored.copy()));

// 本模组砧板配方优先：命中即不再考虑农夫乐事配方
        List<CookingRecipe> ownRecipes = ((ServerLevel) level).getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.COOKING.get());

        for (CookingRecipe recipe : ownRecipes) {
            String app = recipe.getAppliance();
            if (!app.isEmpty() && !app.equals("choppingboard")) continue;
            if (recipe.matches(input, level)) {
                doProcess(List.of(recipe.getResult().copy()), toolStack, player);
                return true;
            }
        }

// 本模组没有对应配方时，转而适配农夫乐事（含附属）的砧板切割配方
        return processFdRecipe(stored, toolStack, player);
    }

// 用农夫乐事切割配方处理：产出按概率取全部产物（与其砧板行为一致）。
    private boolean processFdRecipe(ItemStack stored, ItemStack toolStack, @Nullable Player player) {
        if (level == null) return false;

        FdCuttingCompat.Cutting cutting =
                FdCuttingCompat.findMatch(level.getRecipeManager(), stored, toolStack);
        if (cutting == null) return false;

        List<ItemStack> results = FdCuttingCompat.rollResults(cutting.recipe(), level.random);
        if (results.isEmpty()) {
            results = FdCuttingCompat.readResults(cutting.recipe(), level.registryAccess());
        }
        if (results.isEmpty()) return false;

        doProcess(results, toolStack, player);
        return true;
    }

    private void doProcess(List<ItemStack> results, ItemStack toolStack, @Nullable Player player) {
        if (level == null || results.isEmpty()) return;

        for (ItemStack result : results) {
            if (result.isEmpty()) continue;
            spawnDrop(result.copy());
        }

        inventory.extractItem(SLOT_INGREDIENT, 1, false);

        if (!level.isClientSide && player != null) {
            toolStack.hurtAndBreak(1, player, (p) -> {});
        }

        level.playSound(null, worldPosition, SoundEvents.UI_STONECUTTER_TAKE_RESULT,
                SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());
        tag.putInt("ChopHits", chopHits);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
// deserializeNBT 会按其 "Size" 重建槽位列表，需先把 Size 归一化为当前槽位数再反序列化；
// 若在其之后再调用 setSize，会把刚反序列化出来的物品整体清空（setSize 是换成全新的空列表）
        CompoundTag inventoryTag = tag.getCompound("Inventory").copy();
        inventoryTag.putInt("Size", 3);
        inventory.deserializeNBT(inventoryTag);
        chopHits = tag.getInt("ChopHits");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag t = new CompoundTag();
        saveAdditional(t);
        return t;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
}
