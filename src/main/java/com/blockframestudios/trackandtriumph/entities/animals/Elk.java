package com.blockframestudios.trackandtriumph.entities.animals;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

public class Elk extends TTAnimal {
    public Elk(EntityType<? extends Elk> type, Level level) {
        super(type, level, "elk");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return baseAttributes(20, 0.26, 0);
    }
}
