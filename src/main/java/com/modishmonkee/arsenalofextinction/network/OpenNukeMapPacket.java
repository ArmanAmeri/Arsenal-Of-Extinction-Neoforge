package com.modishmonkee.arsenalofextinction.network;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.modishmonkee.arsenalofextinction.client.screen.NukeTargetMapScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenNukeMapPacket(double playerX, double playerY, double playerZ, float playerYaw)
        implements CustomPacketPayload {

    public static final Type<OpenNukeMapPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ArsenalOfExtinction.MOD_ID, "open_nuke_map"));

    public static final StreamCodec<FriendlyByteBuf, OpenNukeMapPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, pkt) -> {
                        buf.writeDouble(pkt.playerX());
                        buf.writeDouble(pkt.playerY());
                        buf.writeDouble(pkt.playerZ());
                        buf.writeFloat(pkt.playerYaw());
                    },
                    buf -> new OpenNukeMapPacket(
                            buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat()
                    )
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenNukeMapPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> openScreen(packet.playerX(), packet.playerY(), packet.playerZ(), packet.playerYaw()));
    }

    @OnlyIn(Dist.CLIENT)
    private static void openScreen(double px, double py, double pz, float yaw) {
        Minecraft.getInstance().setScreen(new NukeTargetMapScreen(px, py, pz, yaw));
    }
}
