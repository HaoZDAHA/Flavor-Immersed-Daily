package com.fidtest.jei;

import com.fidtest.ExampleMod;
import com.fidtest.choppingboard.ChoppingboardMobDefinition;
import com.fidtest.recipe.ApplianceType;
import com.fidtest.recipe.CookingRecipe;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.HashMap;
import java.util.Map;

public final class ModJeiRecipeTypes {

    private static final Map<ApplianceType, RecipeType<RecipeHolder<CookingRecipe>>> TYPES = new HashMap<>();
// 烘干机按模式拆分的 JEI 配方类型
    private static final Map<String, RecipeType<RecipeHolder<CookingRecipe>>> DRYER_MODES = new HashMap<>();
// 砧板包裹/成型（皮包生食、月饼、粽子）的 JEI 配方类型
    private static final RecipeType<RecipeHolder<CookingRecipe>> CHOPPINGBOARD_WRAP = RecipeType.create(
            ExampleMod.MODID,
            "choppingboard_wrap",
            (Class<RecipeHolder<CookingRecipe>>) (Class<?>) RecipeHolder.class
    );
// 砧板小型生物屠宰（数据包 choppingboard_mob 定义）的 JEI 配方类型
    private static final RecipeType<ChoppingboardMobDefinition> CHOPPINGBOARD_MOB = RecipeType.create(
            ExampleMod.MODID,
            "choppingboard_mob",
            ChoppingboardMobDefinition.class
    );

    static {
        for (ApplianceType app : ApplianceType.values()) {
            TYPES.put(app, RecipeType.create(
                    ExampleMod.MODID,
                    app.getName() + "_cooking",
                    (Class<RecipeHolder<CookingRecipe>>) (Class<?>) RecipeHolder.class
            ));
        }
        DRYER_MODES.put("oven", RecipeType.create(
                ExampleMod.MODID,
                "dryer_oven_cooking",
                (Class<RecipeHolder<CookingRecipe>>) (Class<?>) RecipeHolder.class
        ));
    }

    public static RecipeType<RecipeHolder<CookingRecipe>> forAppliance(ApplianceType app) {
        return TYPES.get(app);
    }

// 烘干机指定模式的 JEI 配方类型（dry=烘干，oven=烤箱）
    public static RecipeType<RecipeHolder<CookingRecipe>> forDryerMode(String mode) {
        return "oven".equals(mode) ? DRYER_MODES.get("oven") : TYPES.get(ApplianceType.DRYER);
    }

// 砧板包裹/成型（皮 + 馅料 → 生食；生馅饼 + 月饼模具 → 生月饼；芦苇叶 + 糯米 → 生粽子）
    public static RecipeType<RecipeHolder<CookingRecipe>> forChoppingboardWrap() {
        return CHOPPINGBOARD_WRAP;
    }

// 砧板小型生物屠宰（数据包 choppingboard_mob/*.json 定义）
    public static RecipeType<ChoppingboardMobDefinition> forChoppingboardMob() {
        return CHOPPINGBOARD_MOB;
    }

    private ModJeiRecipeTypes() {}
}
