package com.fidtest.recipe;

import com.fidtest.ExampleMod;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.slf4j.Logger;

import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// 批量配方加载器 — 一个厨具对应一个 JSON 文件，内含该厨具的所有配方。
//
// JSON 文件存放路径：data/fidworkblock/cooking_recipes/&lt;appliance&gt;.json
// 文件格式为 JSON 数组，每个元素是一个配方对象。
public class CookingRecipeBatchLoader
        extends SimplePreparableReloadListener<Map<ApplianceType, List<RecipeHolder<CookingRecipe>>>> {

    private static final Logger LOGGER = LogUtils.getLogger();

// 所有已加载的配方（线程安全）
    private static final Map<ApplianceType, List<RecipeHolder<CookingRecipe>>> RECIPES =
            new ConcurrentHashMap<>();

// 资源加载（服务端/数据包重载时触发）

    @Override
    protected Map<ApplianceType, List<RecipeHolder<CookingRecipe>>> prepare(
            ResourceManager rm, ProfilerFiller profiler) {

        Map<ApplianceType, List<RecipeHolder<CookingRecipe>>> loaded = new HashMap<>();

        for (ApplianceType appliance : ApplianceType.values()) {
            ResourceLocation fileLoc = ResourceLocation.fromNamespaceAndPath(
                    ExampleMod.MODID,
                    "cooking_recipes/" + appliance.getName() + ".json");

            rm.getResource(fileLoc).ifPresent(resource -> {
                try (var reader = new InputStreamReader(resource.open())) {
                    JsonArray array = com.google.gson.JsonParser.parseReader(reader).getAsJsonArray();
                    List<RecipeHolder<CookingRecipe>> list = new ArrayList<>();

                    int idx = 0;
                    for (JsonElement elem : array) {
                        try {
                            JsonObject obj = elem.getAsJsonObject();
                            CookingRecipe recipe = parseRecipe(obj, appliance.getName());
                            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                                    ExampleMod.MODID,
                                    "batch/" + appliance.getName() + "/" + idx);
                            list.add(new RecipeHolder<>(id, recipe));
                            idx++;
                        } catch (Exception ex) {
                            LOGGER.warn("跳过 {} 中第 {} 个配方（解析失败）: {}",
                                    fileLoc, idx, ex.getMessage());
                        }
                    }
                    if (!list.isEmpty()) {
                        loaded.put(appliance, list);
                        LOGGER.info("成功加载 {} 的 {} 个配方", appliance.getName(), list.size());
                    }
                } catch (Exception ex) {
                    LOGGER.warn("无法读取配方文件 {}: {}", fileLoc, ex.getMessage());
                }
            });
        }

        return loaded;
    }

    @Override
    protected void apply(Map<ApplianceType, List<RecipeHolder<CookingRecipe>>> data,
                         ResourceManager rm, ProfilerFiller profiler) {
        RECIPES.clear();
        RECIPES.putAll(data);
    }

// 公开查询接口

// 获取指定厨具的所有配方
    public static List<RecipeHolder<CookingRecipe>> getRecipes(ApplianceType appliance) {
        return RECIPES.getOrDefault(appliance, List.of());
    }

// 获取全部已加载的配方
    public static Collection<RecipeHolder<CookingRecipe>> getAllRecipes() {
        List<RecipeHolder<CookingRecipe>> all = new ArrayList<>();
        for (List<RecipeHolder<CookingRecipe>> list : RECIPES.values()) {
            all.addAll(list);
        }
        return all;
    }

// JSON 解析

    private static CookingRecipe parseRecipe(JsonObject obj, String applianceName) {
// 解析原料（支持 item 和 tag）
        JsonArray arr = obj.getAsJsonArray("ingredients");
        List<Ingredient> ingredients = new ArrayList<>();
        for (JsonElement e : arr) {
            ingredients.add(Ingredient.CODEC_NONEMPTY
                    .parse(JsonOps.INSTANCE, e)
                    .getOrThrow(err -> new IllegalArgumentException("原料解析失败: " + err)));
        }

// 解析输出物品
        ItemStack result = ItemStack.CODEC
                .parse(JsonOps.INSTANCE, obj.get("result"))
                .getOrThrow(err -> new IllegalArgumentException("输出物品解析失败: " + err));

// 可选容器（如碗）
        Optional<ItemStack> container = Optional.empty();
        if (obj.has("container")) {
            container = Optional.of(ItemStack.CODEC
                    .parse(JsonOps.INSTANCE, obj.get("container"))
                    .getOrThrow(err -> new IllegalArgumentException("容器解析失败: " + err)));
        }

        int cookingTime = obj.has("cookingTime") ? obj.get("cookingTime").getAsInt() : 200;
        float experience = obj.has("experience") ? obj.get("experience").getAsFloat() : 0.0F;
        int minHeat = obj.has("minHeat") ? obj.get("minHeat").getAsInt() : 0;
        String mode = obj.has("mode") ? obj.get("mode").getAsString() : "dry";

        return new CookingRecipe(applianceName, ingredients, result, container,
                cookingTime, experience, minHeat, mode);
    }
}
