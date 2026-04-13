package com.github.alexthe666.alexsmobs.entity;

import net.neoforged.neoforge.common.ItemAbility;

import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntitySharkToothArrow extends AbstractArrow {

    public EntitySharkToothArrow(EntityType<? extends EntitySharkToothArrow> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntitySharkToothArrow(EntityType<? extends EntitySharkToothArrow> type, double x, double y, double z, Level worldIn, ItemStack arrowStack, ItemStack weaponStack) {
        super(type, x, y, z, worldIn, arrowStack, weaponStack);
    }

    public EntitySharkToothArrow(Level worldIn, LivingEntity shooter, ItemStack arrowStack, ItemStack weaponStack) {
        super(AMEntityRegistry.SHARK_TOOTH_ARROW.get(), shooter, worldIn, arrowStack, weaponStack);
        if (shooter instanceof Player) {
            this.pickup = AbstractArrow.Pickup.ALLOWED;
        }
    }

    protected void damageShield(Player player, float damage) {
        if (damage >= 3.0F && player.getUseItem().canPerformAction(ItemAbility.get("shield_block"))) {
            ItemStack copyBeforeUse = player.getUseItem().copy();
            int i = 1 + Mth.floor(damage);
            InteractionHand hand = player.getUsedItemHand();
            EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            player.getUseItem().hurtAndBreak(i, player, slot);

            if (player.getUseItem().isEmpty()) {
                net.neoforged.neoforge.event.EventHooks.onPlayerDestroyItem(player, copyBeforeUse, hand);
                player.setItemSlot(slot, ItemStack.EMPTY);
                player.stopUsingItem();
                this.playSound(SoundEvents.SHIELD_BREAK.value(), 0.8F, 0.8F + this.random.nextFloat() * 0.4F);
            }
        }
    }

    protected void doPostHurtEffects(LivingEntity living) {
        if (living instanceof Player) {
            this.damageShield((Player) living, (float) this.baseDamage);
        }
        Entity entity1 = this.getOwner();
        if (living.getType().builtInRegistryHolder().is(EntityTypeTags.ARTHROPOD) || living instanceof Drowned || !living.getType().builtInRegistryHolder().is(EntityTypeTags.UNDEAD) && living.canBreatheUnderwater()) {
            DamageSource damagesource;
            if (entity1 == null) {
                damagesource = damageSources().arrow(this, this);
            } else {
                damagesource = damageSources().arrow(this, entity1);
            }
            if (living.level() instanceof ServerLevel serverLevel) {
                living.hurtServer(serverLevel, damagesource, 7);
            }
        }
    }


    public boolean isInWater() {
        return false;
    }


    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(AMItemRegistry.SHARK_TOOTH_ARROW.get());
    }

}
