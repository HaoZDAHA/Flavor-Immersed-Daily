package com.flavor_immersed_daily.jei;

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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JEI 农产鉴定分类 — 显示野生采集物→掉落物的配方，每行4个产物自动换行
 */
public class AgriculturalAppraisalMachineCategory implements IRecipeCategory<AgriculturalAppraisalMachineRecipe> {

    public static final RecipeType<AgriculturalAppraisalMachineRecipe> TYPE =
            RecipeType.create(FlavorImmersedDaily.MODID, "agricultural_appraisal", AgriculturalAppraisalMachineRecipe.class);

    private static final int WIDTH = 176;
    private static final int HEIGHT = 150;
    private static final int OUTPUTS_PER_ROW = 4;

    private final IDrawable background;
    private final IDrawable icon;

    public AgriculturalAppraisalMachineCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = helper.createDrawableItemStack(new ItemStack(FlavorImmersedDaily.AGRICULTURALAPPRAISALMACHINE_ITEM.get()));
    }

    @Override
    public RecipeType<AgriculturalAppraisalMachineRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei." + FlavorImmersedDaily.MODID + ".agricultural_appraisal");
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
    public void setRecipe(IRecipeLayoutBuilder builder, AgriculturalAppraisalMachineRecipe recipe, IFocusGroup focuses) {
        // 输入：野生采集物
        builder.addSlot(RecipeIngredientRole.INPUT, 3, 20)
                .addItemStack(recipe.getInput());

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
    public void draw(AgriculturalAppraisalMachineRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
                     double mouseX, double mouseY) {
        var font = Minecraft.getInstance().font;
        // 箭头
        guiGraphics.drawString(font, "→", 28, 22, 0xFF808080, false);
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