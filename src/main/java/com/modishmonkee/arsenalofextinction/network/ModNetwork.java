package com.modishmonkee.arsenalofextinction.network;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetwork {

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ModNetwork::onRegisterPayloads);
    }

    private static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(ArsenalOfExtinction.MOD_ID).versioned("1");

        registrar.playToClient(
                FlashbangPacket.TYPE,
                FlashbangPacket.STREAM_CODEC,
                FlashbangPacket::handle
        );
        registrar.playToClient(
                OpenNukeMapPacket.TYPE,
                OpenNukeMapPacket.STREAM_CODEC,
                OpenNukeMapPacket::handle
        );
        registrar.playToClient(
                ScreenShakePacket.TYPE,
                ScreenShakePacket.STREAM_CODEC,
                ScreenShakePacket::handle
        );
        registrar.playToClient(
                NukeExplosionSoundPacket.TYPE,
                NukeExplosionSoundPacket.STREAM_CODEC,
                NukeExplosionSoundPacket::handle
        );
        registrar.playToServer(
                LaunchNukePacket.TYPE,
                LaunchNukePacket.STREAM_CODEC,
                LaunchNukePacket::handle
        );
    }

    public static void sendFlashbang(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new FlashbangPacket());
    }

    public static void sendScreenShake(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new ScreenShakePacket());
    }

    public static void sendNukeExplosionSound(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new NukeExplosionSoundPacket());
    }

    public static void openNukeMap(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player,
                new OpenNukeMapPacket(player.getX(), player.getY(), player.getZ(), player.getYRot()));
    }
}
