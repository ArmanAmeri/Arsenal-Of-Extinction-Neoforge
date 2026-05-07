package com.modishmonkee.arsenalofextinction.datagen;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<Block>> blockTags,
                              ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, ArsenalOfExtinction.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // No item tags yet — scaffold for future use
    }
}
