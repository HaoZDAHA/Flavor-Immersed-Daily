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

public class JarRecipeCategory implements IRecipeCategory<CookingRecipe> {

    public static final RecipeType<CookingRecipe> TYPE =
            RecipeType.create(FidworkMod.MODID, "jar_cooking", CookingRecipe.class);

    private final IDrawable background, icon;
    private final IDrawableAnimated arrow;
    private final Component title;

    public JarRecipeCategory(IGuiHelper helper, Block iconBlock) {
        background = helper.createDrawable(new ResourceLocation(FidworkMod.MODID, "textures/gui/jei/cooking_background.png"), 0, 0, 116, 56);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(iconBlock));
        title = Component.translatable("jei.fidworkblock.category.jar");
        arrow = helper.drawableBuilder(new ResourceLocation(FidworkMod.MODID, "textures/gui/container/pot_progress.png"), 0, 0, 24, 17)
                .setTextureSize(24, 17).buildAnimated(400, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override public RecipeType<CookingRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return title; }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder b, CookingRecipe r, IFocusGroup f) {
        List<Ingredient> ings = r.getIngredients();
        for (int i = 0; i < Math.min(ings.size(), 4); i++) {
            int x = 13 + (i % 2) * 18, y = 10 + (i / 2) * 18;
            b.addSlot(RecipeIngredientRole.INPUT, x + 1, y + 1).addItemStacks(Arrays.asList(ings.get(i).getItems()));
        }
        b.addSlot(RecipeIngredientRole.OUTPUT, 95, 19).addItemStack(r.getResult());
    }

    @Override public void draw(CookingRecipe r, IRecipeSlotsView s, GuiGraphics g, double mx, double my) { arrow.draw(g, 59, 19); }
    @Override public List<Component> getTooltipStrings(CookingRecipe r, IRecipeSlotsView s, double mx, double my) {
        List<Component> l = new ArrayList<>();
        if (mx >= 59 && mx <= 83 && my >= 19 && my <= 36 && r.getCookingTime() > 0)
            l.add(Component.translatable("gui.jei.category.smelting.time.seconds", r.getCookingTime() / 20));
        return l;
    }
}
