package com.fidtest.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CookingRecipe implements Recipe<CookingRecipeInput> {

    private final ResourceLocation id;
    private final String appliance;
    private final List<Ingredient> ingredients;
    private final ItemStack result;
    private final Optional<ItemStack> container;
    private final int cookingTime;
    private final float experience;
    private final int minHeat;
// 模式：dry=烘干（默认），oven=烤箱；目前仅变温烤箱使用
    private final String mode;

// 额外产物（仅用于 JEI 展示多产物配方，不参与序列化）
    private final List<ItemStack> extraResults = new ArrayList<>();

    public CookingRecipe(ResourceLocation id, String appliance, List<Ingredient> ingredients,
                         ItemStack result, Optional<ItemStack> container,
                         int cookingTime, float experience, int minHeat) {
        this(id, appliance, ingredients, result, container, cookingTime, experience, minHeat, "dry");
    }

    public CookingRecipe(ResourceLocation id, String appliance, List<Ingredient> ingredients,
                         ItemStack result, Optional<ItemStack> container,
                         int cookingTime, float experience, int minHeat, String mode) {
        this.id = id;
        this.appliance = appliance;
        this.ingredients = ingredients;
        this.result = result;
        this.container = container;
        this.cookingTime = cookingTime;
        this.experience = experience;
        this.minHeat = minHeat;
        this.mode = mode;
    }

    @Override
    public ResourceLocation getId() { return id; }

    public String getAppliance() { return appliance; }
    public String getMode() { return mode; }

// 获取烹饪产物；如果输入材料里有带 filling 标签的生食，
// 就把该标签一并复制到熟食上，并把熟食名字改为"馅料名 + 熟食名"（如：泥土水饺）。
    public ItemStack getCookedResult(List<ItemStack> inputs) {
        ItemStack out = result.copy();
        for (ItemStack s : inputs) {
            if (s.isEmpty()) continue;
            CompoundTag tag = s.getTag();
            if (tag != null && tag.contains("filling")) {
// 熟食名字 = 馅料中文名 + 熟食名
                String filling = tag.getString("filling");
                ItemStack fillingItem = new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(filling)));
                String foodName = out.getHoverName().getString();
// 复制生食的自定义 NBT（filling 标签）
                out.setTag(tag.copy());
                if (!fillingItem.isEmpty()) {
                    out.setHoverName(Component.literal(fillingItem.getHoverName().getString() + foodName));
                }
                break;
            }
        }
        return out;
    }
    public ItemStack getResult() { return result; }
    public Optional<ItemStack> getContainer() { return container; }

// 额外产物（仅 JEI 展示用）
    public List<ItemStack> getExtraResults() { return extraResults; }

// 设置额外产物（仅 JEI 展示用）
    public void setExtraResults(List<ItemStack> results) {
        extraResults.clear();
        if (results != null) extraResults.addAll(results);
    }
    public int getCookingTime() { return cookingTime; }
    public float getExperience() { return experience; }
    public int getMinHeat() { return minHeat; }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.addAll(ingredients);
        return list;
    }

    @Override
    public boolean matches(CookingRecipeInput input, Level level) {
        List<ItemStack> inputItems = new ArrayList<>();
        for (int i = 0; i < input.getContainerSize(); i++) {
            ItemStack s = input.getItem(i);
            if (!s.isEmpty()) inputItems.add(s.copy());
        }
        if (inputItems.size() < ingredients.size()) return false;

        List<Ingredient> remaining = new ArrayList<>(ingredients);
        for (ItemStack stack : inputItems) {
            boolean matched = false;
            for (int j = 0; j < remaining.size(); j++) {
                if (remaining.get(j).test(stack)) { remaining.remove(j); matched = true; break; }
            }
            if (!matched) return false;
        }
        return remaining.isEmpty();
    }

