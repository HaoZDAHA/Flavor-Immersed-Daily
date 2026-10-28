package com.fidtest.jei;

import com.fidtest.choppingboard.ChoppingboardMobDefinition;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
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
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 砧板「小型生物屠宰」的 JEI 分类。
// 配方来自数据包 data/<任意命名空间>/choppingboard_mob/*.json：
// 输入物品 + 刀具催化剂 → 合并所有阶段掉落的产物（每行 4 个自动换行）。
// 悬停 Tooltip 提示需要右键的刀数。
public class ChoppingboardMobRecipeCategory implements IRecipeCategory<ChoppingboardMobDefinition> {

    private static final int WIDTH = 170;
    private static final int HEIGHT = 58;
    private static final int OUTPUTS_PER_ROW = 4;

    private final RecipeType<ChoppingboardMobDefinition> recipeType;
    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;

    public ChoppingboardMobRecipeCategory(IGuiHelper helper, net.minecraft.world.level.block.Block iconBlock) {
        this.recipeType = ModJeiRecipeTypes.forChoppingboardMob();
        this.background = helper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = helper.createDrawableItemStack(new ItemStack(iconBlock));
        this.title = Component.translatable("jei.fidworkblock.category.choppingboard_mob");
    }

    @Override
    public RecipeType<ChoppingboardMobDefinition> getRecipeType() {
        return recipeType;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ChoppingboardMobDefinition recipe, IFocusGroup focuses) {
// 输入：放上砧板的物品
        builder.addSlot(RecipeIngredientRole.INPUT, 3, 20)
                .addItemStack(new ItemStack(recipe.input()));

// 催化剂：刀具（物品或标签展开的多个候选）
        List<ItemStack> tools = toolStacks(recipe);
        if (!tools.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.CATALYST, 28, 5).addItemStacks(tools);
        } else {
            builder.addSlot(RecipeIngredientRole.CATALYST, 28, 5);
        }

// 输出：合并所有阶段掉落的产物
        List<ItemStack> outputs = mergeDrops(recipe);
        int startX = 62;
        int startY = 14;
        for (int i = 0; i < outputs.size(); i++) {
            int col = i % OUTPUTS_PER_ROW;
            int row = i / OUTPUTS_PER_ROW;
            builder.addSlot(RecipeIngredientRole.OUTPUT, startX + col * 21, startY + row * 26)
                    .addItemStack(outputs.get(i));
        }
    }

    @Override
    public void draw(ChoppingboardMobDefinition recipe, IRecipeSlotsView slotsView,
                     GuiGraphics guiGraphics, double mouseX, double mouseY) {
        var font = Minecraft.getInstance().font;
        String toolName = toolLabel(recipe);
        if (!toolName.isEmpty()) {
            guiGraphics.drawString(font, toolName, 28, 24, 0xFF555555, false);
        }
        guiGraphics.drawString(font, "→", 50, 22, 0xFF808080, false);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, ChoppingboardMobDefinition recipe,
                           IRecipeSlotsView slotsView, double mouseX, double mouseY) {
        tooltip.add(Component.translatable("jei.fidworkblock.choppingboard_mob.note.hits", recipe.stageCount()));
    }

// 刀具候选：具体物品 → 单个；标签 → 展开所有成员；都解析失败 → 空。
    private static List<ItemStack> toolStacks(ChoppingboardMobDefinition recipe) {
        List<ItemStack> stacks = new ArrayList<>();
        Item toolItem = recipe.toolItem();
        if (toolItem != null) {
            stacks.add(new ItemStack(toolItem));
        } else if (recipe.toolTag() != null) {
            for (ItemStack stack : Ingredient.of(recipe.toolTag()).getItems()) {
                stacks.add(stack);
            }
        }
        return stacks;
    }

    private static String toolLabel(ChoppingboardMobDefinition recipe) {
        if (recipe.toolItem() != null) {
            return new ItemStack(recipe.toolItem()).getHoverName().getString();
        }
        if (recipe.toolTag() != null) {
            return "#" + recipe.toolTag().location();
        }
        return "";
    }

// 合并所有阶段掉落的物品并累加数量。
    private static List<ItemStack> mergeDrops(ChoppingboardMobDefinition recipe) {
        Map<Item, Integer> merged = new LinkedHashMap<>();
        for (ChoppingboardMobDefinition.Stage stage : recipe.stages()) {
            for (Item drop : stage.drops()) {
                merged.merge(drop, 1, Integer::sum);
            }
        }
        List<ItemStack> result = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : merged.entrySet()) {
            result.add(new ItemStack(entry.getKey(), entry.getValue()));
        }
        return result;
    }
}
