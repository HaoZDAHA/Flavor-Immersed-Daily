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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JEI 屠宰分类 — 显示动物→刀具→掉落的配方，每行4个产物自动换行
 */
public class ButcheringRecipeCategory implements IRecipeCategory<ButcheringRecipe> {

    public static final RecipeType<ButcheringRecipe> TYPE =
            RecipeType.create(FlavorImmersedDaily.MODID, "butchering", ButcheringRecipe.class);

    private static final int WIDTH = 170;
    private static final int OUTPUTS_PER_ROW = 4;

    private final IDrawable background;
    private final IDrawable icon;

    public ButcheringRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(WIDTH, 58);
        this.icon = helper.createDrawableItemStack(new ItemStack(ModBlocks.BIGHOOK.asItem()));
    }

    @Override
    public RecipeType<ButcheringRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei." + FlavorImmersedDaily.MODID + ".butchering");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ButcheringRecipe recipe, IFocusGroup focuses) {
        // 输入：死动物
        builder.addSlot(RecipeIngredientRole.INPUT, 3, 20)
                .addItemStack(recipe.getInput());

        // 催化剂：刀具（输入右边靠上）
        builder.addSlot(RecipeIngredientRole.CATALYST, 28, 5)
                .addItemStack(recipe.getTool());

        // 输出：合并重复物品，每行4个自动换行
        List<ItemStack> outputs = mergeDuplicates(recipe.getOutputs());
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
    public void draw(ButcheringRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
                     double mouseX, double mouseY) {
        var font = Minecraft.getInstance().font;
        // 刀具名称（tool slot 下方）
        guiGraphics.drawString(font, recipe.getToolName(), 28, 24, 0xFF555555, false);
        // 箭头
        guiGraphics.drawString(font, "→", 50, 22, 0xFF808080, false);
    }

    /**
     * 合并列表中相同的物品，累加数量
     */
    private static List<ItemStack> mergeDuplicates(List<ItemStack> stacks) {
        Map<Item, Integer> merged = new LinkedHashMap<>();
        for (ItemStack stack : stacks) {
            merged.merge(stack.getItem(), stack.getCount(), Integer::sum);
        }
        List<ItemStack> result = new ArrayList<>();
        for (var entry : merged.entrySet()) {
            result.add(new ItemStack(entry.getKey(), entry.getValue()));
        }
        return result;
    }
}
