package com.fidtest.jei;

import com.fidtest.FidworkMod;
import com.fidtest.block.entity.ChoppingboardBlockEntity;
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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.List;

// 砧板「包裹/成型」的 JEI 分类。
// 三种工序（mode）：
// wrap     —— 皮 + 任意馅料 → 生食（包子/饺子/烧麦/馄饨/汤圆/馅饼）
// mooncake —— 生馅饼 + 月饼模具 → 生月饼
// zongzi   —— 芦苇叶 + 馅料 + 糯米 → 生粽子
public class ChoppingboardWrapRecipeCategory implements IRecipeCategory<CookingRecipe> {

// 工艺类型：包裹
    public static final String MODE_WRAP = "wrap";
// 工艺类型：压月饼
    public static final String MODE_MOONCAKE = "mooncake";
// 工艺类型：包粽子
    public static final String MODE_ZONGZI = "zongzi";
// 工艺类型：擀面杖擀制
    public static final String MODE_ROLL = "roll";

    public static final RecipeType<CookingRecipe> TYPE =
            RecipeType.create(FidworkMod.MODID, "choppingboard_wrap", CookingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final Component title;

    public ChoppingboardWrapRecipeCategory(IGuiHelper helper, Block iconBlock) {
        this.background = helper.createDrawable(
                new ResourceLocation(FidworkMod.MODID, "textures/gui/jei/cooking_background.png"), 0, 0, 116, 56);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(iconBlock));
        this.title = Component.translatable("jei.fidworkblock.category.choppingboard_wrap");
        this.arrow = helper.drawableBuilder(
                new ResourceLocation(FidworkMod.MODID, "textures/gui/container/teapot_progress.png"), 0, 0, 24, 17)
                .setTextureSize(24, 17)
                .buildAnimated(20, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override public RecipeType<CookingRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return title; }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CookingRecipe recipe, IFocusGroup focuses) {
        var ingredients = recipe.getIngredients();

// 主料：皮 / 生馅饼 / 芦苇叶
        if (!ingredients.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 11, 11)
                    .addItemStacks(Arrays.asList(ingredients.get(0).getItems()));
        }

// 第二格：月饼模具或糯米（包裹工序留空）
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
    public void draw(CookingRecipe recipe, IRecipeSlotsView slots, GuiGraphics g, double mx, double my) {
        arrow.draw(g, 40, 20);
    }

    @Override
    public List<Component> getTooltipStrings(CookingRecipe recipe, IRecipeSlotsView slots, double mx, double my) {
// 该工序的说明（悬停配方任意位置都会显示）
        return List.of(Component.translatable(noteKey(recipe.getMode())));
    }

// 该工序需要的工具/材料（没有则返回空）
    private static ItemStack getTool(String mode) {
        if (MODE_MOONCAKE.equals(mode)) {
            return new ItemStack(BuiltInRegistries.ITEM.get(ChoppingboardBlockEntity.MOONCAKE_MOLD_ID));
        }
        if (MODE_ZONGZI.equals(mode)) {
            return new ItemStack(BuiltInRegistries.ITEM.get(ChoppingboardBlockEntity.RICE_ID));
        }
        if (MODE_ROLL.equals(mode)) {
            return new ItemStack(BuiltInRegistries.ITEM.get(ChoppingboardBlockEntity.ROLLING_PIN_ID));
        }
        return ItemStack.EMPTY;
    }

// 工序说明的翻译键
    private static String noteKey(String mode) {
        return switch (mode) {
            case MODE_MOONCAKE -> "jei.fidworkblock.choppingboard.note.mooncake";
            case MODE_ZONGZI -> "jei.fidworkblock.choppingboard.note.zongzi";
            case MODE_ROLL -> "jei.fidworkblock.choppingboard.note.roll";
            default -> "jei.fidworkblock.choppingboard.note.wrap";
        };
    }
}
