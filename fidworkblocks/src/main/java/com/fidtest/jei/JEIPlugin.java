package com.fidtest.jei;

import com.fidtest.ExampleMod;
import com.fidtest.block.entity.ChoppingboardBlockEntity;
import com.fidtest.choppingboard.ChoppingboardMobDefinition;
import com.fidtest.choppingboard.ChoppingboardMobRegistry;
import com.fidtest.choppingboard.ChoppingboardWrapRecipes;
import com.fidtest.integration.FdCuttingCompat;
import com.fidtest.recipe.ApplianceType;
import com.fidtest.recipe.CookingRecipe;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    private static final ResourceLocation UID =
            ResourceLocation.fromNamespaceAndPath(ExampleMod.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() { return UID; }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        IGuiHelper helper = reg.getJeiHelpers().getGuiHelper();
        for (ApplianceType app : ApplianceType.values()) {
            if (app == ApplianceType.CHOPPINGBOARD) {
                reg.addRecipeCategories(new ChoppingboardRecipeCategory(
                        helper, getIconBlock(app), choppingboardKnifeStacks()));
                reg.addRecipeCategories(new ChoppingboardWrapRecipeCategory(helper, getIconBlock(app)));
                reg.addRecipeCategories(new ChoppingboardMobRecipeCategory(helper, getIconBlock(app)));
            } else if (app == ApplianceType.MORTAR) {
                reg.addRecipeCategories(new MortarRecipeCategory(helper, getIconBlock(app)));
            } else if (app == ApplianceType.SQUEEZINGMACHINE) {
                reg.addRecipeCategories(new SqueezingmachineRecipeCategory(helper, getIconBlock(app)));
            } else if (app == ApplianceType.FRYINGPAN) {
                reg.addRecipeCategories(new FryingpanRecipeCategory(helper, getIconBlock(app)));
            } else if (app == ApplianceType.JAR) {
                reg.addRecipeCategories(new JarRecipeCategory(helper, getIconBlock(app)));
            } else if (app == ApplianceType.TEAPOT) {
                reg.addRecipeCategories(new TeapotRecipeCategory(helper, getIconBlock(app)));
            } else if (app == ApplianceType.DRYER) {
// 烘干机拆分为两个类别：烘干模式 + 烤箱模式
                reg.addRecipeCategories(new CookingRecipeCategory(helper, app, getIconBlock(app), "dry"));
                reg.addRecipeCategories(new CookingRecipeCategory(helper, app, getIconBlock(app), "oven"));
            } else {
                reg.addRecipeCategories(new CookingRecipeCategory(helper, app, getIconBlock(app)));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void registerRecipes(IRecipeRegistration reg) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<RecipeHolder<CookingRecipe>> allRecipes =
                recipeManager.getAllRecipesFor(ModRecipeTypes.COOKING.get());

        for (ApplianceType app : ApplianceType.values()) {
            if (app == ApplianceType.DRYER) {
// 烘干模式：mode 为空或 dry
                RecipeType<RecipeHolder<CookingRecipe>> dryType = ModJeiRecipeTypes.forDryerMode("dry");
                List<RecipeHolder<CookingRecipe>> dryRecipes = new ArrayList<>(allRecipes.stream()
                        .filter(h -> {
                            String a = h.value().getAppliance();
                            if (!a.isEmpty() && !a.equals(app.getName())) return false;
                            String m = h.value().getMode();
                            return m.isEmpty() || "dry".equals(m);
                        })
                        .toList());
                if (!dryRecipes.isEmpty()) {
                    reg.addRecipes(dryType, dryRecipes);
                }
// 烤箱模式：mode 为 oven + 所有营火配方
                RecipeType<RecipeHolder<CookingRecipe>> ovenType = ModJeiRecipeTypes.forDryerMode("oven");
                List<RecipeHolder<CookingRecipe>> ovenRecipes = new ArrayList<>(allRecipes.stream()
                        .filter(h -> {
                            String a = h.value().getAppliance();
                            if (!a.isEmpty() && !a.equals(app.getName())) return false;
                            return "oven".equals(h.value().getMode());
                        })
                        .toList());
                ovenRecipes.addAll(collectCampfireRecipes(recipeManager));
                if (!ovenRecipes.isEmpty()) {
                    reg.addRecipes(ovenType, ovenRecipes);
                }
                continue;
            }

            RecipeType<RecipeHolder<CookingRecipe>> type = ModJeiRecipeTypes.forAppliance(app);
            List<RecipeHolder<CookingRecipe>> filtered = new ArrayList<>(allRecipes.stream()
                    .filter(h -> {
                        String a = h.value().getAppliance();
                        return a.isEmpty() || a.equals(app.getName());
                    })
                    .toList());

            if (app == ApplianceType.POT) {
                filtered.addAll(collectFdRecipes(recipeManager, "farmersdelight", "cooking"));
            }

// 砧板：接入农夫乐事（含附属）的切割配方；与本模组配方重叠时优先本模组
            if (app == ApplianceType.CHOPPINGBOARD) {
                filtered.addAll(collectFdCuttingRecipes(recipeManager, filtered));
            }

            if (!filtered.isEmpty()) {
                reg.addRecipes(type, filtered);
            }
        }

// 砧板的包裹/成型工序写在方块逻辑里（不是 JSON 配方），这里合成展示用配方
        List<RecipeHolder<CookingRecipe>> wrapRecipes = collectChoppingboardWrapRecipes();
        if (!wrapRecipes.isEmpty()) {
            reg.addRecipes(ModJeiRecipeTypes.forChoppingboardWrap(), wrapRecipes);
        }

// 砧板小型生物屠宰：来自数据包 data/<任意命名空间>/choppingboard_mob/*.json。
// 注册表通常已由集成服务端的 AddReloadListenerEvent 填充；为空时兜底改用服务端资源管理器
// （不能用客户端资源管理器：那是 CLIENT_RESOURCES，只暴露 assets/ 看不到 data/）。
        if (ChoppingboardMobRegistry.all().isEmpty()) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                ChoppingboardMobRegistry.loadFrom(server.getResourceManager());
            }
        }
        List<ChoppingboardMobDefinition> mobRecipes = new ArrayList<>(ChoppingboardMobRegistry.all());
        if (!mobRecipes.isEmpty()) {
            reg.addRecipes(ModJeiRecipeTypes.forChoppingboardMob(), mobRecipes);
        }
    }

