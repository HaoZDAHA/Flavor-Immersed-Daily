package com.flavor_immersed_daily.integration.emi;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.butchering.ButcheringDefinition;
import com.flavor_immersed_daily.butchering.ButcheringRegistry;
import com.flavor_immersed_daily.basin.BasinWashDefinition;
import com.flavor_immersed_daily.basin.BasinWashRegistry;
import com.flavor_immersed_daily.appraisal.AppraisalSupplementRegistry;
import com.flavor_immersed_daily.config.Config;
import com.flavor_immersed_daily.integration.ButcheringSteps;
import com.flavor_immersed_daily.recipe.CropConversionRecipe;
import com.flavor_immersed_daily.recipe.EggBreakingRecipe;
import com.flavor_immersed_daily.recipe.FridgeFreezingRecipe;
import com.flavor_immersed_daily.recipe.FridgeTemperingRecipe;
import com.flavor_immersed_daily.recipe.ModRecipes;
import com.flavor_immersed_daily.recipe.WoodBasinRecipe;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@EmiEntrypoint
public final class FIDEmiPlugin implements EmiPlugin {
    public static final EmiRecipeCategory WOOD_BASIN = category("wood_basin", ModBlocks.WOODBASIN.asItem());
    public static final EmiRecipeCategory EGG_BREAKING = category("egg_breaking", ModBlocks.EGGBREAKINGMACHINE.asItem());
    public static final EmiRecipeCategory FRIDGE_TEMPERING = category("fridge_tempering", ModBlocks.FRIDGE.asItem());
    public static final EmiRecipeCategory FRIDGE_FREEZING = category("fridge_freezing", ModBlocks.FRIDGE.asItem());
    public static final EmiRecipeCategory BUTCHERING = category("butchering", ModBlocks.BIGHOOK.asItem());
    public static final EmiRecipeCategory APPRAISAL = category("agricultural_appraisal",
            ModBlocks.AGRICULTURALAPPRAISALMACHINE.asItem());
    public static final EmiRecipeCategory CROP_CONVERSION = category("crop_conversion",
            ModBlocks.AGRICULTURALAPPRAISALMACHINE.asItem());
    public static final EmiRecipeCategory MOONCAKE_MOLD = category("mooncake_mold", ModItems.MOONCAKEMOLD.get());

    private static EmiRecipeCategory category(String path, net.minecraft.world.level.ItemLike icon) {
        return new EmiRecipeCategory(id(path), EmiStack.of(icon));
    }

    static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, path);
    }

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(WOOD_BASIN);
        registry.addCategory(EGG_BREAKING);
        registry.addCategory(FRIDGE_TEMPERING);
        registry.addCategory(FRIDGE_FREEZING);
        registry.addCategory(BUTCHERING);
        registry.addCategory(APPRAISAL);
        registry.addCategory(CROP_CONVERSION);
        registry.addCategory(MOONCAKE_MOLD);

        registry.addWorkstation(WOOD_BASIN, EmiStack.of(ModBlocks.WOODBASIN.asItem()));
        registry.addWorkstation(EGG_BREAKING, EmiStack.of(ModBlocks.EGGBREAKINGMACHINE.asItem()));
        registry.addWorkstation(FRIDGE_TEMPERING, EmiStack.of(ModBlocks.FRIDGE.asItem()));
        registry.addWorkstation(FRIDGE_FREEZING, EmiStack.of(ModBlocks.FRIDGE.asItem()));
        registry.addWorkstation(BUTCHERING, EmiStack.of(ModBlocks.BIGHOOK.asItem()));
        registry.addWorkstation(APPRAISAL, EmiStack.of(ModBlocks.AGRICULTURALAPPRAISALMACHINE.asItem()));
        registry.addWorkstation(CROP_CONVERSION, EmiStack.of(ModBlocks.AGRICULTURALAPPRAISALMACHINE.asItem()));
        registry.addWorkstation(MOONCAKE_MOLD, EmiStack.of(ModItems.MOONCAKEMOLD.get()));

        for (RecipeHolder<WoodBasinRecipe> holder : registry.getRecipeManager()
                .getAllRecipesFor(ModRecipes.WOOD_BASIN_TYPE.get())) {
            registry.addRecipe(FIDEmiRecipe.woodBasin(holder));
        }
