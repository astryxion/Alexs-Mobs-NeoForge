package com.github.alexthe666.alexsmobs.event;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.effect.EffectClinging;
import com.github.alexthe666.alexsmobs.entity.*;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import com.github.alexthe666.alexsmobs.entity.EntityJerboa;
import com.github.alexthe666.alexsmobs.entity.EntityMoose;
import com.github.alexthe666.alexsmobs.entity.EntitySeal;
import com.github.alexthe666.alexsmobs.entity.EntitySnowLeopard;
import com.github.alexthe666.alexsmobs.entity.EntityTiger;
import com.github.alexthe666.alexsmobs.entity.util.FlyingFishBootsUtil;
import com.github.alexthe666.alexsmobs.entity.util.RainbowUtil;
import com.github.alexthe666.alexsmobs.entity.util.RockyChestplateUtil;
import com.github.alexthe666.alexsmobs.entity.util.VineLassoUtil;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.item.ILeftClick;
import com.github.alexthe666.alexsmobs.item.ItemGhostlyPickaxe;
import com.github.alexthe666.alexsmobs.network.MessageSpongeRainbow;
import com.github.alexthe666.alexsmobs.network.MessageSwingArm;
import com.github.alexthe666.alexsmobs.misc.AMAdvancementTriggerRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTeleportQueue;
import com.github.alexthe666.alexsmobs.world.AMWorldData;
import com.github.alexthe666.alexsmobs.world.BeachedCachalotWhaleSpawner;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.feline.Ocelot;
import net.minecraft.world.entity.animal.polarbear.PolarBear;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.animal.fish.Pufferfish;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.*;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@EventBusSubscriber(modid = AlexsMobs.MODID)
public class ServerEvents {

    public static final UUID ALEX_UUID = UUID.fromString("71363abe-fd03-49c9-940d-aae8b8209b7c");
    public static final UUID CARRO_UUID = UUID.fromString("98905d4a-1cbc-41a4-9ded-2300404e2290");
    private static final Identifier SAND_SPEED_ID = Identifier.fromNamespaceAndPath(AlexsMobs.MODID, "roadrunner_sand_speed");
    private static final Identifier SNEAK_SPEED_ID = Identifier.fromNamespaceAndPath(AlexsMobs.MODID, "frontier_cap_sneak_speed");
    private static final Random RAND = new Random();
    private static final Map<ServerLevel, BeachedCachalotWhaleSpawner> BEACHED_CACHALOT_WHALE_SPAWNER_MAP = new HashMap<>();

