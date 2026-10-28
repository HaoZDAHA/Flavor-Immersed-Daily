package com.flavor_immersed_daily.integration.jei;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

// 月饼模具 JEI 配方 — 「月饼模具 + 饼皮 → 生月饼 → 成品月饼」的压制链路，
// 并附带成品月饼的效果说明。
public class MooncakeMoldRecipe {

    private final ItemStack dough;
    private final ItemStack rawMooncake;
    private final ItemStack finishedMooncake;
    private final List<Component> description;

    public MooncakeMoldRecipe(ItemStack dough, ItemStack rawMooncake, ItemStack finishedMooncake,
                              List<Component> description) {
        this.dough = dough;
        this.rawMooncake = rawMooncake;
        this.finishedMooncake = finishedMooncake;
        this.description = description;
    }

    public ItemStack getDough() {
        return dough;
    }

    public ItemStack getRawMooncake() {
        return rawMooncake;
    }

    public ItemStack getFinishedMooncake() {
        return finishedMooncake;
    }

    public List<Component> getDescription() {
        return description;
    }
}
