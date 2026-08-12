package com.flavor_immersed_daily.client;

import com.flavor_immersed_daily.block.CoupletBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 对联文字渲染器 — 在方块表面渲染文字
 * 完全参照原版告示牌（AbstractSignRenderer.renderSignText）的标准做法：
 * 1. 以方块中心为原点，Y 轴旋转使文字朝向与模型 blockstate 的 y 值一致
 * 2. 沿表面法线做深度偏移（避免 z-fighting），不依赖 SEE_THROUGH
 * 3. 使用 NORMAL 深度测试（与模型同向显示、背面自然被方块遮挡，兼容各种渲染模组）
 * 4. 缩放采用原版 (scale, -scale, scale) 正 X 形式，杜绝负缩放导致的兼容性问题
 * 横幅（antithetical_couplet_1）：1行文字从左到右横排
 * 竖联（antithetical_couplet_2）：文字从上到下竖排
 */
public class CoupletRenderer implements BlockEntityRenderer<CoupletBlockEntity> {

    /** 文字渲染缩放（原版告示牌为 0.010416667，此处放大到原模组文字大小 0.025） */
    private static final float RENDER_SCALE = 0.025F;
    /** 方块中心到模型表面（z=15.9/16=0.99375）的距离 */
    private static final float CENTER_TO_SURFACE = 0.49375F;
    /** 文字与模型表面的深度间隔（与 blockstate 模型 y 值无关，始终在表面外侧） */
    private static final float DEPTH_OFFSET = 0.008F;
    /** 竖联每行字间距（原版告示牌默认 10） */
    private static final int LINE_HEIGHT = 10;

    private final Font font;

    public CoupletRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.getFont();
    }

    @Override
    public void render(CoupletBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = blockEntity.getBlockState();
        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        String[] lines = blockEntity.getLines();
        int color = blockEntity.getColor();

        // 通过方块ID判断是横幅还是竖联
        String path = state.getBlock().builtInRegistryHolder().key().location().getPath();
        boolean isVertical = path.contains("antithetical_couplet_2");

        // 文字颜色：0=黑色(0xFF000000), 1=黄色(0xFFFFFF00)
        int textColor = color == 1 ? 0xFFFFFF00 : 0xFF000000;

        poseStack.pushPose();

        // 1. 移动到方块中心（原版 translate(0, 0.5, 0.5) 的等价形式）
        poseStack.translate(0.5, 0.5, 0.5);

        // 2. 旋转：与 blockstate 模型的 y 值完全一致（north=0, south=180, east=90, west=270），
        //    保证文字与模型始终同向，杜绝"字跑到别处"
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationFor(facing)));

        // 3. 沿表面法线偏移到模型表面外侧（深度偏移，避免 z-fighting）
        poseStack.translate(0.0F, 0.0F, CENTER_TO_SURFACE + DEPTH_OFFSET);

        // 4. 缩放：原版 (scale, -scale, scale) 形式，X 正缩放保证字形方向正确
        poseStack.scale(RENDER_SCALE, -RENDER_SCALE, RENDER_SCALE);

        if (isVertical) {
            // ---- 竖联：从上到下逐字排列 ----
            // 编辑界面把每个字存在独立的 lines[0]~lines[3] 中，渲染时从各索引读取
            int maxChars = 0;
            for (int i = 0; i < 4; i++) {
                if (lines[i] != null && !lines[i].isEmpty()) {
                    maxChars = i + 1;
                }
            }
            if (maxChars > 0) {
                // 原版告示牌逐行方式：y = -(行数)*行高/2 + i*行高
                int startY = -(maxChars * LINE_HEIGHT) / 2;
                for (int i = 0; i < maxChars; i++) {
                    String charStr = lines[i];
                    if (charStr == null || charStr.isEmpty()) continue;
                    Component component = Component.literal(charStr)
                            .setStyle(Style.EMPTY.withBold(true));
                    int x = -this.font.width(component) / 2;
                    int y = startY + i * LINE_HEIGHT;
                    this.font.drawInBatch(component, x, y, textColor, false,
                            poseStack.last().pose(), bufferSource,
                            Font.DisplayMode.NORMAL, 0, packedLight);
                }
            }
        } else {
            // ---- 横幅：只渲染第一行（横批），水平居中 ----
            String text = lines[0];
            if (text != null && !text.isEmpty()) {
                Component component = Component.literal(text)
                        .setStyle(Style.EMPTY.withBold(true));
                int textWidth = this.font.width(component);
                int x = -textWidth / 2;
                int y = -LINE_HEIGHT / 2;
                this.font.drawInBatch(component, x, y, textColor, false,
                        poseStack.last().pose(), bufferSource,
                        Font.DisplayMode.NORMAL, 0, packedLight);
            }
        }

        poseStack.popPose();
    }

    /**
     * 方块朝向对应的模型 y 旋转角（与 blockstate JSON 的 y 值保持一致）。
     * 原版告示牌通过 facing.getOpposite().get2DDataValue() * 90 计算，此处直接给出等价映射。
     */
    private static float rotationFor(Direction facing) {
        return switch (facing) {
            case SOUTH -> 180.0F;
            case EAST -> 90.0F;
            case WEST -> 270.0F;
            default -> 0.0F; // NORTH
        };
    }
}
