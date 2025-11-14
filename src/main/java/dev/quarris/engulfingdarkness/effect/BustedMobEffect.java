package dev.quarris.engulfingdarkness.effect;

import dev.quarris.engulfingdarkness.ModRef;
import dev.quarris.engulfingdarkness.registry.EffectSetup;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.bus.api.SubscribeEvent;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static net.neoforged.neoforge.event.entity.living.MobEffectEvent.Applicable.Result.DO_NOT_APPLY;

public class BustedMobEffect extends MobEffect {

    public BustedMobEffect() {
        super(MobEffectCategory.HARMFUL, 0x552bbd);
    }

    public Set<EffectCure> getCures()  {
        return Collections.emptySet();
    }

    public static boolean shouldClearEffect(net.minecraft.core.Holder<MobEffect> effect) {
        MobEffect eff = effect.value();
        return eff.isBeneficial() && eff != EffectSetup.SOUL_GUARD.get() && eff != EffectSetup.SENTINEL_PROTECTION.get() && eff != EffectSetup.SOUL_VEIL.get();


    }

    @EventBusSubscriber(modid = ModRef.ID)
    public static class Events {

        @SubscribeEvent
        public static void bustedHealing(LivingHealEvent event) {
            // Prevent healing if busted is in effect
            if (event.getEntity().hasEffect(EffectSetup.BUSTED)) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void cleansePositiveEffects(MobEffectEvent.Added event) {
            // Remove all positive effects when busted is added.
            MobEffect addedEffect = event.getEffectInstance().getEffect().value();
            if (addedEffect == EffectSetup.BUSTED.get()) {
                Set<net.minecraft.core.Holder<MobEffect>> effects = event.getEntity().getActiveEffects().stream()
                        .map(MobEffectInstance::getEffect)
                        .filter(BustedMobEffect::shouldClearEffect)
                        .collect(Collectors.toSet());
                for (var effect : effects) {
                    event.getEntity().removeEffect(effect);
                }
            }
        }

        @SubscribeEvent
        public static void preventPositiveEffects(MobEffectEvent.Applicable event) {
            // Deny all positive effects being applied if busted is in effect.
            if (event.getEntity().hasEffect(EffectSetup.BUSTED) && shouldClearEffect(event.getEffectInstance().getEffect())) {
                event.setResult(DO_NOT_APPLY);
            }
        }

        @SubscribeEvent
        public static void preventRemovingNegativeEffects(MobEffectEvent.Remove event) {
            // Prevent cleansing of negative effects when busted is in effect.
            // Added busted as able to be removed to allow /effect command clearing.
            MobEffect removedEffect = event.getEffect().value();
            if (event.getEntity().hasEffect(EffectSetup.BUSTED) && !(removedEffect.isBeneficial() || removedEffect == EffectSetup.BUSTED.get())) {
                event.setCanceled(true);
            }
        }
    }
}