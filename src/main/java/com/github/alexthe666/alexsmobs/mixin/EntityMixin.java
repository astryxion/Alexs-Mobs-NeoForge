package com.github.alexthe666.alexsmobs.mixin;

import com.github.alexthe666.alexsmobs.misc.AMEntityHooks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public class EntityMixin {

    /**
     * Vanilla refuses passengers whose type cannot serialize. Several mobs mount the player and must be allowed anyway.
     */
    @Redirect(
            method = "startRiding(Lnet/minecraft/world/entity/Entity;ZZ)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;canSerialize()Z")
    )
    private boolean alexsmobs$allowRidingUnsaveableVehicle(EntityType<?> type) {
        return type.canSerialize() || AMEntityHooks.ridesUnsaveableVehicles((Entity) (Object) this);
    }
}
