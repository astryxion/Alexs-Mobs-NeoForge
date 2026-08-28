package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.entity.EntityCachalotEcho;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public class RenderCachalotEcho extends EntityRenderer<EntityCachalotEcho, RenderCachalotEcho.CachalotEchoRenderState> {
    private static final Identifier TEXTURE_0 = Identifier.parse("alexsmobs:textures/entity/cachalot/whale_echo_0.png");
    private static final Identifier TEXTURE_1 = Identifier.parse("alexsmobs:textures/entity/cachalot/whale_echo_1.png");
    private static final Identifier TEXTURE_2 = Identifier.parse("alexsmobs:textures/entity/cachalot/whale_echo_2.png");
    private static final Identifier TEXTURE_3 = Identifier.parse("alexsmobs:textures/entity/cachalot/whale_echo_3.png");
    private static final Identifier GREEN_TEXTURE_0 = Identifier.parse("alexsmobs:textures/entity/cachalot/whale_echo_0_green.png");
    private static final Identifier GREEN_TEXTURE_1 = Identifier.parse("alexsmobs:textures/entity/cachalot/whale_echo_1_green.png");
    private static final Identifier GREEN_TEXTURE_2 = Identifier.parse("alexsmobs:textures/entity/cachalot/whale_echo_2_green.png");
    private static final Identifier GREEN_TEXTURE_3 = Identifier.parse("alexsmobs:textures/entity/cachalot/whale_echo_3_green.png");

    public static final class CachalotEchoRenderState extends EntityRenderState {
        public float yRot;
        public float xRot;
        public boolean fasterAnimation;
        public boolean green;
    }

    public RenderCachalotEcho(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public CachalotEchoRenderState createRenderState() {
        return new CachalotEchoRenderState();
    }

    @Override
    public void extractRenderState(EntityCachalotEcho entity, CachalotEchoRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.yRot = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        state.xRot = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        state.fasterAnimation = entity.isFasterAnimation();
        state.green = entity.isGreen();
    }

    @Override
    public void submit(CachalotEchoRenderState state, PoseStack matrixStackIn, SubmitNodeCollector collector, CameraRenderState cameraState) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0D, 0.25F, 0.0D);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(state.xRot));
        int arcs = Mth.clamp(Mth.floor(state.ageInTicks / 5F), 1, 4);
        matrixStackIn.translate(0.0D, 0.0F, 0.4D);
        for (int i = 0; i < arcs; i++) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0, 0, -0.5F * i);
            renderArc(matrixStackIn, collector, (i + 1) * 5, state.fasterAnimation, state.green);
            matrixStackIn.popPose();
        }
        matrixStackIn.popPose();
        super.submit(state, matrixStackIn, collector, cameraState);
    }

    private void renderArc(PoseStack matrixStackIn, SubmitNodeCollector collector, int age, boolean fast, boolean green) {
        matrixStackIn.pushPose();
        Identifier res;
        if (fast) {
            res = getEntityTextureFaster(age, green);
        } else {
            res = getEntityTexture(age);
        }
        collector.submitCustomGeometry(matrixStackIn, AMRenderTypes.entityCutoutNoCull(res), (pose, ivertexbuilder) -> {
            Matrix4f poseMatrix = pose.pose();
            this.drawVertex(pose, poseMatrix, ivertexbuilder, -1, 0, -1, 0, 0, 1, 0, 1, 240);
            this.drawVertex(pose, poseMatrix, ivertexbuilder, -1, 0, 1, 0, 1, 1, 0, 1, 240);
            this.drawVertex(pose, poseMatrix, ivertexbuilder, 1, 0, 1, 1, 1, 1, 0, 1, 240);
            this.drawVertex(pose, poseMatrix, ivertexbuilder, 1, 0, -1, 1, 0, 1, 0, 1, 240);
        });
        matrixStackIn.popPose();
    }

    public Identifier getTextureLocation(CachalotEchoRenderState state) {
        return TEXTURE_0;
    }

    public void drawVertex(PoseStack.Pose pose, Matrix4f poseMatrix, VertexConsumer consumer, int x, int y, int z, float u, float v, int normalX, int normalY, int normalZ, int packedLight) {
        consumer.addVertex(poseMatrix, (float) x, (float) y, (float) z)
            .setColor(255, 255, 255, 255)
            .setUv(u, v)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setLight(packedLight)
            .setNormal(pose, (float) normalX, (float) normalZ, (float) normalY);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public Identifier getEntityTexture(int age) {
        if (age < 5) {
            return TEXTURE_0;
        } else if (age < 10) {
            return TEXTURE_1;
        } else if (age < 15) {
            return TEXTURE_2;
        } else {
            return TEXTURE_3;
        }
    }

    public Identifier getEntityTextureFaster(int age, boolean green) {
        if (age < 3) {
            return green ? GREEN_TEXTURE_0 : TEXTURE_0;
        } else if (age < 6) {
            return green ? GREEN_TEXTURE_1 : TEXTURE_1;
        } else if (age < 9) {
            return green ? GREEN_TEXTURE_2 : TEXTURE_2;
        } else {
            return green ? GREEN_TEXTURE_3 : TEXTURE_3;
        }
    }
}
