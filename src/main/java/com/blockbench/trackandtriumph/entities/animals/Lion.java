package com.blockbench.trackandtriumph.entities.animals;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

public class Lion extends TTAnimal {
    public Lion(EntityType<? extends Lion> type, Level level) {
        super(type, level, "lion");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return baseAttributes(25, 0.3, 6);
    }
}
