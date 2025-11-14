package dev.quarris.engulfingdarkness.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class PlayerUtil {
    public static ItemStack getHolding(Player player, Predicate<ItemStack> condition) {
        ItemStack holding = player.getMainHandItem();
        if (!holding.isEmpty() && condition.test(holding)) {
            return holding;
        }

        holding = player.getOffhandItem();
        if (!holding.isEmpty() && condition.test(holding)) {
            return holding;
        }

        return ItemStack.EMPTY;
    }
}
