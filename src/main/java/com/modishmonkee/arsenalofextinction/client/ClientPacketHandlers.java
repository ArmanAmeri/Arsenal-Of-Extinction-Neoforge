package com.modishmonkee.arsenalofextinction.client;

import com.modishmonkee.arsenalofextinction.client.screen.NukeTargetMapScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandlers {

    public static void openNukeMap(double px, double py, double pz, float yaw) {
        Minecraft.getInstance().setScreen(new NukeTargetMapScreen(px, py, pz, yaw));
    }
}