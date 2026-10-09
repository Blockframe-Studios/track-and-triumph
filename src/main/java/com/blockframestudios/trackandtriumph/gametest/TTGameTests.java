package com.blockframestudios.trackandtriumph.gametest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.blockframestudios.trackandtriumph.TrackandTriumph;
import com.blockframestudios.trackandtriumph.entities.TTEntities;
import com.blockframestudios.trackandtriumph.entities.animals.TTAnimal;
import com.blockframestudios.trackandtriumph.items.TTDataComponents;
import com.blockframestudios.trackandtriumph.items.TTItems;
import com.blockframestudios.trackandtriumph.items.weapons.RifleActions;
import com.blockframestudios.trackandtriumph.items.weapons.RifleMagazineItem;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Server-side game tests. Run them headless with {@code ./gradlew runGameTestServer}, or in-game with {@code /test runall}.
 * <p>
 * To add a test, call {@link #test(String, Consumer)} from the static block. The framework registers the test function
 * and a matching test instance, and every test runs on an empty arena. A test passes when it calls
 * {@code helper.succeed()} and fails on any thrown assertion or when it runs past {@link #MAX_TICKS}.
 */
public final class TTGameTests {
    private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS =
            DeferredRegister.create(Registries.TEST_FUNCTION, TrackandTriumph.MODID);
    private static final Identifier ENVIRONMENT = id("default");
    private static final Identifier EMPTY_STRUCTURE = Identifier.withDefaultNamespace("empty");
    private static final int MAX_TICKS = 100;
    private static final List<String> TEST_NAMES = new ArrayList<>();

    static {
        test("animals_spawn_and_tick", TTGameTests::animalsSpawnAndTick);
        test("animals_baby_variant", TTGameTests::animalsBabyVariant);
        test("loot_adults_drop_guaranteed_items", TTGameTests::lootAdultsDropGuaranteedItems);
        test("loot_babies_drop_nothing", TTGameTests::lootBabiesDropNothing);
        test("recipes_all_registered", TTGameTests::recipesAllRegistered);
        test("recipe_rifle_round", TTGameTests::recipeRifleRound);
        test("recipe_rifle_magazine", TTGameTests::recipeRifleMagazine);
        test("recipe_hunting_rifle", TTGameTests::recipeHuntingRifle);
        test("rifle_load_magazine", TTGameTests::rifleLoadMagazine);
        test("rifle_load_magazine_stops_at_capacity", TTGameTests::rifleLoadMagazineStopsAtCapacity);
        test("rifle_reload_swaps_magazine", TTGameTests::rifleReloadSwapsMagazine);
        test("rifle_reload_without_magazine", TTGameTests::rifleReloadWithoutMagazine);
        test("assets_cover_all_items", TTGameTests::assetsCoverAllItems);
    }

    private TTGameTests() {}

    // Resolved lazily: the entity types are not registered yet when this class initialises.
    private static Map<String, EntityType<? extends TTAnimal>> animals() {
        Map<String, EntityType<? extends TTAnimal>> animals = new LinkedHashMap<>();
        animals.put("bear", TTEntities.BEAR.get());
        animals.put("boar", TTEntities.BOAR.get());
        animals.put("cape_buffalo", TTEntities.CAPE_BUFFALO.get());
        animals.put("deer", TTEntities.DEER.get());
        animals.put("elephant", TTEntities.ELEPHANT.get());
        animals.put("elk", TTEntities.ELK.get());
        animals.put("lion", TTEntities.LION.get());
        animals.put("moose", TTEntities.MOOSE.get());
        animals.put("rhino", TTEntities.RHINO.get());
        animals.put("tiger", TTEntities.TIGER.get());
        return animals;
    }

    public static void register(IEventBus modEventBus) {
        FUNCTIONS.register(modEventBus);
        modEventBus.addListener(TTGameTests::registerTests);
    }

    private static void registerTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(ENVIRONMENT);
        for (String name : TEST_NAMES) {
            ResourceKey<Consumer<GameTestHelper>> function = ResourceKey.create(Registries.TEST_FUNCTION, id(name));
            event.registerTest(id(name), new FunctionGameTestInstance(function,
                    new TestData<>(environment, EMPTY_STRUCTURE, MAX_TICKS, 0, true)));
        }
    }

    private static void test(String name, Consumer<GameTestHelper> body) {
        TEST_NAMES.add(name);
        FUNCTIONS.register(name, () -> body);
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(TrackandTriumph.MODID, path);
    }

    // ---------------------------------------------------------------- entities

    private static void animalsSpawnAndTick(GameTestHelper helper) {
        floor(helper, 1, 1);
        // Spawn everything inside this test's own 1x1x1 arena: neighbouring tests clear the entities in their bounds when they finish.
        // Persistence is required so mock players from other tests (far away) cannot despawn the animals.
        List<TTAnimal> spawned = new ArrayList<>();
        for (EntityType<? extends TTAnimal> type : animals().values()) {
            TTAnimal animal = helper.spawnWithNoFreeWill(type, 0, 1, 0);
            animal.setPersistenceRequired();
            spawned.add(animal);
        }
        helper.runAfterDelay(5, () -> {
            for (TTAnimal animal : spawned) {
                helper.assertTrue(animal.isAlive(), animal.getType() + " should still be alive after 5 ticks (removed: "
                        + animal.getRemovalReason() + ", health " + animal.getHealth() + ", pos " + animal.position()
                        + ", last damage " + animal.getLastDamageSource() + ")");
                helper.assertTrue(animal.getMaxHealth() > 0, animal.getType() + " should have max health");
                helper.assertTrue(!animal.isBaby(), animal.getType() + " should spawn as an adult");
            }
            helper.succeed();
        });
    }

    private static void animalsBabyVariant(GameTestHelper helper) {
        floor(helper, 4, 4);
        for (EntityType<? extends TTAnimal> type : animals().values()) {
            TTAnimal animal = helper.spawnWithNoFreeWill(type, 1, 1, 1);
            animal.setBaby(true);
            helper.assertTrue(animal.isBaby(), type + " should be a baby after setBaby(true)");
            animal.setBaby(false);
            helper.assertTrue(!animal.isBaby(), type + " should be an adult after setBaby(false)");
            animal.discard();
        }
        helper.succeed();
    }

    /** The empty test structure has no floor, so lay one for tests that spawn mobs. */
    private static void floor(GameTestHelper helper, int sizeX, int sizeZ) {
        for (int x = -2; x < sizeX; x++) {
            for (int z = -2; z < sizeZ; z++) {
                helper.setBlock(x, 0, z, Blocks.STONE);
            }
        }
    }

    // ---------------------------------------------------------------- loot

    /** The drop each adult always produces (cooked meat when burning is not covered). The cape buffalo only has a 75% horn roll. */
    private static Map<String, Item> guaranteedDrops() {
        Map<String, Item> drops = new LinkedHashMap<>();
        drops.put("bear", TTItems.BEAR_PELT.get());
        drops.put("boar", TTItems.BOAR_TUSK.get());
        drops.put("deer", TTItems.DEER_HIDE.get());
        drops.put("elephant", TTItems.ELEPHANT_TUSK.get());
        drops.put("elk", TTItems.RAW_VENISON.get());
        drops.put("lion", TTItems.LION_PELT.get());
        drops.put("moose", TTItems.RAW_VENISON.get());
        drops.put("rhino", TTItems.RHINO_HORN.get());
        drops.put("tiger", TTItems.TIGER_PELT.get());
        return drops;
    }

    private static void lootAdultsDropGuaranteedItems(GameTestHelper helper) {
        floor(helper, 4, 4);
        Map<String, EntityType<? extends TTAnimal>> animals = animals();
        for (var entry : guaranteedDrops().entrySet()) {
            TTAnimal animal = helper.spawnWithNoFreeWill(animals.get(entry.getKey()), 1, 1, 1);
            animal.kill(helper.getLevel());
            int dropped = collectDrops(helper, entry.getValue());
            helper.assertTrue(dropped > 0, "adult " + entry.getKey() + " should always drop " + entry.getValue());
        }
        helper.succeed();
    }

    private static void lootBabiesDropNothing(GameTestHelper helper) {
        floor(helper, 4, 4);
        for (var entry : animals().entrySet()) {
            TTAnimal animal = helper.spawnWithNoFreeWill(entry.getValue(), 1, 1, 1);
            animal.setBaby(true);
            animal.kill(helper.getLevel());
            int dropped = collectDrops(helper, null);
            helper.assertTrue(dropped == 0, "baby " + entry.getKey() + " should drop nothing but dropped " + dropped + " items");
        }
        helper.succeed();
    }

    /**
     * Removes every item entity in the arena (with some slack for bouncing drops) and returns how many items were dropped,
     * counting only {@code item} when it is given.
     */
    private static int collectDrops(GameTestHelper helper, Item item) {
        AABB area = helper.getBounds().inflate(8);
        int total = 0;
        for (ItemEntity drop : helper.getLevel().getEntitiesOfClass(ItemEntity.class, area)) {
            if (item == null || drop.getItem().is(item)) {
                total += drop.getItem().getCount();
            }
            drop.discard();
        }
        return total;
    }

    // ---------------------------------------------------------------- recipes

    private static void recipesAllRegistered(GameTestHelper helper) {
        var manager = helper.getLevel().getServer().getRecipeManager();
        List<String> recipes = List.of("firing_pin", "hunting_rifle", "magazine_follower", "magazine_shell", "magazine_spring",
                "rifle_barrel", "rifle_bolt", "rifle_bullet", "rifle_grip", "rifle_magazine", "rifle_round", "rifle_scope",
                "rifle_stock", "rifle_trigger", "shell_casing");
        for (String name : recipes) {
            ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, id(name));
            helper.assertTrue(manager.byKey(key).isPresent(), "recipe " + key.identifier() + " should be loaded");
        }
        helper.succeed();
    }

    private static void recipeRifleRound(GameTestHelper helper) {
        assertCrafts(helper, 1, 3, List.of(stack(TTItems.RIFLE_BULLET.get()), stack(net.minecraft.world.item.Items.GUNPOWDER),
                stack(TTItems.SHELL_CASING.get())), TTItems.RIFLE_ROUND.get());
        helper.succeed();
    }

    private static void recipeRifleMagazine(GameTestHelper helper) {
        assertCrafts(helper, 1, 3, List.of(stack(TTItems.MAGAZINE_FOLLOWER.get()), stack(TTItems.MAGAZINE_SPRING.get()),
                stack(TTItems.MAGAZINE_SHELL.get())), TTItems.RIFLE_MAGAZINE.get());
        helper.succeed();
    }

    private static void recipeHuntingRifle(GameTestHelper helper) {
        ItemStack e = ItemStack.EMPTY;
        assertCrafts(helper, 3, 3, List.of(
                e, stack(TTItems.RIFLE_SCOPE.get()), e,
                stack(TTItems.FIRING_PIN.get()), stack(TTItems.RIFLE_BOLT.get()), stack(TTItems.RIFLE_BARREL.get()),
                stack(TTItems.RIFLE_STOCK.get()), stack(TTItems.RIFLE_TRIGGER.get()), stack(TTItems.RIFLE_GRIP.get())),
                TTItems.HUNTING_RIFLE.get());
        helper.succeed();
    }

    private static void assertCrafts(GameTestHelper helper, int width, int height, List<ItemStack> grid, Item expected) {
        CraftingInput input = CraftingInput.of(width, height, grid);
        var recipe = helper.getLevel().getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel());
        helper.assertTrue(recipe.isPresent(), "no crafting recipe matched the grid for " + id(BuiltInRegistries.ITEM.getKey(expected).getPath()));
        ItemStack result = recipe.get().value().assemble(input, helper.getLevel().registryAccess());
        helper.assertTrue(result.is(expected), "recipe produced " + result + " instead of " + expected);
    }

    private static ItemStack stack(Item item) {
        return new ItemStack(item);
    }

    // ---------------------------------------------------------------- rifle

    private static void rifleLoadMagazine(GameTestHelper helper) {
        withPlayer(helper, player -> {
            ItemStack magazine = new ItemStack(TTItems.RIFLE_MAGAZINE.get());
            player.setItemInHand(InteractionHand.MAIN_HAND, magazine);
            player.getInventory().add(new ItemStack(TTItems.RIFLE_ROUND.get(), 3));

            RifleActions.loadMagazine(player);

            helper.assertTrue(RifleMagazineItem.getRounds(magazine) == 3, "magazine should hold 3 rounds, had " + RifleMagazineItem.getRounds(magazine));
            helper.assertTrue(countItems(player, TTItems.RIFLE_ROUND.get()) == 0, "all rounds should have been consumed");
        });
    }

    private static void rifleLoadMagazineStopsAtCapacity(GameTestHelper helper) {
        withPlayer(helper, player -> {
            ItemStack magazine = new ItemStack(TTItems.RIFLE_MAGAZINE.get());
            player.setItemInHand(InteractionHand.MAIN_HAND, magazine);
            player.getInventory().add(new ItemStack(TTItems.RIFLE_ROUND.get(), 7));

            RifleActions.loadMagazine(player);

            helper.assertTrue(RifleMagazineItem.getRounds(magazine) == RifleMagazineItem.CAPACITY,
                    "magazine should be full, had " + RifleMagazineItem.getRounds(magazine));
            int left = countItems(player, TTItems.RIFLE_ROUND.get());
            helper.assertTrue(left == 7 - RifleMagazineItem.CAPACITY, "expected " + (7 - RifleMagazineItem.CAPACITY) + " rounds left, had " + left);
        });
    }

    private static void rifleReloadSwapsMagazine(GameTestHelper helper) {
        withPlayer(helper, player -> {
            ItemStack rifle = new ItemStack(TTItems.HUNTING_RIFLE.get());
            rifle.set(TTDataComponents.ROUNDS, 1);
            player.setItemInHand(InteractionHand.MAIN_HAND, rifle);
            ItemStack spare = new ItemStack(TTItems.RIFLE_MAGAZINE.get());
            spare.set(TTDataComponents.ROUNDS, 4);
            player.getInventory().add(spare);

            RifleActions.reloadRifle(player);

            helper.assertTrue(rifle.getOrDefault(TTDataComponents.ROUNDS, -1) == 4, "rifle should now hold the 4-round magazine");
            ItemStack returned = findItem(player, TTItems.RIFLE_MAGAZINE.get());
            helper.assertTrue(returned != null, "the old magazine should be returned to the inventory");
            helper.assertTrue(RifleMagazineItem.getRounds(returned) == 1, "returned magazine should hold 1 round, had " + RifleMagazineItem.getRounds(returned));
        });
    }

    private static void rifleReloadWithoutMagazine(GameTestHelper helper) {
        withPlayer(helper, player -> {
            ItemStack rifle = new ItemStack(TTItems.HUNTING_RIFLE.get());
            player.setItemInHand(InteractionHand.MAIN_HAND, rifle);

            RifleActions.reloadRifle(player);

            helper.assertTrue(!rifle.has(TTDataComponents.ROUNDS), "rifle should stay empty when there is no magazine to insert");
        });
    }

    /** Runs {@code body} with a mock player in the arena, then removes the player and passes the test. */
    @SuppressWarnings("removal") // GameTestHelper has no non-deprecated way to get a ServerPlayer
    private static void withPlayer(GameTestHelper helper, Consumer<ServerPlayer> body) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        try {
            body.accept(player);
        } finally {
            helper.getLevel().getServer().getPlayerList().remove(player);
        }
        helper.succeed();
    }

    private static int countItems(ServerPlayer player, Item item) {
        int total = 0;
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.is(item)) {
                total += stack.getCount();
            }
        }
        return total;
    }

    private static ItemStack findItem(ServerPlayer player, Item item) {
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.is(item)) {
                return stack;
            }
        }
        return null;
    }

    // ---------------------------------------------------------------- assets

    /** Every registered item needs an item definition, a lang entry and a texture (spawn eggs use the generated egg layers). */
    private static void assetsCoverAllItems(GameTestHelper helper) {
        JsonObject lang = readJson("/assets/" + TrackandTriumph.MODID + "/lang/en_us.json");
        List<String> problems = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            Identifier key = BuiltInRegistries.ITEM.getKey(item);
            if (!key.getNamespace().equals(TrackandTriumph.MODID)) {
                continue;
            }
            String path = key.getPath();
            if (!lang.has("item." + TrackandTriumph.MODID + "." + path)) {
                problems.add("missing lang key item." + TrackandTriumph.MODID + "." + path);
            }
            if (!resourceExists("/assets/" + TrackandTriumph.MODID + "/items/" + path + ".json")) {
                problems.add("missing item definition items/" + path + ".json");
            }
        }
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            Identifier key = BuiltInRegistries.ENTITY_TYPE.getKey(type);
            if (key.getNamespace().equals(TrackandTriumph.MODID) && !lang.has("entity." + TrackandTriumph.MODID + "." + key.getPath())) {
                problems.add("missing lang key entity." + TrackandTriumph.MODID + "." + key.getPath());
            }
        }
        helper.assertTrue(problems.isEmpty(), "asset problems:\n  " + String.join("\n  ", problems));
        helper.succeed();
    }

    private static boolean resourceExists(String path) {
        return TrackandTriumph.class.getResource(path) != null;
    }

    private static JsonObject readJson(String path) {
        try (var stream = TrackandTriumph.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalStateException("Missing resource " + path);
            }
            try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                return JsonParser.parseReader(reader).getAsJsonObject();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + path, e);
        }
    }
}
