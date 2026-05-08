package com.modishmonkee.arsenalofextinction.mmcosmetics;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class MonkeeTailLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final MonkeeTailModel<AbstractClientPlayer> model;

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ArsenalOfExtinction.MOD_ID, "textures/entity/tail/monkee_tail.png");

    public MonkeeTailLayer(PlayerRenderer renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new MonkeeTailModel<>(modelSet.bakeLayer(ModModelLayers.MONKEE_TAIL));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

        if (player.getName().getString().equals("Dev") || player.getName().getString().equals("ModishMonkee")) {

            model.idleAnimationState.animateWhen(true, player.tickCount);

            poseStack.pushPose();
            this.getParentModel().body.translateAndRotate(poseStack);

            model.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            var vertexConsumer = buffer.getBuffer(this.model.renderType(TEXTURE));
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

            poseStack.popPose();
        }
    }
}
