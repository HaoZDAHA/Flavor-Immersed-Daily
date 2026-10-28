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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FryingpanRecipeCategory implements IRecipeCategory<CookingRecipe> {

    public static final RecipeType<CookingRecipe> TYPE =
            RecipeType.create(FidworkMod.MODID, "fryingpan_cooking", CookingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final Component title;

    public FryingpanRecipeCategory(IGuiHelper helper, Block iconBlock) {
        this.background = helper.createDrawable(
                new ResourceLocation(FidworkMod.MODID, "textures/gui/jei/cooking_background.png"), 0, 0, 116, 56);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(iconBlock));
        this.title = Component.translatable("jei.fidworkblock.category.fryingpan");
        this.arrow = helper.drawableBuilder(
                        new ResourceLocation(FidworkMod.MODID, "textures/gui/container/pot_progress.png"), 0, 0, 24, 17)
                .setTextureSize(24, 17)
                .buildAnimated(180, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override public RecipeType<CookingRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return title; }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CookingRecipe recipe, IFocusGroup focuses) {
        List<Ingredient> ings = recipe.getIngredients();
        for (int i = 0; i < Math.min(ings.size(), 2); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, 22 + i * 18 + 1, 19 + 1)
                    .addItemStacks(Arrays.asList(ings.get(i).getItems()));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 19).addItemStack(recipe.getResult());
    }

    @Override public void draw(CookingRecipe r, IRecipeSlotsView s, GuiGraphics g, double mx, double my) { arrow.draw(g, 59, 19); }

    @Override public List<Component> getTooltipStrings(CookingRecipe recipe, IRecipeSlotsView slots, double mx, double my) {
        List<Component> list = new ArrayList<>();
        if (mx >= 59 && mx <= 83 && my >= 19 && my <= 36) {
            if (recipe.getCookingTime() > 0)
                list.add(Component.translatable("gui.jei.category.smelting.time.seconds", recipe.getCookingTime() / 20));
        }
        return list;
    }
}
