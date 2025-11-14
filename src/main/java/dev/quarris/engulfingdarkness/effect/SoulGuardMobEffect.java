package dev.quarris.engulfingdarkness.effect;

import dev.quarris.engulfingdarkness.ModRef;
import dev.quarris.engulfingdarkness.registry.EffectSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;


public class SoulGuardMobEffect extends MobEffect {
    public SoulGuardMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xffcd61);
    }

    @EventBusSubscriber(modid = ModRef.ID)
    public static class Events {

        @SubscribeEvent
        public static void onJoinWorld(PlayerEvent.PlayerLoggedInEvent event) {
            event.getEntity().addEffect(new MobEffectInstance(EffectSetup.SOUL_GUARD, 1000000, 0, false, false, true));
        }

        @SubscribeEvent
        public static void onUse(PlayerInteractEvent event) {
            event.getEntity().removeEffect(EffectSetup.SOUL_GUARD);
        }

        @SubscribeEvent
        public static void noTarget(LivingChangeTargetEvent event) {
            if (event.getOriginalAboutToBeSetTarget() != null && event.getOriginalAboutToBeSetTarget().hasEffect(EffectSetup.SOUL_GUARD)) {
                event.setCanceled(true);
            }
        }
    }

    @EventBusSubscriber(value = Dist.CLIENT, modid = ModRef.ID)
    public static class Client {
        @SubscribeEvent
        public static void onMoved(MovementInputUpdateEvent event) {
            Input input = event.getInput();
            boolean hasMoved = input.getMoveVector().length() > 0.01;
            boolean hasJumped = input.jumping; //check out jumping later seems fine to me
            if (hasMoved || hasJumped) {
                PacketHandler.sendTo(new PlayerMovedMessage(), Minecraft.getInstance().player);
            }
        }
    }
}
