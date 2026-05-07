package com.modishmonkee.arsenalofextinction.entity.client;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.modishmonkee.arsenalofextinction.entity.custom.NukeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NukeBombModel extends EntityModel<NukeEntity> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(ArsenalOfExtinction.MOD_ID, "nukebomb"), "main");

    private final ModelPart nuke;

    public NukeBombModel(ModelPart root) {
        this.nuke = root.getChild("nuke");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition nuke = partdefinition.addOrReplaceChild("nuke", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 4F, -8.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition body = nuke.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 34).addBox(-1.0F, -16.0F, -2.0F, 6.0F, 16.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 42).addBox(-1.0F, -16.0F, 10.0F, 6.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, -16.0F, -2.0F, 6.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 7.0F, 0.0F, 1.5708F, 0.0F));
        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(16, 60).addBox(-1.0F, -16.01F, -2.0F, 6.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.45F, 0.0F, 11.25F, 0.0F, 0.7854F, 0.0F));
        PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 52).addBox(-1.0F, -16.01F, -2.0F, 6.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, 0.0F, 8.45F, 0.0F, -0.7854F, 0.0F));
        PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(50, 18).addBox(-1.0F, -16.01F, -2.0F, 6.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4F, 0.0F, -1.2F, 0.0F, -2.3562F, 0.0F));
        PartDefinition cube_r5 = body.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(48, 42).addBox(-1.0F, -16.01F, -2.0F, 6.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.25F, 0.0F, 1.55F, 0.0F, 2.3562F, 0.0F));
        PartDefinition cube_r6 = body.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(32, 42).addBox(-1.0F, -16.0F, -2.0F, 6.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 0.0F, 7.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition head = nuke.addOrReplaceChild("head", CubeListBuilder.create().texOffs(64, 36).addBox(-3.0F, 8.0F, -4.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -28.6F, 6.0F));

        PartDefinition cube_r7  = head.addOrReplaceChild("cube_r7",  CubeListBuilder.create().texOffs(0, 70).addBox(-1.0F, -2.1F, 2.75F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.15F, 16.45F, -6.55F, 0.8727F, -0.7854F, 0.0F));
        PartDefinition cube_r8  = head.addOrReplaceChild("cube_r8",  CubeListBuilder.create().texOffs(70, 74).addBox(-1.0F, -2.0F, -3.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 13.45F, -4.85F, 2.1983F, -0.7854F, -3.1125F));
        PartDefinition cube_r9  = head.addOrReplaceChild("cube_r9",  CubeListBuilder.create().texOffs(16, 78).addBox(-1.0F, -2.0F, -3.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9F, 13.35F, 1.5F, -0.8727F, -0.7854F, 0.0F));
        PartDefinition cube_r10 = head.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(32, 76).addBox(-1.0F, -2.0F, -3.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6F, 11.8F, 2.0F, -0.6707F, -0.445F, 1.0737F));
        PartDefinition cube_r11 = head.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(32, 60).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.4F, 11.65F, -1.0F, 0.0F, 0.0F, 0.8727F));
        PartDefinition cube_r12 = head.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(64, 0).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.4F, 11.65F, -1.0F, 0.0F, 0.0F, -0.8727F));
        PartDefinition cube_r13 = head.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(66, 24).addBox(-3.0F, -1.85F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.55F, -4.5F, 0.8727F, 0.0F, 0.0F));
        PartDefinition cube_r14 = head.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(64, 44).addBox(-3.0F, -1.85F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.55F, 2.5F, -0.8727F, 0.0F, 0.0F));

        PartDefinition tail = nuke.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(64, 52).addBox(-3.0F, 8.0F, -4.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 12.4F, 4.0F, 3.1416F, 0.0F, 0.0F));

        PartDefinition cube_r15 = tail.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(70, 68).addBox(-1.0F, -2.1F, 2.75F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.15F, 16.45F, -6.55F, 0.8727F, -0.7854F, 0.0F));
        PartDefinition cube_r16 = tail.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 76).addBox(-1.0F, -2.0F, -3.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 13.45F, -4.85F, 2.1983F, -0.7854F, -3.1125F));
        PartDefinition cube_r17 = tail.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(80, 60).addBox(-1.0F, -2.0F, -3.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9F, 13.35F, 1.5F, -0.8727F, -0.7854F, 0.0F));
        PartDefinition cube_r18 = tail.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(48, 78).addBox(-1.0F, -2.0F, -3.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6F, 11.8F, 2.0F, -0.6707F, -0.445F, 1.0737F));
        PartDefinition cube_r19 = tail.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(56, 60).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.4F, 11.65F, -1.0F, 0.0F, 0.0F, 0.8727F));
        PartDefinition cube_r20 = tail.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(64, 8).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.4F, 11.65F, -1.0F, 0.0F, 0.0F, -0.8727F));
        PartDefinition cube_r21 = tail.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(32, 68).addBox(-3.0F, -1.85F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.55F, -4.5F, 0.8727F, 0.0F, 0.0F));
        PartDefinition cube_r22 = tail.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(66, 16).addBox(-3.0F, -1.85F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.55F, 2.5F, -0.8727F, 0.0F, 0.0F));

        PartDefinition wing = tail.addOrReplaceChild("wing", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -5.0F, -8.0F, 1.0F, 5.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 17).addBox(-11.0F, -5.0F, -8.0F, 1.0F, 5.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(26, 30).addBox(-8.0F, -3.8F, -5.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 5.8F, 1.0F));

        PartDefinition cube_r23 = wing.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(56, 68).addBox(0.0F, -3.8F, -1.0F, 1.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.4F, 1.5F, -0.3F, 0.3438F, -0.7854F, 0.0F));
        PartDefinition cube_r24 = wing.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(78, 80).addBox(-5.0F, -4.0F, -1.0F, 6.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3F, 0.2F, -6.6F, -0.3469F, 0.7261F, -0.4817F));
        PartDefinition cube_r25 = wing.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(50, 36).addBox(-5.0F, -4.0F, -1.0F, 6.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.35F, 1.7F, -3.6F, -0.3295F, -0.7268F, 0.4754F));
        PartDefinition cube_r26 = wing.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(64, 80).addBox(-5.0F, -4.0F, -1.0F, 6.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.65F, 0.3F, 3.1F, 0.3295F, -0.7268F, -0.4754F));
        PartDefinition cube_r27 = wing.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(26, 15).addBox(0.0F, -5.0F, -8.0F, 1.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.0F, -8.0F, 0.0F, -1.5708F, 0.0F));
        PartDefinition cube_r28 = wing.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(26, 0).addBox(0.0F, -5.0F, -8.0F, 1.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.0F, 3.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition engine = tail.addOrReplaceChild("engine", CubeListBuilder.create().texOffs(16, 34).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 36).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition cube_r29 = engine.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(16, 40).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        PartDefinition cube_r30 = engine.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(16, 38).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, -1.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(NukeEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        // No animation — nuke flies rigidly
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, int color) {
        nuke.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
