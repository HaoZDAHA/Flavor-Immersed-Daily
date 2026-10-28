package com.fidtest.choppingboard;

import com.fidtest.ExampleMod;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// 砧板「小型动物屠宰」定义（数据包 JSON）。
// 供其他模组 / 数据包扩展砧板：把物品放上砧板后，可见对应生物实体平躺渲染，
// 手持刀具多次右键，按阶段依次爆出产物。文件位置：
// data/<任意命名空间>/choppingboard_mob/<名称>.json，本模组会加载所有命名空间下的定义。
// <pre>{@code
// {
// "input": "my_mod:raw_grass_carp", // 放上砧板的物品
// "entity": "my_mod:grass_carp", // 砧板上平躺渲染的实体（可选，缺省则用平面物品图标）
// "tool": "flavor_immersed_daily:sharpknife", // 所需刀具：物品ID 或 "#tag"；可省略=默认切割刀
// "render": { "scale": 0.75, "yaw": 0.0, "y_offset": 0.0, "roll": 0.0, "x_offset": 0.0, "z_offset": 0.0 },
// "stages": [
// { "name": "刮鳞", "drops": ["my_mod:fish_scale", "my_mod:fish_scale"] },
// { "name": "开膛", "drops": ["my_mod:raw_fish_liver"] }
// ]
// }
// }</pre>
public record ChoppingboardMobDefinition(ResourceLocation id, Item input,
                                         @Nullable EntityType<?> entity,
                                         @Nullable Item toolItem, @Nullable TagKey<Item> toolTag,
                                         Render render, List<Stage> stages) {

// 缺省刀具：本体系的切割刀
    private static final String DEFAULT_TOOL = "flavor_immersed_daily:sharpknife";

// 单个屠宰阶段：名称（仅展示用）+ 该刀掉落的物品（重复写 id 即多份）
    public record Stage(@Nullable String name, List<Item> drops) {}

// 砧板上的横置姿态微调
    public record Render(float scale, float yaw, float yOffset, float roll, float xOffset, float zOffset) {
        public static final Render DEFAULT = new Render(0.75F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    }

// 手持物品是否为本定义所需刀具。
// tool 与 tag 都解析失败（例如未安装提供刀具的模组）时放行任意非空物品，避免功能完全不可用。
    public boolean toolMatches(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (toolItem != null) return stack.is(toolItem);
        if (toolTag != null) return stack.is(toolTag);
        return true;
    }

// 需要右键的刀数
    public int stageCount() {
        return stages.size();
    }

// 解析一份定义；结构非法时返回 null（仅打印警告，不影响游戏）。
    @Nullable
    public static ChoppingboardMobDefinition parse(ResourceLocation id, JsonObject json) {
        Item input = itemById(string(json, "input"));
        if (input == null) {
            ExampleMod.LOGGER.warn("ChoppingboardMob: '{}' 缺少合法的 input 物品，已跳过", id);
            return null;
        }

        EntityType<?> entity = null;
        String entityStr = string(json, "entity");
        if (entityStr != null && !entityStr.isBlank()) {
            ResourceLocation rl = ResourceLocation.tryParse(entityStr.trim());
            if (rl != null && BuiltInRegistries.ENTITY_TYPE.containsKey(rl)) {
                entity = BuiltInRegistries.ENTITY_TYPE.get(rl);
            } else {
                ExampleMod.LOGGER.warn("ChoppingboardMob: '{}' 的 entity '{}' 未注册，将只用平面图标渲染", id, entityStr);
            }
        }

// 刀具：省略 → 默认切割刀
        String tool = string(json, "tool");
        if (tool == null || tool.isBlank()) {
            tool = DEFAULT_TOOL;
        }
        Item toolItem = null;
        TagKey<Item> toolTag = null;
        if (tool.startsWith("#")) {
            ResourceLocation tagId = ResourceLocation.tryParse(tool.substring(1));
            if (tagId != null) {
                toolTag = TagKey.create(Registries.ITEM, tagId);
            }
        } else {
            toolItem = itemById(tool);
        }
        if (toolItem == null && toolTag == null) {
            ExampleMod.LOGGER.warn("ChoppingboardMob: '{}' 的 tool '{}' 解析失败，将放行任意刀具", id, tool);
        }

        Render render = parseRender(json);
        List<Stage> stages = parseStages(json);
        if (stages.isEmpty()) {
            ExampleMod.LOGGER.warn("ChoppingboardMob: '{}' 没有任何有效阶段，已跳过", id);
            return null;
        }
        return new ChoppingboardMobDefinition(id, input, entity, toolItem, toolTag, render, List.copyOf(stages));
    }

    private static List<Stage> parseStages(JsonObject json) {
        JsonElement stagesEl = json.get("stages");
        if (stagesEl == null || !stagesEl.isJsonArray()) {
            return List.of();
        }
        List<Stage> stages = new ArrayList<>();
        for (JsonElement element : stagesEl.getAsJsonArray()) {
            if (!element.isJsonObject()) {
                continue;
            }
            JsonObject stageJson = element.getAsJsonObject();
            List<Item> drops = new ArrayList<>();
            JsonElement dropsEl = stageJson.get("drops");
            if (dropsEl != null && dropsEl.isJsonArray()) {
                JsonArray array = dropsEl.getAsJsonArray();
                for (JsonElement dropEl : array) {
                    if (!dropEl.isJsonPrimitive()) {
                        continue;
                    }
                    Item drop = itemById(dropEl.getAsString());
                    if (drop != null) {
                        drops.add(drop);
                    }
                }
            }
            stages.add(new Stage(string(stageJson, "name"), List.copyOf(drops)));
        }
        return stages;
    }

    private static Render parseRender(JsonObject json) {
        JsonElement renderEl = json.get("render");
        if (renderEl == null || !renderEl.isJsonObject()) {
            return Render.DEFAULT;
        }
        JsonObject r = renderEl.getAsJsonObject();
        return new Render(
                floatOr(r, "scale", Render.DEFAULT.scale()),
                floatOr(r, "yaw", Render.DEFAULT.yaw()),
                floatOr(r, "y_offset", Render.DEFAULT.yOffset()),
                floatOr(r, "roll", Render.DEFAULT.roll()),
                floatOr(r, "x_offset", Render.DEFAULT.xOffset()),
                floatOr(r, "z_offset", Render.DEFAULT.zOffset()));
    }

    private static float floatOr(JsonObject json, String key, float fallback) {
        JsonElement el = json.get(key);
        return el != null && el.isJsonPrimitive() ? el.getAsFloat() : fallback;
    }

    @Nullable
    private static Item itemById(@Nullable String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        ResourceLocation rl = ResourceLocation.tryParse(id.trim());
        return rl != null && BuiltInRegistries.ITEM.containsKey(rl) ? BuiltInRegistries.ITEM.get(rl) : null;
    }

    @Nullable
    private static String string(JsonObject json, String key) {
        JsonElement element = json.get(key);
        return element != null && element.isJsonPrimitive() ? element.getAsString() : null;
    }
}
