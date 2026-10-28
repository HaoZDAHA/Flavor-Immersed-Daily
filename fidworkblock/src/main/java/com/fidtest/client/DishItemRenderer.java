package com.fidtest.client;

import com.fidtest.item.DishItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// 工序菜（dish）物品渲染。
// 参考月饼“只渲染纹理中的一小块区域”的思路：底盘照常绘制烘焙模型，
// 之后在盘面上叠加每个食材纹理的一小块（中心子区域），而不是把整个物品图标缩放贴上。
// 坐标说明：dish 的物品模型元素在 0~16 的物品空间里，正面位于 z=7.5。
// 因此所有叠加贴片必须使用同一 0~16 空间，并放在 z>7.5 处，否则会被底盘遮住。
public class DishItemRenderer extends BlockEntityWithoutLevelRenderer {

// 底盘正面的 z 坐标（0~16 物品空间，见 dish 烘焙模型 createUnbakedItemElements）
    private static final float PLATE_SURFACE_Z = 7.5F;

// 盘心坐标
    private static final float PLATE_CENTER = 8.0F;

// 最多显示的食材贴片数量
    private static final int MAX_PATCHES = 8;

// 取纹理中心多大比例做贴片（0.22 表示裁掉四周各 22%，只取中间 56%）
    private static final float TEXTURE_INSET = 0.22F;

    public DishItemRenderer() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext,
                             PoseStack poseStack, MultiBufferSource buffer,
                             int packedLight, int packedOverlay) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();

// 底图：直接绘制烘焙模型的四边形。不能调用 itemRenderer.render(...)，
// 因为 dish 模型 isCustomRenderer()==true，会再次进入 renderByItem 造成无限递归。
        BakedModel baseModel = itemRenderer.getItemModelShaper().getItemModel(stack);
        if (baseModel != null) {
            VertexConsumer baseVc = buffer.getBuffer(RenderType.cutout());
            itemRenderer.renderModelLists(baseModel, stack, packedLight, packedOverlay, poseStack, baseVc);
        }

// 在盘面上叠加食材纹理的局部贴片
        renderIngredientTextures(stack, poseStack, buffer, packedLight, packedOverlay, itemRenderer);
    }

// 把炒菜用到的每个食材纹理的中心子区域，裁成一小块贴在盘面上。
// 贴片堆在盘心，位置/大小/角度用食材列表做随机种子，保证同一盘菜每次摆放一致。
    private void renderIngredientTextures(ItemStack dish, PoseStack poseStack, MultiBufferSource buffer,
                                          int packedLight, int packedOverlay, ItemRenderer itemRenderer) {
        List<String> names = DishItem.getItemNames(dish);
        if (names.isEmpty()) return;

        BakedModel missingModel = Minecraft.getInstance().getModelManager().getMissingModel();
        List<TextureAtlasSprite> sprites = new ArrayList<>();
        for (String name : names) {
            try {
                Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(name));
                if (item == Items.AIR) continue;
                BakedModel model = itemRenderer.getItemModelShaper().getItemModel(new ItemStack(item));
                if (model == null || model == missingModel) continue;
                TextureAtlasSprite sprite = model.getParticleIcon();
                if (sprite == null) continue;
                sprites.add(sprite);
            } catch (Exception ignored) {
            }
        }

        int count = Math.min(sprites.size(), MAX_PATCHES);
        if (count == 0) return;

        long seed = 0L;
        for (String name : names) {
            seed = seed * 31L + name.hashCode();
        }
        Random rng = new Random(seed);

        VertexConsumer vc = buffer.getBuffer(RenderType.cutout());
        Matrix4f matrix = poseStack.last().pose();

        for (int i = 0; i < count; i++) {
            TextureAtlasSprite sprite = sprites.get(i);

// 摆放：中心 + 黄金角环形散开，整体限制在盘面内
            float angle = (float) (i * 2.3999632) + rng.nextFloat() * 0.6F;
            float radius = (i == 0) ? 0.0F : 0.9F + rng.nextFloat() * 1.9F;
            float cx = PLATE_CENTER + (float) Math.cos(angle) * radius;
            float cy = PLATE_CENTER + (float) Math.sin(angle) * radius;

// 贴片半边长，3.4~5.2 单位见方
            float half = 1.7F + rng.nextFloat() * 0.9F;
            float rot = rng.nextFloat() * (float) (Math.PI * 2.0);
// 层层抬高，避免 z-fighting
            float z = PLATE_SURFACE_Z + 0.06F + i * 0.01F;

// 只取食材纹理的中心一块（部分纹理）
            float u0 = lerp(sprite.getU0(), sprite.getU1(), TEXTURE_INSET);
            float u1 = lerp(sprite.getU0(), sprite.getU1(), 1.0F - TEXTURE_INSET);
            float v0 = lerp(sprite.getV0(), sprite.getV1(), TEXTURE_INSET);
            float v1 = lerp(sprite.getV0(), sprite.getV1(), 1.0F - TEXTURE_INSET);

            addPatch(vc, matrix, cx, cy, z, half, rot, u0, v0, u1, v1, packedLight, packedOverlay);
        }
    }

    private static float lerp(float from, float to, float t) {
        return from + (to - from) * t;
    }

// 绘制一块以 (cx,cy) 为中心、绕 Z 轴旋转 rot、法线朝 +Z 的方形纹理贴片
    private static void addPatch(VertexConsumer vc, Matrix4f matrix, float cx, float cy, float z,
                                 float half, float rot, float u0, float v0, float u1, float v1,
                                 int packedLight, int packedOverlay) {
        float cos = (float) Math.cos(rot);
        float sin = (float) Math.sin(rot);

// 局部四角，逆时针（从右下开始），使正面朝向 +Z
        float[][] corners = {
// 右下
                {half, -half},
// 右上
                {half, half},
// 左上
                {-half, half},
// 左下
                {-half, -half},
        };
        float[] us = {u1, u1, u0, u0};
        float[] vs = {v1, v0, v0, v1};

        int lightU = packedLight & 0xFFFF;
        int lightV = (packedLight >> 16) & 0xFFFF;
        int overlayU = packedOverlay & 0xFFFF;
        int overlayV = (packedOverlay >> 16) & 0xFFFF;

        for (int k = 0; k < 4; k++) {
            float lx = corners[k][0];
            float ly = corners[k][1];
            float x = cx + lx * cos - ly * sin;
            float y = cy + lx * sin + ly * cos;
            vc.vertex(matrix, x, y, z)
                    .color(255, 255, 255, 255)
                    .uv(us[k], vs[k])
                    .overlayCoords(overlayU, overlayV)
                    .uv2(lightU, lightV)
                    .normal(0.0F, 0.0F, 1.0F)
                    .endVertex();
        }
    }
}
