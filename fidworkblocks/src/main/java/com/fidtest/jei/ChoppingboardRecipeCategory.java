package com.fidtest.jei;

import com.fidtest.ExampleMod;
import com.fidtest.recipe.ApplianceType;
import com.fidtest.recipe.CookingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.List;

public class ChoppingboardRecipeCategory implements IRecipeCategory<RecipeHolder<CookingRecipe>> {

    private final RecipeType<RecipeHolder<CookingRecipe>> recipeType;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final Component title;
    private final List<ItemStack> toolStacks;

    public ChoppingboardRecipeCategory(IGuiHelper helper, Block iconBlock, List<ItemStack> toolStacks) {
        this.recipeType = ModJeiRecipeTypes.forAppliance(ApplianceType.CHOPPINGBOARD);
        this.toolStacks = toolStacks;

        ResourceLocation bgLoc = ResourceLocation.fromNamespaceAndPath(
                ExampleMod.MODID, "textures/gui/jei/cooking_background.png");
        this.background = helper.createDrawable(bgLoc, 0, 0, 116, 56);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(iconBlock));
        this.title = Component.translatable("jei.fidworkblock.category.choppingboard");

        ResourceLocation arrowTexture = ResourceLocation.fromNamespaceAndPath(
                ExampleMod.MODID, "textures/gui/container/teapot_progress.png");
        this.arrow = helper.drawableBuilder(arrowTexture, 0, 0, 24, 17)
                .setTextureSize(24, 17)
                .buildAnimated(20, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<RecipeHolder<CookingRecipe>> getRecipeType() {
        return recipeType;
    }

    @Override
    public Component getTitle() { return title; }

    @Override
    public IDrawable getBackground() { return background; }

    @Override
    public int getWidth() { return 116; }

    @Override
    public int getHeight() { return 56; }

    @Override
    public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CookingRecipe> holder,
                          IFocusGroup focuses) {
        CookingRecipe recipe = holder.value();
        NonNullList<Ingredient> ingredients = recipe.getIngredients();

        if (!ingredients.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 11, 11)
                    .addItemStacks(Arrays.asList(ingredients.get(0).getItems()));
        }

// 刀具：本模组菜刀 + 农夫乐事刀具标签内的全部刀具（含附属模组）
        if (!toolStacks.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.CATALYST, 11, 35).addItemStacks(toolStacks);
        } else {
            builder.addSlot(RecipeIngredientRole.CATALYST, 11, 35);
        }

// 产物：单个居中展示；农夫乐事切割配方可能有多个产物，按 2 列排布
        List<ItemStack> results = recipe.getDisplayResults();
        if (results.size() == 1) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 81, 20).addItemStack(results.get(0));
        } else {
            for (int i = 0; i < results.size(); i++) {
                int col = i % 2;
                int row = i / 2;
                builder.addSlot(RecipeIngredientRole.OUTPUT, 78 + col * 18, 2 + row * 18)
                        .addItemStack(results.get(i));
            }
        }
    }

    @Override
    public void draw(RecipeHolder<CookingRecipe> holder, IRecipeSlotsView slotsView,
                     GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 40, 20);
    }
}
