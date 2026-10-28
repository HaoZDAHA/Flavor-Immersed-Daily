package com.fidtest;

import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlockEntities;
import com.fidtest.registration.ModBlocks;
import com.fidtest.registration.ModItems;
import com.fidtest.registration.ModMenuTypes;
import com.fidtest.network.DishNamePayload;
import com.fidtest.network.DishOpenPayload;
import com.fidtest.item.CowpeaSpinData;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import net.neoforged.fml.ModList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod(ExampleMod.MODID)
public class ExampleMod {
    public static final String MODID = "fidworkblock";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    public static final Supplier<AttachmentType<CowpeaSpinData>> COWPEA_SPIN =
            ATTACHMENT_TYPES.register("cowpea_spin",
                    () -> AttachmentType.builder(() -> CowpeaSpinData.EMPTY)
                            .serialize(CowpeaSpinData.CODEC)
                            .build());

    static {
        for (var blockEntry : ModBlocks.BLOCKS.getEntries()) {
            if (blockEntry.getId().getPath().equals("steambox")) {
                ITEMS.register("steambox",
                    () -> new com.fidtest.item.SteamboxItem(blockEntry.get(), new Item.Properties()));
            } else {
                ITEMS.register(blockEntry.getId().getPath(),
                    () -> new BlockItem(blockEntry.get(), new Item.Properties()));
            }
        }
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> COOKING_TAB =
            CREATIVE_MODE_TABS.register("cooking_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.fidworkblock.cooking"))
                    .icon(() -> ModBlocks.POT.asItem().getDefaultInstance())
                    .displayItems((params, output) -> {
                        if (ModList.get().isLoaded("flavor_immersed_daily")) return;
                        output.accept(ModItems.STARTBOOK.get());
                        output.accept(ModBlocks.STEAMBOX.get());
                        output.accept(ModBlocks.WOK.get());
                        output.accept(ModBlocks.PRESSURECOOKER.get());
                        output.accept(ModBlocks.CHOPPINGBOARD.get());
                        output.accept(ModBlocks.FRYINGPAN.get());
                        output.accept(ModBlocks.DRYER.get());
                        output.accept(ModBlocks.SQUEEZINGMACHINE.get());
                        output.accept(ModBlocks.JAR.get());
                        output.accept(ModBlocks.TEAPOT.get());
                        output.accept(ModBlocks.POT.get());
                        output.accept(ModBlocks.MORTAR.get());
                        output.accept(ModBlocks.MEATMINCER.get());
                    }).build());

    public ExampleMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerPayloads);

        ModBlocks.BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModRecipeTypes.RECIPE_TYPES.register(modEventBus);
        ModRecipeTypes.RECIPE_SERIALIZERS.register(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

// 玩家进入存档时提醒缺少可选依赖（如帕秋莉手册）
        NeoForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);

// 砧板「小型生物屠宰」定义：读取所有命名空间下的 data/<ns>/choppingboard_mob/*.json
        NeoForge.EVENT_BUS.addListener(AddReloadListenerEvent.class,
                event -> event.addListener(new com.fidtest.choppingboard.ChoppingboardMobRegistry.ReloadListener()));
    }

    private static final ResourceLocation WELCOME_ADVANCEMENT =
            ResourceLocation.fromNamespaceAndPath(MODID, "welcome");

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
// 缺少可选依赖（帕秋莉手册）提醒
            if (!ModList.get().isLoaded("patchouli")) {
                serverPlayer.displayClientMessage(
                        Component.translatable("message.fidworkblock.need_patchouli"), false);
            }
            unlockWelcomeAndGiveStartBook(serverPlayer);
        }
    }

// 首次进入存档时解锁「烟火凡人心」进度并发放教程卷轴。
// 以进度完成状态作为唯一标记（原版会随玩家存档持久化），
// 因此同一存档后续再进入不会再发放教程卷轴。
    private void unlockWelcomeAndGiveStartBook(net.minecraft.server.level.ServerPlayer player) {
        var advancement = player.server.getAdvancements().get(WELCOME_ADVANCEMENT);
        if (advancement == null) return;
        if (player.getAdvancements().getOrStartProgress(advancement).isDone()) return;

        player.getAdvancements().award(advancement, "joined");

        ItemStack book = new ItemStack(ModItems.STARTBOOK.get());
        if (!player.getInventory().add(book)) {
            player.drop(book, false);
        }
        player.displayClientMessage(
                Component.translatable("message.fidworkblock.startbook_given"), false);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                DishNamePayload.TYPE,
                DishNamePayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        var player = context.player();
                        if (payload.slot() >= 0 && payload.slot() < player.getInventory().getContainerSize()) {
                            ItemStack stack = player.getInventory().getItem(payload.slot());
                            if (stack.getItem() instanceof com.fidtest.item.DishItem) {
                                com.fidtest.item.DishItem.setDishName(stack, payload.name(), payload.nameColor());
                                com.fidtest.item.DishItem.setDishDescription(stack, payload.description());
                                com.fidtest.item.DishItem.setChefName(stack, player.getName().getString());
                            }
                        }
                    });
                }
        );
        registrar.playToServer(
                DishOpenPayload.TYPE,
                DishOpenPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        context.player().stopUsingItem();
                    });
                }
        );
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("FidworkBlock cooking system ready!");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (!ModList.get().isLoaded("flavor_immersed_daily")) return;

// 对方现在全模组只剩一个创造标签页 fi_dtools，物品按「横幅分区」排列
// （见其 SectionedCreativeModeTab），本模组物品统一排到「厨房工具」横幅下的开头
        ResourceKey<CreativeModeTab> fiDtoolsKey = ResourceKey.create(
                Registries.CREATIVE_MODE_TAB,
                ResourceLocation.fromNamespaceAndPath("flavor_immersed_daily", "fi_dtools"));
        if (!event.getTabKey().equals(fiDtoolsKey)) return;

// 目标顺序：教程卷轴 → 各类加工机器 → 其余物品
        List<ItemStack> ordered = new ArrayList<>();
        ordered.add(new ItemStack(ModItems.STARTBOOK.get()));
        ordered.add(new ItemStack(ModBlocks.STEAMBOX.get()));
        ordered.add(new ItemStack(ModBlocks.WOK.get()));
        ordered.add(new ItemStack(ModBlocks.PRESSURECOOKER.get()));
        ordered.add(new ItemStack(ModBlocks.CHOPPINGBOARD.get()));
        ordered.add(new ItemStack(ModBlocks.FRYINGPAN.get()));
        ordered.add(new ItemStack(ModBlocks.DRYER.get()));
        ordered.add(new ItemStack(ModBlocks.SQUEEZINGMACHINE.get()));
        ordered.add(new ItemStack(ModBlocks.JAR.get()));
        ordered.add(new ItemStack(ModBlocks.TEAPOT.get()));
        ordered.add(new ItemStack(ModBlocks.POT.get()));
        ordered.add(new ItemStack(ModBlocks.MORTAR.get()));
        ordered.add(new ItemStack(ModBlocks.MEATMINCER.get()));
        for (var itemEntry : ModItems.ITEMS.getEntries()) {
// 菜肴不进创造标签页
            if (itemEntry.get() instanceof com.fidtest.item.DishItem) continue;
// 卷轴已排在最前
            if (itemEntry.get() instanceof com.fidtest.item.StartBookItem) continue;
            ordered.add(new ItemStack(itemEntry.get()));
        }

// insertFirst 是头插，倒序插入才能得到上面的顺序
        for (int i = ordered.size() - 1; i >= 0; i--) {
            event.insertFirst(ordered.get(i), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("FidworkBlock server starting");
    }
}
