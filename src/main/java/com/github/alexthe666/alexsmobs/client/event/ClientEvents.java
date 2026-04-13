package com.github.alexthe666.alexsmobs.client.event;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.ClientProxy;
import com.github.alexthe666.alexsmobs.client.AlexsMobsClientKeys;
import com.github.alexthe666.alexsmobs.client.model.ModelWanderingVillagerRider;
import com.github.alexthe666.alexsmobs.client.model.layered.AMModelLayers;
import com.github.alexthe666.alexsmobs.client.model.ModelRockyChestplateRolling;
import com.github.alexthe666.alexsmobs.client.render.AMItemstackRenderer;
import com.github.alexthe666.alexsmobs.client.render.AMRenderTypes;
import com.github.alexthe666.alexsmobs.client.render.RenderVineLasso;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.effect.EffectClinging;
import com.github.alexthe666.alexsmobs.effect.EffectPowerDown;
import com.github.alexthe666.alexsmobs.entity.EntityBaldEagle;
import com.github.alexthe666.alexsmobs.entity.EntityBlueJay;
import com.github.alexthe666.alexsmobs.entity.EntityElephant;
import com.github.alexthe666.alexsmobs.entity.IFalconry;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.alexsmobs.entity.util.RockyChestplateUtil;
import com.github.alexthe666.alexsmobs.entity.util.VineLassoUtil;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.item.ItemDimensionalCarver;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.client.event.EventGetFluidRenderType;
import com.github.alexthe666.citadel.client.event.EventGetOutlineColor;
import com.github.alexthe666.citadel.client.event.EventGetStarBrightness;
import com.github.alexthe666.citadel.client.event.EventPosePlayerHand;
import com.google.common.base.MoreObjects;
import net.minecraft.client.renderer.RenderPipelines;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.TriState;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
public class ClientEvents {

    private static final Identifier ROCKY_CHESTPLATE_TEXTURE = Identifier.parse("alexsmobs:textures/armor/rocky_chestplate.png");
    private static final ModelRockyChestplateRolling ROCKY_CHESTPLATE_MODEL = new ModelRockyChestplateRolling();

    private boolean previousLavaVision = false;
    public long lastStaticTick = -1;
    public static int renderStaticScreenFor = 0;

