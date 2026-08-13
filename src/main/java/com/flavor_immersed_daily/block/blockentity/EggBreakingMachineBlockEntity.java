package com.flavor_immersed_daily.block.blockentity;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.block.block.machine.EggBreakingMachineBlock;
import com.flavor_immersed_daily.recipe.EggBreakingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EggBreakingMachineBlockEntity extends BlockEntity {

    private int progress;
    private int totalTime;
    private final List<ItemStack> processingResults = new ArrayList<>();

    /** 内部9格库存 */
    private final SimpleContainer inventory = new SimpleContainer(9) {
        @Override
        public void setChanged() {
            super.setChanged();
            EggBreakingMachineBlockEntity.this.setChanged();
        }
    };

    public EggBreakingMachineBlockEntity(BlockPos pos, BlockState state) {
        super(com.flavor_immersed_daily.all.ModBlockEntities.EGG_BREAKING_MACHINE_ENTITY.get(), pos, state);
    }

    public SimpleContainer getInventory() {
        return inventory;
    }

    /**
     * 计算库存中能凑出多少组完整的配方
     */
    public int countCompleteSets(EggBreakingRecipe recipe) {
        List<Ingredient> ingredients = recipe.getIngredientList();
        if (ingredients.isEmpty()) return 0;

        if (recipe.isShaped()) {
            // 有序配方：每个格子对应一个原料位置
            int minSets = Integer.MAX_VALUE;
            for (int i = 0; i < ingredients.size() && i < inventory.getContainerSize(); i++) {
                Ingredient ing = ingredients.get(i);
                if (ing.isEmpty()) continue;
                ItemStack stack = inventory.getItem(i);
                if (!stack.isEmpty() && ing.test(stack)) {
                    minSets = Math.min(minSets, stack.getCount());
                } else {
                    return 0; // 该格没有匹配物品，无法制作任何一组
                }
            }
            return minSets == Integer.MAX_VALUE ? 0 : minSets;
        } else {
            // 无序配方：原料可以在任意格子
            int minSets = Integer.MAX_VALUE;
            for (Ingredient ing : ingredients) {
                if (ing.isEmpty()) continue;
                int count = 0;
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    ItemStack stack = inventory.getItem(i);
                    if (!stack.isEmpty() && ing.test(stack)) {
                        count += stack.getCount();
                    }
                }
                minSets = Math.min(minSets, count);
            }
            return minSets == Integer.MAX_VALUE ? 0 : minSets;
        }
    }

    /**
     * 从库存中消耗指定组数的原料
     */
    public void consumeSets(EggBreakingRecipe recipe, int sets) {
        List<Ingredient> ingredients = recipe.getIngredientList();

        if (recipe.isShaped()) {
            for (int i = 0; i < ingredients.size() && i < inventory.getContainerSize(); i++) {
                Ingredient ing = ingredients.get(i);
                if (ing.isEmpty()) continue;
                inventory.removeItem(i, sets);
            }
        } else {
            for (Ingredient ing : ingredients) {
                if (ing.isEmpty()) continue;
                int remaining = sets;
                for (int i = 0; i < inventory.getContainerSize() && remaining > 0; i++) {
                    ItemStack stack = inventory.getItem(i);
                    if (!stack.isEmpty() && ing.test(stack)) {
                        int consumed = Math.min(stack.getCount(), remaining);
                        stack.shrink(consumed);
                        remaining -= consumed;
                        if (stack.isEmpty()) {
                            inventory.setItem(i, ItemStack.EMPTY);
                        }
                    }
                }
            }
        }

        inventory.setChanged();
        setChanged();
    }

    /**
     * 开始加工，一次性处理 sets 组
     */
    public void startProcessing(EggBreakingRecipe recipe, int sets) {
        if (level == null || level.isClientSide() || recipe == null || sets <= 0) return;

        processingResults.clear();
        for (ItemStack r : recipe.getResults()) {
            int total = r.getCount() * sets;
            int maxSize = r.getMaxStackSize();
            int remaining = total;
            while (remaining > 0) {
                int count = Math.min(remaining, maxSize);
                ItemStack stack = r.copy();
                stack.setCount(count);
                processingResults.add(stack);
                remaining -= count;
            }
        }
        totalTime = recipe.getCookingTime();
        progress = 0;

        level.setBlock(worldPosition, getBlockState().setValue(EggBreakingMachineBlock.STAGE, 1), 3);
        setChanged();
    }

    // ---- Ticks ----

    public static void serverTick(Level level, BlockPos pos, BlockState state, EggBreakingMachineBlockEntity be) {
        if (be.totalTime > 0) {
            be.progress++;

            // 每 5 tick 播放搅拌声
            if (be.progress % 5 == 0) {
                level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.5f, 1.0f);
            }

            if (be.progress >= be.totalTime) {
                be.spawnResults(level, pos);
                be.progress = 0;
                be.totalTime = 0;
                level.setBlock(pos, state.setValue(EggBreakingMachineBlock.STAGE, 0), 3);
            }
            be.setChanged();
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, EggBreakingMachineBlockEntity be) {
        if (state.getValue(EggBreakingMachineBlock.STAGE) == 1) {
            // 每 tick 产生 2-3 个白色粒子模拟搅拌/打蛋效果
            int count = 2 + level.random.nextInt(2);
            for (int i = 0; i < count; i++) {
                double x = pos.getX() + 0.2 + level.random.nextDouble() * 0.6;
                double y = pos.getY() + 0.3 + level.random.nextDouble() * 0.6;
                double z = pos.getZ() + 0.2 + level.random.nextDouble() * 0.6;
                level.addParticle(
                        net.minecraft.core.particles.ParticleTypes.WHITE_ASH,
                        x, y, z,
                        0, 0.05 + level.random.nextDouble() * 0.05, 0
                );
            }
        }
    }

    // ---- 产出 ----

    private void spawnResults(Level level, BlockPos pos) {
        for (ItemStack stack : processingResults) {
            if (!stack.isEmpty()) {
                ItemEntity entity = new ItemEntity(level,
                        pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                        stack.copy());
                entity.setDeltaMovement(0, 0.1, 0);
                level.addFreshEntity(entity);
            }
        }
        processingResults.clear();
    }

    /**
     * 掉落所有物品（加工结果 + 库存）
     */
    public void dropAllItems(Level level, BlockPos pos) {
        // 加工结果
        for (ItemStack stack : processingResults) {
            if (!stack.isEmpty()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack.copy());
            }
        }
        processingResults.clear();

        // 库存物品
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack.copy());
            }
        }
        inventory.clearContent();
    }

    // ---- NBT ----

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("progress", progress);
        tag.putInt("totalTime", totalTime);

        // 保存库存
        CompoundTag invTag = new CompoundTag();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                invTag.put("slot" + i, stack.save(registries));
            }
        }
        tag.put("inventory", invTag);

        // 保存加工结果
        if (!processingResults.isEmpty()) {
            CompoundTag resultsTag = new CompoundTag();
            for (int i = 0; i < processingResults.size(); i++) {
                resultsTag.put("item" + i, processingResults.get(i).save(registries));
            }
            resultsTag.putInt("count", processingResults.size());
            tag.put("processingResults", resultsTag);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        progress = tag.getInt("progress");
        totalTime = tag.getInt("totalTime");

        // 加载库存
        inventory.clearContent();
        if (tag.contains("inventory")) {
            CompoundTag invTag = tag.getCompound("inventory");
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                final int slotIndex = i;
                if (invTag.contains("slot" + i)) {
                    ItemStack.parse(registries, invTag.getCompound("slot" + i))
                            .ifPresent(stack -> inventory.setItem(slotIndex, stack));
                }
            }
        }

        // 加载加工结果
        processingResults.clear();
        if (tag.contains("processingResults")) {
            CompoundTag resultsTag = tag.getCompound("processingResults");
            int count = resultsTag.getInt("count");
            for (int i = 0; i < count; i++) {
                ItemStack.parse(registries, resultsTag.getCompound("item" + i))
                        .ifPresent(processingResults::add);
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}