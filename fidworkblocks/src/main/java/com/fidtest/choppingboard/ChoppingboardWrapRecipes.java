package com.fidtest.choppingboard;

import com.fidtest.ExampleMod;
import com.fidtest.block.entity.ChoppingboardBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 砧板「包裹/成型」工序的展示数据（JEI 与 EMI 共用，保证两侧始终同步）。
// 四种工序（mode）：
// wrap —— 皮 + 任意馅料 → 生食（包子/饺子/烧麦/馄饨/汤圆/馅饼）
// mooncake —— 生馅饼 + 月饼模具 → 生月饼
// zongzi —— 芦苇叶 + 馅料 + 糯米 → 生粽子
// roll —— 面团/面剂子/面皮 + 擀面杖 → 下一步形态
// 这些工序写在方块逻辑里（不是 JSON 配方），故 JEI/EMI 展示配方在此统一合成。
public final class ChoppingboardWrapRecipes {

// 工艺类型：包裹
    public static final String MODE_WRAP = "wrap";
// 工艺类型：压月饼
    public static final String MODE_MOONCAKE = "mooncake";
// 工艺类型：包粽子
    public static final String MODE_ZONGZI = "zongzi";
// 工艺类型：擀面杖擀制
    public static final String MODE_ROLL = "roll";

    private ChoppingboardWrapRecipes() {
    }

// 一条展示配方：输入 → 输出，附带该工序的 mode。
    public record Entry(ResourceLocation id, ItemStack input, ItemStack output, String mode) {
    }

// 合成全部包裹/成型工序的展示配方。
    public static List<Entry> collect() {
        List<Entry> result = new ArrayList<>();

// 六种皮 + 任意馅料 → 对应生食（芦苇叶走粽子流程，不在这个映射里）
        for (ResourceLocation skinId : ChoppingboardBlockEntity.SKIN_IDS) {
            ResourceLocation rawId = ChoppingboardBlockEntity.RAW_FOOD_MAP.get(skinId);
            if (rawId == null) continue;
            ItemStack skin = stackOf(skinId);
            ItemStack raw = stackOf(rawId);
            if (skin.isEmpty() || raw.isEmpty()) continue;
            result.add(new Entry(recipeId("wrap/" + rawId.getPath()), skin, raw, MODE_WRAP));
        }

// 生馅饼 + 月饼模具 → 生月饼（继承馅料的 filling 标签）
        ItemStack pie = stackOf(ChoppingboardBlockEntity.RAW_PIE_ID);
        ItemStack mooncake = stackOf(ChoppingboardBlockEntity.RAW_MOONCAKE_ID);
        if (!pie.isEmpty() && !mooncake.isEmpty()) {
            result.add(new Entry(recipeId("wrap/rawmooncake"), pie, mooncake, MODE_MOONCAKE));
        }

// 芦苇叶 + 馅料 + 糯米 → 生粽子
        ItemStack reedLeaf = stackOf(ChoppingboardBlockEntity.REED_LEAF_ID);
        ItemStack zongzi = stackOf(ChoppingboardBlockEntity.RAW_ZONGZI_ID);
        if (!reedLeaf.isEmpty() && !zongzi.isEmpty()) {
            result.add(new Entry(recipeId("wrap/rawazongzi"), reedLeaf, zongzi, MODE_ZONGZI));
        }

// 擀面杖：面团/面剂子/面皮 → 下一步形态（原地替换，可连续擀制）
        for (Map.Entry<ResourceLocation, ResourceLocation> entry
                : ChoppingboardBlockEntity.ROLLING_PIN_MAP.entrySet()) {
            ItemStack input = stackOf(entry.getKey());
            ItemStack output = stackOf(entry.getValue());
            if (input.isEmpty() || output.isEmpty()) continue;
            result.add(new Entry(recipeId("roll/" + entry.getKey().getPath()), input, output, MODE_ROLL));
        }

        return result;
    }

// 该工序需要的工具/材料（没有则返回空栈）。
    public static ItemStack toolFor(String mode) {
        if (MODE_MOONCAKE.equals(mode)) {
            return stackOf(ChoppingboardBlockEntity.MOONCAKE_MOLD_ID);
        }
        if (MODE_ZONGZI.equals(mode)) {
            return stackOf(ChoppingboardBlockEntity.RICE_ID);
        }
        if (MODE_ROLL.equals(mode)) {
            return stackOf(ChoppingboardBlockEntity.ROLLING_PIN_ID);
        }
        return ItemStack.EMPTY;
    }

// 工序说明的翻译键。
    public static String noteKey(String mode) {
        return switch (mode) {
            case MODE_MOONCAKE -> "jei.fidworkblock.choppingboard.note.mooncake";
            case MODE_ZONGZI -> "jei.fidworkblock.choppingboard.note.zongzi";
            case MODE_ROLL -> "jei.fidworkblock.choppingboard.note.roll";
            default -> "jei.fidworkblock.choppingboard.note.wrap";
        };
    }

    private static ResourceLocation recipeId(String path) {
        return ResourceLocation.fromNamespaceAndPath(ExampleMod.MODID, path);
    }

    private static ItemStack stackOf(ResourceLocation id) {
        return new ItemStack(BuiltInRegistries.ITEM.get(id));
    }
}
