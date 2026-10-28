package com.flavor_immersed_daily.datagen.recipe;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.recipe.ModRecipes;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Consumer;

// Generates all data-pack recipes owned by Flavor Immersed Daily.
public final class WoodBasinRecipe extends RecipeProvider {
    public WoodBasinRecipe(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
        woodBasin(writer, "pineapple", ModItems.PINEAPPLE.get(), ModItems.PINEAPPLEJAM.get());
        woodBasin(writer, "strawberry", ModItems.STRAWBERRY.get(), ModItems.STRAWBERRYJAM.get());
        woodBasin(writer, "orange", ModItems.ORANGE.get(), ModItems.ORANGEJAM.get());
        woodBasin(writer, "winterjujube", ModItems.WINTERJUJUBE.get(), ModItems.WINTERJUJUBEJAM.get());
        woodBasin(writer, "hamimelon", ModItems.HAMIMELON.get(), ModItems.HAMIMELONJAM.get());
        woodBasin(writer, "dragonfruit", ModItems.DRAGONFRUIT.get(), ModItems.DRAGONFRUITJAM.get());
        woodBasin(writer, "tangerine", ModItems.TANGERINE.get(), ModItems.TANGERINEJAM.get());
        woodBasin(writer, "blueberry", ModItems.BLUEBERRY.get(), ModItems.BLUEBERRYJAM.get());
        woodBasin(writer, "pear", ModItems.PEAR.get(), ModItems.PEARJAM.get());
        woodBasin(writer, "lychee", ModItems.LYCHEE.get(), ModItems.LYCHEEJAM.get());
        woodBasin(writer, "plum", ModItems.PLUM.get(), ModItems.PLUMJAM.get());
        woodBasin(writer, "durian", ModItems.DURIAN.get(), ModItems.DURIANJAM.get());
        woodBasin(writer, "mango", ModItems.MANGO.get(), ModItems.MANGOJAM.get());
        woodBasin(writer, "kiwifruit", ModItems.KIWIFRUIT.get(), ModItems.KIWIFRUITJAM.get());
        woodBasin(writer, "pawpaw", ModItems.PAWPAW.get(), ModItems.PAWPAWJAM.get());
        woodBasin(writer, "lemon", ModItems.LEMON.get(), ModItems.LEMONJAM.get());
        woodBasin(writer, "loquat", ModItems.LOQUAT.get(), ModItems.LOQUATJAM.get());
        woodBasin(writer, "apple", Items.APPLE, ModItems.APPLEJAM.get());
        woodBasin(writer, "grape", ModItems.GRAPE.get(), ModItems.GRAPEJAM.get());
        woodBasin(writer, "greenplum", ModItems.GREENPLUM.get(), ModItems.GREEMPLUMJAM.get());
        woodBasin(writer, "greengrape", ModItems.GREENGRAPE.get(), ModItems.GREENGRAPEJAM.get());
        woodBasin(writer, "mulberry", ModItems.MULBERRY.get(), ModItems.MULBERRYJAM.get());
        woodBasin(writer, "hawthorn", ModItems.HAWTHORN.get(), ModItems.HAWTHORNJAM.get());
        woodBasin(writer, "mangosteen", ModItems.MANGOSTEEN.get(), ModItems.MANGOSTEENJAM.get());
        woodBasin(writer, "pomegranate", ModItems.POMEGRANATE.get(), ModItems.POMEGRANATEJAM.get());
        woodBasin(writer, "honeypeach", ModItems.HONEYPEACH.get(), ModItems.HONEYPEACHJAM.get());
        woodBasin(writer, "sweetmelon", ModItems.SWEETMELON.get(), ModItems.SWEETMELONJAM.get());
        woodBasin(writer, "sweetberry", Items.SWEET_BERRIES, ModItems.SWEETBERRYJAM.get());
        woodBasin(writer, "watermelon", Items.MELON_SLICE, ModItems.WATERMELONJAM.get());
        woodBasin(writer, "banana", ModItems.BANANA.get(), ModItems.BANANAJAM.get());
        woodBasin(writer, "apricot", ModItems.APRICOT.get(), ModItems.APRICOTJAM.get());
        woodBasin(writer, "carambola", ModItems.CARAMBOLA.get(), ModItems.CARAMBOLAJAM.get());
        woodBasin(writer, "coconut", ModItems.COCONUT.get(), ModItems.COCONUTJAM.get());
        woodBasin(writer, "cherry", ModItems.CHERRY.get(), ModItems.CHERRYJAM.get());
        woodBasin(writer, "nectarine", ModItems.NECTARINE.get(), ModItems.NECTARINEJAM.get());
    }

    private static void woodBasin(Consumer<FinishedRecipe> writer, String name, Item ingredient, Item result) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "wood_basin/" + name);
        writer.accept(new FinishedRecipe() {
            @Override
            public void serializeRecipeData(JsonObject json) {
                json.add("ingredient", Ingredient.of(ingredient).toJson());
                JsonObject resultJson = new JsonObject();
                resultJson.addProperty("item", BuiltInRegistries.ITEM.getKey(result).toString());
                json.add("result", resultJson);
            }

            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return ModRecipes.WOOD_BASIN_SERIALIZER.get();
            }

            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        });
    }
}