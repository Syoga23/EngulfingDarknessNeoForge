package dev.quarris.engulfingdarkness.effect;

import dev.quarris.engulfingdarkness.ModRef;
import dev.quarris.engulfingdarkness.registry.EffectSetup;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class SoulVeilMobEffect extends MobEffect {
    public SoulVeilMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xffcd61);
    }

    @EventBusSubscriber(modid = ModRef.ID)
    public static class Events {

        @SubscribeEvent
        public static void interactionVeil(PlayerInteractEvent.RightClickBlock event) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            BlockState state = level.getBlockState(pos);
            Player player = event.getEntity();
            if (state.is(Blocks.SOUL_LANTERN)) {
                player.addEffect(new MobEffectInstance(EffectSetup.SOUL_VEIL, 30 * 20 / (ModRef.configs().nightmareMode ? 3 : 1)));
            }

            if (state.is(Blocks.SOUL_CAMPFIRE) && state.getValue(CampfireBlock.LIT)) {
                player.addEffect(new MobEffectInstance(EffectSetup.SOUL_VEIL, 60 * 20 / (ModRef.configs().nightmareMode ? 3 : 1)));
                CampfireBlock.dowse(player, level, pos, state);
                level.setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, false));
            }
        }

        @SubscribeEvent
        public static void interactionVeil(PlayerTickEvent event) {
            if (event.getEntity().getBlockStateOn().is(Blocks.SOUL_SAND)) {
                event.getEntity().addEffect(new MobEffectInstance(EffectSetup.SOUL_VEIL, 5 * 20 / (ModRef.configs().nightmareMode ? 3 : 1)));
            }
        }
    }
}
