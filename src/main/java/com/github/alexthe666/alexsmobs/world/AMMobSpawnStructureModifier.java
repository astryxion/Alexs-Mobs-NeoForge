package com.github.alexthe666.alexsmobs.world;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.world.ModifiableStructureInfo;
import net.neoforged.neoforge.common.world.StructureModifier;

import java.util.function.Supplier;

public class AMMobSpawnStructureModifier implements StructureModifier {
    public static Supplier<? extends MapCodec<? extends StructureModifier>> SERIALIZER;

    public AMMobSpawnStructureModifier() {
    }

    @Override
    public void modify(Holder<Structure> structure, Phase phase, ModifiableStructureInfo.StructureInfo.Builder builder) {
        if (phase == StructureModifier.Phase.ADD) {
            AMWorldRegistry.modifyStructure(structure, builder);
        }
    }

    public MapCodec<? extends StructureModifier> codec() {
        return SERIALIZER.get();
    }

    public static MapCodec<AMMobSpawnStructureModifier> makeCodec() {
        return MapCodec.unit(AMMobSpawnStructureModifier::new);
    }
}
