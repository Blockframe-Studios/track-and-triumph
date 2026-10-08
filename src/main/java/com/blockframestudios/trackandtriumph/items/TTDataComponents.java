package com.blockframestudios.trackandtriumph.items;

import com.blockframestudios.trackandtriumph.TrackandTriumph;
import com.blockframestudios.trackandtriumph.items.weapons.RifleMagazineItem;
import com.mojang.serialization.Codec;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TTDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, TrackandTriumph.MODID);

    /**
     * Rounds loaded in a rifle magazine. On a magazine a missing component means empty; on the rifle a
     * missing component means no magazine is inserted (the item definition switches models on this).
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ROUNDS =
            DATA_COMPONENTS.registerComponentType("rounds", builder -> builder
                    .persistent(Codec.intRange(0, RifleMagazineItem.CAPACITY))
                    .networkSynchronized(ByteBufCodecs.VAR_INT));

    public static void register(IEventBus modEventBus) {
        DATA_COMPONENTS.register(modEventBus);
    }
}
