package com.fidtest.client;

import com.fidtest.block.SqueezingmachineBlock;
import com.fidtest.block.entity.SqueezingmachineBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SqueezingmachineRenderer implements BlockEntityRenderer<SqueezingmachineBlockEntity> {

    private static final float BASE_Y = 6.0F / 16.0F;
    private static final float ITEM_SCALE = 0.25F;
    private static final float ITEM_Y_GAP = 0.01F;

    private static final float[][] ITEM_OFFSETS = {
            {0, 0, 0}, {0.04F, 0, -0.03F}, {-0.03F, 0, 0.04F}, {0.03F, 0, 0.03F}, {-0.04F, 0, -0.04F}
    };
    private static final float[] ITEM_INIT_ROT = {0, 72, 144, 216, 288};

    private final ItemRenderer itemRenderer;

    public SqueezingmachineRenderer(BlockEntityRendererProvider.Context ctx) { this.itemRenderer = ctx.getItemRenderer(); }

    @Override
    public void render(SqueezingmachineBlockEntity be, float pt, PoseStack ps,
                       MultiBufferSource buf, int light, int overlay) {
        float yRot = 0;
        if (be.getLevel() != null) {
            Direction f = be.getBlockState().getValue(SqueezingmachineBlock.FACING);
            yRot = -f.toYRot();
        }

        if (be.isWorking()) {
            renderWorking(be, pt, ps, buf, light, overlay, yRot);
        } else if (be.hasOutput()) {
            ItemStack out = be.getOutputItem();
            if (!out.isEmpty()) renderSingle(out, ps, buf, light, overlay, yRot, be);
        } else if (be.hasInput()) {
            ItemStack in = be.getInputItem();
            if (!in.isEmpty()) { ItemStack s = in.copy(); s.setCount(1); renderSingle(s, ps, buf, light, overlay, yRot, be); }
        }
    }

    private void renderWorking(SqueezingmachineBlockEntity be, float pt, PoseStack ps,
                               MultiBufferSource buf, int light, int overlay, float yRot) {
        List<ItemStack> items = be.getRenderInputItems();
        if (items.isEmpty()) return;
        float spin = (be.getProcessTick() + pt) * 36F;
        ps.pushPose();
        ps.translate(0.5, BASE_Y, 0.5);
        ps.mulPose(Axis.YP.rotationDegrees(yRot));
        for (int i = 0; i < items.size(); i++) {
            float[] off = ITEM_OFFSETS[i % ITEM_OFFSETS.length];
            ps.pushPose();
            ps.translate(off[0], i * (ITEM_SCALE * 0.6F + ITEM_Y_GAP), off[2]);
            ps.mulPose(Axis.YP.rotationDegrees(spin + ITEM_INIT_ROT[i % ITEM_INIT_ROT.length]));
            ps.mulPose(Axis.XP.rotationDegrees(90));
            ps.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            itemRenderer.renderStatic(items.get(i), ItemDisplayContext.FIXED, light, overlay, ps, buf, be.getLevel(), 0);
            ps.popPose();
        }
        ps.popPose();
    }

    private void renderSingle(ItemStack stack, PoseStack ps, MultiBufferSource buf, int light, int overlay, float yRot, SqueezingmachineBlockEntity be) {
        ps.pushPose();
        ps.translate(0.5, BASE_Y + 0.02F, 0.5);
        ps.mulPose(Axis.YP.rotationDegrees(yRot));
        ps.mulPose(Axis.XP.rotationDegrees(90));
        ps.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, ps, buf, be.getLevel(), 0);
        ps.popPose();
    }
}
