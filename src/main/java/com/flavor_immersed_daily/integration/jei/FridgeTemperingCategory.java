package com.flavor_immersed_daily.integration.jei;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.recipe.FridgeTemperingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class FridgeTemperingCategory implements IRecipeCategory<FridgeTemperingRecipe> {

    public static final RecipeType<FridgeTemperingRecipe> TYPE =
            RecipeType.create(FlavorImmersedDaily.MODID, "fridge_tempering", FridgeTemperingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public FridgeTemperingCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(120, 36);
        this.icon = helper.createDrawableItemStack(new ItemStack(ModBlocks.FRIDGE.asItem()));
    }

    @Override
    public RecipeType<FridgeTemperingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei." + FlavorImmersedDaily.MODID + ".fridge_tempering");
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FridgeTemperingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 10)
                .addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 83, 10)
                .addItemStack(recipe.getResult());
    }

    @Override
    public void draw(FridgeTemperingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
                     double mouseX, double mouseY) {
        guiGraphics.drawString(Minecraft.getInstance().font, "→", 46, 14, 0xFF808080, false);
    }
}
