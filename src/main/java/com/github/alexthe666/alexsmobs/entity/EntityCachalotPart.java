package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.network.MessageInteractMultipart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.entity.PartEntity;

import javax.annotation.Nullable;
import java.util.List;

public class EntityCachalotPart extends PartEntity<EntityCachalotWhale> {

    private final EntityDimensions size;
    public float scale = 1;

    public EntityCachalotPart(EntityCachalotWhale parent, float sizeX, float sizeY) {
        super(parent);
        this.size = EntityDimensions.scalable(sizeX, sizeY);
        this.refreshDimensions();
    }

    public EntityCachalotPart(EntityCachalotWhale entityCachalotWhale, float sizeX, float sizeY, EntityDimensions size) {
        super(entityCachalotWhale);
        this.size = size;
    }

    protected void collideWithNearbyEntities() {
        final List<Entity> entities = this.level().getEntities(this, this.getBoundingBox().expandTowards(0.2D, 0.0D, 0.2D));
        Entity parent = this.getParent();
        if (parent != null) {
            entities.stream().filter(entity -> entity != parent && !(entity instanceof EntityCachalotPart && ((EntityCachalotPart) entity).getParent() == parent) && entity.isPushable()).forEach(entity -> entity.push(parent));
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 vec) {
        if(this.level().isClientSide() && this.getParent() != null){
            AlexsMobs.sendMSGToServer(new MessageInteractMultipart(this.getParent().getId(), hand == InteractionHand.OFF_HAND));
        }
        return this.getParent() == null ? InteractionResult.PASS : this.getParent().mobInteract(player, hand);
    }


    protected void collideWithEntity(Entity entityIn) {
        entityIn.push(this);
    }

    public boolean isPickable() {
        return true;
    }

    @Nullable
    public ItemStack getPickResult() {
        Entity parent = this.getParent();
        return parent != null ? parent.getPickResult() : ItemStack.EMPTY;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (this.getParent() != null && !this.getParent().isInvulnerableTo(level, source)) {
            return !this.isInvulnerableToBase(source) && this.getParent().attackEntityPartFrom(this, source, amount);
        }
        return false;
    }

    public boolean is(Entity entityIn) {
        return this == entityIn || this.getParent() == entityIn;
    }

    // getDimensions is now final in 1.21, removed override

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    public void tick(){
        super.tick();
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {

    }
}
