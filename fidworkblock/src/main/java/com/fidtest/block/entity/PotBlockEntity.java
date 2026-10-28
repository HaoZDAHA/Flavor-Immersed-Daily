package com.fidtest.block.entity;

import com.fidtest.recipe.ApplianceType;
import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.CookingRecipeInput;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlockEntities;
import com.fidtest.screen.CookingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PotBlockEntity extends AbstractCookingBlockEntity {

    private static final int INGREDIENT_SLOTS = 6;
    private static final int CONTAINER_INPUT_SLOT = 6;
    private static boolean fdChecked = false;
    private static RecipeType<?> fdCookingType = null;

    public PotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POT.get(), pos, state, ApplianceType.POT);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player p) {
        return new CookingMenu(id, inv, this);
    }

    @Nullable
    private static RecipeType<?> getFdCookingType() {
        if (!fdChecked) {
            fdChecked = true;
            try {
                fdCookingType = net.minecraft.core.registries.BuiltInRegistries.RECIPE_TYPE
                        .get(new ResourceLocation("farmersdelight", "cooking"));
            } catch (Exception ignored) {}
        }
        return fdCookingType;
    }

    @Nullable
    @Override
    protected CookingRecipe findMatchingRecipe() {
        if (level == null || level.isClientSide) return null;

        List<ItemStack> ings = new ArrayList<>();
        for (int i = 0; i < INGREDIENT_SLOTS; i++) ings.add(getStackInSlot(i));
        CookingRecipeInput input = new CookingRecipeInput(ings);
        if (input.isEmpty()) return null;

        List<CookingRecipe> own = ((ServerLevel) level).getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.COOKING.get());
        for (CookingRecipe r : own) {
            String a = r.getAppliance();
            if (!a.isEmpty() && !a.equals("pot")) continue;
            if (r.matches(input, level) && canCook(r)) return r;
        }
        return findFdRecipe(ings);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private CookingRecipe findFdRecipe(List<ItemStack> ingredientItems) {
        RecipeType<?> fdType = getFdCookingType();
        if (fdType == null) return null;

        List<ItemStack> nonEmpty = new ArrayList<>();
        for (ItemStack s : ingredientItems) if (!s.isEmpty()) nonEmpty.add(s.copy());
        if (nonEmpty.isEmpty()) return null;

        List<?> fdRecipes;
        try {
            fdRecipes = ((ServerLevel) level).getRecipeManager().getAllRecipesFor((RecipeType) fdType);
        } catch (Exception e) { return null; }

        for (Object obj : fdRecipes) {
            Recipe<?> recipe = (Recipe<?>) obj;
            try {
                if (!manualMatchIngredients(recipe, nonEmpty)) continue;
                ItemStack fdContainer = getFdOutputContainer(recipe);
                if (!fdContainer.isEmpty()) {
                    ItemStack ourC = getStackInSlot(CONTAINER_INPUT_SLOT);
                    if (ourC.isEmpty() || !ItemStack.isSameItem(ourC, fdContainer)) continue;
                }

                ItemStack fdResult = recipe.getResultItem(level.registryAccess());
                int cookTime = 200;
                try { Method m = recipe.getClass().getMethod("getCookTime"); cookTime = (int) m.invoke(recipe); } catch (Exception ignored) {}
                float exp = 0;
                try { Method m = recipe.getClass().getMethod("getExperience"); exp = (float) m.invoke(recipe); } catch (Exception ignored) {}

                ResourceLocation wrapId = new ResourceLocation("fidworkblock", "fd_compat/" + recipe.getId().getPath());
                return new CookingRecipe(wrapId, "pot", recipe.getIngredients(), fdResult,
                        fdContainer.isEmpty() ? java.util.Optional.empty() : java.util.Optional.of(fdContainer),
                        cookTime, exp, 1);
            } catch (Exception ignored) {}
        }
        return null;
    }

    private boolean manualMatchIngredients(Recipe<?> recipe, List<ItemStack> inputs) {
        List<Ingredient> ings = recipe.getIngredients();
        if (ings.isEmpty() || inputs.size() != ings.size()) return false;
        List<Ingredient> rem = new ArrayList<>(ings);
        for (ItemStack s : inputs) {
            boolean ok = false;
            for (int j = 0; j < rem.size(); j++) {
                if (rem.get(j).test(s)) { rem.remove(j); ok = true; break; }
            }
            if (!ok) return false;
        }
        return rem.isEmpty();
    }

    private ItemStack getFdOutputContainer(Recipe<?> recipe) {
        try {
            Method m = recipe.getClass().getMethod("getOutputContainer");
            return (ItemStack) m.invoke(recipe);
        } catch (Exception e) { return ItemStack.EMPTY; }
    }

    @Override
    protected void cookRecipe(CookingRecipe recipe) {
        List<Ingredient> rem = new ArrayList<>(recipe.getIngredients());
        for (int i = 0; i < INGREDIENT_SLOTS; i++) {
            ItemStack s = getStackInSlot(i);
            if (s.isEmpty()) continue;
            for (int j = 0; j < rem.size(); j++) {
                if (rem.get(j).test(s)) { s.shrink(1); rem.remove(j); break; }
            }
        }
        if (recipe.getContainer().isPresent() && !recipe.getContainer().get().isEmpty()) {
            ItemStack cs = getStackInSlot(CONTAINER_INPUT_SLOT);
            if (!cs.isEmpty()) cs.shrink(1);
        }
        ItemStack result = recipe.getResult().copy();
        ItemStack outSlot = getStackInSlot(ApplianceType.POT.getOutputSlot());
        if (outSlot.isEmpty()) setStackInSlot(ApplianceType.POT.getOutputSlot(), result);
        else outSlot.grow(result.getCount());

        if (recipe.getExperience() > 0 && level != null && !level.isClientSide) {
            ExperienceOrb.award((ServerLevel) level, getBlockPos().getCenter(), (int) recipe.getExperience());
        }
    }
}
