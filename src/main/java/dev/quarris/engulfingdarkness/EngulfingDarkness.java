package dev.quarris.engulfingdarkness;

import dev.quarris.engulfingdarkness.configs.FlameConfigs;
import dev.quarris.engulfingdarkness.packets.PacketHandler;
import dev.quarris.engulfingdarkness.registry.EffectSetup;
import dev.quarris.engulfingdarkness.registry.PotionSetup;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForgeModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;

@Mod(ModRef.ID)
public class EngulfingDarkness {

    public EngulfingDarkness() {
        IEventBus modBus = NeoForgeModLoadingContext.get().getModEventBus();

        Pair<ModConfigs, ModConfigSpec> configSpec = new ModConfigSpec.Builder().configure(ModConfigs::new);
        ModRef.CONFIGS = configSpec.getKey();
        NeoForgeModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, configSpec.getValue());

        modBus.addListener(ModRef.configs()::onLoad);
        modBus.addListener(this::commonSetup);


        EffectSetup.init(modBus);
        PotionSetup.init(modBus);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        PacketHandler.init();
        FlameConfigs.load();
    }
}