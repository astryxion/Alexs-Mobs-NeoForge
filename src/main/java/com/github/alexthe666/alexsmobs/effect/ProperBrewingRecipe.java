package com.github.alexthe666.alexsmobs.effect;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.brewing.BrewingRecipe;

import javax.annotation.Nonnull;

/**
 * Matches potion inputs by item and components, not only by item id.
 */
public class ProperBrewingRecipe extends BrewingRecipe {
    private final ItemStack input;

    public ProperBrewingRecipe(ItemStack input, Ingredient ingredient, ItemStack output) {
        super(Ingredient.of(input.getItem()), ingredient, output);
        this.input = input;
    }

    public ProperBrewingRecipe(Ingredient input, Ingredient ingredient, ItemStack output) {
        super(input, ingredient, output);
        this.input = ItemStack.EMPTY;
    }

    @Override
    public boolean isInput(@Nonnull ItemStack stack) {
        if (!this.input.isEmpty()) {
            return ItemStack.isSameItem(stack, this.input) && ItemStack.isSameItemSameComponents(this.input, stack);
        }
        return super.isInput(stack);
    }
}
