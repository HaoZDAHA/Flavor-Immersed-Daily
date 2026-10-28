package com.flavor_immersed_daily.basin;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// 木盆泡洗定义（数据包 JSON）——供其他模组 / 数据包扩展木盆的漂洗流程。
// 文件位置：data/<任意命名空间>/basin_wash/<名称>.json，本模组会自动加载所有命名空间下的定义。
// <pre>{@code
// {
// "input": "my_mod:bled_duck", // 放进有水木盆的物品
// "output": "my_mod:raw_dressed_duck", // 右键取回时给回的物品（可选，省略=只出 drops）
// "drops": ["minecraft:feather"], // 取回时的额外掉落（可选）
// "model": "my_mod:block/bighook/duck_3", // 泡洗时木盆里显示的自定义方块模型（可选）
// "name": "漂洗" // 展示名（可选，仅用于 JEI/EMI 展示）
// }
// }</pre>
public record BasinWashDefinition(ResourceLocation id, Item input, @Nullable Item output,
                                  List<Item> drops, @Nullable ResourceLocation model, @Nullable String name) {

// 解析一份定义；结构非法时返回 null（仅打印警告，不影响游戏）。
    @Nullable
    public static BasinWashDefinition parse(ResourceLocation id, JsonObject json) {
        Item input = itemById(string(json, "input"));
        if (input == null) {
            FlavorImmersedDaily.LOGGER.warn("BasinWash: '{}' 缺少合法的 input 物品，已跳过", id);
            return null;
        }

        Item output = itemById(string(json, "output"));

        List<Item> drops = new ArrayList<>();
        JsonElement dropsEl = json.get("drops");
        if (dropsEl != null && dropsEl.isJsonArray()) {
            for (JsonElement element : dropsEl.getAsJsonArray()) {
                if (element.isJsonPrimitive()) {
                    Item drop = itemById(element.getAsString().trim());
                    if (drop != null) {
                        drops.add(drop);
                    }
                }
            }
        }

        ResourceLocation model = null;
        String modelStr = string(json, "model");
        if (modelStr != null && !modelStr.isBlank()) {
            model = ResourceLocation.tryParse(modelStr.trim());
        }
        return new BasinWashDefinition(id, input, output, List.copyOf(drops), model, string(json, "name"));
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
