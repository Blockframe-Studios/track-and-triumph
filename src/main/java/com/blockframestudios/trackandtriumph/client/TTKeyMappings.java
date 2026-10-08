package com.blockframestudios.trackandtriumph.client;

import org.lwjgl.glfw.GLFW;

import com.blockframestudios.trackandtriumph.TrackandTriumph;
import com.blockframestudios.trackandtriumph.network.RifleActionPayload;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;

/** Configurable rifle hotkeys (Options > Controls > Key Binds > Track and Triumph: Rifle). */
public class TTKeyMappings {
    public static final KeyMapping.Category CATEGORY =
            new KeyMapping.Category(Identifier.fromNamespaceAndPath(TrackandTriumph.MODID, "rifle"));

    public static final KeyMapping RELOAD_RIFLE = new KeyMapping("key.trackandtriumph.reload_rifle",
            KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, CATEGORY);

    public static final KeyMapping LOAD_MAGAZINE = new KeyMapping("key.trackandtriumph.load_magazine",
            KeyConflictContext.IN_GAME, KeyModifier.SHIFT, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, CATEGORY);

    public static void register(RegisterKeyMappingsEvent event) {
        event.registerCategory(CATEGORY);
        event.register(RELOAD_RIFLE);
        event.register(LOAD_MAGAZINE);
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        // Drain both so a Shift+R press can't also queue a plain R press; load wins if both fired.
        boolean load = false;
        boolean reload = false;
        while (LOAD_MAGAZINE.consumeClick()) {
            load = true;
        }
        while (RELOAD_RIFLE.consumeClick()) {
            reload = true;
        }

        if (load) {
            ClientPacketDistributor.sendToServer(new RifleActionPayload(RifleActionPayload.Action.LOAD_MAGAZINE));
        } else if (reload) {
            ClientPacketDistributor.sendToServer(new RifleActionPayload(RifleActionPayload.Action.RELOAD_RIFLE));
        }
    }
}
