package com.fidtest.jei;

import com.fidtest.ExampleMod;
import com.fidtest.choppingboard.ChoppingboardWrapRecipes;
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

// 砧板「包裹/成型」的 JEI 分类。
// 四种工序（mode）见 ChoppingboardWrapRecipes：
// wrap / mooncake / zongzi / roll。
public class ChoppingboardWrapRecipeCategory implements IRecipeCategory<RecipeHolder<CookingRecipe>> {

    private final RecipeType<RecipeHolder<CookingRecipe>> recipeType;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final Component title;

    public ChoppingboardWrapRecipeCategory(IGuiHelper helper, Block iconBlock) {
        this.recipeType = ModJeiRecipeTypes.forChoppingboardWrap();

        ResourceLocation bgLoc = ResourceLocation.fromNamespaceAndPath(
                ExampleMod.MODID, "textures/gui/jei/cooking_background.png");
        this.background = helper.createDrawable(bgLoc, 0, 0, 116, 56);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(iconBlock));
        this.title = Component.translatable("jei.fidworkblock.category.choppingboard_wrap");

        ResourceLocation arrowTexture = ResourceLocation.fromNamespaceAndPath(
                ExampleMod.MODID, "textures/gui/container/teapot_progress.png");
        this.arrow = helper.drawableBuilder(arrowTexture, 0, 0, 24, 17)
                .setTextureSize(24, 17)
                .buildAnimated(20, IDrawableAnimated.StartDirection.LEFT, false);
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

// 主料：皮 / 生馅饼 / 芦苇叶
        if (!ingredients.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 11, 11)
                    .addItemStacks(Arrays.asList(ingredients.get(0).getItems()));
        }

// 第二格：月饼模具或糯米（包裹工序留空，悬停可看说明）
        ItemStack tool = getTool(recipe.getMode());
        if (tool.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.CATALYST, 11, 35);
        } else {
            builder.addSlot(RecipeIngredientRole.CATALYST, 11, 35).addItemStack(tool);
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 81, 20)
                .addItemStack(recipe.getResult());
    }

    @Override
    public void draw(RecipeHolder<CookingRecipe> holder, IRecipeSlotsView slotsView,
                     GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 40, 20);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<CookingRecipe> holder,
                           IRecipeSlotsView slotsView, double mouseX, double mouseY) {
// 该工序的说明（悬停配方任意位置都会显示）
        tooltip.add(Component.translatable(noteKey(holder.value().getMode())));
    }

// 该工序需要的工具/材料（没有则返回空），与 EMI 侧共用同一数据源。
    private static ItemStack getTool(String mode) {
        return ChoppingboardWrapRecipes.toolFor(mode);
    }

// 工序说明的翻译键，与 EMI 侧共用同一数据源。
    private static String noteKey(String mode) {
        return ChoppingboardWrapRecipes.noteKey(mode);
    }
}
