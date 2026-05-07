package com.modishmonkee.arsenalofextinction.network;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.modishmonkee.arsenalofextinction.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record NukeExplosionSoundPacket() implements CustomPacketPayload {

    public static final Type<NukeExplosionSoundPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ArsenalOfExtinction.MOD_ID, "nuke_explosion_sound"));

    public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, NukeExplosionSoundPacket> STREAM_CODEC =
            StreamCodec.unit(new NukeExplosionSoundPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(NukeExplosionSoundPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> Minecraft.getInstance().execute(() -> {
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
        }));
    }
}
