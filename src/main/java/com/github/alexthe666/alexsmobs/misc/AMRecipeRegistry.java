package com.github.alexthe666.alexsmobs.misc;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class AMRecipeRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> DEF_REG = DeferredRegister.create(Registries.RECIPE_SERIALIZER, AlexsMobs.MODID);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeMimicreamRepair>> MIMICREAM_RECIPE = DEF_REG.register("mimicream_repair", () -> new RecipeSerializer<>(RecipeMimicreamRepair.MAP_CODEC, RecipeMimicreamRepair.STREAM_CODEC));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeBisonUpgrade>> BISON_UPGRADE = DEF_REG.register("bison_upgrade", () -> new RecipeSerializer<>(RecipeBisonUpgrade.MAP_CODEC, RecipeBisonUpgrade.STREAM_CODEC));

    public static void init(){
    }
}
