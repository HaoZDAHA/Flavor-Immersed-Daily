package com.flavor_immersed_daily.datagen;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

import java.util.Set;
import java.util.Map;

/** Generates one item model for every item owned by this mod. */
public final class FIDItemModelProvider {

    private static final Map<String, ResourceLocation> TEXTURE_OVERRIDES = Map.ofEntries(
            Map.entry("chineseleavesseed", modTexture("item/chinese_leaves_seeds")),
            Map.entry("cowpeabeanseed", modTexture("item/cowpeabean")),
            Map.entry("firecracker", ResourceLocation.withDefaultNamespace("item/firework_rocket")),
            Map.entry("greengrapeseed", modTexture("item/grapeseed")),
            Map.entry("millet_grain", modTexture("item/milletgrain")),
            Map.entry("radishseed", modTexture("item/radish_seed")),
            Map.entry("applesapling", modTexture("block/applesapling")),
            Map.entry("apricotsapling", modTexture("block/apricotsapling")),
            Map.entry("carambolasapling", modTexture("block/carambolasapling")),
            Map.entry("cherrysapling", modTexture("block/cherrysapling")),
            Map.entry("coconutsapling", modTexture("block/coconutsapling")),
            Map.entry("duriansapling", modTexture("block/duriansapling")),
            Map.entry("greenplumsapling", modTexture("block/greenplumsapling")),
            Map.entry("hawthornsapling", modTexture("block/hawthornsapling")),
            Map.entry("honeypeachsapling", modTexture("block/honeypeachsapling")),
            Map.entry("kiwifruitsleavesapling", modTexture("block/kiwifruitsleavesapling")),
            Map.entry("lemonsapling", modTexture("block/lemonsapling")),
            Map.entry("loquatleavesapling", modTexture("block/loquatleavesapling")),
            Map.entry("lycheesapling", modTexture("block/lycheesapling")),
            Map.entry("mangosapling", modTexture("block/mangosapling")),
            Map.entry("mangosteensapling", modTexture("block/mangosteensapling")),
            Map.entry("mulberrysapling", modTexture("block/mulberrysapling")),
            Map.entry("nectarinesapling", modTexture("block/nectarinesapling")),
            Map.entry("orangesapling", modTexture("block/orangesapling")),
            Map.entry("pawpawsapling", modTexture("block/pawpawsapling")),
            Map.entry("pearsapling", modTexture("block/pearsapling")),
            Map.entry("pistachionutsapling", modTexture("block/pistachionutsapling")),
            Map.entry("pomegranatesapling", modTexture("block/pomegranatesapling")),
            Map.entry("reddatesapling", modTexture("block/reddatesapling")),
            Map.entry("sweetmelonsapling", modTexture("block/sweetmelonsapling")),
            Map.entry("tangerinesapling", modTexture("block/tangerinesapling")),
            Map.entry("walnutsapling", modTexture("block/walnutsapling")),
            Map.entry("winterjujubesapling", modTexture("block/winterjujubesapling")),
            Map.entry("rawapple", modTexture("block/rawapple_fruit")),
            Map.entry("rawapricot", modTexture("block/rawapricot_fruit")),
            Map.entry("rawcarambola", modTexture("block/rawcarambola_fruit")),
            Map.entry("rawcherry", modTexture("block/rawcherry_fruit")),
            Map.entry("rawcoconut", modTexture("block/rawcoconut")),
            Map.entry("rawdurian", modTexture("block/rawdurian_fruit")),
            Map.entry("rawgreenplum", modTexture("block/rawgreenplum_fruit")),
            Map.entry("rawhawthorn", modTexture("block/rawhawthorn_fruit")),
            Map.entry("rawhoneypeach", modTexture("block/rawhoneypeach_fruit")),
            Map.entry("rawkiwifruit", modTexture("block/rawkiwifruit_fruit")),
            Map.entry("rawlemon", modTexture("block/rawlemon_fruit")),
            Map.entry("rawloquat", modTexture("block/rawloquat_fruit")),
            Map.entry("rawlychee", modTexture("block/rawlychee_fruit")),
            Map.entry("rawmango", modTexture("block/rawmango_fruit")),
            Map.entry("rawmangosteen", modTexture("block/rawmangosteen_fruit")),
            Map.entry("rawmulberry", modTexture("block/rawmulberry_fruit")),
            Map.entry("rawnectarine", modTexture("block/rawnectarine_fruit")),
            Map.entry("rawpawpaw", modTexture("block/rawpawpaw_fruit")),
            Map.entry("rawpear", modTexture("block/rawpear_fruit")),
            Map.entry("rawpistachionut", modTexture("block/rawpistachionut_fruit")),
            Map.entry("rawpomegranate", modTexture("block/rawpomegranate_fruit")),
            Map.entry("rawreddate", modTexture("block/rawreddate_fruit")),
            Map.entry("rawsweetmelon", modTexture("block/rawsweetmelon_fruit")),
            Map.entry("rawtangerine", modTexture("block/rawtangerine_fruit")),
            Map.entry("rawwalnut", modTexture("block/rawwalnut_fruit")),
            Map.entry("rawwinterjujube", modTexture("block/rawwinterjujube_fruit"))
    );

