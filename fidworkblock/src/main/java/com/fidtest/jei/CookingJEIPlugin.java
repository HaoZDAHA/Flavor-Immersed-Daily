package com.fidtest.jei;

import com.fidtest.FidworkMod;
import com.fidtest.choppingboard.ChoppingboardMobDefinition;
import com.fidtest.choppingboard.ChoppingboardMobRegistry;
import com.fidtest.choppingboard.ChoppingboardWrapRecipes;
import com.fidtest.integration.FdCuttingCompat;
import com.fidtest.recipe.ApplianceType;
import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlocks;
import com.fidtest.registration.ModMenuTypes;
import com.fidtest.screen.CookingMenu;
import com.fidtest.screen.CookingScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@JeiPlugin
public class CookingJEIPlugin implements IModPlugin {

    private static final ResourceLocation UID = new ResourceLocation(FidworkMod.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() { return UID; }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        var helper = reg.getJeiHelpers().getGuiHelper();
        reg.addRecipeCategories(new PotRecipeCategory(helper, ModBlocks.POT.get()));
        reg.addRecipeCategories(new CookingRecipeCategory(helper, ApplianceType.PRESSURECOOKER, ModBlocks.PRESSURECOOKER.get()));
        reg.addRecipeCategories(new ChoppingboardRecipeCategory(helper, ModBlocks.CHOPPINGBOARD.get()));
        reg.addRecipeCategories(new ChoppingboardWrapRecipeCategory(helper, ModBlocks.CHOPPINGBOARD.get()));
        reg.addRecipeCategories(new ChoppingboardMobRecipeCategory(helper, ModBlocks.CHOPPINGBOARD.get()));
        reg.addRecipeCategories(new MortarRecipeCategory(helper, ModBlocks.MORTAR.get()));
        reg.addRecipeCategories(new SqueezingmachineRecipeCategory(helper, ModBlocks.SQUEEZINGMACHINE.get()));
// 变温烤箱拆分为两个类别：烘干模式 + 烤箱模式
        reg.addRecipeCategories(new CookingRecipeCategory(helper, ApplianceType.DRYER, ModBlocks.DRYER.get(), "dry"));
        reg.addRecipeCategories(new CookingRecipeCategory(helper, ApplianceType.DRYER, ModBlocks.DRYER.get(), "oven"));
        reg.addRecipeCategories(new FryingpanRecipeCategory(helper, ModBlocks.FRYINGPAN.get()));
        reg.addRecipeCategories(new JarRecipeCategory(helper, ModBlocks.JAR.get()));
        reg.addRecipeCategories(new TeapotRecipeCategory(helper, ModBlocks.TEAPOT.get()));
        reg.addRecipeCategories(new SteamboxRecipeCategory(helper, ModBlocks.STEAMBOX.get()));
        reg.addRecipeCategories(new WokRecipeCategory(helper, ModBlocks.WOK.get()));

    }

