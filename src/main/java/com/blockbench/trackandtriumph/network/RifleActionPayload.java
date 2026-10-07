package com.blockbench.trackandtriumph.network;

import com.blockbench.trackandtriumph.TrackandTriumph;
import com.blockbench.trackandtriumph.items.weapons.RifleActions;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Client -> server: the player pressed one of the rifle hotkeys. */
public record RifleActionPayload(Action action) implements CustomPacketPayload {
    public enum Action {
        RELOAD_RIFLE,
        LOAD_MAGAZINE
    }

    public static final CustomPacketPayload.Type<RifleActionPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(TrackandTriumph.MODID, "rifle_action"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RifleActionPayload> STREAM_CODEC =
            NeoForgeStreamCodecs.<RegistryFriendlyByteBuf, Action>enumCodec(Action.class).map(RifleActionPayload::new, RifleActionPayload::action);

    @Override
    public CustomPacketPayload.Type<RifleActionPayload> type() {
        return TYPE;
    }

    public static void handle(RifleActionPayload payload, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player) {
            switch (payload.action()) {
                case RELOAD_RIFLE -> RifleActions.reloadRifle(player);
                case LOAD_MAGAZINE -> RifleActions.loadMagazine(player);
            }
        }
    }
}
