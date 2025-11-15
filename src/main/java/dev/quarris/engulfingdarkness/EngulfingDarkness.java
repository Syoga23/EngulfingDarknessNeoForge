package dev.quarris.engulfingdarkness;

import dev.quarris.engulfingdarkness.configs.FlameConfigs;
import dev.quarris.engulfingdarkness.darkness.Darkness;
import dev.quarris.engulfingdarkness.packets.PacketHandler;
import dev.quarris.engulfingdarkness.registry.EffectSetup;
import dev.quarris.engulfingdarkness.registry.PotionSetup;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Supplier;

import static net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.ATTACHMENT_TYPES;

@Mod(ModRef.ID)
public class EngulfingDarkness {


    public EngulfingDarkness(IEventBus modBus ,ModContainer modContainer) {



        Pair<net.neoforged.fml.config.ModConfigs, ModConfigSpec> configSpec = new ModConfigSpec.Builder().configure(net.neoforged.fml.config.ModConfigs::new);
        ModRef.CONFIGS = configSpec.getKey();

        modContainer.registerConfig(ModConfig.Type.COMMON, ModConfigs.SPEC);

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