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

public class CookingRecipeCategory implements IRecipeCategory<RecipeHolder<CookingRecipe>> {

    private static final int[][] INPUT_START = {
            {40, 10}, {26, 0}, {22, 10}, {13, 10}, {13, 1}, {4, 10}, {1, 1}
    };
    private static final int[] INPUT_COLS = {1, 1, 2, 3, 2, 3, 3};

    private final ApplianceType appliance;
    private final RecipeType<RecipeHolder<CookingRecipe>> recipeType;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final Component title;

    @SuppressWarnings("unchecked")
    public CookingRecipeCategory(IGuiHelper helper, ApplianceType appliance, Block iconBlock) {
        this(helper, appliance, iconBlock, "");
    }

    @SuppressWarnings("unchecked")
    public CookingRecipeCategory(IGuiHelper helper, ApplianceType appliance, Block iconBlock, String mode) {
        this.appliance = appliance;
        this.recipeType = appliance == ApplianceType.DRYER && !mode.isEmpty()
                ? ModJeiRecipeTypes.forDryerMode(mode)
                : ModJeiRecipeTypes.forAppliance(appliance);

        ResourceLocation bgLoc = ResourceLocation.fromNamespaceAndPath(
                ExampleMod.MODID, "textures/gui/jei/cooking_background.png");
        this.background = helper.createDrawable(bgLoc, 0, 0, 116, 56);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(iconBlock));
        this.title = appliance == ApplianceType.DRYER && "oven".equals(mode)
                ? Component.translatable("jei.fidworkblock.category.dryer_oven")
                : Component.translatable("jei.fidworkblock.category." + appliance.getName());

        ResourceLocation arrowTexture = ResourceLocation.fromNamespaceAndPath(
                ExampleMod.MODID, "textures/gui/container/" + getProgressTexture(appliance) + ".png");
        this.arrow = helper.drawableBuilder(arrowTexture, 0, 0, 24, 17)
                .setTextureSize(24, 17)
                .buildAnimated(appliance.getDefaultCookingTime() > 0 ? appliance.getDefaultCookingTime() : 200,
                        IDrawableAnimated.StartDirection.LEFT, false);
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
        int count = Math.min(ingredients.size(), 6);
        int cols = INPUT_COLS[count];

        for (int i = 0; i < count; i++) {
            int row = i / cols;
            int col = i % cols;
            int x = INPUT_START[count][0] + col * 18;
            int y = INPUT_START[count][1] + row * 18;
            builder.addSlot(RecipeIngredientRole.INPUT, x + 1, y + 1)
                    .addItemStacks(Arrays.asList(ingredients.get(i).getItems()));
        }

// dryer 输出槽特殊位置
        int outX = (appliance == ApplianceType.DRYER) ? 80 : 95;
        int outY = (appliance == ApplianceType.DRYER) ? 6 : 19;
        builder.addSlot(RecipeIngredientRole.OUTPUT, outX, outY)
                .addItemStack(recipe.getResult());

        recipe.getContainer().ifPresent(c ->
                builder.addSlot(RecipeIngredientRole.CATALYST, 73, 37)
                        .addItemStack(c));
    }

    private static String getProgressTexture(ApplianceType appliance) {
        return switch (appliance) {
            case DRYER -> "dryer_progress";
            case POT -> "pot_progress";
            case PRESSURECOOKER -> "pressurecooker_progress";
            default -> "teapot_progress";
        };
    }

    @Override
    public void draw(RecipeHolder<CookingRecipe> holder, IRecipeSlotsView slotsView,
                     GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int arrowX = (appliance == ApplianceType.DRYER) ? 49 : 59;
        int arrowY = (appliance == ApplianceType.DRYER) ? 4 : 19;
        arrow.draw(guiGraphics, arrowX, arrowY);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<CookingRecipe> holder,
                           IRecipeSlotsView slotsView, double mouseX, double mouseY) {
        CookingRecipe recipe = holder.value();
        int arrowX = (appliance == ApplianceType.DRYER) ? 49 : 59;
        int arrowY = (appliance == ApplianceType.DRYER) ? 4 : 19;
        if (mouseX >= arrowX && mouseX <= arrowX + 24
                && mouseY >= arrowY && mouseY <= arrowY + 17) {
            int time = recipe.getCookingTime();
            if (time > 0) {
                tooltip.add(Component.translatable("gui.jei.category.smelting.time.seconds", time / 20));
            }
            if (recipe.getExperience() > 0) {
                tooltip.add(Component.translatable("gui.jei.category.smelting.experience", recipe.getExperience()));
            }
        }
    }
}
