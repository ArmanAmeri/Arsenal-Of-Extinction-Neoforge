package com.modishmonkee.arsenalofextinction.particle;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EventBusSubscriber(modid = ArsenalOfExtinction.MOD_ID)
public class MushroomCloudManager {

    private static final int    PARTICLES_PER_BURST        = 60;
    private static final int    CLOUD_DURATION_TICKS       = 260;
    private static final double SMOKE_EMIT_UNTIL           = 0.70;
    private static final int    GROUND_SMOKE_MIN_PARTICLES = 5;
    private static final int    GROUND_SMOKE_MAX_PARTICLES = 10;
    private static final int    SHOCKWAVE_PARTICLE_COUNT   = 150;

    private static final List<MushroomCloud> activeClouds = new ArrayList<>();

    public static void spawnCloud(ServerLevel level, double x, double y, double z) {
        activeClouds.add(new MushroomCloud(level.dimension(), x, y, z));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        net.minecraft.server.MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        Iterator<MushroomCloud> it = activeClouds.iterator();
        while (it.hasNext()) {
            MushroomCloud cloud = it.next();
            cloud.tick(server);
            if (cloud.isDone()) it.remove();
        }
    }

    private static class MushroomCloud {
        private final ResourceKey<Level> dimension;
        private final double x, y, z;
        private int age = 0;

        MushroomCloud(ResourceKey<Level> dimension, double x, double y, double z) {
            this.dimension = dimension;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        void tick(net.minecraft.server.MinecraftServer server) {
            ServerLevel level = server.getLevel(dimension);
            if (level == null) return;

            List<ServerPlayer> players = level.players();
            if (players.isEmpty()) return;

            age++;
            double progress = (double) age / CLOUD_DURATION_TICKS;

            // ── Shockwave — single burst on tick 1 only ───────────────────────
            if (age == 1) {
                for (ServerPlayer player : players) {
                    level.sendParticles(player, ModParticles.MUSHROOM_SHOCKWAVE.get(),
                            true, x, y, z, SHOCKWAVE_PARTICLE_COUNT, 0, 0, 0, 0.0);
                }
            }

            // ── Ground smoke — emitted during early phase, tapering off ───────
            if (progress <= SMOKE_EMIT_UNTIL) {
                int smokeCount = (int)(GROUND_SMOKE_MAX_PARTICLES * (1.0 - progress / SMOKE_EMIT_UNTIL))
                        + GROUND_SMOKE_MIN_PARTICLES;
                for (ServerPlayer player : players) {
                    level.sendParticles(player, ModParticles.MUSHROOM_GROUND_SMOKE.get(),
                            true, x, y, z, smokeCount, 0, 0, 0, 0.0);
                }
            }

            // ── Stem only — handles both stem rise and cap curl internally ────
            // No cap particle — the cap phase is baked into MUSHROOM_STEM's lifecycle
            int stemCount = PARTICLES_PER_BURST;
            if (progress > 0.75) {
                double fade = 1.0 - (progress - 0.75) / 0.25;
                stemCount = (int)(PARTICLES_PER_BURST * fade);
            }
            for (ServerPlayer player : players) {
                level.sendParticles(player, ModParticles.MUSHROOM_STEM.get(),
                        true, x, y, z, stemCount, 0, 0, 0, 0.0);
            }
        }

        boolean isDone() {
            return age >= CLOUD_DURATION_TICKS;
        }
    }
}