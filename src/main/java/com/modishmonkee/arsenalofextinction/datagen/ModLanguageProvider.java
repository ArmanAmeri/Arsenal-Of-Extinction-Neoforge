package com.modishmonkee.arsenalofextinction.datagen;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.modishmonkee.arsenalofextinction.effect.ModEffects;
import com.modishmonkee.arsenalofextinction.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(PackOutput output) {
        super(output, ArsenalOfExtinction.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // Items
        add(ModItems.NUKE_CALLER.get(), "Nuke Caller");
        add(ModItems.MOD_ICON.get(), "Arsenal of Extinction");

        // Effects
        add(ModEffects.RADIATED.get(), "Radiated");

        // Creative tab
        add("creativetab.arsenalofextinction.arsenal_tab", "AOE");

        // Death messages
        add("death.attack.nuke_explosion", "%1$s was obliterated by a nuclear explosion");
        add("death.attack.nuke_explosion.player", "%1$s was obliterated by a nuclear explosion whilst fighting %2$s");
        add("death.attack.radiated", "%1$s died from radiation poisoning");
        add("death.attack.radiated.player", "%1$s died from radiation poisoning whilst fighting %2$s");
    }
}
