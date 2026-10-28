package com.flavor_immersed_daily.datagen.lang;

import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.all.ModBlocks;
import com.tterrag.registrate.providers.RegistrateLangProvider;

// Generates the English text which does not belong to a registry object.
public final class ModLangs {
    private ModLangs() {
    }

// Adds DeferredRegister object names and all non-registry translations.
    public static void addTranslations(RegistrateLangProvider provider) {
        ModItems.REGISTRY.getEntries().forEach(entry -> provider.addItem(() -> entry.get()));
        ModBlocks.REGISTRY.getEntries().forEach(entry -> provider.addBlock(() -> entry.get()));
        addCreativeTabs(provider);
        addEntities(provider);
        addEffects(provider);
        addConfiguration(provider);
        addTooltips(provider);
        addJei(provider);
        addIntegrations(provider);
        addGui(provider);
        provider.add("screen.flavor_immersed_daily.firecracker", "Firecracker Config");
        provider.add("subtitles.flavor_immersed_daily.manbaout", "Manba punch");
    }

    private static void addEntities(RegistrateLangProvider provider) {
        provider.add("entity.flavor_immersed_daily.falling_fruit", "Falling Fruit");
    }

    private static void addCreativeTabs(RegistrateLangProvider provider) {
        provider.add("itemGroup.flavor_immersed_daily.agriculture", "Flavor Immersed Daily: Agriculture");
        provider.add("itemGroup.flavor_immersed_daily.food", "Flavor Immersed Daily: Food");
        provider.add("itemGroup.flavor_immersed_daily.ingredient", "Flavor Immersed Daily: Ingredients");
        provider.add("itemGroup.flavor_immersed_daily.fi_dtools", "Flavor Immersed Daily");
        provider.add("gui.flavor_immersed_daily.creative_section.kitchen_tools", "Kitchen Tools");
        provider.add("gui.flavor_immersed_daily.creative_section.farming", "Farming");
        provider.add("gui.flavor_immersed_daily.creative_section.livestock", "Livestock Produce");
        provider.add("gui.flavor_immersed_daily.creative_section.food", "Delicacies");
        provider.add("gui.flavor_immersed_daily.creative_section.ingredient_misc", "Misc Ingredients");
        provider.add("gui.flavor_immersed_daily.creative_section.building", "Building & Decoration");
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
        provider.add("effect.flavor_immersed_daily.expose_evil", "Evil Exposure");
        provider.add("effect.flavor_immersed_daily.expose_evil.description", "Burns in open sky during the day. Smite affects you as if undead. Melee attacks have a chance to turn villagers into zombie villagers. Being attacked with blood in hand dispels the effect.");
        provider.add("effect.flavor_immersed_daily.yi_de_roar", "Roar");
        provider.add("effect.flavor_immersed_daily.yi_de_roar.description", "Your attacks ignore the target's invulnerability frames, dealing damage on every hit.");
        provider.add("effect.flavor_immersed_daily.guan_yu_strike", "Martial Saint");
        provider.add("effect.flavor_immersed_daily.guan_yu_strike.description", "Attacking with food consumes it and converts its saturation into true damage (max 8, configurable).");
        provider.add("death.attack.flavor_immersed_daily.real_damage", "%1$s was slain by true damage");
        provider.add("effect.flavor_immersed_daily.aniseed_ward", "Aniseed Ward");
        provider.add("effect.flavor_immersed_daily.aniseed_ward.description", "While active, undead creatures within 8 blocks are slowed.");
        provider.add("effect.flavor_immersed_daily.reunion_recovery", "Reunion");
        provider.add("effect.flavor_immersed_daily.reunion_recovery.description", "While active, all players within 16 blocks gain Regeneration (radius and level are configurable).");
        provider.add("effect.flavor_immersed_daily.crumbly_crust", "Crumbly Crust");
        provider.add("effect.flavor_immersed_daily.crumbly_crust.description", "When attacked, drop mooncake crumbs and lure nearby hostile mobs to that spot for 3 seconds, making them give up hunting you; the effect is then removed.");
        provider.add("effect.flavor_immersed_daily.eclipse_devour", "Eclipse Devour");
        provider.add("effect.flavor_immersed_daily.eclipse_devour.description", "Attacking an entity marks it as your combat target; whenever you recover health, deal the same amount of damage to your combat target.");
        provider.add("effect.flavor_immersed_daily.cool_skin", "Cool Skin");
        provider.add("effect.flavor_immersed_daily.cool_skin.description", "Immune to fire damage; melee attacks deal +1 damage to hostile mobs that are immune to fire. Removed when you draw near a fire source.");
    }

