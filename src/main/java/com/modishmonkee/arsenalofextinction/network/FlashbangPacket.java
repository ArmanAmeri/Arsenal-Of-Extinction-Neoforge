package com.modishmonkee.arsenalofextinction.network;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.modishmonkee.arsenalofextinction.client.FlashbangEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record FlashbangPacket() implements CustomPacketPayload {

    public static final Type<FlashbangPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ArsenalOfExtinction.MOD_ID, "flashbang"));

    public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, FlashbangPacket> STREAM_CODEC =
            StreamCodec.unit(new FlashbangPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(FlashbangPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> Minecraft.getInstance().execute(FlashbangEffect::trigger));
    }
}
