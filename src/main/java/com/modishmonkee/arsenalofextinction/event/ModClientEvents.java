package com.modishmonkee.arsenalofextinction.event;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.modishmonkee.arsenalofextinction.entity.ModEntities;
import com.modishmonkee.arsenalofextinction.entity.client.NukeBombModel;
import com.modishmonkee.arsenalofextinction.entity.client.NukeRenderer;
import com.modishmonkee.arsenalofextinction.particle.ModParticles;
import com.modishmonkee.arsenalofextinction.particle.MushroomCloudParticle;
import com.modishmonkee.arsenalofextinction.particle.RadiationParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = ArsenalOfExtinction.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.NUKEBOMB.get(), NukeRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(NukeBombModel.LAYER_LOCATION, NukeBombModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.RADIATION.get(), RadiationParticle.Provider::new);
        event.registerSpriteSet(ModParticles.MUSHROOM_STEM.get(), MushroomCloudParticle.StemProvider::new);
        event.registerSpriteSet(ModParticles.MUSHROOM_CAP.get(), MushroomCloudParticle.CapProvider::new);
        event.registerSpriteSet(ModParticles.MUSHROOM_GROUND_SMOKE.get(), MushroomCloudParticle.GroundSmokeProvider::new);
        event.registerSpriteSet(ModParticles.MUSHROOM_SHOCKWAVE.get(), MushroomCloudParticle.ShockwaveProvider::new);
    }
}
