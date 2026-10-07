package com.blockbench.trackandtriumph.items.weapons;

import com.blockbench.trackandtriumph.items.TTDataComponents;
import com.blockbench.trackandtriumph.items.TTItems;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * A 5-round rifle magazine. Rounds are stored in the {@code trackandtriumph:rounds} component.
 * Fill it by clicking rifle rounds (held on the cursor) onto it in an inventory.
 */
public class RifleMagazineItem extends Item {
    public static final int CAPACITY = 5;

    public RifleMagazineItem(Properties properties) {
        super(properties);
    }

    public static int getRounds(ItemStack magazine) {
        return magazine.getOrDefault(TTDataComponents.ROUNDS, 0);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action != ClickAction.PRIMARY || !other.is(TTItems.RIFLE_ROUND.get())) {
            return false;
        }

        int moved = Math.min(CAPACITY - getRounds(stack), other.getCount());
        if (moved > 0) {
            stack.set(TTDataComponents.ROUNDS, getRounds(stack) + moved);
            other.shrink(moved);
            player.playSound(SoundEvents.CROSSBOW_LOADING_END.value(), 0.8F, 1.0F);
        } else {
            player.playSound(SoundEvents.DISPENSER_FAIL, 0.6F, 1.0F);
        }
        return true;
    }
}
