package com.blockframestudios.trackandtriumph.entities.animals;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

public class Bear extends TTAnimal {
    public Bear(EntityType<? extends Bear> type, Level level) {
        super(type, level, "bear");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return baseAttributes(30, 0.25, 6);
    }
}
