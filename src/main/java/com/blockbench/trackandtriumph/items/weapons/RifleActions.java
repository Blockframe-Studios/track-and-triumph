package com.blockbench.trackandtriumph.items.weapons;

import java.util.ArrayList;
import java.util.List;

import com.blockbench.trackandtriumph.items.TTDataComponents;
import com.blockbench.trackandtriumph.items.TTItems;
import com.blockbench.trackandtriumph.sounds.TTSounds;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

/**
 * Server-side handling of the rifle hotkeys (see {@code RifleActionPayload}).
 */
public final class RifleActions {
    private RifleActions() {}

    /** Swap the fullest loaded magazine in the inventory into the held rifle (the old magazine takes its slot). */
    public static void reloadRifle(ServerPlayer player) {
        ItemStack rifle = heldRifle(player);
        if (rifle == null) {
            message(player, "no_rifle");
            return;
        }

        Inventory inventory = player.getInventory();
        int currentRounds = rifle.getOrDefault(TTDataComponents.ROUNDS, -1);

        int bestSlot = -1;
        int bestRounds = Math.max(currentRounds, 0);
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.is(TTItems.RIFLE_MAGAZINE.get()) && RifleMagazineItem.getRounds(stack) > bestRounds) {
                bestSlot = slot;
                bestRounds = RifleMagazineItem.getRounds(stack);
            }
        }
        if (bestSlot < 0) {
            message(player, currentRounds >= 0 ? "no_better_magazine" : "no_magazine");
            return;
        }

        ItemStack replacement = ItemStack.EMPTY;
        if (currentRounds >= 0) {
            replacement = new ItemStack(TTItems.RIFLE_MAGAZINE.get());
            replacement.set(TTDataComponents.ROUNDS, currentRounds);
        }
        inventory.setItem(bestSlot, replacement);
        rifle.set(TTDataComponents.ROUNDS, bestRounds);
        sound(player, true);
    }

    /**
     * Top up a magazine from rifle rounds in the inventory. Prefers the magazine inserted in a held rifle,
     * then magazines in hand, then the fullest not-yet-full magazine in the inventory.
     */
    public static void loadMagazine(ServerPlayer player) {
        ItemStack target = findMagazineToFill(player);
        if (target == null) {
            message(player, "no_magazine_to_fill");
            return;
        }

        Inventory inventory = player.getInventory();
        int rounds = RifleMagazineItem.getRounds(target);
        int space = RifleMagazineItem.CAPACITY - rounds;
        int loaded = 0;
        for (int slot = 0; slot < inventory.getContainerSize() && loaded < space; slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.is(TTItems.RIFLE_ROUND.get())) {
                int taken = Math.min(space - loaded, stack.getCount());
                stack.shrink(taken);
                loaded += taken;
            }
        }
        if (loaded == 0) {
            message(player, "no_rounds");
            return;
        }
        target.set(TTDataComponents.ROUNDS, rounds + loaded);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), TTSounds.RIFLE_RELOAD.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private static ItemStack findMagazineToFill(ServerPlayer player) {
        List<ItemStack> candidates = new ArrayList<>();

        for (ItemStack held : List.of(player.getMainHandItem(), player.getOffhandItem())) {
            if (held.is(TTItems.HUNTING_RIFLE.get()) && held.has(TTDataComponents.ROUNDS)) {
                candidates.add(held);
            }
        }
        for (ItemStack held : List.of(player.getMainHandItem(), player.getOffhandItem())) {
            if (held.is(TTItems.RIFLE_MAGAZINE.get())) {
                candidates.add(held);
            }
        }
        for (ItemStack stack : candidates) {
            if (RifleMagazineItem.getRounds(stack) < RifleMagazineItem.CAPACITY) {
                return stack;
            }
        }

        ItemStack best = null;
        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.is(TTItems.RIFLE_MAGAZINE.get()) && RifleMagazineItem.getRounds(stack) < RifleMagazineItem.CAPACITY
                    && (best == null || RifleMagazineItem.getRounds(stack) > RifleMagazineItem.getRounds(best))) {
                best = stack;
            }
        }
        return best;
    }

    private static ItemStack heldRifle(ServerPlayer player) {
        if (player.getMainHandItem().is(TTItems.HUNTING_RIFLE.get())) {
            return player.getMainHandItem();
        }
        if (player.getOffhandItem().is(TTItems.HUNTING_RIFLE.get())) {
            return player.getOffhandItem();
        }
        return null;
    }

    private static void message(ServerPlayer player, String key) {
        player.displayClientMessage(Component.translatable("message.trackandtriumph.rifle." + key), true);
    }

    private static void sound(ServerPlayer player, boolean success) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                success ? SoundEvents.CROSSBOW_LOADING_END.value() : SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 0.8F, 1.0F);
    }
}
