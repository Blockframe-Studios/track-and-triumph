package com.blockbench.trackandtriumph.entities.animals;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

public class Deer extends TTAnimal {
    public Deer(EntityType<? extends Deer> type, Level level) {
        super(type, level, "deer");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return baseAttributes(12, 0.28, 0);
    }
}
