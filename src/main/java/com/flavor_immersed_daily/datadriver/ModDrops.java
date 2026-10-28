package com.flavor_immersed_daily.datadriver;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 基础掉落物表格加载器。
// 屠宰、木盆漂洗、农产鉴定机的"默认产物"统一从模组自带的 JSON 中读取，随模组打包并随更新自动生效，
// 玩家不再需要手动更新掉落物配置。Config 中仅保留玩家在此基础上额外添加的产物。
public final class ModDrops {

    private static final String ROOT = "data/flavor_immersed_daily/drop_tables/";
    private static final String BUTCHER_DIR = ROOT + "butcher/";

    public static Map<Integer, List<String>> cattleDefaults = new HashMap<>();
    public static Map<Integer, List<String>> sheepDefaults = new HashMap<>();
    public static Map<Integer, List<String>> pigDefaults = new HashMap<>();
    public static Map<Integer, List<String>> chickenDefaults = new HashMap<>();
    public static Map<String, List<String>> appraisalDefaults = new HashMap<>();
    public static List<String> washedChickenDefaults = new ArrayList<>();

    private static boolean initialized = false;

    private ModDrops() {
    }

// 惰性加载一次，服务端（生成掉落）与客户端（JEI 展示）共用同一份数据。
    public static synchronized void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        cattleDefaults = loadButcher("cattle");
        sheepDefaults = loadButcher("sheep");
        pigDefaults = loadButcher("pig");
        chickenDefaults = loadButcher("chicken");
        appraisalDefaults = loadAppraisal();
        washedChickenDefaults = loadWashedChicken();
    }

// 屠宰掉落表，格式：{"阶段数字": ["modid:item", ...]}
    private static Map<Integer, List<String>> loadButcher(String animal) {
        Map<Integer, List<String>> result = new HashMap<>();
        JsonElement root = readJson(BUTCHER_DIR + animal + ".json");
        if (root == null || !root.isJsonObject()) {
            return result;
        }
        for (Map.Entry<String, JsonElement> entry : root.getAsJsonObject().entrySet()) {
            int stage;
            try {
                stage = Integer.parseInt(entry.getKey());
            } catch (NumberFormatException ignored) {
                continue;
            }
            result.put(stage, toStringList(entry.getValue()));
        }
        return result;
    }

// 农产鉴定掉落表，格式：{"输入物品id": ["modid:item", ...]}
    private static Map<String, List<String>> loadAppraisal() {
        Map<String, List<String>> result = new HashMap<>();
        JsonElement root = readJson(ROOT + "appraisal.json");
        if (root == null || !root.isJsonObject()) {
            return result;
        }
        for (Map.Entry<String, JsonElement> entry : root.getAsJsonObject().entrySet()) {
            result.put(entry.getKey(), toStringList(entry.getValue()));
        }
        return result;
    }

// 木盆漂洗额外产物，格式：["modid:item", ...]
    private static List<String> loadWashedChicken() {
        JsonElement root = readJson(ROOT + "washed_chicken.json");
        if (root == null || !root.isJsonArray()) {
            return new ArrayList<>();
        }
        return toStringList(root);
    }

    private static List<String> toStringList(JsonElement element) {
        List<String> list = new ArrayList<>();
        if (element == null || !element.isJsonArray()) {
            return list;
        }
        for (JsonElement e : element.getAsJsonArray()) {
            if (e.isJsonPrimitive() && e.getAsJsonPrimitive().isString()) {
                String s = e.getAsString().trim();
                if (!s.isEmpty()) {
                    list.add(s);
                }
            }
        }
        return list;
    }

// 从模组资源（classpath）读取 JSON，客户端与服务端均可访问。
    private static JsonElement readJson(String path) {
        try (InputStream in = ModDrops.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                FlavorImmersedDaily.LOGGER.warn("ModDrops: dropping table resource '{}' not found", path);
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            return JsonParser.parseReader(reader);
        } catch (Exception e) {
            FlavorImmersedDaily.LOGGER.error("ModDrops: failed to load dropping table '{}'", path, e);
            return null;
        }
    }
}