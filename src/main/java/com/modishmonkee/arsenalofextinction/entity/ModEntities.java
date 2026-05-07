package com.modishmonkee.arsenalofextinction.entity;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.modishmonkee.arsenalofextinction.entity.custom.NukeEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ArsenalOfExtinction.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<NukeEntity>> NUKEBOMB =
            ENTITY_TYPES.register("nukebomb", () -> EntityType.Builder.<NukeEntity>of(
                            NukeEntity::new, MobCategory.MISC)
                    .sized(7.0f, 5.0f)
                    .clientTrackingRange(24)
                    .updateInterval(1)
                    .build(ResourceLocation.fromNamespaceAndPath(ArsenalOfExtinction.MOD_ID, "nukebomb").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
