package com.flavor_immersed_daily.integration.jei;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModBlocks;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

// JEI 作物转换分类 — 显示转换组内物品的互相转换，每行4个产物自动换行
public class CropConversionCategory implements IRecipeCategory<CropConversionJeiRecipe> {

    public static final RecipeType<CropConversionJeiRecipe> TYPE =
            RecipeType.create(FlavorImmersedDaily.MODID, "crop_conversion", CropConversionJeiRecipe.class);

    private static final int WIDTH = 176;
    private static final int HEIGHT = 150;
    private static final int OUTPUTS_PER_ROW = 4;

// 分类背景贴图（含槽位底图）
    private static final ResourceLocation BACKGROUND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "textures/jei/crop_conversion.png");

    private final IDrawable background;
    private final IDrawable icon;

    public CropConversionCategory(IGuiHelper helper) {
        this.background = helper.drawableBuilder(BACKGROUND_TEXTURE, 0, 0, WIDTH, HEIGHT)
                .setTextureSize(WIDTH, HEIGHT)
                .build();
        this.icon = helper.createDrawableItemStack(new ItemStack(ModBlocks.AGRICULTURALAPPRAISALMACHINE.asItem()));
    }

    @Override
    public RecipeType<CropConversionJeiRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei." + FlavorImmersedDaily.MODID + ".crop_conversion");
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
    public void setRecipe(IRecipeLayoutBuilder builder, CropConversionJeiRecipe recipe, IFocusGroup focuses) {
// 输入：转换组内的物品
        builder.addSlot(RecipeIngredientRole.INPUT, 3, 20)
                .addItemStack(recipe.getInput());

// 输出：组内其他已安装物品，每行4个自动换行
        List<ItemStack> outputs = recipe.getOutputs();
        int startX = 62;
        int startY = 14;
        for (int i = 0; i < outputs.size(); i++) {
            int col = i % OUTPUTS_PER_ROW;
            int row = i / OUTPUTS_PER_ROW;
            builder.addSlot(RecipeIngredientRole.OUTPUT,
                            startX + col * 21,
                            startY + row * 26)
                    .addItemStack(outputs.get(i));
        }
    }

    @Override
    public void draw(CropConversionJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
                     double mouseX, double mouseY) {
        var font = Minecraft.getInstance().font;
// 箭头
        guiGraphics.drawString(font, "→", 28, 22, 0xFF808080, false);
    }
}
