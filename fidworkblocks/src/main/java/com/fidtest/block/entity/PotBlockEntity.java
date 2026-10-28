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
import net.minecraft.world.item.crafting.RecipeHolder;
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

    public PotBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.POT.get(), pos, blockState, ApplianceType.POT);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new CookingMenu(id, inv, this);
    }

    @Nullable
    private static RecipeType<?> getFdCookingType() {
        if (!fdChecked) {
            fdChecked = true;
            try {
                ResourceLocation id = ResourceLocation.fromNamespaceAndPath("farmersdelight", "cooking");
                var type = net.minecraft.core.registries.BuiltInRegistries.RECIPE_TYPE.get(id);
                if (type != null) {
                    fdCookingType = type;
                }
            } catch (Exception ignored) {}
        }
        return fdCookingType;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    protected RecipeHolder<CookingRecipe> findMatchingRecipe() {
        if (getLevel() == null || getLevel().isClientSide) return null;

        List<ItemStack> ingredientItems = new ArrayList<>();
        for (int i = 0; i < INGREDIENT_SLOTS; i++) {
            ingredientItems.add(getStackInSlot(i));
        }

        CookingRecipeInput input = new CookingRecipeInput(ingredientItems);
        if (input.isEmpty()) return null;

// 1. 优先匹配自己的配方（只用槽 0-5）
        List<RecipeHolder<CookingRecipe>> ownRecipes =
                ((ServerLevel) getLevel()).getRecipeManager()
                        .getAllRecipesFor(ModRecipeTypes.COOKING.get());

        for (RecipeHolder<CookingRecipe> holder : ownRecipes) {
            CookingRecipe recipe = holder.value();
            String app = recipe.getAppliance();
            if (!app.isEmpty() && !app.equals("pot")) continue;
            if (recipe.matches(input, getLevel()) && canCook(recipe)) {
                return holder;
            }
        }

// 2. 回退匹配 FD 配方
        return findFdRecipe(ingredientItems);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private RecipeHolder<CookingRecipe> findFdRecipe(List<ItemStack> ingredientItems) {
        RecipeType<?> fdType = getFdCookingType();
        if (fdType == null) return null;

        List<ItemStack> nonEmpty = new ArrayList<>();
        for (ItemStack s : ingredientItems) {
            if (!s.isEmpty()) nonEmpty.add(s.copy());
        }
        if (nonEmpty.isEmpty()) return null;

        List<RecipeHolder<?>> fdRecipes;
        try {
            fdRecipes = ((ServerLevel) getLevel()).getRecipeManager().getAllRecipesFor((RecipeType) fdType);
        } catch (Exception e) {
            return null;
        }

        for (RecipeHolder<?> holder : fdRecipes) {
            Recipe<?> recipe = holder.value();
            try {
                if (!manualMatchIngredients(recipe, nonEmpty)) {
                    continue;
                }

                ItemStack fdContainer = getFdOutputContainer(recipe);
                if (!fdContainer.isEmpty()) {
                    ItemStack ourContainer = getStackInSlot(CONTAINER_INPUT_SLOT);
                    if (ourContainer.isEmpty() || !ItemStack.isSameItem(ourContainer, fdContainer)) {
                        continue;
                    }
                }

                ItemStack fdResult = recipe.getResultItem(getLevel().registryAccess());
                int fdCookTime = 200;
                try {
                    Method cookTimeMethod = recipe.getClass().getMethod("getCookTime");
                    fdCookTime = (int) cookTimeMethod.invoke(recipe);
                } catch (Exception ignored) {}
                float fdExp = 0;
                try {
                    Method expMethod = recipe.getClass().getMethod("getExperience");
                    fdExp = (float) expMethod.invoke(recipe);
                } catch (Exception ignored) {}

                CookingRecipe wrapped = new CookingRecipe(
                        "pot",
                        recipe.getIngredients(),
                        fdResult,
                        fdContainer.isEmpty() ? java.util.Optional.empty() : java.util.Optional.of(fdContainer),
                        fdCookTime,
                        fdExp,
                        1
                );

                return new RecipeHolder<>(holder.id(), wrapped);
            } catch (Exception ignored) {}
        }
        return null;
    }

    private boolean manualMatchIngredients(Recipe<?> recipe, List<ItemStack> inputs) {
        List<Ingredient> ingredients = recipe.getIngredients();
        if (ingredients.isEmpty()) return false;
        if (inputs.size() != ingredients.size()) return false;

        List<Ingredient> remaining = new ArrayList<>(ingredients);
        for (ItemStack stack : inputs) {
            boolean matched = false;
            for (int j = 0; j < remaining.size(); j++) {
                if (remaining.get(j).test(stack)) {
                    remaining.remove(j);
                    matched = true;
                    break;
                }
            }
            if (!matched) return false;
        }
        return remaining.isEmpty();
    }

    private ItemStack getFdOutputContainer(Recipe<?> recipe) {
        try {
            Method m = recipe.getClass().getMethod("getOutputContainer");
            return (ItemStack) m.invoke(recipe);
        } catch (Exception e) {
            return ItemStack.EMPTY;
        }
    }

    @Override
    protected void cookRecipe(CookingRecipe recipe) {
// 先复制一份输入（含 NBT），供产物继承 filling——必须在消耗输入之前
        List<ItemStack> inputItems = new ArrayList<>();
        for (int i = 0; i < INGREDIENT_SLOTS; i++) inputItems.add(getStackInSlot(i).copy());

        List<Ingredient> remaining = new ArrayList<>(recipe.getIngredients());
        for (int i = 0; i < INGREDIENT_SLOTS; i++) {
            ItemStack s = getStackInSlot(i);
            if (s.isEmpty()) continue;
            for (int j = 0; j < remaining.size(); j++) {
                if (remaining.get(j).test(s)) {
                    s.shrink(1);
                    remaining.remove(j);
                    break;
                }
            }
        }

        if (recipe.getContainer().isPresent() && !recipe.getContainer().get().isEmpty()) {
            ItemStack containerSlot = getStackInSlot(CONTAINER_INPUT_SLOT);
            if (!containerSlot.isEmpty()) {
                containerSlot.shrink(1);
            }
        }

        ItemStack result = recipe.getCookedResult(inputItems);
        ItemStack outSlot = getStackInSlot(ApplianceType.POT.getOutputSlot());
        if (outSlot.isEmpty()) {
            setStackInSlot(ApplianceType.POT.getOutputSlot(), result);
        } else {
            outSlot.grow(result.getCount());
        }

        if (recipe.getExperience() > 0 && getLevel() != null && !getLevel().isClientSide) {
            ExperienceOrb.award((ServerLevel) getLevel(), getBlockPos().getCenter(), (int) recipe.getExperience());
        }
    }
}