// 木盆内置洗鸡（未命中数据包 basin_wash 定义时的默认产出：拔好毛的鸡 + 附加掉落），与 JEI 侧一致
        List<ItemStack> washOutputs = new ArrayList<>();
        washOutputs.add(new ItemStack(ModItems.CHICKENWITHOUTFEATHER.get()));
        for (String itemId : Config.washedChickenDrops) {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemId));
            if (item != null) {
                washOutputs.add(new ItemStack(item));
            }
        }
        registry.addRecipe(FIDEmiRecipe.woodBasinWashInfo(washOutputs));
// 数据包自定义泡洗：木盆 输入物品 → 产物（与 JEI 侧逻辑一致）
        if (BasinWashRegistry.all().isEmpty()) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                BasinWashRegistry.loadFrom(server.getResourceManager());
            }
        }
        for (BasinWashDefinition definition : BasinWashRegistry.all()) {
            if (definition.output() == null && definition.drops().isEmpty()) {
                continue;
            }
            registry.addRecipe(FIDEmiRecipe.basinWash(definition));
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
// 数据包自定义屠宰定义（其他模组 / 数据包通过 data/<ns>/butchering/*.json 接入）
        addCustomButcheringRecipes(registry);

// 农产鉴定：掉落物来自 JSON 默认表 + config 额外产物 + 数据包补充产物（见 Config.getWildDropsMap）
        if (AppraisalSupplementRegistry.all().isEmpty()) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                AppraisalSupplementRegistry.loadFrom(server.getResourceManager());
            }
        }
        for (Map.Entry<String, List<String>> entry : Config.getWildDropsMap().entrySet()) {
            Item inputItem = BuiltInRegistries.ITEM.get(ResourceLocation.parse(entry.getKey()));
            List<ItemStack> outputs = buildDropStacks(entry.getValue());
            if (inputItem == null || outputs.isEmpty()) {
                continue;
            }
// 输入物品 id 形如 flavor_immersed_daily:xxx，ResourceLocation 的 path 不允许冒号，做一下替换
            String safeInput = entry.getKey().replace(':', '_');
            registry.addRecipe(new FIDAppraisalEmiRecipe(
                    id("appraisal/" + safeInput), new ItemStack(inputItem), outputs));
        }

// 作物转换：每个转换组内每个已安装物品各生成一条配方，输入为该物品、输出为组内其他已安装物品（与机器实际行为一致）
        int cropIndex = 0;
        for (RecipeHolder<CropConversionRecipe> holder : registry.getRecipeManager()
                .getAllRecipesFor(ModRecipes.CROP_CONVERSION_TYPE.get())) {
            CropConversionRecipe recipe = holder.value();
            for (Item inputItem : recipe.getLoadedItems(null)) {
                List<ItemStack> outputs = new ArrayList<>();
                for (Item outputItem : recipe.getLoadedItems(inputItem)) {
                    outputs.add(new ItemStack(outputItem));
                }
                if (outputs.isEmpty()) {
                    continue;
                }
                registry.addRecipe(new FIDCropConversionEmiRecipe(
                        id("crop_conversion/" + cropIndex++), new ItemStack(inputItem), outputs));
            }
        }

// 月饼模具：压制链路 + 信息页
        addMooncakeMoldRecipe(registry, "mooncake", ModItems.RAWFLOURPANCAKE.get(),
                ModItems.RAWMOONCAKE.get(), ModItems.MOONCAKE.get());
        addMooncakeMoldRecipe(registry, "cantonese_mooncake", ModItems.SWEET_OIL_DOUGH.get(),
                ModItems.RAWCANTONESE_MOONCAKE.get(), ModItems.CANTONESE_MOONCAKE.get());
        addMooncakeMoldRecipe(registry, "suzhou_mooncake", ModItems.WATER_SHORT_DOUGH.get(),
                ModItems.RAWSUZHOU_MOONCAKE.get(), ModItems.SUZHOU_MOONCAKE.get());
        addMooncakeMoldRecipe(registry, "beijing_mooncake", ModItems.SYRUP_DOUGH.get(),
                ModItems.RAWBEIJING_MOONCAKE.get(), ModItems.BEIJING_MOONCAKE.get());
        addMooncakeMoldRecipe(registry, "snow_skin_mooncake", ModItems.GLUTINOUS_MILK_DOUGH.get(),
                ModItems.RAWSNOW_SKIN_MOONCAKE.get(), ModItems.SNOW_SKIN_MOONCAKE.get());

        addInfo(registry, "mooncake_mold", ModItems.MOONCAKEMOLD.get(), List.of(
                Component.translatable("jei.flavor_immersed_daily.mooncake_mold.info.usage"),
                Component.translatable("jei.flavor_immersed_daily.mooncake_mold.info.press"),
                Component.translatable("jei.flavor_immersed_daily.mooncake_mold.info.kinds")));
        addMooncakeInfo(registry, "cantonese_mooncake", ModItems.CANTONESE_MOONCAKE.get(), "reunion_recovery");
        addMooncakeInfo(registry, "suzhou_mooncake", ModItems.SUZHOU_MOONCAKE.get(), "crumbly_crust");
        addMooncakeInfo(registry, "beijing_mooncake", ModItems.BEIJING_MOONCAKE.get(), "eclipse_devour");
        addMooncakeInfo(registry, "snow_skin_mooncake", ModItems.SNOW_SKIN_MOONCAKE.get(), "cool_skin");
    }

