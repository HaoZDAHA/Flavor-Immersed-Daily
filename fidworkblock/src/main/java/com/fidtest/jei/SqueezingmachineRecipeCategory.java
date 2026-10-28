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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SqueezingmachineRecipeCategory implements IRecipeCategory<CookingRecipe> {

    public static final RecipeType<CookingRecipe> TYPE =
            RecipeType.create(FidworkMod.MODID, "squeezingmachine_cooking", CookingRecipe.class);

    private final IDrawable background, icon;
    private final IDrawableAnimated arrow;
    private final Component title;

    public SqueezingmachineRecipeCategory(IGuiHelper helper, Block iconBlock) {
        background = helper.createDrawable(new ResourceLocation(FidworkMod.MODID, "textures/gui/jei/cooking_background.png"), 0, 0, 116, 56);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(iconBlock));
        title = Component.translatable("jei.fidworkblock.category.squeezingmachine");
        arrow = helper.drawableBuilder(new ResourceLocation(FidworkMod.MODID, "textures/gui/container/pot_progress.png"), 0, 0, 24, 17).setTextureSize(24, 17).buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override public RecipeType<CookingRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return title; }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder b, CookingRecipe r, IFocusGroup f) {
        var ings = r.getIngredients();
        if (!ings.isEmpty()) b.addSlot(RecipeIngredientRole.INPUT, 27, 19).addItemStacks(Arrays.asList(ings.get(0).getItems()));
        b.addSlot(RecipeIngredientRole.OUTPUT, 95, 19).addItemStack(r.getResult());
    }

    @Override public void draw(CookingRecipe r, IRecipeSlotsView s, GuiGraphics g, double mx, double my) { arrow.draw(g, 59, 19); }
    @Override public List<Component> getTooltipStrings(CookingRecipe r, IRecipeSlotsView s, double mx, double my) { return List.of(); }
}
