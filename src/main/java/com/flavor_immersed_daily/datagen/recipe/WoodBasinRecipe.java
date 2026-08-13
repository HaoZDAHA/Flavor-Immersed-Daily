package com.flavor_immersed_daily.datagen.recipe;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

/** Generates all data-pack recipes owned by Flavor Immersed Daily. */
public final class WoodBasinRecipe extends RecipeProvider {
    public WoodBasinRecipe(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        woodBasin(output, "pineapple", ModItems.PINEAPPLE.get(), ModItems.PINEAPPLEJAM.get());
        woodBasin(output, "strawberry", ModItems.STRAWBERRY.get(), ModItems.STRAWBERRYJAM.get());
        woodBasin(output, "orange", ModItems.ORANGE.get(), ModItems.ORANGEJAM.get());
        woodBasin(output, "winterjujube", ModItems.WINTERJUJUBE.get(), ModItems.WINTERJUJUBEJAM.get());
        woodBasin(output, "hamimelon", ModItems.HAMIMELON.get(), ModItems.HAMIMELONJAM.get());
        woodBasin(output, "dragonfruit", ModItems.DRAGONFRUIT.get(), ModItems.DRAGONFRUITJAM.get());
        woodBasin(output, "tangerine", ModItems.TANGERINE.get(), ModItems.TANGERINEJAM.get());
        woodBasin(output, "blueberry", ModItems.BLUEBERRY.get(), ModItems.BLUEBERRYJAM.get());
        woodBasin(output, "pear", ModItems.PEAR.get(), ModItems.PEARJAM.get());
        woodBasin(output, "lychee", ModItems.LYCHEE.get(), ModItems.LYCHEEJAM.get());
        woodBasin(output, "plum", ModItems.PLUM.get(), ModItems.PLUMJAM.get());
        woodBasin(output, "durian", ModItems.DURIAN.get(), ModItems.DURIANJAM.get());
        woodBasin(output, "mango", ModItems.MANGO.get(), ModItems.MANGOJAM.get());
        woodBasin(output, "kiwifruit", ModItems.KIWIFRUIT.get(), ModItems.KIWIFRUITJAM.get());
        woodBasin(output, "pawpaw", ModItems.PAWPAW.get(), ModItems.PAWPAWJAM.get());
        woodBasin(output, "lemon", ModItems.LEMON.get(), ModItems.LEMONJAM.get());
        woodBasin(output, "loquat", ModItems.LOQUAT.get(), ModItems.LOQUATJAM.get());
        woodBasin(output, "apple", Items.APPLE, ModItems.APPLEJAM.get());
        woodBasin(output, "grape", ModItems.GRAPE.get(), ModItems.GRAPEJAM.get());
        woodBasin(output, "greenplum", ModItems.GREENPLUM.get(), ModItems.GREEMPLUMJAM.get());
        woodBasin(output, "greengrape", ModItems.GREENGRAPE.get(), ModItems.GREENGRAPEJAM.get());
        woodBasin(output, "mulberry", ModItems.MULBERRY.get(), ModItems.MULBERRYJAM.get());
        woodBasin(output, "hawthorn", ModItems.HAWTHORN.get(), ModItems.HAWTHORNJAM.get());
        woodBasin(output, "mangosteen", ModItems.MANGOSTEEN.get(), ModItems.MANGOSTEENJAM.get());
        woodBasin(output, "pomegranate", ModItems.POMEGRANATE.get(), ModItems.POMEGRANATEJAM.get());
        woodBasin(output, "honeypeach", ModItems.HONEYPEACH.get(), ModItems.HONEYPEACHJAM.get());
        woodBasin(output, "sweetmelon", ModItems.SWEETMELON.get(), ModItems.SWEETMELONJAM.get());
        woodBasin(output, "sweetberry", Items.SWEET_BERRIES, ModItems.SWEETBERRYJAM.get());
        woodBasin(output, "watermelon", Items.MELON_SLICE, ModItems.WATERMELONJAM.get());
        woodBasin(output, "banana", ModItems.BANANA.get(), ModItems.BANANAJAM.get());
        woodBasin(output, "apricot", ModItems.APRICOT.get(), ModItems.APRICOTJAM.get());
        woodBasin(output, "carambola", ModItems.CARAMBOLA.get(), ModItems.CARAMBOLAJAM.get());
        woodBasin(output, "coconut", ModItems.COCONUT.get(), ModItems.COCONUTJAM.get());
        woodBasin(output, "cherry", ModItems.CHERRY.get(), ModItems.CHERRYJAM.get());
        woodBasin(output, "nectarine", ModItems.NECTARINE.get(), ModItems.NECTARINEJAM.get());
    }

    private static void woodBasin(RecipeOutput output, String name, Item ingredient, Item result) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, "wood_basin/" + name);
        output.accept(id, new com.flavor_immersed_daily.recipe.WoodBasinRecipe(Ingredient.of(ingredient), new ItemStack(result)), null);
    }
}
