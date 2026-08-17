package com.github.alexthe666.alexsmobs.misc;

import com.github.alexthe666.alexsmobs.entity.EntityBaldEagle;
import com.github.alexthe666.alexsmobs.entity.EntityCapuchinMonkey;
import com.github.alexthe666.alexsmobs.entity.EntityCrimsonMosquito;
import com.github.alexthe666.alexsmobs.entity.EntityCrow;
import com.github.alexthe666.alexsmobs.entity.EntityEnderiophage;
import com.github.alexthe666.alexsmobs.entity.EntityPotoo;
import com.github.alexthe666.alexsmobs.entity.EntitySugarGlider;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.entity.PartEntity;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Small entity helpers for construction-time safety, multipart IDs, and riding vehicles that cannot serialize.
 */
public final class AMEntityHooks {
    private static final AtomicInteger CLIENT_PART_ID = new AtomicInteger();

    private AMEntityHooks() {
    }

    public static boolean isFullyConstructed(Entity entity) {
        return entity.getEntityData() != null;
    }

    /**
     * Client {@code Level#getNextEntityId()} always returns 0, and part entities never receive
     * {@code recreateFromPacket}. A zero ID throws from {@link Entity#getId()} during tracking end.
     */
    public static void assignClientPartId(Entity part) {
        if (part.level().isClientSide()) {
            part.setId(nextFallbackPartId());
        }
    }

    /**
     * Binds part IDs to {@code parentId + index + 1}, matching vanilla Ender Dragon. Call from the
     * parent's {@link Entity#setId(int)} so client add-entity packets assign non-zero IDs before tracking.
     */
    public static void bindPartIds(int parentId, PartEntity<?>[] parts) {
        if (parts == null) {
            return;
        }
        for (int i = 0; i < parts.length; i++) {
            PartEntity<?> part = parts[i];
            if (part == null) {
                continue;
            }
            int partId = parentId + i + 1;
            if (partId == 0) {
                partId = nextFallbackPartId();
            }
            part.setId(partId);
        }
    }

    private static int nextFallbackPartId() {
        int id = CLIENT_PART_ID.decrementAndGet();
        return id == 0 ? CLIENT_PART_ID.decrementAndGet() : id;
    }

    public static boolean ridesUnsaveableVehicles(Entity rider) {
        return rider instanceof EntityCrimsonMosquito
                || rider instanceof EntityEnderiophage
                || rider instanceof EntityBaldEagle
                || rider instanceof EntityCrow
                || rider instanceof EntityCapuchinMonkey
                || rider instanceof EntityPotoo
                || rider instanceof EntitySugarGlider;
    }
}
