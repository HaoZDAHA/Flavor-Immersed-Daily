package com.flavor_immersed_daily.client;

import com.flavor_immersed_daily.all.ModBlocks;

import com.flavor_immersed_daily.all.ModItems;

import com.flavor_immersed_daily.block.block.processing.BighookBlock;
import com.flavor_immersed_daily.block.blockentity.BighookBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// 大挂钩渲染器 — 在挂钩前方浮动渲染当前需要的刀具
public class BighookRenderer implements BlockEntityRenderer<BlockEntity> {

    public BighookRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BlockEntity entity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = entity.getBlockState();
        if (state.getBlock() != ModBlocks.BIGHOOK.get()) return;

// 数据包自定义屠宰：显示挂钩上的物品与当前阶段所需刀具
        if (entity instanceof BighookBlockEntity be && be.hasCustomButchering()) {
            renderCustom(entity, be, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
            return;
        }

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

// 渲染数据包自定义屠宰：挂钩中央显示当前胴体物品，前方浮动所需刀具。
    private void renderCustom(BlockEntity entity, BighookBlockEntity be, float partialTick, PoseStack poseStack,
                              MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Direction facing = entity.getBlockState().getValue(BighookBlock.FACING);
        double offsetX = facing.getNormal().getX() * 0.65;
        double offsetZ = facing.getNormal().getZ() * 0.65;
        double bob = Math.sin((entity.getLevel().getGameTime() + partialTick) * 0.1) * 0.06;

// 当前阶段的胴体：优先用自定义方块模型，缺失时退回物品渲染
        ResourceLocation customModel = be.getCustomModel();
        boolean rendered = customModel != null
                && renderCustomModel(entity, poseStack, bufferSource, facing, customModel, packedLight, packedOverlay);
        if (!rendered) {
            ItemStack display = be.getCustomDisplay();
            if (!display.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0.5, 0.55, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(180 - facing.toYRot()));
                poseStack.scale(0.7f, 0.7f, 0.7f);
                Minecraft.getInstance().getItemRenderer().renderStatic(
                        display, ItemDisplayContext.FIXED, packedLight, packedOverlay,
                        poseStack, bufferSource, entity.getLevel(), 0);
                poseStack.popPose();
            }
        }

// 当前阶段所需刀具（挂钩前方浮动）
        ItemStack tool = be.getCustomTool();
        if (!tool.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5 + offsetX, 0.5 + bob, 0.5 + offsetZ);
            poseStack.mulPose(Axis.YP.rotationDegrees(180 - facing.toYRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(-30));
            poseStack.scale(0.5f, 0.5f, 0.5f);
            Minecraft.getInstance().getItemRenderer().renderStatic(
                    tool, ItemDisplayContext.FIXED, packedLight, packedOverlay,
                    poseStack, bufferSource, entity.getLevel(), 0);
            poseStack.popPose();
        }
    }

// 尝试把 modelId 指向的方块模型画在挂钩方块上；成功返回 true（调用方据此跳过 display 物品）。
    private static boolean renderCustomModel(BlockEntity entity, PoseStack poseStack, MultiBufferSource bufferSource,
                                             Direction facing, ResourceLocation modelId,
                                             int packedLight, int packedOverlay) {
        Minecraft mc = Minecraft.getInstance();
        BakedModel model = mc.getModelManager().getModel(ModelResourceLocation.standalone(modelId));
        if (model == null || model == mc.getModelManager().getMissingModel()) {
            return false;
        }
        BlockState state = entity.getBlockState();
        long seed = state.getSeed(entity.getBlockPos());
        poseStack.pushPose();
// 绕方块中心按朝向旋转，角度与 blockstate 变体里的 y 一致（north=0/east=90/south=180/west=270）
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(180 - facing.toYRot()));
        poseStack.translate(-0.5, 0.0, -0.5);
        mc.getBlockRenderer().getModelRenderer().tesselateBlock(
                entity.getLevel(), model, state, entity.getBlockPos(), poseStack,
                bufferSource.getBuffer(RenderType.cutout()), true,
                RandomSource.create(seed), seed, packedOverlay);
        poseStack.popPose();
        return true;
    }

    private static ItemStack getToolForStage(int stage, int animal) {
        boolean isChicken = animal == 4;
        return switch (stage) {
            case 1 -> new ItemStack(ModItems.WIDEEDGEDKNIFE.get());
            case 2 -> isChicken ? ItemStack.EMPTY : new ItemStack(ModItems.SHARPKNIFE.get());
            case 3 -> new ItemStack(ModItems.BONECUTTERKNIFE.get());
            case 4 -> new ItemStack(ModItems.SHARPKNIFE.get());
            case 5 -> new ItemStack(ModItems.SHARPKNIFE.get());
            case 6 -> new ItemStack(ModItems.SHARPKNIFE.get());
            default -> ItemStack.EMPTY;
        };
    }
}