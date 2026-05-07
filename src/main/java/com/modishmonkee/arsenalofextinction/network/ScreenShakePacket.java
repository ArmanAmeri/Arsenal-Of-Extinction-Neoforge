package com.modishmonkee.arsenalofextinction.network;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.modishmonkee.arsenalofextinction.client.ScreenShakeEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ScreenShakePacket() implements CustomPacketPayload {

    public static final Type<ScreenShakePacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ArsenalOfExtinction.MOD_ID, "screen_shake"));

    public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, ScreenShakePacket> STREAM_CODEC =
            StreamCodec.unit(new ScreenShakePacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ScreenShakePacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> Minecraft.getInstance().execute(ScreenShakeEffect::trigger));
    }
}
