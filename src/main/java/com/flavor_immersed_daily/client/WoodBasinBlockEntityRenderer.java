package com.flavor_immersed_daily.client;

import com.flavor_immersed_daily.all.ModItems;

import com.flavor_immersed_daily.block.block.processing.WoodBasinBlock;
import com.flavor_immersed_daily.block.blockentity.WoodBasinBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

// 木盆渲染器 — 渲染chickenwithoutblood浮动 / 水果小堆 / 屠宰产物
public class WoodBasinBlockEntityRenderer implements BlockEntityRenderer<WoodBasinBlockEntity> {

    private static final ItemStack BLEDCHICKEN_STACK = new ItemStack(ModItems.CHICKENWITHOUTBLOOD.get());

    public WoodBasinBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(WoodBasinBlockEntity entity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (entity.getBlockState().getValue(WoodBasinBlock.HAS_CHICKEN)) {
            renderChicken(entity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        }
        if (entity.getBlockState().getValue(WoodBasinBlock.HAS_FRUIT)) {
            renderFruitPile(entity, poseStack, bufferSource, packedLight, packedOverlay);
        }
        if (entity.hasStorage()) {
            renderStorage(entity, poseStack, bufferSource, packedLight, packedOverlay);
        }
    }

    private void renderChicken(WoodBasinBlockEntity entity, float partialTick, PoseStack poseStack,
                                MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        float ticks = entity.getTickCount() + partialTick;
        float floatOffset = (float) Math.sin(ticks * 0.08) * 0.06f;

// 数据包 basin_wash 定义的自定义模型优先
        ResourceLocation customModel = entity.getWashModel();
        if (customModel != null && renderCustomModel(entity, poseStack, bufferSource, customModel,
                floatOffset, packedLight, packedOverlay)) {
            return;
        }

// 否则按泡洗物品渲染；旧存档（无 washStack）回退为放血鸡
        ItemStack stack = entity.getWashStack();
        if (stack.isEmpty()) {
            stack = BLEDCHICKEN_STACK;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 0.25 + floatOffset, 0.5);
        poseStack.mulPose(Axis.XP.rotationDegrees(65));
        poseStack.scale(1.0f, 1.0f, 1.0f);

        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack, ItemDisplayContext.GROUND, packedLight, packedOverlay,
                poseStack, bufferSource, entity.getLevel(), 0);
        poseStack.popPose();
    }

// 把 basin_wash 定义的方块模型画在木盆里，附带上下浮动；成功返回 true。
    private boolean renderCustomModel(WoodBasinBlockEntity entity, PoseStack poseStack, MultiBufferSource bufferSource,
                                      ResourceLocation modelId, float floatOffset,
                                      int packedLight, int packedOverlay) {
        Minecraft mc = Minecraft.getInstance();
        BakedModel model = mc.getModelManager().getModel(ModelResourceLocation.standalone(modelId));
        if (model == null || model == mc.getModelManager().getMissingModel()) {
            return false;
        }
        long seed = entity.getBlockPos().asLong();
        poseStack.pushPose();
        poseStack.translate(0.0, floatOffset, 0.0);
        mc.getBlockRenderer().getModelRenderer().tesselateBlock(
                entity.getLevel(), model, entity.getBlockState(), entity.getBlockPos(), poseStack,
                bufferSource.getBuffer(RenderType.cutout()), true,
                RandomSource.create(seed), seed, packedOverlay);
        poseStack.popPose();
        return true;
    }

    private void renderFruitPile(WoodBasinBlockEntity entity, PoseStack poseStack,
                                  MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack fruitStack = entity.getFruitStack();
        if (fruitStack.isEmpty()) return;

// 用blockPos做种子，每个位置的小堆外观固定
        long seed = entity.getBlockPos().asLong();
        RandomSource random = RandomSource.create(seed);

// 渲染 9 个水果实例，躺平散落在盆内
// 盆地 y≈4px=0.25，水果摊平在 y≈0.28~0.32
        for (int i = 0; i < 9; i++) {
            poseStack.pushPose();

// 位置：散落在盆地内
            float x = (float) (0.25 + random.nextDouble() * 0.5);
            float z = (float) (0.25 + random.nextDouble() * 0.5);
            float y = (float) (0.26 + random.nextDouble() * 0.06);

            poseStack.translate(x, y, z);

// 放平：绕X轴旋转约90度让物品横躺
            poseStack.mulPose(Axis.XP.rotationDegrees(80 + random.nextFloat() * 20));
// 不同方向散开
            poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360));
// 略微倾斜
            poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 30 - 15));

// 大小不一
            float scale = 0.3f + random.nextFloat() * 0.3f;
            poseStack.scale(scale, scale, scale);

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    fruitStack, ItemDisplayContext.FIXED, packedLight, packedOverlay,
                    poseStack, bufferSource, entity.getLevel(), 0);
            poseStack.popPose();
        }
    }

    private void renderStorage(WoodBasinBlockEntity entity, PoseStack poseStack,
                               MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        List<ItemStack> stacks = entity.getStorageStacks();
        if (stacks.isEmpty()) return;

// 把每种物品按数量展开成图标，最多 16 个
        List<ItemStack> icons = new ArrayList<>();
        for (ItemStack s : stacks) {
            int limit = 16 - icons.size();
            int n = Math.max(1, Math.min(s.getCount(), limit));
            for (int i = 0; i < n; i++) {
                icons.add(s);
            }
            if (icons.size() >= 16) break;
        }

        long seed = entity.getBlockPos().asLong();
        RandomSource random = RandomSource.create(seed);

// 堆在盆底（盆高6px，y≈0.12~0.3），散落摆放
        for (ItemStack icon : icons) {
            poseStack.pushPose();
            float x = 0.22f + random.nextFloat() * 0.56f;
            float z = 0.22f + random.nextFloat() * 0.56f;
            float y = 0.1f + random.nextFloat() * 0.2f;

            poseStack.translate(x, y, z);
            poseStack.mulPose(Axis.XP.rotationDegrees(70 + random.nextFloat() * 40));
            poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360));
            poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 40 - 20));

            float scale = 0.26f + random.nextFloat() * 0.26f;
            poseStack.scale(scale, scale, scale);

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    icon, ItemDisplayContext.FIXED, packedLight, packedOverlay,
                    poseStack, bufferSource, entity.getLevel(), 0);
            poseStack.popPose();
        }
    }
}
