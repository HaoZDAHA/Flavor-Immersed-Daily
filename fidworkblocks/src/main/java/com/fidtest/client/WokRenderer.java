package com.fidtest.client;

import com.fidtest.block.WokBlock;
import com.fidtest.block.entity.WokBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

import java.util.List;

public class WokRenderer implements BlockEntityRenderer<WokBlockEntity> {

    private static final float WOK_BOTTOM = 2.0F / 16.0F;
    private static final float ITEM_BASE_Y = WOK_BOTTOM + 0.02F;
    private static final float TOSS_MAX_HEIGHT = 0.3F;

    private static final float[][] ITEM_POSITIONS = {
            {-0.12F, -0.06F}, {0.0F, -0.06F}, {0.12F, -0.06F},
            {-0.12F, 0.06F},  {0.0F, 0.06F},  {0.12F, 0.06F}
    };

    private static final float[] ITEM_ROT_SPEED = {1.0F, 1.3F, 0.8F, 1.1F, 0.9F, 1.2F};
    private static final float[] ITEM_ROT_AXIS_X = {0.3F, 0.5F, 0.2F, 0.6F, 0.4F, 0.1F};
    private static final float[] ITEM_ROT_AXIS_Z = {0.2F, 0.1F, 0.4F, 0.3F, 0.5F, 0.6F};

    private final ItemRenderer itemRenderer;

    public WokRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(WokBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        List<ItemStack> items = be.getStoredItems();
        if (items.isEmpty()) return;

        float yRot = 0;
        if (be.getLevel() != null) {
            Direction facing = be.getBlockState().getValue(WokBlock.FACING);
            yRot = -facing.toYRot();
        }

        boolean tossing = be.getTossTicks() > 0;
        float tossProgress = 0;
        float tossOffset = 0;
        if (tossing) {
            tossProgress = (be.getTossTicks() - partialTick) / 12.0F;
            tossOffset = (float) Math.sin(tossProgress * Math.PI) * TOSS_MAX_HEIGHT;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, ITEM_BASE_Y + tossOffset, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));

        for (int i = 0; i < items.size(); i++) {
            poseStack.pushPose();
            float[] pos = ITEM_POSITIONS[i % ITEM_POSITIONS.length];
            poseStack.translate(pos[0], i * 0.015F, pos[1]);

            if (tossing) {
                float speed = ITEM_ROT_SPEED[i % ITEM_ROT_SPEED.length];
                float angle = tossProgress * 360.0F * speed;
                float ax = ITEM_ROT_AXIS_X[i % ITEM_ROT_AXIS_X.length];
                float az = ITEM_ROT_AXIS_Z[i % ITEM_ROT_AXIS_Z.length];
                poseStack.mulPose(Axis.of(new Vector3f(ax, 0.2F, az))
                        .rotationDegrees(angle));
            } else {
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
            }

            poseStack.scale(0.28F, 0.28F, 0.28F);
            itemRenderer.renderStatic(items.get(i),
                    net.minecraft.world.item.ItemDisplayContext.FIXED,
                    packedLight, packedOverlay, poseStack, bufferSource, be.getLevel(), 0);
            poseStack.popPose();
        }

        poseStack.popPose();
    }
}
