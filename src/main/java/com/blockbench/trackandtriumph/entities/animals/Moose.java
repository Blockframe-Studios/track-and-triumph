package com.blockbench.trackandtriumph.entities.animals;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

public class Moose extends TTAnimal {
    public Moose(EntityType<? extends Moose> type, Level level) {
        super(type, level, "moose");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return baseAttributes(35, 0.23, 0);
    }
}
