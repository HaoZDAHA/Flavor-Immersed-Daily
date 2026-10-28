package com.fidtest.item;

import com.fidtest.network.DishOpenPayload;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class DishItem extends Item {

    public DishItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    @SuppressWarnings("removal")
    public void initializeClient(Consumer<net.neoforged.neoforge.client.extensions.common.IClientItemExtensions> consumer) {
        consumer.accept(new net.neoforged.neoforge.client.extensions.common.IClientItemExtensions() {
            private final com.fidtest.client.DishItemRenderer renderer = new com.fidtest.client.DishItemRenderer();
            @Override
            public net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            if (level.isClientSide) {
                com.fidtest.client.DishItemClientHelper.openNamingScreen(player, held);
            }
            return InteractionResultHolder.success(held);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        FoodProperties food = stack.get(DataComponents.FOOD);
        if (food != null) {
            tooltipComponents.add(Component.translatable("tooltip.fidworkblock.dish_hunger", food.nutrition()));
        }

        int stirCount = getStirCount(stack);
        String levelKey = getCookingLevelKey(stirCount);
        tooltipComponents.add(Component.translatable("tooltip.fidworkblock.dish_cooking_level",
                Component.translatable(levelKey)));

        String seasoning = getSeasoningEffect(stack);
        if (!seasoning.isEmpty()) {
            try {
                ResourceLocation id = ResourceLocation.parse(seasoning);
                var item = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(id);
                if (item != null) {
                    Component displayName = new ItemStack(item).getHoverName();
                    tooltipComponents.add(Component.translatable("tooltip.fidworkblock.dish_seasoning", displayName));
                } else {
                    tooltipComponents.add(Component.translatable("tooltip.fidworkblock.dish_seasoning", seasoning));
                }
            } catch (Exception e) {
                tooltipComponents.add(Component.translatable("tooltip.fidworkblock.dish_seasoning", seasoning));
            }
        }

        String desc = getDishDescription(stack);
        if (!desc.isEmpty()) {
            tooltipComponents.add(Component.literal(desc).withColor(0xAAAAAA));
        }
        String chef = getChefName(stack);
        if (!chef.isEmpty()) {
            tooltipComponents.add(Component.translatable("tooltip.fidworkblock.dish_chef", chef));
        }
        tooltipComponents.add(Component.translatable("tooltip.fidworkblock.dish_hint").withColor(0x555555));
    }

    public static String getCookingLevelKey(int stirCount) {
        if (stirCount <= 2) return "cooking_level.fidworkblock.raw";
        if (stirCount <= 4) return "cooking_level.fidworkblock.undercooked";
        if (stirCount <= 9) return "cooking_level.fidworkblock.perfect";
        if (stirCount <= 12) return "cooking_level.fidworkblock.overcooked";
        return "cooking_level.fidworkblock.burnt";
    }

    public static boolean isBurnt(int stirCount) {
        return stirCount >= 13;
    }

    public static float getHungerMultiplier(int stirCount) {
        if (stirCount <= 2) return 0.25F;
        if (stirCount <= 4) return 0.5F;
        if (stirCount <= 9) return 1.0F;
        if (stirCount <= 12) return 0.5F;
        return 0.25F;
    }

    public static ItemStack createProceduralDish(List<ItemStack> inputs) {
        ItemStack dish = new ItemStack(com.fidtest.registration.ModItems.DISH.get());

        List<String> names = new ArrayList<>();
        int totalHunger = 0;
        for (ItemStack input : inputs) {
            ResourceLocation id = input.getItem().builtInRegistryHolder().key().location();
            names.add(id.toString());
            FoodProperties fp = input.getItem().components().get(DataComponents.FOOD);
            if (fp != null) {
                totalHunger += fp.nutrition();
            }
        }

        CompoundTag wokData = new CompoundTag();

        ListTag nameList = new ListTag();
        for (String n : names) {
            nameList.add(StringTag.valueOf(n));
        }
        wokData.put("Items", nameList);
        wokData.putInt("Hunger", Math.max(1, totalHunger / 2));

        CompoundTag root = new CompoundTag();
        root.put("WokDish", wokData);
        dish.set(DataComponents.CUSTOM_DATA, CustomData.of(root));

        return dish;
    }

    public static void setFoodProperties(ItemStack dish, List<ItemStack> inputs, int stirCount) {
        int totalHunger = 0;
        for (ItemStack input : inputs) {
            FoodProperties fp = input.getItem().components().get(DataComponents.FOOD);
            if (fp != null) {
                totalHunger += fp.nutrition();
            }
        }
        int dishHunger = Math.max(1, (int)(totalHunger * getHungerMultiplier(stirCount)));
        dish.set(DataComponents.FOOD, new FoodProperties(
                dishHunger, 0.2F, false, 1.6F, Optional.empty(), List.of()));
    }

    public static String getDishName(ItemStack dish) {
        try {
            CustomData custom = dish.get(DataComponents.CUSTOM_DATA);
            if (custom != null) {
                CompoundTag tag = custom.copyTag();
                if (tag.contains("WokDish")) {
                    CompoundTag wokData = tag.getCompound("WokDish");
                    if (wokData.contains("Name")) {
                        return wokData.getString("Name");
                    }
                }
            }
        } catch (Exception ignored) {}
        return "";
    }

    public static Component getDefaultDishName(ItemStack dish) {
        List<String> itemNames = getItemNames(dish);
        if (itemNames.isEmpty()) return Component.literal("炒菜");
        Component result = Component.empty();
        for (int i = 0; i < itemNames.size(); i++) {
            if (i > 0) result = result.copy().append("炒");
            try {
                ResourceLocation id = ResourceLocation.parse(itemNames.get(i));
                var item = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(id);
                if (item != null) {
                    result = result.copy().append(new ItemStack(item).getHoverName());
                } else {
                    result = result.copy().append(id.getPath());
                }
            } catch (Exception e) {
                result = result.copy().append(itemNames.get(i));
            }
        }
        return result;
    }

    public static void setDishName(ItemStack dish, String name, int color) {
        CustomData custom = dish.get(DataComponents.CUSTOM_DATA);
        CompoundTag root = (custom != null) ? custom.copyTag() : new CompoundTag();
        if (!root.contains("WokDish")) root.put("WokDish", new CompoundTag());
        CompoundTag wokData = root.getCompound("WokDish");
        wokData.putString("Name", name);
        wokData.putInt("NameColor", color);
        dish.set(DataComponents.CUSTOM_DATA, CustomData.of(root));

        String hex = String.format("#%06X", color & 0xFFFFFF);
        dish.set(DataComponents.CUSTOM_NAME,
                Component.literal(name).withColor(color));
    }

    public static void setDishDescription(ItemStack dish, String desc) {
        CustomData custom = dish.get(DataComponents.CUSTOM_DATA);
        CompoundTag root = (custom != null) ? custom.copyTag() : new CompoundTag();
        if (!root.contains("WokDish")) root.put("WokDish", new CompoundTag());
        root.getCompound("WokDish").putString("Description", desc);
        dish.set(DataComponents.CUSTOM_DATA, CustomData.of(root));
    }

    public static void setChefName(ItemStack dish, String chef) {
        CustomData custom = dish.get(DataComponents.CUSTOM_DATA);
        CompoundTag root = (custom != null) ? custom.copyTag() : new CompoundTag();
        if (!root.contains("WokDish")) root.put("WokDish", new CompoundTag());
        root.getCompound("WokDish").putString("Chef", chef);
        dish.set(DataComponents.CUSTOM_DATA, CustomData.of(root));
    }

    public static String getDishDescription(ItemStack dish) {
        try {
            CustomData custom = dish.get(DataComponents.CUSTOM_DATA);
            if (custom != null) {
                CompoundTag tag = custom.copyTag();
                if (tag.contains("WokDish")) {
                    CompoundTag wokData = tag.getCompound("WokDish");
                    if (wokData.contains("Description")) {
                        return wokData.getString("Description");
                    }
                }
            }
        } catch (Exception ignored) {}
        return "";
    }

    public static int getNameColor(ItemStack dish) {
        try {
            CustomData custom = dish.get(DataComponents.CUSTOM_DATA);
            if (custom != null) {
                CompoundTag tag = custom.copyTag();
                if (tag.contains("WokDish")) {
                    CompoundTag wokData = tag.getCompound("WokDish");
                    if (wokData.contains("NameColor")) {
                        return wokData.getInt("NameColor");
                    }
                }
            }
        } catch (Exception ignored) {}
        return 0xFFFFFF;
    }

    public static String getChefName(ItemStack dish) {
        try {
            CustomData custom = dish.get(DataComponents.CUSTOM_DATA);
            if (custom != null) {
                CompoundTag tag = custom.copyTag();
                if (tag.contains("WokDish")) {
                    CompoundTag wokData = tag.getCompound("WokDish");
                    if (wokData.contains("Chef")) {
                        return wokData.getString("Chef");
                    }
                }
            }
        } catch (Exception ignored) {}
        return "";
    }

    public static List<String> getItemNames(ItemStack dish) {
        List<String> names = new ArrayList<>();
        try {
            CustomData custom = dish.get(DataComponents.CUSTOM_DATA);
            if (custom != null) {
                CompoundTag tag = custom.copyTag();
                if (tag.contains("WokDish")) {
                    CompoundTag wokData = tag.getCompound("WokDish");
                    ListTag list = wokData.getList("Items", 8);
                    for (int i = 0; i < list.size(); i++) {
                        names.add(list.getString(i));
                    }
                }
            }
        } catch (Exception ignored) {}
        return names;
    }

    public static int getHungerValue(ItemStack dish) {
        try {
            CustomData custom = dish.get(DataComponents.CUSTOM_DATA);
            if (custom != null) {
                CompoundTag tag = custom.copyTag();
                if (tag.contains("WokDish")) {
                    CompoundTag wokData = tag.getCompound("WokDish");
                    if (wokData.contains("Hunger")) {
                        return wokData.getInt("Hunger");
                    }
                }
            }
        } catch (Exception ignored) {}
        return 1;
    }

    public static void setSeasoningEffect(ItemStack dish, String seasoning) {
        CustomData custom = dish.get(DataComponents.CUSTOM_DATA);
        CompoundTag root = (custom != null) ? custom.copyTag() : new CompoundTag();
        root.putString("seasoning", seasoning);
        dish.set(DataComponents.CUSTOM_DATA, CustomData.of(root));
    }

    public static String getSeasoningEffect(ItemStack dish) {
        try {
            CustomData custom = dish.get(DataComponents.CUSTOM_DATA);
            if (custom != null) {
                CompoundTag tag = custom.copyTag();
                if (tag.contains("seasoning")) {
                    return tag.getString("seasoning");
                }
            }
        } catch (Exception ignored) {}
        return "";
    }

    public static void setStirCount(ItemStack dish, int stirCount) {
        CustomData custom = dish.get(DataComponents.CUSTOM_DATA);
        CompoundTag root = (custom != null) ? custom.copyTag() : new CompoundTag();
        if (!root.contains("WokDish")) root.put("WokDish", new CompoundTag());
        root.getCompound("WokDish").putInt("StirCount", stirCount);
        dish.set(DataComponents.CUSTOM_DATA, CustomData.of(root));
    }

    public static int getStirCount(ItemStack dish) {
        try {
            CustomData custom = dish.get(DataComponents.CUSTOM_DATA);
            if (custom != null) {
                CompoundTag tag = custom.copyTag();
                if (tag.contains("WokDish")) {
                    CompoundTag wokData = tag.getCompound("WokDish");
                    if (wokData.contains("StirCount")) {
                        return wokData.getInt("StirCount");
                    }
                }
            }
        } catch (Exception ignored) {}
        return 0;
    }
}
