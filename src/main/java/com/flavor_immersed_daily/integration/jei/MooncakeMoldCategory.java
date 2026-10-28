package com.flavor_immersed_daily.integration.jei;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModItems;
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
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.List;

// JEI 月饼模具分类 — 展示「月饼模具 + 饼皮 → 生月饼 → 成品月饼」的压制链路，
// 下方给出成品月饼的效果说明；月饼模具作为输入槽出现，因此查看模具用途时会列出全部页面。
public class MooncakeMoldCategory implements IRecipeCategory<MooncakeMoldRecipe> {

    public static final RecipeType<MooncakeMoldRecipe> TYPE =
            RecipeType.create(FlavorImmersedDaily.MODID, "mooncake_mold", MooncakeMoldRecipe.class);

    private static final int WIDTH = 140;
    private static final int HEIGHT = 100;

// 槽位与箭头坐标
    private static final int SLOT_Y = 20;
    private static final int MOLD_X = 10;
    private static final int DOUGH_X = 32;
    private static final int ARROW1_X = 54;
    private static final int RAW_X = 70;
    private static final int ARROW2_X = 92;
    private static final int FINISHED_X = 108;
    private static final int ARROW_Y = SLOT_Y + 4;

// 说明文字起始 Y、行距与颜色
    private static final int TEXT_Y = 42;
    private static final int TEXT_LINE_HEIGHT = 10;
    private static final int TEXT_COLOR = 0xFF_55_55_55;
// 文字最多占用的行数，防止超出分类高度
    private static final int MAX_TEXT_LINES = 6;

// 分类背景贴图（含槽位底图）
    private static final ResourceLocation BACKGROUND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "textures/jei/mooncake_mold.png");

    private final IDrawable background;
    private final IDrawable icon;

    public MooncakeMoldCategory(IGuiHelper helper) {
        this.background = helper.drawableBuilder(BACKGROUND_TEXTURE, 0, 0, WIDTH, HEIGHT)
                .setTextureSize(WIDTH, HEIGHT)
                .build();
        this.icon = helper.createDrawableItemStack(new ItemStack(ModItems.MOONCAKEMOLD.get()));
    }

    @Override
    public RecipeType<MooncakeMoldRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei." + FlavorImmersedDaily.MODID + ".mooncake_mold");
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
    public void setRecipe(IRecipeLayoutBuilder builder, MooncakeMoldRecipe recipe, IFocusGroup focuses) {
// 月饼模具：加工用的工具，展示在输入槽
        builder.addSlot(RecipeIngredientRole.INPUT, MOLD_X, SLOT_Y)
                .addItemStack(new ItemStack(ModItems.MOONCAKEMOLD.get()));

        if (!recipe.getDough().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, DOUGH_X, SLOT_Y)
                    .addItemStack(recipe.getDough());
        }
        if (!recipe.getRawMooncake().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, RAW_X, SLOT_Y)
                    .addItemStack(recipe.getRawMooncake());
        }
        if (!recipe.getFinishedMooncake().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, FINISHED_X, SLOT_Y)
                    .addItemStack(recipe.getFinishedMooncake());
        }
    }

    @Override
    public void draw(MooncakeMoldRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
                     double mouseX, double mouseY) {
        var font = Minecraft.getInstance().font;

// 压制 / 烹制箭头
        guiGraphics.drawString(font, "→", ARROW1_X, ARROW_Y, 0xFF_80_80_80, false);
        guiGraphics.drawString(font, "→", ARROW2_X, ARROW_Y, 0xFF_80_80_80, false);

// 说明文字
        int textY = TEXT_Y;
        int lineCount = 0;
        for (Component line : recipe.getDescription()) {
            for (FormattedCharSequence seq : font.split(line, WIDTH - 8)) {
                if (lineCount >= MAX_TEXT_LINES) {
                    return;
                }
                guiGraphics.drawString(font, seq, 4, textY, TEXT_COLOR, false);
                textY += TEXT_LINE_HEIGHT;
                lineCount++;
            }
        }
    }
}
