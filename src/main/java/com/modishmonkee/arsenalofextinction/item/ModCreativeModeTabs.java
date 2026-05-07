package com.modishmonkee.arsenalofextinction.item;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArsenalOfExtinction.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ARSENAL_TAB =
            CREATIVE_MODE_TABS.register("arsenal_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.arsenalofextinction.arsenal_tab"))
                    .icon(() -> ModItems.MOD_ICON.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.NUKE_CALLER.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
