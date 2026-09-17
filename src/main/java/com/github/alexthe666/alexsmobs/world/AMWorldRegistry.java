package com.github.alexthe666.alexsmobs.world;

import net.minecraft.util.valueproviders.UniformInt;

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
            builder.getStructureSettings().getOrAddSpawnOverrides(MobCategory.MONSTER).addSpawn(new MobSpawnSettings.SpawnerData(AMEntityRegistry.MIMICUBE.get(), UniformInt.of(1, 3)), AMConfig.mimicubeSpawnWeight);
        }
        if (AMConfig.soulVultureSpawnOnFossil && structure.is(BuiltinStructures.NETHER_FOSSIL) && AMConfig.soulVultureSpawnWeight > 0) {
            builder.getStructureSettings().getOrAddSpawnOverrides(MobCategory.MONSTER).addSpawn(new MobSpawnSettings.SpawnerData(AMEntityRegistry.SOUL_VULTURE.get(), UniformInt.of(1, 1)), AMConfig.soulVultureSpawnWeight);
        }
        if (AMConfig.restrictSkelewagSpawns && structure.is(BuiltinStructures.SHIPWRECK) && AMConfig.skelewagSpawnWeight > 0) {
            builder.getStructureSettings().getOrAddSpawnOverrides(MobCategory.MONSTER).addSpawn(new MobSpawnSettings.SpawnerData(AMEntityRegistry.SKELEWAG.get(), UniformInt.of(1, 2)), AMConfig.skelewagSpawnWeight);
        }
        if (AMConfig.restrictUnderminerSpawns && structure.is(AMTagRegistry.SPAWNS_UNDERMINERS) && AMConfig.underminerSpawnWeight > 0) {
            builder.getStructureSettings().getOrAddSpawnOverrides(MobCategory.AMBIENT).addSpawn(new MobSpawnSettings.SpawnerData(AMEntityRegistry.UNDERMINER.get(), UniformInt.of(1, 1)), AMConfig.underminerSpawnWeight);
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
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.GRIZZLY_BEAR.get(), MobCategory.CREATURE, AMConfig.grizzlyBearSpawnWeight, UniformInt.of(2, 3));
        }
        if (testBiome(BiomeConfig.roadrunner, biome) && AMConfig.roadrunnerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.ROADRUNNER.get(), MobCategory.CREATURE, AMConfig.roadrunnerSpawnWeight, UniformInt.of(2, 2));
        }
        if (testBiome(BiomeConfig.boneSerpent, biome) && AMConfig.boneSerpentSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.BONE_SERPENT.get(), MobCategory.MONSTER, AMConfig.boneSerpentSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.gazelle, biome) && AMConfig.gazelleSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.GAZELLE.get(), MobCategory.CREATURE, AMConfig.gazelleSpawnWeight, UniformInt.of(7, 7));
        }
        if (testBiome(BiomeConfig.crocodile, biome) && AMConfig.crocodileSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.CROCODILE.get(), MobCategory.CREATURE, AMConfig.crocodileSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.fly, biome) && AMConfig.flySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.FLY.get(), MobCategory.AMBIENT, AMConfig.flySpawnWeight, UniformInt.of(2, 3));
        }
        if (testBiome(BiomeConfig.hummingbird, biome) && AMConfig.hummingbirdSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.HUMMINGBIRD.get(), MobCategory.CREATURE, AMConfig.hummingbirdSpawnWeight, UniformInt.of(7, 7));
        }
        if (testBiome(BiomeConfig.orca, biome) && AMConfig.orcaSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.ORCA.get(), MobCategory.WATER_CREATURE, AMConfig.orcaSpawnWeight, UniformInt.of(3, 4));
        }
        if (testBiome(BiomeConfig.sunbird, biome) && AMConfig.sunbirdSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.SUNBIRD.get(), MobCategory.CREATURE, AMConfig.sunbirdSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.gorilla, biome) && AMConfig.gorillaSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.GORILLA.get(), MobCategory.CREATURE, AMConfig.gorillaSpawnWeight, UniformInt.of(7, 7));
        }
        if (testBiome(BiomeConfig.crimsonMosquito, biome) && AMConfig.crimsonMosquitoSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.CRIMSON_MOSQUITO.get(), MobCategory.MONSTER, AMConfig.crimsonMosquitoSpawnWeight, UniformInt.of(4, 4));
        }
        if (testBiome(BiomeConfig.rattlesnake, biome) && AMConfig.rattlesnakeSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.RATTLESNAKE.get(), MobCategory.CREATURE, AMConfig.rattlesnakeSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.endergrade, biome) && AMConfig.endergradeSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.ENDERGRADE.get(), MobCategory.CREATURE, AMConfig.endergradeSpawnWeight, UniformInt.of(2, 6));
        }
        if (testBiome(BiomeConfig.hammerheadShark, biome) && AMConfig.hammerheadSharkSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.HAMMERHEAD_SHARK.get(), MobCategory.WATER_CREATURE, AMConfig.hammerheadSharkSpawnWeight, UniformInt.of(2, 3));
        }
        if (testBiome(BiomeConfig.lobster, biome) && AMConfig.lobsterSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.LOBSTER.get(), MobCategory.WATER_AMBIENT, AMConfig.lobsterSpawnWeight, UniformInt.of(3, 5));
        }
        if (testBiome(BiomeConfig.komodoDragon, biome) && AMConfig.komodoDragonSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.KOMODO_DRAGON.get(), MobCategory.CREATURE, AMConfig.komodoDragonSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.capuchinMonkey, biome) && AMConfig.capuchinMonkeySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.CAPUCHIN_MONKEY.get(), MobCategory.CREATURE, AMConfig.capuchinMonkeySpawnWeight, UniformInt.of(9, 16));
        }
        if (testBiome(BiomeConfig.caveCentipede, biome) && AMConfig.caveCentipedeSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.CENTIPEDE_HEAD.get(), MobCategory.MONSTER, AMConfig.caveCentipedeSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.warpedToad, biome) && AMConfig.warpedToadSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.WARPED_TOAD.get(), MobCategory.CREATURE, AMConfig.warpedToadSpawnWeight, UniformInt.of(5, 5));
        }
        if (testBiome(BiomeConfig.moose, biome) && AMConfig.mooseSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.MOOSE.get(), MobCategory.CREATURE, AMConfig.mooseSpawnWeight, UniformInt.of(3, 4));
        }
        if (testBiome(BiomeConfig.mimicube, biome) && AMConfig.mimicubeSpawnWeight > 0 && !AMConfig.mimicubeSpawnInEndCity) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.MIMICUBE.get(), MobCategory.MONSTER, AMConfig.mimicubeSpawnWeight, UniformInt.of(1, 3));
        }
        if (testBiome(BiomeConfig.raccoon, biome) && AMConfig.raccoonSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.RACCOON.get(), MobCategory.CREATURE, AMConfig.raccoonSpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.blobfish, biome) && AMConfig.blobfishSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.BLOBFISH.get(), MobCategory.WATER_AMBIENT, AMConfig.blobfishSpawnWeight, UniformInt.of(2, 2));
        }
        if (testBiome(BiomeConfig.seal, biome) && AMConfig.sealSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.SEAL.get(), MobCategory.CREATURE, AMConfig.sealSpawnWeight, UniformInt.of(3, 8));
        }
        if (testBiome(BiomeConfig.cockroach, biome) && AMConfig.cockroachSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.COCKROACH.get(), MobCategory.AMBIENT, AMConfig.cockroachSpawnWeight, UniformInt.of(5, 5));
        }
        if (testBiome(BiomeConfig.shoebill, biome) && AMConfig.shoebillSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.SHOEBILL.get(), MobCategory.CREATURE, AMConfig.shoebillSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.elephant, biome) && AMConfig.elephantSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.ELEPHANT.get(), MobCategory.CREATURE, AMConfig.elephantSpawnWeight, UniformInt.of(3, 5));
        }
        if (testBiome(BiomeConfig.soulVulture, biome) && AMConfig.soulVultureSpawnWeight > 0 && !AMConfig.soulVultureSpawnOnFossil) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.SOUL_VULTURE.get(), MobCategory.MONSTER, AMConfig.soulVultureSpawnWeight, UniformInt.of(2, 3));
        }
        if (testBiome(BiomeConfig.snowLeopard, biome) && AMConfig.snowLeopardSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.SNOW_LEOPARD.get(), MobCategory.CREATURE, AMConfig.snowLeopardSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.spectre, biome) && AMConfig.spectreSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.SPECTRE.get(), MobCategory.CREATURE, AMConfig.spectreSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.crow, biome) && AMConfig.crowSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.CROW.get(), MobCategory.CREATURE, AMConfig.crowSpawnWeight, UniformInt.of(3, 5));
        }
        if (testBiome(BiomeConfig.alligatorSnappingTurtle, biome) && AMConfig.alligatorSnappingTurtleSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.ALLIGATOR_SNAPPING_TURTLE.get(), MobCategory.CREATURE, AMConfig.alligatorSnappingTurtleSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.mungus, biome) && AMConfig.mungusSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.MUNGUS.get(), MobCategory.CREATURE, AMConfig.mungusSpawnWeight, UniformInt.of(3, 5));
        }
        if (testBiome(BiomeConfig.mantisShrimp, biome) && AMConfig.mantisShrimpSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.MANTIS_SHRIMP.get(), MobCategory.WATER_CREATURE, AMConfig.mantisShrimpSpawnWeight, UniformInt.of(1, 4));
        }
        if (testBiome(BiomeConfig.guster, biome) && AMConfig.gusterSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.GUSTER.get(), MobCategory.MONSTER, AMConfig.gusterSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.warpedMosco, biome) && AMConfig.warpedMoscoSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.WARPED_MOSCO.get(), MobCategory.MONSTER, AMConfig.warpedMoscoSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.straddler, biome) && AMConfig.straddlerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.STRADDLER.get(), MobCategory.MONSTER, AMConfig.straddlerSpawnWeight, UniformInt.of(1, 3));
        }
        if (testBiome(BiomeConfig.stradpole, biome) && AMConfig.stradpoleSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.STRADPOLE.get(), MobCategory.WATER_AMBIENT, AMConfig.stradpoleSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.emu, biome) && AMConfig.emuSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.EMU.get(), MobCategory.CREATURE, AMConfig.emuSpawnWeight, UniformInt.of(2, 5));
        }
        if (testBiome(BiomeConfig.platypus, biome) && AMConfig.platypusSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.PLATYPUS.get(), MobCategory.CREATURE, AMConfig.platypusSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.dropbear, biome) && AMConfig.dropbearSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.DROPBEAR.get(), MobCategory.MONSTER, AMConfig.dropbearSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.tasmanianDevil, biome) && AMConfig.tasmanianDevilSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.TASMANIAN_DEVIL.get(), MobCategory.CREATURE, AMConfig.tasmanianDevilSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.kangaroo, biome) && AMConfig.kangarooSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.KANGAROO.get(), MobCategory.CREATURE, AMConfig.kangarooSpawnWeight, UniformInt.of(3, 5));
        }
        if (testBiome(BiomeConfig.cachalot_whale_spawns, biome) && AMConfig.cachalotWhaleSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.CACHALOT_WHALE.get(), MobCategory.WATER_CREATURE, AMConfig.cachalotWhaleSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.enderiophage_spawns, biome) && AMConfig.enderiophageSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.ENDERIOPHAGE.get(), MobCategory.CREATURE, AMConfig.enderiophageSpawnWeight, UniformInt.of(2, 2));
        }
        if (testBiome(BiomeConfig.baldEagle, biome) && AMConfig.baldEagleSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.BALD_EAGLE.get(), MobCategory.CREATURE, AMConfig.baldEagleSpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.tiger, biome) && AMConfig.tigerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.TIGER.get(), MobCategory.CREATURE, AMConfig.tigerSpawnWeight, UniformInt.of(1, 3));
        }
        if (testBiome(BiomeConfig.tarantula_hawk, biome) && AMConfig.tarantulaHawkSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.TARANTULA_HAWK.get(), MobCategory.CREATURE, AMConfig.tarantulaHawkSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.void_worm, biome) && AMConfig.voidWormSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.VOID_WORM.get(), MobCategory.MONSTER, AMConfig.voidWormSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.frilled_shark, biome) && AMConfig.frilledSharkSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.FRILLED_SHARK.get(), MobCategory.WATER_CREATURE, AMConfig.frilledSharkSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.mimic_octopus, biome) && AMConfig.mimicOctopusSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.MIMIC_OCTOPUS.get(), MobCategory.WATER_CREATURE, AMConfig.mimicOctopusSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.seagull, biome) && AMConfig.seagullSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.SEAGULL.get(), MobCategory.CREATURE, AMConfig.seagullSpawnWeight, UniformInt.of(3, 6));
        }
        if (testBiome(BiomeConfig.froststalker, biome) && AMConfig.froststalkerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.FROSTSTALKER.get(), MobCategory.CREATURE, AMConfig.froststalkerSpawnWeight, UniformInt.of(5, 7));
        }
        if (testBiome(BiomeConfig.tusklin, biome) && AMConfig.tusklinSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.TUSKLIN.get(), MobCategory.CREATURE, AMConfig.tusklinSpawnWeight, UniformInt.of(3, 5));
        }
        if (testBiome(BiomeConfig.laviathan, biome) && AMConfig.laviathanSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.LAVIATHAN.get(), MobCategory.CREATURE, AMConfig.laviathanSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.cosmaw, biome) && AMConfig.cosmawSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.COSMAW.get(), MobCategory.CREATURE, AMConfig.cosmawSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.toucan, biome) && AMConfig.toucanSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.TOUCAN.get(), MobCategory.CREATURE, AMConfig.toucanSpawnWeight, UniformInt.of(5, 5));
        }
        if (testBiome(BiomeConfig.maned_wolf, biome) && AMConfig.manedWolfSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.MANED_WOLF.get(), MobCategory.CREATURE, AMConfig.manedWolfSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.anaconda, biome) && AMConfig.anacondaSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.ANACONDA.get(), MobCategory.CREATURE, AMConfig.anacondaSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.anteater, biome) && AMConfig.anteaterSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.ANTEATER.get(), MobCategory.CREATURE, AMConfig.anteaterSpawnWeight, UniformInt.of(1, 3));
        }
        if (testBiome(BiomeConfig.rocky_roller, biome) && AMConfig.rockyRollerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.ROCKY_ROLLER.get(), MobCategory.MONSTER, AMConfig.rockyRollerSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.flutter, biome) && AMConfig.flutterSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.FLUTTER.get(), MobCategory.AMBIENT, AMConfig.flutterSpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.gelada_monkey, biome) && AMConfig.geladaMonkeySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.GELADA_MONKEY.get(), MobCategory.CREATURE, AMConfig.geladaMonkeySpawnWeight, UniformInt.of(9, 16));
        }
        if (testBiome(BiomeConfig.jerboa, biome) && AMConfig.jerboaSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.JERBOA.get(), MobCategory.AMBIENT, AMConfig.jerboaSpawnWeight, UniformInt.of(1, 3));
        }
        if (testBiome(BiomeConfig.terrapin, biome) && AMConfig.terrapinSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.TERRAPIN.get(), MobCategory.WATER_AMBIENT, AMConfig.terrapinSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.comb_jelly, biome) && AMConfig.combJellySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.COMB_JELLY.get(), MobCategory.WATER_AMBIENT, AMConfig.combJellySpawnWeight, UniformInt.of(2, 3));
        }
        if (testBiome(BiomeConfig.cosmic_cod, biome) && AMConfig.cosmicCodSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.COSMIC_COD.get(), MobCategory.AMBIENT, AMConfig.cosmicCodSpawnWeight, UniformInt.of(9, 13));
        }
        if (testBiome(BiomeConfig.bunfungus, biome) && AMConfig.bunfungusSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.BUNFUNGUS.get(), MobCategory.CREATURE, AMConfig.bunfungusSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.bison, biome) && AMConfig.bisonSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.BISON.get(), MobCategory.CREATURE, AMConfig.bisonSpawnWeight, UniformInt.of(6, 10));
        }
        if (testBiome(BiomeConfig.giant_squid, biome) && AMConfig.giantSquidSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.GIANT_SQUID.get(), MobCategory.WATER_CREATURE, AMConfig.giantSquidSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.devils_hole_pupfish, biome) && AMConfig.devilsHolePupfishSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.DEVILS_HOLE_PUPFISH.get(), MobCategory.WATER_AMBIENT, AMConfig.devilsHolePupfishSpawnWeight, UniformInt.of(5, 12));
        }
        if (testBiome(BiomeConfig.catfish, biome) && AMConfig.catfishSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.CATFISH.get(), MobCategory.WATER_AMBIENT, AMConfig.catfishSpawnWeight, UniformInt.of(1, 3));
        }
        if (testBiome(BiomeConfig.flying_fish, biome) && AMConfig.flyingFishSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.FLYING_FISH.get(), MobCategory.WATER_AMBIENT, AMConfig.flyingFishSpawnWeight, UniformInt.of(3, 6));
        }
        if (testBiome(BiomeConfig.skelewag, biome) && AMConfig.skelewagSpawnWeight > 0 && !AMConfig.restrictSkelewagSpawns) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.SKELEWAG.get(), MobCategory.MONSTER, AMConfig.skelewagSpawnWeight, UniformInt.of(2, 3));
        }
        if (testBiome(BiomeConfig.rain_frog, biome) && AMConfig.rainFrogSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.RAIN_FROG.get(), MobCategory.AMBIENT, AMConfig.rainFrogSpawnWeight, UniformInt.of(1, 3));
        }
        if (testBiome(BiomeConfig.potoo, biome) && AMConfig.potooSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.POTOO.get(), MobCategory.CREATURE, AMConfig.potooSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.mudskipper, biome) && AMConfig.mudskipperSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.MUDSKIPPER.get(), MobCategory.CREATURE, AMConfig.mudskipperSpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.rhinoceros, biome) && AMConfig.rhinocerosSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.RHINOCEROS.get(), MobCategory.CREATURE, AMConfig.rhinocerosSpawnWeight, UniformInt.of(3, 5));
        }
        if (testBiome(BiomeConfig.sugar_glider, biome) && AMConfig.sugarGliderSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.SUGAR_GLIDER.get(), MobCategory.CREATURE, AMConfig.sugarGliderSpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.farseer, biome) && AMConfig.farseerSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.FARSEER.get(), MobCategory.MONSTER, AMConfig.farseerSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.skreecher, biome) && AMConfig.skreecherSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.SKREECHER.get(), MobCategory.MONSTER, AMConfig.skreecherSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.underminer, biome) && AMConfig.underminerSpawnWeight > 0 && !AMConfig.restrictUnderminerSpawns) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.UNDERMINER.get(), MobCategory.AMBIENT, AMConfig.underminerSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.murmur, biome) && AMConfig.murmurSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.MURMUR.get(), MobCategory.MONSTER, AMConfig.murmurSpawnWeight, UniformInt.of(1, 1));
        }
        if (testBiome(BiomeConfig.skunk, biome) && AMConfig.skunkSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.SKUNK.get(), MobCategory.CREATURE, AMConfig.skunkSpawnWeight, UniformInt.of(1, 2));
        }
        if (testBiome(BiomeConfig.banana_slug, biome) && AMConfig.bananaSlugSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.BANANA_SLUG.get(), MobCategory.CREATURE, AMConfig.bananaSlugSpawnWeight, UniformInt.of(2, 3));
        }
        if (testBiome(BiomeConfig.blue_jay, biome) && AMConfig.blueJaySpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.BLUE_JAY.get(), MobCategory.CREATURE, AMConfig.blueJaySpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.caiman, biome) && AMConfig.caimanSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.CAIMAN.get(), MobCategory.CREATURE, AMConfig.caimanSpawnWeight, UniformInt.of(2, 4));
        }
        if (testBiome(BiomeConfig.triops, biome) && AMConfig.triopsSpawnWeight > 0) {
            builder.getMobSpawnSettings().addSpawn(AMEntityRegistry.TRIOPS.get(), MobCategory.WATER_AMBIENT, AMConfig.triopsSpawnWeight, UniformInt.of(2, 6));
        }
    }

    public static void addLeafcutterAntSpawns(Holder<Biome> biome, HolderSet<PlacedFeature> features, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (testBiome(BiomeConfig.leafcutter_anthill_spawns, biome) && AMConfig.leafcutterAnthillSpawnChance > 0) {
            features.forEach(feature -> builder.getGenerationSettings().getFeatures(GenerationStep.Decoration.SURFACE_STRUCTURES).add(feature));


        }
    }
}
