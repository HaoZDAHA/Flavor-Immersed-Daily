package com.flavor_immersed_daily.integration.jei;

import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.all.ModBlocks;

import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.recipe.EggBreakingRecipe;
import com.flavor_immersed_daily.recipe.FridgeTemperingRecipe;
import com.flavor_immersed_daily.recipe.FridgeFreezingRecipe;
import com.flavor_immersed_daily.recipe.ModRecipes;
import com.flavor_immersed_daily.recipe.WoodBasinRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ButcheringRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new WoodBasinInfoCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FridgeTemperingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FridgeFreezingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new EggBreakingMachineCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AgriculturalAppraisalMachineCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<ButcheringRecipe> recipes = new ArrayList<>();

        // 牛
        addRecipes(recipes, ModItems.DEADCATTLE.get(), 1,
                new Item[]{ModItems.WIDEEDGEDKNIFE.get(), ModItems.SHARPKNIFE.get(),
                        ModItems.BONECUTTERKNIFE.get(), ModItems.SHARPKNIFE.get(),
                        ModItems.SHARPKNIFE.get()},
                new String[]{"放血", "剥皮", "剔骨", "掏空", "切肉"});

        // 羊
        addRecipes(recipes, ModItems.DEADSHEEP.get(), 2,
                new Item[]{ModItems.WIDEEDGEDKNIFE.get(), ModItems.SHARPKNIFE.get(),
                        ModItems.BONECUTTERKNIFE.get(), ModItems.SHARPKNIFE.get(),
                        ModItems.SHARPKNIFE.get()},
                new String[]{"放血", "剥皮", "剔骨", "掏空", "切肉"});

        // 猪
        addRecipes(recipes, ModItems.DEADPIG.get(), 3,
                new Item[]{ModItems.WIDEEDGEDKNIFE.get(), ModItems.SHARPKNIFE.get(),
                        ModItems.BONECUTTERKNIFE.get(), ModItems.SHARPKNIFE.get(),
                        ModItems.SHARPKNIFE.get()},
                new String[]{"放血", "剥皮", "剔骨", "掏空", "切肉"});

        // 鸡（特殊流程）
        addDropRecipe(recipes, ModItems.DEADCHICKEN.get(), 4, 1,
                ModItems.WIDEEDGEDKNIFE.get(), "放血");
        addDropRecipe(recipes, ModItems.CHICKENWITHOUTFEATHER.get(), 4, 5,
                ModItems.SHARPKNIFE.get(), "掏空");
        addDropRecipe(recipes, ModItems.DEADCHICKEN.get(), 4, 6,
                ModItems.SHARPKNIFE.get(), "切割");

        registration.addRecipes(ButcheringRecipeCategory.TYPE, recipes);

        // ===== 木盆信息页 =====
        List<WoodBasinInfoRecipe> basinRecipes = new ArrayList<>();
        buildBasinRecipes(basinRecipes);
        registration.addRecipes(WoodBasinInfoCategory.TYPE, basinRecipes);

        // ===== 冰箱·变温室 =====
        List<FridgeTemperingRecipe> temperingRecipes = new ArrayList<>();
        buildFridgeTemperingRecipes(temperingRecipes);
        registration.addRecipes(FridgeTemperingCategory.TYPE, temperingRecipes);

        // ===== 冰箱·冷冻室 =====
        List<FridgeFreezingRecipe> freezingRecipes = new ArrayList<>();
        buildFridgeFreezingRecipes(freezingRecipes);
        registration.addRecipes(FridgeFreezingCategory.TYPE, freezingRecipes);

        // ===== 打蛋机 =====
        List<EggBreakingRecipe> eggBreakingRecipes = new ArrayList<>();
        buildEggBreakingRecipes(eggBreakingRecipes);
        registration.addRecipes(EggBreakingMachineCategory.TYPE, eggBreakingRecipes);

        // ===== 农产鉴定机 =====
        List<AgriculturalAppraisalMachineRecipe> appraisalRecipes = new ArrayList<>();
        buildAgriculturalAppraisalRecipes(appraisalRecipes);
        registration.addRecipes(AgriculturalAppraisalMachineCategory.TYPE, appraisalRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FRIDGE.asItem()),
                FridgeTemperingCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FRIDGE.asItem()),
                FridgeFreezingCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.EGGBREAKINGMACHINE.asItem()),
                EggBreakingMachineCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.AGRICULTURALAPPRAISALMACHINE.asItem()),
                AgriculturalAppraisalMachineCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.WOODBASIN.asItem()), WoodBasinInfoCategory.TYPE);
    }

    private void buildBasinRecipes(List<WoodBasinInfoRecipe> recipes) {
        List<ItemStack> washOutputs = new ArrayList<>();
        washOutputs.add(new ItemStack(ModItems.CHICKENWITHOUTFEATHER.get()));
        for (String itemId : Config.washedChickenDrops) {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemId));
            if (item != null) {
                washOutputs.add(new ItemStack(item));
            }
        }
        recipes.add(new WoodBasinInfoRecipe(ItemStack.EMPTY, washOutputs,
                "jei.flavor_immersed_daily.woodbasin_info.wash_desc"));

        var level = Minecraft.getInstance().level;
        if (level == null) return;
        for (var holder : level.getRecipeManager().getAllRecipesFor(ModRecipes.WOOD_BASIN_TYPE.get())) {
            WoodBasinRecipe recipe = holder.value();
            ItemStack[] ingredients = recipe.getIngredient().getItems();
            if (ingredients.length == 0) continue;
            ItemStack fruitInput = ingredients[0].copy();
            List<ItemStack> jamOutput = new ArrayList<>();
            ItemStack jam = recipe.getResult();
            if (!fruitInput.isEmpty() && !jam.isEmpty()) {
                jamOutput.add(jam);
                recipes.add(new WoodBasinInfoRecipe(fruitInput, jamOutput,
                        "jei.flavor_immersed_daily.woodbasin_info.stomp_desc"));
            }
        }
    }

    private void buildFridgeTemperingRecipes(List<FridgeTemperingRecipe> recipes) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        for (var holder : level.getRecipeManager().getRecipes()) {
            if (holder.value() instanceof FridgeTemperingRecipe r) {
                recipes.add(r);
            }
        }
    }

    private void buildFridgeFreezingRecipes(List<FridgeFreezingRecipe> recipes) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        for (var holder : level.getRecipeManager().getRecipes()) {
            if (holder.value() instanceof FridgeFreezingRecipe r) {
                recipes.add(r);
            }
        }
    }

    private void addRecipes(List<ButcheringRecipe> recipes, Item input, int animalType,
                            Item[] tools, String[] stageNames) {
        for (int stage = 1; stage <= 5; stage++) {
            List<ItemStack> outputs = buildOutputs(animalType, stage);
            if (outputs.isEmpty()) continue;
            recipes.add(new ButcheringRecipe(
                    new ItemStack(input),
                    new ItemStack(tools[stage - 1]),
                    stageNames[stage - 1],
                    outputs
            ));
        }
    }

    private void addDropRecipe(List<ButcheringRecipe> recipes, Item input, int animalType,
                               int stage, Item tool, String stageName) {
        List<ItemStack> outputs = buildOutputs(animalType, stage);
        if (outputs.isEmpty()) return;
        recipes.add(new ButcheringRecipe(
                new ItemStack(input),
                new ItemStack(tool),
                stageName,
                outputs
        ));
    }

    private List<ItemStack> buildOutputs(int animalType, int stage) {
        List<String> dropIds = Config.getDrops(animalType, stage);
        List<ItemStack> outputs = new ArrayList<>();
        for (String itemId : dropIds) {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemId));
            if (item != null) {
                outputs.add(new ItemStack(item));
            }
        }
        return outputs;
    }

    private void buildEggBreakingRecipes(List<EggBreakingRecipe> recipes) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        for (var holder : level.getRecipeManager().getRecipes()) {
            if (holder.value() instanceof EggBreakingRecipe r) {
                recipes.add(r);
            }
        }
    }

    private void buildAgriculturalAppraisalRecipes(List<AgriculturalAppraisalMachineRecipe> recipes) {
        Map<String, List<String>> wildDrops = Config.getWildDropsMap();
        for (Map.Entry<String, List<String>> entry : wildDrops.entrySet()) {
            String inputId = entry.getKey();
            List<String> dropIds = entry.getValue();
            if (dropIds.isEmpty()) continue;

            Item inputItem = BuiltInRegistries.ITEM.get(ResourceLocation.parse(inputId));
            if (inputItem == null) continue;

            List<ItemStack> outputs = new ArrayList<>();
            for (String dropId : dropIds) {
                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(dropId));
                if (item != null) {
                    outputs.add(new ItemStack(item));
                }
            }
            if (!outputs.isEmpty()) {
                recipes.add(new AgriculturalAppraisalMachineRecipe(
                        new ItemStack(inputItem), outputs));
            }
        }
    }
}
