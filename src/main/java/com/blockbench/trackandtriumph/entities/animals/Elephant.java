package com.blockbench.trackandtriumph.entities.animals;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

public class Elephant extends TTAnimal {
    public Elephant(EntityType<? extends Elephant> type, Level level) {
        super(type, level, "elephant");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return baseAttributes(80, 0.2, 12);
    }
}
