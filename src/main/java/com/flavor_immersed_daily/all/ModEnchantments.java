package com.flavor_immersed_daily.all;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// 本模组附魔的延迟注册器（1.20.1 附魔为注册表写死，非数据驱动）
public final class ModEnchantments {

    private ModEnchantments() {
    }

    public static final DeferredRegister<Enchantment> REGISTRY =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, FlavorImmersedDaily.MODID);

// 无限砍伐：砍伐原木时按等级有概率在原位置还原该原木；1/2/3 级对应 20%/50%/80%
    public static final RegistryObject<Enchantment> INFINITE_LOGGING = REGISTRY.register("infinite_logging", () ->
            new Enchantment(Enchantment.Rarity.RARE, EnchantmentCategory.DIGGER,
                    new EquipmentSlot[]{EquipmentSlot.MAINHAND}) {
                @Override
                public int getMinLevel() {
                    return 1;
                }

                @Override
                public int getMaxLevel() {
                    return 3;
                }

                @Override
                public int getMinCost(int level) {
                    return 10 + (level - 1) * 10;
                }

                @Override
                public int getMaxCost(int level) {
                    return 20 + (level - 1) * 10;
                }
            });

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}