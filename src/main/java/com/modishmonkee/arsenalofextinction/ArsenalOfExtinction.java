package com.modishmonkee.arsenalofextinction;

import com.modishmonkee.arsenalofextinction.block.ModBlocks;
import com.modishmonkee.arsenalofextinction.effect.ModEffects;
import com.modishmonkee.arsenalofextinction.entity.ModEntities;
import com.modishmonkee.arsenalofextinction.item.ModCreativeModeTabs;
import com.modishmonkee.arsenalofextinction.item.ModItems;
import com.modishmonkee.arsenalofextinction.network.ModNetwork;
import com.modishmonkee.arsenalofextinction.particle.ModParticles;
import com.modishmonkee.arsenalofextinction.sound.ModSounds;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(ArsenalOfExtinction.MOD_ID)
public class ArsenalOfExtinction {

    public static final String MOD_ID = "arsenalofextinction";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ArsenalOfExtinction(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        ModParticles.register(modEventBus);
        ModEffects.register(modEventBus);
        ModNetwork.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Arsenal of Extinction: Common setup complete.");
    }
}
