package com.github.alexthe666.alexsmobs.world;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AMFeatureRegistry {
    public static final DeferredRegister<MapCodec<? extends Feature>> DEF_REG = DeferredRegister.create(Registries.FEATURE_TYPE, AlexsMobs.MODID);

    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<? extends Feature>> LEAFCUTTER_ANTHILL = DEF_REG.register("leafcutter_anthill", () -> FeatureLeafcutterAnthill.CODEC);

}
