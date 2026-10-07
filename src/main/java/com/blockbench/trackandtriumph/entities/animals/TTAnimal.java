package com.blockbench.trackandtriumph.entities.animals;

import net.minecraft.world.entity.EntityType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Base class for every Track and Triumph animal. Animation names in the asset files follow the
 * pattern {@code animation.<species>.<name>} (idle, walk, run, plus graze / attack / gore / trumpet).
 */
public abstract class TTAnimal extends AgeableMob implements GeoEntity {
    private final AnimatableInstanceCache animatableCache = GeckoLibUtil.createInstanceCache(this);
    private final String species;

    protected TTAnimal(EntityType<? extends TTAnimal> type, Level level, String species) {
        super(type, level);
        this.species = species;
    }

    protected static AttributeSupplier.Builder baseAttributes(double health, double speed, double attackDamage) {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, health)
                .add(Attributes.MOVEMENT_SPEED, speed)
                .add(Attributes.ATTACK_DAMAGE, attackDamage);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return (AgeableMob) getType().create(level, EntitySpawnReason.BREEDING);
    }

    // Babies have their own geo/texture/animation set: animation.<species>_baby.<name>
    private String animationPrefix() {
        return "animation." + species + (isBaby() ? "_baby." : ".");
    }

    protected RawAnimation loop(String name) {
        return RawAnimation.begin().thenLoop(animationPrefix() + name);
    }

    protected RawAnimation once(String name) {
        return RawAnimation.begin().thenPlay(animationPrefix() + name);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("movement", state ->
                state.setAndContinue(state.isMoving() ? loop("walk") : loop("idle"))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animatableCache;
    }
}
