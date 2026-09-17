package com.github.alexthe666.alexsmobs.misc;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class BlossomLootModifier extends LootModifier {

    public static final MapCodec<BlossomLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            codecStart(inst).apply(inst, BlossomLootModifier::new));

    public BlossomLootModifier(Optional<Holder<LootItemCondition>> condition, int priority) {
        super(condition, priority);
    }

    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (AMConfig.acaciaBlossomsDropFromLeaves) {
            ItemInstance toolInstance = context.getOptional(LootContextParams.TOOL);
            ItemStack ctxTool;
            if (toolInstance instanceof ItemStack stack) {
                ctxTool = stack;
            } else if (toolInstance instanceof ItemStackTemplate template) {
                ctxTool = template.create();
            } else {
                ctxTool = ItemStack.EMPTY;
            }
            RandomSource random = context.getRandom();
            var enchantments = context.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            if (!ctxTool.isEmpty()) {
                int silkTouch = ctxTool.getEnchantmentLevel(enchantments.getOrThrow(Enchantments.SILK_TOUCH));
                if (silkTouch > 0 || ctxTool.getItem() instanceof ShearsItem) {
                    return generatedLoot;
                }
            }
            int bonusLevel = !ctxTool.isEmpty() ? ctxTool.getEnchantmentLevel(enchantments.getOrThrow(Enchantments.FORTUNE)) : 0;
            int blossomStep = (int) Math.floor(AMConfig.acaciaBlossomChance * 0.1F);
            int blossomRarity = AMConfig.acaciaBlossomChance - (bonusLevel * blossomStep);
            if (blossomRarity < 1 || random.nextInt(blossomRarity) == 0) {
                generatedLoot.add(new ItemStack(AMItemRegistry.ACACIA_BLOSSOM.get()));
            }
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
