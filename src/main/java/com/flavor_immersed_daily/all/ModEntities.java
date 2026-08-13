package com.flavor_immersed_daily.all;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.entity.FallingFruitEntity;
import com.flavor_immersed_daily.entity.FirecrackerEntity;
import com.flavor_immersed_daily.entity.SeatEntity;
import com.flavor_immersed_daily.entity.ThrownFruitEntity;
import com.flavor_immersed_daily.entity.WindowPaperEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY =
            DeferredRegister.create(Registries.ENTITY_TYPE, FlavorImmersedDaily.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<FallingFruitEntity>> FALLING_FRUIT =
            REGISTRY.register("falling_fruit", () -> EntityType.Builder.of(FallingFruitEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(10)
                    .updateInterval(20)
                    .build("falling_fruit"));

    public static final DeferredHolder<EntityType<?>, EntityType<SeatEntity>> SEAT_ENTITY =
            REGISTRY.register("seat", () -> EntityType.Builder.<SeatEntity>of(SeatEntity::new, MobCategory.MISC)
                    .sized(0.0f, 0.0f)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("seat"));

    public static final DeferredHolder<EntityType<?>, EntityType<WindowPaperEntity>> WINDOW_PAPER_ENTITY =
            REGISTRY.register("windowpaper_1", () -> EntityType.Builder.<WindowPaperEntity>of(WindowPaperEntity::new, MobCategory.MISC)
                    .sized(1.0f, 1.0f)
                    .clientTrackingRange(64)
                    .updateInterval(20)
                    .build("windowpaper_1"));

    public static final DeferredHolder<EntityType<?>, EntityType<FirecrackerEntity>> FIRECRACKER_ENTITY =
            REGISTRY.register("firecracker", () -> EntityType.Builder.<FirecrackerEntity>of(FirecrackerEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("firecracker"));

    public static final DeferredHolder<EntityType<?>, EntityType<ThrownFruitEntity>> THROWN_FRUIT_ENTITY =
            REGISTRY.register("thrown_fruit", () -> EntityType.Builder.<ThrownFruitEntity>of(ThrownFruitEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("thrown_fruit"));

    private ModEntities() {}

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
