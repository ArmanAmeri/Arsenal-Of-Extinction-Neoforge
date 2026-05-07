package com.modishmonkee.arsenalofextinction.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ScreenShakeEffect {

    private static int shakeTicks = 0;
    private static final Random random = new Random();

    private static final int SHAKE_DURATION = 300;
    private static final int FADE_DURATION = 250;
    private static final float MAX_SHAKE = 0.3f;

    private static float prevX = 0f;
    private static float prevY = 0f;

    public static float offsetX = 0f;
    public static float offsetY = 0f;

    public static void trigger() {
        shakeTicks = SHAKE_DURATION;
        prevX = 0f;
        prevY = 0f;
    }

    public static void update() {
        if (shakeTicks <= 0) {
            offsetX = 0f;
            offsetY = 0f;
            return;
        }

        float intensity;
        if (shakeTicks > FADE_DURATION) {
            intensity = 1.0f;
        } else {
            intensity = (float) shakeTicks / FADE_DURATION;
            intensity = intensity * intensity;
        }

        float shakeAmount = intensity * MAX_SHAKE;

        float newX = (random.nextFloat() - 0.5f) * 2f * shakeAmount;
        float newY = (random.nextFloat() - 0.5f) * 2f * shakeAmount;

        prevX = prevX * 0.3f + newX * 0.7f;
        prevY = prevY * 0.3f + newY * 0.7f;

        offsetX = prevX;
        offsetY = prevY;

        shakeTicks--;
    }
}
