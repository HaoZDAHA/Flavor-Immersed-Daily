package com.flavor_immersed_daily.integration.jei;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.recipe.EggBreakingRecipe;
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

public class EggBreakingMachineCategory implements IRecipeCategory<EggBreakingRecipe> {

    public static final RecipeType<EggBreakingRecipe> TYPE =
            RecipeType.create(FlavorImmersedDaily.MODID, "egg_breaking", EggBreakingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public EggBreakingMachineCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(120, 72);
        this.icon = helper.createDrawableItemStack(new ItemStack(ModBlocks.EGGBREAKINGMACHINE.asItem()));
    }

    @Override
    public RecipeType<EggBreakingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei." + FlavorImmersedDaily.MODID + ".egg_breaking");
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
    public void setRecipe(IRecipeLayoutBuilder builder, EggBreakingRecipe recipe, IFocusGroup focuses) {
        // 3x3 输入网格
        var ingredients = recipe.getIngredients();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int idx = col + row * 3;
                int x = 1 + col * 18;
                int y = 1 + row * 18;
                if (idx < ingredients.size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                            .addIngredients(ingredients.get(idx));
                }
            }
        }

        // 输出产物（最多2个）
        var results = recipe.getResults();
        for (int i = 0; i < results.size(); i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 83 + i * 18, 19)
                    .addItemStack(results.get(i));
        }
    }

    @Override
    public void draw(EggBreakingRecipe recipe, IRecipeSlotsView slots, GuiGraphics guiGraphics,
                     double mouseX, double mouseY) {
        var font = Minecraft.getInstance().font;
        guiGraphics.drawString(font, "→", 64, 28, 0xFF808080, false);
        if (recipe.isShaped()) {
            guiGraphics.drawString(font, Component.translatable("jei." + FlavorImmersedDaily.MODID + ".shaped"),
                    1, 57, 0xFF808080);
        } else {
            guiGraphics.drawString(font, Component.translatable("jei." + FlavorImmersedDaily.MODID + ".shapeless"),
                    1, 57, 0xFF808080);
        }
    }
}
