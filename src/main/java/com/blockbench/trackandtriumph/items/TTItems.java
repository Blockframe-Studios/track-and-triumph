package com.blockbench.trackandtriumph.items;

import com.blockbench.trackandtriumph.TrackandTriumph;
import com.blockbench.trackandtriumph.entities.TTEntities;
import com.blockbench.trackandtriumph.items.weapons.HuntingRifleItem;
import com.blockbench.trackandtriumph.items.weapons.RifleMagazineItem;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TTItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TrackandTriumph.MODID);

    public static final DeferredItem<Item> BAIT_BAG = ITEMS.registerSimpleItem("bait_bag", p -> p.stacksTo(1));
    public static final DeferredItem<Item> BEAR_CLAW = ITEMS.registerSimpleItem("bear_claw");
    public static final DeferredItem<Item> BEAR_PELT = ITEMS.registerSimpleItem("bear_pelt");
    public static final DeferredItem<Item> BINOCULARS = ITEMS.registerSimpleItem("binoculars", p -> p.stacksTo(1));
    public static final DeferredItem<Item> BOAR_TUSK = ITEMS.registerSimpleItem("boar_tusk");
    public static final DeferredItem<Item> BUFFALO_HORNS = ITEMS.registerSimpleItem("buffalo_horns");
    public static final DeferredItem<Item> COOKED_BEAR_MEAT = ITEMS.registerSimpleItem("cooked_bear_meat", p -> p.food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).build()));
    public static final DeferredItem<Item> COOKED_BOAR_MEAT = ITEMS.registerSimpleItem("cooked_boar_meat", p -> p.food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).build()));
    public static final DeferredItem<Item> COOKED_VENISON = ITEMS.registerSimpleItem("cooked_venison", p -> p.food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.8f).build()));
    public static final DeferredItem<Item> DEER_ANTLERS = ITEMS.registerSimpleItem("deer_antlers");
    public static final DeferredItem<Item> DEER_HIDE = ITEMS.registerSimpleItem("deer_hide");
    public static final DeferredItem<Item> ELEPHANT_TUSK = ITEMS.registerSimpleItem("elephant_tusk");
    public static final DeferredItem<Item> ELK_ANTLERS = ITEMS.registerSimpleItem("elk_antlers");
    public static final DeferredItem<Item> GAME_CALL = ITEMS.registerSimpleItem("game_call", p -> p.stacksTo(1));
    public static final DeferredItem<Item> HUNTING_BOW = ITEMS.registerSimpleItem("hunting_bow", p -> p.stacksTo(1));
    public static final DeferredItem<Item> HUNTING_KNIFE = ITEMS.registerSimpleItem("hunting_knife", p -> p.stacksTo(1));
    public static final DeferredItem<Item> HUNTING_RIFLE = ITEMS.registerItem("hunting_rifle", HuntingRifleItem::new, p -> p.stacksTo(1));
    public static final DeferredItem<Item> RIFLE_MAGAZINE = ITEMS.registerItem("rifle_magazine", RifleMagazineItem::new, p -> p.stacksTo(1));
    public static final DeferredItem<Item> LION_PELT = ITEMS.registerSimpleItem("lion_pelt");
    public static final DeferredItem<Item> MOOSE_ANTLERS = ITEMS.registerSimpleItem("moose_antlers");
    public static final DeferredItem<Item> RAW_BEAR_MEAT = ITEMS.registerSimpleItem("raw_bear_meat", p -> p.food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).build()));
    public static final DeferredItem<Item> RAW_BOAR_MEAT = ITEMS.registerSimpleItem("raw_boar_meat", p -> p.food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).build()));
    public static final DeferredItem<Item> RAW_VENISON = ITEMS.registerSimpleItem("raw_venison", p -> p.food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).build()));
    public static final DeferredItem<Item> RHINO_HORN = ITEMS.registerSimpleItem("rhino_horn");
    public static final DeferredItem<Item> RIFLE_ROUND = ITEMS.registerSimpleItem("rifle_round");
    public static final DeferredItem<Item> TIGER_PELT = ITEMS.registerSimpleItem("tiger_pelt");
    public static final DeferredItem<Item> VENISON_JERKY = ITEMS.registerSimpleItem("venison_jerky", p -> p.food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6f).build()));

    public static final DeferredItem<Item> BEAR_SPAWN_EGG = ITEMS.registerItem("bear_spawn_egg", SpawnEggItem::new, p -> p.spawnEgg(TTEntities.BEAR.get()));
    public static final DeferredItem<Item> BOAR_SPAWN_EGG = ITEMS.registerItem("boar_spawn_egg", SpawnEggItem::new, p -> p.spawnEgg(TTEntities.BOAR.get()));
    public static final DeferredItem<Item> CAPE_BUFFALO_SPAWN_EGG = ITEMS.registerItem("cape_buffalo_spawn_egg", SpawnEggItem::new, p -> p.spawnEgg(TTEntities.CAPE_BUFFALO.get()));
    public static final DeferredItem<Item> DEER_SPAWN_EGG = ITEMS.registerItem("deer_spawn_egg", SpawnEggItem::new, p -> p.spawnEgg(TTEntities.DEER.get()));
    public static final DeferredItem<Item> ELEPHANT_SPAWN_EGG = ITEMS.registerItem("elephant_spawn_egg", SpawnEggItem::new, p -> p.spawnEgg(TTEntities.ELEPHANT.get()));
    public static final DeferredItem<Item> ELK_SPAWN_EGG = ITEMS.registerItem("elk_spawn_egg", SpawnEggItem::new, p -> p.spawnEgg(TTEntities.ELK.get()));
    public static final DeferredItem<Item> LION_SPAWN_EGG = ITEMS.registerItem("lion_spawn_egg", SpawnEggItem::new, p -> p.spawnEgg(TTEntities.LION.get()));
    public static final DeferredItem<Item> MOOSE_SPAWN_EGG = ITEMS.registerItem("moose_spawn_egg", SpawnEggItem::new, p -> p.spawnEgg(TTEntities.MOOSE.get()));
    public static final DeferredItem<Item> RHINO_SPAWN_EGG = ITEMS.registerItem("rhino_spawn_egg", SpawnEggItem::new, p -> p.spawnEgg(TTEntities.RHINO.get()));
    public static final DeferredItem<Item> TIGER_SPAWN_EGG = ITEMS.registerItem("tiger_spawn_egg", SpawnEggItem::new, p -> p.spawnEgg(TTEntities.TIGER.get()));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