    private static void addConfiguration(RegistrateLangProvider provider) {
        provider.add("flavor_immersed_daily.configuration.title", "Flavor Immersed Daily Configs");
        provider.add("flavor_immersed_daily.configuration.section.flavor_immersed_daily.common.toml", "Flavor Immersed Daily Configs");
        provider.add("flavor_immersed_daily.configuration.section.flavor_immersed_daily.common.toml.title", "Flavor Immersed Daily Configs");
    }

    private static void addTooltips(RegistrateLangProvider provider) {
        provider.add("tooltip.flavor_immersed_daily.crop_type.farmland", "Farmland crop that can be planted on farmland");
        provider.add("tooltip.flavor_immersed_daily.basin_contents", "Butcher drops stored:");
        provider.add("tooltip.flavor_immersed_daily.crop_type.paddy", "Paddy crop that can be planted in deep water");
        provider.add("tooltip.flavor_immersed_daily.crop_type.mushroom", "Mushroom crop that can be planted on wood");
        provider.add("tooltip.flavor_immersed_daily.crop_type.trellis", "Trellis crop that can be planted on crop trellises");
        provider.add("tooltip.flavor_immersed_daily.seasoning", "This item is a seasoning. Hold it in your off-hand while eating or add it while stir-frying to gain a special buff.");
        provider.add("tooltip.flavor_immersed_daily.hold_shift", "Hold Shift for more information");
        provider.add("tooltip.flavor_immersed_daily.windowpaper", "Right-click a wall to place window paper. It is transparent when put away and can be cut into any shape.");
        provider.add("tooltip.flavor_immersed_daily.couplet", "Right-click a wall to place. Its text can be edited.");
        provider.add("tooltip.flavor_immersed_daily.raddles", "Right-click the underside of a block to hang it; the four characters on the board can be edited.");
        provider.add("tooltip.flavor_immersed_daily.raddles.owner", "The placer can set the riddle and its answer, and store up to 16 slots of rewards.");
        provider.add("tooltip.flavor_immersed_daily.raddles.answer", "Other players can right-click to answer; a correct answer wins one reward (once per player per board).");
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
        provider.add("tooltip.flavor_immersed_daily.hot_glutinous", "Attacking a creature consumes one, ignites it for 3 seconds and applies Evil Exposure. Attacking a player launches them high into the air with a scream.");
        provider.add("tooltip.flavor_immersed_daily.doorpaper_left", "Right-click with food: consumes 1 food and grants Roar for food saturation x10 seconds (attacks ignore invulnerability frames).");
        provider.add("tooltip.flavor_immersed_daily.doorpaper_right", "Right-click with food: consumes 1 food and grants Martial Saint for food saturation x10 seconds (food attacks deal true damage).");
        provider.add("tooltip.flavor_immersed_daily.mooncake_mold.desc1", "Right-click to open the mold: put dough and filling into the two slots.");
        provider.add("tooltip.flavor_immersed_daily.mooncake_mold.desc2", "Right-click again to press out a raw mooncake; leftover materials are returned.");
        provider.add("tooltip.flavor_immersed_daily.mooncake_mold.stored_dough", "Dough: %s x%s");
        provider.add("tooltip.flavor_immersed_daily.mooncake_mold.stored_filling", "Filling: %s x%s");
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
        provider.add("jei.flavor_immersed_daily.mooncake_mold", "Mooncake Mold");
        provider.add("jei.flavor_immersed_daily.mooncake_mold.info.usage", "Right-click a held Mooncake Mold to open its screen, then put dough and filling into the two slots. Materials are kept in the mold after closing the screen.");
        provider.add("jei.flavor_immersed_daily.mooncake_mold.info.press", "Right-click again to press: it produces min(dough, filling) raw mooncakes at once, and returns any leftover materials to your inventory.");
        provider.add("jei.flavor_immersed_daily.mooncake_mold.info.kinds", "Each dough makes a different mooncake: Raw Flour Pancake to Mooncake, Sweet Oil Dough to Cantonese Mooncake, Water Short Dough to Suzhou Mooncake, Syrup Dough to Beijing Mooncake, Glutinous Milk Dough to Snow Skin Mooncake.");
        provider.add("jei.flavor_immersed_daily.mooncake_mold.info.relation", "The Mooncake Mold is used to press out the matching raw mooncake.");
        provider.add("jei.flavor_immersed_daily.mooncake_mold.buff_line", "Eating it grants the \"%1$s\" effect: %2$s");
        provider.add("jei.flavor_immersed_daily.mooncake_mold.desc.plain", "An ordinary mooncake with no special effect when eaten.");
    }