    private static final Map<String, String> BLOCK_MODEL_OVERRIDES = Map.ofEntries(
            Map.entry("cinnamonwood", "cinnamonwood_stage0"),
            Map.entry("grapeblock", "grapeblock_stage0"),
            Map.entry("rawbanana", "rawbanana_stage0"),
            Map.entry("orchard_heartwood_button", "orchard_heartwood_button_inventory"),
            Map.entry("orchard_heartwood_fence", "orchard_heartwood_fence_inventory"),
            Map.entry("solarwood_button", "solarwood_button_inventory"),
            Map.entry("solarwood_fence", "solarwood_fence_inventory"),
            Map.entry("stonebark_button", "stonebark_button_inventory"),
            Map.entry("stonebark_fence", "stonebark_fence_inventory"),
            Map.entry("verdant_grace_button", "verdant_grace_button_inventory"),
            Map.entry("verdant_grace_fence", "verdant_grace_fence_inventory"),
            Map.entry("vineheart_timber_button", "vineheart_timber_button_inventory"),
            Map.entry("vineheart_timber_fence", "vineheart_timber_fence_inventory")
    );

    private static final Set<String> BLOCK_ITEM_MODELS = Set.of(
            "agriculturalappraisalmachine", "antithetical_couplet_1", "antithetical_couplet_2", "appleleave_fruiting_leaves", "bananawood", "canvas_screen_1", "canvas_screen_2", "carambolaleave_fruiting_leaves", "chinese_knotting", "cinnamonleaves", "cinnamonwood", "coconutleave_fruiting_leaves", "colorful_fireworks_box", "cowpea", "deadcattle", "deadchicken", "deadpig", "deadsheep", "durianleave_fruiting_leaves", "fairy_sparkler", "goldlantern", "grapeblock", "honeypeachleave_fruiting_leaves", "incense_burner", "kiwifruitleave_fruiting_leaves", "kiwifruitsleave_fruiting_leaves", "lamp_cabinet", "leftdoorpaper", "lemonleave_fruiting_leaves", "lycheeleave_fruiting_leaves", "mangoleave_fruiting_leaves", "mangosteenleave_fruiting_leaves", "mulberryleave_fruiting_leaves", "mulberryleaves_fruiting_leaves", "nectarineleave_fruiting_leaves", "orangeleave_fruiting_leaves", "orchard_heartwood_button", "orchard_heartwood_fence", "orchard_heartwood_fence_gate", "orchard_heartwood_leaves", "orchard_heartwood_log", "orchard_heartwood_planks", "orchard_heartwood_pressure_plate", "orchard_heartwood_slab", "orchard_heartwood_stairs", "orchardchair", "pawpawleave_fruiting_leaves", "pearleave_fruiting_leaves", "pearleaves_fruiting_leaves", "pistachionutleave_fruiting_leaves", "plank_hanging_light", "rawbanana", "reddateleave_fruiting_leaves", "redlantern", "rightdoorpaper", "solarwood_button", "solarwood_fence", "solarwood_fence_gate", "solarwood_leaves", "solarwood_log", "solarwood_planks", "solarwood_pressure_plate", "solarwood_slab", "solarwood_stairs", "solarwoodchair", "stone_lion", "stonebark_button", "stonebark_fence", "stonebark_fence_gate", "stonebark_leaves", "stonebark_log", "stonebark_planks", "stonebark_pressure_plate", "stonebark_slab", "stonebark_stairs", "stonebarkchair", "sweetmelonleave_fruiting_leaves", "tangerineleave_fruiting_leaves", "verdant_grace_button", "verdant_grace_fence", "verdant_grace_fence_gate", "verdant_grace_fruiting_leaves", "verdant_grace_fruiting_leaves_apricot", "verdant_grace_fruiting_leaves_cherry", "verdant_grace_fruiting_leaves_greenplum", "verdant_grace_fruiting_leaves_hawthorn", "verdant_grace_fruiting_leaves_loquat", "verdant_grace_fruiting_leaves_pomegranate", "verdant_grace_leaves", "verdant_grace_log", "verdant_grace_planks", "verdant_grace_pressure_plate", "verdant_grace_slab", "verdant_grace_stairs", "verdantgracechair", "vineheart_timber_button", "vineheart_timber_fence", "vineheart_timber_fence_gate", "vineheart_timber_leaves", "vineheart_timber_log", "vineheart_timber_planks", "vineheart_timber_pressure_plate", "vineheart_timber_slab", "vineheart_timber_stairs", "vineheartchair", "walnutleave_fruiting_leaves", "walnutleaves_fruiting_leaves", "winterjujubeleave_fruiting_leaves", "woodbasin"
    );

