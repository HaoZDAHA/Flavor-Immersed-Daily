package com.flavor_immersed_daily.all;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.block.blockentity.BighookBlockEntity;
import com.flavor_immersed_daily.block.blockentity.ColorfulFireworksBoxBlockEntity;
import com.flavor_immersed_daily.block.blockentity.CoupletBlockEntity;
import com.flavor_immersed_daily.block.blockentity.EggBreakingMachineBlockEntity;
import com.flavor_immersed_daily.block.blockentity.FridgeBlockEntity;
import com.flavor_immersed_daily.block.blockentity.WoodBasinBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FlavorImmersedDaily.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WoodBasinBlockEntity>> WOODBASIN_ENTITY =
            REGISTRY.register("woodbasin_entity",
                    () -> BlockEntityType.Builder.of(WoodBasinBlockEntity::new, ModBlocks.WOODBASIN.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BighookBlockEntity>> BIGHOOK_BE =
            REGISTRY.register("bighook_be",
                    () -> BlockEntityType.Builder.of(BighookBlockEntity::new, ModBlocks.BIGHOOK.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FridgeBlockEntity>> FRIDGE_ENTITY =
            REGISTRY.register("fridge_entity",
                    () -> BlockEntityType.Builder.of(FridgeBlockEntity::new, ModBlocks.FRIDGE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EggBreakingMachineBlockEntity>> EGG_BREAKING_MACHINE_ENTITY =
            REGISTRY.register("egg_breaking_machine_entity",
                    () -> BlockEntityType.Builder.of(EggBreakingMachineBlockEntity::new, ModBlocks.EGGBREAKINGMACHINE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ColorfulFireworksBoxBlockEntity>> COLORFUL_FIREWORKS_BOX_ENTITY =
            REGISTRY.register("colorful_fireworks_box_entity",
                    () -> BlockEntityType.Builder.of(ColorfulFireworksBoxBlockEntity::new, ModBlocks.COLORFUL_FIREWORKS_BOX.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CoupletBlockEntity>> COUPLET_ENTITY =
            REGISTRY.register("couplet_entity", () -> BlockEntityType.Builder.of(CoupletBlockEntity::new,
                    ModBlocks.ANTITHETICAL_COUPLET_1.get(), ModBlocks.ANTITHETICAL_COUPLET_2.get()).build(null));

    private ModBlockEntities() {
    }

    public static void register(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }
}
