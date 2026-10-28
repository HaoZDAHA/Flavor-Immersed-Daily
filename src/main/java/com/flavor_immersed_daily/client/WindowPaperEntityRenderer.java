package com.flavor_immersed_daily.client;

import com.flavor_immersed_daily.entity.WindowPaperEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.joml.Matrix4f;

// 窗纸实体渲染器 — 逐像素渲染为彩色四边形（MCPaint 风格）
// 使用 POSITION_COLOR_LIGHTMAP 格式，无需纹理
// 参照原版 PaintingRenderer 的实现：
// EntityRenderDispatcher 只做了平移，没有旋转。
// 我们需要自己施加旋转使四边形朝向玩家。
public class WindowPaperEntityRenderer extends EntityRenderer<WindowPaperEntity> {

// 每个像素占 1/16 方块
    private static final float PIXEL_SIZE = 1.0f / 16.0f;

    public WindowPaperEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(WindowPaperEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        int[] pixelData = entity.getPixelData();

        poseStack.pushPose();

// 参照原版 PaintingRenderer: 施加 (180 - yaw) 旋转，使四边形朝向玩家
// entityYaw = entity.getYRot(), 由 HangingEntity.setDirection() 设置:
// SOUTH → yaw=0, WEST → yaw=90, NORTH → yaw=180, EAST → yaw=270
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));

        VertexConsumer consumer = bufferSource.getBuffer(WindowPaperRenderType.WINDOWPAPER);
        Matrix4f matrix = poseStack.last().pose();

// 渲染 16x16 个像素
// 坐标映射: GUI 中 (0,0) 在左上角, (15,15) 在右下角
// 3D 中 X 向右, Y 向上, 所以需要 Y 翻转 (ry = 15 - py)
// X 方向: 经过 (180 - yaw) 旋转后, 所有方向都是 X 翻转 (rx = 15 - px)
// 这样 GUI 中绘制的图案在墙上显示时方向一致
        for (int i = 0; i < 256; i++) {
            int color = pixelData[i];
            int alpha = (color >>> 24) & 0xFF;
            if (alpha <= 2) continue;

            int px = i % 16;
            int py = i / 16;

// Y 翻转: GUI 顶部 (py=0) → 3D 顶部 (ry=15)
            int ry = 15 - py;
// X 翻转: 使图案左右方向与 GUI 一致
            int rx = 15 - px;

            float left = rx * PIXEL_SIZE - 0.5f;
            float top = ry * PIXEL_SIZE - 0.5f;
            float right = left + PIXEL_SIZE;
            float bottom = top + PIXEL_SIZE;

            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;

            int lightU = packedLight & 0xFFFF;
            int lightV = (packedLight >> 16) & 0xFFFF;

            consumer.addVertex(matrix, left, bottom, 0).setColor(r, g, b, alpha).setUv2(lightU, lightV);
            consumer.addVertex(matrix, right, bottom, 0).setColor(r, g, b, alpha).setUv2(lightU, lightV);
            consumer.addVertex(matrix, right, top, 0).setColor(r, g, b, alpha).setUv2(lightU, lightV);
            consumer.addVertex(matrix, left, top, 0).setColor(r, g, b, alpha).setUv2(lightU, lightV);
        }

        poseStack.popPose();

// 在渲染之后调用 super.render()，参照 PaintingRenderer 的顺序
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public net.minecraft.resources.ResourceLocation getTextureLocation(WindowPaperEntity entity) {
        return null;
    }
}