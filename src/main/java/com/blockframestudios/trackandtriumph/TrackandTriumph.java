package com.blockframestudios.trackandtriumph;

import com.blockframestudios.trackandtriumph.entities.animals.*;
import org.slf4j.Logger;

import com.blockframestudios.trackandtriumph.entities.TTEntities;
import com.blockframestudios.trackandtriumph.entities.animals.*;
import com.blockframestudios.trackandtriumph.gametest.TTGameTests;
import com.blockframestudios.trackandtriumph.items.TTCreativeTabs;
import com.blockframestudios.trackandtriumph.items.TTDataComponents;
import com.blockframestudios.trackandtriumph.items.TTItems;
import com.blockframestudios.trackandtriumph.network.TTNetworking;
import com.blockframestudios.trackandtriumph.sounds.TTSounds;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(TrackandTriumph.MODID)
public class TrackandTriumph {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "trackandtriumph";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "trackandtriumph" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public TrackandTriumph(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);

        // Register track and triumph components
        TTDataComponents.register(modEventBus);
        TTSounds.register(modEventBus);
        TTItems.register(modEventBus);
        TTCreativeTabs.register(modEventBus);
        TTEntities.register(modEventBus);
        TTGameTests.register(modEventBus);
        modEventBus.addListener(this::registerEntityAttributes);
        modEventBus.addListener(TTNetworking::registerPayloads);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (TrackandTriumph) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
//        LOGGER.info("HELLO FROM COMMON SETUP");
//
//        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
//            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
//        }
//
//        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());
//
//        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    private void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(TTEntities.BEAR.get(), Bear.createAttributes().build());
        event.put(TTEntities.BOAR.get(), Boar.createAttributes().build());
        event.put(TTEntities.CAPE_BUFFALO.get(), CapeBuffalo.createAttributes().build());
        event.put(TTEntities.DEER.get(), Deer.createAttributes().build());
        event.put(TTEntities.ELEPHANT.get(), Elephant.createAttributes().build());
        event.put(TTEntities.ELK.get(), Elk.createAttributes().build());
        event.put(TTEntities.LION.get(), Lion.createAttributes().build());
        event.put(TTEntities.MOOSE.get(), Moose.createAttributes().build());
        event.put(TTEntities.RHINO.get(), Rhino.createAttributes().build());
        event.put(TTEntities.TIGER.get(), Tiger.createAttributes().build());
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
//        LOGGER.info("HELLO from server starting");
    }
}
