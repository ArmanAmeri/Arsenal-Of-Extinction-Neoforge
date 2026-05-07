package com.modishmonkee.arsenalofextinction.particle;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.modishmonkee.arsenalofextinction.effect.ModEffects;
import com.modishmonkee.arsenalofextinction.effect.RadiatedEffect;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EventBusSubscriber(modid = ArsenalOfExtinction.MOD_ID)
public class RadiationZoneManager {

    private static final List<RadiationZone> activeZones = new ArrayList<>();
    private static final int PARTICLES_PER_TICK = 60;
    private static final int MAX_RADIATION_LEVEL = 10;

    public static void addZone(ServerLevel level, double x, double y, double z, double radius, int durationTicks) {
        AABB box = new AABB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
        activeZones.add(new RadiationZone(level.dimension(), box, durationTicks));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        var radiatedHolder = net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT
                .getHolder(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
                        "arsenalofextinction", "radiated")).orElseThrow();

        Iterator<RadiationZone> it = activeZones.iterator();
        while (it.hasNext()) {
            RadiationZone zone = it.next();

            ServerLevel level = server.getLevel(zone.dimension);
            if (level == null) {
                zone.tick();
                if (zone.isDone()) it.remove();
                continue;
            }

            for (int i = 0; i < PARTICLES_PER_TICK; i++) {
                double px = zone.box.minX + Math.random() * (zone.box.maxX - zone.box.minX);
                double py = zone.box.minY + Math.random() * (zone.box.maxY - zone.box.minY);
                double pz = zone.box.minZ + Math.random() * (zone.box.maxZ - zone.box.minZ);
                level.sendParticles(ModParticles.RADIATION.get(), px, py, pz, 1, 0, 0, 0, 0.02);
            }

            zone.tickInCloud();

            if (zone.shouldGainLevel()) {
                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, zone.box);
                for (LivingEntity entity : entities) {
                    MobEffectInstance existing = entity.getEffect(radiatedHolder);
                    int currentAmplifier = (existing != null) ? existing.getAmplifier() : -1;
                    int newAmplifier = Math.min(currentAmplifier + 1, MAX_RADIATION_LEVEL - 1);

                    entity.addEffect(new MobEffectInstance(
                            radiatedHolder,
                            RadiatedEffect.EFFECT_DURATION_TICKS,
                            newAmplifier,
                            false,
                            true,
                            true
                    ));
                }
            }

            zone.tick();
            if (zone.isDone()) it.remove();
        }
    }

    public static class RadiationZone {
        private final ResourceKey<Level> dimension;
        private final AABB box;
        private int ticksRemaining;
        private int levelGainTimer = 0;

        public RadiationZone(ResourceKey<Level> dimension, AABB box, int durationTicks) {
            this.dimension = dimension;
            this.box = box;
            this.ticksRemaining = durationTicks;
        }

        public void tickInCloud() { levelGainTimer++; }

        public boolean shouldGainLevel() {
            return levelGainTimer % RadiatedEffect.TICKS_PER_LEVEL_GAIN == 0;
        }

        public void tick() { ticksRemaining--; }

        public boolean isDone() { return ticksRemaining <= 0; }
    }
}
