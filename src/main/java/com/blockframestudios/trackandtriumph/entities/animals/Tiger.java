package com.blockframestudios.trackandtriumph.entities.animals;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

public class Tiger extends TTAnimal {
    public Tiger(EntityType<? extends Tiger> type, Level level) {
        super(type, level, "tiger");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return baseAttributes(25, 0.3, 7);
    }
}
