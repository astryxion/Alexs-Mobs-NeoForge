package com.github.alexthe668.alexsmobs.entity.ai;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public interface ILootsChests {

    boolean isLootable(Container inventory);

    boolean shouldLootItem(ItemStack stack);
}