    @Override
    @SuppressWarnings("unchecked")
    public void registerRecipes(IRecipeRegistration reg) {
        RecipeManager rm = Minecraft.getInstance().level.getRecipeManager();
        List<CookingRecipe> allOwn = rm.getAllRecipesFor(ModRecipeTypes.COOKING.get());

        List<CookingRecipe> potRecipes = new ArrayList<>();
        potRecipes.addAll(allOwn.stream().filter(r -> matchesAppliance(r, "pot")).collect(Collectors.toList()));
        potRecipes.addAll(collectFdRecipes(rm));
        if (!potRecipes.isEmpty()) reg.addRecipes(PotRecipeCategory.TYPE, potRecipes);

        List<CookingRecipe> pcRecipes = allOwn.stream()
                .filter(r -> matchesAppliance(r, "pressurecooker")).collect(Collectors.toList());
        if (!pcRecipes.isEmpty()) reg.addRecipes(CookingRecipeCategory.pcType(), pcRecipes);

        List<CookingRecipe> cbRecipes = new ArrayList<>(allOwn.stream()
                .filter(r -> matchesAppliance(r, "choppingboard")).collect(Collectors.toList()));
// 砧板接入农夫乐事（含附属）的切割配方；与本模组配方重叠时优先本模组
        cbRecipes.addAll(collectFdCuttingRecipes(rm, cbRecipes));
        if (!cbRecipes.isEmpty()) reg.addRecipes(ChoppingboardRecipeCategory.TYPE, cbRecipes);

        List<CookingRecipe> mrRecipes = allOwn.stream()
                .filter(r -> matchesAppliance(r, "mortar")).collect(Collectors.toList());
        if (!mrRecipes.isEmpty()) reg.addRecipes(MortarRecipeCategory.TYPE, mrRecipes);

        List<CookingRecipe> sqRecipes = allOwn.stream()
                .filter(r -> matchesAppliance(r, "squeezingmachine")).collect(Collectors.toList());
        if (!sqRecipes.isEmpty()) reg.addRecipes(SqueezingmachineRecipeCategory.TYPE, sqRecipes);

// 变温烤箱·烘干模式：mode 为空或 dry
        List<CookingRecipe> drRecipes = allOwn.stream()
                .filter(r -> matchesAppliance(r, "dryer"))
                .filter(r -> r.getMode().isEmpty() || "dry".equals(r.getMode()))
                .collect(Collectors.toList());
        if (!drRecipes.isEmpty()) reg.addRecipes(CookingRecipeCategory.drType(), drRecipes);

// 变温烤箱·烤箱模式：mode 为 oven 的配方 + 所有原版营火配方
        List<CookingRecipe> ovenRecipes = new ArrayList<>(allOwn.stream()
                .filter(r -> matchesAppliance(r, "dryer"))
                .filter(r -> "oven".equals(r.getMode()))
                .collect(Collectors.toList()));
        ovenRecipes.addAll(collectCampfireRecipes(rm));
        if (!ovenRecipes.isEmpty()) reg.addRecipes(CookingRecipeCategory.ovenType(), ovenRecipes);

        List<CookingRecipe> fpRecipes = allOwn.stream()
                .filter(r -> matchesAppliance(r, "fryingpan")).collect(Collectors.toList());
        if (!fpRecipes.isEmpty()) reg.addRecipes(FryingpanRecipeCategory.TYPE, fpRecipes);

        List<CookingRecipe> jarRecipes = allOwn.stream()
                .filter(r -> matchesAppliance(r, "jar")).collect(Collectors.toList());
        if (!jarRecipes.isEmpty()) reg.addRecipes(JarRecipeCategory.TYPE, jarRecipes);

        List<CookingRecipe> tpRecipes = allOwn.stream()
                .filter(r -> matchesAppliance(r, "teapot")).collect(Collectors.toList());
        if (!tpRecipes.isEmpty()) reg.addRecipes(TeapotRecipeCategory.TYPE, tpRecipes);

        List<CookingRecipe> sbRecipes = allOwn.stream()
                .filter(r -> matchesAppliance(r, "steambox")).collect(Collectors.toList());
        if (!sbRecipes.isEmpty()) reg.addRecipes(SteamboxRecipeCategory.TYPE, sbRecipes);

        List<CookingRecipe> wokRecipes = allOwn.stream()
                .filter(r -> matchesAppliance(r, "wok")).collect(Collectors.toList());
        if (!wokRecipes.isEmpty()) reg.addRecipes(WokRecipeCategory.TYPE, wokRecipes);

// 砧板的包裹/成型工序写在方块逻辑里（不是 JSON 配方），这里合成展示用配方
        List<CookingRecipe> wrapRecipes = collectChoppingboardWrapRecipes();
        if (!wrapRecipes.isEmpty()) reg.addRecipes(ChoppingboardWrapRecipeCategory.TYPE, wrapRecipes);

// 砧板小型生物屠宰：来自数据包 data/<任意命名空间>/choppingboard_mob/*.json。
// 注册表通常已由集成服务端的 AddReloadListenerEvent 填充；为空时兜底改用服务端资源管理器。
        if (ChoppingboardMobRegistry.all().isEmpty()) {
            MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                ChoppingboardMobRegistry.loadFrom(server.getResourceManager());
            }
        }
        List<ChoppingboardMobDefinition> mobRecipes = new ArrayList<>(ChoppingboardMobRegistry.all());
        if (!mobRecipes.isEmpty()) reg.addRecipes(ChoppingboardMobRecipeCategory.TYPE, mobRecipes);

    }

