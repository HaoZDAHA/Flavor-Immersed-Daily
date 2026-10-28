package com.fidtest.item;

import com.fidtest.FidworkMod;
import com.fidtest.registration.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import vazkii.patchouli.api.PatchouliAPI;

// 烟火-教程卷轴（startbook）
// 依赖 Patchouli：已安装时物品携带 patchouli:book 数据并打开对应教程书；
// 未安装时游戏照常运行，物品右键会提示缺少帕秋莉手册。
public class StartBookItem extends Item {

// 教程书 ID
    public static final ResourceLocation BOOK_ID = new ResourceLocation(FidworkMod.MODID, "startbook");

// 是否已安装帕秋莉手册
    private static final boolean HAS_PATCHOULI = ModList.get().isLoaded("patchouli");

    public StartBookItem() {
        super(new Item.Properties().stacksTo(1));
    }

// 构造一本带教程书数据的卷轴（供创造模式标签页与首次进服发放使用）
    public static ItemStack createBookStack() {
        ItemStack stack = new ItemStack(ModItems.STARTBOOK.get());
        stack.getOrCreateTag().putString("patchouli:book", BOOK_ID.toString());
        return stack;
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
// 与帕秋莉自带的书籍物品一致：仅在服务端发送打开手册的封包
        if (player instanceof ServerPlayer serverPlayer) {
            openBook(serverPlayer);
        }
        return InteractionResultHolder.success(stack);
    }

// 单独抽出以隔离帕秋莉 API 引用，未安装帕秋莉时不会加载该类
    private static void openBook(ServerPlayer player) {
        PatchouliAPI.get().openBookGUI(player, BOOK_ID);
    }
}
