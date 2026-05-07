package com.modishmonkee.arsenalofextinction.entity.client;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.modishmonkee.arsenalofextinction.entity.custom.NukeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class NukeRenderer extends EntityRenderer<NukeEntity> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ArsenalOfExtinction.MOD_ID, "textures/entity/nukebomb/nukebomb.png");

    private final NukeBombModel model;

    public NukeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new NukeBombModel(context.bakeLayer(NukeBombModel.LAYER_LOCATION));
    }

    @Override
    public void render(NukeEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        poseStack.pushPose();
        poseStack.scale(4.0f, 4.0f, 4.0f);

        poseStack.mulPose(Axis.YP.rotationDegrees(
                Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 180.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(
                Mth.lerp(partialTick, entity.xRotO, entity.getXRot())));

        var vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURE));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(NukeEntity entity) {
        return TEXTURE;
    }
}
