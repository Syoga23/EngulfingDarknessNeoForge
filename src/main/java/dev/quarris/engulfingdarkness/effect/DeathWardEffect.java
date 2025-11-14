package dev.quarris.engulfingdarkness.effect;

import dev.quarris.engulfingdarkness.ModRef;
import dev.quarris.engulfingdarkness.registry.EffectSetup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class DeathWardEffect extends MobEffect {

    public DeathWardEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x444444);
    }

    @EventBusSubscriber(modid = ModRef.ID)
    public static class Events {

        @SubscribeEvent
        public static void deathProtection(LivingDeathEvent event) {
            if (event.getEntity() instanceof Player player && player.hasEffect(EffectSetup.DEATH_WARD)) {
                if (!player.level().isClientSide()) {
                    ServerLevel level = ((ServerLevel) player.level());
                    player.setHealth(player.getMaxHealth() * 0.25f);
                    player.addEffect(new MobEffectInstance(EffectSetup.SOUL_VEIL, 1200));
                    player.removeEffect(EffectSetup.DEATH_WARD);
                    level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, player.getX(), player.getY(), player.getZ(), 20, player.getRandom().nextDouble() - 0.5, player.getRandom().nextDouble() * 2, player.getRandom().nextDouble() - 0.5, player.getRandom().nextGaussian());
                }
                player.level().playSound(null, player, SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1, 1);
                event.setCanceled(true);
            }
        }
    }
}