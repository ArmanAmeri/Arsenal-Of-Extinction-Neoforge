package com.modishmonkee.arsenalofextinction.effect;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, ArsenalOfExtinction.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> RADIATED = MOB_EFFECTS.register("radiated",
            () -> new RadiatedEffect(MobEffectCategory.HARMFUL, 0x39FF14));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
