package com.flavor_immersed_daily.integration.jei;

import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.all.ModBlocks;

import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.butchering.ButcheringDefinition;
import com.flavor_immersed_daily.butchering.ButcheringRegistry;
import com.flavor_immersed_daily.basin.BasinWashDefinition;
import com.flavor_immersed_daily.basin.BasinWashRegistry;
import com.flavor_immersed_daily.appraisal.AppraisalSupplementRegistry;
import com.flavor_immersed_daily.integration.ButcheringSteps;
import com.flavor_immersed_daily.recipe.CropConversionRecipe;
import com.flavor_immersed_daily.recipe.EggBreakingRecipe;
import com.flavor_immersed_daily.recipe.FridgeTemperingRecipe;
import com.flavor_immersed_daily.recipe.FridgeFreezingRecipe;
import com.flavor_immersed_daily.recipe.ModRecipes;
import com.flavor_immersed_daily.recipe.WoodBasinRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(FlavorImmersedDaily.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ButcheringRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new WoodBasinInfoCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FridgeTemperingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FridgeFreezingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new EggBreakingMachineCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AgriculturalAppraisalMachineCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CropConversionCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new MooncakeMoldCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<ButcheringRecipe> recipes = new ArrayList<>();
        for (ButcheringSteps.Step step : ButcheringSteps.all()) {
            List<ItemStack> outputs = buildOutputs(step.animalType(), step.stage());
            if (outputs.isEmpty()) {
                continue;
            }
            recipes.add(new ButcheringRecipe(new ItemStack(step.input()), new ItemStack(step.tool()),
                    step.stageName(), outputs));
        }
// 数据包自定义屠宰定义（其他模组 / 数据包通过 data/<ns>/butchering/*.json 接入）
        buildCustomButcheringRecipes(recipes);
        registration.addRecipes(ButcheringRecipeCategory.TYPE, recipes);

// 木盆信息页
        List<WoodBasinInfoRecipe> basinRecipes = new ArrayList<>();
        buildBasinRecipes(basinRecipes);
        registration.addRecipes(WoodBasinInfoCategory.TYPE, basinRecipes);

// 冰箱·变温室
        List<FridgeTemperingRecipe> temperingRecipes = new ArrayList<>();
        buildFridgeTemperingRecipes(temperingRecipes);
        registration.addRecipes(FridgeTemperingCategory.TYPE, temperingRecipes);

// 冰箱·冷冻室
        List<FridgeFreezingRecipe> freezingRecipes = new ArrayList<>();
        buildFridgeFreezingRecipes(freezingRecipes);
        registration.addRecipes(FridgeFreezingCategory.TYPE, freezingRecipes);

// 打蛋机
        List<EggBreakingRecipe> eggBreakingRecipes = new ArrayList<>();
        buildEggBreakingRecipes(eggBreakingRecipes);
        registration.addRecipes(EggBreakingMachineCategory.TYPE, eggBreakingRecipes);

// 农产鉴定机
        List<AgriculturalAppraisalMachineRecipe> appraisalRecipes = new ArrayList<>();
        buildAgriculturalAppraisalRecipes(appraisalRecipes);
        registration.addRecipes(AgriculturalAppraisalMachineCategory.TYPE, appraisalRecipes);

// 作物转换
        List<CropConversionJeiRecipe> cropConversionRecipes = new ArrayList<>();
        buildCropConversionRecipes(cropConversionRecipes);
        registration.addRecipes(CropConversionCategory.TYPE, cropConversionRecipes);

// 月饼模具
        List<MooncakeMoldRecipe> mooncakeMoldRecipes = new ArrayList<>();
        buildMooncakeMoldRecipes(mooncakeMoldRecipes);
        registration.addRecipes(MooncakeMoldCategory.TYPE, mooncakeMoldRecipes);

// 月饼模具的详细用法说明
        registration.addIngredientInfo(new ItemStack(ModItems.MOONCAKEMOLD.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.flavor_immersed_daily.mooncake_mold.info.usage"),
                Component.translatable("jei.flavor_immersed_daily.mooncake_mold.info.press"),
                Component.translatable("jei.flavor_immersed_daily.mooncake_mold.info.kinds"));

// 四种特色月饼的效果说明，并说明由月饼模具压制而生
        addMooncakeInfo(registration, ModItems.CANTONESE_MOONCAKE.get(), "reunion_recovery");
        addMooncakeInfo(registration, ModItems.SUZHOU_MOONCAKE.get(), "crumbly_crust");
        addMooncakeInfo(registration, ModItems.BEIJING_MOONCAKE.get(), "eclipse_devour");
        addMooncakeInfo(registration, ModItems.SNOW_SKIN_MOONCAKE.get(), "cool_skin");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FRIDGE.get().asItem()),
                FridgeTemperingCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FRIDGE.get().asItem()),
                FridgeFreezingCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.EGGBREAKINGMACHINE.get().asItem()),
                EggBreakingMachineCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.AGRICULTURALAPPRAISALMACHINE.get().asItem()),
                AgriculturalAppraisalMachineCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.AGRICULTURALAPPRAISALMACHINE.get().asItem()),
                CropConversionCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.WOODBASIN.get().asItem()), WoodBasinInfoCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.MOONCAKEMOLD.get()), MooncakeMoldCategory.TYPE);
    }

    private void buildBasinRecipes(List<WoodBasinInfoRecipe> recipes) {
        List<ItemStack> washOutputs = new ArrayList<>();
        washOutputs.add(new ItemStack(ModItems.CHICKENWITHOUTFEATHER.get()));
        for (String itemId : Config.washedChickenDrops) {
            Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(itemId));
            if (item != null) {
                washOutputs.add(new ItemStack(item));
            }
        }
        recipes.add(new WoodBasinInfoRecipe(ItemStack.EMPTY, washOutputs,
                "jei.flavor_immersed_daily.woodbasin_info.wash_desc"));

