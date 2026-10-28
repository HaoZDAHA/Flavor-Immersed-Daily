package com.flavor_immersed_daily.butchering;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

// 屠宰定义（数据包 JSON）——供其他模组 / 数据包扩展大挂钩的屠宰流程。
// 文件位置：data/<任意命名空间>/butchering/<名称>.json，本模组会自动加载所有命名空间下的定义。

// {
// "input": "my_mod:dead_donkey",          // 挂上空挂钩所用的物品
// "stages": [
// {
// "tool": "flavor_immersed_daily:wideedgedknife", // 所需刀具：物品ID 或 "#tag"
// "display": "my_mod:dead_donkey",      // 该阶段挂钩上显示的物品（可选，默认同 input）
// "model": "my_mod:block/dead_dog_1",   // 该阶段挂钩上替换显示的方块模型（可选，优先级高于 display）
// "drops": ["my_mod:donkey_blood"]      // 该阶段完成后产出的物品
// }
// // ... 更多阶段；用完后挂钩自动清空
// ]
// }
// }
public record ButcheringDefinition(ResourceLocation id, Item input, List<Stage> stages) {

// 单个屠宰阶段：所需刀具 + 挂钩显示物 + 产出。
    public record Stage(@Nullable Item toolItem, @Nullable TagKey<Item> toolTag,
                        Item display, @Nullable ResourceLocation model, List<Item> drops) {

// 手持物品是否满足本阶段所需刀具。
        public boolean matches(ItemStack stack) {
            if (stack.isEmpty()) {
                return false;
            }
            if (toolItem != null) {
                return stack.is(toolItem);
            }
            if (toolTag != null) {
                return stack.is(toolTag);
            }
            return false;
        }
    }

// 解析一份定义；结构非法时返回 null（仅打印警告，不影响游戏）。
    @Nullable
    public static ButcheringDefinition parse(ResourceLocation id, JsonObject json) {
        Item input = itemById(string(json, "input"));
        if (input == null) {
            FlavorImmersedDaily.LOGGER.warn("Butchering: '{}' 缺少合法的 input 物品，已跳过", id);
            return null;
        }

        JsonElement stagesEl = json.get("stages");
        if (stagesEl == null || !stagesEl.isJsonArray()) {
            FlavorImmersedDaily.LOGGER.warn("Butchering: '{}' 缺少 stages 数组，已跳过", id);
            return null;
        }

        List<Stage> stages = new ArrayList<>();
        for (JsonElement element : stagesEl.getAsJsonArray()) {
            if (!element.isJsonObject()) {
                continue;
            }
            Stage stage = parseStage(element.getAsJsonObject(), input);
            if (stage != null) {
                stages.add(stage);
            }
        }
        if (stages.isEmpty()) {
            FlavorImmersedDaily.LOGGER.warn("Butchering: '{}' 没有任何有效阶段，已跳过", id);
            return null;
        }
        return new ButcheringDefinition(id, input, List.copyOf(stages));
    }

    @Nullable
    private static Stage parseStage(JsonObject json, Item defaultDisplay) {
        Item toolItem = null;
        TagKey<Item> toolTag = null;
        String tool = string(json, "tool");
        if (tool != null && !tool.isBlank()) {
            if (tool.startsWith("#")) {
                ResourceLocation tagId = ResourceLocation.tryParse(tool.substring(1));
                if (tagId != null) {
                    toolTag = TagKey.create(Registries.ITEM, tagId);
                }
            } else {
                toolItem = itemById(tool);
            }
        }
        if (toolItem == null && toolTag == null) {
            FlavorImmersedDaily.LOGGER.warn("Butchering: 阶段缺少合法的 tool，已跳过该阶段");
            return null;
        }

        Item display = itemById(string(json, "display"));
        if (display == null) {
            display = defaultDisplay;
        }

        ResourceLocation model = null;
        String modelStr = string(json, "model");
        if (modelStr != null && !modelStr.isBlank()) {
            model = ResourceLocation.tryParse(modelStr.trim());
        }

        List<Item> drops = new ArrayList<>();
        JsonElement dropsEl = json.get("drops");
        if (dropsEl != null && dropsEl.isJsonArray()) {
            JsonArray array = dropsEl.getAsJsonArray();
            for (JsonElement element : array) {
                if (element.isJsonPrimitive()) {
                    Item drop = itemById(element.getAsString().trim());
                    if (drop != null) {
                        drops.add(drop);
                    }
                }
            }
        }
        return new Stage(toolItem, toolTag, display, model, List.copyOf(drops));
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
