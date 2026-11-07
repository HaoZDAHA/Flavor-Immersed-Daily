package com.fidtest.client;

import com.fidtest.client.model.DishModelGeometry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DishItemRenderer extends BlockEntityWithoutLevelRenderer {
    public DishItemRenderer() { super(null, null); }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext,
                             PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
        if (displayContext == ItemDisplayContext.GUI || displayContext.firstPerson()
                || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                || displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
            VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(DishRenderCache.get(stack).icon()));
            iconVertex(vc, pose, 0, 0, 0, 1, light, overlay);
            iconVertex(vc, pose, 1, 0, 1, 1, light, overlay);
            iconVertex(vc, pose, 1, 1, 1, 0, light, overlay);
            iconVertex(vc, pose, 0, 1, 0, 0, light, overlay);
        } else {
            renderDish(stack, pose, buffer, light, overlay);
        }
    }

    public static void renderDish(ItemStack stack, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
        var mc = Minecraft.getInstance();
        var plate = mc.getModelManager().getModel(ModelResourceLocation.standalone(DishModelGeometry.PLATE));
        mc.getItemRenderer().renderModelLists(plate, stack, light, overlay, pose,
                buffer.getBuffer(RenderType.entityCutoutNoCull(TextureAtlas.LOCATION_BLOCKS)));
        DishRenderCache.get(stack).render(pose,
                buffer.getBuffer(RenderType.entityTranslucent(TextureAtlas.LOCATION_BLOCKS)), light, overlay);
    }

    private static void iconVertex(VertexConsumer vc, PoseStack pose, float x, float y, float u, float v, int light, int overlay) {
        vc.addVertex(pose.last(), x, y, .5f).setColor(255, 255, 255, 255).setUv(u, v)
                .setOverlay(overlay).setLight(light).setNormal(pose.last(), 0, 0, 1);
    }
}