// 合成砧板包裹/成型工序的展示配方：
// 皮 + 任意馅料 → 生食；生馅饼 + 月饼模具 → 生月饼；芦苇叶 + 糯米 → 生粽子；
// 面团/面剂子/面皮 + 擀面杖 → 下一步形态。
// 工序类型记录在 CookingRecipe 的 mode 字段里。
// 数据来自 ChoppingboardWrapRecipes（JEI 与 EMI 共用，保证两侧同步）。
    private List<RecipeHolder<CookingRecipe>> collectChoppingboardWrapRecipes() {
        List<RecipeHolder<CookingRecipe>> result = new ArrayList<>();
        for (ChoppingboardWrapRecipes.Entry entry : ChoppingboardWrapRecipes.collect()) {
            CookingRecipe recipe = new CookingRecipe(
                    "choppingboard", List.of(Ingredient.of(entry.input())), entry.output(),
                    Optional.empty(), 0, 0, 0, entry.mode());
            result.add(new RecipeHolder<>(entry.id(), recipe));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private List<RecipeHolder<CookingRecipe>> collectFdRecipes(RecipeManager recipeManager, String namespace, String path) {
        List<RecipeHolder<CookingRecipe>> result = new ArrayList<>();
        try {
            ResourceLocation fdId = ResourceLocation.fromNamespaceAndPath(namespace, path);
            var fdType = BuiltInRegistries.RECIPE_TYPE.get(fdId);
            if (fdType == null) return result;

            List<RecipeHolder<?>> fdRecipes = recipeManager.getAllRecipesFor((net.minecraft.world.item.crafting.RecipeType) fdType);
            var registryAccess = Minecraft.getInstance().level.registryAccess();

            for (RecipeHolder<?> holder : fdRecipes) {
                try {
                    Recipe<?> recipe = holder.value();
                    ItemStack fdResult = recipe.getResultItem(registryAccess);
                    ItemStack fdContainer = getFdOutputContainer(recipe);
                    int cookTime = 200;
                    float exp = 0;
                    try {
                        Method m = recipe.getClass().getMethod("getCookTime");
                        cookTime = (int) m.invoke(recipe);
                    } catch (Exception ignored) {}
                    try {
                        Method m = recipe.getClass().getMethod("getExperience");
                        exp = (float) m.invoke(recipe);
                    } catch (Exception ignored) {}

                    CookingRecipe wrapped = new CookingRecipe(
                            "pot", recipe.getIngredients(), fdResult,
                            fdContainer.isEmpty() ? Optional.empty() : Optional.of(fdContainer),
                            cookTime, exp, 1);
                    ResourceLocation wrappedId = ResourceLocation.fromNamespaceAndPath(
                            ExampleMod.MODID, "fd_compat/" + holder.id().getPath());
                    result.add(new RecipeHolder<>(wrappedId, wrapped));
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        return result;
    }

// 收集农夫乐事（含附属模组）砧板切割配方并包装成本模组的展示配方。
// 只保留刀具配方（排除斧头去皮、锄头翻土等）；若某输入的切割配方本模组砧板
// 已有覆盖（即 ownRecipes 里已有该输入的配方），则跳过，保证本模组配方优先。
    private List<RecipeHolder<CookingRecipe>> collectFdCuttingRecipes(
            RecipeManager recipeManager, List<RecipeHolder<CookingRecipe>> ownRecipes) {
        List<RecipeHolder<CookingRecipe>> result = new ArrayList<>();
        var registryAccess = Minecraft.getInstance().level.registryAccess();

// 本模组砧板已覆盖的输入物品：重叠时优先本模组配方
        Set<Item> coveredInputs = new HashSet<>();
        for (RecipeHolder<CookingRecipe> holder : ownRecipes) {
            for (Ingredient ingredient : holder.value().getIngredients()) {
                for (ItemStack stack : ingredient.getItems()) {
                    coveredInputs.add(stack.getItem());
                }
            }
        }

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

            List<ItemStack> results = FdCuttingCompat.readResults(cutting.recipe(), registryAccess);
            if (results.isEmpty()) continue;

            CookingRecipe wrapped = new CookingRecipe(
                    "choppingboard", List.of(cutting.input()), results.get(0),
                    Optional.empty(), 0, 0, 0);
            wrapped.setExtraResults(results);
            ResourceLocation wrappedId = ResourceLocation.fromNamespaceAndPath(
                    ExampleMod.MODID, "fd_cutting/" + cutting.id().getPath());
            result.add(new RecipeHolder<>(wrappedId, wrapped));
        }
        return result;
    }

// 砧板刀具展示：本模组菜刀 + 农夫乐事刀具标签内的全部刀具（含附属模组）。
    private static List<ItemStack> choppingboardKnifeStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        ItemStack kitchenKnife = new ItemStack(BuiltInRegistries.ITEM.get(
                ResourceLocation.fromNamespaceAndPath("flavor_immersed_daily", "kitchenknife")));
        if (!kitchenKnife.isEmpty()) {
            stacks.add(kitchenKnife);
        }
        BuiltInRegistries.ITEM.getTag(ChoppingboardBlockEntity.FD_KNIFE_TAG).ifPresent(holders -> {
            for (var holder : holders) {
                ItemStack stack = new ItemStack(holder.value());
                if (stacks.stream().noneMatch(existing -> existing.is(stack.getItem()))) {
                    stacks.add(stack);
                }
            }
        });
        return stacks;
    }

// 收集所有营火配方，包装成烘干机烤箱模式配方（供 JEI 显示）
    private List<RecipeHolder<CookingRecipe>> collectCampfireRecipes(RecipeManager recipeManager) {
        List<RecipeHolder<CookingRecipe>> result = new ArrayList<>();
        var registryAccess = Minecraft.getInstance().level.registryAccess();
        try {
            List<RecipeHolder<CampfireCookingRecipe>> campfireRecipes =
                    recipeManager.getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType.CAMPFIRE_COOKING);
            for (RecipeHolder<CampfireCookingRecipe> holder : campfireRecipes) {
                CampfireCookingRecipe recipe = holder.value();
                CookingRecipe wrapped = new CookingRecipe(
                        "dryer",
                        recipe.getIngredients(),
                        recipe.getResultItem(registryAccess),
                        java.util.Optional.empty(),
                        recipe.getCookingTime(),
                        recipe.getExperience(),
                        0,
                        "oven");
                ResourceLocation wrappedId = ResourceLocation.fromNamespaceAndPath(
                        ExampleMod.MODID, "campfire_compat/" + holder.id().getPath());
                result.add(new RecipeHolder<>(wrappedId, wrapped));
            }
        } catch (Exception ignored) {}
        return result;
    }

    private ItemStack getFdOutputContainer(Recipe<?> recipe) {
        try {
            Method m = recipe.getClass().getMethod("getOutputContainer");
            return (ItemStack) m.invoke(recipe);
        } catch (Exception e) {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
        for (ApplianceType app : ApplianceType.values()) {
            RecipeType<RecipeHolder<CookingRecipe>> type = ModJeiRecipeTypes.forAppliance(app);
            reg.addRecipeCatalyst(new ItemStack(getIconBlock(app)), type);
// 烘干机同时是烤箱模式类别的催化剂
            if (app == ApplianceType.DRYER) {
                reg.addRecipeCatalyst(new ItemStack(getIconBlock(app)),
                        ModJeiRecipeTypes.forDryerMode("oven"));
            }
// 砧板也是包裹/成型工序的催化剂
            if (app == ApplianceType.CHOPPINGBOARD) {
                reg.addRecipeCatalyst(new ItemStack(getIconBlock(app)),
                        ModJeiRecipeTypes.forChoppingboardWrap());
// 以及小型生物屠宰的催化剂
                reg.addRecipeCatalyst(new ItemStack(getIconBlock(app)),
                        ModJeiRecipeTypes.forChoppingboardMob());
            }
        }
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration reg) {
        for (ApplianceType app : ApplianceType.values()) {
            if (app == ApplianceType.CHOPPINGBOARD) continue;
            if (app == ApplianceType.STEAMBOX) continue;
            if (app == ApplianceType.MORTAR) continue;
            if (app == ApplianceType.SQUEEZINGMACHINE) continue;
            if (app == ApplianceType.FRYINGPAN) continue;
            if (app == ApplianceType.JAR) continue;
            if (app == ApplianceType.TEAPOT) continue;
            if (app == ApplianceType.MEATMINCER) continue;
            RecipeType<RecipeHolder<CookingRecipe>> type = ModJeiRecipeTypes.forAppliance(app);
            int inputSlots = app.getInputSlots();
            int totalApplianceSlots = app.getInventorySize();
            reg.addRecipeTransferHandler(
                    com.fidtest.screen.CookingMenu.class,
                    com.fidtest.registration.ModMenuTypes.COOKING.get(),
                    type,
                    0, inputSlots,
                    totalApplianceSlots, 36);
// 烘干机烤箱模式类别的 transfer handler
            if (app == ApplianceType.DRYER) {
                reg.addRecipeTransferHandler(
                        com.fidtest.screen.CookingMenu.class,
                        com.fidtest.registration.ModMenuTypes.COOKING.get(),
                        ModJeiRecipeTypes.forDryerMode("oven"),
                        0, inputSlots,
                        totalApplianceSlots, 36);
            }
        }
    }

    private static Block getIconBlock(ApplianceType app) {
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