    @SubscribeEvent
    public void onOutlineEntityColor(EventGetOutlineColor event) {
        if(event.getEntityIn() instanceof Enemy && AlexsMobs.PROXY.getSingingBlueJayId() != -1){
            Entity entity = event.getEntityIn().level().getEntity(AlexsMobs.PROXY.getSingingBlueJayId());
            if(entity instanceof EntityBlueJay jay && jay.isAlive() && jay.isMakingMonstersBlue()){
                event.setColor(0X4B95FE);
                event.setResult(TriState.TRUE);
            }
        }
        if (event.getEntityIn() instanceof ItemEntity && ((ItemEntity) event.getEntityIn()).getItem().is(AMTagRegistry.VOID_WORM_DROPS)){
            int fromColor = 0;
            int toColor = 0X21E5FF;
            float startR = (float) (fromColor >> 16 & 255) / 255.0F;
            float startG = (float) (fromColor >> 8 & 255) / 255.0F;
            float startB = (float) (fromColor & 255) / 255.0F;
            float endR = (float) (toColor >> 16 & 255) / 255.0F;
            float endG = (float) (toColor >> 8 & 255) / 255.0F;
            float endB = (float) (toColor & 255) / 255.0F;
            float f = (float) (Math.cos(0.4F * (event.getEntityIn().tickCount + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true))) + 1.0F) * 0.5F;
            float r = (endR - startR) * f + startR;
            float g = (endG - startG) * f + startG;
            float b = (endB - startB) * f + startB;
            int j = ((((int) (r * 255)) & 0xFF) << 16) |
                    ((((int) (g * 255)) & 0xFF) << 8) |
                    ((((int) (b * 255)) & 0xFF) << 0);
            event.setColor(j);
            event.setResult(TriState.TRUE);
        }
    }

    @SubscribeEvent
    public void onGetStarBrightness(EventGetStarBrightness event) {
        if (Minecraft.getInstance().player.hasEffect(AMEffectRegistry.POWER_DOWN)) {
            if (Minecraft.getInstance().player.getEffect(AMEffectRegistry.POWER_DOWN) != null) {
                MobEffectInstance instance = Minecraft.getInstance().player.getEffect(AMEffectRegistry.POWER_DOWN);
                EffectPowerDown powerDown = (EffectPowerDown) instance.getEffect();
                int duration = instance.getDuration();
                float partialTicks = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
                float f = (Math.min(powerDown.getActiveTime(), duration) + partialTicks) * 0.1F;
                event.setBrightness(0);
                event.setResult(TriState.TRUE);
            }

        }
    }

    @SubscribeEvent
    public void onFogColor(ViewportEvent.ComputeFogColor event) {
        if (Minecraft.getInstance().player == null) {
            return;
        }
        if (Minecraft.getInstance().player.hasEffect(AMEffectRegistry.POWER_DOWN)) {
            if (Minecraft.getInstance().player.getEffect(AMEffectRegistry.POWER_DOWN) != null) {
                event.setBlue(0);
                event.setRed(0);
                event.setGreen(0);
            }

        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onFogDensity(ViewportEvent.RenderFog event) {
        if (Minecraft.getInstance().player == null) {
            return;
        }
        FogType fogType = event.getCamera().getFluidInCamera();
        if (Minecraft.getInstance().player.hasEffect(AMEffectRegistry.LAVA_VISION) && fogType == FogType.LAVA) {
            event.setNearPlaneDistance(-8.0F);
            event.setFarPlaneDistance(50.0F);
        }
        if (Minecraft.getInstance().player.hasEffect(AMEffectRegistry.POWER_DOWN) && fogType == FogType.NONE) {
            if (Minecraft.getInstance().player.getEffect(AMEffectRegistry.POWER_DOWN) != null) {
                float initEnd = event.getFarPlaneDistance();
                if (!Float.isFinite(initEnd) || initEnd <= 0.0F) {
                    sanitizeFogPlanes(event.getFogData());
                    return;
                }
                MobEffectInstance instance = Minecraft.getInstance().player.getEffect(AMEffectRegistry.POWER_DOWN);
                EffectPowerDown powerDown = (EffectPowerDown) instance.getEffect();
                int duration = instance.getDuration();
                float partialTicks = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
                float f = Math.min(20, (Math.min(powerDown.getActiveTime() + partialTicks, duration + partialTicks))) * 0.05F;
                event.setNearPlaneDistance(-8.0F);
                float f1 = 8.0F + (1 - f) * Math.max(0, initEnd - 8.0F);
                if (!Float.isFinite(f1)) {
                    sanitizeFogPlanes(event.getFogData());
                    return;
                }
                event.setFarPlaneDistance(Math.max(f1, event.getNearPlaneDistance() + 16.0F));
            }

        }
        sanitizeFogPlanes(event.getFogData());
    }

    /** Avoid NaN / inverted fog planes that blow up sky shaders (full white screen). */
    private static void sanitizeFogPlanes(FogData fogData) {
        float start = fogData.environmentalStart;
        float end = fogData.environmentalEnd;
        if (!Float.isFinite(start) || !Float.isFinite(end)) {
            return;
        }
        if (end <= start) {
            fogData.environmentalEnd = start + 32.0F;
        }
    }

    @SubscribeEvent
    public void onPreRenderEntity(RenderLivingEvent.Pre<?, ?, ?> event) {
        LivingEntity entity = AlexsMobsClientKeys.getLiving(event.getRenderState());
        if (entity != null && RockyChestplateUtil.isRockyRolling(entity)) {
            event.setCanceled(true);
            event.getPoseStack().pushPose();
            float limbSwing = entity.walkAnimation.position() - entity.walkAnimation.speed() * (1.0F - event.getPartialTick());
            float limbSwingAmount = entity.walkAnimation.speed(event.getPartialTick());
            float yRot = entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * event.getPartialTick();
            float roll = entity.walkAnimation.position(event.getPartialTick());
            int packedLight = ((EntityRenderState) event.getRenderState()).lightCoords;
            boolean foil = entity.getItemBySlot(EquipmentSlot.CHEST).hasFoil();
            event.getPoseStack().translate(0.0D, entity.getBbHeight() - entity.getBbHeight() * 0.5F, 0.0D);
            event.getPoseStack().mulPose(Axis.YN.rotationDegrees(180F + yRot));
            event.getPoseStack().mulPose(Axis.ZP.rotationDegrees(180.0F));
            event.getPoseStack().mulPose(Axis.XP.rotationDegrees(100F * roll));
            ROCKY_CHESTPLATE_MODEL.setupAnim(entity, limbSwing, limbSwingAmount, entity.tickCount + event.getPartialTick(), 0, 0);
            SubmitNodeCollector collector = event.getSubmitNodeCollector();
            collector.submitCustomGeometry(event.getPoseStack(), RenderTypes.armorCutoutNoCull(ROCKY_CHESTPLATE_TEXTURE), (pose, vertexConsumer) -> {
                PoseStack local = new PoseStack();
                local.pushPose();
                local.last().set(pose);
                ROCKY_CHESTPLATE_MODEL.renderToBuffer(local, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
                local.popPose();
            });
            if (foil) {
                collector.submitCustomGeometry(event.getPoseStack(), RenderTypes.armorEntityGlint(), (pose, vertexConsumer) -> {
                    PoseStack local = new PoseStack();
                    local.pushPose();
                    local.last().set(pose);
                    ROCKY_CHESTPLATE_MODEL.renderToBuffer(local, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
                    local.popPose();
                });
            }
            event.getPoseStack().popPose();
            NeoForge.EVENT_BUS.post(new RenderLivingEvent.Post(event.getRenderState(), event.getRenderer(), event.getPartialTick(), event.getPoseStack(), event.getSubmitNodeCollector()));
            return;
        }
        if (entity != null && entity instanceof WanderingTrader && entity.getType() == EntityType.WANDERING_TRADER) {
            if (entity.getVehicle() instanceof EntityElephant) {
                LivingEntityRenderer<?, ?, ?> renderer = event.getRenderer();
                if (!(renderer.getModel() instanceof ModelWanderingVillagerRider)) {
                    ModelWanderingVillagerRider replacement = new ModelWanderingVillagerRider(Minecraft.getInstance().getEntityModels().bakeLayer(AMModelLayers.SITTING_WANDERING_VILLAGER));
                    try {
                        java.lang.reflect.Field modelField = LivingEntityRenderer.class.getDeclaredField("model");
                        modelField.setAccessible(true);
                        modelField.set(renderer, replacement);
                    } catch (ReflectiveOperationException ex) {
                        AlexsMobs.LOGGER.warn("Failed to apply wandering trader elephant rider model", ex);
                    }
                }
            }
        }
        if (entity != null && (entity.hasEffect(AMEffectRegistry.CLINGING) && EffectClinging.isUpsideDown(entity) || entity.hasEffect(AMEffectRegistry.DEBILITATING_STING) && entity.entityTags().contains("arthropod") && entity.getBbWidth() > entity.getBbHeight())) {
            event.getPoseStack().pushPose();
            event.getPoseStack().translate(0.0D, entity.getBbHeight() + 0.1F, 0.0D);
            event.getPoseStack().mulPose(Axis.ZP.rotationDegrees(180.0F));
            entity.yBodyRotO = -entity.yBodyRotO;
            entity.yBodyRot = -entity.yBodyRot;
            entity.yHeadRotO = -entity.yHeadRotO;
            entity.yHeadRot = -entity.yHeadRot;
        }
        if (entity != null && entity.hasEffect(AMEffectRegistry.ENDER_FLU)) {
            event.getPoseStack().pushPose();
            event.getPoseStack().mulPose(Axis.YP.rotationDegrees((float) (Math.cos((double) entity.tickCount * 7F) * Math.PI * (double) 1.2F)));
            float vibrate = 0.05F;
            event.getPoseStack().translate((entity.getRandom().nextFloat() - 0.5F) * vibrate, (entity.getRandom().nextFloat() - 0.5F) * vibrate, (entity.getRandom().nextFloat() - 0.5F) * vibrate);
        }
    }

    @SubscribeEvent
    public void onPostRenderEntity(RenderLivingEvent.Post<?, ?, ?> event) {
        LivingEntity entity = AlexsMobsClientKeys.getLiving(event.getRenderState());
        if (entity != null && RockyChestplateUtil.isRockyRolling(entity)) {
            return;
        }
        if (entity != null && entity.hasEffect(AMEffectRegistry.ENDER_FLU)) {
            event.getPoseStack().popPose();
        }
        if (entity != null && (entity.hasEffect(AMEffectRegistry.CLINGING) && EffectClinging.isUpsideDown(entity) || entity.hasEffect(AMEffectRegistry.DEBILITATING_STING) && entity.entityTags().contains("arthropod") && entity.getBbWidth() > entity.getBbHeight())) {
            event.getPoseStack().popPose();
            entity.yBodyRotO = -entity.yBodyRotO;
            entity.yBodyRot = -entity.yBodyRot;
            entity.yHeadRotO = -entity.yHeadRotO;
            entity.yHeadRot = -entity.yHeadRot;
        }
        if (entity != null && VineLassoUtil.hasLassoData(entity) && !(entity instanceof Player)) {
            Entity lassoedOwner = VineLassoUtil.getLassoedTo(entity);
            if (lassoedOwner instanceof LivingEntity && lassoedOwner != entity) {
                double d0 = Mth.lerp(event.getPartialTick(), entity.xOld, entity.getX());
                double d1 = Mth.lerp(event.getPartialTick(), entity.yOld, entity.getY());
                double d2 = Mth.lerp(event.getPartialTick(), entity.zOld, entity.getZ());
                event.getPoseStack().pushPose();
                event.getPoseStack().translate(-d0, -d1, -d2);
                RenderVineLasso.renderVine(entity, event.getPartialTick(), event.getPoseStack(), event.getSubmitNodeCollector(), (LivingEntity) lassoedOwner, ((LivingEntity) lassoedOwner).getMainArm() == HumanoidArm.LEFT, 0.1F);
                event.getPoseStack().popPose();
            }
        }
    }

    @SubscribeEvent
    public void onPoseHand(EventPosePlayerHand event) {
        LivingEntity player = (LivingEntity) event.getEntityIn();
        float f = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
        boolean leftHand = false;
        boolean usingLasso = player.isUsingItem() && player.getUseItem().is(AMItemRegistry.VINE_LASSO.get());
        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == AMItemRegistry.VINE_LASSO.get()) {
            leftHand = player.getMainArm() == HumanoidArm.LEFT;
        } else if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() == AMItemRegistry.VINE_LASSO.get()) {
            leftHand = player.getMainArm() != HumanoidArm.LEFT;
        }
        if (leftHand && event.isLeftHand() && usingLasso) {
            //float swing = (float) Math.sin(player.tickCount + f) * 0.5F;
            event.setResult(TriState.TRUE);
            event.getModel().leftArm.xRot = Maths.rad(-120F) + Mth.sin(player.tickCount + f) * 0.5F;
            event.getModel().leftArm.yRot = Maths.rad(-20F) + Mth.cos(player.tickCount + f) * 0.5F;
        }
        if (!leftHand && !event.isLeftHand() && usingLasso) {
            event.setResult(TriState.TRUE);
            event.getModel().rightArm.xRot = Maths.rad(-120F) + Mth.sin(player.tickCount + f) * 0.5F;
            event.getModel().rightArm.yRot = Maths.rad(20F) - Mth.cos(player.tickCount + f) * 0.5F;
        }
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        if (Minecraft.getInstance().getCameraEntity() instanceof IFalconry) {
            event.setCanceled(true);
        }
        if (!Minecraft.getInstance().player.getPassengers().isEmpty() && event.getHand() == InteractionHand.MAIN_HAND) {
            Player player = Minecraft.getInstance().player;
            boolean leftHand = false;
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == AMItemRegistry.FALCONRY_GLOVE.get()) {
                leftHand = player.getMainArm() == HumanoidArm.LEFT;
            } else if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() == AMItemRegistry.FALCONRY_GLOVE.get()) {
                leftHand = player.getMainArm() != HumanoidArm.LEFT;
            }
            for (Entity entity : player.getPassengers()) {
                if (entity instanceof IFalconry falconry) {
                    float yaw = player.yBodyRotO + (player.yBodyRot - player.yBodyRotO) * event.getPartialTick();
                    ClientProxy.currentUnrenderedEntities.remove(entity.getUUID());
                    PoseStack matrixStackIn = event.getPoseStack();
                    matrixStackIn.pushPose();
                    matrixStackIn.scale(0.5F, 0.5F, 0.5F);
                    matrixStackIn.translate(leftHand ? -falconry.getHandOffset() : falconry.getHandOffset(), -0.6F, -1F);
                    matrixStackIn.mulPose(Axis.YP.rotationDegrees(yaw));
                    if (leftHand) {
                        matrixStackIn.mulPose(Axis.YP.rotationDegrees(90));
                    } else {
                        matrixStackIn.mulPose(Axis.YN.rotationDegrees(90));
                    }
                    renderEntity(entity, 0, 0, 0, 0, event.getPartialTick(), matrixStackIn, event.getSubmitNodeCollector());
                    matrixStackIn.popPose();
                    ClientProxy.currentUnrenderedEntities.add(entity.getUUID());
                }
            }
        }
        if (Minecraft.getInstance().player.getUseItem().getItem() instanceof ItemDimensionalCarver && event.getItemStack().getItem() instanceof ItemDimensionalCarver) {
            PoseStack matrixStackIn = event.getPoseStack();
            matrixStackIn.pushPose();
            ItemInHandRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer();
            InteractionHand hand = MoreObjects.firstNonNull(Minecraft.getInstance().player.swingingArm, InteractionHand.MAIN_HAND);
            float f = Minecraft.getInstance().player.getAttackAnim(event.getPartialTick());
            //float f1 = Mth.lerp(event.getPartialTick(), Minecraft.getInstance().player.xRotO, Minecraft.getInstance().player.getXRot());
            float f5 = -0.4F * Mth.sin(Mth.sqrt(f) * Mth.PI);
            float f6 = 0.2F * Mth.sin(Mth.sqrt(f) * Mth.TWO_PI);
            float f10 = -0.2F * Mth.sin(f * Mth.PI);
            HumanoidArm handside = hand == InteractionHand.MAIN_HAND ? Minecraft.getInstance().player.getMainArm() : Minecraft.getInstance().player.getMainArm().getOpposite();
            boolean flag3 = handside == HumanoidArm.RIGHT;
            int l = flag3 ? 1 : -1;
            matrixStackIn.translate((float) l * f5, f6, f10);
        }
    }

    public <E extends Entity> void renderEntity(E entityIn, double x, double y, double z, float yaw, float partialTicks, PoseStack matrixStack, SubmitNodeCollector collector) {
        ClientProxy.submitEntityInWorld(entityIn, x, y, z, yaw, partialTicks, matrixStack, collector);
    }

    // @SubscribeEvent
    // public void onRenderNameplate(RenderNameTagEvent event) {
    //     if (Minecraft.getInstance().getCameraEntity() instanceof EntityBaldEagle && event.getEntity() == Minecraft.getInstance().player) {
    //         if (Minecraft.getInstance().hasSingleplayerServer()) {
    //             event.setResult(net.neoforged.neoforge.common.util.TriState.FALSE);
    //         }
    //     }
    // }

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderLevelStageEvent.AfterSky event) {
            if (!AMConfig.shadersCompat) {
                if (Minecraft.getInstance().player.hasEffect(AMEffectRegistry.LAVA_VISION)) {
                    if (!previousLavaVision) {
                        // Note: liquidBlockRenderer is private in 1.21 - lava vision fluid swap disabled
                        // Would need mixin to access Minecraft.getInstance().getBlockRenderer().liquidBlockRenderer
                        updateAllChunks();
                    }
                } else {
                    if (previousLavaVision) {
                        // Note: liquidBlockRenderer is private in 1.21 - lava vision fluid swap disabled
                        updateAllChunks();
                    }
                }
                previousLavaVision = Minecraft.getInstance().player.hasEffect(AMEffectRegistry.LAVA_VISION);
            }
            if (Minecraft.getInstance().getCameraEntity() instanceof EntityBaldEagle) {
                EntityBaldEagle eagle = (EntityBaldEagle) Minecraft.getInstance().getCameraEntity();
                LocalPlayer playerEntity = Minecraft.getInstance().player;

                if (((EntityBaldEagle) Minecraft.getInstance().getCameraEntity()).shouldHoodedReturn() || eagle.isRemoved()) {
                    Minecraft.getInstance().setCameraEntity(playerEntity);
                    Minecraft.getInstance().options.setCameraType(CameraType.values()[AlexsMobs.PROXY.getPreviousPOV()]);
                } else {
                    float rotX = Mth.wrapDegrees(playerEntity.getYRot() + playerEntity.yHeadRot);
                    float rotY = playerEntity.getXRot();
                    Entity over = null;
                    if (Minecraft.getInstance().hitResult instanceof EntityHitResult) {
                        over = ((EntityHitResult) Minecraft.getInstance().hitResult).getEntity();
                    } else {
                        Minecraft.getInstance().hitResult = null;
                    }
                    boolean loadChunks = playerEntity.level().getDefaultClockTime() % 10 == 0;
                    ((EntityBaldEagle) Minecraft.getInstance().getCameraEntity()).directFromPlayer(rotX, rotY, false, over);
                    // Send eagle controls to server
                    AlexsMobs.sendMSGToServer(new com.github.alexthe666.alexsmobs.network.MessageUpdateEagleControls(
                        Minecraft.getInstance().getCameraEntity().getId(), rotX, rotY, loadChunks, over == null ? -1 : over.getId()));
                }
            }
    }

    private void updateAllChunks() {
        // Force chunk rebuild - simplified approach for 1.21
        // viewArea is private, so we use the public API instead
        Minecraft.getInstance().levelRenderer.allChanged();
    }

    @SubscribeEvent
    public void onGetFluidRenderType(EventGetFluidRenderType event) {
        if (Minecraft.getInstance().player.hasEffect(AMEffectRegistry.LAVA_VISION) && (event.getFluidState().is(Fluids.LAVA) || event.getFluidState().is(Fluids.FLOWING_LAVA))) {
            event.setRenderType(RenderTypes.translucentMovingBlock());
            event.setResult(TriState.TRUE);
        }
    }

    @SubscribeEvent
    public void clientTick(net.neoforged.neoforge.client.event.ClientTickEvent.Pre event) {
        AMItemstackRenderer.incrementTick();
    }

    @SubscribeEvent
    public void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getEffect(AMEffectRegistry.EARTHQUAKE) != null && !Minecraft.getInstance().isPaused()) {
            int duration = Minecraft.getInstance().player.getEffect(AMEffectRegistry.EARTHQUAKE).getDuration();
            float f = (Math.min(10, duration) + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true)) * 0.1F;
            double intensity = (double)f * Minecraft.getInstance().options.screenEffectScale().get();
            RandomSource rng = Minecraft.getInstance().player.getRandom();
            // Camera shake effect using roll/pitch - Camera.move() is protected in 1.21
            float shakeX = (float)(rng.nextFloat() * 0.5F * intensity);
            float shakeY = (float)(rng.nextFloat() * 1.0F * intensity);
            event.setRoll(event.getRoll() + shakeX);
            event.setPitch(event.getPitch() + shakeY);
        }
    }

    @SubscribeEvent
    public void onPostGameOverlay(RenderGuiLayerEvent.Post event) {
        if(renderStaticScreenFor > 0){
            if (Minecraft.getInstance().player.isAlive() && lastStaticTick != Minecraft.getInstance().level.getGameTime()) {
                renderStaticScreenFor--;
            }
            float staticLevel = (renderStaticScreenFor / 60F);
            if (event.getName().equals(VanillaGuiLayers.CAMERA_OVERLAYS)) {
                var gui = event.getGuiGraphics();
                float ageInTicks = Minecraft.getInstance().level.getGameTime() + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
                float staticIndexX = (float) Math.sin(ageInTicks * 0.2F) * 2;
                float staticIndexY = (float) Math.cos(ageInTicks * 0.2F + 3F) * 2;
                float minU = 10 * staticIndexX * 0.125F;
                float maxU = 10 * (0.5F + staticIndexX * 0.125F);
                float minV = 10 * staticIndexY * 0.125F;
                float maxV = 10 * (0.125F + staticIndexY * 0.125F);
                int tint = ARGB.colorFromFloat(staticLevel, 1.0F, 1.0F, 1.0F);
                gui.blit(
                        RenderPipelines.GUI_TEXTURED,
                        AMRenderTypes.STATIC_TEXTURE,
                        0,
                        0,
                        minU,
                        minV,
                        gui.guiWidth(),
                        gui.guiHeight(),
                        (int) (maxU - minU),
                        (int) (maxV - minV),
                        tint);
            }
            lastStaticTick = Minecraft.getInstance().level.getGameTime();
        }
    }
}
