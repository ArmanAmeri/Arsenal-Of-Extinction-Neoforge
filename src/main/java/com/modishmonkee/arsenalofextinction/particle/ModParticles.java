package com.modishmonkee.arsenalofextinction.particle;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, ArsenalOfExtinction.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RADIATION =
            PARTICLE_TYPES.register("radiation", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUSHROOM_STEM =
            PARTICLE_TYPES.register("mushroom_stem", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUSHROOM_CAP =
            PARTICLE_TYPES.register("mushroom_cap", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUSHROOM_GROUND_SMOKE =
            PARTICLE_TYPES.register("mushroom_ground_smoke", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUSHROOM_SHOCKWAVE =
            PARTICLE_TYPES.register("mushroom_shockwave", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
