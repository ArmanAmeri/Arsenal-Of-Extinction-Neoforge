package com.modishmonkee.arsenalofextinction.mmcosmetics;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;

public class MonkeeTailModel<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart tail;
    public final AnimationState idleAnimationState = new AnimationState();

    public MonkeeTailModel(ModelPart root) {
        this.root = root;
        this.tail = root.getChild("tail");
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 11F, 2F, 0.0F, 3.1416F, 0.0F));

        PartDefinition bone1 = tail.addOrReplaceChild("bone1", CubeListBuilder.create(), PartPose.offset(-0.0043F, -0.0121F, 0.8123F));
        bone1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(8, 0)
                        .addBox(-0.9814F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.4399F, 0.0F, 0.0F));

        PartDefinition bone2 = bone1.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(0.0F, -0.3F, -4.35F));
        bone2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(8, 7)
                        .addBox(-0.9814F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6545F, 0.0F, 0.0F));

        PartDefinition bone3 = bone2.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offset(0.0F, -3.25F, -2.9F));
        bone3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 14)
                        .addBox(-0.9814F, -5.0F, -0.95F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition bone4 = bone3.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offset(0.0F, -4.75F, 0.9F));
        bone4.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(8, 14)
                        .addBox(-0.9814F, -5.0F, -0.95F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition bone5 = bone4.addOrReplaceChild("bone5", CubeListBuilder.create(), PartPose.offset(0.0F, -4.45F, -0.45F));
        bone5.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(16, 0)
                        .addBox(-0.9814F, -5.0F, -0.95F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6545F, 0.0F, 0.0F));

        PartDefinition bone6 = bone5.addOrReplaceChild("bone6", CubeListBuilder.create(), PartPose.offset(0.0F, -3.6F, -2.6F));
        bone6.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.9814F, -5.0F, -0.95F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.2217F, 0.0F, 0.0F));

        PartDefinition bone7 = bone6.addOrReplaceChild("bone7", CubeListBuilder.create(), PartPose.offset(0.0F, -1.7F, -3.9F));
        bone7.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 7)
                        .addBox(-0.9814F, -5.0F, -0.95F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.8326F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(idleAnimationState, MonkeeTailAnimations.ANIM_TAIL_IDLE, ageInTicks, 1f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        tail.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