// 数据包自定义泡洗定义（其他模组 / 数据包通过 data/<ns>/basin_wash/*.json 接入）
        if (BasinWashRegistry.all().isEmpty()) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                BasinWashRegistry.loadFrom(server.getResourceManager());
            }
        }
        for (BasinWashDefinition definition : BasinWashRegistry.all()) {
            List<ItemStack> washResults = new ArrayList<>();
            if (definition.output() != null) {
                washResults.add(new ItemStack(definition.output()));
            }
            for (Item drop : definition.drops()) {
                washResults.add(new ItemStack(drop));
            }
            if (washResults.isEmpty()) {
                continue;
            }
            recipes.add(new WoodBasinInfoRecipe(new ItemStack(definition.input()), washResults,
                    "jei.flavor_immersed_daily.woodbasin_info.wash_desc"));
        }

        var level = Minecraft.getInstance().level;
        if (level == null) return;
        for (WoodBasinRecipe recipe : level.getRecipeManager().getAllRecipesFor(ModRecipes.WOOD_BASIN_TYPE.get())) {
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
        for (var recipe : level.getRecipeManager().getRecipes()) {
            if (recipe instanceof FridgeTemperingRecipe r) {
                recipes.add(r);
            }
        }
    }

    private void buildFridgeFreezingRecipes(List<FridgeFreezingRecipe> recipes) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        for (var recipe : level.getRecipeManager().getRecipes()) {
            if (recipe instanceof FridgeFreezingRecipe r) {
                recipes.add(r);
            }
        }
    }

    private List<ItemStack> buildOutputs(int animalType, int stage) {
        List<String> dropIds = Config.getDrops(animalType, stage);
        List<ItemStack> outputs = new ArrayList<>();
        for (String itemId : dropIds) {
            Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(itemId));
            if (item != null) {
                outputs.add(new ItemStack(item));
            }
        }
        return outputs;
    }

// 数据包定义的屠宰步骤 → JEI 配方。
// 大挂钩的自定义屠宰流程由 data/<ns>/butchering/*.json 描述，
// 这里把每个阶段转成一条「死动物 + 刀具 → 掉落物」的配方，使附属模组的屠宰产物
// 与内置四种动物一样出现在屠宰 JEI 分类里。
    private void buildCustomButcheringRecipes(List<ButcheringRecipe> recipes) {
// 单机 / 局域网：注册表已由集成服务端的 AddReloadListenerEvent（data 资源包）填充。
// 注意不能用 Minecraft.getInstance().getResourceManager()：那是 CLIENT_RESOURCES 管理器，只暴露 assets/ 看不到 data/，
// 扫不到任何定义还会用空表清空注册表。注册表为空时兜底改用服务端的数据资源管理器。
        if (ButcheringRegistry.all().isEmpty()) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                ButcheringRegistry.loadFrom(server.getResourceManager());
            }
        }
        for (ButcheringDefinition definition : ButcheringRegistry.all()) {
            for (ButcheringDefinition.Stage stage : definition.stages()) {
// JEI 无法渲染标签刀具，缺少具体刀具物品的阶段跳过
                if (stage.toolItem() == null || stage.drops().isEmpty()) {
                    continue;
                }
                ItemStack tool = new ItemStack(stage.toolItem());
                List<ItemStack> outputs = new ArrayList<>();
                for (Item drop : stage.drops()) {
                    outputs.add(new ItemStack(drop));
                }
                recipes.add(new ButcheringRecipe(new ItemStack(definition.input()), tool,
                        tool.getHoverName().getString(), outputs));
            }
        }
    }

    private void buildEggBreakingRecipes(List<EggBreakingRecipe> recipes) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        for (var recipe : level.getRecipeManager().getRecipes()) {
            if (recipe instanceof EggBreakingRecipe r) {
                recipes.add(r);
            }
        }
    }

    private void buildAgriculturalAppraisalRecipes(List<AgriculturalAppraisalMachineRecipe> recipes) {
// 数据包补充产物（data/<ns>/appraisal_supplement/*.json）：注册表为空时兜底用服务端数据资源管理器
        if (AppraisalSupplementRegistry.all().isEmpty()) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                AppraisalSupplementRegistry.loadFrom(server.getResourceManager());
            }
        }
        Map<String, List<String>> wildDrops = Config.getWildDropsMap();
        for (Map.Entry<String, List<String>> entry : wildDrops.entrySet()) {
            String inputId = entry.getKey();
            List<String> dropIds = entry.getValue();
            if (dropIds.isEmpty()) continue;

            Item inputItem = BuiltInRegistries.ITEM.get(new ResourceLocation(inputId));
            if (inputItem == null) continue;

            List<ItemStack> outputs = new ArrayList<>();
            for (String dropId : dropIds) {
                Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(dropId));
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

// 作物转换：每个转换组内每个已安装物品各生成一条 JEI 配方，
// 输入为该物品、输出为组内其他已安装物品（与机器实际行为一致）。
    private void buildCropConversionRecipes(List<CropConversionJeiRecipe> recipes) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        for (CropConversionRecipe recipe :
                level.getRecipeManager().getAllRecipesFor(ModRecipes.CROP_CONVERSION_TYPE.get())) {
            for (Item inputItem : recipe.getLoadedItems(null)) {
                List<ItemStack> outputs = new ArrayList<>();
                for (Item outputItem : recipe.getLoadedItems(inputItem)) {
                    outputs.add(new ItemStack(outputItem));
                }
                if (!outputs.isEmpty()) {
                    recipes.add(new CropConversionJeiRecipe(new ItemStack(inputItem), outputs));
                }
            }
        }
    }

