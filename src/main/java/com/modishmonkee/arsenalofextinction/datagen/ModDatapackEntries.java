package com.modishmonkee.arsenalofextinction.datagen;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.modishmonkee.arsenalofextinction.damage.ModDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackEntries extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, ctx -> {
                ctx.register(ModDamageTypes.NUKE_EXPLOSION, new DamageType(
                        "nuke_explosion",
                        DamageScaling.NEVER,
                        0.1f,
                        DamageEffects.HURT
                ));
                ctx.register(ModDamageTypes.RADIATED, new DamageType(
                        "radiated",
                        DamageScaling.NEVER,
                        0.0f,
                        DamageEffects.HURT
                ));
            });

    public ModDatapackEntries(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(ArsenalOfExtinction.MOD_ID));
    }
}
