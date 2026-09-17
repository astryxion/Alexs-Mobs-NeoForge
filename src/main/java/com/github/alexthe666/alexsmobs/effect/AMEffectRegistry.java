package com.github.alexthe666.alexsmobs.effect;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AMEffectRegistry {
    public static final DeferredRegister<MobEffect> EFFECT_DEF_REG = DeferredRegister.create(Registries.MOB_EFFECT, AlexsMobs.MODID);
    public static final DeferredRegister<Potion> POTION_DEF_REG = DeferredRegister.create(Registries.POTION, AlexsMobs.MODID);

    public static final DeferredHolder<MobEffect, MobEffect> KNOCKBACK_RESISTANCE = EFFECT_DEF_REG.register("knockback_resistance", ()-> new EffectKnockbackResistance());
    public static final DeferredHolder<MobEffect, MobEffect> LAVA_VISION = EFFECT_DEF_REG.register("lava_vision", ()-> new EffectLavaVision());
    public static final DeferredHolder<MobEffect, MobEffect> SUNBIRD_BLESSING = EFFECT_DEF_REG.register("sunbird_blessing", ()-> new EffectSunbird(false));
    public static final DeferredHolder<MobEffect, MobEffect> SUNBIRD_CURSE = EFFECT_DEF_REG.register("sunbird_curse", ()-> new EffectSunbird(true));
    public static final DeferredHolder<MobEffect, MobEffect> POISON_RESISTANCE = EFFECT_DEF_REG.register("poison_resistance", ()-> new EffectPoisonResistance());
    public static final DeferredHolder<MobEffect, MobEffect> OILED = EFFECT_DEF_REG.register("oiled", ()-> new EffectOiled());
    public static final DeferredHolder<MobEffect, MobEffect> ORCAS_MIGHT = EFFECT_DEF_REG.register("orcas_might", ()-> new EffectOrcaMight());
    public static final DeferredHolder<MobEffect, MobEffect> BUG_PHEROMONES = EFFECT_DEF_REG.register("bug_pheromones", ()-> new EffectBugPheromones());
    public static final DeferredHolder<MobEffect, MobEffect> SOULSTEAL = EFFECT_DEF_REG.register("soulsteal", ()-> new EffectSoulsteal());
    public static final DeferredHolder<MobEffect, MobEffect> CLINGING = EFFECT_DEF_REG.register("clinging", ()-> new EffectClinging());
    public static final DeferredHolder<MobEffect, MobEffect> ENDER_FLU = EFFECT_DEF_REG.register("ender_flu", ()-> new EffectEnderFlu());
    public static final DeferredHolder<MobEffect, MobEffect> FEAR = EFFECT_DEF_REG.register("fear", ()-> new EffectFear());
    public static final DeferredHolder<MobEffect, MobEffect> TIGERS_BLESSING = EFFECT_DEF_REG.register("tigers_blessing", ()-> new EffectTigersBlessing());
    public static final DeferredHolder<MobEffect, MobEffect> DEBILITATING_STING = EFFECT_DEF_REG.register("debilitating_sting", ()-> new EffectDebilitatingSting());
    public static final DeferredHolder<MobEffect, MobEffect> EXSANGUINATION = EFFECT_DEF_REG.register("exsanguination", ()-> new EffectExsanguination());
    public static final DeferredHolder<MobEffect, MobEffect> EARTHQUAKE = EFFECT_DEF_REG.register("earthquake", ()-> new EffectEarthquake());
    public static final DeferredHolder<MobEffect, MobEffect> FLEET_FOOTED = EFFECT_DEF_REG.register("fleet_footed", ()-> new EffectFleetFooted());
    public static final DeferredHolder<MobEffect, MobEffect> POWER_DOWN = EFFECT_DEF_REG.register("power_down", ()-> new EffectPowerDown());

    public static final DeferredHolder<MobEffect, MobEffect> MOSQUITO_REPELLENT = EFFECT_DEF_REG.register("mosquito_repellent", ()-> new EffectMosquitoRepellent());
    public static final DeferredHolder<Potion, Potion> KNOCKBACK_RESISTANCE_POTION = POTION_DEF_REG.register("knockback_resistance", ()-> potion("knockback_resistance", new MobEffectInstance(effect(KNOCKBACK_RESISTANCE), 3600)));
    public static final DeferredHolder<Potion, Potion> LONG_KNOCKBACK_RESISTANCE_POTION = POTION_DEF_REG.register("long_knockback_resistance", ()-> potion("long_knockback_resistance", new MobEffectInstance(effect(KNOCKBACK_RESISTANCE), 9600)));
    public static final DeferredHolder<Potion, Potion> STRONG_KNOCKBACK_RESISTANCE_POTION = POTION_DEF_REG.register("strong_knockback_resistance", ()-> potion("strong_knockback_resistance", new MobEffectInstance(effect(KNOCKBACK_RESISTANCE), 1800, 1)));
    public static final DeferredHolder<Potion, Potion> LAVA_VISION_POTION = POTION_DEF_REG.register("lava_vision", ()-> potion("lava_vision", new MobEffectInstance(effect(LAVA_VISION), 3600)));
    public static final DeferredHolder<Potion, Potion> LONG_LAVA_VISION_POTION = POTION_DEF_REG.register("long_lava_vision", ()-> potion("long_lava_vision", new MobEffectInstance(effect(LAVA_VISION), 9600)));
    public static final DeferredHolder<Potion, Potion> SPEED_III_POTION = POTION_DEF_REG.register("speed_iii", ()-> potion("speed_iii", new MobEffectInstance(MobEffects.SPEED, 2200, 2)));
    public static final DeferredHolder<Potion, Potion> POISON_RESISTANCE_POTION = POTION_DEF_REG.register("poison_resistance", ()-> potion("poison_resistance", new MobEffectInstance(effect(POISON_RESISTANCE), 3600)));
    public static final DeferredHolder<Potion, Potion> LONG_POISON_RESISTANCE_POTION = POTION_DEF_REG.register("long_poison_resistance", ()-> potion("long_poison_resistance", new MobEffectInstance(effect(POISON_RESISTANCE), 9600)));
    public static final DeferredHolder<Potion, Potion> BUG_PHEROMONES_POTION = POTION_DEF_REG.register("bug_pheromones", ()-> potion("bug_pheromones", new MobEffectInstance(effect(BUG_PHEROMONES), 3600)));
    public static final DeferredHolder<Potion, Potion> LONG_BUG_PHEROMONES_POTION = POTION_DEF_REG.register("long_bug_pheromones", ()-> potion("long_bug_pheromones", new MobEffectInstance(effect(BUG_PHEROMONES), 9600)));
    public static final DeferredHolder<Potion, Potion> SOULSTEAL_POTION = POTION_DEF_REG.register("soulsteal", ()-> potion("soulsteal", new MobEffectInstance(effect(SOULSTEAL), 3600)));
    public static final DeferredHolder<Potion, Potion> LONG_SOULSTEAL_POTION = POTION_DEF_REG.register("long_soulsteal", ()-> potion("long_soulsteal", new MobEffectInstance(effect(SOULSTEAL), 9600)));
    public static final DeferredHolder<Potion, Potion> STRONG_SOULSTEAL_POTION = POTION_DEF_REG.register("strong_soulsteal", ()-> potion("strong_soulsteal", new MobEffectInstance(effect(SOULSTEAL), 1800, 1)));
    public static final DeferredHolder<Potion, Potion> CLINGING_POTION = POTION_DEF_REG.register("clinging", ()-> potion("clinging", new MobEffectInstance(effect(CLINGING), 3600)));
    public static final DeferredHolder<Potion, Potion> LONG_CLINGING_POTION = POTION_DEF_REG.register("long_clinging", ()-> potion("long_clinging", new MobEffectInstance(effect(CLINGING), 9600)));

    private static Potion potion(String name, MobEffectInstance... effects) {
        return new Potion(name, effects);
    }

    private static Holder<MobEffect> effect(DeferredHolder<MobEffect, MobEffect> holder) {
        return BuiltInRegistries.MOB_EFFECT.wrapAsHolder(holder.get());
    }

    public static ItemStack createPotion(Holder<Potion> potion) {
        return PotionContents.createItemStack(Items.POTION, potion);
    }

    public static ItemStack createPotion(Potion potion) {
        return PotionContents.createItemStack(Items.POTION, BuiltInRegistries.POTION.wrapAsHolder(potion));
    }
}
