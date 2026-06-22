package com.github.alexthe666.alexsmobs;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.client.AlexsMobsClientKeys;
import com.github.alexthe666.alexsmobs.client.color.AMSpawnEggTintSource;
import com.github.alexthe666.alexsmobs.client.ClientLayerRegistry;
import com.github.alexthe666.alexsmobs.client.event.ClientEvents;
import com.github.alexthe666.alexsmobs.client.gui.GUIAnimalDictionary;
import com.github.alexthe666.alexsmobs.client.gui.GUITransmutationTable;
import com.github.alexthe666.alexsmobs.client.particle.*;
import com.github.alexthe666.alexsmobs.client.render.*;
import com.github.alexthe666.alexsmobs.client.render.AMItemstackRenderer;
import com.github.alexthe666.alexsmobs.client.render.item.AMItemRenderProperties;
import com.github.alexthe666.alexsmobs.client.render.item.CustomArmorRenderProperties;
import com.github.alexthe666.alexsmobs.client.render.item.GhostlyPickaxeItemModel;
import com.github.alexthe666.alexsmobs.client.render.tile.RenderCapsid;
import com.github.alexthe666.alexsmobs.client.render.tile.RenderTransmutationTable;
import com.github.alexthe666.alexsmobs.client.render.tile.RenderVoidWormBeak;
import com.github.alexthe666.alexsmobs.client.sound.SoundBearMusicBox;
import com.github.alexthe666.alexsmobs.client.sound.SoundLaCucaracha;
import com.github.alexthe666.alexsmobs.client.sound.SoundWormBoss;
import com.github.alexthe666.alexsmobs.entity.*;
import com.github.alexthe666.alexsmobs.entity.util.RainbowUtil;
import com.github.alexthe666.alexsmobs.inventory.AMMenuRegistry;
import com.github.alexthe666.alexsmobs.item.*;
import com.github.alexthe666.alexsmobs.tileentity.AMTileEntityRegistry;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import com.google.common.reflect.TypeToken;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.FrameGraphSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleGroupsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
// FMLJavaModLoadingContext removed in 1.21

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
// @Mod.EventBusSubscriber removed - register client listeners from client setup / proxy instead
public class ClientProxy extends CommonProxy {

    public static final Int2ObjectMap<SoundBearMusicBox> BEAR_MUSIC_BOX_SOUND_MAP = new Int2ObjectOpenHashMap<>();
    public static final Int2ObjectMap<SoundLaCucaracha> COCKROACH_SOUND_MAP = new Int2ObjectOpenHashMap<>();
    public static final Int2ObjectMap<SoundWormBoss> WORMBOSS_SOUND_MAP = new Int2ObjectOpenHashMap<>();
    public static final List<UUID> currentUnrenderedEntities = new ArrayList<>();
    public static int voidPortalCreationTime = 0;
    /** Latest world-render camera; used by {@link AMItemstackRenderer} for entity-in-item preview. */
    public static volatile CameraRenderState lastCameraRenderState;
    public CameraType prevPOV = CameraType.FIRST_PERSON;
    public boolean initializedRainbowBuffers = false;
    private int pupfishChunkX = 0;
    private int pupfishChunkZ = 0;
    /** Mirrored for {@link PupfishLocatorInChunkProperty} (item model property; no entity reference). */
    private static volatile int pupfishChunkXModel = 0;
    private static volatile int pupfishChunkZModel = 0;
    private int singingBlueJayId = -1;
    private final ItemStack[] transmuteStacks = new ItemStack[3];

    @Override
    public void sendToServer(CustomPacketPayload message) {
        ClientPacketDistributor.sendToServer(message);
    }

