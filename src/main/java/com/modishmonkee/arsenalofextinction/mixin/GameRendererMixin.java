package com.modishmonkee.arsenalofextinction.mixin;

import com.modishmonkee.arsenalofextinction.client.ScreenShakeEffect;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    private int lastUpdateTick = -1;

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void onRenderLevel(DeltaTracker deltaTracker, CallbackInfo ci) {
        int currentTick = (int) net.minecraft.client.Minecraft.getInstance().level.getGameTime();
        if (currentTick != lastUpdateTick) {
            lastUpdateTick = currentTick;
            ScreenShakeEffect.update();
        }
    }

    @Inject(method = "bobHurt", at = @At("HEAD"))
    private void onBobHurt(PoseStack poseStack, float partialTick, CallbackInfo ci) {
        if (ScreenShakeEffect.offsetX != 0f || ScreenShakeEffect.offsetY != 0f) {
            poseStack.translate(ScreenShakeEffect.offsetX, ScreenShakeEffect.offsetY, 0f);
        }
    }
}
