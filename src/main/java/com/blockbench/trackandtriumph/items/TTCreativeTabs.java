package com.blockbench.trackandtriumph.items;

import com.blockbench.trackandtriumph.TrackandTriumph;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TTCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TrackandTriumph.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TRACK_AND_TRIUMPH = CREATIVE_MODE_TABS.register("track_and_triumph", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.trackandtriumph"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> TTItems.HUNTING_RIFLE.get().getDefaultInstance())
            .displayItems((parameters, output) -> TTItems.ITEMS.getEntries().forEach(item -> output.accept(item.get())))
            .build());

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
