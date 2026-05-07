package com.modishmonkee.arsenalofextinction.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RadiationParticle extends TextureSheetParticle {

    protected RadiationParticle(ClientLevel level, double x, double y, double z,
                                double xSpeed, double ySpeed, double zSpeed,
                                SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.pickSprite(sprites);

        this.xd = (Math.random() - 0.5) * 2.0;
        this.yd = (Math.random() - 0.5) * 2.0;
        this.zd = (Math.random() - 0.5) * 2.0;

        // 7 minutes to match radiation zone duration
        this.lifetime = 8400 + random.nextInt(200);

        this.gravity    = 0.0f;
        this.hasPhysics = false;
    }

    @Override
    public void tick() {
        super.tick();

        this.xd += (Math.random() - 0.5) * 0.002;
        this.yd += (Math.random() - 0.5) * 0.001;
        this.zd += (Math.random() - 0.5) * 0.002;

        this.xd = Math.clamp(this.xd, -0.03, 0.03);
        this.yd = Math.clamp(this.yd, -0.02, 0.02);
        this.zd = Math.clamp(this.zd, -0.03, 0.03);

        if (this.age > this.lifetime - 40) {
            this.alpha = Math.max(0f, this.alpha - 0.025f);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new RadiationParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}
