package com.flavor_immersed_daily.integration.jei;

import com.flavor_immersed_daily.Config;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.block.WoodBasinBlock;
import com.flavor_immersed_daily.recipe.EggBreakingRecipe;
import com.flavor_immersed_daily.recipe.FridgeTemperingRecipe;
import com.flavor_immersed_daily.recipe.FridgeFreezingRecipe;
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
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<ButcheringRecipe> recipes = new ArrayList<>();

        // 牛
        addRecipes(recipes, FlavorImmersedDaily.DEADCATTLE.get(), 1,
                new Item[]{FlavorImmersedDaily.WIDEEDGEDKNIFE.get(), FlavorImmersedDaily.SHARPKNIFE.get(),
                        FlavorImmersedDaily.BONECUTTERKNIFE.get(), FlavorImmersedDaily.SHARPKNIFE.get(),
                        FlavorImmersedDaily.SHARPKNIFE.get()},
                new String[]{"放血", "剥皮", "剔骨", "掏空", "切肉"});

        // 羊
        addRecipes(recipes, FlavorImmersedDaily.DEADSHEEP.get(), 2,
                new Item[]{FlavorImmersedDaily.WIDEEDGEDKNIFE.get(), FlavorImmersedDaily.SHARPKNIFE.get(),
                        FlavorImmersedDaily.BONECUTTERKNIFE.get(), FlavorImmersedDaily.SHARPKNIFE.get(),
                        FlavorImmersedDaily.SHARPKNIFE.get()},
                new String[]{"放血", "剥皮", "剔骨", "掏空", "切肉"});

        // 猪
        addRecipes(recipes, FlavorImmersedDaily.DEADPIG.get(), 3,
                new Item[]{FlavorImmersedDaily.WIDEEDGEDKNIFE.get(), FlavorImmersedDaily.SHARPKNIFE.get(),
                        FlavorImmersedDaily.BONECUTTERKNIFE.get(), FlavorImmersedDaily.SHARPKNIFE.get(),
                        FlavorImmersedDaily.SHARPKNIFE.get()},
                new String[]{"放血", "剥皮", "剔骨", "掏空", "切肉"});

        // 鸡（特殊流程）
        addDropRecipe(recipes, FlavorImmersedDaily.DEADCHICKEN.get(), 4, 1,
                FlavorImmersedDaily.WIDEEDGEDKNIFE.get(), "放血");
        addDropRecipe(recipes, FlavorImmersedDaily.PLUCKEDCHICKEN.get(), 4, 5,
                FlavorImmersedDaily.SHARPKNIFE.get(), "切肉");
        addDropRecipe(recipes, FlavorImmersedDaily.DEADCHICKEN.get(), 4, 6,
                FlavorImmersedDaily.SHARPKNIFE.get(), "回收");

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
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(FlavorImmersedDaily.FRIDGE_ITEM.get()),
                FridgeTemperingCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(FlavorImmersedDaily.FRIDGE_ITEM.get()),
                FridgeFreezingCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(FlavorImmersedDaily.EGGBREAKINGMACHINE_ITEM.get()),
                EggBreakingMachineCategory.TYPE);
    }

    private void buildBasinRecipes(List<WoodBasinInfoRecipe> recipes) {
        List<ItemStack> washOutputs = new ArrayList<>();
        washOutputs.add(new ItemStack(FlavorImmersedDaily.PLUCKEDCHICKEN.get()));
        for (String itemId : Config.washedChickenDrops) {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemId));
            if (item != null) {
                washOutputs.add(new ItemStack(item));
            }
        }
        recipes.add(new WoodBasinInfoRecipe(ItemStack.EMPTY, washOutputs,
                "jei.flavor_immersed_daily.woodbasin_info.wash_desc"));

        for (Map.Entry<String, String> entry : WoodBasinBlock.getFruitToJamMap().entrySet()) {
            ItemStack fruitInput = WoodBasinInfoRecipe.itemFromId(entry.getKey());
            List<ItemStack> jamOutput = new ArrayList<>();
            ItemStack jam = WoodBasinInfoRecipe.itemFromId(entry.getValue());
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
}
