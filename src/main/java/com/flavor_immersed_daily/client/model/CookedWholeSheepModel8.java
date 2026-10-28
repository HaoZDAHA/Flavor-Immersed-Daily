package com.flavor_immersed_daily.client.model;

import com.flavor_immersed_daily.FlavorImmersedDaily;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.resources.ResourceLocation;

public class CookedWholeSheepModel8 extends CookedWholeSheepModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation(FlavorImmersedDaily.MODID, "cooked_whole_sheep_8"), "main");

    public CookedWholeSheepModel8(ModelPart root) {
        super(root, true);
    }

    public static LayerDefinition createBodyLayer() {

		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition sheep = partdefinition.addOrReplaceChild("sheep", CubeListBuilder.create().texOffs(56, 39).addBox(-8.375F, -0.7576F, -0.8257F, 34.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(81, 73).addBox(-10.375F, -0.7576F, -0.8257F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(93, 70).addBox(-14.375F, 3.2424F, -1.3257F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(58, 17).addBox(6.625F, -1.7576F, -5.8257F, 4.5F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.625F, 4.75F, 8.0F));

		PartDefinition leg3 = sheep.addOrReplaceChild("leg3", CubeListBuilder.create(), PartPose.offset(6.625F, 9.2424F, -12.8257F));

		PartDefinition leg2 = leg3.addOrReplaceChild("leg2", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.1867F, -8.0F, 20.9796F, 0.0F, 1.5708F, 0.0F));

		PartDefinition leg4 = leg3.addOrReplaceChild("leg4", CubeListBuilder.create(), PartPose.offsetAndRotation(18.8133F, -8.0F, 20.9796F, 0.0F, 3.1416F, 0.0F));

		PartDefinition leg5 = leg4.addOrReplaceChild("leg5", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 17.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition roast = partdefinition.addOrReplaceChild("roast", CubeListBuilder.create().texOffs(24, 45).addBox(-8.0F, 3.0F, -1.0F, 32.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(60, 91).addBox(-8.0F, -1.0F, 17.0F, 32.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(61, 69).addBox(22.0F, -1.0F, 1.0F, 2.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(92, 69).addBox(-8.0F, -1.0F, 1.0F, 2.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(60, 108).addBox(22.0F, -17.0F, 6.0F, 2.0F, 16.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(60, 108).addBox(-8.0F, -17.0F, 6.0F, 2.0F, 16.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(56, 112).addBox(-6.0F, -1.0F, 1.0F, 28.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(108, 105).addBox(-2.5307F, 0.4F, 3.3045F, 7.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(108, 105).addBox(6.4693F, 0.4F, 9.3045F, 7.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(84, 0).addBox(-8.0F, -2.0F, -2.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(84, 0).addBox(-1.0F, -2.0F, -2.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(84, 0).addBox(11.0F, -2.0F, 14.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(84, 0).addBox(18.0F, -2.0F, 14.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition cube_r1 = roast.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(108, 105).addBox(-5.0307F, -1.1F, -5.1955F, 7.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.5F, 1.5F, 14.5F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r2 = roast.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(108, 105).addBox(-5.0307F, -1.1F, -5.1955F, 7.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 1.5F, 10.5F, 0.0F, -2.3562F, 0.0F));

		PartDefinition cube_r3 = roast.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(108, 105).addBox(-5.0307F, -1.1F, -5.1955F, 7.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.5F, 1.5F, 8.5F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r4 = roast.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(60, 91).addBox(-16.0F, -2.0F, -1.0F, 32.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 1.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cube_r5 = roast.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(94, 103).addBox(-2.0F, -3.0F, -1.5F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(94, 103).addBox(2.0F, -3.0F, -1.5F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(94, 103).addBox(6.0F, -3.0F, -1.5F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(94, 103).addBox(10.0F, -3.0F, -1.5F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(94, 103).addBox(-6.0F, -3.0F, -1.5F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(94, 103).addBox(-10.0F, -3.0F, -1.5F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(94, 103).addBox(14.0F, -3.0F, -1.5F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 5.0F, 18.5F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r6 = roast.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(94, 103).addBox(-2.0F, -3.0F, -1.5F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(94, 103).addBox(10.0F, -3.0F, -1.5F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(94, 103).addBox(14.0F, -3.0F, -1.5F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(94, 103).addBox(2.0F, -3.0F, -1.5F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(94, 103).addBox(6.0F, -3.0F, -1.5F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(94, 103).addBox(22.0F, -3.0F, -1.5F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(94, 103).addBox(18.0F, -3.0F, -1.5F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 5.0F, -0.5F, -0.3927F, 0.0F, 0.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(102, 0).addBox(-7.0F, -11.0F, -1.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(102, 0).addBox(19.0F, -11.0F, 15.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(120, 25).addBox(0.0F, -12.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(120, 25).addBox(12.0F, -12.0F, 15.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r7 = bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(120, 25).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0F, -11.0F, 18.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r8 = bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(120, 25).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -11.0F, 2.0F, 0.0F, -0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);

    }
}
