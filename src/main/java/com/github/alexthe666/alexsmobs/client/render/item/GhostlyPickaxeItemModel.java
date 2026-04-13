package com.github.alexthe666.alexsmobs.client.render.item;

import com.github.alexthe666.alexsmobs.client.render.AMRenderTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public final class GhostlyPickaxeItemModel implements ItemModel {

    private final ItemModel inner;

    public GhostlyPickaxeItemModel(ItemModel inner) {
        this.inner = inner;
    }

    @Override
    public void update(ItemStackRenderState state, ItemStack stack, ItemModelResolver resolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        this.inner.update(state, stack, resolver, displayContext, level, owner, seed);
        int n = state.activeLayerCount;
        for (int i = 0; i < n; i++) {
            var layer = state.layers[i];
            var quads = layer.prepareQuadList();
            for (int q = 0; q < quads.size(); q++) {
                quads.set(q, fullbrightGhost(quads.get(q)));
            }
        }
    }

    private static BakedQuad fullbrightGhost(BakedQuad quad) {
        var mi = quad.materialInfo();
        var newMi = new BakedQuad.MaterialInfo(
                mi.sprite(),
                mi.layer(),
                AMRenderTypes.getGhostPickaxe(TextureAtlas.LOCATION_ITEMS),
                mi.tintIndex(),
                mi.shade(),
                15);
        return new BakedQuad(
                quad.position0(),
                quad.position1(),
                quad.position2(),
                quad.position3(),
                quad.packedUV0(),
                quad.packedUV1(),
                quad.packedUV2(),
                quad.packedUV3(),
                quad.direction(),
                newMi);
    }
}