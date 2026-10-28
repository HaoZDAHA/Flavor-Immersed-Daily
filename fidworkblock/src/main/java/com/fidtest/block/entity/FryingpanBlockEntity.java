package com.fidtest.block.entity;

import com.fidtest.block.DryerBlock;
import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.CookingRecipeInput;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FryingpanBlockEntity extends BlockEntity {

    public static final int OIL_SLOT = 0, INGREDIENT_SLOT = 1, OUTPUT_SLOT = 2, DEFAULT_FRY_TIME = 60;
    private static final ResourceLocation OIL_ID = new ResourceLocation("flavor_immersed_daily", "cookingoil");

    private final ItemStackHandler inventory = new ItemStackHandler(3) {
        @Override protected void onContentsChanged(int s) { setChanged(); }
        @Override public int getSlotLimit(int s) { return 64; }
    };

    private boolean working;
    private int processTick, totalProcessTime = DEFAULT_FRY_TIME;
    private List<ItemStack> renderInputItems = new ArrayList<>();
    private ItemStack resultTemplate = ItemStack.EMPTY;
    private int pendingResultTotal;

    public FryingpanBlockEntity(BlockPos pos, BlockState state) { super(ModBlockEntities.FRYINGPAN.get(), pos, state); }

    public ItemStackHandler getInventory() { return inventory; }
    public boolean isWorking() { return working; }
    public int getProcessTick() { return processTick; }
    public int getTotalProcessTime() { return totalProcessTime; }
    public List<ItemStack> getRenderInputItems() { return renderInputItems; }
    public boolean hasOil() { return !inventory.getStackInSlot(OIL_SLOT).isEmpty(); }
    public boolean hasIngredient() { return !inventory.getStackInSlot(INGREDIENT_SLOT).isEmpty(); }
    public boolean hasOutput() { return !inventory.getStackInSlot(OUTPUT_SLOT).isEmpty() || pendingResultTotal > 0; }
    public int getOilCount() { return inventory.getStackInSlot(OIL_SLOT).getCount(); }
    public ItemStack getOilItem() { return inventory.getStackInSlot(OIL_SLOT); }
    public ItemStack getIngredientItem() { return inventory.getStackInSlot(INGREDIENT_SLOT); }
    public ItemStack getOutputItem() { return inventory.getStackInSlot(OUTPUT_SLOT); }

    public boolean isOilItem(ItemStack s) { return !s.isEmpty() && s.getItem().builtInRegistryHolder().key().location().equals(OIL_ID); }

    public boolean isRecipeIngredient(ItemStack stack) {
        if (level == null || level.isClientSide || stack.isEmpty()) return false;
        for (CookingRecipe r : ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get())) {
            String a = r.getAppliance();
            if (!a.isEmpty() && !a.equals("fryingpan")) continue;
            for (Ingredient ing : r.getIngredients()) if (ing.test(stack)) return true;
        }
        return false;
    }

    public ItemStack addOil(ItemStack s) { return inventory.insertItem(OIL_SLOT, s, false); }
    public ItemStack addIngredient(ItemStack s) { return inventory.insertItem(INGREDIENT_SLOT, s, false); }
    public ItemStack removeOil() { return inventory.extractItem(OIL_SLOT, 64, false); }
    public ItemStack removeIngredient() { return inventory.extractItem(INGREDIENT_SLOT, 64, false); }

    public boolean isOnDryer() {
        return level != null && level.getBlockState(worldPosition.below()).getBlock() instanceof DryerBlock;
    }

    public boolean startFrying() {
        if (level == null || level.isClientSide || working || !hasIngredient() || !hasOil() || !isOnDryer()) return false;
        int oilCount = getOilCount();
        ItemStack ingredientCopy = getIngredientItem().copy();
        int ingredientCount = ingredientCopy.getCount();
        if (oilCount < ingredientCount) return false;

        CookingRecipeInput input = new CookingRecipeInput(List.of(ingredientCopy.copy()));
        for (CookingRecipe recipe : ((ServerLevel) level).getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get())) {
            String a = recipe.getAppliance();
            if (!a.isEmpty() && !a.equals("fryingpan")) continue;
            if (!recipe.matches(input, level)) continue;

            int cost = 1;
            for (Ingredient ing : recipe.getIngredients())
                for (ItemStack test : ing.getItems())
                    if (test.getItem() == ingredientCopy.getItem()) { cost = Math.max(cost, test.getCount()); break; }

            int batches = Math.min(ingredientCount / cost, oilCount);
            if (batches <= 0) return false;

            List<ItemStack> rList = new ArrayList<>();
            ItemStack single = ingredientCopy.copy(); single.setCount(1);
            for (int i = 0; i < Math.min(batches, 5); i++) rList.add(single.copy());
            renderInputItems = rList;
            resultTemplate = recipe.getResult().copy();
            pendingResultTotal = resultTemplate.getCount() * batches;
            totalProcessTime = recipe.getCookingTime() > 0 ? recipe.getCookingTime() : DEFAULT_FRY_TIME;
            working = true; processTick = 0;
            inventory.extractItem(INGREDIENT_SLOT, batches * cost, false);
            inventory.extractItem(OIL_SLOT, batches, false);
            setChanged();
            level.playSound(null, worldPosition, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 1.0F);
            return true;
        }
        return false;
    }

    public void collectOutput(Player player) {
        if (level == null || level.isClientSide) return;
        ItemStack result;
        if (pendingResultTotal > 0) {
            int give = Math.min(pendingResultTotal, resultTemplate.getMaxStackSize());
            result = resultTemplate.copy(); result.setCount(give); pendingResultTotal -= give;
        } else {
            result = inventory.extractItem(OUTPUT_SLOT, 64, false);
        }
        if (!result.isEmpty()) {
            if (!player.isCreative()) player.getInventory().add(result);
            int remaining = getOilCount();
            player.displayClientMessage(Component.translatable(remaining > 0 ? "message.fidworkblock.fryingpan_oil_remaining" : "message.fidworkblock.fryingpan_no_oil", remaining > 0 ? remaining : 0), true);
        }
        setChanged();
    }

    public static void serverTick(FryingpanBlockEntity be) {
        if (!be.working) return;
        be.processTick++;
        if (be.level instanceof ServerLevel sl && be.processTick % 8 == 0)
            sl.sendParticles(ParticleTypes.LAVA, be.worldPosition.getX()+0.5, be.worldPosition.getY()+0.3, be.worldPosition.getZ()+0.5, 4, 0.2, 0.05, 0.2, 0.02);
        if (be.processTick % 20 == 0 && be.level != null)
            be.level.playSound(null, be.worldPosition, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.3F, 1.0F + be.level.random.nextFloat() * 0.2F);

        if (be.processTick >= be.totalProcessTime) {
            be.working = false; be.processTick = 0; be.renderInputItems.clear();
            ItemStack out = be.inventory.getStackInSlot(OUTPUT_SLOT);
            int max = be.resultTemplate.getMaxStackSize(), total = be.pendingResultTotal;
            if (!out.isEmpty() && ItemStack.isSameItem(out, be.resultTemplate)) { int add = Math.min(total, max - out.getCount()); out.grow(add); total -= add; }
            if (total > 0 && out.isEmpty()) { int put = Math.min(total, max); ItemStack ns = be.resultTemplate.copy(); ns.setCount(put); be.inventory.setStackInSlot(OUTPUT_SLOT, ns); total -= put; }
            while (total > 0) { int dc = Math.min(total, max); ItemStack ds = be.resultTemplate.copy(); ds.setCount(dc); ItemEntity e = new ItemEntity(be.level, be.worldPosition.getX()+0.5, be.worldPosition.getY()+0.5, be.worldPosition.getZ()+0.5, ds); e.setDefaultPickUpDelay(); be.level.addFreshEntity(e); total -= dc; }
            be.pendingResultTotal = 0; be.setChanged();
            if (be.level != null) be.level.playSound(null, be.worldPosition, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override public void saveAdditional(CompoundTag tag) { super.saveAdditional(tag); tag.put("Inventory", inventory.serializeNBT()); tag.putBoolean("Working", working); tag.putInt("ProcessTick", processTick); tag.putInt("TotalProcessTime", totalProcessTime); tag.putInt("PendingResultTotal", pendingResultTotal); if (!resultTemplate.isEmpty()) tag.put("ResultTemplate", resultTemplate.save(new CompoundTag())); ListTag rl = new ListTag(); for (ItemStack s : renderInputItems) { ItemStack c = s.copy(); c.setCount(1); rl.add(c.save(new CompoundTag())); } tag.put("RenderItems", rl); }
    @Override public void load(CompoundTag tag) { super.load(tag); inventory.deserializeNBT(tag.getCompound("Inventory")); working = tag.getBoolean("Working"); processTick = tag.getInt("ProcessTick"); totalProcessTime = tag.getInt("TotalProcessTime"); if (totalProcessTime <= 0) totalProcessTime = DEFAULT_FRY_TIME; pendingResultTotal = tag.getInt("PendingResultTotal"); resultTemplate = tag.contains("ResultTemplate") ? ItemStack.of(tag.getCompound("ResultTemplate")) : ItemStack.EMPTY; renderInputItems = new ArrayList<>(); ListTag rl = tag.getList("RenderItems", Tag.TAG_COMPOUND); for (int i = 0; i < rl.size(); i++) renderInputItems.add(ItemStack.of(rl.getCompound(i))); }
    @Override public CompoundTag getUpdateTag() { CompoundTag t = new CompoundTag(); saveAdditional(t); return t; }
    @Nullable @Override public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
    @Override public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) { load(pkt.getTag()); }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }
}
