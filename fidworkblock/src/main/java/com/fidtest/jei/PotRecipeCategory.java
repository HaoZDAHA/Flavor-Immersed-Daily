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
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PotRecipeCategory implements IRecipeCategory<CookingRecipe> {

    public static final RecipeType<CookingRecipe> TYPE =
            RecipeType.create(FidworkMod.MODID, "pot_cooking", CookingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final Component title;

    public PotRecipeCategory(IGuiHelper helper, Block iconBlock) {
        ResourceLocation bgLoc = new ResourceLocation(FidworkMod.MODID, "textures/gui/jei/cooking_background.png");
        this.background = helper.createDrawable(bgLoc, 0, 0, 116, 56);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(iconBlock));
        this.title = Component.translatable("jei.fidworkblock.category.pot");

        ResourceLocation arrowTex = new ResourceLocation(FidworkMod.MODID, "textures/gui/container/pot_progress.png");
        this.arrow = helper.drawableBuilder(arrowTex, 0, 0, 24, 17)
                .setTextureSize(24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override public RecipeType<CookingRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return title; }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CookingRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        int n = ingredients.size();
        for (int i = 0; i < Math.min(n, 6); i++) {
            int row = i / 3;
            int col = i % 3;
            int x = 13 + col * 18;
            int y = 10 + row * 18;
            builder.addSlot(RecipeIngredientRole.INPUT, x + 1, y + 1)
                    .addItemStacks(Arrays.asList(ingredients.get(i).getItems()));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 19)
                .addItemStack(recipe.getResult());
        recipe.getContainer().ifPresent(c ->
                builder.addSlot(RecipeIngredientRole.CATALYST, 73, 37).addItemStack(c));
    }

    @Override
    public void draw(CookingRecipe recipe, IRecipeSlotsView slotsView,
                     GuiGraphics g, double mouseX, double mouseY) {
        arrow.draw(g, 59, 19);
    }

    @Override
    public List<Component> getTooltipStrings(CookingRecipe recipe, IRecipeSlotsView slotsView,
                                             double mouseX, double mouseY) {
        List<Component> list = new ArrayList<>();
        if (mouseX >= 59 && mouseX <= 83 && mouseY >= 19 && mouseY <= 36) {
            if (recipe.getCookingTime() > 0) {
                list.add(Component.translatable("gui.jei.category.smelting.time.seconds", recipe.getCookingTime() / 20));
            }
            if (recipe.getExperience() > 0) {
                list.add(Component.translatable("gui.jei.category.smelting.experience", recipe.getExperience()));
            }
        }
        return list;
    }
}
