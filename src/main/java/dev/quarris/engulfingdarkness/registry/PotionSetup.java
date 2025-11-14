package dev.quarris.engulfingdarkness.registry;

import dev.quarris.engulfingdarkness.ModRef;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.IEventBus;

public class PotionSetup {

    public static final DeferredRegister<Potion> REGISTRY =
            DeferredRegister.create(Registries.POTION, ModRef.ID);

    public static final DeferredHolder<Potion, Potion> SOUL_VEIL = REGISTRY.register("soul_veil",
            () -> new Potion(new MobEffectInstance[]{
                    new MobEffectInstance(EffectSetup.SOUL_VEIL, 300 * 20)
            }));

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}