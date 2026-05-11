package com.modishmonkee.arsenalofextinction.network;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.modishmonkee.arsenalofextinction.client.ClientPacketHandlers;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
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
        ctx.enqueueWork(() -> Minecraft.getInstance().execute(
                ClientPacketHandlers::playNukeExplosionSounds
        ));
    }
}