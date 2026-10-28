package com.fidtest.item;

import com.fidtest.ExampleMod;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.item.PatchouliDataComponents;

// 烟火-教程卷轴（startbook）
// 依赖 Patchouli：已安装时物品携带 patchouli:book 组件并打开对应教程书；
// 未安装时游戏照常运行，物品右键会提示缺少帕秋莉手册。
public class StartBookItem extends Item {

    public static final ResourceLocation BOOK_ID =
            ResourceLocation.fromNamespaceAndPath(ExampleMod.MODID, "startbook");

// 是否已安装帕秋莉手册
    private static final boolean HAS_PATCHOULI = ModList.get().isLoaded("patchouli");

// 帕秋莉的书绑定组件，仅在已安装时引用，避免未安装时类加载崩溃
    @Nullable
    private static final DataComponentType<ResourceLocation> BOOK_COMPONENT = HAS_PATCHOULI
            ? PatchouliDataComponents.BOOK
            : null;

    public StartBookItem() {
        super(buildProperties());
    }

    private static Item.Properties buildProperties() {
        Item.Properties props = new Item.Properties().stacksTo(1);
        if (BOOK_COMPONENT != null) {
            props.component(BOOK_COMPONENT, BOOK_ID);
        }
        return props;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!HAS_PATCHOULI) {
            if (!level.isClientSide) {
                player.displayClientMessage(
                        Component.translatable("message.fidworkblock.need_patchouli"), true);
            }
            return InteractionResultHolder.success(stack);
        }
// 与 Patchouli 自带 ItemModBook 的行为一致：仅在服务端发送打开手册的封包
        if (player instanceof ServerPlayer serverPlayer) {
            PatchouliAPI.get().openBookGUI(serverPlayer, BOOK_ID);
        }
        return InteractionResultHolder.success(stack);
    }
}
