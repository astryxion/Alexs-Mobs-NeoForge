package com.github.alexthe666.alexsmobs.client.particle;

import com.github.alexthe666.alexsmobs.client.render.AMRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ParticleSkulkBoom extends Particle {
    public static final ParticleRenderType SKULK_BOOM = new ParticleRenderType("alexsmobs_skulk_boom", "SB");

    private float size;
    private float prevSize;
    private float alpha = 1.0F;
    private float prevAlpha;
    private final float alphaDecrease;

    private ParticleSkulkBoom(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z);
        Minecraft.getInstance().getTextureManager().getTexture(AMRenderTypes.SKULK_BOOM_TEXTURE);
        this.setSize(1, 0.1F);
        this.gravity = 0.0F;
        this.xd = motionX;
        this.yd = motionY;
        this.zd = motionZ;
        this.lifetime = 20 + this.random.nextInt(20);
        this.alphaDecrease = 1F / (float) Math.max(this.lifetime, 1);
        this.size = 0.3F;
        this.prevSize = this.size;
        this.prevAlpha = 1F;
    }

    @Override
    public void tick() {
        super.tick();
        this.prevSize = this.size;
        this.prevAlpha = this.alpha;
        this.size += 0.3F;
        this.xd *= 0.1D;
        this.yd *= 0.8D;
        this.zd *= 0.1D;
        if (this.alpha > 0.0F) {
            this.alpha = Math.max(this.alpha - this.alphaDecrease, 0.0F);
        }
        this.setSize(1 + this.size, 0.1F);
    }

    @Override
    public ParticleRenderType getGroup() {
        return SKULK_BOOM;
    }

    void submitBoom(SubmitNodeCollector collector, Camera camera, float partialTick) {
        Vec3 cameraPos = camera.position();
        float x = (float) (Mth.lerp(partialTick, this.xo, this.x) - cameraPos.x());
        float y = (float) (Mth.lerp(partialTick, this.yo, this.y) - cameraPos.y());
        float z = (float) (Mth.lerp(partialTick, this.zo, this.z) - cameraPos.z());
        float scale = this.prevSize + partialTick * (this.size - this.prevSize);
        float alphaLerp = this.prevAlpha + partialTick * (this.alpha - this.prevAlpha);
        Quaternionf flat = Axis.XP.rotationDegrees(90.0F);
        Vector3f[] corners = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)
        };
        for (Vector3f corner : corners) {
            corner.rotate(flat);
            corner.mul(scale);
            corner.add(x, y, z);
        }
        int color = ARGB.colorFromFloat(alphaLerp, 1.0F, 1.0F, 1.0F);
        PoseStack poseStack = new PoseStack();
        collector.submitCustomGeometry(poseStack, AMRenderTypes.getSkulkBoom(), (pose, consumer) -> {
            Matrix4f matrix = pose.pose();
            vertex(consumer, matrix, corners[0], 1.0F, 1.0F, color);
            vertex(consumer, matrix, corners[1], 1.0F, 0.0F, color);
            vertex(consumer, matrix, corners[2], 0.0F, 0.0F, color);
            vertex(consumer, matrix, corners[3], 0.0F, 1.0F, color);
        });
    }

    private static void vertex(VertexConsumer consumer, Matrix4f matrix, Vector3f pos, float u, float v, int color) {
        consumer.addVertex(matrix, pos.x(), pos.y(), pos.z())
                .setColor(color)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(240)
                .setNormal(0.0F, -1.0F, 0.0F);
    }

    public static final class SkulkBoomParticleGroup extends ParticleGroup<ParticleSkulkBoom> {
        public SkulkBoomParticleGroup(ParticleEngine engine) {
            super(engine);
        }

        @Override
        public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTick) {
            List<ParticleSkulkBoom> list = new ArrayList<>();
            for (ParticleSkulkBoom particle : this.particles) {
                if (frustum.pointInFrustum(particle.x, particle.y, particle.z)) {
                    list.add(particle);
                }
            }
            return new SkulkBoomState(list, partialTick);
        }
    }

    private record SkulkBoomState(List<ParticleSkulkBoom> particles, float partialTick) implements ParticleGroupRenderState {
        @Override
        public void submit(SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
            Camera camera = Minecraft.getInstance().gameRenderer.mainCamera();
            for (ParticleSkulkBoom particle : this.particles) {
                particle.submitBoom(collector, camera, this.partialTick);
            }
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new ParticleSkulkBoom(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
