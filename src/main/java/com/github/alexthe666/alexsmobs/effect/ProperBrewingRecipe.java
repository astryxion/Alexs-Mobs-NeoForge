package com.github.alexthe666.alexsmobs.effect;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.brewing.BrewingRecipe;

/**
 * Custom brewing recipe wrapper; input matching is delegated to {@link Ingredient#test(ItemStack)} (26.1+),
 * which replaces the legacy {@code Ingredient#getItems()} iteration used on older versions.
 */
public class ProperBrewingRecipe extends BrewingRecipe {

    public ProperBrewingRecipe(Ingredient input, Ingredient ingredient, ItemStack output) {
        super(input, ingredient, output);
    }
}
