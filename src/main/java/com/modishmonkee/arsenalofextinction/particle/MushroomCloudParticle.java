package com.modishmonkee.arsenalofextinction.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MushroomCloudParticle extends TextureSheetParticle {

    // ── Stem / cap ────────────────────────────────────────────────────────────
    private static final double STEM_BASE_OFFSET  = 25.0;
    private static final double STEM_HEIGHT       = 140.0;
    private static final double STEM_RADIUS       = 18.0;
    private static final double STEM_TAPER        = 0.6;
    private static final double CAP_RING_RADIUS   = 28.0;
    private static final double CAP_TUBE_RADIUS   = 15.0;
    private static final double CAP_ROLL_SPEED    = 6.0;
    private static final int    RISE_TICKS        = 30;
    private static final int    TOTAL_LIFETIME    = 120;
    private static final int    CLOUD_FADE_TICKS  = 50;

    // ── Ground smoke ──────────────────────────────────────────────────────────
    private static final double SMOKE_SPAWN_RADIUS      = 5.0;
    private static final double SMOKE_DRIFT_RADIUS      = 100.0;
    private static final double SMOKE_RISE_HEIGHT_START = 10.0;
    private static final double SMOKE_RISE_HEIGHT_END   = 35.0;
    private static final double SMOKE_FADE_START        = 0.75;
    private static final float  SMOKE_SIZE_MIN          = 6.0f;
    private static final float  SMOKE_SIZE_MAX          = 10.0f;
    private static final float  SMOKE_ALPHA_MIN         = 0.96f;
    private static final float  SMOKE_ALPHA_MAX         = 1.0f;
    private static final int    SMOKE_LIFETIME          = 200;
    private static final int    SMOKE_LIFETIME_VAR      = 40;

    // ── Shockwave ring ────────────────────────────────────────────────────────
    private static final double SHOCKWAVE_HEIGHT_FRAC   = 0.65;
    private static final double SHOCKWAVE_START_R       = STEM_RADIUS * 0.1;
    private static final double SHOCKWAVE_END_R         = 200.0;
    private static final double SHOCKWAVE_FADE_START    = 0.80;
    private static final float  SHOCKWAVE_SIZE_START    = 5.0f;
    private static final float  SHOCKWAVE_SIZE_END      = 25.0f;
    private static final float  SHOCKWAVE_ALPHA         = 0.96f;
    private static final int    SHOCKWAVE_LIFETIME      = 150;
    private static final int    SHOCKWAVE_LIFETIME_VAR  = 20;

    // ═════════════════════════════════════════════════════════════════════════

    private enum Kind { CLOUD, GROUND_SMOKE, SHOCKWAVE }

    private final Kind      kind;
    private final double    originX, originY, originZ;
    private final double    angle;
    private final double    stemOffset;
    private final SpriteSet sprites;

    private MushroomCloudParticle(ClientLevel level, double x, double y, double z,
                                  SpriteSet sprites, Kind kind) {
        super(level, x, y, z);
        this.sprites    = sprites;
        this.kind       = kind;
        // Start on first sprite frame — setSpriteFromAge will animate it in tick()
        this.setSpriteFromAge(sprites);

        this.originX    = x;
        this.originY    = y;
        this.originZ    = z;
        this.angle      = random.nextDouble() * 2.0 * Math.PI;
        this.stemOffset = Math.sqrt(random.nextDouble());

        this.hasPhysics = false;
        this.gravity    = 0f;

        switch (kind) {
            case CLOUD -> {
                this.quadSize = 6.0f + random.nextFloat() * 4.0f;
                this.alpha    = 1.0f;
                this.lifetime = TOTAL_LIFETIME + random.nextInt(20) - 10;
            }
            case GROUND_SMOKE -> {
                this.quadSize = SMOKE_SIZE_MIN + random.nextFloat() * (SMOKE_SIZE_MAX - SMOKE_SIZE_MIN);
                this.alpha    = SMOKE_ALPHA_MIN + random.nextFloat() * (SMOKE_ALPHA_MAX - SMOKE_ALPHA_MIN);
                this.lifetime = SMOKE_LIFETIME + random.nextInt(SMOKE_LIFETIME_VAR);
            }
            case SHOCKWAVE -> {
                this.quadSize = SHOCKWAVE_SIZE_START;
                this.alpha    = SHOCKWAVE_ALPHA;
                this.lifetime = SHOCKWAVE_LIFETIME + random.nextInt(SHOCKWAVE_LIFETIME_VAR);
            }
        }

        double[] pos = computePos(0.0);
        this.x  = pos[0]; this.xo = pos[0];
        this.y  = pos[1]; this.yo = pos[1];
        this.z  = pos[2]; this.zo = pos[2];

        updateColour(0.0);
    }

    @Override
    public AABB getBoundingBox() {
        double r   = Math.max(CAP_RING_RADIUS + CAP_TUBE_RADIUS, SHOCKWAVE_END_R) + 10;
        double top = originY + STEM_HEIGHT + CAP_TUBE_RADIUS + 10;
        double bot = originY - STEM_BASE_OFFSET - 10;
        return new AABB(originX - r, bot, originZ - r, originX + r, top, originZ + r);
    }

    @Override
    public void tick() {
        if (removed) return;

        age++;
        if (age >= lifetime) { remove(); return; }

        double t = (double) age / lifetime;

        // Animate through sprite frames based on this particle's own age —
        // stem uses mushroom_0..3, cap uses mushroom_4..7 (separate sprite sets)
        this.setSpriteFromAge(sprites);

        switch (kind) {
            case CLOUD -> {
                if (age > lifetime - CLOUD_FADE_TICKS) {
                    alpha = Math.max(0f, alpha - (1.0f / CLOUD_FADE_TICKS));
                }
            }
            case GROUND_SMOKE -> {
                if (t >= SMOKE_FADE_START) {
                    double fadeFrac = (t - SMOKE_FADE_START) / (1.0 - SMOKE_FADE_START);
                    alpha = Math.max(0f, (float)(0.8 * (1.0 - fadeFrac)));
                }
            }
            case SHOCKWAVE -> {
                if (t >= SHOCKWAVE_FADE_START) {
                    double fadeFrac = (t - SHOCKWAVE_FADE_START) / (1.0 - SHOCKWAVE_FADE_START);
                    alpha = Math.max(0f, (float)(SHOCKWAVE_ALPHA * (1.0 - fadeFrac)));
                }
                quadSize = SHOCKWAVE_SIZE_START + (float)(t * (SHOCKWAVE_SIZE_END - SHOCKWAVE_SIZE_START));
            }
        }

        double[] pos = computePos(t);
        this.xo = pos[0]; this.x = pos[0];
        this.yo = pos[1]; this.y = pos[1];
        this.zo = pos[2]; this.z = pos[2];

        updateColour(t);
    }

    private void updateColour(double t) {
        switch (kind) {
            case CLOUD -> {
                double riseEnd = (double) RISE_TICKS / lifetime;
                if (t < riseEnd) {
                    double p = t / riseEnd;
                    rCol = 1.0f;
                    gCol = (float)(1.0 - p * 0.6);
                    bCol = (float)(0.8 - p * 0.7);
                } else {
                    double p = (t - riseEnd) / (1.0 - riseEnd);
                    rCol = (float)(1.0  - p * 0.7);
                    gCol = (float)(0.3  - p * 0.05);
                    bCol = (float)(0.05 + p * 0.15);
                }
            }
            case GROUND_SMOKE -> {
                double brightness = 0.22 + t * 0.30;
                rCol = (float) Math.min(1.0, brightness);
                gCol = (float) Math.min(1.0, brightness);
                bCol = (float) Math.min(1.0, brightness);
            }
            case SHOCKWAVE -> {
                rCol = 1.0f;
                gCol = 1.0f;
                bCol = 1.0f;
            }
        }
    }

    private double[] computePos(double t) {
        return switch (kind) {
            case CLOUD        -> computeCloudPos(t);
            case GROUND_SMOKE -> computeSmokePos(t);
            case SHOCKWAVE    -> computeShockwavePos(t);
        };
    }

    private double[] computeCloudPos(double t) {
        double riseEnd = (double) RISE_TICKS / lifetime;
        if (t < riseEnd) {
            double p          = t / riseEnd;
            double stemBottom = originY - STEM_BASE_OFFSET;
            double stemTop    = originY + STEM_HEIGHT;
            double py         = stemBottom + p * (stemTop - stemBottom);
            double r          = STEM_RADIUS * (1.0 - p * STEM_TAPER) * stemOffset;
            return new double[]{ originX + r * Math.cos(angle), py, originZ + r * Math.sin(angle) };
        } else {
            double p          = (t - riseEnd) / (1.0 - riseEnd);
            double theta      = Math.PI - p * (Math.PI * CAP_ROLL_SPEED);
            double capCentreY = originY + STEM_HEIGHT;
            double tubeR      = CAP_RING_RADIUS + CAP_TUBE_RADIUS * Math.cos(theta);
            return new double[]{ originX + tubeR * Math.cos(angle),
                    capCentreY + CAP_TUBE_RADIUS * Math.sin(theta),
                    originZ + tubeR * Math.sin(angle) };
        }
    }

    private double[] computeSmokePos(double t) {
        double eased  = 1.0 - Math.pow(1.0 - t, 2.0);
        double spawnR = SMOKE_SPAWN_RADIUS * stemOffset;
        double r      = spawnR + (SMOKE_DRIFT_RADIUS - spawnR) * eased;
        double py     = (originY - STEM_BASE_OFFSET * 0.5)
                + SMOKE_RISE_HEIGHT_START
                + (SMOKE_RISE_HEIGHT_END - SMOKE_RISE_HEIGHT_START) * eased;
        return new double[]{ originX + r * Math.cos(angle), py, originZ + r * Math.sin(angle) };
    }

    private double[] computeShockwavePos(double t) {
        double shockwaveY = originY - STEM_BASE_OFFSET * 0.5 + STEM_HEIGHT * SHOCKWAVE_HEIGHT_FRAC;
        double r          = SHOCKWAVE_START_R + (SHOCKWAVE_END_R - SHOCKWAVE_START_R) * t;
        return new double[]{ originX + r * Math.cos(angle), shockwaveY, originZ + r * Math.sin(angle) };
    }

    @Override
    public ParticleRenderType getRenderType() {
        return (kind == Kind.SHOCKWAVE)
                ? ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
                : ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class StemProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public StemProvider(SpriteSet sprites) { this.sprites = sprites; }
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new MushroomCloudParticle(level, x, y, z, sprites, Kind.CLOUD);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class CapProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public CapProvider(SpriteSet sprites) { this.sprites = sprites; }
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new MushroomCloudParticle(level, x, y, z, sprites, Kind.CLOUD);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class GroundSmokeProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public GroundSmokeProvider(SpriteSet sprites) { this.sprites = sprites; }
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new MushroomCloudParticle(level, x, y, z, sprites, Kind.GROUND_SMOKE);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ShockwaveProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public ShockwaveProvider(SpriteSet sprites) { this.sprites = sprites; }
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new MushroomCloudParticle(level, x, y, z, sprites, Kind.SHOCKWAVE);
        }
    }
}