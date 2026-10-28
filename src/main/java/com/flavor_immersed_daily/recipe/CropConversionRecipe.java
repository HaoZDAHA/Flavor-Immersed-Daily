package com.flavor_immersed_daily.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

// 作物转换组配方。
// 单个 JSON 内列出的一组物品 ID（如各模组的 tomato）可以互相转换：把组内任意一个
// 物品放进作物鉴定机，即可转换为组内任意另一个物品。组内物品不要求全部已安装——
// 只有当前实际加载的模组提供的物品才会参与转换（解析时仅存物品 ID 字符串，
// 不校验存在性，因此未安装的模组物品不会导致数据包解析失败）。
public class CropConversionRecipe implements Recipe<SingleRecipeInput> {

    private final List<ResourceLocation> group;

    public CropConversionRecipe(List<ResourceLocation> group) {
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
        for (ResourceLocation id : group) {
            if (id.equals(excludeId)) {
                continue;
            }
            Item item = BuiltInRegistries.ITEM.get(id);
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
    public boolean matches(SingleRecipeInput input, Level level) {
        return contains(input.item().getItem());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        for (ResourceLocation id : group) {
            Item item = BuiltInRegistries.ITEM.get(id);
            if (item != Items.AIR) {
                return new ItemStack(item);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        for (ResourceLocation id : group) {
            Item item = BuiltInRegistries.ITEM.get(id);
            if (item != Items.AIR) {
                list.add(Ingredient.of(item));
            }
        }
        return list;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CROP_CONVERSION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CROP_CONVERSION_TYPE.get();
    }

    public static final MapCodec<CropConversionRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ResourceLocation.CODEC.listOf().fieldOf("group").forGetter(CropConversionRecipe::getGroupItems)
    ).apply(inst, CropConversionRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CropConversionRecipe> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public CropConversionRecipe decode(RegistryFriendlyByteBuf buf) {
            int count = buf.readVarInt();
            List<ResourceLocation> group = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                group.add(ResourceLocation.STREAM_CODEC.decode(buf));
            }
            return new CropConversionRecipe(group);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, CropConversionRecipe recipe) {
            buf.writeVarInt(recipe.getGroupItems().size());
            for (ResourceLocation id : recipe.getGroupItems()) {
                ResourceLocation.STREAM_CODEC.encode(buf, id);
            }
        }
    };
}
