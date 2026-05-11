package com.modishmonkee.arsenalofextinction.client;

import com.modishmonkee.arsenalofextinction.client.screen.NukeTargetMapScreen;
import com.modishmonkee.arsenalofextinction.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandlers {

    public static void openNukeMap(double px, double py, double pz, float yaw) {
        Minecraft.getInstance().setScreen(new NukeTargetMapScreen(px, py, pz, yaw));
    }

    public static void playNukeExplosionSounds() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getSoundManager() == null) return;

        mc.getSoundManager().play(new SimpleSoundInstance(
                ModSounds.NUKE_EXPLOSION.get().getLocation(),
                SoundSource.MASTER,
                1.0f, 0.6f,
                RandomSource.create(),
                false, 0,
                SoundInstance.Attenuation.NONE,
                0.0, 0.0, 0.0,
                true
        ));

        mc.getSoundManager().play(new SimpleSoundInstance(
                ModSounds.NUKE_RUMBLE.get().getLocation(),
                SoundSource.MASTER,
                1.0f, 1.0f,
                RandomSource.create(),
                false, 0,
                SoundInstance.Attenuation.NONE,
                0.0, 0.0, 0.0,
                true
        ));
    }
}