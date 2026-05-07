package com.modishmonkee.arsenalofextinction.network;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.modishmonkee.arsenalofextinction.entity.ModEntities;
import com.modishmonkee.arsenalofextinction.entity.custom.NukeEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record LaunchNukePacket(double targetX, double targetY, double targetZ)
        implements CustomPacketPayload {

    private static final int SPAWN_HEIGHT = 300;

    public static final Type<LaunchNukePacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ArsenalOfExtinction.MOD_ID, "launch_nuke"));

    public static final StreamCodec<FriendlyByteBuf, LaunchNukePacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, pkt) -> {
                        buf.writeDouble(pkt.targetX());
                        buf.writeDouble(pkt.targetY());
                        buf.writeDouble(pkt.targetZ());
                    },
                    buf -> new LaunchNukePacket(buf.readDouble(), buf.readDouble(), buf.readDouble())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(LaunchNukePacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player == null) return;

            ServerLevel serverLevel = player.serverLevel();

            double randomAngle = Math.random() * 2 * Math.PI;
            double randomRadius = 50 + Math.random() * 30;

            double spawnX = player.getX() + Math.cos(randomAngle) * randomRadius;
            double spawnY = player.getY() + SPAWN_HEIGHT;
            double spawnZ = player.getZ() + Math.sin(randomAngle) * randomRadius;

            NukeEntity nuke = new NukeEntity(ModEntities.NUKEBOMB.get(), serverLevel);
            nuke.setPos(spawnX, spawnY, spawnZ);
            nuke.setTarget(packet.targetX(), packet.targetY(), packet.targetZ());
            nuke.preloadPathChunks(serverLevel,
                    new Vec3(spawnX, spawnY, spawnZ),
                    new Vec3(packet.targetX(), packet.targetY(), packet.targetZ()));

            serverLevel.addFreshEntity(nuke);
        });
    }
}
