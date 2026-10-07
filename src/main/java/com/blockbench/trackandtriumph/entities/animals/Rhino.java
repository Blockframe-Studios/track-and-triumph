package com.blockbench.trackandtriumph.entities.animals;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

public class Rhino extends TTAnimal {
    public Rhino(EntityType<? extends Rhino> type, Level level) {
        super(type, level, "rhino");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return baseAttributes(50, 0.22, 10);
    }
}
