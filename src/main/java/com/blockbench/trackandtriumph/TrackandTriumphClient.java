package com.blockbench.trackandtriumph;

import java.util.function.Supplier;

import com.blockbench.trackandtriumph.entities.TTEntities;
import com.blockbench.trackandtriumph.entities.animals.TTAnimal;

import com.blockbench.trackandtriumph.client.TTAnimalModel;
import com.blockbench.trackandtriumph.client.TTKeyMappings;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = TrackandTriumph.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = TrackandTriumph.MODID, value = Dist.CLIENT)
public class TrackandTriumphClient {
    public TrackandTriumphClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        registerGeoRenderer(event, TTEntities.BEAR);
        registerGeoRenderer(event, TTEntities.BOAR);
        registerGeoRenderer(event, TTEntities.CAPE_BUFFALO);
        registerGeoRenderer(event, TTEntities.DEER);
        registerGeoRenderer(event, TTEntities.ELEPHANT);
        registerGeoRenderer(event, TTEntities.ELK);
        registerGeoRenderer(event, TTEntities.LION);
        registerGeoRenderer(event, TTEntities.MOOSE);
        registerGeoRenderer(event, TTEntities.RHINO);
        registerGeoRenderer(event, TTEntities.TIGER);
    }

    // Assets are looked up by registry name (plus _baby for babies): geo/entity/<id>.geo.json, animations/entity/<id>.animation.json, textures/entity/<id>.png
    private static <T extends TTAnimal> void registerGeoRenderer(EntityRenderersEvent.RegisterRenderers event, Supplier<EntityType<T>> type) {
        event.registerEntityRenderer(type.get(), context -> new GeoEntityRenderer<>(context, new TTAnimalModel<>(BuiltInRegistries.ENTITY_TYPE.getKey(type.get()))));
    }

    @SubscribeEvent
    static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        TTKeyMappings.register(event);
    }

    @SubscribeEvent
    static void onClientTick(ClientTickEvent.Post event) {
        TTKeyMappings.onClientTick(event);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        TrackandTriumph.LOGGER.info("HELLO FROM CLIENT SETUP");
        TrackandTriumph.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
