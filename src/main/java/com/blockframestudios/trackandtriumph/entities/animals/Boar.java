package com.blockframestudios.trackandtriumph.entities.animals;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

public class Boar extends TTAnimal {
    public Boar(EntityType<? extends Boar> type, Level level) {
        super(type, level, "boar");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return baseAttributes(12, 0.25, 3);
    }
}
