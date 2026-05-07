package com.modishmonkee.arsenalofextinction.datagen;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ArsenalOfExtinction.MOD_ID)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Client-side providers
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(output));

        // Server-side providers
        generator.addProvider(event.includeServer(), new ModDatapackEntries(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, lookupProvider));

        ModBlockTagProvider blockTags = new ModBlockTagProvider(output, lookupProvider, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ModItemTagProvider(output, lookupProvider, blockTags.contentsGetter(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ModBlockLootTableProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ModBlockStateProvider(output, event.getExistingFileHelper()));
    }
}
