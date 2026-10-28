package com.fidtest.integration.emi;

import com.fidtest.ExampleMod;
import com.fidtest.choppingboard.ChoppingboardMobDefinition;
import com.fidtest.choppingboard.ChoppingboardMobRegistry;
import com.fidtest.choppingboard.ChoppingboardWrapRecipes;
import com.fidtest.integration.FdCookingCompat;
import com.fidtest.integration.FdCuttingCompat;
import com.fidtest.recipe.ApplianceType;
import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlocks;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// EMI 集成入口 —— 展示砧板配方：
// <ul>
// <li>切割配方：本模组砧板自带配方 + 农夫乐事（含附属）引入的刀具切割配方（重叠时优先本模组）；</li>
// <li>「小型生物屠宰」配方（数据包 choppingboard_mob/*.json）。</li>
// </ul>
// EMI 为可选模组：未安装时本入口点不会被加载。
@EmiEntrypoint
public final class FidworkEmiPlugin implements EmiPlugin {

    public static final EmiRecipeCategory CHOPPINGBOARD_CUTTING = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(ExampleMod.MODID, "choppingboard"),
            EmiStack.of(ModBlocks.CHOPPINGBOARD.asItem()));

    public static final EmiRecipeCategory CHOPPINGBOARD_WRAP = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(ExampleMod.MODID, "choppingboard_wrap"),
            EmiStack.of(ModBlocks.CHOPPINGBOARD.asItem()));

    public static final EmiRecipeCategory CHOPPINGBOARD_MOB = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(ExampleMod.MODID, "choppingboard_mob"),
            EmiStack.of(ModBlocks.CHOPPINGBOARD.asItem()));

// 各机器对应的 EMI 配方分类（砧板的配方由切割/包裹/屠宰分类展示，不在此列）。
    private static final Map<ApplianceType, EmiRecipeCategory> APPLIANCE_CATEGORIES =
            new EnumMap<>(ApplianceType.class);

// 变温烤箱拆分为「烘干」「烤箱」两个分类，与 JEI 一致。
    private static final EmiRecipeCategory DRYER_DRY = category("dryer_dry", ModBlocks.DRYER.asItem());
    private static final EmiRecipeCategory DRYER_OVEN = category("dryer_oven", ModBlocks.DRYER.asItem());

    static {
        for (ApplianceType app : ApplianceType.values()) {
            if (app == ApplianceType.CHOPPINGBOARD || app == ApplianceType.DRYER) continue;
            APPLIANCE_CATEGORIES.put(app, category(app.getName(), iconBlock(app)));
        }
    }

    private static EmiRecipeCategory category(String path, ItemLike icon) {
        return new EmiRecipeCategory(
                ResourceLocation.fromNamespaceAndPath(ExampleMod.MODID, path), EmiStack.of(icon));
    }

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(CHOPPINGBOARD_CUTTING);
        registry.addWorkstation(CHOPPINGBOARD_CUTTING, EmiStack.of(ModBlocks.CHOPPINGBOARD.asItem()));

        registry.addCategory(CHOPPINGBOARD_WRAP);
        registry.addWorkstation(CHOPPINGBOARD_WRAP, EmiStack.of(ModBlocks.CHOPPINGBOARD.asItem()));

        registry.addCategory(CHOPPINGBOARD_MOB);
        registry.addWorkstation(CHOPPINGBOARD_MOB, EmiStack.of(ModBlocks.CHOPPINGBOARD.asItem()));

// 各机器分类 + 工作站（与 JEI 各机器分类一一对应）
        for (Map.Entry<ApplianceType, EmiRecipeCategory> entry : APPLIANCE_CATEGORIES.entrySet()) {
            registry.addCategory(entry.getValue());
            registry.addWorkstation(entry.getValue(), EmiStack.of(iconBlock(entry.getKey())));
        }
        registry.addCategory(DRYER_DRY);
        registry.addWorkstation(DRYER_DRY, EmiStack.of(ModBlocks.DRYER.asItem()));
        registry.addCategory(DRYER_OVEN);
        registry.addWorkstation(DRYER_OVEN, EmiStack.of(ModBlocks.DRYER.asItem()));

// 砧板切割配方：本模组配方优先，再补充农夫乐事刀具切割配方
        addChoppingboardCuttingRecipes(registry);

// 各机器配方（含农夫乐事烹饪锅、营火→烤箱模式），与 JEI 侧同源
        addApplianceRecipes(registry);

