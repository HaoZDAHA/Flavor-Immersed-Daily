package com.fidtest.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
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

    private final String appliance;
    private final List<Ingredient> ingredients;
    private final ItemStack result;
    private final Optional<ItemStack> container;
    private final int cookingTime;
    private final float experience;
    private final int minHeat;
// 模式：dry=烘干（默认），oven=烤箱；目前仅烘干机使用
    private final String mode;

// JEI/EMI 展示用的完整产物列表（如农夫乐事切割配方可能有多个产物）。
// 仅用于展示，不参与序列化与实际产出逻辑。
    private List<ItemStack> extraResults = List.of();

    public CookingRecipe(String appliance, List<Ingredient> ingredients, ItemStack result,
                         Optional<ItemStack> container, int cookingTime, float experience, int minHeat) {
        this(appliance, ingredients, result, container, cookingTime, experience, minHeat, "dry");
    }

    public CookingRecipe(String appliance, List<Ingredient> ingredients, ItemStack result,
                         Optional<ItemStack> container, int cookingTime, float experience, int minHeat, String mode) {
        this.appliance = appliance;
        this.ingredients = ingredients;
        this.result = result;
        this.container = container;
        this.cookingTime = cookingTime;
        this.experience = experience;
        this.minHeat = minHeat;
        this.mode = mode;
    }

    public static final MapCodec<CookingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("appliance", "").forGetter(CookingRecipe::getAppliance),
                    Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter(CookingRecipe::getIngredients),
                    ItemStack.CODEC.fieldOf("result").forGetter(CookingRecipe::getResult),
                    ItemStack.CODEC.optionalFieldOf("container").forGetter(CookingRecipe::getContainer),
                    Codec.INT.fieldOf("cookingTime").forGetter(CookingRecipe::getCookingTime),
                    Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(CookingRecipe::getExperience),
                    Codec.INT.optionalFieldOf("minHeat", 0).forGetter(CookingRecipe::getMinHeat),
                    Codec.STRING.optionalFieldOf("mode", "dry").forGetter(CookingRecipe::getMode)
            ).apply(instance, CookingRecipe::new)
    );

    public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, CookingRecipe> STREAM_CODEC =
            net.minecraft.network.codec.ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

    public String getAppliance() { return appliance; }

    public String getMode() { return mode; }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.addAll(ingredients);
        return list;
    }

    public ItemStack getResult() { return result; }

// 设置 JEI/EMI 展示用的完整产物列表（仅展示，不参与序列化）。
    public void setExtraResults(List<ItemStack> results) {
        this.extraResults = results == null ? List.of() : results;
    }

// JEI/EMI 展示用产物：有完整列表用完整列表，否则退回单个产物。
    public List<ItemStack> getDisplayResults() {
        return extraResults.isEmpty() ? List.of(result) : extraResults;
    }

// 获取烹饪产物；如果输入材料里有带 filling 标签（CUSTOM_DATA）的生食，
// 就把该标签一并复制到熟食上，并把熟食名字改为"馅料名 + 熟食名"（如：泥土水饺）。
    public ItemStack getCookedResult(List<ItemStack> inputs) {
        ItemStack out = result.copy();
        for (ItemStack s : inputs) {
            if (s.isEmpty()) continue;
            var custom = s.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
            if (custom != null && custom.getUnsafe().contains("filling")) {
                out.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, custom);
// 熟食名字 = 馅料中文名 + 熟食名
                String filling = custom.getUnsafe().getString("filling");
                ItemStack fillingItem = new ItemStack(
                        net.minecraft.core.registries.BuiltInRegistries.ITEM.get(
                                net.minecraft.resources.ResourceLocation.parse(filling)));
                if (!fillingItem.isEmpty()) {
                    String fillingName = fillingItem.getHoverName().getString();
                    String foodName = out.getHoverName().getString();
                    out.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME,
                            net.minecraft.network.chat.Component.literal(fillingName + foodName));
                }
                break;
            }
        }
        return out;
    }
    public Optional<ItemStack> getContainer() { return container; }
    public int getCookingTime() { return cookingTime; }
    public float getExperience() { return experience; }
    public int getMinHeat() { return minHeat; }

    @Override
    public boolean matches(CookingRecipeInput input, Level level) {
        List<ItemStack> inputItems = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            ItemStack s = input.getItem(i);
            if (!s.isEmpty()) inputItems.add(s.copy());
        }
        if (inputItems.size() < ingredients.size()) return false;

        List<Ingredient> remaining = new ArrayList<>(ingredients);
        for (ItemStack stack : inputItems) {
            boolean matched = false;
            for (int j = 0; j < remaining.size(); j++) {
                if (remaining.get(j).test(stack)) {
                    remaining.remove(j);
                    matched = true;
                    break;
                }
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
        for (int i = 0; i < input.size(); i++) {
            ItemStack s = input.getItem(i);
            if (!s.isEmpty()) inputs.add(s.copy());
        }
        if (inputs.isEmpty()) return false;

// 种类必须恰好被配方覆盖：出现配方不需要的材料，整批不匹配
        for (ItemStack stack : inputs) {
            boolean accepted = false;
            for (Ingredient ing : ingredients) {
                if (ing.test(stack)) {
                    accepted = true;
                    break;
                }
            }
            if (!accepted) return false;
        }

// 每种材料需要多少个（默认 1，取材料条目中各物品栈的最大数量）
        List<Integer> required = new ArrayList<>();
        for (Ingredient ing : ingredients) {
            int cost = 1;
            for (ItemStack is : ing.getItems()) {
                cost = Math.max(cost, is.getCount());
            }
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
    public ItemStack assemble(CookingRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) { return true; }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
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

    @Override
    public ItemStack getToastSymbol() { return this.result; }
}
