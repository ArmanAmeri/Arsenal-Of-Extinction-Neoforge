package com.modishmonkee.arsenalofextinction.datagen;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArsenalOfExtinction.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // No blocks yet — scaffold is here for when you add them
    }
}