// 严谨匹配（茶壶、罐子用）：放了什么材料就匹配需要这些材料的配方。
// 1. 输入的每种材料都必须本配方需要，混入了配方之外的材料就不匹配——
// 避免出现子集歧义（例如做酱油的材料，被"只需其中两种"的豆芽配方误认）；
// 2. 同种材料允许堆在同一格或分散在多格，按数量跨槽累计，只要配方里的每种材料都能凑够数量即可。
    public boolean matchesAccurate(CookingRecipeInput input, Level level) {
        if (ingredients.isEmpty()) return false;

        List<ItemStack> inputs = new ArrayList<>();
        for (int i = 0; i < input.getContainerSize(); i++) {
            ItemStack s = input.getItem(i);
            if (!s.isEmpty()) inputs.add(s.copy());
        }
        if (inputs.isEmpty()) return false;

// 种类必须恰好被配方覆盖：出现配方不需要的材料，整批不匹配
        for (ItemStack stack : inputs) {
            boolean accepted = false;
            for (Ingredient ing : ingredients) {
                if (ing.test(stack)) { accepted = true; break; }
            }
            if (!accepted) return false;
        }

// 每种材料需要多少个（默认 1，取材料条目中各物品栈的最大数量）
        List<Integer> required = new ArrayList<>();
        for (Ingredient ing : ingredients) {
            int cost = 1;
            for (ItemStack is : ing.getItems()) cost = Math.max(cost, is.getCount());
            required.add(cost);
        }

// 每个输入栈可以按数量贡献给多条同种材料条目（如荞麦茶需要两格/两堆荞麦），但不能超过它自身的数量
        int[] consumed = new int[ingredients.size()];
        for (ItemStack stack : inputs) {
            int remaining = stack.getCount();
            for (int j = 0; j < ingredients.size() && remaining > 0; j++) {
                if (consumed[j] >= required.get(j)) continue;
                if (ingredients.get(j).test(stack)) {
                    int take = Math.min(required.get(j) - consumed[j], remaining);
                    consumed[j] += take;
                    remaining -= take;
                }
            }
        }

        for (int j = 0; j < ingredients.size(); j++) {
            if (consumed[j] < required.get(j)) return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(CookingRecipeInput input, RegistryAccess access) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) { return true; }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return this.result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.COOKING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.COOKING.get();
    }

    @Override
    public boolean isIncomplete() {
        return ingredients.stream().anyMatch(Ingredient::isEmpty);
    }

    public static class Serializer implements RecipeSerializer<CookingRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public CookingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String appliance = json.has("appliance") ? json.get("appliance").getAsString() : "";

            JsonArray arr = json.getAsJsonArray("ingredients");
            List<Ingredient> ings = new ArrayList<>();
            for (JsonElement e : arr) ings.add(Ingredient.fromJson(e));

            JsonObject res = json.getAsJsonObject("result");

// 兼容 "id" 和 "item" 两种字段名
            String itemId;
            if (res.has("id")) {
                itemId = res.get("id").getAsString();
            } else if (res.has("item")) {
                itemId = res.get("item").getAsString();
            } else {
                throw new com.google.gson.JsonSyntaxException("Missing 'id' or 'item' field in result");
            }

            ResourceLocation resId = new ResourceLocation(itemId);
            int cnt = res.has("count") ? res.get("count").getAsInt() : 1;
            ItemStack result = new ItemStack(BuiltInRegistries.ITEM.get(resId), cnt);

            Optional<ItemStack> container = Optional.empty();
            if (json.has("container")) {
                JsonObject co = json.getAsJsonObject("container");

// 兼容 container 中的 "id" 和 "item" 两种字段名
                String containerId;
                if (co.has("id")) {
                    containerId = co.get("id").getAsString();
                } else if (co.has("item")) {
                    containerId = co.get("item").getAsString();
                } else {
                    throw new com.google.gson.JsonSyntaxException("Missing 'id' or 'item' field in container");
                }

                ResourceLocation cId = new ResourceLocation(containerId);
                int cCnt = co.has("count") ? co.get("count").getAsInt() : 1;
                container = Optional.of(new ItemStack(BuiltInRegistries.ITEM.get(cId), cCnt));
            }

            int time = json.has("cookingtime") || json.has("cookingTime") ?
                       (json.has("cookingtime") ? json.get("cookingtime").getAsInt() : json.get("cookingTime").getAsInt()) : 200;
            float exp = json.has("experience") ? json.get("experience").getAsFloat() : 0F;
            int heat = json.has("minHeat") ? json.get("minHeat").getAsInt() : 0;
            String mode = json.has("mode") ? json.get("mode").getAsString() : "dry";

            return new CookingRecipe(recipeId, appliance, ings, result, container, time, exp, heat, mode);
        }

        @Override
        public CookingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
            String app = buf.readUtf();
            int n = buf.readVarInt();
            List<Ingredient> ings = new ArrayList<>();
            for (int i = 0; i < n; i++) ings.add(Ingredient.fromNetwork(buf));
            ItemStack result = buf.readItem();
            boolean hasC = buf.readBoolean();
            Optional<ItemStack> container = hasC ? Optional.of(buf.readItem()) : Optional.empty();
            int time = buf.readVarInt();
            float exp = buf.readFloat();
            int heat = buf.readVarInt();
            String mode = buf.readUtf();
            return new CookingRecipe(recipeId, app, ings, result, container, time, exp, heat, mode);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CookingRecipe r) {
            buf.writeUtf(r.appliance);
            buf.writeVarInt(r.ingredients.size());
            for (Ingredient ing : r.ingredients) ing.toNetwork(buf);
            buf.writeItem(r.result);
            buf.writeBoolean(r.container.isPresent());
            r.container.ifPresent(buf::writeItem);
            buf.writeVarInt(r.cookingTime);
            buf.writeFloat(r.experience);
            buf.writeVarInt(r.minHeat);
            buf.writeUtf(r.mode);
        }
    }
}
