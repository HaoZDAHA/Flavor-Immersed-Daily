package com.flavor_immersed_daily.datagen.lang;

import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.all.ModBlocks;
import com.tterrag.registrate.providers.RegistrateLangProvider;

/** Generates the English text which does not belong to a registry object. */
public final class ModLangs {
    private ModLangs() {
    }

    /** Adds DeferredRegister object names and all non-registry translations. */
    public static void addTranslations(RegistrateLangProvider provider) {
        ModItems.REGISTRY.getEntries().forEach(entry -> provider.addItem(() -> entry.get()));
        ModBlocks.REGISTRY.getEntries().forEach(entry -> provider.addBlock(() -> entry.get()));
        addCreativeTabs(provider);
        addEffects(provider);
        addConfiguration(provider);
        addTooltips(provider);
        addJei(provider);
        addGui(provider);
        provider.add("screen.flavor_immersed_daily.firecracker", "Firecracker Config");
        provider.add("subtitles.flavor_immersed_daily.manbaout", "Manba punch");
    }

    private static void addCreativeTabs(RegistrateLangProvider provider) {
        provider.add("itemGroup.flavor_immersed_daily.agriculture", "Flavor Immersed Daily: Agriculture");
        provider.add("itemGroup.flavor_immersed_daily.food", "Flavor Immersed Daily: Food");
        provider.add("itemGroup.flavor_immersed_daily.ingredient", "Flavor Immersed Daily: Ingredients");
        provider.add("itemGroup.flavor_immersed_daily.fi_dtools", "Flavor Immersed Daily: Tools");
    }

    private static void addEffects(RegistrateLangProvider provider) {
        provider.add("effect.flavor_immersed_daily.flatulence", "Flatulence");
        provider.add("effect.flavor_immersed_daily.sesame_slip", "Sesame Slip");
        provider.add("effect.flavor_immersed_daily.sesame_slip.description", "While active, the entity's step height is raised to 2.1 blocks.");
        provider.add("effect.flavor_immersed_daily.acetic_erosion", "Acetic Erosion");
        provider.add("effect.flavor_immersed_daily.acetic_erosion.description", "Melee attacks deal 1 extra durability damage to each piece of the target's armor.");
        provider.add("effect.flavor_immersed_daily.butter_pitcher", "Butter Pitcher");
        provider.add("effect.flavor_immersed_daily.butter_pitcher.description", "Projectiles have a 25% chance to freeze non-player, non-boss targets for 5 seconds.");
        provider.add("effect.flavor_immersed_daily.frozen", "Frozen");
        provider.add("effect.flavor_immersed_daily.frozen.description", "AI is disabled; the entity can only be attacked.");
        provider.add("effect.flavor_immersed_daily.bean_fury", "Bean Fury");
        provider.add("effect.flavor_immersed_daily.bean_fury.description", "Melee attacks can critically strike without jumping, with an increased 25% chance.");
        provider.add("effect.flavor_immersed_daily.flavor_base", "Flavor Base");
        provider.add("effect.flavor_immersed_daily.flavor_base.description", "For each flavor-family buff active, gain +1 attack damage and +0.1 movement speed (max 10 stacks).");
        provider.add("effect.flavor_immersed_daily.solar_brew", "Solar Brew");
        provider.add("effect.flavor_immersed_daily.solar_brew.description", "Melee attacks deal 0.5 extra fire damage to entities in open sky, and 0.5 more to undead.");
        provider.add("effect.flavor_immersed_daily.hulk_leek", "Hulk Leek");
        provider.add("effect.flavor_immersed_daily.hulk_leek.description", "Melee attacks transform baby entities into adults.");
        provider.add("effect.flavor_immersed_daily.fury_assault", "Fury Assault");
        provider.add("effect.flavor_immersed_daily.fury_assault.description", "Melee attacks cost 1 health and ignite a 10-block cone ahead, dealing 2 fire damage.");
        provider.add("effect.flavor_immersed_daily.crimson_mamba", "Crimson Mamba Elbow");
        provider.add("effect.flavor_immersed_daily.crimson_mamba.description", "Melee attacks knock back targets much harder, pushing nearby creatures in the same direction, and play a special sound on every hit.");
    }

    private static void addConfiguration(RegistrateLangProvider provider) {
        provider.add("flavor_immersed_daily.configuration.title", "Flavor Immersed Daily Configs");
        provider.add("flavor_immersed_daily.configuration.section.flavor_immersed_daily.common.toml", "Flavor Immersed Daily Configs");
        provider.add("flavor_immersed_daily.configuration.section.flavor_immersed_daily.common.toml.title", "Flavor Immersed Daily Configs");
    }

