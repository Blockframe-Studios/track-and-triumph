package com.blockbench.trackandtriumph.entities.animals;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

public class CapeBuffalo extends TTAnimal {
    public CapeBuffalo(EntityType<? extends CapeBuffalo> type, Level level) {
        super(type, level, "cape_buffalo");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return baseAttributes(40, 0.22, 7);
    }
}