    private static void addIntegrations(RegistrateLangProvider provider) {
        provider.add("emi.category.flavor_immersed_daily.wood_basin", "Wood Basin");
        provider.add("emi.category.flavor_immersed_daily.egg_breaking", "Egg Breaking Machine");
        provider.add("emi.category.flavor_immersed_daily.fridge_tempering", "Fridge - Tempering");
        provider.add("emi.category.flavor_immersed_daily.fridge_freezing", "Fridge - Freezing");
        provider.add("emi.category.flavor_immersed_daily.butchering", "Butchering");
        provider.add("emi.category.flavor_immersed_daily.agricultural_appraisal", "Agricultural Appraisal");
        provider.add("emi.category.flavor_immersed_daily.crop_conversion", "Crop Conversion");
        provider.add("emi.flavor_immersed_daily.time", "%s s");
        provider.add("jade.flavor_immersed_daily.contents", "Contents");
        provider.add("jade.flavor_immersed_daily.progress", "Processing: %s%%");
        provider.add("jade.flavor_immersed_daily.tempering", "Tempering: %s%%");
        provider.add("jade.flavor_immersed_daily.freezing", "Freezing: %s%%");
        provider.add("jade.flavor_immersed_daily.growth", "Growth: %s%%");
        provider.add("jade.flavor_immersed_daily.growing", "Growing");
        provider.add("jade.flavor_immersed_daily.mature", "Mature");
        provider.add("jade.flavor_immersed_daily.fruit_maturity", "Maturity: %s%%");
    }

    private static void addGui(RegistrateLangProvider provider) {
        provider.add("container.flavor_immersed_daily.mooncake_mold", "Mooncake Mold");
        provider.add("gui.flavor_immersed_daily.mooncake_mold.dough", "Dough");
        provider.add("gui.flavor_immersed_daily.mooncake_mold.filling", "Filling");
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
        provider.add("gui.flavor_immersed_daily.raddles.title", "Edit Riddle Board");
        provider.add("gui.flavor_immersed_daily.raddles.chars", "Board Text");
        provider.add("gui.flavor_immersed_daily.raddles.question", "Question");
        provider.add("gui.flavor_immersed_daily.raddles.answer", "Answer");
        provider.add("gui.flavor_immersed_daily.raddles.rewards", "Rewards");
        provider.add("gui.flavor_immersed_daily.raddles.confirm", "Save");
        provider.add("gui.flavor_immersed_daily.raddles.answer_title", "Answer the Riddle");
        provider.add("gui.flavor_immersed_daily.raddles.question_prefix", "Riddle:");
        provider.add("gui.flavor_immersed_daily.raddles.reward_prefix", "Rewards:");
        provider.add("gui.flavor_immersed_daily.raddles.answer_hint", "Type your answer here");
        provider.add("gui.flavor_immersed_daily.raddles.submit", "Submit");
        provider.add("gui.flavor_immersed_daily.raddles.hint", "Right-click to answer~ there may be a reward");
        provider.add("gui.flavor_immersed_daily.raddles.no_riddle", "This riddle board has no answer yet");
        provider.add("gui.flavor_immersed_daily.raddles.no_riddle.owner", "This riddle board has no answer yet. Right-click to set the answer and rewards");
        provider.add("gui.flavor_immersed_daily.raddles.no_riddle.chat", "This riddle board has no answer yet");
        provider.add("gui.flavor_immersed_daily.raddles.result.success", "Correct! You received a reward");
        provider.add("gui.flavor_immersed_daily.raddles.result.no_reward", "Correct, but all the rewards have been taken");
        provider.add("gui.flavor_immersed_daily.raddles.result.wrong", "Wrong answer, try again");
        provider.add("gui.flavor_immersed_daily.raddles.result.claimed", "You have already claimed this riddle's reward");
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
