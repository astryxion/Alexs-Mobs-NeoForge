package com.github.alexthe666.alexsmobs.client;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.client.render.layer.LayerRainbow;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.player.PlayerModelType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;

import java.util.List;
import java.util.stream.Collectors;
public class ClientLayerRegistry {

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        List<EntityType<? extends LivingEntity>> entityTypes = ImmutableList.copyOf(
                BuiltInRegistries.ENTITY_TYPE.stream()
                        .filter(DefaultAttributes::hasSupplier)
                        .map(entityType -> (EntityType<? extends LivingEntity>) entityType)
                        .collect(Collectors.toList()));
        entityTypes.forEach((entityType -> {
            addLayerIfApplicable(entityType, event);
        }));
        for (PlayerModelType skinModel : event.getSkins()) {
            var skin = event.getPlayerRenderer(skinModel);
            if (skin != null) {
                skin.addLayer(new LayerRainbow<>(skin));
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addRainbowLayerToLivingRenderer(LivingEntityRenderer<?, ?, ?> livingRenderer) {
        livingRenderer.addLayer(new LayerRainbow(livingRenderer));
    }

    private static void addLayerIfApplicable(EntityType<? extends LivingEntity> entityType, EntityRenderersEvent.AddLayers event) {
        if(entityType != EntityType.ENDER_DRAGON){
            try{
                var renderer = event.getRenderer(entityType);
                if (renderer instanceof LivingEntityRenderer<?, ?, ?> livingRenderer) {
                    addRainbowLayerToLivingRenderer(livingRenderer);
                }
            }catch (Exception e){
                AlexsMobs.LOGGER.warn("Could not apply rainbow color layer to " + BuiltInRegistries.ENTITY_TYPE.getKey(entityType) + ", has custom renderer that is not LivingEntityRenderer.");
            }
        }
    }
}
