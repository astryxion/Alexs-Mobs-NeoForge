package com.github.alexthe666.alexsmobs.item;

import static com.github.alexthe666.alexsmobs.item.AMArmorMaterials.*;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.*;
import com.github.alexthe666.alexsmobs.misc.AMJukeboxSongs;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.citadel.server.block.LecternBooks;
import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.enchantment.Repairable;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Optional;

public class AMItemRegistry {
    // TODO 1.21: Migrate AMArmorMaterial to new Holder<ArmorMaterial> system
    // public static final AMArmorMaterial ROADRUNNER_ARMOR_MATERIAL = new AMArmorMaterial("roadrunner", 18, new int[]{3, 3, 3, 3}, 20, SoundEvents.ARMOR_EQUIP_TURTLE.value(), 0);
    // public static final AMArmorMaterial CROCODILE_ARMOR_MATERIAL = new AMArmorMaterial("crocodile", 22, new int[]{2, 5, 7, 3}, 25, SoundEvents.ARMOR_EQUIP_TURTLE.value(), 1);
    // public static final AMArmorMaterial CENTIPEDE_ARMOR_MATERIAL = new AMArmorMaterial("centipede", 20, new int[]{6, 6, 6, 6}, 22, SoundEvents.ARMOR_EQUIP_TURTLE.value(), 0.5F);
    // public static final AMArmorMaterial MOOSE_ARMOR_MATERIAL = new AMArmorMaterial("moose", 19, new int[]{3, 3, 3, 3}, 21, SoundEvents.ARMOR_EQUIP_TURTLE.value(), 0.5F);
    // public static final AMArmorMaterial RACCOON_ARMOR_MATERIAL = new AMArmorMaterial("raccoon", 17, new int[]{3, 3, 3, 3}, 21, SoundEvents.ARMOR_EQUIP_LEATHER.value(), 2.5F);
    // public static final AMArmorMaterial SOMBRERO_ARMOR_MATERIAL = new AMArmorMaterial("sombrero", 14, new int[]{2, 2, 2, 2}, 30, SoundEvents.ARMOR_EQUIP_LEATHER.value(), 0.5F);
    // public static final AMArmorMaterial SPIKED_TURTLE_SHELL_ARMOR_MATERIAL = new AMArmorMaterial("spiked_turtle_shell", 35, new int[]{3, 3, 3, 3}, 30, SoundEvents.ARMOR_EQUIP_TURTLE.value(), 1F, 0.2F);
    // public static final AMArmorMaterial FEDORA_ARMOR_MATERIAL = new AMArmorMaterial("fedora", 10, new int[]{2, 2, 2, 2}, 30, SoundEvents.ARMOR_EQUIP_LEATHER.value(), 0.5F);
    // public static final AMArmorMaterial EMU_ARMOR_MATERIAL = new AMArmorMaterial("emu", 9, new int[]{4, 4, 4, 4}, 20, SoundEvents.ARMOR_EQUIP_LEATHER.value(), 0.5F);
    // public static final AMArmorMaterial TARANTULA_HAWK_ELYTRA_MATERIAL = new AMArmorMaterial("tarantula_hawk_elytra", 9, new int[]{3, 3, 3, 3}, 5, SoundEvents.ARMOR_EQUIP_LEATHER.value(), 0);
    // public static final AMArmorMaterial FROSTSTALKER_ARMOR_MATERIAL = new AMArmorMaterial("froststalker", 9, new int[]{3, 3, 3, 3}, 15, SoundEvents.ARMOR_EQUIP_LEATHER.value(), 0.5F);
    // public static final AMArmorMaterial ROCKY_ARMOR_MATERIAL = new AMArmorMaterial("rocky_roller", 20, new int[]{2, 5, 7, 3}, 10, SoundEvents.ARMOR_EQUIP_TURTLE.value(), 0.5F);
    // public static final AMArmorMaterial FLYING_FISH_MATERIAL = new AMArmorMaterial("flying_fish", 9, new int[]{1, 1, 1, 1}, 8, SoundEvents.ARMOR_EQUIP_LEATHER.value(), 0F);
    // public static final AMArmorMaterial NOVELTY_HAT_MATERIAL = new AMArmorMaterial("novelty_hat", 10, new int[]{2, 2, 2, 2}, 30, SoundEvents.ARMOR_EQUIP_LEATHER.value(), 0F);
    // public static final AMArmorMaterial KIMONO_MATERIAL = new AMArmorMaterial("kimono", 8, new int[]{3, 3, 3, 3}, 15, SoundEvents.ARMOR_EQUIP_LEATHER.value(), 0F);

    public static final DeferredRegister.Items DEF_REG = DeferredRegister.createItems(AlexsMobs.MODID);

    static{
        initSpawnEggs();
    }

