package com.flavor_immersed_daily.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

// 烤全羊模型基类 — 10 个阶段共用结构：
// sheep（羊骨架，绕烤叉旋转的动画部分）、roast（烤肉盘）、bb_main（烤架，最后阶段消失）
public abstract class CookedWholeSheepModel extends EntityModel<Entity> {

    protected final ModelPart sheep;
    protected final ModelPart roast;
    protected final ModelPart bbMain;

    protected CookedWholeSheepModel(ModelPart root, boolean hasStand) {
        this.sheep = root.getChild("sheep");
        this.roast = root.getChild("roast");
        this.bbMain = hasStand ? root.getChild("bb_main") : null;
    }

// 旋转部分（羊骨架，绕烤叉旋转）
    public ModelPart getSpit() {
        return sheep;
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight,
                               int packedOverlay, int packedColor) {
        sheep.render(poseStack, vertexConsumer, packedLight, packedOverlay, packedColor);
        roast.render(poseStack, vertexConsumer, packedLight, packedOverlay, packedColor);
        if (bbMain != null) {
            bbMain.render(poseStack, vertexConsumer, packedLight, packedOverlay, packedColor);
        }
    }
}
