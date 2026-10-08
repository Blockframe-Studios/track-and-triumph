package com.blockframestudios.trackandtriumph.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class TTNetworking {
    private static final String PROTOCOL_VERSION = "1";

    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToServer(RifleActionPayload.TYPE, RifleActionPayload.STREAM_CODEC, RifleActionPayload::handle);
    }
}