// 单条月饼模具压制链路：月饼模具 + 饼皮 → 生月饼 → 成品月饼
    private static void addMooncakeMoldRecipe(EmiRegistry registry, String path, Item dough, Item raw, Item finished) {
        registry.addRecipe(new FIDMooncakeMoldEmiRecipe(id("mooncake_mold/" + path),
                new ItemStack(ModItems.MOONCAKEMOLD.get()), new ItemStack(dough),
                new ItemStack(raw), new ItemStack(finished)));
    }

// 特色月饼信息页：效果说明 + 由月饼模具压制而生
    private static void addMooncakeInfo(EmiRegistry registry, String path, Item mooncake, String effectId) {
        addInfo(registry, path, mooncake, List.of(
                buffDesc(effectId),
                Component.translatable("jei.flavor_immersed_daily.mooncake_mold.info.relation")));
    }

// 注册一条 EMI 信息页（对应 JEI 的 addIngredientInfo）
    private static void addInfo(EmiRegistry registry, String path, Item item, List<Component> lines) {
        registry.addRecipe(new EmiInfoRecipe(List.<EmiIngredient>of(EmiStack.of(item)), lines, id("info/" + path)));
    }

// 「食用后获得「XX」效果：说明」
    private static Component buffDesc(String effectId) {
        return Component.translatable("jei.flavor_immersed_daily.mooncake_mold.buff_line",
                Component.translatable("effect." + FlavorImmersedDaily.MODID + "." + effectId),
                Component.translatable("effect." + FlavorImmersedDaily.MODID + "." + effectId + ".description"));
    }

// 数据包定义的屠宰步骤 → EMI 配方（与 JEI 侧逻辑一致）。
// 注册表已由集成服务端的 data 资源包填充；为空时兜底改用服务端的数据资源管理器，
// 不能用客户端资源管理器（只暴露 assets/，看不到 data/）。标签刀具或空产出的阶段跳过。
    private static void addCustomButcheringRecipes(EmiRegistry registry) {
        if (ButcheringRegistry.all().isEmpty()) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                ButcheringRegistry.loadFrom(server.getResourceManager());
            }
        }
        for (ButcheringDefinition definition : ButcheringRegistry.all()) {
            List<ButcheringDefinition.Stage> stages = definition.stages();
            for (int i = 0; i < stages.size(); i++) {
                ButcheringDefinition.Stage stage = stages.get(i);
                if (stage.toolItem() == null || stage.drops().isEmpty()) {
                    continue;
                }
                List<ItemStack> outputs = new ArrayList<>();
                for (Item drop : stage.drops()) {
                    outputs.add(new ItemStack(drop));
                }
                ItemStack tool = new ItemStack(stage.toolItem());
                String stageName = stage.name() != null ? stage.name() : tool.getHoverName().getString();
                String safeId = definition.id().getNamespace() + "_" + definition.id().getPath().replace('/', '_');
                registry.addRecipe(new FIDButcheringEmiRecipe(id("butchering/custom/" + safeId + "/" + i),
                        new ItemStack(definition.input()), tool, stageName, outputs));
            }
        }
    }

// 把掉落物 id 解析成物品栈列表。
    private static List<ItemStack> buildDropStacks(List<String> dropIds) {
        List<ItemStack> result = new ArrayList<>();
        for (String dropId : dropIds) {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(dropId));
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