    private FIDItemModelProvider() {
    }

    public static void generateAllModels(RegistrateItemModelProvider provider) {
        com.flavor_immersed_daily.all.ModItems.REGISTRY.getEntries().forEach(entry -> {
            String name = entry.getId().getPath();
            if (!BLOCK_ITEM_MODELS.contains(name)) {
                ResourceLocation texture = TEXTURE_OVERRIDES.get(name);
                if (texture == null) {
                    texture = provider.modLoc("item/" + name);
                }
                if (provider.existingFileHelper.exists(texture, PackType.CLIENT_RESOURCES, ".png", "textures")) {
                    provider.withExistingParent(name, provider.mcLoc("item/generated"))
                            .texture("layer0", texture);
                } else {
                    provider.withExistingParent(name, provider.mcLoc("item/generated"));
                }
            }
        });
        com.flavor_immersed_daily.all.ModBlocks.BLOCK_ITEMS.getEntries().forEach(entry -> {
            String name = entry.getId().getPath();
            if (!BLOCK_ITEM_MODELS.contains(name)) {
                ResourceLocation texture = TEXTURE_OVERRIDES.get(name);
                if (texture == null) {
                    texture = provider.modLoc("item/" + name);
                }
                if (provider.existingFileHelper.exists(texture, PackType.CLIENT_RESOURCES, ".png", "textures")) {
                    provider.withExistingParent(name, provider.mcLoc("item/generated"))
                            .texture("layer0", texture);
                } else {
                    provider.withExistingParent(name, provider.mcLoc("item/generated"));
                }
            }
        });
    }

    private static ResourceLocation modTexture(String path) {
        return ResourceLocation.fromNamespaceAndPath(FlavorImmersedDaily.MODID, path);
    }
}
