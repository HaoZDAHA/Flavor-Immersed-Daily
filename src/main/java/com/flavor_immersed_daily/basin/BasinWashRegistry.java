package com.flavor_immersed_daily.basin;

import com.flavor_immersed_daily.FlavorImmersedDaily;
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

// 木盆泡洗注册表 —— 汇总所有数据包中的 basin_wash/*.json。
// 其他模组只要在自身 jar 的 data/<命名空间>/basin_wash/ 下放置 JSON，
// 即可接入木盆的漂洗流程，无需编译期依赖本模组。
// 数据在进入世界 / 重载数据包时由 ReloadListener 刷新。
public final class BasinWashRegistry {

// 数据包目录：data/&lt;命名空间&gt;/basin_wash/&lt;名称&gt;.json
    public static final String DIRECTORY = "basin_wash";

    private static final Map<ResourceLocation, BasinWashDefinition> BY_ID = new LinkedHashMap<>();
    private static final Map<Item, BasinWashDefinition> BY_INPUT = new HashMap<>();

    private BasinWashRegistry() {
    }

// 重新解析全部定义（由数据包重载触发）。
    public static void reload(Map<ResourceLocation, JsonElement> jsons) {
        Map<ResourceLocation, BasinWashDefinition> byId = new LinkedHashMap<>();
        Map<Item, BasinWashDefinition> byInput = new HashMap<>();
        for (Map.Entry<ResourceLocation, JsonElement> entry : jsons.entrySet()) {
            if (!entry.getValue().isJsonObject()) {
                continue;
            }
            BasinWashDefinition definition = BasinWashDefinition.parse(entry.getKey(), entry.getValue().getAsJsonObject());
            if (definition == null) {
                continue;
            }
            BasinWashDefinition previous = byInput.put(definition.input(), definition);
            if (previous != null) {
                FlavorImmersedDaily.LOGGER.warn("BasinWash: 输入物品 {} 被 '{}' 与 '{}' 同时定义，后者生效",
                        definition.input(), previous.id(), definition.id());
            }
            byId.put(entry.getKey(), definition);
        }
        synchronized (BasinWashRegistry.class) {
            BY_ID.clear();
            BY_ID.putAll(byId);
            BY_INPUT.clear();
            BY_INPUT.putAll(byInput);
        }
        FlavorImmersedDaily.LOGGER.info("BasinWash: 已加载 {} 条泡洗定义", byId.size());
    }

// 按定义 ID 查询。
    @Nullable
    public static BasinWashDefinition get(@Nullable ResourceLocation id) {
        return id == null ? null : BY_ID.get(id);
    }

// 按放入木盆的输入物品查询。
    @Nullable
    public static BasinWashDefinition getByInput(@Nullable Item item) {
        return item == null ? null : BY_INPUT.get(item);
    }

// 当前全部定义（只读）。
    public static List<BasinWashDefinition> all() {
        return List.copyOf(BY_ID.values());
    }

// 从任意资源管理器解析并汇总全部 basin_wash 定义。
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
                FlavorImmersedDaily.LOGGER.warn("BasinWash: 无法读取 '{}'：{}", file, e.getMessage());
            }
        }
        reload(jsons);
    }

// 数据包重载监听器：读取所有命名空间下的 basin_wash/*.json。
    public static final class ReloadListener extends SimpleJsonResourceReloadListener {
        private static final Gson GSON = new Gson();

        public ReloadListener() {
            super(GSON, DIRECTORY);
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager resourceManager, ProfilerFiller profiler) {
            BasinWashRegistry.reload(jsons);
        }
    }
}
