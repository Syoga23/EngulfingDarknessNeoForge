package dev.quarris.engulfingdarkness.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.common.EffectCure;

import java.util.Collections;
import java.util.Set;

public class SentinelProtectionEffect extends MobEffect{
    public SentinelProtectionEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xfcba03);
    }

    public Set<EffectCure> getCures()  {
        return Collections.emptySet();
    }

}
