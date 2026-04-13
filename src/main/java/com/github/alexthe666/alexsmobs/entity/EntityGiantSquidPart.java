package com.github.alexthe666.alexsmobs.entity;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import net.minecraft.network.syncher.SynchedEntityData;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.network.MessageHurtMultipart;
import com.github.alexthe666.alexsmobs.network.MessageInteractMultipart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.entity.PartEntity;

import javax.annotation.Nullable;

public class EntityGiantSquidPart extends PartEntity<EntityGiantSquid> implements IHurtableMultipart {
    
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        // No synched data for this part entity
    }

    private final EntityDimensions size;
    public float scale = 1;
    private boolean collisionOnly = false;

    public EntityGiantSquidPart(EntityGiantSquid parent, float sizeX, float sizeY) {
        super(parent);
        this.size = EntityDimensions.scalable(sizeX, sizeY);
        this.refreshDimensions();
    }

    public EntityGiantSquidPart(EntityGiantSquid parent, float sizeX, float sizeY, boolean collisionOnly) {
        this(parent, sizeX, sizeY);
        this.collisionOnly = collisionOnly;
    }

    public boolean fireImmune() {
        return true;
    }

    protected void collideWithNearbyEntities() {

    }

    public InteractionResult interact(Player player, InteractionHand hand) {
        if(this.level().isClientSide() && this.getParent() != null){
            AlexsMobs.sendMSGToServer(new MessageInteractMultipart(this.getParent().getId(), hand == InteractionHand.OFF_HAND));
        }
        return this.getParent() == null ? InteractionResult.PASS : this.getParent().mobInteract(player, hand);
    }

    public boolean canBeCollidedWith() {
        return !collisionOnly;
    }

    protected void collideWithEntity(Entity entityIn) {
        if(!collisionOnly){
            entityIn.push(this);
        }
    }

    public boolean isPickable() {
        return !collisionOnly;
    }

    @Nullable
    public ItemStack getPickResult() {
        Entity parent = this.getParent();
        return parent != null ? parent.getPickResult() : ItemStack.EMPTY;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
        EntityGiantSquid parent = this.getParent();
        if (this.level().isClientSide() && parent != null && !parent.isInvulnerableTo(serverLevel, source) && !collisionOnly) {
            AlexsMobs.sendMSGToServer(new MessageHurtMultipart(this.getId(), parent.getId(), amount, source.getMsgId()));
        }
        return !collisionOnly && parent != null && !parent.isInvulnerableTo(serverLevel, source) && parent.attackEntityPartFrom(this, source, amount);
    }

    public boolean is(Entity entityIn) {
        return this == entityIn || this.getParent() == entityIn;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        throw new UnsupportedOperationException();
    }

    // getDimensions is now final in 1.21, removed override

    public void tick(){
        super.tick();
    }

    @Override
    protected void readAdditionalSaveData(ValueInput compound) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput compound) {

    }

    @Override
    public void onAttackedFromServer(LivingEntity parent, float damage, DamageSource damageSource) {
        if (damageSource != null && parent.level() instanceof ServerLevel sl) {
            parent.hurtServer(sl, damageSource, damage);
        }
    }
}
