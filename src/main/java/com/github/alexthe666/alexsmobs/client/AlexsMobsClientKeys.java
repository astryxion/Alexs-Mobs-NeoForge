package com.github.alexthe666.alexsmobs.client;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public final class AlexsMobsClientKeys {

    public static final ContextKey<LivingEntity> RENDER_STATE_LIVING_ENTITY =
            new ContextKey<>(Identifier.fromNamespaceAndPath("alexsmobs", "render_state_living_entity"));
    public static final ContextKey<Entity> RENDER_STATE_ENTITY =
            new ContextKey<>(Identifier.fromNamespaceAndPath("alexsmobs", "render_state_entity"));

    private AlexsMobsClientKeys() {
    }

    public static LivingEntity getLiving(LivingEntityRenderState state) {
        return state.getRenderData(RENDER_STATE_LIVING_ENTITY);
    }

    public static Entity getEntity(EntityRenderState state) {
        return state.getRenderData(RENDER_STATE_ENTITY);
    }
}