package com.fidtest.jei;

import com.fidtest.FidworkMod;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CookingRecipeCategory implements IRecipeCategory<CookingRecipe> {

    private static final int[][] INPUT_START = {
            {40, 10}, {26, 0}, {22, 10}, {13, 10}
    };
    private static final int[] INPUT_COLS = {1, 1, 2, 3};

    private final ApplianceType appliance;
    private final RecipeType<CookingRecipe> type;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final Component title;

    public CookingRecipeCategory(IGuiHelper helper, ApplianceType appliance, Block iconBlock) {
        this(helper, appliance, iconBlock, "");
    }

    public CookingRecipeCategory(IGuiHelper helper, ApplianceType appliance, Block iconBlock, String mode) {
        this.appliance = appliance;
// 变温烤箱拆成两类：烘干模式走原 dryer 类型，烤箱模式走独立类型
        this.type = appliance == ApplianceType.DRYER && "oven".equals(mode)
                ? ovenType()
                : RecipeType.create(FidworkMod.MODID, appliance.getName() + "_cooking", CookingRecipe.class);

        ResourceLocation bgLoc = new ResourceLocation(FidworkMod.MODID, "textures/gui/jei/cooking_background.png");
        this.background = helper.createDrawable(bgLoc, 0, 0, 116, 56);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(iconBlock));
        this.title = appliance == ApplianceType.DRYER && "oven".equals(mode)
                ? Component.translatable("jei.fidworkblock.category.dryer_oven")
                : Component.translatable("jei.fidworkblock.category." + appliance.getName());

        String progressName = appliance == ApplianceType.PRESSURECOOKER ? "pressurecooker_progress" : appliance.getName() + "_progress";
        ResourceLocation arrowTex = new ResourceLocation(FidworkMod.MODID, "textures/gui/container/" + progressName + ".png");
        int cookTime = appliance.getDefaultCookingTime();
        this.arrow = helper.drawableBuilder(arrowTex, 0, 0, 24, 17)
                .setTextureSize(24, 17)
                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
    }

// 变温烤箱·烤箱模式的 JEI 配方类型
    public static RecipeType<CookingRecipe> ovenType() {
        return RecipeType.create(FidworkMod.MODID, "dryer_oven_cooking", CookingRecipe.class);
    }

    public static RecipeType<CookingRecipe> pcType() {
        return RecipeType.create(FidworkMod.MODID, ApplianceType.PRESSURECOOKER.getName() + "_cooking", CookingRecipe.class);
    }

    public static RecipeType<CookingRecipe> drType() {
        return RecipeType.create(FidworkMod.MODID, ApplianceType.DRYER.getName() + "_cooking", CookingRecipe.class);
    }

    public static RecipeType<CookingRecipe> fpType() {
        return RecipeType.create(FidworkMod.MODID, ApplianceType.FRYINGPAN.getName() + "_cooking", CookingRecipe.class);
    }

    public static RecipeType<CookingRecipe> jarType() {
        return RecipeType.create(FidworkMod.MODID, ApplianceType.JAR.getName() + "_cooking", CookingRecipe.class);
    }

    public static RecipeType<CookingRecipe> sbType() {
        return RecipeType.create(FidworkMod.MODID, ApplianceType.STEAMBOX.getName() + "_cooking", CookingRecipe.class);
    }

    public static RecipeType<CookingRecipe> wkType() {
        return RecipeType.create(FidworkMod.MODID, ApplianceType.WOK.getName() + "_cooking", CookingRecipe.class);
    }

    @Override public RecipeType<CookingRecipe> getRecipeType() { return type; }
    @Override public Component getTitle() { return title; }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CookingRecipe recipe, IFocusGroup focuses) {
        List<Ingredient> ingredients = recipe.getIngredients();
        int n = Math.min(ingredients.size(), 3);
        int cols = INPUT_COLS[n];
        int bx = INPUT_START[n][0];
        int by = INPUT_START[n][1];
        for (int i = 0; i < n; i++) {
            int x = bx + (i % cols) * 18;
            int y = by + (i / cols) * 18;
            builder.addSlot(RecipeIngredientRole.INPUT, x + 1, y + 1)
                    .addItemStacks(Arrays.asList(ingredients.get(i).getItems()));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 19).addItemStack(recipe.getResult());
    }

    @Override
    public void draw(CookingRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics g, double mx, double my) {
        arrow.draw(g, 59, 19);
    }

    @Override
    public List<Component> getTooltipStrings(CookingRecipe recipe, IRecipeSlotsView slotsView, double mx, double my) {
        List<Component> list = new ArrayList<>();
        if (mx >= 59 && mx <= 83 && my >= 19 && my <= 36) {
            if (recipe.getCookingTime() > 0)
                list.add(Component.translatable("gui.jei.category.smelting.time.seconds", recipe.getCookingTime() / 20));
            if (recipe.getExperience() > 0)
                list.add(Component.translatable("gui.jei.category.smelting.experience", recipe.getExperience()));
        }
        return list;
    }
}
