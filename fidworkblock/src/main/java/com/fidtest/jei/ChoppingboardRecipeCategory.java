package com.fidtest.jei;

import com.fidtest.FidworkMod;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.List;

public class ChoppingboardRecipeCategory implements IRecipeCategory<CookingRecipe> {

    public static final RecipeType<CookingRecipe> TYPE =
            RecipeType.create(FidworkMod.MODID, "choppingboard_cooking", CookingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final Component title;

    public ChoppingboardRecipeCategory(IGuiHelper helper, Block iconBlock) {
        this.background = helper.createDrawable(
                new ResourceLocation(FidworkMod.MODID, "textures/gui/jei/cooking_background.png"), 0, 0, 116, 56);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(iconBlock));
        this.title = Component.translatable("jei.fidworkblock.category.choppingboard");
        this.arrow = helper.drawableBuilder(
                new ResourceLocation(FidworkMod.MODID, "textures/gui/container/pot_progress.png"), 0, 0, 24, 17)
                .setTextureSize(24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override public RecipeType<CookingRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return title; }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CookingRecipe recipe, IFocusGroup focuses) {
        var ings = recipe.getIngredients();
        if (!ings.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 27, 19)
                    .addItemStacks(Arrays.asList(ings.get(0).getItems()));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 19)
                .addItemStack(recipe.getResult());
// 农夫乐事切割配方的额外产物（多产物）
        var extras = recipe.getExtraResults();
        for (int i = 1; i < extras.size(); i++) {
            int col = (i - 1) % 2;
            int row = (i - 1) / 2;
            builder.addSlot(RecipeIngredientRole.OUTPUT, 95 + col * 18, 37 + row * 18)
                    .addItemStack(extras.get(i));
        }
    }

    @Override
    public void draw(CookingRecipe recipe, IRecipeSlotsView slots, GuiGraphics g, double mx, double my) {
        arrow.draw(g, 59, 19);
    }
    @Override public List<Component> getTooltipStrings(CookingRecipe r, IRecipeSlotsView s, double mx, double my) { return List.of(); }

}
