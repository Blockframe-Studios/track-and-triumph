package com.blockbench.trackandtriumph.items.weapons;

import java.util.function.Predicate;

import com.blockbench.trackandtriumph.items.TTDataComponents;
import com.blockbench.trackandtriumph.items.TTItems;
import com.blockbench.trackandtriumph.sounds.TTSounds;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import org.jspecify.annotations.Nullable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Magazine-fed hunting rifle. The {@code trackandtriumph:rounds} component holds the rounds in the inserted
 * magazine; no component means no magazine is inserted.
 * <ul>
 *   <li>Right-click: fire one round.</li>
 *   <li>Sneak + right-click: eject the magazine.</li>
 *   <li>Inventory: click a magazine (on the cursor) onto the rifle to insert it; right-click the rifle with an empty cursor to eject.</li>
 * </ul>
 */
public class HuntingRifleItem extends Item {
    public static final int COOLDOWN_TICKS = 20;
    // Ticks after a shot at which the item definition switches to the bolt_back / second bolt_up frames
    // (cooldown thresholds 0.65 and 0.35 of COOLDOWN_TICKS); the bolt sounds are timed to match.
    private static final int BOLT_PULLBACK_TICK = 7;
    private static final int BOLT_CLOSE_TICK = 13;
    public static final double RANGE = 32.0;
    public static final float DAMAGE = 12.0F;
    // Effectively "until released"; the rifle only uses this to hold the aim pose.
    private static final int USE_DURATION_TICKS = 72000;

    public HuntingRifleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack rifle = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            return ejectMagazine(level, player, rifle) ? InteractionResult.CONSUME : InteractionResult.PASS;
        }

        // Holding right-click keeps the rifle "in use", which the item definition shows as the aim pose.
        // The shot happens on release (see releaseUsing); recoil and the bolt cycle then play from the item cooldown.
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        if (!(entity instanceof Player player) || (slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND)) {
            return;
        }
        float cooldown = player.getCooldowns().getCooldownPercent(stack, 0.0F);
        if (cooldown <= 0.0F) {
            return;
        }

        int elapsed = COOLDOWN_TICKS - Math.round(cooldown * COOLDOWN_TICKS);
        if (elapsed == BOLT_PULLBACK_TICK) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), TTSounds.RIFLE_BOLT_PULLBACK.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
        } else if (elapsed == BOLT_CLOSE_TICK) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), TTSounds.RIFLE_BOLT_CLOSE.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return USE_DURATION_TICKS;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) {
            return false;
        }

        Integer rounds = stack.get(TTDataComponents.ROUNDS);
        if (rounds == null || rounds <= 0) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), TTSounds.RIFLE_EMPTY.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            return false;
        }

        if (level instanceof ServerLevel serverLevel) {
            stack.set(TTDataComponents.ROUNDS, rounds - 1);
            fire(serverLevel, player);
        }
        player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
        return true;
    }

    private static void fire(ServerLevel level, Player player) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), TTSounds.RIFLE_SHOOT.value(), SoundSource.PLAYERS, 1.0F, 1.0F);

        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(player.getLookAngle().scale(RANGE));
        HitResult blockHit = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
        }

        Predicate<Entity> canHit = entity -> !entity.isSpectator() && entity.isPickable();
        AABB searchBox = new AABB(start, end).inflate(1.0);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(player, start, end, searchBox, canHit, start.distanceToSqr(end));
        if (entityHit != null) {
            entityHit.getEntity().hurtServer(level, level.damageSources().playerAttack(player), DAMAGE);
        }
    }

    private static boolean ejectMagazine(Level level, Player player, ItemStack rifle) {
        Integer rounds = rifle.get(TTDataComponents.ROUNDS);
        if (rounds == null) {
            return false;
        }
        if (!level.isClientSide()) {
            ItemStack magazine = new ItemStack(TTItems.RIFLE_MAGAZINE.get());
            magazine.set(TTDataComponents.ROUNDS, rounds);
            rifle.remove(TTDataComponents.ROUNDS);
            if (!player.getInventory().add(magazine)) {
                player.drop(magazine, false);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_START.value(), SoundSource.PLAYERS, 0.8F, 1.0F);
        }
        return true;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.PRIMARY && other.is(TTItems.RIFLE_MAGAZINE.get())) {
            if (stack.has(TTDataComponents.ROUNDS)) {
                player.playSound(SoundEvents.DISPENSER_FAIL, 0.6F, 1.0F);
            } else {
                stack.set(TTDataComponents.ROUNDS, RifleMagazineItem.getRounds(other));
                other.shrink(1);
                player.playSound(SoundEvents.CROSSBOW_LOADING_END.value(), 0.8F, 1.0F);
            }
            return true;
        }

        if (action == ClickAction.SECONDARY && other.isEmpty() && stack.has(TTDataComponents.ROUNDS)) {
            ItemStack magazine = new ItemStack(TTItems.RIFLE_MAGAZINE.get());
            magazine.set(TTDataComponents.ROUNDS, stack.getOrDefault(TTDataComponents.ROUNDS, 0));
            stack.remove(TTDataComponents.ROUNDS);
            access.set(magazine);
            player.playSound(SoundEvents.CROSSBOW_LOADING_START.value(), 0.8F, 1.0F);
            return true;
        }
        return false;
    }
}
