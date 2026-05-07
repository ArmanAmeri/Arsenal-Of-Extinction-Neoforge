package com.modishmonkee.arsenalofextinction.mixin;

import com.modishmonkee.arsenalofextinction.client.FlashbangEffect;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(GuiGraphics pGuiGraphics, DeltaTracker pDeltaTracker, CallbackInfo ci) {
        FlashbangEffect.render(pGuiGraphics);
    }
}
