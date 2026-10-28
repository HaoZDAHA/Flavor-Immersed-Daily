package com.fidtest.choppingboard;

import com.fidtest.ExampleMod;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 砧板「小型生物屠宰」定义注册表 —— 汇总所有数据包中的 choppingboard_mob/*.json。
// 其他模组只要在自身 jar 的 data/<任意命名空间>/choppingboard_mob/ 下放置 JSON，
// 即可让物品放上砧板后显示对应生物实体、并支持手持刀具多次右键依次爆出产物，
// 无需编译期依赖本模组，也无需依赖本模组提供的物品。
// 数据在进入世界 / 重载数据包时由 ReloadListener 刷新。
public final class ChoppingboardMobRegistry {

// 数据包目录：data/&lt;命名空间&gt;/choppingboard_mob/&lt;名称&gt;.json
    public static final String DIRECTORY = "choppingboard_mob";

    private static final Map<ResourceLocation, ChoppingboardMobDefinition> BY_ID = new LinkedHashMap<>();
    private static final Map<Item, ChoppingboardMobDefinition> BY_INPUT = new HashMap<>();

    private ChoppingboardMobRegistry() {
    }

// 重新解析全部定义（由数据包重载触发）。
    public static void reload(Map<ResourceLocation, JsonElement> jsons) {
        Map<ResourceLocation, ChoppingboardMobDefinition> byId = new LinkedHashMap<>();
        Map<Item, ChoppingboardMobDefinition> byInput = new HashMap<>();
        for (Map.Entry<ResourceLocation, JsonElement> entry : jsons.entrySet()) {
            if (!entry.getValue().isJsonObject()) {
                continue;
            }
            ChoppingboardMobDefinition definition =
                    ChoppingboardMobDefinition.parse(entry.getKey(), entry.getValue().getAsJsonObject());
            if (definition == null) {
                continue;
            }
            ChoppingboardMobDefinition previous = byInput.put(definition.input(), definition);
            if (previous != null) {
                ExampleMod.LOGGER.warn("ChoppingboardMob: 输入物品 {} 被 '{}' 与 '{}' 同时定义，后者生效",
                        definition.input(), previous.id(), definition.id());
            }
            byId.put(entry.getKey(), definition);
        }
        synchronized (ChoppingboardMobRegistry.class) {
            BY_ID.clear();
            BY_ID.putAll(byId);
            BY_INPUT.clear();
            BY_INPUT.putAll(byInput);
        }
        ExampleMod.LOGGER.info("ChoppingboardMob: 已加载 {} 条砧板屠宰定义", byId.size());
    }

// 按定义 ID 查询。
    @Nullable
    public static ChoppingboardMobDefinition get(@Nullable ResourceLocation id) {
        return id == null ? null : BY_ID.get(id);
    }

// 按放上砧板的输入物品查询。
    @Nullable
    public static ChoppingboardMobDefinition getByInput(@Nullable Item item) {
        return item == null ? null : BY_INPUT.get(item);
    }

// 当前全部定义（只读）。
    public static List<ChoppingboardMobDefinition> all() {
        return List.copyOf(BY_ID.values());
    }

// 从任意资源管理器解析并汇总全部定义。
// 供 JEI 在客户端按需调用：客户端重载监听器与 JEI 自身的重载顺序不确定，
// 直接在 JEI 注册配方时读取服务端数据资源包，可稳定拿到各模组 jar 内的 JSON。
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
                ExampleMod.LOGGER.warn("ChoppingboardMob: 无法读取 '{}'：{}", file, e.getMessage());
            }
        }
        reload(jsons);
    }

// 数据包重载监听器：读取所有命名空间下的 choppingboard_mob/*.json。
    public static final class ReloadListener extends SimpleJsonResourceReloadListener {
        private static final Gson GSON = new Gson();

        public ReloadListener() {
            super(GSON, DIRECTORY);
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager resourceManager,
                             ProfilerFiller profiler) {
            ChoppingboardMobRegistry.reload(jsons);
        }
    }
}
