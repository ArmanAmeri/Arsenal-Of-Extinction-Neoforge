package com.modishmonkee.arsenalofextinction.datagen;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ModBlockLootTableProvider extends LootTableProvider {

    public ModBlockLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(ModBlockLoot::new, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.BLOCK)
        ), lookupProvider);
    }

    private static class ModBlockLoot extends BlockLootSubProvider {

        protected ModBlockLoot(HolderLookup.Provider lookupProvider) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
        }

        @Override
        protected void generate() {
            // No blocks yet — scaffold for future blocks
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return com.modishmonkee.arsenalofextinction.block.ModBlocks.BLOCKS
                    .getEntries().stream()
                    .map(DeferredHolder::get)
                    .collect(Collectors.toList());
        }
    }
}
