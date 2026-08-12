package com.flavor_immersed_daily.client;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.block.BighookBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 大挂钩渲染器 — 在挂钩前方浮动渲染当前需要的刀具
 */
public class BighookRenderer implements BlockEntityRenderer<BlockEntity> {

    public BighookRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BlockEntity entity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = entity.getBlockState();
        if (state.getBlock() != FlavorImmersedDaily.BIGHOOK.get()) return;

        int stage = state.getValue(BighookBlock.STAGE);
        int animal = state.getValue(BighookBlock.ANIMAL);
        if (stage == 0) return;

        ItemStack toolStack = getToolForStage(stage, animal);
        if (toolStack.isEmpty()) return;

        Direction facing = state.getValue(BighookBlock.FACING);

        // 在挂钩前方（方块朝向的那面）渲染
        double offsetX = facing.getNormal().getX() * 0.65;
        double offsetZ = facing.getNormal().getZ() * 0.65;

        // 上下微微浮动动画（让刀具看起来更生动）
        double bob = Math.sin((entity.getLevel().getGameTime() + partialTick) * 0.1) * 0.06;

        poseStack.pushPose();
        poseStack.translate(0.5 + offsetX, 0.5 + bob, 0.5 + offsetZ);

        // 物品始终面向方块朝向的玩家
        poseStack.mulPose(Axis.YP.rotationDegrees(180 - facing.toYRot()));
        // 刀具位于玩家上方，略微向玩家方向俯视倾斜
        poseStack.mulPose(Axis.XP.rotationDegrees(-30));

        float scale = 0.5f;
        poseStack.scale(scale, scale, scale);

        Minecraft.getInstance().getItemRenderer().renderStatic(
                toolStack, ItemDisplayContext.FIXED, packedLight, packedOverlay,
                poseStack, bufferSource, entity.getLevel(), 0);

        poseStack.popPose();
    }

    private static ItemStack getToolForStage(int stage, int animal) {
        boolean isChicken = animal == 4;
        return switch (stage) {
            case 1 -> new ItemStack(FlavorImmersedDaily.WIDEEDGEDKNIFE.get());
            case 2 -> isChicken ? ItemStack.EMPTY : new ItemStack(FlavorImmersedDaily.SHARPKNIFE.get());
            case 3 -> new ItemStack(FlavorImmersedDaily.BONECUTTERKNIFE.get());
            case 4 -> new ItemStack(FlavorImmersedDaily.SHARPKNIFE.get());
            case 5 -> new ItemStack(FlavorImmersedDaily.SHARPKNIFE.get());
            case 6 -> new ItemStack(FlavorImmersedDaily.SHARPKNIFE.get());
            default -> ItemStack.EMPTY;
        };
    }
}