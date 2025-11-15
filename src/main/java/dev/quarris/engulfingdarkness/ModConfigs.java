package dev.quarris.engulfingdarkness;

import dev.quarris.engulfingdarkness.darkness.LightBringer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static dev.quarris.engulfingdarkness.ModRef.Tags.LIGHT_BRINGERS;

public class ModConfigs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue DARKNESS_LIGHT_LEVEL = BUILDER
            .comment("The light level that the darkness should trigger at.")
            .defineInRange("darkness_level", 4, 0, 15);

    // Updated: Using new fluent listConfig API for NeoForge 1.21.1+
    public static final ModConfigSpec.ConfigValue<List<String>> WHITELISTED_DIMS = BUILDER
            .listConfig("whitelisted_dimensions")
            .comment("Whitelisted dimensions")
            .defaultValue(defaultDims().get())  // defaultDims() provides Supplier<List<String>>
            .validator(o -> o instanceof String name && ResourceLocation.tryParse(name) != null)
            .allowEmpty()
            .build();

    public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("whitelisted_dimensions", List.of("minecraft:iron_ingot"), () -> "", Config::validateItemName);

    /*
    builder.push("dimension");
        this.rawWhitelistedDims = builder.defineListAllowEmpty(Collections.singletonList("whitelisted_dimensions"), ModConfigs::defaultDims, o -> o instanceof String name && ResourceLocation.tryParse(name) != null);
        this.rawTreatDimsAsBlacklisted = builder.define("treat_dims_as_blacklist", false);
        builder.pop();
        */

    public static final ModConfigSpec.BooleanValue TREAT_DIMS_AS_BLACKLISTED = BUILDER
            .comment("")
            .define("treat_dims_as_blacklist", false);

    public static final ModConfigSpec.DoubleValue DARKNESS_DAMAGE = BUILDER
            .comment("Percentage of max health to deal to the player every time darkness damage is dealt.")
            .defineInRange("darkness_damage", 20.0, 0.0, 100.0);

    public static final ModConfigSpec.DoubleValue DARKNESS_LEVEL_INCREMENT = BUILDER
            .comment("[DEPRECATED (use darkness_timer)] How fast does the darkness engulf when in low light level.")
            .defineInRange("darkness_increment", 0.01, 0.001, 1.0);

    public static final ModConfigSpec.DoubleValue DANGER_LEVEL_INCREMENT = BUILDER
            .comment("[DEPRECATED (use danger_timer)] Once in full darkness, how fast until the damage starts to trigger.")
            .defineInRange("danger_increment", 0.03, 0.001, 1.0);

    public static final ModConfigSpec.DoubleValue DANGER_TIMER = BUILDER
            .comment("Amount of time (in seconds) for the player to start taking damage after fully engulfed.")
            .defineInRange("danger_timer", 2.0, 1.0, 600.0);

    public static final ModConfigSpec.DoubleValue ENGULF_TIMER = BUILDER
            .comment("Amount of time (in seconds) for the darkness to fully engulf you.")
            .defineInRange("darkness_timer", 5.0, 1.0, 600.0);

    public static final ModConfigSpec.IntValue SPAWN_VEILED_TIMER = BUILDER
            .comment("Amount of time the Veiled effect will last (in seconds) when first joining the world or after each death. Set to 0 to never apply the effect.")
            .defineInRange("spawn_veiled_timer", 90, 0, 1000000);

    public static final ModConfigSpec.BooleanValue NIGHTMARE_MODE = BUILDER
            .comment("Nightmare Mode increases difficulty by ramping up the effects and damage dealt significantly")
            .define("nightmare_mode", false);

    public static final ModConfigSpec.BooleanValue DEBUG_MODE = BUILDER
            .comment("Debug mode displays or give info about the state of darkness at any given time.")
            .define("debug_mode", false);

    // Updated: Using new fluent listConfig API; adjusted default supplier and validator for light bringers
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List("minecraft:iron_ingot"), () -> "", Config::validateItemName);

    // Build the spec (static, as in the example)
    public static final ModConfigSpec SPEC = BUILDER.build();

    // Public static fields (populated in onLoad for cached access)
    public static int darknessLightLevel;
    public static Set<ResourceLocation> whitelistedDims;
    public static boolean treatDimsAsBlacklisted;
    public static double darknessDamage;
    public static double darknessLevelIncrement;
    public static double dangerLevelIncrement;
    public static double dangerTimer;
    public static double engulfTimer;
    public static int spawnVeiledTimer;
    public static boolean debugMode;
    public static boolean nightmareMode;

    // Static block to preload defaults (optional, for consistency with example)
    static {
        // Fixed: Call .get() on Suppliers
        whitelistedDims.addAll(defaultDims().get().stream().map(ResourceLocation::tryParse).filter(java.util.Objects::nonNull).toList());
        defaultLightBringers().get().forEach(s -> {
            String[] split = s.split(";");
            ResourceLocation itemId = ResourceLocation.tryParse(split[0]);
            if (itemId != null) {
                int flame = Integer.parseInt(split[1]);
                LightBringer.addLightBringer(BuiltInRegistries.ITEM.get(itemId), flame);  // Adjust LightBringer API as needed
            }
        });
    }

    // Assume @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD) is applied to this class
    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading event) {
        if (!"your_mod_id".equals(event.getConfig().getModId()) || event.getConfig().getSpec() != SPEC) {  // Replace "your_mod_id" with your actual mod ID, e.g., ModRef.ID
            return;
        }

        darknessLightLevel = DARKNESS_LIGHT_LEVEL.get();
        treatDimsAsBlacklisted = TREAT_DIMS_AS_BLACKLISTED.get();
        darknessDamage = DARKNESS_DAMAGE.get();
        darknessLevelIncrement = DARKNESS_LEVEL_INCREMENT.get();
        dangerLevelIncrement = DANGER_LEVEL_INCREMENT.get();
        dangerTimer = DANGER_TIMER.get();
        engulfTimer = ENGULF_TIMER.get();
        spawnVeiledTimer = SPAWN_VEILED_TIMER.get();
        debugMode = DEBUG_MODE.get();
        nightmareMode = NIGHTMARE_MODE.get();

        whitelistedDims.clear();
        whitelistedDims.addAll(WHITELISTED_DIMS.get().stream().map(ResourceLocation::tryParse).filter(java.util.Objects::nonNull).toList());

        // Assuming you need to reset Light Bringers; adjust as needed (e.g., clear a registry if it exists)
        LIGHT_BRINGERS.get().forEach(s -> {
            String[] split = s.split(";");
            ResourceLocation itemId = ResourceLocation.tryParse(split[0]);
            if (itemId != null) {
                int flame = Integer.parseInt(split[1]);
                LightBringer.addLightBringer(BuiltInRegistries.ITEM.get(itemId), flame);
            }
        });
    }

    public static boolean isAllowed(ResourceLocation dimension) {
        return treatDimsAsBlacklisted != whitelistedDims.contains(dimension);
    }

    private static Supplier<List<String>> defaultDims() {
        return () -> List.of(Level.OVERWORLD.location().toString());
    }

    private static Supplier<List<String>> defaultLightBringers() {
        return () -> List.of(
                BuiltInRegistries.ITEM.getKey(Items.TORCH) + ";640",
                BuiltInRegistries.ITEM.getKey(Items.SOUL_TORCH) + ";1280",
                BuiltInRegistries.ITEM.getKey(Items.GLOWSTONE_DUST) + ";6400"
        );
    }
}