    private static void addTooltips(RegistrateLangProvider provider) {
        provider.add("tooltip.flavor_immersed_daily.seed", "A seed that can be planted.");
        provider.add("tooltip.flavor_immersed_daily.crop_type.farmland", "Farmland crop that can be planted on farmland");
        provider.add("tooltip.flavor_immersed_daily.crop_type.paddy", "Paddy crop that can be planted in deep water");
        provider.add("tooltip.flavor_immersed_daily.crop_type.mushroom", "Mushroom crop that can be planted on wood");
        provider.add("tooltip.flavor_immersed_daily.seasoning", "This item is a seasoning. Hold it in your off-hand while eating or add it while stir-frying to gain a special buff.");
        provider.add("tooltip.flavor_immersed_daily.hold_shift", "Hold Shift for more information");
        provider.add("tooltip.flavor_immersed_daily.windowpaper", "Right-click a wall to place window paper. It is transparent when put away and can be cut into any shape.");
        provider.add("tooltip.flavor_immersed_daily.couplet", "Right-click a wall to place. Its text can be edited.");
        provider.add("tooltip.flavor_immersed_daily.rare_fruit_variant", "Rare fruit variant, obtained with a chance when harvesting.");
        provider.add("tooltip.flavor_immersed_daily.purified_water_bucket", "Right-click a water source to get purified water. Uses 1 durability.");
        provider.add("tooltip.flavor_immersed_daily.kitchen_scissors", "Breaking grass and leaves with scissors can drop wild crops.");
        provider.add("tooltip.flavor_immersed_daily.wild_fruit_harvest", "Obtained by breaking leaves with scissors. Use on an agricultural appraisal machine to identify it.");
        provider.add("tooltip.flavor_immersed_daily.wild_crop_harvest", "Obtained by breaking grass with scissors. Use on an agricultural appraisal machine to identify it.");
        provider.add("tooltip.flavor_immersed_daily.reedleaf_harvest", "Can drop when breaking grass with scissors.");
        provider.add("tooltip.flavor_immersed_daily.bighook", "Animal carcasses can be hung here for butchering and processing.");
        provider.add("tooltip.flavor_immersed_daily.dead_animal_harvest", "Obtained by slaughtering an animal with a bone cutter. Hang it on a big hook for processing.");
        provider.add("tooltip.flavor_immersed_daily.chicken_without_blood", "Add purified water to a wood basin, then put it in to remove feathers.");
        provider.add("tooltip.flavor_immersed_daily.chicken_without_feather", "Continue processing by hanging it on a big hook.");
        provider.add("tooltip.flavor_immersed_daily.agriculturalappraisalmachine", "Hold wild crops and right-click directly.");
        provider.add("tooltip.flavor_immersed_daily.fireworks.desc_line1", "Customizable fireworks launcher");
        provider.add("tooltip.flavor_immersed_daily.fireworks.desc_line2", "Sneak-right-click to configure; right-click or redstone to launch");
        provider.add("tooltip.flavor_immersed_daily.fireworks.desc_line3", "Parameters are preserved when broken");
        provider.add("tooltip.flavor_immersed_daily.fireworks.shape", "Shape: %s");
        provider.add("tooltip.flavor_immersed_daily.fireworks.color", "Color: %s");
        provider.add("tooltip.flavor_immersed_daily.fireworks.fade_color", "Fade Color: %s");
        provider.add("tooltip.flavor_immersed_daily.fireworks.trail", "Trail: %s");
        provider.add("tooltip.flavor_immersed_daily.fireworks.angle", "Angle: %s degrees");
        provider.add("tooltip.flavor_immersed_daily.fireworks.speed", "Speed: %s");
        provider.add("tooltip.flavor_immersed_daily.fireworks.distance", "Distance: %s");
        provider.add("tooltip.flavor_immersed_daily.fireworks.curve_a", "Curve A: %s");
        provider.add("tooltip.flavor_immersed_daily.fireworks.curve_b", "Curve B: %s");
        provider.add("tooltip.flavor_immersed_daily.firecracker.shape", "Shape: %s");
        provider.add("tooltip.flavor_immersed_daily.firecracker.color", "Color: %s");
        provider.add("tooltip.flavor_immersed_daily.firecracker.fade_color", "Fade Color: %s");
        provider.add("tooltip.flavor_immersed_daily.firecracker.desc1", "Customizable firecracker");
        provider.add("tooltip.flavor_immersed_daily.firecracker.desc2", "Right-click to throw; sneak-right-click to configure");
        provider.add("tooltip.flavor_immersed_daily.seedable_fruit.desc1", "Sneak-right-click to get grape products");
        provider.add("tooltip.flavor_immersed_daily.fairy_sparkler.desc1", "Glowing fairy sparkler");
        provider.add("tooltip.flavor_immersed_daily.fairy_sparkler.desc2", "Hold right-click in both hands to emit particles");
        provider.add("tooltip.flavor_immersed_daily.fairy_sparkler.desc3", "Sneak-right-click to configure colors and shapes");
        provider.add("tooltip.flavor_immersed_daily.throwable_fruit.desc1", "Sneak-right-click to throw. Drops materials on impact.");
        provider.add("tooltip.flavor_immersed_daily.coconut_meat.desc", "Obtained by throwing a coconut");
        provider.add("tooltip.flavor_immersed_daily.coconut_shell.desc", "Obtained by throwing a coconut");
        provider.add("tooltip.flavor_immersed_daily.durian_meat.desc", "Obtained by throwing a durian");
        provider.add("tooltip.flavor_immersed_daily.durian_shell.desc", "Obtained by throwing a durian");
        provider.add("tooltip.flavor_immersed_daily.sapling_harvest", "When a fruit tree matures, right-click its trunk with a stick to shake down fruit.");
    }

