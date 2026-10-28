package com.flavor_immersed_daily.client;

import com.flavor_immersed_daily.block.block.decorative.RaddlesBlock;
import com.flavor_immersed_daily.block.blockentity.RaddlesBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.block.state.BlockState;

// 灯谜牌文字渲染器 — 参照对联（CoupletRenderer）的做法：
// 1. 以方块中心为基准平移到牌面表面，向玩家侧做深度偏移避免 z-fighting
// 2. 负 Y 缩放让字形正面朝向观察者，文字自上而下排列
// 3. 使用 NORMAL 深度测试，被支撑方块自然遮挡
// 牌面主体块 X∈[4,12]、Y∈[0,14]、Z∈[7,9]（单位 1/16 格），正反两面都渲染
// 4. 先按方块 FACING 绕 Y 轴旋转，使文字跟随牌面朝向
public class RaddlesRenderer implements BlockEntityRenderer<RaddlesBlockEntity> {

// 文字缩放：1 字宽 ≈ 3.3 像素，牌面宽 8 像素
    private static final float RENDER_SCALE = 0.023F;
// 牌面 +Z 面
    private static final float BOARD_FRONT_Z = 9.0F / 16.0F;
// 牌面 -Z 面
    private static final float BOARD_BACK_Z = 7.0F / 16.0F;
// 牌面水平中心
    private static final float BOARD_CENTER_X = 0.5F;
// 牌面竖直中心（牌面高 14/16 格）
    private static final float BOARD_CENTER_Y = 7.0F / 16.0F;
// 文字与牌面的深度间隔
    private static final float DEPTH_OFFSET = 0.008F;
// 字距：4 字竖向共约 0.81 格，牌面高 0.875 格
    private static final int LINE_HEIGHT = 9;
// 文字颜色：金色，与红褐色牌面对比明显
    private static final int TEXT_COLOR = 0xFFFFFF00;

    private final Font font;

    public RaddlesRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.getFont();
    }

    @Override
    public void render(RaddlesBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        String[] chars = blockEntity.getChars();

// 按方块朝向绕 Y 轴旋转（基准：牌面沿 Z 轴、正面为 +Z），使文字跟随牌面
        BlockState state = blockEntity.getBlockState();
        Direction facing = state.hasProperty(RaddlesBlock.FACING) ? state.getValue(RaddlesBlock.FACING) : Direction.SOUTH;
        poseStack.pushPose();
        poseStack.translate(BOARD_CENTER_X, 0.0F, BOARD_CENTER_X);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.translate(-BOARD_CENTER_X, 0.0F, -BOARD_CENTER_X);

// 正面（+Z）、背面（-Z）各渲染一次，两面都能读到谜面
        for (int side = 0; side < 2; side++) {
            boolean front = side == 0;
            poseStack.pushPose();
            poseStack.translate(BOARD_CENTER_X, BOARD_CENTER_Y,
                    front ? BOARD_FRONT_Z + DEPTH_OFFSET : BOARD_BACK_Z - DEPTH_OFFSET);
// 背面沿 X 轴取负，使文字从背面观察时方向正确
            poseStack.scale(front ? RENDER_SCALE : -RENDER_SCALE, -RENDER_SCALE, RENDER_SCALE);
            drawChars(chars, poseStack, bufferSource, packedLight);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

// 自上而下逐字竖排
    private void drawChars(String[] chars, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        int startY = -(RaddlesBlockEntity.CHAR_COUNT * LINE_HEIGHT) / 2;
        for (int i = 0; i < RaddlesBlockEntity.CHAR_COUNT; i++) {
            String charStr = chars[i];
            if (charStr == null || charStr.isEmpty()) continue;
            Component component = Component.literal(charStr)
                    .setStyle(Style.EMPTY.withBold(true));
            int x = -this.font.width(component) / 2;
            int y = startY + i * LINE_HEIGHT;
            this.font.drawInBatch(component, x, y, TEXT_COLOR, false,
                    poseStack.last().pose(), bufferSource,
                    Font.DisplayMode.NORMAL, 0, packedLight);
        }
    }
}