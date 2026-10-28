package com.fidtest.client;

import com.fidtest.block.entity.ChoppingboardBlockEntity;
import com.fidtest.choppingboard.ChoppingboardMobDefinition;
import com.fidtest.choppingboard.ChoppingboardMobRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ChoppingboardRenderer implements BlockEntityRenderer<ChoppingboardBlockEntity> {

// 每个实体类型复用一个 dummy 实体，避免每帧 new。只在同一客户端世界内有效。
    private static final Map<EntityType<?>, Entity> DUMMY_CACHE = new HashMap<>();

    private final ItemRenderer itemRenderer;

    public ChoppingboardRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ChoppingboardBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack stored = be.getStoredItem();
        ItemStack skin = be.getSkinItem();
        ItemStack filling = be.getFillingItem();

        if (skin.isEmpty()) {
            if (!stored.isEmpty()) {
                ChoppingboardMobDefinition def = ChoppingboardMobRegistry.getByInput(stored.getItem());
                if (def != null && def.entity() != null) {
// 屠宰用小型生物：渲染对应生物实体横置在砧板顶面
                    renderEntityOnBoard(be, def, stored, poseStack, bufferSource, packedLight, packedOverlay);
                } else {
                    renderItemOnBoard(be, stored, 0.4F, 0.07F, poseStack, bufferSource, packedLight, packedOverlay);
                }
            }
        } else {
// 有皮：先渲染皮，再在皮面正上方约1像素处渲染馅料（馅料较小，皮四周仍可见）
            renderItemOnBoard(be, skin, 0.45F, 0.075F, poseStack, bufferSource, packedLight, packedOverlay);
            if (!filling.isEmpty()) {
                renderItemOnBoard(be, filling, 0.28F, 0.14F, poseStack, bufferSource, packedLight, packedOverlay);
            }
        }
    }

// 把定义中的生物实体横置渲染在砧板顶面。
// 直接调用 EntityRenderer#render 绕开 EntityRenderDispatcher.render 附带的阴影与
// 视锥判定；实体贴图自动走它自己的 RenderType。任何异常（例如未注册渲染器）都回退到平面物品图标。
    private void renderEntityOnBoard(ChoppingboardBlockEntity be, ChoppingboardMobDefinition def, ItemStack stored,
                                     PoseStack poseStack, MultiBufferSource bufferSource,
                                     int packedLight, int packedOverlay) {
        if (be.getLevel() == null) return;
        EntityType<?> type = def.entity();
        if (type == null) return;

        Entity dummy = DUMMY_CACHE.get(type);
        if (dummy == null || dummy.level() != be.getLevel()) {
            dummy = type.create(be.getLevel());
            if (dummy == null) {
                renderItemOnBoard(be, stored, 0.4F, 0.07F, poseStack, bufferSource, packedLight, packedOverlay);
                return;
            }
            DUMMY_CACHE.put(type, dummy);
        }

        EntityRenderer<?> renderer;
        try {
            renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(dummy);
        } catch (Exception e) {
            renderItemOnBoard(be, stored, 0.4F, 0.07F, poseStack, bufferSource, packedLight, packedOverlay);
            return;
        }
        if (renderer == null) {
            renderItemOnBoard(be, stored, 0.4F, 0.07F, poseStack, bufferSource, packedLight, packedOverlay);
            return;
        }

// 静止展示，不播放动画：partialTicks 传 0，把 tickCount 钉死为 0 即彻底静止
        dummy.setPos(0.0, 0.0, 0.0);
        dummy.tickCount = 0;

        Direction facing = be.getBlockState().getValue(com.fidtest.block.ChoppingboardBlock.FACING);
        ChoppingboardMobDefinition.Render r = def.render();

        poseStack.pushPose();
// 砧板顶面在 y = 1/16 处
        poseStack.translate(0.5, 0.0625 + r.yOffset(), 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot() + r.yaw()));
// 本地水平平移：随方块朝向一起转，保证任何朝向都往"方块中心"收；y 不变
        poseStack.translate(r.xOffset(), 0.0F, r.zOffset());
        poseStack.mulPose(Axis.ZP.rotationDegrees(r.roll()));
        poseStack.scale(r.scale(), r.scale(), r.scale());
        renderRaw(renderer, dummy, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void renderRaw(EntityRenderer renderer, Entity entity, PoseStack poseStack,
                                  MultiBufferSource bufferSource, int packedLight) {
        renderer.render(entity, 0.0F, 0.0F, poseStack, bufferSource, packedLight);
    }

    private void renderItemOnBoard(ChoppingboardBlockEntity be, ItemStack item, float scale, float yOffset,
                                   PoseStack poseStack, MultiBufferSource bufferSource,
                                   int packedLight, int packedOverlay) {
        poseStack.pushPose();

        float yRot = 0;
        if (be.getLevel() != null) {
            Direction facing = be.getBlockState().getValue(
                    com.fidtest.block.ChoppingboardBlock.FACING);
            yRot = -facing.toYRot();
        }

        poseStack.translate(0.5, yOffset, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        poseStack.scale(scale, scale, scale);

        itemRenderer.renderStatic(item,
                net.minecraft.world.item.ItemDisplayContext.FIXED,
                packedLight, packedOverlay, poseStack, bufferSource, be.getLevel(), 0);

        poseStack.popPose();
    }
}
