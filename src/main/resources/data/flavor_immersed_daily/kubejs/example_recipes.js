// Copy this file to kubejs/server_scripts/flavor_immersed_daily_recipes.js.
// It is an example only and is not loaded from this mod JAR.
ServerEvents.recipes(event => {
  event.recipes.flavor_immersed_daily.fridgeFreezing('minecraft:ice', 'minecraft:water_bucket', 80)
    .id('kubejs:fid_freeze_water')

  event.recipes.flavor_immersed_daily.fridgeTempering('minecraft:apple', 'minecraft:golden_apple', 200)
    .id('kubejs:fid_temper_apple')

  event.recipes.flavor_immersed_daily.woodBasin('flavor_immersed_daily:applejam', 'minecraft:apple')
    .id('kubejs:fid_basin_apple')

  event.recipes.flavor_immersed_daily.eggBreaking(['minecraft:slime_ball'], ['minecraft:egg'], 60)
    .id('kubejs:fid_break_egg')
})
