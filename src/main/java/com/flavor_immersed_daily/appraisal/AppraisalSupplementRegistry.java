package com.flavor_immersed_daily.appraisal;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 农产鉴定「补充产物」注册表 —— 汇总所有数据包中的 appraisal_supplement/*.json。
// 其他模组只要在自身 jar 的 data/<命名空间>/appraisal_supplement/ 下放置 JSON，
// 即可为农产鉴定机（右键鉴定野生作物堆）补充额外产物，无需编译期依赖本模组。
// 文件格式与本模组自带的 drop_tables/appraisal.json 一致，
// 是一个「输入物品ID → 额外产物ID数组」的对象；多个文件、多个模组可对同一输入追加产物：
// <pre>{@code
// {
// "flavor_immersed_daily:wildseedplant": ["my_mod:custom_seed", "my_mod:custom_seed"],
// "flavor_immersed_daily:wildmushroomplant": ["my_mod:spore"]
// }
// }</pre>
// 补充产物会与本模组基础掉落合并；与基础掉落重复的条目会被忽略。
// 数据在进入世界 / 重载数据包时由 ReloadListener 刷新。
public final class AppraisalSupplementRegistry {

// 数据包目录：data/&lt;命名空间&gt;/appraisal_supplement/&lt;名称&gt;.json
    public static final String DIRECTORY = "appraisal_supplement";

// 输入物品ID → 补充产物ID列表
    private static final Map<String, List<String>> SUPPLEMENTS = new LinkedHashMap<>();

    private AppraisalSupplementRegistry() {
    }

// 重新解析全部补充定义（由数据包重载触发）。
    public static void reload(Map<ResourceLocation, JsonElement> jsons) {
        Map<String, List<String>> merged = new LinkedHashMap<>();
        for (Map.Entry<ResourceLocation, JsonElement> file : jsons.entrySet()) {
            if (!file.getValue().isJsonObject()) {
                FlavorImmersedDaily.LOGGER.warn("AppraisalSupplement: '{}' 不是 JSON 对象，已跳过", file.getKey());
                continue;
            }
            for (Map.Entry<String, JsonElement> entry : file.getValue().getAsJsonObject().entrySet()) {
                String inputId = entry.getKey() == null ? "" : entry.getKey().trim();
// 输入必须是合法的物品ID，避免附属模组写错导致 JEI/EMI 解析时崩溃
                if (inputId.isEmpty() || ResourceLocation.tryParse(inputId) == null) {
                    if (!inputId.isEmpty()) {
                        FlavorImmersedDaily.LOGGER.warn("AppraisalSupplement: '{}' 中 '{}' 不是合法物品ID，已跳过",
                                file.getKey(), inputId);
                    }
                    continue;
                }
                List<String> extras = toStringList(entry.getValue());
                if (extras.isEmpty()) {
                    continue;
                }
                merged.computeIfAbsent(inputId, ignored -> new ArrayList<>()).addAll(extras);
            }
        }
        synchronized (AppraisalSupplementRegistry.class) {
            SUPPLEMENTS.clear();
            SUPPLEMENTS.putAll(merged);
        }
        FlavorImmersedDaily.LOGGER.info("AppraisalSupplement: 已加载 {} 条野生作物鉴定补充定义", merged.size());
    }

// 某个输入物品的补充产物（无则返回空列表）。
    public static List<String> getSupplements(String inputId) {
        List<String> list = SUPPLEMENTS.get(inputId);
        return list == null ? Collections.emptyList() : list;
    }

// 当前全部补充定义（只读副本）。
    public static Map<String, List<String>> all() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(SUPPLEMENTS));
    }

// 从任意资源管理器解析并汇总全部 appraisal_supplement 定义。
// 供 JEI/EMI 在客户端按需调用：客户端重载监听器与它们自身的重载顺序不确定，
// 需改用服务端的数据资源管理器（客户端资源管理器只暴露 assets/，看不到 data/）。
    public static void loadFrom(ResourceManager resourceManager) {
        Map<ResourceLocation, JsonElement> jsons = new LinkedHashMap<>();
        String prefix = DIRECTORY + "/";
        for (Map.Entry<ResourceLocation, Resource> entry :
                resourceManager.listResources(DIRECTORY, path -> path.getPath().endsWith(".json")).entrySet()) {
            ResourceLocation file = entry.getKey();
            String filePath = file.getPath();
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(file.getNamespace(),
                    filePath.substring(prefix.length(), filePath.length() - ".json".length()));
            try (Reader reader = entry.getValue().openAsReader()) {
                jsons.put(id, JsonParser.parseReader(reader));
            } catch (Exception e) {
                FlavorImmersedDaily.LOGGER.warn("AppraisalSupplement: 无法读取 '{}'：{}", file, e.getMessage());
            }
        }
        reload(jsons);
    }

    private static List<String> toStringList(JsonElement element) {
        List<String> list = new ArrayList<>();
        if (element == null || !element.isJsonArray()) {
            return list;
        }
        for (JsonElement e : element.getAsJsonArray()) {
            if (e.isJsonPrimitive() && e.getAsJsonPrimitive().isString()) {
                String s = e.getAsString().trim();
// 产物也必须是合法物品ID（未安装模组的物品会在使用时被跳过，不会报错）
                if (!s.isEmpty() && ResourceLocation.tryParse(s) != null) {
                    list.add(s);
                }
            }
        }
        return list;
    }

// 数据包重载监听器：读取所有命名空间下的 appraisal_supplement/*.json。
    public static final class ReloadListener extends SimpleJsonResourceReloadListener {
        private static final Gson GSON = new Gson();

        public ReloadListener() {
            super(GSON, DIRECTORY);
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager resourceManager,
                             ProfilerFiller profiler) {
            AppraisalSupplementRegistry.reload(jsons);
        }
    }
}