// 月饼模具：每种饼皮对应一条压制链路
// 「月饼模具 + 饼皮 → 生月饼 → 成品月饼」，与 com.flavor_immersed_daily.item.MooncakeMoldItem
// 的饼皮映射保持一致。
    private void buildMooncakeMoldRecipes(List<MooncakeMoldRecipe> recipes) {
        recipes.add(new MooncakeMoldRecipe(
                new ItemStack(ModItems.RAWFLOURPANCAKE.get()),
                new ItemStack(ModItems.RAWMOONCAKE.get()),
                new ItemStack(ModItems.HARD_MOONCAKE.get()),
                List.of(Component.translatable("jei.flavor_immersed_daily.mooncake_mold.desc.plain"))));

        recipes.add(new MooncakeMoldRecipe(
                new ItemStack(ModItems.SWEET_OIL_DOUGH.get()),
                new ItemStack(ModItems.RAWCANTONESE_MOONCAKE.get()),
                new ItemStack(ModItems.CANTONESE_MOONCAKE.get()),
                List.of(buffDesc("reunion_recovery"))));

        recipes.add(new MooncakeMoldRecipe(
                new ItemStack(ModItems.WATER_SHORT_DOUGH.get()),
                new ItemStack(ModItems.RAWSUZHOU_MOONCAKE.get()),
                new ItemStack(ModItems.SUZHOU_MOONCAKE.get()),
                List.of(buffDesc("crumbly_crust"))));

        recipes.add(new MooncakeMoldRecipe(
                new ItemStack(ModItems.SYRUP_DOUGH.get()),
                new ItemStack(ModItems.RAWBEIJING_MOONCAKE.get()),
                new ItemStack(ModItems.BEIJING_MOONCAKE.get()),
                List.of(buffDesc("eclipse_devour"))));

        recipes.add(new MooncakeMoldRecipe(
                new ItemStack(ModItems.GLUTINOUS_MILK_DOUGH.get()),
                new ItemStack(ModItems.RAWSNOW_SKIN_MOONCAKE.get()),
                new ItemStack(ModItems.SNOW_SKIN_MOONCAKE.get()),
                List.of(buffDesc("cool_skin"))));
    }

// 特色月饼的信息页 — 效果说明 + 与月饼模具的关系
    private void addMooncakeInfo(IRecipeRegistration registration, Item mooncake, String effectId) {
        registration.addIngredientInfo(new ItemStack(mooncake), VanillaTypes.ITEM_STACK,
                buffDesc(effectId),
                Component.translatable("jei.flavor_immersed_daily.mooncake_mold.info.relation"));
    }

// 组合「效果名 + 效果说明」的月饼 buff 描述
    private static Component buffDesc(String effectId) {
        return Component.translatable("jei.flavor_immersed_daily.mooncake_mold.buff_line",
                Component.translatable("effect." + FlavorImmersedDaily.MODID + "." + effectId),
                Component.translatable("effect." + FlavorImmersedDaily.MODID + "." + effectId + ".description"));
    }
}
