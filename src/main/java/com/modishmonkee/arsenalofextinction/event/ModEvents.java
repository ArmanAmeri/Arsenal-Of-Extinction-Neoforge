package com.modishmonkee.arsenalofextinction.event;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.modishmonkee.arsenalofextinction.effect.ModEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

@EventBusSubscriber(modid = ArsenalOfExtinction.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onCureEffect(MobEffectEvent.Remove event) {
        if (event.getEffect() != null && event.getEffect().is(ModEffects.RADIATED)) {
            event.setCanceled(true);
        }
    }
}
