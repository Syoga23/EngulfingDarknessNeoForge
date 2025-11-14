package dev.quarris.engulfingdarkness.effect;

import dev.quarris.engulfingdarkness.ModRef;
import dev.quarris.engulfingdarkness.registry.EffectSetup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
//LivingTickEvent - replacement \/
//import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EasyTargetEffect extends MobEffect {

    public EasyTargetEffect() {
        super(MobEffectCategory.HARMFUL, 0x5a1c8c);
    }

    @EventBusSubscriber(modid = ModRef.ID)
    public static class Events {

        private static final TargetingConditions EASY_TARGET_TEST = TargetingConditions.forCombat()
                .ignoreLineOfSight()
                .selector(entity -> entity.hasEffect(EffectSetup.EASY_TARGET));

        @SubscribeEvent
        public static void focusEasyTarget(EntityTickEvent event) {  // Use LivingTickEvent directly
            if (!(event.getEntity() instanceof Mob mob) || mob.getRandom().nextFloat() < 0.1) {
                return;
            }

            LivingEntity target = mob.getTarget();
            if (target == null || target.hasEffect(EffectSetup.EASY_TARGET)) {  // Pass Holder directly
                return;
            }

            LivingEntity entity = mob.getTarget();
            Level level = entity.level();
            if (!(level instanceof ServerLevel serverLevel)) return;
            double followRange = entity.getAttributeValue(Attributes.FOLLOW_RANGE);
            List<Player> players = mob.level().players().stream()
                    .filter(e -> EASY_TARGET_TEST.range(followRange).test(mob, e))  // test(attacker, target) — без уровня
                    .collect(Collectors.toList());
            if (players.isEmpty()) return;
            players.sort(Comparator.comparingDouble(entity::distanceToSqr));
            mob.setTarget(players.getFirst());
        }

        @SubscribeEvent
        public static void increaseDamageTaken(LivingIncomingDamageEvent event) {
            if (event.getEntity().hasEffect(EffectSetup.EASY_TARGET)) {
                float originalAmount = event.getAmount();
                float newAmount = originalAmount * 1.5f;
                event.setAmount(newAmount);
            }
        }
    }
}
