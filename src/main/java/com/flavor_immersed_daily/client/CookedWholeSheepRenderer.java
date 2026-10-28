package com.flavor_immersed_daily.client;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import com.flavor_immersed_daily.all.ModBlocks;
import com.flavor_immersed_daily.block.block.food.CookedWholeSheepBlock;
import com.flavor_immersed_daily.block.blockentity.CookedWholeSheepBlockEntity;
import com.flavor_immersed_daily.client.model.CookedWholeSheepModel;
import com.flavor_immersed_daily.client.model.CookedWholeSheepModel0;
import com.flavor_immersed_daily.client.model.CookedWholeSheepModel1;
import com.flavor_immersed_daily.client.model.CookedWholeSheepModel2;
import com.flavor_immersed_daily.client.model.CookedWholeSheepModel3;
import com.flavor_immersed_daily.client.model.CookedWholeSheepModel4;
import com.flavor_immersed_daily.client.model.CookedWholeSheepModel5;
import com.flavor_immersed_daily.client.model.CookedWholeSheepModel6;
import com.flavor_immersed_daily.client.model.CookedWholeSheepModel7;
import com.flavor_immersed_daily.client.model.CookedWholeSheepModel8;
import com.flavor_immersed_daily.client.model.CookedWholeSheepModel9;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.math.Axis;

import java.util.function.Supplier;

// 烤全羊渲染器 — 按方块状态中的 STAGE 选择 0~9 号模型，
// 仅在主方块（ORIGIN=0）上渲染，羊骨架绕烤叉持续旋转（8 秒一圈）。
public class CookedWholeSheepRenderer implements BlockEntityRenderer<CookedWholeSheepBlockEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            FlavorImmersedDaily.MODID, "textures/entity/cooked_whole_sheep.png");

// Blockbench 以 modded_entity_flip_y=true 导出（坐标按 x→-x, y→24-y 翻转），
// 因此导出模型在游戏中呈上下颠倒（火盆在上、羊在下）。
// 渲染时对整个模型做一次整体扭转（绕 Z 轴 180° 后 Y 上移 24，即 x→-x, y→24-y），
// 完全翻回设计空间（与 bbmodel 一致：羊在上、火在下）。
// 设计空间范围（像素单位，16px=1格）：
// x: -30.49 ~ 18.00   y: -0.35 ~ 25.00   z: -5.28 ~ 42.00（stage 0 含羊腿）
// 以各阶段不变的部件（火盆+烤架）中心为锚点，使火盆始终固定在区域中央，羊吃完后火盆不动。
// 设计空间 火盆/烤架 中心 X
    private static final float STAND_CENTER_X = -8.0F;
// 设计空间 火盆/烤架 中心 Z
    private static final float STAND_CENTER_Z = 9.0F;
// 设计空间最低点抬升量（贴地）
    private static final float GROUND_LIFT = 0.35F;
