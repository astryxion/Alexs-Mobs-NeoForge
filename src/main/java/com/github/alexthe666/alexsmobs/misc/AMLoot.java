package com.github.alexthe666.alexsmobs.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public final class AMLoot {
    private AMLoot() {
    }

    public static ResourceKey<LootTable> of(Identifier id) {
        return ResourceKey.create(Registries.LOOT_TABLE, id);
    }
}
