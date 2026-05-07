package com.modishmonkee.arsenalofextinction.item;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.modishmonkee.arsenalofextinction.item.custom.NukeCallerItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, ArsenalOfExtinction.MOD_ID);

    public static final DeferredHolder<Item, Item> NUKE_CALLER = ITEMS.register("nuke_caller",
            () -> new NukeCallerItem(new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, Item> MOD_ICON = ITEMS.register("mod_icon",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