// 砧板包裹/成型工序（皮+馅 → 生食；生馅饼+模具 → 生月饼；芦苇叶+糯米 → 生粽子；
// 面团/面剂子/面皮 + 擀面杖 → 下一步形态）：与 JEI 侧共用同一数据源，保证同步
        for (ChoppingboardWrapRecipes.Entry entry : ChoppingboardWrapRecipes.collect()) {
            registry.addRecipe(new FIDChoppingboardWrapEmiRecipe(entry.id(), entry));
        }

// 注册表通常已由集成服务端的 AddReloadListenerEvent 填充；为空时兜底用服务端资源管理器
// （客户端资源管理器只暴露 assets/，看不到 data/）。
        if (ChoppingboardMobRegistry.all().isEmpty()) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                ChoppingboardMobRegistry.loadFrom(server.getResourceManager());
            }
        }
        for (ChoppingboardMobDefinition definition : ChoppingboardMobRegistry.all()) {
            String safeId = definition.id().getNamespace() + "_" + definition.id().getPath().replace('/', '_');
            registry.addRecipe(new FIDChoppingboardMobEmiRecipe(
                    ResourceLocation.fromNamespaceAndPath(ExampleMod.MODID, "choppingboard_mob/" + safeId),
                    definition));
        }
    }

// 收集砧板切割配方：先注册本模组配方，再补充农夫乐事（含附属）的刀具切割配方；
// 若某输入的切割配方本模组已有覆盖，则跳过农夫乐事配方，保证本模组配方优先。
    private static void addChoppingboardCuttingRecipes(EmiRegistry registry) {
        RecipeManager recipeManager = registry.getRecipeManager();
        var clientLevel = net.minecraft.client.Minecraft.getInstance().level;
        var registries = clientLevel != null ? clientLevel.registryAccess() : null;

// 本模组砧板自带配方（appliance = choppingboard）
        List<RecipeHolder<CookingRecipe>> ownRecipes = new ArrayList<>();
        for (RecipeHolder<CookingRecipe> holder : recipeManager.getAllRecipesFor(ModRecipeTypes.COOKING.get())) {
            if ("choppingboard".equals(holder.value().getAppliance())) {
                ownRecipes.add(holder);
            }
        }

        Set<Item> coveredInputs = new HashSet<>();
        for (RecipeHolder<CookingRecipe> holder : ownRecipes) {
            List<Ingredient> ingredients = holder.value().getIngredients();
            if (ingredients.isEmpty()) continue;
            for (ItemStack stack : ingredients.get(0).getItems()) {
                coveredInputs.add(stack.getItem());
            }
            List<ItemStack> results = holder.value().getDisplayResults();
            if (results.isEmpty()) continue;
            registry.addRecipe(FIDChoppingboardCuttingEmiRecipe.ofOwn(
                    holder.id(), ingredients.get(0), results));
        }

// 农夫乐事切割配方：仅刀具配方，且与本模组重叠时跳过
        for (FdCuttingCompat.Cutting cutting : FdCuttingCompat.all(recipeManager)) {
            if (!FdCuttingCompat.usesKnife(cutting)) continue;

            boolean overlap = false;
            for (ItemStack stack : cutting.input().getItems()) {
                if (coveredInputs.contains(stack.getItem())) {
                    overlap = true;
                    break;
                }
            }
            if (overlap) continue;

            List<ItemStack> results = FdCuttingCompat.readResults(cutting.recipe(), registries);
            if (results.isEmpty()) continue;

            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                    ExampleMod.MODID, "fd_cutting/" + cutting.id().getPath());
            registry.addRecipe(FIDChoppingboardCuttingEmiRecipe.ofFd(id, cutting.input(), results));
        }
    }

