package com.github.alexthe666.alexsmobs.world;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

import java.util.function.Supplier;

public class AMMobSpawnBiomeModifier implements BiomeModifier {
    public static Supplier<? extends MapCodec<? extends BiomeModifier>> SERIALIZER;

    public AMMobSpawnBiomeModifier() {
    }

    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            AMWorldRegistry.addBiomeSpawns(biome, builder);
        }
    }

    public MapCodec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }

    public static MapCodec<AMMobSpawnBiomeModifier> makeCodec() {
        return MapCodec.unit(AMMobSpawnBiomeModifier::new);
    }
}
