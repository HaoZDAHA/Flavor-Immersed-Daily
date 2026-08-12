package com.flavor_immersed_daily.integration.jei;

import com.flavor_immersed_daily.FlavorImmersedDaily;
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
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * JEI 木盆信息页 — 输入槽 + 输出槽 + 底部描述文字
 */
public class WoodBasinInfoCategory implements IRecipeCategory<WoodBasinInfoRecipe> {

    public static final RecipeType<WoodBasinInfoRecipe> TYPE =
            RecipeType.create(FlavorImmersedDaily.MODID, "woodbasin_info", WoodBasinInfoRecipe.class);

    private static final int WIDTH = 144;
    private static final int HEIGHT = 72;

    private final IDrawable background;
    private final IDrawable icon;

    public WoodBasinInfoCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = helper.createDrawableItemStack(new ItemStack(FlavorImmersedDaily.WOODBASIN_ITEM.get()));
    }

    @Override
    public RecipeType<WoodBasinInfoRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei." + FlavorImmersedDaily.MODID + ".woodbasin_info");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WoodBasinInfoRecipe recipe, IFocusGroup focuses) {
        // 输入槽：左侧居中
        if (!recipe.getInput().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 8, 14)
                    .addItemStack(recipe.getInput());
        }

        // 输出槽：右侧排列
        List<ItemStack> outputs = recipe.getOutputs();
        int slotSize = 18;
        int startX = 50;
        int startY = 8;
        int perRow = 4;

        for (int i = 0; i < outputs.size(); i++) {
            int col = i % perRow;
            int row = i / perRow;
            builder.addSlot(RecipeIngredientRole.OUTPUT,
                            startX + col * slotSize,
                            startY + row * slotSize)
                    .addItemStack(outputs.get(i));
        }
    }

    @Override
    public void draw(WoodBasinInfoRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
                     double mouseX, double mouseY) {
        var font = Minecraft.getInstance().font;

        // 输入→输出箭头
        if (!recipe.getInput().isEmpty()) {
            guiGraphics.drawString(font, "→", 30, 18, 0xFF808080, false);
        }

        // 底部描述文字
        String desc = recipe.getDescription();
        if (desc != null && !desc.isEmpty()) {
            int maxWidth = WIDTH - 8;
            List<FormattedCharSequence> lines = font.split(Component.translatable(desc), maxWidth);
            int textY = HEIGHT - 4 - lines.size() * 10;
            for (int i = 0; i < lines.size(); i++) {
                guiGraphics.drawString(font, lines.get(i), 4, textY + i * 10, 0xFF555555, false);
            }
        }
    }
}