    @SubscribeEvent
    public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        AMEffectRegistry.registerBrewingRecipes(event.getBuilder());
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post tick) {
        Level level = tick.getLevel();
        if (!level.isClientSide() && level instanceof ServerLevel serverWorld) {
            BEACHED_CACHALOT_WHALE_SPAWNER_MAP.computeIfAbsent(serverWorld, BeachedCachalotWhaleSpawner::new).tick();
            if (!AMTeleportQueue.PLAYERS.isEmpty()) {
                for (Triple<ServerPlayer, ServerLevel, BlockPos> triple : AMTeleportQueue.PLAYERS) {
                    ServerPlayer player = triple.getLeft();
                    ServerLevel endpointWorld = triple.getMiddle();
                    BlockPos endpoint = triple.getRight();
                    int heightFromMap = endpointWorld.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, endpoint.getX(), endpoint.getZ());
                    endpoint = new BlockPos(endpoint.getX(), Math.max(heightFromMap, endpoint.getY()), endpoint.getZ());
                    player.teleportTo(endpointWorld, endpoint.getX() + 0.5, endpoint.getY() + 0.5, endpoint.getZ() + 0.5,
                            Set.of(), player.getYRot(), player.getXRot(), false);
                    ChunkPos chunkpos = ChunkPos.containing(endpoint);
                    endpointWorld.getChunkSource().addTicketWithRadius(TicketType.UNKNOWN, chunkpos, 1);
                    player.connection.send(new ClientboundSetExperiencePacket(player.experienceProgress, player.totalExperience, player.experienceLevel));
                }
                AMTeleportQueue.PLAYERS.clear();
            }
        }
        AMWorldData data = AMWorldData.get(level);
        if (data != null) {
            data.tickPupfish();
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().removeIf(affected ->
                affected instanceof ItemEntity itemEntity
                        && itemEntity.getItem().is(AMBlockRegistry.TRANSMUTATION_TABLE.get().asItem()));
    }

    protected static BlockHitResult rayTrace(Level worldIn, Player player, ClipContext.Fluid fluidMode) {
        float x = player.getXRot();
        float y = player.getYRot();
        Vec3 vector3d = player.getEyePosition(1.0F);
        float f0 = -y * Mth.DEG_TO_RAD - Mth.PI;
        float f1 = -x * Mth.DEG_TO_RAD;
        float f2 = Mth.cos(f0);
        float f3 = Mth.sin(f0);
        float f4 = -Mth.cos(f1);
        float f5 = Mth.sin(f1);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = player.blockInteractionRange();
        Vec3 vector3d1 = vector3d.add(f6 * d0, f5 * d0, f7 * d0);
        return worldIn.clip(new ClipContext(vector3d, vector3d1, ClipContext.Block.OUTLINE, fluidMode, player));
    }

    /**
     * Check if an entity should not scare pufferfish (Alex's Mobs fish and other aquatic creatures)
     */
    private static boolean isNotScaryForPufferfish(LivingEntity entity) {
        return entity instanceof EntityLobster ||
               entity instanceof EntityBlobfish ||
               entity instanceof EntityTerrapin ||
               entity instanceof EntityCombJelly ||
               entity instanceof EntityCosmicCod ||
               entity instanceof EntityCatfish ||
               entity instanceof EntityFlyingFish ||
               entity instanceof EntityMudskipper ||
               entity instanceof EntityTriops ||
               entity instanceof EntityDevilsHolePupfish ||
               entity instanceof AbstractFish ||  // Vanilla fish
               entity instanceof AbstractSchoolingFish ||  // Vanilla schooling fish
               entity instanceof Squid ||  // Vanilla squid
               entity instanceof Dolphin;  // Vanilla dolphin
    }

    private static net.minecraft.network.syncher.EntityDataAccessor<Integer> PUFF_STATE_ACCESSOR = null;
    private static boolean PUFF_STATE_INIT_ATTEMPTED = false;
    
    /**
     * Set pufferfish puff state using reflection
     */
    @SuppressWarnings("unchecked")
    private static void setPufferfishState(Pufferfish pufferfish, int state) {
        if (!PUFF_STATE_INIT_ATTEMPTED) {
            PUFF_STATE_INIT_ATTEMPTED = true;
            try {
                // Find the PUFF_STATE field by scanning all static fields
                for (var field : Pufferfish.class.getDeclaredFields()) {
                    if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) && 
                        net.minecraft.network.syncher.EntityDataAccessor.class.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);
                        var accessor = (net.minecraft.network.syncher.EntityDataAccessor<?>) field.get(null);
                        // Test if this is the puff state accessor by checking current value type
                        try {
                            Object value = pufferfish.getEntityData().get(accessor);
                            if (value instanceof Integer && field.getName().toLowerCase().contains("puff")) {
                                PUFF_STATE_ACCESSOR = (net.minecraft.network.syncher.EntityDataAccessor<Integer>) accessor;
                                break;
                            }
                        } catch (Exception ignored) {}
                    }
                }
                // If not found by name, try to find by matching the getter value
                if (PUFF_STATE_ACCESSOR == null) {
                    int currentPuffState = pufferfish.getPuffState();
                    for (var field : Pufferfish.class.getDeclaredFields()) {
                        if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) && 
                            net.minecraft.network.syncher.EntityDataAccessor.class.isAssignableFrom(field.getType())) {
                            field.setAccessible(true);
                            var accessor = (net.minecraft.network.syncher.EntityDataAccessor<?>) field.get(null);
                            try {
                                Object value = pufferfish.getEntityData().get(accessor);
                                if (value instanceof Integer intVal && intVal == currentPuffState) {
                                    PUFF_STATE_ACCESSOR = (net.minecraft.network.syncher.EntityDataAccessor<Integer>) accessor;
                                    break;
                                }
                            } catch (Exception ignored) {}
                        }
                    }
                }
            } catch (Exception e) {
                AlexsMobs.LOGGER.warn("Failed to find PUFF_STATE field: " + e.getMessage());
            }
        }
        
        if (PUFF_STATE_ACCESSOR != null) {
            pufferfish.getEntityData().set(PUFF_STATE_ACCESSOR, state);
        }
    }

    private static java.lang.reflect.Field INFLATE_COUNTER_FIELD = null;
    private static java.lang.reflect.Field DEFLATE_TIMER_FIELD = null;
    private static boolean INFLATE_FIELDS_INIT_ATTEMPTED = false;
    
    /**
     * Reset pufferfish inflate counter to prevent re-inflation
     */
    private static void resetPufferfishInflateCounter(Pufferfish pufferfish) {
        if (!INFLATE_FIELDS_INIT_ATTEMPTED) {
            INFLATE_FIELDS_INIT_ATTEMPTED = true;
            try {
                // Find inflateCounter and deflateTimer fields
                for (var field : Pufferfish.class.getDeclaredFields()) {
                    if (!java.lang.reflect.Modifier.isStatic(field.getModifiers()) && field.getType() == int.class) {
                        String name = field.getName().toLowerCase();
                        if (name.contains("inflate") || name.contains("counter")) {
                            field.setAccessible(true);
                            INFLATE_COUNTER_FIELD = field;
                        } else if (name.contains("deflate") || name.contains("timer")) {
                            field.setAccessible(true);
                            DEFLATE_TIMER_FIELD = field;
                        }
                    }
                }
                // If not found by name, get all int fields
                if (INFLATE_COUNTER_FIELD == null) {
                    for (var field : Pufferfish.class.getDeclaredFields()) {
                        if (!java.lang.reflect.Modifier.isStatic(field.getModifiers()) && field.getType() == int.class) {
                            field.setAccessible(true);
                            // The first non-static int field is likely inflateCounter
                            if (INFLATE_COUNTER_FIELD == null) {
                                INFLATE_COUNTER_FIELD = field;
                            } else if (DEFLATE_TIMER_FIELD == null) {
                                DEFLATE_TIMER_FIELD = field;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                AlexsMobs.LOGGER.warn("Failed to find inflate counter fields: " + e.getMessage());
            }
        }
        
        try {
            if (INFLATE_COUNTER_FIELD != null) {
                INFLATE_COUNTER_FIELD.setInt(pufferfish, 0);
            }
            if (DEFLATE_TIMER_FIELD != null) {
                DEFLATE_TIMER_FIELD.setInt(pufferfish, 0);
            }
        } catch (Exception ignored) {}
    }

    private static BlockPos getDownPos(BlockPos entered, LevelAccessor world) {
        int i = 0;
        while (world.isEmptyBlock(entered) && i < 3) {
            entered = entered.below();
            i++;
        }
        return entered;
    }


    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (AMConfig.giveBookOnStartup) {
            CompoundTag playerData = event.getEntity().getPersistentData();
            CompoundTag data = playerData.getCompoundOrEmpty(Player.PERSISTED_NBT_TAG);
            if (!data.getBooleanOr("alexsmobs_has_book", false)) {
                ItemHandlerHelper.giveItemToPlayer(event.getEntity(), new ItemStack(AMItemRegistry.ANIMAL_DICTIONARY.get()));
                boolean isAlex = Objects.equals(event.getEntity().getUUID(), ALEX_UUID);
                if (isAlex || Objects.equals(event.getEntity().getUUID(), CARRO_UUID)) {
                    ItemHandlerHelper.giveItemToPlayer(event.getEntity(), new ItemStack(AMItemRegistry.BEAR_DUST.get()));
                }
                if (isAlex) {
                    ItemHandlerHelper.giveItemToPlayer(event.getEntity(), new ItemStack(AMItemRegistry.NOVELTY_HAT.get()));
                }
                data.putBoolean("alexsmobs_has_book", true);
                playerData.put(Player.PERSISTED_NBT_TAG, data);
            }
        }
    }

    /**
     * Client-only: empty left-click with an {@link ILeftClick} item must notify the server (vanilla never does).
     * Matches {@code ServerEvents.java.original} / 1.21.1 behavior for tendon whip and falconry glove.
     */
    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        boolean flag = false;
        Player player = event.getEntity();
        ItemStack leftItem = player.getOffhandItem();
        ItemStack rightItem = player.getMainHandItem();
        if (leftItem.getItem() instanceof ILeftClick iLeftClick) {
            iLeftClick.onLeftClick(leftItem, player);
            flag = true;
        }
        if (rightItem.getItem() instanceof ILeftClick iLeftClick) {
            iLeftClick.onLeftClick(rightItem, player);
            flag = true;
        }
        if (flag && event.getLevel().isClientSide()) {
            AlexsMobs.sendMSGToServer(new MessageSwingArm());
        }
    }

    @SubscribeEvent
    public static void onPlayerAttackEntityEvent(AttackEntityEvent event) {
        if (event.getTarget() instanceof LivingEntity living) {
            if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() == AMItemRegistry.MOOSE_HEADGEAR.get()) {
                living.knockback(1F, Mth.sin(event.getEntity().getYRot() * Mth.DEG_TO_RAD),
                        -Mth.cos(event.getEntity().getYRot() * Mth.DEG_TO_RAD), event.getEntity().damageSources().mobAttack(event.getEntity()), 1.0F);
            }
            if (event.getEntity().hasEffect(AMEffectRegistry.TIGERS_BLESSING)
                    && !event.getTarget().isAlliedTo(event.getEntity()) && !(event.getTarget() instanceof EntityTiger)) {
                AABB bb = new AABB(event.getEntity().getX() - 32, event.getEntity().getY() - 32, event.getEntity().getZ() - 32,
                        event.getEntity().getX() + 32, event.getEntity().getY() + 32, event.getEntity().getZ() + 32);
                var tigers = event.getEntity().level().getEntitiesOfClass(EntityTiger.class, bb, EntitySelector.ENTITY_STILL_ALIVE);
                for (EntityTiger tiger : tigers) {
                    if (!tiger.isBaby()) {
                        tiger.setTarget(living);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onHarvestCheck(PlayerEvent.HarvestCheck event) {
        if (event.getEntity() != null && event.getEntity().isHolding(AMItemRegistry.GHOSTLY_PICKAXE.get())
                && ItemGhostlyPickaxe.shouldStoreInGhost(event.getEntity(), event.getEntity().getMainHandItem())) {
            event.setCanHarvest(false);
        }
    }

    @SubscribeEvent
    public static void onStruckByLightning(EntityStruckByLightningEvent event) {
        if (event.getEntity().getType() == EntityTypes.SQUID && !event.getEntity().level().isClientSide()) {
            ServerLevel level = (ServerLevel) event.getEntity().level();
            event.setCanceled(true);
            EntityGiantSquid squid = AMEntityRegistry.GIANT_SQUID.get().create(level, EntitySpawnReason.CONVERSION);
            squid.snapTo(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                    event.getEntity().getYRot(), event.getEntity().getXRot());
            squid.finalizeSpawn(level, level.getCurrentDifficultyAt(squid.blockPosition()), EntitySpawnReason.CONVERSION, null);
            if (event.getEntity().hasCustomName()) {
                squid.setCustomName(event.getEntity().getCustomName());
                squid.setCustomNameVisible(event.getEntity().isCustomNameVisible());
            }
            squid.setBlue(true);
            squid.setPersistenceRequired();
            level.addFreshEntityWithPassengers(squid);
            event.getEntity().discard();
        }
    }


    @SubscribeEvent
    public static void onProjectileHit(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof EntityHitResult hitResult
                && hitResult.getEntity() instanceof EntityEmu emu && !event.getEntity().level().isClientSide()) {
            if ((emu.getAnimation() == EntityEmu.ANIMATION_DODGE_RIGHT || emu.getAnimation() == EntityEmu.ANIMATION_DODGE_LEFT)
                    && emu.getAnimationTick() < 7) {
                event.setCanceled(true);
                return;
            }
            if (emu.getAnimation() != EntityEmu.ANIMATION_DODGE_RIGHT && emu.getAnimation() != EntityEmu.ANIMATION_DODGE_LEFT) {
                boolean left;
                Vec3 arrowPos = event.getEntity().position();
                Vec3 rightVector = emu.getLookAngle().yRot(0.5F * Mth.PI).add(emu.position());
                Vec3 leftVector = emu.getLookAngle().yRot(-0.5F * Mth.PI).add(emu.position());
                if (arrowPos.distanceTo(rightVector) < arrowPos.distanceTo(leftVector)) {
                    left = false;
                } else if (arrowPos.distanceTo(rightVector) > arrowPos.distanceTo(leftVector)) {
                    left = true;
                } else {
                    left = emu.getRandom().nextBoolean();
                }
                Vec3 vector3d2 = event.getEntity().getDeltaMovement().yRot((float) ((left ? -0.5F : 0.5F) * Math.PI)).normalize();
                emu.setAnimation(left ? EntityEmu.ANIMATION_DODGE_LEFT : EntityEmu.ANIMATION_DODGE_RIGHT);
                emu.needsSync = true;
                if (!emu.horizontalCollision) {
                    emu.move(MoverType.SELF, new Vec3(vector3d2.x() * 0.25F, 0.1F, vector3d2.z() * 0.25F));
                }
                if (event.getEntity() instanceof Projectile projectile) {
                    if (projectile.getOwner() instanceof ServerPlayer serverPlayer) {
                        AlexsMobs.LOGGER.info("Emu dodged! Triggering EMU_DODGE advancement for player: {}", serverPlayer.getName().getString());
                        AMAdvancementTriggerRegistry.EMU_DODGE.get().trigger(serverPlayer);
                    }
                }
                emu.setDeltaMovement(emu.getDeltaMovement().add(vector3d2.x() * 0.5F, 0.32F, vector3d2.z() * 0.5F));
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDespawnAttempt(MobDespawnEvent event) {
        if (event.getEntity().hasEffect(AMEffectRegistry.DEBILITATING_STING)
                && event.getEntity().getEffect(AMEffectRegistry.DEBILITATING_STING) != null
                && event.getEntity().getEffect(AMEffectRegistry.DEBILITATING_STING).getAmplifier() > 0) {
            event.setResult(MobDespawnEvent.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        var entity = event.getEntity();
        try {
            if (AMConfig.spidersAttackFlies && entity instanceof Spider spider) {
                spider.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(spider, EntityFly.class, 1, true, false, null));
            } else if (AMConfig.wolvesAttackMoose && entity instanceof Wolf wolf) {
                wolf.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(wolf, EntityMoose.class, false, null));
            } else if (AMConfig.polarBearsAttackSeals && entity instanceof PolarBear bear) {
                bear.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(bear, EntitySeal.class, 15, true, true, null));
            } else if (entity instanceof Creeper creeper) {
                creeper.targetSelector.addGoal(3, new AvoidEntityGoal<>(creeper, EntitySnowLeopard.class, 6.0F, 1.0D, 1.2D));
                creeper.targetSelector.addGoal(3, new AvoidEntityGoal<>(creeper, EntityTiger.class, 6.0F, 1.0D, 1.2D));
            } else if (AMConfig.catsAndFoxesAttackJerboas && (entity instanceof Fox || entity instanceof Cat || entity instanceof Ocelot)) {
                Mob mb = (Mob) entity;
                mb.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(mb, EntityJerboa.class, 45, true, true, null));
            } else if (AMConfig.bunfungusTransformation && entity instanceof Rabbit rabbit) {
                rabbit.goalSelector.addGoal(3, new TemptGoal(rabbit, 1.0D, Ingredient.of(AMItemRegistry.MUNGAL_SPORES.get()), false));
            } else if (AMConfig.dolphinsAttackFlyingFish && entity instanceof Dolphin dolphin) {
                dolphin.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(dolphin, EntityFlyingFish.class, 70, true, true, null));
            }
        } catch (Exception e) {
            AlexsMobs.LOGGER.warn("Tried to add unique behaviors to vanilla mobs and encountered an error");
        }
    }

    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event) {
        if (VineLassoUtil.hasLassoData(event.getEntity())) {
            VineLassoUtil.lassoTo(null, event.getEntity());
            event.getDrops().add(new ItemEntity(event.getEntity().level(), event.getEntity().getX(),
                    event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(AMItemRegistry.VINE_LASSO.get())));
        }
    }

    @SubscribeEvent
    public static void onItemUseLast(LivingEntityUseItemEvent.Finish event) {
        if (event.getItem().getItem() == Items.CHORUS_FRUIT && RAND.nextInt(3) == 0
                && event.getEntity().hasEffect(AMEffectRegistry.ENDER_FLU)) {
            event.getEntity().removeEffect(AMEffectRegistry.ENDER_FLU);
        }
    }


    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        var entity = (LivingEntity) event.getEntity();
        if (!entity.level().isClientSide()
                && entity instanceof net.minecraft.world.entity.player.Player player
                && player.isFallFlying()
                && player.getItemBySlot(EquipmentSlot.CHEST).is(AMItemRegistry.TARANTULA_HAWK_ELYTRA.get())) {
            int ticks = player.getFallFlyingTicks();
            if ((ticks + 1) % 20 == 0) {
                player.getItemBySlot(EquipmentSlot.CHEST).hurtAndBreak(1, entity, EquipmentSlot.CHEST);
            }
        }
        // Make pufferfish not puff up around Alex's Mobs fish
        if (entity instanceof Pufferfish pufferfish && !entity.level().isClientSide()) {
            // Check if only Alex's Mobs fish are nearby (no scary entities)
            var nearbyEntities = pufferfish.level().getEntitiesOfClass(LivingEntity.class, 
                pufferfish.getBoundingBox().inflate(2.0D), 
                e -> e != pufferfish && !(e instanceof Pufferfish) && !e.isSpectator());
            
            boolean hasScaryEntity = false;
            boolean hasFriendlyFish = false;
            for (LivingEntity nearby : nearbyEntities) {
                if (isNotScaryForPufferfish(nearby)) {
                    hasFriendlyFish = true;
                } else {
                    hasScaryEntity = true;
                    break;
                }
            }
            // If only friendly fish nearby (no scary entities), keep pufferfish deflated
            if (hasFriendlyFish && !hasScaryEntity) {
                setPufferfishState(pufferfish, 0);
                resetPufferfishInflateCounter(pufferfish);
            }
        }
        
        if (entity instanceof Player player) {
            if (player.getEyeHeight() < player.getBbHeight() * 0.5F) {
                player.refreshDimensions();
            }
            if (entity.getAttributes().hasAttribute(Attributes.MOVEMENT_SPEED)) {
                var attributes = entity.getAttribute(Attributes.MOVEMENT_SPEED);
                if (player.getItemBySlot(EquipmentSlot.FEET).getItem() == AMItemRegistry.ROADDRUNNER_BOOTS.get()
                        || attributes.hasModifier(SAND_SPEED_ID)) {
                    boolean sand = player.level().getBlockState(getDownPos(player.blockPosition(), player.level())).is(BlockTags.SAND);
                    if (sand && !attributes.hasModifier(SAND_SPEED_ID)) {
                        attributes.addPermanentModifier(new AttributeModifier(SAND_SPEED_ID, 0.1F, AttributeModifier.Operation.ADD_VALUE));
                    }
                    if (player.tickCount % 25 == 0
                            && (player.getItemBySlot(EquipmentSlot.FEET).getItem() != AMItemRegistry.ROADDRUNNER_BOOTS.get() || !sand)
                            && attributes.hasModifier(SAND_SPEED_ID)) {
                        attributes.removeModifier(SAND_SPEED_ID);
                    }
                }
                if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == AMItemRegistry.FRONTIER_CAP.get()
                        || attributes.hasModifier(SNEAK_SPEED_ID)) {
                    boolean shift = player.isShiftKeyDown();
                    if (shift && !attributes.hasModifier(SNEAK_SPEED_ID)) {
                        attributes.addPermanentModifier(new AttributeModifier(SNEAK_SPEED_ID, 0.1F, AttributeModifier.Operation.ADD_VALUE));
                    }
                    if ((!shift || player.getItemBySlot(EquipmentSlot.HEAD).getItem() != AMItemRegistry.FRONTIER_CAP.get())
                            && attributes.hasModifier(SNEAK_SPEED_ID)) {
                        attributes.removeModifier(SNEAK_SPEED_ID);
                    }
                }
            }
            if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == AMItemRegistry.SPIKED_TURTLE_SHELL.get()) {
                if (!player.isEyeInFluid(FluidTags.WATER)) {
                    player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 310, 0, false, false, true));
                }
            }
        }
        ItemStack boots = entity.getItemBySlot(EquipmentSlot.FEET);
        if (!boots.isEmpty()) {
            CompoundTag tag = boots.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                    net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
            if (tag.getBooleanOr("BisonFur", false)) {
                BlockPos posBelow = new BlockPos((int) entity.getX(), (int) (entity.getBoundingBox().minY - 0.1F), (int) entity.getZ());
                if (entity.level().getBlockState(posBelow).is(Blocks.POWDER_SNOW)) {
                    entity.setOnGround(true);
                    entity.setTicksFrozen(0);
                    entity.setPos(entity.getX(), Math.max(entity.getY(), posBelow.getY() + 1F), entity.getZ());
                }
                if (entity.isInPowderSnow) {
                    entity.setOnGround(true);
                    entity.setDeltaMovement(entity.getDeltaMovement().add(0, 0.1F, 0));
                }
            }
        }
        if (entity.getItemBySlot(EquipmentSlot.LEGS).getItem() == AMItemRegistry.CENTIPEDE_LEGGINGS.get()) {
            if (entity.horizontalCollision && !entity.isInWater()) {
                entity.fallDistance = 0.0F;
                Vec3 motion = entity.getDeltaMovement();
                double d2 = 0.1D;
                if (entity.isShiftKeyDown() || entity.isSuppressingSlidingDownLadder()) {
                    d2 = 0.0D;
                }
                motion = new Vec3(Mth.clamp(motion.x, -0.15F, 0.15F), d2, Mth.clamp(motion.z, -0.15F, 0.15F));
                entity.setDeltaMovement(motion);
            }
        }

        if (entity.getItemBySlot(EquipmentSlot.HEAD).getItem() == AMItemRegistry.SOMBRERO.get()
                && !entity.level().isClientSide() && AlexsMobs.isAprilFools() && AMEntityRegistry.isInWaterOrBubble(entity)) {
            RandomSource random = entity.getRandom();
            if (random.nextInt(245) == 0 && !EntitySeaBear.isMobSafe(entity)) {
                int dist = 32;
                var nearbySeabears = entity.level().getEntitiesOfClass(EntitySeaBear.class, entity.getBoundingBox().inflate(dist, dist, dist));
                if (nearbySeabears.isEmpty() && entity.level() instanceof ServerLevel serverLevel) {
                    EntitySeaBear bear = AMEntityRegistry.SEA_BEAR.get().create(serverLevel, EntitySpawnReason.TRIGGERED);
                    if (bear != null) {
                        BlockPos at = entity.blockPosition();
                        BlockPos farOff = null;
                        for (int i = 0; i < 15; i++) {
                            int f1 = (int) Math.signum(random.nextInt() - 0.5F);
                            int f2 = (int) Math.signum(random.nextInt() - 0.5F);
                            BlockPos pos1 = at.offset(f1 * (10 + random.nextInt(dist - 10)), random.nextInt(1), f2 * (10 + random.nextInt(dist - 10)));
                            if (entity.level().isWaterAt(pos1)) {
                                farOff = pos1;
                            }
                        }
                        if (farOff != null) {
                            bear.setPos(farOff.getX() + 0.5F, farOff.getY() + 0.5F, farOff.getZ() + 0.5F);
                            bear.setYRot(random.nextFloat() * 360F);
                            bear.setTarget(entity);
                            entity.level().addFreshEntity(bear);
                        }
                    }
                } else {
                    for (EntitySeaBear seaBear : nearbySeabears) {
                        seaBear.setTarget(entity);
                    }
                }
            }
        }
        if (VineLassoUtil.hasLassoData(entity)) {
            VineLassoUtil.tickLasso(entity);
        }
        if (RockyChestplateUtil.isWearing(entity)) {
            RockyChestplateUtil.tickRockyRolling(entity);
        }
        if (FlyingFishBootsUtil.isWearing(entity)) {
            FlyingFishBootsUtil.tickFlyingFishBoots(entity);
        }
    }

    @SubscribeEvent
    public static void onLivingSetTargetEvent(LivingChangeTargetEvent event) {
        if (event.getNewAboutToBeSetTarget() != null && event.getEntity() instanceof Mob mob) {
            // Check for arthropod type using vanilla tag
            if (mob.getType().builtInRegistryHolder().is(net.minecraft.tags.EntityTypeTags.ARTHROPOD)) {
                if (event.getNewAboutToBeSetTarget().hasEffect(AMEffectRegistry.BUG_PHEROMONES)
                        && event.getEntity().getLastHurtByMob() != event.getNewAboutToBeSetTarget()) {
                    event.setCanceled(true);
                    return;
                }
            }
            // Check for undead type using vanilla tag
            if (mob.getType().builtInRegistryHolder().is(net.minecraft.tags.EntityTypeTags.UNDEAD) && !mob.getType().builtInRegistryHolder().is(AMTagRegistry.IGNORES_KIMONO)) {
                if (event.getNewAboutToBeSetTarget().getItemBySlot(EquipmentSlot.CHEST).is(AMItemRegistry.UNSETTLING_KIMONO.get())
                        && event.getEntity().getLastHurtByMob() != event.getNewAboutToBeSetTarget()) {
                    event.setCanceled(true);
                }
            }
        }
    }


    @SubscribeEvent
    public static void onLivingDamageEvent(LivingIncomingDamageEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            if (event.getAmount() > 0 && attacker.hasEffect(AMEffectRegistry.SOULSTEAL)
                    && attacker.getEffect(AMEffectRegistry.SOULSTEAL) != null) {
                int level = attacker.getEffect(AMEffectRegistry.SOULSTEAL).getAmplifier() + 1;
                if (attacker.getHealth() < attacker.getMaxHealth()
                        && ThreadLocalRandom.current().nextFloat() < (0.25F + (level * 0.25F))) {
                    attacker.heal(Math.min(event.getAmount() / 2F * level, 2 + 2 * level));
                }
            }
            if (event.getEntity() instanceof Player player) {
                if (attacker instanceof EntityMimicOctopus octopus && octopus.isOwnedBy(player)) {
                    event.setCanceled(true);
                    return;
                }
                if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == AMItemRegistry.SPIKED_TURTLE_SHELL.get()) {
                    if (attacker.distanceTo(player) < attacker.getBbWidth() + player.getBbWidth() + 0.5F) {
                        attacker.hurt(attacker.damageSources().thorns(player), 1F);
                        attacker.knockback(0.5F, Mth.sin((attacker.getYRot() + 180) * Mth.DEG_TO_RAD),
                                -Mth.cos((attacker.getYRot() + 180) * Mth.DEG_TO_RAD), attacker.damageSources().thorns(player), 1.0F);
                    }
                }
            }
        }
        if (!event.getEntity().getItemBySlot(EquipmentSlot.LEGS).isEmpty()
                && event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getItem() == AMItemRegistry.EMU_LEGGINGS.get()) {
            if (event.getSource().is(DamageTypeTags.IS_PROJECTILE) && event.getEntity().getRandom().nextFloat() < AMConfig.emuPantsDodgeChance) {
                event.setCanceled(true);
            }
        }
        if (!event.getEntity().getUseItem().isEmpty()
                && event.getSource().getEntity() instanceof LivingEntity living
                && event.getEntity().getUseItem().getItem() == AMItemRegistry.SHIELD_OF_THE_DEEP.get()) {
            boolean flag = false;
            if (living.distanceTo(event.getEntity()) <= 4.0F && !living.hasEffect(AMEffectRegistry.EXSANGUINATION)) {
                living.addEffect(new MobEffectInstance(AMEffectRegistry.EXSANGUINATION, 60, 2));
                flag = true;
            }
            if (event.getEntity().isInWater()) {
                event.getEntity().setAirSupply(Math.min(event.getEntity().getMaxAirSupply(), event.getEntity().getAirSupply() + 150));
                flag = true;
            }
            if (flag) {
                event.getEntity().getUseItem().hurtAndBreak(1, event.getEntity(),
                        event.getEntity().getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }
        }
    }

    @SubscribeEvent
    public static void onUseItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (event.getItemStack().getItem() == Items.WHEAT && player.getVehicle() instanceof EntityElephant elephant) {
            if (elephant.triggerCharge(event.getItemStack())) {
                player.swing(event.getHand());
                if (!player.isCreative()) {
                    event.getItemStack().shrink(1);
                }
            }
        }
        if (event.getItemStack().getItem() == Items.GLASS_BOTTLE && AMConfig.lavaBottleEnabled) {
            HitResult raytraceresult = rayTrace(event.getLevel(), player, ClipContext.Fluid.SOURCE_ONLY);
            if (raytraceresult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockpos = ((BlockHitResult) raytraceresult).getBlockPos();
                if (event.getLevel().mayInteract(player, blockpos)) {
                    if (event.getLevel().getFluidState(blockpos).is(FluidTags.LAVA)) {
                        player.gameEvent(GameEvent.ITEM_INTERACT_START);
                        event.getLevel().playSound(player, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                        player.awardStat(Stats.ITEM_USED.get(Items.GLASS_BOTTLE));
                        player.igniteForSeconds(6);
                        if (!player.addItem(new ItemStack(AMItemRegistry.LAVA_BOTTLE.get()))) {
                            player.spawnAtLocation((ServerLevel) event.getLevel(), new ItemStack(AMItemRegistry.LAVA_BOTTLE.get()));
                        }
                        player.swing(event.getHand());
                        if (!player.isCreative()) {
                            event.getItemStack().shrink(1);
                        }
                    }
                }
            }
        }
        if (RainbowUtil.tryWashOffRainbow(player, event.getItemStack())) {
            player.swing(event.getHand());
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }


    @SubscribeEvent
    public static void onInteractWithEntity(PlayerInteractEvent.EntityInteract event) {
        interactWithEntity(event.getEntity(), event.getItemStack(), event.getLevel(), event.getTarget(), () -> {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        });
    }

    private static void interactWithEntity(Player player, ItemStack held, Level level, Entity targeted, Runnable consume) {
        if (targeted instanceof LivingEntity living) {
            if (!player.isShiftKeyDown() && VineLassoUtil.hasLassoData(living)) {
                if (!player.level().isClientSide() && player.level() instanceof ServerLevel serverLevel) {
                    targeted.spawnAtLocation(serverLevel, new ItemStack(AMItemRegistry.VINE_LASSO.get()));
                }
                VineLassoUtil.lassoTo(null, living);
                consume.run();
            }
            if (!(targeted instanceof Player) && !(targeted instanceof EntityEndergrade)
                    && living.hasEffect(AMEffectRegistry.ENDER_FLU)) {
                if (held.getItem() == Items.CHORUS_FRUIT) {
                    if (!player.isCreative()) {
                        held.shrink(1);
                    }
                    targeted.gameEvent(GameEvent.EAT);
                    targeted.playSound(SoundEvents.GENERIC_EAT.value(), 1.0F, 0.5F + player.getRandom().nextFloat());
                    if (player.getRandom().nextFloat() < 0.4F) {
                        living.removeEffect(AMEffectRegistry.ENDER_FLU);
                        Items.CHORUS_FRUIT.finishUsingItem(held.copy(), level, living);
                    }
                    consume.run();
                }
            }
            if (RainbowUtil.tryWashOffRainbow(living, player, held)) {
                consume.run();
            }
            if (living instanceof Rabbit rabbit && held.getItem() == AMItemRegistry.MUNGAL_SPORES.get()
                    && AMConfig.bunfungusTransformation) {
                var random = ThreadLocalRandom.current();
                if (!player.level().isClientSide() && random.nextFloat() < 0.15F) {
                    EntityBunfungus bunfungus = rabbit.convertTo(
                            AMEntityRegistry.BUNFUNGUS.get(),
                            ConversionParams.single(rabbit, true, false),
                            EntitySpawnReason.CONVERSION,
                            bun -> {});
                    if (bunfungus != null) {
                        player.level().addFreshEntity(bunfungus);
                        bunfungus.setTransformsIn(EntityBunfungus.MAX_TRANSFORM_TIME);
                    }
                } else {
                    for (int i = 0; i < 2 + random.nextInt(2); i++) {
                        double d0 = random.nextGaussian() * 0.02D;
                        double d1 = 0.05F + random.nextGaussian() * 0.02D;
                        double d2 = random.nextGaussian() * 0.02D;
                        targeted.level().addParticle(AMParticleRegistry.BUNFUNGUS_TRANSFORMATION.get(),
                                targeted.getRandomX(0.7F), targeted.getY(0.6F),
                                targeted.getRandomZ(0.7F), d0, d1, d2);
                    }
                }
                if (!player.isCreative()) {
                    held.shrink(1);
                }
                consume.run();
            }
        }
    }


    @SubscribeEvent
    public static void onUseItemAir(PlayerInteractEvent.RightClickEmpty event) {
        ItemStack stack = event.getEntity().getItemInHand(event.getHand());
        if (stack.isEmpty()) {
            stack = event.getEntity().getItemBySlot(EquipmentSlot.MAINHAND);
        }
        if (stack.is(Items.SPONGE)) {
            if (RainbowUtil.tryWashOffRainbow(event.getEntity(), stack)) {
                event.getEntity().swing(InteractionHand.MAIN_HAND);
            }
            // RightClickEmpty is client-only; the server must persist the clear or the effect returns on rejoin.
            if (event.getLevel().isClientSide()) {
                AlexsMobs.sendMSGToServer(new MessageSpongeRainbow());
            }
        }
    }

    @SubscribeEvent
    public static void onUseItemOnBlock(PlayerInteractEvent.RightClickBlock event) {
        if (AlexsMobs.isAprilFools() && event.getItemStack().is(Items.STICK)
                && !event.getEntity().getCooldowns().isOnCooldown(new ItemStack(Items.STICK))) {
            BlockState state = event.getEntity().level().getBlockState(event.getPos());
            boolean flag = false;
            if (state.is(Blocks.SAND)) {
                flag = true;
                event.getEntity().level().setBlockAndUpdate(event.getPos(), AMBlockRegistry.SAND_CIRCLE.get().defaultBlockState());
            } else if (state.is(Blocks.RED_SAND)) {
                flag = true;
                event.getEntity().level().setBlockAndUpdate(event.getPos(), AMBlockRegistry.RED_SAND_CIRCLE.get().defaultBlockState());
            }
            if (flag) {
                event.setCanceled(true);
                event.getEntity().gameEvent(GameEvent.BLOCK_PLACE);
                event.getEntity().playSound(SoundEvents.SAND_BREAK, 1, 1);
                event.getEntity().getCooldowns().addCooldown(new ItemStack(Items.STICK), 30);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }

    /**
     * Villager trades are registered via data pack ({@code data/alexsmobs/villager_trade}). This filters offers when
     * {@link AMConfig} disables wandering trader integration or specific mob spawns (matching old event behavior).
     */
    @SubscribeEvent
    public static void onEntityJoinWanderingTrader(net.neoforged.neoforge.event.entity.EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof WanderingTrader trader)) {
            return;
        }
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }
        serverLevel.getServer().execute(() -> {
            if (!trader.isAlive()) {
                return;
            }
            if (!AMConfig.wanderingTraderOffers) {
                trader.getOffers().removeIf(ServerEvents::isAlexsmobsWanderingTrade);
                return;
            }
            if (AMConfig.cockroachSpawnWeight <= 0) {
                trader.getOffers().removeIf(offer -> offer.getResult().is(AMItemRegistry.COCKROACH_OOTHECA.get()));
            }
            if (AMConfig.blobfishSpawnWeight <= 0) {
                trader.getOffers().removeIf(offer -> offer.getResult().is(AMItemRegistry.BLOBFISH_BUCKET.get()));
            }
            if (AMConfig.crocodileSpawnWeight <= 0) {
                trader.getOffers().removeIf(offer -> offer.getResult().is(AMBlockRegistry.CROCODILE_EGG.get().asItem()));
            }
        });
    }

    private static boolean isAlexsmobsWanderingTrade(net.minecraft.world.item.trading.MerchantOffer offer) {
        return offer.getResult().is(AMItemRegistry.ANIMAL_DICTIONARY.get())
                || offer.getResult().is(AMItemRegistry.ACACIA_BLOSSOM.get())
                || offer.getResult().is(AMItemRegistry.COCKROACH_OOTHECA.get())
                || offer.getResult().is(AMItemRegistry.BLOBFISH_BUCKET.get())
                || offer.getResult().is(AMBlockRegistry.CROCODILE_EGG.get().asItem())
                || offer.getResult().is(AMItemRegistry.BEAR_FUR.get())
                || offer.getResult().is(AMItemRegistry.CROCODILE_SCUTE.get())
                || offer.getResult().is(AMItemRegistry.ROADRUNNER_FEATHER.get())
                || offer.getResult().is(AMItemRegistry.MOSQUITO_LARVA.get())
                || offer.getResult().is(AMItemRegistry.SOMBRERO.get())
                || offer.getResult().is(AMBlockRegistry.BANANA_PEEL.get().asItem())
                || offer.getResult().is(AMItemRegistry.BLOOD_SAC.get());
    }

    @SubscribeEvent
    public static void onEntityFinalizeSpawn(FinalizeSpawnEvent event) {
        if (event.getEntity() instanceof WanderingTrader trader && event.getLevel() instanceof ServerLevel serverLevel) {
            trySpawnElephantTrader(trader, serverLevel);
        }
    }

    private static void trySpawnElephantTrader(WanderingTrader trader, ServerLevel level) {
        if (AMConfig.elephantTraderSpawnChance <= 0) {
            return;
        }
        var biome = level.getBiome(trader.blockPosition()).value();
        if (RAND.nextFloat() > AMConfig.elephantTraderSpawnChance) {
            return;
        }
        if (AMConfig.limitElephantTraderBiomes && biome.getBaseTemperature() < 1.0F) {
            return;
        }
        ChunkPos chunkPos = ChunkPos.containing(trader.blockPosition());
        if (level.getChunkSource().getChunkNow(chunkPos.x(), chunkPos.z()) == null) {
            return;
        }
        EntityElephant elephant = AMEntityRegistry.ELEPHANT.get().create(level, EntitySpawnReason.TRIGGERED);
        if (elephant == null) {
            return;
        }
        elephant.copyPosition(trader);
        if (elephant.canSpawnWithTraderHere()) {
            elephant.setTrader(true);
            elephant.setChested(true);
            level.addFreshEntity(elephant);
            trader.startRiding(elephant, true, true);
            elephant.addElephantLoot(null, RAND.nextInt());
        }
    }

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        var customData = event.getItemStack().get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.getBooleanOr("BisonFur", false)) {
                event.getToolTip().add(Component.translatable("item.alexsmobs.insulated_with_fur").withStyle(ChatFormatting.AQUA));
            }
        }
    }

    @SubscribeEvent
    public static void onAddReloadListener(AddServerReloadListenersEvent event) {
        AlexsMobs.LOGGER.info("Adding datapack listener capsid_recipes");
        event.addListener(Identifier.fromNamespaceAndPath(AlexsMobs.MODID, "capsid_recipes"), AlexsMobs.PROXY.getCapsidRecipeManager());
    }
}