// 注册全部机器配方，过滤逻辑与 JEI 侧严格一致：
// <ul>
// <li>按配方的 appliance 字段匹配对应机器分类；</li>
// <li>锅额外接入农夫乐事烹饪锅配方（FdCookingCompat）；</li>
// <li>变温烤箱拆分为「烘干」（mode 空/dry）与「烤箱」（mode=oven）两个分类，
// 烤箱模式再追加全部营火配方。</li>
// </ul>
    private static void addApplianceRecipes(EmiRegistry registry) {
        RecipeManager recipeManager = registry.getRecipeManager();
        var clientLevel = net.minecraft.client.Minecraft.getInstance().level;
        HolderLookup.Provider registries = clientLevel != null ? clientLevel.registryAccess() : null;

        List<RecipeHolder<CookingRecipe>> all =
                recipeManager.getAllRecipesFor(ModRecipeTypes.COOKING.get());

// 各机器（砧板与变温烤箱单独处理）
        for (Map.Entry<ApplianceType, EmiRecipeCategory> entry : APPLIANCE_CATEGORIES.entrySet()) {
            ApplianceType appliance = entry.getKey();
            EmiRecipeCategory category = entry.getValue();
            for (RecipeHolder<CookingRecipe> holder : all) {
                String a = holder.value().getAppliance();
                if (!a.isEmpty() && !a.equals(appliance.getName())) continue;
                registry.addRecipe(applianceRecipe(category, holder.id(), appliance, holder.value()));
            }
// 锅：追加农夫乐事烹饪锅配方
            if (appliance == ApplianceType.POT) {
                addFdCookingRecipes(registry, category, recipeManager, registries);
            }
        }

// 变温烤箱：烘干模式（mode 为空或 dry）
        for (RecipeHolder<CookingRecipe> holder : all) {
            CookingRecipe recipe = holder.value();
            String a = recipe.getAppliance();
            if (!a.isEmpty() && !a.equals(ApplianceType.DRYER.getName())) continue;
            String mode = recipe.getMode();
            if (mode.isEmpty() || "dry".equals(mode)) {
                registry.addRecipe(applianceRecipe(DRYER_DRY, holder.id(), ApplianceType.DRYER, recipe));
            }
        }
// 变温烤箱：烤箱模式（mode=oven）
        for (RecipeHolder<CookingRecipe> holder : all) {
            CookingRecipe recipe = holder.value();
            String a = recipe.getAppliance();
            if (!a.isEmpty() && !a.equals(ApplianceType.DRYER.getName())) continue;
            if ("oven".equals(recipe.getMode())) {
                registry.addRecipe(applianceRecipe(DRYER_OVEN, holder.id(), ApplianceType.DRYER, recipe));
            }
        }
// 营火配方 → 烤箱模式
        if (registries != null) {
            for (RecipeHolder<CampfireCookingRecipe> holder
                    : recipeManager.getAllRecipesFor(RecipeType.CAMPFIRE_COOKING)) {
                CampfireCookingRecipe recipe = holder.value();
                ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                        ExampleMod.MODID, "campfire_compat/" + holder.id().getPath());
                registry.addRecipe(new FIDApplianceEmiRecipe(DRYER_OVEN, id, ApplianceType.DRYER,
                        recipe.getIngredients(), List.of(recipe.getResultItem(registries)),
                        ItemStack.EMPTY, recipe.getCookingTime(), recipe.getExperience()));
            }
        }
    }

// 农夫乐事烹饪锅配方 → 本模组锅分类（与 JEI 侧同源）。
    private static void addFdCookingRecipes(EmiRegistry registry, EmiRecipeCategory category,
                                            RecipeManager recipeManager, HolderLookup.Provider registries) {
        for (FdCookingCompat.Cooking cooking : FdCookingCompat.all(recipeManager, registries)) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                    ExampleMod.MODID, "fd_compat/" + cooking.id().getPath());
            registry.addRecipe(new FIDApplianceEmiRecipe(category, id, ApplianceType.POT,
                    cooking.ingredients(), List.of(cooking.result()), cooking.container(),
                    cooking.cookTime(), cooking.experience()));
        }
    }

// 把一条本模组烹饪配方包装成 EMI 机器配方。
    private static FIDApplianceEmiRecipe applianceRecipe(EmiRecipeCategory category, ResourceLocation id,
                                                         ApplianceType appliance, CookingRecipe recipe) {
        return new FIDApplianceEmiRecipe(category, id, appliance, recipe.getIngredients(),
                recipe.getDisplayResults(), recipe.getContainer().orElse(ItemStack.EMPTY),
                recipe.getCookingTime(), recipe.getExperience());
    }

    private static Block iconBlock(ApplianceType app) {
        return switch (app) {
            case STEAMBOX -> ModBlocks.STEAMBOX.get();
            case WOK -> ModBlocks.WOK.get();
            case PRESSURECOOKER -> ModBlocks.PRESSURECOOKER.get();
            case CHOPPINGBOARD -> ModBlocks.CHOPPINGBOARD.get();
            case FRYINGPAN -> ModBlocks.FRYINGPAN.get();
            case DRYER -> ModBlocks.DRYER.get();
            case SQUEEZINGMACHINE -> ModBlocks.SQUEEZINGMACHINE.get();
            case JAR -> ModBlocks.JAR.get();
            case TEAPOT -> ModBlocks.TEAPOT.get();
            case POT -> ModBlocks.POT.get();
            case MORTAR -> ModBlocks.MORTAR.get();
            case MEATMINCER -> ModBlocks.MEATMINCER.get();
        };
    }
}
