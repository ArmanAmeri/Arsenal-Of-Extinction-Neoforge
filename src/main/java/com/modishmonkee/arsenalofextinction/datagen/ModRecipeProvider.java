package com.modishmonkee.arsenalofextinction.datagen;

import com.modishmonkee.arsenalofextinction.ArsenalOfExtinction;
import com.modishmonkee.arsenalofextinction.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // No recipes yet — scaffold for future crafting recipes
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.NUKE_CALLER.get())
                .pattern("NRN")
                .pattern("RBR")
                .pattern("NRN")
                .define('B', Items.BEACON)
                .define('N', Items.NETHERITE_INGOT)
                .define('R', Items.REDSTONE_BLOCK)
                .unlockedBy("has_beacon", has(Items.BEACON))
                .save(output);
    }
}
