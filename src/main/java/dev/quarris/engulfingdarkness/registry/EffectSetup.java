package dev.quarris.engulfingdarkness.registry;

import dev.quarris.engulfingdarkness.ModRef;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import dev.quarris.engulfingdarkness.effect.*;
import net.neoforged.neoforge.registries.DeferredHolder;


public class EffectSetup {

    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, ModRef.ID);

    public static final DeferredHolder<MobEffect, SoulGuardMobEffect> SOUL_GUARD =
            REGISTRY.register("soul_guard", SoulGuardMobEffect::new);

    public static final DeferredHolder<MobEffect, SoulVeilMobEffect> SOUL_VEIL =
            REGISTRY.register("soul_veil", SoulVeilMobEffect::new);

    public static final DeferredHolder<MobEffect, BustedMobEffect> BUSTED =
            REGISTRY.register("busted", BustedMobEffect::new);

    public static final DeferredHolder<MobEffect, SentinelProtectionEffect> SENTINEL_PROTECTION =
            REGISTRY.register("sentinel_protection", SentinelProtectionEffect::new);

    public static final DeferredHolder<MobEffect, DeathWardEffect> DEATH_WARD =
            REGISTRY.register("death_ward", DeathWardEffect::new);

    public static final DeferredHolder<MobEffect, ResilienceEffect> RESILIENCE =
            REGISTRY.register("resilience", ResilienceEffect::new);

    public static final DeferredHolder<MobEffect, PiercerEffect> PIERCER =
            REGISTRY.register("piercer", PiercerEffect::new);

    public static final DeferredHolder<MobEffect, EasyTargetEffect> EASY_TARGET =
            REGISTRY.register("easy_target", EasyTargetEffect::new);

    public static final DeferredHolder<MobEffect, CasterMobEffect> CASTER =
            REGISTRY.register("caster", CasterMobEffect::new);

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}

