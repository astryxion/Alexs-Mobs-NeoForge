package com.github.alexthe666.alexsmobs.client.render.layer;

import com.github.alexthe666.alexsmobs.client.AlexsMobsClientKeys;
import com.github.alexthe666.alexsmobs.client.model.ModelCockroach;
import com.github.alexthe666.alexsmobs.client.model.layered.AMModelLayers;
import com.github.alexthe666.alexsmobs.client.model.layered.ModelSombrero;
import com.github.alexthe666.alexsmobs.client.render.AMRenderTypes;
import com.github.alexthe666.alexsmobs.client.render.CitadelEntityModelBridge;
import com.github.alexthe666.alexsmobs.client.render.RenderCockroach;
import com.github.alexthe666.alexsmobs.entity.EntityCockroach;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class LayerCockroachMaracas extends RenderLayer<LivingEntityRenderState, CitadelEntityModelBridge<EntityCockroach>> {

    private ItemStack stack;
    private final ModelSombrero sombrero;
    private static final Identifier SOMBRERO_TEX = Identifier.parse("alexsmobs:textures/armor/sombrero.png");

    public LayerCockroachMaracas(RenderCockroach render, EntityRendererProvider.Context renderManagerIn) {
        super(render);
        this.sombrero = new ModelSombrero(renderManagerIn.bakeLayer(AMModelLayers.SOMBRERO));

    }

    @Override
    public void submit(PoseStack matrixStackIn, SubmitNodeCollector bufferIn, int packedLightIn, LivingEntityRenderState state, float netHeadYaw, float headPitch) {
        EntityCockroach entitylivingbaseIn = AlexsMobsClientKeys.getLiving(state) instanceof EntityCockroach c ? c : null;
        if (entitylivingbaseIn == null || !entitylivingbaseIn.hasMaracas()) {
            return;
        }
        if (this.stack == null) {
            this.stack = new ItemStack(AMItemRegistry.MARACA.get());
        }
        int overlay = LivingEntityRenderer.getOverlayCoords(state, 0.0F);
        matrixStackIn.pushPose();
        if (entitylivingbaseIn.isBaby()) {
            matrixStackIn.scale(0.65F, 0.65F, 0.65F);
            matrixStackIn.translate(0.0D, 0.815D, 0.125D);
        }
        matrixStackIn.pushPose();
        translateToHand(0, matrixStackIn);
        matrixStackIn.translate(-0.25F, 0.0F, 0);
        matrixStackIn.scale(1.4F, 1.4F, 1.4F);
        matrixStackIn.rotate(Axis.XP.rotationDegrees(-90F));
        matrixStackIn.rotate(Axis.ZP.rotationDegrees(60F));
        ItemStackRenderState _irs1 = new ItemStackRenderState();
        Minecraft.getInstance().getItemModelResolver().updateForLiving(_irs1, stack, ItemDisplayContext.GROUND, entitylivingbaseIn);
        _irs1.submit(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 0);
        matrixStackIn.popPose();
        matrixStackIn.pushPose();
        translateToHand(1, matrixStackIn);
        matrixStackIn.translate(0.25F, 0.0F, 0);
        matrixStackIn.scale(1.4F, 1.4F, 1.4F);
        matrixStackIn.rotate(Axis.XP.rotationDegrees(90F));
        matrixStackIn.rotate(Axis.ZP.rotationDegrees(-120F));
        ItemStackRenderState _irs2 = new ItemStackRenderState();
        Minecraft.getInstance().getItemModelResolver().updateForLiving(_irs2, stack, ItemDisplayContext.GROUND, entitylivingbaseIn);
        _irs2.submit(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 0);
        matrixStackIn.popPose();
        matrixStackIn.pushPose();
        translateToHand(2, matrixStackIn);
        matrixStackIn.translate(-0.35F, 0.0F, 0);
        matrixStackIn.scale(1.4F, 1.4F, 1.4F);
        matrixStackIn.rotate(Axis.XP.rotationDegrees(-90F));
        matrixStackIn.rotate(Axis.ZP.rotationDegrees(60F));
        ItemStackRenderState _irs3 = new ItemStackRenderState();
        Minecraft.getInstance().getItemModelResolver().updateForLiving(_irs3, stack, ItemDisplayContext.GROUND, entitylivingbaseIn);
        _irs3.submit(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 0);
        matrixStackIn.popPose();
        matrixStackIn.pushPose();
        translateToHand(3, matrixStackIn);
        matrixStackIn.translate(0.35F, 0.0F, 0);
        matrixStackIn.scale(1.4F, 1.4F, 1.4F);
        matrixStackIn.rotate(Axis.XP.rotationDegrees(90F));
        matrixStackIn.rotate(Axis.ZP.rotationDegrees(-120F));
        ItemStackRenderState _irs4 = new ItemStackRenderState();
        Minecraft.getInstance().getItemModelResolver().updateForLiving(_irs4, stack, ItemDisplayContext.GROUND, entitylivingbaseIn);
        _irs4.submit(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 0);
        matrixStackIn.popPose();
        if (!entitylivingbaseIn.isHeadless()) {
            matrixStackIn.pushPose();
            translateToHand(4, matrixStackIn);
            matrixStackIn.rotate(Axis.XP.rotationDegrees(60F * entitylivingbaseIn.danceProgress * 0.2F));
            matrixStackIn.translate(0.0F, 0.15F - entitylivingbaseIn.danceProgress * 0.008F, 0.02F);
            matrixStackIn.scale(0.8F, 0.8F, 0.8F);
            bufferIn.submitCustomGeometry(matrixStackIn, AMRenderTypes.entityCutoutNoCull(SOMBRERO_TEX), (pose, consumer) -> {
                PoseStack renderPose = new PoseStack();
                renderPose.pushPose();
                renderPose.last().set(pose);
                sombrero.renderToBuffer(renderPose, consumer, packedLightIn, overlay, -1);
                renderPose.popPose();
            });
            matrixStackIn.popPose();
        }
        matrixStackIn.popPose();
    }

    protected void translateToHand(int hand, PoseStack matrixStack) {
        ModelCockroach m = cockroachModel();
        m.root.translateAndRotate(matrixStack);
        m.abdomen.translateAndRotate(matrixStack);
        if (hand == 0) {
            m.right_leg_front.translateAndRotate(matrixStack);
        } else if (hand == 1) {
            m.left_leg_front.translateAndRotate(matrixStack);
        } else if (hand == 2) {
            m.right_leg_mid.translateAndRotate(matrixStack);
        } else if (hand == 3) {
            m.left_leg_mid.translateAndRotate(matrixStack);
        } else {
            m.neck.translateAndRotate(matrixStack);
            m.head.translateAndRotate(matrixStack);
        }
    }

    private ModelCockroach cockroachModel() {
        return (ModelCockroach) ((CitadelEntityModelBridge<?>) getParentModel()).citadel();
    }
}