// 合成砧板包裹/成型工序的展示配方：
// 皮 + 任意馅料 → 生食；生馅饼 + 月饼模具 → 生月饼；芦苇叶 + 糯米 → 生粽子；
// 面团/面剂子/面皮 + 擀面杖 → 下一步形态。
// 工序类型记录在 CookingRecipe 的 mode 字段里。
// 数据来自 ChoppingboardWrapRecipes。
    private List<CookingRecipe> collectChoppingboardWrapRecipes() {
        List<CookingRecipe> result = new ArrayList<>();
        for (ChoppingboardWrapRecipes.Entry entry : ChoppingboardWrapRecipes.collect()) {
            result.add(new CookingRecipe(entry.id(), "choppingboard", List.of(Ingredient.of(entry.input())),
                    entry.output(), Optional.empty(), 0, 0, 0, entry.mode()));
        }
        return result;
    }

// 收集农夫乐事（含附属模组）砧板切割配方并包装成本模组的展示配方。
// 只保留刀具配方（排除斧头去皮、锄头翻土等）；若某输入的切割配方本模组砧板
// 已有覆盖（即 ownRecipes 里已有该输入的配方），则跳过，保证本模组配方优先。
    private List<CookingRecipe> collectFdCuttingRecipes(RecipeManager rm, List<CookingRecipe> ownRecipes) {
        List<CookingRecipe> result = new ArrayList<>();
        RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();

// 本模组砧板已覆盖的输入物品：重叠时优先本模组配方
        Set<Item> coveredInputs = new HashSet<>();
        for (CookingRecipe recipe : ownRecipes) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                for (ItemStack stack : ingredient.getItems()) {
                    coveredInputs.add(stack.getItem());
                }
            }
        }

        for (FdCuttingCompat.Cutting cutting : FdCuttingCompat.all(rm)) {
            if (!FdCuttingCompat.usesKnife(cutting)) continue;

            boolean overlap = false;
            for (ItemStack stack : cutting.input().getItems()) {
                if (coveredInputs.contains(stack.getItem())) {
                    overlap = true;
                    break;
                }
            }
            if (overlap) continue;

            List<ItemStack> results = FdCuttingCompat.readResults(cutting.recipe(), registryAccess);
            if (results.isEmpty()) continue;

            CookingRecipe wrapped = new CookingRecipe(
                    new ResourceLocation(FidworkMod.MODID, "fd_cutting/" + cutting.id().getPath()),
                    "choppingboard", List.of(cutting.input()), results.get(0),
                    Optional.empty(), 0, 0, 0);
            wrapped.setExtraResults(results);
            result.add(wrapped);
        }
        return result;
    }

