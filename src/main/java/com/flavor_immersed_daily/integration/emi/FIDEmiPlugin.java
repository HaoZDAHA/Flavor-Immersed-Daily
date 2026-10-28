package com.flavor_immersed_daily.integration.emi;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.integration.ButcheringSteps;
import com.flavor_immersed_daily.recipe.EggBreakingRecipe;
import com.flavor_immersed_daily.recipe.FridgeFreezingRecipe;
import com.flavor_immersed_daily.recipe.FridgeTemperingRecipe;
import com.flavor_immersed_daily.recipe.ModRecipes;
import com.flavor_immersed_daily.recipe.WoodBasinRecipe;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@EmiEntrypoint
public final class FIDEmiPlugin implements EmiPlugin {
    public static final EmiRecipeCategory WOOD_BASIN = category("wood_basin", ModBlocks.WOODBASIN.get().asItem());
    public static final EmiRecipeCategory EGG_BREAKING = category("egg_breaking", ModBlocks.EGGBREAKINGMACHINE.get().asItem());
    public static final EmiRecipeCategory FRIDGE_TEMPERING = category("fridge_tempering", ModBlocks.FRIDGE.get().asItem());
    public static final EmiRecipeCategory FRIDGE_FREEZING = category("fridge_freezing", ModBlocks.FRIDGE.get().asItem());
    public static final EmiRecipeCategory BUTCHERING = category("butchering", ModBlocks.BIGHOOK.get().asItem());
    public static final EmiRecipeCategory APPRAISAL = category("agricultural_appraisal",
            ModBlocks.AGRICULTURALAPPRAISALMACHINE.get().asItem());

    private static EmiRecipeCategory category(String path, net.minecraft.world.level.ItemLike icon) {
        return new EmiRecipeCategory(id(path), EmiStack.of(icon));
    }

    static ResourceLocation id(String path) {
        return new ResourceLocation(FlavorImmersedDaily.MODID, path);
    }

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(WOOD_BASIN);
        registry.addCategory(EGG_BREAKING);
        registry.addCategory(FRIDGE_TEMPERING);
        registry.addCategory(FRIDGE_FREEZING);
        registry.addCategory(BUTCHERING);
        registry.addCategory(APPRAISAL);

        registry.addWorkstation(WOOD_BASIN, EmiStack.of(ModBlocks.WOODBASIN.get().asItem()));
        registry.addWorkstation(EGG_BREAKING, EmiStack.of(ModBlocks.EGGBREAKINGMACHINE.get().asItem()));
        registry.addWorkstation(FRIDGE_TEMPERING, EmiStack.of(ModBlocks.FRIDGE.get().asItem()));
        registry.addWorkstation(FRIDGE_FREEZING, EmiStack.of(ModBlocks.FRIDGE.get().asItem()));
        registry.addWorkstation(BUTCHERING, EmiStack.of(ModBlocks.BIGHOOK.get().asItem()));
        registry.addWorkstation(APPRAISAL, EmiStack.of(ModBlocks.AGRICULTURALAPPRAISALMACHINE.get().asItem()));

        for (RecipeHolder<WoodBasinRecipe> holder : registry.getRecipeManager()
                .getAllRecipesFor(ModRecipes.WOOD_BASIN_TYPE.get())) {
            registry.addRecipe(FIDEmiRecipe.woodBasin(holder));
        }
        for (RecipeHolder<EggBreakingRecipe> holder : registry.getRecipeManager()
                .getAllRecipesFor(ModRecipes.EGG_BREAKING_TYPE.get())) {
            registry.addRecipe(FIDEmiRecipe.eggBreaking(holder));
        }
        for (RecipeHolder<FridgeTemperingRecipe> holder : registry.getRecipeManager()
                .getAllRecipesFor(ModRecipes.FRIDGE_TEMPERING_TYPE.get())) {
            registry.addRecipe(FIDEmiRecipe.tempering(holder));
        }
        for (RecipeHolder<FridgeFreezingRecipe> holder : registry.getRecipeManager()
                .getAllRecipesFor(ModRecipes.FRIDGE_FREEZING_TYPE.get())) {
            registry.addRecipe(FIDEmiRecipe.freezing(holder));
        }

// 屠宰：掉落物来自 JSON 默认表 + config 额外产物（见 Config.getDrops）
        for (ButcheringSteps.Step step : ButcheringSteps.all()) {
            List<ItemStack> outputs = buildDropStacks(Config.getDrops(step.animalType(), step.stage()));
            if (outputs.isEmpty()) {
                continue;
            }
            ResourceLocation id = id("butchering/" + step.animalType() + "/" + step.stage());
            registry.addRecipe(new FIDButcheringEmiRecipe(id,
                    new ItemStack(step.input()), new ItemStack(step.tool()), step.stageName(), outputs));
        }

// 农产鉴定：掉落物来自 JSON 默认表 + config 额外产物（见 Config.getWildDropsMap）
        for (Map.Entry<String, List<String>> entry : Config.getWildDropsMap().entrySet()) {
            Item inputItem = BuiltInRegistries.ITEM.get(new ResourceLocation(entry.getKey()));
            List<ItemStack> outputs = buildDropStacks(entry.getValue());
            if (inputItem == null || outputs.isEmpty()) {
                continue;
            }
// 输入物品 id 形如 flavor_immersed_daily:xxx，ResourceLocation 的 path 不允许冒号，做一下替换
            String safeInput = entry.getKey().replace(':', '_');
            registry.addRecipe(new FIDAppraisalEmiRecipe(
                    id("appraisal/" + safeInput), new ItemStack(inputItem), outputs));
        }
    }

// 把掉落物 id 解析成物品栈列表。
    private static List<ItemStack> buildDropStacks(List<String> dropIds) {
        List<ItemStack> result = new ArrayList<>();
        for (String dropId : dropIds) {
            Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(dropId));
            if (item != null) {
                result.add(new ItemStack(item));
            }
        }
        return result;
    }

// 合并相同物品并累加数量（用于掉落展示，等价于 JEI 的合并逻辑）。
    static List<ItemStack> mergeDrops(List<ItemStack> stacks) {
        Map<Item, Integer> merged = new LinkedHashMap<>();
        for (ItemStack stack : stacks) {
            merged.merge(stack.getItem(), stack.getCount(), Integer::sum);
        }
        List<ItemStack> result = new ArrayList<>();
        for (Map.Entry<Item, Integer> e : merged.entrySet()) {
            result.add(new ItemStack(e.getKey(), e.getValue()));
        }
        return result;
    }
}
