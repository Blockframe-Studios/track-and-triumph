package com.blockbench.trackandtriumph.sounds;

import com.blockbench.trackandtriumph.TrackandTriumph;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Sound events; the files and event definitions live in assets/trackandtriumph/sounds.json. */
public class TTSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, TrackandTriumph.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> RIFLE_SHOOT = register("rifle.shoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> RIFLE_EMPTY = register("rifle.empty");
    public static final DeferredHolder<SoundEvent, SoundEvent> RIFLE_BOLT_PULLBACK = register("rifle.bolt_pullback");
    public static final DeferredHolder<SoundEvent, SoundEvent> RIFLE_BOLT_CLOSE = register("rifle.bolt_close");
    public static final DeferredHolder<SoundEvent, SoundEvent> RIFLE_RELOAD = register("rifle.reload");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(TrackandTriumph.MODID, name)));
    }

    public static void register(IEventBus modEventBus) {
        SOUND_EVENTS.register(modEventBus);
    }
}