    public static final DeferredHolder<Item, Item> TAB_ICON = DEF_REG.registerItem("tab_icon", ItemTabIcon::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> ANIMAL_DICTIONARY = DEF_REG.registerItem("animal_dictionary", ItemAnimalDictionary::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Item> BEAR_FUR = DEF_REG.registerItem("bear_fur", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> BEAR_DUST = DEF_REG.registerItem("bear_dust", ItemBearDust::new, () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final DeferredHolder<Item, Item> ROADRUNNER_FEATHER = DEF_REG.registerItem("roadrunner_feather", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> ROADDRUNNER_BOOTS = DEF_REG.registerItem("roadrunner_boots", props -> new ItemModArmor(ROADRUNNER_ARMOR_MATERIAL, ArmorType.BOOTS, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> LAVA_BOTTLE = DEF_REG.registerItem("lava_bottle", Item::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Item> BONE_SERPENT_TOOTH = DEF_REG.registerItem("bone_serpent_tooth", Item::new, () -> new Item.Properties().fireResistant());
    public static final DeferredHolder<Item, Item> GAZELLE_HORN = DEF_REG.registerItem("gazelle_horn", Item::new, () -> new Item.Properties().fireResistant());
    public static final DeferredHolder<Item, Item> CROCODILE_SCUTE = DEF_REG.registerItem("crocodile_scute", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> CROCODILE_CHESTPLATE = DEF_REG.registerItem("crocodile_chestplate", props -> new ItemModArmor(CROCODILE_ARMOR_MATERIAL, ArmorType.CHESTPLATE, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> MAGGOT = DEF_REG.registerItem("maggot", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2F).build()));
    public static final DeferredHolder<Item, Item> BANANA = DEF_REG.registerItem("banana", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.3F).build()));
    public static final DeferredHolder<Item, Item> ANCIENT_DART = DEF_REG.registerItem("ancient_dart", Item::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredHolder<Item, Item> HALO = DEF_REG.registerItem("halo", ItemInventoryOnly::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> BLOOD_SAC = DEF_REG.registerItem("blood_sac", Item::new, Item.Properties::new);

    public static final DeferredHolder<Item, Item> MOSQUITO_PROBOSCIS = DEF_REG.registerItem("mosquito_proboscis", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> BLOOD_SPRAYER = DEF_REG.registerItem("blood_sprayer", ItemBloodSprayer::new, () -> new Item.Properties().durability(100));
    public static final DeferredHolder<Item, Item> RATTLESNAKE_RATTLE = DEF_REG.registerItem("rattlesnake_rattle", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> CHORUS_ON_A_STICK = DEF_REG.registerItem("chorus_on_a_stick", Item::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Item> SHARK_TOOTH = DEF_REG.registerItem("shark_tooth", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> SHARK_TOOTH_ARROW = DEF_REG.registerItem("shark_tooth_arrow", ItemModArrow::new, () -> new Item.Properties());
    public static final DeferredHolder<Item, Item> LOBSTER_TAIL = DEF_REG.registerItem("lobster_tail", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.4F).build()));
    public static final DeferredHolder<Item, Item> COOKED_LOBSTER_TAIL = DEF_REG.registerItem("cooked_lobster_tail", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.65F).build()));
    public static final DeferredHolder<Item, Item> LOBSTER_BUCKET = DEF_REG.registerItem("lobster_bucket", props -> new ItemModFishBucket(AMEntityRegistry.LOBSTER, Fluids.WATER, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> KOMODO_SPIT = DEF_REG.registerItem("komodo_spit", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> KOMODO_SPIT_BOTTLE = DEF_REG.registerItem("komodo_spit_bottle", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> POISON_BOTTLE = DEF_REG.registerItem("poison_bottle", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> SOPA_DE_MACACO = DEF_REG.registerItem("sopa_de_macaco", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.4F).build()).stacksTo(1));
    public static final DeferredHolder<Item, Item> CENTIPEDE_LEG = DEF_REG.registerItem("centipede_leg", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> CENTIPEDE_LEGGINGS = DEF_REG.registerItem("centipede_leggings", props -> new ItemModArmor(CENTIPEDE_ARMOR_MATERIAL, ArmorType.LEGGINGS, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> MOSQUITO_LARVA = DEF_REG.registerItem("mosquito_larva", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> MOOSE_ANTLER = DEF_REG.registerItem("moose_antler", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> MOOSE_HEADGEAR = DEF_REG.registerItem("moose_headgear", props -> new ItemModArmor(MOOSE_ARMOR_MATERIAL, ArmorType.HELMET, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> MOOSE_RIBS = DEF_REG.registerItem("moose_ribs", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.6F).build()));
    public static final DeferredHolder<Item, Item> COOKED_MOOSE_RIBS = DEF_REG.registerItem("cooked_moose_ribs", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.85F).build()));
    public static final DeferredHolder<Item, Item> MIMICREAM = DEF_REG.registerItem("mimicream", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> RACCOON_TAIL = DEF_REG.registerItem("raccoon_tail", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> FRONTIER_CAP = DEF_REG.registerItem("frontier_cap", props -> new ItemModArmor(RACCOON_ARMOR_MATERIAL, ArmorType.HELMET, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> BLOBFISH = DEF_REG.registerItem("blobfish", Item::new, () -> foodWithChanceEffect(new FoodProperties.Builder().nutrition(3).saturationModifier(0.4F).build(), MobEffects.POISON, 120, 0, 1.0F));
    public static final DeferredHolder<Item, Item> BLOBFISH_BUCKET = DEF_REG.registerItem("blobfish_bucket", props -> new ItemModFishBucket(AMEntityRegistry.BLOBFISH, Fluids.WATER, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> FISH_OIL = DEF_REG.registerItem("fish_oil", ItemFishOil::new, () -> new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).food(new FoodProperties.Builder().nutrition(0).saturationModifier(0.2F).build()));
    public static final DeferredHolder<Item, Item> MARACA = DEF_REG.registerItem("maraca", ItemMaraca::new, () -> new Item.Properties());
    public static final DeferredHolder<Item, Item> SOMBRERO = DEF_REG.registerItem("sombrero", props -> new ItemModArmor(SOMBRERO_ARMOR_MATERIAL, ArmorType.HELMET, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> COCKROACH_WING_FRAGMENT = DEF_REG.registerItem("cockroach_wing_fragment", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> COCKROACH_WING = DEF_REG.registerItem("cockroach_wing", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> COCKROACH_OOTHECA = DEF_REG.registerItem("cockroach_ootheca", ItemAnimalEgg::new, () -> new Item.Properties());
    public static final DeferredHolder<Item, Item> ACACIA_BLOSSOM = DEF_REG.registerItem("acacia_blossom", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> SOUL_HEART = DEF_REG.registerItem("soul_heart", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> SPIKED_SCUTE = DEF_REG.registerItem("spiked_scute", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> SPIKED_TURTLE_SHELL = DEF_REG.registerItem("spiked_turtle_shell", props -> new ItemModArmor(SPIKED_TURTLE_SHELL_ARMOR_MATERIAL, ArmorType.HELMET, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> SHRIMP_FRIED_RICE = DEF_REG.registerItem("shrimp_fried_rice", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(1F).build()));
    public static final DeferredHolder<Item, Item> GUSTER_EYE = DEF_REG.registerItem("guster_eye", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> POCKET_SAND = DEF_REG.registerItem("pocket_sand", ItemPocketSand::new, () -> new Item.Properties().durability(220));
    public static final DeferredHolder<Item, Item> WARPED_MUSCLE = DEF_REG.registerItem("warped_muscle", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> HEMOLYMPH_SAC = DEF_REG.registerItem("hemolymph_sac", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> HEMOLYMPH_BLASTER = DEF_REG.registerItem("hemolymph_blaster", ItemHemolymphBlaster::new, () -> new Item.Properties().durability(150));
    public static final DeferredHolder<Item, Item> WARPED_MIXTURE = DEF_REG.registerItem("warped_mixture", Item::new, () -> new Item.Properties().rarity(Rarity.RARE).stacksTo(1).craftRemainder(Items.GLASS_BOTTLE));
    public static final DeferredHolder<Item, Item> STRADDLITE = DEF_REG.registerItem("straddlite", Item::new, () -> new Item.Properties().fireResistant());
    public static final DeferredHolder<Item, Item> STRADPOLE_BUCKET = DEF_REG.registerItem("stradpole_bucket", props -> new ItemModFishBucket(AMEntityRegistry.STRADPOLE, Fluids.LAVA, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> STRADDLEBOARD = DEF_REG.registerItem("straddleboard", ItemStraddleboard::new, () -> new Item.Properties().fireResistant().durability(220));
    public static final DeferredHolder<Item, Item> EMU_EGG = DEF_REG.registerItem("emu_egg", ItemAnimalEgg::new, () -> new Item.Properties().stacksTo(8));
    public static final DeferredHolder<Item, Item> BOILED_EMU_EGG = DEF_REG.registerItem("boiled_emu_egg", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(1F).build()));
    public static final DeferredHolder<Item, Item> EMU_FEATHER = DEF_REG.registerItem("emu_feather", Item::new, () -> new Item.Properties().fireResistant());
    public static final DeferredHolder<Item, Item> EMU_LEGGINGS = DEF_REG.registerItem("emu_leggings", props -> new ItemModArmor(EMU_ARMOR_MATERIAL, ArmorType.LEGGINGS, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> PLATYPUS_BUCKET = DEF_REG.registerItem("platypus_bucket", props -> new ItemModFishBucket(AMEntityRegistry.PLATYPUS, Fluids.WATER, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> FEDORA = DEF_REG.registerItem("fedora", props -> new ItemModArmor(FEDORA_ARMOR_MATERIAL, ArmorType.HELMET, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> DROPBEAR_CLAW = DEF_REG.registerItem("dropbear_claw", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> KANGAROO_MEAT = DEF_REG.registerItem("kangaroo_meat", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6F).build()));
    public static final DeferredHolder<Item, Item> COOKED_KANGAROO_MEAT = DEF_REG.registerItem("cooked_kangaroo_meat", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.85F).build()));
    public static final DeferredHolder<Item, Item> KANGAROO_HIDE = DEF_REG.registerItem("kangaroo_hide", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> KANGAROO_BURGER = DEF_REG.registerItem("kangaroo_burger", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(12).saturationModifier(1F).build()));
    public static final DeferredHolder<Item, Item> AMBERGRIS = DEF_REG.registerItem("ambergris", props -> new ItemFuel(props, 12800), Item.Properties::new);
    public static final DeferredHolder<Item, Item> CACHALOT_WHALE_TOOTH = DEF_REG.registerItem("cachalot_whale_tooth", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> ECHOLOCATOR = DEF_REG.registerItem("echolocator", props -> new ItemEcholocator(props, ItemEcholocator.EchoType.ECHOLOCATION), () -> new Item.Properties().durability(100));
    public static final DeferredHolder<Item, Item> ENDOLOCATOR = DEF_REG.registerItem("endolocator", props -> new ItemEcholocator(props, ItemEcholocator.EchoType.ENDER), () -> new Item.Properties().durability(25));
    public static final DeferredHolder<Item, Item> GONGYLIDIA = DEF_REG.registerItem("gongylidia", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(1.2F).build()));
    public static final DeferredHolder<Item, Item> LEAFCUTTER_ANT_PUPA = DEF_REG.registerItem("leafcutter_ant_pupa", ItemLeafcutterPupa::new, () -> new Item.Properties());
    public static final DeferredHolder<Item, Item> ENDERIOPHAGE_ROCKET = DEF_REG.registerItem("enderiophage_rocket", ItemEnderiophageRocket::new, () -> new Item.Properties());
    public static final DeferredHolder<Item, Item> FALCONRY_GLOVE_INVENTORY = DEF_REG.registerItem("falconry_glove_inventory", ItemInventoryOnly::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> FALCONRY_GLOVE_HAND = DEF_REG.registerItem("falconry_glove_hand", ItemInventoryOnly::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> FALCONRY_GLOVE = DEF_REG.registerItem("falconry_glove", ItemFalconryGlove::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Item> FALCONRY_HOOD = DEF_REG.registerItem("falconry_hood", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> TARANTULA_HAWK_WING_FRAGMENT = DEF_REG.registerItem("tarantula_hawk_wing_fragment", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> TARANTULA_HAWK_WING = DEF_REG.registerItem("tarantula_hawk_wing", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> TARANTULA_HAWK_ELYTRA = DEF_REG.registerItem("tarantula_hawk_elytra", ItemTarantulaHawkElytra::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final DeferredHolder<Item, Item> MYSTERIOUS_WORM = DEF_REG.registerItem("mysterious_worm", ItemMysteriousWorm::new, () -> new Item.Properties().rarity(Rarity.RARE));
    public static final DeferredHolder<Item, Item> VOID_WORM_MANDIBLE = DEF_REG.registerItem("void_worm_mandible", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> VOID_WORM_EYE = DEF_REG.registerItem("void_worm_eye", Item::new, () -> new Item.Properties().rarity(Rarity.RARE));
    public static final DeferredHolder<Item, Item> DIMENSIONAL_CARVER = DEF_REG.registerItem("dimensional_carver", ItemDimensionalCarver::new, () -> new Item.Properties().durability(20).rarity(Rarity.EPIC));
    public static final DeferredHolder<Item, Item> SHATTERED_DIMENSIONAL_CARVER = DEF_REG.registerItem("shattered_dimensional_carver", ItemShatteredDimensionalCarver::new, () -> new Item.Properties().durability(4).rarity(Rarity.RARE));
    public static final DeferredHolder<Item, Item> SERRATED_SHARK_TOOTH = DEF_REG.registerItem("serrated_shark_tooth", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> FRILLED_SHARK_BUCKET = DEF_REG.registerItem("frilled_shark_bucket", props -> new ItemModFishBucket(AMEntityRegistry.FRILLED_SHARK, Fluids.WATER, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> SHIELD_OF_THE_DEEP = DEF_REG.registerItem("shield_of_the_deep", ItemShieldOfTheDeep::new, () -> new Item.Properties()
        .durability(400)
        .rarity(Rarity.UNCOMMON)
        .component(DataComponents.REPAIRABLE, new Repairable(HolderSet.direct(BuiltInRegistries.ITEM.wrapAsHolder(SERRATED_SHARK_TOOTH.get()))))
        .delayedComponent(DataComponents.BLOCKS_ATTACKS, context -> new BlocksAttacks(
            0.25F,
            1.0F,
            List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
            new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
            Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
            Optional.of(SoundEvents.SHIELD_BLOCK),
            Optional.of(SoundEvents.SHIELD_BREAK))));
    public static final DeferredHolder<Item, Item> MIMIC_OCTOPUS_BUCKET = DEF_REG.registerItem("mimic_octopus_bucket", props -> new ItemModFishBucket(AMEntityRegistry.MIMIC_OCTOPUS, Fluids.WATER, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> FROSTSTALKER_HORN = DEF_REG.registerItem("froststalker_horn", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> FROSTSTALKER_HELMET = DEF_REG.registerItem("froststalker_helmet", props -> new ItemModArmor(FROSTSTALKER_ARMOR_MATERIAL, ArmorType.HELMET, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> PIGSHOES = DEF_REG.registerItem("pigshoes", ItemPigshoes::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Item> STRADDLE_HELMET = DEF_REG.registerItem("straddle_helmet", Item::new, () -> new Item.Properties().fireResistant());
    public static final DeferredHolder<Item, Item> STRADDLE_SADDLE = DEF_REG.registerItem("straddle_saddle", Item::new, () -> new Item.Properties().fireResistant());
    public static final DeferredHolder<Item, Item> COSMIC_COD = DEF_REG.registerItem("cosmic_cod", Item::new, () -> foodWithChanceEffect(new FoodProperties.Builder().nutrition(6).saturationModifier(0.3F).build(), Holder.direct(AMEffectRegistry.ENDER_FLU.get()), 12000, 0, 0.15F));
    public static final DeferredHolder<Item, Item> SHED_SNAKE_SKIN = DEF_REG.registerItem("shed_snake_skin", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> VINE_LASSO_INVENTORY = DEF_REG.registerItem("vine_lasso_inventory", ItemInventoryOnly::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> VINE_LASSO_HAND = DEF_REG.registerItem("vine_lasso_hand", ItemInventoryOnly::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> VINE_LASSO = DEF_REG.registerItem("vine_lasso", ItemVineLasso::new, () -> new Item.Properties().stacksTo(1));
    public static final DeferredHolder<Item, Item> ROCKY_SHELL = DEF_REG.registerItem("rocky_shell", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> ROCKY_CHESTPLATE = DEF_REG.registerItem("rocky_chestplate", props -> new ItemModArmor(ROCKY_ARMOR_MATERIAL, ArmorType.CHESTPLATE, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> POTTED_FLUTTER = DEF_REG.registerItem("potted_flutter", ItemFlutterPot::new, () -> new Item.Properties());
    public static final DeferredHolder<Item, Item> TERRAPIN_BUCKET = DEF_REG.registerItem("terrapin_bucket", props -> new ItemModFishBucket(AMEntityRegistry.TERRAPIN, Fluids.WATER, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> COMB_JELLY_BUCKET = DEF_REG.registerItem("comb_jelly_bucket", props -> new ItemModFishBucket(AMEntityRegistry.COMB_JELLY, Fluids.WATER, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> RAINBOW_JELLY = DEF_REG.registerItem("rainbow_jelly", ItemRainbowJelly::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2F).build()));
    public static final DeferredHolder<Item, Item> COSMIC_COD_BUCKET = DEF_REG.registerItem("cosmic_cod_bucket", ItemCosmicCodBucket::new, () -> new Item.Properties());
    public static final DeferredHolder<Item, Item> MUNGAL_SPORES = DEF_REG.registerItem("mungal_spores", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> BISON_FUR = DEF_REG.registerItem("bison_fur", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> LOST_TENTACLE = DEF_REG.registerItem("lost_tentacle", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> SQUID_GRAPPLE = DEF_REG.registerItem("squid_grapple", ItemSquidGrapple::new, () -> new Item.Properties().durability(450));
    public static final DeferredHolder<Item, Item> DEVILS_HOLE_PUPFISH_BUCKET = DEF_REG.registerItem("devils_hole_pupfish_bucket", props -> new ItemModFishBucket(AMEntityRegistry.DEVILS_HOLE_PUPFISH, Fluids.WATER, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> PUPFISH_LOCATOR = DEF_REG.registerItem("pupfish_locator", props -> new ItemEcholocator(props, ItemEcholocator.EchoType.PUPFISH), () -> new Item.Properties().durability(200));
    public static final DeferredHolder<Item, Item> SMALL_CATFISH_BUCKET = DEF_REG.registerItem("small_catfish_bucket", props -> new ItemModFishBucket(AMEntityRegistry.CATFISH, Fluids.WATER, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> MEDIUM_CATFISH_BUCKET = DEF_REG.registerItem("medium_catfish_bucket", props -> new ItemModFishBucket(AMEntityRegistry.CATFISH, Fluids.WATER, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> LARGE_CATFISH_BUCKET = DEF_REG.registerItem("large_catfish_bucket", props -> new ItemModFishBucket(AMEntityRegistry.CATFISH, Fluids.WATER, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> RAW_CATFISH = DEF_REG.registerItem("raw_catfish", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build()));
    public static final DeferredHolder<Item, Item> COOKED_CATFISH = DEF_REG.registerItem("cooked_catfish", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.5F).build()));
    public static final DeferredHolder<Item, Item> FLYING_FISH = DEF_REG.registerItem("flying_fish", Item::new, () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.4F).build()));
    public static final DeferredHolder<Item, Item> FLYING_FISH_BOOTS = DEF_REG.registerItem("flying_fish_boots", props -> new ItemModArmor(FLYING_FISH_MATERIAL, ArmorType.BOOTS, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> FLYING_FISH_BUCKET = DEF_REG.registerItem("flying_fish_bucket", props -> new ItemModFishBucket(AMEntityRegistry.FLYING_FISH, Fluids.WATER, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> FISH_BONES = DEF_REG.registerItem("fish_bones", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> SKELEWAG_SWORD_INVENTORY = DEF_REG.registerItem("skelewag_sword_inventory", ItemInventoryOnly::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> SKELEWAG_SWORD_HAND = DEF_REG.registerItem("skelewag_sword_hand", ItemInventoryOnly::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> SKELEWAG_SWORD = DEF_REG.registerItem("skelewag_sword", ItemSkelewagSword::new, () -> new Item.Properties().stacksTo(1).durability(430));
    public static final DeferredHolder<Item, Item> NOVELTY_HAT = DEF_REG.registerItem("novelty_hat", props -> new ItemModArmor(NOVELTY_HAT_MATERIAL, ArmorType.HELMET, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> MUDSKIPPER_BUCKET = DEF_REG.registerItem("mudskipper_bucket", props -> new ItemModFishBucket(AMEntityRegistry.MUDSKIPPER, Fluids.WATER, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> FARSEER_ARM = DEF_REG.registerItem("farseer_arm", Item::new, () -> new Item.Properties().rarity(Rarity.RARE));
    public static final DeferredHolder<Item, Item> SKREECHER_SOUL = DEF_REG.registerItem("skreecher_soul", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> GHOSTLY_PICKAXE = DEF_REG.registerItem("ghostly_pickaxe", ItemGhostlyPickaxe::new, () -> new Item.Properties());
    public static final DeferredHolder<Item, Item> ELASTIC_TENDON = DEF_REG.registerItem("elastic_tendon", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> TENDON_WHIP = DEF_REG.registerItem("tendon_whip", ItemTendonWhip::new, () -> new Item.Properties());
    public static final DeferredHolder<Item, Item> UNSETTLING_KIMONO = DEF_REG.registerItem("unsettling_kimono", props -> new ItemModArmor(KIMONO_MATERIAL, ArmorType.CHESTPLATE, props), Item.Properties::new);
    public static final DeferredHolder<Item, Item> STINK_BOTTLE = DEF_REG.registerItem("stink_bottle", props -> new ItemStinkBottle(AMBlockRegistry.SKUNK_SPRAY, props), () -> new Item.Properties().stacksTo(16));

    public static final DeferredHolder<Item, Item> STINK_RAY_HAND = DEF_REG.registerItem("stink_ray_hand", ItemInventoryOnly::new, Item.Properties::new);

    public static final DeferredHolder<Item, Item> STINK_RAY_INVENTORY = DEF_REG.registerItem("stink_ray_inventory", ItemInventoryOnly::new, Item.Properties::new);

    public static final DeferredHolder<Item, Item> STINK_RAY_EMPTY_HAND = DEF_REG.registerItem("stink_ray_empty_hand", ItemInventoryOnly::new, Item.Properties::new);

    public static final DeferredHolder<Item, Item> STINK_RAY_EMPTY_INVENTORY = DEF_REG.registerItem("stink_ray_empty_inventory", ItemInventoryOnly::new, Item.Properties::new);

    public static final DeferredHolder<Item, Item> STINK_RAY = DEF_REG.registerItem("stink_ray", ItemStinkRay::new, () -> new Item.Properties().durability(5));
    public static final DeferredHolder<Item, Item> BANANA_SLUG_SLIME = DEF_REG.registerItem("banana_slug_slime", Item::new, Item.Properties::new);
    public static final DeferredHolder<Item, Item> MOSQUITO_REPELLENT_STEW = DEF_REG.registerItem("mosquito_repellent_stew", Item::new, () -> foodWithChanceEffect(new FoodProperties.Builder().nutrition(4).alwaysEdible().saturationModifier(0.3F).build(), Holder.direct(AMEffectRegistry.MOSQUITO_REPELLENT.get()), 24000, 0, 1.0F).stacksTo(1));
    public static final DeferredHolder<Item, Item> TRIOPS_BUCKET = DEF_REG.registerItem("triops_bucket", props -> new ItemModFishBucket(AMEntityRegistry.TRIOPS, Fluids.WATER, props), Item.Properties::new);

    public static final DeferredHolder<Item, Item> MUSIC_DISC_THIME = DEF_REG.registerItem("music_disc_thime", Item::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(AMJukeboxSongs.THIME));
    public static final DeferredHolder<Item, Item> MUSIC_DISC_DAZE = DEF_REG.registerItem("music_disc_daze", Item::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(AMJukeboxSongs.DAZE));

    public static void initSpawnEggs() {
        DEF_REG.registerItem("spawn_egg_grizzly_bear", props -> new AMSpawnEggItem(props, 0X693A2C, 0X976144), () -> new Item.Properties().spawnEgg(AMEntityRegistry.GRIZZLY_BEAR.get()));
        DEF_REG.registerItem("spawn_egg_roadrunner", props -> new AMSpawnEggItem(props, 0X3A2E26, 0XFBE9CE), () -> new Item.Properties().spawnEgg(AMEntityRegistry.ROADRUNNER.get()));
        DEF_REG.registerItem("spawn_egg_bone_serpent", props -> new AMSpawnEggItem(props, 0XE5D9C4, 0XFF6038), () -> new Item.Properties().spawnEgg(AMEntityRegistry.BONE_SERPENT.get()));
        DEF_REG.registerItem("spawn_egg_gazelle", props -> new AMSpawnEggItem(props, 0XDDA675, 0X2C2925), () -> new Item.Properties().spawnEgg(AMEntityRegistry.GAZELLE.get()));
        DEF_REG.registerItem("spawn_egg_crocodile", props -> new AMSpawnEggItem(props, 0X738940, 0XA6A15E), () -> new Item.Properties().spawnEgg(AMEntityRegistry.CROCODILE.get()));
        DEF_REG.registerItem("spawn_egg_fly", props -> new AMSpawnEggItem(props, 0X464241, 0X892E2E), () -> new Item.Properties().spawnEgg(AMEntityRegistry.FLY.get()));
        DEF_REG.registerItem("spawn_egg_hummingbird", props -> new AMSpawnEggItem(props, 0X325E7F, 0X44A75F), () -> new Item.Properties().spawnEgg(AMEntityRegistry.HUMMINGBIRD.get()));
        DEF_REG.registerItem("spawn_egg_orca", props -> new AMSpawnEggItem(props, 0X2C2C2C, 0XD6D8E4), () -> new Item.Properties().spawnEgg(AMEntityRegistry.ORCA.get()));
        DEF_REG.registerItem("spawn_egg_sunbird", props -> new AMSpawnEggItem(props, 0XF6694F, 0XFFDDA0), () -> new Item.Properties().spawnEgg(AMEntityRegistry.SUNBIRD.get()));
        DEF_REG.registerItem("spawn_egg_gorilla", props -> new AMSpawnEggItem(props, 0X595B5D, 0X1C1C21), () -> new Item.Properties().spawnEgg(AMEntityRegistry.GORILLA.get()));
        DEF_REG.registerItem("spawn_egg_crimson_mosquito", props -> new AMSpawnEggItem(props, 0X53403F, 0XC11A1A), () -> new Item.Properties().spawnEgg(AMEntityRegistry.CRIMSON_MOSQUITO.get()));
        DEF_REG.registerItem("spawn_egg_rattlesnake", props -> new AMSpawnEggItem(props, 0XCEB994, 0X937A5B), () -> new Item.Properties().spawnEgg(AMEntityRegistry.RATTLESNAKE.get()));
        DEF_REG.registerItem("spawn_egg_endergrade", props -> new AMSpawnEggItem(props, 0X7862B3, 0x81BDEB), () -> new Item.Properties().spawnEgg(AMEntityRegistry.ENDERGRADE.get()));
        DEF_REG.registerItem("spawn_egg_hammerhead_shark", props -> new AMSpawnEggItem(props, 0X8A92B5, 0XB9BED8), () -> new Item.Properties().spawnEgg(AMEntityRegistry.HAMMERHEAD_SHARK.get()));
        DEF_REG.registerItem("spawn_egg_lobster", props -> new AMSpawnEggItem(props, 0XC43123, 0XDD5F38), () -> new Item.Properties().spawnEgg(AMEntityRegistry.LOBSTER.get()));
        DEF_REG.registerItem("spawn_egg_komodo_dragon", props -> new AMSpawnEggItem(props, 0X746C4F, 0X564231), () -> new Item.Properties().spawnEgg(AMEntityRegistry.KOMODO_DRAGON.get()));
        DEF_REG.registerItem("spawn_egg_capuchin_monkey", props -> new AMSpawnEggItem(props, 0X25211F, 0XF1DAB3), () -> new Item.Properties().spawnEgg(AMEntityRegistry.CAPUCHIN_MONKEY.get()));
        DEF_REG.registerItem("spawn_egg_centipede", props -> new AMSpawnEggItem(props, 0X342B2E, 0X733449), () -> new Item.Properties().spawnEgg(AMEntityRegistry.CENTIPEDE_HEAD.get()));
        DEF_REG.registerItem("spawn_egg_warped_toad", props -> new AMSpawnEggItem(props, 0X1F968E, 0XFEAC6D), () -> new Item.Properties().spawnEgg(AMEntityRegistry.WARPED_TOAD.get()));
        DEF_REG.registerItem("spawn_egg_moose", props -> new AMSpawnEggItem(props, 0X36302A, 0XD4B183), () -> new Item.Properties().spawnEgg(AMEntityRegistry.MOOSE.get()));
        DEF_REG.registerItem("spawn_egg_mimicube", props -> new AMSpawnEggItem(props, 0X8A80C1, 0X5E4F6F), () -> new Item.Properties().spawnEgg(AMEntityRegistry.MIMICUBE.get()));
        DEF_REG.registerItem("spawn_egg_raccoon", props -> new AMSpawnEggItem(props, 0X85827E, 0X2A2726), () -> new Item.Properties().spawnEgg(AMEntityRegistry.RACCOON.get()));
        DEF_REG.registerItem("spawn_egg_blobfish", props -> new AMSpawnEggItem(props, 0XDBC6BD, 0X9E7A7F), () -> new Item.Properties().spawnEgg(AMEntityRegistry.BLOBFISH.get()));
        DEF_REG.registerItem("spawn_egg_seal", props -> new AMSpawnEggItem(props, 0X483C32, 0X66594C), () -> new Item.Properties().spawnEgg(AMEntityRegistry.SEAL.get()));
        DEF_REG.registerItem("spawn_egg_cockroach", props -> new AMSpawnEggItem(props, 0X0D0909, 0X42241E), () -> new Item.Properties().spawnEgg(AMEntityRegistry.COCKROACH.get()));
        DEF_REG.registerItem("spawn_egg_shoebill", props -> new AMSpawnEggItem(props, 0X828282, 0XD5B48A), () -> new Item.Properties().spawnEgg(AMEntityRegistry.SHOEBILL.get()));
        DEF_REG.registerItem("spawn_egg_elephant", props -> new AMSpawnEggItem(props, 0X8D8987, 0XEDE5D1), () -> new Item.Properties().spawnEgg(AMEntityRegistry.ELEPHANT.get()));
        DEF_REG.registerItem("spawn_egg_soul_vulture", props -> new AMSpawnEggItem(props, 0X23262D, 0X57F4FF), () -> new Item.Properties().spawnEgg(AMEntityRegistry.SOUL_VULTURE.get()));
        DEF_REG.registerItem("spawn_egg_snow_leopard", props -> new AMSpawnEggItem(props, 0XACA293, 0X26201D), () -> new Item.Properties().spawnEgg(AMEntityRegistry.SNOW_LEOPARD.get()));
        DEF_REG.registerItem("spawn_egg_spectre", props -> new AMSpawnEggItem(props, 0XC8D0EF, 0X8791EF), () -> new Item.Properties().spawnEgg(AMEntityRegistry.SPECTRE.get()));
        DEF_REG.registerItem("spawn_egg_crow", props -> new AMSpawnEggItem(props, 0X0D111C, 0X1C2030), () -> new Item.Properties().spawnEgg(AMEntityRegistry.CROW.get()));
        DEF_REG.registerItem("spawn_egg_alligator_snapping_turtle", props -> new AMSpawnEggItem(props, 0X6C5C52, 0X456926), () -> new Item.Properties().spawnEgg(AMEntityRegistry.ALLIGATOR_SNAPPING_TURTLE.get()));
        DEF_REG.registerItem("spawn_egg_mungus", props -> new AMSpawnEggItem(props, 0X836A8D, 0X45454C), () -> new Item.Properties().spawnEgg(AMEntityRegistry.MUNGUS.get()));
        DEF_REG.registerItem("spawn_egg_mantis_shrimp", props -> new AMSpawnEggItem(props, 0XDB4858, 0X15991E), () -> new Item.Properties().spawnEgg(AMEntityRegistry.MANTIS_SHRIMP.get()));
        DEF_REG.registerItem("spawn_egg_guster", props -> new AMSpawnEggItem(props, 0XF8D49A, 0XFF720A), () -> new Item.Properties().spawnEgg(AMEntityRegistry.GUSTER.get()));
        DEF_REG.registerItem("spawn_egg_warped_mosco", props -> new AMSpawnEggItem(props, 0X322F58, 0X5B5EF1), () -> new Item.Properties().spawnEgg(AMEntityRegistry.WARPED_MOSCO.get()));
        DEF_REG.registerItem("spawn_egg_straddler", props -> new AMSpawnEggItem(props, 0X5D5F6E, 0XCDA886), () -> new Item.Properties().spawnEgg(AMEntityRegistry.STRADDLER.get()));
        DEF_REG.registerItem("spawn_egg_stradpole", props -> new AMSpawnEggItem(props, 0X5D5F6E, 0X576A8B), () -> new Item.Properties().spawnEgg(AMEntityRegistry.STRADPOLE.get()));
        DEF_REG.registerItem("spawn_egg_emu", props -> new AMSpawnEggItem(props, 0X665346, 0X3B3938), () -> new Item.Properties().spawnEgg(AMEntityRegistry.EMU.get()));
        DEF_REG.registerItem("spawn_egg_platypus", props -> new AMSpawnEggItem(props, 0X7D503E, 0X363B43), () -> new Item.Properties().spawnEgg(AMEntityRegistry.PLATYPUS.get()));
        DEF_REG.registerItem("spawn_egg_dropbear", props -> new AMSpawnEggItem(props, 0X8A2D35, 0X60A3A3), () -> new Item.Properties().spawnEgg(AMEntityRegistry.DROPBEAR.get()));
        DEF_REG.registerItem("spawn_egg_tasmanian_devil", props -> new AMSpawnEggItem(props, 0X252426, 0XA8B4BF), () -> new Item.Properties().spawnEgg(AMEntityRegistry.TASMANIAN_DEVIL.get()));
        DEF_REG.registerItem("spawn_egg_kangaroo", props -> new AMSpawnEggItem(props, 0XCE9D65, 0XDEBDA0), () -> new Item.Properties().spawnEgg(AMEntityRegistry.KANGAROO.get()));
        DEF_REG.registerItem("spawn_egg_cachalot_whale", props -> new AMSpawnEggItem(props, 0X949899, 0X5F666E), () -> new Item.Properties().spawnEgg(AMEntityRegistry.CACHALOT_WHALE.get()));
        DEF_REG.registerItem("spawn_egg_leafcutter_ant", props -> new AMSpawnEggItem(props, 0X964023, 0XA65930), () -> new Item.Properties().spawnEgg(AMEntityRegistry.LEAFCUTTER_ANT.get()));
        DEF_REG.registerItem("spawn_egg_enderiophage", props -> new AMSpawnEggItem(props, 0X872D83, 0XF6E2CD), () -> new Item.Properties().spawnEgg(AMEntityRegistry.ENDERIOPHAGE.get()));
        DEF_REG.registerItem("spawn_egg_bald_eagle", props -> new AMSpawnEggItem(props, 0X321F18, 0XF4F4F4), () -> new Item.Properties().spawnEgg(AMEntityRegistry.BALD_EAGLE.get()));
        DEF_REG.registerItem("spawn_egg_tiger", props -> new AMSpawnEggItem(props, 0XC7612E, 0X2A3233), () -> new Item.Properties().spawnEgg(AMEntityRegistry.TIGER.get()));
        DEF_REG.registerItem("spawn_egg_tarantula_hawk", props -> new AMSpawnEggItem(props, 0X234763, 0XE37B38), () -> new Item.Properties().spawnEgg(AMEntityRegistry.TARANTULA_HAWK.get()));
        DEF_REG.registerItem("spawn_egg_void_worm", props -> new AMSpawnEggItem(props, 0X0F1026, 0X1699AB), () -> new Item.Properties().spawnEgg(AMEntityRegistry.VOID_WORM.get()));
        DEF_REG.registerItem("spawn_egg_frilled_shark", props -> new AMSpawnEggItem(props, 0X726B6B, 0X873D3D), () -> new Item.Properties().spawnEgg(AMEntityRegistry.FRILLED_SHARK.get()));
        DEF_REG.registerItem("spawn_egg_mimic_octopus", props -> new AMSpawnEggItem(props, 0XFFEBDC, 0X1D1C1F), () -> new Item.Properties().spawnEgg(AMEntityRegistry.MIMIC_OCTOPUS.get()));
        DEF_REG.registerItem("spawn_egg_seagull", props -> new AMSpawnEggItem(props, 0XC9D2DC, 0XFFD850), () -> new Item.Properties().spawnEgg(AMEntityRegistry.SEAGULL.get()));
        DEF_REG.registerItem("spawn_egg_froststalker", props -> new AMSpawnEggItem(props, 0X788AC1, 0XA1C3FF), () -> new Item.Properties().spawnEgg(AMEntityRegistry.FROSTSTALKER.get()));
        DEF_REG.registerItem("spawn_egg_tusklin", props -> new AMSpawnEggItem(props, 0X735841, 0XE8E2D5), () -> new Item.Properties().spawnEgg(AMEntityRegistry.TUSKLIN.get()));
        DEF_REG.registerItem("spawn_egg_laviathan", props -> new AMSpawnEggItem(props, 0XD68356, 0X3C3947), () -> new Item.Properties().spawnEgg(AMEntityRegistry.LAVIATHAN.get()));
        DEF_REG.registerItem("spawn_egg_cosmaw", props -> new AMSpawnEggItem(props, 0X746DBD, 0XD6BFE3), () -> new Item.Properties().spawnEgg(AMEntityRegistry.COSMAW.get()));
        DEF_REG.registerItem("spawn_egg_toucan", props -> new AMSpawnEggItem(props, 0XF58F33, 0X1E2133), () -> new Item.Properties().spawnEgg(AMEntityRegistry.TOUCAN.get()));
        DEF_REG.registerItem("spawn_egg_maned_wolf", props -> new AMSpawnEggItem(props, 0XBB7A47, 0X40271A), () -> new Item.Properties().spawnEgg(AMEntityRegistry.MANED_WOLF.get()));
        DEF_REG.registerItem("spawn_egg_anaconda", props -> new AMSpawnEggItem(props, 0X565C22, 0XD3763F), () -> new Item.Properties().spawnEgg(AMEntityRegistry.ANACONDA.get()));
        DEF_REG.registerItem("spawn_egg_anteater", props -> new AMSpawnEggItem(props, 0X4C3F3A, 0XCCBCB4), () -> new Item.Properties().spawnEgg(AMEntityRegistry.ANTEATER.get()));
        DEF_REG.registerItem("spawn_egg_rocky_roller", props -> new AMSpawnEggItem(props, 0XB0856F, 0X999184), () -> new Item.Properties().spawnEgg(AMEntityRegistry.ROCKY_ROLLER.get()));
        DEF_REG.registerItem("spawn_egg_flutter", props -> new AMSpawnEggItem(props, 0X70922D, 0XD07BE3), () -> new Item.Properties().spawnEgg(AMEntityRegistry.FLUTTER.get()));
        DEF_REG.registerItem("spawn_egg_gelada_monkey", props -> new AMSpawnEggItem(props, 0XB08C64, 0XFF4F53), () -> new Item.Properties().spawnEgg(AMEntityRegistry.GELADA_MONKEY.get()));
        DEF_REG.registerItem("spawn_egg_jerboa", props -> new AMSpawnEggItem(props, 0XDEC58A, 0XDE9D90), () -> new Item.Properties().spawnEgg(AMEntityRegistry.JERBOA.get()));
        DEF_REG.registerItem("spawn_egg_terrapin", props -> new AMSpawnEggItem(props, 0X6E6E30, 0X929647), () -> new Item.Properties().spawnEgg(AMEntityRegistry.TERRAPIN.get()));
        DEF_REG.registerItem("spawn_egg_comb_jelly", props -> new AMSpawnEggItem(props, 0XCFE9FE, 0X6EFF8B), () -> new Item.Properties().spawnEgg(AMEntityRegistry.COMB_JELLY.get()));
        DEF_REG.registerItem("spawn_egg_cosmic_cod", props -> new AMSpawnEggItem(props, 0X6985C7, 0XE2D1FF), () -> new Item.Properties().spawnEgg(AMEntityRegistry.COSMIC_COD.get()));
        DEF_REG.registerItem("spawn_egg_bunfungus", props -> new AMSpawnEggItem(props, 0X6F6D91, 0XC92B29), () -> new Item.Properties().spawnEgg(AMEntityRegistry.BUNFUNGUS.get()));
        DEF_REG.registerItem("spawn_egg_bison", props -> new AMSpawnEggItem(props, 0X4C3A2E, 0X7A6546), () -> new Item.Properties().spawnEgg(AMEntityRegistry.BISON.get()));
        DEF_REG.registerItem("spawn_egg_giant_squid", props -> new AMSpawnEggItem(props, 0XAB4B4D, 0XD67D6B), () -> new Item.Properties().spawnEgg(AMEntityRegistry.GIANT_SQUID.get()));
        DEF_REG.registerItem("spawn_egg_devils_hole_pupfish", props -> new AMSpawnEggItem(props, 0X567BC4, 0X6C4475), () -> new Item.Properties().spawnEgg(AMEntityRegistry.DEVILS_HOLE_PUPFISH.get()));
        DEF_REG.registerItem("spawn_egg_catfish", props -> new AMSpawnEggItem(props, 0X807757, 0X8A7466), () -> new Item.Properties().spawnEgg(AMEntityRegistry.CATFISH.get()));
        DEF_REG.registerItem("spawn_egg_flying_fish", props -> new AMSpawnEggItem(props, 0X7BBCED, 0X6881B3), () -> new Item.Properties().spawnEgg(AMEntityRegistry.FLYING_FISH.get()));
        DEF_REG.registerItem("spawn_egg_skelewag", props -> new AMSpawnEggItem(props, 0XD9FCB1, 0X3A4F30), () -> new Item.Properties().spawnEgg(AMEntityRegistry.SKELEWAG.get()));
        DEF_REG.registerItem("spawn_egg_rain_frog", props -> new AMSpawnEggItem(props, 0XC0B59B, 0X7B654F), () -> new Item.Properties().spawnEgg(AMEntityRegistry.RAIN_FROG.get()));
        DEF_REG.registerItem("spawn_egg_potoo", props -> new AMSpawnEggItem(props, 0X8C7753, 0XFFC042), () -> new Item.Properties().spawnEgg(AMEntityRegistry.POTOO.get()));
        DEF_REG.registerItem("spawn_egg_mudskipper", props -> new AMSpawnEggItem(props, 0X60704A, 0X49806C), () -> new Item.Properties().spawnEgg(AMEntityRegistry.MUDSKIPPER.get()));
        DEF_REG.registerItem("spawn_egg_rhinoceros", props -> new AMSpawnEggItem(props, 0XA19594, 0X827474), () -> new Item.Properties().spawnEgg(AMEntityRegistry.RHINOCEROS.get()));
        DEF_REG.registerItem("spawn_egg_sugar_glider", props -> new AMSpawnEggItem(props, 0X868181, 0XEBEBE0), () -> new Item.Properties().spawnEgg(AMEntityRegistry.SUGAR_GLIDER.get()));
        DEF_REG.registerItem("spawn_egg_farseer", props -> new AMSpawnEggItem(props, 0X33374F, 0X91FF59), () -> new Item.Properties().spawnEgg(AMEntityRegistry.FARSEER.get()));
        DEF_REG.registerItem("spawn_egg_skreecher", props -> new AMSpawnEggItem(props, 0X074857, 0X7FF8FF), () -> new Item.Properties().spawnEgg(AMEntityRegistry.SKREECHER.get()));
        DEF_REG.registerItem("spawn_egg_underminer", props -> new AMSpawnEggItem(props, 0XD6E2FF, 0X6C84C4), () -> new Item.Properties().spawnEgg(AMEntityRegistry.UNDERMINER.get()));
        DEF_REG.registerItem("spawn_egg_murmur", props -> new AMSpawnEggItem(props, 0X804448, 0XB5AF9C), () -> new Item.Properties().spawnEgg(AMEntityRegistry.MURMUR.get()));
        DEF_REG.registerItem("spawn_egg_skunk", props -> new AMSpawnEggItem(props, 0X222D36, 0XE4E5F2), () -> new Item.Properties().spawnEgg(AMEntityRegistry.SKUNK.get()));
        DEF_REG.registerItem("spawn_egg_banana_slug", props -> new AMSpawnEggItem(props, 0XFFD045, 0XFFF173), () -> new Item.Properties().spawnEgg(AMEntityRegistry.BANANA_SLUG.get()));
        DEF_REG.registerItem("spawn_egg_blue_jay", props -> new AMSpawnEggItem(props, 0X5FB7FE, 0X293B42), () -> new Item.Properties().spawnEgg(AMEntityRegistry.BLUE_JAY.get()));
        DEF_REG.registerItem("spawn_egg_caiman", props -> new AMSpawnEggItem(props, 0X5C5631, 0XBBC45C), () -> new Item.Properties().spawnEgg(AMEntityRegistry.CAIMAN.get()));
        DEF_REG.registerItem("spawn_egg_triops", props -> new AMSpawnEggItem(props, 0X967954, 0XCA7150), () -> new Item.Properties().spawnEgg(AMEntityRegistry.TRIOPS.get()));
        registerPatternItem("bear");
        registerPatternItem("australia_0");
        registerPatternItem("australia_1");
        registerPatternItem("new_mexico");
        registerPatternItem("brazil");
        for(int i = 0; i <= 10; i++){
            DEF_REG.registerItem("dimensional_carver_shard_" + i, ItemInventoryOnly::new, Item.Properties::new);
        }
    }

    private static Item.Properties foodWithChanceEffect(FoodProperties food, Holder<net.minecraft.world.effect.MobEffect> effect, int duration, int amplifier, float probability) {
        return new Item.Properties().food(food, Consumable.builder()
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(effect, duration, amplifier), probability))
                .build());
    }

    private static void registerPatternItem(String name) {
        DEF_REG.registerItem("banner_pattern_" + name, Item::new, () -> (new Item.Properties()).stacksTo(1)
                .component(DataComponents.PROVIDES_BANNER_PATTERNS, HolderSet.empty()));
    }

    public static void init() {
        // TODO 1.21: Armor materials are data-driven

        // // TODO 1.21: Armor materials are data-driven
 // CROCODILE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(CROCODILE_SCUTE.get()));
        // TODO 1.21: Armor materials are data-driven

        // // TODO 1.21: Armor materials are data-driven
 // ROADRUNNER_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(ROADRUNNER_FEATHER.get()));
        // TODO 1.21: Armor materials are data-driven

        // // TODO 1.21: Armor materials are data-driven
 // CENTIPEDE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(CENTIPEDE_LEG.get()));
        // TODO 1.21: Armor materials are data-driven

        // // TODO 1.21: Armor materials are data-driven
 // MOOSE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(MOOSE_ANTLER.get()));
        // TODO 1.21: Armor materials are data-driven

        // // TODO 1.21: Armor materials are data-driven
 // RACCOON_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(RACCOON_TAIL.get()));
        // TODO 1.21: Armor materials are data-driven

        // // TODO 1.21: Armor materials are data-driven
 // SOMBRERO_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(Items.HAY_BLOCK));
        // TODO 1.21: Armor materials are data-driven

        // // TODO 1.21: Armor materials are data-driven
 // SPIKED_TURTLE_SHELL_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(SPIKED_SCUTE.get()));
        // TODO 1.21: Armor materials are data-driven

        // // TODO 1.21: Armor materials are data-driven
 // FEDORA_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(Items.LEATHER));
        // TODO 1.21: Armor materials are data-driven

        // // TODO 1.21: Armor materials are data-driven
 // EMU_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(EMU_FEATHER.get()));
        // TODO 1.21: Armor materials are data-driven

        // // TODO 1.21: Armor materials are data-driven
 // ROCKY_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(ROCKY_SHELL.get()));
        // TODO 1.21: Armor materials are data-driven

        // FLYING_FISH_MATERIAL.setRepairMaterial(Ingredient.of(FLYING_FISH.get()));
        // TODO 1.21: Armor materials are data-driven

        // NOVELTY_HAT_MATERIAL.setRepairMaterial(Ingredient.of(Items.BONE));
        // TODO 1.21: Armor materials are data-driven

        // KIMONO_MATERIAL.setRepairMaterial(Ingredient.of(ItemTags.WOOL));
        LecternBooks.BOOKS.put(ANIMAL_DICTIONARY.getId(), new LecternBooks.BookData(0X606B26, 0XFDF8ED));
    }

    public static void initDispenser(){
        DispenserBlock.registerBehavior(SHARK_TOOTH_ARROW.get(), new net.minecraft.core.dispenser.DefaultDispenseItemBehavior() /* TODO 1.21: ProjectileDispenseBehavior changed */ {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                EntitySharkToothArrow entityarrow = new EntitySharkToothArrow(AMEntityRegistry.SHARK_TOOTH_ARROW.get(), position.x(), position.y(), position.z(), worldIn, stackIn, ItemStack.EMPTY);
                entityarrow.pickup = AbstractArrow.Pickup.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(ANCIENT_DART.get(), new net.minecraft.core.dispenser.DefaultDispenseItemBehavior() /* TODO 1.21: ProjectileDispenseBehavior changed */ {
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                EntityTossedItem tossedItem = new EntityTossedItem(worldIn, position.x(), position.y(), position.z());
                tossedItem.setDart(true);
                return tossedItem;
            }
        });
        DispenserBlock.registerBehavior(COCKROACH_OOTHECA.get(), new net.minecraft.core.dispenser.DefaultDispenseItemBehavior() /* TODO 1.21: ProjectileDispenseBehavior changed */ {
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                EntityCockroachEgg entityarrow = new EntityCockroachEgg(worldIn, position.x(), position.y(), position.z());
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(EMU_EGG.get(), new net.minecraft.core.dispenser.DefaultDispenseItemBehavior() /* TODO 1.21: ProjectileDispenseBehavior changed */ {
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                EntityEmuEgg entityarrow = new EntityEmuEgg(worldIn, position.x(), position.y(), position.z());
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(ENDERIOPHAGE_ROCKET.get(), new net.minecraft.core.dispenser.DefaultDispenseItemBehavior() /* TODO 1.21: ProjectileDispenseBehavior changed */ {
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                EntityEnderiophageRocket entityarrow = new EntityEnderiophageRocket(worldIn, position.x(), position.y(), position.z(), stackIn);
                return entityarrow;
            }
        });
        DispenseItemBehavior bucketDispenseBehavior = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            public ItemStack execute(BlockSource blockSource, ItemStack stack) {
                Item item = stack.getItem();
                if (!(item instanceof BucketItem bucketItem)) {
                    return this.defaultDispenseItemBehavior.dispense(blockSource, stack);
                }
                DispensibleContainerItem dispensiblecontaineritem = (DispensibleContainerItem) item;
                BlockPos blockpos = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));
                Level level = blockSource.level();
                if (bucketItem.emptyContents(null, level, blockpos, null, stack)) {
                    dispensiblecontaineritem.checkExtraContent(null, level, stack, blockpos);
                    return new ItemStack(Items.BUCKET);
                } else {
                    return this.defaultDispenseItemBehavior.dispense(blockSource, stack);
                }
            }
        };
        DispenserBlock.registerBehavior(LOBSTER_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(BLOBFISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(STRADPOLE_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(PLATYPUS_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(FRILLED_SHARK_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(MIMIC_OCTOPUS_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(TERRAPIN_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(COMB_JELLY_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(COSMIC_COD_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(DEVILS_HOLE_PUPFISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(SMALL_CATFISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(MEDIUM_CATFISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(LARGE_CATFISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(FLYING_FISH_BUCKET.get(), bucketDispenseBehavior);
        DispenserBlock.registerBehavior(MUDSKIPPER_BUCKET.get(), bucketDispenseBehavior);
        ComposterBlock.COMPOSTABLES.put(BANANA.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(AMBlockRegistry.BANANA_PEEL.get().asItem(), 1F);
        ComposterBlock.COMPOSTABLES.put(ACACIA_BLOSSOM.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(GONGYLIDIA.get(), 0.9F);
    }

}
