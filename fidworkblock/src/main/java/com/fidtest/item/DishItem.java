package com.fidtest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.ArrayList;
import java.util.List;

public class DishItem extends Item {

    public DishItem(Properties p) { super(p.stacksTo(1).food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2F).build())); }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
        consumer.accept(new net.minecraftforge.client.extensions.common.IClientItemExtensions() {
            private final com.fidtest.client.DishItemRenderer renderer = new com.fidtest.client.DishItemRenderer();
            @Override
            public net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    @Override public UseAnim getUseAnimation(ItemStack s) { return UseAnim.EAT; }
    @Override public int getUseDuration(ItemStack s) { return 32; }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        int hunger = getHungerValue(stack);
        if (entity instanceof Player p) p.getFoodData().eat(hunger, 0.2F);
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            if (level.isClientSide) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                        () -> () -> com.fidtest.FidworkModClient.openDishNamingScreen(hand));
            }
            return InteractionResultHolder.success(held);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tips, TooltipFlag flag) {
        int hunger = getHungerValue(stack);
        tips.add(Component.translatable("tooltip.fidworkblock.dish_hunger", hunger));
        int sc = getStirCount(stack);
        tips.add(Component.translatable("tooltip.fidworkblock.dish_cooking_level", Component.translatable(getCookingLevelKey(sc))));
        String seasoning = getSeasoningEffect(stack);
        if (!seasoning.isEmpty()) try {
            tips.add(Component.translatable("tooltip.fidworkblock.dish_seasoning", new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(seasoning))).getHoverName()));
        } catch (Exception e) { tips.add(Component.literal(seasoning)); }
        String desc = getDishDescription(stack);
        if (!desc.isEmpty()) tips.add(Component.literal(desc).withStyle(ChatFormatting.GRAY));
        String chef = getChefName(stack);
        if (!chef.isEmpty()) tips.add(Component.translatable("tooltip.fidworkblock.dish_chef", chef));
        tips.add(Component.translatable("tooltip.fidworkblock.dish_hint").withStyle(ChatFormatting.DARK_GRAY));
    }

    public static String getCookingLevelKey(int sc) {
        if (sc <= 2) return "cooking_level.fidworkblock.raw"; if (sc <= 4) return "cooking_level.fidworkblock.undercooked";
        if (sc <= 9) return "cooking_level.fidworkblock.perfect"; if (sc <= 12) return "cooking_level.fidworkblock.overcooked";
        return "cooking_level.fidworkblock.burnt";
    }

    private static CompoundTag getWokData(ItemStack dish) { CompoundTag t = dish.getTag(); return t != null && t.contains("WokDish") ? t.getCompound("WokDish") : null; }
    private static CompoundTag getOrCreateWokData(ItemStack dish) { CompoundTag t = dish.getOrCreateTag(); if (!t.contains("WokDish")) t.put("WokDish", new CompoundTag()); return t.getCompound("WokDish"); }

    public static ItemStack createProceduralDish(List<ItemStack> inputs) {
        ItemStack dish = new ItemStack(com.fidtest.registration.ModItems.DISH.get());
        CompoundTag wok = new CompoundTag();
        ListTag colorList = new ListTag(), nameList = new ListTag();
        int total = 0;
        for (ItemStack in : inputs) {
            String id = in.getItem().builtInRegistryHolder().key().location().toString();
            nameList.add(StringTag.valueOf(id));
            colorList.add(IntTag.valueOf(hashToColor(id)));
            FoodProperties fp = in.getItem().getFoodProperties(in, null);
            if (fp != null) total += fp.getNutrition();
        }
        wok.put("Colors", colorList); wok.put("Items", nameList); wok.putInt("Hunger", Math.max(1, total / 2));
        dish.getOrCreateTag().put("WokDish", wok);
        dish.setHoverName(getDefaultDishName(dish));
        return dish;
    }

    public static Component getDefaultDishName(ItemStack dish) {
        List<String> itemNames = getItemNames(dish);
        if (itemNames.isEmpty()) return Component.translatable("item.fidworkblock.dish");
        MutableComponent result = Component.empty();
        for (int i = 0; i < itemNames.size(); i++) {
            if (i > 0) result = result.copy().append("炒");
            try {
                var item = BuiltInRegistries.ITEM.get(new ResourceLocation(itemNames.get(i)));
                if (item != null) result = result.copy().append(new ItemStack(item).getHoverName());
                else result = result.copy().append(itemNames.get(i));
            } catch (Exception e) { result = result.copy().append(itemNames.get(i)); }
        }
        return result;
    }

    public static void setDishName(ItemStack dish, String name, int color) {
        getOrCreateWokData(dish).putString("Name", name);
        getOrCreateWokData(dish).putInt("NameColor", color);
        dish.setHoverName(Component.literal(name).withStyle(s -> s.withColor(color)));
    }

    public static void setDishDescription(ItemStack dish, String desc) { getOrCreateWokData(dish).putString("Description", desc); }
    public static void setChefName(ItemStack dish, String chef) { getOrCreateWokData(dish).putString("Chef", chef); }

    public static String getDishDescription(ItemStack dish) { CompoundTag w = getWokData(dish); return w != null && w.contains("Description") ? w.getString("Description") : ""; }
    public static String getChefName(ItemStack dish) { CompoundTag w = getWokData(dish); return w != null && w.contains("Chef") ? w.getString("Chef") : ""; }

    public static void setFoodProperties(ItemStack dish, List<ItemStack> inputs, int sc) {
        int h = 0;
        for (ItemStack in : inputs) { FoodProperties fp = in.getItem().getFoodProperties(in, null); if (fp != null) h += fp.getNutrition(); }
        getOrCreateWokData(dish).putInt("Hunger", Math.max(1, (int)(h * getHungerMultiplier(sc))));
    }

    public static float getHungerMultiplier(int sc) { if (sc <= 2) return 0.25F; if (sc <= 4) return 0.5F; if (sc <= 9) return 1.0F; if (sc <= 12) return 0.5F; return 0.25F; }

    public static void setStirCount(ItemStack dish, int sc) { getOrCreateWokData(dish).putInt("StirCount", sc); }
    public static int getStirCount(ItemStack dish) { CompoundTag w = getWokData(dish); return w != null && w.contains("StirCount") ? w.getInt("StirCount") : 0; }
    public static void setSeasoningEffect(ItemStack dish, String s) { dish.getOrCreateTag().putString("seasoningeffect", s); }
    public static String getSeasoningEffect(ItemStack dish) { CompoundTag t = dish.getTag(); return t != null && t.contains("seasoningeffect") ? t.getString("seasoningeffect") : ""; }
    public static int getHungerValue(ItemStack dish) { CompoundTag w = getWokData(dish); return w != null && w.contains("Hunger") ? w.getInt("Hunger") : 4; }

    public static List<String> getItemNames(ItemStack dish) {
        List<String> names = new ArrayList<>();
        CompoundTag w = getWokData(dish);
        if (w != null && w.contains("Items")) { ListTag l = w.getList("Items", 8); for (int i = 0; i < l.size(); i++) names.add(l.getString(i)); }
        return names;
    }

    private static int hashToColor(String name) { int h = name.hashCode(); int r = Math.min(((h>>16)&0xFF)|0x40, 220); int g = Math.min(((h>>8)&0xFF)|0x40, 220); int b = Math.min((h&0xFF)|0x40, 220); return (r<<16)|(g<<8)|b; }
}
