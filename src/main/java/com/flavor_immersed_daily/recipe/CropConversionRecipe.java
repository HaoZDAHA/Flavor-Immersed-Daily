package com.flavor_immersed_daily.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

// 作物转换组配方。
// 单个 JSON 内列出的一组物品 ID（如各模组的 tomato）可以互相转换：把组内任意一个
// 物品放进作物鉴定机，即可转换为组内任意另一个物品。组内物品不要求全部已安装——
// 只有当前实际加载的模组提供的物品才会参与转换（解析时仅存物品 ID 字符串，
// 不校验存在性，因此未安装的模组物品不会导致数据包解析失败）。
public class CropConversionRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    private final List<ResourceLocation> group;

    public CropConversionRecipe(ResourceLocation id, List<ResourceLocation> group) {
        this.id = id;
        this.group = group;
    }

// 组内全部物品 ID（含未安装模组的物品）。
    public List<ResourceLocation> getGroupItems() {
        return group;
    }

// 组内“当前已加载”的物品（排除输入物品本身）。
    public List<Item> getLoadedItems(Item exclude) {
        List<Item> list = new ArrayList<>();
        ResourceLocation excludeId = exclude == null ? null : BuiltInRegistries.ITEM.getKey(exclude);
        for (ResourceLocation itemId : group) {
            if (itemId.equals(excludeId)) {
                continue;
            }
            Item item = BuiltInRegistries.ITEM.get(itemId);
            if (item != Items.AIR) {
                list.add(item);
            }
        }
        return list;
    }

// 输入物品是否属于本转换组。
    public boolean contains(Item item) {
        return group.contains(BuiltInRegistries.ITEM.getKey(item));
    }

    @Override
    public boolean matches(Container input, Level level) {
        return input.getContainerSize() >= 1 && contains(input.getItem(0).getItem());
    }

    @Override
    public ItemStack assemble(Container input, RegistryAccess registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registries) {
        for (ResourceLocation itemId : group) {
            Item item = BuiltInRegistries.ITEM.get(itemId);
            if (item != Items.AIR) {
                return new ItemStack(item);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        for (ResourceLocation itemId : group) {
            Item item = BuiltInRegistries.ITEM.get(itemId);
            if (item != Items.AIR) {
                list.add(Ingredient.of(item));
            }
        }
        return list;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CROP_CONVERSION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CROP_CONVERSION_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<CropConversionRecipe> {

        @Override
        public CropConversionRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonArray array = GsonHelper.getAsJsonArray(json, "group");
            List<ResourceLocation> group = new ArrayList<>();
            for (JsonElement element : array) {
                String itemId = GsonHelper.convertToString(element, "group");
                ResourceLocation parsed = ResourceLocation.tryParse(itemId);
                if (parsed == null) {
                    FlavorImmersedDaily.LOGGER.warn("CropConversion: 忽略非法物品 ID '{}'（配方 {}）", itemId, id);
                    continue;
                }
                group.add(parsed);
            }
            return new CropConversionRecipe(id, group);
        }

        @Override
        public CropConversionRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int count = buf.readVarInt();
            List<ResourceLocation> group = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                group.add(buf.readResourceLocation());
            }
            return new CropConversionRecipe(id, group);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CropConversionRecipe recipe) {
            buf.writeVarInt(recipe.getGroupItems().size());
            for (ResourceLocation itemId : recipe.getGroupItems()) {
                buf.writeResourceLocation(itemId);
            }
        }
    }
}
