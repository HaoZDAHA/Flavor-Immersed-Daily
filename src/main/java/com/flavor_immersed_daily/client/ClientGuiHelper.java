package com.flavor_immersed_daily.client;

import com.flavor_immersed_daily.block.blockentity.CoupletBlockEntity;
import com.flavor_immersed_daily.block.blockentity.RaddlesBlockEntity;
import com.flavor_immersed_daily.entity.WindowPaperEntity;
import com.flavor_immersed_daily.screen.ColorfulFireworksBoxConfigScreen;
import com.flavor_immersed_daily.screen.CoupletEditScreen;
import com.flavor_immersed_daily.screen.FairySparklerConfigScreen;
import com.flavor_immersed_daily.screen.FirecrackerConfigScreen;
import com.flavor_immersed_daily.screen.RaddlesAnswerScreen;
import com.flavor_immersed_daily.screen.WindowPaperScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

// 客户端 GUI 辅助类 — 集中打开所有配置界面。
// 注意：本类不标记 @OnlyIn(Dist.CLIENT)，公共类只在 isClientSide 分支内调用，
// 服务端不会执行到这些方法，从而避免加载客户端类。
public class ClientGuiHelper {

    public static void openFirecrackerConfig(ItemStack stack) {
        Minecraft.getInstance().setScreen(new FirecrackerConfigScreen(stack));
    }

    public static void openFairySparklerConfig() {
        Minecraft.getInstance().setScreen(new FairySparklerConfigScreen());
    }

    public static void openColorfulFireworksBoxConfig(BlockPos pos) {
        Minecraft.getInstance().setScreen(new ColorfulFireworksBoxConfigScreen(pos));
    }

    public static void openCoupletEdit(CoupletBlockEntity blockEntity) {
        Minecraft.getInstance().setScreen(new CoupletEditScreen(blockEntity));
    }

// 非放置者右键灯谜牌：打开答题界面
    public static void openRaddlesAnswer(RaddlesBlockEntity blockEntity) {
        Minecraft.getInstance().setScreen(new RaddlesAnswerScreen(blockEntity));
    }

// 答题结果由服务端校验后回传，若答题界面仍开着则更新其提示
    public static void handleRaddlesResult(int result) {
        if (Minecraft.getInstance().screen instanceof RaddlesAnswerScreen screen) {
            screen.onResult(result);
        }
    }

    public static void openWindowPaper(WindowPaperEntity entity) {
        Minecraft.getInstance().setScreen(new WindowPaperScreen(entity));
    }
}