// 8 秒一圈
    private static final float SPIN_TICKS = 160F;

    private final CookedWholeSheepModel[] models = new CookedWholeSheepModel[10];

    public CookedWholeSheepRenderer(BlockEntityRendererProvider.Context context) {
        EntityModelSet modelSet = context.getModelSet();
        models[0] = new CookedWholeSheepModel0(modelSet.bakeLayer(CookedWholeSheepModel0.LAYER_LOCATION));
        models[1] = new CookedWholeSheepModel1(modelSet.bakeLayer(CookedWholeSheepModel1.LAYER_LOCATION));
        models[2] = new CookedWholeSheepModel2(modelSet.bakeLayer(CookedWholeSheepModel2.LAYER_LOCATION));
        models[3] = new CookedWholeSheepModel3(modelSet.bakeLayer(CookedWholeSheepModel3.LAYER_LOCATION));
        models[4] = new CookedWholeSheepModel4(modelSet.bakeLayer(CookedWholeSheepModel4.LAYER_LOCATION));
        models[5] = new CookedWholeSheepModel5(modelSet.bakeLayer(CookedWholeSheepModel5.LAYER_LOCATION));
        models[6] = new CookedWholeSheepModel6(modelSet.bakeLayer(CookedWholeSheepModel6.LAYER_LOCATION));
        models[7] = new CookedWholeSheepModel7(modelSet.bakeLayer(CookedWholeSheepModel7.LAYER_LOCATION));
        models[8] = new CookedWholeSheepModel8(modelSet.bakeLayer(CookedWholeSheepModel8.LAYER_LOCATION));
        models[9] = new CookedWholeSheepModel9(modelSet.bakeLayer(CookedWholeSheepModel9.LAYER_LOCATION));
    }

    @Override
    public void render(CookedWholeSheepBlockEntity entity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = entity.getLevel();
        if (level == null) return;
        BlockState state = entity.getBlockState();
        if (state.getBlock() != ModBlocks.COOKED_WHOLE_SHEEP.get()) return;
// 只渲染主方块
        if (state.getValue(CookedWholeSheepBlock.ORIGIN) != 0) return;

        int stage = state.getValue(CookedWholeSheepBlock.STAGE);
        CookedWholeSheepModel model = models[stage];
        if (model == null) return;

        poseStack.pushPose();
// 1) 平移到 2x2x2 连体区域中心（区域自主方块向 +X/+Y/+Z 延伸，跨度 2 格）
        poseStack.translate(1.0, 0.0, 1.0);
// 2) 按方块朝向旋转，模型正面（设计空间 +Z）对准放置方向
        poseStack.mulPose(Axis.YP.rotationDegrees(state.getValue(CookedWholeSheepBlock.FACING).toYRot()));
// 3) 模型像素单位(16px=1格) → 方块单位
        poseStack.scale(1 / 16f, 1 / 16f, 1 / 16f);
// 4) 在翻转后的设计空间内，以火盆/烤架为锚点居中、整体贴地
        poseStack.translate(-STAND_CENTER_X, GROUND_LIFT, -STAND_CENTER_Z);
// 5) 整体扭转：几何先绕 Z 轴 180° 翻转，再 Y 上移 24（即 x→-x, y→24-y），
// 把导出时的翻转翻回，恢复设计姿态（羊在上、火在下）
// → (-x, 24-y, z)
        poseStack.translate(0.0, 24.0, 0.0);
// (x,y,z) → (-x,-y,z)
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));

// 旋转动画：羊骨架绕烤叉（X 轴）持续旋转，8 秒一圈
        float ticks = level.getGameTime() + partialTick;
        model.getSpit().xRot = (ticks % SPIN_TICKS) / SPIN_TICKS * Mth.TWO_PI;

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURE));
        model.renderToBuffer(poseStack, consumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

// 供客户端注册 10 个阶段的模型层定义
    public static final ModelLayerLocation[] LAYER_LOCATIONS = {
            CookedWholeSheepModel0.LAYER_LOCATION,
            CookedWholeSheepModel1.LAYER_LOCATION,
            CookedWholeSheepModel2.LAYER_LOCATION,
            CookedWholeSheepModel3.LAYER_LOCATION,
            CookedWholeSheepModel4.LAYER_LOCATION,
            CookedWholeSheepModel5.LAYER_LOCATION,
            CookedWholeSheepModel6.LAYER_LOCATION,
            CookedWholeSheepModel7.LAYER_LOCATION,
            CookedWholeSheepModel8.LAYER_LOCATION,
            CookedWholeSheepModel9.LAYER_LOCATION,
    };

    @SuppressWarnings("unchecked")
    public static final Supplier<LayerDefinition>[] LAYER_DEFINITIONS = new Supplier[] {
            CookedWholeSheepModel0::createBodyLayer,
            CookedWholeSheepModel1::createBodyLayer,
            CookedWholeSheepModel2::createBodyLayer,
            CookedWholeSheepModel3::createBodyLayer,
            CookedWholeSheepModel4::createBodyLayer,
            CookedWholeSheepModel5::createBodyLayer,
            CookedWholeSheepModel6::createBodyLayer,
            CookedWholeSheepModel7::createBodyLayer,
            CookedWholeSheepModel8::createBodyLayer,
            CookedWholeSheepModel9::createBodyLayer,
    };
}
