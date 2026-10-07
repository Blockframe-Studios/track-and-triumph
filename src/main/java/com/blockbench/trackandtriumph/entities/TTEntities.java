package com.blockbench.trackandtriumph.entities;

import com.blockbench.trackandtriumph.TrackandTriumph;
import com.blockbench.trackandtriumph.entities.animals.*;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TTEntities {
    public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(TrackandTriumph.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<Bear>> BEAR = ENTITY_TYPES.registerEntityType(
            "bear", Bear::new, MobCategory.CREATURE, builder -> builder.sized(1.4f, 1.5f));

    public static final DeferredHolder<EntityType<?>, EntityType<Boar>> BOAR = ENTITY_TYPES.registerEntityType(
            "boar", Boar::new, MobCategory.CREATURE, builder -> builder.sized(0.9f, 0.9f));

    public static final DeferredHolder<EntityType<?>, EntityType<CapeBuffalo>> CAPE_BUFFALO = ENTITY_TYPES.registerEntityType(
            "cape_buffalo", CapeBuffalo::new, MobCategory.CREATURE, builder -> builder.sized(1.6f, 1.7f));

    public static final DeferredHolder<EntityType<?>, EntityType<Deer>> DEER = ENTITY_TYPES.registerEntityType(
            "deer", Deer::new, MobCategory.CREATURE, builder -> builder.sized(0.9f, 1.5f));

    public static final DeferredHolder<EntityType<?>, EntityType<Elephant>> ELEPHANT = ENTITY_TYPES.registerEntityType(
            "elephant", Elephant::new, MobCategory.CREATURE, builder -> builder.sized(3.0f, 3.0f));

    public static final DeferredHolder<EntityType<?>, EntityType<Elk>> ELK = ENTITY_TYPES.registerEntityType(
            "elk", Elk::new, MobCategory.CREATURE, builder -> builder.sized(1.3f, 1.9f));

    public static final DeferredHolder<EntityType<?>, EntityType<Lion>> LION = ENTITY_TYPES.registerEntityType(
            "lion", Lion::new, MobCategory.CREATURE, builder -> builder.sized(1.2f, 1.3f));

    public static final DeferredHolder<EntityType<?>, EntityType<Moose>> MOOSE = ENTITY_TYPES.registerEntityType(
            "moose", Moose::new, MobCategory.CREATURE, builder -> builder.sized(1.5f, 2.1f));

    public static final DeferredHolder<EntityType<?>, EntityType<Rhino>> RHINO = ENTITY_TYPES.registerEntityType(
            "rhino", Rhino::new, MobCategory.CREATURE, builder -> builder.sized(1.8f, 1.9f));

    public static final DeferredHolder<EntityType<?>, EntityType<Tiger>> TIGER = ENTITY_TYPES.registerEntityType(
            "tiger", Tiger::new, MobCategory.CREATURE, builder -> builder.sized(1.2f, 1.3f));

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }
}
