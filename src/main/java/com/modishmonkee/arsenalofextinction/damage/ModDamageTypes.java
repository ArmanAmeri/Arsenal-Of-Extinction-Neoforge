package com.modishmonkee.arsenalofextinction.damage;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class ModDamageTypes {

    public static final ResourceKey<DamageType> NUKE_EXPLOSION = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(ArsenalOfExtinction.MOD_ID, "nuke_explosion")
    );

    public static final ResourceKey<DamageType> RADIATED = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(ArsenalOfExtinction.MOD_ID, "radiated")
    );

    public static DamageSource nukeExplosion(Level level) {
        return new DamageSource(
                level.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(NUKE_EXPLOSION)
        );
    }

    public static DamageSource radiated(Level level) {
        return new DamageSource(
                level.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(RADIATED)
        );
    }
}
