package com.github.alexthe666.alexsmobs.world;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.config.BiomeConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.common.world.ModifiableStructureInfo;
import net.neoforged.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

// @Mod.EventBusSubscriber removed - use direct registration(modid = AlexsMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AMWorldRegistry {

    public static void modifyStructure(Holder<Structure> structure, ModifiableStructureInfo.StructureInfo.Builder builder) {
        if (AMConfig.mimicubeSpawnInEndCity && structure.is(BuiltinStructures.END_CITY) && AMConfig.mimicubeSpawnWeight > 0) {
            builder.getStructureSettings().getOrAddSpawnOverrides(MobCategory.MONSTER).addSpawn(new MobSpawnSettings.SpawnerData(AMEntityRegistry.MIMICUBE.get(), 1, 3), AMConfig.mimicubeSpawnWeight);
        }
        if (AMConfig.soulVultureSpawnOnFossil && structure.is(BuiltinStructures.NETHER_FOSSIL) && AMConfig.soulVultureSpawnWeight > 0) {
            builder.getStructureSettings().getOrAddSpawnOverrides(MobCategory.MONSTER).addSpawn(new MobSpawnSettings.SpawnerData(AMEntityRegistry.SOUL_VULTURE.get(), 1, 1), AMConfig.soulVultureSpawnWeight);
        }
        if (AMConfig.restrictSkelewagSpawns && structure.is(BuiltinStructures.SHIPWRECK) && AMConfig.skelewagSpawnWeight > 0) {
            builder.getStructureSettings().getOrAddSpawnOverrides(MobCategory.MONSTER).addSpawn(new MobSpawnSettings.SpawnerData(AMEntityRegistry.SKELEWAG.get(), 1, 2), AMConfig.skelewagSpawnWeight);
        }
        if (AMConfig.restrictUnderminerSpawns && structure.is(AMTagRegistry.SPAWNS_UNDERMINERS) && AMConfig.underminerSpawnWeight > 0) {
            builder.getStructureSettings().getOrAddSpawnOverrides(MobCategory.AMBIENT).addSpawn(new MobSpawnSettings.SpawnerData(AMEntityRegistry.UNDERMINER.get(), 1, 1), AMConfig.underminerSpawnWeight);
        }
    }

    private static Identifier getBiomeName(Holder<Biome> biome) {
        return biome.unwrap().map((resourceKey) -> resourceKey.identifier(), (noKey) -> null);
    }

    public static boolean testBiome(Pair<String, SpawnBiomeData> entry, Holder<Biome> biome) {
        boolean result = false;
        try {
            result = BiomeConfig.test(entry, biome, getBiomeName(biome));
        } catch (Exception e) {
            AlexsMobs.LOGGER.warn("could not test biome config for " + entry.getLeft() + ", defaulting to no spawns for mob");
            result = false;
        }
        return result;
    }

    public static void addBiomeSpawns(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (testBiome(BiomeConfig.grizzlyBear, biome) && AMConfig.grizzlyBearSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.grizzlyBearSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.GRIZZLY_BEAR.get(), 2, 3));
        }
        if (testBiome(BiomeConfig.roadrunner, biome) && AMConfig.roadrunnerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.roadrunnerSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.ROADRUNNER.get(), 2, 2));
        }
        if (testBiome(BiomeConfig.boneSerpent, biome) && AMConfig.boneSerpentSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, AMConfig.boneSerpentSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.BONE_SERPENT.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.gazelle, biome) && AMConfig.gazelleSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.gazelleSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.GAZELLE.get(), 7, 7));
        }
        if (testBiome(BiomeConfig.crocodile, biome) && AMConfig.crocodileSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.crocodileSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.CROCODILE.get(), 1, 2));
        }
        if (testBiome(BiomeConfig.fly, biome) && AMConfig.flySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.AMBIENT, AMConfig.flySpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.FLY.get(), 2, 3));
        }
        if (testBiome(BiomeConfig.hummingbird, biome) && AMConfig.hummingbirdSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.hummingbirdSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.HUMMINGBIRD.get(), 7, 7));
        }
        if (testBiome(BiomeConfig.orca, biome) && AMConfig.orcaSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_CREATURE, AMConfig.orcaSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.ORCA.get(), 3, 4));
        }
        if (testBiome(BiomeConfig.sunbird, biome) && AMConfig.sunbirdSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.sunbirdSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.SUNBIRD.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.gorilla, biome) && AMConfig.gorillaSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.gorillaSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.GORILLA.get(), 7, 7));
        }
        if (testBiome(BiomeConfig.crimsonMosquito, biome) && AMConfig.crimsonMosquitoSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, AMConfig.crimsonMosquitoSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.CRIMSON_MOSQUITO.get(), 4, 4));
        }
        if (testBiome(BiomeConfig.rattlesnake, biome) && AMConfig.rattlesnakeSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.rattlesnakeSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.RATTLESNAKE.get(), 1, 2));
        }
        if (testBiome(BiomeConfig.endergrade, biome) && AMConfig.endergradeSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.endergradeSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.ENDERGRADE.get(), 2, 6));
        }
        if (testBiome(BiomeConfig.hammerheadShark, biome) && AMConfig.hammerheadSharkSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_CREATURE, AMConfig.hammerheadSharkSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.HAMMERHEAD_SHARK.get(), 2, 3));
        }
        if (testBiome(BiomeConfig.lobster, biome) && AMConfig.lobsterSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, AMConfig.lobsterSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.LOBSTER.get(), 3, 5));
        }
        if (testBiome(BiomeConfig.komodoDragon, biome) && AMConfig.komodoDragonSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.komodoDragonSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.KOMODO_DRAGON.get(), 1, 2));
        }
        if (testBiome(BiomeConfig.capuchinMonkey, biome) && AMConfig.capuchinMonkeySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.capuchinMonkeySpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.CAPUCHIN_MONKEY.get(), 9, 16));
        }
        if (testBiome(BiomeConfig.caveCentipede, biome) && AMConfig.caveCentipedeSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, AMConfig.caveCentipedeSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.CENTIPEDE_HEAD.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.warpedToad, biome) && AMConfig.warpedToadSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.warpedToadSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.WARPED_TOAD.get(), 5, 5));
        }
        if (testBiome(BiomeConfig.moose, biome) && AMConfig.mooseSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.mooseSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.MOOSE.get(), 3, 4));
        }
        if (testBiome(BiomeConfig.mimicube, biome) && AMConfig.mimicubeSpawnWeight > 0 && !AMConfig.mimicubeSpawnInEndCity) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, AMConfig.mimicubeSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.MIMICUBE.get(), 1, 3));
        }
        if (testBiome(BiomeConfig.raccoon, biome) && AMConfig.raccoonSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.raccoonSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.RACCOON.get(), 2, 4));
        }
        if (testBiome(BiomeConfig.blobfish, biome) && AMConfig.blobfishSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, AMConfig.blobfishSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.BLOBFISH.get(), 2, 2));
        }
        if (testBiome(BiomeConfig.seal, biome) && AMConfig.sealSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.sealSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.SEAL.get(), 3, 8));
        }
        if (testBiome(BiomeConfig.cockroach, biome) && AMConfig.cockroachSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.AMBIENT, AMConfig.cockroachSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.COCKROACH.get(), 5, 5));
        }
        if (testBiome(BiomeConfig.shoebill, biome) && AMConfig.shoebillSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.shoebillSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.SHOEBILL.get(), 1, 2));
        }
        if (testBiome(BiomeConfig.elephant, biome) && AMConfig.elephantSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.elephantSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.ELEPHANT.get(), 3, 5));
        }
        if (testBiome(BiomeConfig.soulVulture, biome) && AMConfig.soulVultureSpawnWeight > 0 && !AMConfig.soulVultureSpawnOnFossil) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, AMConfig.soulVultureSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.SOUL_VULTURE.get(), 2, 3));
        }
        if (testBiome(BiomeConfig.snowLeopard, biome) && AMConfig.snowLeopardSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.snowLeopardSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.SNOW_LEOPARD.get(), 1, 2));
        }
        if (testBiome(BiomeConfig.spectre, biome) && AMConfig.spectreSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.spectreSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.SPECTRE.get(), 1, 2));
        }
        if (testBiome(BiomeConfig.crow, biome) && AMConfig.crowSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.crowSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.CROW.get(), 3, 5));
        }
        if (testBiome(BiomeConfig.alligatorSnappingTurtle, biome) && AMConfig.alligatorSnappingTurtleSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.alligatorSnappingTurtleSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.ALLIGATOR_SNAPPING_TURTLE.get(), 1, 2));
        }
        if (testBiome(BiomeConfig.mungus, biome) && AMConfig.mungusSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.mungusSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.MUNGUS.get(), 3, 5));
        }
        if (testBiome(BiomeConfig.mantisShrimp, biome) && AMConfig.mantisShrimpSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_CREATURE, AMConfig.mantisShrimpSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.MANTIS_SHRIMP.get(), 1, 4));
        }
        if (testBiome(BiomeConfig.guster, biome) && AMConfig.gusterSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, AMConfig.gusterSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.GUSTER.get(), 1, 2));
        }
        if (testBiome(BiomeConfig.warpedMosco, biome) && AMConfig.warpedMoscoSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, AMConfig.warpedMoscoSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.WARPED_MOSCO.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.straddler, biome) && AMConfig.straddlerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, AMConfig.straddlerSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.STRADDLER.get(), 1, 3));
        }
        if (testBiome(BiomeConfig.stradpole, biome) && AMConfig.stradpoleSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, AMConfig.stradpoleSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.STRADPOLE.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.emu, biome) && AMConfig.emuSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.emuSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.EMU.get(), 2, 5));
        }
        if (testBiome(BiomeConfig.platypus, biome) && AMConfig.platypusSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.platypusSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.PLATYPUS.get(), 1, 2));
        }
        if (testBiome(BiomeConfig.dropbear, biome) && AMConfig.dropbearSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, AMConfig.dropbearSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.DROPBEAR.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.tasmanianDevil, biome) && AMConfig.tasmanianDevilSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.tasmanianDevilSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.TASMANIAN_DEVIL.get(), 1, 2));
        }
        if (testBiome(BiomeConfig.kangaroo, biome) && AMConfig.kangarooSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.kangarooSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.KANGAROO.get(), 3, 5));
        }
        if (testBiome(BiomeConfig.cachalot_whale_spawns, biome) && AMConfig.cachalotWhaleSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_CREATURE, AMConfig.cachalotWhaleSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.CACHALOT_WHALE.get(), 1, 2));
        }
        if (testBiome(BiomeConfig.enderiophage_spawns, biome) && AMConfig.enderiophageSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.enderiophageSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.ENDERIOPHAGE.get(), 2, 2));
        }
        if (testBiome(BiomeConfig.baldEagle, biome) && AMConfig.baldEagleSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.baldEagleSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.BALD_EAGLE.get(), 2, 4));
        }
        if (testBiome(BiomeConfig.tiger, biome) && AMConfig.tigerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.tigerSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.TIGER.get(), 1, 3));
        }
        if (testBiome(BiomeConfig.tarantula_hawk, biome) && AMConfig.tarantulaHawkSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.tarantulaHawkSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.TARANTULA_HAWK.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.void_worm, biome) && AMConfig.voidWormSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, AMConfig.voidWormSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.VOID_WORM.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.frilled_shark, biome) && AMConfig.frilledSharkSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_CREATURE, AMConfig.frilledSharkSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.FRILLED_SHARK.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.mimic_octopus, biome) && AMConfig.mimicOctopusSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_CREATURE, AMConfig.mimicOctopusSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.MIMIC_OCTOPUS.get(), 1, 2));
        }
        if (testBiome(BiomeConfig.seagull, biome) && AMConfig.seagullSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.seagullSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.SEAGULL.get(), 3, 6));
        }
        if (testBiome(BiomeConfig.froststalker, biome) && AMConfig.froststalkerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.froststalkerSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.FROSTSTALKER.get(), 5, 7));
        }
        if (testBiome(BiomeConfig.tusklin, biome) && AMConfig.tusklinSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.tusklinSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.TUSKLIN.get(), 3, 5));
        }
        if (testBiome(BiomeConfig.laviathan, biome) && AMConfig.laviathanSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.laviathanSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.LAVIATHAN.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.cosmaw, biome) && AMConfig.cosmawSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.cosmawSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.COSMAW.get(), 1, 2));
        }
        if (testBiome(BiomeConfig.toucan, biome) && AMConfig.toucanSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.toucanSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.TOUCAN.get(), 5, 5));
        }
        if (testBiome(BiomeConfig.maned_wolf, biome) && AMConfig.manedWolfSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.manedWolfSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.MANED_WOLF.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.anaconda, biome) && AMConfig.anacondaSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.anacondaSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.ANACONDA.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.anteater, biome) && AMConfig.anteaterSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.anteaterSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.ANTEATER.get(), 1, 3));
        }
        if (testBiome(BiomeConfig.rocky_roller, biome) && AMConfig.rockyRollerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, AMConfig.rockyRollerSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.ROCKY_ROLLER.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.flutter, biome) && AMConfig.flutterSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.AMBIENT, AMConfig.flutterSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.FLUTTER.get(), 2, 4));
        }
        if (testBiome(BiomeConfig.gelada_monkey, biome) && AMConfig.geladaMonkeySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.geladaMonkeySpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.GELADA_MONKEY.get(), 9, 16));
        }
        if (testBiome(BiomeConfig.jerboa, biome) && AMConfig.jerboaSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.AMBIENT, AMConfig.jerboaSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.JERBOA.get(), 1, 3));
        }
        if (testBiome(BiomeConfig.terrapin, biome) && AMConfig.terrapinSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, AMConfig.terrapinSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.TERRAPIN.get(), 1, 2));
        }
        if (testBiome(BiomeConfig.comb_jelly, biome) && AMConfig.combJellySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, AMConfig.combJellySpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.COMB_JELLY.get(), 2, 3));
        }
        if (testBiome(BiomeConfig.cosmic_cod, biome) && AMConfig.cosmicCodSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.AMBIENT, AMConfig.cosmicCodSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.COSMIC_COD.get(), 9, 13));
        }
        if (testBiome(BiomeConfig.bunfungus, biome) && AMConfig.bunfungusSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.bunfungusSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.BUNFUNGUS.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.bison, biome) && AMConfig.bisonSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.bisonSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.BISON.get(), 6, 10));
        }
        if (testBiome(BiomeConfig.giant_squid, biome) && AMConfig.giantSquidSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_CREATURE, AMConfig.giantSquidSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.GIANT_SQUID.get(), 1, 2));
        }
        if (testBiome(BiomeConfig.devils_hole_pupfish, biome) && AMConfig.devilsHolePupfishSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, AMConfig.devilsHolePupfishSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.DEVILS_HOLE_PUPFISH.get(), 5, 12));
        }
        if (testBiome(BiomeConfig.catfish, biome) && AMConfig.catfishSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, AMConfig.catfishSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.CATFISH.get(), 1, 3));
        }
        if (testBiome(BiomeConfig.flying_fish, biome) && AMConfig.flyingFishSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, AMConfig.flyingFishSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.FLYING_FISH.get(), 3, 6));
        }
        if (testBiome(BiomeConfig.skelewag, biome) && AMConfig.skelewagSpawnWeight > 0 && !AMConfig.restrictSkelewagSpawns) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, AMConfig.skelewagSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.SKELEWAG.get(), 2, 3));
        }
        if (testBiome(BiomeConfig.rain_frog, biome) && AMConfig.rainFrogSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.AMBIENT, AMConfig.rainFrogSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.RAIN_FROG.get(), 1, 3));
        }
        if (testBiome(BiomeConfig.potoo, biome) && AMConfig.potooSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.potooSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.POTOO.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.mudskipper, biome) && AMConfig.mudskipperSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.mudskipperSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.MUDSKIPPER.get(), 2, 4));
        }
        if (testBiome(BiomeConfig.rhinoceros, biome) && AMConfig.rhinocerosSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.rhinocerosSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.RHINOCEROS.get(), 3, 5));
        }
        if (testBiome(BiomeConfig.sugar_glider, biome) && AMConfig.sugarGliderSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.sugarGliderSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.SUGAR_GLIDER.get(), 2, 4));
        }
        if (testBiome(BiomeConfig.farseer, biome) && AMConfig.farseerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, AMConfig.farseerSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.FARSEER.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.skreecher, biome) && AMConfig.skreecherSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, AMConfig.skreecherSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.SKREECHER.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.underminer, biome) && AMConfig.underminerSpawnWeight > 0 && !AMConfig.restrictUnderminerSpawns) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.AMBIENT, AMConfig.underminerSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.UNDERMINER.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.murmur, biome) && AMConfig.murmurSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, AMConfig.murmurSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.MURMUR.get(), 1, 1));
        }
        if (testBiome(BiomeConfig.skunk, biome) && AMConfig.skunkSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.skunkSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.SKUNK.get(), 1, 2));
        }
        if (testBiome(BiomeConfig.banana_slug, biome) && AMConfig.bananaSlugSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.bananaSlugSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.BANANA_SLUG.get(), 2, 3));
        }
        if (testBiome(BiomeConfig.blue_jay, biome) && AMConfig.blueJaySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.blueJaySpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.BLUE_JAY.get(), 2, 4));
        }
        if (testBiome(BiomeConfig.caiman, biome) && AMConfig.caimanSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, AMConfig.caimanSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.CAIMAN.get(), 2, 4));
        }
        if (testBiome(BiomeConfig.triops, biome) && AMConfig.triopsSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, AMConfig.triopsSpawnWeight, new MobSpawnSettings.SpawnerData(AMEntityRegistry.TRIOPS.get(), 2, 6));
        }
    }

    public static void addLeafcutterAntSpawns(Holder<Biome> biome, HolderSet<PlacedFeature> features, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (testBiome(BiomeConfig.leafcutter_anthill_spawns, biome) && AMConfig.leafcutterAnthillSpawnChance > 0) {
            features.forEach(feature -> builder.getGenerationSettings().getFeatures(GenerationStep.Decoration.SURFACE_STRUCTURES).add(feature));


        }
    }
}
