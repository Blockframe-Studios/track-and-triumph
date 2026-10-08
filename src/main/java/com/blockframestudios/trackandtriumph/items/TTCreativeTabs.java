package com.blockframestudios.trackandtriumph.items;

import com.blockframestudios.trackandtriumph.TrackandTriumph;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
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
        modEventBus.addListener(TTCreativeTabs::addSpawnEggs);
    }

    private static void addSpawnEggs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            TTItems.ITEMS.getEntries().stream()
                    .map(DeferredHolder::get)
                    .filter(item -> item instanceof SpawnEggItem)
                    .forEach(event::accept);
        }
    }
}
