package com.blockframestudios.trackandtriumph.client;

import com.blockframestudios.trackandtriumph.entities.animals.TTAnimal;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

/**
 * Uses {@code <id>} assets for adults and {@code <id>_baby} assets for babies.
 */
public class TTAnimalModel<T extends TTAnimal> extends GeoModel<T> {
    private final DefaultedEntityGeoModel<T> adult;
    private final DefaultedEntityGeoModel<T> baby;

    public TTAnimalModel(Identifier id) {
        this.adult = new DefaultedEntityGeoModel<>(id);
        this.baby = new DefaultedEntityGeoModel<>(id.withSuffix("_baby"));
    }

    private DefaultedEntityGeoModel<T> pick(GeoRenderState state) {
        return state instanceof LivingEntityRenderState living && living.isBaby ? baby : adult;
    }

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return pick(renderState).getModelResource(renderState);
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return pick(renderState).getTextureResource(renderState);
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        return (animatable.isBaby() ? baby : adult).getAnimationResource(animatable);
    }
}