    private static void addJei(RegistrateLangProvider provider) {
        provider.add("jei.flavor_immersed_daily.butchering", "Butchering");
        provider.add("jei.flavor_immersed_daily.agricultural_appraisal", "Agricultural Appraisal");
        provider.add("jei.flavor_immersed_daily.woodbasin_info", "Wood Basin");
        provider.add("jei.flavor_immersed_daily.woodbasin_info.wash_desc", "Right-click with Tidy Water to fill, then right-click with a Bled Chicken. Wait, then retrieve the Plucked Chicken and extra drops.");
        provider.add("jei.flavor_immersed_daily.woodbasin_info.stomp_desc", "Right-click with fruit to place it in an empty basin, then step on it to make jam.");
        provider.add("jei.flavor_immersed_daily.fridge_tempering", "Fridge - Tempering");
        provider.add("jei.flavor_immersed_daily.fridge_freezing", "Fridge - Freezing");
        provider.add("jei.flavor_immersed_daily.egg_breaking", "Egg Breaking Machine");
        provider.add("jei.flavor_immersed_daily.shaped", "Shaped");
        provider.add("jei.flavor_immersed_daily.shapeless", "Shapeless");
    }

    private static void addGui(RegistrateLangProvider provider) {
        provider.add("gui.flavor_immersed_daily.fairy_sparkler.color", "Color");
        provider.add("gui.flavor_immersed_daily.fairy_sparkler.shape", "Shape");
        provider.add("gui.flavor_immersed_daily.fairy_sparkler.shape.circle", "Circle");
        provider.add("gui.flavor_immersed_daily.fairy_sparkler.shape.spiral", "Spiral");
        provider.add("gui.flavor_immersed_daily.fairy_sparkler.shape.heart", "Heart");
        provider.add("gui.flavor_immersed_daily.fairy_sparkler.ring1", "Inner Ring");
        provider.add("gui.flavor_immersed_daily.fairy_sparkler.ring2", "Outer Ring");
        provider.add("gui.flavor_immersed_daily.windowpaper.cutter", "Cutter");
        provider.add("gui.flavor_immersed_daily.couplet.title", "Edit Couplet");
        provider.add("gui.flavor_immersed_daily.couplet.color.0", "Black Text");
        provider.add("gui.flavor_immersed_daily.couplet.color.1", "Yellow Text");
        provider.add("gui.flavor_immersed_daily.fireworks_box.color", "Firework Color");
        provider.add("gui.flavor_immersed_daily.fireworks_box.fade_color", "2nd Color");
        provider.add("gui.flavor_immersed_daily.fireworks_box.shape", "Firework Shape");
        provider.add("gui.flavor_immersed_daily.fireworks_box.shape.small_ball", "Small Ball");
        provider.add("gui.flavor_immersed_daily.fireworks_box.shape.large_ball", "Large Ball");
        provider.add("gui.flavor_immersed_daily.fireworks_box.shape.star", "Star");
        provider.add("gui.flavor_immersed_daily.fireworks_box.shape.creeper", "Creeper");
        provider.add("gui.flavor_immersed_daily.fireworks_box.shape.burst", "Burst");
        provider.add("gui.flavor_immersed_daily.fireworks_box.trail", "Trail");
        provider.add("gui.flavor_immersed_daily.fireworks_box.on", "On");
        provider.add("gui.flavor_immersed_daily.fireworks_box.off", "Off");
        provider.add("gui.flavor_immersed_daily.fireworks_box.angle", "Tilt Angle");
        provider.add("gui.flavor_immersed_daily.fireworks_box.speed", "Flight Speed");
        provider.add("gui.flavor_immersed_daily.fireworks_box.distance", "Flight Distance");
        provider.add("gui.flavor_immersed_daily.fireworks_box.curveA", "Curve A");
        provider.add("gui.flavor_immersed_daily.fireworks_box.curveB", "Curve B");
        provider.add("gui.flavor_immersed_daily.fireworks_box.confirm", "Confirm");
        provider.add("gui.flavor_immersed_daily.firecracker.shape", "Shape: %s");
        provider.add("gui.flavor_immersed_daily.firecracker.shape.0", "Small Ball");
        provider.add("gui.flavor_immersed_daily.firecracker.shape.1", "Large Ball");
        provider.add("gui.flavor_immersed_daily.firecracker.shape.2", "Star");
        provider.add("gui.flavor_immersed_daily.firecracker.shape.3", "Creeper");
        provider.add("gui.flavor_immersed_daily.firecracker.shape.4", "Burst");
        provider.add("gui.flavor_immersed_daily.firecracker.color", "Color: %s");
        provider.add("gui.flavor_immersed_daily.firecracker.fade_color", "Fade Color: %s");
        provider.add("gui.flavor_immersed_daily.firecracker.done", "Done");
    }
}
