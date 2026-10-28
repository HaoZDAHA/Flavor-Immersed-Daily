package com.fidtest;

import com.fidtest.item.StartBookItem;
import com.fidtest.recipe.ModRecipeTypes;
import com.fidtest.registration.ModBlockEntities;
import com.fidtest.registration.ModBlocks;
import com.fidtest.registration.ModItems;
import com.fidtest.registration.ModMenuTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(FidworkMod.MODID)
public class FidworkMod {
    public static final String MODID = "fidworkblock";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final boolean HAS_FID = ModList.get().isLoaded("flavor_immersed_daily");
    public static final boolean HAS_PATCHOULI = ModList.get().isLoaded("patchouli");

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

// 安装了 FID 时直接不注册本模组的标签页（最简单的隐藏：根本不注册），物品改由 onBuildCreativeTabContents 放进 FID 的工具标签页
    public static final RegistryObject<CreativeModeTab> COOKING_TAB;

    static {
        if (HAS_FID) {
            COOKING_TAB = null;
        } else {
            COOKING_TAB = CREATIVE_MODE_TABS.register("cooking_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.fidworkblock.cooking"))
                    .icon(() -> ModBlocks.POT.get().asItem().getDefaultInstance())
                    .displayItems((params, output) -> {
                        output.accept(ModBlocks.WOK.get());
                        output.accept(ModBlocks.STEAMBOX.get());
                        output.accept(ModBlocks.TEAPOT.get());
                        output.accept(ModBlocks.JAR.get());
                        output.accept(ModBlocks.SQUEEZINGMACHINE.get());
                        output.accept(ModBlocks.MORTAR.get());
                        output.accept(ModBlocks.CHOPPINGBOARD.get());
                        output.accept(ModBlocks.PRESSURECOOKER.get());
                        output.accept(ModBlocks.POT.get());
                        output.accept(ModBlocks.DRYER.get());
                        output.accept(ModBlocks.FRYINGPAN.get());
                        output.accept(ModBlocks.MEATMINCER.get());
                        output.accept(StartBookItem.createBookStack());
                    }).build());
        }
    }

    public FidworkMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModRecipeTypes.RECIPE_TYPES.register(modEventBus);
        ModRecipeTypes.RECIPE_SERIALIZERS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        if (HAS_FID) {
            modEventBus.addListener(this::onBuildCreativeTabContents);
        }
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
// 玩家进入存档时提醒缺少可选依赖并发放教程卷轴
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
// 数据包重载时刷新砧板屠宰定义
        MinecraftForge.EVENT_BUS.addListener(this::onAddReloadListeners);
    }

// 注册数据包重载监听器：汇总所有命名空间下的 choppingboard_mob/*.json
    private void onAddReloadListeners(net.minecraftforge.event.AddReloadListenerEvent event) {
        event.addListener(new com.fidtest.choppingboard.ChoppingboardMobRegistry.ReloadListener());
    }

    private void onBuildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (!HAS_FID) return;

        var key = BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(event.getTab());
        if (key.isEmpty()) return;

// 只处理 FID 的工具标签页
        if (!key.get().location().equals(new ResourceLocation("flavor_immersed_daily", "fi_dtools"))) return;

        var entries = event.getEntries();
        CreativeModeTab.TabVisibility vis = CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;

// 期望的显示顺序（与独立模式标签页一致）
        ItemStack[] items = {
                new ItemStack(ModBlocks.WOK.get()),
                new ItemStack(ModBlocks.STEAMBOX.get()),
                new ItemStack(ModBlocks.TEAPOT.get()),
                new ItemStack(ModBlocks.JAR.get()),
                new ItemStack(ModBlocks.SQUEEZINGMACHINE.get()),
                new ItemStack(ModBlocks.MORTAR.get()),
                new ItemStack(ModBlocks.CHOPPINGBOARD.get()),
                new ItemStack(ModBlocks.PRESSURECOOKER.get()),
                new ItemStack(ModBlocks.POT.get()),
                new ItemStack(ModBlocks.DRYER.get()),
                new ItemStack(ModBlocks.FRYINGPAN.get()),
                new ItemStack(ModBlocks.MEATMINCER.get()),
        };
// putFirst 每次插入到最前，故倒序遍历才能保持上面的显示顺序
        for (int i = items.length - 1; i >= 0; i--) {
            entries.putFirst(items[i], vis);
        }
// 教程卷轴放在标签页最前方
        entries.putFirst(StartBookItem.createBookStack(), vis);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("FidworkBlock cooking system ready!");
    }

// 首次进入存档解锁的「欢迎」进度 ID
    private static final ResourceLocation WELCOME_ADVANCEMENT =
            new ResourceLocation(MODID, "welcome");

// 玩家进入存档时提醒缺少可选依赖，并首次发放教程卷轴
    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;

        if (!HAS_PATCHOULI) {
            serverPlayer.displayClientMessage(
                    Component.translatable("message.fidworkblock.need_patchouli"), false);
        }
        unlockWelcomeAndGiveStartBook(serverPlayer);
    }

// 首次进入存档时解锁「烟火凡人心」进度并发放教程卷轴。
// 以进度完成状态作为唯一标记（进度会随玩家存档持久化），
// 因此同一存档后续再进入不会再重复发放教程卷轴。
    private void unlockWelcomeAndGiveStartBook(ServerPlayer player) {
        var advancement = player.server.getAdvancements().getAdvancement(WELCOME_ADVANCEMENT);
        if (advancement == null) return;
        if (player.getAdvancements().getOrStartProgress(advancement).isDone()) return;

        player.getAdvancements().award(advancement, "joined");

        ItemStack book = StartBookItem.createBookStack();
        if (!player.getInventory().add(book)) {
            player.drop(book, false);
        }
        player.displayClientMessage(
                Component.translatable("message.fidworkblock.startbook_given"), false);
    }
}
