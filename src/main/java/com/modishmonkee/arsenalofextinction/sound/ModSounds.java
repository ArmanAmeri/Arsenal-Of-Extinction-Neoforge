package com.modishmonkee.arsenalofextinction.sound;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, ArsenalOfExtinction.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> NUKE_FALLING = registerSoundEvent("nuke_falling");
    public static final DeferredHolder<SoundEvent, SoundEvent> NUKE_EXPLOSION = registerSoundEvent("nuke_explosion");
    public static final DeferredHolder<SoundEvent, SoundEvent> NUKE_RUMBLE = registerSoundEvent("nuke_rumble");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(
                ResourceLocation.fromNamespaceAndPath(ArsenalOfExtinction.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
