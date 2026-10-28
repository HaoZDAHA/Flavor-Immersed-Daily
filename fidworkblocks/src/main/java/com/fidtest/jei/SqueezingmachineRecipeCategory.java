// 新文件：C:\Users\15244\IdeaProjects\fidworkblock\src\main\java\com\fidtest\jei\SqueezingmachineRecipeCategory.java
package com.fidtest.jei;

import com.fidtest.ExampleMod;
import com.fidtest.recipe.ApplianceType;
import com.fidtest.recipe.CookingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
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

public class SqueezingmachineRecipeCategory implements IRecipeCategory<RecipeHolder<CookingRecipe>> {

    private final RecipeType<RecipeHolder<CookingRecipe>> recipeType;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final Component title;

    public SqueezingmachineRecipeCategory(IGuiHelper helper, Block iconBlock) {
        this.recipeType = ModJeiRecipeTypes.forAppliance(ApplianceType.SQUEEZINGMACHINE);

        ResourceLocation bgLoc = ResourceLocation.fromNamespaceAndPath(
                ExampleMod.MODID, "textures/gui/jei/cooking_background.png");
        this.background = helper.createDrawable(bgLoc, 0, 0, 116, 56);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(iconBlock));
        this.title = Component.translatable("jei.fidworkblock.category.squeezingmachine");

        ResourceLocation arrowTexture = ResourceLocation.fromNamespaceAndPath(
                ExampleMod.MODID, "textures/gui/container/teapot_progress.png");
        this.arrow = helper.drawableBuilder(arrowTexture, 0, 0, 24, 17)
                .setTextureSize(24, 17)
                .buildAnimated(80, IDrawableAnimated.StartDirection.LEFT, false);
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
            builder.addSlot(RecipeIngredientRole.INPUT, 11, 20)
                    .addItemStacks(Arrays.asList(ingredients.get(0).getItems()));
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 81, 20)
                .addItemStack(recipe.getResult());
    }

    @Override
    public void draw(RecipeHolder<CookingRecipe> holder, IRecipeSlotsView slotsView,
                     GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 40, 20);
    }
}