    @SubscribeEvent
    public static void onItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(Identifier.fromNamespaceAndPath(AlexsMobs.MODID, "spawn_egg_layer"), AMSpawnEggTintSource.MAP_CODEC);
    }

    @SubscribeEvent
    public static void onBlockTintSources(RegisterColorHandlersEvent.BlockTintSources event) {
        AlexsMobs.LOGGER.info("loaded in block colorizer");
        BlockTintSource rainbow = new BlockTintSource() {
            @Override
            public int color(BlockState state) {
                return -1;
            }

            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                return level != null && pos != null ? RainbowUtil.calculateGlassColor(pos) : -1;
            }
        };
        event.register(ImmutableList.of(rainbow), AMBlockRegistry.RAINBOW_GLASS.get());
    }

    @SubscribeEvent
    public static void onRegisterConditionalItemModelProperties(RegisterConditionalItemModelPropertyEvent event) {
        event.register(Identifier.fromNamespaceAndPath(AlexsMobs.MODID, "blood_sprayer_empty"), BloodSprayerEmptyProperty.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(AlexsMobs.MODID, "hemolymph_blaster_empty"), HemolymphBlasterEmptyProperty.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(AlexsMobs.MODID, "tendon_whip_active"), TendonWhipActiveProperty.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(AlexsMobs.MODID, "pupfish_locator_in_chunk"), PupfishLocatorInChunkProperty.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(AlexsMobs.MODID, "sombrero_silly"), SombreroSillyProperty.MAP_CODEC);
    }

    @Override
    public void init(IEventBus bus) {
        bus.addListener(ClientProxy::onBakingCompleted);
        bus.addListener(ClientProxy::onRegisterClientExtensions);
        bus.addListener(ClientProxy::onBlockTintSources);
        bus.addListener(ClientProxy::onItemTintSources);
        bus.addListener(ClientProxy::onRegisterConditionalItemModelProperties);
        bus.addListener(ClientProxy::onRegisterRenderers);
        bus.addListener(ClientProxy::onRegisterRenderStateModifiers);
        bus.addListener(ClientLayerRegistry::onAddLayers);
        bus.addListener(ClientProxy::setupParticles);
        bus.addListener(ClientProxy::setupParticleGroups);
        bus.addListener(ClientProxy::onRegisterMenuScreens);
        bus.addListener(ClientProxy::onRegisterRenderPipelines);
    }

    @SubscribeEvent
    public static void onRegisterRenderPipelines(RegisterRenderPipelinesEvent event) {
        event.registerPipeline(AMRenderTypes.GHOST_PICKAXE_PIPELINE);
        event.registerPipeline(AMRenderTypes.UNDERMINER_PIPELINE);
    }
    public static void onRegisterRenderStateModifiers(RegisterRenderStateModifiersEvent event) {
        event.registerEntityModifier(
                new TypeToken<LivingEntityRenderer<? extends LivingEntity, LivingEntityRenderState, ?>>() {},
                (entity, renderState) -> renderState.setRenderData(AlexsMobsClientKeys.RENDER_STATE_LIVING_ENTITY, entity));
        event.registerEntityModifier(
                new TypeToken<EntityRenderer<? extends Entity, ? extends EntityRenderState>>() {},
                (entity, renderState) -> renderState.setRenderData(AlexsMobsClientKeys.RENDER_STATE_ENTITY, entity));
    }
    public static void onRegisterMenuScreens(net.neoforged.neoforge.client.event.RegisterMenuScreensEvent event) {
        event.register(AMMenuRegistry.TRANSMUTATION_TABLE.get(), GUITransmutationTable::new);
        AlexsMobs.LOGGER.info("Registered Transmutation Table screen");
    }

    /**
     * Renders an entity using the 26.1 submit pipeline (used from entity-attached layers and {@link ClientEvents}).
     */
    public static <E extends Entity> void submitEntityInWorld(E entityIn, double x, double y, double z, float yaw, float partialTicks, PoseStack matrixStack, SubmitNodeCollector collector) {
        EntityRenderer<? super E, ?> render = null;
        EntityRenderDispatcher manager = Minecraft.getInstance().getEntityRenderDispatcher();
        net.minecraft.client.renderer.state.level.CameraRenderState camera = lastCameraRenderState;
        try {
            render = manager.getRenderer(entityIn);

            if (render != null && camera != null) {
                try {
                    @SuppressWarnings("unchecked")
                    EntityRenderState state = manager.extractEntity(entityIn, partialTicks);
                    manager.submit(state, camera, x, y, z, matrixStack, collector);
                } catch (Throwable throwable1) {
                    throw new ReportedException(CrashReport.forThrowable(throwable1, "Rendering entity in world"));
                }
            }
        } catch (Throwable throwable3) {
            CrashReport crashreport = CrashReport.forThrowable(throwable3, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being rendered");
            entityIn.fillCrashReportCategory(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.addCategory("Renderer details");
            crashreportcategory1.setDetail("Assigned renderer", render);
            crashreportcategory1.setDetail("Rotation", Float.valueOf(yaw));
            crashreportcategory1.setDetail("Delta", Float.valueOf(partialTicks));
            throw new ReportedException(crashreport);
        }
    }
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AMEntityRegistry.GRIZZLY_BEAR.get(), RenderGrizzlyBear::new);
        event.registerEntityRenderer(AMEntityRegistry.ROADRUNNER.get(), RenderRoadrunner::new);
        event.registerEntityRenderer(AMEntityRegistry.BONE_SERPENT.get(), RenderBoneSerpent::new);
        event.registerEntityRenderer(AMEntityRegistry.BONE_SERPENT_PART.get(), RenderBoneSerpentPart::new);
        event.registerEntityRenderer(AMEntityRegistry.GAZELLE.get(), RenderGazelle::new);
        event.registerEntityRenderer(AMEntityRegistry.CROCODILE.get(), RenderCrocodile::new);
        event.registerEntityRenderer(AMEntityRegistry.FLY.get(), RenderFly::new);
        event.registerEntityRenderer(AMEntityRegistry.HUMMINGBIRD.get(), RenderHummingbird::new);
        event.registerEntityRenderer(AMEntityRegistry.ORCA.get(), RenderOrca::new);
        event.registerEntityRenderer(AMEntityRegistry.SUNBIRD.get(), RenderSunbird::new);
        event.registerEntityRenderer(AMEntityRegistry.GORILLA.get(), RenderGorilla::new);
        event.registerEntityRenderer(AMEntityRegistry.CRIMSON_MOSQUITO.get(), RenderCrimsonMosquito::new);
        event.registerEntityRenderer(AMEntityRegistry.MOSQUITO_SPIT.get(), RenderMosquitoSpit::new);
        event.registerEntityRenderer(AMEntityRegistry.RATTLESNAKE.get(), RenderRattlesnake::new);
        event.registerEntityRenderer(AMEntityRegistry.ENDERGRADE.get(), RenderEndergrade::new);
        event.registerEntityRenderer(AMEntityRegistry.HAMMERHEAD_SHARK.get(), RenderHammerheadShark::new);
        event.registerEntityRenderer(AMEntityRegistry.SHARK_TOOTH_ARROW.get(), RenderSharkToothArrow::new);
        event.registerEntityRenderer(AMEntityRegistry.LOBSTER.get(), RenderLobster::new);
        event.registerEntityRenderer(AMEntityRegistry.KOMODO_DRAGON.get(), RenderKomodoDragon::new);
        event.registerEntityRenderer(AMEntityRegistry.CAPUCHIN_MONKEY.get(), RenderCapuchinMonkey::new);
        event.registerEntityRenderer(AMEntityRegistry.TOSSED_ITEM.get(), RenderTossedItem::new);
        event.registerEntityRenderer(AMEntityRegistry.CENTIPEDE_HEAD.get(), RenderCentipedeHead::new);
        event.registerEntityRenderer(AMEntityRegistry.CENTIPEDE_BODY.get(), RenderCentipedeBody::new);
        event.registerEntityRenderer(AMEntityRegistry.CENTIPEDE_TAIL.get(), RenderCentipedeTail::new);
        event.registerEntityRenderer(AMEntityRegistry.WARPED_TOAD.get(), RenderWarpedToad::new);
        event.registerEntityRenderer(AMEntityRegistry.MOOSE.get(), RenderMoose::new);
        event.registerEntityRenderer(AMEntityRegistry.MIMICUBE.get(), RenderMimicube::new);
        event.registerEntityRenderer(AMEntityRegistry.RACCOON.get(), RenderRaccoon::new);
        event.registerEntityRenderer(AMEntityRegistry.BLOBFISH.get(), RenderBlobfish::new);
        event.registerEntityRenderer(AMEntityRegistry.SEAL.get(), RenderSeal::new);
        event.registerEntityRenderer(AMEntityRegistry.COCKROACH.get(), RenderCockroach::new);
        event.registerEntityRenderer(AMEntityRegistry.COCKROACH_EGG.get(), (render) -> {
            return new ThrownItemRenderer<>(render, 0.75F, true);
        });
        event.registerEntityRenderer(AMEntityRegistry.SHOEBILL.get(), RenderShoebill::new);
        event.registerEntityRenderer(AMEntityRegistry.ELEPHANT.get(), RenderElephant::new);
        event.registerEntityRenderer(AMEntityRegistry.SOUL_VULTURE.get(), RenderSoulVulture::new);
        event.registerEntityRenderer(AMEntityRegistry.SNOW_LEOPARD.get(), RenderSnowLeopard::new);
        event.registerEntityRenderer(AMEntityRegistry.SPECTRE.get(), RenderSpectre::new);
        event.registerEntityRenderer(AMEntityRegistry.CROW.get(), RenderCrow::new);
        event.registerEntityRenderer(AMEntityRegistry.ALLIGATOR_SNAPPING_TURTLE.get(), RenderAlligatorSnappingTurtle::new);
        event.registerEntityRenderer(AMEntityRegistry.MUNGUS.get(), RenderMungus::new);
        event.registerEntityRenderer(AMEntityRegistry.MANTIS_SHRIMP.get(), RenderMantisShrimp::new);
        event.registerEntityRenderer(AMEntityRegistry.GUSTER.get(), RenderGuster::new);
        event.registerEntityRenderer(AMEntityRegistry.SAND_SHOT.get(), RenderSandShot::new);
        event.registerEntityRenderer(AMEntityRegistry.GUST.get(), RenderGust::new);
        event.registerEntityRenderer(AMEntityRegistry.WARPED_MOSCO.get(), RenderWarpedMosco::new);
        event.registerEntityRenderer(AMEntityRegistry.HEMOLYMPH.get(), RenderHemolymph::new);
        event.registerEntityRenderer(AMEntityRegistry.STRADDLER.get(), RenderStraddler::new);
        event.registerEntityRenderer(AMEntityRegistry.STRADPOLE.get(), RenderStradpole::new);
        event.registerEntityRenderer(AMEntityRegistry.STRADDLEBOARD.get(), RenderStraddleboard::new);
        event.registerEntityRenderer(AMEntityRegistry.EMU.get(), RenderEmu::new);
        event.registerEntityRenderer(AMEntityRegistry.EMU_EGG.get(), (render) -> {
            return new ThrownItemRenderer<>(render, 0.75F, true);
        });
        event.registerEntityRenderer(AMEntityRegistry.PLATYPUS.get(), RenderPlatypus::new);
        event.registerEntityRenderer(AMEntityRegistry.DROPBEAR.get(), RenderDropBear::new);
        event.registerEntityRenderer(AMEntityRegistry.TASMANIAN_DEVIL.get(), RenderTasmanianDevil::new);
        event.registerEntityRenderer(AMEntityRegistry.KANGAROO.get(), RenderKangaroo::new);
        event.registerEntityRenderer(AMEntityRegistry.CACHALOT_WHALE.get(), RenderCachalotWhale::new);
        event.registerEntityRenderer(AMEntityRegistry.CACHALOT_ECHO.get(), RenderCachalotEcho::new);
        event.registerEntityRenderer(AMEntityRegistry.LEAFCUTTER_ANT.get(), RenderLeafcutterAnt::new);
        event.registerEntityRenderer(AMEntityRegistry.ENDERIOPHAGE.get(), RenderEnderiophage::new);
        event.registerEntityRenderer(AMEntityRegistry.ENDERIOPHAGE_ROCKET.get(), (render) -> {
            return new ThrownItemRenderer<>(render, 0.75F, true);
        });
        event.registerEntityRenderer(AMEntityRegistry.BALD_EAGLE.get(), RenderBaldEagle::new);
        event.registerEntityRenderer(AMEntityRegistry.TIGER.get(), RenderTiger::new);
        event.registerEntityRenderer(AMEntityRegistry.TARANTULA_HAWK.get(), RenderTarantulaHawk::new);
        event.registerEntityRenderer(AMEntityRegistry.VOID_WORM.get(), RenderVoidWormHead::new);
        event.registerEntityRenderer(AMEntityRegistry.VOID_WORM_PART.get(), RenderVoidWormBody::new);
        event.registerEntityRenderer(AMEntityRegistry.VOID_WORM_SHOT.get(), RenderVoidWormShot::new);
        event.registerEntityRenderer(AMEntityRegistry.VOID_PORTAL.get(), RenderVoidPortal::new);
        event.registerEntityRenderer(AMEntityRegistry.FRILLED_SHARK.get(), RenderFrilledShark::new);
        event.registerEntityRenderer(AMEntityRegistry.MIMIC_OCTOPUS.get(), RenderMimicOctopus::new);
        event.registerEntityRenderer(AMEntityRegistry.SEAGULL.get(), RenderSeagull::new);
        event.registerEntityRenderer(AMEntityRegistry.FROSTSTALKER.get(), RenderFroststalker::new);
        event.registerEntityRenderer(AMEntityRegistry.ICE_SHARD.get(), RenderIceShard::new);
        event.registerEntityRenderer(AMEntityRegistry.TUSKLIN.get(), RenderTusklin::new);
        event.registerEntityRenderer(AMEntityRegistry.LAVIATHAN.get(), RenderLaviathan::new);
        event.registerEntityRenderer(AMEntityRegistry.COSMAW.get(), RenderCosmaw::new);
        event.registerEntityRenderer(AMEntityRegistry.TOUCAN.get(), RenderToucan::new);
        event.registerEntityRenderer(AMEntityRegistry.MANED_WOLF.get(), RenderManedWolf::new);
        event.registerEntityRenderer(AMEntityRegistry.ANACONDA.get(), RenderAnaconda::new);
        event.registerEntityRenderer(AMEntityRegistry.ANACONDA_PART.get(), RenderAnacondaPart::new);
        event.registerEntityRenderer(AMEntityRegistry.VINE_LASSO.get(), RenderVineLasso::new);
        event.registerEntityRenderer(AMEntityRegistry.ANTEATER.get(), RenderAnteater::new);
        event.registerEntityRenderer(AMEntityRegistry.ROCKY_ROLLER.get(), RenderRockyRoller::new);
        event.registerEntityRenderer(AMEntityRegistry.FLUTTER.get(), RenderFlutter::new);
        event.registerEntityRenderer(AMEntityRegistry.POLLEN_BALL.get(), RenderPollenBall::new);
        event.registerEntityRenderer(AMEntityRegistry.GELADA_MONKEY.get(), RenderGeladaMonkey::new);
        event.registerEntityRenderer(AMEntityRegistry.JERBOA.get(), RenderJerboa::new);
        event.registerEntityRenderer(AMEntityRegistry.TERRAPIN.get(), RenderTerrapin::new);
        event.registerEntityRenderer(AMEntityRegistry.COMB_JELLY.get(), RenderCombJelly::new);
        event.registerEntityRenderer(AMEntityRegistry.COSMIC_COD.get(), RenderCosmicCod::new);
        event.registerEntityRenderer(AMEntityRegistry.BUNFUNGUS.get(), RenderBunfungus::new);
        event.registerEntityRenderer(AMEntityRegistry.BISON.get(), RenderBison::new);
        event.registerEntityRenderer(AMEntityRegistry.GIANT_SQUID.get(), RenderGiantSquid::new);
        event.registerEntityRenderer(AMEntityRegistry.SQUID_GRAPPLE.get(), RenderSquidGrapple::new);
        event.registerEntityRenderer(AMEntityRegistry.SEA_BEAR.get(), RenderSeaBear::new);
        event.registerEntityRenderer(AMEntityRegistry.DEVILS_HOLE_PUPFISH.get(), RenderDevilsHolePupfish::new);
        event.registerEntityRenderer(AMEntityRegistry.CATFISH.get(), RenderCatfish::new);
        event.registerEntityRenderer(AMEntityRegistry.FLYING_FISH.get(), RenderFlyingFish::new);
        event.registerEntityRenderer(AMEntityRegistry.SKELEWAG.get(), RenderSkelewag::new);
        event.registerEntityRenderer(AMEntityRegistry.RAIN_FROG.get(), RenderRainFrog::new);
        event.registerEntityRenderer(AMEntityRegistry.POTOO.get(), RenderPotoo::new);
        event.registerEntityRenderer(AMEntityRegistry.MUDSKIPPER.get(), RenderMudskipper::new);
        event.registerEntityRenderer(AMEntityRegistry.MUD_BALL.get(), RenderMudBall::new);
        event.registerEntityRenderer(AMEntityRegistry.RHINOCEROS.get(), RenderRhinoceros::new);
        event.registerEntityRenderer(AMEntityRegistry.SUGAR_GLIDER.get(), RenderSugarGlider::new);
        event.registerEntityRenderer(AMEntityRegistry.FARSEER.get(), RenderFarseer::new);
        event.registerEntityRenderer(AMEntityRegistry.SKREECHER.get(), RenderSkreecher::new);
        event.registerEntityRenderer(AMEntityRegistry.UNDERMINER.get(), RenderUnderminer::new);
        event.registerEntityRenderer(AMEntityRegistry.MURMUR.get(), RenderMurmurBody::new);
        event.registerEntityRenderer(AMEntityRegistry.MURMUR_HEAD.get(), RenderMurmurHead::new);
        event.registerEntityRenderer(AMEntityRegistry.TENDON_SEGMENT.get(), RenderTendonSegment::new);
        event.registerEntityRenderer(AMEntityRegistry.SKUNK.get(), RenderSkunk::new);
        event.registerEntityRenderer(AMEntityRegistry.FART.get(), RenderFart::new);
        event.registerEntityRenderer(AMEntityRegistry.BANANA_SLUG.get(), RenderBananaSlug::new);
        event.registerEntityRenderer(AMEntityRegistry.BLUE_JAY.get(), RenderBlueJay::new);
        event.registerEntityRenderer(AMEntityRegistry.CAIMAN.get(), RenderCaiman::new);
        event.registerEntityRenderer(AMEntityRegistry.TRIOPS.get(), RenderTriops::new);
        event.registerBlockEntityRenderer(AMTileEntityRegistry.CAPSID.get(), RenderCapsid::new);
        event.registerBlockEntityRenderer(AMTileEntityRegistry.VOID_WORM_BEAK.get(), RenderVoidWormBeak::new);
        event.registerBlockEntityRenderer(AMTileEntityRegistry.TRANSMUTATION_TABLE.get(), RenderTransmutationTable::new);
    }

    public void clientInit() {
        NeoForge.EVENT_BUS.register(new ClientEvents());
        NeoForge.EVENT_BUS.addListener((FrameGraphSetupEvent e) -> lastCameraRenderState = e.getCameraState());
        initRainbowBuffers();
    }

    private void initRainbowBuffers() {
        // BufferBuilder API changed in 1.21.1 - needs ByteBufferBuilder, Mode, and VertexFormat
        // Temporarily commented out until proper implementation
        // Minecraft.getInstance().renderBuffers().fixedBuffers.put(AMRenderTypes.COMBJELLY_RAINBOW_GLINT, new BufferBuilder(new ByteBufferBuilder(AMRenderTypes.COMBJELLY_RAINBOW_GLINT.bufferSize()), VertexFormat.Mode.QUADS, AMRenderTypes.COMBJELLY_RAINBOW_GLINT.format()));
        // Minecraft.getInstance().renderBuffers().fixedBuffers.put(AMRenderTypes.VOID_WORM_PORTAL_OVERLAY, new BufferBuilder(new ByteBufferBuilder(AMRenderTypes.VOID_WORM_PORTAL_OVERLAY.bufferSize()), VertexFormat.Mode.QUADS, AMRenderTypes.VOID_WORM_PORTAL_OVERLAY.format()));
        // Minecraft.getInstance().renderBuffers().fixedBuffers.put(AMRenderTypes.STATIC_PORTAL, new BufferBuilder(new ByteBufferBuilder(AMRenderTypes.STATIC_PORTAL.bufferSize()), VertexFormat.Mode.QUADS, AMRenderTypes.STATIC_PORTAL.format()));
        // Minecraft.getInstance().renderBuffers().fixedBuffers.put(AMRenderTypes.STATIC_PARTICLE, new BufferBuilder(new ByteBufferBuilder(AMRenderTypes.STATIC_PARTICLE.bufferSize()), VertexFormat.Mode.QUADS, AMRenderTypes.STATIC_PARTICLE.format()));
        // Minecraft.getInstance().renderBuffers().fixedBuffers.put(AMRenderTypes.STATIC_ENTITY, new BufferBuilder(new ByteBufferBuilder(AMRenderTypes.STATIC_ENTITY.bufferSize()), VertexFormat.Mode.QUADS, AMRenderTypes.STATIC_ENTITY.format()));
        initializedRainbowBuffers = true;
    }

    private static void onBakingCompleted(final ModelEvent.ModifyBakingResult e) {
        var itemModels = e.getBakingResult().itemStackModels();
        Identifier ghostlyPickaxeItem = Identifier.fromNamespaceAndPath(AlexsMobs.MODID, "ghostly_pickaxe");
        itemModels.computeIfPresent(ghostlyPickaxeItem, (k, baked) -> new GhostlyPickaxeItemModel(baked));
        for (String path : AMItemstackRenderer.ISTER_ITEM_MODEL_PATHS) {
            // 26.1: itemStackModels keys match assets/<ns>/items/<path>.json → Identifier is ns:path (not ns:item/path).
            Identifier id = Identifier.fromNamespaceAndPath(AlexsMobs.MODID, path);
            itemModels.computeIfPresent(id, (k, baked) -> AMItemstackRenderer.SpecialItemModel.INSTANCE);
        }
    }
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        CustomArmorRenderProperties armorExtensions = new CustomArmorRenderProperties();
        event.registerItem(
                armorExtensions,
                AMItemRegistry.ROADDRUNNER_BOOTS.get(),
                AMItemRegistry.CROCODILE_CHESTPLATE.get(),
                AMItemRegistry.CENTIPEDE_LEGGINGS.get(),
                AMItemRegistry.MOOSE_HEADGEAR.get(),
                AMItemRegistry.FRONTIER_CAP.get(),
                AMItemRegistry.SOMBRERO.get(),
                AMItemRegistry.SPIKED_TURTLE_SHELL.get(),
                AMItemRegistry.EMU_LEGGINGS.get(),
                AMItemRegistry.FEDORA.get(),
                AMItemRegistry.FROSTSTALKER_HELMET.get(),
                AMItemRegistry.ROCKY_CHESTPLATE.get(),
                AMItemRegistry.FLYING_FISH_BOOTS.get(),
                AMItemRegistry.TARANTULA_HAWK_ELYTRA.get(),
                AMItemRegistry.NOVELTY_HAT.get(),
                AMItemRegistry.UNSETTLING_KIMONO.get());
        AlexsMobs.LOGGER.info("Registered custom armor render extensions");
    }

    public void openBookGUI(ItemStack itemStackIn) {
        Minecraft.getInstance().gui.setScreen(new GUIAnimalDictionary(itemStackIn));
    }

    public void openBookGUI(ItemStack itemStackIn, String page) {
        Minecraft.getInstance().gui.setScreen(new GUIAnimalDictionary(itemStackIn, page));
    }

    public Player getClientSidePlayer() {
        return Minecraft.getInstance().player;
    }
    public Object getArmorModel(int armorId, LivingEntity entity) {
        switch (armorId) {
            /*
            case 0:
                return ROADRUNNER_BOOTS_MODEL;
            case 1:
                return MOOSE_HEADGEAR_MODEL;
            case 2:
                return FRONTIER_CAP_MODEL.withAnimations(entity);
            case 3:
                return SOMBRERO_MODEL;
            case 4:
                return SPIKED_TURTLE_SHELL_MODEL;
            case 5:
                return FEDORA_MODEL;
            case 6:
                return ELYTRA_MODEL.withAnimations(entity);

             */
            default:
                return null;
        }
    }
    public void onEntityStatus(Entity entity, byte updateKind) {
        if (updateKind == 67) {
            if (entity instanceof EntityCockroach && entity.isAlive()) {
                SoundLaCucaracha sound;
                if (COCKROACH_SOUND_MAP.get(entity.getId()) == null) {
                    sound = new SoundLaCucaracha((EntityCockroach) entity);
                    COCKROACH_SOUND_MAP.put(entity.getId(), sound);
                } else {
                    sound = COCKROACH_SOUND_MAP.get(entity.getId());
                }
                if (!Minecraft.getInstance().getSoundManager().isActive(sound) && sound.canPlaySound() && sound.isOnlyCockroach()) {
                    Minecraft.getInstance().getSoundManager().play(sound);
                }
            } else if (entity instanceof EntityVoidWorm && entity.isAlive()) {
                final float f2 = Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MUSIC);
                if (f2 <= 0) {
                    WORMBOSS_SOUND_MAP.clear();
                } else {
                    SoundWormBoss sound;
                    if (WORMBOSS_SOUND_MAP.get(entity.getId()) == null) {
                        sound = new SoundWormBoss((EntityVoidWorm) entity);
                        WORMBOSS_SOUND_MAP.put(entity.getId(), sound);
                    } else {
                        sound = WORMBOSS_SOUND_MAP.get(entity.getId());
                    }
                    if (!Minecraft.getInstance().getSoundManager().isActive(sound) && sound.isNearest()) {
                        Minecraft.getInstance().getSoundManager().play(sound);
                    }
                }
            } else if (entity instanceof EntityGrizzlyBear && entity.isAlive()) {
                SoundBearMusicBox sound;
                if (BEAR_MUSIC_BOX_SOUND_MAP.get(entity.getId()) == null) {
                    sound = new SoundBearMusicBox((EntityGrizzlyBear) entity);
                    BEAR_MUSIC_BOX_SOUND_MAP.put(entity.getId(), sound);
                } else {
                    sound = BEAR_MUSIC_BOX_SOUND_MAP.get(entity.getId());
                }
                if (!Minecraft.getInstance().getSoundManager().isActive(sound) && sound.canPlaySound() && sound.isOnlyMusicBox()) {
                    Minecraft.getInstance().getSoundManager().play(sound);
                }
            } else if (entity instanceof EntityBlueJay && entity.isAlive()) {
                singingBlueJayId = entity.getId();
            }
        }
        if (entity instanceof EntityBlueJay && entity.isAlive() && updateKind == 68) {
            singingBlueJayId = -1;
        }
    }

    public void updateBiomeVisuals(int x, int z) {
        Minecraft.getInstance().levelRenderer.resetLevelRenderData();
    }

    public static void setupParticleGroups(RegisterParticleGroupsEvent event) {
        event.register(ParticleBearFreddy.BEAR_FREDDY, ParticleBearFreddy.BearFreddyParticleGroup::new);
    }

    public static void setupParticles(RegisterParticleProvidersEvent registry) {
        AlexsMobs.LOGGER.debug("Registered particle factories");
        registry.registerSpriteSet(AMParticleRegistry.GUSTER_SAND_SPIN.get(), ParticleGusterSandSpin.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.GUSTER_SAND_SHOT.get(), ParticleGusterSandShot.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.GUSTER_SAND_SPIN_RED.get(), ParticleGusterSandSpin.FactoryRed::new);
        registry.registerSpriteSet(AMParticleRegistry.GUSTER_SAND_SHOT_RED.get(), ParticleGusterSandShot.FactoryRed::new);
        registry.registerSpriteSet(AMParticleRegistry.GUSTER_SAND_SPIN_SOUL.get(), ParticleGusterSandSpin.FactorySoul::new);
        registry.registerSpriteSet(AMParticleRegistry.GUSTER_SAND_SHOT_SOUL.get(), ParticleGusterSandShot.FactorySoul::new);
        registry.registerSpriteSet(AMParticleRegistry.HEMOLYMPH.get(), ParticleHemolymph.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.PLATYPUS_SENSE.get(), ParticlePlatypus.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.WHALE_SPLASH.get(), ParticleWhaleSplash.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.DNA.get(), ParticleDna.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.SHOCKED.get(), ParticleSimpleHeart.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.WORM_PORTAL.get(), ParticleWormPortal.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.INVERT_DIG.get(), ParticleInvertDig.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.TEETH_GLINT.get(), ParticleTeethGlint.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.SMELLY.get(), ParticleSmelly.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.BUNFUNGUS_TRANSFORMATION.get(), ParticleBunfungusTransformation.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.FUNGUS_BUBBLE.get(), ParticleFungusBubble.Factory::new);
        registry.registerSpecial(AMParticleRegistry.BEAR_FREDDY.get(), new ParticleBearFreddy.Factory());
        registry.registerSpriteSet(AMParticleRegistry.SUNBIRD_FEATHER.get(), ParticleSunbirdFeather.Factory::new);
        registry.registerSpecial(AMParticleRegistry.STATIC_SPARK.get(), new ParticleStaticSpark.Factory());
        registry.registerSpecial(AMParticleRegistry.SKULK_BOOM.get(), new ParticleSkulkBoom.Factory());
        registry.registerSpriteSet(AMParticleRegistry.BIRD_SONG.get(), ParticleBirdSong.Factory::new);
    }


    public void setRenderViewEntity(Entity entity) {
        prevPOV = Minecraft.getInstance().options.getCameraType();
        Minecraft.getInstance().setCameraEntity(entity);
        Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
    }

    public void resetRenderViewEntity() {
        Minecraft.getInstance().setCameraEntity(Minecraft.getInstance().player);
    }

    public int getPreviousPOV() {
        return prevPOV.ordinal();
    }

    public boolean isFarFromCamera(double x, double y, double z) {
        Minecraft lvt_1_1_ = Minecraft.getInstance();
        return lvt_1_1_.gameRenderer.mainCamera().position().distanceToSqr(x, y, z) >= 256.0D;
    }

    public void resetVoidPortalCreation(Player player) {

    }

    @SubscribeEvent
    public void onRegisterEntityRenders(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }

    @Override
    public Object getISTERProperties() {
        return new AMItemRenderProperties();
    }

    @Override
    public Object getArmorRenderProperties() {
        return new CustomArmorRenderProperties();
    }

    public void spawnSpecialParticle(int type) {
        if (type == 0) {
            Minecraft.getInstance().level.addParticle(AMParticleRegistry.BEAR_FREDDY.get(), Minecraft.getInstance().player.getX(), Minecraft.getInstance().player.getY(), Minecraft.getInstance().player.getZ(), 0, 0, 0);
        }
    }

    public void processVisualFlag(Entity entity, int flag) {
        if (entity == Minecraft.getInstance().player && flag == 87) {
            ClientEvents.renderStaticScreenFor = 60;
        }
    }

    public void setPupfishChunkForItem(int chunkX, int chunkZ) {
        this.pupfishChunkX = chunkX;
        this.pupfishChunkZ = chunkZ;
        pupfishChunkXModel = chunkX;
        pupfishChunkZModel = chunkZ;
    }

    public void setDisplayTransmuteResult(int slot, ItemStack stack){
        transmuteStacks[Mth.clamp(slot, 0, 2)] = stack;
    }

    public ItemStack getDisplayTransmuteResult(int slot){
        ItemStack stack = transmuteStacks[Mth.clamp(slot, 0, 2)];
        return stack == null ? ItemStack.EMPTY : stack;
    }

    public int getSingingBlueJayId() {
        return singingBlueJayId;
    }

    private record BloodSprayerEmptyProperty() implements ConditionalItemModelProperty {
        static final MapCodec<BloodSprayerEmptyProperty> MAP_CODEC = MapCodec.unit(new BloodSprayerEmptyProperty());

        @Override
        public boolean get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed, ItemDisplayContext ctx) {
            return !ItemBloodSprayer.isUsable(stack)
                    || entity instanceof Player p && p.getCooldowns().isOnCooldown(stack);
        }

        @Override
        public MapCodec<? extends ConditionalItemModelProperty> type() {
            return MAP_CODEC;
        }
    }

    private record HemolymphBlasterEmptyProperty() implements ConditionalItemModelProperty {
        static final MapCodec<HemolymphBlasterEmptyProperty> MAP_CODEC = MapCodec.unit(new HemolymphBlasterEmptyProperty());

        @Override
        public boolean get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed, ItemDisplayContext ctx) {
            return !ItemHemolymphBlaster.isUsable(stack)
                    || entity instanceof Player p && p.getCooldowns().isOnCooldown(stack);
        }

        @Override
        public MapCodec<? extends ConditionalItemModelProperty> type() {
            return MAP_CODEC;
        }
    }

    private record TendonWhipActiveProperty() implements ConditionalItemModelProperty {
        static final MapCodec<TendonWhipActiveProperty> MAP_CODEC = MapCodec.unit(new TendonWhipActiveProperty());

        @Override
        public boolean get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed, ItemDisplayContext ctx) {
            return ItemTendonWhip.isActive(stack, entity);
        }

        @Override
        public MapCodec<? extends ConditionalItemModelProperty> type() {
            return MAP_CODEC;
        }
    }

    private record PupfishLocatorInChunkProperty() implements ConditionalItemModelProperty {
        static final MapCodec<PupfishLocatorInChunkProperty> MAP_CODEC = MapCodec.unit(new PupfishLocatorInChunkProperty());

        @Override
        public boolean get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed, ItemDisplayContext ctx) {
            int x = ClientProxy.pupfishChunkXModel * 16;
            int z = ClientProxy.pupfishChunkZModel * 16;
            return entity != null && entity.getX() >= x && entity.getX() <= x + 16 && entity.getZ() >= z && entity.getZ() <= z + 16;
        }

        @Override
        public MapCodec<? extends ConditionalItemModelProperty> type() {
            return MAP_CODEC;
        }
    }

    private record SombreroSillyProperty() implements ConditionalItemModelProperty {
        static final MapCodec<SombreroSillyProperty> MAP_CODEC = MapCodec.unit(new SombreroSillyProperty());

        @Override
        public boolean get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed, ItemDisplayContext ctx) {
            return AlexsMobs.isAprilFools();
        }

        @Override
        public MapCodec<? extends ConditionalItemModelProperty> type() {
            return MAP_CODEC;
        }
    }

}
