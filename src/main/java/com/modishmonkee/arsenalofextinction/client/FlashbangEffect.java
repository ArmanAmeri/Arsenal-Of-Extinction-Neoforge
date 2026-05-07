package com.modishmonkee.arsenalofextinction.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FlashbangEffect {

    private static int flashTicks = 0;
    private static final int FLASH_DURATION = 400;
    private static final int FADE_DURATION = 350;

    public static void trigger() {
        flashTicks = FLASH_DURATION;
    }

    public static void render(GuiGraphics graphics) {
        if (flashTicks <= 0) return;

        Minecraft mc = Minecraft.getInstance();

        float alpha;
        if (flashTicks > FADE_DURATION) {
            alpha = 1.0f;
        } else {
            alpha = (float) flashTicks / FADE_DURATION;
            alpha = alpha * alpha;
        }

        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();

        int flashColor = 0x00FFFFFF;
        int color = ((int)(alpha * 255) << 24) | flashColor;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        graphics.fill(0, 0, w, h, color);
        RenderSystem.disableBlend();

        flashTicks--;
    }
}