// 收集所有原版营火配方，包装成变温烤箱烤箱模式的配方（供 JEI 显示）
    private List<CookingRecipe> collectCampfireRecipes(RecipeManager rm) {
        List<CookingRecipe> result = new ArrayList<>();
        RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();
        try {
            List<CampfireCookingRecipe> campfireRecipes =
                    rm.getAllRecipesFor(RecipeType.CAMPFIRE_COOKING);
            for (CampfireCookingRecipe recipe : campfireRecipes) {
                ResourceLocation wrapId = new ResourceLocation(FidworkMod.MODID,
                        "campfire_compat/" + recipe.getId().getPath());
                result.add(new CookingRecipe(wrapId, ApplianceType.DRYER.getName(),
                        recipe.getIngredients(), recipe.getResultItem(registryAccess),
                        Optional.empty(), recipe.getCookingTime(), recipe.getExperience(), 0, "oven"));
            }
        } catch (Exception ignored) {}
        return result;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.POT.get()), PotRecipeCategory.TYPE);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.PRESSURECOOKER.get()), CookingRecipeCategory.pcType());
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.CHOPPINGBOARD.get()), ChoppingboardRecipeCategory.TYPE);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.CHOPPINGBOARD.get()), ChoppingboardWrapRecipeCategory.TYPE);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.CHOPPINGBOARD.get()), ChoppingboardMobRecipeCategory.TYPE);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.MORTAR.get()), MortarRecipeCategory.TYPE);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.SQUEEZINGMACHINE.get()), SqueezingmachineRecipeCategory.TYPE);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.DRYER.get()), CookingRecipeCategory.drType());
// 变温烤箱同时是烤箱模式类别的催化剂
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.DRYER.get()), CookingRecipeCategory.ovenType());
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.FRYINGPAN.get()), FryingpanRecipeCategory.TYPE);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.JAR.get()), JarRecipeCategory.TYPE);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.TEAPOT.get()), TeapotRecipeCategory.TYPE);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.STEAMBOX.get()), SteamboxRecipeCategory.TYPE);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.WOK.get()), WokRecipeCategory.TYPE);

    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration reg) {
        reg.addRecipeClickArea(CookingScreen.class, 104, 28, 24, 17, PotRecipeCategory.TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration reg) {
        reg.addRecipeTransferHandler(CookingMenu.class, ModMenuTypes.COOKING.get(),
                PotRecipeCategory.TYPE, 0, 7, 8, 36);
        reg.addRecipeTransferHandler(CookingMenu.class, ModMenuTypes.COOKING.get(),
                CookingRecipeCategory.pcType(), 0, 3, 4, 36);
// 变温烤箱烤箱模式的配方传输
        reg.addRecipeTransferHandler(CookingMenu.class, ModMenuTypes.COOKING.get(),
                CookingRecipeCategory.ovenType(), 0, 1, 2, 36);
    }

    private boolean matchesAppliance(CookingRecipe r, String name) {
        String a = r.getAppliance();
        return a.isEmpty() || a.equals(name);
    }

    @SuppressWarnings("unchecked")
    private List<CookingRecipe> collectFdRecipes(RecipeManager rm) {
        List<CookingRecipe> result = new ArrayList<>();
        try {
            RecipeType<?> fdType = (RecipeType<?>) BuiltInRegistries.RECIPE_TYPE
                    .get(new ResourceLocation("farmersdelight", "cooking"));
            if (fdType == null) return result;

            List<?> fdRecipes = rm.getAllRecipesFor((RecipeType) fdType);
            var regAccess = Minecraft.getInstance().level.registryAccess();

            for (Object obj : fdRecipes) {
                Recipe<?> recipe = (Recipe<?>) obj;
                try {
                    ItemStack fdResult = recipe.getResultItem(regAccess);
                    ItemStack fdContainer = getFdContainer(recipe);
                    int cookTime = 200;
                    try { Method m = recipe.getClass().getMethod("getCookTime"); cookTime = (int) m.invoke(recipe); } catch (Exception ignored) {}
                    float exp = 0F;
                    try { Method m = recipe.getClass().getMethod("getExperience"); exp = (float) m.invoke(recipe); } catch (Exception ignored) {}

                    ResourceLocation wrapId = new ResourceLocation(FidworkMod.MODID, "fd_compat/" + recipe.getId().getPath());
                    result.add(new CookingRecipe(wrapId, "pot", recipe.getIngredients(), fdResult,
                            fdContainer.isEmpty() ? Optional.empty() : Optional.of(fdContainer), cookTime, exp, 1));
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        return result;
    }

    private ItemStack getFdContainer(Recipe<?> recipe) {
        try {
            Method m = recipe.getClass().getMethod("getOutputContainer");
            return (ItemStack) m.invoke(recipe);
        } catch (Exception e) { return ItemStack.EMPTY; }
    }
}
