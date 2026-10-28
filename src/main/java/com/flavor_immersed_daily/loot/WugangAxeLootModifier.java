package com.flavor_immersed_daily.loot;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModItems;
import com.flavor_immersed_daily.item.WugangAxeItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

// 吴刚之斧的地牢宝箱获取途径 — 在命中的宝箱战利品表中按概率追加一柄天生附魔的吴刚之斧。
// 命中的战利品表与概率由 wugang_axe_dungeon.json 中的条件声明（10 个原版宝箱表 × 10%）。
public class WugangAxeLootModifier extends LootModifier {

    public static final Codec<WugangAxeLootModifier> CODEC =
            RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, WugangAxeLootModifier::new));

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> REGISTRY =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, FlavorImmersedDaily.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> SERIALIZER =
            REGISTRY.register("wugang_axe_dungeon", () -> CODEC);

    public WugangAxeLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
// 条件已保证仅在目标宝箱表触发，这里追加一柄天生带无限砍伐 III 的斧头
        generatedLoot.add(WugangAxeItem.makeEnchanted(ModItems.WUGANG_AXE.get()));
        return generatedLoot;
    }

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}